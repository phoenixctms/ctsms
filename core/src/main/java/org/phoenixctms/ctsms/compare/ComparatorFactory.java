package org.phoenixctms.ctsms.compare;

import static java.util.Comparator.*;

import java.util.Comparator;

import org.phoenixctms.ctsms.domain.Course;
import org.phoenixctms.ctsms.domain.OrganisationContactParticulars;
import org.phoenixctms.ctsms.domain.PersonContactParticulars;
import org.phoenixctms.ctsms.domain.Staff;
import org.phoenixctms.ctsms.vo.BankAccountOutVO;
import org.phoenixctms.ctsms.vo.CvPositionPDFVO;
import org.phoenixctms.ctsms.vo.CvSectionVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;

public final class ComparatorFactory {

	private ComparatorFactory() {
		// private constructor
	}

	public static Comparator<Course> createCourseComparator() {
		return nullsLast(comparing(Course::getName).thenComparingLong(Course::getId));
	}

	private static AlphaNumComparator alphaNum = new AlphaNumComparator();

	public static Comparator<BankAccountOutVO> createBankAccount() {
		Comparator<ProbandOutVO> probandComp = createProbandOutVO();
		return nullsLast(comparing(BankAccountOutVO::getProband, probandComp));
	}

	public static Comparator<ProbandOutVO> createProbandOutVO() {
		return nullsLast(new Comparator<ProbandOutVO>() {

			Comparator<ProbandOutVO> personComparator = comparing(ProbandOutVO::getLastName, alphaNum).thenComparing(ProbandOutVO::getFirstName, alphaNum);
			Comparator<ProbandOutVO> animalComparator = comparing(ProbandOutVO::getAnimalName);

			@Override
			public int compare(ProbandOutVO a, ProbandOutVO b) {
				if (a.isPerson() && b.isPerson()) {
					personComparator.compare(a, b);
				}
				if (a.isPerson() && !b.isPerson()) {
					return -1;
				}
				if (!a.isPerson() && b.isPerson()) {
					return 1;
				}
				return animalComparator.compare(a, b);
			}
		});
	}

	public static Comparator<Staff> createStaffComparator() {
		Comparator<Staff> staffComp = new Comparator<Staff>() {

			Comparator<PersonContactParticulars> pcp = nullsLast(comparing(PersonContactParticulars::getLastName, alphaNum));
			Comparator<OrganisationContactParticulars> ocp = nullsLast(comparing(OrganisationContactParticulars::getOrganisationName, alphaNum));

			@Override
			public int compare(Staff a, Staff b) {
				if (a.isPerson() && b.isPerson()) {
					return pcp.compare(a.getPersonParticulars(), b.getPersonParticulars());
				}
				if (a.isPerson() && !b.isPerson()) {
					return -1;
				}
				if (!a.isPerson() && b.isPerson()) {
					return 1;
				}
				return ocp.compare(a.getOrganisationParticulars(), b.getOrganisationParticulars());
			}
		};
		return staffComp;
	}

	public static Comparator<CvPositionPDFVO> createCvPositionPDFVO() {
		Comparator<CvPositionPDFVO> dateComp = nullsLast(comparing(CvPositionPDFVO::getStart, nullsLast(naturalOrder())));
		Comparator<CvSectionVO> positionComp = nullsLast(comparingLong(CvSectionVO::getPosition));
		return nullsLast(comparing(CvPositionPDFVO::getSection, positionComp).thenComparing(dateComp));
	}
}
