package org.phoenixctms.ctsms.compare;

import java.util.Comparator;

import org.phoenixctms.ctsms.vo.ProbandGroupOutVO;
import org.phoenixctms.ctsms.vo.VisitOutVO;

public class ProbandGroupOutVOTokenComparator extends AlphanumComparatorBase implements Comparator<ProbandGroupOutVO> {

	@Override
	public int compare(ProbandGroupOutVO a, ProbandGroupOutVO b) {
		if (a != null && b != null) {
			int tokenComparison = comp(a.getToken(), b.getToken());
			if (tokenComparison != 0) {
				return tokenComparison;
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
