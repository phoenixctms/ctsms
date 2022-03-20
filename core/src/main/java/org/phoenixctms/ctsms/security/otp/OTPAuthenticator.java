package org.phoenixctms.ctsms.security.otp;

import org.phoenixctms.ctsms.domain.User;
import org.phoenixctms.ctsms.enumeration.OTPAuthenticatorType;
import org.phoenixctms.ctsms.util.DefaultMessages;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.MessageCodes;

public abstract class OTPAuthenticator {

	public abstract String createOtpSecret() throws Exception;

	public abstract boolean verifyOtp(User user, String plainPassword, String code) throws Exception;

	public final static OTPAuthenticator getInstance(OTPAuthenticatorType type) {
		switch (type) {
			case GOOGLE_AUTHENTICATOR:
				return new GoogleAuthenticator();
			default:
				throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.UNSUPPORTED_OTP_AUTHENTICATOR_TYPE, DefaultMessages.UNSUPPORTED_OTP_AUTHENTICATOR_TYPE, type));
		}
	}
}
