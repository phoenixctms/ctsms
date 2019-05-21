package org.phoenixctms.ctsms.web.model.staff;

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
import org.phoenixctms.ctsms.vo.StaffContactDetailValueInVO;
import org.phoenixctms.ctsms.vo.StaffContactDetailValueOutVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.web.component.datatable.DataTable;
import org.phoenixctms.ctsms.web.model.IDVO;
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
public class StaffContactDetailBean extends ManagedBeanBase {

	public static void copyContactDetailValueOutToIn(StaffContactDetailValueInVO in, StaffContactDetailValueOutVO out) {
		if (in != null && out != null) {
			ContactDetailTypeVO contactDetailTypeVO = out.getType();
			StaffOutVO staffVO = out.getStaff();
			in.setId(out.getId());
			in.setVersion(out.getVersion());
			in.setStaffId(staffVO == null ? null : staffVO.getId());
			in.setTypeId(contactDetailTypeVO == null ? null : contactDetailTypeVO.getId());
			in.setValue(out.getValue());
			in.setNotify(out.getNotify());
			in.setNa(out.getNa());
			in.setComment(out.getComment());
		}
	}

	public static void initContactDetailValueDefaultValues(StaffContactDetailValueInVO in, Long staffId) {
		if (in != null) {
			in.setId(null);
			in.setVersion(null);
			in.setStaffId(staffId);
			in.setTypeId(null);
			in.setValue(Messages.getString(MessageCodes.STAFF_CONTACT_DETAIL_VALUE_PRESET));
			in.setNotify(Settings.getBoolean(SettingCodes.STAFF_CONTACT_NOTIFY_PRESET, Bundle.SETTINGS, DefaultSettings.STAFF_CONTACT_NOTIFY_PRESET));
			in.setNa(Settings.getBoolean(SettingCodes.STAFF_CONTACT_NA_PRESET, Bundle.SETTINGS, DefaultSettings.STAFF_CONTACT_NA_PRESET));
			in.setComment(Messages.getString(MessageCodes.STAFF_CONTACT_DETAIL_COMMENT_PRESET));
		}
	}

	private StaffContactDetailValueInVO in;
	private StaffContactDetailValueOutVO out;
	private Long staffId;
	private ArrayList<SelectItem> availableTypes;
	private StaffContactDetailValueLazyModel contactDetailValueModel;
	private ContactDetailTypeVO contactDetailType;

	public StaffContactDetailBean() {
		super();
		contactDetailValueModel = new StaffContactDetailValueLazyModel();
	}

	@Override
	public String addAction() {
		StaffContactDetailValueInVO backup = new StaffContactDetailValueInVO(in);
		in.setId(null);
		in.setVersion(null);
		sanitizeInVals();
		try {
			out = WebUtil.getServiceLocator().getStaffService().addStaffContactDetailValue(WebUtil.getAuthentication(), in);
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
		RequestContext requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_STAFF_CONTACT_DETAIL_TAB_TITLE_BASE64,
				JSValues.AJAX_STAFF_CONTACT_DETAIL_VALUE_COUNT, MessageCodes.STAFF_CONTACT_DETAILS_TAB_TITLE, MessageCodes.STAFF_CONTACT_DETAILS_TAB_TITLE_WITH_COUNT, new Long(
						contactDetailValueModel.getRowCount()));
		if (operationSuccess && in.getStaffId() != null) {
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_STAFF_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_STAFF_JOURNAL_ENTRY_COUNT,
					MessageCodes.STAFF_JOURNAL_TAB_TITLE, MessageCodes.STAFF_JOURNAL_TAB_TITLE_WITH_COUNT,
					WebUtil.getJournalCount(JournalModule.STAFF_JOURNAL, in.getStaffId()));
		}
	}

	@Override
	protected String changeAction(Long id) {
		DataTable.clearFilters("staffcontactdetail_list");
		out = null;
		this.staffId = id;
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
			out = WebUtil.getServiceLocator().getStaffService().deleteStaffContactDetailValue(WebUtil.getAuthentication(), id);
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

	public StaffContactDetailValueLazyModel getContactDetailValueModel() {
		return contactDetailValueModel;
	}

	public StaffContactDetailValueInVO getIn() {
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

	public StaffContactDetailValueOutVO getOut() {
		return out;
	}

	public IDVO getSelectedStaffContactDetailValue() {
		if (this.out != null) {
			return IDVO.transformVo(this.out);
		} else {
			return null;
		}
	}

	@Override
	public String getTitle() {
		if (out != null) {
			return Messages.getMessage(MessageCodes.STAFF_CONTACT_DETAIL_VALUE_TITLE, Long.toString(out.getId()), out.getType().getName());
		} else {
			return Messages.getString(MessageCodes.CREATE_NEW_STAFF_CONTACT_DETAIL_VALUE);
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
		Long id = WebUtil.getLongParamValue(GetParamNames.STAFF_CONTACT_DETAIL_VALUE_ID);
		if (id != null) {
			this.load(id);
		} else {
			initIn();
			initSets();
		}
	}

	private void initIn() {
		if (in == null) {
			in = new StaffContactDetailValueInVO();
		}
		if (out != null) {
			copyContactDetailValueOutToIn(in, out);
			staffId = in.getStaffId();
		} else {
			initContactDetailValueDefaultValues(in, staffId);
		}
	}

	private void initSets() {
		contactDetailValueModel.setStaffId(in.getStaffId());
		contactDetailValueModel.updateRowCount();
		availableTypes = WebUtil.getAvailableStaffContactDetailTypes(this.in.getStaffId(), this.in.getTypeId());
		loadSelectedType();
	}

	@Override
	public boolean isCreateable() {
		return (in.getStaffId() == null ? false : true);
	}

	@Override
	public boolean isCreated() {
		return out != null;
	}

	public boolean isNotifyEnabled() {
		if (contactDetailType != null) {
			return !in.isNa() && (contactDetailType.isEmail() || contactDetailType.isPhone());
		} else {
			return !in.isNa();
		}
	}

	@Override
	public String loadAction() {
		return loadAction(in.getId());
	}

	@Override
	public String loadAction(Long id) {
		out = null;
		try {
			out = WebUtil.getServiceLocator().getStaffService().getStaffContactDetailValue(WebUtil.getAuthentication(), id);
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

	public void setSelectedStaffContactDetailValue(IDVO contactDetailValue) {
		if (contactDetailValue != null) {
			this.out = (StaffContactDetailValueOutVO) contactDetailValue.getVo();
			this.initIn();
			initSets();
		}
	}

	@Override
	public String updateAction() {
		StaffContactDetailValueInVO backup = new StaffContactDetailValueInVO(in);
		sanitizeInVals();
		try {
			out = WebUtil.getServiceLocator().getStaffService().updateStaffContactDetailValue(WebUtil.getAuthentication(), in);
			initIn();
			initSets();
			addOperationSuccessMessage(MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
			return UPDATE_OUTCOME;
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
}
