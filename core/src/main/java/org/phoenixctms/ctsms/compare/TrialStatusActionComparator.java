package org.phoenixctms.ctsms.compare;

import java.util.Comparator;

import org.phoenixctms.ctsms.domain.TrialStatusAction;

public class TrialStatusActionComparator extends AlphanumComparatorBase implements Comparator<TrialStatusAction> {

	@Override
	public int compare(TrialStatusAction a, TrialStatusAction b) {
		if (a != null && b != null) {
			org.phoenixctms.ctsms.enumeration.TrialStatusAction actionA = a.getAction();
			org.phoenixctms.ctsms.enumeration.TrialStatusAction actionB = b.getAction();
			if (actionA != null && actionB != null) {
				return comp(actionA.getValue(), actionB.getValue());
			} else if (actionA == null && actionB != null) {
				return -1;
			} else if (actionA != null && actionB == null) {
				return 1;
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
