package org.phoenixctms.ctsms.email;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;

public abstract class EmailSender<ENTITY, RECIPIENT> {

	protected final static String EMAIL_TO_PERSONAL_NAME = "{0} ({1})";

	protected final static String EMAIL_ENCODING = "UTF-8";


	private JavaMailSender mailSender;

	protected abstract int prepare(MimeMessage mimeMessage, ENTITY entity, RECIPIENT recipient) throws Exception;

	public int send(ENTITY entity, RECIPIENT recipient) throws Exception {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		int toCount = prepare(mimeMessage, entity, recipient);
		if (toCount > 0) {
			mailSender.send(mimeMessage);
		}
		return toCount;
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
}
