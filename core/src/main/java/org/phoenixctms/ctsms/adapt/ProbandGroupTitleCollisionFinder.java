package org.phoenixctms.ctsms.adapt;

import java.util.Collection;

import org.phoenixctms.ctsms.domain.ProbandGroup;
import org.phoenixctms.ctsms.domain.ProbandGroupDao;
import org.phoenixctms.ctsms.domain.Trial;
import org.phoenixctms.ctsms.domain.TrialDao;
import org.phoenixctms.ctsms.vo.ProbandGroupInVO;

public class ProbandGroupTitleCollisionFinder extends ProbandGroupCollisionFinder {

	public ProbandGroupTitleCollisionFinder(TrialDao trialDao, ProbandGroupDao probandGroupDao) {
		super(trialDao, probandGroupDao);
	}

	@Override
	protected Collection<ProbandGroup> getCollidingItems(
			ProbandGroupInVO in, Trial root) {
		return probandGroupDao.findByTrialTitleToken(in.getTrialId(), in.getTitle(), null);
	}
}
