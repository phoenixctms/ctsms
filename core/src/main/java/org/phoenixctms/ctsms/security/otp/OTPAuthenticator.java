package org.phoenixctms.ctsms.security.otp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.phoenixctms.ctsms.domain.Password;
import org.phoenixctms.ctsms.domain.User;
import org.phoenixctms.ctsms.enumeration.OTPAuthenticatorType;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.DefaultMessages;
import org.phoenixctms.ctsms.util.DefaultSettings;
import org.phoenixctms.ctsms.util.KeyValueString;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.MessageCodes;
import org.phoenixctms.ctsms.util.ServiceUtil;
import org.phoenixctms.ctsms.util.SettingCodes;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.vo.PasswordInVO;

public abstract class OTPAuthenticator {

	private final static String TEMPLATE_ENCODING = "UTF-8";
	protected VelocityEngine velocityEngine;

	public String createOtpSecret() throws Exception {
		return null;
	}

	public abstract boolean verifyOtp(String otpSecret, String otp, String otpToken) throws Exception;

	public String sendOtp(User user) throws Exception {
		return null;
	}

	public String getOtpRegistrationInfo(Password password, String plainDepartmentPassword) throws Exception {
		return null;
	}

	public final static OTPAuthenticator getInstance(OTPAuthenticatorType type) {
		switch (type) {
			case GOOGLE_AUTHENTICATOR:
				return (OTPAuthenticator) CoreUtil.getApplicationContext().getBean(GoogleAuthenticator.BEAN_NAME);
			default:
				throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.UNSUPPORTED_OTP_AUTHENTICATOR_TYPE, DefaultMessages.UNSUPPORTED_OTP_AUTHENTICATOR_TYPE, type));
		}
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	protected String getOtpPRegistrationInfoMessage(Map messageParameters) throws Exception {
		return ServiceUtil.getVslFileMessage(velocityEngine, L10nUtil.getOTPRegistrationInfoMessageTemplate(Locales.USER, getType().name()),
				messageParameters, TEMPLATE_ENCODING);
	}

	protected abstract OTPAuthenticatorType getType();

	protected Map createTemplateModel(Map messageParameters, Object vo) throws Exception {
		Map model = CoreUtil.createEmptyTemplateModel();
		if (vo != null) {
			boolean enumerateEntities = Settings.getBoolean(SettingCodes.OTP_REGISTRATION_INFO_TEMPLATE_MODEL_ENUMERATE_ENTITIES, Bundle.SETTINGS,
					DefaultSettings.OTP_REGISTRATION_INFO_TEMPLATE_MODEL_ENUMERATE_ENTITIES);
			boolean excludeEncryptedFields = Settings.getBoolean(SettingCodes.OTP_REGISTRATION_INFO_TEMPLATE_MODEL_OMIT_ENCRYPTED_FIELDS, Bundle.SETTINGS,
					DefaultSettings.OTP_REGISTRATION_INFO_TEMPLATE_MODEL_OMIT_ENCRYPTED_FIELDS);
			String datetimePattern = Settings.getString(SettingCodes.OTP_REGISTRATION_INFO_TEMPLATE_MODEL_DATETIME_PATTERN, Bundle.SETTINGS,
					DefaultSettings.OTP_REGISTRATION_INFO_TEMPLATE_MODEL_DATETIME_PATTERN);
			String datePattern = Settings.getString(SettingCodes.OTP_REGISTRATION_INFO_TEMPLATE_MODEL_DATE_PATTERN, Bundle.SETTINGS,
					DefaultSettings.OTP_REGISTRATION_INFO_TEMPLATE_MODEL_DATE_PATTERN);
			String timePattern = Settings.getString(SettingCodes.OTP_REGISTRATION_INFO_TEMPLATE_MODEL_TIME_PATTERN, Bundle.SETTINGS,
					DefaultSettings.OTP_REGISTRATION_INFO_TEMPLATE_MODEL_TIME_PATTERN);
			Iterator<KeyValueString> voFieldIt = KeyValueString
					.getKeyValuePairs(
							vo.getClass(),
							Settings.getInt(SettingCodes.OTP_REGISTRATION_INFO_TEMPLATE_MODEL_VO_DEPTH, Bundle.SETTINGS,
									DefaultSettings.OTP_REGISTRATION_INFO_TEMPLATE_MODEL_VO_DEPTH),
							excludeEncryptedFields,
							null,
							enumerateEntities,
							Settings.getBoolean(SettingCodes.OTP_REGISTRATION_INFO_TEMPLATE_MODEL_ENUMERATE_REFERENCES, Bundle.SETTINGS,
									DefaultSettings.OTP_REGISTRATION_INFO_TEMPLATE_MODEL_ENUMERATE_REFERENCES),
							Settings.getBoolean(SettingCodes.OTP_REGISTRATION_INFO_TEMPLATE_MODEL_ENUMERATE_COLLECTIONS, Bundle.SETTINGS,
									DefaultSettings.OTP_REGISTRATION_INFO_TEMPLATE_MODEL_ENUMERATE_COLLECTIONS),
							Settings.getBoolean(SettingCodes.OTP_REGISTRATION_INFO_TEMPLATE_MODEL_ENUMERATE_MAPS, Bundle.SETTINGS,
									DefaultSettings.OTP_REGISTRATION_INFO_TEMPLATE_MODEL_ENUMERATE_MAPS),
							OTPRegistrationInfoMessageTemplateParameters.TEMPLATE_MODEL_FIELD_NAME_ASSOCIATION_PATH_SEPARATOR,
							OTPRegistrationInfoMessageTemplateParameters.TEMPLATE_MODEL_LOWER_CASE_FIELD_NAMES)
					.iterator();
			while (voFieldIt.hasNext()) {
				KeyValueString keyValuePair = voFieldIt.next();
				Iterator<ArrayList<Object>> indexesKeysIt = keyValuePair.getIndexesKeys(vo).iterator();
				while (indexesKeysIt.hasNext()) {
					ArrayList<Object> indexesKeys = indexesKeysIt.next();
					messageParameters.put(keyValuePair.getKey(indexesKeys),
							keyValuePair.getValue(Locales.USER, vo, indexesKeys, datetimePattern, datePattern, timePattern, enumerateEntities,
									excludeEncryptedFields));
				}
			}
			//messageParameters.put(OTPRegistrationInfoMessageTemplateParameters.PASSWORD, passwordVO);
		}
		//		model.put(
		//				NotificationMessageTemplateParameters.GENERATED_ON,
		//				Settings.getSimpleDateFormat(SettingCodes.NOTIFICATION_TEMPLATE_MODEL_DATETIME_PATTERN, Bundle.SETTINGS,
		//						DefaultSettings.NOTIFICATION_TEMPLATE_MODEL_DATETIME_PATTERN, Locales.NOTIFICATION).format(today));
		model.put(OTPRegistrationInfoMessageTemplateParameters.APPLICATION_ABBREVIATION,
				Settings.getString(SettingCodes.APPLICATION_ABBREVIATION, Bundle.SETTINGS, null));
		model.put(
				OTPRegistrationInfoMessageTemplateParameters.INSTANCE_NAME, Settings.getInstanceName());
		//		model.put(
		//				OTPRegistrationInfoMessageTemplateParameters.HTTP_BASE_URL, Settings.getHttpBaseUrl());
		//model.put(
		//		OTPRegistrationInfoMessageTemplateParameters.HTTP_DOMAIN_NAME, Settings.getHttpDomainName());
		model.put(OTPRegistrationInfoMessageTemplateParameters.TEMPLATE_ENCODING, TEMPLATE_ENCODING);
		model.put(OTPRegistrationInfoMessageTemplateParameters.STRING_UTILS, ServiceUtil.VELOCITY_STRING_UTILS);
		if (messageParameters != null && messageParameters.size() > 0) {
			model.putAll(messageParameters);
		}
		return model;
	}

	public void checkLogonLimitations(PasswordInVO password) throws ServiceException {
	}
}
