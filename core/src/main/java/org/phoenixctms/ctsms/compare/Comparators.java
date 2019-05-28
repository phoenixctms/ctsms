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
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Function;

import org.phoenixctms.ctsms.domain.AnimalContactParticulars;
import org.phoenixctms.ctsms.domain.Course;
import org.phoenixctms.ctsms.domain.ECRFStatusAction;
import org.phoenixctms.ctsms.domain.Inventory;
import org.phoenixctms.ctsms.domain.OrganisationContactParticulars;
import org.phoenixctms.ctsms.domain.PersonContactParticulars;
import org.phoenixctms.ctsms.domain.Proband;
import org.phoenixctms.ctsms.domain.ProbandContactParticulars;
import org.phoenixctms.ctsms.domain.Staff;
import org.phoenixctms.ctsms.domain.TrialStatusAction;
import org.phoenixctms.ctsms.security.CryptoUtil;
import org.phoenixctms.ctsms.util.AssociationPath;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
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
import org.phoenixctms.ctsms.vo.ProbandListStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.vo.ProbandStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.StaffStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.TeamMemberOutVO;
import org.phoenixctms.ctsms.vo.TeamMemberRoleVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.vo.VisitOutVO;
import org.phoenixctms.ctsms.vo.VisitScheduleItemOutVO;

public final class Comparators {

	public static final Comparator<String> ALPHANUM = new AlphaNumComparator();
	public static final Comparator<String> ALPHANUM_TRIM = nullsFirst(comparing(String::trim, ALPHANUM));
	public static final Comparator<DateInterval> DATE_INTERVAL_INTERVAL = createInterval(DateInterval::getStart);
	public static final Comparator<DutyRosterTurnOutVO> DUTY_ROSTER_TURN_OUT_VO_SCHEDULE_INTERVAL = createScheduleInterval(DutyRosterTurnOutVO::getStart, DutyRosterTurnOutVO::getStop);
	public static final Comparator<DutyRosterTurnOutVO> DUTY_ROSTER_TURN_OUT_VO_INTERVAL = createInterval(DutyRosterTurnOutVO::getStart);
	public static final Comparator<InventoryBookingOutVO> INVENTORY_BOOKING_OUT_VO_SCHEDULE_INTERVAL = createScheduleInterval(InventoryBookingOutVO::getStart, InventoryBookingOutVO::getStop);
	public static final Comparator<InventoryBookingOutVO> INVENTORY_BOOKING_OUT_VO_INTERVAL = createInterval(InventoryBookingOutVO::getStart);
	public static final Comparator<InventoryStatusEntryOutVO> INVENTORY_STATUS_ENTRY_OUT_VO_SCHEDULE_INTERVAL = createScheduleInterval(InventoryStatusEntryOutVO::getStart,
			InventoryStatusEntryOutVO::getStop);
	public static final Comparator<InventoryStatusEntryOutVO> INVENTORY_STATUS_ENTRY_OUT_VO_INTERVAL = createInterval(InventoryStatusEntryOutVO::getStart);
	public static final Comparator<ProbandStatusEntryOutVO> PROBAND_STATUS_ENTRY_OUT_VO_SCHEDULE_INTERVAL = createScheduleInterval(ProbandStatusEntryOutVO::getStart,
			ProbandStatusEntryOutVO::getStop);
	public static final Comparator<ProbandStatusEntryOutVO> PROBAND_STATUS_ENTRY_OUT_VO_INTERVAL = createInterval(ProbandStatusEntryOutVO::getStart);
	public static final Comparator<StaffStatusEntryOutVO> STAFF_STATUS_ENTRY_OUT_VO_SCHEDULE_INTERVAL = createScheduleInterval(StaffStatusEntryOutVO::getStart, StaffStatusEntryOutVO::getStop);
	public static final Comparator<StaffStatusEntryOutVO> STAFF_STATUS_ENTRY_OUT_VO_INTERVAL = createInterval(StaffStatusEntryOutVO::getStart);
	public static final Comparator<VisitScheduleItemOutVO> VISIT_SCHEDULE_ITEM_OUT_VO_SCHEDULE_INTERVAL = createScheduleInterval(VisitScheduleItemOutVO::getStart,
			VisitScheduleItemOutVO::getStop);
	public static final Comparator<VisitScheduleItemOutVO> VISIT_SCHEDULE_ITEM_OUT_VO_INTERVAL = createInterval(VisitScheduleItemOutVO::getStart);
	public static final Comparator<AspSubstanceVO> ASP_SUBSTANCE_VO_ID = createNullSafe(AspSubstanceVO::getId);
	public static final Comparator<InputFieldSelectionSetValueJsonVO> INPUT_FIELD_SELECTION_SET_VALUE_JSON_VO_ID = createNullSafe(
			InputFieldSelectionSetValueJsonVO::getId);
	public static final Comparator<InputFieldSelectionSetValueOutVO> INPUT_FIELD_SELECTION_SET_VALUE_OUT_VO_ID = createNullSafe(
			InputFieldSelectionSetValueOutVO::getId);
	public static final Comparator<Inventory> INVENTORY = createInventory();
	public static final Comparator<AssociationPath> ASSOCIATION_PATH = createAssociationPath();
	public static final Comparator<Course> COURSE = createCourse();
	public static final Comparator<ECRFFieldValueOutVO> ECRF_FIELD_VALUE_OUT_VO = createECRFFieldValueOutVO();
	@SuppressWarnings("rawtypes")
	public static final Comparator ECRF_FIELD_VALUE_STATUS_ENTRY = createEcrfFieldValueStatusEntry();
	public static final Comparator<ECRFStatusAction> ECRF_STATUS_ACTION = createEcrfStatusAction();
	public static final Comparator<InquiryValueOutVO> INQUIRY_VALUE_OUT_VO = createInquiryValueOutVO();
	public static final Comparator<Map.Entry<String, String>> KEY_VALUE_LENGTH = createKeyValueLength().reversed();
	public static final Comparator<MoneyTransferOutVO> MONEY_TRANSFER_OUT_VO = createMoneyTransferOutVO();
	public static final Comparator<Proband> PROBAND = createProband();
	public static final Comparator<ProbandListStatusEntryOutVO> PROBAND_LIST_STATUS_ENTRY_OUT_VO = createProbandListStatusEntryOutVO();
	public static final Comparator<BankAccountOutVO> BANK_ACCOUNT_OUT_VO = createBankAccountOutVO();
	public static final Comparator<CvPositionPDFVO> CV_POSITION_PDF_VO = createCvPositionPDFVO();
	public static final Comparator<InputFieldSelectionSetValueOutVO> INPUT_FIELD_SELECTION_SET_VALUE_OUT_VO = createInputFieldSelectionSetValueOutVO();
	public static final Comparator<ProbandListEntryTagValueOutVO> PROBAND_LIST_ENTRY_TAG_VALUE_OUT_VO = createProbandListEntryTagValueOutVO();
	public static final Comparator<ProbandOutVO> PROBAND_OUT_VO = createProbandOutVO();
	public static final Comparator<Staff> STAFF = createStaff();
	public static final Comparator<TeamMemberOutVO> TEAM_MEMBER_OUT_VO = createTeamMemberOutVO();
	public static final Comparator<TrialStatusAction> TRIAL_STATUS_ACTION = createTrialStatusAction();
	public static final Comparator<VisitScheduleItemOutVO> VISIT_SCHEDULE_ITEM_OUT_VO_COMPLEX = createVisitScheduleItemOutVOComplex();
	public static final Comparator<VisitScheduleItemOutVO> VISIT_SCHEDULE_ITEM_OUT_VO_TEMPORAL = createVisitScheduleItemOutVOTemporal();
	public static final Comparator<CriterionInstantVO> CRITERION_INSTANT_VO_POSITION = createNullSafe(CriterionInstantVO::getPosition);
	/**
	 * Returns a comparator that compares on the outcome of a boolean extracting function.
	 * If the evaluated booleans for two objects are 
	 * <ul>
	 * <li>equal and true, compA is used</li>
	 * <li>equal and false compB is used</li>
	 * <li>different, then will be ordered as true < false 
	 * </ul>
	 * 
	 * @param <T> 
	 * @param <U>
	 * @param decideExtractor function to return the boolean
	 * @param compA comparator to use on true
	 * @param compB comparator to use on false
	 * @return a comparator that compares on extracted booleans or, if they are equal, on one of the comparators.
	 */
	public static <T, U extends Comparable<? super U>> Comparator<T> decide(
			Function<? super T, Boolean> decideExtractor, Comparator<T> compA, Comparator<T> compB) {
		Objects.requireNonNull(decideExtractor);
		Objects.requireNonNull(compA);
		Objects.requireNonNull(compB);
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
		return nullsFirst(comparing(obj -> CommonUtil.reflectiveGetIdCall(obj), nullsFirst(naturalOrder())));
	}

	public static <T, U extends Comparable<? super U>> Comparator<T> createNullSafe(Function<? super T, ? extends U> keyExtractor) {
		return nullsFirst(comparing(keyExtractor, nullsFirst(naturalOrder())));
	}

	private static <T> Comparator<T> createInterval(Function<T, ? extends Date> startAccessor) {
		return nullsFirst(comparing(startAccessor, nullsLast(naturalOrder())));
	}

	private static <T> Comparator<T> createScheduleInterval(Function<T, ? extends Date> startAccessor, Function<T, ? extends Date> stopAccessor) {
		Comparator<T> durationComp = nullsFirst(
				comparing(x -> x == null ? null : CommonUtil.dateDeltaSecs(startAccessor.apply(x), stopAccessor.apply(x)), nullsFirst(reverseOrder())));
		return durationComp.thenComparing(createInterval(startAccessor));
	}

	// special case comparators 
	private static Comparator<AssociationPath> createAssociationPath() {
		return nullsFirst(comparing(AssociationPath::getJoinOrder));
	}

	private static Comparator<BankAccountOutVO> createBankAccountOutVO() {
		Comparator<ProbandOutVO> lastnameComp = nullsFirst(comparing(ProbandOutVO::getLastName, ALPHANUM).thenComparingLong(ProbandOutVO::getId));
		return nullsFirst(comparing(BankAccountOutVO::getProband, lastnameComp).thenComparingLong(BankAccountOutVO::getId));
	}

	private static Comparator<Course> createCourse() {
		return nullsFirst(comparing(Course::getName, ALPHANUM).thenComparingLong(Course::getId));
	}

	private static Comparator<CvPositionPDFVO> createCvPositionPDFVO() {
		Comparator<CvSectionVO> positionComp = nullsFirst(comparingLong(CvSectionVO::getPosition));
		Comparator<CvPositionPDFVO> dateComp = nullsFirst(comparing(x -> (x.getStart() != null ? x.getStart() : x.getStop()), nullsFirst(naturalOrder())));
		return nullsFirst(comparing(CvPositionPDFVO::getSection, positionComp).thenComparing(dateComp));
	}

	private static Comparator<Object> createEcrfFieldValueStatusEntry() {
		return nullsFirst(comparing(x -> {
			if (x instanceof ECRFFieldValueOutVO) {
				return ((ECRFFieldValueOutVO) x).getModifiedTimestamp();
			} else if (x instanceof ECRFFieldStatusEntryOutVO) {
				return ((ECRFFieldStatusEntryOutVO) x).getModifiedTimestamp();
			} else {
				return null;
			}
		}, nullsFirst(naturalOrder())));
	}

	// Object tree and order priority of ECRFFieldValueOutVO:
	//	ECRFFieldValueOutVO
	//	|----index          (3)  
	//	|----ECRFFieldOutVO
	//		|----section    (2)
	//		|----position   (4)
	//		|----ECRFOutVO  
	//	   		|----id     (1)
	private static Comparator<ECRFFieldValueOutVO> createECRFFieldValueOutVO() {
		Comparator<ECRFFieldValueOutVO> idComp = comparing(x -> {
			if (x.getEcrfField() == null)
				return null;
			if (x.getEcrfField().getEcrf() == null)
				return null;
			return x.getEcrfField().getEcrf().getId();
		}, nullsFirst(naturalOrder()));
		Comparator<ECRFFieldValueOutVO> sectionComp = comparing(x -> {
			if (x.getEcrfField() == null)
				return null;
			return x.getEcrfField().getSection();
		}, nullsFirst(naturalOrder()));
		Comparator<ECRFFieldValueOutVO> indexComp = comparing(ECRFFieldValueOutVO::getIndex, nullsFirst(naturalOrder()));
		Comparator<ECRFFieldValueOutVO> positionComp = comparing(x -> {
			if (x.getEcrfField() == null)
				return null;
			return x.getEcrfField().getPosition();
		}, nullsFirst(naturalOrder()));
		return nullsFirst(idComp.thenComparing(sectionComp).thenComparing(indexComp).thenComparing(positionComp));
	}

	private static Comparator<ECRFStatusAction> createEcrfStatusAction() {
		return nullsFirst(
				comparing(ECRFStatusAction::getAction, nullsFirst(comparing(org.phoenixctms.ctsms.enumeration.ECRFStatusAction::getValue, ALPHANUM))));
	}

	private static Comparator<InputFieldSelectionSetValueOutVO> createInputFieldSelectionSetValueOutVO() {
		Comparator<InputFieldOutVO> fieldNameComp = comparing(InputFieldOutVO::getName, ALPHANUM);
		Comparator<InputFieldSelectionSetValueOutVO> fieldComp = comparing(InputFieldSelectionSetValueOutVO::getField, nullsFirst(fieldNameComp));
		Comparator<InputFieldSelectionSetValueOutVO> valueComp = comparing(InputFieldSelectionSetValueOutVO::getValue, ALPHANUM);
		Comparator<InputFieldSelectionSetValueOutVO> nameComp = comparing(InputFieldSelectionSetValueOutVO::getName, ALPHANUM);
		Comparator<InputFieldSelectionSetValueOutVO> idComp = comparingLong(InputFieldSelectionSetValueOutVO::getId);
		return nullsFirst(fieldComp.thenComparing(valueComp).thenComparing(nameComp).thenComparing(idComp));
	}

	private static Comparator<InquiryValueOutVO> createInquiryValueOutVO() {
		Comparator<TrialOutVO> trialComp = comparing(TrialOutVO::getId);
		Comparator<InquiryOutVO> inquiryOutComp = comparing(InquiryOutVO::getTrial, trialComp)
				.thenComparing(comparing(InquiryOutVO::getCategory, nullsFirst(naturalOrder())))
				.thenComparing(comparing(InquiryOutVO::getPosition));
		return nullsFirst(comparing(InquiryValueOutVO::getInquiry, nullsFirst(inquiryOutComp)));
	}

	private static Comparator<Inventory> createInventory() {
		return nullsFirst(comparing(Inventory::getName, ALPHANUM).thenComparing(Inventory::getId));
	}

	private static Comparator<Entry<String, String>> createKeyValueLength() {
		return nullsFirst(comparing(Entry::getValue, nullsFirst(comparingInt(String::length))));
	}

	private static Comparator<MoneyTransferOutVO> createMoneyTransferOutVO() {
		return nullsFirst(
				comparing(MoneyTransferOutVO::getTransactionTimestamp, nullsFirst(naturalOrder())).thenComparingLong(MoneyTransferOutVO::getId));
	}

	private static Comparator<Proband> createProband() {
		Comparator<ProbandContactParticulars> lastNameComp = comparing(x -> getLastName(x), ALPHANUM);
		Comparator<ProbandContactParticulars> firstNameComp = comparing(x -> getFirstName(x), ALPHANUM);
		Comparator<ProbandContactParticulars> personNameComp = lastNameComp.thenComparing(firstNameComp);
		Comparator<ProbandContactParticulars> personAliasComp = comparing(ProbandContactParticulars::getAlias, ALPHANUM);
		Comparator<AnimalContactParticulars> animalAliasComp = comparing(AnimalContactParticulars::getAlias, ALPHANUM);
		Comparator<AnimalContactParticulars> animalNameComp = comparing(AnimalContactParticulars::getAnimalName, ALPHANUM);
		Comparator<Proband> unblindedPerson = comparing(Proband::getPersonParticulars, nullsFirst(personNameComp)).thenComparingLong(Proband::getId);
		Comparator<Proband> blindedPerson = comparing(Proband::getPersonParticulars, nullsFirst(personAliasComp)).thenComparingLong(Proband::getId);
		Comparator<Proband> unblindedAnimal = comparing(Proband::getAnimalParticulars, nullsFirst(animalNameComp)).thenComparingLong(Proband::getId);
		Comparator<Proband> blindedAnimal = comparing(Proband::getAnimalParticulars, nullsFirst(animalAliasComp)).thenComparingLong(Proband::getId);
		return nullsFirst(decide(Proband::isPerson, decide(Proband::isBlinded, blindedPerson, unblindedPerson), decide(Proband::isBlinded, blindedAnimal, unblindedAnimal)));
	}

	private static Comparator<ProbandListEntryTagValueOutVO> createProbandListEntryTagValueOutVO() {
		return nullsFirst(comparing(ProbandListEntryTagValueOutVO::getTag, nullsFirst(comparing(ProbandListEntryTagOutVO::getPosition, nullsFirst(naturalOrder())))));
	}

	private static Comparator<ProbandListStatusEntryOutVO> createProbandListStatusEntryOutVO() {
		return nullsFirst(
				comparing(ProbandListStatusEntryOutVO::getRealTimestamp).thenComparing(ProbandListStatusEntryOutVO::getId));
	}

	private static Comparator<ProbandOutVO> createProbandOutVO() {
		Comparator<ProbandOutVO> personComparator = comparing(ProbandOutVO::getLastName, ALPHANUM).thenComparing(ProbandOutVO::getFirstName, ALPHANUM)
				.thenComparingLong(ProbandOutVO::getId);
		Comparator<ProbandOutVO> animalComparator = comparing(ProbandOutVO::getAnimalName).thenComparingLong(ProbandOutVO::getId);
		return nullsFirst(decide(ProbandOutVO::isPerson, personComparator, animalComparator));
	}

	private static Comparator<Staff> createStaff() {
		Comparator<PersonContactParticulars> personComp = nullsFirst(comparing(PersonContactParticulars::getLastName, ALPHANUM)
				.thenComparing(PersonContactParticulars::getFirstName, ALPHANUM));
		Comparator<OrganisationContactParticulars> orgComp = nullsFirst(comparing(OrganisationContactParticulars::getOrganisationName, ALPHANUM));
		return nullsFirst(decide(Staff::isPerson,
				comparing(Staff::getPersonParticulars, personComp).thenComparing(Staff::getId),
				comparing(Staff::getOrganisationParticulars, orgComp).thenComparing(Staff::getId)));
	}

	private static Comparator<TeamMemberOutVO> createTeamMemberOutVO() {
		Comparator<TeamMemberRoleVO> roleComp = nullsFirst(comparing(TeamMemberRoleVO::getName, ALPHANUM));
		Comparator<StaffOutVO> personComp = comparing(StaffOutVO::getLastName, ALPHANUM);
		Comparator<StaffOutVO> orgComp = comparing(StaffOutVO::getOrganisationName, ALPHANUM);
		return nullsFirst(comparing(TeamMemberOutVO::getRole, roleComp)
				.thenComparing(TeamMemberOutVO::getStaff, nullsFirst(decide(StaffOutVO::isPerson, personComp, orgComp)))
				.thenComparing(TeamMemberOutVO::getId));
	}

	private static Comparator<TrialStatusAction> createTrialStatusAction() {
		return nullsFirst(comparing(TrialStatusAction::getAction, nullsFirst(comparing(org.phoenixctms.ctsms.enumeration.TrialStatusAction::getValue, ALPHANUM))));
	}

	private static Comparator<VisitScheduleItemOutVO> createVisitScheduleItemOutVOComplex() {
		Comparator<VisitScheduleItemOutVO> trialComp = comparing(VisitScheduleItemOutVO::getTrial, nullsFirst(comparing(TrialOutVO::getName, ALPHANUM)));
		Comparator<VisitScheduleItemOutVO> probandComp = comparing(VisitScheduleItemOutVO::getGroup, nullsFirst(comparing(ProbandGroupOutVO::getToken, ALPHANUM)));
		Comparator<VisitScheduleItemOutVO> visitComp = comparing(VisitScheduleItemOutVO::getVisit, nullsFirst(comparing(VisitOutVO::getToken, ALPHANUM)));
		Comparator<VisitScheduleItemOutVO> tokenComp = comparing(VisitScheduleItemOutVO::getToken, ALPHANUM);
		return nullsFirst(trialComp.thenComparing(probandComp).thenComparing(visitComp).thenComparing(tokenComp).thenComparing(VISIT_SCHEDULE_ITEM_OUT_VO_TEMPORAL));
	}

	private static Comparator<VisitScheduleItemOutVO> createVisitScheduleItemOutVOTemporal() {
		Comparator<VisitScheduleItemOutVO> dateComp = comparing(VisitScheduleItemOutVO::getStart, nullsFirst(naturalOrder()));
		Comparator<VisitScheduleItemOutVO> idComp = comparingLong(VisitScheduleItemOutVO::getId);
		return nullsFirst(dateComp.thenComparing(idComp));
	}

	// helper methods for the proband comparator
	private static String getDecrypted(ProbandContactParticulars particulars,
			Function<ProbandContactParticulars, byte[]> ivFun,
			Function<ProbandContactParticulars, byte[]> valueFun) {
		if (particulars != null && CoreUtil.isPassDecryption()) {
			try {
				return (String) CryptoUtil.decryptValue(ivFun.apply(particulars), valueFun.apply(particulars));
			} catch (Exception e) {
			}
		}
		return null;
	}

	private static String getFirstName(ProbandContactParticulars particulars) {
		return getDecrypted(particulars, ProbandContactParticulars::getFirstNameIv, ProbandContactParticulars::getEncryptedFirstName);
	}

	private static String getLastName(ProbandContactParticulars particulars) {
		return getDecrypted(particulars, ProbandContactParticulars::getLastNameIv, ProbandContactParticulars::getEncryptedLastName);
	}

	private Comparators() {
		// private constructor
	}
}
