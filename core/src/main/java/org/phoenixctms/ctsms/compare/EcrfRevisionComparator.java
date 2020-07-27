package org.phoenixctms.ctsms.compare;

import java.util.Comparator;

import org.phoenixctms.ctsms.domain.ECRF;

public class EcrfRevisionComparator extends AlphanumComparatorBase implements Comparator<ECRF> {

	@Override
	public int compare(ECRF a, ECRF b) {
		if (a != null && b != null) {
			int revisionComparison = comp(a.getRevision(), b.getRevision());
			if (revisionComparison != 0) {
				return revisionComparison;
			} else {
				return a.getId().compareTo(b.getId());
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
