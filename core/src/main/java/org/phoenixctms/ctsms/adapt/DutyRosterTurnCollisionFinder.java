package org.phoenixctms.ctsms.adapt;

import java.util.Collection;
import java.util.Date;

import org.hibernate.LockMode;

import org.phoenixctms.ctsms.domain.DutyRosterTurn;
import org.phoenixctms.ctsms.domain.DutyRosterTurnDao;
import org.phoenixctms.ctsms.domain.Staff;
import org.phoenixctms.ctsms.domain.StaffDao;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.DutyRosterTurnInVO;

public class DutyRosterTurnCollisionFinder extends OverlapFinder<DutyRosterTurnInVO, DutyRosterTurn, Staff> {

	private StaffDao staffDao;
	private DutyRosterTurnDao dutyRosterTurnDao;

	public DutyRosterTurnCollisionFinder(StaffDao staffDao, DutyRosterTurnDao dutyRosterTurnDao) {
		this.dutyRosterTurnDao = dutyRosterTurnDao;
		this.staffDao = staffDao;
	}

	@Override
	protected Staff aquireWriteLock(DutyRosterTurnInVO in, boolean lock)
			throws ServiceException {
		if (in.getStaffId() != null) {
			if (lock) {
				return CheckIDUtil.checkStaffId(in.getStaffId(), staffDao, LockMode.PESSIMISTIC_WRITE);
			} else {
				return CheckIDUtil.checkStaffId(in.getStaffId(), staffDao);
			}
		}
		return null;
	}

	@Override
	protected boolean equals(DutyRosterTurnInVO in, DutyRosterTurn existing) {
		return in.getId().equals(existing.getId());
	}

	@Override
	protected Collection<DutyRosterTurn> getCollidingItems(DutyRosterTurnInVO in, Staff root) {
		return dutyRosterTurnDao.findByStaffTrialCalendarInterval(in.getStaffId(), null, null, CommonUtil.dateToTimestamp(in.getStart()), CommonUtil.dateToTimestamp(in.getStop()));
	}

	@Override
	protected Date getInStart(DutyRosterTurnInVO in) {
		return in.getStart();
	}

	@Override
	protected Date getInStop(DutyRosterTurnInVO in) {
		return in.getStop();
	}

	@Override
	protected long getMaxOverlappingEvents(Staff root) throws ServiceException {
		if (root != null) {
			return root.getMaxOverlappingShifts();
		} else {
			return 1l;
		}
	}

	@Override
	protected Date getStart(DutyRosterTurn collidingItem) {
		return collidingItem.getStart();
	}

	@Override
	protected Date getStop(DutyRosterTurn collidingItem) {
		return collidingItem.getStop();
	}

	@Override
	protected boolean isNew(DutyRosterTurnInVO in) {
		return in.getId() == null;
	}
}
