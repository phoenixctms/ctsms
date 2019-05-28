package org.phoenixctms.ctsms.compare;

import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingInt;
import static java.util.Comparator.comparingLong;
import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsFirst;
import static java.util.Comparator.nullsLast;
import static java.util.Comparator.reverseOrder;

import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import org.phoenixctms.ctsms.domain.Course;
import org.phoenixctms.ctsms.domain.ECRFStatusAction;
import org.phoenixctms.ctsms.domain.Inventory;
import org.phoenixctms.ctsms.domain.OrganisationContactParticulars;
import org.phoenixctms.ctsms.domain.PersonContactParticulars;
import org.phoenixctms.ctsms.domain.Staff;
import org.phoenixctms.ctsms.domain.TrialStatusAction;
import org.phoenixctms.ctsms.util.AssociationPath;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.date.DateInterval;
import org.phoenixctms.ctsms.vo.BankAccountOutVO;
import org.phoenixctms.ctsms.vo.CvPositionPDFVO;
import org.phoenixctms.ctsms.vo.CvSectionVO;
import org.phoenixctms.ctsms.vo.DutyRosterTurnOutVO;
import org.phoenixctms.ctsms.vo.ECRFFieldStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.ECRFFieldValueOutVO;
import org.phoenixctms.ctsms.vo.InputFieldOutVO;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueOutVO;
import org.phoenixctms.ctsms.vo.InventoryBookingOutVO;
import org.phoenixctms.ctsms.vo.InventoryStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.ProbandGroupOutVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagOutVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagValueOutVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.vo.ProbandStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.StaffStatusEntryOutVO;
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
		return nullsFirst(comparing(Course::getName).thenComparingLong(Course::getId));
	}

	public static final Comparator<String> ALPHANUM_COMPARATOR = new AlphaNumComparator();
	public static final Comparator<String> ALPHANUM_TRIM_COMPARATOR = nullsFirst(comparing(String::trim, ALPHANUM_COMPARATOR));

	public static Comparator<BankAccountOutVO> createBankAccount() {
		Comparator<ProbandOutVO> probandComp = createProbandOutVO();
		return nullsFirst(comparing(BankAccountOutVO::getProband, probandComp));
	}

	public static Comparator<ProbandOutVO> createProbandOutVO() {
		return nullsFirst(new Comparator<ProbandOutVO>() {

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

			Comparator<PersonContactParticulars> pcp = nullsFirst(comparing(PersonContactParticulars::getLastName, ALPHANUM_COMPARATOR));
			Comparator<OrganisationContactParticulars> ocp = nullsFirst(comparing(OrganisationContactParticulars::getOrganisationName, ALPHANUM_COMPARATOR));

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
		Comparator<CvPositionPDFVO> dateComp = nullsFirst(comparing(CvPositionPDFVO::getStart, nullsFirst(naturalOrder())));
		Comparator<CvSectionVO> positionComp = nullsFirst(comparingLong(CvSectionVO::getPosition));
		return nullsFirst(comparing(CvPositionPDFVO::getSection, positionComp).thenComparing(dateComp));
	}

	public static Comparator<AssociationPath> createAssociationPath() {
		return nullsFirst(comparing(AssociationPath::getJoinOrder, nullsFirst(naturalOrder())));
	}

	public static Comparator<TeamMemberOutVO> createTeamMemberOutVO() {
		Comparator<TeamMemberRoleVO> roleComp = nullsFirst(comparing(TeamMemberRoleVO::getName, ALPHANUM_COMPARATOR));
		Comparator<StaffOutVO> staffNameComp = nullsFirst(new Comparator<StaffOutVO>() {

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
		return nullsFirst(comparing(TeamMemberOutVO::getRole, roleComp)
				.thenComparing(TeamMemberOutVO::getStaff, staffNameComp)
				.thenComparing(TeamMemberOutVO::getId));
	}

	public static Comparator<VisitScheduleItemOutVO> createVisitScheduleItemOutVO() {
		Comparator<VisitScheduleItemOutVO> trialComp = nullsFirst(comparing(VisitScheduleItemOutVO::getTrial, nullsFirst(comparing(TrialOutVO::getName, ALPHANUM_COMPARATOR))));
		Comparator<VisitScheduleItemOutVO> probandComp = nullsFirst(
				comparing(VisitScheduleItemOutVO::getGroup, nullsFirst(comparing(ProbandGroupOutVO::getToken, ALPHANUM_COMPARATOR))));
		Comparator<VisitScheduleItemOutVO> visitComp = nullsFirst(comparing(VisitScheduleItemOutVO::getVisit, nullsFirst(comparing(VisitOutVO::getToken, ALPHANUM_COMPARATOR))));
		Comparator<VisitScheduleItemOutVO> tokenComp = nullsFirst(comparing(VisitScheduleItemOutVO::getToken, ALPHANUM_COMPARATOR));
		return nullsFirst(trialComp.thenComparing(probandComp).thenComparing(visitComp).thenComparing(tokenComp).thenComparing(createVisitScheduleItemOutVOTemporalOnly()));
	}

	public static Comparator<VisitScheduleItemOutVO> createVisitScheduleItemOutVOTemporalOnly() {
		Comparator<VisitScheduleItemOutVO> dateComp = nullsFirst(comparing(VisitScheduleItemOutVO::getStart, nullsFirst(naturalOrder())));
		Comparator<VisitScheduleItemOutVO> idComp = comparingLong(VisitScheduleItemOutVO::getId);
		return nullsFirst(dateComp.thenComparing(idComp));
	}

	public static Comparator<Inventory> createInventory() {
		return nullsFirst(comparing(Inventory::getName, ALPHANUM_COMPARATOR));
	}

	public static Comparator<ECRFStatusAction> createECRFStatusAction() {
		return nullsFirst(comparing(ECRFStatusAction::getAction, nullsFirst((a, b) -> ALPHANUM_COMPARATOR.compare(a.getValue(), b.getValue()))));
	}

	public static Comparator<TrialStatusAction> createTrialStatusAction() {
		return nullsFirst(comparing(TrialStatusAction::getAction, nullsFirst((a, b) -> ALPHANUM_COMPARATOR.compare(a.getValue(), b.getValue()))));
	}

	public static Comparator<InputFieldSelectionSetValueOutVO> createInputFieldSelectionSetValueOutVO() {
		Comparator<InputFieldOutVO> fieldNameComp = comparing(InputFieldOutVO::getName, nullsFirst(ALPHANUM_COMPARATOR));
		Comparator<InputFieldSelectionSetValueOutVO> fieldComp = comparing(InputFieldSelectionSetValueOutVO::getField, nullsFirst(fieldNameComp));
		Comparator<InputFieldSelectionSetValueOutVO> valueComp = comparing(InputFieldSelectionSetValueOutVO::getValue, nullsFirst(ALPHANUM_COMPARATOR));
		Comparator<InputFieldSelectionSetValueOutVO> nameComp = comparing(InputFieldSelectionSetValueOutVO::getName, nullsFirst(ALPHANUM_COMPARATOR));
		Comparator<InputFieldSelectionSetValueOutVO> idComp = comparingLong(InputFieldSelectionSetValueOutVO::getId);
		return nullsFirst(fieldComp.thenComparing(valueComp).thenComparing(nameComp).thenComparing(idComp));
	}

	public static final Comparator<StaffStatusEntryOutVO> STAFF_STATUS_ENTRY_OUT_VO_INTERVAL_COMP = createInterval(StaffStatusEntryOutVO::getStart, StaffStatusEntryOutVO::getStop);
	public static final Comparator<InventoryBookingOutVO> INVENTORY_BOOKING_OUT_VO_INTERVAL_COMP = createInterval(InventoryBookingOutVO::getStart, InventoryBookingOutVO::getStop);
	public static final Comparator<DateInterval> DATE_INTERVAL_INTERVAL_COMP = createInterval(DateInterval::getStart, DateInterval::getStop);
	public static final Comparator<VisitScheduleItemOutVO> VISIT_SCHEDULE_ITEM_OUT_VO_INTERVAL_COMP = createInterval(VisitScheduleItemOutVO::getStart,
			VisitScheduleItemOutVO::getStop);
	public static final Comparator<InventoryStatusEntryOutVO> INVENTORY_STATUS_ENTRY_OUT_VO_INTERVAL_COMP = createInterval(InventoryStatusEntryOutVO::getStart,
			InventoryStatusEntryOutVO::getStop);
	public static final Comparator<ProbandStatusEntryOutVO> PROBAND_STATUS_ENTRY_OUT_VO_INTERVAL_COMP = createInterval(ProbandStatusEntryOutVO::getStart,
			ProbandStatusEntryOutVO::getStop);
	public static final Comparator<DutyRosterTurnOutVO> DUTY_ROASTER_TURN_OUT_VO_INTERVAL_COMP = createInterval(DutyRosterTurnOutVO::getStart, DutyRosterTurnOutVO::getStop);
	public static final Comparator<InventoryBookingOutVO> INVENTORY_BOOKING_OUT_VO_SCHEDULE_INTERVAL_COMP = createScheduleInterval(InventoryBookingOutVO::getStart,
			InventoryBookingOutVO::getStop);
	public static final Comparator<VisitScheduleItemOutVO> VISIT_SCHEDULE_ITEM_OUT_VO_SCHEDULE_INTERVAL_COMP = createScheduleInterval(VisitScheduleItemOutVO::getStart,
			VisitScheduleItemOutVO::getStop);
	public static final Comparator<InventoryStatusEntryOutVO> INVENTORY_STATUS_ENTRY_OUT_VO_SCHEDULE_INTERVAL_COMP = createScheduleInterval(InventoryStatusEntryOutVO::getStart,
			InventoryStatusEntryOutVO::getStop);
	public static final Comparator<DutyRosterTurnOutVO> DUTY_ROSTER_TURN_OUT_VO_SCHEDULE_INTERVAL_COMP = createScheduleInterval(DutyRosterTurnOutVO::getStart,
			DutyRosterTurnOutVO::getStop);
	public static final Comparator<StaffStatusEntryOutVO> STAFF_STATUS_ENTRY_OUT_VO_SCHEDULE_INTERVAL_COMP = createScheduleInterval(StaffStatusEntryOutVO::getStart,
			StaffStatusEntryOutVO::getStop);
	public static final Comparator<ProbandStatusEntryOutVO> PROBAND_STATUS_ENTRY_OUT_VO_SCHEDULE_INTERVAL_COMP = createScheduleInterval(ProbandStatusEntryOutVO::getStart,
			ProbandStatusEntryOutVO::getStop);
	public static final Comparator ECRF_FIELD_VALUE_STATUS_ENTRY_OUT_VO_COMP = nullsFirst(comparing(x -> {
		if (x instanceof ECRFFieldValueOutVO) {
			return ((ECRFFieldValueOutVO) x).getModifiedTimestamp();
		} else if (x instanceof ECRFFieldStatusEntryOutVO) {
			return ((ECRFFieldStatusEntryOutVO) x).getModifiedTimestamp();
		} else {
			return null;
		}
	}, nullsFirst(naturalOrder())));

	private static <T> Comparator<T> createScheduleInterval(Function<T, ? extends Date> startAccessor, Function<T, ? extends Date> stopAccessor) {
		Comparator<T> durationComparison = nullsFirst(comparing(x -> x == null ? null : CommonUtil.dateDeltaSecs(startAccessor.apply(x), stopAccessor.apply(x)), nullsLast(reverseOrder())));
		return durationComparison.thenComparing(createInterval(startAccessor, stopAccessor));
	}

	private static <T> Comparator<T> createInterval(Function<T, ? extends Date> startAccessor, Function<T, ? extends Date> stopAccessor) {
		return nullsFirst(comparing(startAccessor,nullsLast(naturalOrder())).thenComparing(stopAccessor,nullsLast(naturalOrder())));
	}

	public static Comparator<ProbandListEntryTagValueOutVO> createProbandListEntryTagValueOutVO() {
		return nullsFirst(comparing(ProbandListEntryTagValueOutVO::getTag, nullsFirst(comparing(ProbandListEntryTagOutVO::getPosition, nullsFirst(naturalOrder())))));
	}
	
	public static Comparator<Map.Entry<String, String>> createKeyValueLength() {
		return nullsFirst(comparing(x->x.getValue(), nullsFirst(comparingInt(String::length))));
	}

	public static <T, U extends Comparable<? super U>> Comparator<T> createSafeLong(Function<? super T, ? extends U> keyExtractor) {
		return nullsFirst(comparing(keyExtractor, nullsFirst(naturalOrder())));
	}

	public static <T> Comparator<T> createReflectionId() {
		return nullsFirst(comparing(obj -> CommonUtil.getSafeLong(obj, "getId"), nullsFirst(naturalOrder())));
	}
}
