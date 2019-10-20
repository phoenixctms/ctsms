package org.phoenixctms.ctsms.compare;

import java.util.Comparator;
import java.util.Date;

import org.phoenixctms.ctsms.vo.CvPositionPDFVO;
import org.phoenixctms.ctsms.vo.CvSectionVO;

public class CvPositionPDFVOComparator implements Comparator<CvPositionPDFVO> {

	@Override
	public int compare(CvPositionPDFVO a, CvPositionPDFVO b) {
		if (a != null && b != null) {
			CvSectionVO cvSectionA = a.getSection();
			CvSectionVO cvSectionB = b.getSection();
			if (cvSectionA != null && cvSectionB != null) {
				if (cvSectionA.getPosition() < cvSectionB.getPosition()) {
					return -1;
				} else if (cvSectionA.getPosition() > cvSectionB.getPosition()) {
					return 1;
				} else {
					Date dateA = (a.getStart() != null ? a.getStart() : a.getStop());
					Date dateB = (b.getStart() != null ? b.getStart() : b.getStop());
					if (dateA != null && dateB != null) {
						return dateA.compareTo(dateB);
					} else if (dateA == null && dateB != null) {
						return 1;
					} else if (dateA != null && dateB == null) {
						return -1;
					} else {
						return 0;
					}
				}
			} else if (cvSectionA == null && cvSectionB != null) {
				return -1;
			} else if (cvSectionA != null && cvSectionB == null) {
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