package org.phoenixctms.ctsms.compare;

import java.util.Comparator;

import org.phoenixctms.ctsms.vo.ECRFFieldOutVO;
import org.phoenixctms.ctsms.vo.ECRFFieldValueOutVO;
import org.phoenixctms.ctsms.vo.ECRFOutVO;

public class EcrfFieldValueOutVOComparator implements Comparator<ECRFFieldValueOutVO> {

	@Override
	public int compare(ECRFFieldValueOutVO a, ECRFFieldValueOutVO b) {
		if (a != null && b != null) {
			ECRFFieldOutVO ecrfFieldA = a.getEcrfField();
			ECRFFieldOutVO ecrfFieldB = b.getEcrfField();
			if (ecrfFieldA != null && ecrfFieldB != null) {
				ECRFOutVO ecrfA = ecrfFieldA.getEcrf();
				ECRFOutVO ecrfB = ecrfFieldB.getEcrf();
				if (ecrfA != null && ecrfB != null) {
					if (ecrfA.getId() > ecrfB.getId()) {
						return 1;
					} else if (ecrfA.getId() < ecrfB.getId()) {
						return -1;
					} else {
						String sectionA = ecrfFieldA.getSection();
						String sectionB = ecrfFieldB.getSection();
						if (sectionA != null && sectionB != null) {
							int sectionComparison = sectionA.compareTo(sectionB);
							if (sectionComparison != 0) {
								return sectionComparison;
							}
						} else if (sectionA == null && sectionB != null) {
							return -1;
						} else if (sectionA != null && sectionB == null) {
							return 1;
						}
						Long indexA = a.getIndex();
						Long indexB = b.getIndex();
						if (indexA != null && indexB != null) {
							int indexComparison = indexA.compareTo(indexB);
							if (indexComparison != 0) {
								return indexComparison;
							}
						} else if (indexA == null && indexB != null) {
							return -1;
						} else if (indexA != null && indexB == null) {
							return 1;
						}
						if (ecrfFieldA.getPosition() < ecrfFieldB.getPosition()) {
							return -1;
						} else if (ecrfFieldA.getPosition() > ecrfFieldB.getPosition()) {
							return 1;
						} else {
							return 0;
						}
					}
				} else if (ecrfA == null && ecrfB != null) {
					return -1;
				} else if (ecrfA != null && ecrfB == null) {
					return 1;
				} else {
					return 0;
				}
			} else if (ecrfFieldA == null && ecrfFieldB != null) {
				return -1;
			} else if (ecrfFieldA != null && ecrfFieldB == null) {
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
