package org.phoenixctms.ctsms.web.model.inputfield;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.InputFieldOutVO;
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
	private String locale; // the language to preview the input field in
	private ArrayList<SelectItem> locales;

	public DummyInputFieldValueBean() {
		super();
		inputModel = new DummyInputModel();
		locale = CommonUtil.localeToString(WebUtil.getLocale());
	}

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
		RequestContext requestContext = RequestContext.getCurrentInstance();
		if (operationSuccess && inputFieldId != null) {
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_SELECTION_SET_VALUE_TAB_TITLE_BASE64, JSValues.AJAX_SELECTION_SET_VALUE_COUNT,
					MessageCodes.SELECTION_SET_VALUES_TAB_TITLE, MessageCodes.SELECTION_SET_VALUES_TAB_TITLE_WITH_COUNT, WebUtil.getSelectionSetValueCount(inputFieldId));
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_INPUT_FIELD_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_INPUT_FIELD_JOURNAL_ENTRY_COUNT,
					MessageCodes.INPUT_FIELD_JOURNAL_TAB_TITLE, MessageCodes.INPUT_FIELD_JOURNAL_TAB_TITLE_WITH_COUNT,
					WebUtil.getJournalCountSafe(JournalModule.INPUT_FIELD_JOURNAL, inputFieldId));
		}
		if (requestContext != null) {
			requestContext.addCallbackParam(JSValues.AJAX_OPERATION_SUCCESS.toString(), operationSuccess);
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
			WebUtil.getServiceLocator().getInputFieldService().checkInputFieldValue(getPreviewAuthentication(), inputModel.getInputFieldValue());
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_INFO, MessageCodes.DUMMY_INPUT_FIELD_INPUT_VALID, inputModel.getValueString());
			return VALID_OUTCOME;
		} catch (ServiceException e) {
			Messages.addFieldInputMessages(Messages.DUMMY_INPUT_FIELD_ID, FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} catch (AuthorisationException | IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
		return ERROR_OUTCOME;
	}

	/**
	 * The authentication of the user, with the language to localize the input field texts
	 * (and validation messages) in. The language is passed only if it is not the user's own one, as the user's own
	 * language may no longer be a supported one (which the authentication rejects as an explicit language).
	 */
	private AuthenticationVO getPreviewAuthentication() {
		AuthenticationVO auth = WebUtil.getAuthentication();
		if (auth != null && !CommonUtil.isEmptyString(locale) && !locale.equals(CommonUtil.localeToString(WebUtil.getLocale()))) {
			auth = new AuthenticationVO(auth);
			auth.setLocale(locale);
		}
		return auth;
	}

	private InputFieldOutVO getInputField() {
		if (inputFieldId != null) {
			try {
				return WebUtil.getServiceLocator().getInputFieldService().getInputField(getPreviewAuthentication(), inputFieldId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return null;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public ArrayList<SelectItem> getLocales() {
		if (locales == null) {
			// English has no texts of its own, it shows the default (root) texts, which are the raw keys
			// where no text exists; so label it as in the localization tab, and list it first:
			locales = new ArrayList<SelectItem>();
			Iterator<SelectItem> it = WebUtil.getLocales().iterator();
			while (it.hasNext()) {
				SelectItem item = it.next();
				if (Locale.ENGLISH.getLanguage().equals(item.getValue())) {
					locales.add(0, new SelectItem(item.getValue(), Messages.getString(MessageCodes.INPUT_FIELD_L10N_ROOT_LOCALE_LABEL)));
				} else {
					locales.add(item);
				}
			}
		}
		return locales;
	}

	public void changeLocale() {
		inputModel.setInputField(getInputField());
		actionPostProcess(CHANGE_OUTCOME);
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
		inputModel.setInputField(getInputField());
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
