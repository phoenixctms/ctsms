package org.phoenixctms.ctsms.web.model.staff;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.vo.DutyRosterTurnInVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
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
public class StaffDutyRosterTurnBean extends DutyRosterTurnBeanBase {

	public static void initDutyRosterTurnDefaultValues(DutyRosterTurnInVO in, Long staffId) {
		if (in != null) {
			boolean selfAllocatablePreset = Settings.getBoolean(SettingCodes.DUTY_ROSTER_SELF_ALLOCATABLE_PRESET, Bundle.SETTINGS,
					DefaultSettings.DUTY_ROSTER_SELF_ALLOCATABLE_PRESET);
			in.setId(null);
			in.setVersion(null);
			in.setComment(Messages.getString(MessageCodes.DUTY_ROSTER_TURN_COMMENT_PRESET));
			in.setStaffId(staffId);
			in.setStart(null);
			in.setStop(null);
			in.setTrialId(null);
			in.setSelfAllocatable(false);
			in.setTitle(Messages.getString(MessageCodes.DUTY_ROSTER_TURN_TITLE_PRESET));
			in.setCalendar(Messages.getString(MessageCodes.DUTY_ROSTER_TURN_CALENDAR_PRESET));
			in.setVisitScheduleItemId(null);
		}
	}

	private Long staffId;
	private StaffOutVO staff;
	private StaffDutyRosterTurnLazyModel dutyRosterTurnModel;

	public StaffDutyRosterTurnBean() {
		super();
		dutyRosterTurnModel = new StaffDutyRosterTurnLazyModel();
	}

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
		RequestContext requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_STAFF_DUTY_ROSTER_TURN_TAB_TITLE_BASE64,
				JSValues.AJAX_STAFF_DUTY_ROSTER_TURN_COUNT, MessageCodes.STAFF_DUTY_ROSTER_TURNS_TAB_TITLE, MessageCodes.STAFF_DUTY_ROSTER_TURNS_TAB_TITLE_WITH_COUNT, new Long(
						dutyRosterTurnModel.getRowCount()));
		if (operationSuccess && in.getStaffId() != null) {
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_STAFF_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_STAFF_JOURNAL_ENTRY_COUNT,
					MessageCodes.STAFF_JOURNAL_TAB_TITLE, MessageCodes.STAFF_JOURNAL_TAB_TITLE_WITH_COUNT,
					WebUtil.getJournalCount(JournalModule.STAFF_JOURNAL, in.getStaffId()));
		}
	}

	@Override
	protected String changeAction(Long id) {
		LazyDataModelBase.clearFilters("dutyrosterturn_list");
		out = null;
		this.staffId = id;
		initIn();
		initSets();
		return CHANGE_OUTCOME;
	}

	public StaffDutyRosterTurnLazyModel getDutyRosterTurnModel() {
		return dutyRosterTurnModel;
	}

	@Override
	public String getTitle() {
		if (out != null) {
			return Messages.getMessage(MessageCodes.STAFF_DUTY_ROSTER_TURN_TITLE, Long.toString(out.getId()), getTitle(out),
					DateUtil.getDateTimeStartStopString(out.getStart(), out.getStop()));
		} else {
			return Messages.getString(MessageCodes.STAFF_CREATE_NEW_DUTY_ROSTER_TURN);
		}
	}

	@Override
	public ArrayList<SelectItem> getVisitScheduleItems() {
		return getTrialVisitScheduleItems(); // must not be cached!
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
			staffId = in.getStaffId();
		} else {
			initDutyRosterTurnDefaultValues(in, staffId);
		}
	}

	@Override
	protected void initSets() {
		collidingStaffStatusEntryModelCache.clear();
		collidingInventoryBookingModelCache.clear();
		dutyRosterTurnModel.setStaffId(in.getStaffId());
		dutyRosterTurnModel.updateRowCount();
		staff = WebUtil.getStaff(in.getStaffId(), null, null, null);
		filterCalendars = WebUtil.getDutyRosterTurnFilterCalendars(null, null, staffId);
		filterTitles = WebUtil.getDutyRosterTurnFilterTitles(null, null, staffId);
		if (staff != null) {
			if (!WebUtil.isStaffPerson(staff)) {
				Messages.addLocalizedMessageClientId("dutyRosterScheduleInputMessages", FacesMessage.SEVERITY_INFO, MessageCodes.STAFF_NOT_PERSON);
			} else if (!WebUtil.isStaffAllocatable(staff)) {
				Messages.addLocalizedMessageClientId("dutyRosterScheduleInputMessages", FacesMessage.SEVERITY_INFO, MessageCodes.STAFF_NOT_ALLOCATABLE);
			}
		}
	}

	@Override
	public boolean isCreateable() {
		return (this.in.getStaffId() == null ? false : WebUtil.isStaffAllocatable(staff));
	}

	@Override
	public boolean isEditable() {
		return isCreated() && WebUtil.isStaffAllocatable(staff);
	}

	public boolean isInputVisible() {
		return isCreated() || WebUtil.isStaffAllocatable(staff);
	}

	@Override
	public boolean isRemovable() {
		return isCreated() && WebUtil.isStaffAllocatable(staff);
	}
}
