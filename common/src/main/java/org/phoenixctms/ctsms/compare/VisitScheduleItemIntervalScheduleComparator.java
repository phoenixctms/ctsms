package org.phoenixctms.ctsms.compare;

import java.util.Date;

import org.phoenixctms.ctsms.vo.VisitScheduleItemOutVO;

public class VisitScheduleItemIntervalScheduleComparator extends IntervalScheduleComparatorBase<VisitScheduleItemOutVO> {

	public VisitScheduleItemIntervalScheduleComparator(boolean intDesc) {
		super(intDesc);
	}

	@Override
	protected Date getStart(VisitScheduleItemOutVO item) {
		return item.getStart();
	}

	@Override
	protected Date getStop(VisitScheduleItemOutVO item) {
		return item.getStop();
	}
}
