package org.phoenixctms.ctsms.email;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.mail.internet.MimeMessage;

import org.phoenixctms.ctsms.domain.ContactDetailType;
import org.phoenixctms.ctsms.domain.Notification;
import org.phoenixctms.ctsms.domain.Staff;
import org.phoenixctms.ctsms.domain.StaffContactDetailValue;
import org.phoenixctms.ctsms.domain.StaffDao;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.DefaultSettings;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.ServiceExceptionCodes;
import org.phoenixctms.ctsms.util.SettingCodes;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.vo.EmailAttachmentVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.springframework.mail.javamail.MimeMessageHelper;

public class NotificationEmailSender extends EmailSender<Notification, Staff> {

	private final static boolean HTML = false;
	private StaffDao staffDao;

	@Override
	protected void addAttachments(Notification notification, Staff recipient, ArrayList<EmailAttachmentVO> attachments) throws Exception {
	}

	@Override
	protected boolean isHtml() {
		return HTML;
	}

	@Override
	protected MimeMessage loadMessage(Notification entity, Staff recipient, Date now) throws Exception {
		return null;
	}

	@Override
	protected void prepareMessage(MimeMessage mimeMessage, Notification notification, Staff recipient, StringBuilder text, Date now) throws Exception {
		if (!Settings.getBoolean(SettingCodes.EMAIL_NOTIFICATIONS_ENABLED, Bundle.SETTINGS, DefaultSettings.EMAIL_NOTIFICATIONS_ENABLED)) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.EMAIL_NOTIFICATIONS_DISABLED);
		}
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, getEncoding());
		String subject = notification.getSubject();
		if (!CommonUtil.isEmptyString(subject)) {
			mimeMessageHelper.setSubject(subject);
		} else {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.EMAIL_EMPTY_SUBJECT);
		}
		String message = notification.getMessage();
		if (!CommonUtil.isEmptyString(message)) {
			mimeMessageHelper.setText(message, isHtml());
			text.append(message);
		} else {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.EMAIL_EMPTY_MESSAGE);
		}
		mimeMessageHelper.setFrom(Settings.getEmailNotificationFromAddress(), Settings.getEmailNotificationFromName());
		StaffOutVO recipientVO = staffDao.toStaffOutVO(recipient);
		Iterator<StaffContactDetailValue> contactsIt = recipient.getContactDetailValues().iterator();
		while (contactsIt.hasNext()) {
			StaffContactDetailValue contact = contactsIt.next();
			ContactDetailType contactType;
			if (!contact.isNa() && contact.isNotify() && (contactType = contact.getType()).isEmail()) {
				mimeMessageHelper.addTo(contact.getValue(),
						MessageFormat.format(EMAIL_TO_PERSONAL_NAME, recipientVO.getName(), L10nUtil.getContactDetailTypeName(Locales.NOTIFICATION, contactType.getNameL10nKey())));
			}
		}
	}

	public void setStaffDao(StaffDao staffDao) {
		this.staffDao = staffDao;
	}

	@Override
	protected void storeMessage(MimeMessage mimeMessage, Notification entity, Staff recipient, Date now) throws Exception {
	}
}
