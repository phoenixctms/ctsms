package org.phoenixctms.ctsms.compare;

import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingLong;
import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsLast;

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
import org.phoenixctms.ctsms.vo.ProbandGroupOutVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.TeamMemberOutVO;
import org.phoenixctms.ctsms.vo.TeamMemberRoleVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.vo.VisitOutVO;
import org.phoenixctms.ctsms.vo.VisitScheduleItemOutVO;

public final class ComparatorFactory {

	private ComparatorFactory() {
		// private constructor
	}

	public static Comparator<Course> createCourseComparator() {
		return nullsLast(comparing(Course::getName).thenComparingLong(Course::getId));
	}

	public static final Comparator<String> ALPHANUM_COMPARATOR = new AlphaNumComparator();
	public static final Comparator<String> ALPHANUM_TRIM_COMPARATOR = nullsLast(comparing(String::trim,ALPHANUM_COMPARATOR));

	public static Comparator<BankAccountOutVO> createBankAccount() {
		Comparator<ProbandOutVO> probandComp = createProbandOutVO();
		return nullsLast(comparing(BankAccountOutVO::getProband, probandComp));
	}

	public static Comparator<ProbandOutVO> createProbandOutVO() {
		return nullsLast(new Comparator<ProbandOutVO>() {

			Comparator<ProbandOutVO> personComparator = comparing(ProbandOutVO::getLastName, ALPHANUM_COMPARATOR).thenComparing(ProbandOutVO::getFirstName, ALPHANUM_COMPARATOR);
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

			Comparator<PersonContactParticulars> pcp = nullsLast(comparing(PersonContactParticulars::getLastName, ALPHANUM_COMPARATOR));
			Comparator<OrganisationContactParticulars> ocp = nullsLast(comparing(OrganisationContactParticulars::getOrganisationName, ALPHANUM_COMPARATOR));

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
		
		Comparator<TeamMemberRoleVO> roleComp = nullsLast(comparing(TeamMemberRoleVO::getName, ALPHANUM_COMPARATOR));
		Comparator<StaffOutVO> staffNameComp = nullsLast(new Comparator<StaffOutVO>() {

			@Override
			public int compare(StaffOutVO a, StaffOutVO b) {
				if (a.isPerson() && b.isPerson()) {
					return ALPHANUM_COMPARATOR.compare(a.getLastName(), b.getLastName());
				}
				if (a.isPerson() && !b.isPerson()) {
					return -1;
				}
				if (!a.isPerson() && b.isPerson()) {
					return 1;
				}
				return ALPHANUM_COMPARATOR.compare(a.getOrganisationName(), b.getOrganisationName());
			}
			
		});
		return nullsLast(comparing(TeamMemberOutVO::getRole, roleComp)
				.thenComparing(TeamMemberOutVO::getStaff,staffNameComp)
				.thenComparing(TeamMemberOutVO::getId));
	}
	
	public static Comparator<VisitScheduleItemOutVO> createVisitScheduleItemOutVO() {
		Comparator<VisitScheduleItemOutVO> trialComp = nullsLast(comparing(VisitScheduleItemOutVO::getTrial, nullsLast(comparing(TrialOutVO::getName, ALPHANUM_COMPARATOR))));
		Comparator<VisitScheduleItemOutVO> probandComp = nullsLast(comparing(VisitScheduleItemOutVO::getGroup, nullsLast(comparing(ProbandGroupOutVO::getToken, ALPHANUM_COMPARATOR))));
		Comparator<VisitScheduleItemOutVO> visitComp = nullsLast(comparing(VisitScheduleItemOutVO::getVisit, nullsLast(comparing(VisitOutVO::getToken, ALPHANUM_COMPARATOR))));
		Comparator<VisitScheduleItemOutVO> tokenComp = nullsLast(comparing(VisitScheduleItemOutVO::getToken, ALPHANUM_COMPARATOR));
		return nullsLast(trialComp.thenComparing(probandComp).thenComparing(visitComp).thenComparing(tokenComp).thenComparing(createVisitScheduleItemOutVOTemporalOnly()));
	}
	
	public static Comparator<VisitScheduleItemOutVO> createVisitScheduleItemOutVOTemporalOnly(){
		Comparator<VisitScheduleItemOutVO> dateComp = nullsLast(comparing(VisitScheduleItemOutVO::getStart, nullsLast(naturalOrder())));
		Comparator<VisitScheduleItemOutVO> idComp = comparingLong(VisitScheduleItemOutVO::getId);
		return nullsLast(dateComp.thenComparing(idComp));
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
