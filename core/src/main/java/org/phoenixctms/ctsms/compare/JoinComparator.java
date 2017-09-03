package org.phoenixctms.ctsms.compare;

import java.util.Comparator;

import org.phoenixctms.ctsms.util.AssociationPath;

public class JoinComparator implements Comparator<AssociationPath> {

	@Override
	public int compare(AssociationPath a, AssociationPath b) {
		if (a != null && b != null) {
			if (a.getJoinOrder() < b.getJoinOrder()) {
				return -1;
			} else if (a.getJoinOrder() > b.getJoinOrder()) {
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
