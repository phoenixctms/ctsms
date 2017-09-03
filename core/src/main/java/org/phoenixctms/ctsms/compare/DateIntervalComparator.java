package org.phoenixctms.ctsms.compare;

import java.util.Date;

import org.phoenixctms.ctsms.util.date.DateInterval;

public class DateIntervalComparator extends IntervalComparatorBase<DateInterval> {

	public DateIntervalComparator(boolean intDesc) {
		super(intDesc);
	}

	@Override
	public int compare(DateInterval a, DateInterval b) {
		return super.compare(a, b);
	}

	@Override
	protected Date getStart(DateInterval item) {
		return item.getStart();
	}

	@Override
	protected Date getStop(DateInterval item) {
		return item.getStop();
	}
}
