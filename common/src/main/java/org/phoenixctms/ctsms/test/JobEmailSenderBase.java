package org.phoenixctms.ctsms.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Address;
import javax.mail.Message.RecipientType;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.phoenixctms.ctsms.vo.MimeTypeVO;

public abstract class JobEmailSenderBase {

	protected final static String EMAIL_ENCODING = "UTF-8";
	protected final static String DEFAULT_EMAIL_ADDRESS_SEPARATOR = ";";
	protected final static Pattern emailAddressSeparatorRegexp = Pattern.compile(DEFAULT_EMAIL_ADDRESS_SEPARATOR + "|,| ");

	private class EmailAttachment {

		private byte[] data;
		private String mimeType;
		private String fileName;

		public EmailAttachment(byte[] data, String mimeType, String fileName) {
			super();
			this.data = data;
			this.mimeType = mimeType;
			this.fileName = fileName;
		}

		public byte[] getData() {
			return data;
		}

		public String getFileName() {
			return fileName;
		}

		public String getMimeType() {
			return mimeType;
		}
	}

	private ArrayList<EmailAttachment> attachments;

	public void reset() {
		attachments = new ArrayList<EmailAttachment>();
	}

	public void addEmailAttachment(byte[] data, MimeTypeVO contentType, String fileName) {
		addEmailAttachment(data, contentType.getMimeType(), fileName);
	}

	public void addEmailAttachment(byte[] data, String mimeType, String fileName) {
		attachments.add(new EmailAttachment(data, mimeType, fileName));
	}

	protected abstract String getEmailEncoding();

	protected int prepareMultipart(MimeMessage mimeMessage, String text) throws Exception {
		Address[] to = mimeMessage.getRecipients(RecipientType.TO);
		int toCount = to != null ? to.length : 0;
		if (toCount > 0) {
			if (attachments.size() > 0) {
				Multipart multipart = new MimeMultipart();
				Iterator<EmailAttachment> it = attachments.iterator();
				MimeBodyPart messageBodyPart = new MimeBodyPart();
				messageBodyPart.setText(text, getEmailEncoding());
				multipart.addBodyPart(messageBodyPart);
				while (it.hasNext()) {
					EmailAttachment attachment = it.next();
					messageBodyPart = new MimeBodyPart();
					DataSource source = new ByteArrayDataSource(attachment.getData(), attachment.getMimeType());
					messageBodyPart.setDataHandler(new DataHandler(source));
					messageBodyPart.setFileName(attachment.getFileName());
					multipart.addBodyPart(messageBodyPart);
				}
				mimeMessage.setContent(multipart);
			}
		}
		return toCount;
	}
}