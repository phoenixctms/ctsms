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

public class UserContext extends VOCacheContext {// implements Principal {

	private static final long serialVersionUID = 1L;
	private User user;
	private Password lastPassword;
	private String plainPassword;
	private String host;
	private String realm;
	private Locale locale;
	private TimeZone timeZone;
	private String plainDepartmentPassword;
	private SecretKey departmentKey;
	private PrivateKey privateKey;
	private PublicKey publicKey;
	private boolean isLocaleSet;
	private boolean isTimeZoneSet;
	private boolean isPlainDepartmentPasswordSet;
	private boolean isDepartmentKeySet;
	private boolean isPrivateKeySet;
	private boolean isPublicKeySet;
	private Boolean isTrustedHost;

	public UserContext() {
		super();
		reset();
	}

	public UserContext(User user, Password lastPassword, String plainPassword, String host, String realm) {
		super();
		reset();
		this.user = user;
		this.lastPassword = lastPassword;
		this.plainPassword = plainPassword;
		this.host = host;
		this.realm = realm;
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
		if (!isLocaleSet && user != null) {
			try {
				locale = CommonUtil.localeFromString(user.getLocale());
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

	public String getRealm() {
		return realm;
	}

	public TimeZone getTimeZone() {
		if (!isTimeZoneSet && user != null) {
			timeZone = CommonUtil.timeZoneFromString(user.getTimeZone());
			isTimeZoneSet = true;
		}
		return timeZone;
	}

	public User getUser() {
		return user;
	}

	// public boolean isPassDecryption() {
	public boolean isTrustedHost() {
		if (isTrustedHost == null) {
			isTrustedHost = CoreUtil.checkHostIp(host);
		}
		return isTrustedHost;
	}

	public void reset() {
		super.reset();
		this.user = null;
		this.lastPassword = null;
		this.plainPassword = null;
		this.host = null;
		this.realm = null;
		isTrustedHost = null;
		locale = null;
		isLocaleSet = false;
		timeZone = null;
		isTimeZoneSet = false;
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
	}

	public void setLastPassword(Password lastPassword) {
		resetDecrypted();
		this.lastPassword = lastPassword;
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

	public void setRealm(String realm) {
		this.realm = realm;
	}

	public void setUser(User user) {
		reset();
		this.user = user;
	}

	@Override
	public String toString()
	{
		return getName();
	}
}
