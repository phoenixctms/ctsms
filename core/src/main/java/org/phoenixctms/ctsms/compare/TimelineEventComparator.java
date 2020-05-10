package org.phoenixctms.ctsms.compare;

import java.util.Comparator;
import java.util.Date;

import org.phoenixctms.ctsms.domain.TimelineEvent;
import org.phoenixctms.ctsms.domain.Trial;

public class TimelineEventComparator extends AlphanumComparatorBase implements Comparator<TimelineEvent> {

	private boolean temporalOnly;

	public TimelineEventComparator(boolean temporalOnly) {
		this.temporalOnly = temporalOnly;
	}

	@Override
	public int compare(TimelineEvent a, TimelineEvent b) {
		if (a != null && b != null) {
			if (!temporalOnly) {
				Trial trialA = a.getTrial();
				Trial trialB = b.getTrial();
				if (trialA != null && trialB != null) {
					int trialComparison = comp(trialA.getName(), trialB.getName());
					if (trialComparison != 0) {
						return trialComparison;
					}
				} else if (trialA == null && trialB != null) {
					return -1;
				} else if (trialA != null && trialB == null) {
					return 1;
				}
			}
			Date startA = a.getStart();
			Date startB = b.getStart();
			if (startA != null && startB != null) {
				int startComparison = startA.compareTo(startB);
				if (startComparison != 0) {
					return startComparison;
				}
			} else if (startA == null && startB != null) {
				return 1;
			} else if (startA != null && startB == null) {
				return -1;
			} else {
				return 0;
			}
			if (a.getId() > b.getId()) {
				return 1;
			} else if (a.getId() < b.getId()) {
				return -1;
			} else {
				return 0;
			}
		} else if (a == null && b != null) {
			return -1;
		} else if (a != null && b == null) {
			return 1;
		} else {
			return 0;
		}
	}
}
