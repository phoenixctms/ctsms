package org.phoenixctms.ctsms.web.model.proband;

import java.io.ByteArrayInputStream;
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
import org.phoenixctms.ctsms.vo.ProbandAddressInVO;
import org.phoenixctms.ctsms.vo.ProbandAddressOutVO;
import org.phoenixctms.ctsms.vo.ProbandLetterPDFVO;
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
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean
@ViewScoped
public class ProbandAddressBean extends ManagedBeanBase {

	public static void copyAddressOutToIn(ProbandAddressInVO in, ProbandAddressOutVO out) {
		if (in != null && out != null) {
			AddressTypeVO addressTypeVO = out.getType();
			ProbandOutVO probandVO = out.getProband();
			in.setAfnus(out.getAfnus());
			in.setCareOf(out.getCareOf());
			in.setCityName(out.getCityName());
			in.setCountryName(out.getCountryName());
			in.setDeliver(out.getDeliver());
			in.setDoorNumber(out.getDoorNumber());
			in.setEntrance(out.getEntrance());
			in.setHouseNumber(out.getHouseNumber());
			in.setId(out.getId());
			in.setVersion(out.getVersion());
			in.setProbandId(probandVO == null ? null : probandVO.getId());
			in.setStreetName(out.getStreetName());
			in.setTypeId(addressTypeVO == null ? null : addressTypeVO.getId());
			in.setZipCode(out.getZipCode());
		}
	}

	public static void initAddressDefaultValues(ProbandAddressInVO in, Long probandId) {
		if (in != null) {
			in.setAfnus(Settings.getBoolean(SettingCodes.PROBAND_ADDRESS_AFNUS_PRESET, Bundle.SETTINGS, DefaultSettings.PROBAND_ADDRESS_AFNUS_PRESET));
			in.setCareOf(Messages.getString(MessageCodes.PROBAND_ADDRESS_CARE_OF_PRESET));
			in.setCityName(Messages.getString(MessageCodes.PROBAND_ADDRESS_CITY_NAME_PRESET));
			in.setCountryName(Messages.getString(MessageCodes.PROBAND_ADDRESS_COUNTRY_NAME_PRESET));
			in.setDeliver(Settings.getBoolean(SettingCodes.PROBAND_ADDRESS_DELIVER_PRESET, Bundle.SETTINGS, DefaultSettings.PROBAND_ADDRESS_DELIVER_PRESET));
			in.setDoorNumber(Messages.getString(MessageCodes.PROBAND_ADDRESS_DOOR_NUMBER_PRESET));
			in.setEntrance(Messages.getString(MessageCodes.PROBAND_ADDRESS_ENTRANCE_PRESET));
			in.setHouseNumber(Messages.getString(MessageCodes.PROBAND_ADDRESS_HOUSE_NUMBER_PRESET));
			in.setId(null);
			in.setVersion(null);
			in.setProbandId(probandId);
			in.setStreetName(Messages.getString(MessageCodes.PROBAND_ADDRESS_STREET_NAME_PRESET));
			in.setTypeId(null);
			in.setZipCode(Messages.getString(MessageCodes.PROBAND_ADDRESS_ZIP_CODE_PRESET));
		}
	}

	private ProbandAddressInVO in;
	private ProbandAddressOutVO out;
	private Long probandId;
	private ProbandOutVO proband;
	private ArrayList<SelectItem> availableTypes;
	private ProbandAddressLazyModel addressModel;
	private AddressTypeVO addressType;

	public ProbandAddressBean() {
		super();
		addressModel = new ProbandAddressLazyModel();
	}

	@Override
	public String addAction() {
		ProbandAddressInVO backup = new ProbandAddressInVO(in);
		// Long idBackup = in.getId();
		// Long versionBackup = in.getVersion();
		in.setId(null);
		in.setVersion(null);
		try {
			out = WebUtil.getServiceLocator().getProbandService().addProbandAddress(WebUtil.getAuthentication(), in);
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
		RequestContext requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_PROBAND_ADDRESS_TAB_TITLE_BASE64, JSValues.AJAX_PROBAND_ADDRESS_COUNT,
				MessageCodes.PROBAND_ADDRESSES_TAB_TITLE, MessageCodes.PROBAND_ADDRESSES_TAB_TITLE_WITH_COUNT, new Long(addressModel.getRowCount()));
		if (operationSuccess && in.getProbandId() != null) {
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_PROBAND_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_PROBAND_JOURNAL_ENTRY_COUNT,
					MessageCodes.PROBAND_JOURNAL_TAB_TITLE, MessageCodes.PROBAND_JOURNAL_TAB_TITLE_WITH_COUNT,
					WebUtil.getJournalCount(JournalModule.PROBAND_JOURNAL, in.getProbandId()));
		}
	}

	@Override
	protected String changeAction(Long id) {
		LazyDataModelBase.clearFilters("probandaddress_list");
		out = null;
		this.probandId = id;
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
			out = WebUtil.getServiceLocator().getProbandService().deleteProbandAddress(WebUtil.getAuthentication(), id);
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

	public ProbandAddressLazyModel getAddressModel() {
		return addressModel;
	}

	public ArrayList<SelectItem> getAvailableTypes() {
		return availableTypes;
	}

	public ProbandAddressInVO getIn() {
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

	public ProbandAddressOutVO getOut() {
		return out;
	}

	public StreamedContent getProbandLetterPdfStreamedContent(ProbandAddressOutVO address) throws Exception {
		if (address != null) {
			try {
				ProbandLetterPDFVO probandLetter = WebUtil.getServiceLocator().getProbandService().renderProbandLetterPDF(WebUtil.getAuthentication(), address.getId());
				return new DefaultStreamedContent(new ByteArrayInputStream(probandLetter.getDocumentDatas()), probandLetter.getContentType().getMimeType(),
						probandLetter.getFileName());
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
				throw e;
			} catch (AuthorisationException e) {
				throw e;
			} catch (ServiceException e) {
				throw e;
			} catch (IllegalArgumentException e) {
				throw e;
			}
		}
		return null;
	}

	public StreamedContent getProbandLettersPdfStreamedContent() throws Exception {
		return WebUtil.getProbandLettersPdfStreamedContent(in.getProbandId());
	}

	public IDVO getSelectedProbandAddress() {
		if (this.out != null) {
			return IDVO.transformVo(this.out);
		} else {
			return null;
		}
	}

	@Override
	public String getTitle() {
		if (out != null) {
			return Messages.getMessage(MessageCodes.PROBAND_ADDRESS_TITLE, Long.toString(out.getId()), out.getType().getName());
		} else {
			return Messages.getString(MessageCodes.CREATE_NEW_PROBAND_ADDRESS);
		}
	}

	public boolean getWireTransfer() {
		return out != null ? out.getWireTransfer() : false;
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
		// System.out.println("POSTCONSTRUCT: " + this.toString());
		Long id = WebUtil.getLongParamValue(GetParamNames.PROBAND_ADDRESS_ID);
		if (id != null) {
			this.load(id);
		} else {
			initIn();
			initSets();
		}
	}

	private void initIn() {
		if (in == null) {
			in = new ProbandAddressInVO();
		}
		if (out != null) {
			copyAddressOutToIn(in, out);
			probandId = in.getProbandId();
		} else {
			initAddressDefaultValues(in, probandId);
		}
	}

	private void initSets() {
		addressModel.setProbandId(in.getProbandId());
		addressModel.updateRowCount();
		availableTypes = WebUtil.getAvailableProbandAddressTypes(this.in.getProbandId(), this.in.getTypeId());
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

	public boolean isPerson() {
		return WebUtil.isProbandPerson(proband);
	}

	@Override
	public boolean isRemovable() {
		return isCreated() && !WebUtil.isProbandLocked(proband);
	}

	public boolean isStrict() {
		return Settings.getBoolean(SettingCodes.PROBAND_ADDRESS_STRICT, Bundle.SETTINGS, DefaultSettings.PROBAND_ADDRESS_STRICT);
	}

	@Override
	public String loadAction() {
		return loadAction(in.getId());
	}

	@Override
	public String loadAction(Long id) {
		out = null;
		try {
			out = WebUtil.getServiceLocator().getProbandService().getProbandAddress(WebUtil.getAuthentication(), id);
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
		addressType = WebUtil.getAddressType(in.getTypeId());
	}

	@Override
	public String resetAction() {
		out = null;
		initIn();
		initSets();
		return RESET_OUTCOME;
	}

	public void setSelectedProbandAddress(IDVO probandAddress) {
		if (probandAddress != null) {
			this.out = (ProbandAddressOutVO) probandAddress.getVo();
			this.initIn();
			initSets();
		}
	}

	public void setWireTransfer(boolean wiretransfer) {
	}

	public void setWireTransferAddress() {
		Long probandAddressId = WebUtil.getLongParamValue(GetParamNames.PROBAND_ADDRESS_ID);
		Long version = WebUtil.getLongParamValue(GetParamNames.VERSION);
		if (probandAddressId != null && version != null) {
			try {
				WebUtil.getServiceLocator().getProbandService().setProbandAddressWireTransfer(WebUtil.getAuthentication(), probandAddressId, version);
				initIn();
				initSets();
				addOperationSuccessMessage(MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
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
		}
	}

	@Override
	public String updateAction() {
		try {
			out = WebUtil.getServiceLocator().getProbandService().updateProbandAddress(WebUtil.getAuthentication(), in);
			initIn();
			initSets();
			addOperationSuccessMessage(MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
			return UPDATE_OUTCOME;
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
}
