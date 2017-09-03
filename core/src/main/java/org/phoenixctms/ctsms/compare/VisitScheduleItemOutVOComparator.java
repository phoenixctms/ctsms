package org.phoenixctms.ctsms.compare;

import java.util.Comparator;
import java.util.Date;

import org.phoenixctms.ctsms.vo.ProbandGroupOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.vo.VisitOutVO;
import org.phoenixctms.ctsms.vo.VisitScheduleItemOutVO;

public class VisitScheduleItemOutVOComparator extends AlphanumComparatorBase implements Comparator<VisitScheduleItemOutVO> {

	private boolean temporalOnly;

	public VisitScheduleItemOutVOComparator(boolean temporalOnly) {
		this.temporalOnly = temporalOnly;
	}

	@Override
	public int compare(VisitScheduleItemOutVO a, VisitScheduleItemOutVO b) {
		if (a != null && b != null) {
			if (!temporalOnly) {
				TrialOutVO trialA = a.getTrial();
				TrialOutVO trialB = b.getTrial();
				if (trialA != null && trialB != null) {
					int trialComparison = comp(trialA.getName(), trialB.getName());
					if (trialComparison != 0) {
						return trialComparison;
					}
				} else if (trialA == null && trialB != null) {
					return -1;
				} else if (trialA != null && trialB == null) {
					return 1;
				}
				ProbandGroupOutVO groupA = a.getGroup();
				ProbandGroupOutVO groupB = b.getGroup();
				if (groupA != null && groupB != null) {
					int groupComparison = comp(groupA.getToken(), groupB.getToken());
					if (groupComparison != 0) {
						return groupComparison;
					}
				} else if (groupA == null && groupB != null) {
					return -1;
				} else if (groupA != null && groupB == null) {
					return 1;
				}
				VisitOutVO visitA = a.getVisit();
				VisitOutVO visitB = b.getVisit();
				if (visitA != null && visitB != null) {
					int visitComparison = comp(visitA.getToken(), visitB.getToken());
					if (visitComparison != 0) {
						return visitComparison;
					}
				} else if (visitA == null && visitB != null) {
					return -1;
				} else if (visitA != null && visitB == null) {
					return 1;
				}
				String tokenA = a.getToken();
				String tokenB = b.getToken();
				if (tokenA != null && tokenB != null) {
					int tokenComparison = comp(tokenA, tokenB);
					if (tokenComparison != 0) {
						return tokenComparison;
					}
				} else if (tokenA == null && tokenB != null) {
					return -1;
				} else if (tokenA != null && tokenB == null) {
					return 1;
				}
			}
			Date startA = a.getStart();
			Date startB = b.getStart();
			if (startA != null && startB != null) {
				int startComparison = startA.compareTo(startB);
				if (startComparison != 0) {
					return startComparison;
				}
			} else if (startA == null && startB != null) {
				return -1;
			} else if (startA != null && startB == null) {
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