package org.phoenixctms.ctsms.web.model.trial;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.TeamMemberInVO;
import org.phoenixctms.ctsms.vo.TeamMemberOutVO;
import org.phoenixctms.ctsms.vo.TeamMemberRoleVO;
import org.phoenixctms.ctsms.vo.TeamMembersExcelVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.web.component.datatable.DataTable;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.model.ShiftDurationSummaryModel;
import org.phoenixctms.ctsms.web.model.shared.StaffMultiPickerModel;
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
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean
@ViewScoped
public class TeamMemberBean extends ManagedBeanBase {

	public static void copyTeamMemberOutToIn(TeamMemberInVO in, TeamMemberOutVO out) {
		if (in != null && out != null) {
			TeamMemberRoleVO roleVO = out.getRole();
			StaffOutVO staffVO = out.getStaff();
			TrialOutVO trialVO = out.getTrial();
			in.setRoleId(roleVO == null ? null : roleVO.getId());
			in.setTrialId(trialVO == null ? null : trialVO.getId());
			in.setId(out.getId());
			in.setVersion(out.getVersion());
			in.setStaffId(staffVO == null ? null : staffVO.getId());
			in.setAccess(out.getAccess());
			in.setSign(out.getSign());
			in.setResolve(out.getResolve());
			in.setVerify(out.getVerify());
			in.setNotifyTimelineEvent(out.getNotifyTimelineEvent());
			in.setNotifyEcrfValidatedStatus(out.getNotifyEcrfValidatedStatus());
			in.setNotifyEcrfReviewStatus(out.getNotifyEcrfReviewStatus());
			in.setNotifyEcrfVerifiedStatus(out.getNotifyEcrfVerifiedStatus());
			in.setNotifyEcrfFieldStatus(out.getNotifyEcrfFieldStatus());
			in.setNotifyOther(out.getNotifyOther());
		}
	}

	public static void initTeamMemberDefaultValues(TeamMemberInVO in, Long trialId) {
		if (in != null) {
			in.setRoleId(null);
			in.setTrialId(trialId);
			in.setId(null);
			in.setVersion(null);
			in.setStaffId(null);
			in.setAccess(Settings.getBoolean(SettingCodes.TEAM_MEMBER_ACCESS_PRESET, Bundle.SETTINGS, DefaultSettings.TEAM_MEMBER_ACCESS_PRESET));
			in.setNotifyTimelineEvent(Settings.getBoolean(SettingCodes.TEAM_MEMBER_NOTIFY_TIMELINE_EVENT_PRESET, Bundle.SETTINGS,
					DefaultSettings.TEAM_MEMBER_NOTIFY_TIMELINE_EVENT_PRESET));
			in.setNotifyEcrfValidatedStatus(Settings.getBoolean(SettingCodes.TEAM_MEMBER_NOTIFY_ECRF_VALIDATED_STATUS_PRESET, Bundle.SETTINGS,
					DefaultSettings.TEAM_MEMBER_NOTIFY_ECRF_VALIDATED_STATUS_PRESET));
			in.setNotifyEcrfReviewStatus(Settings.getBoolean(SettingCodes.TEAM_MEMBER_NOTIFY_ECRF_REVIEW_STATUS_PRESET, Bundle.SETTINGS,
					DefaultSettings.TEAM_MEMBER_NOTIFY_ECRF_REVIEW_STATUS_PRESET));
			in.setNotifyEcrfVerifiedStatus(Settings.getBoolean(SettingCodes.TEAM_MEMBER_NOTIFY_ECRF_VERIFIED_STATUS_PRESET, Bundle.SETTINGS,
					DefaultSettings.TEAM_MEMBER_NOTIFY_ECRF_VERIFIED_STATUS_PRESET));
			in.setNotifyEcrfFieldStatus(Settings.getBoolean(SettingCodes.TEAM_MEMBER_NOTIFY_ECRF_FIELD_STATUS_PRESET, Bundle.SETTINGS,
					DefaultSettings.TEAM_MEMBER_NOTIFY_ECRF_FIELD_STATUS_PRESET));
			in.setNotifyOther(Settings.getBoolean(SettingCodes.TEAM_MEMBER_NOTIFY_OTHER_PRESET, Bundle.SETTINGS,
					DefaultSettings.TEAM_MEMBER_NOTIFY_OTHER_PRESET));
			in.setSign(Settings.getBoolean(SettingCodes.TEAM_MEMBER_SIGN_PRESET, Bundle.SETTINGS, DefaultSettings.TEAM_MEMBER_SIGN_PRESET));
			in.setResolve(Settings.getBoolean(SettingCodes.TEAM_MEMBER_RESOLVE_PRESET, Bundle.SETTINGS, DefaultSettings.TEAM_MEMBER_RESOLVE_PRESET));
			in.setVerify(Settings.getBoolean(SettingCodes.TEAM_MEMBER_VERIFY_PRESET, Bundle.SETTINGS, DefaultSettings.TEAM_MEMBER_VERIFY_PRESET));
		}
	}

	private Date now;
	private TeamMemberInVO in;
	private TeamMemberOutVO out;
	private Long trialId;
	private TrialOutVO trial;
	private StaffMultiPickerModel staffMultiPicker;
	private Long bulkAddRoleId;
	private boolean bulkAddAccess;
	private ArrayList<SelectItem> availableRoles;
	private TeamMemberLazyModel teamMemberModel;
	private HashMap<Long, ShiftDurationSummaryModel> shiftDurationModelCache;

	public TeamMemberBean() {
		super();
		now = new Date();
		shiftDurationModelCache = new HashMap<Long, ShiftDurationSummaryModel>();
		teamMemberModel = new TeamMemberLazyModel();
		staffMultiPicker = new StaffMultiPickerModel();
	}

	@Override
	public String addAction() {
		TeamMemberInVO backup = new TeamMemberInVO(in);
		in.setId(null);
		in.setVersion(null);
		sanitizeInVals();
		try {
			out = WebUtil.getServiceLocator().getTrialService().addTeamMember(WebUtil.getAuthentication(), in);
			initIn();
			initSets();
			addOperationSuccessMessage(MessageCodes.ADD_OPERATION_SUCCESSFUL);
			return ADD_OUTCOME;
		} catch (ServiceException | IllegalArgumentException | AuthorisationException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	public final void addBulk() {
		actionPostProcess(addBulkAction());
	}

	public String addBulkAction() {
		try {
			if (bulkAddRoleId != null) {
				Set<Long> ids = this.staffMultiPicker.getSelectionIds();
				Iterator<TeamMemberOutVO> it = WebUtil.getServiceLocator().getTrialService()
						.addTeamMembers(WebUtil.getAuthentication(), trialId, bulkAddRoleId, bulkAddAccess,
								Settings.getBoolean(SettingCodes.TEAM_MEMBER_SIGN_PRESET, Bundle.SETTINGS, DefaultSettings.TEAM_MEMBER_SIGN_PRESET),
								Settings.getBoolean(SettingCodes.TEAM_MEMBER_RESOLVE_PRESET, Bundle.SETTINGS, DefaultSettings.TEAM_MEMBER_RESOLVE_PRESET),
								Settings.getBoolean(SettingCodes.TEAM_MEMBER_VERIFY_PRESET, Bundle.SETTINGS, DefaultSettings.TEAM_MEMBER_VERIFY_PRESET), ids)
						.iterator();
				while (it.hasNext()) {
					this.staffMultiPicker.removeId(it.next().getStaff().getId());
				}
				int itemsLeft = staffMultiPicker.getSelection().size();
				if (itemsLeft > 0) {
					Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.BULK_ADD_OPERATION_INCOMPLETE, ids.size() - itemsLeft, ids.size());
				} else {
					Messages.addLocalizedMessage(FacesMessage.SEVERITY_INFO, MessageCodes.BULK_ADD_OPERATION_SUCCESSFUL, ids.size(), ids.size());
				}
				teamMemberModel.updateRowCount();
				return BULK_ADD_OUTCOME;
			} else {
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_ERROR, MessageCodes.BULK_ADD_TEAM_MEMBER_ROLE_REQUIRED);
			}
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
		RequestContext requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_TEAM_MEMBER_TAB_TITLE_BASE64, JSValues.AJAX_TEAM_MEMBER_COUNT,
				MessageCodes.TEAM_MEMBERS_TAB_TITLE, MessageCodes.TEAM_MEMBERS_TAB_TITLE_WITH_COUNT, new Long(teamMemberModel.getRowCount()));
		if (operationSuccess && in.getTrialId() != null) {
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_TRIAL_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_TRIAL_JOURNAL_ENTRY_COUNT,
					MessageCodes.TRIAL_JOURNAL_TAB_TITLE, MessageCodes.TRIAL_JOURNAL_TAB_TITLE_WITH_COUNT,
					WebUtil.getJournalCount(JournalModule.TRIAL_JOURNAL, in.getTrialId()));
		}
	}

	@Override
	protected String changeAction(Long id) {
		DataTable.clearFilters("team_member_list");
		out = null;
		this.trialId = id;
		staffMultiPicker.clear();
		bulkAddRoleId = null;
		bulkAddAccess = Settings.getBoolean(SettingCodes.TEAM_MEMBER_ACCESS_PRESET, Bundle.SETTINGS, DefaultSettings.TEAM_MEMBER_ACCESS_PRESET);
		initIn();
		initSets();
		return CHANGE_OUTCOME;
	}

	@Override
	public String deleteAction() {
		return deleteAction(in.getId());
	}

	@Override
	public String deleteAction(Long id) {
		try {
			out = WebUtil.getServiceLocator().getTrialService().deleteTeamMember(WebUtil.getAuthentication(), id);
			initIn();
			initSets();
			out = null;
			addOperationSuccessMessage(MessageCodes.DELETE_OPERATION_SUCCESSFUL);
			return DELETE_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	public ArrayList<SelectItem> getAvailableRoles() {
		return availableRoles;
	}

	public Long getBulkAddRoleId() {
		return bulkAddRoleId;
	}

	public TeamMemberInVO getIn() {
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

	public TeamMemberOutVO getOut() {
		return out;
	}

	public IDVO getSelectedTeamMember() {
		if (this.out != null) {
			return IDVO.transformVo(this.out);
		} else {
			return null;
		}
	}

	public ShiftDurationSummaryModel getShiftDurationModel(StaffOutVO staff) {
		return ShiftDurationSummaryModel.getCachedShiftDurationModel(staff, now, shiftDurationModelCache);
	}

	public StaffMultiPickerModel getStaffMultiPicker() {
		return staffMultiPicker;
	}

	public String getStaffName() {
		return WebUtil.staffIdToName(in.getStaffId());
	}

	public TeamMemberLazyModel getTeamMemberModel() {
		return teamMemberModel;
	}

	public StreamedContent getTeamMembersExcelStreamedContent() throws Exception {
		try {
			TeamMembersExcelVO excel = WebUtil.getServiceLocator().getTrialService().exportTeamMembers(WebUtil.getAuthentication(), trialId);
			return new DefaultStreamedContent(new ByteArrayInputStream(excel.getDocumentDatas()), excel.getContentType().getMimeType(), excel.getFileName());
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
			throw e;
		} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
			throw e;
		}
	}

	@Override
	public String getTitle() {
		if (out != null) {
			return Messages.getMessage(MessageCodes.TEAM_MEMBER_TITLE, Long.toString(out.getId()), out.getRole().getName(), out.getStaff().getName());
		} else {
			return Messages.getString(MessageCodes.CREATE_NEW_TEAM_MEMBER);
		}
	}

	public void handleAccessChange() {
		if (!in.getAccess()) {
			in.setSign(false);
			in.setResolve(false);
			in.setVerify(false);
		}
	}

	@PostConstruct
	private void init() {
		Long id = WebUtil.getLongParamValue(GetParamNames.TEAM_MEMBER_ID);
		if (id != null) {
			this.load(id);
		} else {
			initIn();
			initSets();
		}
	}

	private void initIn() {
		if (in == null) {
			in = new TeamMemberInVO();
		}
		if (out != null) {
			copyTeamMemberOutToIn(in, out);
			trialId = in.getTrialId();
		} else {
			initTeamMemberDefaultValues(in, trialId);
		}
	}

	private void initSets() {
		now = new Date();
		shiftDurationModelCache.clear();
		teamMemberModel.setTrialId(in.getTrialId());
		teamMemberModel.updateRowCount();
		Collection<TeamMemberRoleVO> roleVOs = null;
		try {
			roleVOs = WebUtil.getServiceLocator().getSelectionSetService().getAvailableTeamMemberRoles(WebUtil.getAuthentication(), in.getTrialId(), in.getRoleId());
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
		if (roleVOs != null) {
			availableRoles = new ArrayList<SelectItem>(roleVOs.size());
			Iterator<TeamMemberRoleVO> it = roleVOs.iterator();
			while (it.hasNext()) {
				TeamMemberRoleVO roleVO = it.next();
				availableRoles.add(new SelectItem(roleVO.getId().toString(), roleVO.getName()));
			}
		} else {
			availableRoles = new ArrayList<SelectItem>();
		}
		trial = WebUtil.getTrial(this.in.getTrialId());
		if (WebUtil.isTrialLocked(trial)) {
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.TRIAL_LOCKED);
		}
	}

	public boolean isBulkAddAccess() {
		return bulkAddAccess;
	}

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

	public boolean isInputVisible() {
		return isCreated() || !WebUtil.isTrialLocked(trial);
	}

	@Override
	public boolean isRemovable() {
		return isCreated() && !WebUtil.isTrialLocked(trial);
	}

	public boolean isStaffBulkCreateable() {
		return isCreateable() && staffMultiPicker.getIsEnabled();
	}

	@Override
	public String loadAction() {
		return loadAction(in.getId());
	}

	@Override
	public String loadAction(Long id) {
		out = null;
		try {
			out = WebUtil.getServiceLocator().getTrialService().getTeamMember(WebUtil.getAuthentication(), id);
			return LOAD_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
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

	@Override
	public String resetAction() {
		out = null;
		initIn();
		initSets();
		return RESET_OUTCOME;
	}

	private void sanitizeInVals() {
		if (!in.getAccess()) {
			in.setSign(false);
			in.setResolve(false);
			in.setVerify(false);
		}
	}

	public void setBulkAddAccess(boolean bulkAddAccess) {
		this.bulkAddAccess = bulkAddAccess;
	}

	public void setBulkAddRoleId(Long bulkAddRoleId) {
		this.bulkAddRoleId = bulkAddRoleId;
	}

	public void setSelectedTeamMember(IDVO teamMember) {
		if (teamMember != null) {
			this.out = (TeamMemberOutVO) teamMember.getVo();
			this.initIn();
			initSets();
		}
	}

	@Override
	public String updateAction() {
		sanitizeInVals();
		try {
			out = WebUtil.getServiceLocator().getTrialService().updateTeamMember(WebUtil.getAuthentication(), in);
			initIn();
			initSets();
			addOperationSuccessMessage(MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
			return UPDATE_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	public StreamedContent getTrialTrainingRecordPdfStreamedContent(TeamMemberOutVO teamMember, boolean appendCertificates) throws Exception {
		return WebUtil.getTrialTrainingRecordPdfStreamedContent(teamMember != null ? teamMember.getStaff().getId() : null, in.getTrialId(), appendCertificates);
	}
}
