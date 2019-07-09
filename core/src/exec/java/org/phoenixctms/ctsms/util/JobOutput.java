package org.phoenixctms.ctsms.util;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.phoenixctms.ctsms.enumeration.JobStatus;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.service.shared.JobService;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.JobFileVO;
import org.phoenixctms.ctsms.vo.JobOutVO;
import org.phoenixctms.ctsms.vo.JobUpdateVO;
import org.phoenixctms.ctsms.vo.MimeTypeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

public class JobOutput {

	static class EmailAttachment {

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

	@Autowired
	protected JobService jobService;
	private final static String EMAIL_ENCODING = "UTF-8";
	private final static String LINE_FORMAT = "{0}";
	private final static String LINE_FORMAT_WITH_TIMESTAMP = "{0}: {1}";
	private final static boolean ALWAYS_PRINT_TIMESTAMP = false;
	private final static String DEFAULT_EMAIL_ADDRESS_SEPARATOR = ";";
	private final static Pattern emailAddressSeparatorRegexp = Pattern.compile(DEFAULT_EMAIL_ADDRESS_SEPARATOR + "|,| ");
	private final static String JOB_FILE_NAME_FORMAT = "{0}.{1}";
	private JobUpdateVO job;
	private AuthenticationVO auth;
	private JobFileVO jobFile;

	private static StringBuilder getEmailRecipients(CommandLine line, boolean send) {
		StringBuilder recipients = new StringBuilder();
		if (line.hasOption(DBToolOptions.EMAIL_RECIPIENTS_OPT)) {
			if (recipients.length() > 0) {
				recipients.append(DEFAULT_EMAIL_ADDRESS_SEPARATOR);
			}
			recipients.append(line.getOptionValue(DBToolOptions.EMAIL_RECIPIENTS_OPT));
		}
		if (line.hasOption(DBToolOptions.EMAIL_RECIPIENTS_IF_COUNT_GT_ZERO_OPT) && send) {
			if (recipients.length() > 0) {
				recipients.append(DEFAULT_EMAIL_ADDRESS_SEPARATOR);
			}
			recipients.append(line.getOptionValue(DBToolOptions.EMAIL_RECIPIENTS_IF_COUNT_GT_ZERO_OPT));
		}
		return recipients;
	}

	private Date start;
	private StringBuilder output;
	private ArrayList<EmailAttachment> attachments;
	private JavaMailSender mailSender;

	public JobOutput() {
		reset();
	}

	public void addEmailAttachment(byte[] data, MimeTypeVO contentType, String fileName) {
		addEmailAttachment(data, contentType.getMimeType(), fileName);
	}

	public void addEmailAttachment(byte[] data, String mimeType, String fileName) {
		attachments.add(new EmailAttachment(data, mimeType, fileName));
		if (this.job != null) {
			this.job.setDatas(data);
			this.job.setFileName(fileName);
			this.job.setMimeType(mimeType);
		}
	}

	public void addEmailCsvAttachment(byte[] data) {
		addEmailAttachment(data, ExecUtil.CSV_MIMETYPE_STRING, getJobFileName(ExecUtil.CSV_FILENAME_EXTENSION));
	}

	public void addEmailXlsAttachment(byte[] data) {
		addEmailAttachment(data, CoreUtil.EXCEL_MIMETYPE_STRING, getJobFileName(CoreUtil.EXCEL_FILENAME_EXTENSION));
	}

	private String getJobFileName(String extension) {
		if (job != null) {
			return MessageFormat.format(JOB_FILE_NAME_FORMAT, Long.toString(job.getId()), extension);
		}
		return null;
	}

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

	public String getOutput() {
		return output.toString();
	}

	public Date getStart() {
		return start;
	}

	private void prepareEmail(MimeMessage mimeMessage, String subject, String emailAddresses) throws Exception {
		// int toCount = 0;
		if (!CommonUtil.isEmptyString(emailAddresses)) {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, getEmailEncoding());
			mimeMessageHelper.setSubject(subject);
			mimeMessageHelper.setText(output.toString());
			mimeMessageHelper.setFrom(Settings.getEmailExecFromAddress(), Settings.getEmailExecFromName());
			String[] addresses = emailAddressSeparatorRegexp.split(emailAddresses, -1);
			for (int i = 0; i < addresses.length; i++) {
				if (!CommonUtil.isEmptyString(addresses[i])) {
					mimeMessageHelper.addTo(addresses[i].trim());
					// toCount++;
				}
			}
		}
		// return toCount;
	}

	public void printExecutionTime(boolean error) {
		println(error ? "ERROR OCCURED" : "done" + " - execution time: " + ((new Date()).getTime() - start.getTime()) / 1000 + " seconds", true);
	}

	public void println(String line) {
		println(line, ALWAYS_PRINT_TIMESTAMP);
	}

	public void println(String line, boolean withTimestamp) {
		if (output.length() > 0) {
			output.append("\n");
		}
		String lineFormatted;
		if (withTimestamp) {
			lineFormatted = MessageFormat.format(LINE_FORMAT_WITH_TIMESTAMP,
					CommonUtil.formatDate(new Date(), ExecSettings.getString(ExecSettingCodes.DATETIME_PATTERN, ExecDefaultSettings.DATETIME_PATTERN)), line);
		} else {
			lineFormatted = MessageFormat.format(LINE_FORMAT, line);
		}
		output.append(lineFormatted);
		System.out.println(lineFormatted);
		//updateJob();
	}

	public void updateJob() {
		if (this.job != null) {
			try {
				JobOutVO job = jobService.updateJob(auth, this.job);
				this.job.setVersion(job.getVersion());
			} catch (AuthenticationException | AuthorisationException | ServiceException e) {
			}
		}
	}

	public void flushJob() {
		if (this.job != null) {
			this.job.setJobOutput(output.toString());
		}
		updateJob();
	}

	public void flushJob(JobStatus status) {
		if (this.job != null) {
			this.job.setStatus(status);
		}
		flushJob();
	}

	public void setJobId(Long jobId) throws Exception {
		this.job = null;
		this.jobFile = null;
		if (jobId != null) {
			JobOutVO job = jobService.getJob(auth, jobId);
			this.job = new JobUpdateVO();
			this.job.setId(jobId);
			this.job.setVersion(job.getVersion());
			if (job.getHasFile()) {
				jobFile = jobService.getJobFile(auth, jobId);
				this.job.setDatas(jobFile.getDatas());
				this.job.setFileName(jobFile.getFileName());
				this.job.setMimeType(jobFile.getContentType().getMimeType());
			}
			this.job.setStatus(JobStatus.PROCESSING);
			this.job.setJobOutput(null);
			updateJob();
		}
	}

	public JobFileVO getJobFile() {
		return jobFile;
	}

	public void setAuthentication(AuthenticationVO auth) {
		this.auth = auth;
	}

	public void printPrelude(Option task) {
		String applicationName = Settings.getString(SettingCodes.APPLICATION_NAME, Bundle.SETTINGS, null);
		String applicationVersion = Settings.getString(SettingCodes.APPLICATION_VERSION, Bundle.SETTINGS, null);
		String instanceName = Settings.getInstanceName();
		if (!CommonUtil.isEmptyString(applicationName)) {
			println(applicationName);
		}
		if (!CommonUtil.isEmptyString(applicationVersion)) {
			println("version: " + applicationVersion);
		}
		if (!CommonUtil.isEmptyString(instanceName)) {
			println("instance: " + instanceName);
		}
		println("jvm: " + System.getProperty("java.version"));
		//Iterator<Entry<String, String>> it = System.getenv().entrySet().iterator();
		//while (it.hasNext()) {
		//	Entry<String, String> envVar = it.next();
		//	println(envVar.getKey() + "=" + envVar.getValue());
		//}
		if (task != null && !CommonUtil.isEmptyString(task.getDescription())) {
			println(task.getDescription());
		}
	}

	public void printStarting() {
		println("starting...", true);
	}

	public void reset() {
		attachments = new ArrayList<EmailAttachment>();
		start = new Date();
		output = new StringBuilder();
		job = null;
		jobFile = null;
		auth = null;
	}

	public int send(String subjectFormat, Option task, CommandLine line, boolean send) throws Exception {
		if (line != null) {
			StringBuilder recipients = getEmailRecipients(line, send);
			if (recipients.length() > 0) {
				MimeMessage mimeMessage = mailSender.createMimeMessage();
				prepareEmail(mimeMessage, MessageFormat.format(subjectFormat, task.getDescription()), recipients.toString());
				Address[] to = mimeMessage.getRecipients(RecipientType.TO);
				int toCount = to != null ? to.length : 0;
				if (toCount > 0) {
					if (attachments.size() > 0) {
						Multipart multipart = new MimeMultipart();
						Iterator<EmailAttachment> it = attachments.iterator();
						MimeBodyPart messageBodyPart = new MimeBodyPart();
						messageBodyPart.setText(output.toString(), getEmailEncoding());
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
					mailSender.send(mimeMessage);
				}
				return toCount;
			}
		}
		return 0;
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void setOutput(String output) {
		this.output = new StringBuilder(output);
	}

	public void setStart(Date start) {
		this.start = (start == null ? new Date() : start);
	}
}
