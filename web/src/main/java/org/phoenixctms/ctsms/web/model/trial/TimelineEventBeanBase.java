package org.phoenixctms.ctsms.web.model.trial;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.EventImportance;
import org.phoenixctms.ctsms.enumeration.VariablePeriod;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.js.JsUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.EventImportanceVO;
import org.phoenixctms.ctsms.vo.TimelineEventInVO;
import org.phoenixctms.ctsms.vo.TimelineEventOutVO;
import org.phoenixctms.ctsms.vo.TimelineEventTypeVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.vo.VariablePeriodVO;
import org.phoenixctms.ctsms.web.model.EventImportanceSelector;
import org.phoenixctms.ctsms.web.model.EventImportanceSelectorListener;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.model.VariablePeriodSelector;
import org.phoenixctms.ctsms.web.model.VariablePeriodSelectorListener;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.JSValues;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.context.RequestContext;

public abstract class TimelineEventBeanBase extends ManagedBeanBase implements VariablePeriodSelectorListener, EventImportanceSelectorListener {

	private static final int IMPORTANCE_PROPERTY_ID = 1;
	private static final int REMINDER_PERIOD_PROPERTY_ID = 1;

	public static void copyTimelineEventOutToIn(TimelineEventInVO in, TimelineEventOutVO out, Date today) {
		if (in != null && out != null) {
			TrialOutVO trialVO = out.getTrial();
			TimelineEventTypeVO eventTypeVO = out.getType();
			VariablePeriodVO reminderPeriodVO = out.getReminderPeriod();
			EventImportanceVO importanceVO = out.getImportance();
			in.setDescription(out.getDescription());
			in.setId(out.getId());
			in.setImportance(importanceVO == null ? null : importanceVO.getImportance());
			in.setNotify(out.getNotify());
			in.setReminderPeriod(reminderPeriodVO == null ? null : reminderPeriodVO.getPeriod());
			in.setReminderPeriodDays(out.getReminderPeriodDays());
			in.setDismissed(getDismissed(out, today));
			in.setShow(out.getShow());
			in.setStart(out.getStart());
			in.setStop(out.getStop());
			in.setTitle(out.getTitle());
			in.setTrialId(trialVO == null ? null : trialVO.getId());
			in.setTypeId(eventTypeVO == null ? null : eventTypeVO.getId());
			in.setVersion(out.getVersion());
		}
	}

	protected static boolean getDismissed(TimelineEventOutVO timelineEvent, Date today) {
		boolean dismissed = false;
		Date reminderStart = (timelineEvent == null ? null : timelineEvent.getReminderStart());
		if (reminderStart != null && today != null) {
			if (today.compareTo(reminderStart) >= 0) {
				Date dismissedTimestamp = timelineEvent.getDismissedTimestamp();
				if (dismissedTimestamp != null && reminderStart.compareTo(dismissedTimestamp) <= 0) {
					dismissed = timelineEvent.isDismissed();
				}
			}
		}
		return dismissed;
	}

	public static void initTimelineEventDefaultValues(TimelineEventInVO in, Long trialId) {
		if (in != null) {
			in.setDescription(Messages.getString(MessageCodes.TIMELINE_EVENT_DESCRIPTION_PRESET));
			in.setId(null);
			in.setImportance(Settings.getEventImportance(SettingCodes.TIMELINE_EVENT_IMPORTANCE_PRESET, Bundle.SETTINGS, DefaultSettings.TIMELINE_EVENT_IMPORTANCE_PRESET));
			in.setNotify(Settings.getBoolean(SettingCodes.TIMELINE_EVENT_NOTIFY_PRESET, Bundle.SETTINGS, DefaultSettings.TIMELINE_EVENT_NOTIFY_PRESET));
			in.setReminderPeriod(Settings.getVariablePeriod(SettingCodes.TIMELINE_EVENT_REMINDER_PERIOD_PRESET, Bundle.SETTINGS,
					DefaultSettings.TIMELINE_EVENT_REMINDER_PERIOD_PRESET));
			in.setReminderPeriodDays(Settings.getLongNullable(SettingCodes.TIMELINE_EVENT_REMINDER_PERIOD_DAYS_PRESET, Bundle.SETTINGS,
					DefaultSettings.TIMELINE_EVENT_REMINDER_PERIOD_DAYS_PRESET));
			in.setDismissed(Settings.getBoolean(SettingCodes.TIMELINE_EVENT_DISMISSED_PRESET, Bundle.SETTINGS, DefaultSettings.TIMELINE_EVENT_DISMISSED_PRESET));
			in.setShow(Settings.getBoolean(SettingCodes.TIMELINE_EVENT_SHOW_PRESET, Bundle.SETTINGS, DefaultSettings.TIMELINE_EVENT_SHOW_PRESET));
			in.setStart(new Date());
			in.setStop(null);
			in.setTitle(Messages.getString(MessageCodes.TIMELINE_EVENT_TITLE_PRESET));
			in.setTrialId(trialId);
			in.setTypeId(null);
			in.setVersion(null);
		}
	}

	protected Date today;
	protected TimelineEventInVO in;
	protected TimelineEventOutVO out;
	private TimelineEventTypeVO timelineEventType;
	private EventImportanceSelector importance;
	private VariablePeriodSelector reminder;
	protected Long trialId;
	protected TrialOutVO trial;

	public TimelineEventBeanBase() {
		super();
		today = new Date();
		setReminder(new VariablePeriodSelector(this, REMINDER_PERIOD_PROPERTY_ID));
		setEventImportance(new EventImportanceSelector(this, IMPORTANCE_PROPERTY_ID));
	}

	@Override
	public String addAction() {
		TimelineEventInVO backup = new TimelineEventInVO(in);
		in.setId(null);
		in.setVersion(null);
		sanitizeInVals();
		try {
			out = WebUtil.getServiceLocator().getTrialService().addTimelineEvent(WebUtil.getAuthentication(), in);
			initIn();
			initSets();
			addOperationSuccessMessage(MessageCodes.ADD_OPERATION_SUCCESSFUL);
			return ADD_OUTCOME;
		} catch (ServiceException|IllegalArgumentException|AuthorisationException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	@Override
	public String deleteAction() {
		return deleteAction(in.getId());
	}

	@Override
	public String deleteAction(Long id) {
		try {
			out = WebUtil.getServiceLocator().getTrialService().deleteTimelineEvent(WebUtil.getAuthentication(), id);
			initIn();
			initSets();
			out = null;
			addOperationSuccessMessage(MessageCodes.DELETE_OPERATION_SUCCESSFUL);
			return DELETE_OUTCOME;
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	public boolean getDismissed(TimelineEventOutVO timelineEvent) {
		return getDismissed(timelineEvent, today);
	}

	public EventImportanceSelector getEventImportance() { // naming conflict if not "event"importance
		return importance;
	}

	protected ArrayList<SelectItem> getEventTypes() {
		ArrayList<SelectItem> statusTypes;
		Collection<TimelineEventTypeVO> eventTypeVOs = null;
		try {
			eventTypeVOs = WebUtil.getServiceLocator().getSelectionSetService().getAvailableTimelineEventTypes(WebUtil.getAuthentication(), in.getTrialId(), in.getTypeId());
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
		if (eventTypeVOs != null) {
			statusTypes = new ArrayList<SelectItem>(eventTypeVOs.size());
			Iterator<TimelineEventTypeVO> it = eventTypeVOs.iterator();
			while (it.hasNext()) {
				TimelineEventTypeVO eventTypeVO = it.next();
				statusTypes.add(new SelectItem(eventTypeVO.getId().toString(), eventTypeVO.getName()));
			}
		} else {
			statusTypes = new ArrayList<SelectItem>();
		}
		return statusTypes;
	}

	@Override
	public EventImportance getImportance(int property) {
		switch (property) {
			case IMPORTANCE_PROPERTY_ID:
				return this.in.getImportance();
			default:
				return EventImportanceSelectorListener.NO_SELECTION_EVENT_IMPORTANCE;
		}
	}

	public TimelineEventInVO getIn() {
		return in;
	}

	@Override
	public String getModifiedAnnotation() {
		if (out != null) {
			return WebUtil.getModifiedAnnotation(out.getVersion(), out.getModifiedUser(), out.getModifiedTimestamp());
		} else {
			return super.getModifiedAnnotation();
		}
	}

	public TimelineEventOutVO getOut() {
		return out;
	}

	@Override
	public VariablePeriod getPeriod(int property) {
		switch (property) {
			case REMINDER_PERIOD_PROPERTY_ID:
				return this.in.getReminderPeriod();
			default:
				return VariablePeriodSelectorListener.NO_SELECTION_VARIABLE_PERIOD;
		}
	}

	public VariablePeriodSelector getReminder() {
		return reminder;
	}

	public IDVO getSelectedTimelineEvent() {
		if (this.out != null) {
			return IDVO.transformVo(this.out);
		} else {
			return null;
		}
	}

	public TimelineEventTypeVO getTimelineEventType() {
		return timelineEventType;
	}

	private String getTitlePreset() {
		switch (timelineEventType.getTitlePresetType()) {
			case TRIAL_NAME:
				return CommonUtil.trialOutVOToString(trial);
			case TEXT:
				return timelineEventType.getTitlePreset();
			default:
		}
		return null;
	}

	public void handleTypeChange() {
		loadSelectedType();
		RequestContext requestContext = RequestContext.getCurrentInstance();
		if (requestContext != null && timelineEventType != null) {
			requestContext.addCallbackParam(JSValues.AJAX_TIMELINE_EVENT_TYPE_IMPORTANCE_PRESET.toString(), timelineEventType.getImportancePreset().name());
			requestContext.addCallbackParam(JSValues.AJAX_TIMELINE_EVENT_TYPE_SHOW_PRESET.toString(), timelineEventType.getShowPreset());
			requestContext.addCallbackParam(JSValues.AJAX_TIMELINE_EVENT_TYPE_NOTIFY_PRESET.toString(), timelineEventType.getNotifyPreset());
			requestContext.addCallbackParam(JSValues.AJAX_TIMELINE_EVENT_TYPE_TITLE_PRESET_BASE64.toString(), JsUtil.encodeBase64(getTitlePreset(), false));
			requestContext.addCallbackParam(JSValues.AJAX_TIMELINE_EVENT_TYPE_TITLE_PRESET_FIXED.toString(), timelineEventType.getTitlePresetFixed());
		}
	}

	protected void initIn() {
		if (in == null) {
			in = new TimelineEventInVO();
		}
		if (out != null) {
			copyTimelineEventOutToIn(in, out, today);
			trialId = in.getTrialId();
		} else {
			initTimelineEventDefaultValues(in, trialId);
		}
	}

	protected void initSets() {
		today = new Date();
		loadSelectedType();
		initSpecificSets();
		trial = WebUtil.getTrial(this.in.getTrialId());
		if (WebUtil.isTrialLocked(trial)) {
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.TRIAL_LOCKED);
		}
	}

	protected abstract void initSpecificSets();

	@Override
	public boolean isCreateable() {
		return (in.getTrialId() == null ? false : !WebUtil.isTrialLocked(trial));
	}

	@Override
	public boolean isCreated() {
		return out != null;
	}

	@Override
	public boolean isEditable() {
		return isCreated() && !WebUtil.isTrialLocked(trial);
	}

	public boolean isReminderPeriodSelectorEnabled() {
		return true;
	}

	public boolean isReminderPeriodSpinnerEnabled() {
		return this.in.getReminderPeriod() == null || VariablePeriod.EXPLICIT.equals(this.in.getReminderPeriod());
	}

	@Override
	public boolean isRemovable() {
		return isCreated() && !WebUtil.isTrialLocked(trial);
	}

	@Override
	public String loadAction() {
		return loadAction(in.getId());
	}

	@Override
	public String loadAction(Long id) {
		out = null;
		try {
			out = WebUtil.getServiceLocator().getTrialService().getTimelineEvent(WebUtil.getAuthentication(), id);
			return LOAD_OUTCOME;
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} finally {
			initIn();
			initSets();
		}
		return ERROR_OUTCOME;
	}

	protected void loadSelectedType() {
		timelineEventType = WebUtil.getTimelineEventType(in.getTypeId());
	}

	@Override
	public String resetAction() {
		out = null;
		initIn();
		initSets();
		return RESET_OUTCOME;
	}

	protected void sanitizeInVals() {
		if (timelineEventType != null) {
			if (timelineEventType.isTitlePresetFixed()) {
				in.setTitle(getTitlePreset());
			}
		}
	}

	public void setEventImportance(EventImportanceSelector importance) {
		this.importance = importance;
	}

	@Override
	public void setImportance(int property, EventImportance importance) {
		switch (property) {
			case IMPORTANCE_PROPERTY_ID:
				this.in.setImportance(importance);
				break;
			default:
		}
	}

	@Override
	public void setPeriod(int property, VariablePeriod period) {
		switch (property) {
			case REMINDER_PERIOD_PROPERTY_ID:
				this.in.setReminderPeriod(period);
				break;
			default:
		}
	}

	public void setReminder(VariablePeriodSelector reminder) {
		this.reminder = reminder;
	}

	public void setSelectedTimelineEvent(IDVO timelineEvent) {
		if (timelineEvent != null) {
			this.out = (TimelineEventOutVO) timelineEvent.getVo();
			this.initIn();
			initSets();
		}
	}

	@Override
	public String updateAction() {
		TimelineEventInVO backup = new TimelineEventInVO(in);
		sanitizeInVals();
		try {
			out = WebUtil.getServiceLocator().getTrialService().updateTimelineEvent(WebUtil.getAuthentication(), in);
			initIn();
			initSets();
			addOperationSuccessMessage(MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
			return UPDATE_OUTCOME;
		} catch (ServiceException|IllegalArgumentException|AuthorisationException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}
}
