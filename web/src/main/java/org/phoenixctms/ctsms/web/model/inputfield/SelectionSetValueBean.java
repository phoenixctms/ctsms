package org.phoenixctms.ctsms.web.model.inputfield;

import java.io.UnsupportedEncodingException;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.InputFieldOutVO;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueInVO;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueOutVO;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.model.shared.inputfield.InputFieldOutVOConfig;
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
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONException;

@ManagedBean
@ViewScoped
public class SelectionSetValueBean extends ManagedBeanBase {

	public static void copySelectionSetValueOutToIn(InputFieldSelectionSetValueInVO in, InputFieldSelectionSetValueOutVO out) {
		if (in != null && out != null) {
			InputFieldOutVO fieldVO = out.getField();
			in.setFieldId(fieldVO == null ? null : fieldVO.getId());
			in.setId(out.getId());
			in.setLocalized(out.getLocalized());
			in.setName(out.getNameL10nKey());
			in.setPreset(out.getPreset());
			in.setValue(out.getValue());
			in.setVersion(out.getVersion());
			in.setInkRegions(out.getInkRegions());
			in.setStrokesId(out.getStrokesId());
		}
	}

	private final static String getNewStrokesId() {
		return CommonUtil.generateUUID();
	}

	public static void initSelectionSetValueDefaultValues(InputFieldSelectionSetValueInVO in, Long inputFieldId) {
		if (in != null) {
			in.setFieldId(inputFieldId);
			in.setId(null);
			in.setPreset(false);
			in.setVersion(null);
			in.setLocalized(Settings.getBoolean(SettingCodes.INPUT_FIELD_SELECTION_SET_VALUE_LOCALIZED_PRESET, Bundle.SETTINGS,
					DefaultSettings.INPUT_FIELD_SELECTION_SET_VALUE_LOCALIZED_PRESET));
			in.setName(Messages.getString(MessageCodes.INPUT_FIELD_SELECTION_SET_VALUE_NAME_PRESET));
			in.setValue(Messages.getString(MessageCodes.INPUT_FIELD_SELECTION_SET_VALUE_VALUE_PRESET));
			in.setInkRegions(null);
			in.setStrokesId(getNewStrokesId());
		}
	}

	private InputFieldSelectionSetValueInVO in;
	private InputFieldSelectionSetValueOutVO out;
	private Long inputFieldId;
	private InputFieldOutVO inputField;
	private InputFieldOutVOConfig config;
	private InputFieldSelectionSetValueLazyModel selectionSetValueModel;
	private String inkRegionsJson;
	private boolean inkRegionsJsonVisible;
	private String deferredDeleteReason;

	public SelectionSetValueBean() {
		super();
		config = new InputFieldOutVOConfig();
		selectionSetValueModel = new InputFieldSelectionSetValueLazyModel();
	}

	@Override
	public String addAction() {
		InputFieldSelectionSetValueInVO backup = new InputFieldSelectionSetValueInVO(in);
		in.setId(null);
		in.setVersion(null);
		sanitizeInVals();
		try {
			out = WebUtil.getServiceLocator().getInputFieldService().addSelectionSetValue(WebUtil.getAuthentication(), in);
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
		RequestContext requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_SELECTION_SET_VALUE_TAB_TITLE_BASE64,
				JSValues.AJAX_SELECTION_SET_VALUE_COUNT, MessageCodes.SELECTION_SET_VALUES_TAB_TITLE, MessageCodes.SELECTION_SET_VALUES_TAB_TITLE_WITH_COUNT, new Long(
						selectionSetValueModel.getRowCount()));
		if (operationSuccess && in.getFieldId() != null) {
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_INPUT_FIELD_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_INPUT_FIELD_JOURNAL_ENTRY_COUNT,
					MessageCodes.INPUT_FIELD_JOURNAL_TAB_TITLE, MessageCodes.INPUT_FIELD_JOURNAL_TAB_TITLE_WITH_COUNT,
					WebUtil.getJournalCount(JournalModule.INPUT_FIELD_JOURNAL, in.getFieldId()));
		}
	}

	@Override
	protected String changeAction(Long id) {
		LazyDataModelBase.clearFilters("selectionsetvalue_list");
		out = null;
		this.inputFieldId = id;
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
			out = WebUtil.getServiceLocator().getInputFieldService().deleteSelectionSetValue(WebUtil.getAuthentication(), id,
					Settings.getBoolean(SettingCodes.SELECTION_SET_VALUE_DEFERRED_DELETE, Bundle.SETTINGS, DefaultSettings.SELECTION_SET_VALUE_DEFERRED_DELETE), false,
					deferredDeleteReason);
			initIn();
			initSets();
			if (!out.getDeferredDelete()) {
				addOperationSuccessMessage(MessageCodes.DELETE_OPERATION_SUCCESSFUL);
			}
			out = null;
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

	public String getDeferredDeleteReason() {
		return deferredDeleteReason;
	}

	public InputFieldSelectionSetValueInVO getIn() {
		return in;
	}

	public String getInkRegions() throws UnsupportedEncodingException {
		return CommonUtil.inkValueToString(in.getInkRegions());
	}

	public String getInkRegionsJson() {
		return inkRegionsJson;
	}

	public InputFieldOutVO getInputField() {
		return inputField;
	}

	public Long getInputFieldValueCount(InputFieldSelectionSetValueOutVO selectionSetValue) {
		if (selectionSetValue != null) {
			try {
				return WebUtil.getServiceLocator().getInputFieldService().getInputFieldValueCount(WebUtil.getAuthentication(), selectionSetValue.getId());
			} catch (ServiceException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			} catch (AuthorisationException e) {
			} catch (IllegalArgumentException e) {
			}
		}
		return null;
	}

	@Override
	public String getModifiedAnnotation() {
		if (out != null) {
			return WebUtil.getModifiedAnnotation(out.getVersion(), out.getModifiedUser(), out.getModifiedTimestamp());
		} else {
			return super.getModifiedAnnotation();
		}
	}

	public InputFieldSelectionSetValueOutVO getOut() {
		return out;
	}

	public Integer getRegionCount(InputFieldSelectionSetValueOutVO selectionSetValue) throws JSONException, UnsupportedEncodingException {
		byte[] inkRegions;
		if (selectionSetValue != null && (inkRegions = selectionSetValue.getInkRegions()) != null && inkRegions.length > 0) {
			return (new JSONArray(CommonUtil.inkValueToString(inkRegions))).length();
		}
		return null;
	}

	public IDVO getSelectedInputFieldSelectionSetValue() {
		if (this.out != null) {
			return IDVO.transformVo(this.out);
		} else {
			return null;
		}
	}

	public InputFieldSelectionSetValueLazyModel getSelectionSetValueModel() {
		return selectionSetValueModel;
	}

	@Override
	public String getTitle() {
		if (out != null) {
			return Messages.getMessage(MessageCodes.INPUT_FIELD_SELECTION_SET_VALUE_TITLE, Long.toString(out.getId()), out.getUniqueName());
		} else {
			return Messages.getString(MessageCodes.CREATE_NEW_INPUT_FIELD_SELECTION_SET_VALUE);
		}
	}

	public void handleJsonToSketch() throws UnsupportedEncodingException {
		setInkRegions(WebUtil.compressJson(inkRegionsJson));
	}

	public void handleSketchToJson() throws UnsupportedEncodingException {
		inkRegionsJson = WebUtil.beautifyJson(getInkRegions());
		inkRegionsJsonVisible = true;
	}

	@PostConstruct
	private void init() {
		Long id = WebUtil.getLongParamValue(GetParamNames.INPUT_FIELD_SELECTION_SET_VALUE_ID);
		if (id != null) {
			this.load(id);
		} else {
			initIn();
			initSets();
		}
	}

	private void initIn() {
		if (in == null) {
			in = new InputFieldSelectionSetValueInVO();
		}
		if (out != null) {
			copySelectionSetValueOutToIn(in, out);
			inputFieldId = in.getFieldId();
		} else {
			initSelectionSetValueDefaultValues(in, inputFieldId);
		}
	}

	private void initSets() {
		selectionSetValueModel.setInputFieldId(in.getFieldId());
		selectionSetValueModel.updateRowCount();
		inputField = WebUtil.getInputField(in.getFieldId());
		config.setInputField(inputField);
		inkRegionsJson = "";
		if (config.isSketch()) {
			try {
				inkRegionsJson = WebUtil.beautifyJson(getInkRegions());
			} catch (UnsupportedEncodingException e) {
			}
		}
		inkRegionsJsonVisible = false;
		if (!config.isSelect()) {
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_INFO, MessageCodes.INPUT_FIELD_NOT_SELECT);
		}
		deferredDeleteReason = (out == null ? null : out.getDeferredDeleteReason());
		if (out != null && out.isDeferredDelete()) { // && Settings.getBoolean(SettingCodes.SELECTION_SET_VALUE_DEFERRED_DELETE, Bundle.SETTINGS,
			// DefaultSettings.SELECTION_SET_VALUE_DEFERRED_DELETE)) {
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.MARKED_FOR_DELETION, deferredDeleteReason);
		}
	}

	public boolean isAutocomplete() {
		return config.isAutocomplete();
	}

	@Override
	public boolean isCreateable() {
		return in.getFieldId() == null ? false : config.isSketch() ? !isCreated() : config.isSelect();
	}

	@Override
	public boolean isCreated() {
		return out != null;
	}

	public boolean isDeferredDelete() {
		return Settings.getBoolean(SettingCodes.SELECTION_SET_VALUE_DEFERRED_DELETE, Bundle.SETTINGS, DefaultSettings.SELECTION_SET_VALUE_DEFERRED_DELETE);
	}

	@Override
	public boolean isEditable() {
		return isCreated() && config.isSelect();
	}

	public boolean isInkRegionsJsonVisible() {
		return inkRegionsJsonVisible;
	}

	public boolean isInputVisible() {
		return isCreated() || config.isSelect();
	}

	@Override
	public boolean isRemovable() {
		return isCreated() && config.isSelect();
	}

	public boolean isSketch() {
		return config.isSketch();
	}

	@Override
	public String loadAction() {
		return loadAction(in.getId());
	}

	@Override
	public String loadAction(Long id) {
		out = null;
		try {
			out = WebUtil.getServiceLocator().getInputFieldService().getSelectionSetValue(WebUtil.getAuthentication(), id);
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

	@Override
	public String resetAction() {
		out = null;
		initIn();
		initSets();
		return RESET_OUTCOME;
	}

	protected void sanitizeInVals() {
		if (in.getValue() == null) {
			in.setValue("");
		}
		if (isSketch()) {
			in.setPreset(false);
		} else {
			in.setStrokesId(null);
			in.setInkRegions(null);
		}
	}

	public void setDeferredDeleteReason(String deferredDeleteReason) {
		this.deferredDeleteReason = deferredDeleteReason;
	}

	public void setInkRegions(String value) throws UnsupportedEncodingException {
		in.setInkRegions(CommonUtil.stringToInkValue(value));
	}

	public void setInkRegionsJson(String inkRegionsJson) {
		this.inkRegionsJson = inkRegionsJson;
	}

	public void setInkRegionsJsonVisible(boolean inkRegionsJsonVisible) {
		this.inkRegionsJsonVisible = inkRegionsJsonVisible;
	}

	public void setSelectedInputFieldSelectionSetValue(IDVO inputFieldSelectionSet) {
		if (inputFieldSelectionSet != null) {
			this.out = (InputFieldSelectionSetValueOutVO) inputFieldSelectionSet.getVo();
			this.initIn();
			initSets();
		}
	}

	@Override
	public String updateAction() {
		InputFieldSelectionSetValueInVO backup = new InputFieldSelectionSetValueInVO(in);
		sanitizeInVals();
		try {
			out = WebUtil.getServiceLocator().getInputFieldService().updateSelectionSetValue(WebUtil.getAuthentication(), in);
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
