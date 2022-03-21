package org.phoenixctms.ctsms.security.otp;

import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.phoenixctms.ctsms.domain.Password;
import org.phoenixctms.ctsms.domain.User;
import org.phoenixctms.ctsms.email.NotificationMessageTemplateParameters;
import org.phoenixctms.ctsms.enumeration.OTPAuthenticatorType;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.DefaultMessages;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.MessageCodes;
import org.phoenixctms.ctsms.util.ServiceUtil;
import org.phoenixctms.ctsms.vo.PasswordInVO;

public abstract class OTPAuthenticator {

	private OTPAuthenticatorType type;
	private final static String TEMPLATE_ENCODING = "UTF-8";
	protected VelocityEngine velocityEngine;

	public String createOtpSecret() throws Exception {
		return null;
	}

	public abstract boolean verifyOtp(String otpSecret, String otpEntered, String otpSent) throws Exception;

	public String sendOtp(User user) throws Exception {
		return null;
	}

	public String getOtpRegistrationInfo(Password password, String plainDepartmentPassword) throws Exception {
		return null;
	}

	public final static OTPAuthenticator getInstance(OTPAuthenticatorType type) {
		OTPAuthenticator instance;
		switch (type) {
			case GOOGLE_AUTHENTICATOR:
				instance = new GoogleAuthenticator();
				break;
			default:
				throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.UNSUPPORTED_OTP_AUTHENTICATOR_TYPE, DefaultMessages.UNSUPPORTED_OTP_AUTHENTICATOR_TYPE, type));
		}
		instance.setType(type);
		return instance;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	private void setType(OTPAuthenticatorType type) {
		this.type = type;
	}

	protected String getOtpPRegistrationInfoMessage(Map messageParameters) throws Exception {
		return ServiceUtil.getVslFileMessage(velocityEngine, L10nUtil.getOTPRegistrationInfoMessageTemplate(Locales.USER, type.name()),
				messageParameters, TEMPLATE_ENCODING);
	}

	protected Map createTemplateModel(Map messageParameters) throws Exception {
		Map model = CoreUtil.createEmptyTemplateModel();
		//		NotificationVO preliminaryNotificationVO = this.toNotificationVO(notification);
		//		boolean enumerateEntities = Settings.getBoolean(SettingCodes.NOTIFICATION_TEMPLATE_MODEL_ENUMERATE_ENTITIES, Bundle.SETTINGS,
		//				DefaultSettings.NOTIFICATION_TEMPLATE_MODEL_ENUMERATE_ENTITIES);
		//		boolean excludeEncryptedFields = Settings.getBoolean(SettingCodes.NOTIFICATION_TEMPLATE_MODEL_OMIT_ENCRYPTED_FIELDS, Bundle.SETTINGS,
		//				DefaultSettings.NOTIFICATION_TEMPLATE_MODEL_OMIT_ENCRYPTED_FIELDS);
		//		String datetimePattern = Settings.getString(SettingCodes.NOTIFICATION_TEMPLATE_MODEL_DATETIME_PATTERN, Bundle.SETTINGS,
		//				DefaultSettings.NOTIFICATION_TEMPLATE_MODEL_DATETIME_PATTERN);
		//		String datePattern = Settings.getString(SettingCodes.NOTIFICATION_TEMPLATE_MODEL_DATE_PATTERN, Bundle.SETTINGS, DefaultSettings.NOTIFICATION_TEMPLATE_MODEL_DATE_PATTERN);
		//		String timePattern = Settings.getString(SettingCodes.NOTIFICATION_TEMPLATE_MODEL_TIME_PATTERN, Bundle.SETTINGS, DefaultSettings.NOTIFICATION_TEMPLATE_MODEL_TIME_PATTERN);
		//		Iterator<KeyValueString> voFieldIt = KeyValueString
		//				.getKeyValuePairs(
		//						NotificationVO.class,
		//						Settings.getInt(SettingCodes.NOTIFICATION_TEMPLATE_MODEL_VO_DEPTH, Bundle.SETTINGS, DefaultSettings.NOTIFICATION_TEMPLATE_MODEL_VO_DEPTH),
		//						excludeEncryptedFields,
		//						null,
		//						enumerateEntities,
		//						Settings.getBoolean(SettingCodes.NOTIFICATION_TEMPLATE_MODEL_ENUMERATE_REFERENCES, Bundle.SETTINGS,
		//								DefaultSettings.NOTIFICATION_TEMPLATE_MODEL_ENUMERATE_REFERENCES),
		//						Settings.getBoolean(SettingCodes.NOTIFICATION_TEMPLATE_MODEL_ENUMERATE_COLLECTIONS, Bundle.SETTINGS,
		//								DefaultSettings.NOTIFICATION_TEMPLATE_MODEL_ENUMERATE_COLLECTIONS),
		//						Settings.getBoolean(SettingCodes.NOTIFICATION_TEMPLATE_MODEL_ENUMERATE_MAPS, Bundle.SETTINGS, DefaultSettings.NOTIFICATION_TEMPLATE_MODEL_ENUMERATE_MAPS),
		//						NotificationMessageTemplateParameters.TEMPLATE_MODEL_FIELD_NAME_ASSOCIATION_PATH_SEPARATOR,
		//						NotificationMessageTemplateParameters.TEMPLATE_MODEL_LOWER_CASE_FIELD_NAMES)
		//				.iterator();
		//		while (voFieldIt.hasNext()) {
		//			KeyValueString keyValuePair = voFieldIt.next();
		//			Iterator<ArrayList<Object>> indexesKeysIt = keyValuePair.getIndexesKeys(preliminaryNotificationVO).iterator();
		//			while (indexesKeysIt.hasNext()) {
		//				ArrayList<Object> indexesKeys = indexesKeysIt.next();
		//				model.put(keyValuePair.getKey(indexesKeys),
		//						keyValuePair.getValue(Locales.NOTIFICATION, preliminaryNotificationVO, indexesKeys, datetimePattern, datePattern, timePattern, enumerateEntities,
		//								excludeEncryptedFields));
		//			}
		//		}
		//		model.put(NotificationMessageTemplateParameters.NOTIFICATION, preliminaryNotificationVO);
		//		model.put(
		//				NotificationMessageTemplateParameters.GENERATED_ON,
		//				Settings.getSimpleDateFormat(SettingCodes.NOTIFICATION_TEMPLATE_MODEL_DATETIME_PATTERN, Bundle.SETTINGS,
		//						DefaultSettings.NOTIFICATION_TEMPLATE_MODEL_DATETIME_PATTERN, Locales.NOTIFICATION).format(today));
		//		model.put(
		//				NotificationMessageTemplateParameters.INSTANCE_NAME, Settings.getInstanceName());
		//		model.put(
		//				NotificationMessageTemplateParameters.HTTP_BASE_URL, Settings.getHttpBaseUrl());
		//		model.put(
		//				NotificationMessageTemplateParameters.HTTP_DOMAIN_NAME, Settings.getHttpDomainName());
		model.put(NotificationMessageTemplateParameters.STRING_UTILS, ServiceUtil.VELOCITY_STRING_UTILS);
		if (messageParameters != null && messageParameters.size() > 0) {
			model.putAll(messageParameters);
		}
		return model;
	}

	public void checkLogonLimitations(PasswordInVO password) throws ServiceException {
	}
}
