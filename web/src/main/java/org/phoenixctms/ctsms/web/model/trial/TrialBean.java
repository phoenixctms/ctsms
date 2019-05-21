package org.phoenixctms.ctsms.web.model.trial;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.DBModule;
import org.phoenixctms.ctsms.enumeration.FileModule;
import org.phoenixctms.ctsms.enumeration.HyperlinkModule;
import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.enumeration.RandomizationMode;
import org.phoenixctms.ctsms.enumeration.TrialStatusAction;
import org.phoenixctms.ctsms.enumeration.VariablePeriod;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.js.JsUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.DepartmentVO;
import org.phoenixctms.ctsms.vo.RandomizationModeVO;
import org.phoenixctms.ctsms.vo.SignatureVO;
import org.phoenixctms.ctsms.vo.SponsoringTypeVO;
import org.phoenixctms.ctsms.vo.SurveyStatusTypeVO;
import org.phoenixctms.ctsms.vo.TrialInVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.vo.TrialRandomizationListVO;
import org.phoenixctms.ctsms.vo.TrialStatusActionVO;
import org.phoenixctms.ctsms.vo.TrialStatusTypeVO;
import org.phoenixctms.ctsms.vo.TrialTypeVO;
import org.phoenixctms.ctsms.vo.UserOutVO;
import org.phoenixctms.ctsms.vo.VariablePeriodVO;
import org.phoenixctms.ctsms.web.model.RandomizationModeSelector;
import org.phoenixctms.ctsms.web.model.RandomizationModeSelectorListener;
import org.phoenixctms.ctsms.web.model.VariablePeriodSelector;
import org.phoenixctms.ctsms.web.model.VariablePeriodSelectorListener;
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
public class TrialBean extends GenerateRandomListBean implements VariablePeriodSelectorListener, RandomizationModeSelectorListener {

	private static final int BLOCKING_PERIOD_PROPERTY_ID = 1;
	private static final int RANDOMIZATION_MODE_PROPERTY_ID = 1;

	public static void copyTrialOutToIn(TrialInVO in, TrialOutVO out) {
		if (in != null && out != null) {
			DepartmentVO departmentVO = out.getDepartment();
			TrialStatusTypeVO statusVO = out.getStatus();
			TrialTypeVO typeVO = out.getType();
			SponsoringTypeVO sponsoringVO = out.getSponsoring();
			SurveyStatusTypeVO surveyStatusVO = out.getSurveyStatus();
			VariablePeriodVO blockingPeriodVO = out.getBlockingPeriod();
			RandomizationModeVO randomizationVO = out.getRandomization();
			in.setDepartmentId(departmentVO == null ? null : departmentVO.getId());
			in.setDescription(out.getDescription());
			in.setSignupProbandList(out.getSignupProbandList());
			in.setSignupInquiries(out.getSignupInquiries());
			in.setSignupRandomize(out.getSignupRandomize());
			in.setSignupDescription(out.getSignupDescription());
			in.setId(out.getId());
			in.setName(out.getName());
			in.setStatusId(statusVO == null ? null : statusVO.getId());
			in.setTitle(out.getTitle());
			in.setVersion(out.getVersion());
			in.setTypeId(typeVO == null ? null : typeVO.getId());
			in.setSponsoringId(sponsoringVO == null ? null : sponsoringVO.getId());
			in.setSurveyStatusId(surveyStatusVO == null ? null : surveyStatusVO.getId());
			in.setExclusiveProbands(out.getExclusiveProbands());
			in.setProbandAliasFormat(out.getProbandAliasFormat());
			in.setBlockingPeriod(blockingPeriodVO == null ? null : blockingPeriodVO.getPeriod());
			in.setBlockingPeriodDays(out.getBlockingPeriodDays());
			in.setDutySelfAllocationLocked(out.getDutySelfAllocationLocked());
			in.setDutySelfAllocationLockedUntil(out.getDutySelfAllocationLockedUntil());
			in.setDutySelfAllocationLockedFrom(out.getDutySelfAllocationLockedFrom());
			in.setRandomization(randomizationVO == null ? null : randomizationVO.getMode());
			in.setRandomizationList(null); // out.getRandomizationList());
		}
	}

	public static void initTrialDefaultValues(TrialInVO in, UserOutVO user) {
		if (in != null) {
			in.setDepartmentId(user == null ? null : user.getDepartment().getId());
			in.setDescription(Messages.getString(MessageCodes.TRIAL_DESCRIPTION_PRESET));
			in.setSignupProbandList(Settings.getBoolean(SettingCodes.TRIAL_SIGNUP_PROBAND_LIST_PRESET, Bundle.SETTINGS, DefaultSettings.TRIAL_SIGNUP_PROBAND_LIST_PRESET));
			in.setSignupInquiries(Settings.getBoolean(SettingCodes.TRIAL_SIGNUP_INQUIRIES_PRESET, Bundle.SETTINGS, DefaultSettings.TRIAL_SIGNUP_INQUIRIES_PRESET));
			in.setSignupRandomize(Settings.getBoolean(SettingCodes.TRIAL_SIGNUP_RANDOMIZE_PRESET, Bundle.SETTINGS, DefaultSettings.TRIAL_SIGNUP_RANDOMIZE_PRESET));
			in.setSignupDescription(Messages.getString(MessageCodes.TRIAL_SIGNUP_DESCRIPTION_PRESET));
			in.setId(null);
			in.setName(Messages.getString(MessageCodes.TRIAL_NAME_PRESET));
			in.setStatusId(null);
			in.setTitle(Messages.getString(MessageCodes.TRIAL_TITLE_PRESET));
			in.setVersion(null);
			in.setTypeId(null);
			in.setSponsoringId(null);
			in.setSurveyStatusId(null);
			in.setProbandAliasFormat(Messages.getString(MessageCodes.TRIAL_PROBAND_ALIAS_FORMAT_PRESET));
			in.setExclusiveProbands(Settings.getBoolean(SettingCodes.TRIAL_EXCLUSIVE_PROBANDS_PRESET, Bundle.SETTINGS, DefaultSettings.TRIAL_EXCLUSIVE_PROBANDS_PRESET));
			in.setBlockingPeriod(Settings.getVariablePeriod(SettingCodes.TRIAL_BLOCKING_PERIOD_PRESET, Bundle.SETTINGS, DefaultSettings.TRIAL_BLOCKING_PERIOD_PRESET));
			in.setBlockingPeriodDays(Settings.getLongNullable(SettingCodes.TRIAL_BLOCKING_PERIOD_DAYS_PRESET, Bundle.SETTINGS, DefaultSettings.TRIAL_BLOCKING_PERIOD_DAYS_PRESET));
			in.setDutySelfAllocationLocked(Settings.getBoolean(SettingCodes.TRIAL_DUTY_SELF_ALLOCATION_LOCKED_PRESET, Bundle.SETTINGS,
					DefaultSettings.TRIAL_DUTY_SELF_ALLOCATION_LOCKED_PRESET));
			in.setDutySelfAllocationLockedUntil(null);
			in.setDutySelfAllocationLockedFrom(null);
			in.setRandomization(Settings.getRandomizationMode(SettingCodes.TRIAL_RANDOMIZATION_PRESET, Bundle.SETTINGS, DefaultSettings.TRIAL_RANDOMIZATION_PRESET));
			in.setRandomizationList(null); // Settings.getString(SettingCodes.TRIAL_RANDOMIZATION_LIST_PRESET, Bundle.SETTINGS, DefaultSettings.TRIAL_RANDOMIZATION_LIST_PRESET));
		}
	}

	private static Boolean statusTypeContainsAction(TrialStatusTypeVO statusType, TrialStatusAction statusAction) {
		if (statusType != null && statusAction != null) {
			Iterator<TrialStatusActionVO> it = statusType.getActions().iterator();
			while (it.hasNext()) {
				TrialStatusAction action = it.next().getAction();
				if (statusAction.equals(action)) {
					return true;
				}
			}
			return false;
		}
		return null;
	}

	private String password;
	private TrialInVO in;
	private TrialOutVO out;
	private SignatureVO signature;
	private ArrayList<SelectItem> statusTypes;
	private ArrayList<SelectItem> departments;
	private ArrayList<SelectItem> trialTypes;
	private ArrayList<SelectItem> sponsoringTypes;
	private ArrayList<SelectItem> surveyStatusTypes;
	private TrialStatusTypeVO trialStatusType;
	private HashMap<String, Long> tabCountMap;
	private HashMap<String, String> tabTitleMap;
	private VariablePeriodSelector blocking;
	private Collection<TrialStatusTypeVO> allStatusTypes;
	private String deferredDeleteReason;
	private RandomizationModeSelector randomizationMode;

	public TrialBean() {
		super();
		tabCountMap = new HashMap<String, Long>();
		tabTitleMap = new HashMap<String, String>();
		allStatusTypes = loadAllTrialStatusTypes();
		setBlocking(new VariablePeriodSelector(this, BLOCKING_PERIOD_PROPERTY_ID));
		setRandomization(new RandomizationModeSelector(this, RANDOMIZATION_MODE_PROPERTY_ID));
	}

	@Override
	public String addAction() {
		TrialInVO backup = new TrialInVO(in);
		// Long idBackup = in.getId();
		// Long versionBackup = in.getVersion();
		in.setId(null);
		in.setVersion(null);
		sanitizeInVals();
		try {
			// if (getTrialStatusTypePasswordRequired()) {
			// WebUtil.testPassword(password);
			// }
			out = WebUtil.getServiceLocator().getTrialService().addTrial(WebUtil.getAuthentication(), in);
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
		} finally {
			password = null;
		}
		return ERROR_OUTCOME;
	}

	@Override
	protected void addGenerateRandomizationListWarnMessage() {
		if (getRandomizationMode() != null) {
			switch (getRandomizationMode()) {
				case GROUP_STRATIFIED:
				case TAG_SELECT_STRATIFIED:
					Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.STRATIFICATION_RANDOMISATION_LIST_USED);
					break;
				default:
					break;
			}
		}
	}

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
		RequestContext requestContext = RequestContext.getCurrentInstance();
		if (requestContext != null) {
			requestContext.addCallbackParam(JSValues.AJAX_WINDOW_TITLE_BASE64.toString(), JsUtil.encodeBase64(getTitle(operationSuccess), false));
			requestContext.addCallbackParam(JSValues.AJAX_WINDOW_NAME.toString(), getWindowName(operationSuccess));
			requestContext.addCallbackParam(JSValues.AJAX_OPERATION_SUCCESS.toString(), operationSuccess);
			requestContext.addCallbackParam(JSValues.AJAX_ROOT_ENTITY_CREATED.toString(), out != null);
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_TRIAL_TAG_TAB_TITLE_BASE64, JSValues.AJAX_TRIAL_TAG_VALUE_COUNT,
					MessageCodes.TRIAL_TAGS_TAB_TITLE, MessageCodes.TRIAL_TAGS_TAB_TITLE_WITH_COUNT, tabCountMap.get(JSValues.AJAX_TRIAL_TAG_VALUE_COUNT.toString()));
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_TEAM_MEMBER_TAB_TITLE_BASE64, JSValues.AJAX_TEAM_MEMBER_COUNT,
					MessageCodes.TEAM_MEMBERS_TAB_TITLE, MessageCodes.TEAM_MEMBERS_TAB_TITLE_WITH_COUNT, tabCountMap.get(JSValues.AJAX_TEAM_MEMBER_COUNT.toString()));
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_TIMELINE_EVENT_TAB_TITLE_BASE64, JSValues.AJAX_TIMELINE_EVENT_COUNT,
					MessageCodes.TIMELINE_EVENTS_TAB_TITLE, MessageCodes.TIMELINE_EVENTS_TAB_TITLE_WITH_COUNT, tabCountMap.get(JSValues.AJAX_TIMELINE_EVENT_COUNT.toString()));
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_TRIAL_INVENTORY_BOOKING_TAB_TITLE_BASE64, JSValues.AJAX_TRIAL_INVENTORY_BOOKING_COUNT,
					MessageCodes.TRIAL_INVENTORY_BOOKINGS_TAB_TITLE, MessageCodes.TRIAL_INVENTORY_BOOKINGS_TAB_TITLE_WITH_COUNT,
					tabCountMap.get(JSValues.AJAX_TRIAL_INVENTORY_BOOKING_COUNT.toString()));
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_VISIT_TAB_TITLE_BASE64, JSValues.AJAX_VISIT_COUNT, MessageCodes.VISITS_TAB_TITLE,
					MessageCodes.VISITS_TAB_TITLE_WITH_COUNT, tabCountMap.get(JSValues.AJAX_VISIT_COUNT.toString()));
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_PROBAND_GROUP_TAB_TITLE_BASE64, JSValues.AJAX_PROBAND_GROUP_COUNT,
					MessageCodes.PROBAND_GROUPS_TAB_TITLE, MessageCodes.PROBAND_GROUPS_TAB_TITLE_WITH_COUNT, tabCountMap.get(JSValues.AJAX_PROBAND_GROUP_COUNT.toString()));
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_VISIT_SCHEDULE_TAB_TITLE_BASE64, JSValues.AJAX_VISIT_SCHEDULE_ITEM_COUNT,
					MessageCodes.VISIT_SCHEDULE_TAB_TITLE, MessageCodes.VISIT_SCHEDULE_TAB_TITLE_WITH_COUNT, tabCountMap.get(JSValues.AJAX_VISIT_SCHEDULE_ITEM_COUNT.toString()));
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_TRIAL_DUTY_ROSTER_TURN_TAB_TITLE_BASE64, JSValues.AJAX_TRIAL_DUTY_ROSTER_TURN_COUNT,
					MessageCodes.TRIAL_DUTY_ROSTER_TURNS_TAB_TITLE, MessageCodes.TRIAL_DUTY_ROSTER_TURNS_TAB_TITLE_WITH_COUNT,
					tabCountMap.get(JSValues.AJAX_TRIAL_DUTY_ROSTER_TURN_COUNT.toString()));
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_PROBAND_LIST_ENTRY_TAG_TAB_TITLE_BASE64, JSValues.AJAX_PROBAND_LIST_ENTRY_TAG_COUNT,
					MessageCodes.PROBAND_LIST_ENTRY_TAGS_TAB_TITLE, MessageCodes.PROBAND_LIST_ENTRY_TAGS_TAB_TITLE_WITH_COUNT,
					tabCountMap.get(JSValues.AJAX_PROBAND_LIST_ENTRY_TAG_COUNT.toString()));
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_STRATIFICATION_RANDOMIZATION_LIST_TAB_TITLE_BASE64,
					JSValues.AJAX_STRATIFICATION_RANDOMIZATION_LIST_COUNT,
					MessageCodes.STRATIFICATION_RANDOMIZATION_LISTS_TAB_TITLE, MessageCodes.STRATIFICATION_RANDOMIZATION_LISTS_TAB_TITLE_WITH_COUNT,
					tabCountMap.get(JSValues.AJAX_STRATIFICATION_RANDOMIZATION_LIST_COUNT.toString()));
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_INQUIRY_TAB_TITLE_BASE64, JSValues.AJAX_INQUIRY_COUNT, MessageCodes.INQUIRIES_TAB_TITLE,
					MessageCodes.INQUIRIES_TAB_TITLE_WITH_COUNT, tabCountMap.get(JSValues.AJAX_INQUIRY_COUNT.toString()));
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_ECRF_TAB_TITLE_BASE64, JSValues.AJAX_ECRF_COUNT,
					MessageCodes.ECRFS_TAB_TITLE, MessageCodes.ECRFS_TAB_TITLE_WITH_COUNT, tabCountMap.get(JSValues.AJAX_ECRF_COUNT.toString()));
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_ECRF_FIELD_TAB_TITLE_BASE64, JSValues.AJAX_ECRF_FIELD_COUNT,
					MessageCodes.ECRF_FIELDS_TAB_TITLE, MessageCodes.ECRF_FIELDS_TAB_TITLE_WITH_COUNT, tabCountMap.get(JSValues.AJAX_ECRF_FIELD_COUNT.toString()));
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_PROBAND_LIST_ENTRY_TAB_TITLE_BASE64, JSValues.AJAX_PROBAND_LIST_ENTRY_COUNT,
					MessageCodes.PROBAND_LIST_TAB_TITLE, MessageCodes.PROBAND_LIST_TAB_TITLE_WITH_COUNT, tabCountMap.get(JSValues.AJAX_PROBAND_LIST_ENTRY_COUNT.toString()));
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_ECRF_FIELD_STATUS_TAB_TITLE_BASE64, JSValues.AJAX_ECRF_FIELD_STATUS_COUNT,
					MessageCodes.ECRF_FIELD_STATUS_TAB_TITLE, MessageCodes.ECRF_FIELD_STATUS_TAB_TITLE_WITH_COUNT,
					tabCountMap.get(JSValues.AJAX_ECRF_FIELD_STATUS_COUNT.toString()));
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_TRIAL_HYPERLINK_TAB_TITLE_BASE64, JSValues.AJAX_TRIAL_HYPERLINK_COUNT,
					MessageCodes.TRIAL_HYPERLINKS_TAB_TITLE, MessageCodes.TRIAL_HYPERLINKS_TAB_TITLE_WITH_COUNT, tabCountMap.get(JSValues.AJAX_TRIAL_HYPERLINK_COUNT.toString()));
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_TRIAL_FILE_TAB_TITLE_BASE64, JSValues.AJAX_TRIAL_FILE_COUNT,
					MessageCodes.TRIAL_FILES_TAB_TITLE, MessageCodes.TRIAL_FILES_TAB_TITLE_WITH_COUNT, tabCountMap.get(JSValues.AJAX_TRIAL_FILE_COUNT.toString()));
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_TRIAL_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_TRIAL_JOURNAL_ENTRY_COUNT,
					MessageCodes.TRIAL_JOURNAL_TAB_TITLE, MessageCodes.TRIAL_JOURNAL_TAB_TITLE_WITH_COUNT, tabCountMap.get(JSValues.AJAX_TRIAL_JOURNAL_ENTRY_COUNT.toString()));
		}
	}

	@Override
	protected String changeAction(Long id) {
		out = null;
		if (id != null) {
			try {
				out = WebUtil.getServiceLocator().getTrialService().getTrial(WebUtil.getAuthentication(), id);
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
				Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			} catch (AuthenticationException e) {
				Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
				WebUtil.publishException(e);
			}
		}
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
			out = WebUtil.getServiceLocator().getTrialService().deleteTrial(WebUtil.getAuthentication(), id,
					Settings.getBoolean(SettingCodes.TRIAL_DEFERRED_DELETE, Bundle.SETTINGS, DefaultSettings.TRIAL_DEFERRED_DELETE),
					false, deferredDeleteReason);
			initIn();
			initSets();
			if (!out.getDeferredDelete()) {
				addOperationSuccessMessage(MessageCodes.DELETE_OPERATION_SUCCESSFUL);
			}
			out = null;
			return DELETE_OUTCOME;
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	public Collection<TrialStatusTypeVO> getAllStatusTypes() {
		return allStatusTypes;
	}

	public VariablePeriodSelector getBlocking() {
		return blocking;
	}

	public String getDeferredDeleteReason() {
		return deferredDeleteReason;
	}

	public ArrayList<SelectItem> getDepartments() {
		return departments;
	}

	public TrialInVO getIn() {
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

	public TrialOutVO getOut() {
		return out;
	}

	public String getPassword() {
		return password;
	}

	@Override
	public VariablePeriod getPeriod(int property) {
		switch (property) {
			case BLOCKING_PERIOD_PROPERTY_ID:
				return this.in.getBlockingPeriod();
			default:
				return VariablePeriodSelectorListener.NO_SELECTION_VARIABLE_PERIOD;
		}
	}

	public RandomizationModeSelector getRandomization() {
		return randomizationMode;
	}

	@Override
	protected String getRandomizationList() {
		return in.getRandomizationList();
	}

	@Override
	protected RandomizationMode getRandomizationMode() {
		return this.in.getRandomization();
	}

	@Override
	public RandomizationMode getRandomizationMode(int property) {
		switch (property) {
			case RANDOMIZATION_MODE_PROPERTY_ID:
				return this.in.getRandomization();
			default:
				return RandomizationModeSelectorListener.NO_SELECTION_RANDOMIZATION_MODE;
		}
	}

	public boolean getShowTerminalStateMessage() {
		if (in.getStatusId() != null) {
			Collection<TrialStatusTypeVO> statusTypeVOs = null;
			try {
				statusTypeVOs = WebUtil.getServiceLocator().getSelectionSetService().getTrialStatusTypeTransitions(WebUtil.getAuthentication(), in.getStatusId());
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
			if (statusTypeVOs != null) {
				if (statusTypeVOs.size() > 1 || (statusTypeVOs.size() == 1 && !statusTypeVOs.iterator().next().getId().equals(in.getStatusId()))) {
					return false;
				} else {
					return true;
				}
			}
		}
		return false;
	}

	public SignatureVO getSignature() {
		return signature;
	}

	public ArrayList<SelectItem> getSponsoringTypes() {
		return sponsoringTypes;
	}

	public ArrayList<SelectItem> getStatusTypes() {
		return statusTypes;
	}

	public ArrayList<SelectItem> getSurveyStatusTypes() {
		return surveyStatusTypes;
	}

	public String getTabTitle(String tab) {
		return tabTitleMap.get(tab);
	}

	@Override
	public String getTitle() {
		return getTitle(WebUtil.getLongParamValue(GetParamNames.TRIAL_ID) == null);
	}

	private String getTitle(boolean operationSuccess) {
		if (out != null) {
			return Messages.getMessage(out.getDeferredDelete() ? MessageCodes.DELETED_TITLE : MessageCodes.TRIAL_TITLE, Long.toString(out.getId()),
					CommonUtil.trialOutVOToString(out));
		} else {
			return Messages.getString(operationSuccess ? MessageCodes.CREATE_NEW_TRIAL : MessageCodes.ERROR_LOADING_TRIAL);
		}
	}

	@Override
	protected Long getTrialId() {
		return this.in.getId();
	}

	public TrialStatusTypeVO getTrialStatusType() {
		return trialStatusType;
	}

	public boolean getTrialStatusTypePasswordRequired() {
		if (Settings.getBoolean(SettingCodes.TRIAL_STATUS_UPDATE_REQUIRES_PASSWORD, Bundle.SETTINGS,
				DefaultSettings.TRIAL_STATUS_UPDATE_REQUIRES_PASSWORD)) {
			return Boolean.TRUE.equals(statusTypeContainsAction(trialStatusType, TrialStatusAction.SIGN_TRIAL));
		} else {
			return false;
		}
	}

	public ArrayList<SelectItem> getTrialTypes() {
		return trialTypes;
	}

	@Override
	public String getWindowName() {
		return getWindowName(WebUtil.getLongParamValue(GetParamNames.TRIAL_ID) == null);
	}

	private String getWindowName(boolean operationSuccess) {
		if (out != null) {
			return String.format(JSValues.TRIAL_ENTITY_WINDOW_NAME.toString(), Long.toString(out.getId()), WebUtil.getWindowNameUniqueToken());
		} else {
			if (operationSuccess) {
				return String.format(JSValues.TRIAL_ENTITY_WINDOW_NAME.toString(), JSValues.NEW_ENTITY_WINDOW_NAME_SUFFIX.toString(), "");
			} else {
				Long trialId = WebUtil.getLongParamValue(GetParamNames.TRIAL_ID);
				if (trialId != null) {
					return String.format(JSValues.TRIAL_ENTITY_WINDOW_NAME.toString(), trialId.toString(), WebUtil.getWindowNameUniqueToken());
				} else {
					return String.format(JSValues.TRIAL_ENTITY_WINDOW_NAME.toString(), JSValues.NEW_ENTITY_WINDOW_NAME_SUFFIX.toString(), "");
				}
			}
		}
	}

	public void handleDutySelfAllocationLockedChange() {
	}

	public void handleStatusTypeChange() {
		loadTrialStatusType();
	}

	@PostConstruct
	private void init() {
		Long id = WebUtil.getLongParamValue(GetParamNames.TRIAL_ID);
		if (id != null) {
			this.load(id);
		} else {
			this.change();
		}
	}

	private void initIn() {
		if (in == null) {
			in = new TrialInVO();
		}
		if (out != null) {
			copyTrialOutToIn(in, out);
		} else {
			initTrialDefaultValues(in, WebUtil.getUser());
		}
	}

	private void initSets() {
		password = null;
		tabCountMap.clear();
		tabTitleMap.clear();
		// PSFVO psf = new PSFVO();
		// psf.setPageSize(0);
		Long count = (out == null ? null : WebUtil.getTrialTagValueCount(in.getId()));
		tabCountMap.put(JSValues.AJAX_TRIAL_TAG_VALUE_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_TRIAL_TAG_VALUE_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.TRIAL_TAGS_TAB_TITLE, MessageCodes.TRIAL_TAGS_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null : WebUtil.getTeamMemberCount(in.getId()));
		tabCountMap.put(JSValues.AJAX_TEAM_MEMBER_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_TEAM_MEMBER_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.TEAM_MEMBERS_TAB_TITLE, MessageCodes.TEAM_MEMBERS_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null : WebUtil.getTimelineEventCount(in.getId()));
		tabCountMap.put(JSValues.AJAX_TIMELINE_EVENT_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_TIMELINE_EVENT_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.TIMELINE_EVENTS_TAB_TITLE, MessageCodes.TIMELINE_EVENTS_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null : WebUtil.getTrialInventoryBookingCount(in.getId()));
		tabCountMap.put(JSValues.AJAX_TRIAL_INVENTORY_BOOKING_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_TRIAL_INVENTORY_BOOKING_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.TRIAL_INVENTORY_BOOKINGS_TAB_TITLE, MessageCodes.TRIAL_INVENTORY_BOOKINGS_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null : WebUtil.getVisitCount(in.getId()));
		tabCountMap.put(JSValues.AJAX_VISIT_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_VISIT_COUNT.toString(), WebUtil.getTabTitleString(MessageCodes.VISITS_TAB_TITLE, MessageCodes.VISITS_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null : WebUtil.getProbandGroupCount(in.getId()));
		tabCountMap.put(JSValues.AJAX_PROBAND_GROUP_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_PROBAND_GROUP_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.PROBAND_GROUPS_TAB_TITLE, MessageCodes.PROBAND_GROUPS_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null : WebUtil.getVisitScheduleItemCount(in.getId(), null));
		tabCountMap.put(JSValues.AJAX_VISIT_SCHEDULE_ITEM_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_VISIT_SCHEDULE_ITEM_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.VISIT_SCHEDULE_TAB_TITLE, MessageCodes.VISIT_SCHEDULE_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null : WebUtil.getTrialDutyRosterCount(in.getId()));
		tabCountMap.put(JSValues.AJAX_TRIAL_DUTY_ROSTER_TURN_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_TRIAL_DUTY_ROSTER_TURN_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.TRIAL_DUTY_ROSTER_TURNS_TAB_TITLE, MessageCodes.TRIAL_DUTY_ROSTER_TURNS_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null : WebUtil.getProbandListEntryTagCount(in.getId()));
		tabCountMap.put(JSValues.AJAX_PROBAND_LIST_ENTRY_TAG_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_PROBAND_LIST_ENTRY_TAG_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.PROBAND_LIST_ENTRY_TAGS_TAB_TITLE, MessageCodes.PROBAND_LIST_ENTRY_TAGS_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null : WebUtil.getStratificationRandomizationListCount(in.getId()));
		tabCountMap.put(JSValues.AJAX_STRATIFICATION_RANDOMIZATION_LIST_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_STRATIFICATION_RANDOMIZATION_LIST_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.STRATIFICATION_RANDOMIZATION_LISTS_TAB_TITLE, MessageCodes.STRATIFICATION_RANDOMIZATION_LISTS_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null : WebUtil.getInquiryCount(in.getId(), null, null));
		tabCountMap.put(JSValues.AJAX_INQUIRY_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_INQUIRY_COUNT.toString(), WebUtil.getTabTitleString(MessageCodes.INQUIRIES_TAB_TITLE, MessageCodes.INQUIRIES_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null : WebUtil.getProbandListEntryCount(in.getId(), null, true));
		tabCountMap.put(JSValues.AJAX_PROBAND_LIST_ENTRY_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_PROBAND_LIST_ENTRY_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.PROBAND_LIST_TAB_TITLE, MessageCodes.PROBAND_LIST_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null : WebUtil.getEcrfCount(in.getId()));
		tabCountMap.put(JSValues.AJAX_ECRF_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_ECRF_COUNT.toString(), WebUtil.getTabTitleString(MessageCodes.ECRFS_TAB_TITLE, MessageCodes.ECRFS_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null : WebUtil.getEcrfFieldCount(in.getId(), null));
		tabCountMap.put(JSValues.AJAX_ECRF_FIELD_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_ECRF_FIELD_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.ECRF_FIELDS_TAB_TITLE, MessageCodes.ECRF_FIELDS_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null
				: WebUtil.getEcrfFieldStatusEntryCount(
						Settings.getEcrfFieldStatusQueue(SettingCodes.ECRF_FIELD_STATUS_QUEUE, Bundle.SETTINGS, DefaultSettings.ECRF_FIELD_STATUS_QUEUE), in.getId(), null, null,
						true));
		tabCountMap.put(JSValues.AJAX_ECRF_FIELD_STATUS_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_ECRF_FIELD_STATUS_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.ECRF_FIELD_STATUS_TAB_TITLE, MessageCodes.ECRF_FIELD_STATUS_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null : WebUtil.getHyperlinkCount(HyperlinkModule.TRIAL_HYPERLINK, in.getId()));
		tabCountMap.put(JSValues.AJAX_TRIAL_HYPERLINK_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_TRIAL_HYPERLINK_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.TRIAL_HYPERLINKS_TAB_TITLE, MessageCodes.TRIAL_HYPERLINKS_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null : WebUtil.getTotalFileCount(FileModule.TRIAL_DOCUMENT, in.getId()));
		tabCountMap.put(JSValues.AJAX_TRIAL_FILE_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_TRIAL_FILE_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.TRIAL_FILES_TAB_TITLE, MessageCodes.TRIAL_FILES_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null : WebUtil.getJournalCount(JournalModule.TRIAL_JOURNAL, in.getId()));
		tabCountMap.put(JSValues.AJAX_TRIAL_JOURNAL_ENTRY_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_TRIAL_JOURNAL_ENTRY_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.TRIAL_JOURNAL_TAB_TITLE, MessageCodes.TRIAL_JOURNAL_TAB_TITLE_WITH_COUNT, count));
		departments = WebUtil.getVisibleDepartments(in.getDepartmentId());
		trialTypes = WebUtil.getVisibleTrialTypes(in.getTypeId());
		sponsoringTypes = WebUtil.getVisibleSponsoringTypes(in.getSponsoringId());
		surveyStatusTypes = WebUtil.getVisibleSurveyStatusTypes(in.getSurveyStatusId());
		Collection<TrialStatusTypeVO> statusTypeVOs = null;
		if (out != null) {
			try {
				statusTypeVOs = WebUtil.getServiceLocator().getSelectionSetService().getTrialStatusTypeTransitions(WebUtil.getAuthentication(), in.getStatusId());
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		} else {
			try {
				statusTypeVOs = WebUtil.getServiceLocator().getSelectionSetService().getInitialTrialStatusTypes(WebUtil.getAuthentication());
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		if (statusTypeVOs != null) {
			statusTypes = new ArrayList<SelectItem>(statusTypeVOs.size());
			Iterator<TrialStatusTypeVO> it = statusTypeVOs.iterator();
			while (it.hasNext()) {
				TrialStatusTypeVO typeVO = it.next();
				statusTypes.add(new SelectItem(typeVO.getId().toString(), typeVO.getName()));
			}
		} else {
			statusTypes = new ArrayList<SelectItem>();
		}
		loadTrialStatusType();
		loadTrialRandomizationList();
		loadSignature();
		deferredDeleteReason = (out == null ? null : out.getDeferredDeleteReason());
		if (out != null && out.isDeferredDelete()) { // && Settings.getBoolean(SettingCodes.TRIAL_DEFERRED_DELETE, Bundle.SETTINGS, DefaultSettings.TRIAL_DEFERRED_DELETE)) {
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.MARKED_FOR_DELETION, deferredDeleteReason);
		}
	}

	public boolean isBlockingPeriodSelectorEnabled() {
		return this.in.isExclusiveProbands();
	}

	public boolean isBlockingPeriodSpinnerEnabled() {
		return (this.in.isExclusiveProbands() && (this.in.getBlockingPeriod() == null || VariablePeriod.EXPLICIT.equals(this.in.getBlockingPeriod())));
	}

	@Override
	public boolean isCreateable() {
		return WebUtil.getModuleEnabled(DBModule.TRIAL_DB);
	}

	@Override
	public boolean isCreated() {
		return out != null;
	}

	public boolean isDeferredDelete() {
		return Settings.getBoolean(SettingCodes.TRIAL_DEFERRED_DELETE, Bundle.SETTINGS, DefaultSettings.TRIAL_DEFERRED_DELETE);
	}

	@Override
	public boolean isEditable() {
		return WebUtil.getModuleEnabled(DBModule.TRIAL_DB) && super.isEditable();
	}

	@Override
	public boolean isRemovable() {
		return WebUtil.getModuleEnabled(DBModule.TRIAL_DB) && super.isRemovable();
	}

	public boolean isSignupRandomizeEnabled() {
		return this.in.getSignupProbandList();
	}

	public boolean isTabEmphasized(String tab) {
		return WebUtil.isTabCountEmphasized(tabCountMap.get(tab));
	}

	@Override
	public String loadAction() {
		return loadAction(in.getId());
	}

	@Override
	public String loadAction(Long id) {
		out = null;
		try {
			out = WebUtil.getServiceLocator().getTrialService().getTrial(WebUtil.getAuthentication(), id);
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

	private Collection<TrialStatusTypeVO> loadAllTrialStatusTypes() {
		// Collection<ECRFStatusTypeVO> statusTypeVOs = null;
		try {
			return WebUtil.getServiceLocator().getSelectionSetService().getAllTrialStatusTypes(WebUtil.getAuthentication());
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
		return new ArrayList<TrialStatusTypeVO>();
		// if (statusTypeVOs != null) {
		// statusTypes = new ArrayList<SelectItem>(statusTypeVOs.size());
		// Iterator<ECRFStatusTypeVO> it = statusTypeVOs.iterator();
		// while (it.hasNext()) {
		// ECRFStatusTypeVO typeVO = it.next();
		// statusTypes.add(new SelectItem(typeVO.getId().toString(), typeVO.getName()));
		// }
		// } else {
		// statusTypes = new ArrayList<SelectItem>();
		// }
	}

	private void loadSignature() {
		signature = null;
		if (in.getId() != null) {
			try {
				signature = WebUtil.getServiceLocator().getTrialService().getTrialSignature(WebUtil.getAuthentication(), in.getId());
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
	}

	private void loadTrialRandomizationList() {
		TrialRandomizationListVO trialRandomizationList = null;
		in.setRandomizationList(Settings.getString(SettingCodes.TRIAL_RANDOMIZATION_LIST_PRESET, Bundle.SETTINGS, DefaultSettings.TRIAL_RANDOMIZATION_LIST_PRESET));
		if (in.getId() != null) {
			try {
				trialRandomizationList = WebUtil.getServiceLocator().getTrialService().getTrialRandomizationList(WebUtil.getAuthentication(), in.getId());
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
			if (trialRandomizationList != null) {
				in.setRandomizationList(trialRandomizationList.getRandomizationList());
			}
		}
	}

	private void loadTrialStatusType() {
		trialStatusType = WebUtil.getTrialStatusType(in.getStatusId());
	}

	@Override
	public String resetAction() {
		out = null;
		initIn();
		initSets();
		return RESET_OUTCOME;
	}

	private void sanitizeInVals() {
		if (!in.getDutySelfAllocationLocked()) {
			in.setDutySelfAllocationLockedUntil(null);
			in.setDutySelfAllocationLockedFrom(null);
		}
		if (in.getRandomization() == null) {
			in.setRandomizationList(null);
		}
		if (!in.getSignupProbandList()) {
			in.setSignupRandomize(false);
		}
		// signup...
	}

	public void setBlocking(VariablePeriodSelector validity) {
		this.blocking = validity;
	}

	public void setDeferredDeleteReason(String deferredDeleteReason) {
		this.deferredDeleteReason = deferredDeleteReason;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public void setPeriod(int property, VariablePeriod period) {
		switch (property) {
			case BLOCKING_PERIOD_PROPERTY_ID:
				this.in.setBlockingPeriod(period);
				break;
			default:
		}
	}

	public void setRandomization(RandomizationModeSelector randomizationMode) {
		this.randomizationMode = randomizationMode;
	}

	@Override
	protected void setRandomizationList(String randomizationList) {
		in.setRandomizationList(randomizationList);
	}

	@Override
	public void setRandomizationMode(int property, RandomizationMode mode) {
		switch (property) {
			case RANDOMIZATION_MODE_PROPERTY_ID:
				this.in.setRandomization(mode);
				break;
			default:
		}
	}

	@Override
	public String updateAction() {
		sanitizeInVals();
		try {
			if (getTrialStatusTypePasswordRequired()) {
				WebUtil.testPassword(password);
			}
			out = WebUtil.getServiceLocator().getTrialService().updateTrial(WebUtil.getAuthentication(), in);
			initIn();
			initSets();
			addOperationSuccessMessage(MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
			return UPDATE_OUTCOME;
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} finally {
			password = null;
		}
		return ERROR_OUTCOME;
	}

	public void verifySignature() {
		try {
			signature = WebUtil.getServiceLocator().getTrialService().verifyTrialSignature(WebUtil.getAuthentication(), in.getId());
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
	}
}
