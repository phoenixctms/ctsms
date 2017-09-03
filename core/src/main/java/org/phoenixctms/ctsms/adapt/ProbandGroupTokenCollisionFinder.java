package org.phoenixctms.ctsms.adapt;

import java.util.Collection;

import org.phoenixctms.ctsms.domain.ProbandGroup;
import org.phoenixctms.ctsms.domain.ProbandGroupDao;
import org.phoenixctms.ctsms.domain.Trial;
import org.phoenixctms.ctsms.domain.TrialDao;
import org.phoenixctms.ctsms.vo.ProbandGroupInVO;

public class ProbandGroupTokenCollisionFinder extends ProbandGroupCollisionFinder {

	public ProbandGroupTokenCollisionFinder(TrialDao trialDao, ProbandGroupDao probandGroupDao) {
		super(trialDao, probandGroupDao);
	}

	@Override
	protected Collection<ProbandGroup> getCollidingItems(
			ProbandGroupInVO in, Trial root) {
		return probandGroupDao.findByTrialTitleToken(in.getTrialId(), null, in.getToken());
	}
}
