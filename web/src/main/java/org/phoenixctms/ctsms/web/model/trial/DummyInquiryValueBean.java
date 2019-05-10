package org.phoenixctms.ctsms.web.model.trial;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.InquiryValueInVO;
import org.phoenixctms.ctsms.vo.ProbandAddressOutVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.web.model.shared.InquiryValueBeanBase;
import org.phoenixctms.ctsms.web.model.shared.inputfield.InquiryDummyInputModelList;
import org.phoenixctms.ctsms.web.model.shared.inputfield.InquiryInputModelList;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;

@ManagedBean
@ViewScoped
public class DummyInquiryValueBean extends InquiryValueBeanBase {

	private final static boolean SIGNUP_FORM = true;

	public DummyInquiryValueBean() {
		super();
		paginator.setShowPagesMessage(false);
	}

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
		appendRequestContextCallbackInputModelValuesOutArgs(operationSuccess);
	}

	@Override
	protected String changeAction(Long id) {
		inquiryValuesOut = null;
		jsInquiryValuesOut = null;
		this.trialId = id;
		paginator.initPages(true, trialId);
		initIn(true, false);
		initSets();
		return CHANGE_OUTCOME;
	}

	public void checkInput() {
		actionPostProcess(checkInputAction());
	}

	public String checkInputAction() {
		try {
			portionInquiryValues(WebUtil.getServiceLocator().getTrialService().checkInquiryValues(WebUtil.getAuthentication(), new HashSet<InquiryValueInVO>(inquiryValuesIn)));
			initIn(false, false);
			initSets();
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_INFO, MessageCodes.DUMMY_INQUIRY_VALUE_INPUT_VALID);
			return VALID_OUTCOME;
		} catch (ServiceException e) {
			setInputModelErrorMsgs(e.getData());
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

	@Override
	protected int getDefaultFieldsPerRow() {
		return Settings.getInt(SettingCodes.DUMMY_INQUIRY_VALUES_DEFAULT_FIELDS_PER_ROW, Bundle.SETTINGS, DefaultSettings.DUMMY_INQUIRY_VALUES_DEFAULT_FIELDS_PER_ROW);
	}

	@Override
	protected int getDefaultPageSize() {
		return Settings.getInt(SettingCodes.DUMMY_INQUIRY_VALUES_DEFAULT_PAGE_SIZE, Bundle.SETTINGS, DefaultSettings.DUMMY_INQUIRY_VALUES_DEFAULT_PAGE_SIZE);
	}

	@Override
	protected InquiryInputModelList getInquiryInputModelList(
			ArrayList<InquiryValueInVO> values) {
		return new InquiryDummyInputModelList(values);
	}

	@Override
	protected ArrayList<String> getPageSizeStrings() {
		return Settings.getStringList(SettingCodes.DUMMY_INQUIRY_VALUES_PAGE_SIZES, Bundle.SETTINGS, DefaultSettings.DUMMY_INQUIRY_VALUES_PAGE_SIZES);
	}

	@Override
	protected ProbandOutVO getProband() {
		return null;
	}

	@Override
	protected Collection<ProbandAddressOutVO> getProbandAddresses() {
		return null;
	}

	@PostConstruct
	private void init() {
		initIn(true, false);
		initSets();
	}

	@Override
	protected void initIn(boolean loadAllJsValues, boolean keepInquiryValuesIn) {
		if (inquiryValuesIn == null) {
			inquiryValuesIn = new ArrayList<InquiryValueInVO>();
		}
		if (trialId == null) {
			inquiryValuesIn.clear();
			if (inquiryValuesOut != null || jsInquiryValuesOut != null) {
				inquiryValuesOut.clear();
				jsInquiryValuesOut.clear();
			}
		} else {
			if (inquiryValuesOut == null || jsInquiryValuesOut == null) {
				try {
					portionInquiryValues(isSignup() ? WebUtil.getServiceLocator().getTrialService()
							.getInquiryPresetValues(WebUtil.getAuthentication(), trialId, null, true, true, loadAllJsValues, paginator.getPsf())
							: WebUtil.getServiceLocator().getTrialService()
									.getInquiryPresetValues(WebUtil.getAuthentication(), trialId, true, null, true, loadAllJsValues, paginator.getPsf()));
				} catch (ServiceException e) {
				} catch (AuthenticationException e) {
					WebUtil.publishException(e);
				} catch (AuthorisationException e) {
				} catch (IllegalArgumentException e) {
				}
			}
			copyInquiryValuesOutToIn(inquiryValuesIn, inquiryValuesOut, keepInquiryValuesIn);
		}
	}

	@Override
	protected void initSpecificSets() {
	}

	@Override
	public boolean isCreated() {
		return trial != null && inquiryValuesIn.size() > 0;
	}

	@Override
	public boolean isEditable() {
		return false;
	}

	@Override
	protected boolean isSignup() {
		return SIGNUP_FORM;
	}

	@Override
	public String loadAction() {
		return loadAction(this.trialId);
	}

	@Override
	public String loadAction(Long id) {
		inquiryValuesOut = null;
		jsInquiryValuesOut = null;
		this.trialId = id;
		initIn(true, false);
		initSets();
		return LOAD_OUTCOME;
	}

	@Override
	public String resetAction() {
		inquiryValuesOut = null;
		jsInquiryValuesOut = null;
		initIn(true, false);
		initSets();
		return RESET_OUTCOME;
	}
}