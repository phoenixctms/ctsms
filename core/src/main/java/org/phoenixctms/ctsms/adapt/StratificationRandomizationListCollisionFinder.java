package org.phoenixctms.ctsms.adapt;

import java.util.Collection;
import java.util.HashSet;

import org.hibernate.LockMode;
import org.phoenixctms.ctsms.domain.StratificationRandomizationList;
import org.phoenixctms.ctsms.domain.StratificationRandomizationListDao;
import org.phoenixctms.ctsms.domain.Trial;
import org.phoenixctms.ctsms.domain.TrialDao;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.vo.StratificationRandomizationListInVO;

public class StratificationRandomizationListCollisionFinder extends CollisionFinder<StratificationRandomizationListInVO, StratificationRandomizationList, Trial> {

	private TrialDao trialDao;
	private StratificationRandomizationListDao stratificationRandomizationListDao;

	public StratificationRandomizationListCollisionFinder(TrialDao trialDao, StratificationRandomizationListDao stratificationRandomizationListDao) {
		this.trialDao = trialDao;
		this.stratificationRandomizationListDao = stratificationRandomizationListDao;
	}

	@Override
	protected Trial aquireWriteLock(StratificationRandomizationListInVO in) throws ServiceException {
		return CheckIDUtil.checkTrialId(in.getTrialId(), trialDao, LockMode.PESSIMISTIC_WRITE);
	}

	@Override
	protected boolean equals(StratificationRandomizationListInVO in,
			StratificationRandomizationList existing) {
		return in.getId().equals(existing.getId());
	}

	@Override
	protected Collection<StratificationRandomizationList> getCollidingItems(
			StratificationRandomizationListInVO in, Trial root) {
		return stratificationRandomizationListDao.findByTrialTagValues(in.getTrialId(), new HashSet<Long>(in.getSelectionSetValueIds()));
	}

	@Override
	protected boolean isNew(StratificationRandomizationListInVO in) {
		return in.getId() == null;
	}
}
