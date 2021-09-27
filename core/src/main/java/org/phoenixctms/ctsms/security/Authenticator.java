package org.phoenixctms.ctsms.security;

import java.security.Key;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.apache.commons.codec.binary.Base64;
import org.phoenixctms.ctsms.UserContext;
import org.phoenixctms.ctsms.domain.JournalEntryDao;
import org.phoenixctms.ctsms.domain.Password;
import org.phoenixctms.ctsms.domain.PasswordDao;
import org.phoenixctms.ctsms.domain.User;
import org.phoenixctms.ctsms.domain.UserDao;
import org.phoenixctms.ctsms.enumeration.AuthenticationType;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.util.AuthenticationExceptionCodes;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.DefaultMessages;
import org.phoenixctms.ctsms.util.DefaultSettings;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.MessageCodes;
import org.phoenixctms.ctsms.util.ServiceUtil;
import org.phoenixctms.ctsms.util.SettingCodes;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.util.SystemMessageCodes;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.LdapEntryVO;
import org.phoenixctms.ctsms.vo.PasswordInVO;
import org.phoenixctms.ctsms.vo.PasswordOutVO;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SigningKeyResolverAdapter;

public class Authenticator {

	private static String obfuscateWrongPassword(String password) {
		return CoreUtil.OBFUSCATED_STRING;
	}

	private PasswordDao passwordDao;
	private UserDao userDao;
	private JournalEntryDao journalEntryDao;
	private LdapService ldapService1;
	private LdapService ldapService2;
	private final static String JWT_PWD_HEADER_KEY = "pwd";

	public String issueJwt(AuthenticationVO auth, String realm, Long validityPeriodSecs) throws Exception {
		authenticate(auth, true, realm);
		JwtBuilder jwtBuilder = Jwts.builder();
		//prevent attacks that use signed claims as starting point:
		jwtBuilder.setId(CommonUtil.generateUUID());
		//user id to lookup when verifying:
		User user = CoreUtil.getUser();
		jwtBuilder.setSubject(Long.toString(user.getId()));
		if (validityPeriodSecs != null) {
			//expiry, if specified
			jwtBuilder.setExpiration(new Date(System.currentTimeMillis() + validityPeriodSecs * 1000l));
		} else {
			jwtBuilder.setIssuedAt(new Date(System.currentTimeMillis()));
		}
		//prevent using JWT across instances:
		jwtBuilder.setIssuer(Settings.getInstanceName());
		//encrypt the user password with the user's private key, and add it to payload:
		jwtBuilder.setHeaderParam(JWT_PWD_HEADER_KEY,
				new String(Base64.encodeBase64(CryptoUtil.encrypt(CoreUtil.getUserContext().getPrivateKey(), auth.getPassword().getBytes()))));
		//sign payload with a key derived from the user department's department key:
		jwtBuilder.signWith(CryptoUtil.createJwtKey(CoreUtil.getUserContext().getDepartmentKey()));
		return jwtBuilder.compact();
	}

	public String[] verifyJwt(String jwt) throws Exception {
		final String[] credentials = new String[2];
		try {
			JwtParserBuilder parserBuilder = Jwts.parserBuilder();
			parserBuilder.setSigningKeyResolver(new SigningKeyResolverAdapter() {

				@Override
				public Key resolveSigningKey(JwsHeader jwsHeader, Claims claims) {
					try {
						//check for valid user id:
						User user = CheckIDUtil.checkUserId(Long.parseLong(claims.getSubject()), userDao);
						credentials[0] = user.getName();
						//decrypt the user password using the user's public key:
						String plainPassword = new String(
								CryptoUtil.decrypt(CryptoUtil.getPublicKey(user.getKeyPair().getPublicKey()), Base64.decodeBase64((String) jwsHeader.get(JWT_PWD_HEADER_KEY))));
						credentials[1] = plainPassword;
						//get the jwt signing key from the user's department key:
						String plainDepartmentPassword = CryptoUtil.decryptDepartmentPassword(passwordDao.findLastPassword(user.getId()), plainPassword);
						SecretKey departmentKey = CryptoUtil.getDepartmentKey(CryptoUtil.decryptDepartmentKey(user.getDepartment(), plainDepartmentPassword));
						return CryptoUtil.createJwtKey(departmentKey);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			});
			//verify if jwt is authentic:
			parserBuilder.build().parseClaimsJws(jwt);
		} catch (Throwable t) {
			AuthenticationException e = L10nUtil.initAuthenticationException(AuthenticationExceptionCodes.INVALID_JWT, jwt);
			if (t instanceof RuntimeException) {
				e.initCause(t.getCause());
			} else {
				e.initCause(t);
			}
			throw e;
		}
		return credentials;
	}

	public Authenticator() {
	}

	public Password authenticate(AuthenticationVO auth, boolean logon) throws Exception {
		return authenticate(auth, logon, null);
	}

	public Password authenticate(AuthenticationVO auth, boolean logon, String realm) throws Exception {
		if (auth != null) {
			User user = null;
			try {
				user = (User) userDao.searchUniqueName(UserDao.TRANSFORM_NONE, auth.getUsername());
			} catch (Throwable t) {
				AuthenticationException e = L10nUtil.initAuthenticationException(AuthenticationExceptionCodes.UNKNOWN_USER, auth.getUsername());
				e.initCause(t);
				throw e;
			}
			UserContext userContext = CoreUtil.getUserContext();
			userContext.setUser(user, userDao.toUserInheritedVO(user));
			userContext.setHost(auth.getHost());
			userContext.setRealm(realm);
			if (user == null) {
				throw L10nUtil.initAuthenticationException(AuthenticationExceptionCodes.UNKNOWN_USER, auth.getUsername());
			}
			if (user.isDeferredDelete()) {
				throw L10nUtil.initAuthenticationException(AuthenticationExceptionCodes.USER_MARKED_FOR_DELETION, auth.getUsername());
			}
			if (userContext.getInheritedUser().getLocked()) {
				throw L10nUtil.initAuthenticationException(AuthenticationExceptionCodes.USER_LOCKED, auth.getUsername());
			}
			switch (user.getAuthMethod()) {
				case LOCAL:
					return authenticateLocal(auth, user, logon);
				case LDAP1:
				case LDAP2:
					return authenticateLdap(auth, user, logon);
				default:
					throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.UNSUPPORTED_AUTHENTICATION_TYPE, DefaultMessages.UNSUPPORTED_AUTHENTICATION_TYPE,
							new Object[] { user.getAuthMethod().toString() }));
			}
		} else {
			throw L10nUtil.initAuthenticationException(AuthenticationExceptionCodes.AUTHENTICATION_REQUIRED);
		}
	}

	private Password authenticateLdap(AuthenticationVO auth, User user, boolean logon) throws Exception {
		UserContext userContext = CoreUtil.getUserContext();
		Timestamp now = new Timestamp(System.currentTimeMillis());
		boolean ldapFailover = false;
		boolean remoteAuthenticated = false;
		if (logon && !ldapFailover) {
			try {
				// http://docs.spring.io/spring-ldap/docs/1.3.x/reference/html/user-authentication.html
				remoteAuthenticated = getLdapService(user.getAuthMethod()).authenticate(auth);
			} catch (Exception e) {
				ldapFailover = Settings.getBoolean(SettingCodes.LDAP_FAILOVER, Bundle.SETTINGS, DefaultSettings.LDAP_FAILOVER);
				if (!ldapFailover) {
					throw L10nUtil.initAuthenticationException(e.getMessage());
				}
			}
		}
		if (!logon || remoteAuthenticated || ldapFailover) {
			Password lastPassword = passwordDao.findLastPassword(user.getId());
			userContext.setLastPassword(lastPassword);
			if (lastPassword == null) {
				throw L10nUtil.initAuthenticationException(AuthenticationExceptionCodes.NO_LOCAL_PASSWORD_SET, auth.getUsername());
			}
			if (logon) {
				setLogonAttempt(lastPassword, auth, now);
			}
			if (ckeckLocalPassword(lastPassword, auth.getPassword())) {
				// fine!
				userContext.setPlainPassword(auth.getPassword());
			} else if (logon && !ldapFailover) {
				if (auth.getLocalPassword() != null && auth.getLocalPassword().length() > 0) {
					if (ckeckLocalPassword(lastPassword, auth.getLocalPassword())) {
						userContext.setPlainPassword(auth.getLocalPassword());
						PasswordInVO localPassword = new PasswordInVO();
						ServiceUtil.applyLogonLimitations(localPassword, lastPassword);
						lastPassword = ServiceUtil.createPassword(false, passwordDao.passwordInVOToEntity(localPassword), user, now, lastPassword, auth.getPassword(),
								userContext.getPlainDepartmentPassword(), passwordDao);
						userContext.setLastPassword(lastPassword);
						userContext.setPlainPassword(auth.getPassword());
						PasswordOutVO passwordVO = passwordDao.toPasswordOutVO(lastPassword);
						ServiceUtil.logSystemMessage(user, passwordVO.getUser(), now, CoreUtil.getUser(), SystemMessageCodes.LOCAL_PASSWORD_CREATED, passwordVO, null,
								journalEntryDao);
					} else {
						throw L10nUtil.initAuthenticationException(AuthenticationExceptionCodes.WRONG_LOCAL_PASSWORD, obfuscateWrongPassword(auth.getLocalPassword()));
					}
				} else {
					throw L10nUtil.initAuthenticationException(AuthenticationExceptionCodes.LOCAL_PASSWORD_REQUIRED);
				}
			} else {
				throw L10nUtil.initAuthenticationException(AuthenticationExceptionCodes.WRONG_LOCAL_PASSWORD, obfuscateWrongPassword(auth.getLocalPassword()));
			}
			if (logon) {
				setSuccessfullLogon(lastPassword, auth, now);
			}
			return lastPassword;
		} else {
			throw L10nUtil.initAuthenticationException(AuthenticationExceptionCodes.WRONG_REMOTE_PASSWORD, obfuscateWrongPassword(auth.getPassword()));
		}
	}

	private Password authenticateLocal(AuthenticationVO auth, User user, boolean logon) throws Exception {
		UserContext userContext = CoreUtil.getUserContext();
		Timestamp now = new Timestamp(System.currentTimeMillis());
		Password lastPassword = passwordDao.findLastPassword(user.getId());
		userContext.setLastPassword(lastPassword);
		if (lastPassword == null) {
			throw L10nUtil.initAuthenticationException(AuthenticationExceptionCodes.NO_PASSWORD_SET, auth.getUsername());
		}
		if (logon) {
			setLogonAttempt(lastPassword, auth, now);
		}
		if (lastPassword.isLimitLogons() && (lastPassword.getSuccessfulLogons() - (logon ? 0 : 1)) >= lastPassword.getMaxSuccessfulLogons()) {
			throw L10nUtil.initAuthenticationException(AuthenticationExceptionCodes.SUCCESSFUL_LOGON_LIMIT_EXCEEDED, lastPassword.getSuccessfulLogons(),
					lastPassword.getMaxSuccessfulLogons());
		}
		if (lastPassword.isLimitWrongPasswordAttempts()
				&& (lastPassword.getWrongPasswordAttemptsSinceLastSuccessfulLogon() - (logon ? 0 : 1)) >= lastPassword.getMaxWrongPasswordAttemptsSinceLastSuccessfulLogon()) {
			throw L10nUtil.initAuthenticationException(AuthenticationExceptionCodes.WRONG_PASSWORD_ATTEMPT_LIMIT_EXCEEDED,
					lastPassword.getWrongPasswordAttemptsSinceLastSuccessfulLogon(), lastPassword.getMaxWrongPasswordAttemptsSinceLastSuccessfulLogon());
		}
		if (lastPassword.isExpires()) {
			Date expiration = ServiceUtil.getLogonExpirationDate(lastPassword);
			if ((new Date()).compareTo(expiration) >= 0) {
				AuthenticationException exception = L10nUtil.initAuthenticationException(AuthenticationExceptionCodes.PASSWORD_EXPIRED);
				exception.setData(expiration);
				throw exception;
			}
		}
		boolean passwordValid = false;
		try {
			passwordValid = CryptoUtil.checkPassword(lastPassword, auth.getPassword());
		} catch (Exception e) {
			throw L10nUtil.initAuthenticationException(e.getMessage());
		}
		if (!passwordValid) {
			if (logon) {
				lastPassword.setWrongPasswordAttemptsSinceLastSuccessfulLogon(lastPassword.getWrongPasswordAttemptsSinceLastSuccessfulLogon() + 1);
			}
			throw L10nUtil.initAuthenticationException(AuthenticationExceptionCodes.WRONG_PASSWORD, obfuscateWrongPassword(auth.getPassword()));
		}
		userContext.setPlainPassword(auth.getPassword());
		if (logon) {
			setSuccessfullLogon(lastPassword, auth, now);
		}
		return lastPassword;
	}

	private boolean ckeckLocalPassword(Password lastPassword, String plainPassword) throws AuthenticationException {
		try {
			return CryptoUtil.checkPassword(lastPassword, plainPassword);
		} catch (Exception e) {
			throw L10nUtil.initAuthenticationException(e.getMessage());
		}
	}

	private LdapService getLdapService(AuthenticationType authMethod) {
		switch (authMethod) {
			case LDAP1:
				return ldapService1;
			case LDAP2:
				return ldapService2;
			default:
				throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.UNKNOWN_LDAP_SERVICE, DefaultMessages.UNKNOWN_LDAP_SERVICE,
						new Object[] { authMethod.toString() }));
		}
	}

	public List<LdapEntryVO> search(AuthenticationType authMethod, Integer limit, String... filterArgs) {
		return getLdapService(authMethod).search(limit, filterArgs);
	}

	public List<LdapEntryVO> searchAuth(AuthenticationType authMethod, String username) {
		return getLdapService(authMethod).searchAuth(username);
	}

	public void setJournalEntryDao(JournalEntryDao journalEntryDao) {
		this.journalEntryDao = journalEntryDao;
	}

	public void setLdapService1(LdapService ldapService1) {
		this.ldapService1 = ldapService1;
	}

	public void setLdapService2(LdapService ldapService2) {
		this.ldapService2 = ldapService2;
	}

	private void setLogonAttempt(Password lastPassword, AuthenticationVO auth, Timestamp now) {
		lastPassword.setLastLogonAttemptHost(auth.getHost());
		lastPassword.setLastLogonAttemptTimestamp(now);
	}

	public void setPasswordDao(PasswordDao passwordDao) {
		this.passwordDao = passwordDao;
	}

	private void setSuccessfullLogon(Password lastPassword, AuthenticationVO auth, Timestamp now) {
		lastPassword.setSuccessfulLogons(lastPassword.getSuccessfulLogons() + 1);
		lastPassword.setWrongPasswordAttemptsSinceLastSuccessfulLogon(0);
		lastPassword.setLastSuccessfulLogonHost(auth.getHost());
		lastPassword.setLastSuccessfulLogonTimestamp(now);
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
}
