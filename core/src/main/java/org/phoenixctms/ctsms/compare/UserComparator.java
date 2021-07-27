package org.phoenixctms.ctsms.compare;

import java.util.Comparator;

import org.phoenixctms.ctsms.domain.User;

public class UserComparator extends AlphanumComparatorBase implements Comparator<User> {

	@Override
	public int compare(User a, User b) {
		if (a != null && b != null) {
			int nameComparison = comp(a.getName(), b.getName());
			if (nameComparison != 0) {
				return nameComparison;
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
