package org.phoenixctms.ctsms.web.model.inventory;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.RangePeriod;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.InventoryBookingInVO;
import org.phoenixctms.ctsms.vo.InventoryBookingOutVO;
import org.phoenixctms.ctsms.vo.InventoryBookingsExcelVO;
import org.phoenixctms.ctsms.vo.RangeIntervalVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.VisitScheduleExcelVO;
import org.phoenixctms.ctsms.web.model.shared.CollidingCourseParticipationStatusEntryEagerModel;
import org.phoenixctms.ctsms.web.model.shared.CollidingDutyRosterTurnEagerModel;
import org.phoenixctms.ctsms.web.model.shared.CollidingInventoryStatusEntryEagerModel;
import org.phoenixctms.ctsms.web.model.shared.CollidingProbandStatusEntryEagerModel;
import org.phoenixctms.ctsms.web.model.shared.CollidingStaffStatusEntryEagerModel;
import org.phoenixctms.ctsms.web.model.shared.CourseEvent;
import org.phoenixctms.ctsms.web.model.shared.HolidayEvent;
import org.phoenixctms.ctsms.web.model.shared.InventoryBookingBeanBase;
import org.phoenixctms.ctsms.web.model.shared.InventoryBookingEvent;
import org.phoenixctms.ctsms.web.model.shared.ProbandStatusEvent;
import org.phoenixctms.ctsms.web.model.trial.VisitScheduleItemEvent;
import org.phoenixctms.ctsms.web.util.DateUtil;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.GetParamNames;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.event.DateSelectEvent;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.ScheduleEntrySelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.StreamedContent;

@ManagedBean
@ViewScoped
public class ScheduleInventoryBookingBean extends InventoryBookingBeanBase {

	public static void initBookingDefaultValues(InventoryBookingInVO in, StaffOutVO identity) {
		if (in != null) {
			in.setId(null);
			in.setVersion(null);
			in.setComment(Messages.getString(MessageCodes.BOOKING_COMMENT_PRESET));
			in.setCourseId(null);
			in.setInventoryId(null);
			in.setOnBehalfOfId(identity != null ? identity.getId() : null);
			in.setProbandId(null);
			in.setStart(null);
			in.setStop(null);
			in.setTrialId(null);
			in.setCalendar(Messages.getString(MessageCodes.INVENTORY_BOOKING_CALENDAR_PRESET));
		}
	}

	private ArrayList<SelectItem> departments;
	private InventoryBookingLazyScheduleModel bookingScheduleModel;
	private HashMap<Long, CollidingStaffStatusEntryEagerModel> collidingStaffStatusEntryModelCache;
	private HashMap<Long, CollidingDutyRosterTurnEagerModel> collidingDutyRosterTurnModelCache;
	private HashMap<Long, CollidingProbandStatusEntryEagerModel> collidingProbandStatusEntryModelCache;
	private HashMap<Long, CollidingCourseParticipationStatusEntryEagerModel> collidingCourseParticipationStatusEntryModelCache;
	private InventoryBookingEvent bookingEvent;
	private InventoryStatusEvent inventoryStatusEvent;
	private HolidayEvent holidayEvent;
	private ProbandStatusEvent probandStatusEvent;
	private MaintenanceItemEvent maintenanceItemEvent;
	private CourseEvent courseEvent;
	private VisitScheduleItemEvent visitScheduleItemEvent;

	public ScheduleInventoryBookingBean() {
		super();
		collidingStaffStatusEntryModelCache = new HashMap<Long, CollidingStaffStatusEntryEagerModel>();
		collidingDutyRosterTurnModelCache = new HashMap<Long, CollidingDutyRosterTurnEagerModel>();
		collidingProbandStatusEntryModelCache = new HashMap<Long, CollidingProbandStatusEntryEagerModel>();
		collidingCourseParticipationStatusEntryModelCache = new HashMap<Long, CollidingCourseParticipationStatusEntryEagerModel>();
		bookingScheduleModel = new InventoryBookingLazyScheduleModel(
				collidingInventoryStatusEntryModelCache,
				collidingStaffStatusEntryModelCache,
				collidingDutyRosterTurnModelCache,
				collidingProbandStatusEntryModelCache,
				collidingCourseParticipationStatusEntryModelCache);
	}

	private void abortScheduleEvent() {
		bookingScheduleModel.reLoadEvents();
		WebUtil.setAjaxCancelFlag(true);
		Messages.addLocalizedMessage(FacesMessage.SEVERITY_FATAL, MessageCodes.SCHEDULE_EVENT_NOT_EDITABLE);
	}

	@Override
	protected void addEvent() {
		if (bookingEvent != null) {
			bookingEvent.setOut(out);
			updateEventCollisionCounts();
			bookingScheduleModel.addEvent(bookingEvent);
		}
	}

	@Override
	protected boolean deleteEvent() {
		if (bookingEvent != null) {
			bookingEvent.setOut(out);
			updateEventCollisionCounts();
			return bookingScheduleModel.deleteEvent(bookingEvent);
		}
		return false;
	}

	public InventoryBookingLazyScheduleModel getBookingScheduleModel() {
		return bookingScheduleModel;
	}

	public CollidingCourseParticipationStatusEntryEagerModel getCollidingCourseParticipationStatusEntryModel(InventoryBookingOutVO courseBooking) {
		return CollidingCourseParticipationStatusEntryEagerModel.getCachedCollidingCourseParticipationStatusEntryModel(courseBooking,
				true, collidingCourseParticipationStatusEntryModelCache);
	}

	public CollidingDutyRosterTurnEagerModel getCollidingDutyRosterTurnModel(InventoryBookingOutVO courseBooking) {
		return CollidingDutyRosterTurnEagerModel.getCachedCollidingDutyRosterTurnModel(courseBooking, true, collidingDutyRosterTurnModelCache);
	}

	public CollidingProbandStatusEntryEagerModel getCollidingProbandStatusEntryModel(InventoryBookingOutVO probandBooking) {
		return CollidingProbandStatusEntryEagerModel.getCachedCollidingProbandStatusEntryModel(probandBooking, true, collidingProbandStatusEntryModelCache);
	}

	public CollidingStaffStatusEntryEagerModel getCollidingStaffStatusEntryModel(InventoryBookingOutVO courseBooking) {
		return CollidingStaffStatusEntryEagerModel.getCachedCollidingStaffStatusEntryModel(courseBooking, true, collidingStaffStatusEntryModelCache);
	}

	public ArrayList<SelectItem> getDepartments() {
		return departments;
	}

	@Override
	public String getTitle() {
		if (out != null) {
			return Messages.getMessage(MessageCodes.SCHEDULE_INVENTORY_BOOKING_TITLE, Long.toString(out.getId()), CommonUtil.inventoryOutVOToString(out.getInventory()),
					DateUtil.getDateTimeStartStopString(out.getStart(), out.getStop()));
		} else {
			return Messages.getMessage(MessageCodes.SCHEDULE_CREATE_NEW_INVENTORY_BOOKING, WebUtil.getCalendarWeekString(in.getStart()));
		}
	}

	public void handleDepartmentCategoryChange() {
		bookingScheduleModel.reLoadEvents();
		initSets();
	}

	@PostConstruct
	private void init() {
		initIn();
		initSets();
	}

	@Override
	protected void initIn() {
		if (in == null) {
			in = new InventoryBookingInVO();
		}
		if (out != null) {
			copyBookingOutToIn(in, out);
		} else {
			initBookingDefaultValues(in, WebUtil.getUserIdentity());
		}
	}

	@Override
	protected void initSets() {
		bookingScheduleModel.clearCaches();
		if (departments == null) {
			StaffOutVO user = WebUtil.getUserIdentity();
			departments = WebUtil.getVisibleDepartments(user == null ? null : user.getDepartment().getId());
		}
		filterCalendars = WebUtil.getInventoryBookingFilterCalendars(bookingScheduleModel.getDepartmentId(), null, null, null, null);
	}

	@Override
	public boolean isCreateable() {
		return true;
	}

	@Override
	protected void loadEvent() {
		if (bookingEvent != null ||
				inventoryStatusEvent != null ||
				holidayEvent != null ||
				visitScheduleItemEvent != null ||
				probandStatusEvent != null ||
				maintenanceItemEvent != null ||
				courseEvent != null) {
			bookingEvent = new InventoryBookingEvent(out);
			updateEventCollisionCounts();
			inventoryStatusEvent = null;
			holidayEvent = null;
			visitScheduleItemEvent = null;
			probandStatusEvent = null;
			maintenanceItemEvent = null;
			courseEvent = null;
		}
	}

	private void moveResizeEvent(AjaxBehaviorEvent ajaxEvent) {
		ScheduleEvent event = null;
		ScheduleEntryMoveEvent moveEvent = null;
		ScheduleEntryResizeEvent resizeEvent = null;
		if (ajaxEvent instanceof ScheduleEntryMoveEvent) {
			moveEvent = (ScheduleEntryMoveEvent) ajaxEvent;
			event = moveEvent.getScheduleEvent();
		} else if (ajaxEvent instanceof ScheduleEntryResizeEvent) {
			resizeEvent = (ScheduleEntryResizeEvent) ajaxEvent;
			event = resizeEvent.getScheduleEvent();
		} else {
			abortScheduleEvent();
			return;
		}
		this.out = null;
		if (event instanceof InventoryBookingEvent) {
			bookingEvent = (InventoryBookingEvent) event;
			inventoryStatusEvent = null;
			holidayEvent = null;
			visitScheduleItemEvent = null;
			probandStatusEvent = null;
			maintenanceItemEvent = null;
			courseEvent = null;
			this.in = (InventoryBookingInVO) bookingEvent.getData();
			if (moveEvent != null) {
				this.in.setStart(DateUtil.addDayMinuteDelta(in.getStart(), moveEvent.getDayDelta(), moveEvent.getMinuteDelta()));
				this.in.setStop(DateUtil.addDayMinuteDelta(in.getStop(), moveEvent.getDayDelta(), moveEvent.getMinuteDelta()));
			}
			if (resizeEvent != null) {
				this.in.setStop(DateUtil.addDayMinuteDelta(in.getStop(), resizeEvent.getDayDelta(), resizeEvent.getMinuteDelta()));
			}
			this.update();
		} else if (event instanceof InventoryStatusEvent) {
			bookingEvent = null;
			inventoryStatusEvent = (InventoryStatusEvent) event;
			holidayEvent = null;
			visitScheduleItemEvent = null;
			probandStatusEvent = null;
			maintenanceItemEvent = null;
			courseEvent = null;
			initIn();
			initSets();
			abortScheduleEvent();
		} else if (event instanceof HolidayEvent) {
			bookingEvent = null;
			inventoryStatusEvent = null;
			holidayEvent = (HolidayEvent) event;
			visitScheduleItemEvent = null;
			probandStatusEvent = null;
			maintenanceItemEvent = null;
			courseEvent = null;
			initIn();
			initSets();
			abortScheduleEvent();
		} else if (event instanceof VisitScheduleItemEvent) {
			bookingEvent = null;
			inventoryStatusEvent = null;
			holidayEvent = null;
			visitScheduleItemEvent = (VisitScheduleItemEvent) event;
			probandStatusEvent = null;
			maintenanceItemEvent = null;
			courseEvent = null;
			initIn();
			initSets();
			abortScheduleEvent();
		} else if (event instanceof ProbandStatusEvent) {
			bookingEvent = null;
			inventoryStatusEvent = null;
			holidayEvent = null;
			visitScheduleItemEvent = null;
			probandStatusEvent = (ProbandStatusEvent) event;
			maintenanceItemEvent = null;
			courseEvent = null;
			initIn();
			initSets();
			abortScheduleEvent();
		} else if (event instanceof MaintenanceItemEvent) {
			bookingEvent = null;
			inventoryStatusEvent = null;
			holidayEvent = null;
			visitScheduleItemEvent = null;
			probandStatusEvent = null;
			maintenanceItemEvent = (MaintenanceItemEvent) event;
			courseEvent = null;
			initIn();
			initSets();
			abortScheduleEvent();
		} else if (event instanceof CourseEvent) {
			bookingEvent = null;
			inventoryStatusEvent = null;
			holidayEvent = null;
			visitScheduleItemEvent = null;
			probandStatusEvent = null;
			maintenanceItemEvent = null;
			courseEvent = (CourseEvent) event;
			initIn();
			initSets();
			abortScheduleEvent();
		} else {
			bookingEvent = null;
			inventoryStatusEvent = null;
			holidayEvent = null;
			visitScheduleItemEvent = null;
			probandStatusEvent = null;
			maintenanceItemEvent = null;
			courseEvent = null;
			initIn();
			initSets();
			abortScheduleEvent();
		}
	}

	public void onDateSelect(DateSelectEvent selectEvent) {
		this.out = null;
		initIn();
		in.setStart(DateUtil.sanitizeScheduleTimestamp(false, selectEvent.getDate()));
		in.setStop(DateUtil.addDayMinuteDelta(in.getStart(), 0,
				Settings.getInt(SettingCodes.BOOKING_DURATION_MINUTES_PRESET, Bundle.SETTINGS, DefaultSettings.BOOKING_DURATION_MINUTES_PRESET)));
		bookingEvent = new InventoryBookingEvent(in);
		updateEventCollisionCounts();
		inventoryStatusEvent = null;
		holidayEvent = null;
		visitScheduleItemEvent = null;
		probandStatusEvent = null;
		maintenanceItemEvent = null;
		courseEvent = null;
		initSets();
	}

	public void onEventMove(ScheduleEntryMoveEvent moveEvent) {
		moveResizeEvent(moveEvent);
	}

	public void onEventResize(ScheduleEntryResizeEvent resizeEvent) {
		moveResizeEvent(resizeEvent);
	}

	public void onEventSelect(ScheduleEntrySelectEvent selectEvent) {
		ScheduleEvent event = selectEvent.getScheduleEvent();
		this.out = null;
		if (event instanceof InventoryBookingEvent) {
			bookingEvent = (InventoryBookingEvent) event;
			inventoryStatusEvent = null;
			holidayEvent = null;
			visitScheduleItemEvent = null;
			probandStatusEvent = null;
			maintenanceItemEvent = null;
			courseEvent = null;
			this.in = (InventoryBookingInVO) bookingEvent.getData();
			this.out = bookingEvent.getOut();
			initSets();
		} else if (event instanceof InventoryStatusEvent) {
			bookingEvent = null;
			inventoryStatusEvent = (InventoryStatusEvent) event;
			holidayEvent = null;
			visitScheduleItemEvent = null;
			probandStatusEvent = null;
			maintenanceItemEvent = null;
			courseEvent = null;
			initIn();
			initSets();
			abortScheduleEvent();
		} else if (event instanceof HolidayEvent) {
			bookingEvent = null;
			inventoryStatusEvent = null;
			holidayEvent = (HolidayEvent) event;
			visitScheduleItemEvent = null;
			probandStatusEvent = null;
			maintenanceItemEvent = null;
			courseEvent = null;
			initIn();
			initSets();
			abortScheduleEvent();
		} else if (event instanceof VisitScheduleItemEvent) {
			bookingEvent = null;
			inventoryStatusEvent = null;
			holidayEvent = null;
			visitScheduleItemEvent = (VisitScheduleItemEvent) event;
			probandStatusEvent = null;
			maintenanceItemEvent = null;
			courseEvent = null;
			initIn();
			initSets();
			abortScheduleEvent();
		} else if (event instanceof ProbandStatusEvent) {
			bookingEvent = null;
			inventoryStatusEvent = null;
			holidayEvent = null;
			visitScheduleItemEvent = null;
			probandStatusEvent = (ProbandStatusEvent) event;
			maintenanceItemEvent = null;
			courseEvent = null;
			initIn();
			initSets();
			abortScheduleEvent();
		} else if (event instanceof MaintenanceItemEvent) {
			bookingEvent = null;
			inventoryStatusEvent = null;
			holidayEvent = null;
			visitScheduleItemEvent = null;
			probandStatusEvent = null;
			maintenanceItemEvent = (MaintenanceItemEvent) event;
			courseEvent = null;
			initIn();
			initSets();
			abortScheduleEvent();
		} else if (event instanceof CourseEvent) {
			bookingEvent = null;
			inventoryStatusEvent = null;
			holidayEvent = null;
			visitScheduleItemEvent = null;
			probandStatusEvent = null;
			maintenanceItemEvent = null;
			courseEvent = (CourseEvent) event;
			initIn();
			initSets();
			abortScheduleEvent();
		} else {
			bookingEvent = null;
			inventoryStatusEvent = null;
			holidayEvent = null;
			visitScheduleItemEvent = null;
			probandStatusEvent = null;
			maintenanceItemEvent = null;
			courseEvent = null;
			initIn();
			initSets();
			abortScheduleEvent();
		}
	}

	@Override
	protected void updateEvent() {
		if (bookingEvent != null) {
			bookingEvent.setOut(out);
			updateEventCollisionCounts();
			bookingScheduleModel.updateEvent(bookingEvent);
		}
	}

	private void updateEventCollisionCounts() {
		bookingEvent.setCollidingInventoryStatusEntryCount(CollidingInventoryStatusEntryEagerModel.getCachedCollidingInventoryStatusEntryModel(out,
				true, collidingInventoryStatusEntryModelCache).getAllRowCount());
		bookingEvent.setCollidingStaffStatusEntryCount(CollidingStaffStatusEntryEagerModel.getCachedCollidingStaffStatusEntryModel(out, true, collidingStaffStatusEntryModelCache)
				.getAllRowCount());
		bookingEvent.setCollidingDutyRosterTurnCount(CollidingDutyRosterTurnEagerModel.getCachedCollidingDutyRosterTurnModel(out, true, collidingDutyRosterTurnModelCache)
				.getAllRowCount());
		bookingEvent.setCollidingProbandStatusEntryCount(CollidingProbandStatusEntryEagerModel
				.getCachedCollidingProbandStatusEntryModel(out, true, collidingProbandStatusEntryModelCache).getAllRowCount());
		bookingEvent.setCollidingCourseParticipationStatusEntryCount(CollidingCourseParticipationStatusEntryEagerModel.getCachedCollidingCourseParticipationStatusEntryModel(out,
				true, collidingCourseParticipationStatusEntryModelCache).getAllRowCount());
	}

	protected Date date;
	protected RangePeriod rangePeriod;
	protected String rangeLabel;

	public void updateDateRangePeriod() {
		try {
			rangeLabel = WebUtil.getParamValue(GetParamNames.RANGE_LABEL);
		} catch (Exception e) {
			rangeLabel = null;
		}
		try {
			rangePeriod = RangePeriod.fromString(WebUtil.getParamValue(GetParamNames.RANGE_PERIOD));
		} catch (Exception e) {
			rangePeriod = null;
		}
		try {
			date = DateUtil.sanitizeScheduleTimestamp(true, new Date(WebUtil.getLongParamValue(GetParamNames.DATE)));
		} catch (Exception e) {
			date = null;
		}
	}

	public String getDateRangePeriodDialogTitle() {
		if (date != null && rangePeriod != null) {
			try {
				RangeIntervalVO range = WebUtil.getServiceLocator().getToolsService().getRangeInterval(date, rangePeriod);
				return Messages.getMessage(MessageCodes.BOOKING_SCHEDULE_DATE_RANGE_PERIOD_DIALOG_TITLE, rangeLabel,
						DateUtil.getDateTimeStartStopString(range.getStart(), range.getStop()));
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
			}
		}
		return "";
	}

	public StreamedContent getProbandInventoryBookingsExcelStreamedContent() throws Exception {
		try {
			RangeIntervalVO range = WebUtil.getServiceLocator().getToolsService().getRangeInterval(date, rangePeriod);
			InventoryBookingsExcelVO excel = WebUtil.getServiceLocator().getInventoryService().exportInventoryBookings(
					WebUtil.getAuthentication(), bookingScheduleModel.getDepartmentId(), null, null, bookingScheduleModel.getCalendar(),
					range.getStart(), range.getStop(), true, true, null, null, null, null);
			return new DefaultStreamedContent(new ByteArrayInputStream(excel.getDocumentDatas()), excel.getContentType().getMimeType(), excel.getFileName());
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
			throw e;
		} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
			throw e;
		}
	}

	public StreamedContent getCourseInventoryBookingsExcelStreamedContent() throws Exception {
		try {
			RangeIntervalVO range = WebUtil.getServiceLocator().getToolsService().getRangeInterval(date, rangePeriod);
			InventoryBookingsExcelVO excel = WebUtil.getServiceLocator().getInventoryService().exportInventoryBookings(
					WebUtil.getAuthentication(), null, bookingScheduleModel.getDepartmentId(), null, bookingScheduleModel.getCalendar(),
					range.getStart(), range.getStop(), null, null, true, true, null, null);
			return new DefaultStreamedContent(new ByteArrayInputStream(excel.getDocumentDatas()), excel.getContentType().getMimeType(), excel.getFileName());
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
			throw e;
		} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
			throw e;
		}
	}

	public StreamedContent getTrialInventoryBookingsExcelStreamedContent() throws Exception {
		try {
			RangeIntervalVO range = WebUtil.getServiceLocator().getToolsService().getRangeInterval(date, rangePeriod);
			InventoryBookingsExcelVO excel = WebUtil.getServiceLocator().getInventoryService().exportInventoryBookings(
					WebUtil.getAuthentication(), null, null, bookingScheduleModel.getDepartmentId(), bookingScheduleModel.getCalendar(),
					range.getStart(), range.getStop(), null, null, null, null, true, true);
			return new DefaultStreamedContent(new ByteArrayInputStream(excel.getDocumentDatas()), excel.getContentType().getMimeType(), excel.getFileName());
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
			throw e;
		} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
			throw e;
		}
	}

	public StreamedContent getVisitScheduleExcelStreamedContent() throws Exception {
		try {
			RangeIntervalVO range = WebUtil.getServiceLocator().getToolsService().getRangeInterval(date, rangePeriod);
			VisitScheduleExcelVO excel = WebUtil.getServiceLocator().getTrialService().exportVisitSchedule(
					WebUtil.getAuthentication(), bookingScheduleModel.getTrialId(), null, bookingScheduleModel.getDepartmentId(), range.getStart(), range.getStop());
			return new DefaultStreamedContent(new ByteArrayInputStream(excel.getDocumentDatas()), excel.getContentType().getMimeType(), excel.getFileName());
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
			throw e;
		} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
			throw e;
		}
	}

	public boolean isEnableDepartmentFilter() {
		return Settings.getBoolean(SettingCodes.INVENTORY_BOOKING_SCHEDULE_ENABLE_DEPARTMENT_FILTER, Bundle.SETTINGS,
				DefaultSettings.INVENTORY_BOOKING_SCHEDULE_ENABLE_DEPARTMENT_FILTER);
	}

	//----no: calendar_filter
	public boolean isEnableInventoryCategoryFilter() {
		return Settings.getBoolean(SettingCodes.INVENTORY_BOOKING_SCHEDULE_ENABLE_INVENTORY_CATEGORY_FILTER, Bundle.SETTINGS,
				DefaultSettings.INVENTORY_BOOKING_SCHEDULE_ENABLE_INVENTORY_CATEGORY_FILTER);
	}

	public boolean isEnableInventoryFilter() {
		return Settings.getBoolean(SettingCodes.INVENTORY_BOOKING_SCHEDULE_ENABLE_INVENTORY_FILTER, Bundle.SETTINGS,
				DefaultSettings.INVENTORY_BOOKING_SCHEDULE_ENABLE_INVENTORY_FILTER);
	}

	public boolean isEnableProbandFilter() {
		return Settings.getBoolean(SettingCodes.INVENTORY_BOOKING_SCHEDULE_ENABLE_PROBAND_FILTER, Bundle.SETTINGS,
				DefaultSettings.INVENTORY_BOOKING_SCHEDULE_ENABLE_PROBAND_FILTER);
	}

	public boolean isEnableCourseFilter() {
		return Settings.getBoolean(SettingCodes.INVENTORY_BOOKING_SCHEDULE_ENABLE_COURSE_FILTER, Bundle.SETTINGS, DefaultSettings.INVENTORY_BOOKING_SCHEDULE_ENABLE_COURSE_FILTER);
	}

	public boolean isEnableTrialFilter() {
		return Settings.getBoolean(SettingCodes.INVENTORY_BOOKING_SCHEDULE_ENABLE_TRIAL_FILTER, Bundle.SETTINGS, DefaultSettings.INVENTORY_BOOKING_SCHEDULE_ENABLE_TRIAL_FILTER);
	}

	public boolean isEnableResponsiblePersonFilter() {
		return Settings.getBoolean(SettingCodes.INVENTORY_BOOKING_SCHEDULE_ENABLE_RESPONSIBLE_PERSON_FILTER, Bundle.SETTINGS,
				DefaultSettings.INVENTORY_BOOKING_SCHEDULE_ENABLE_RESPONSIBLE_PERSON_FILTER);
	}

	public boolean isEnableBookingsFilter() {
		return Settings.getBoolean(SettingCodes.INVENTORY_BOOKING_SCHEDULE_ENABLE_BOOKINGS_FILTER, Bundle.SETTINGS,
				DefaultSettings.INVENTORY_BOOKING_SCHEDULE_ENABLE_BOOKINGS_FILTER);
	}

	public boolean isEnableVisitScheduleFilter() {
		return Settings.getBoolean(SettingCodes.INVENTORY_BOOKING_SCHEDULE_ENABLE_VISIT_SCHEDULE_FILTER, Bundle.SETTINGS,
				DefaultSettings.INVENTORY_BOOKING_SCHEDULE_ENABLE_VISIT_SCHEDULE_FILTER);
	}

	public boolean isEnableProbandStatusFilter() {
		return Settings.getBoolean(SettingCodes.INVENTORY_BOOKING_SCHEDULE_ENABLE_PROBAND_STATUS_FILTER, Bundle.SETTINGS,
				DefaultSettings.INVENTORY_BOOKING_SCHEDULE_ENABLE_PROBAND_STATUS_FILTER);
	}

	public boolean isEnableCoursesFilter() {
		return Settings.getBoolean(SettingCodes.INVENTORY_BOOKING_SCHEDULE_ENABLE_COURSES_FILTER, Bundle.SETTINGS,
				DefaultSettings.INVENTORY_BOOKING_SCHEDULE_ENABLE_COURSES_FILTER);
	}

	public boolean isEnableInventoryStatusFilter() {
		return Settings.getBoolean(SettingCodes.INVENTORY_BOOKING_SCHEDULE_ENABLE_INVENTORY_STATUS_FILTER, Bundle.SETTINGS,
				DefaultSettings.INVENTORY_BOOKING_SCHEDULE_ENABLE_INVENTORY_STATUS_FILTER);
	}

	public boolean isEnableCollisionsFilter() {
		return Settings.getBoolean(SettingCodes.INVENTORY_BOOKING_SCHEDULE_ENABLE_COLLISIONS_FILTER, Bundle.SETTINGS,
				DefaultSettings.INVENTORY_BOOKING_SCHEDULE_ENABLE_COLLISIONS_FILTER);
	}

	public boolean isEnableMaintenanceItemsFilter() {
		return Settings.getBoolean(SettingCodes.INVENTORY_BOOKING_SCHEDULE_ENABLE_MAINTENANCE_ITEMS_FILTER, Bundle.SETTINGS,
				DefaultSettings.INVENTORY_BOOKING_SCHEDULE_ENABLE_MAINTENANCE_ITEMS_FILTER);
	}

	public boolean isEnableHolidaysFilter() {
		return Settings.getBoolean(SettingCodes.INVENTORY_BOOKING_SCHEDULE_ENABLE_HOLIDAYS_FILTER, Bundle.SETTINGS,
				DefaultSettings.INVENTORY_BOOKING_SCHEDULE_ENABLE_HOLIDAYS_FILTER);
	}
}
