package org.phoenixctms.ctsms.compare;

import java.util.Date;

import org.phoenixctms.ctsms.vo.ProbandStatusEntryOutVO;

public class ProbandStatusEntryIntervalComparator extends IntervalComparatorBase<ProbandStatusEntryOutVO> {

	public ProbandStatusEntryIntervalComparator(boolean desc) {
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
