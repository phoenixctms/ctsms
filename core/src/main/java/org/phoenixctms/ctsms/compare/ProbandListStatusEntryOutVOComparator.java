package org.phoenixctms.ctsms.compare;

import java.util.Comparator;

import org.phoenixctms.ctsms.vo.ProbandListStatusEntryOutVO;

public class ProbandListStatusEntryOutVOComparator implements Comparator<ProbandListStatusEntryOutVO> {

	@Override
	public int compare(ProbandListStatusEntryOutVO a, ProbandListStatusEntryOutVO b) {
		if (a != null && b != null) {
			int dateComparison = a.getRealTimestamp().compareTo(b.getRealTimestamp());
			if (dateComparison != 0) {
				return dateComparison;
			} else if (a.getId() < b.getId()) {
				return -1;
			} else if (a.getId() > b.getId()) {
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
