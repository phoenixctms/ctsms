package org.phoenixctms.ctsms.compare;

import java.util.Comparator;

import org.phoenixctms.ctsms.vo.ProbandOutVO;

public class ProbandOutVOComparator extends AlphanumComparatorBase implements Comparator<ProbandOutVO> {

	@Override
	public int compare(ProbandOutVO a, ProbandOutVO b) {
		if (a != null && b != null) {
			// int lastnameComparison = comp(a.getLastName(), b.getLastName());
			// if (lastnameComparison != 0) {
			// return lastnameComparison;
			// } else {
			// int firstNameComparison = comp(a.getFirstName(), b.getFirstName());
			// if (firstNameComparison != 0) {
			// return firstNameComparison;
			// } else {
			// if (a.getId() > b.getId()) {
			// return 1;
			// } else if (a.getId() < b.getId()) {
			// return -1;
			// } else {
			// return 0;
			// }
			// }
			// }
			if (a.isPerson() && b.isPerson()) {
				int lastnameComparison = comp(a.getLastName(), b.getLastName());
				if (lastnameComparison != 0) {
					return lastnameComparison;
				} else {
					int firstNameComparison = comp(a.getFirstName(), b.getFirstName());
					if (firstNameComparison != 0) {
						return firstNameComparison;
					} else {
						if (a.getId() > b.getId()) {
							return 1;
						} else if (a.getId() < b.getId()) {
							return -1;
						} else {
							return 0;
						}
					}
				}
			} else if (a.isPerson() && !b.isPerson()) {
				return -1;
			} else if (!a.isPerson() && b.isPerson()) {
				return 1;
			} else {
				int animalnameComparison = comp(a.getAnimalName(), b.getAnimalName());
				if (animalnameComparison != 0) {
					return animalnameComparison;
				} else {
					if (a.getId() > b.getId()) {
						return 1;
					} else if (a.getId() < b.getId()) {
						return -1;
					} else {
						return 0;
					}
				}
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
