package org.phoenixctms.ctsms.compare;


import java.util.Comparator;
import java.util.Date;

import org.phoenixctms.ctsms.vo.ECRFFieldStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.ECRFFieldValueOutVO;

public class EcrfFieldValueStatusEntryOutVOComparator implements Comparator {

	private int tsDesc;

	private EcrfFieldValueStatusEntryOutVOComparator() {
	}

	public EcrfFieldValueStatusEntryOutVOComparator(boolean tsDesc) {
		this.tsDesc = (tsDesc ? -1 : 1);
	}

	@Override
	public int compare(Object a, Object b) {
		Date modifiedTimestampA = null;
		Date modifiedTimestampB = null;
		if (a instanceof ECRFFieldValueOutVO && b instanceof ECRFFieldValueOutVO) {
			modifiedTimestampA = ((ECRFFieldValueOutVO) a).getModifiedTimestamp();
			modifiedTimestampB = ((ECRFFieldValueOutVO) b).getModifiedTimestamp();
		} else if (a instanceof ECRFFieldStatusEntryOutVO && b instanceof ECRFFieldStatusEntryOutVO) {
			modifiedTimestampA = ((ECRFFieldStatusEntryOutVO) a).getModifiedTimestamp();
			modifiedTimestampB = ((ECRFFieldStatusEntryOutVO) b).getModifiedTimestamp();
		} else if (a instanceof ECRFFieldValueOutVO && b instanceof ECRFFieldStatusEntryOutVO) {
			modifiedTimestampA = ((ECRFFieldValueOutVO) a).getModifiedTimestamp();
			modifiedTimestampB = ((ECRFFieldStatusEntryOutVO) b).getModifiedTimestamp();
		} else if (a instanceof ECRFFieldStatusEntryOutVO && b instanceof ECRFFieldValueOutVO) {
			modifiedTimestampA = ((ECRFFieldStatusEntryOutVO) a).getModifiedTimestamp();
			modifiedTimestampB = ((ECRFFieldValueOutVO) b).getModifiedTimestamp();
			// } else {
			// return 0;
		}
		if (modifiedTimestampA != null && modifiedTimestampB != null) {
			int modifiedTimestampComparison = modifiedTimestampA.compareTo(modifiedTimestampB);
			// return modifiedTimestampComparison;
			if (modifiedTimestampComparison != 0) {
				return modifiedTimestampComparison * tsDesc;
			}
		} else if (modifiedTimestampA == null && modifiedTimestampB != null) {
			return -1 * tsDesc;
		} else if (modifiedTimestampA != null && modifiedTimestampB == null) {
			return 1 * tsDesc;
		}
		return 0;
	}
}
