
package org.phoenixctms.ctsms.adapt;

import java.util.Collection;

import org.hibernate.LockMode;
import org.phoenixctms.ctsms.domain.ProbandListEntryTag;
import org.phoenixctms.ctsms.domain.ProbandListEntryTagDao;
import org.phoenixctms.ctsms.domain.Trial;
import org.phoenixctms.ctsms.domain.TrialDao;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagInVO;

public class ProbandListEntryTagRandomizeCollisionFinder extends CollisionFinder<ProbandListEntryTagInVO, ProbandListEntryTag, Trial> {

	private TrialDao trialDao;
	private ProbandListEntryTagDao probandListEntryTagDao;

	public ProbandListEntryTagRandomizeCollisionFinder(TrialDao trialDao, ProbandListEntryTagDao probandListEntryTagDao) {
		this.probandListEntryTagDao = probandListEntryTagDao;
		this.trialDao = trialDao;
	}

	@Override
	protected Trial aquireWriteLock(ProbandListEntryTagInVO in)
			throws ServiceException {
		return CheckIDUtil.checkTrialId(in.getTrialId(), trialDao, LockMode.PESSIMISTIC_WRITE);
	}

	@Override
	protected boolean equals(ProbandListEntryTagInVO in,
			ProbandListEntryTag existing) {
		return in.getId().equals(existing.getId());
	}

	@Override
	protected Collection<ProbandListEntryTag> getCollidingItems(
			ProbandListEntryTagInVO in, Trial root) {
		return probandListEntryTagDao.findByTrialFieldStratificationRandomize(in.getTrialId(), null, null, true);
	}

	@Override
	protected boolean isNew(ProbandListEntryTagInVO in) {
		return in.getId() == null;
	}
}
