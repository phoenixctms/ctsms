package org.phoenixctms.ctsms.web.model;

import org.phoenixctms.ctsms.enumeration.OTPAuthenticatorType;

public interface OTPAuthenticatorTypeSelectorListener {

	public final static OTPAuthenticatorType NO_SELECTION_OTP_AUTHENTICATION_TYPE = null;

	public OTPAuthenticatorType getOtpAuthenticatorType(int property);

	public void setOtpAuthenticatorType(int property, OTPAuthenticatorType type);
}
