package org.phoenixctms.ctsms.compare;

import java.util.Comparator;

import org.phoenixctms.ctsms.domain.ECRFStatusAction;

public class EcrfStatusActionComparator extends AlphanumComparatorBase implements Comparator<ECRFStatusAction> {

	@Override
	public int compare(ECRFStatusAction a, ECRFStatusAction b) {
		if (a != null && b != null) {
			org.phoenixctms.ctsms.enumeration.ECRFStatusAction actionA = a.getAction();
			org.phoenixctms.ctsms.enumeration.ECRFStatusAction actionB = b.getAction();
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
