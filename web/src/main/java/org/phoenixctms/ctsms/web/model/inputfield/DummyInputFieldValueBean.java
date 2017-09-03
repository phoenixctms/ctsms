package org.phoenixctms.ctsms.web.model.inputfield;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.model.shared.inputfield.DummyInputModel;
import org.phoenixctms.ctsms.web.model.shared.inputfield.InputModel;
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
public class DummyInputFieldValueBean extends ManagedBeanBase {

	private DummyInputModel inputModel;
	private Long inputFieldId;

	public DummyInputFieldValueBean() {
		super();
		inputModel = new DummyInputModel();
	}

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
		RequestContext requestContext = RequestContext.getCurrentInstance();
		if (operationSuccess && inputFieldId != null) {
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_SELECTION_SET_VALUE_TAB_TITLE_BASE64, JSValues.AJAX_SELECTION_SET_VALUE_COUNT,
					MessageCodes.SELECTION_SET_VALUES_TAB_TITLE, MessageCodes.SELECTION_SET_VALUES_TAB_TITLE_WITH_COUNT, WebUtil.getSelectionSetValueCount(inputFieldId));
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_INPUT_FIELD_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_INPUT_FIELD_JOURNAL_ENTRY_COUNT,
					MessageCodes.INPUT_FIELD_JOURNAL_TAB_TITLE, MessageCodes.INPUT_FIELD_JOURNAL_TAB_TITLE_WITH_COUNT,
					WebUtil.getJournalCount(JournalModule.INPUT_FIELD_JOURNAL, inputFieldId));
		}
		if (requestContext != null) {
			requestContext.addCallbackParam(JSValues.AJAX_OPERATION_SUCCESS.toString(), operationSuccess);
			// requestContext.addCallbackParam(JSValues.AJAX_INPUT_FIELD_VARIABLE_VALUES_BASE64.toString(),
			// JsUtil.encodeBase64(JsUtil.inputFieldVariableValueToJson(new ArrayList()), false));
		}
	}

	@Override
	protected String changeAction(Long id) {
		this.inputFieldId = id;
		initIn();
		initSets();
		return CHANGE_OUTCOME;
	}

	public void checkInput() {
		actionPostProcess(checkInputAction());
	}

	public String checkInputAction() {
		try {
			WebUtil.getServiceLocator().getInputFieldService().checkInputFieldValue(WebUtil.getAuthentication(), inputModel.getInputFieldValue());
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_INFO, MessageCodes.DUMMY_INPUT_FIELD_INPUT_VALID, inputModel.getValueString());
			return VALID_OUTCOME;
		} catch (ServiceException e) {
			Messages.addFieldInputMessages(Messages.DUMMY_INPUT_FIELD_ID, FacesMessage.SEVERITY_ERROR, e.getMessage());
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

	public InputModel getInputModel() {
		return inputModel;
	}

	@Override
	public String getTitle() {
		if (inputModel.getInputField() != null) {
			return Messages.getMessage(MessageCodes.DUMMY_INPUT_FIELD_INPUT_TITLE, Long.toString(inputModel.getInputField().getId()),
					CommonUtil.inputFieldOutVOToString(inputModel.getInputField()));
		} else {
			return Messages.getString(MessageCodes.NO_DUMMY_INPUT_FIELD_INPUT);
		}
	}

	@PostConstruct
	private void init() {
		Long id = WebUtil.getLongParamValue(GetParamNames.INPUT_FIELD_ID);
		if (id != null) {
			this.load(id);
		} else {
			initIn();
			initSets();
		}
	}

	private void initIn() {
		inputModel.setInputField(WebUtil.getInputField(inputFieldId));
		inputModel.getInputFieldValue().setFieldId(inputFieldId);
	}

	private void initSets() {
		inputModel.applyPresets();
		inputModel.getInputFieldValue().setOptional(
				Settings.getBoolean(SettingCodes.INPUT_FIELD_DUMMY_OPTIONAL_PRESET, Bundle.SETTINGS, DefaultSettings.INPUT_FIELD_DUMMY_OPTIONAL_PRESET));
		inputModel.setDisabled(Settings.getBoolean(SettingCodes.INPUT_FIELD_DUMMY_DISABLED_PRESET, Bundle.SETTINGS, DefaultSettings.INPUT_FIELD_DUMMY_DISABLED_PRESET));
		if (!isCreated()) {
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_INFO, MessageCodes.INPUT_FIELD_NOT_CREATED);
		}
	}

	@Override
	public boolean isCreateable() {
		return false;
	}

	@Override
	public boolean isCreated() {
		if (inputModel.getInputField() != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean isEditable() {
		return false;
	}

	public boolean isInputVisible() {
		return isCreated();
	}

	@Override
	public boolean isRemovable() {
		return false;
	}

	@Override
	public String loadAction() {
		return loadAction(this.inputFieldId);
	}

	@Override
	public String loadAction(Long id) {
		this.inputFieldId = id;
		initIn();
		initSets();
		return LOAD_OUTCOME;
	}

	@Override
	public String resetAction() {
		initIn();
		initSets();
		return RESET_OUTCOME;
	}
}
