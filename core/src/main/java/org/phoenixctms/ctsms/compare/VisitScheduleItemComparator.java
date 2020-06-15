package org.phoenixctms.ctsms.compare;

import java.util.Comparator;
import java.util.Date;

import org.phoenixctms.ctsms.domain.VisitScheduleItem;

public class VisitScheduleItemComparator implements Comparator<VisitScheduleItem> {

	private int intDesc;
	private boolean start;

	public VisitScheduleItemComparator(boolean desc, boolean start) {
		this.intDesc = (desc ? -1 : 1);
		this.start = start;
	}

	@Override
	public int compare(VisitScheduleItem a, VisitScheduleItem b) {
		if (a != null && b != null) {
			if (start) {
				Date startA = a.getStart();
				Date startB = b.getStart();
				if (startA != null && startB != null) {
					int startComparison = startA.compareTo(startB);
					if (startComparison != 0) {
						return intDesc * startComparison;
					}
				} else if (startA == null && startB != null) {
					return intDesc * 1;
				} else if (startA != null && startB == null) {
					return intDesc * -1;
				} else {
					return 0;
				}
			} else {
				Date stopA = a.getStop();
				Date stopB = b.getStop();
				if (stopA != null && stopB != null) {
					int stopComparison = stopA.compareTo(stopB);
					if (stopComparison != 0) {
						return intDesc * stopComparison;
					}
				} else if (stopA == null && stopB != null) {
					return intDesc * 1;
				} else if (stopA != null && stopB == null) {
					return intDesc * -1;
				} else {
					return 0;
				}
			}
			if (a.getId() > b.getId()) {
				return intDesc * 1;
			} else if (a.getId() < b.getId()) {
				return intDesc * -1;
			} else {
				return 0;
			}
		} else if (a == null && b != null) {
			return intDesc * -1;
		} else if (a != null && b == null) {
			return intDesc * 1;
		} else {
			return 0;
		}
	}
}
