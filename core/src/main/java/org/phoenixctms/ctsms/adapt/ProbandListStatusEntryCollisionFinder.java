package org.phoenixctms.ctsms.adapt;

import java.sql.Timestamp;
import java.util.Collection;

import org.hibernate.LockMode;
import org.phoenixctms.ctsms.domain.Proband;
import org.phoenixctms.ctsms.domain.ProbandDao;
import org.phoenixctms.ctsms.domain.ProbandListEntry;
import org.phoenixctms.ctsms.domain.ProbandListEntryDao;
import org.phoenixctms.ctsms.domain.ProbandListStatusEntry;
import org.phoenixctms.ctsms.domain.ProbandListStatusEntryDao;
import org.phoenixctms.ctsms.domain.Trial;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.ProbandListStatusEntryInVO;

public class ProbandListStatusEntryCollisionFinder extends BlockingProbandListStatusCollisionFinder<ProbandListStatusEntryInVO, ProbandListStatusEntry> {// ProbandListStatusEntryCollisionFinderBase<ProbandListStatusEntryInVO>{

	private ProbandListEntryDao probandListEntryDao;
	private ProbandListStatusEntryDao probandListStatusEntryDao;
	private ProbandDao probandDao;

	public ProbandListStatusEntryCollisionFinder(ProbandDao probandDao, ProbandListEntryDao probandListEntryDao, ProbandListStatusEntryDao probandListStatusEntryDao) {
		this.probandListStatusEntryDao = probandListStatusEntryDao;
		this.probandListEntryDao = probandListEntryDao;
		this.probandDao = probandDao;
	}

	@Override
	protected Proband aquireWriteLock(ProbandListStatusEntryInVO in) throws ServiceException {
		ProbandListEntry listEntry = CheckIDUtil.checkProbandListEntryId(in.getListEntryId(), probandListEntryDao);
		Proband proband = listEntry.getProband();
		probandDao.lock(proband, LockMode.PESSIMISTIC_WRITE);
		return proband;
		// return ServiceUtil.checkProbandId(listEntry.getProband().getId(), probandDao, LockMode.PESSIMISTIC_WRITE);
	}

	@Override
	protected boolean equals(ProbandListStatusEntryInVO in,
			ProbandListStatusEntry existing) {
		return in.getId().equals(existing.getId());
	}

	@Override
	protected Collection<ProbandListStatusEntry> getCollidingItems(
			ProbandListStatusEntryInVO in, Proband root) throws ServiceException {
		Long probandId;
		if (root == null) {
			ProbandListEntry listEntry = CheckIDUtil.checkProbandListEntryId(in.getListEntryId(), probandListEntryDao);
			probandId = listEntry.getProband().getId();
		} else {
			probandId = root.getId();
		}
		return probandListStatusEntryDao.findByTrialProband(null, probandId, true, null);
	}

	@Override
	protected ProbandListStatusEntry getLastStatus(
			ProbandListStatusEntry existing) {
		return existing;
	}

	@Override
	protected Timestamp getRealTimestamp(ProbandListStatusEntryInVO in) {
		return CommonUtil.dateToTimestamp(in.getRealTimestamp());
	}

	@Override
	protected Trial getTrial(ProbandListStatusEntryInVO in)
			throws ServiceException {
		ProbandListEntry listEntry = CheckIDUtil.checkProbandListEntryId(in.getListEntryId(), probandListEntryDao);
		return listEntry.getTrial();
	}

	@Override
	protected boolean isNew(ProbandListStatusEntryInVO in) {
		return in.getId() == null;
	}
}
