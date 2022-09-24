package org.phoenixctms.ctsms.test;

import javax.mail.internet.MimeMessage;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.Settings;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

public class ReportEmailSender extends JobEmailSenderBase {

	private JavaMailSender mailSender;

	public ReportEmailSender() {
		reset();
	}

	public String getEmailRecipients() {
		return System.getProperty("ctsms.test.emailrecipients");
	}

	@Override
	protected String getEmailEncoding() {
		String encoding = null;
		try {
			encoding = ((org.springframework.mail.javamail.JavaMailSenderImpl) mailSender).getDefaultEncoding();
		} catch (Exception e) {
		}
		if (CommonUtil.isEmptyString(encoding)) {
			encoding = EMAIL_ENCODING;
		}
		return encoding;
	}

	private void prepareEmail(MimeMessage mimeMessage, String subject, String text, String emailAddresses) throws Exception {
		if (!CommonUtil.isEmptyString(emailAddresses)) {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, getEmailEncoding());
			mimeMessageHelper.setSubject(subject);
			mimeMessageHelper.setText(text);
			mimeMessageHelper.setFrom(Settings.getEmailExecFromAddress(), Settings.getEmailExecFromName());
			String[] addresses = emailAddressSeparatorRegexp.split(emailAddresses, -1);
			for (int i = 0; i < addresses.length; i++) {
				if (!CommonUtil.isEmptyString(addresses[i])) {
					mimeMessageHelper.addTo(addresses[i].trim());
				}
			}
		}
	}

	public int send(String subject, String text) throws Exception {
		String recipients = getEmailRecipients();
		if (!CommonUtil.isEmptyString(recipients)) {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			prepareEmail(mimeMessage, subject, text, recipients);
			int toCount = prepareMultipart(mimeMessage, text);
			if (toCount > 0) {
				mailSender.send(mimeMessage);
			}
			return toCount;
		}
		return 0;
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
}
