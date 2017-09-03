package org.phoenixctms.ctsms.adapt;

import java.util.Collection;

import org.hibernate.LockMode;

import org.phoenixctms.ctsms.domain.Staff;
import org.phoenixctms.ctsms.domain.StaffDao;
import org.phoenixctms.ctsms.domain.StaffStatusEntry;
import org.phoenixctms.ctsms.domain.StaffStatusEntryDao;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.StaffStatusEntryInVO;

public class StaffStatusEntryCollisionFinder extends CollisionFinder<StaffStatusEntryInVO, StaffStatusEntry, Staff> {

	private StaffDao staffDao;
	private StaffStatusEntryDao staffStatusEntryDao;

	public StaffStatusEntryCollisionFinder(StaffDao staffDao, StaffStatusEntryDao staffStatusEntryDao) {
		this.staffStatusEntryDao = staffStatusEntryDao;
		this.staffDao = staffDao;
	}

	@Override
	protected Staff aquireWriteLock(StaffStatusEntryInVO in)
			throws ServiceException {
		return CheckIDUtil.checkStaffId(in.getStaffId(), staffDao, LockMode.PESSIMISTIC_WRITE);
	}

	@Override
	protected boolean equals(StaffStatusEntryInVO in, StaffStatusEntry existing) {
		return in.getId().equals(existing.getId());
	}

	@Override
	protected Collection<StaffStatusEntry> getCollidingItems(
			StaffStatusEntryInVO in, Staff root) {
		return staffStatusEntryDao.findByStaffInterval(in.getStaffId(), CommonUtil.dateToTimestamp(in.getStart()), CommonUtil.dateToTimestamp(in.getStop()), false);
	}

	@Override
	protected boolean isNew(StaffStatusEntryInVO in) {
		return in.getId() == null;
	}
}
