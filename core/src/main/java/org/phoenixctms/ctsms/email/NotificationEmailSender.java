package org.phoenixctms.ctsms.email;

import java.text.MessageFormat;
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
import org.phoenixctms.ctsms.util.SettingCodes;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.springframework.mail.javamail.MimeMessageHelper;

public class NotificationEmailSender extends EmailSender<Notification, Staff> {

	private StaffDao staffDao;

	@Override
	protected int prepare(MimeMessage mimeMessage, Notification notification, Staff recipient) throws Exception {
		if (!Settings.getBoolean(SettingCodes.EMAIL_NOTIFICATIONS_ENABLED, Bundle.SETTINGS, DefaultSettings.EMAIL_NOTIFICATIONS_ENABLED)) {
			return 0;
		}
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, EMAIL_ENCODING);
		String subject = notification.getSubject();
		String text = notification.getMessage();
		if (!CommonUtil.isEmptyString(subject)) {
			mimeMessageHelper.setSubject(subject);
		} else {
			return 0;
		}
		if (!CommonUtil.isEmptyString(text)) {
			mimeMessageHelper.setText(text);
		} else {
			return 0;
		}
		mimeMessageHelper.setFrom(Settings.getEmailNotificationFromAddress(), Settings.getEmailNotificationFromName());
		StaffOutVO recipientVO = staffDao.toStaffOutVO(recipient);
		int toCount = 0;
		Iterator<StaffContactDetailValue> contactsIt = recipient.getContactDetailValues().iterator();
		while (contactsIt.hasNext()) {
			StaffContactDetailValue contact = contactsIt.next();
			ContactDetailType contactType;
			if (!contact.isNa() && contact.isNotify() && (contactType = contact.getType()).isEmail()) {
				mimeMessageHelper.addTo(contact.getValue(),
						MessageFormat.format(EMAIL_TO_PERSONAL_NAME, recipientVO.getName(), L10nUtil.getContactDetailTypeName(Locales.NOTIFICATION, contactType.getNameL10nKey())));
				toCount++;
			}
		}
		return toCount;
	}

	public void setStaffDao(StaffDao staffDao) {
		this.staffDao = staffDao;
	}
}
