package org.phoenixctms.ctsms.adapt;

import java.util.Collection;

import org.hibernate.LockMode;
import org.phoenixctms.ctsms.domain.Proband;
import org.phoenixctms.ctsms.domain.ProbandDao;
import org.phoenixctms.ctsms.domain.ProbandStatusEntry;
import org.phoenixctms.ctsms.domain.ProbandStatusEntryDao;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.ProbandStatusEntryInVO;

public class ProbandStatusEntryCollisionFinder extends CollisionFinder<ProbandStatusEntryInVO, ProbandStatusEntry, Proband> {

	private ProbandDao probandDao;
	private ProbandStatusEntryDao probandStatusEntryDao;

	public ProbandStatusEntryCollisionFinder(ProbandDao probandDao, ProbandStatusEntryDao probandStatusEntryDao) {
		this.probandStatusEntryDao = probandStatusEntryDao;
		this.probandDao = probandDao;
	}

	@Override
	protected Proband aquireWriteLock(ProbandStatusEntryInVO in) throws ServiceException {
		return CheckIDUtil.checkProbandId(in.getProbandId(), probandDao, LockMode.PESSIMISTIC_WRITE);
	}

	@Override
	protected boolean equals(ProbandStatusEntryInVO in, ProbandStatusEntry existing) {
		return in.getId().equals(existing.getId());
	}

	@Override
	protected Collection<ProbandStatusEntry> getCollidingItems(
			ProbandStatusEntryInVO in, Proband root) {
		return probandStatusEntryDao.findByProbandInterval(in.getProbandId(), CommonUtil.dateToTimestamp(in.getStart()), CommonUtil.dateToTimestamp(in.getStop()), false, null);
	}

	@Override
	protected boolean isNew(ProbandStatusEntryInVO in) {
		return in.getId() == null;
	}
}
