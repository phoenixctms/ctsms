package org.phoenixctms.ctsms.adapt;

import java.util.Collection;

import org.hibernate.LockMode;

import org.phoenixctms.ctsms.domain.ProbandListEntry;
import org.phoenixctms.ctsms.domain.ProbandListEntryDao;
import org.phoenixctms.ctsms.domain.ProbandListEntryTagValue;
import org.phoenixctms.ctsms.domain.ProbandListEntryTagValueDao;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagValueInVO;

public class ProbandListEntryTagValueCollisionFinder extends CollisionFinder<ProbandListEntryTagValueInVO, ProbandListEntryTagValue, ProbandListEntry> {

	private ProbandListEntryDao probandListEntryDao;
	private ProbandListEntryTagValueDao probandListEntryTagValueDao;

	public ProbandListEntryTagValueCollisionFinder(ProbandListEntryDao probandListEntryDao, ProbandListEntryTagValueDao probandListEntryTagValueDao) {
		this.probandListEntryTagValueDao = probandListEntryTagValueDao;
		this.probandListEntryDao = probandListEntryDao;
	}

	@Override
	protected ProbandListEntry aquireWriteLock(ProbandListEntryTagValueInVO in) throws ServiceException {
		return CheckIDUtil.checkProbandListEntryId(in.getListEntryId(), probandListEntryDao, LockMode.PESSIMISTIC_WRITE);
	}

	@Override
	protected boolean equals(ProbandListEntryTagValueInVO in,
			ProbandListEntryTagValue existing) {
		return in.getId().equals(existing.getId());
	}

	@Override
	protected Collection<ProbandListEntryTagValue> getCollidingItems(
			ProbandListEntryTagValueInVO in, ProbandListEntry root) {
		return probandListEntryTagValueDao.findByListEntryListEntryTag(in.getListEntryId(), in.getTagId());
	}

	@Override
	protected boolean isNew(ProbandListEntryTagValueInVO in) {
		return in.getId() == null;
	}
}
