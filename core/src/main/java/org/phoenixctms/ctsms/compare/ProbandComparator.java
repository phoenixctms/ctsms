
package org.phoenixctms.ctsms.compare;

import java.util.Comparator;

import org.phoenixctms.ctsms.domain.AnimalContactParticulars;
import org.phoenixctms.ctsms.domain.Proband;
import org.phoenixctms.ctsms.domain.ProbandContactParticulars;
import org.phoenixctms.ctsms.security.CryptoUtil;
import org.phoenixctms.ctsms.util.CoreUtil;

public class ProbandComparator extends AlphanumComparatorBase implements Comparator<Proband> {

	private static String getFirstName(ProbandContactParticulars particulars) {
		if (particulars != null && CoreUtil.isPassDecryption()) {
			try {
				return (String) CryptoUtil.decryptValue(particulars.getFirstNameIv(), particulars.getEncryptedFirstName());
			} catch (Exception e) {
			}
		}
		return null;
	}

	private static String getLastName(ProbandContactParticulars particulars) {
		if (particulars != null && CoreUtil.isPassDecryption()) {
			try {
				return (String) CryptoUtil.decryptValue(particulars.getLastNameIv(), particulars.getEncryptedLastName());
			} catch (Exception e) {
			}
		}
		return null;
	}

	@Override
	public int compare(Proband a, Proband b) {
		if (a != null && b != null) {
			if (a.isPerson() && b.isPerson()) {
				ProbandContactParticulars app = a.getPersonParticulars();
				ProbandContactParticulars bpp = b.getPersonParticulars();
				if (app != null && bpp != null) {
					if (!a.isBlinded() && !b.isBlinded()) {
						int lastnameComparison = comp(getLastName(app), getLastName(bpp));
						if (lastnameComparison != 0) {
							return lastnameComparison;
						} else {
							int firstNameComparison = comp(getFirstName(app), getFirstName(bpp));
							if (firstNameComparison != 0) {
								return firstNameComparison;
							} else {
								return a.getId().compareTo(b.getId());
							}
						}
					} else if (!a.isBlinded() && b.isBlinded()) {
						return -1;
					} else if (a.isBlinded() && !b.isBlinded()) {
						return 1;
					} else {
						int aliasComparison = comp(app.getAlias(), bpp.getAlias());
						if (aliasComparison != 0) {
							return aliasComparison;
						} else {
							return a.getId().compareTo(b.getId());
						}
					}
				} else {
					return 0;
				}

			} else if (a.isPerson() && !b.isPerson()) {
				return -1;
			} else if (!a.isPerson() && b.isPerson()) {
				return 1;
			} else {
				AnimalContactParticulars aap = a.getAnimalParticulars();
				AnimalContactParticulars bap = b.getAnimalParticulars();
				if (aap != null && bap != null) {
					if (!a.isBlinded() && !b.isBlinded()) {
						int animalNameComparison = comp(aap.getAnimalName(), bap.getAnimalName());
						if (animalNameComparison != 0) {
							return animalNameComparison;
						} else {
							return a.getId().compareTo(b.getId());
						}
					} else if (!a.isBlinded() && b.isBlinded()) {
						return -1;
					} else if (a.isBlinded() && !b.isBlinded()) {
						return 1;
					} else {
						int aliasComparison = comp(aap.getAlias(), bap.getAlias());
						if (aliasComparison != 0) {
							return aliasComparison;
						} else {
							return a.getId().compareTo(b.getId());
						}
					}
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
