package org.phoenixctms.ctsms.compare;

import java.util.Date;

import org.phoenixctms.ctsms.vo.DutyRosterTurnOutVO;

public class DutyRosterTurnIntervalComparator extends IntervalComparatorBase<DutyRosterTurnOutVO> {

	public DutyRosterTurnIntervalComparator(boolean intDesc) {
		super(intDesc);
	}

	@Override
	protected Date getStart(DutyRosterTurnOutVO item) {
		return item.getStart();
	}

	@Override
	protected Date getStop(DutyRosterTurnOutVO item) {
		return item.getStop();
	}
}
