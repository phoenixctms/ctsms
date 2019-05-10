package org.phoenixctms.ctsms.compare;

import java.util.Date;

import org.phoenixctms.ctsms.util.CommonUtil;

public abstract class IntervalScheduleComparatorBase<T> extends IntervalComparatorBase<T> {

	public IntervalScheduleComparatorBase(boolean intDesc) {
		super(intDesc);
	}

	@Override
	public int compare(T a, T b) {
		if (a != null && b != null) {
			Date intervalAStart = getStart(a);
			Date intervalAStop = getStop(a);
			Date intervalBStart = getStart(b);
			Date intervalBStop = getStop(b);
			if (intervalAStart != null && intervalAStop != null) {
				if (intervalBStart != null && intervalBStop != null) {
					long intervalADuration = CommonUtil.dateDeltaSecs(intervalAStart, intervalAStop);
					long intervalBDuration = CommonUtil.dateDeltaSecs(intervalBStart, intervalBStop);
					if (intervalADuration == intervalBDuration) {
						return super.compare(a, b);
					} else {
						return (new Long(intervalBDuration)).compareTo(intervalADuration);
					}
				} else {
					return 1;
				}
			} else {
				if (intervalBStart != null && intervalBStop != null) {
					return -1;
				} else {
					return super.compare(a, b);
				}
			}
		} else {
			return super.compare(a, b);
		}
	}
}
