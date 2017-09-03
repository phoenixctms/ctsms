package org.phoenixctms.ctsms.adapt;

import java.util.Collection;

import org.hibernate.LockMode;

import org.phoenixctms.ctsms.domain.Trial;
import org.phoenixctms.ctsms.domain.TrialDao;
import org.phoenixctms.ctsms.domain.VisitScheduleItem;
import org.phoenixctms.ctsms.domain.VisitScheduleItemDao;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.vo.VisitScheduleItemInVO;

public class VisitScheduleItemCollisionFinder extends CollisionFinder<VisitScheduleItemInVO, VisitScheduleItem, Trial> {

	private TrialDao trialDao;
	private VisitScheduleItemDao visitScheduleItemDao;

	public VisitScheduleItemCollisionFinder(TrialDao trialDao, VisitScheduleItemDao visitScheduleItemDao) {
		this.visitScheduleItemDao = visitScheduleItemDao;
		this.trialDao = trialDao;
	}

	@Override
	protected Trial aquireWriteLock(VisitScheduleItemInVO in)
			throws ServiceException {
		return CheckIDUtil.checkTrialId(in.getTrialId(), trialDao, LockMode.PESSIMISTIC_WRITE);
	}

	@Override
	protected boolean equals(VisitScheduleItemInVO in,
			VisitScheduleItem existing) {
		return in.getId().equals(existing.getId());
	}

	@Override
	protected Collection<VisitScheduleItem> getCollidingItems(
			VisitScheduleItemInVO in, Trial root) {
		return visitScheduleItemDao.findCollidingTrialGroupVisit(in.getTrialId(), in.getGroupId(), in.getVisitId());
	}

	@Override
	protected boolean isNew(VisitScheduleItemInVO in) {
		return in.getId() == null;
	}

	@Override
	protected boolean match(VisitScheduleItemInVO in,
			VisitScheduleItem existing, Trial root) {
		String inToken = in.getToken();
		String existingToken = existing.getToken();
		if (inToken == null || inToken.length() == 0) {
			if (existingToken == null || existingToken.length() == 0) {
				return true;
			}
		} else {
			if (inToken.equals(existingToken)) {
				return true;
			}
		}
		return false;
	}
}
