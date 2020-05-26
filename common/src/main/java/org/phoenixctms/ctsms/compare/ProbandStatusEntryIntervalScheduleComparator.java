package org.phoenixctms.ctsms.compare;

import java.util.Date;

import org.phoenixctms.ctsms.vo.ProbandStatusEntryOutVO;

public class ProbandStatusEntryIntervalScheduleComparator extends IntervalScheduleComparatorBase<ProbandStatusEntryOutVO> {

	public ProbandStatusEntryIntervalScheduleComparator(boolean desc) {
		super(desc);
	}

	@Override
	protected Date getStart(ProbandStatusEntryOutVO item) {
		return item.getStart();
	}

	@Override
	protected Date getStop(ProbandStatusEntryOutVO item) {
		return item.getStop();
	}
}
