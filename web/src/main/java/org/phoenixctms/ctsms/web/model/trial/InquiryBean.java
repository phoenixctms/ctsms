package org.phoenixctms.ctsms.web.model.trial;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.enumeration.PositionMovement;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.InputFieldOutVO;
import org.phoenixctms.ctsms.vo.InquiryInVO;
import org.phoenixctms.ctsms.vo.InquiryOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.web.component.datatable.DataTable;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.model.shared.InputFieldMultiPickerModel;
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
public class InquiryBean extends ManagedBeanBase {

	public static void copyInquiryOutToIn(InquiryInVO in, InquiryOutVO out) {
		if (in != null && out != null) {
			InputFieldOutVO fieldVO = out.getField();
			TrialOutVO trialVO = out.getTrial();
			in.setActive(out.getActive());
			in.setActiveSignup(out.getActiveSignup());
			in.setCategory(out.getCategory());
			in.setDisabled(out.getDisabled());
			in.setFieldId(fieldVO == null ? null : fieldVO.getId());
			in.setTitle(out.getTitle());
			in.setId(out.getId());
			in.setOptional(out.getOptional());
			in.setExcelValue(out.getExcelValue());
			in.setExcelDate(out.getExcelDate());
			in.setPosition(out.getPosition());
			in.setTrialId(trialVO == null ? null : trialVO.getId());
			in.setComment(out.getComment());
			in.setVersion(out.getVersion());
			in.setJsVariableName(out.getJsVariableName());
			in.setJsValueExpression(out.getJsValueExpression());
			in.setJsOutputExpression(out.getJsOutputExpression());
		}
	}

	public static void initInquiryDefaultValues(InquiryInVO in, Long trialId) {
		if (in != null) {
			Long position = null;
			String category = Messages.getString(MessageCodes.INQUIRY_CATEGORY_PRESET);
			if (trialId != null) {
				try {
					position = WebUtil.getServiceLocator().getTrialService().getInquiryMaxPosition(WebUtil.getAuthentication(), trialId, category);
				} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
				} catch (AuthenticationException e) {
					WebUtil.publishException(e);
				}
			}
			in.setActive(Settings.getBoolean(SettingCodes.INQUIRY_ACTIVE_PRESET, Bundle.SETTINGS, DefaultSettings.INQUIRY_ACTIVE_PRESET));
			in.setActiveSignup(Settings.getBoolean(SettingCodes.INQUIRY_ACTIVE_SIGNUP_PRESET, Bundle.SETTINGS, DefaultSettings.INQUIRY_ACTIVE_SIGNUP_PRESET));
			in.setCategory(category);
			in.setDisabled(Settings.getBoolean(SettingCodes.INQUIRY_DISABLED_PRESET, Bundle.SETTINGS, DefaultSettings.INQUIRY_DISABLED_PRESET));
			in.setFieldId(null);
			in.setTitle(Messages.getString(MessageCodes.INQUIRY_TITLE_PRESET));
			in.setId(null);
			in.setOptional(Settings.getBoolean(SettingCodes.INQUIRY_OPTIONAL_PRESET, Bundle.SETTINGS, DefaultSettings.INQUIRY_OPTIONAL_PRESET));
			in.setExcelValue(Settings.getBoolean(SettingCodes.INQUIRY_EXCEL_PRESET, Bundle.SETTINGS, DefaultSettings.INQUIRY_EXCEL_PRESET));
			in.setExcelDate(Settings.getBoolean(SettingCodes.INQUIRY_EXCEL_PRESET, Bundle.SETTINGS, DefaultSettings.INQUIRY_EXCEL_PRESET));
			in.setPosition(position == null ? CommonUtil.LIST_INITIAL_POSITION : position + 1L);
			in.setTrialId(trialId);
			in.setComment(Messages.getString(MessageCodes.INQUIRY_COMMENT_PRESET));
			in.setVersion(null);
			in.setJsVariableName(Messages.getString(MessageCodes.INQUIRY_JS_VARIABLE_NAME_PRESET));
			in.setJsValueExpression(Messages.getString(MessageCodes.INQUIRY_JS_VALUE_EXPRESSION_PRESET));
			in.setJsOutputExpression(Messages.getString(MessageCodes.INQUIRY_JS_OUTPUT_EXPRESSION_PRESET));
		}
	}

	private InquiryInVO in;
	private InquiryOutVO out;
	private Long trialId;
	private TrialOutVO trial;
	private ArrayList<SelectItem> filterCategories;
	private InquiryLazyModel inquiryModel;
	private InputFieldMultiPickerModel inputFieldMultiPicker;
	private String bulkAddCategory;
	private boolean bulkAddOptional;
	private boolean bulkAddExcel;
	private String deferredDeleteReason;

	public InquiryBean() {
		super();
		inquiryModel = new InquiryLazyModel();
		inputFieldMultiPicker = new InputFieldMultiPickerModel();
	}

	@Override
	public String addAction() {
		InquiryInVO backup = new InquiryInVO(in);
		// Long idBackup = in.getId();
		// Long versionBackup = in.getVersion();
		in.setId(null);
		in.setVersion(null);
		try {
			out = WebUtil.getServiceLocator().getTrialService().addInquiry(WebUtil.getAuthentication(), in);
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

	public final void addBulk() {
		actionPostProcess(addBulkAction());
	}

	public String addBulkAction() {
		try {
			if (bulkAddCategory != null && bulkAddCategory.length() > 0) {
				Set<Long> ids = this.inputFieldMultiPicker.getSelectionIds();
				Iterator<InquiryOutVO> it = WebUtil.getServiceLocator().getTrialService()
						.addInquiries(WebUtil.getAuthentication(), trialId, bulkAddCategory, bulkAddOptional, bulkAddExcel, ids).iterator();
				while (it.hasNext()) {
					this.inputFieldMultiPicker.removeId(it.next().getField().getId());
				}
				int itemsLeft = inputFieldMultiPicker.getSelection().size();
				if (itemsLeft > 0) {
					Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.BULK_ADD_OPERATION_INCOMPLETE, ids.size() - itemsLeft, ids.size());
				} else {
					Messages.addLocalizedMessage(FacesMessage.SEVERITY_INFO, MessageCodes.BULK_ADD_OPERATION_SUCCESSFUL, ids.size(), ids.size());
				}
				// inquiryModel.updateRowCount();
				initInquiryModel();
				return BULK_ADD_OUTCOME;
			} else {
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_ERROR, MessageCodes.BULK_ADD_INQUIRY_CATEGORY_REQUIRED);
			}
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
		RequestContext requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_INQUIRY_TAB_TITLE_BASE64, JSValues.AJAX_INQUIRY_COUNT,
				MessageCodes.INQUIRIES_TAB_TITLE, MessageCodes.INQUIRIES_TAB_TITLE_WITH_COUNT, new Long(inquiryModel.getRowCount()));
		if (operationSuccess && in.getTrialId() != null) {
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_TRIAL_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_TRIAL_JOURNAL_ENTRY_COUNT,
					MessageCodes.TRIAL_JOURNAL_TAB_TITLE, MessageCodes.TRIAL_JOURNAL_TAB_TITLE_WITH_COUNT,
					WebUtil.getJournalCount(JournalModule.TRIAL_JOURNAL, in.getTrialId()));
		}
	}

	@Override
	protected String changeAction(Long id) {
		DataTable.clearFilters("inquiry_list");
		out = null;
		this.trialId = id;
		this.inputFieldMultiPicker.clear();
		bulkAddCategory = Messages.getString(MessageCodes.INQUIRY_CATEGORY_PRESET);
		bulkAddOptional = Settings.getBoolean(SettingCodes.INQUIRY_OPTIONAL_PRESET, Bundle.SETTINGS, DefaultSettings.INQUIRY_OPTIONAL_PRESET);
		bulkAddExcel = Settings.getBoolean(SettingCodes.INQUIRY_EXCEL_PRESET, Bundle.SETTINGS, DefaultSettings.INQUIRY_EXCEL_PRESET);
		initIn();
		initSets();
		return CHANGE_OUTCOME;
	}

	public List<String> completeBulkAddCategory(String query) {
		bulkAddCategory = query;
		return getCompleteCategoryList(query);
	}

	public List<String> completeCategory(String query) {
		in.setCategory(query);
		return getCompleteCategoryList(query);
	}

	@Override
	public String deleteAction() {
		return deleteAction(in.getId());
	}

	@Override
	public String deleteAction(Long id) {
		try {
			out = WebUtil.getServiceLocator().getTrialService().deleteInquiry(WebUtil.getAuthentication(), id,
					Settings.getBoolean(SettingCodes.INQUIRY_DEFERRED_DELETE, Bundle.SETTINGS, DefaultSettings.INQUIRY_DEFERRED_DELETE), false, deferredDeleteReason);
			initIn();
			initSets();
			if (!out.getDeferredDelete()) {
				addOperationSuccessMessage(MessageCodes.DELETE_OPERATION_SUCCESSFUL);
			}
			out = null;
			return DELETE_OUTCOME;
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	public String getBulkAddCategory() {
		return bulkAddCategory;
	}

	private List<String> getCompleteCategoryList(String query) {
		Collection<String> categories = null;
		if (in.getTrialId() != null) {
			try {
				categories = WebUtil.getServiceLocator().getTrialService().getInquiryCategories(WebUtil.getAuthentication(), in.getTrialId(), query, null, null, null);
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		if (categories != null) {
			try {
				return ((List<String>) categories);
			} catch (ClassCastException e) {
			}
		}
		return new ArrayList<String>();
	}

	public String getDeferredDeleteReason() {
		return deferredDeleteReason;
	}

	public String getFieldName() {
		return WebUtil.inputFieldIdToName(in.getFieldId());
	}

	public ArrayList<SelectItem> getFilterCategories() {
		return filterCategories;
	}

	public InquiryInVO getIn() {
		return in;
	}

	public InputFieldMultiPickerModel getInputFieldMultiPicker() {
		return inputFieldMultiPicker;
	}

	public InquiryLazyModel getInquiryModel() {
		return inquiryModel;
	}

	@Override
	public String getModifiedAnnotation() {
		if (out != null) {
			return WebUtil.getModifiedAnnotation(out.getVersion(), out.getModifiedUser(), out.getModifiedTimestamp());
		} else {
			return super.getModifiedAnnotation();
		}
	}

	public InquiryOutVO getOut() {
		return out;
	}

	public IDVO getSelectedInquiry() {
		if (this.out != null) {
			return IDVO.transformVo(this.out);
		} else {
			return null;
		}
	}

	@Override
	public String getTitle() {
		if (out != null) {
			return Messages.getMessage(MessageCodes.INQUIRY_TITLE, Long.toString(out.getId()), out.getField().getName());
		} else {
			return Messages.getString(MessageCodes.CREATE_NEW_INQUIRY);
		}
	}

	public void handleBulkAddCategorySelect(SelectEvent event) {
		bulkAddCategory = (String) event.getObject();
	}

	public void handleCategorySelect(SelectEvent event) {
		in.setCategory((String) event.getObject());
	}

	@PostConstruct
	private void init() {
		Long id = WebUtil.getLongParamValue(GetParamNames.INQUIRY_ID);
		if (id != null) {
			this.load(id);
		} else {
			initIn();
			initSets();
		}
	}

	private void initIn() {
		if (in == null) {
			in = new InquiryInVO();
		}
		if (out != null) {
			copyInquiryOutToIn(in, out);
			trialId = in.getTrialId();
		} else {
			initInquiryDefaultValues(in, trialId);
		}
	}

	private void initInquiryModel() {
		inquiryModel.setTrialId(in.getTrialId());
		inquiryModel.updateRowCount();
		Collection<String> categoryStrings = null;
		if (in.getTrialId() != null) {
			try {
				categoryStrings = WebUtil.getServiceLocator().getTrialService().getInquiryCategories(WebUtil.getAuthentication(), in.getTrialId(), null, null, null, null);
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			} catch (AuthorisationException|ServiceException|IllegalArgumentException e) {
			}
		}
		if (categoryStrings != null) {
			filterCategories = new ArrayList<SelectItem>(categoryStrings.size());
			Iterator<String> it = categoryStrings.iterator();
			while (it.hasNext()) {
				String category = it.next();
				filterCategories.add(new SelectItem(category, category));
			}
		} else {
			filterCategories = new ArrayList<SelectItem>();
		}
		filterCategories.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
	}

	private void initSets() {
		initInquiryModel();
		trial = WebUtil.getTrial(this.in.getTrialId());
		if (WebUtil.isTrialLocked(trial)) {
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.TRIAL_LOCKED);
		}
		deferredDeleteReason = (out == null ? null : out.getDeferredDeleteReason());
		if (out != null && out.isDeferredDelete()) { // && Settings.getBoolean(SettingCodes.INQUIRY_DEFERRED_DELETE, Bundle.SETTINGS, DefaultSettings.INQUIRY_DEFERRED_DELETE)) {
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.MARKED_FOR_DELETION, deferredDeleteReason);
		}
	}

	public boolean isBulkAddExcel() {
		return bulkAddExcel;
	}

	public boolean isBulkAddOptional() {
		return bulkAddOptional;
	}

	@Override
	public boolean isCreateable() {
		return (in.getTrialId() == null ? false : !WebUtil.isTrialLocked(trial));
	}

	@Override
	public boolean isCreated() {
		return out != null;
	}

	public boolean isDeferredDelete() {
		return Settings.getBoolean(SettingCodes.INQUIRY_DEFERRED_DELETE, Bundle.SETTINGS, DefaultSettings.INQUIRY_DEFERRED_DELETE);
	}

	@Override
	public boolean isEditable() {
		return isCreated() && !WebUtil.isTrialLocked(trial);
	}

	public boolean isInputFieldBulkCreateable() {
		return isCreateable() && inputFieldMultiPicker.getIsEnabled();
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
			out = WebUtil.getServiceLocator().getTrialService().getInquiry(WebUtil.getAuthentication(), id);
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

	public void moveDown(Long inquiryId) {
		try {
			out = WebUtil.getServiceLocator().getTrialService().moveInquiry(WebUtil.getAuthentication(), inquiryId, PositionMovement.DOWN);
			initIn();
			initSets();
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
	}

	public void moveFirst(Long inquiryId) {
		try {
			out = WebUtil.getServiceLocator().getTrialService().moveInquiry(WebUtil.getAuthentication(), inquiryId, PositionMovement.FIRST);
			initIn();
			initSets();
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
	}

	public void moveLast(Long inquiryId) {
		try {
			out = WebUtil.getServiceLocator().getTrialService().moveInquiry(WebUtil.getAuthentication(), inquiryId, PositionMovement.LAST);
			initIn();
			initSets();
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
	}

	public void moveTo() {
		Long inquiryId = WebUtil.getLongParamValue(GetParamNames.INQUIRY_ID);
		Long targetPosition = WebUtil.getLongParamValue(GetParamNames.TARGET_POSITION);
		if (inquiryId != null && targetPosition != null) {
			try {
				Collection<InquiryOutVO> updated = WebUtil.getServiceLocator().getTrialService().moveInquiryTo(WebUtil.getAuthentication(), inquiryId, targetPosition);
				out = null;
				initIn();
				initSets();
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_INFO, MessageCodes.POSITIONS_UPDATE_SUCCESSFUL, updated.size());
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
				Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			} catch (AuthenticationException e) {
				Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
				WebUtil.publishException(e);
			}
		}
	}

	public void moveUp(Long inquiryId) {
		try {
			out = WebUtil.getServiceLocator().getTrialService().moveInquiry(WebUtil.getAuthentication(), inquiryId, PositionMovement.UP);
			initIn();
			initSets();
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
	}

	public final void normalizePositions() {
		actionPostProcess(normalizePositionsAction());
	}

	public String normalizePositionsAction() {
		try {
			Collection<InquiryOutVO> updated = WebUtil.getServiceLocator().getTrialService().normalizeInquiryPositions(WebUtil.getAuthentication(), in.getId());
			out = null;
			initIn();
			initSets();
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_INFO, MessageCodes.POSITIONS_UPDATE_SUCCESSFUL, updated.size());
			return UPDATE_OUTCOME;
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
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

	public void setBulkAddCategory(String bulkAddCategory) {
		this.bulkAddCategory = bulkAddCategory;
	}

	public void setBulkAddExcel(boolean bulkAddExcel) {
		this.bulkAddExcel = bulkAddExcel;
	}

	public void setBulkAddOptional(boolean bulkAddOptional) {
		this.bulkAddOptional = bulkAddOptional;
	}

	public void setDeferredDeleteReason(String deferredDeleteReason) {
		this.deferredDeleteReason = deferredDeleteReason;
	}

	public void setSelectedInquiry(IDVO inquiry) {
		if (inquiry != null) {
			this.out = (InquiryOutVO) inquiry.getVo();
			this.initIn();
			initSets();
		}
	}

	@Override
	public String updateAction() {
		try {
			out = WebUtil.getServiceLocator().getTrialService().updateInquiry(WebUtil.getAuthentication(), in);
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
