package org.phoenixctms.ctsms.web.model.trial;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.TimelineEventOutVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;

@ManagedBean
@ViewScoped
public class TimelineEventOverviewBean extends ManagedBeanBase {

	private Date today;
	private TimelineScheduleLazyModel timelineScheduleModel;

	public TimelineEventOverviewBean() {
		super();
		today = new Date();
		timelineScheduleModel = new TimelineScheduleLazyModel();
	}

	public void dismiss(TimelineEventOutVO timelineEvent) {
		if (timelineEvent != null) {
			try {
				WebUtil.getServiceLocator().getTrialService().setTimelineEventDismissed(WebUtil.getAuthentication(), timelineEvent.getId(), timelineEvent.getVersion(), true);
				initIn();
				initSets();
				addOperationSuccessMessage(MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
				Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			} catch (AuthenticationException e) {
				Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
				WebUtil.publishException(e);
			}
		}
	}

	public String getTimelineEventDueInString(TimelineEventOutVO timelineEvent) {
		if (timelineEvent != null) {
			return WebUtil.getExpirationDueInString(today, timelineEvent.getStart());
		}
		return "";
	}

	public TimelineScheduleLazyModel getTimelineScheduleModel() {
		return timelineScheduleModel;
	}

	public void handleIdentityTeamMemberChange() {
		initSets();
	}

	@PostConstruct
	private void init() {
		initIn();
		initSets();
	}

	private void initIn() {
	}

	private void initSets() {
		today = new Date();
		timelineScheduleModel.updateRowCount();
		LazyDataModelBase.clearFilters("timelineevent_list");
	}

	@Override
	public boolean isCreateable() {
		return false;
	}

	@Override
	public boolean isCreated() {
		return false;
	}

	@Override
	public String loadAction() {
		initIn();
		initSets();
		return LOAD_OUTCOME;
	}

	public String timelineEventToColor(TimelineEventOutVO timelineEvent) {
		if (timelineEvent != null) {
			return WebUtil.expirationToColor(today, timelineEvent.getStart(), Settings.getTimelineEventDueInColorMap(),
					Settings.getColor(SettingCodes.TIMELINE_EVENT_OVERDUE_COLOR, Bundle.SETTINGS, DefaultSettings.TIMELINE_EVENT_OVERDUE_COLOR));
		}
		return "";
	}
}
