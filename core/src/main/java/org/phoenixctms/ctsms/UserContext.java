package org.phoenixctms.ctsms;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Locale;
import java.util.TimeZone;

import javax.crypto.SecretKey;

import org.phoenixctms.ctsms.domain.Password;
import org.phoenixctms.ctsms.domain.User;
import org.phoenixctms.ctsms.security.CryptoUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.vo.UserInheritedVO;

public class UserContext extends VOCacheContext {

	private static final long serialVersionUID = 1L;
	private User user;
	private UserInheritedVO inheritedUser;
	private Password lastPassword;
	private String plainPassword;
	private String host;
	private String realm;
	private String methodName;
	private String jwtAudience;
	private Locale locale;
	private TimeZone timeZone;
	private String dateFormat;
	private String decimalSeparator;
	private String plainDepartmentPassword;
	private SecretKey departmentKey;
	private PrivateKey privateKey;
	private PublicKey publicKey;
	private boolean isLocaleSet;
	private boolean isTimeZoneSet;
	private boolean isDateFormatSet;
	private boolean isDecimalSeparatorSet;
	private boolean isPlainDepartmentPasswordSet;
	private boolean isDepartmentKeySet;
	private boolean isPrivateKeySet;
	private boolean isPublicKeySet;
	private Boolean isTrustedHost;
	private boolean trustedDevice;

	public UserContext() {
		super();
		reset();
	}

	public UserContext(User user, UserInheritedVO inheritedUser, Password lastPassword, String plainPassword, String host, String realm) {
		super();
		reset();
		this.user = user;
		this.inheritedUser = inheritedUser;
		this.lastPassword = lastPassword;
		this.plainPassword = plainPassword;
		this.host = host;
		this.realm = realm;
	}

	public String getDateFormat() {
		if (!isDateFormatSet && inheritedUser != null) {
			dateFormat = inheritedUser.getDateFormat();
			isDateFormatSet = true;
		}
		return dateFormat;
	}

	public String getDecimalSeparator() {
		if (!isDecimalSeparatorSet && inheritedUser != null) {
			decimalSeparator = inheritedUser.getDecimalSeparator();
			isDecimalSeparatorSet = true;
		}
		return decimalSeparator;
	}

	public SecretKey getDepartmentKey() throws Exception {
		if (!isDepartmentKeySet && user != null) {
			departmentKey = CryptoUtil.getDepartmentKey(CryptoUtil.decryptDepartmentKey(user.getDepartment(), getPlainDepartmentPassword()));
			isDepartmentKeySet = true;
		}
		return departmentKey;
	}

	public String getHost() {
		return host;
	}

	public Password getLastPassword() {
		return lastPassword;
	}

	public Locale getLocale() {
		if (!isLocaleSet && inheritedUser != null) {
			try {
				locale = CommonUtil.localeFromString(inheritedUser.getLocale());
			} catch (Exception e) {
				locale = null;
			}
			isLocaleSet = true;
		}
		return locale;
	}

	@Override
	public String getName() {
		return user == null ? null : user.getName();
	}

	public String getPlainDepartmentPassword() throws Exception {
		if (!isPlainDepartmentPasswordSet && lastPassword != null) {
			plainDepartmentPassword = CryptoUtil.decryptDepartmentPassword(lastPassword, plainPassword);
			isPlainDepartmentPasswordSet = true;
		}
		return plainDepartmentPassword;
	}

	public PrivateKey getPrivateKey() throws Exception {
		if (!isPrivateKeySet && user != null) {
			privateKey = CryptoUtil.getPrivateKey(CryptoUtil.decryptPrivateKey(user.getKeyPair(), getPlainDepartmentPassword()));
			isPrivateKeySet = true;
		}
		return privateKey;
	}

	public PublicKey getPublicKey() throws Exception {
		if (!isPublicKeySet && user != null) {
			publicKey = CryptoUtil.getPublicKey(user.getKeyPair().getPublicKey());
			isPublicKeySet = true;
		}
		return publicKey;
	}

	public String getMethodName() {
		return methodName;
	}

	public String getRealm() {
		return realm;
	}

	public String getJwtAudience() {
		return jwtAudience;
	}

	public TimeZone getTimeZone() {
		if (!isTimeZoneSet && inheritedUser != null) {
			timeZone = CommonUtil.timeZoneFromString(inheritedUser.getTimeZone());
			isTimeZoneSet = true;
		}
		return timeZone;
	}

	public User getUser() {
		return user;
	}

	public UserInheritedVO getInheritedUser() {
		return inheritedUser;
	}

	/**
	 * The host is within the trusted ip ranges, or the call comes from a remembered device of the user.
	 */
	public boolean isTrustedHost() {
		return trustedDevice || isTrustedHostIp();
	}

	/**
	 * The host is within the trusted ip ranges.
	 */
	public boolean isTrustedHostIp() {
		if (isTrustedHost == null) {
			isTrustedHost = CoreUtil.checkHostIp(host);
		}
		return isTrustedHost;
	}

	/**
	 * The call carries the token of a device remembered after a 2FA logon of the user (see TrustedDeviceUtil).
	 */
	public boolean isTrustedDevice() {
		return trustedDevice;
	}

	public void setTrustedDevice(boolean trustedDevice) {
		this.trustedDevice = trustedDevice;
	}

	@Override
	public void reset() {
		super.reset();
		this.user = null;
		this.inheritedUser = null;
		this.lastPassword = null;
		this.plainPassword = null;
		this.host = null;
		this.realm = null;
		this.methodName = null;
		this.jwtAudience = null;
		isTrustedHost = null;
		trustedDevice = false;
		locale = null;
		isLocaleSet = false;
		timeZone = null;
		isTimeZoneSet = false;
		dateFormat = null;
		isDateFormatSet = false;
		decimalSeparator = null;
		isDecimalSeparatorSet = false;
		publicKey = null;
		isPublicKeySet = false;
		resetDecrypted();
	}

	private void resetDecrypted() {
		plainDepartmentPassword = null;
		departmentKey = null;
		privateKey = null;
		isPlainDepartmentPasswordSet = false;
		isDepartmentKeySet = false;
		isPrivateKeySet = false;
	}

	public void setDepartmentKey(SecretKey departmentKey) throws Exception {
		this.departmentKey = departmentKey;
		isDepartmentKeySet = true;
	}

	public void setHost(String host) {
		this.host = host;
		isTrustedHost = null;
		trustedDevice = false;
	}

	public void setLastPassword(Password lastPassword) {
		resetDecrypted();
		this.lastPassword = lastPassword;
	}

	public String getPlainPassword() {
		return plainPassword;
	}

	public void setPlainPassword(String plainPassword) {
		resetDecrypted();
		this.plainPassword = plainPassword;
	}

	public void setPrivateKey(PrivateKey privateKey) throws Exception {
		this.privateKey = privateKey;
		isPrivateKeySet = true;
	}

	public void setPublicKey(PublicKey publicKey) throws Exception {
		this.publicKey = publicKey;
		isPublicKeySet = true;
	}

	/**
	 * Localizes the results of this call in the given language instead of the user's own
	 * (see AuthenticationVO.locale).
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
		isLocaleSet = true;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public void setRealm(String realm) {
		this.realm = realm;
	}

	public void setJwtAudience(String jwtAudience) {
		this.jwtAudience = jwtAudience;
	}

	public void setUser(User user, UserInheritedVO inheritedUser) {
		reset();
		this.user = user;
		this.inheritedUser = inheritedUser;
	}

	public UserContext copyForThread() {
		UserContext copy = new UserContext(user, inheritedUser, lastPassword, plainPassword, host, realm);
		copy.isLocaleSet = isLocaleSet;
		copy.locale = locale;
		copy.isTimeZoneSet = isTimeZoneSet;
		copy.timeZone = timeZone;
		copy.isDateFormatSet = isDateFormatSet;
		copy.dateFormat = dateFormat;
		copy.isDecimalSeparatorSet = isDecimalSeparatorSet;
		copy.decimalSeparator = decimalSeparator;
		copy.isPlainDepartmentPasswordSet = isPlainDepartmentPasswordSet;
		copy.plainDepartmentPassword = plainDepartmentPassword;
		copy.isDepartmentKeySet = isDepartmentKeySet;
		copy.departmentKey = departmentKey;
		copy.isPrivateKeySet = isPrivateKeySet;
		copy.privateKey = privateKey;
		copy.isPublicKeySet = isPublicKeySet;
		copy.publicKey = publicKey;
		copy.isTrustedHost = isTrustedHost;
		copy.trustedDevice = trustedDevice;
		copy.methodName = methodName;
		copy.jwtAudience = jwtAudience;
		return copy;
	}

	@Override
	public String toString() {
		return getName();
	}
}
