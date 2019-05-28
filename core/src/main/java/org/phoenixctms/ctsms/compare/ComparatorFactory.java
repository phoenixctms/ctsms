package org.phoenixctms.ctsms.compare;

import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingInt;
import static java.util.Comparator.comparingLong;
import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsFirst;
import static java.util.Comparator.nullsLast;
import static java.util.Comparator.reverseOrder;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
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
import org.phoenixctms.ctsms.vo.AspSubstanceVO;
import org.phoenixctms.ctsms.vo.BankAccountOutVO;
import org.phoenixctms.ctsms.vo.CriterionInstantVO;
import org.phoenixctms.ctsms.vo.CvPositionPDFVO;
import org.phoenixctms.ctsms.vo.CvSectionVO;
import org.phoenixctms.ctsms.vo.DutyRosterTurnOutVO;
import org.phoenixctms.ctsms.vo.ECRFFieldStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.ECRFFieldValueOutVO;
import org.phoenixctms.ctsms.vo.InputFieldOutVO;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueJsonVO;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueOutVO;
import org.phoenixctms.ctsms.vo.InquiryOutVO;
import org.phoenixctms.ctsms.vo.InquiryValueOutVO;
import org.phoenixctms.ctsms.vo.InventoryBookingOutVO;
import org.phoenixctms.ctsms.vo.InventoryStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.MoneyTransferOutVO;
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

	public static final Comparator<String> ALPHANUM_COMP = new AlphaNumComparator();
	public static final Comparator<String> ALPHANUM_TRIM_COMP = nullsFirst(comparing(String::trim, ALPHANUM_COMP));
	public static final Comparator<DateInterval> DATE_INTERVAL_INTERVAL_COMP = createInterval(DateInterval::getStart, DateInterval::getStop);
	public static final Comparator<DutyRosterTurnOutVO> DUTY_ROASTER_TURN_OUT_VO_INTERVAL_COMP = createInterval(DutyRosterTurnOutVO::getStart, DutyRosterTurnOutVO::getStop);
	public static final Comparator<DutyRosterTurnOutVO> DUTY_ROSTER_TURN_OUT_VO_SCHEDULE_INTERVAL_COMP = createScheduleInterval(DutyRosterTurnOutVO::getStart,
			DutyRosterTurnOutVO::getStop);
	public static final Comparator ECRF_FIELD_VALUE_STATUS_ENTRY_OUT_VO_COMP = nullsFirst(comparing(x -> {
		if (x instanceof ECRFFieldValueOutVO) {
			return ((ECRFFieldValueOutVO) x).getModifiedTimestamp();
		} else if (x instanceof ECRFFieldStatusEntryOutVO) {
			return ((ECRFFieldStatusEntryOutVO) x).getModifiedTimestamp();
		} else {
			return null;
		}
	}, nullsFirst(naturalOrder())));
	public static final Comparator<InventoryBookingOutVO> INVENTORY_BOOKING_OUT_VO_INTERVAL_COMP = createInterval(InventoryBookingOutVO::getStart, InventoryBookingOutVO::getStop);
	public static final Comparator<InventoryBookingOutVO> INVENTORY_BOOKING_OUT_VO_SCHEDULE_INTERVAL_COMP = createScheduleInterval(InventoryBookingOutVO::getStart,
			InventoryBookingOutVO::getStop);
	public static final Comparator<InventoryStatusEntryOutVO> INVENTORY_STATUS_ENTRY_OUT_VO_INTERVAL_COMP = createInterval(InventoryStatusEntryOutVO::getStart,
			InventoryStatusEntryOutVO::getStop);
	public static final Comparator<InventoryStatusEntryOutVO> INVENTORY_STATUS_ENTRY_OUT_VO_SCHEDULE_INTERVAL_COMP = createScheduleInterval(InventoryStatusEntryOutVO::getStart,
			InventoryStatusEntryOutVO::getStop);
	public static final Comparator<ProbandStatusEntryOutVO> PROBAND_STATUS_ENTRY_OUT_VO_INTERVAL_COMP = createInterval(ProbandStatusEntryOutVO::getStart,
			ProbandStatusEntryOutVO::getStop);
	public static final Comparator<ProbandStatusEntryOutVO> PROBAND_STATUS_ENTRY_OUT_VO_SCHEDULE_INTERVAL_COMP = createScheduleInterval(ProbandStatusEntryOutVO::getStart,
			ProbandStatusEntryOutVO::getStop);
	public static final Comparator<StaffStatusEntryOutVO> STAFF_STATUS_ENTRY_OUT_VO_INTERVAL_COMP = createInterval(StaffStatusEntryOutVO::getStart, StaffStatusEntryOutVO::getStop);
	public static final Comparator<StaffStatusEntryOutVO> STAFF_STATUS_ENTRY_OUT_VO_SCHEDULE_INTERVAL_COMP = createScheduleInterval(StaffStatusEntryOutVO::getStart,
			StaffStatusEntryOutVO::getStop);
	public static final Comparator<VisitScheduleItemOutVO> VISIT_SCHEDULE_ITEM_OUT_VO_INTERVAL_COMP = createInterval(VisitScheduleItemOutVO::getStart,
			VisitScheduleItemOutVO::getStop);
	public static final Comparator<VisitScheduleItemOutVO> VISIT_SCHEDULE_ITEM_OUT_VO_SCHEDULE_INTERVAL_COMP = createScheduleInterval(VisitScheduleItemOutVO::getStart,
			VisitScheduleItemOutVO::getStop);
	public static final Comparator<InputFieldSelectionSetValueOutVO> SELECTION_SET_VALUE_OUT_VO_ID_COMPARATOR = createNullSafe(InputFieldSelectionSetValueOutVO::getId);
	public static final Comparator<InputFieldSelectionSetValueJsonVO> SELECTION_SET_VALUE_JSON_VO_ID_COMPARATOR = createNullSafe(InputFieldSelectionSetValueJsonVO::getId);
	public static final Comparator<CriterionInstantVO> CRITERION_INSTANT_VO_COMPARATOR = ComparatorFactory.createNullSafe(CriterionInstantVO::getPosition);
	public static final Comparator<AspSubstanceVO> SUBSTANCE_ID_COMPARATOR = ComparatorFactory.createNullSafe(AspSubstanceVO::getId);
	public static final Comparator<AssociationPath> ASSOCIATION_PATH_COMP = nullsFirst(comparing(AssociationPath::getJoinOrder, nullsFirst(naturalOrder())));
	public static final Comparator<Course> COURSE_COMP = nullsFirst(comparing(Course::getName).thenComparingLong(Course::getId));
	public static final Comparator<ECRFStatusAction> ECRF_STATUS_ACTION_COMP = nullsFirst(
			comparing(ECRFStatusAction::getAction, nullsFirst((a, b) -> ALPHANUM_COMP.compare(a.getValue(), b.getValue()))));
	public static final Comparator<Inventory> INVENTORY_COMP = nullsFirst(comparing(Inventory::getName, ALPHANUM_COMP));
	public static final Comparator<Map.Entry<String, String>> KEY_VALUE_LENGTH_COMP = nullsFirst(comparing(x -> x.getValue(), nullsFirst(comparingInt(String::length))));
	public static final Comparator<MoneyTransferOutVO> MONEY_TRANSFER_OUT_COMP = nullsFirst(
			comparing(MoneyTransferOutVO::getTransactionTimestamp, nullsFirst(naturalOrder())).thenComparing(MoneyTransferOutVO::getId));
	public static final Comparator<InquiryValueOutVO> INQUIRY_VALUE_OUT_VO_COMP = createInquiryValueOutVO();

	private static Comparator<InquiryValueOutVO> createInquiryValueOutVO() {
		Comparator<TrialOutVO> trialComp = comparing(TrialOutVO::getId);
		Comparator<InquiryOutVO> inquiryOutComp = comparing(InquiryOutVO::getTrial, trialComp)
				.thenComparing(comparing(InquiryOutVO::getCategory, nullsFirst(naturalOrder())))
				.thenComparing(comparing(InquiryOutVO::getPosition));
		return nullsFirst(comparing(InquiryValueOutVO::getInquiry, nullsFirst(inquiryOutComp)));
	}

	public static Comparator<BankAccountOutVO> createBankAccount() {
		Comparator<ProbandOutVO> probandComp = createProbandOutVO();
		return nullsFirst(comparing(BankAccountOutVO::getProband, probandComp));
	}

	public static Comparator<CvPositionPDFVO> createCvPositionPDFVO() {
		Comparator<CvPositionPDFVO> dateComp = nullsFirst(comparing(CvPositionPDFVO::getStart, nullsFirst(naturalOrder())));
		Comparator<CvSectionVO> positionComp = nullsFirst(comparingLong(CvSectionVO::getPosition));
		return nullsFirst(comparing(CvPositionPDFVO::getSection, positionComp).thenComparing(dateComp));
	}

	public static Comparator<InputFieldSelectionSetValueOutVO> createInputFieldSelectionSetValueOutVO() {
		Comparator<InputFieldOutVO> fieldNameComp = comparing(InputFieldOutVO::getName, ALPHANUM_COMP);
		Comparator<InputFieldSelectionSetValueOutVO> fieldComp = comparing(InputFieldSelectionSetValueOutVO::getField, nullsFirst(fieldNameComp));
		Comparator<InputFieldSelectionSetValueOutVO> valueComp = comparing(InputFieldSelectionSetValueOutVO::getValue, ALPHANUM_COMP);
		Comparator<InputFieldSelectionSetValueOutVO> nameComp = comparing(InputFieldSelectionSetValueOutVO::getName, ALPHANUM_COMP);
		Comparator<InputFieldSelectionSetValueOutVO> idComp = comparingLong(InputFieldSelectionSetValueOutVO::getId);
		return nullsFirst(fieldComp.thenComparing(valueComp).thenComparing(nameComp).thenComparing(idComp));
	}

	public static Comparator<ProbandListEntryTagValueOutVO> createProbandListEntryTagValueOutVO() {
		return nullsFirst(comparing(ProbandListEntryTagValueOutVO::getTag, nullsFirst(comparing(ProbandListEntryTagOutVO::getPosition, nullsFirst(naturalOrder())))));
	}

	public static Comparator<ProbandOutVO> createProbandOutVO() {
		Comparator<ProbandOutVO> animalComparator = comparing(ProbandOutVO::getAnimalName);
		Comparator<ProbandOutVO> personComparator = comparing(ProbandOutVO::getLastName, ALPHANUM_COMP).thenComparing(ProbandOutVO::getFirstName, ALPHANUM_COMP);
		return nullsFirst(decide(ProbandOutVO::isPerson, personComparator, animalComparator));
	}

	public static <T, U extends Comparable<? super U>> Comparator<T> decide(
			Function<? super T, Boolean> decideExtractor, Comparator<T> compA, Comparator<T> compB) {
		Objects.requireNonNull(decideExtractor);
		return (Comparator<T>) (c1, c2) -> {
			boolean a = decideExtractor.apply(c1);
			boolean b = decideExtractor.apply(c2);
			if (a && b) {
				return compA.compare(c1, c2);
			} else if (a && !b) {
				return -1;
			} else if (!a && b) {
				return 1;
			} else {
				return compB.compare(c1, c2);
			}
		};
	}

	public static final <T> Comparator<T> createGetIdReflective() {
		return nullsFirst(comparing(obj -> CommonUtil.getSafeLong(obj, "getId"), nullsFirst(naturalOrder())));
	}

	public static Comparator<Staff> createStaffComparator() {
		Comparator<OrganisationContactParticulars> orgComp = nullsFirst(comparing(OrganisationContactParticulars::getOrganisationName, ALPHANUM_COMP));
		Comparator<PersonContactParticulars> personComp = nullsFirst(comparing(PersonContactParticulars::getLastName, ALPHANUM_COMP));
		Comparator<Staff> staffComp = decide(Staff::isPerson, comparing(Staff::getPersonParticulars, personComp), comparing(Staff::getOrganisationParticulars, orgComp));
		return nullsFirst(staffComp);
	}

	public static Comparator<TeamMemberOutVO> createTeamMemberOutVO() {
		Comparator<TeamMemberRoleVO> roleComp = nullsFirst(comparing(TeamMemberRoleVO::getName, ALPHANUM_COMP));
		Comparator<StaffOutVO> staffPersonComp = comparing(StaffOutVO::getLastName, ALPHANUM_COMP);
		Comparator<StaffOutVO> staffOrgComp = comparing(StaffOutVO::getOrganisationName, ALPHANUM_COMP);
		Comparator<StaffOutVO> staffComp = nullsFirst(decide(StaffOutVO::isPerson, staffPersonComp, staffOrgComp));
		return nullsFirst(comparing(TeamMemberOutVO::getRole, roleComp).thenComparing(TeamMemberOutVO::getStaff, staffComp).thenComparing(TeamMemberOutVO::getId));
	}

	public static Comparator<TrialStatusAction> createTrialStatusAction() {
		return nullsFirst(comparing(TrialStatusAction::getAction, nullsFirst((a, b) -> ALPHANUM_COMP.compare(a.getValue(), b.getValue()))));
	}

	public static Comparator<VisitScheduleItemOutVO> createVisitScheduleItemOutVO() {
		Comparator<VisitScheduleItemOutVO> trialComp = nullsFirst(comparing(VisitScheduleItemOutVO::getTrial, nullsFirst(comparing(TrialOutVO::getName, ALPHANUM_COMP))));
		Comparator<VisitScheduleItemOutVO> probandComp = nullsFirst(
				comparing(VisitScheduleItemOutVO::getGroup, nullsFirst(comparing(ProbandGroupOutVO::getToken, ALPHANUM_COMP))));
		Comparator<VisitScheduleItemOutVO> visitComp = nullsFirst(comparing(VisitScheduleItemOutVO::getVisit, nullsFirst(comparing(VisitOutVO::getToken, ALPHANUM_COMP))));
		Comparator<VisitScheduleItemOutVO> tokenComp = nullsFirst(comparing(VisitScheduleItemOutVO::getToken, ALPHANUM_COMP));
		return nullsFirst(trialComp.thenComparing(probandComp).thenComparing(visitComp).thenComparing(tokenComp).thenComparing(createVisitScheduleItemOutVOTemporalOnly()));
	}

	public static Comparator<VisitScheduleItemOutVO> createVisitScheduleItemOutVOTemporalOnly() {
		Comparator<VisitScheduleItemOutVO> dateComp = nullsFirst(comparing(VisitScheduleItemOutVO::getStart, nullsFirst(naturalOrder())));
		Comparator<VisitScheduleItemOutVO> idComp = comparingLong(VisitScheduleItemOutVO::getId);
		return nullsFirst(dateComp.thenComparing(idComp));
	}

	public static <T, U extends Comparable<? super U>> Comparator<T> createNullSafe(Function<? super T, ? extends U> keyExtractor) {
		return nullsFirst(comparing(keyExtractor, nullsFirst(naturalOrder())));
	}

	private static <T> Comparator<T> createInterval(Function<T, ? extends Date> startAccessor, Function<T, ? extends Date> stopAccessor) {
		return nullsFirst(comparing(startAccessor, nullsLast(naturalOrder())).thenComparing(stopAccessor, nullsLast(naturalOrder())));
	}

	private static <T> Comparator<T> createScheduleInterval(Function<T, ? extends Date> startAccessor, Function<T, ? extends Date> stopAccessor) {
		Comparator<T> durationComparison = nullsFirst(
				comparing(x -> x == null ? null : CommonUtil.dateDeltaSecs(startAccessor.apply(x), stopAccessor.apply(x)), nullsLast(reverseOrder())));
		return durationComparison.thenComparing(createInterval(startAccessor, stopAccessor));
	}

	private ComparatorFactory() {
		// private constructor
	}
}
