package org.phoenixctms.ctsms.web.model.shared;

import java.util.ArrayList;

import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;

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
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.util.JSValues;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.context.RequestContext;

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
		}
	}

	protected static Long getInitialPosition(Long trialId) {
		Long position = null;
		if (trialId != null) {
			try {
				position = WebUtil.getServiceLocator().getTrialService().getProbandListEntryMaxPosition(WebUtil.getAuthentication(), trialId);
			} catch (ServiceException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			} catch (AuthorisationException e) {
			} catch (IllegalArgumentException e) {
			}
		}
		return position == null ? CommonUtil.LIST_INITIAL_POSITION : position + 1L;
	}

	protected ProbandListEntryInVO in;
	protected ProbandListEntryOutVO out;
	protected ProbandListEntryModel probandListEntryModel;
	protected TrialOutVO trial;
	protected ProbandOutVO proband;
	protected ArrayList<SelectItem> probandGroups;
	private ProbandListStatusEntryBean probandListStatusEntryBean;
	private ProbandListEntryTagValueBean probandListEntryTagValueBean;


	public ProbandListEntryBeanBase() {
		super();
		probandListEntryModel = createProbandListEntryModel();
		probandListStatusEntryBean = new ProbandListStatusEntryBean();
		probandListEntryTagValueBean = new ProbandListEntryTagValueBean();
	}

	@Override
	public String addAction()
	{
		ProbandListEntryInVO backup = new ProbandListEntryInVO(in);
		// Long idBackup = in.getId();
		// Long versionBackup = in.getVersion();
		in.setId(null);
		in.setVersion(null);
		try {
			out = WebUtil.getServiceLocator().getTrialService().addProbandListEntry(WebUtil.getAuthentication(), false, in);
			initIn();
			// initSets(true, false, false);
			initSets(false, false, false);
			addOperationSuccessMessage("probandListEntryMessages", MessageCodes.ADD_OPERATION_SUCCESSFUL);
			return ADD_OUTCOME;
		} catch (ServiceException e) {
			in.copy(backup);
			Messages.addMessageClientId("probandListEntryMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			in.copy(backup);
			Messages.addMessageClientId("probandListEntryMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
			in.copy(backup);
			Messages.addMessageClientId("probandListEntryMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (IllegalArgumentException e) {
			in.copy(backup);
			Messages.addMessageClientId("probandListEntryMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
		return ERROR_OUTCOME;
	}

	@Override
	protected String changeAction(Long id) {
		LazyDataModelBase.clearFilters(getDataTableId());
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
		} catch (ServiceException e) {
			Messages.addMessageClientId("probandListEntryMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessageClientId("probandListEntryMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
			Messages.addMessageClientId("probandListEntryMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (IllegalArgumentException e) {
			Messages.addMessageClientId("probandListEntryMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
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

	public ArrayList<SelectItem> getProbandGroups() {
		return probandGroups;
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

	@Override
	public boolean isRemovable() {
		return isCreated() && !isTrialLocked() && !isProbandLocked();
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
		} catch (ServiceException e) {
			Messages.addMessageClientId("probandListEntryMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessageClientId("probandListEntryMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
			Messages.addMessageClientId("probandListEntryMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (IllegalArgumentException e) {
			Messages.addMessageClientId("probandListEntryMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
		} finally {
			initIn();
			initSets(false, false, false);
		}
		return ERROR_OUTCOME;
	}

	// public void onTabViewChange(TabChangeEvent event) {
	// }

	@Override
	public String resetAction() {
		out = null;
		initIn();
		initSets(true, false, false);
		return RESET_OUTCOME;
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
		try {
			out = WebUtil.getServiceLocator().getTrialService().updateProbandListEntry(WebUtil.getAuthentication(), in, null);
			initIn();
			initSets(false, false, false);
			addOperationSuccessMessage("probandListEntryMessages", MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
			return UPDATE_OUTCOME;
		} catch (ServiceException e) {
			Messages.addMessageClientId("probandListEntryMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessageClientId("probandListEntryMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
			Messages.addMessageClientId("probandListEntryMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (IllegalArgumentException e) {
			Messages.addMessageClientId("probandListEntryMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
		return ERROR_OUTCOME;
	}
}
