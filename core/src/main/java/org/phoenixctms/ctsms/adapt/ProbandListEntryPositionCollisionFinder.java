package org.phoenixctms.ctsms.adapt;

import java.util.Collection;

import org.hibernate.LockMode;

import org.phoenixctms.ctsms.domain.ProbandListEntry;
import org.phoenixctms.ctsms.domain.ProbandListEntryDao;
import org.phoenixctms.ctsms.domain.Trial;
import org.phoenixctms.ctsms.domain.TrialDao;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.vo.ProbandListEntryInVO;

public class ProbandListEntryPositionCollisionFinder extends CollisionFinder<ProbandListEntryInVO, ProbandListEntry, Trial> {

	private TrialDao trialDao;
	private ProbandListEntryDao probandListEntryDao;

	public ProbandListEntryPositionCollisionFinder(TrialDao trialDao, ProbandListEntryDao probandListEntryDao) {
		this.probandListEntryDao = probandListEntryDao;
		this.trialDao = trialDao;
	}

	@Override
	protected Trial aquireWriteLock(ProbandListEntryInVO in) throws ServiceException {
		return CheckIDUtil.checkTrialId(in.getTrialId(), trialDao, LockMode.PESSIMISTIC_WRITE);
	}

	@Override
	protected boolean equals(ProbandListEntryInVO in,
			ProbandListEntry existing) {
		return in.getId().equals(existing.getId());
	}

	@Override
	protected Collection<ProbandListEntry> getCollidingItems(
			ProbandListEntryInVO in, Trial root) {
		return probandListEntryDao.findByTrialPosition(in.getTrialId(), in.getPosition());
	}

	@Override
	protected boolean isNew(ProbandListEntryInVO in) {
		return in.getId() == null;
	}
}