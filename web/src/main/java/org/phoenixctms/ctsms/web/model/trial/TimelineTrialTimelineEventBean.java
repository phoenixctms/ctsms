package org.phoenixctms.ctsms.web.model.trial;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.js.JsUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.TimelineEventOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.vo.VisitScheduleAppointmentVO;
import org.phoenixctms.ctsms.vo.VisitScheduleItemOutVO;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.util.DateUtil;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.JSValues;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.context.RequestContext;
import org.primefaces.extensions.event.timeline.TimelineModificationEvent;
import org.primefaces.extensions.model.timeline.TimelineModel;

@ManagedBean
@ViewScoped
public class TimelineTrialTimelineEventBean extends TimelineEventBeanBase {

	private static org.primefaces.extensions.model.timeline.TimelineEvent createTimelineEventFromOut(TimelineEventOutVO timelineEvent, boolean group) {
		org.primefaces.extensions.model.timeline.TimelineEvent event = new org.primefaces.extensions.model.timeline.TimelineEvent();
		event.setData(new IDVO(timelineEvent));
		event.setStartDate(DateUtil.sanitizeTimelineTimestamp(true, timelineEvent.getStart()));
		event.setEndDate(DateUtil.sanitizeTimelineTimestamp(true, timelineEvent.getStop()));
		if (group) {
			setGroup(event, timelineEvent.getTrial());
		}
		event.setEditable(true);
		return event;
	}

	private static org.primefaces.extensions.model.timeline.TimelineEvent createTimelineEventFromOut(VisitScheduleItemOutVO visitScheduleItem, boolean group) {
		org.primefaces.extensions.model.timeline.TimelineEvent event = new org.primefaces.extensions.model.timeline.TimelineEvent();
		event.setData(new IDVO(visitScheduleItem));
		event.setStartDate(DateUtil.sanitizeTimelineTimestamp(true, visitScheduleItem.getStart()));
		event.setEndDate(DateUtil.sanitizeTimelineTimestamp(true, visitScheduleItem.getStop()));
		if (group) {
			setGroup(event, visitScheduleItem.getTrial());
		}
		event.setEditable(false);
		return event;
	}

	private static org.primefaces.extensions.model.timeline.TimelineEvent createTimelineEventFromOut(VisitScheduleAppointmentVO visitScheduleAppointment, boolean group) {
		org.primefaces.extensions.model.timeline.TimelineEvent event = new org.primefaces.extensions.model.timeline.TimelineEvent();
		event.setData(new IDVO(visitScheduleAppointment));
		event.setStartDate(DateUtil.sanitizeTimelineTimestamp(true, visitScheduleAppointment.getStart()));
		event.setEndDate(DateUtil.sanitizeTimelineTimestamp(true, visitScheduleAppointment.getStop()));
		if (group) {
			setGroup(event, visitScheduleAppointment.getTrial());
		}
		event.setEditable(false);
		return event;
	}

	private static void setGroup(org.primefaces.extensions.model.timeline.TimelineEvent event, TrialOutVO trial) {
		event.setGroup(Messages.getMessage(MessageCodes.TIMELINE_TRIAL_GROUP_TITLE,
				trial.getStatus().getId(),
				Long.toString(trial.getId()),
				"",
				trial.getStatus().getNodeStyleClass(),
				WebUtil.quoteJSString(CommonUtil.trialOutVOToString(trial), false)));
	}

	ArrayList<SelectItem> departments;
	private TimelineModel timelines;
	private Long filterDepartmentId;
	private Long filterStatusId;
	private Long filterTypeId;
	private Long filterTrialId;
	private Long filterVisitTypeId;
	private boolean showTimelineEvents;
	private boolean showVisitScheduleItems;
	private boolean showAll;
	private boolean showDescription;
	private boolean showStartStop;
	private Date rangeStart;
	private Date rangeEnd;
	private Date now;

	public TimelineTrialTimelineEventBean() {
		super();
		timelines = new TimelineModel();
		showAll = Settings.getBoolean(SettingCodes.TIMELINE_SHOW_ALL_PRESET, Bundle.SETTINGS, DefaultSettings.TIMELINE_SHOW_ALL_PRESET);
		showDescription = Settings.getBoolean(SettingCodes.TIMELINE_SHOW_DESCRIPTION_PRESET, Bundle.SETTINGS, DefaultSettings.TIMELINE_SHOW_DESCRIPTION_PRESET);
		showStartStop = Settings.getBoolean(SettingCodes.TIMELINE_SHOW_START_STOP_PRESET, Bundle.SETTINGS, DefaultSettings.TIMELINE_SHOW_START_STOP_PRESET);
		now = new Date();
		rangeStart = WebUtil.subIntervals(now,
				Settings.getVariablePeriod(SettingCodes.TIMELINE_RANGE_START_PERIOD_BEFORE, Bundle.SETTINGS, DefaultSettings.TIMELINE_RANGE_START_PERIOD_BEFORE),
				Settings.getLongNullable(SettingCodes.TIMELINE_RANGE_START_PERIOD_BEFORE_DAYS, Bundle.SETTINGS, DefaultSettings.TIMELINE_RANGE_START_PERIOD_BEFORE_DAYS), 1);
		rangeEnd = WebUtil.addIntervals(now,
				Settings.getVariablePeriod(SettingCodes.TIMELINE_RANGE_START_PERIOD_AFTER, Bundle.SETTINGS, DefaultSettings.TIMELINE_RANGE_START_PERIOD_AFTER),
				Settings.getLongNullable(SettingCodes.TIMELINE_RANGE_START_PERIOD_AFTER_DAYS, Bundle.SETTINGS, DefaultSettings.TIMELINE_RANGE_START_PERIOD_AFTER_DAYS), 1);
		showTimelineEvents = Settings.getBoolean(SettingCodes.TIMELINE_SHOW_TIMELINE_EVENTS_PRESET, Bundle.SETTINGS, DefaultSettings.TIMELINE_SHOW_TIMELINE_EVENTS_PRESET);
		showVisitScheduleItems = Settings.getBoolean(SettingCodes.TIMELINE_SHOW_VISIT_SCHEDULE_ITEMS_PRESET, Bundle.SETTINGS,
				DefaultSettings.TIMELINE_SHOW_VISIT_SCHEDULE_ITEMS_PRESET);
		StaffOutVO identity = WebUtil.getUserIdentity();
		if (identity != null) {
			if (Settings.getBoolean(SettingCodes.TIMELINE_ACTIVE_IDENTITY_DEPARTMENT_PRESET, Bundle.SETTINGS, DefaultSettings.TIMELINE_ACTIVE_IDENTITY_DEPARTMENT_PRESET)) {
				setDepartmentId(identity.getDepartment().getId());
			}
		}
	}

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
		RequestContext requestContext = RequestContext.getCurrentInstance();
		if (requestContext != null) {
			requestContext.addCallbackParam(JSValues.AJAX_DIALOG_TITLE_BASE64.toString(), JsUtil.encodeBase64(getTitle(), false));
			requestContext.addCallbackParam(JSValues.AJAX_OPERATION_SUCCESS.toString(), operationSuccess);
		}
	}

	public ArrayList<SelectItem> getAvailableTypes() {
		return getEventTypes();
	}

	public Long getDepartmentId() {
		return filterDepartmentId;
	}

	public ArrayList<SelectItem> getDepartments() {
		return departments;
	}

	public String getFilterTrialName() {
		return WebUtil.trialIdToName(filterTrialId);
	}

	public Long getFilterVisitTypeId() {
		return filterVisitTypeId;
	}

	public Date getRangeEnd() {
		return rangeEnd;
	}

	public Date getRangeStart() {
		return rangeStart;
	}

	public Long getStatusId() {
		return filterStatusId;
	}

	public TimelineModel getTimelineModel() {
		return timelines;
	}

	@Override
	public String getTitle() {
		if (out != null) {
			return Messages.getMessage(MessageCodes.TIMELINE_TIMELINE_EVENT_TITLE, CommonUtil.trialOutVOToString(trial), Long.toString(out.getId()), out.getTitle());
		} else {
			return Messages.getMessage(MessageCodes.TIMELINE_CREATE_NEW_TIMELINE_EVENT, CommonUtil.trialOutVOToString(trial));
		}
	}

	public Long getTrialId() {
		return filterTrialId;
	}

	public String getTrialName() {
		return WebUtil.trialOutVOToString(WebUtil.getTrial(this.in.getTrialId()));
	}

	public Long getTypeId() {
		return filterTypeId;
	}

	public void handleAddTrialTimelineEvent() {
		if (filterTrialId != null) {
			FacesContext context = FacesContext.getCurrentInstance();
			Map map = context.getExternalContext().getRequestParameterMap();
			String ajaxTimelineEventStart = (String) map.get(JSValues.AJAX_TIMELINE_EVENT_START.toString());
			String ajaxTimelineEventEnd = (String) map.get(JSValues.AJAX_TIMELINE_EVENT_END.toString());
			this.out = null;
			this.trialId = filterTrialId;
			initIn();
			try {
				this.in.setStart(DateUtil.sanitizeTimelineTimestamp(false, DateUtil.parseJsUTCDate(ajaxTimelineEventStart)));
			} catch (ParseException e) {
				this.in.setStart(null);
			}
			try {
				this.in.setStop(DateUtil.sanitizeTimelineTimestamp(false, DateUtil.parseJsUTCDate(ajaxTimelineEventEnd)));
			} catch (ParseException e) {
				this.in.setStop(null);
			}
			initSets();
			appendRequestContextCallbackArgs(true);
		} else {
			WebUtil.setAjaxCancelFlag(true);
		}
	}

	public void handleDeleteTrialTimelineEvent() {
		FacesContext context = FacesContext.getCurrentInstance();
		Map map = context.getExternalContext().getRequestParameterMap();
		String ajaxTimelineEventIndex = (String) map.get(JSValues.AJAX_TIMELINE_EVENT_INDEX.toString());
		try {
			setSelectedTimelineEvent((IDVO) timelines.getEvent(ajaxTimelineEventIndex).getData());
		} catch (Exception e) {
			return;
		}
		in.setShow(false);
		this.update();
	}

	public void handleDepartmentTypeStatusTrialChange() {
		updateTimelineModel();
	}

	public void handleUpdateTrialTimelineRange() {
		FacesContext context = FacesContext.getCurrentInstance();
		Map map = context.getExternalContext().getRequestParameterMap();
		String ajaxTimelineRangeStart = (String) map.get(JSValues.AJAX_TIMELINE_RANGE_START.toString());
		String ajaxTimelineRangeEnd = (String) map.get(JSValues.AJAX_TIMELINE_RANGE_END.toString());
		try {
			rangeStart = DateUtil.sanitizeTimelineTimestamp(false, DateUtil.parseJsUTCDate(ajaxTimelineRangeStart));
		} catch (ParseException e) {
			rangeStart = null;
		}
		try {
			rangeEnd = DateUtil.sanitizeTimelineTimestamp(false, DateUtil.parseJsUTCDate(ajaxTimelineRangeEnd));
		} catch (ParseException e) {
			rangeEnd = null;
		}
	}

	@PostConstruct
	private void init() {
		initIn();
		initSets();
	}

	@Override
	protected void initSpecificSets() {
		if (departments == null) {
			StaffOutVO user = WebUtil.getUserIdentity();
			departments = WebUtil.getVisibleDepartments(user == null ? null : user.getDepartment().getId());
		}
		updateTimelineModel();
	}

	public boolean isShowAll() {
		return showAll;
	}

	public boolean isShowDescription() {
		return showDescription;
	}

	public boolean isShowStartStop() {
		return showStartStop;
	}

	public boolean isShowTimelineEvents() {
		return showTimelineEvents;
	}

	public boolean isShowVisitScheduleItems() {
		return showVisitScheduleItems;
	}

	public void onChange(TimelineModificationEvent e) {
		// get clone of the TimelineEvent to be changed with new start / end dates
		org.primefaces.extensions.model.timeline.TimelineEvent trialTimelineEvent = e.getTimelineEvent();
		// update booking in DB..
		setSelectedTimelineEvent((IDVO) trialTimelineEvent.getData());
		this.in.setStart(DateUtil.sanitizeTimelineTimestamp(false, trialTimelineEvent.getStartDate()));
		this.in.setStop(DateUtil.sanitizeTimelineTimestamp(false, trialTimelineEvent.getEndDate()));
		this.update();
	}

	public void onEdit(TimelineModificationEvent e) {
		org.primefaces.extensions.model.timeline.TimelineEvent trialTimelineEvent = e.getTimelineEvent();
		setSelectedTimelineEvent((IDVO) trialTimelineEvent.getData());
		appendRequestContextCallbackArgs(true);
	}

	public void setDepartmentId(Long departmentId) {
		this.filterDepartmentId = departmentId;
	}

	public void setFilterVisitTypeId(Long filterVisitTypeId) {
		this.filterVisitTypeId = filterVisitTypeId;
	}

	public void setShowAll(boolean showAll) {
		this.showAll = showAll;
	}

	public void setShowDescription(boolean showDescription) {
		this.showDescription = showDescription;
	}

	public void setShowStartStop(boolean showStartStop) {
		this.showStartStop = showStartStop;
	}

	public void setShowTimelineEvents(boolean showTimelineEvents) {
		this.showTimelineEvents = showTimelineEvents;
	}

	public void setShowVisitScheduleItems(boolean showVisitScheduleItems) {
		this.showVisitScheduleItems = showVisitScheduleItems;
	}

	public void setStatusId(Long statusId) {
		this.filterStatusId = statusId;
	}

	public void setTrialId(Long filterTrialId) {
		this.filterTrialId = filterTrialId;
	}

	public void setTypeId(Long typeId) {
		this.filterTypeId = typeId;
	}

	public void updateTimelineModel() {
		timelines.clear();
		if (showTimelineEvents) {
			Collection<TimelineEventOutVO> timelineEvents = null;
			try {
				timelineEvents = WebUtil
						.getServiceLocator()
						.getTrialService()
						.getTimelineEventInterval(WebUtil.getAuthentication(), filterTrialId, filterDepartmentId, filterStatusId, filterTypeId, showAll ? null : true, rangeStart,
								rangeEnd);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
			if (timelineEvents != null && timelineEvents.size() > 0) {
				Iterator<TimelineEventOutVO> timelineEventsIt = timelineEvents.iterator();
				while (timelineEventsIt.hasNext()) {
					timelines.add(createTimelineEventFromOut(timelineEventsIt.next(), filterTrialId == null));
				}
			}
		}
		if (showVisitScheduleItems) {
			Collection<VisitScheduleAppointmentVO> visitScheduleAppointments = null;
			try {
				visitScheduleAppointments = WebUtil
						.getServiceLocator()
						.getTrialService()
						.getVisitScheduleItemInterval(WebUtil.getAuthentication(), filterTrialId, filterDepartmentId, filterStatusId, filterVisitTypeId, rangeStart, rangeEnd,
								false);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
			if (visitScheduleAppointments != null && visitScheduleAppointments.size() > 0) {
				Iterator<VisitScheduleAppointmentVO> visitScheduleAppointmentsIt = visitScheduleAppointments.iterator();
				while (visitScheduleAppointmentsIt.hasNext()) {
					timelines.add(createTimelineEventFromOut(visitScheduleAppointmentsIt.next(), filterTrialId == null));
				}
			}
		}
	}

	public String visitScheduleItemToStartStopString(VisitScheduleItemOutVO visitScheduleItem) {
		if (visitScheduleItem != null) {
			if (CommonUtil.isSameDay(visitScheduleItem.getStart(), visitScheduleItem.getStop())) {
				return Messages.getMessage(MessageCodes.TIMELINE_TRIAL_EVENT_START_STOP_LABEL, DateUtil.getDateTimeFormat().format(visitScheduleItem.getStart()), DateUtil
						.getTimeFormat().format(visitScheduleItem.getStop()));
			} else {
				return Messages.getMessage(MessageCodes.TIMELINE_TRIAL_EVENT_START_STOP_LABEL, DateUtil.getDateTimeFormat().format(visitScheduleItem.getStart()), DateUtil
						.getDateTimeFormat().format(visitScheduleItem.getStop()));
			}
		}
		return "";
	}

	public String visitScheduleAppointmentToStartStopString(VisitScheduleAppointmentVO visitScheduleAppointment) {
		if (visitScheduleAppointment != null) {
			if (CommonUtil.isSameDay(visitScheduleAppointment.getStart(), visitScheduleAppointment.getStop())) {
				return Messages.getMessage(MessageCodes.TIMELINE_TRIAL_EVENT_START_STOP_LABEL, DateUtil.getDateTimeFormat().format(visitScheduleAppointment.getStart()), DateUtil
						.getTimeFormat().format(visitScheduleAppointment.getStop()));
			} else {
				return Messages.getMessage(MessageCodes.TIMELINE_TRIAL_EVENT_START_STOP_LABEL, DateUtil.getDateTimeFormat().format(visitScheduleAppointment.getStart()), DateUtil
						.getDateTimeFormat().format(visitScheduleAppointment.getStop()));
			}
		}
		return "";
	}

	public boolean isEnableDepartmentFilter() {
		return Settings.getBoolean(SettingCodes.TIMELINE_ENABLE_DEPARTMENT_FILTER, Bundle.SETTINGS, DefaultSettings.TIMELINE_ENABLE_DEPARTMENT_FILTER);
	}

	public boolean isEnableTrialStatusTypeFilter() {
		return Settings.getBoolean(SettingCodes.TIMELINE_ENABLE_TRIAL_STATUS_TYPE_FILTER, Bundle.SETTINGS,
				DefaultSettings.TIMELINE_ENABLE_TRIAL_STATUS_TYPE_FILTER);
	}
	//----no: trial_filter
	//	public boolean isEnableTrialFilter() {
	//		return Settings.getBoolean(SettingCodes.TIMELINE_SCHEDULE_ENABLE_TRIAL_FILTER, Bundle.SETTINGS,
	//				DefaultSettings.TIMELINE_SCHEDULE_ENABLE_TRIAL_FILTER);
	//	}

	public boolean isEnableShowTimelineEventsFilter() {
		return Settings.getBoolean(SettingCodes.TIMELINE_ENABLE_TIMELINE_EVENTS_FILTER, Bundle.SETTINGS,
				DefaultSettings.TIMELINE_ENABLE_TIMELINE_EVENTS_FILTER);
	}

	public boolean isEnableTimelineEventTypeFilter() {
		return Settings.getBoolean(SettingCodes.TIMELINE_ENABLE_TIMELINE_EVENT_TYPE_FILTER, Bundle.SETTINGS,
				DefaultSettings.TIMELINE_ENABLE_TIMELINE_EVENT_TYPE_FILTER);
	}

	public boolean isEnableShowAllFilter() {
		return Settings.getBoolean(SettingCodes.TIMELINE_ENABLE_SHOW_ALL_FILTER, Bundle.SETTINGS,
				DefaultSettings.TIMELINE_ENABLE_SHOW_ALL_FILTER);
	}

	public boolean isEnableShowVisitScheduleItemsFilter() {
		return Settings.getBoolean(SettingCodes.TIMELINE_ENABLE_VISIT_SCHEDULE_ITEMS_FILTER, Bundle.SETTINGS,
				DefaultSettings.TIMELINE_ENABLE_VISIT_SCHEDULE_ITEMS_FILTER);
	}

	public boolean isEnableVisitTypeFilter() {
		return Settings.getBoolean(SettingCodes.TIMELINE_ENABLE_VISIT_TYPE_FILTER, Bundle.SETTINGS, DefaultSettings.TIMELINE_ENABLE_VISIT_TYPE_FILTER);
	}

	public boolean isEnableShowDescriptionFilter() {
		return Settings.getBoolean(SettingCodes.TIMELINE_ENABLE_DESCRIPTION_FILTER, Bundle.SETTINGS,
				DefaultSettings.TIMELINE_ENABLE_DESCRIPTION_FILTER);
	}

	public boolean isEnableShowStartStopFilter() {
		return Settings.getBoolean(SettingCodes.TIMELINE_ENABLE_START_STOP_FILTER, Bundle.SETTINGS,
				DefaultSettings.TIMELINE_ENABLE_START_STOP_FILTER);
	}
}
