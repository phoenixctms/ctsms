package org.phoenixctms.ctsms.compare;

import java.util.Comparator;

import org.phoenixctms.ctsms.vo.ProbandListEntryTagOutVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagValueOutVO;

public class ProbandListEntryTagValueOutVOComparator implements Comparator<ProbandListEntryTagValueOutVO> {

	@Override
	public int compare(ProbandListEntryTagValueOutVO a, ProbandListEntryTagValueOutVO b) {
		if (a != null && b != null) {
			ProbandListEntryTagOutVO listEntryTagA = a.getTag();
			ProbandListEntryTagOutVO listEntryTagB = b.getTag();
			if (listEntryTagA != null && listEntryTagB != null) {
				if (listEntryTagA.getPosition() < listEntryTagB.getPosition()) {
					return -1;
				} else if (listEntryTagA.getPosition() > listEntryTagB.getPosition()) {
					return 1;
				} else {
					return 0;
				}
			} else if (listEntryTagA == null && listEntryTagB != null) {
				return -1;
			} else if (listEntryTagA != null && listEntryTagB == null) {
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
