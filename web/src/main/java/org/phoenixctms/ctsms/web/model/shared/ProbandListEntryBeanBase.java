package org.phoenixctms.ctsms.web.model.shared;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.faces.application.FacesMessage;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.js.JsUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.ProbandGroupOutVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryInVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryOutVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.web.component.datatable.DataTable;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.JSValues;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

public abstract class ProbandListEntryBeanBase extends ManagedBeanBase {

	public static void copyProbandListEntryOutToIn(ProbandListEntryInVO in, ProbandListEntryOutVO out) {
		if (in != null && out != null) {
			TrialOutVO trialVO = out.getTrial();
			ProbandOutVO probandVO = out.getProband();
			ProbandGroupOutVO groupVO = out.getGroup();
			in.setGroupId(groupVO == null ? null : groupVO.getId());
			in.setId(out.getId());
			in.setPosition(out.getPosition());
			in.setProbandId(probandVO == null ? null : probandVO.getId());
			in.setTrialId(trialVO == null ? null : trialVO.getId());
			in.setVersion(out.getVersion());
			in.setRating(out.getRating());
			in.setRatingMax(out.getRatingMax() != null ? out.getRatingMax()
					: Settings.getLongNullable(SettingCodes.PROBAND_LIST_ENTRY_RATING_MAX_PRESET, Bundle.SETTINGS, DefaultSettings.PROBAND_LIST_ENTRY_RATING_MAX_PRESET));
		}
	}

	protected static Long getInitialPosition(Long trialId) {
		Long position = null;
		if (trialId != null) {
			try {
				position = WebUtil.getServiceLocator().getTrialService().getProbandListEntryMaxPosition(WebUtil.getAuthentication(), trialId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return position == null ? CommonUtil.LIST_INITIAL_POSITION : position + 1L;
	}

	protected ProbandListEntryInVO in;
	protected ProbandListEntryOutVO out;
	protected ProbandListEntryModel probandListEntryModel;
	protected TrialOutVO trial;
	protected ProbandOutVO proband;
	private ProbandGroupOutVO group;
	private ProbandListStatusEntryBean probandListStatusEntryBean;
	private ProbandListEntryTagValueBean probandListEntryTagValueBean;

	public ProbandListEntryBeanBase() {
		super();
		probandListEntryModel = createProbandListEntryModel();
		probandListStatusEntryBean = new ProbandListStatusEntryBean();
		probandListEntryTagValueBean = new ProbandListEntryTagValueBean();
	}

	@Override
	public String addAction() {
		return addAction(false, false);
	}

	protected String addAction(boolean randomize, boolean createProband) {
		ProbandListEntryInVO backup = new ProbandListEntryInVO(in);
		in.setId(null);
		in.setVersion(null);
		sanitizeInVals(createProband);
		try {
			out = WebUtil.getServiceLocator().getTrialService().addProbandListEntry(WebUtil.getAuthentication(), createProband, false, randomize, in);
			initIn();
			initSets(false, false, false);
			addOperationSuccessMessage("probandListEntryMessages", MessageCodes.ADD_OPERATION_SUCCESSFUL);
			return ADD_OUTCOME;
		} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
			in.copy(backup);
			Messages.addMessageClientId("probandListEntryMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			in.copy(backup);
			Messages.addMessageClientId("probandListEntryMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	public final void addRandomized() {
		actionPostProcess(addRandomizedAction());
	}

	public String addRandomizedAction() {
		return addAction(true, false);
	}

	@Override
	protected String changeAction(Long id) {
		DataTable.clearFilters(getDataTableId());
		out = null;
		probandListEntryModel.clearSelectedColumns();
		changeSpecific(id);
		initIn();
		initSets(true, false, false);
		return CHANGE_OUTCOME;
	}

	protected abstract void changeSpecific(Long id);

	protected abstract ProbandListEntryModel createProbandListEntryModel();

	@Override
	public String deleteAction() {
		return deleteAction(in.getId());
	}

	@Override
	public String deleteAction(Long id) {
		try {
			out = WebUtil.getServiceLocator().getTrialService().deleteProbandListEntry(WebUtil.getAuthentication(), id);
			initIn();
			initSets(false, true, false);
			out = null;
			addOperationSuccessMessage("probandListEntryMessages", MessageCodes.DELETE_OPERATION_SUCCESSFUL);
			return DELETE_OUTCOME;
		} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
			Messages.addMessageClientId("probandListEntryMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessageClientId("probandListEntryMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	protected abstract String getDataTableId();

	public ProbandListEntryInVO getIn() {
		return in;
	}

	public abstract String getMainTabTitle();

	@Override
	public String getModifiedAnnotation() {
		if (out != null) {
			return WebUtil.getModifiedAnnotation(out.getVersion(), out.getModifiedUser(), out.getModifiedTimestamp());
		} else {
			return super.getModifiedAnnotation();
		}
	}

	public ProbandListEntryOutVO getOut() {
		return out;
	}

	public ProbandListEntryModel getProbandListEntryModel() {
		return probandListEntryModel;
	}

	public ProbandListEntryTagValueBean getProbandListEntryTagValueBean() {
		return probandListEntryTagValueBean;
	}

	public ProbandListStatusEntryBean getProbandListStatusEntryBean() {
		return probandListStatusEntryBean;
	}

	public IDVO getSelectedProbandListEntry() {
		if (this.out != null) {
			return IDVO.transformVo(this.out);
		} else {
			return null;
		}
	}

	public TrialOutVO getTrial() {
		return trial;
	}

	protected abstract void initIn();

	protected void initSets() {
		initSets(false, false, false);
	}

	private void initSets(boolean reset, boolean deleted, boolean select) {
		initSpecificSets(reset, deleted, select);
		loadProbandGroup();
		probandListEntryModel.resetRows();
		probandListEntryModel.updateRowCount();
		probandListStatusEntryBean.changeRootEntity(deleted ? null : in.getId());
		probandListEntryTagValueBean.changeRootEntity(deleted ? null : in.getId());
	}

	protected abstract void initSpecificSets(boolean reset, boolean deleted, boolean select);

	@Override
	public boolean isCreated() {
		return out != null;
	}

	@Override
	public boolean isEditable() {
		return isCreated() && !isTrialLocked() && !isProbandLocked();
	}

	public boolean isInputVisible() {
		return isCreated() || (!isTrialLocked() && !isProbandLocked());
	}

	public boolean isProbandLocked() {
		return WebUtil.isProbandLocked(proband);
	}

	public boolean isRandomization() {
		return (trial != null ? trial.getRandomization() != null : false);
	}

	@Override
	public boolean isRemovable() {
		return isCreated() && !isTrialLocked() && !isProbandLocked();
	}

	public boolean isShowProbandListEntryRating() {
		return Settings.getBoolean(SettingCodes.SHOW_PROBAND_LIST_ENTRY_RATING, Bundle.SETTINGS, DefaultSettings.SHOW_PROBAND_LIST_ENTRY_RATING);
	}

	public boolean isTrialLocked() {
		return WebUtil.isTrialLocked(trial);
	}

	@Override
	public String loadAction() {
		return loadAction(in.getId());
	}

	@Override
	public String loadAction(Long id) {
		out = null;
		try {
			out = WebUtil.getServiceLocator().getTrialService().getProbandListEntry(WebUtil.getAuthentication(), id);
			return LOAD_OUTCOME;
		} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
			Messages.addMessageClientId("probandListEntryMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessageClientId("probandListEntryMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} finally {
			initIn();
			initSets(false, false, false);
		}
		return ERROR_OUTCOME;
	}

	@Override
	public String resetAction() {
		out = null;
		initIn();
		initSets(true, false, false);
		return RESET_OUTCOME;
	}

	private void sanitizeInVals(boolean createProband) {
		if (group != null) {
			in.setGroupId(group.getId());
		} else {
			in.setGroupId(null);
		}
		if (createProband) {
			in.setProbandId(null);
		}
		if (in.getRatingMax() != null) {
			if (in.getRating() == null) {
				in.setRating(0l);
			}
		} else {
			in.setRating(null);
		}
	}

	public void setSelectedProbandListEntry(IDVO probandListEntry) {
		if (probandListEntry != null) {
			this.out = (ProbandListEntryOutVO) probandListEntry.getVo();
			this.initIn();
			initSets(false, false, true);
			this.probandListEntryTagValueBean.appendRequestContextCallbackArgs(true);
			RequestContext requestContext = RequestContext.getCurrentInstance();
			if (requestContext != null) {
				requestContext.addCallbackParam(JSValues.AJAX_PROBAND_LIST_TAB_TITLE_BASE64.toString(), JsUtil.encodeBase64(getMainTabTitle(), false));
			}
		}
	}

	@Override
	public String updateAction() {
		return updateAction(false);
	}

	private String updateAction(boolean randomize) {
		ProbandListEntryInVO backup = new ProbandListEntryInVO(in);
		sanitizeInVals(false);
		try {
			out = WebUtil.getServiceLocator().getTrialService().updateProbandListEntry(WebUtil.getAuthentication(), in, null, randomize);
			initIn();
			initSets(false, false, false);
			addOperationSuccessMessage("probandListEntryMessages", MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
			return UPDATE_OUTCOME;
		} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
			in.copy(backup);
			Messages.addMessageClientId("probandListEntryMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			in.copy(backup);
			Messages.addMessageClientId("probandListEntryMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
		return ERROR_OUTCOME;
	}

	public final void updateRandomized() {
		actionPostProcess(updateRandomizedAction());
	}

	public String updateRandomizedAction() {
		return updateAction(true);
	}

	public List<IDVO> completeGroup(String query) {
		if (trial != null) {
			try {
				Collection probandGroupVOs = WebUtil.getServiceLocator().getToolsService().completeProbandGroup(WebUtil.getAuthentication(), query, query, trial.getId(), null);
				IDVO.transformVoCollection(probandGroupVOs);
				return (List<IDVO>) probandGroupVOs;
			} catch (ClassCastException e) {
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return new ArrayList<IDVO>();
	}

	public IDVO getGroup() {
		if (group != null) {
			return IDVO.transformVo(group);
		}
		return null;
	}

	public void setGroup(IDVO group) {
		if (group != null) {
			this.group = (ProbandGroupOutVO) group.getVo();
		} else {
			this.group = null;
		}
	}

	public void handleGroupSelect(SelectEvent event) {
	}

	public void handleGroupUnselect(UnselectEvent event) {
	}

	private void loadProbandGroup() {
		group = WebUtil.getProbandGroup(in.getGroupId());
	}
}
