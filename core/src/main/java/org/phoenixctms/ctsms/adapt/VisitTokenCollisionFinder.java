package org.phoenixctms.ctsms.adapt;

import java.util.Collection;

import org.phoenixctms.ctsms.domain.Trial;
import org.phoenixctms.ctsms.domain.TrialDao;
import org.phoenixctms.ctsms.domain.Visit;
import org.phoenixctms.ctsms.domain.VisitDao;
import org.phoenixctms.ctsms.vo.VisitInVO;

public class VisitTokenCollisionFinder extends VisitCollisionFinder {

	public VisitTokenCollisionFinder(TrialDao trialDao, VisitDao visitDao) {
		super(trialDao, visitDao);
	}

	@Override
	protected Collection<Visit> getCollidingItems(
			VisitInVO in, Trial root) {
		return visitDao.findByTrialTitleToken(in.getTrialId(), null, in.getToken());
	}
}
