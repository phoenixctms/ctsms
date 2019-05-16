package org.phoenixctms.ctsms.web.model.trial;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.vo.DutyRosterTurnInVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.web.component.datatable.DataTable;
import org.phoenixctms.ctsms.web.model.shared.DutyRosterTurnBeanBase;
import org.phoenixctms.ctsms.web.util.DateUtil;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.GetParamNames;
import org.phoenixctms.ctsms.web.util.JSValues;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.context.RequestContext;

@ManagedBean
@ViewScoped
public class TrialDutyRosterTurnBean extends DutyRosterTurnBeanBase {

	public static void initDutyRosterTurnDefaultValues(DutyRosterTurnInVO in, Long trialId, StaffOutVO identity) {
		if (in != null) {
			boolean selfAllocatablePreset = Settings.getBoolean(SettingCodes.DUTY_ROSTER_SELF_ALLOCATABLE_PRESET, Bundle.SETTINGS,
					DefaultSettings.DUTY_ROSTER_SELF_ALLOCATABLE_PRESET);
			in.setId(null);
			in.setVersion(null);
			in.setComment(Messages.getString(MessageCodes.DUTY_ROSTER_TURN_COMMENT_PRESET));
			in.setStaffId(null);
			in.setStart(null);
			in.setStop(null);
			in.setTrialId(trialId);
			in.setSelfAllocatable(selfAllocatablePreset);
			in.setTitle(Messages.getString(MessageCodes.DUTY_ROSTER_TURN_TITLE_PRESET));
			in.setCalendar(Messages.getString(MessageCodes.DUTY_ROSTER_TURN_CALENDAR_PRESET));
			in.setVisitScheduleItemId(null);
		}
	}

	private Long trialId;
	private TrialOutVO trial;
	private TrialDutyRosterTurnLazyModel dutyRosterTurnModel;

	public TrialDutyRosterTurnBean() {
		super();
		dutyRosterTurnModel = new TrialDutyRosterTurnLazyModel();
	}

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
		RequestContext requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_TRIAL_DUTY_ROSTER_TURN_TAB_TITLE_BASE64,
				JSValues.AJAX_TRIAL_DUTY_ROSTER_TURN_COUNT, MessageCodes.TRIAL_DUTY_ROSTER_TURNS_TAB_TITLE, MessageCodes.TRIAL_DUTY_ROSTER_TURNS_TAB_TITLE_WITH_COUNT, new Long(
						dutyRosterTurnModel.getRowCount()));
		if (operationSuccess && in.getTrialId() != null) {
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_TRIAL_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_TRIAL_JOURNAL_ENTRY_COUNT,
					MessageCodes.TRIAL_JOURNAL_TAB_TITLE, MessageCodes.TRIAL_JOURNAL_TAB_TITLE_WITH_COUNT,
					WebUtil.getJournalCount(JournalModule.TRIAL_JOURNAL, in.getTrialId()));
		}
	}

	@Override
	protected String changeAction(Long id) {
		DataTable.clearFilters("dutyrosterturn_list");
		out = null;
		this.trialId = id;
		initIn();
		initSets();
		return CHANGE_OUTCOME;
	}

	public TrialDutyRosterTurnLazyModel getDutyRosterTurnModel() {
		return dutyRosterTurnModel;
	}

	@Override
	public String getTitle() {
		if (out != null) {
			return Messages.getMessage(MessageCodes.TRIAL_DUTY_ROSTER_TURN_TITLE, Long.toString(out.getId()), getTitle(out),
					DateUtil.getDateTimeStartStopString(out.getStart(), out.getStop()));
		} else {
			return Messages.getString(MessageCodes.TRIAL_CREATE_NEW_DUTY_ROSTER_TURN);
		}
	}

	@Override
	public ArrayList<SelectItem> getVisitScheduleItems() {
		return getTrialVisitScheduleItems(); // must not be cached!
	}

	@Override
	public void handleVisitScheduleItemChange() {
		// don't alter trialid here...
	}

	@PostConstruct
	private void init() {
		Long id = WebUtil.getLongParamValue(GetParamNames.DUTY_ROSTER_TURN_ID);
		if (id != null) {
			this.load(id);
		} else {
			initIn();
			initSets();
		}
	}

	@Override
	protected void initIn() {
		if (in == null) {
			in = new DutyRosterTurnInVO();
		}
		if (out != null) {
			copyDutyRosterTurnOutToIn(in, out);
			trialId = in.getTrialId();
		} else {
			initDutyRosterTurnDefaultValues(in, trialId, WebUtil.getUserIdentity());
		}
	}

	@Override
	protected void initSets() {
		collidingStaffStatusEntryModelCache.clear();
		collidingInventoryBookingModelCache.clear();
		dutyRosterTurnModel.setTrialId(in.getTrialId());
		dutyRosterTurnModel.updateRowCount();
		trial = WebUtil.getTrial(this.in.getTrialId());
		filterCalendars = WebUtil.getDutyRosterTurnFilterCalendars(null, trialId, null);
		filterTitles = WebUtil.getDutyRosterTurnFilterTitles(null, trialId, null);
		if (WebUtil.isTrialLocked(trial)) {
			Messages.addLocalizedMessageClientId("dutyRosterScheduleInputMessages", FacesMessage.SEVERITY_WARN, MessageCodes.TRIAL_LOCKED);
		}
	}

	@Override
	public boolean isCreateable() {
		return (in.getTrialId() == null ? false : !WebUtil.isTrialLocked(trial));
	}

	@Override
	public boolean isEditable() {
		return isCreated() && !WebUtil.isTrialLocked(trial);
	}

	public boolean isInputVisible() {
		return isCreated() || !WebUtil.isTrialLocked(trial);
	}

	@Override
	public boolean isRemovable() {
		return isCreated() && !WebUtil.isTrialLocked(trial);
	}
}
