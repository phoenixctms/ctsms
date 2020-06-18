package org.phoenixctms.ctsms.compare;

import java.util.Comparator;

import org.phoenixctms.ctsms.vo.BankAccountOutVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;

public class BankAccountOutVOComparator extends AlphanumComparatorBase implements Comparator<BankAccountOutVO> {

	@Override
	public int compare(BankAccountOutVO a, BankAccountOutVO b) {
		if (a != null && b != null) {
			ProbandOutVO probandA = a.getProband();
			ProbandOutVO probandB = b.getProband();
			if (probandA != null && probandB != null) {
				int probandNameComparison = comp(probandA.getLastName(), probandB.getLastName());
				if (probandNameComparison != 0) {
					return probandNameComparison;
				} else {
					if (probandA.getId() > probandB.getId()) {
						return 1;
					} else if (probandA.getId() < probandB.getId()) {
						return -1;
					}
				}
			} else if (probandA == null && probandB != null) {
				return -1;
			} else if (probandA != null && probandB == null) {
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