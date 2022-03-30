package org.phoenixctms.ctsms.security.otp;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base32;
import org.phoenixctms.ctsms.domain.Password;
import org.phoenixctms.ctsms.domain.PasswordDao;
import org.phoenixctms.ctsms.enumeration.OTPAuthenticatorType;
import org.phoenixctms.ctsms.security.CryptoUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.DefaultSettings;
import org.phoenixctms.ctsms.util.SettingCodes;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;

public class GoogleAuthenticator extends OTPAuthenticator {

	private final static OTPAuthenticatorType TYPE = OTPAuthenticatorType.GOOGLE_AUTHENTICATOR;
	public final static String BEAN_NAME = "googleAuthenticator";
	private static final String SECRET_RANDOM_ALGORITHM = "SHA1PRNG";
	private static final int SECRET_LENGTH = 20;
	private static final String MAC_ALGORITHM = "HmacSHA1";
	private PasswordDao passwordDao;

	protected GoogleAuthenticator() {
		super();
	}

	@Override
	public String createOtpSecret() throws NoSuchAlgorithmException {
		//128bits at least. we dont use scratch codes.
		SecureRandom rand = SecureRandom.getInstance(SECRET_RANDOM_ALGORITHM);
		byte[] secret = new byte[SECRET_LENGTH];
		rand.nextBytes(secret);
		Base32 codec = new Base32();
		return codec.encodeToString(secret);
	}

	@Override
	public boolean verifyOtp(String otpSecret, String otp, String otpToken) throws InvalidKeyException, NumberFormatException, NoSuchAlgorithmException {
		return verifyOtp(otpSecret, Long.parseLong(otp), System.currentTimeMillis());
	}

	@Override
	public String getOtpRegistrationInfo(Password password, String plainDepartmentPassword) throws Exception {
		Map messageParameters = CoreUtil.createEmptyTemplateModel();
		String otpSecret = null;
		if (password != null) {
			otpSecret = CryptoUtil.decryptOtpSecret(password, CryptoUtil.decryptPassword(password, plainDepartmentPassword));
		}
		if (otpSecret != null) {
			messageParameters.put(OTPRegistrationInfoMessageTemplateParameters.OTP_SECRET, otpSecret);
		}
		Map model = createTemplateModel(messageParameters, passwordDao.toPasswordOutVO(password));
		return getOtpPRegistrationInfoMessage(model);
	}

	//https://thegreyblog.blogspot.com/2011/12/google-authenticator-using-it-in-your.html
	private static boolean verifyOtp(String otpSecret, long otp, long time) throws NoSuchAlgorithmException, InvalidKeyException {
		Base32 codec = new Base32();
		byte[] secret = codec.decode(otpSecret);
		//the window in time steps to tolerate for clock skew:
		int window = Settings.getInt(SettingCodes.GOOGLE_AUTHENTICATOR_WINDOW, Bundle.SETTINGS, DefaultSettings.GOOGLE_AUTHENTICATOR_WINDOW);
		for (int i = -((window - 1) / 2); i <= window / 2; ++i) {
			long hash = verifyOtp(secret,
					time / (Settings.getInt(SettingCodes.GOOGLE_AUTHENTICATOR_STEP_SIZE_SECS, Bundle.SETTINGS, DefaultSettings.GOOGLE_AUTHENTICATOR_STEP_SIZE_SECS) * 1000) + i);
			if (hash == otp) {
				return true;
			}
		}
		return false;
	}

	//https://thegreyblog.blogspot.com/2011/12/google-authenticator-using-it-in-your.html
	private static int verifyOtp(byte[] secret, long t) throws NoSuchAlgorithmException, InvalidKeyException {
		byte[] data = new byte[8];
		long value = t; //the current time in steps
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

	@Override
	protected OTPAuthenticatorType getType() {
		return TYPE;
	}

	public void setPasswordDao(PasswordDao passwordDao) {
		this.passwordDao = passwordDao;
	}
}
