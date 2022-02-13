// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 * TEMPLATE:    SpringServiceImpl.vsl in andromda-spring cartridge
 * MODEL CLASS: AndroMDAModel::ctsms::org.phoenixctms.ctsms::service::shared::JobService
 * STEREOTYPE:  Service
 */
package org.phoenixctms.ctsms.service.shared;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.phoenixctms.ctsms.adapt.JobCollisionFinder;
import org.phoenixctms.ctsms.domain.Criteria;
import org.phoenixctms.ctsms.domain.InputField;
import org.phoenixctms.ctsms.domain.Job;
import org.phoenixctms.ctsms.domain.JobDao;
import org.phoenixctms.ctsms.domain.JobType;
import org.phoenixctms.ctsms.domain.JournalEntry;
import org.phoenixctms.ctsms.domain.JournalEntryDao;
import org.phoenixctms.ctsms.domain.MimeType;
import org.phoenixctms.ctsms.domain.Proband;
import org.phoenixctms.ctsms.domain.Trial;
import org.phoenixctms.ctsms.domain.User;
import org.phoenixctms.ctsms.enumeration.DBModule;
import org.phoenixctms.ctsms.enumeration.FileModule;
import org.phoenixctms.ctsms.enumeration.JobModule;
import org.phoenixctms.ctsms.enumeration.JobStatus;
import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CommonUtil.EllipsisPlacement;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.DefaultMessages;
import org.phoenixctms.ctsms.util.DefaultSettings;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.MessageCodes;
import org.phoenixctms.ctsms.util.ServiceExceptionCodes;
import org.phoenixctms.ctsms.util.SettingCodes;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.util.SystemMessageCodes;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.CriteriaOutVO;
import org.phoenixctms.ctsms.vo.InputFieldOutVO;
import org.phoenixctms.ctsms.vo.JobAddVO;
import org.phoenixctms.ctsms.vo.JobFileVO;
import org.phoenixctms.ctsms.vo.JobOutVO;
import org.phoenixctms.ctsms.vo.JobUpdateVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;

/**
 * @see org.phoenixctms.ctsms.service.shared.JobService
 */
public class JobServiceImpl extends JobServiceBase {

	private final static int JOB_OUTPUT_MAX_LENGTH = 64 * 1024;

	private static JournalEntry logSystemMessage(Trial trial, TrialOutVO trialVO, Timestamp now, User modified, String systemMessageCode, Object result, Object original,
			JournalEntryDao journalEntryDao) throws Exception {
		return journalEntryDao.addSystemMessage(trial, now, modified, systemMessageCode, new Object[] { CommonUtil.trialOutVOToString(trialVO) },
				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, !CommonUtil.getUseJournalEncryption(JournalModule.TRIAL_JOURNAL, null)) });
	}

	private static JournalEntry logSystemMessage(Proband proband, ProbandOutVO probandVO, Timestamp now, User modified, String systemMessageCode, Object result, Object original,
			JournalEntryDao journalEntryDao) throws Exception {
		boolean journalEncrypted = CommonUtil.getUseJournalEncryption(JournalModule.PROBAND_JOURNAL, null);
		return journalEntryDao.addSystemMessage(proband, now, modified, systemMessageCode, journalEncrypted ? new Object[] { CommonUtil.probandOutVOToString(probandVO) }
				: new Object[] { Long.toString(probandVO.getId()) },
				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, !journalEncrypted) });
	}

	private static JournalEntry logSystemMessage(InputField inputField, InputFieldOutVO inputFieldVO, Timestamp now, User modified, String systemMessageCode, Object result,
			Object original,
			JournalEntryDao journalEntryDao) throws Exception {
		return journalEntryDao.addSystemMessage(inputField, now, modified, systemMessageCode, new Object[] { CommonUtil.inputFieldOutVOToString(inputFieldVO) },
				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, false) });
	}

	private static JournalEntry logSystemMessage(Criteria criteria, CriteriaOutVO criteriaVO, Timestamp now, User modified, String systemMessageCode, Object result,
			Object original, JournalEntryDao journalEntryDao) throws Exception {
		return journalEntryDao.addSystemMessage(criteria, now, modified, systemMessageCode, new Object[] { CommonUtil.criteriaOutVOToString(criteriaVO) },
				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, !CommonUtil.getUseJournalEncryption(JournalModule.CRITERIA_JOURNAL, null)) });
	}

	private int numIdsSet(JobAddVO job) {
		int result = 0;
		result += (job.getTrialId() != null) ? 1 : 0;
		result += (job.getProbandId() != null) ? 1 : 0;
		result += (job.getInputFieldId() != null) ? 1 : 0;
		result += (job.getCriteriaId() != null) ? 1 : 0;
		return result;
	}

	private void checkJobModuleId(JobModule module, Long id) throws ServiceException {
		if (id != null) {
			switch (module) {
				case TRIAL_JOB:
					CheckIDUtil.checkTrialId(id, this.getTrialDao());
					break;
				case PROBAND_JOB:
					CheckIDUtil.checkProbandId(id, this.getProbandDao());
					break;
				case INPUT_FIELD_JOB:
					CheckIDUtil.checkInputFieldId(id, this.getInputFieldDao());
					break;
				case INVENTORY_CRITERIA_JOB:
					if (!DBModule.INVENTORY_DB.equals(CheckIDUtil.checkCriteriaId(id, this.getCriteriaDao()).getModule())) {
						throw L10nUtil.initServiceException(ServiceExceptionCodes.JOB_WRONG_CRITERIA_MODULE);
					}
					break;
				case STAFF_CRITERIA_JOB:
					if (!DBModule.STAFF_DB.equals(CheckIDUtil.checkCriteriaId(id, this.getCriteriaDao()).getModule())) {
						throw L10nUtil.initServiceException(ServiceExceptionCodes.JOB_WRONG_CRITERIA_MODULE);
					}
					break;
				case COURSE_CRITERIA_JOB:
					if (!DBModule.COURSE_DB.equals(CheckIDUtil.checkCriteriaId(id, this.getCriteriaDao()).getModule())) {
						throw L10nUtil.initServiceException(ServiceExceptionCodes.JOB_WRONG_CRITERIA_MODULE);
					}
					break;
				case TRIAL_CRITERIA_JOB:
					if (!DBModule.TRIAL_DB.equals(CheckIDUtil.checkCriteriaId(id, this.getCriteriaDao()).getModule())) {
						throw L10nUtil.initServiceException(ServiceExceptionCodes.JOB_WRONG_CRITERIA_MODULE);
					}
					break;
				case INPUT_FIELD_CRITERIA_JOB:
					if (!DBModule.INPUT_FIELD_DB.equals(CheckIDUtil.checkCriteriaId(id, this.getCriteriaDao()).getModule())) {
						throw L10nUtil.initServiceException(ServiceExceptionCodes.JOB_WRONG_CRITERIA_MODULE);
					}
					break;
				case PROBAND_CRITERIA_JOB:
					if (!DBModule.PROBAND_DB.equals(CheckIDUtil.checkCriteriaId(id, this.getCriteriaDao()).getModule())) {
						throw L10nUtil.initServiceException(ServiceExceptionCodes.JOB_WRONG_CRITERIA_MODULE);
					}
					break;
				case USER_CRITERIA_JOB:
					if (!DBModule.USER_DB.equals(CheckIDUtil.checkCriteriaId(id, this.getCriteriaDao()).getModule())) {
						throw L10nUtil.initServiceException(ServiceExceptionCodes.JOB_WRONG_CRITERIA_MODULE);
					}
					break;
				case MASS_MAIL_CRITERIA_JOB:
					if (!DBModule.MASS_MAIL_DB.equals(CheckIDUtil.checkCriteriaId(id, this.getCriteriaDao()).getModule())) {
						throw L10nUtil.initServiceException(ServiceExceptionCodes.JOB_WRONG_CRITERIA_MODULE);
					}
					break;
				default:
					// not supported for now...
					throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.UNSUPPORTED_JOB_MODULE, DefaultMessages.UNSUPPORTED_JOB_MODULE,
							new Object[] { module.toString() }));
			}
		}
	}

	private void checkJobFile(byte[] data, String mimeType, String fileName, JobType type) throws ServiceException {
		if (data == null || data.length == 0) {
			if (type.isInputFile()) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.JOB_FILE_REQUIRED);
			} else {
				if (mimeType != null) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.JOB_FILE_MIME_TYPE_NOT_NULL);
				}
				if (fileName != null) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.JOB_FILE_NAME_NOT_NULL);
				}
			}
		} else {
			if (!type.isInputFile() && !type.isOutputFile()) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.JOB_FILE_NOT_NULL);
			} else {
				Integer jobDataFileSizeLimit = Settings.getIntNullable(SettingCodes.JOB_FILE_SIZE_LIMIT, Bundle.SETTINGS, DefaultSettings.JOB_FILE_SIZE_LIMIT);
				if (jobDataFileSizeLimit != null && data.length > jobDataFileSizeLimit) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.JOB_FILE_SIZE_LIMIT_EXCEEDED,
							CommonUtil.humanReadableByteCount(jobDataFileSizeLimit, CoreUtil.getUser().getDecimalSeparator()));
				}
				if (mimeType == null) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.JOB_FILE_MIME_TYPE_REQUIRED);
				}
				Iterator<MimeType> it = this.getMimeTypeDao().findByMimeTypeModule(mimeType, FileModule.JOB_FILE).iterator();
				if (!it.hasNext()) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.JOB_FILE_MIME_TYPE_UNKNOWN, mimeType);
				}
				if (CommonUtil.isEmptyString(fileName)) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.JOB_FILE_NAME_REQUIRED);
				}
			}
		}
	}

	private void checkUpdateJobInput(JobUpdateVO jobIn, JobType type) throws ServiceException {
		checkJobFile(jobIn.getDatas(), jobIn.getMimeType(), jobIn.getFileName(), type);
	}

	private void checkAddJobInput(JobAddVO jobIn) throws ServiceException {
		JobType type = CheckIDUtil.checkJobTypeId(jobIn.getTypeId(), this.getJobTypeDao());
		switch (type.getModule()) {
			case TRIAL_JOB:
				CheckIDUtil.checkTrialId(jobIn.getTrialId(), this.getTrialDao());
				if (numIdsSet(jobIn) > 1) {
					// no other references must be set...
					throw L10nUtil.initServiceException(ServiceExceptionCodes.JOB_TRIAL_ONLY_ALLOWED);
				}
				break;
			case PROBAND_JOB:
				CheckIDUtil.checkProbandId(jobIn.getProbandId(), this.getProbandDao());
				if (numIdsSet(jobIn) > 1) {
					// no other references must be set...
					throw L10nUtil.initServiceException(ServiceExceptionCodes.JOB_PROBAND_ONLY_ALLOWED);
				}
				break;
			case INPUT_FIELD_JOB:
				CheckIDUtil.checkInputFieldId(jobIn.getInputFieldId(), this.getInputFieldDao());
				if (numIdsSet(jobIn) > 1) {
					// no other references must be set...
					throw L10nUtil.initServiceException(ServiceExceptionCodes.JOB_INPUT_FIELD_ONLY_ALLOWED);
				}
				break;
			case INVENTORY_CRITERIA_JOB:
				if (!DBModule.INVENTORY_DB.equals(CheckIDUtil.checkCriteriaId(jobIn.getCriteriaId(), this.getCriteriaDao()).getModule())) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.JOB_WRONG_CRITERIA_MODULE);
				}
				if (numIdsSet(jobIn) > 1) {
					// no other references must be set...
					throw L10nUtil.initServiceException(ServiceExceptionCodes.JOB_CRITERIA_ONLY_ALLOWED);
				}
				break;
			case STAFF_CRITERIA_JOB:
				if (!DBModule.STAFF_DB.equals(CheckIDUtil.checkCriteriaId(jobIn.getCriteriaId(), this.getCriteriaDao()).getModule())) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.JOB_WRONG_CRITERIA_MODULE);
				}
				if (numIdsSet(jobIn) > 1) {
					// no other references must be set...
					throw L10nUtil.initServiceException(ServiceExceptionCodes.JOB_CRITERIA_ONLY_ALLOWED);
				}
				break;
			case COURSE_CRITERIA_JOB:
				if (!DBModule.COURSE_DB.equals(CheckIDUtil.checkCriteriaId(jobIn.getCriteriaId(), this.getCriteriaDao()).getModule())) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.JOB_WRONG_CRITERIA_MODULE);
				}
				if (numIdsSet(jobIn) > 1) {
					// no other references must be set...
					throw L10nUtil.initServiceException(ServiceExceptionCodes.JOB_CRITERIA_ONLY_ALLOWED);
				}
				break;
			case TRIAL_CRITERIA_JOB:
				if (!DBModule.TRIAL_DB.equals(CheckIDUtil.checkCriteriaId(jobIn.getCriteriaId(), this.getCriteriaDao()).getModule())) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.JOB_WRONG_CRITERIA_MODULE);
				}
				if (numIdsSet(jobIn) > 1) {
					// no other references must be set...
					throw L10nUtil.initServiceException(ServiceExceptionCodes.JOB_CRITERIA_ONLY_ALLOWED);
				}
				break;
			case INPUT_FIELD_CRITERIA_JOB:
				if (!DBModule.INPUT_FIELD_DB.equals(CheckIDUtil.checkCriteriaId(jobIn.getCriteriaId(), this.getCriteriaDao()).getModule())) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.JOB_WRONG_CRITERIA_MODULE);
				}
				if (numIdsSet(jobIn) > 1) {
					// no other references must be set...
					throw L10nUtil.initServiceException(ServiceExceptionCodes.JOB_CRITERIA_ONLY_ALLOWED);
				}
				break;
			case PROBAND_CRITERIA_JOB:
				if (!DBModule.PROBAND_DB.equals(CheckIDUtil.checkCriteriaId(jobIn.getCriteriaId(), this.getCriteriaDao()).getModule())) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.JOB_WRONG_CRITERIA_MODULE);
				}
				if (numIdsSet(jobIn) > 1) {
					// no other references must be set...
					throw L10nUtil.initServiceException(ServiceExceptionCodes.JOB_CRITERIA_ONLY_ALLOWED);
				}
				break;
			case USER_CRITERIA_JOB:
				if (!DBModule.USER_DB.equals(CheckIDUtil.checkCriteriaId(jobIn.getCriteriaId(), this.getCriteriaDao()).getModule())) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.JOB_WRONG_CRITERIA_MODULE);
				}
				if (numIdsSet(jobIn) > 1) {
					// no other references must be set...
					throw L10nUtil.initServiceException(ServiceExceptionCodes.JOB_CRITERIA_ONLY_ALLOWED);
				}
				break;
			case MASS_MAIL_CRITERIA_JOB:
				if (!DBModule.MASS_MAIL_DB.equals(CheckIDUtil.checkCriteriaId(jobIn.getCriteriaId(), this.getCriteriaDao()).getModule())) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.JOB_WRONG_CRITERIA_MODULE);
				}
				if (numIdsSet(jobIn) > 1) {
					// no other references must be set...
					throw L10nUtil.initServiceException(ServiceExceptionCodes.JOB_CRITERIA_ONLY_ALLOWED);
				}
				break;
			default:
				// not supported for now...
				throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.UNSUPPORTED_JOB_MODULE, DefaultMessages.UNSUPPORTED_JOB_MODULE,
						new Object[] { type.getModule().toString() }));
		}
		if ((type.isDaily()
				|| type.isWeekly()
				|| type.isMonthly())
				&& (new JobCollisionFinder(type.getModule(), this.getJobDao(), this.getTrialDao(), this.getProbandDao(), this.getInputFieldDao(), this.getCriteriaDao()))
						.collides(jobIn)) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.JOB_ALREADY_EXISTS, L10nUtil.getJobTypeName(Locales.USER, type.getNameL10nKey()));
		}
		if (!CommonUtil.isEmptyString(jobIn.getEmailRecipients())) {
			if (!type.isEmailRecipients()) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.JOB_EMAIL_RECIPIENTS_NOT_NULL);
			}
			try {
				InternetAddress.parse(jobIn.getEmailRecipients(), true);
			} catch (AddressException e) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.JOB_INVALID_EMAIL_RECIPIENTS, jobIn.getEmailRecipients(), e.getMessage());
			}
		}
		checkJobFile(jobIn.getDatas(), jobIn.getMimeType(), jobIn.getFileName(), type);
	}

	/**
	 * @see org.phoenixctms.ctsms.service.shared.JobService#addJob(AuthenticationVO, JobInVO)
	 */
	protected JobOutVO handleAddJob(AuthenticationVO auth, JobAddVO newJob)
			throws Exception {
		checkAddJobInput(newJob);
		JobDao jobDao = this.getJobDao();
		Job job = jobDao.jobAddVOToEntity(newJob);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		job.setStatus(JobStatus.CREATED);
		job.setJobOutput(CommonUtil.clipString(job.getJobOutput(), JOB_OUTPUT_MAX_LENGTH, EllipsisPlacement.LEADING));
		CoreUtil.modifyVersion(job, now, user);
		job = jobDao.create(job);
		JobOutVO result = jobDao.toJobOutVO(job);
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		switch (job.getType().getModule()) {
			case TRIAL_JOB:
				logSystemMessage(job.getTrial(), result.getTrial(), now, user, SystemMessageCodes.JOB_CREATED, result, null, journalEntryDao);
				break;
			case PROBAND_JOB:
				logSystemMessage(job.getProband(), result.getProband(), now, user, SystemMessageCodes.JOB_CREATED, result, null, journalEntryDao);
				break;
			case INPUT_FIELD_JOB:
				logSystemMessage(job.getInputField(), result.getInputField(), now, user, SystemMessageCodes.JOB_CREATED, result, null, journalEntryDao);
				break;
			case INVENTORY_CRITERIA_JOB:
			case STAFF_CRITERIA_JOB:
			case COURSE_CRITERIA_JOB:
			case TRIAL_CRITERIA_JOB:
			case INPUT_FIELD_CRITERIA_JOB:
			case PROBAND_CRITERIA_JOB:
			case USER_CRITERIA_JOB:
			case MASS_MAIL_CRITERIA_JOB:
				logSystemMessage(job.getCriteria(), result.getCriteria(), now, user, SystemMessageCodes.JOB_CREATED, result, null, journalEntryDao);
				break;
			default:
				// not supported for now...
				throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.UNSUPPORTED_JOB_MODULE, DefaultMessages.UNSUPPORTED_JOB_MODULE,
						new Object[] { job.getType().getModule().toString() }));
		}
		if (!job.getType().isDaily()
				&& !job.getType().isWeekly()
				&& !job.getType().isMonthly()) {
			CoreUtil.launchJob(auth, job, false);
		}
		return result;
	}

	@Override
	protected JobOutVO handleUpdateJob(AuthenticationVO auth, JobUpdateVO modifiedJob) throws Exception {
		JobDao jobDao = this.getJobDao();
		Job originalJob = CheckIDUtil.checkJobId(modifiedJob.getId(), jobDao);
		checkUpdateJobInput(modifiedJob, originalJob.getType());
		JobOutVO original = jobDao.toJobOutVO(originalJob);
		jobDao.evict(originalJob);
		Job job = jobDao.jobUpdateVOToEntity(modifiedJob);
		job.setJobOutput(CommonUtil.clipString(job.getJobOutput(), JOB_OUTPUT_MAX_LENGTH, EllipsisPlacement.LEADING));
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(originalJob, job, now, user);
		jobDao.update(job);
		JobOutVO result = jobDao.toJobOutVO(job);
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		switch (job.getType().getModule()) {
			case TRIAL_JOB:
				logSystemMessage(job.getTrial(), result.getTrial(), now, user, SystemMessageCodes.JOB_UPDATED, result, original, journalEntryDao);
				break;
			case PROBAND_JOB:
				logSystemMessage(job.getProband(), result.getProband(), now, user, SystemMessageCodes.JOB_UPDATED, result, original, journalEntryDao);
				break;
			case INPUT_FIELD_JOB:
				logSystemMessage(job.getInputField(), result.getInputField(), now, user, SystemMessageCodes.JOB_UPDATED, result, original, journalEntryDao);
				break;
			case INVENTORY_CRITERIA_JOB:
			case STAFF_CRITERIA_JOB:
			case COURSE_CRITERIA_JOB:
			case TRIAL_CRITERIA_JOB:
			case INPUT_FIELD_CRITERIA_JOB:
			case PROBAND_CRITERIA_JOB:
			case USER_CRITERIA_JOB:
			case MASS_MAIL_CRITERIA_JOB:
				logSystemMessage(job.getCriteria(), result.getCriteria(), now, user, SystemMessageCodes.JOB_UPDATED, result, original, journalEntryDao);
				break;
			default:
				// not supported for now...
				throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.UNSUPPORTED_JOB_MODULE, DefaultMessages.UNSUPPORTED_JOB_MODULE,
						new Object[] { job.getType().getModule().toString() }));
		}
		return result;
	}

	@Override
	protected JobOutVO handleDeleteJob(AuthenticationVO auth, Long jobId) throws Exception {
		JobDao jobDao = this.getJobDao();
		Job job = CheckIDUtil.checkJobId(jobId, jobDao);
		JobOutVO result = jobDao.toJobOutVO(job);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		switch (job.getType().getModule()) {
			case TRIAL_JOB:
				Trial trial = job.getTrial();
				trial.removeJobs(job);
				job.setTrial(null);
				jobDao.remove(job);
				logSystemMessage(trial, result.getTrial(), now, user, SystemMessageCodes.JOB_DELETED, result, null, journalEntryDao);
				break;
			case PROBAND_JOB:
				Proband proband = job.getProband();
				proband.removeJobs(job);
				job.setProband(null);
				jobDao.remove(job);
				logSystemMessage(proband, result.getProband(), now, user, SystemMessageCodes.JOB_DELETED, result, null, journalEntryDao);
				break;
			case INPUT_FIELD_JOB:
				InputField inputField = job.getInputField();
				inputField.removeJobs(job);
				job.setInputField(null);
				jobDao.remove(job);
				logSystemMessage(inputField, result.getInputField(), now, user, SystemMessageCodes.JOB_DELETED, result, null, journalEntryDao);
				break;
			case INVENTORY_CRITERIA_JOB:
			case STAFF_CRITERIA_JOB:
			case COURSE_CRITERIA_JOB:
			case TRIAL_CRITERIA_JOB:
			case INPUT_FIELD_CRITERIA_JOB:
			case PROBAND_CRITERIA_JOB:
			case USER_CRITERIA_JOB:
			case MASS_MAIL_CRITERIA_JOB:
				Criteria criteria = job.getCriteria();
				criteria.removeJobs(job);
				job.setCriteria(null);
				jobDao.remove(job);
				logSystemMessage(criteria, result.getCriteria(), now, user, SystemMessageCodes.JOB_DELETED, result, null, journalEntryDao);
				break;
			default:
				// not supported for now...
				throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.UNSUPPORTED_JOB_MODULE, DefaultMessages.UNSUPPORTED_JOB_MODULE,
						new Object[] { job.getType().getModule().toString() }));
		}
		return result;
	}

	@Override
	protected JobOutVO handleGetJob(AuthenticationVO auth, Long jobId) throws Exception {
		JobDao jobDao = this.getJobDao();
		Job job = CheckIDUtil.checkJobId(jobId, jobDao);
		JobOutVO result = jobDao.toJobOutVO(job);
		return result;
	}

	@Override
	protected Collection<JobOutVO> handleGetJobs(AuthenticationVO auth, JobModule module, Long id, PSFVO psf) throws Exception {
		checkJobModuleId(module, id);
		JobDao jobDao = this.getJobDao();
		Collection jobs = jobDao.findJobs(module, id, psf);
		jobDao.toJobOutVOCollection(jobs);
		return jobs;
	}

	@Override
	protected long handleGetJobCount(AuthenticationVO auth, JobModule module, Long id) throws Exception {
		checkJobModuleId(module, id);
		return this.getJobDao().getCount(module, id);
	}

	@Override
	protected JobFileVO handleGetJobFile(AuthenticationVO auth, Long jobId) throws Exception {
		JobDao jobDao = this.getJobDao();
		Job job = CheckIDUtil.checkJobId(jobId, jobDao);
		JobFileVO result = jobDao.toJobFileVO(job);
		return result;
	}
}