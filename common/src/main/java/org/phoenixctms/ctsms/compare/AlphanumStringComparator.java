package org.phoenixctms.ctsms.compare;

import java.util.Comparator;

public class AlphanumStringComparator extends AlphanumComparatorBase implements Comparator<String> {

	private boolean trim;

	private AlphanumStringComparator() {
	}

	public AlphanumStringComparator(boolean trim) {
		this.trim = trim;
	}

	@Override
	public int compare(String a, String b) {
		if (a != null && b != null) {
			if (trim) {
				return comp(a.trim(), b.trim());
			} else {
				return comp(a, b);
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
