package org.phoenixctms.ctsms.adapt;

import org.hibernate.LockMode;

import org.phoenixctms.ctsms.domain.Trial;
import org.phoenixctms.ctsms.domain.TrialDao;
import org.phoenixctms.ctsms.domain.Visit;
import org.phoenixctms.ctsms.domain.VisitDao;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.vo.VisitInVO;

public abstract class VisitCollisionFinder extends CollisionFinder<VisitInVO, Visit, Trial> {

	private TrialDao trialDao;
	protected VisitDao visitDao;

	public VisitCollisionFinder(TrialDao trialDao, VisitDao visitDao) {
		this.visitDao = visitDao;
		this.trialDao = trialDao;
	}

	@Override
	protected Trial aquireWriteLock(VisitInVO in)
			throws ServiceException {
		return CheckIDUtil.checkTrialId(in.getTrialId(), trialDao, LockMode.PESSIMISTIC_WRITE);
	}

	@Override
	protected boolean equals(VisitInVO in,
			Visit existing) {
		return in.getId().equals(existing.getId());
	}

	@Override
	protected boolean isNew(VisitInVO in) {
		return in.getId() == null;
	}
}