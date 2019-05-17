package org.phoenixctms.ctsms.web.model.trial;

import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.ProbandGroupInVO;
import org.phoenixctms.ctsms.vo.ProbandGroupOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.model.shared.ProbandListEntryModel;
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
public class ProbandGroupBean extends ManagedBeanBase {

	public static void copyProbandGroupOutToIn(ProbandGroupInVO in, ProbandGroupOutVO out) {
		if (in != null && out != null) {
			TrialOutVO trialVO = out.getTrial();
			in.setId(out.getId());
			in.setTitle(out.getTitle());
			in.setToken(out.getToken());
			in.setDescription(out.getDescription());
			in.setRandomize(out.getRandomize());
			in.setTrialId(trialVO == null ? null : trialVO.getId());
			in.setVersion(out.getVersion());
		}
	}

	public static void initProbandGroupDefaultValues(ProbandGroupInVO in, Long trialId) {
		if (in != null) {
			in.setId(null);
			in.setTitle(Messages.getString(MessageCodes.PROBAND_GROUP_TITLE_PRESET));
			in.setToken(Messages.getString(MessageCodes.PROBAND_GROUP_TOKEN_PRESET));
			in.setDescription(Messages.getString(MessageCodes.PROBAND_GROUP_DESCRIPTION_PRESET));
			in.setRandomize(Settings.getBoolean(SettingCodes.PROBAND_GROUP_RANDOMIZE_PRESET, Bundle.SETTINGS, DefaultSettings.PROBAND_GROUP_RANDOMIZE_PRESET));
			in.setTrialId(trialId);
			in.setVersion(null);
		}
	}

	private ProbandGroupInVO in;
	private ProbandGroupOutVO out;
	private Long trialId;
	private TrialOutVO trial;
	private ProbandGroupLazyModel probandGroupModel;
	private HashMap<Long, ProbandListEntryModel> probandListEntryModelCache;

	public ProbandGroupBean() {
		super();
		probandListEntryModelCache = new HashMap<Long, ProbandListEntryModel>();
		probandGroupModel = new ProbandGroupLazyModel();
	}

	@Override
	public String addAction() {
		ProbandGroupInVO backup = new ProbandGroupInVO(in);
		// Long idBackup = in.getId();
		// Long versionBackup = in.getVersion();
		in.setId(null);
		in.setVersion(null);
		try {
			out = WebUtil.getServiceLocator().getTrialService().addProbandGroup(WebUtil.getAuthentication(), in);
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
	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
		RequestContext requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_PROBAND_GROUP_TAB_TITLE_BASE64, JSValues.AJAX_PROBAND_GROUP_COUNT,
				MessageCodes.PROBAND_GROUPS_TAB_TITLE, MessageCodes.PROBAND_GROUPS_TAB_TITLE_WITH_COUNT, new Long(probandGroupModel.getRowCount()));
		if (operationSuccess && in.getTrialId() != null) {
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_VISIT_SCHEDULE_TAB_TITLE_BASE64, JSValues.AJAX_VISIT_SCHEDULE_ITEM_COUNT,
					MessageCodes.VISIT_SCHEDULE_TAB_TITLE, MessageCodes.VISIT_SCHEDULE_TAB_TITLE_WITH_COUNT, WebUtil.getVisitScheduleItemCount(in.getTrialId(), null));
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_TRIAL_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_TRIAL_JOURNAL_ENTRY_COUNT,
					MessageCodes.TRIAL_JOURNAL_TAB_TITLE, MessageCodes.TRIAL_JOURNAL_TAB_TITLE_WITH_COUNT,
					WebUtil.getJournalCount(JournalModule.TRIAL_JOURNAL, in.getTrialId()));
		}
	}

	@Override
	protected String changeAction(Long id) {
		LazyDataModelBase.clearFilters("probandgroup_list");
		out = null;
		this.trialId = id;
		initIn();
		initSets();
		return CHANGE_OUTCOME;
	}

	private void clearCaches(boolean select) {
		if (!select) {
			probandListEntryModelCache.clear();
		}
	}

	@Override
	public String deleteAction() {
		return deleteAction(in.getId());
	}

	@Override
	public String deleteAction(Long id) {
		try {
			out = WebUtil.getServiceLocator().getTrialService().deleteProbandGroup(WebUtil.getAuthentication(), id, null);
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

	public ProbandGroupInVO getIn() {
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

	public ProbandGroupOutVO getOut() {
		return out;
	}

	public ProbandGroupLazyModel getProbandGroupModel() {
		return probandGroupModel;
	}

	public ProbandListEntryModel getProbandListEntryModel(ProbandGroupOutVO group) {
		return ProbandListEntryModel.getCachedProbandListEntryModel(group, probandListEntryModelCache,
				Settings.getBoolean(SettingCodes.PROBAND_GROUPS_SHOW_PROBAND_LIST_ENTRY_TAG_COLUMN, Bundle.SETTINGS,
						DefaultSettings.PROBAND_GROUPS_SHOW_PROBAND_LIST_ENTRY_TAG_COLUMN),
				Settings.getBoolean(SettingCodes.PROBAND_GROUPS_SHOW_INQUIRY_COLUMN, Bundle.SETTINGS, DefaultSettings.PROBAND_GROUPS_SHOW_INQUIRY_COLUMN), true);
	}

	public IDVO getSelectedProbandGroup() {
		if (this.out != null) {
			return IDVO.transformVo(this.out);
		} else {
			return null;
		}
	}

	@Override
	public String getTitle() {
		if (out != null) {
			return Messages.getMessage(MessageCodes.PROBAND_GROUP_TITLE, Long.toString(out.getId()), out.getTitle());
		} else {
			return Messages.getString(MessageCodes.CREATE_NEW_PROBAND_GROUP);
		}
	}

	public TrialOutVO getTrial() {
		return trial;
	}

	@PostConstruct
	private void init() {
		Long id = WebUtil.getLongParamValue(GetParamNames.PROBAND_GROUP_ID);
		if (id != null) {
			this.load(id);
		} else {
			initIn();
			initSets();
		}
	}

	private void initIn() {
		if (in == null) {
			in = new ProbandGroupInVO();
		}
		if (out != null) {
			copyProbandGroupOutToIn(in, out);
			trialId = in.getTrialId();
		} else {
			initProbandGroupDefaultValues(in, trialId);
		}
	}

	private void initSets() {
		initSets(false);
	}

	private void initSets(boolean select) {
		clearCaches(select);
		probandGroupModel.setTrialId(in.getTrialId());
		probandGroupModel.updateRowCount();
		trial = WebUtil.getTrial(this.in.getTrialId());
		if (WebUtil.isTrialLocked(trial)) {
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.TRIAL_LOCKED);
		}
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

	@Override
	public String loadAction() {
		return loadAction(in.getId());
	}

	@Override
	public String loadAction(Long id) {
		out = null;
		try {
			out = WebUtil.getServiceLocator().getTrialService().getProbandGroup(WebUtil.getAuthentication(), id);
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

	@Override
	public String resetAction() {
		out = null;
		initIn();
		initSets();
		return RESET_OUTCOME;
	}

	public void setSelectedProbandGroup(IDVO probandGroup) {
		if (probandGroup != null) {
			this.out = (ProbandGroupOutVO) probandGroup.getVo();
			this.initIn();
			initSets(true);
		}
	}

	@Override
	public String updateAction() {
		try {
			out = WebUtil.getServiceLocator().getTrialService().updateProbandGroup(WebUtil.getAuthentication(), in);
			initIn();
			initSets();
			addOperationSuccessMessage(MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
			return UPDATE_OUTCOME;
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}
}
