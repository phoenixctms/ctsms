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

public class ProbandListEntryProbandCollisionFinder extends CollisionFinder<ProbandListEntryInVO, ProbandListEntry, Trial> {

	private TrialDao trialDao;
	private ProbandListEntryDao probandListEntryDao;

	public ProbandListEntryProbandCollisionFinder(TrialDao trialDao, ProbandListEntryDao probandListEntryDao) {
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
		return probandListEntryDao.findByTrialGroupProbandCountPerson(in.getTrialId(), null, in.getProbandId(), true, null, null);
	}

	@Override
	protected boolean isNew(ProbandListEntryInVO in) {
		return in.getId() == null;
	}
}