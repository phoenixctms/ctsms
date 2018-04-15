// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 * TEMPLATE:    SpringServiceImpl.vsl in andromda-spring cartridge
 * MODEL CLASS: AndroMDAModel::ctsms::org.phoenixctms.ctsms::service::massmail::MassMailService
 * STEREOTYPE:  Service
 */
package org.phoenixctms.ctsms.service.massmail;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.velocity.app.VelocityEngine;
import org.hibernate.LockMode;
import org.phoenixctms.ctsms.domain.File;
import org.phoenixctms.ctsms.domain.FileDao;
import org.phoenixctms.ctsms.domain.JournalEntry;
import org.phoenixctms.ctsms.domain.JournalEntryDao;
import org.phoenixctms.ctsms.domain.MassMail;
import org.phoenixctms.ctsms.domain.MassMailDao;
import org.phoenixctms.ctsms.domain.MassMailRecipient;
import org.phoenixctms.ctsms.domain.MassMailRecipientDao;
import org.phoenixctms.ctsms.domain.MassMailStatusType;
import org.phoenixctms.ctsms.domain.MassMailStatusTypeDao;
import org.phoenixctms.ctsms.domain.MassMailType;
import org.phoenixctms.ctsms.domain.Proband;
import org.phoenixctms.ctsms.domain.ProbandListStatusTypeDao;
import org.phoenixctms.ctsms.domain.Trial;
import org.phoenixctms.ctsms.domain.TrialDao;
import org.phoenixctms.ctsms.domain.User;
import org.phoenixctms.ctsms.email.MassMailEmailSender;
import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.DefaultMessages;
import org.phoenixctms.ctsms.util.DefaultSettings;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.MessageCodes;
import org.phoenixctms.ctsms.util.ServiceExceptionCodes;
import org.phoenixctms.ctsms.util.ServiceUtil;
import org.phoenixctms.ctsms.util.SettingCodes;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.util.SystemMessageCodes;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.DepartmentVO;
import org.phoenixctms.ctsms.vo.EmailMessageVO;
import org.phoenixctms.ctsms.vo.MassMailInVO;
import org.phoenixctms.ctsms.vo.MassMailOutVO;
import org.phoenixctms.ctsms.vo.MassMailProgressVO;
import org.phoenixctms.ctsms.vo.MassMailRecipientInVO;
import org.phoenixctms.ctsms.vo.MassMailRecipientOutVO;
import org.phoenixctms.ctsms.vo.MassMailStatusTypeVO;
import org.phoenixctms.ctsms.vo.MassMailTypeVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.ProbandListStatusTypeVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;

/**
 * @see org.phoenixctms.ctsms.service.massmail.MassMailService
 */
public class MassMailServiceImpl
extends MassMailServiceBase
{



	// private final static String VELOCITY_LOG_TAG = "massmail";

	// private static JournalEntry logSystemMessage(Proband proband, MassMailOutVO massMailVO, Timestamp now, User user, String systemMessageCode, Object result, Object original,
	// JournalEntryDao journalEntryDao) throws Exception {
	// return journalEntryDao.addSystemMessage(proband, now, user, systemMessageCode, new Object[] { CommonUtil.massMailOutVOToString(massMailVO) },
	// new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, !CommonUtil.getUseJournalEncryption(JournalModule.PROBAND_JOURNAL, null)) });
	// }



	private static JournalEntry logSystemMessage(Trial trial, MassMailOutVO massMailVO, Timestamp now, User user, String systemMessageCode, Object result, Object original,
			JournalEntryDao journalEntryDao) throws Exception {
		return journalEntryDao.addSystemMessage(trial, now, user, systemMessageCode, new Object[] { CommonUtil.massMailOutVOToString(massMailVO) },
				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, !CommonUtil.getUseJournalEncryption(JournalModule.TRIAL_JOURNAL, null)) });
	}

	private static JournalEntry logSystemMessage(User user, MassMailOutVO massMailVO, Timestamp now, User modified, String systemMessageCode, Object result, Object original,
			JournalEntryDao journalEntryDao) throws Exception {
		if (user == null) {
			return null;
		}
		return journalEntryDao.addSystemMessage(user, now, modified, systemMessageCode, new Object[] { CommonUtil.massMailOutVOToString(massMailVO) },
				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, !CommonUtil.getUseJournalEncryption(JournalModule.USER_JOURNAL, null)) });
	}

	private VelocityEngine velocityEngine;

	private MassMailEmailSender massMailEmailSender;

	private void checkAddMassMailInput(MassMailInVO massMailIn, Date now) throws ServiceException {
		checkMassMailInput(massMailIn, now);
		MassMailStatusTypeDao massMailStatusTypeDao = this.getMassMailStatusTypeDao();
		MassMailStatusType state = CheckIDUtil.checkMassMailStatusTypeId(massMailIn.getStatusId(), massMailStatusTypeDao);
		boolean validState = false;
		Iterator<MassMailStatusType> statesIt = massMailStatusTypeDao.findInitialStates().iterator();
		while (statesIt.hasNext()) {
			if (state.equals(statesIt.next())) {
				validState = true;
				break;
			}
		}
		if (!validState) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_INITIAL_TRIAL_STATUS_TYPE, L10nUtil.getMassMailStatusTypeName(Locales.USER, state.getNameL10nKey()));
		}

	}

	private void checkMassMailInput(MassMailInVO massMailIn, Date now) throws ServiceException {
		CheckIDUtil.checkDepartmentId(massMailIn.getDepartmentId(), this.getDepartmentDao());
		MassMailType massMailType = CheckIDUtil.checkMassMailTypeId(massMailIn.getTypeId(), this.getMassMailTypeDao());
		TrialOutVO trialVO = null;
		if (massMailIn.getTrialId() != null) {
			TrialDao trialDao = this.getTrialDao();
			Trial trial = CheckIDUtil.checkTrialId(massMailIn.getTrialId(), trialDao);
			ServiceUtil.checkTrialLocked(trial);
			trialVO = trialDao.toTrialOutVO(trial);
		} else if (massMailType.isTrialRequired()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.MASS_MAIL_TRIAL_REQUIRED, L10nUtil.getMassMailTypeName(Locales.USER, massMailType.getNameL10nKey()));
		}
		ProbandListStatusTypeVO probandListStatusTypeVO = null;
		if (massMailIn.getProbandListStatusId() != null) {
			ProbandListStatusTypeDao probandListStatusTypeDao = this.getProbandListStatusTypeDao();
			probandListStatusTypeVO = probandListStatusTypeDao
					.toProbandListStatusTypeVO(CheckIDUtil.checkProbandListStatusTypeId(massMailIn.getProbandListStatusId(), probandListStatusTypeDao));
			if (trialVO != null && trialVO.getType().isPerson() != probandListStatusTypeVO.isPerson()) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.MASS_MAIL_WRONG_PROBAND_LIST_STATUS_TYPE, probandListStatusTypeVO.getName());
			}
		} else {
			if (massMailType.isProbandListStausRequired()) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.MASS_MAIL_PROBAND_LIST_STATUS_TYPE_REQUIRED,
						L10nUtil.getMassMailTypeName(Locales.USER, massMailType.getNameL10nKey()));
			}
			if (massMailIn.getProbandListStatusResend()) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.MASS_MAIL_PROBAND_LIST_STATUS_RESEND_NOT_FALSE);
			}
		}
		ServiceUtil.checkLocale(massMailIn.getLocale());

		ServiceUtil.getMassMailSubject(massMailIn.getSubjectFormat(), L10nUtil.getLocales(massMailIn.getLocale()), massMailIn.getMaleSalutation(), massMailIn.getFemaleSalutation(),
				null, trialVO,
				probandListStatusTypeVO);

		ServiceUtil.getMassMailMessage(velocityEngine, createMassMailOutVO(massMailIn), null, null, now, null, this.getTrialTagValueDao(), this.getProbandListEntryDao(),
				this.getProbandListEntryTagValueDao(), this.getInventoryBookingDao(),
				this.getProbandTagValueDao(),
				this.getProbandContactDetailValueDao(),
				this.getProbandAddressDao(),
				this.getDiagnosisDao(),
				this.getProcedureDao(),
				this.getMedicationDao(),
				this.getBankAccountDao());

		CoreUtil.checkEmailAddress(massMailIn.getFromAddress(), true);
		CoreUtil.checkEmailAddress(massMailIn.getReplyToAddress(), true);
		if (!CommonUtil.isEmptyString(massMailIn.getOtherTo())) {
			try {
				InternetAddress.parse(massMailIn.getOtherTo(), massMailEmailSender.isStrictEmailAddresses());
			} catch (AddressException e) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.MASS_MAIL_INVALID_OTHER_TO, massMailIn.getOtherTo(), e.getMessage());
			}
		} else if (!massMailIn.getProbandTo() && !massMailIn.getPhysicianTo() && !massMailIn.getTrialTeamTo()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.MASS_MAIL_OTHER_TO_REQUIRED);
		}
		if (massMailIn.getTrialId() == null && massMailIn.getTrialTeamTo()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.MASS_MAIL_TRIAL_TEAM_TO_NOT_FALSE);
		}
		if (!CommonUtil.isEmptyString(massMailIn.getCc())) {
			try {
				InternetAddress.parse(massMailIn.getCc(), massMailEmailSender.isStrictEmailAddresses());
			} catch (AddressException e) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.MASS_MAIL_INVALID_CC, massMailIn.getCc(), e.getMessage());
			}
		}
		if (!CommonUtil.isEmptyString(massMailIn.getBcc())) {
			try {
				InternetAddress.parse(massMailIn.getBcc(), massMailEmailSender.isStrictEmailAddresses());
			} catch (AddressException e) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.MASS_MAIL_INVALID_BCC, massMailIn.getBcc(), e.getMessage());
			}
		}
		if (!massMailIn.getAttachMassMailFiles() && massMailIn.getMassMailFilesLogicalPath() != null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.MASS_MAIL_MASS_MAIL_FILES_LOGICAL_PATH_NOT_NULL);
		}
		if (massMailIn.getTrialId() == null) {
			if (massMailIn.getAttachTrialFiles()) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.MASS_MAIL_ATTACH_TRIAL_FILES_NOT_FALSE);
			}
			// if (massMailIn.getAttachProbandListEntryTags()) {
			// throw L10nUtil.initServiceException(ServiceExceptionCodes.MASS_MAIL_ATTACH_PROBAND_LIST_ENTRY_TAGS_NOT_FALSE);
			// }
			// if (massMailIn.getAttachEcrfs()) {
			// throw L10nUtil.initServiceException(ServiceExceptionCodes.MASS_MAIL_ATTACH_ECRFS_NOT_FALSE);
			// }
			// if (massMailIn.getAttachReimbursementsPdf()) {
			// throw L10nUtil.initServiceException(ServiceExceptionCodes.MASS_MAIL_ATTACH_REIMBURSEMENTS_PDF_NOT_FALSE);
			// }
		}
		if (!massMailIn.getAttachTrialFiles() && massMailIn.getTrialFilesLogicalPath() != null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.MASS_MAIL_TRIAL_FILES_LOGICAL_PATH_NOT_NULL);
		}
		if (!massMailIn.getAttachProbandFiles() && massMailIn.getProbandFilesLogicalPath() != null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.MASS_MAIL_PROBAND_FILES_LOGICAL_PATH_NOT_NULL);
		}
	}

	private void checkUpdateMassMailInput(MassMail originalMassMail, MassMailInVO massMailIn, Date now, User user) throws ServiceException {
		checkMassMailInput(massMailIn, now);
		MassMailStatusTypeDao massMailStatusTypeDao = this.getMassMailStatusTypeDao();
		MassMailStatusType state = CheckIDUtil.checkMassMailStatusTypeId(massMailIn.getStatusId(), massMailStatusTypeDao);
		boolean validState = false;
		Iterator<MassMailStatusType> statesIt = massMailStatusTypeDao.findTransitions(originalMassMail.getStatus().getId()).iterator();
		while (statesIt.hasNext()) {
			if (state.equals(statesIt.next())) {
				validState = true;
				break;
			}
		}
		if (!validState) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_NEW_TRIAL_STATUS_TYPE, L10nUtil.getMassMailStatusTypeName(Locales.USER, state.getNameL10nKey()));
		}
	}


	private   MassMailOutVO createMassMailOutVO(MassMailInVO massMailIn) {

		MassMailOutVO massMailVO = new MassMailOutVO();

		DepartmentVO departmentVO = massMailIn.getDepartmentId() != null ? this.getDepartmentDao().toDepartmentVO(this.getDepartmentDao().load(massMailIn.getDepartmentId()))
				: null;
		MassMailStatusTypeVO statusVO = massMailIn.getStatusId() != null
				? this.getMassMailStatusTypeDao().toMassMailStatusTypeVO(this.getMassMailStatusTypeDao().load(massMailIn.getStatusId())) : null;
				MassMailTypeVO typeVO = massMailIn.getTypeId() != null ? this.getMassMailTypeDao().toMassMailTypeVO(this.getMassMailTypeDao().load(massMailIn.getTypeId())) : null;
				ProbandListStatusTypeVO probandListStatusTypeVO = massMailIn.getProbandListStatusId() != null ? this.getProbandListStatusTypeDao()
						.toProbandListStatusTypeVO(this.getProbandListStatusTypeDao().load(massMailIn.getProbandListStatusId())) : null;
						TrialOutVO trialVO = massMailIn.getTrialId() != null ? this.getTrialDao().toTrialOutVO(this.getTrialDao().load(massMailIn.getTrialId())) : null;

						if (massMailIn.getId() != null) {
							massMailVO.setId(massMailIn.getId());
						}
						massMailVO.setName(massMailIn.getName());
						massMailVO.setDescription(massMailIn.getDescription());
						massMailVO.setDepartment(departmentVO);
						massMailVO.setStart(massMailIn.getStart());
						massMailVO.setStatus(statusVO );
						massMailVO.setLockAfterSending(massMailIn.getLockAfterSending());
						massMailVO.setType(typeVO );
						massMailVO.setProbandListStatus(probandListStatusTypeVO);
						massMailVO.setProbandListStatusResend(massMailIn.getProbandListStatusResend());
						massMailVO.setTrial(trialVO);
						massMailVO.setFromAddress(massMailIn.getFromAddress());
						massMailVO.setFromName(massMailIn.getFromName());
						massMailVO.setLocale(massMailIn.getLocale());
						massMailVO.setMaleSalutation(massMailIn.getMaleSalutation());
						massMailVO.setFemaleSalutation(massMailIn.getFemaleSalutation());
						massMailVO.setSubjectFormat(massMailIn.getSubjectFormat());
						massMailVO.setTextTemplate(massMailIn.getTextTemplate());
						massMailVO.setReplyToAddress(massMailIn.getReplyToAddress());
						massMailVO.setReplyToName(massMailIn.getReplyToName());
						massMailVO.setProbandTo(massMailIn.getProbandTo());
						massMailVO.setPhysicianTo(massMailIn.getPhysicianTo());
						massMailVO.setTrialTeamTo(massMailIn.getTrialTeamTo());
						massMailVO.setOtherTo(massMailIn.getOtherTo());
						massMailVO.setCc(massMailIn.getCc());
						massMailVO.setBcc(massMailIn.getBcc());
						massMailVO.setUseBeacon(massMailIn.getUseBeacon());
						massMailVO.setAttachMassMailFiles(massMailIn.getAttachMassMailFiles());
						massMailVO.setMassMailFilesLogicalPath(massMailIn.getMassMailFilesLogicalPath());
						massMailVO.setAttachTrialFiles(massMailIn.getAttachTrialFiles());
						massMailVO.setTrialFilesLogicalPath(massMailIn.getTrialFilesLogicalPath());
						massMailVO.setAttachProbandFiles(massMailIn.getAttachProbandFiles());
						massMailVO.setProbandFilesLogicalPath(massMailIn.getProbandFilesLogicalPath());
						massMailVO.setAttachInquiries(massMailIn.getAttachInquiries());
						massMailVO.setAttachProbandListEntryTags(massMailIn.getAttachProbandListEntryTags());
						massMailVO.setAttachEcrfs(massMailIn.getAttachEcrfs());
						massMailVO.setAttachProbandLetter(massMailIn.getAttachProbandLetter());
						massMailVO.setAttachReimbursementsPdf(massMailIn.getAttachReimbursementsPdf());
						if (massMailIn.getVersion() != null) {
							massMailVO.setVersion(massMailIn.getVersion());
						}
						return massMailVO;

	}



	/**
	 * @see org.phoenixctms.ctsms.service.massmail.MassMailService#addMassMail(AuthenticationVO, MassMailInVO)
	 */
	protected MassMailOutVO handleAddMassMail(AuthenticationVO auth, MassMailInVO newMassMail)
			throws Exception
	{
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		checkAddMassMailInput(newMassMail, now);
		MassMailDao massMailDao = this.getMassMailDao();
		MassMail massMail = massMailDao.massMailInVOToEntity(newMassMail);

		CoreUtil.modifyVersion(massMail, now, user);
		massMail = massMailDao.create(massMail);
		MassMailOutVO result = massMailDao.toMassMailOutVO(massMail);
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		ServiceUtil.logSystemMessage(massMail, result, now, user, SystemMessageCodes.MASS_MAIL_CREATED, result, null, journalEntryDao);
		Trial trial = massMail.getTrial();
		if (trial != null) {
			logSystemMessage(trial, result, now, user, SystemMessageCodes.MASS_MAIL_CREATED, result, null, journalEntryDao);
		}
		return result;
	}

	@Override
	protected MassMailRecipientOutVO handleAddMassMailRecipient(AuthenticationVO auth, MassMailRecipientInVO newMassMailRecipient) throws Exception {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		return ServiceUtil.addMassMailRecipient(newMassMailRecipient, now, user, this.getMassMailDao(), this.getProbandDao(), this.getTrialDao(), this.getMassMailRecipientDao(),
				this.getJournalEntryDao());
	}

	@Override
	protected Collection<MassMailRecipientOutVO> handleAddMassMailRecipients(AuthenticationVO auth, Long massMailId, Set<Long> probandIds) throws Exception {
		MassMailDao massMailDao = this.getMassMailDao();
		MassMail massMail = CheckIDUtil.checkMassMailId(massMailId, massMailDao);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		ArrayList<MassMailRecipientOutVO> result = new ArrayList<MassMailRecipientOutVO>();
		ArrayList<Long> ids = new ArrayList<Long>();
		if (probandIds != null) {
			Iterator<Long> probandIt = probandIds.iterator();
			while (probandIt.hasNext()) {
				Long probandId = probandIt.next();
				MassMailRecipientInVO newMassMailRecipient = new MassMailRecipientInVO();
				newMassMailRecipient.setMassMailId(massMailId);
				newMassMailRecipient.setProbandId(probandId);
				try {
					result.add(ServiceUtil.addMassMailRecipient(newMassMailRecipient, now, user, this.getMassMailDao(), this.getProbandDao(), this.getTrialDao(),
							this.getMassMailRecipientDao(),
							this.getJournalEntryDao()));
					ids.add(probandId);
				} catch (ServiceException e) {
					// ignore; due to existent proband....
				}
			}
		}
		logSystemMessage(massMail, massMailDao.toMassMailOutVO(massMail), now, user, probandIds, ids,
				SystemMessageCodes.MASS_MAIL_RECIPIENTS_CREATED);
		return result;
	}

	/**
	 * @see org.phoenixctms.ctsms.service.massmail.MassMailService#deleteMassMail(AuthenticationVO, Long, boolean, boolean)
	 */
	protected MassMailOutVO handleDeleteMassMail(AuthenticationVO auth, Long massMailId, boolean defer, boolean force)
			throws Exception {
		MassMailDao massMailDao = this.getMassMailDao();
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		MassMailOutVO result;
		if (!force && defer) {
			MassMail originalMassMail = CheckIDUtil.checkMassMailId(massMailId, massMailDao);
			MassMailOutVO original = massMailDao.toMassMailOutVO(originalMassMail);
			massMailDao.evict(originalMassMail);
			MassMail massMail = CheckIDUtil.checkMassMailId(massMailId, massMailDao, LockMode.PESSIMISTIC_WRITE);
			massMail.setDeferredDelete(true);
			CoreUtil.modifyVersion(massMail, originalMassMail.getVersion(), now, user); // no opt. locking
			massMailDao.update(massMail);
			result = massMailDao.toMassMailOutVO(massMail);
			ServiceUtil.logSystemMessage(massMail, result, now, user, SystemMessageCodes.MASS_MAIL_MARKED_FOR_DELETION, result, original, journalEntryDao);
			Trial trial = massMail.getTrial();
			if (trial != null) {
				logSystemMessage(trial, result, now, user, SystemMessageCodes.MASS_MAIL_MARKED_FOR_DELETION, result, original, journalEntryDao);
			}
		} else {
			MassMail massMail = CheckIDUtil.checkMassMailId(massMailId, massMailDao, LockMode.PESSIMISTIC_WRITE);
			result = massMailDao.toMassMailOutVO(massMail);
			// if (deleteCascade) {
			boolean checkProbandLocked = Settings.getBoolean(SettingCodes.REMOVE_MASS_MAIL_CHECK_PROBAND_LOCKED, Bundle.SETTINGS,
					DefaultSettings.REMOVE_MASS_MAIL_CHECK_PROBAND_LOCKED);
			MassMailRecipientDao massMailRecipientDao = this.getMassMailRecipientDao();
			Iterator<MassMailRecipient> recipientsIt = massMail.getRecipients().iterator();
			while (recipientsIt.hasNext()) {
				MassMailRecipient recipient = recipientsIt.next();
				Proband proband = recipient.getProband();
				if (proband != null) {
					if (checkProbandLocked) {
						ServiceUtil.checkProbandLocked(proband);
					}
					MassMailRecipientOutVO recipientVO = massMailRecipientDao.toMassMailRecipientOutVO(recipient);
					ServiceUtil.logSystemMessage(proband, result, now, user, SystemMessageCodes.MASS_MAIL_DELETED_RECIPIENT_DELETED, recipientVO, null, journalEntryDao);
					proband.removeMassMailReceipts(recipient);
					recipient.setProband(null);
				}
				// Trial trial = recipient.getTrial();
				// if (trial != null) {
				// trial.removeMassMailReceipts(recipient);
				// recipient.setTrial(null);
				// }
				recipient.setMassMail(null);
				massMailRecipientDao.remove(recipient);
			}
			massMail.getRecipients().clear();
			Iterator<JournalEntry> journalEntriesIt = massMail.getJournalEntries().iterator();
			while (journalEntriesIt.hasNext()) {
				JournalEntry journalEntry = journalEntriesIt.next();
				journalEntry.setMassMail(null);
				journalEntryDao.remove(journalEntry);
			}
			massMail.getJournalEntries().clear();
			FileDao fileDao = this.getFileDao();
			Iterator<File> filesIt = massMail.getFiles().iterator();
			while (filesIt.hasNext()) {
				File file = filesIt.next();
				file.setMassMail(null);
				fileDao.remove(file);
			}
			massMail.getFiles().clear();
			Trial trial = massMail.getTrial();
			if (trial != null) {
				ServiceUtil.checkTrialLocked(trial);
				logSystemMessage(trial, result, now, user, SystemMessageCodes.MASS_MAIL_DELETED_TRIAL_REMOVED, result, null, journalEntryDao);
				trial.removeMassMails(massMail);
				massMail.setTrial(null);
			}
			massMailDao.remove(massMail);
			logSystemMessage(user, result, now, user, SystemMessageCodes.MASS_MAIL_DELETED, result, null, journalEntryDao);
		}
		return result;
	}

	@Override
	protected MassMailRecipientOutVO handleDeleteMassMailRecipient(AuthenticationVO auth, Long massMailRecipientId) throws Exception {
		MassMailRecipientDao massMailRecpientDao = this.getMassMailRecipientDao();
		MassMailRecipient recipient = CheckIDUtil.checkMassMailRecipientId(massMailRecipientId, massMailRecpientDao, LockMode.PESSIMISTIC_WRITE);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		return removeMassMailRecipient(recipient, now, user);
	}

	@Override
	protected Collection<MassMailRecipientOutVO> handleDeleteMassMailRecipients(AuthenticationVO auth, Long massMailId, Set<Long> probandIds) throws Exception {
		MassMailDao massMailDao = this.getMassMailDao();
		MassMail massMail = CheckIDUtil.checkMassMailId(massMailId, massMailDao, LockMode.PESSIMISTIC_WRITE);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		MassMailRecipientDao massMailRecpeintDao = this.getMassMailRecipientDao();
		ArrayList<MassMailRecipientOutVO> result = new ArrayList<MassMailRecipientOutVO>();
		ArrayList<Long> ids = new ArrayList<Long>();
		ArrayList<Long> idsSorted;
		if (probandIds != null) {
			idsSorted = new ArrayList<Long>(probandIds);
			Collections.sort(idsSorted);
			Iterator<Long> probandIt = idsSorted.iterator();
			while (probandIt.hasNext()) {
				Long probandId = probandIt.next();
				try {
					MassMailRecipient massMailRecipient = massMailRecpeintDao.findByMassMailProband(massMailId, probandId);
					if (massMailRecipient != null) {
						result.add(removeMassMailRecipient(massMailRecipient, now, user));
						ids.add(probandId);
					}
				} catch (ServiceException e) {
				}
			}
		} else {
			idsSorted = new ArrayList<Long>();
		}
		logSystemMessage(massMail, massMailDao.toMassMailOutVO(massMail), now, user, ids, idsSorted, SystemMessageCodes.MASS_MAIL_RECIPIENTS_DELETED);
		return result;
	}

	@Override
	protected EmailMessageVO handleGetEmailMessage(AuthenticationVO auth, Long massMailRecipientId) throws Exception {
		MassMailRecipientDao massMailRecpientDao = this.getMassMailRecipientDao();
		MassMailRecipient recipient = CheckIDUtil.checkMassMailRecipientId(massMailRecipientId, massMailRecpientDao);
		massMailRecpientDao.refresh(recipient);
		return massMailRecpientDao.toEmailMessageVO(recipient);
	}

	/**
	 * @see org.phoenixctms.ctsms.service.massmail.MassMailService#getMassMail(AuthenticationVO, Long)
	 */
	protected MassMailOutVO handleGetMassMail(AuthenticationVO auth, Long massMailId)
			throws Exception
	{
		MassMailDao massMailDao = this.getMassMailDao();
		MassMail massMail = CheckIDUtil.checkMassMailId(massMailId, massMailDao);
		MassMailOutVO result = massMailDao.toMassMailOutVO(massMail);
		return result;
	}

	@Override
	protected long handleGetMassMailCount(AuthenticationVO auth, Long trialId, Long probandListStatusTypeId, Boolean locked, Long resendProbandId) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		if (resendProbandId != null) {
			CheckIDUtil.checkProbandId(resendProbandId, this.getProbandDao());
		}
		return this.getMassMailDao().getCount(trialId, probandListStatusTypeId, locked, resendProbandId);
	}


	/**
	 * @see org.phoenixctms.ctsms.service.massmail.MassMailService#getMassMailList(AuthenticationVO, Long, Long, PSFVO)
	 */
	protected  Collection<MassMailOutVO> handleGetMassMailList(AuthenticationVO auth, Long massMailId, Long departmentId, PSFVO psf)
			throws Exception
	{
		MassMailDao massMailDao = this.getMassMailDao();
		if (massMailId != null) {
			CheckIDUtil.checkMassMailId(massMailId, massMailDao);
		}
		if (departmentId != null) {
			CheckIDUtil.checkDepartmentId(departmentId, this.getDepartmentDao());
		}
		Collection massMails = massMailDao.findByIdDepartment(massMailId, departmentId, psf);
		massMailDao.toMassMailOutVOCollection(massMails);
		return massMails;
	}

	@Override
	protected MassMailProgressVO handleGetMassMailProgress(AuthenticationVO auth, Long massMailId) throws Exception {
		MassMail massMail = CheckIDUtil.checkMassMailId(massMailId, this.getMassMailDao());
		MassMailProgressVO massMailProgress = new MassMailProgressVO();
		MassMailRecipientDao massMailRecipientDao = this.getMassMailRecipientDao();
		massMailProgress.setRecipientTotalCount( massMailRecipientDao.getCount(massMailId, null, false));
		massMailProgress.setRecipientPendingCount( massMailRecipientDao.getCount(massMailId, null, true));
		return massMailProgress;
	}



	@Override
	protected MassMailRecipientOutVO handleGetMassMailRecipient(AuthenticationVO auth, Long massMailRecipientId) throws Exception {
		MassMailRecipientDao massMailRecpientDao = this.getMassMailRecipientDao();
		MassMailRecipient recipient = CheckIDUtil.checkMassMailRecipientId(massMailRecipientId, massMailRecpientDao);
		massMailRecpientDao.refresh(recipient);
		return massMailRecpientDao.toMassMailRecipientOutVO(recipient);
	}

	@Override
	protected long handleGetMassMailRecipientCount(AuthenticationVO auth, Long massMailId, Long probandId, boolean pending) throws Exception {

		if (massMailId != null) {
			CheckIDUtil.checkMassMailId(massMailId, this.getMassMailDao());
		}
		if (probandId != null) {
			CheckIDUtil.checkProbandId(probandId, this.getProbandDao());
		}

		return this.getMassMailRecipientDao().getCount(massMailId, probandId, pending);
	}

	@Override
	protected Collection<MassMailRecipientOutVO> handleGetMassMailRecipientList(AuthenticationVO auth, Long massMailId, Long probandId, PSFVO psf) throws Exception {
		if (massMailId != null) {
			CheckIDUtil.checkMassMailId(massMailId, this.getMassMailDao());
		}
		if (probandId != null) {
			CheckIDUtil.checkProbandId(probandId, this.getProbandDao());
		}
		MassMailRecipientDao massMailRecpientDao = this.getMassMailRecipientDao();
		Collection recipients = massMailRecpientDao.findByMassMailProband(massMailId, probandId, psf);
		massMailRecpientDao.toMassMailRecipientOutVOCollection(recipients);
		return recipients;
	}

	@Override
	protected Collection<MassMailRecipientOutVO> handleGetPendingRecipientList(AuthenticationVO auth, Long departmentId, Boolean pending, boolean scheduled, PSFVO psf)
			throws Exception {
		if (departmentId != null) {
			CheckIDUtil.checkDepartmentId(departmentId, this.getDepartmentDao());
		}
		MassMailRecipientDao massMailRecpientDao = this.getMassMailRecipientDao();
		Timestamp now = new Timestamp(System.currentTimeMillis());
		Collection recipients = massMailRecpientDao.findByDepartmentPending(departmentId, now, pending, scheduled, psf);
		massMailRecpientDao.toMassMailRecipientOutVOCollection(recipients);
		return recipients;
	}

	@Override
	protected String handleGetSubject(AuthenticationVO auth, MassMailInVO massMailIn, Long probandId) throws ServiceException {
		return ServiceUtil.getMassMailSubject(massMailIn.getSubjectFormat(),
				L10nUtil.getLocales(massMailIn.getLocale()),
				massMailIn.getMaleSalutation(), massMailIn.getFemaleSalutation(),
				probandId != null ? this.getProbandDao().toProbandOutVO(this.getProbandDao().load(probandId)) : null,
						// trialId != null ? this.getTrialDao().toTrialOutVO(this.getTrialDao().load(trialId)) :
						massMailIn.getTrialId() != null ? this.getTrialDao().toTrialOutVO(this.getTrialDao().load(massMailIn.getTrialId())) : null,
								massMailIn.getProbandListStatusId() != null ? this.getProbandListStatusTypeDao()
										.toProbandListStatusTypeVO(this.getProbandListStatusTypeDao().load(massMailIn.getProbandListStatusId())) : null);
	}

	@Override
	protected String handleGetText(AuthenticationVO auth, MassMailInVO massMailIn, Long probandId) throws Exception {
		return ServiceUtil.getMassMailMessage(velocityEngine, createMassMailOutVO(massMailIn),
				probandId != null ? this.getProbandDao().toProbandOutVO(this.getProbandDao().load(probandId)) : null, null,
						// trialId != null ? this.getTrialDao().toTrialOutVO(this.getTrialDao().load(trialId)) : null,
						new Timestamp(System.currentTimeMillis()),
						null, this.getTrialTagValueDao(), this.getProbandListEntryDao(), this.getProbandListEntryTagValueDao(), this.getInventoryBookingDao(),
						this.getProbandTagValueDao(),
						this.getProbandContactDetailValueDao(),
						this.getProbandAddressDao(),
						this.getDiagnosisDao(),
						this.getProcedureDao(),
						this.getMedicationDao(),
						this.getBankAccountDao());
	}

	@Override
	protected long handleProcessMassMails(AuthenticationVO auth, Long departmentId, PSFVO psf) throws Exception {
		// Timestamp now = new Timestamp(System.currentTimeMillis());
		// User user = CoreUtil.getUser();
		// MassMailRecipientInVO newRecipient = new MassMailRecipientInVO();
		// newRecipient.setMassMailId(6514744l);
		// newRecipient.setProbandId(6472770l);
		// MassMailRecipientOutVO test = ServiceUtil.addMassMailRecipients(newRecipient, now, user, this.getMassMailDao(), this.getProbandDao(), this.getMassMailRecipientDao(),
		// this.getJournalEntryDao());
		if (departmentId != null) {
			CheckIDUtil.checkDepartmentId(departmentId, this.getDepartmentDao());
		}
		long totalEmailCount = 0;
		int delayMillis = Settings.getInt(SettingCodes.EMAIL_PROCESS_MASS_MAILS_DELAY_MILLIS, Bundle.SETTINGS, DefaultSettings.EMAIL_PROCESS_MASS_MAILS_DELAY_MILLIS);
		User user = CoreUtil.getUser();
		Timestamp now = new Timestamp(System.currentTimeMillis());
		MassMailRecipientDao massMailRecipientDao = this.getMassMailRecipientDao();
		MassMailDao massMailDao = this.getMassMailDao();
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		HashSet<MassMail> massMails = new HashSet<MassMail>();
		Iterator<MassMailRecipient> recipientsIt = massMailRecipientDao.findPending(null, departmentId, user.getDepartment().getId(), now, true, psf).iterator();
		while (recipientsIt.hasNext()) {
			MassMailRecipient recipient = recipientsIt.next(); // massMailRecipientDao.load(recipientsIt.next().getId(),
			// LockMode.PESSIMISTIC_WRITE);
			massMailRecipientDao.refresh(recipient, LockMode.PESSIMISTIC_WRITE);
			if (!ServiceUtil.isMassMailRecipientPending(recipient)) {
				continue;
			}
			// MassMail massMail = this.getMassMailDao().load(6514744l);
			// MassMailRecipient recipient = this.getMassMailRecipientDao().load(6524156l);
			MassMail massMail = recipient.getMassMail();
			boolean sent = false;
			boolean cancelled = false;
			int toCount = 0;
			try {
				toCount = massMailEmailSender.prepareAndSend(massMail, recipient);
				if (toCount > 0) {
					sent = true;
					if (delayMillis > 0) {
						Thread.currentThread().sleep(delayMillis);
					}
				} else {
					recipient.setErrorMessage(
							L10nUtil.getString(Locales.NOTIFICATION, MessageCodes.MASS_MAIL_CACNELLED_NO_RECIPIENTS, DefaultMessages.MASS_MAIL_CACNELLED_NO_RECIPIENTS));
					cancelled = true;
				}
			} catch (Throwable t) {
				recipient.setErrorMessage(t.getMessage());
				if (delayMillis > 0) {
					Thread.currentThread().sleep(delayMillis);
				}
			}
			recipient.setTimesProcessed(recipient.getTimesProcessed() + 1l);
			now = new Timestamp(System.currentTimeMillis());
			recipient.setProcessedTimestamp(now);
			recipient.setSent(sent);
			recipient.setCancelled(cancelled);
			CoreUtil.modifyVersion(recipient, recipient.getVersion(), recipient.getModifiedTimestamp(), user);
			this.getMassMailRecipientDao().update(recipient);
			this.getMassMailRecipientDao().commitAndResumeTransaction();
			// EncryptedEmailVO result = this.getMassMailRecipientDao().toEncryptedEmailVO(recipient);
			totalEmailCount += toCount;
			massMails.add(massMail);
		}
		now = new Timestamp(System.currentTimeMillis());
		Iterator<MassMail> massMailsIt = massMails.iterator();
		while (massMailsIt.hasNext()) {
			MassMail massMail = massMailsIt.next();
			if (massMail.isLockAfterSending() && !massMail.getStatus().isLocked() && massMailRecipientDao.getCount(massMail.getId(), null, true) == 0l) {
				Iterator<MassMailStatusType> statusIt = massMail.getStatus().getTransitions().iterator();
				while (statusIt.hasNext()) {
					MassMailStatusType newStatus = statusIt.next();
					if (newStatus.isLocked()) {
						MassMailOutVO original = massMailDao.toMassMailOutVO(massMail);
						massMail.setStatus(newStatus);
						CoreUtil.modifyVersion(massMail, massMail.getVersion(), now, user);
						massMailDao.update(massMail);
						MassMailOutVO massMailVO = massMailDao.toMassMailOutVO(massMail);
						// ServiceUtil.logSystemMessage(massMail, massMailVO, now, user, SystemMessageCodes.MASS_MAIL_LOCKED, massMailVO, original, journalEntryDao);
						journalEntryDao.addSystemMessage(massMail, now, user, SystemMessageCodes.MASS_MAIL_LOCKED,
								new Object[] { Long.toString(massMailRecipientDao.getCount(massMail.getId(), null, false)) },
								new Object[] { CoreUtil.getSystemMessageCommentContent(massMailVO, original,
										!CommonUtil.getUseJournalEncryption(JournalModule.MASS_MAIL_JOURNAL, null)) });
						break;
					}
				}
			}
		}
		return totalEmailCount;
	}

	@Override
	protected MassMailRecipientOutVO handleResetMassMailRecipient(AuthenticationVO auth, Long massMailRecipientId, Long version) throws Exception {
		MassMailRecipientDao massMailRecipientDao = this.getMassMailRecipientDao();
		MassMailRecipient recipient = CheckIDUtil.checkMassMailRecipientId(massMailRecipientId, massMailRecipientDao, LockMode.PESSIMISTIC_WRITE);
		// if (recipient.getTimesProcessed() == 0l) {
		// x
		// }
		MassMailRecipientOutVO original = massMailRecipientDao.toMassMailRecipientOutVO(recipient);
		ServiceUtil.resetMassMailRecipient(recipient, original);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();

		CoreUtil.modifyVersion(recipient, version.longValue(), now, user);
		massMailRecipientDao.update(recipient);
		MassMailRecipientOutVO result = massMailRecipientDao.toMassMailRecipientOutVO(recipient);
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		ServiceUtil.logSystemMessage(recipient.getMassMail(), result.getProband(), now, user, SystemMessageCodes.MASS_MAIL_RECIPIENT_RESET, result, original, journalEntryDao);
		ServiceUtil.logSystemMessage(recipient.getProband(), result.getMassMail(), now, user, SystemMessageCodes.MASS_MAIL_RECIPIENT_RESET, result, original, journalEntryDao);
		return result;
	}

	protected MassMailOutVO handleUpdateMassMail(AuthenticationVO auth, MassMailInVO modifiedMassMail)
			throws Exception {
		MassMailDao massMailDao = this.getMassMailDao();
		MassMail originalMassMail = CheckIDUtil.checkMassMailId(modifiedMassMail.getId(), massMailDao, LockMode.PESSIMISTIC_WRITE);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		checkUpdateMassMailInput(originalMassMail, modifiedMassMail, now, user);
		MassMailOutVO original = massMailDao.toMassMailOutVO(originalMassMail);
		massMailDao.evict(originalMassMail);
		MassMail massMail = massMailDao.massMailInVOToEntity(modifiedMassMail);
		// Timestamp now = new Timestamp(System.currentTimeMillis());
		// User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(originalMassMail, massMail, now, user);
		massMailDao.update(massMail);
		MassMailOutVO result = massMailDao.toMassMailOutVO(massMail);
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		ServiceUtil.logSystemMessage(massMail, result, now, user, SystemMessageCodes.MASS_MAIL_UPDATED, result, original, journalEntryDao);
		Trial trial = massMail.getTrial();
		if (trial != null) {
			logSystemMessage(trial, result, now, user, SystemMessageCodes.MASS_MAIL_UPDATED, result, original, journalEntryDao);
		}
		return result;
	}

	public JournalEntry logSystemMessage(MassMail massMail, MassMailOutVO massMailVO, Timestamp now, User modified, Collection<Long> resultIds, Collection<Long> inputIds,
			String systemMessageCode) throws Exception {
		return this.getJournalEntryDao().addSystemMessage(massMail, now, modified, systemMessageCode,
				new Object[] { CommonUtil.massMailOutVOToString(massMailVO), Integer.toString(resultIds.size()), Integer.toString(inputIds.size()) },
				new Object[] { resultIds, inputIds });
	}

	private MassMailRecipientOutVO removeMassMailRecipient(MassMailRecipient recipient, Timestamp now, User user) throws Exception {
		MassMail massMail = recipient.getMassMail();
		ServiceUtil.checkMassMailLocked(massMail);
		Proband proband = recipient.getProband();
		// Trial trial = recipient.getTrial();
		if (proband != null) {
			ServiceUtil.checkProbandLocked(proband);
		}
		MassMailRecipientDao massMailRecpeintDao = this.getMassMailRecipientDao();
		MassMailRecipientOutVO result = massMailRecpeintDao.toMassMailRecipientOutVO(recipient);
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		massMail.removeRecipients(recipient);
		if (proband != null) {
			proband.removeMassMailReceipts(recipient);
		}
		// if (trial != null) {
		// trial.removeMassMailReceipts(recipient);
		// }
		massMailRecpeintDao.remove(recipient);
		if (proband != null) {
			ServiceUtil.logSystemMessage(proband, result.getMassMail(), now, user, SystemMessageCodes.MASS_MAIL_RECIPIENT_DELETED, result, null, journalEntryDao);
			ServiceUtil.logSystemMessage(massMail, result.getProband(), now, user, SystemMessageCodes.MASS_MAIL_RECIPIENT_DELETED, result, null, journalEntryDao);
		} else {
			ServiceUtil.logSystemMessage(massMail, result.getMassMail(), now, user, SystemMessageCodes.MASS_MAIL_RECIPIENT_DELETED, result, null, journalEntryDao);
		}
		// ServiceUtil.logSystemMessage(trial, result.getTrial(), now, user, SystemMessageCodes.PROBAND_LIST_ENTRY_DELETED, result, null, journalEntryDao);

		return result;
	}

	public void setMassMailEmailSender(MassMailEmailSender massMailEmailSender) {
		this.massMailEmailSender = massMailEmailSender;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}
}