package org.phoenixctms.ctsms.email;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Address;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.EmailAttachmentVO;
import org.springframework.mail.javamail.JavaMailSender;

public abstract class EmailSender<ENTITY, RECIPIENT> {

	protected final static String EMAIL_TO_PERSONAL_NAME = "{0} ({1})";

	private final static String EMAIL_ENCODING = "UTF-8";

	private static final String HTML_CONTENT_SUBTYPE = "html";
	private static final String PLAIN_CONTENT_SUBTYPE = "plain";

	protected JavaMailSender mailSender;

	protected abstract void addAttachments(ENTITY entity, RECIPIENT recipient, ArrayList<EmailAttachmentVO> attachments) throws Exception;


	private final MimeMessage createMessage(ENTITY entity, RECIPIENT recipient)  throws Exception {
		Date now = new Date();
		MimeMessage mimeMessage = loadMessage(entity, recipient, now);
		if (mimeMessage != null) {
			return mimeMessage;
		} else {
			mimeMessage = mailSender.createMimeMessage();
			StringBuilder text = new StringBuilder();

			prepareMessage(mimeMessage, entity, recipient, text, now);
			if (getToCount(mimeMessage) > 0) {
				ArrayList<EmailAttachmentVO> attachments = new ArrayList<EmailAttachmentVO>();
				addAttachments(entity, recipient,attachments);
				if ( attachments.size() > 0) {
					Multipart multipart = new MimeMultipart();
					Iterator<EmailAttachmentVO> it = attachments.iterator();
					MimeBodyPart messageBodyPart = new MimeBodyPart();
					messageBodyPart.setText(text.toString(), getEncoding(), isHtml() ? HTML_CONTENT_SUBTYPE : PLAIN_CONTENT_SUBTYPE);
					multipart.addBodyPart(messageBodyPart);
					while (it.hasNext()) {
						EmailAttachmentVO attachment = it.next();
						messageBodyPart = new MimeBodyPart();
						DataSource source = new ByteArrayDataSource(attachment.getDatas(), attachment.getContentType().getMimeType());
						messageBodyPart.setDataHandler(new DataHandler(source));
						messageBodyPart.setFileName(attachment.getFileName());
						multipart.addBodyPart(messageBodyPart);
					}
					mimeMessage.setContent(multipart);
				}
				storeMessage(mimeMessage, entity, recipient, now);
				// return mimeMessage;
			}
			return mimeMessage;
		}
	}

	protected String getEncoding() {
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

	public JavaMailSender getMailSender() {
		return mailSender;
	}

	public final int getToCount(MimeMessage mimeMessage) throws Exception {
		if (mimeMessage != null) {
			Address[] to = mimeMessage.getRecipients(RecipientType.TO);
			if (to != null) {
				return to.length;
			}
		}
		return 0;
	}

	protected abstract boolean isHtml();

	protected abstract MimeMessage loadMessage(ENTITY entity, RECIPIENT recipient, Date now) throws Exception;

	public final MimeMessage prepare(ENTITY entity, RECIPIENT recipient) throws Exception {
		return createMessage(entity, recipient);
	}

	public final int prepareAndSend(ENTITY entity, RECIPIENT recipient) throws Exception {
		return send(prepare(entity,recipient));
	}

	protected abstract void prepareMessage(MimeMessage mimeMessage, ENTITY entity, RECIPIENT recipient, StringBuilder text, Date now) throws Exception;

	public final int send(MimeMessage mimeMessage) throws Exception {
		int toCount = getToCount(mimeMessage);
		if (toCount > 0) {
			mailSender.send(mimeMessage);
		}
		return toCount;
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	protected abstract void storeMessage(MimeMessage mimeMessage, ENTITY entity, RECIPIENT recipient, Date now) throws Exception;
}
