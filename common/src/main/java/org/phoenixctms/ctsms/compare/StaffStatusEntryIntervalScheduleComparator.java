package org.phoenixctms.ctsms.compare;

import java.util.Date;

import org.phoenixctms.ctsms.vo.StaffStatusEntryOutVO;

public class StaffStatusEntryIntervalScheduleComparator extends IntervalScheduleComparatorBase<StaffStatusEntryOutVO> {

	public StaffStatusEntryIntervalScheduleComparator(boolean desc) {
		super(desc);
	}

	@Override
	protected Date getStart(StaffStatusEntryOutVO item) {
		return item.getStart();
	}

	@Override
	protected Date getStop(StaffStatusEntryOutVO item) {
		return item.getStop();
	}
}
