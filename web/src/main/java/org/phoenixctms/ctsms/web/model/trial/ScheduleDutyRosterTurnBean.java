package org.phoenixctms.ctsms.web.model.trial;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Date;

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
import org.phoenixctms.ctsms.vo.DutyRosterTurnInVO;
import org.phoenixctms.ctsms.vo.InventoryBookingsExcelVO;
import org.phoenixctms.ctsms.vo.RangeIntervalVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.VisitScheduleExcelVO;
import org.phoenixctms.ctsms.web.model.shared.CollidingInventoryBookingEagerModel;
import org.phoenixctms.ctsms.web.model.shared.CollidingStaffStatusEntryEagerModel;
import org.phoenixctms.ctsms.web.model.shared.CourseEvent;
import org.phoenixctms.ctsms.web.model.shared.DutyRosterTurnBeanBase;
import org.phoenixctms.ctsms.web.model.shared.HolidayEvent;
import org.phoenixctms.ctsms.web.model.shared.ProbandStatusEvent;
import org.phoenixctms.ctsms.web.model.shared.StaffNaCountEvent;
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
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.StreamedContent;

@ManagedBean
@ViewScoped
public class ScheduleDutyRosterTurnBean extends DutyRosterTurnBeanBase {

	public static void initDutyRosterTurnDefaultValues(DutyRosterTurnInVO in, StaffOutVO identity) {
		if (in != null) {
			boolean selfAllocatablePreset = Settings.getBoolean(SettingCodes.DUTY_ROSTER_SELF_ALLOCATABLE_PRESET, Bundle.SETTINGS,
					DefaultSettings.DUTY_ROSTER_SELF_ALLOCATABLE_PRESET);
			in.setId(null);
			in.setVersion(null);
			in.setComment(Messages.getString(MessageCodes.DUTY_ROSTER_TURN_COMMENT_PRESET));
			in.setStaffId(null);
			in.setStart(null);
			in.setStop(null);
			in.setTrialId(null);
			in.setSelfAllocatable(selfAllocatablePreset);
			in.setTitle(Messages.getString(MessageCodes.DUTY_ROSTER_TURN_TITLE_PRESET));
			in.setCalendar(Messages.getString(MessageCodes.DUTY_ROSTER_TURN_CALENDAR_PRESET));
			in.setVisitScheduleItemId(null);
		}
	}

	private DutyRosterLazyScheduleModel dutyRosterScheduleModel;
	private ArrayList<SelectItem> staffCategories;
	private ArrayList<SelectItem> departments;
	private DutyRosterTurnEvent dutyRosterTurnEvent;
	private StaffStatusEvent statusEntryEvent;
	private HolidayEvent holidayEvent;
	private StaffNaCountEvent staffNaCountEvent;
	private VisitScheduleItemEvent visitScheduleItemEvent;
	private CourseInventoryBookingEvent courseInventoryBookingEvent;
	private CourseEvent courseEvent;
	private ProbandStatusEvent probandStatusEvent;
	private TimelineEvent timelineEvent;

	public ScheduleDutyRosterTurnBean() {
		super();
		dutyRosterScheduleModel = new DutyRosterLazyScheduleModel(collidingStaffStatusEntryModelCache, collidingInventoryBookingModelCache);
	}

	private void abortScheduleEvent() {
		dutyRosterScheduleModel.reLoadEvents();
		WebUtil.setAjaxCancelFlag(true);
		Messages.addLocalizedMessage(FacesMessage.SEVERITY_FATAL, MessageCodes.SCHEDULE_EVENT_NOT_EDITABLE);
	}

	@Override
	protected void addEvent() {
		if (dutyRosterTurnEvent != null) {
			dutyRosterTurnEvent.setOut(out);
			updateEventCollisionCounts();
			dutyRosterScheduleModel.addEvent(dutyRosterTurnEvent);
		}
	}

	@Override
	protected boolean deleteEvent() {
		if (dutyRosterTurnEvent != null) {
			dutyRosterTurnEvent.setOut(out);
			updateEventCollisionCounts();
			return dutyRosterScheduleModel.deleteEvent(dutyRosterTurnEvent);
		}
		return false;
	}

	public ArrayList<SelectItem> getDepartments() {
		return departments;
	}

	public LazyScheduleModel getDutyRosterScheduleModel() {
		return dutyRosterScheduleModel;
	}

	public Boolean getDutySelfAllocationLocked() {
		if (isCreated()) {
			return WebUtil.isDutySelfAllocationLocked(out.getTrial(), out.getStart(), true);
		} else {
			return null;
		}
	}

	public ArrayList<SelectItem> getStaffCategories() {
		return staffCategories;
	}

	@Override
	public String getTitle() {
		if (out != null) {
			return Messages.getMessage(MessageCodes.SCHEDULE_DUTY_ROSTER_TURN_TITLE, Long.toString(out.getId()), getTitle(out),
					DateUtil.getDateTimeStartStopString(out.getStart(), out.getStop()));
		} else {
			return Messages.getMessage(MessageCodes.SCHEDULE_CREATE_NEW_DUTY_ROSTER_TURN, WebUtil.getCalendarWeekString(in.getStart()));
		}
	}

	@Override
	public ArrayList<SelectItem> getVisitScheduleItems() {
		return getVisitScheduleItems(dutyRosterScheduleModel.getDepartmentId()); // must not be cached!
	}

	public void handleDepartmentCategoryTrialChange() {
		dutyRosterScheduleModel.reLoadEvents();
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
			in = new DutyRosterTurnInVO();
		}
		if (out != null) {
			copyDutyRosterTurnOutToIn(in, out);
		} else {
			initDutyRosterTurnDefaultValues(in, WebUtil.getUserIdentity());
		}
	}

	@Override
	protected void initSets() {
		dutyRosterScheduleModel.clearCaches();
		if (staffCategories == null) {
			staffCategories = WebUtil.getVisibleStaffCategories(true, null, null);
		}
		if (departments == null) {
			StaffOutVO user = WebUtil.getUserIdentity();
			departments = WebUtil.getVisibleDepartments(user == null ? null : user.getDepartment().getId());
		}
		filterCalendars = WebUtil.getDutyRosterTurnFilterCalendars(dutyRosterScheduleModel.getDepartmentId(), null, null);
	}

	@Override
	public boolean isCreateable() {
		return true;
	}

	public boolean isSelfAllocatable() {
		return isCreated() && out.getStaff() == null && out.getSelfAllocatable() && !WebUtil.isDutySelfAllocationLocked(out);
	}

	public boolean isSelfAllocationReleasable() {
		return isCreated() && out.getSelfAllocatable() && WebUtil.isUserIdentityIdLoggedIn(out.getStaff() == null ? null : out.getStaff().getId())
				&& !WebUtil.isDutySelfAllocationLocked(out);
	}

	@Override
	protected void loadEvent() {
		if (dutyRosterTurnEvent != null ||
				statusEntryEvent != null ||
				holidayEvent != null ||
				staffNaCountEvent != null ||
				visitScheduleItemEvent != null ||
				courseInventoryBookingEvent != null ||
				courseEvent != null ||
				probandStatusEvent != null ||
				timelineEvent != null) {
			dutyRosterTurnEvent = new DutyRosterTurnEvent(out);
			updateEventCollisionCounts();
			statusEntryEvent = null;
			holidayEvent = null;
			staffNaCountEvent = null;
			visitScheduleItemEvent = null;
			courseInventoryBookingEvent = null;
			courseEvent = null;
			probandStatusEvent = null;
			timelineEvent = null;
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
		if (event instanceof DutyRosterTurnEvent) {
			dutyRosterTurnEvent = (DutyRosterTurnEvent) event;
			statusEntryEvent = null;
			holidayEvent = null;
			staffNaCountEvent = null;
			visitScheduleItemEvent = null;
			courseInventoryBookingEvent = null;
			courseEvent = null;
			probandStatusEvent = null;
			timelineEvent = null;
			this.in = (DutyRosterTurnInVO) dutyRosterTurnEvent.getData();
			if (moveEvent != null) {
				this.in.setStart(DateUtil.addDayMinuteDelta(in.getStart(), moveEvent.getDayDelta(), moveEvent.getMinuteDelta()));
				this.in.setStop(DateUtil.addDayMinuteDelta(in.getStop(), moveEvent.getDayDelta(), moveEvent.getMinuteDelta()));
			}
			if (resizeEvent != null) {
				this.in.setStop(DateUtil.addDayMinuteDelta(in.getStop(), resizeEvent.getDayDelta(), resizeEvent.getMinuteDelta()));
			}
			this.update();
		} else if (event instanceof StaffStatusEvent) {
			dutyRosterTurnEvent = null;
			statusEntryEvent = (StaffStatusEvent) event;
			holidayEvent = null;
			staffNaCountEvent = null;
			visitScheduleItemEvent = null;
			courseInventoryBookingEvent = null;
			courseEvent = null;
			probandStatusEvent = null;
			timelineEvent = null;
			initIn();
			initSets();
			abortScheduleEvent();
		} else if (event instanceof HolidayEvent) {
			dutyRosterTurnEvent = null;
			statusEntryEvent = null;
			holidayEvent = (HolidayEvent) event;
			staffNaCountEvent = null;
			visitScheduleItemEvent = null;
			courseInventoryBookingEvent = null;
			courseEvent = null;
			probandStatusEvent = null;
			timelineEvent = null;
			initIn();
			initSets();
			abortScheduleEvent();
		} else if (event instanceof StaffNaCountEvent) {
			dutyRosterTurnEvent = null;
			statusEntryEvent = null;
			holidayEvent = null;
			staffNaCountEvent = (StaffNaCountEvent) event;
			visitScheduleItemEvent = null;
			courseInventoryBookingEvent = null;
			courseEvent = null;
			probandStatusEvent = null;
			timelineEvent = null;
			initIn();
			initSets();
			abortScheduleEvent();
		} else if (event instanceof VisitScheduleItemEvent) {
			dutyRosterTurnEvent = null;
			statusEntryEvent = null;
			holidayEvent = null;
			staffNaCountEvent = null;
			visitScheduleItemEvent = (VisitScheduleItemEvent) event;
			courseInventoryBookingEvent = null;
			courseEvent = null;
			probandStatusEvent = null;
			timelineEvent = null;
			initIn();
			initSets();
			abortScheduleEvent();
		} else if (event instanceof CourseInventoryBookingEvent) {
			dutyRosterTurnEvent = null;
			statusEntryEvent = null;
			holidayEvent = null;
			staffNaCountEvent = null;
			visitScheduleItemEvent = null;
			courseInventoryBookingEvent = (CourseInventoryBookingEvent) event;
			courseEvent = null;
			probandStatusEvent = null;
			timelineEvent = null;
			initIn();
			initSets();
			abortScheduleEvent();
		} else if (event instanceof CourseEvent) {
			dutyRosterTurnEvent = null;
			statusEntryEvent = null;
			holidayEvent = null;
			staffNaCountEvent = null;
			visitScheduleItemEvent = null;
			courseInventoryBookingEvent = null;
			courseEvent = (CourseEvent) event;
			probandStatusEvent = null;
			timelineEvent = null;
			initIn();
			initSets();
			abortScheduleEvent();
		} else if (event instanceof ProbandStatusEvent) {
			dutyRosterTurnEvent = null;
			statusEntryEvent = null;
			holidayEvent = null;
			staffNaCountEvent = null;
			visitScheduleItemEvent = null;
			courseInventoryBookingEvent = null;
			courseEvent = null;
			probandStatusEvent = (ProbandStatusEvent) event;
			timelineEvent = null;
			initIn();
			initSets();
			abortScheduleEvent();
		} else if (event instanceof TimelineEvent) {
			dutyRosterTurnEvent = null;
			statusEntryEvent = null;
			holidayEvent = null;
			staffNaCountEvent = null;
			visitScheduleItemEvent = null;
			courseInventoryBookingEvent = null;
			courseEvent = null;
			probandStatusEvent = null;
			timelineEvent = (TimelineEvent) event;
			initIn();
			initSets();
			abortScheduleEvent();
		} else {
			dutyRosterTurnEvent = null;
			statusEntryEvent = null;
			holidayEvent = null;
			staffNaCountEvent = null;
			visitScheduleItemEvent = null;
			courseInventoryBookingEvent = null;
			courseEvent = null;
			probandStatusEvent = null;
			timelineEvent = null;
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
				Settings.getInt(SettingCodes.DUTY_ROSTER_TURN_DURATION_MINUTES_PRESET, Bundle.SETTINGS, DefaultSettings.DUTY_ROSTER_TURN_DURATION_MINUTES_PRESET)));
		dutyRosterTurnEvent = new DutyRosterTurnEvent(in);
		updateEventCollisionCounts();
		statusEntryEvent = null;
		holidayEvent = null;
		staffNaCountEvent = null;
		visitScheduleItemEvent = null;
		courseInventoryBookingEvent = null;
		courseEvent = null;
		probandStatusEvent = null;
		timelineEvent = null;
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
		if (event instanceof DutyRosterTurnEvent) {
			dutyRosterTurnEvent = (DutyRosterTurnEvent) event;
			statusEntryEvent = null;
			holidayEvent = null;
			staffNaCountEvent = null;
			visitScheduleItemEvent = null;
			courseInventoryBookingEvent = null;
			courseEvent = null;
			probandStatusEvent = null;
			timelineEvent = null;
			this.in = (DutyRosterTurnInVO) dutyRosterTurnEvent.getData();
			this.out = dutyRosterTurnEvent.getOut();
			initSets();
		} else if (event instanceof StaffStatusEvent) {
			dutyRosterTurnEvent = null;
			statusEntryEvent = (StaffStatusEvent) event;
			holidayEvent = null;
			staffNaCountEvent = null;
			visitScheduleItemEvent = null;
			courseInventoryBookingEvent = null;
			courseEvent = null;
			probandStatusEvent = null;
			timelineEvent = null;
			initIn();
			initSets();
			abortScheduleEvent();
		} else if (event instanceof HolidayEvent) {
			dutyRosterTurnEvent = null;
			statusEntryEvent = null;
			holidayEvent = (HolidayEvent) event;
			staffNaCountEvent = null;
			visitScheduleItemEvent = null;
			courseInventoryBookingEvent = null;
			courseEvent = null;
			probandStatusEvent = null;
			timelineEvent = null;
			initIn();
			initSets();
			abortScheduleEvent();
		} else if (event instanceof StaffNaCountEvent) {
			dutyRosterTurnEvent = null;
			statusEntryEvent = null;
			holidayEvent = null;
			staffNaCountEvent = (StaffNaCountEvent) event;
			visitScheduleItemEvent = null;
			courseInventoryBookingEvent = null;
			courseEvent = null;
			probandStatusEvent = null;
			timelineEvent = null;
			initIn();
			initSets();
			abortScheduleEvent();
		} else if (event instanceof VisitScheduleItemEvent) {
			dutyRosterTurnEvent = null;
			statusEntryEvent = null;
			holidayEvent = null;
			staffNaCountEvent = null;
			visitScheduleItemEvent = (VisitScheduleItemEvent) event;
			courseInventoryBookingEvent = null;
			courseEvent = null;
			probandStatusEvent = null;
			timelineEvent = null;
			initIn();
			initSets();
			abortScheduleEvent();
		} else if (event instanceof CourseInventoryBookingEvent) {
			dutyRosterTurnEvent = null;
			statusEntryEvent = null;
			holidayEvent = null;
			staffNaCountEvent = null;
			visitScheduleItemEvent = null;
			courseInventoryBookingEvent = (CourseInventoryBookingEvent) event;
			courseEvent = null;
			probandStatusEvent = null;
			timelineEvent = null;
			initIn();
			initSets();
			abortScheduleEvent();
		} else if (event instanceof CourseEvent) {
			dutyRosterTurnEvent = null;
			statusEntryEvent = null;
			holidayEvent = null;
			staffNaCountEvent = null;
			visitScheduleItemEvent = null;
			courseInventoryBookingEvent = null;
			courseEvent = (CourseEvent) event;
			probandStatusEvent = null;
			timelineEvent = null;
			initIn();
			initSets();
			abortScheduleEvent();
		} else if (event instanceof ProbandStatusEvent) {
			dutyRosterTurnEvent = null;
			statusEntryEvent = null;
			holidayEvent = null;
			staffNaCountEvent = null;
			visitScheduleItemEvent = null;
			courseInventoryBookingEvent = null;
			courseEvent = null;
			probandStatusEvent = (ProbandStatusEvent) event;
			timelineEvent = null;
			initIn();
			initSets();
			abortScheduleEvent();
		} else if (event instanceof TimelineEvent) {
			dutyRosterTurnEvent = null;
			statusEntryEvent = null;
			holidayEvent = null;
			staffNaCountEvent = null;
			visitScheduleItemEvent = null;
			courseInventoryBookingEvent = null;
			courseEvent = null;
			probandStatusEvent = null;
			timelineEvent = (TimelineEvent) event;
			initIn();
			initSets();
			abortScheduleEvent();
		} else {
			dutyRosterTurnEvent = null;
			statusEntryEvent = null;
			holidayEvent = null;
			staffNaCountEvent = null;
			visitScheduleItemEvent = null;
			courseInventoryBookingEvent = null;
			courseEvent = null;
			probandStatusEvent = null;
			timelineEvent = null;
			initIn();
			initSets();
			abortScheduleEvent();
		}
	}

	public void releaseSelfAllocation() {
		selfAllocate(false);
	}

	public void selfAllocate() {
		selfAllocate(true);
	}

	private void selfAllocate(boolean allocate) {
		try {
			out = WebUtil.getServiceLocator().getStaffService().selfAllocateDutyRosterTurn(WebUtil.getAuthentication(), in.getId(), in.getVersion(), allocate);
			initIn();
			initSets();
			updateEvent();
			addOperationSuccessMessage("dutyRosterScheduleSelfAllocationMessages", MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
		} catch (ServiceException e) {
			out = WebUtil.getDutyRosterTurn(in.getId());
			initIn();
			initSets();
			updateEvent();
			Messages.addMessageClientId("dutyRosterScheduleSelfAllocationMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessageClientId("dutyRosterScheduleSelfAllocationMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
			Messages.addMessageClientId("dutyRosterScheduleSelfAllocationMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (IllegalArgumentException e) {
			Messages.addMessageClientId("dutyRosterScheduleSelfAllocationMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}

	@Override
	protected void updateEvent() {
		if (dutyRosterTurnEvent != null) {
			dutyRosterTurnEvent.setOut(out);
			updateEventCollisionCounts();
			dutyRosterScheduleModel.updateEvent(dutyRosterTurnEvent);
		}
	}

	private void updateEventCollisionCounts() {
		dutyRosterTurnEvent.setCollidingStaffStatusEntryCount(CollidingStaffStatusEntryEagerModel.getCachedCollidingStaffStatusEntryModel(out, true,
				collidingStaffStatusEntryModelCache)
				.getAllRowCount());
		dutyRosterTurnEvent.setCollidingInventoryBookingCount(CollidingInventoryBookingEagerModel.getCachedCollidingInventoryBookingModel(out, true,
				collidingInventoryBookingModelCache)
				.getAllRowCount());
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
				return Messages.getMessage(MessageCodes.DUTY_ROSTER_SCHEDULE_DATE_RANGE_PERIOD_DIALOG_TITLE, rangeLabel,
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
					WebUtil.getAuthentication(), dutyRosterScheduleModel.getDepartmentId(), null, null, null,
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
					WebUtil.getAuthentication(), null, dutyRosterScheduleModel.getDepartmentId(), null, null,
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
					WebUtil.getAuthentication(), null, null, dutyRosterScheduleModel.getDepartmentId(), null,
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
			VisitScheduleExcelVO excel = WebUtil.getServiceLocator().getTrialService().exportVisitAppointmentSchedule(
					WebUtil.getAuthentication(), dutyRosterScheduleModel.getTrialId(),
					null,
					dutyRosterScheduleModel.getDepartmentId(), range.getStart(), range.getStop());
			return new DefaultStreamedContent(new ByteArrayInputStream(excel.getDocumentDatas()), excel.getContentType().getMimeType(), excel.getFileName());
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
			throw e;
		} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
			throw e;
		}
	}

	public boolean isEnableDepartmentFilter() {
		return Settings.getBoolean(SettingCodes.DUTY_ROSTER_SCHEDULE_ENABLE_DEPARTMENT_FILTER, Bundle.SETTINGS, DefaultSettings.DUTY_ROSTER_SCHEDULE_ENABLE_DEPARTMENT_FILTER);
	}

	//----no: calendar_filter
	public boolean isEnableTrialStatusTypeFilter() {
		return Settings.getBoolean(SettingCodes.DUTY_ROSTER_SCHEDULE_ENABLE_TRIAL_STATUS_TYPE_FILTER, Bundle.SETTINGS,
				DefaultSettings.DUTY_ROSTER_SCHEDULE_ENABLE_TRIAL_STATUS_TYPE_FILTER);
	}

	//----no: trial_filter
	public boolean isEnableTrialFilter() {
		return Settings.getBoolean(SettingCodes.DUTY_ROSTER_SCHEDULE_ENABLE_TRIAL_FILTER, Bundle.SETTINGS,
				DefaultSettings.DUTY_ROSTER_SCHEDULE_ENABLE_TRIAL_FILTER);
	}

	public boolean isEnableStaffCategoryFilter() {
		return Settings.getBoolean(SettingCodes.DUTY_ROSTER_SCHEDULE_ENABLE_STAFF_CATEGORY_FILTER, Bundle.SETTINGS,
				DefaultSettings.DUTY_ROSTER_SCHEDULE_ENABLE_STAFF_CATEGORY_FILTER);
	}

	//----no: teammember_filter
	public boolean isEnableTeamMemberFilter() {
		return Settings.getBoolean(SettingCodes.DUTY_ROSTER_SCHEDULE_ENABLE_TEAM_MEMBER_FILTER, Bundle.SETTINGS,
				DefaultSettings.DUTY_ROSTER_SCHEDULE_ENABLE_TEAM_MEMBER_FILTER);
	}

	public boolean isEnableVisitScheduleFilter() {
		return Settings.getBoolean(SettingCodes.DUTY_ROSTER_SCHEDULE_ENABLE_VISIT_SCHEDULE_FILTER, Bundle.SETTINGS,
				DefaultSettings.DUTY_ROSTER_SCHEDULE_ENABLE_VISIT_SCHEDULE_FILTER);
	}

	public boolean isEnableVisitTypeFilter() {
		return Settings.getBoolean(SettingCodes.DUTY_ROSTER_SCHEDULE_ENABLE_VISIT_TYPE_FILTER, Bundle.SETTINGS, DefaultSettings.DUTY_ROSTER_SCHEDULE_ENABLE_VISIT_TYPE_FILTER);
	}

	public boolean isEnableDutyRosterFilter() {
		return Settings.getBoolean(SettingCodes.DUTY_ROSTER_SCHEDULE_ENABLE_DUTY_ROSTER_FILTER, Bundle.SETTINGS, DefaultSettings.DUTY_ROSTER_SCHEDULE_ENABLE_DUTY_ROSTER_FILTER);
	}

	public boolean isEnableTrialBookingsFilter() {
		return Settings.getBoolean(SettingCodes.DUTY_ROSTER_SCHEDULE_ENABLE_TRIAL_BOOKINGS_FILTER, Bundle.SETTINGS,
				DefaultSettings.DUTY_ROSTER_SCHEDULE_ENABLE_TRIAL_BOOKINGS_FILTER);
	}

	public boolean isEnableStaffStatusFilter() {
		return Settings.getBoolean(SettingCodes.DUTY_ROSTER_SCHEDULE_ENABLE_STAFF_STATUS_FILTER, Bundle.SETTINGS, DefaultSettings.DUTY_ROSTER_SCHEDULE_ENABLE_STAFF_STATUS_FILTER);
	}

	public boolean isEnableCollisionsFilter() {
		return Settings.getBoolean(SettingCodes.DUTY_ROSTER_SCHEDULE_ENABLE_COLLISIONS_FILTER, Bundle.SETTINGS, DefaultSettings.DUTY_ROSTER_SCHEDULE_ENABLE_COLLISIONS_FILTER);
	}

	public boolean isEnableProbandStatusFilter() {
		return Settings.getBoolean(SettingCodes.DUTY_ROSTER_SCHEDULE_ENABLE_PROBAND_STATUS_FILTER, Bundle.SETTINGS,
				DefaultSettings.DUTY_ROSTER_SCHEDULE_ENABLE_PROBAND_STATUS_FILTER);
	}

	public boolean isEnableCoursesFilter() {
		return Settings.getBoolean(SettingCodes.DUTY_ROSTER_SCHEDULE_ENABLE_COURSES_FILTER, Bundle.SETTINGS, DefaultSettings.DUTY_ROSTER_SCHEDULE_ENABLE_COURSES_FILTER);
	}

	public boolean isEnableCourseBookingsFilter() {
		return Settings.getBoolean(SettingCodes.DUTY_ROSTER_SCHEDULE_ENABLE_COURSE_BOOKINGS_FILTER, Bundle.SETTINGS,
				DefaultSettings.DUTY_ROSTER_SCHEDULE_ENABLE_COURSE_BOOKINGS_FILTER);
	}

	public boolean isEnableTimelineEventsFilter() {
		return Settings.getBoolean(SettingCodes.DUTY_ROSTER_SCHEDULE_ENABLE_TIMELINE_EVENTS_FILTER, Bundle.SETTINGS,
				DefaultSettings.DUTY_ROSTER_SCHEDULE_ENABLE_TIMELINE_EVENTS_FILTER);
	}

	public boolean isEnableHolidayFilter() {
		return Settings.getBoolean(SettingCodes.DUTY_ROSTER_SCHEDULE_ENABLE_HOLIDAY_FILTER, Bundle.SETTINGS, DefaultSettings.DUTY_ROSTER_SCHEDULE_ENABLE_HOLIDAY_FILTER);
	}
}
