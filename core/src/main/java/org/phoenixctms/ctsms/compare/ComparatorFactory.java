package org.phoenixctms.ctsms.compare;

import static java.util.Comparator.*;

import java.util.Comparator;
import java.util.function.Function;

import org.phoenixctms.ctsms.domain.Course;
import org.phoenixctms.ctsms.domain.OrganisationContactParticulars;
import org.phoenixctms.ctsms.domain.PersonContactParticulars;
import org.phoenixctms.ctsms.domain.Staff;
import org.phoenixctms.ctsms.util.AssociationPath;
import org.phoenixctms.ctsms.vo.BankAccountOutVO;
import org.phoenixctms.ctsms.vo.CvPositionPDFVO;
import org.phoenixctms.ctsms.vo.CvSectionVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.TeamMemberOutVO;
import org.phoenixctms.ctsms.vo.TeamMemberRoleVO;

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
	
	public static Comparator<AssociationPath> createAssociationPath() {
		return nullsLast(comparing(AssociationPath::getJoinOrder, nullsLast(naturalOrder())));
	}
	
	public static Comparator<TeamMemberOutVO> createTeamMemberOutVO() {
		
		Comparator<TeamMemberRoleVO> roleComp = nullsLast(comparing(TeamMemberRoleVO::getName, alphaNum));
		Comparator<StaffOutVO> staffNameComp = nullsLast(new Comparator<StaffOutVO>() {

			@Override
			public int compare(StaffOutVO a, StaffOutVO b) {
				if (a.isPerson() && b.isPerson()) {
					return alphaNum.compare(a.getLastName(), b.getLastName());
				}
				if (a.isPerson() && !b.isPerson()) {
					return -1;
				}
				if (!a.isPerson() && b.isPerson()) {
					return 1;
				}
				return alphaNum.compare(a.getOrganisationName(), b.getOrganisationName());
			}
			
		});
		return nullsLast(comparing(TeamMemberOutVO::getRole, roleComp)
				.thenComparing(TeamMemberOutVO::getStaff,staffNameComp)
				.thenComparing(TeamMemberOutVO::getId));
	}

	public static <T,U extends Comparable<? super U>> Comparator<T> createSafeLong(Function<? super T, ? extends U> keyExtractor) {
		return nullsLast(comparing(keyExtractor, nullsLast(naturalOrder())));
	}
	
	public static <T> Comparator<T> createReflectionId(){
		return nullsLast(comparing(obj -> getSafeLong(obj, "getId"), nullsLast(naturalOrder())));
	}
	
	public static Long getSafeLong(Object obj, String methodName) {
		if (obj == null) {
			return null;
		}
		try {
			return (Long) obj.getClass().getMethod(methodName).invoke(obj);
		} catch (Exception e) {
			return null;
		}
	}
	
	
}
