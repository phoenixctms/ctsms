package org.phoenixctms.ctsms.adapt;

import org.hibernate.LockMode;

import org.phoenixctms.ctsms.domain.ProbandGroup;
import org.phoenixctms.ctsms.domain.ProbandGroupDao;
import org.phoenixctms.ctsms.domain.Trial;
import org.phoenixctms.ctsms.domain.TrialDao;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.vo.ProbandGroupInVO;

public abstract class ProbandGroupCollisionFinder extends CollisionFinder<ProbandGroupInVO, ProbandGroup, Trial> {

	private TrialDao trialDao;
	protected ProbandGroupDao probandGroupDao;

	public ProbandGroupCollisionFinder(TrialDao trialDao, ProbandGroupDao probandGroupDao) {
		this.probandGroupDao = probandGroupDao;
		this.trialDao = trialDao;
	}

	@Override
	protected Trial aquireWriteLock(ProbandGroupInVO in) throws ServiceException {
		return CheckIDUtil.checkTrialId(in.getTrialId(), trialDao, LockMode.PESSIMISTIC_WRITE);
	}

	@Override
	protected boolean equals(ProbandGroupInVO in,
			ProbandGroup existing) {
		return in.getId().equals(existing.getId());
	}

	@Override
	protected boolean isNew(ProbandGroupInVO in) {
		return in.getId() == null;
	}
}
