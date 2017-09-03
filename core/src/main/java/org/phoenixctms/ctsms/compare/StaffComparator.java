package org.phoenixctms.ctsms.compare;

import java.util.Comparator;

import org.phoenixctms.ctsms.domain.OrganisationContactParticulars;
import org.phoenixctms.ctsms.domain.PersonContactParticulars;
import org.phoenixctms.ctsms.domain.Staff;

public class StaffComparator extends AlphanumComparatorBase implements Comparator<Staff> {

	@Override
	public int compare(Staff a, Staff b) {
		if (a != null && b != null) {
			if (a.isPerson() && b.isPerson()) {
				PersonContactParticulars app = a.getPersonParticulars();
				PersonContactParticulars bpp = b.getPersonParticulars();
				if (app != null && bpp != null) {
					int lastnameComparison = comp(app.getLastName(), bpp.getLastName());
					if (lastnameComparison != 0) {
						return lastnameComparison;
					} else {
						int firstNameComparison = comp(app.getFirstName(), bpp.getFirstName());
						if (firstNameComparison != 0) {
							return firstNameComparison;
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
				OrganisationContactParticulars aop = a.getOrganisationParticulars();
				OrganisationContactParticulars bop = b.getOrganisationParticulars();
				if (aop != null && bop != null) {
					int organisationNameComparison = comp(aop.getOrganisationName(), bop.getOrganisationName());
					if (organisationNameComparison != 0) {
						return organisationNameComparison;
					} else {
						return a.getId().compareTo(b.getId());
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
