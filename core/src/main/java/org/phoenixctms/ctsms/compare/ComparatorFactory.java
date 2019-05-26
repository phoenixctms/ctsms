package org.phoenixctms.ctsms.compare;

import static java.util.Comparator.comparing;
import static java.util.Comparator.nullsLast;

import java.util.Comparator;

import org.phoenixctms.ctsms.domain.Course;
import org.phoenixctms.ctsms.domain.OrganisationContactParticulars;
import org.phoenixctms.ctsms.domain.PersonContactParticulars;
import org.phoenixctms.ctsms.domain.Staff;

public final class ComparatorFactory {
	private ComparatorFactory() {
		// private constructor
	}
	
	public static Comparator<Course> createCourseComparator(){
		return nullsLast(comparing(Course::getName).thenComparingLong(Course::getId));
	}
	
	public static  Comparator<Staff> createStaffComparator(){
		Comparator<Staff> staffComp = new Comparator<Staff>() {

			Comparator<PersonContactParticulars>  pcp = nullsLast(comparing(PersonContactParticulars::getLastName, AlphaNumComparator::compare));
			Comparator<OrganisationContactParticulars> ocp = nullsLast(comparing(OrganisationContactParticulars::getOrganisationName,AlphaNumComparator::compare));
			
			@Override
			public int compare(Staff s1, Staff s2) {
				if (s1.isPerson() && s2.isPerson()) {
					return pcp.compare(s1.getPersonParticulars(), s2.getPersonParticulars());
				}
				if (s1.isPerson() && !s2.isPerson()) {
					return -1;
				}
				if (!s1.isPerson() && s2.isPerson()) {
					return 1;
				}
				return ocp.compare(s1.getOrganisationParticulars(), s2.getOrganisationParticulars());
			}
		};
		return staffComp;
	}
}
