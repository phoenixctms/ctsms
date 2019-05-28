package org.phoenixctms.ctsms.compare;

import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingInt;
import static java.util.Comparator.comparingLong;
import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsLast;

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
		return nullsLast(comparing(Course::getName).thenComparingLong(Course::getId));
	}

	public static final Comparator<String> ALPHANUM_COMPARATOR = new AlphaNumComparator();
	public static final Comparator<String> ALPHANUM_TRIM_COMPARATOR = nullsLast(comparing(String::trim, ALPHANUM_COMPARATOR));

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
				.thenComparing(TeamMemberOutVO::getStaff, staffNameComp)
				.thenComparing(TeamMemberOutVO::getId));
	}

	public static Comparator<VisitScheduleItemOutVO> createVisitScheduleItemOutVO() {
		Comparator<VisitScheduleItemOutVO> trialComp = nullsLast(comparing(VisitScheduleItemOutVO::getTrial, nullsLast(comparing(TrialOutVO::getName, ALPHANUM_COMPARATOR))));
		Comparator<VisitScheduleItemOutVO> probandComp = nullsLast(
				comparing(VisitScheduleItemOutVO::getGroup, nullsLast(comparing(ProbandGroupOutVO::getToken, ALPHANUM_COMPARATOR))));
		Comparator<VisitScheduleItemOutVO> visitComp = nullsLast(comparing(VisitScheduleItemOutVO::getVisit, nullsLast(comparing(VisitOutVO::getToken, ALPHANUM_COMPARATOR))));
		Comparator<VisitScheduleItemOutVO> tokenComp = nullsLast(comparing(VisitScheduleItemOutVO::getToken, ALPHANUM_COMPARATOR));
		return nullsLast(trialComp.thenComparing(probandComp).thenComparing(visitComp).thenComparing(tokenComp).thenComparing(createVisitScheduleItemOutVOTemporalOnly()));
	}

	public static Comparator<VisitScheduleItemOutVO> createVisitScheduleItemOutVOTemporalOnly() {
		Comparator<VisitScheduleItemOutVO> dateComp = nullsLast(comparing(VisitScheduleItemOutVO::getStart, nullsLast(naturalOrder())));
		Comparator<VisitScheduleItemOutVO> idComp = comparingLong(VisitScheduleItemOutVO::getId);
		return nullsLast(dateComp.thenComparing(idComp));
	}

	public static Comparator<Inventory> createInventory() {
		return nullsLast(comparing(Inventory::getName, ALPHANUM_COMPARATOR));
	}

	public static Comparator<ECRFStatusAction> createECRFStatusAction() {
		return nullsLast(comparing(ECRFStatusAction::getAction, nullsLast((a, b) -> ALPHANUM_COMPARATOR.compare(a.getValue(), b.getValue()))));
	}

	public static Comparator<TrialStatusAction> createTrialStatusAction() {
		return nullsLast(comparing(TrialStatusAction::getAction, nullsLast((a, b) -> ALPHANUM_COMPARATOR.compare(a.getValue(), b.getValue()))));
	}

	public static Comparator<InputFieldSelectionSetValueOutVO> createInputFieldSelectionSetValueOutVO() {
		Comparator<InputFieldOutVO> fieldNameComp = comparing(InputFieldOutVO::getName, nullsLast(ALPHANUM_COMPARATOR));
		Comparator<InputFieldSelectionSetValueOutVO> fieldComp = comparing(InputFieldSelectionSetValueOutVO::getField, nullsLast(fieldNameComp));
		Comparator<InputFieldSelectionSetValueOutVO> valueComp = comparing(InputFieldSelectionSetValueOutVO::getValue, nullsLast(ALPHANUM_COMPARATOR));
		Comparator<InputFieldSelectionSetValueOutVO> nameComp = comparing(InputFieldSelectionSetValueOutVO::getName, nullsLast(ALPHANUM_COMPARATOR));
		Comparator<InputFieldSelectionSetValueOutVO> idComp = comparingLong(InputFieldSelectionSetValueOutVO::getId);
		return nullsLast(fieldComp.thenComparing(valueComp).thenComparing(nameComp).thenComparing(idComp));
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
	public static final Comparator ECRF_FIELD_VALUE_STATUS_ENTRY_OUT_VO_COMP = nullsLast(comparing(x -> {
		if (x instanceof ECRFFieldValueOutVO) {
			return ((ECRFFieldValueOutVO) x).getModifiedTimestamp();
		} else if (x instanceof ECRFFieldStatusEntryOutVO) {
			return ((ECRFFieldStatusEntryOutVO) x).getModifiedTimestamp();
		} else {
			return null;
		}
	}, nullsLast(naturalOrder())));

	private static <T> Comparator<T> createScheduleInterval(Function<T, ? extends Date> startAccessor, Function<T, ? extends Date> stopAccessor) {
		return new Comparator<T>() {

			Comparator<T> intervalComp = createInterval(startAccessor, stopAccessor);

			@Override
			public int compare(T a, T b) {
				if (a != null && b != null) {
					Date intervalAStart = startAccessor.apply(a);
					Date intervalAStop = stopAccessor.apply(a);
					Date intervalBStart = startAccessor.apply(b);
					Date intervalBStop = stopAccessor.apply(b);
					if (intervalAStart != null && intervalAStop != null) {
						if (intervalBStart != null && intervalBStop != null) {
							long intervalADuration = CommonUtil.dateDeltaSecs(intervalAStart, intervalAStop);
							long intervalBDuration = CommonUtil.dateDeltaSecs(intervalBStart, intervalBStop);
							if (intervalADuration == intervalBDuration) {
								return intervalComp.compare(a, b);
							} else {
								return (new Long(intervalBDuration)).compareTo(intervalADuration);
							}
						} else {
							return 1;
						}
					} else {
						if (intervalBStart != null && intervalBStop != null) {
							return -1;
						} else {
							return intervalComp.compare(a, b);
						}
					}
				} else {
					return intervalComp.compare(a, b);
				}
			}
		};
	}

	private static <T> Comparator<T> createInterval(Function<T, ? extends Date> startAccessor, Function<T, ? extends Date> stopAccessor) {
		return new Comparator<T>() {

			@Override
			public int compare(T a, T b) {
				if (a != null && b != null) {
					Date intervalAStart = startAccessor.apply(a);
					Date intervalAStop = stopAccessor.apply(a);
					Date intervalBStart = startAccessor.apply(b);
					Date intervalBStop = stopAccessor.apply(b);
					if (intervalAStart != null && intervalAStop != null) { // closed interval
						if (intervalBStart != null && intervalBStop != null) { // closed duration
							return intervalAStart.compareTo(intervalBStart);
						} else if (intervalBStart == null && intervalBStop != null) {
							return -1;
						} else if (intervalBStart != null && intervalBStop == null) {
							return intervalAStart.compareTo(intervalBStart);
						} else {
							return -1;
						}
					} else if (intervalAStart == null && intervalAStop != null) {
						if (intervalBStart != null && intervalBStop != null) {
							return 1;
						} else if (intervalBStart == null && intervalBStop != null) {
							return 0;
						} else if (intervalBStart != null && intervalBStop == null) {
							return 1;
						} else {
							return 0;
						}
					} else if (intervalAStart != null && intervalAStop == null) {
						if (intervalBStart != null && intervalBStop != null) {
							return intervalAStart.compareTo(intervalBStart);
						} else if (intervalBStart == null && intervalBStop != null) {
							return -1;
						} else if (intervalBStart != null && intervalBStop == null) {
							return intervalAStart.compareTo(intervalBStart);
						} else {
							return -1;
						}
					} else {
						if (intervalBStart != null && intervalBStop != null) {
							return 1;
						} else if (intervalBStart == null && intervalBStop != null) {
							return 0;
						} else if (intervalBStart != null && intervalBStop == null) {
							return 1;
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
		};
	}

	public static Comparator<ProbandListEntryTagValueOutVO> createProbandListEntryTagValueOutVO() {
		return nullsLast(comparing(ProbandListEntryTagValueOutVO::getTag, nullsLast(comparing(ProbandListEntryTagOutVO::getPosition, nullsLast(naturalOrder())))));
	}
	
	public static Comparator<Map.Entry<String, String>> createKeyValueLength() {
		return nullsLast(comparing(x->x.getValue(), nullsLast(comparingInt(String::length))));
	}

	public static <T, U extends Comparable<? super U>> Comparator<T> createSafeLong(Function<? super T, ? extends U> keyExtractor) {
		return nullsLast(comparing(keyExtractor, nullsLast(naturalOrder())));
	}

	public static <T> Comparator<T> createReflectionId() {
		return nullsLast(comparing(obj -> CommonUtil.getSafeLong(obj, "getId"), nullsLast(naturalOrder())));
	}
}
