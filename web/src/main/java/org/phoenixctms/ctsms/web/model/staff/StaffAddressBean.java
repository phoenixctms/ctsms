package org.phoenixctms.ctsms.web.model.staff;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.AddressTypeVO;
import org.phoenixctms.ctsms.vo.StaffAddressInVO;
import org.phoenixctms.ctsms.vo.StaffAddressOutVO;
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
import org.primefaces.event.SelectEvent;

@ManagedBean
@ViewScoped
public class StaffAddressBean extends ManagedBeanBase {

	public static void copyAddressOutToIn(StaffAddressInVO in, StaffAddressOutVO out) {
		if (in != null && out != null) {
			AddressTypeVO addressTypeVO = out.getType();
			StaffOutVO staffVO = out.getStaff();
			in.setAfnus(out.getAfnus());
			in.setCareOf(out.getCareOf());
			in.setCityName(out.getCityName());
			in.setCountryName(out.getCountryName());
			in.setCv(out.getCv());
			in.setDeliver(out.getDeliver());
			in.setDoorNumber(out.getDoorNumber());
			in.setEntrance(out.getEntrance());
			in.setHouseNumber(out.getHouseNumber());
			in.setId(out.getId());
			in.setVersion(out.getVersion());
			in.setStaffId(staffVO == null ? null : staffVO.getId());
			in.setStreetName(out.getStreetName());
			in.setTypeId(addressTypeVO == null ? null : addressTypeVO.getId());
			in.setZipCode(out.getZipCode());
		}
	}

	public static void initAddressDefaultValues(StaffAddressInVO in, Long staffId) {
		if (in != null) {
			in.setAfnus(Settings.getBoolean(SettingCodes.STAFF_ADDRESS_AFNUS_PRESET, Bundle.SETTINGS, DefaultSettings.STAFF_ADDRESS_AFNUS_PRESET));
			in.setCareOf(Messages.getString(MessageCodes.STAFF_ADDRESS_CARE_OF_PRESET));
			in.setCityName(Messages.getString(MessageCodes.STAFF_ADDRESS_CITY_NAME_PRESET));
			in.setCountryName(Messages.getString(MessageCodes.STAFF_ADDRESS_COUNTRY_NAME_PRESET));
			in.setCv(Settings.getBoolean(SettingCodes.STAFF_ADDRESS_CV_PRESET, Bundle.SETTINGS, DefaultSettings.STAFF_ADDRESS_CV_PRESET));
			in.setDeliver(Settings.getBoolean(SettingCodes.STAFF_ADDRESS_DELIVER_PRESET, Bundle.SETTINGS, DefaultSettings.STAFF_ADDRESS_DELIVER_PRESET));
			in.setDoorNumber(Messages.getString(MessageCodes.STAFF_ADDRESS_DOOR_NUMBER_PRESET));
			in.setEntrance(Messages.getString(MessageCodes.STAFF_ADDRESS_ENTRANCE_PRESET));
			in.setHouseNumber(Messages.getString(MessageCodes.STAFF_ADDRESS_HOUSE_NUMBER_PRESET));
			in.setId(null);
			in.setVersion(null);
			in.setStaffId(staffId);
			in.setStreetName(Messages.getString(MessageCodes.STAFF_ADDRESS_STREET_NAME_PRESET));
			in.setTypeId(null);
			in.setZipCode(Messages.getString(MessageCodes.STAFF_ADDRESS_ZIP_CODE_PRESET));
		}
	}

	private StaffAddressInVO in;
	private StaffAddressOutVO out;
	private Long staffId;
	private ArrayList<SelectItem> availableTypes;
	private StaffAddressLazyModel addressModel;
	private AddressTypeVO addressType;

	public StaffAddressBean() {
		super();
		addressModel = new StaffAddressLazyModel();
	}

	@Override
	public String addAction() {
		StaffAddressInVO backup = new StaffAddressInVO(in);
		in.setId(null);
		in.setVersion(null);
		try {
			out = WebUtil.getServiceLocator().getStaffService().addStaffAddress(WebUtil.getAuthentication(), in);
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

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
		RequestContext requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_STAFF_ADDRESS_TAB_TITLE_BASE64, JSValues.AJAX_STAFF_ADDRESS_COUNT,
				MessageCodes.STAFF_ADDRESSES_TAB_TITLE, MessageCodes.STAFF_ADDRESSES_TAB_TITLE_WITH_COUNT, new Long(addressModel.getRowCount()));
		if (operationSuccess && in.getStaffId() != null) {
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_STAFF_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_STAFF_JOURNAL_ENTRY_COUNT,
					MessageCodes.STAFF_JOURNAL_TAB_TITLE, MessageCodes.STAFF_JOURNAL_TAB_TITLE_WITH_COUNT,
					WebUtil.getJournalCount(JournalModule.STAFF_JOURNAL, in.getStaffId()));
		}
	}

	@Override
	protected String changeAction(Long id) {
		DataTable.clearFilters("staffaddress_list");
		out = null;
		this.staffId = id;
		initIn();
		initSets();
		return CHANGE_OUTCOME;
	}

	public List<String> completeCityName(String query) {
		this.in.setCityName(query);
		return WebUtil.completeCityName(this.in.getCountryName(), this.in.getZipCode(), query);
	}

	public List<String> completeCountryName(String query) {
		this.in.setCountryName(query);
		return WebUtil.completeCountryName(this.in.getCountryName());
	}

	public List<String> completeStreetName(String query) {
		this.in.setStreetName(query);
		return WebUtil.completeStreetName(this.in.getCountryName(), this.in.getZipCode(), this.in.getCityName(), query);
	}

	public List<String> completeZipCode(String query) {
		this.in.setZipCode(query);
		return WebUtil.completeZipCode(this.in.getCountryName(), query, this.in.getCityName(), this.in.getStreetName());
	}

	@Override
	public String deleteAction() {
		return deleteAction(in.getId());
	}

	@Override
	public String deleteAction(Long id) {
		try {
			out = WebUtil.getServiceLocator().getStaffService().deleteStaffAddress(WebUtil.getAuthentication(), id);
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

	public StaffAddressLazyModel getAddressModel() {
		return addressModel;
	}

	public ArrayList<SelectItem> getAvailableTypes() {
		return availableTypes;
	}

	public StaffAddressInVO getIn() {
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

	public StaffAddressOutVO getOut() {
		return out;
	}

	public IDVO getSelectedStaffAddress() {
		if (this.out != null) {
			return IDVO.transformVo(this.out);
		} else {
			return null;
		}
	}

	@Override
	public String getTitle() {
		if (out != null) {
			return Messages.getMessage(MessageCodes.STAFF_ADDRESS_TITLE, Long.toString(out.getId()), out.getType().getName());
		} else {
			return Messages.getString(MessageCodes.CREATE_NEW_STAFF_ADDRESS);
		}
	}

	public void handleCityNameSelect(SelectEvent event) {
		in.setCityName((String) event.getObject());
	}

	public void handleCountryNameSelect(SelectEvent event) {
		in.setCountryName((String) event.getObject());
	}

	public void handleStreetNameSelect(SelectEvent event) {
		in.setStreetName((String) event.getObject());
	}

	public void handleTypeChange() {
		loadSelectedType();
		RequestContext requestContext = RequestContext.getCurrentInstance();
		if (requestContext != null && addressType != null) {
			requestContext.addCallbackParam(JSValues.AJAX_ADDRESS_TYPE_DELIVER_PRESET.toString(), addressType.getDeliverPreset());
		}
	}

	public void handleZipCodeSelect(SelectEvent event) {
		in.setZipCode((String) event.getObject());
	}

	@PostConstruct
	private void init() {
		Long id = WebUtil.getLongParamValue(GetParamNames.STAFF_ADDRESS_ID);
		if (id != null) {
			this.load(id);
		} else {
			initIn();
			initSets();
		}
	}

	private void initIn() {
		if (in == null) {
			in = new StaffAddressInVO();
		}
		if (out != null) {
			copyAddressOutToIn(in, out);
			staffId = in.getStaffId();
		} else {
			initAddressDefaultValues(in, staffId);
		}
	}

	private void initSets() {
		addressModel.setStaffId(in.getStaffId());
		addressModel.updateRowCount();
		availableTypes = WebUtil.getAvailableStaffAddressTypes(this.in.getStaffId(), this.in.getTypeId());
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

	public boolean isStrict() {
		return Settings.getBoolean(SettingCodes.STAFF_ADDRESS_STRICT, Bundle.SETTINGS, DefaultSettings.STAFF_ADDRESS_STRICT);
	}

	@Override
	public String loadAction() {
		return loadAction(in.getId());
	}

	@Override
	public String loadAction(Long id) {
		out = null;
		try {
			out = WebUtil.getServiceLocator().getStaffService().getStaffAddress(WebUtil.getAuthentication(), id);
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

	private void loadSelectedType() {
		addressType = WebUtil.getAddressType(in.getTypeId());
	}

	@Override
	public String resetAction() {
		out = null;
		initIn();
		initSets();
		return RESET_OUTCOME;
	}

	public void setSelectedStaffAddress(IDVO staffAddress) {
		if (staffAddress != null) {
			this.out = (StaffAddressOutVO) staffAddress.getVo();
			this.initIn();
			initSets();
		}
	}

	@Override
	public String updateAction() {
		try {
			out = WebUtil.getServiceLocator().getStaffService().updateStaffAddress(WebUtil.getAuthentication(), in);
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
}
