package org.phoenixctms.ctsms.email;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Pattern;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.phoenixctms.ctsms.domain.BankAccountDao;
import org.phoenixctms.ctsms.domain.ContactDetailType;
import org.phoenixctms.ctsms.domain.CourseParticipationStatusEntry;
import org.phoenixctms.ctsms.domain.DiagnosisDao;
import org.phoenixctms.ctsms.domain.ECRFDao;
import org.phoenixctms.ctsms.domain.ECRFFieldDao;
import org.phoenixctms.ctsms.domain.ECRFFieldStatusEntryDao;
import org.phoenixctms.ctsms.domain.ECRFFieldStatusTypeDao;
import org.phoenixctms.ctsms.domain.ECRFFieldValueDao;
import org.phoenixctms.ctsms.domain.ECRFStatusEntryDao;
import org.phoenixctms.ctsms.domain.File;
import org.phoenixctms.ctsms.domain.FileDao;
import org.phoenixctms.ctsms.domain.InputFieldDao;
import org.phoenixctms.ctsms.domain.InputFieldSelectionSetValueDao;
import org.phoenixctms.ctsms.domain.InquiryDao;
import org.phoenixctms.ctsms.domain.InquiryValueDao;
import org.phoenixctms.ctsms.domain.InventoryBookingDao;
import org.phoenixctms.ctsms.domain.JournalEntryDao;
import org.phoenixctms.ctsms.domain.MassMail;
import org.phoenixctms.ctsms.domain.MassMailRecipient;
import org.phoenixctms.ctsms.domain.MassMailRecipientDao;
import org.phoenixctms.ctsms.domain.MedicationDao;
import org.phoenixctms.ctsms.domain.MimeType;
import org.phoenixctms.ctsms.domain.MimeTypeDao;
import org.phoenixctms.ctsms.domain.MoneyTransferDao;
import org.phoenixctms.ctsms.domain.Proband;
import org.phoenixctms.ctsms.domain.ProbandAddressDao;
import org.phoenixctms.ctsms.domain.ProbandContactDetailValue;
import org.phoenixctms.ctsms.domain.ProbandContactDetailValueDao;
import org.phoenixctms.ctsms.domain.ProbandDao;
import org.phoenixctms.ctsms.domain.ProbandListEntry;
import org.phoenixctms.ctsms.domain.ProbandListEntryDao;
import org.phoenixctms.ctsms.domain.ProbandListEntryTagDao;
import org.phoenixctms.ctsms.domain.ProbandListEntryTagValueDao;
import org.phoenixctms.ctsms.domain.ProbandTagValueDao;
import org.phoenixctms.ctsms.domain.ProcedureDao;
import org.phoenixctms.ctsms.domain.SignatureDao;
import org.phoenixctms.ctsms.domain.Staff;
import org.phoenixctms.ctsms.domain.StaffContactDetailValue;
import org.phoenixctms.ctsms.domain.StaffContactDetailValueDao;
import org.phoenixctms.ctsms.domain.TeamMember;
import org.phoenixctms.ctsms.domain.Trial;
import org.phoenixctms.ctsms.domain.TrialDao;
import org.phoenixctms.ctsms.domain.TrialTagValueDao;
import org.phoenixctms.ctsms.domain.User;
import org.phoenixctms.ctsms.domain.UserDao;
import org.phoenixctms.ctsms.enumeration.FileModule;
import org.phoenixctms.ctsms.enumeration.PaymentMethod;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.pdf.PDFImprinter;
import org.phoenixctms.ctsms.pdf.ProbandLetterPDFPainter;
import org.phoenixctms.ctsms.security.CipherStream;
import org.phoenixctms.ctsms.security.CryptoUtil;
import org.phoenixctms.ctsms.util.AssociationPath;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.DefaultMessages;
import org.phoenixctms.ctsms.util.DefaultSettings;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.MessageCodes;
import org.phoenixctms.ctsms.util.MethodTransfilter;
import org.phoenixctms.ctsms.util.ServiceExceptionCodes;
import org.phoenixctms.ctsms.util.ServiceUtil;
import org.phoenixctms.ctsms.util.SettingCodes;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.vo.ECRFPDFVO;
import org.phoenixctms.ctsms.vo.EmailAttachmentVO;
import org.phoenixctms.ctsms.vo.FileContentOutVO;
import org.phoenixctms.ctsms.vo.InquiriesPDFVO;
import org.phoenixctms.ctsms.vo.MassMailOutVO;
import org.phoenixctms.ctsms.vo.MassMailRecipientOutVO;
import org.phoenixctms.ctsms.vo.ProbandContactDetailValueOutVO;
import org.phoenixctms.ctsms.vo.ProbandLetterPDFVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagsPDFVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.vo.ReimbursementsPDFVO;
import org.phoenixctms.ctsms.vo.StaffContactDetailValueOutVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.springframework.mail.javamail.MimeMessageHelper;

public class MassMailEmailSender extends EmailSender<MassMail, MassMailRecipient> {


	private final static boolean HTML = true;

	private final static PaymentMethod PAYOFF_PAYMENT_METHOD = null;
	private final static Boolean PAYOFF_PAID = false;
	private final static Boolean INQUIRIES_ACTIVE = null;
	private final static Boolean INQUIRIES_ACTIVE_SIGNUP = null;
	private final static boolean INQUIRIES_BLANK = false;
	private final static boolean PROBAND_LIST_ENTRY_TAGS_BLANK = false;
	private final static boolean ECRFS_BLANK = false;
	private final static Pattern MAIL_USER_DOMAIN_REGEXP = Pattern.compile("@");
	private static final MethodTransfilter RESOLVE_MAIL_ADDRESS_TRANSFILTER = MethodTransfilter.getEntityMethodTransfilter(true);

	public static String getBeaconImageHtmlElement(String beacon) {
		return MessageFormat.format(ServiceUtil.BEACON_IMAGE_HTML_ELEMENT, Settings.getHttpBaseUrl(), CommonUtil.BEACON_PATH, beacon, CommonUtil.GIF_FILENAME_EXTENSION);
	}
	// public final static boolean STRICT_EMAIL_ADRESSES = false;
	private MassMailRecipientDao massMailRecipientDao;
	private TrialDao trialDao;
	private ECRFDao eCRFDao;
	private ECRFFieldDao eCRFFieldDao;
	private ECRFFieldValueDao eCRFFieldValueDao;
	private ECRFStatusEntryDao eCRFStatusEntryDao;
	private ECRFFieldStatusEntryDao eCRFFieldStatusEntryDao;
	private ECRFFieldStatusTypeDao eCRFFieldStatusTypeDao;
	private ProbandDao probandDao;
	private SignatureDao signatureDao;
	private MoneyTransferDao moneyTransferDao;
	private BankAccountDao bankAccountDao;
	private JournalEntryDao journalEntryDao;
	private InquiryDao inquiryDao;
	private ProbandAddressDao probandAddressDao;
	private InquiryValueDao inquiryValueDao;
	private UserDao userDao;
	private InputFieldDao inputFieldDao;
	private InputFieldSelectionSetValueDao inputFieldSelectionSetValueDao;
	private FileDao fileDao;
	private ProbandContactDetailValueDao probandContactDetailValueDao;
	private TrialTagValueDao trialTagValueDao;
	private ProbandListEntryDao probandListEntryDao;
	private ProbandListEntryTagDao probandListEntryTagDao;

	private ProbandListEntryTagValueDao probandListEntryTagValueDao;

	private InventoryBookingDao inventoryBookingDao;
	private boolean strictEmailAddresses;

	private VelocityEngine velocityEngine;
	private ProbandTagValueDao probandTagValueDao;
	private DiagnosisDao diagnosisDao;

	private ProcedureDao procedureDao;

	private MedicationDao medicationDao;
	private StaffContactDetailValueDao staffContactDetailValueDao;
	private MimeTypeDao mimeTypeDao;

	@Override
	protected void addAttachments(MassMail massMail, MassMailRecipient recipient, ArrayList<EmailAttachmentVO> attachments) throws Exception {
		if (massMail.isAttachMassMailFiles()) {
			Iterator<File> filesIt = fileDao.findFiles(FileModule.MASS_MAIL_DOCUMENT, massMail.getId(), massMail.getMassMailFilesLogicalPath(), true, null, null, null, null, null)
					.iterator();
			if (!filesIt.hasNext()) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.MASS_MAIL_NO_MASS_MAIL_FILES_ATTACHMENTS);
			}
			while (filesIt.hasNext()) {
				attachments.add(fileContentOutVOtoEmailAttachentVO(fileDao.toFileContentOutVO(filesIt.next())));
			}
		}
		if (massMail.isAttachTrialFiles()) {
			Iterator<File> filesIt = fileDao
					.findFiles(FileModule.TRIAL_DOCUMENT, massMail.getTrial().getId(), massMail.getTrialFilesLogicalPath(), true, null, null, null, null, null)
					.iterator();
			if (!filesIt.hasNext()) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.MASS_MAIL_NO_TRIAL_FILES_ATTACHMENTS);
			}
			while (filesIt.hasNext()) {
				attachments.add(fileContentOutVOtoEmailAttachentVO(fileDao.toFileContentOutVO(filesIt.next())));
			}
		}
		if (recipient.getProband() != null) {
			if (massMail.isAttachProbandFiles()) {
				Iterator<File> filesIt = fileDao
						.findFiles(FileModule.PROBAND_DOCUMENT, recipient.getProband().getId(), massMail.getProbandFilesLogicalPath(), true, null, null, null, null, null)
						.iterator();
				if (!filesIt.hasNext()) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.MASS_MAIL_NO_PROBAND_FILES_ATTACHMENTS);
				}
				while (filesIt.hasNext()) {
					attachments.add(fileContentOutVOtoEmailAttachentVO(fileDao.toFileContentOutVO(filesIt.next())));
				}
			}
			if (massMail.isAttachInquiries()) {
				MassMailRecipientOutVO recipientVO = massMailRecipientDao.toMassMailRecipientOutVO(recipient);
				Collection<Trial> trials = new ArrayList<Trial>();
				if (massMail.getTrial() != null) {
					trials.add(massMail.getTrial());
				} else {
					// trials = trialDao.findBySignup(massMail.getDepartment().getId(), true, null);
					Iterator<Trial> trialIt = trialDao.findByInquiryValuesProbandSorted(null, recipient.getProband().getId(), INQUIRIES_ACTIVE, INQUIRIES_ACTIVE_SIGNUP).iterator();
					while (trialIt.hasNext()) {
						Trial trial = trialIt.next();
						if (inquiryValueDao.getCount(trial.getId(), INQUIRIES_ACTIVE, INQUIRIES_ACTIVE_SIGNUP, recipient.getProband().getId()) > 0) {
							trials.add(trial);
						}
					}
				}
				// if (trials.size() > 0) {
				InquiriesPDFVO inquiriesPDF = ServiceUtil.renderInquiries(recipient.getProband(), recipientVO.getProband(),
						trials, INQUIRIES_ACTIVE,
						INQUIRIES_ACTIVE_SIGNUP, INQUIRIES_BLANK, trialDao, inquiryDao, inquiryValueDao, inputFieldDao, inputFieldSelectionSetValueDao,
						userDao);
				// InquiriesPDFVO inquiriesPDF = ServiceUtil.renderInquiriesSignup(recipient.getProband(), recipientVO.getProband(),
				// trials,
				// INQUIRIES_ACTIVE_SIGNUP, trialDao, inquiryDao, inquiryValueDao, inputFieldDao, inputFieldSelectionSetValueDao,
				// userDao);
				if (inquiriesPDF.getTrials().size() > 0) {
					EmailAttachmentVO attachment = new EmailAttachmentVO();
					attachment.setDatas(inquiriesPDF.getDocumentDatas());
					attachment.setFileSize((long) attachment.getDatas().length);
					attachment.setFileName(inquiriesPDF.getFileName());
					attachment.setContentType(inquiriesPDF.getContentType());
					attachments.add(attachment);
				} else {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.MASS_MAIL_NO_INQUIRIES_ATTACHMENT);
				}
				// }
			}
			if (massMail.isAttachProbandListEntryTags()) {
				// MassMailRecipientOutVO recipientVO = massMailRecipientDao.toMassMailRecipientOutVO(recipient);
				ProbandListEntryTagsPDFVO probandListEntryTagsPDF = ServiceUtil.renderProbandListEntryTags(recipient.getProband(), massMail.getTrial(),
						PROBAND_LIST_ENTRY_TAGS_BLANK,
						probandListEntryDao, probandListEntryTagDao, probandListEntryTagValueDao,
						inputFieldDao, inputFieldSelectionSetValueDao, userDao);
				if (probandListEntryTagsPDF.getListEntries().size() > 0) {
					EmailAttachmentVO attachment = new EmailAttachmentVO();
					attachment.setDatas(probandListEntryTagsPDF.getDocumentDatas());
					attachment.setFileSize((long) attachment.getDatas().length);
					attachment.setFileName(probandListEntryTagsPDF.getFileName());
					attachment.setContentType(probandListEntryTagsPDF.getContentType());
					attachments.add(attachment);
				} else {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.MASS_MAIL_NO_PROBAND_LIST_ENTRY_TAGS_ATTACHMENT);
				}
			}
			if (massMail.isAttachEcrfs()) {
				Collection<Trial> trials;
				if (massMail.getTrial() != null) {
					trials = new ArrayList<Trial>();
					trials.add(massMail.getTrial());
				} else {
					trials = trialDao.findByParticipatingProbandSorted(recipient.getProband().getId());
				}
				Iterator<Trial> trialsIt = trials.iterator();
				int count = 0;
				while (trialsIt.hasNext()) {
					Trial trial = trialsIt.next();
					ProbandListEntry listEntry = probandListEntryDao.findByTrialProband(trial.getId(), recipient.getProband().getId());
					ECRFPDFVO ecrfsPDF = ServiceUtil.renderEcrfs(listEntry, trial, null, ECRFS_BLANK, null,
							probandListEntryDao, eCRFDao, eCRFFieldDao, eCRFFieldValueDao,
							inputFieldDao, inputFieldSelectionSetValueDao, eCRFStatusEntryDao, eCRFFieldStatusEntryDao, eCRFFieldStatusTypeDao,
							probandListEntryTagDao, probandListEntryTagValueDao, signatureDao, userDao);
					if (ecrfsPDF.getStatusEntries().size() > 0) {
						EmailAttachmentVO attachment = new EmailAttachmentVO();
						attachment.setDatas(ecrfsPDF.getDocumentDatas());
						attachment.setFileSize((long) attachment.getDatas().length);
						attachment.setFileName(ecrfsPDF.getFileName());
						attachment.setContentType(ecrfsPDF.getContentType());
						attachments.add(attachment);
						count++;
					}
				}
				if (count == 0) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.MASS_MAIL_NO_ECRFS_ATTACHMENTS);
				}
			}
			if (massMail.isAttachProbandLetter()) {
				MassMailRecipientOutVO recipientVO = massMailRecipientDao.toMassMailRecipientOutVO(recipient);
				ArrayList<ProbandOutVO> probandVOs = new ArrayList<ProbandOutVO>();
				probandVOs.add(recipientVO.getProband());
				ProbandLetterPDFPainter painter = ServiceUtil.createProbandLetterPDFPainter(probandVOs, probandAddressDao);
				User user = CoreUtil.getUser();
				painter.getPdfVO().setRequestingUser(userDao.toUserOutVO(user));
				(new PDFImprinter(painter, painter)).render();
				ProbandLetterPDFVO probandLetterPDF = painter.getPdfVO();
				if (probandLetterPDF.getProbands().size() > 0) {
					EmailAttachmentVO attachment = new EmailAttachmentVO();
					attachment.setDatas(probandLetterPDF.getDocumentDatas());
					attachment.setFileSize((long) attachment.getDatas().length);
					attachment.setFileName(probandLetterPDF.getFileName());
					attachment.setContentType(probandLetterPDF.getContentType());
					attachments.add(attachment);
				} else {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.MASS_MAIL_NO_PROBAND_LETTER_ATTACHMENT);
				}
			}
			if (massMail.isAttachReimbursementsPdf()) {
				ReimbursementsPDFVO reimbursementsPDF = ServiceUtil.renderReimbursements(recipient.getProband(), massMail.getTrial(), PAYOFF_PAYMENT_METHOD, PAYOFF_PAID,
						probandDao, trialDao, moneyTransferDao, trialTagValueDao,
						bankAccountDao, probandAddressDao, userDao);
				if (reimbursementsPDF.getProbands().size() > 0) {
					EmailAttachmentVO attachment = new EmailAttachmentVO();
					attachment.setDatas(reimbursementsPDF.getDocumentDatas());
					attachment.setFileSize((long) attachment.getDatas().length);
					attachment.setFileName(reimbursementsPDF.getFileName());
					attachment.setContentType(reimbursementsPDF.getContentType());
					attachments.add(attachment);
				} else {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.MASS_MAIL_NO_REIMBURSEMENTS_PDF_ATTACHMENT);
				}
			}
		}
	}

	private  EmailAttachmentVO fileContentOutVOtoEmailAttachentVO(FileContentOutVO file) throws ServiceException {

		if (file.isDecrypted()) {
			EmailAttachmentVO attachment  = new EmailAttachmentVO();
			attachment.setDatas(file.getDatas());
			attachment.setFileSize((long) attachment.getDatas().length);
			attachment.setFileName(file.getFileName());
			Iterator<MimeType> it = mimeTypeDao.findByMimeTypeModule(file.getContentType().getMimeType(), FileModule.MASS_MAIL_DOCUMENT).iterator();
			if (it.hasNext()) {
				attachment.setContentType(mimeTypeDao.toMimeTypeVO(it.next()));
			} else {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.MASS_MAIL_ATTACHMENT_MIME_TYPE_UNKNOWN, file.getContentType().getMimeType());
			}
			return attachment;

		} else {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DECRYPT_FILE);
		}


	}
	@Override
	protected boolean isHtml() {
		return HTML;
	}

	public boolean isStrictEmailAddresses() {
		return strictEmailAddresses;
	}

	@Override
	protected MimeMessage loadMessage(MassMail entity, MassMailRecipient recipient, Date now) throws Exception {
		Long maxAgeSeconds = Settings.getLongNullable(SettingCodes.MASS_MAIL_MIME_MESSAGE_MAX_AGE_SECONDS, Bundle.SETTINGS, DefaultSettings.MASS_MAIL_MIME_MESSAGE_MAX_AGE_SECONDS);
		if (recipient.getMimeMessageSize() > 0l && recipient.getMimeMessageTimestamp() != null
				&& (maxAgeSeconds == null || now.compareTo(new Date(recipient.getMimeMessageTimestamp().getTime() + maxAgeSeconds * 1000l)) < 0)) {
			InputStream stream = null;
			try {
				if (!CoreUtil.isPassDecryption()) {
					throw new Exception();
				}
				stream = CryptoUtil.createDecryptionStream(recipient.getMimeMessageDataIv(), new ByteArrayInputStream(recipient.getEncryptedMimeMessageData()));
				return mailSender.createMimeMessage(stream);
				// } catch (MessagingException e) {
				// throw new RuntimeException(e);
				// } catch (Exception e) {
				// throw new Exception("xxx");
			} finally {
				if (stream != null) {
					try {
						stream.close();
					} catch (IOException e1) {
					}
				}
			}
		}
		return null;
	}

	@Override
	protected void prepareMessage(MimeMessage mimeMessage, MassMail massMail,
			MassMailRecipient recipient, StringBuilder text, Date now) throws Exception {
		if (!Settings.getBoolean(SettingCodes.SEND_MASS_MAILS, Bundle.SETTINGS, DefaultSettings.SEND_MASS_MAILS)) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.SENDING_MASS_MAILS_DISABLED);
		}
		// Timestamp now = new Timestamp(System.currentTimeMillis());
		// int toCount = 0;
		MassMailRecipientOutVO recipientVO = massMailRecipientDao.toMassMailRecipientOutVO(recipient);
		ProbandOutVO probandVO = recipientVO.getProband();

		if (probandVO != null && !probandVO.getDeferredDelete()) {
			if (!probandVO.isDecrypted() ) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DECRYPT_PROBAND);
			}
			ServiceUtil.checkProbandLocked(recipient.getProband());

			MassMailOutVO massMailVO = recipientVO.getMassMail();
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, getEncoding());
			if (!CommonUtil.isEmptyString(massMail.getFromName())) {
				mimeMessageHelper.setFrom(resolveMailAddress(recipient, massMail.getFromAddress(), true), massMail.getFromName());
			} else {
				mimeMessageHelper.setFrom(resolveMailAddress(recipient, massMail.getFromAddress(), true));
			}
			if (!CommonUtil.isEmptyString(massMail.getReplyToName())) {
				mimeMessageHelper.setReplyTo(resolveMailAddress(recipient, massMail.getReplyToAddress(), true), massMail.getReplyToName());
			} else {
				mimeMessageHelper.setReplyTo(resolveMailAddress(recipient, massMail.getReplyToAddress(), true));
			}
			Locales locale = L10nUtil.getLocales(massMailVO.getLocale());
			String subject = ServiceUtil.getMassMailSubject(massMailVO.getSubjectFormat(), locale, massMailVO.getMaleSalutation(), massMailVO.getFemaleSalutation(), probandVO,
					massMailVO.getTrial(), // recipientVO.getTrial() != null ? recipientVO.getTrial() : massMailVO.getTrial(),
					massMailVO.getProbandListStatus());
			if (!CommonUtil.isEmptyString(subject)) {
				mimeMessageHelper.setSubject(subject);
			} else {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.EMAIL_EMPTY_SUBJECT);
			}
			String message = ServiceUtil.getMassMailMessage(velocityEngine, massMailVO, probandVO, recipientVO.getBeacon(), now, null, trialTagValueDao, probandListEntryDao, // recipientVO.getTrial()
					probandListEntryTagValueDao, inventoryBookingDao,
					probandTagValueDao,
					probandContactDetailValueDao,
					probandAddressDao,
					diagnosisDao,
					procedureDao,
					medicationDao,
					bankAccountDao,
					journalEntryDao);
			if (!CommonUtil.isEmptyString(message)) {
				text.append(message);
				if (massMailVO.getUseBeacon() && isHtml()) {
					text.append(getBeaconImageHtmlElement(recipientVO.getBeacon()));
				}
				mimeMessageHelper.setText(text.toString(), isHtml());
			} else {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.EMAIL_EMPTY_MESSAGE);
			}
			if (massMailVO.getProbandTo()) {
				Iterator<ProbandContactDetailValue> contactsIt = recipient.getProband().getContactDetailValues().iterator();
				while (contactsIt.hasNext()) {
					ProbandContactDetailValue contact = contactsIt.next();
					ContactDetailType contactType;
					if (!contact.isNa() && contact.isNotify() && (contactType = contact.getType()).isEmail()) {
						ProbandContactDetailValueOutVO contactVO = probandContactDetailValueDao.toProbandContactDetailValueOutVO(contact);
						if (contactVO.isDecrypted()) {
							mimeMessageHelper.addTo(contactVO.getValue(),
									MessageFormat.format(EMAIL_TO_PERSONAL_NAME, probandVO.getName(), L10nUtil.getContactDetailTypeName(locale, contactType.getNameL10nKey())));
							// toCount++;
						} else {
							throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DECRYPT_PROBAND_CONTACT_DETAIL_VALUE);
						}
					}
				}
			}
			StaffOutVO physicianVO = recipientVO.getProband().getPhysician();
			if (massMailVO.getPhysicianTo() && physicianVO != null) {
				Iterator<StaffContactDetailValue> contactsIt = recipient.getProband().getPhysician().getContactDetailValues().iterator();
				while (contactsIt.hasNext()) {
					StaffContactDetailValue contact = contactsIt.next();
					ContactDetailType contactType;
					if (!contact.isNa() && contact.isNotify() && (contactType = contact.getType()).isEmail()) {
						mimeMessageHelper.addTo(contact.getValue(),
								MessageFormat.format(EMAIL_TO_PERSONAL_NAME, physicianVO.getName(), L10nUtil.getContactDetailTypeName(locale, contactType.getNameL10nKey())));
						// toCount++;
					}
				}
			}
			if (massMailVO.getTrialTeamTo() && massMail.getTrial() != null) {
				Iterator<TeamMember> membersIt = massMail.getTrial().getMembers().iterator();
				while (membersIt.hasNext()) {
					TeamMember teamMember = membersIt.next();
					if (teamMember.isNotifyOther()) {
						Iterator<StaffContactDetailValue> contactsIt = teamMember.getStaff().getContactDetailValues().iterator();
						while (contactsIt.hasNext()) {
							StaffContactDetailValue contact = contactsIt.next();
							ContactDetailType contactType;
							if (!contact.isNa() && contact.isNotify() && (contactType = contact.getType()).isEmail()) {
								StaffContactDetailValueOutVO contactVO = staffContactDetailValueDao.toStaffContactDetailValueOutVO(contact);
								mimeMessageHelper.addTo(contact.getValue(),
										MessageFormat.format(EMAIL_TO_PERSONAL_NAME, contactVO.getStaff().getName(),
												L10nUtil.getContactDetailTypeName(locale, contactType.getNameL10nKey())));
								// toCount++;
							}
						}
					}
				}
			}
			if (!CommonUtil.isEmptyString(massMail.getOtherTo())) {
				try {
					InternetAddress[] otherTo = InternetAddress.parse(massMail.getOtherTo(), strictEmailAddresses);
					if (otherTo != null) {
						for (int i = 0; i < otherTo.length; i++) {
							Iterator<InternetAddress> it = resolveMailAddresses(recipient, otherTo[i].getAddress(), false).iterator();
							if (it.hasNext()) {
								while (it.hasNext()) {
									mimeMessageHelper.addTo(it.next());
								}
							} else {
								mimeMessageHelper.addTo(otherTo[i]);
							}
						}
					}
				} catch (AddressException e) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.MASS_MAIL_INVALID_OTHER_TO, massMail.getOtherTo(), e.getMessage());
				}
			}
			if (!CommonUtil.isEmptyString(massMail.getCc())) {
				try {
					InternetAddress[] cc = InternetAddress.parse(massMail.getCc(), strictEmailAddresses);
					if (cc != null) {
						for (int i = 0; i < cc.length; i++) {
							Iterator<InternetAddress> it = resolveMailAddresses(recipient, cc[i].getAddress(), false).iterator();
							if (it.hasNext()) {
								while (it.hasNext()) {
									mimeMessageHelper.addCc(it.next());
								}
							} else {
								mimeMessageHelper.addCc(cc[i]);
							}
						}
					}
				} catch (AddressException e) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.MASS_MAIL_INVALID_CC, massMail.getCc(), e.getMessage());
				}
			}
			if (!CommonUtil.isEmptyString(massMail.getBcc())) {
				try {
					InternetAddress[] bcc = InternetAddress.parse(massMail.getBcc(), strictEmailAddresses);
					if (bcc != null) {
						for (int i = 0; i < bcc.length; i++) {
							Iterator<InternetAddress> it = resolveMailAddresses(recipient, bcc[i].getAddress(), false).iterator();
							if (it.hasNext()) {
								while (it.hasNext()) {
									mimeMessageHelper.addBcc(it.next());
								}
							} else {
								mimeMessageHelper.addBcc(bcc[i]);
							}
						}
					}
				} catch (AddressException e) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.MASS_MAIL_INVALID_BCC, massMail.getBcc(), e.getMessage());
				}
			}

		} else {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_DELETED_OR_MARKED_FOR_DELETION);
		}
		// return toCount;
	}

	private String resolveMailAddress(MassMailRecipient recipient, String address, boolean reply) throws Exception {
		ArrayList<InternetAddress> result = resolveMailAddresses(recipient, address, reply);
		if (result.size() > 0) {
			return result.iterator().next().getAddress();
		}
		return address;
	}

	private ArrayList<InternetAddress> resolveMailAddresses(MassMailRecipient recipient, String address, boolean reply) throws Exception {
		String resolveMailAddressDomainName = Settings.getString(SettingCodes.RESOLVE_MAIL_ADDRESS_DOMAIN_NAME, Bundle.SETTINGS, DefaultSettings.RESOLVE_MAIL_ADDRESS_DOMAIN_NAME);
		ArrayList<InternetAddress> result = new ArrayList<InternetAddress>();
		if (!CommonUtil.isEmptyString(resolveMailAddressDomainName) && !CommonUtil.isEmptyString(address)) {
			String[] addressParts = MAIL_USER_DOMAIN_REGEXP.split(address, 2);
			if (addressParts != null && addressParts.length == 2 && resolveMailAddressDomainName.equals(addressParts[1])) {
				User user = null;
				try {
					user = (User) userDao.searchUniqueName(UserDao.TRANSFORM_NONE, addressParts[0]);
				} catch (Throwable t) {
				}
				Locales locale = null;
				if (user != null) {
					if (recipient != null) {
						locale = L10nUtil.getLocales(recipient.getMassMail().getLocale());
					} else {
						locale = L10nUtil.getLocales(user.getLocale());
					}
					if (user.getIdentity() != null) {
						Iterator<StaffContactDetailValue> contactsIt = user.getIdentity().getContactDetailValues().iterator();
						while (contactsIt.hasNext()) {
							StaffContactDetailValue contact = contactsIt.next();
							ContactDetailType contactType;
							if (!contact.isNa() && (contactType = contact.getType()).isEmail() &&
									(reply ? contactType.isBusiness() : contact.isNotify())) {
								StaffContactDetailValueOutVO contactVO = staffContactDetailValueDao.toStaffContactDetailValueOutVO(contact);
								result.add(new InternetAddress(contact.getValue(),
										MessageFormat.format(EMAIL_TO_PERSONAL_NAME, contactVO.getStaff().getName(),
												L10nUtil.getContactDetailTypeName(locale, contactType.getNameL10nKey()))));
							}
						}
					}
				} else if (recipient != null) {
					locale = L10nUtil.getLocales(recipient.getMassMail().getLocale());
					AssociationPath getter = new AssociationPath(addressParts[0]);
					Object userProbandStaff = null;
					try {
						userProbandStaff = getter.invoke(recipient, null, RESOLVE_MAIL_ADDRESS_TRANSFILTER, false);
					} catch (NoSuchMethodException e) {
						throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.UNSUPPORTED_MAIL_RECIPIENT_PROPERTY,
								DefaultMessages.UNSUPPORTED_MAIL_RECIPIENT_ENTITY,
								new Object[] { e.getMessage() }));
					}
					if (userProbandStaff != null) {
						Iterator it;
						if (userProbandStaff instanceof Collection) {
							it = ((Collection) userProbandStaff).iterator();
						} else {
							ArrayList<Object> userProbandStaffs = new ArrayList<Object>(1);
							userProbandStaffs.add(userProbandStaff);
							it = userProbandStaffs.iterator();
						}
						while (it.hasNext()) {
							userProbandStaff = it.next();
							Staff staff = null;
							Proband proband = null;
							if (userProbandStaff instanceof User) {
								staff = ((User) userProbandStaff).getIdentity();
							} else if (userProbandStaff instanceof TeamMember) {
								staff = ((TeamMember) userProbandStaff).getStaff();
							} else if (userProbandStaff instanceof CourseParticipationStatusEntry) {
								staff = ((CourseParticipationStatusEntry) userProbandStaff).getStaff();
							} else if (userProbandStaff instanceof ProbandListEntry) {
								proband = ((ProbandListEntry) userProbandStaff).getProband();
							} else if (userProbandStaff instanceof Proband) {
								proband = (Proband) userProbandStaff;
							} else if (userProbandStaff instanceof Staff) {
								staff = (Staff) userProbandStaff;
							} else {
								throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.UNSUPPORTED_MAIL_RECIPIENT_ENTITY,
										DefaultMessages.UNSUPPORTED_MAIL_RECIPIENT_ENTITY,
										new Object[] { userProbandStaff.getClass().getName() }));
							}
							if (staff != null) {
								Iterator<StaffContactDetailValue> contactsIt = staff.getContactDetailValues().iterator();
								while (contactsIt.hasNext()) {
									StaffContactDetailValue contact = contactsIt.next();
									ContactDetailType contactType;
									if (!contact.isNa() && (contactType = contact.getType()).isEmail() &&
											(reply ? contactType.isBusiness() : contact.isNotify())) {
										StaffContactDetailValueOutVO contactVO = staffContactDetailValueDao.toStaffContactDetailValueOutVO(contact);
										result.add(new InternetAddress(contact.getValue(),
												MessageFormat.format(EMAIL_TO_PERSONAL_NAME, contactVO.getStaff().getName(),
														L10nUtil.getContactDetailTypeName(locale, contactType.getNameL10nKey()))));
									}
								}
							}
							if (proband != null) {
								Iterator<ProbandContactDetailValue> contactsIt = ((Proband) userProbandStaff).getContactDetailValues().iterator();
								while (contactsIt.hasNext()) {
									ProbandContactDetailValue contact = contactsIt.next();
									ContactDetailType contactType;
									if (!contact.isNa() && (contactType = contact.getType()).isEmail() &&
											(reply ? contactType.isBusiness() : contact.isNotify())) {
										ProbandContactDetailValueOutVO contactVO = probandContactDetailValueDao.toProbandContactDetailValueOutVO(contact);
										if (contactVO.isDecrypted()) {
											result.add(new InternetAddress(contactVO.getValue(),
													MessageFormat.format(EMAIL_TO_PERSONAL_NAME, contactVO.getProband().getName(),
															L10nUtil.getContactDetailTypeName(locale, contactType.getNameL10nKey()))));
										} else {
											throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DECRYPT_PROBAND_CONTACT_DETAIL_VALUE);
										}
									}
								}
							}
						}
					}
				}

			}
		}
		return result;
	}


	public void setBankAccountDao(BankAccountDao bankAccountDao) {
		this.bankAccountDao = bankAccountDao;
	}

	public void setDiagnosisDao(DiagnosisDao diagnosisDao) {
		this.diagnosisDao = diagnosisDao;
	}

	public void seteCRFDao(ECRFDao eCRFDao) {
		this.eCRFDao = eCRFDao;
	}
	public void seteCRFFieldDao(ECRFFieldDao eCRFFieldDao) {
		this.eCRFFieldDao = eCRFFieldDao;
	}



	public void seteCRFFieldStatusEntryDao(ECRFFieldStatusEntryDao eCRFFieldStatusEntryDao) {
		this.eCRFFieldStatusEntryDao = eCRFFieldStatusEntryDao;
	}

	public void seteCRFFieldStatusTypeDao(ECRFFieldStatusTypeDao eCRFFieldStatusTypeDao) {
		this.eCRFFieldStatusTypeDao = eCRFFieldStatusTypeDao;
	}
	public void seteCRFFieldValueDao(ECRFFieldValueDao eCRFFieldValueDao) {
		this.eCRFFieldValueDao = eCRFFieldValueDao;
	}

	public void seteCRFStatusEntryDao(ECRFStatusEntryDao eCRFStatusEntryDao) {
		this.eCRFStatusEntryDao = eCRFStatusEntryDao;
	}

	public void setFileDao(FileDao fileDao) {
		this.fileDao = fileDao;
	}

	public void setInputFieldDao(InputFieldDao inputFieldDao) {
		this.inputFieldDao = inputFieldDao;
	}

	public void setInputFieldSelectionSetValueDao(InputFieldSelectionSetValueDao inputFieldSelectionSetValueDao) {
		this.inputFieldSelectionSetValueDao = inputFieldSelectionSetValueDao;
	}

	public void setInquiryDao(InquiryDao inquiryDao) {
		this.inquiryDao = inquiryDao;
	}

	public void setInquiryValueDao(InquiryValueDao inquiryValueDao) {
		this.inquiryValueDao = inquiryValueDao;
	}
	public void setInventoryBookingDao(InventoryBookingDao inventoryBookingDao) {
		this.inventoryBookingDao = inventoryBookingDao;
	}
	public void setJournalEntryDao(JournalEntryDao journalEntryDao) {
		this.journalEntryDao = journalEntryDao;
	}
	public void setmassMailRecipientDao(MassMailRecipientDao massMailRecipientDao) {
		this.massMailRecipientDao = massMailRecipientDao;
	}

	public void setMedicationDao(MedicationDao medicationDao) {
		this.medicationDao = medicationDao;
	}

	public void setMimeTypeDao(MimeTypeDao mimeTypeDao) {
		this.mimeTypeDao = mimeTypeDao;
	}

	public void setMoneyTransferDao(MoneyTransferDao moneyTransferDao) {
		this.moneyTransferDao = moneyTransferDao;
	}

	public void setProbandAddressDao(ProbandAddressDao probandAddressDao) {
		this.probandAddressDao = probandAddressDao;
	}


	public void setProbandContactDetailValueDao(ProbandContactDetailValueDao probandContactDetailValueDao) {
		this.probandContactDetailValueDao = probandContactDetailValueDao;
	}

	public void setProbandDao(ProbandDao probandDao) {
		this.probandDao = probandDao;
	}

	public void setProbandListEntryDao(ProbandListEntryDao probandListEntryDao) {
		this.probandListEntryDao = probandListEntryDao;
	}

	public void setProbandListEntryTagDao(ProbandListEntryTagDao probandListEntryTagDao) {
		this.probandListEntryTagDao = probandListEntryTagDao;
	}

	public void setProbandListEntryTagValueDao(ProbandListEntryTagValueDao probandListEntryTagValueDao) {
		this.probandListEntryTagValueDao = probandListEntryTagValueDao;
	}
	public void setProbandTagValueDao(ProbandTagValueDao probandTagValueDao) {
		this.probandTagValueDao = probandTagValueDao;
	}

	public void setProcedureDao(ProcedureDao procedureDao) {
		this.procedureDao = procedureDao;
	}

	public void setSignatureDao(SignatureDao signatureDao) {
		this.signatureDao = signatureDao;
	}

	public void setStaffContactDetailValueDao(StaffContactDetailValueDao staffContactDetailValueDao) {
		this.staffContactDetailValueDao = staffContactDetailValueDao;
	}

	public void setStrictEmailAddresses(boolean strictEmailAddresses) {
		this.strictEmailAddresses = strictEmailAddresses;
	}

	public void setTrialDao(TrialDao trialDao) {
		this.trialDao = trialDao;
	}

	public void setTrialTagValueDao(TrialTagValueDao trialTagValueDao) {
		this.trialTagValueDao = trialTagValueDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	@Override
	protected void storeMessage(MimeMessage mimeMessage, MassMail entity, MassMailRecipient recipient, Date now) throws Exception {
		// byte[] mimeMessageData = null;
		// byte[] mimeMessageIv = null;
		// try {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		mimeMessage.writeTo(buffer);
		recipient.setMimeMessageSize(buffer.size());
		buffer.reset();
		CipherStream cipherStream = CryptoUtil.createEncryptionStream(buffer);
		recipient.setMimeMessageDataIv(cipherStream.getIv());
		// recipient.setMimeMessageSize(mimeMessage.getSize());
		mimeMessage.writeTo(cipherStream);
		cipherStream.close();
		recipient.setEncryptedMimeMessageData(buffer.toByteArray());
		recipient.setMimeMessageTimestamp(CommonUtil.dateToTimestamp(now));
		// recipient.setMimeMessageSize(buffer.size());
		// } catch (Exception e) {
		// throw new RuntimeException(e);
		// }
		// recipient.setMimeMessageSize(buffer.size());
		// recipient.setEncryptedMimeMessageData(mimeMessageData);
		// recipient.setMimeMessageDataIv(mimeMessageIv);
		// CoreUtil.modifyVersion(entity, recipient., now, CoreUtil.getUser());
		// massMailRecipientDao.update(recipient);
	}
}
