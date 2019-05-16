package org.phoenixctms.ctsms.web.model.trial;

import java.util.Date;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.phoenixctms.ctsms.js.JsUtil;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.web.component.datatable.DataTable;
import org.phoenixctms.ctsms.web.model.PickerBeanBase;
import org.phoenixctms.ctsms.web.model.ShiftDurationSummaryModel;
import org.phoenixctms.ctsms.web.model.shared.CollidingStaffStatusEntryEagerModel;
import org.phoenixctms.ctsms.web.util.DateUtil;
import org.phoenixctms.ctsms.web.util.GetParamNames;
import org.phoenixctms.ctsms.web.util.JSValues;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.context.RequestContext;

@ManagedBean
@ViewScoped
public class TeamMemberPickerBean extends PickerBeanBase {

	private Date start;
	private Date stop;
	private Long trialId;
	private TrialOutVO trial;
	private TeamMemberLazyModel teamMemberModel;
	private HashMap<Long, ShiftDurationSummaryModel> shiftDurationModelCache;
	private HashMap<Long, CollidingStaffStatusEntryEagerModel> collidingStaffStatusEntryModelCache;

	public TeamMemberPickerBean() {
		super();
		collidingStaffStatusEntryModelCache = new HashMap<Long, CollidingStaffStatusEntryEagerModel>();
		shiftDurationModelCache = new HashMap<Long, ShiftDurationSummaryModel>();
		teamMemberModel = new TeamMemberLazyModel();
	}

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
		RequestContext requestContext = RequestContext.getCurrentInstance();
		if (requestContext != null) {
			requestContext.addCallbackParam(JSValues.AJAX_WINDOW_TITLE_BASE64.toString(), JsUtil.encodeBase64(getTitle(), false));
			requestContext.addCallbackParam(JSValues.AJAX_OPERATION_SUCCESS.toString(), operationSuccess);
		}
	}

	@Override
	protected String changeAction(Long id) {
		DataTable.clearFilters("team_member_list");
		this.trialId = id;
		initIn();
		initSets();
		return CHANGE_OUTCOME;
	}

	public CollidingStaffStatusEntryEagerModel getCollidingStaffStatusEntryModel(StaffOutVO staff) {
		return CollidingStaffStatusEntryEagerModel.getCachedCollidingStaffStatusEntryModel(staff, start, stop, true, collidingStaffStatusEntryModelCache);
	}

	@Override
	protected String getCurrentPageIds() {
		return this.teamMemberModel.getCurrentPageIds();
	}

	public String getMaxOverlappingShiftsString(StaffOutVO staff) {
		if (staff != null && staff.getAllocatable()) {
			return Messages.getMessage(MessageCodes.MAX_OVERLAPPING_LABEL, staff.getMaxOverlappingShifts());
		}
		return "";
	}

	public String getSetPickerIDJSCall(StaffOutVO staff) {
		return getSetPickerIDJSCall(staff == null ? null : staff.getId(), WebUtil.clipStringPicker(WebUtil.staffOutVOToString(staff)));
	}

	public ShiftDurationSummaryModel getShiftDurationModel(StaffOutVO staff) {
		return ShiftDurationSummaryModel.getCachedShiftDurationModel(staff, start != null ? start : new Date(), shiftDurationModelCache);
	}

	public TeamMemberLazyModel getTeamMemberModel() {
		return teamMemberModel;
	}

	@Override
	public String getTitle() {
		return Messages.getMessage(MessageCodes.TEAM_MEMBER_PICKER_TITLE, WebUtil.trialOutVOToString(trial));
	}

	@PostConstruct
	private void init() {
		initPickTarget();
		try {
			start = DateUtil.sanitizeTimelineTimestamp(false, new Date(WebUtil.getLongParamValue(GetParamNames.START)));
		} catch (Exception e) {
			start = null;
		}
		try {
			stop = DateUtil.sanitizeTimelineTimestamp(false, new Date(WebUtil.getLongParamValue(GetParamNames.STOP)));
		} catch (Exception e) {
			stop = null;
		}
		this.change(WebUtil.getParamValue(GetParamNames.TRIAL_ID));
	}

	private void initIn() {
		trial = WebUtil.getTrial(trialId);
	}

	private void initSets() {
		collidingStaffStatusEntryModelCache.clear();
		shiftDurationModelCache.clear();
		teamMemberModel.setTrialId(trialId);
		teamMemberModel.updateRowCount();
	}

	@Override
	public boolean isCreateable() {
		return false;
	}

	@Override
	public boolean isCreated() {
		return false;
	}
}
