package org.phoenixctms.ctsms.web.model.trial;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.vo.VisitInVO;
import org.phoenixctms.ctsms.vo.VisitOutVO;
import org.phoenixctms.ctsms.vo.VisitTypeVO;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
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
public class VisitBean extends ManagedBeanBase {

	public static void copyVisitOutToIn(VisitInVO in, VisitOutVO out) {
		if (in != null && out != null) {
			VisitTypeVO visitTypeVO = out.getType();
			TrialOutVO trialVO = out.getTrial();
			in.setDescription(out.getDescription());
			in.setId(out.getId());
			in.setReimbursement(out.getReimbursement());
			in.setTitle(out.getTitle());
			in.setToken(out.getToken());
			in.setTrialId(trialVO == null ? null : trialVO.getId());
			in.setTypeId(visitTypeVO == null ? null : visitTypeVO.getId());
			in.setVersion(out.getVersion());
		}
	}

	public static void initVisitDefaultValues(VisitInVO in, Long trialId) {
		if (in != null) {
			in.setDescription(Messages.getString(MessageCodes.VISIT_DESCRIPTION_PRESET));
			in.setId(null);
			in.setReimbursement(Settings.getFloatNullable(SettingCodes.VISIT_REIMBURSEMENT_PRESET, Bundle.SETTINGS, DefaultSettings.VISIT_REIMBURSEMENT_PRESET));
			in.setTitle(Messages.getString(MessageCodes.VISIT_TITLE_PRESET));
			in.setToken(Messages.getString(MessageCodes.VISIT_TOKEN_PRESET));
			in.setTrialId(trialId);
			in.setTypeId(null);
			in.setVersion(null);
		}
	}

	private VisitInVO in;
	private VisitOutVO out;
	private Long trialId;
	private TrialOutVO trial;
	private ArrayList<SelectItem> availableTypes;
	private VisitLazyModel visitModel;
	private VisitTypeVO visitType;

	public VisitBean() {
		super();
		visitModel = new VisitLazyModel();
	}

	@Override
	public String addAction() {
		VisitInVO backup = new VisitInVO(in);
		// Long idBackup = in.getId();
		// Long versionBackup = in.getVersion();
		in.setId(null);
		in.setVersion(null);
		try {
			out = WebUtil.getServiceLocator().getTrialService().addVisit(WebUtil.getAuthentication(), in);
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
		RequestContext requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_VISIT_TAB_TITLE_BASE64, JSValues.AJAX_VISIT_COUNT,
				MessageCodes.VISITS_TAB_TITLE, MessageCodes.VISITS_TAB_TITLE_WITH_COUNT, new Long(visitModel.getRowCount()));
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
		LazyDataModelBase.clearFilters("visit_list");
		out = null;
		this.trialId = id;
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
			out = WebUtil.getServiceLocator().getTrialService().deleteVisit(WebUtil.getAuthentication(), id);
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

	public ArrayList<SelectItem> getAvailableTypes() {
		return availableTypes;
	}

	public VisitInVO getIn() {
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

	public VisitOutVO getOut() {
		return out;
	}

	public IDVO getSelectedVisit() {
		if (this.out != null) {
			return IDVO.transformVo(this.out);
		} else {
			return null;
		}
	}

	public boolean getShowVisitTypeTravelMessage() {
		return (visitType != null ? visitType.isTravel() : false);
	}

	@Override
	public String getTitle() {
		if (out != null) {
			return Messages.getMessage(MessageCodes.VISIT_TITLE, Long.toString(out.getId()), out.getTitle());
		} else {
			return Messages.getString(MessageCodes.CREATE_NEW_VISIT);
		}
	}

	public VisitLazyModel getVisitModel() {
		return visitModel;
	}

	public void handleTypeChange() {
		loadSelectedType();
	}

	@PostConstruct
	private void init() {
		Long id = WebUtil.getLongParamValue(GetParamNames.VISIT_ID);
		if (id != null) {
			this.load(id);
		} else {
			initIn();
			initSets();
		}
	}

	private void initIn() {
		if (in == null) {
			in = new VisitInVO();
		}
		if (out != null) {
			copyVisitOutToIn(in, out);
			trialId = in.getTrialId();
		} else {
			initVisitDefaultValues(in, trialId);
		}
	}

	private void initSets() {
		visitModel.setTrialId(in.getTrialId());
		visitModel.updateRowCount();
		Collection<VisitTypeVO> typeVOs = null;
		try {
			typeVOs = WebUtil.getServiceLocator().getSelectionSetService().getAvailableVisitTypes(WebUtil.getAuthentication(), this.in.getTrialId(), this.in.getTypeId());
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
		if (typeVOs != null) {
			availableTypes = new ArrayList<SelectItem>(typeVOs.size());
			Iterator<VisitTypeVO> it = typeVOs.iterator();
			while (it.hasNext()) {
				VisitTypeVO typeVO = it.next();
				availableTypes.add(new SelectItem(typeVO.getId().toString(), typeVO.getName()));
			}
		} else {
			availableTypes = new ArrayList<SelectItem>();
		}
		trial = WebUtil.getTrial(this.in.getTrialId());
		loadSelectedType();
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
			out = WebUtil.getServiceLocator().getTrialService().getVisit(WebUtil.getAuthentication(), id);
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

	private void loadSelectedType() {
		visitType = WebUtil.getVisitType(in.getTypeId());
	}

	@Override
	public String resetAction() {
		out = null;
		initIn();
		initSets();
		return RESET_OUTCOME;
	}

	public void setSelectedVisit(IDVO visit) {
		if (visit != null) {
			this.out = (VisitOutVO) visit.getVo();
			this.initIn();
			initSets();
		}
	}

	@Override
	public String updateAction() {
		try {
			out = WebUtil.getServiceLocator().getTrialService().updateVisit(WebUtil.getAuthentication(), in);
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
