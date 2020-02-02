package org.phoenixctms.ctsms.adapt;

import java.sql.Timestamp;
import java.util.Collection;

import org.hibernate.LockMode;
import org.phoenixctms.ctsms.domain.Proband;
import org.phoenixctms.ctsms.domain.ProbandDao;
import org.phoenixctms.ctsms.domain.ProbandListEntry;
import org.phoenixctms.ctsms.domain.ProbandListEntryDao;
import org.phoenixctms.ctsms.domain.ProbandListStatusEntry;
import org.phoenixctms.ctsms.domain.Trial;
import org.phoenixctms.ctsms.domain.TrialDao;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.vo.ProbandListEntryInVO;

public class ProbandListEntryStatusCollisionFinder extends BlockingProbandListStatusCollisionFinder<ProbandListEntryInVO, ProbandListEntry> {

	private ProbandListEntryDao probandListEntryDao;
	private ProbandDao probandDao;
	private TrialDao trialDao;
	private Timestamp now;

	public ProbandListEntryStatusCollisionFinder(ProbandDao probandDao, TrialDao trialDao, ProbandListEntryDao probandListEntryDao, Timestamp now) {
		this.probandListEntryDao = probandListEntryDao;
		this.probandDao = probandDao;
		this.trialDao = trialDao;
		this.now = now;
	}

	@Override
	protected Proband aquireWriteLock(ProbandListEntryInVO in) throws ServiceException {
		return CheckIDUtil.checkProbandId(in.getProbandId(), probandDao, LockMode.PESSIMISTIC_WRITE);
	}

	@Override
	protected boolean equals(ProbandListEntryInVO in,
			ProbandListEntry existing) {
		return in.getId().equals(existing.getId());
	}

	@Override
	protected Collection<ProbandListEntry> getCollidingItems(
			ProbandListEntryInVO in, Proband root) throws ServiceException {
		return probandListEntryDao.findByTrialGroupProbandCountPerson(null, null, in.getProbandId(), true, null, null);
	}

	@Override
	protected ProbandListStatusEntry getLastStatus(ProbandListEntry existing) {
		return existing.getLastStatus();
	}

	@Override
	protected Timestamp getRealTimestamp(ProbandListEntryInVO in) {
		return now;
	}

	@Override
	protected Trial getTrial(ProbandListEntryInVO in) throws ServiceException {
		return CheckIDUtil.checkTrialId(in.getTrialId(), trialDao);
	}

	@Override
	protected boolean isNew(ProbandListEntryInVO in) {
		return in.getId() == null;
	}
}
