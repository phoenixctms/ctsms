package org.phoenixctms.ctsms.web.model;

import java.util.ArrayList;

import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.OTPAuthenticatorType;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class OTPAuthenticatorTypeSelector {

	private OTPAuthenticatorTypeSelectorListener bean;
	private int property;
	private ArrayList<SelectItem> otpAuthenticatorTypes;
	private final static String NO_SELECTION_STRING = CommonUtil.NO_SELECTION_VALUE;

	public OTPAuthenticatorTypeSelector(OTPAuthenticatorTypeSelectorListener bean, int property) {
		this.bean = bean;
		this.property = property;
		otpAuthenticatorTypes = WebUtil.getOTPAuthenticatorTypes();
	}

	public OTPAuthenticatorTypeSelectorListener getBean() {
		return bean;
	}

	public String getType() {
		if (bean != null) {
			OTPAuthenticatorType type = bean.getOtpAuthenticatorType(property);
			if (type != null) {
				return type.name();
			} else {
				return NO_SELECTION_STRING;
			}
		}
		return NO_SELECTION_STRING;
	}

	public ArrayList<SelectItem> getTypes() {
		return otpAuthenticatorTypes;
	}

	public void setBean(OTPAuthenticatorTypeSelectorListener bean) {
		this.bean = bean;
	}

	public void setType(String name) {
		if (bean != null) {
			OTPAuthenticatorType type;
			if (name != null && !name.equals(NO_SELECTION_STRING)) {
				try {
					type = OTPAuthenticatorType.fromString(name);
				} catch (Exception e) {
					type = OTPAuthenticatorTypeSelectorListener.NO_SELECTION_OTP_AUTHENTICATION_TYPE;
				}
			} else {
				type = OTPAuthenticatorTypeSelectorListener.NO_SELECTION_OTP_AUTHENTICATION_TYPE;
			}
			bean.setOtpAuthenticatorType(property, type);
		}
	}
}
