package org.phoenixctms.ctsms.compare;

import java.util.Comparator;

import org.phoenixctms.ctsms.vo.DepartmentVO;

public class DepartmentVOComparator extends AlphanumComparatorBase implements Comparator<DepartmentVO> {

	@Override
	public int compare(DepartmentVO a, DepartmentVO b) {
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
