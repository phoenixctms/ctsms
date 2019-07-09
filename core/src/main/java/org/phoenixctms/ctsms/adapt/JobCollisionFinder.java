package org.phoenixctms.ctsms.adapt;

import java.util.Collection;

import org.hibernate.LockMode;
import org.phoenixctms.ctsms.domain.CriteriaDao;
import org.phoenixctms.ctsms.domain.InputFieldDao;
import org.phoenixctms.ctsms.domain.Job;
import org.phoenixctms.ctsms.domain.JobDao;
import org.phoenixctms.ctsms.domain.ProbandDao;
import org.phoenixctms.ctsms.domain.TrialDao;
import org.phoenixctms.ctsms.enumeration.JobModule;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.DefaultMessages;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.MessageCodes;
import org.phoenixctms.ctsms.vo.JobAddVO;

public class JobCollisionFinder<ROOT> extends CollisionFinder<JobAddVO, Job, ROOT> {

	private JobDao jobDao;
	private TrialDao trialDao;
	private ProbandDao probandDao;
	private InputFieldDao inputFieldDao;
	private CriteriaDao criteriaDao;
	private JobModule module;

	public JobCollisionFinder(JobModule module, JobDao jobDao, TrialDao trialDao,
			ProbandDao probandDao, InputFieldDao inputFieldDao, CriteriaDao criteriaDao) {
		this.module = module;
		this.jobDao = jobDao;
		this.trialDao = trialDao;
		this.probandDao = probandDao;
		this.inputFieldDao = inputFieldDao;
		this.criteriaDao = criteriaDao;
	}

	@Override
	protected boolean equals(JobAddVO in, Job existing) {
		return false;
	}

	@Override
	protected Collection<Job> getCollidingItems(
			JobAddVO in, ROOT root) {
		try {
			return jobDao.findByType(module, CommonUtil.getEntityId(root), in.getTypeId());
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	protected boolean isNew(JobAddVO in) {
		return true;
	}

	@Override
	protected ROOT aquireWriteLock(JobAddVO in) throws ServiceException {
		switch (module) {
			case TRIAL_JOB:
				return (ROOT) trialDao.load(in.getTrialId(), LockMode.PESSIMISTIC_WRITE);
			case PROBAND_JOB:
				return (ROOT) probandDao.load(in.getProbandId(), LockMode.PESSIMISTIC_WRITE);
			case INPUT_FIELD_JOB:
				return (ROOT) inputFieldDao.load(in.getInputFieldId(), LockMode.PESSIMISTIC_WRITE);
			case INVENTORY_CRITERIA_JOB:
			case STAFF_CRITERIA_JOB:
			case COURSE_CRITERIA_JOB:
			case TRIAL_CRITERIA_JOB:
			case INPUT_FIELD_CRITERIA_JOB:
			case PROBAND_CRITERIA_JOB:
			case USER_CRITERIA_JOB:
			case MASS_MAIL_CRITERIA_JOB:
				return (ROOT) criteriaDao.load(in.getCriteriaId(), LockMode.PESSIMISTIC_WRITE);
			default:
				// not supported for now...
				throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.UNSUPPORTED_JOB_MODULE, DefaultMessages.UNSUPPORTED_JOB_MODULE,
						new Object[] { module.toString() }));
		}
	}
}
