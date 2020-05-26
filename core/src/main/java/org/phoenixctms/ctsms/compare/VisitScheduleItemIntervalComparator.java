package org.phoenixctms.ctsms.compare;

import java.util.Date;

import org.phoenixctms.ctsms.vo.VisitScheduleItemOutVO;

public class VisitScheduleItemIntervalComparator extends IntervalComparatorBase<VisitScheduleItemOutVO> {

	public VisitScheduleItemIntervalComparator(boolean desc) {
		super(desc);
	}

	@Override
	protected boolean isInterval(VisitScheduleItemOutVO item) {
		return item != null && item.getStart() != null && item.getStop() != null;
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
