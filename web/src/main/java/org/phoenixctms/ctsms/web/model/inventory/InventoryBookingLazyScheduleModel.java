package org.phoenixctms.ctsms.web.model.inventory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.phoenixctms.ctsms.compare.InventoryBookingIntervalScheduleComparator;
import org.phoenixctms.ctsms.compare.InventoryStatusEntryIntervalScheduleComparator;
import org.phoenixctms.ctsms.compare.ProbandStatusEntryIntervalScheduleComparator;
import org.phoenixctms.ctsms.compare.VisitScheduleAppointmentIntervalScheduleComparator;
import org.phoenixctms.ctsms.enumeration.Color;
import org.phoenixctms.ctsms.enumeration.VariablePeriod;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.CourseOutVO;
import org.phoenixctms.ctsms.vo.HolidayVO;
import org.phoenixctms.ctsms.vo.InventoryBookingOutVO;
import org.phoenixctms.ctsms.vo.InventoryStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.MaintenanceScheduleItemOutVO;
import org.phoenixctms.ctsms.vo.ProbandStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.VisitScheduleAppointmentVO;
import org.phoenixctms.ctsms.web.model.LazyScheduleModelBase;
import org.phoenixctms.ctsms.web.model.shared.CollidingCourseParticipationStatusEntryEagerModel;
import org.phoenixctms.ctsms.web.model.shared.CollidingDutyRosterTurnEagerModel;
import org.phoenixctms.ctsms.web.model.shared.CollidingInventoryStatusEntryEagerModel;
import org.phoenixctms.ctsms.web.model.shared.CollidingProbandStatusEntryEagerModel;
import org.phoenixctms.ctsms.web.model.shared.CollidingStaffStatusEntryEagerModel;
import org.phoenixctms.ctsms.web.model.shared.CourseEvent;
import org.phoenixctms.ctsms.web.model.shared.HolidayEvent;
import org.phoenixctms.ctsms.web.model.shared.InventoryBookingEvent;
import org.phoenixctms.ctsms.web.model.shared.ProbandStatusEvent;
import org.phoenixctms.ctsms.web.model.trial.VisitScheduleAppointmentEvent;
import org.phoenixctms.ctsms.web.util.DateUtil;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class InventoryBookingLazyScheduleModel extends LazyScheduleModelBase {

	private HashMap<Long, CollidingInventoryStatusEntryEagerModel> collidingInventoryStatusEntryModelCache;
	private HashMap<Long, CollidingStaffStatusEntryEagerModel> collidingStaffStatusEntryModelCache;
	private HashMap<Long, CollidingDutyRosterTurnEagerModel> collidingDutyRosterTurnModelCache;
	private HashMap<Long, CollidingProbandStatusEntryEagerModel> collidingProbandStatusEntryModelCache;
	private HashMap<Long, CollidingCourseParticipationStatusEntryEagerModel> collidingCourseParticipationStatusEntryModelCache;
	private boolean showHolidays;
	private boolean showBookings;
	private boolean showInventoryStatus;
	private boolean showProbandStatus;
	private boolean showMaintenanceItems;
	private boolean showCourses;
	private boolean showVisitSchedule;
	private boolean showCollisions;
	private Long showCollisionsThresholdDays;
	private Long departmentId;
	private Long inventoryCategoryId;
	private Long probandCategoryId;
	private Long courseCategoryId;
	private Long inventoryStatusTypeId;
	private Long responsiblePersonId;
	private Long inventoryId;
	private Boolean notify;
	private Long probandId;
	private Long courseId;
	private Long trialId;
	private Long visitTypeId;
	private String calendar;
	private Boolean hideProbandAvailability;
	private Boolean hideInventoryAvailability;
	private List<Color> trialColors;

	public InventoryBookingLazyScheduleModel(
			HashMap<Long, CollidingInventoryStatusEntryEagerModel> collidingInventoryStatusEntryModelCache,
			HashMap<Long, CollidingStaffStatusEntryEagerModel> collidingStaffStatusEntryModelCache,
			HashMap<Long, CollidingDutyRosterTurnEagerModel> collidingDutyRosterTurnModelCache,
			HashMap<Long, CollidingProbandStatusEntryEagerModel> collidingProbandStatusEntryModelCache,
			HashMap<Long, CollidingCourseParticipationStatusEntryEagerModel> collidingCourseParticipationStatusEntryModelCache) {
		super();
		this.collidingInventoryStatusEntryModelCache = collidingInventoryStatusEntryModelCache;
		this.collidingStaffStatusEntryModelCache = collidingStaffStatusEntryModelCache;
		this.collidingDutyRosterTurnModelCache = collidingDutyRosterTurnModelCache;
		this.collidingProbandStatusEntryModelCache = collidingProbandStatusEntryModelCache;
		this.collidingCourseParticipationStatusEntryModelCache = collidingCourseParticipationStatusEntryModelCache;
		showHolidays = Settings.getBoolean(SettingCodes.INVENTORY_BOOKING_SCHEDULE_SHOW_HOLIDAYS_PRESET, Bundle.SETTINGS,
				DefaultSettings.INVENTORY_BOOKING_SCHEDULE_SHOW_HOLIDAYS_PRESET);
		showBookings = Settings.getBoolean(SettingCodes.INVENTORY_BOOKING_SCHEDULE_SHOW_BOOKINGS_PRESET, Bundle.SETTINGS,
				DefaultSettings.INVENTORY_BOOKING_SCHEDULE_SHOW_BOOKINGS_PRESET);
		showInventoryStatus = Settings.getBoolean(SettingCodes.INVENTORY_BOOKING_SCHEDULE_SHOW_INVENTORY_STATUS_PRESET, Bundle.SETTINGS,
				DefaultSettings.INVENTORY_BOOKING_SCHEDULE_SHOW_INVENTORY_STATUS_PRESET);
		showProbandStatus = Settings.getBoolean(SettingCodes.INVENTORY_BOOKING_SCHEDULE_SHOW_PROBAND_STATUS_PRESET, Bundle.SETTINGS,
				DefaultSettings.INVENTORY_BOOKING_SCHEDULE_SHOW_PROBAND_STATUS_PRESET);
		showMaintenanceItems = Settings.getBoolean(SettingCodes.INVENTORY_BOOKING_SCHEDULE_SHOW_MAINTENANCE_ITEMS_PRESET, Bundle.SETTINGS,
				DefaultSettings.INVENTORY_BOOKING_SCHEDULE_SHOW_MAINTENANCE_ITEMS_PRESET);
		showCourses = Settings.getBoolean(SettingCodes.INVENTORY_BOOKING_SCHEDULE_SHOW_SHOW_COURSES_PRESET, Bundle.SETTINGS,
				DefaultSettings.INVENTORY_BOOKING_SCHEDULE_SHOW_SHOW_COURSES_PRESET);
		showVisitSchedule = Settings.getBoolean(SettingCodes.INVENTORY_BOOKING_SCHEDULE_SHOW_VISIT_SCHEDULE_PRESET, Bundle.SETTINGS,
				DefaultSettings.INVENTORY_BOOKING_SCHEDULE_SHOW_VISIT_SCHEDULE_PRESET);
		trialColors = Settings.getColorList(SettingCodes.INVENTORY_BOOKING_SCHEDULE_TRIAL_COLORS, Bundle.SETTINGS, DefaultSettings.INVENTORY_BOOKING_SCHEDULE_TRIAL_COLORS);
		showCollisionsThresholdDays = Settings.getLongNullable(SettingCodes.INVENTORY_BOOKING_SCHEDULE_SHOW_COLLISIONS_THRESHOLD_DAYS, Bundle.SETTINGS,
				DefaultSettings.INVENTORY_BOOKING_SCHEDULE_SHOW_COLLISIONS_THRESHOLD_DAYS);
		setShowNotifyOnly(Settings.getBoolean(SettingCodes.MAINTENANCE_SCHEDULE_SHOW_NOTIFY_ONLY_PRESET, Bundle.SETTINGS,
				DefaultSettings.MAINTENANCE_SCHEDULE_SHOW_NOTIFY_ONLY_PRESET));
		setIgnoreObsoleteProbandStatus(Settings.getBoolean(SettingCodes.PROBAND_STATUS_IGNORE_OBSOLETE_PRESET, Bundle.SETTINGS,
				DefaultSettings.PROBAND_STATUS_IGNORE_OBSOLETE_PRESET));
		setIgnoreObsoleteInventoryStatus(Settings.getBoolean(SettingCodes.INVENTORY_STATUS_IGNORE_OBSOLETE_PRESET, Bundle.SETTINGS,
				DefaultSettings.INVENTORY_STATUS_IGNORE_OBSOLETE_PRESET));
		showCollisions = Settings.getBoolean(SettingCodes.INVENTORY_BOOKING_SCHEDULE_SHOW_COLLISIONS_PRESET, Bundle.SETTINGS,
				DefaultSettings.INVENTORY_BOOKING_SCHEDULE_SHOW_COLLISIONS_PRESET);
		StaffOutVO identity = WebUtil.getUserIdentity();
		if (identity != null) {
			if (Settings.getBoolean(SettingCodes.INVENTORY_BOOKING_SCHEDULE_ACTIVE_IDENTITY_PRESET, Bundle.SETTINGS,
					DefaultSettings.INVENTORY_BOOKING_SCHEDULE_ACTIVE_IDENTITY_PRESET)) {
				setResponsiblePersonId(identity.getId());
			}
			if (Settings.getBoolean(SettingCodes.INVENTORY_BOOKING_SCHEDULE_ACTIVE_IDENTITY_DEPARTMENT_PRESET, Bundle.SETTINGS,
					DefaultSettings.INVENTORY_BOOKING_SCHEDULE_ACTIVE_IDENTITY_DEPARTMENT_PRESET)) {
				setDepartmentId(identity.getDepartment().getId());
			}
		}
	}

	@Override
	public void clearCaches() {
		collidingInventoryStatusEntryModelCache.clear();
		collidingStaffStatusEntryModelCache.clear();
		collidingDutyRosterTurnModelCache.clear();
		collidingProbandStatusEntryModelCache.clear();
		collidingCourseParticipationStatusEntryModelCache.clear();
	}

	public String getCalendar() {
		return calendar;
	}

	public Long getCourseCategoryId() {
		return courseCategoryId;
	}

	public Long getCourseId() {
		return courseId;
	}

	public String getCourseName() {
		return WebUtil.courseIdToName(courseId);
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public boolean getIgnoreObsoleteInventoryStatus() {
		return hideInventoryAvailability != null;
	}

	public boolean getIgnoreObsoleteProbandStatus() {
		return hideProbandAvailability != null;
	}

	public Long getInventoryCategoryId() {
		return inventoryCategoryId;
	}

	public Long getInventoryId() {
		return inventoryId;
	}

	public String getInventoryName() {
		return WebUtil.inventoryIdToName(inventoryId);
	}

	public Long getInventoryStatusTypeId() {
		return inventoryStatusTypeId;
	}

	@Override
	protected void getLazyResult(Date start, Date stop) {
		Date from = DateUtil.sanitizeScheduleTimestamp(true, start);
		Date to = DateUtil.sanitizeScheduleTimestamp(true, stop);
		AuthenticationVO auth = WebUtil.getAuthentication();
		if (showHolidays) {
			Collection<HolidayVO> holidays = null;
			try {
				holidays = WebUtil.getServiceLocator().getToolsService()
						.getHolidays(auth, from, to, Settings.getBooleanNullable(SettingCodes.SHOW_HOLIDAYS, Bundle.SETTINGS, DefaultSettings.SHOW_HOLIDAYS));
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
			if (holidays != null) {
				Iterator<HolidayVO> it = holidays.iterator();
				while (it.hasNext()) {
					addEvent(new HolidayEvent(it.next()));
				}
			}
		}
		if (showBookings) {
			Collection<InventoryBookingOutVO> bookings = null;
			try {
				bookings = WebUtil.getServiceLocator().getInventoryService()
						.getInventoryBookingInterval(auth, departmentId, inventoryCategoryId, inventoryId, responsiblePersonId, probandId, courseId, trialId, calendar, from, to,
								false);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
			if (bookings != null) {
				bookings = new ArrayList(bookings);
				Collections.sort((ArrayList) bookings, new InventoryBookingIntervalScheduleComparator(false));
				boolean loadCollisions = showCollisions && (showCollisionsThresholdDays == null ? true
						: to.compareTo(WebUtil.addIntervals(from, VariablePeriod.EXPLICIT,
								showCollisionsThresholdDays, 1)) <= 0);
				HashMap<Long, Color> trialColorMap = new HashMap<Long, Color>(bookings.size());
				Iterator<Color> trialColorsIt = CommonUtil.getCircularIterator(trialColors);
				Iterator<InventoryBookingOutVO> it = bookings.iterator();
				while (it.hasNext()) {
					InventoryBookingOutVO booking = it.next();
					if (booking.getTrial() != null && !trialColorMap.containsKey(booking.getTrial().getId()) && trialColorsIt.hasNext()) {
						trialColorMap.put(booking.getTrial().getId(), trialColorsIt.next());
					}
					InventoryBookingEvent event = new InventoryBookingEvent(booking);
					event.setCollidingInventoryStatusEntryCount(CollidingInventoryStatusEntryEagerModel.getCachedCollidingInventoryStatusEntryModel(booking, loadCollisions,
							collidingInventoryStatusEntryModelCache).getAllRowCount());
					event.setCollidingStaffStatusEntryCount(CollidingStaffStatusEntryEagerModel.getCachedCollidingStaffStatusEntryModel(booking, loadCollisions,
							collidingStaffStatusEntryModelCache).getAllRowCount());
					event.setCollidingDutyRosterTurnCount(CollidingDutyRosterTurnEagerModel.getCachedCollidingDutyRosterTurnModel(booking, loadCollisions,
							collidingDutyRosterTurnModelCache)
							.getAllRowCount());
					event.setCollidingProbandStatusEntryCount(CollidingProbandStatusEntryEagerModel.getCachedCollidingProbandStatusEntryModel(booking, loadCollisions,
							collidingProbandStatusEntryModelCache).getAllRowCount());
					event.setCollidingCourseParticipationStatusEntryCount(CollidingCourseParticipationStatusEntryEagerModel.getCachedCollidingCourseParticipationStatusEntryModel(
							booking, loadCollisions, collidingCourseParticipationStatusEntryModelCache).getAllRowCount());
					event.setTrialColorMap(trialColorMap);
					addEvent(event);
				}
			}
		}
		if (showInventoryStatus) {
			Collection<InventoryStatusEntryOutVO> statusEntries = null;
			try {
				statusEntries = WebUtil.getServiceLocator().getInventoryService()
						.getInventoryStatusEntryInterval(auth, departmentId, inventoryCategoryId, inventoryStatusTypeId, hideInventoryAvailability, from, to, false);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
			if (statusEntries != null) {
				statusEntries = new ArrayList(statusEntries);
				Collections.sort((ArrayList) statusEntries, new InventoryStatusEntryIntervalScheduleComparator(false));
				Iterator<InventoryStatusEntryOutVO> it = statusEntries.iterator();
				while (it.hasNext()) {
					addEvent(new InventoryStatusEvent(it.next()));
				}
			}
		}
		if (showProbandStatus) {
			Collection<ProbandStatusEntryOutVO> statusEntries = null;
			try {
				statusEntries = WebUtil.getServiceLocator().getProbandService()
						.getProbandStatusEntryInterval(auth, departmentId, probandCategoryId, hideProbandAvailability, from, to, false);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
			if (statusEntries != null) {
				statusEntries = new ArrayList(statusEntries);
				Collections.sort((ArrayList) statusEntries, new ProbandStatusEntryIntervalScheduleComparator(false));
				Iterator<ProbandStatusEntryOutVO> it = statusEntries.iterator();
				while (it.hasNext()) {
					addEvent(new ProbandStatusEvent(it.next()));
				}
			}
		}
		if (showVisitSchedule) {
			Collection<VisitScheduleAppointmentVO> visitScheduleAppointments = null;
			try {
				visitScheduleAppointments = WebUtil.getServiceLocator().getTrialService()
						.getVisitScheduleItemInterval(auth, trialId, departmentId, null, visitTypeId, from, to, false);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
			if (visitScheduleAppointments != null) {
				visitScheduleAppointments = new ArrayList(visitScheduleAppointments);
				Collections.sort((ArrayList) visitScheduleAppointments, new VisitScheduleAppointmentIntervalScheduleComparator(false));
				Iterator<VisitScheduleAppointmentVO> it = visitScheduleAppointments.iterator();
				while (it.hasNext()) {
					addEvent(new VisitScheduleAppointmentEvent(it.next()));
				}
			}
		}
		if (showMaintenanceItems) {
			Collection<MaintenanceScheduleItemOutVO> maintenanceItems = null;
			try {
				maintenanceItems = WebUtil.getServiceLocator().getInventoryService()
						.getMaintenanceInterval(auth, inventoryId, departmentId, inventoryCategoryId, responsiblePersonId, notify, from, to);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
			if (maintenanceItems != null) {
				Iterator<MaintenanceScheduleItemOutVO> it = maintenanceItems.iterator();
				while (it.hasNext()) {
					MaintenanceScheduleItemOutVO maintenanceItem = it.next();
					MaintenanceItemEvent event = new MaintenanceItemEvent(maintenanceItem);
					event.setDate(maintenanceItem.getNextRecurrence()); // Special workaround in inventoryservice!
					addEvent(event);
				}
			}
		}
		if (showCourses) {
			Collection<CourseOutVO> courses = null;
			try {
				courses = WebUtil.getServiceLocator().getCourseService().getCourseInterval(auth, departmentId, courseCategoryId, from, to);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
			if (courses != null) {
				Iterator<CourseOutVO> it = courses.iterator();
				while (it.hasNext()) {
					addEvent(new CourseEvent(it.next()));
				}
			}
		}
	}

	public Long getProbandCategoryId() {
		return probandCategoryId;
	}

	public Long getProbandId() {
		return probandId;
	}

	public String getProbandName() {
		return WebUtil.probandIdToName(probandId);
	}

	public Long getResponsiblePersonId() {
		return responsiblePersonId;
	}

	public String getResponsiblePersonName() {
		return WebUtil.staffIdToName(responsiblePersonId);
	}

	public boolean getShowNotifyOnly() {
		return notify != null;
	}

	public Long getTrialId() {
		return trialId;
	}

	public String getTrialName() {
		return WebUtil.trialIdToName(trialId);
	}

	public Long getVisitTypeId() {
		return visitTypeId;
	}

	public boolean isShowBookings() {
		return showBookings;
	}

	public boolean isShowCourses() {
		return showCourses;
	}

	public boolean isShowHolidays() {
		return showHolidays;
	}

	public boolean isShowInventoryStatus() {
		return showInventoryStatus;
	}

	public boolean isShowMaintenanceItems() {
		return showMaintenanceItems;
	}

	public boolean isShowProbandStatus() {
		return showProbandStatus;
	}

	public boolean isShowVisitSchedule() {
		return showVisitSchedule;
	}

	public void setCalendar(String calendar) {
		this.calendar = calendar;
	}

	public void setCourseCategoryId(Long courseCategoryId) {
		this.courseCategoryId = courseCategoryId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public void setIgnoreObsoleteInventoryStatus(boolean ignoreObsolete) {
		this.hideInventoryAvailability = ignoreObsolete ? false : null;
	}

	public void setIgnoreObsoleteProbandStatus(boolean ignoreObsolete) {
		this.hideProbandAvailability = ignoreObsolete ? false : null;
	}

	public void setInventoryCategoryId(Long inventoryCategoryId) {
		this.inventoryCategoryId = inventoryCategoryId;
	}

	public void setInventoryId(Long inventoryId) {
		this.inventoryId = inventoryId;
	}

	public void setInventoryStatusTypeId(Long inventoryStatusTypeId) {
		this.inventoryStatusTypeId = inventoryStatusTypeId;
	}

	public void setProbandCategoryId(Long probandCategoryId) {
		this.probandCategoryId = probandCategoryId;
	}

	public void setProbandId(Long probandId) {
		this.probandId = probandId;
	}

	public void setResponsiblePersonId(Long responsiblePersonId) {
		this.responsiblePersonId = responsiblePersonId;
	}

	public void setShowBookings(boolean showBookings) {
		this.showBookings = showBookings;
	}

	public void setShowCourses(boolean showCourses) {
		this.showCourses = showCourses;
	}

	public void setShowHolidays(boolean showHolidays) {
		this.showHolidays = showHolidays;
	}

	public void setShowInventoryStatus(boolean showInventoryStatus) {
		this.showInventoryStatus = showInventoryStatus;
	}

	public void setShowMaintenanceItems(boolean showMaintenanceItems) {
		this.showMaintenanceItems = showMaintenanceItems;
	}

	public void setShowNotifyOnly(boolean notify) {
		this.notify = notify ? true : null;
	}

	public void setShowProbandStatus(boolean showProbandStatus) {
		this.showProbandStatus = showProbandStatus;
	}

	public void setShowVisitSchedule(boolean showVisitSchedule) {
		this.showVisitSchedule = showVisitSchedule;
	}

	public void setTrialId(Long trialId) {
		this.trialId = trialId;
	}

	public void setVisitTypeId(Long visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public boolean isShowCollisions() {
		return showCollisions;
	}

	public void setShowCollisions(boolean showCollisions) {
		this.showCollisions = showCollisions;
	}
}
