package org.phoenixctms.ctsms.compare;

import java.util.Comparator;

import org.phoenixctms.ctsms.vo.ECRFOutVO;

public class EcrfOutVONameComparator extends AlphanumComparatorBase implements Comparator<ECRFOutVO> {

	@Override
	public int compare(ECRFOutVO a, ECRFOutVO b) {
		if (a != null && b != null) {
			int nameComparison = comp(a.getName(), b.getName());
			if (nameComparison != 0) {
				return nameComparison;
			} else {
				if (a.getId() > b.getId()) {
					return 1;
				} else if (a.getId() < b.getId()) {
					return -1;
				} else {
					return 0;
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
