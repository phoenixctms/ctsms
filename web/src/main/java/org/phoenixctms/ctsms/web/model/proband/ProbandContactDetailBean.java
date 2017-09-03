package org.phoenixctms.ctsms.web.model.proband;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.ContactDetailTypeVO;
import org.phoenixctms.ctsms.vo.ProbandContactDetailValueInVO;
import org.phoenixctms.ctsms.vo.ProbandContactDetailValueOutVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
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
public class ProbandContactDetailBean extends ManagedBeanBase {

	public static void copyContactDetailValueOutToIn(ProbandContactDetailValueInVO in, ProbandContactDetailValueOutVO out) {
		if (in != null && out != null) {
			ContactDetailTypeVO contactDetailTypeVO = out.getType();
			ProbandOutVO probandVO = out.getProband();
			in.setId(out.getId());
			in.setVersion(out.getVersion());
			in.setProbandId(probandVO == null ? null : probandVO.getId());
			in.setTypeId(contactDetailTypeVO == null ? null : contactDetailTypeVO.getId());
			in.setValue(out.getValue());
			in.setNotify(out.getNotify());
			in.setNa(out.getNa());
			in.setComment(out.getComment());
		}
	}

	public static void initContactDetailValueDefaultValues(ProbandContactDetailValueInVO in, Long probandId) {
		if (in != null) {
			in.setId(null);
			in.setVersion(null);
			in.setProbandId(probandId);
			in.setTypeId(null);
			in.setValue(Messages.getString(MessageCodes.PROBAND_CONTACT_DETAIL_VALUE_PRESET));
			in.setNotify(Settings.getBoolean(SettingCodes.PROBAND_CONTACT_NOTIFY_PRESET, Bundle.SETTINGS, DefaultSettings.PROBAND_CONTACT_NOTIFY_PRESET));
			in.setNa(Settings.getBoolean(SettingCodes.PROBAND_CONTACT_NA_PRESET, Bundle.SETTINGS, DefaultSettings.PROBAND_CONTACT_NA_PRESET));
			in.setComment(Messages.getString(MessageCodes.PROBAND_CONTACT_DETAIL_COMMENT_PRESET));
		}
	}

	private ProbandContactDetailValueInVO in;
	private ProbandContactDetailValueOutVO out;
	private Long probandId;
	private ProbandOutVO proband;
	private ArrayList<SelectItem> availableTypes;
	private ProbandContactDetailValueLazyModel contactDetailValueModel;
	private ContactDetailTypeVO contactDetailType;

	public ProbandContactDetailBean() {
		super();
		contactDetailValueModel = new ProbandContactDetailValueLazyModel();
	}

	@Override
	public String addAction()
	{
		ProbandContactDetailValueInVO backup = new ProbandContactDetailValueInVO(in);
		in.setId(null);
		in.setVersion(null);
		sanitizeInVals();
		try {
			out = WebUtil.getServiceLocator().getProbandService().addProbandContactDetailValue(WebUtil.getAuthentication(), in);
			initIn();
			initSets();
			addOperationSuccessMessage(MessageCodes.ADD_OPERATION_SUCCESSFUL);
			return ADD_OUTCOME;
		} catch (ServiceException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (IllegalArgumentException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
		return ERROR_OUTCOME;
	}

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
		RequestContext requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_PROBAND_CONTACT_DETAIL_TAB_TITLE_BASE64,
				JSValues.AJAX_PROBAND_CONTACT_DETAIL_VALUE_COUNT, MessageCodes.PROBAND_CONTACT_DETAILS_TAB_TITLE, MessageCodes.PROBAND_CONTACT_DETAILS_TAB_TITLE_WITH_COUNT,
				new Long(contactDetailValueModel.getRowCount()));
		if (operationSuccess && in.getProbandId() != null) {
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_PROBAND_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_PROBAND_JOURNAL_ENTRY_COUNT,
					MessageCodes.PROBAND_JOURNAL_TAB_TITLE, MessageCodes.PROBAND_JOURNAL_TAB_TITLE_WITH_COUNT,
					WebUtil.getJournalCount(JournalModule.PROBAND_JOURNAL, in.getProbandId()));
		}
	}

	@Override
	protected String changeAction(Long id) {
		LazyDataModelBase.clearFilters("probandcontactdetail_list");
		out = null;
		this.probandId = id;
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
			out = WebUtil.getServiceLocator().getProbandService().deleteProbandContactDetailValue(WebUtil.getAuthentication(), id);
			initIn();
			initSets();
			out = null;
			addOperationSuccessMessage(MessageCodes.DELETE_OPERATION_SUCCESSFUL);
			return DELETE_OUTCOME;
		} catch (ServiceException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
		return ERROR_OUTCOME;
	}

	public ArrayList<SelectItem> getAvailableTypes() {
		return availableTypes;
	}

	public ProbandContactDetailValueLazyModel getContactDetailValueModel() {
		return contactDetailValueModel;
	}

	public ProbandContactDetailValueInVO getIn() {
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

	public ProbandContactDetailValueOutVO getOut() {
		return out;
	}

	public IDVO getSelectedProbandContactDetailValue() {
		if (this.out != null) {
			return IDVO.transformVo(this.out);
		} else {
			return null;
		}
	}

	@Override
	public String getTitle() {
		if (out != null) {
			return Messages.getMessage(MessageCodes.PROBAND_CONTACT_DETAIL_VALUE_TITLE, Long.toString(out.getId()), out.getType().getName());
		} else {
			return Messages.getString(MessageCodes.CREATE_NEW_PROBAND_CONTACT_DETAIL_VALUE);
		}
	}

	public void handleNaChange() {
		if (in.isNa()) {
			in.setValue("");
			in.setNotify(false);
		}
	}

	public void handleTypeChange() {
		loadSelectedType();
		RequestContext requestContext = RequestContext.getCurrentInstance();
		if (requestContext != null && contactDetailType != null) {
			requestContext.addCallbackParam(JSValues.AJAX_CONTACT_DETAIL_TYPE_EMAIL.toString(), contactDetailType.getEmail());
			requestContext.addCallbackParam(JSValues.AJAX_CONTACT_DETAIL_TYPE_PHONE.toString(), contactDetailType.getPhone());
			requestContext.addCallbackParam(JSValues.AJAX_CONTACT_DETAIL_NA.toString(), in.getNa());
			requestContext.addCallbackParam(JSValues.AJAX_CONTACT_DETAIL_TYPE_NOTIFY_PRESET.toString(), contactDetailType.getNotifyPreset());
		}
	}

	@PostConstruct
	private void init() {
		// System.out.println("POSTCONSTRUCT: " + this.toString());
		Long id = WebUtil.getLongParamValue(GetParamNames.PROBAND_CONTACT_DETAIL_VALUE_ID);
		if (id != null) {
			this.load(id);
		} else {
			initIn();
			initSets();
		}
	}

	private void initIn() {
		if (in == null) {
			in = new ProbandContactDetailValueInVO();
		}
		if (out != null) {
			copyContactDetailValueOutToIn(in, out);
			probandId = in.getProbandId();
		} else {
			initContactDetailValueDefaultValues(in, probandId);
		}
	}

	private void initSets() {
		contactDetailValueModel.setProbandId(in.getProbandId());
		contactDetailValueModel.updateRowCount();
		availableTypes = WebUtil.getAvailableProbandContactDetailTypes(this.in.getProbandId(), this.in.getTypeId());
		loadSelectedType();
		proband = WebUtil.getProband(this.in.getProbandId(), null, null, null);
		if (WebUtil.isProbandLocked(proband)) {
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.PROBAND_LOCKED);
		}
	}

	@Override
	public boolean isCreateable() {
		return (in.getProbandId() == null ? false : !WebUtil.isProbandLocked(proband));
	}

	@Override
	public boolean isCreated() {
		return out != null;
	}

	@Override
	public boolean isEditable() {
		return isCreated() && !WebUtil.isProbandLocked(proband);
	}

	public boolean isInputVisible() {
		return isCreated() || !WebUtil.isProbandLocked(proband);
	}

	public boolean isNotifyEnabled() {
		if (contactDetailType != null) {
			return !in.isNa() && (contactDetailType.isEmail() || contactDetailType.isPhone());
		} else {
			return !in.isNa();
		}
	}

	@Override
	public boolean isRemovable() {
		return isCreated() && !WebUtil.isProbandLocked(proband);
	}

	@Override
	public String loadAction() {
		return loadAction(in.getId());
	}

	@Override
	public String loadAction(Long id) {
		out = null;
		try {
			out = WebUtil.getServiceLocator().getProbandService().getProbandContactDetailValue(WebUtil.getAuthentication(), id);
			if (!out.isDecrypted()) {
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_ERROR, MessageCodes.ENCRYPTED_PROBAND_CONTACT_DETAIL_VALUE, Long.toString(out.getId()));
				out = null;
				return ERROR_OUTCOME;
			}
			return LOAD_OUTCOME;
		} catch (ServiceException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} finally {
			initIn();
			initSets();
		}
		return ERROR_OUTCOME;
	}

	private void loadSelectedType() {
		contactDetailType = WebUtil.getContactDetailType(in.getTypeId());
	}

	@Override
	public String resetAction() {
		out = null;
		initIn();
		initSets();
		return RESET_OUTCOME;
	}

	private void sanitizeInVals() {
		if (in.isNa()) {
			in.setValue(null);
			in.setNotify(false);
		} else if (contactDetailType != null && !contactDetailType.isEmail() && !contactDetailType.isPhone()) {
			in.setNotify(false);
		}
	}

	public void setSelectedProbandContactDetailValue(IDVO contactDetailValue) {
		if (contactDetailValue != null) {
			this.out = (ProbandContactDetailValueOutVO) contactDetailValue.getVo();
			this.initIn();
			initSets();
		}
	}

	@Override
	public String updateAction() {
		ProbandContactDetailValueInVO backup = new ProbandContactDetailValueInVO(in);
		sanitizeInVals();
		try {
			out = WebUtil.getServiceLocator().getProbandService().updateProbandContactDetailValue(WebUtil.getAuthentication(), in);
			initIn();
			initSets();
			addOperationSuccessMessage(MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
			return UPDATE_OUTCOME;
		} catch (ServiceException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (IllegalArgumentException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
		return ERROR_OUTCOME;
	}
}