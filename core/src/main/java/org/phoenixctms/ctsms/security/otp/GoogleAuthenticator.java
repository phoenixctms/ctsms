package org.phoenixctms.ctsms.security.otp;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base32;
import org.phoenixctms.ctsms.domain.PasswordDao;
import org.phoenixctms.ctsms.domain.User;
import org.phoenixctms.ctsms.security.CryptoUtil;
import org.phoenixctms.ctsms.util.DefaultSettings;
import org.phoenixctms.ctsms.util.SettingCodes;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;

public class GoogleAuthenticator extends OTPAuthenticator {

	private static final String SECRET_RANDOM_ALGORITHM = "SHA1PRNG";
	private static final int SECRET_LENGTH = 20;
	private static final String MAC_ALGORITHM = "HmacSHA1";
	private PasswordDao passwordDao = null;

	public String createOtpSecret() throws NoSuchAlgorithmException {
		SecureRandom rand = SecureRandom.getInstance(SECRET_RANDOM_ALGORITHM);
		byte[] secret = new byte[SECRET_LENGTH];
		rand.nextBytes(secret);
		Base32 codec = new Base32();
		return codec.encodeToString(secret);
	}

	public boolean verifyOtp(User user, String plainPassword, String code) throws Exception {
		return verifyOtp(CryptoUtil.decryptOtpSecret(passwordDao.findLastPassword(user.getId()), plainPassword), Long.parseLong(code), System.currentTimeMillis());
	}

	private static boolean verifyOtp(String otpSecret, long code, long time) throws NoSuchAlgorithmException, InvalidKeyException {
		Base32 codec = new Base32();
		byte[] secret = codec.decode(otpSecret);
		int window = Settings.getInt(SettingCodes.GOOGLE_AUTHENTICATOR_WINDOW, Bundle.SETTINGS, DefaultSettings.GOOGLE_AUTHENTICATOR_WINDOW);
		for (int i = -((window - 1) / 2); i <= window / 2; ++i) {
			long hash = verifyOtp(secret,
					time / (Settings.getInt(SettingCodes.GOOGLE_AUTHENTICATOR_STEP_SIZE_SECS, Bundle.SETTINGS, DefaultSettings.GOOGLE_AUTHENTICATOR_STEP_SIZE_SECS) * 1000) + i);
			if (hash == code) {
				return true;
			}
		}
		return false;
	}

	private static int verifyOtp(byte[] secret, long t) throws NoSuchAlgorithmException, InvalidKeyException {
		byte[] data = new byte[8];
		long value = t;
		for (int i = 8; i-- > 0; value >>>= 8) {
			data[i] = (byte) value;
		}
		SecretKeySpec signKey = new SecretKeySpec(secret, MAC_ALGORITHM);
		Mac mac = Mac.getInstance(MAC_ALGORITHM);
		mac.init(signKey);
		byte[] hash = mac.doFinal(data);
		int offset = hash[hash.length - 1] & 0xF;
		long truncatedHash = 0;
		for (int i = 0; i < 4; ++i) {
			truncatedHash <<= 8;
			truncatedHash |= (hash[offset + i] & 0xFF);
		}
		truncatedHash &= 0x7FFFFFFF;
		truncatedHash %= 1000000;
		return (int) truncatedHash;
	}

	public PasswordDao getPasswordDao() {
		return passwordDao;
	}

	public void setPasswordDao(PasswordDao passwordDao) {
		this.passwordDao = passwordDao;
	}
}
