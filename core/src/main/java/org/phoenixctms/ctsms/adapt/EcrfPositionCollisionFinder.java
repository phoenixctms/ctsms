package org.phoenixctms.ctsms.adapt;

import java.util.Collection;

import org.hibernate.LockMode;
import org.phoenixctms.ctsms.domain.ECRF;
import org.phoenixctms.ctsms.domain.ECRFDao;
import org.phoenixctms.ctsms.domain.Trial;
import org.phoenixctms.ctsms.domain.TrialDao;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.vo.ECRFInVO;

public class EcrfPositionCollisionFinder extends CollisionFinder<ECRFInVO, ECRF, Trial> {

	private TrialDao trialDao;
	private ECRFDao ecrfDao;

	public EcrfPositionCollisionFinder(TrialDao trialDao, ECRFDao ecrfDao) {
		this.ecrfDao = ecrfDao;
		this.trialDao = trialDao;
	}

	@Override
	protected Trial aquireWriteLock(ECRFInVO in)
			throws ServiceException {
		return CheckIDUtil.checkTrialId(in.getTrialId(), trialDao, LockMode.PESSIMISTIC_WRITE);
	}

	@Override
	protected boolean equals(ECRFInVO in,
			ECRF existing) {
		return in.getId().equals(existing.getId());
	}

	@Override
	protected Collection<ECRF> getCollidingItems(
			ECRFInVO in, Trial root) {
		return ecrfDao.findCollidingTrialGroupPosition(in.getTrialId(), in.getGroupId(), in.getPosition());
	}

	@Override
	protected boolean isNew(ECRFInVO in) {
		return in.getId() == null;
	}
}

