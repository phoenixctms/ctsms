package org.phoenixctms.ctsms.compare;

import java.util.Comparator;

import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueOutVO;

public class InputFieldSelectionSetValueOutVOComparator extends AlphanumComparatorBase implements Comparator<InputFieldSelectionSetValueOutVO> {

	@Override
	public int compare(InputFieldSelectionSetValueOutVO a, InputFieldSelectionSetValueOutVO b) {
		if (a != null && b != null) {
			String valueA = a.getValue();
			String valueB = b.getValue();
			if (valueA != null && valueB != null) {
				int valueComparison = comp(valueA, valueB);
				if (valueComparison != 0) {
					return valueComparison;
				}
			} else if (valueA == null && valueB != null) {
				return -1;
			} else if (valueA != null && valueB == null) {
				return 1;
			}
			String nameA = a.getName();
			String nameB = b.getName();
			if (nameA != null && nameB != null) {
				int nameComparison = comp(nameA, nameB);
				if (nameComparison != 0) {
					return nameComparison;
				}
			} else if (nameA == null && nameB != null) {
				return -1;
			} else if (nameA != null && nameB == null) {
				return 1;
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