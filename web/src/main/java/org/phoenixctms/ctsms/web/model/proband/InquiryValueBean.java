package org.phoenixctms.ctsms.web.model.proband;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
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
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.InquiriesPDFVO;
import org.phoenixctms.ctsms.vo.InquiryValueInVO;
import org.phoenixctms.ctsms.vo.InquiryValueOutVO;
import org.phoenixctms.ctsms.vo.ProbandAddressOutVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.vo.TrialStatusTypeVO;
import org.phoenixctms.ctsms.vo.UserOutVO;
import org.phoenixctms.ctsms.web.model.shared.InquiryValueBeanBase;
import org.phoenixctms.ctsms.web.model.shared.inputfield.InquiryInputModelList;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.JSValues;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean
@ViewScoped
public class InquiryValueBean extends InquiryValueBeanBase {

	private Long probandId;
	private ProbandOutVO proband;
	private Collection<ProbandAddressOutVO> probandAddresses;
	private ArrayList<SelectItem> trials;
	private Long trialsWithoutInquiryValuesCount;
	private Object[] totalCounts;

	public InquiryValueBean() {
		super();
		probandId = null;
		proband = null;
		probandAddresses = null;
		trialsWithoutInquiryValuesCount = null;
		totalCounts = new Object[3];
		trials = new ArrayList<SelectItem>();
	}

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
		RequestContext requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_INQUIRY_VALUE_TAB_TITLE_BASE64, JSValues.AJAX_INQUIRY_VALUE_COUNT,
				MessageCodes.INQUIRY_VALUES_TAB_TITLE, MessageCodes.INQUIRY_VALUES_TAB_TITLE_WITH_COUNT, trialsWithoutInquiryValuesCount, totalCounts);
		if (operationSuccess && probandId != null) {
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_PROBAND_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_PROBAND_JOURNAL_ENTRY_COUNT,
					MessageCodes.PROBAND_JOURNAL_TAB_TITLE, MessageCodes.PROBAND_JOURNAL_TAB_TITLE_WITH_COUNT,
					WebUtil.getJournalCount(JournalModule.PROBAND_JOURNAL, probandId));
		}
		appendRequestContextCallbackInputModelValuesOutArgs(operationSuccess);
	}

	@Override
	protected String changeAction(Long id) {
		inquiryValuesOut = null;
		jsInquiryValuesOut = null;
		this.trialId = null;
		this.probandId = id;
		paginator.initPages(true, trialId);
		initIn(true, false);
		initSets();
		return CHANGE_OUTCOME;
	}

	@Override
	protected int getDefaultFieldsPerRow() {
		return Settings.getInt(SettingCodes.INQUIRY_VALUES_DEFAULT_FIELDS_PER_ROW, Bundle.SETTINGS, DefaultSettings.INQUIRY_VALUES_DEFAULT_FIELDS_PER_ROW);
	}

	@Override
	protected int getDefaultPageSize() {
		return Settings.getInt(SettingCodes.INQUIRY_VALUES_DEFAULT_PAGE_SIZE, Bundle.SETTINGS, DefaultSettings.INQUIRY_VALUES_DEFAULT_PAGE_SIZE);
	}

	public StreamedContent getInquiriesPdfStreamedContent(boolean blank) throws Exception {
		if (probandId != null) {
			try {
				InquiriesPDFVO inquiriesPdf = WebUtil.getServiceLocator().getProbandService().renderInquiries(WebUtil.getAuthentication(), null, probandId, true, null, blank);
				return new DefaultStreamedContent(new ByteArrayInputStream(inquiriesPdf.getDocumentDatas()), inquiriesPdf.getContentType().getMimeType(),
						inquiriesPdf.getFileName());
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
				throw e;
			} catch (AuthorisationException|ServiceException|IllegalArgumentException e) {
				throw e;
			}
		}
		return null;
	}

	@Override
	protected InquiryInputModelList getInquiryInputModelList(
			ArrayList<InquiryValueInVO> values) {
		return new InquiryInputModelList(values);
	}

	public String getInquiryPdfButtonLabel(boolean blank) {
		if (blank) {
			return Messages.getMessage(MessageCodes.BLANK_INQUIRY_PDF_BUTTON_LABEL, CommonUtil.trialOutVOToString(trial));
		} else {
			return Messages.getMessage(MessageCodes.INQUIRY_PDF_BUTTON_LABEL, CommonUtil.trialOutVOToString(trial));
		}
	}

	public StreamedContent getInquiryPdfStreamedContent(boolean blank) throws Exception {
		if (trialId != null && probandId != null) {
			try {
				InquiriesPDFVO inquiryPdf = WebUtil.getServiceLocator().getProbandService().renderInquiries(WebUtil.getAuthentication(), trialId, probandId, true, null, blank);
				return new DefaultStreamedContent(new ByteArrayInputStream(inquiryPdf.getDocumentDatas()), inquiryPdf.getContentType().getMimeType(), inquiryPdf.getFileName());
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
				throw e;
			} catch (AuthorisationException|ServiceException|IllegalArgumentException e) {
				throw e;
			}
		}
		return null;
	}

	@Override
	public String getModifiedAnnotation() {
		if (inquiryValuesOut != null && inquiryValuesOut.size() > 0) {
			Long version = null;
			UserOutVO modifiedUser = null;
			Date modifiedTimestamp = null;
			Iterator<InquiryValueOutVO> it = inquiryValuesOut.iterator();
			while (it.hasNext()) {
				InquiryValueOutVO inquiryValueVO = it.next();
				Date inquiryModifiedTimestamp = inquiryValueVO.getModifiedTimestamp();
				if (modifiedTimestamp == null || (inquiryModifiedTimestamp != null && modifiedTimestamp.compareTo(inquiryModifiedTimestamp) < 0)) {
					modifiedUser = inquiryValueVO.getModifiedUser();
					modifiedTimestamp = inquiryModifiedTimestamp;
				}
				if (version == null) {
					version = inquiryValueVO.getVersion();
				} else {
					version = Math.max(version.longValue(), inquiryValueVO.getVersion());
				}
			}
			if (version == null || modifiedUser == null || modifiedTimestamp == null) {
				return super.getModifiedAnnotation();
			} else {
				return WebUtil.getModifiedAnnotation(version.longValue(), modifiedUser, modifiedTimestamp);
			}
		} else {
			return super.getModifiedAnnotation();
		}
	}

	@Override
	protected ArrayList<String> getPageSizeStrings() {
		return Settings.getStringList(SettingCodes.INQUIRY_VALUES_PAGE_SIZES, Bundle.SETTINGS, DefaultSettings.INQUIRY_VALUES_PAGE_SIZES);
	}

	@Override
	protected ProbandOutVO getProband() {
		return proband;
	}

	@Override
	protected Collection<ProbandAddressOutVO> getProbandAddresses() {
		return probandAddresses;
	}

	public Long getTrialId() {
		return trialId;
	}

	public ArrayList<SelectItem> getTrials() {
		return trials;
	}

	public void handleTrialChange() {
		inquiryValuesOut = null;
		jsInquiryValuesOut = null;
		paginator.initPages(true, trialId);
		initIn(true, false);
		loadTrial();
		updateInputModelsMap();
		updateInputModelModifiedAnnotations();
		if (WebUtil.isProbandLocked(proband)) {
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.PROBAND_LOCKED);
		} else if (trial != null) {
			TrialStatusTypeVO trialStatusType = trial.getStatus();
			if (trialStatusType.getLockdown()) {
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.TRIAL_LOCKED);
			} else if (!trialStatusType.getInquiryValueInputEnabled()) {
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_INFO, MessageCodes.INQUIRY_VALUE_INPUT_DISABLED);
			}
		}
		appendRequestContextCallbackInputModelValuesOutArgs(true);
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
		if (probandId == null || trialId == null) {
			inquiryValuesIn.clear();
			if (inquiryValuesOut != null || jsInquiryValuesOut != null) {
				inquiryValuesOut.clear();
				jsInquiryValuesOut.clear();
			}
		} else {
			if (inquiryValuesOut == null || jsInquiryValuesOut == null) {
				try {
					portionInquiryValues(WebUtil.getServiceLocator().getProbandService()
							.getInquiryValues(WebUtil.getAuthentication(), trialId, true, null, probandId, true, loadAllJsValues, paginator.getPsf()));
				} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
				} catch (AuthenticationException e) {
					WebUtil.publishException(e);
				}
			}
			copyInquiryValuesOutToIn(inquiryValuesIn, inquiryValuesOut, keepInquiryValuesIn);
		}
	}

	@Override
	protected void initSpecificSets() {
		loadProband();
		probandAddresses = loadProbandAddresses();
		trialsWithoutInquiryValuesCount = WebUtil.getTrialsFromInquiryValues(probandId, true, null, trials, totalCounts); // trialId
		if (WebUtil.isProbandLocked(proband)) {
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.PROBAND_LOCKED);
		}
	}

	@Override
	public boolean isCreated() {
		if (inquiryValuesOut != null && inquiryValuesOut.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean isEditable() {
		if (trial != null && proband != null && inquiryValuesIn.size() > 0) {
			TrialStatusTypeVO trialStatusType = trial.getStatus();
			if (!trialStatusType.getLockdown() && trialStatusType.getInquiryValueInputEnabled()) {
				return !WebUtil.isProbandLocked(proband);
			}
		}
		return false;
	}

	@Override
	protected boolean isSignup() {
		return false;
	}

	@Override
	public String loadAction() {
		inquiryValuesOut = null;
		jsInquiryValuesOut = null;
		try {
			portionInquiryValues(WebUtil.getServiceLocator().getProbandService()
					.getInquiryValues(WebUtil.getAuthentication(), trialId, true, null, probandId, true, true, paginator.getPsf()));
			return LOAD_OUTCOME;
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} finally {
			initIn(true, false);
			initSets();
		}
		return ERROR_OUTCOME;
	}

	private void loadProband() {
		proband = WebUtil.getProband(probandId, null, null, null);
	}

	private Collection<ProbandAddressOutVO> loadProbandAddresses() {
		// probandAddresses = null;
		if (probandId != null) {
			try {
				// probandAddresses =
				return WebUtil.getServiceLocator().getProbandService().getProbandAddressList(WebUtil.getAuthentication(), probandId, null);
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return null;
	}

	public void setTrialId(Long trialId) {
		this.trialId = trialId;
	}

	@Override
	public String updateAction() {
		try {
			portionInquiryValues(WebUtil.getServiceLocator().getProbandService().setInquiryValues(WebUtil.getAuthentication(), new HashSet<InquiryValueInVO>(inquiryValuesIn)));
			initIn(false, false);
			initSets();
			addOperationSuccessMessage(MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
			return UPDATE_OUTCOME;
		} catch (ServiceException e) {
			setInputModelErrorMsgs(e.getData());
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} catch (AuthorisationException|IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
		return ERROR_OUTCOME;
	}
}
