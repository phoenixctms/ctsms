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
import org.phoenixctms.ctsms.vo.ECRFFieldInVO;
import org.phoenixctms.ctsms.vo.ECRFFieldOutVO;
import org.phoenixctms.ctsms.vo.ECRFOutVO;
import org.phoenixctms.ctsms.vo.InputFieldOutVO;
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
public class EcrfFieldBean extends ManagedBeanBase {

	public static void copyEcrfFieldOutToIn(ECRFFieldInVO in, ECRFFieldOutVO out) {
		if (in != null && out != null) {
			InputFieldOutVO fieldVO = out.getField();
			ECRFOutVO ecrfVO = out.getEcrf();
			TrialOutVO trialVO = out.getTrial();
			in.setSection(out.getSection());
			in.setDisabled(out.getDisabled());
			in.setSeries(out.getSeries());
			in.setFieldId(fieldVO == null ? null : fieldVO.getId());
			in.setTitle(out.getTitle());
			in.setId(out.getId());
			in.setOptional(out.getOptional());
			in.setAuditTrail(out.getAuditTrail());
			in.setReasonForChangeRequired(out.getReasonForChangeRequired());
			in.setNotify(out.getNotify());
			in.setPosition(out.getPosition());
			in.setTrialId(trialVO == null ? null : trialVO.getId());
			in.setEcrfId(ecrfVO == null ? null : ecrfVO.getId());
			in.setComment(out.getComment());
			in.setVersion(out.getVersion());
			in.setJsVariableName(out.getJsVariableName());
			in.setJsValueExpression(out.getJsValueExpression());
			in.setJsOutputExpression(out.getJsOutputExpression());
			in.setExternalId(out.getExternalId());
		}
	}

	public static void initEcrfFieldDefaultValues(ECRFFieldInVO in, Long trialId, Long ecrfId) {
		if (in != null) {
			Long position = null;
			String section = Messages.getString(MessageCodes.ECRF_FIELD_SECTION_PRESET);
			if (ecrfId != null) {
				try {
					position = WebUtil.getServiceLocator().getTrialService().getEcrfFieldMaxPosition(WebUtil.getAuthentication(), ecrfId, section);
				} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
				} catch (AuthenticationException e) {
					WebUtil.publishException(e);
				}
			}
			in.setSection(section);
			in.setSeries(Settings.getBoolean(SettingCodes.ECRF_FIELD_SERIES_PRESET, Bundle.SETTINGS, DefaultSettings.ECRF_FIELD_SERIES_PRESET));
			in.setDisabled(Settings.getBoolean(SettingCodes.ECRF_FIELD_DISABLED_PRESET, Bundle.SETTINGS, DefaultSettings.ECRF_FIELD_DISABLED_PRESET));
			in.setFieldId(null);
			in.setTitle(Messages.getString(MessageCodes.ECRF_FIELD_TITLE_PRESET));
			in.setId(null);
			in.setOptional(Settings.getBoolean(SettingCodes.ECRF_FIELD_OPTIONAL_PRESET, Bundle.SETTINGS, DefaultSettings.ECRF_FIELD_OPTIONAL_PRESET));
			in.setAuditTrail(Settings.getBoolean(SettingCodes.ECRF_FIELD_AUDIT_TRAIL_PRESET, Bundle.SETTINGS, DefaultSettings.ECRF_FIELD_AUDIT_TRAIL_PRESET));
			in.setReasonForChangeRequired(Settings.getBoolean(SettingCodes.ECRF_FIELD_AUDIT_TRAIL_PRESET, Bundle.SETTINGS, DefaultSettings.ECRF_FIELD_AUDIT_TRAIL_PRESET));
			in.setNotify(false);
			in.setPosition(position == null ? CommonUtil.LIST_INITIAL_POSITION : position + 1L);
			in.setTrialId(trialId);
			in.setEcrfId(ecrfId);
			in.setComment(Messages.getString(MessageCodes.ECRF_FIELD_COMMENT_PRESET));
			in.setVersion(null);
			in.setJsVariableName(Messages.getString(MessageCodes.ECRF_FIELD_JS_VARIABLE_NAME_PRESET));
			in.setJsValueExpression(Messages.getString(MessageCodes.ECRF_FIELD_JS_VALUE_EXPRESSION_PRESET));
			in.setJsOutputExpression(Messages.getString(MessageCodes.ECRF_FIELD_JS_OUTPUT_EXPRESSION_PRESET));
			in.setExternalId(Messages.getString(MessageCodes.ECRF_FIELD_EXTERNAL_ID_PRESET));
		}
	}

	private ECRFFieldInVO in;
	private ECRFFieldOutVO out;
	private Long trialId;
	private TrialOutVO trial;
	private Long ecrfId;
	private ECRFOutVO ecrf;
	private ArrayList<SelectItem> filterVisits;
	private ArrayList<SelectItem> filterProbandGroups;
	private ArrayList<SelectItem> filterSections;
	private ArrayList<SelectItem> oldSections;
	private EcrfLazyModel ecrfModel;
	private EcrfFieldLazyModel ecrfFieldModel;
	private InputFieldMultiPickerModel inputFieldMultiPicker;
	private String bulkAddSection;
	private boolean bulkAddOptional;
	private boolean bulkAddSeries;
	private boolean bulkAddAuditTrail;
	private String oldSection;
	private String newSection;
	private String deferredDeleteReason;

	public EcrfFieldBean() {
		super();
		ecrfModel = new EcrfLazyModel();
		ecrfFieldModel = new EcrfFieldLazyModel();
		inputFieldMultiPicker = new InputFieldMultiPickerModel();
	}

	@Override
	public String addAction() {
		ECRFFieldInVO backup = new ECRFFieldInVO(in);
		in.setId(null);
		in.setVersion(null);
		sanitizeInVals();
		try {
			out = WebUtil.getServiceLocator().getTrialService().addEcrfField(WebUtil.getAuthentication(), in);
			initIn();
			initSets();
			addOperationSuccessMessage("ecrfFieldInputMessages", MessageCodes.ADD_OPERATION_SUCCESSFUL);
			return ADD_OUTCOME;
		} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
			in.copy(backup);
			Messages.addMessageClientId("ecrfFieldInputMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			in.copy(backup);
			Messages.addMessageClientId("ecrfFieldInputMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	public final void addBulk() {
		actionPostProcess(addBulkAction());
	}

	public String addBulkAction() {
		try {
			if (bulkAddSection != null && bulkAddSection.length() > 0) {
				Set<Long> ids = this.inputFieldMultiPicker.getSelectionIds();
				Iterator<ECRFFieldOutVO> it = WebUtil.getServiceLocator().getTrialService()
						.addEcrfFields(WebUtil.getAuthentication(), ecrfId, bulkAddSection, bulkAddSeries, bulkAddOptional, bulkAddAuditTrail, ids).iterator();
				while (it.hasNext()) {
					this.inputFieldMultiPicker.removeId(it.next().getField().getId());
				}
				int itemsLeft = inputFieldMultiPicker.getSelection().size();
				if (itemsLeft > 0) {
					Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.BULK_ADD_OPERATION_INCOMPLETE, ids.size() - itemsLeft, ids.size());
				} else {
					Messages.addLocalizedMessage(FacesMessage.SEVERITY_INFO, MessageCodes.BULK_ADD_OPERATION_SUCCESSFUL, ids.size(), ids.size());
				}
				initEcrfFieldModel();
				return BULK_ADD_OUTCOME;
			} else {
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_ERROR, MessageCodes.BULK_ADD_ECRF_FIELD_SECTION_REQUIRED);
			}
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
		RequestContext requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_ECRF_FIELD_TAB_TITLE_BASE64, JSValues.AJAX_ECRF_FIELD_COUNT,
				MessageCodes.ECRF_FIELDS_TAB_TITLE, MessageCodes.ECRF_FIELDS_TAB_TITLE_WITH_COUNT, WebUtil.getEcrfFieldCount(in.getTrialId(), null));
		if (operationSuccess && in.getTrialId() != null) {
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_TRIAL_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_TRIAL_JOURNAL_ENTRY_COUNT,
					MessageCodes.TRIAL_JOURNAL_TAB_TITLE, MessageCodes.TRIAL_JOURNAL_TAB_TITLE_WITH_COUNT,
					WebUtil.getJournalCount(JournalModule.TRIAL_JOURNAL, in.getTrialId()));
		}
	}

	@Override
	protected String changeAction(Long id) {
		DataTable.clearFilters("ecrffield_ecrf_list");
		DataTable.clearFilters("ecrffield_list");
		out = null;
		this.trialId = id;
		this.ecrfId = null;
		ecrfModel.setTrialId(this.trialId);
		ecrfModel.updateRowCount();
		this.inputFieldMultiPicker.clear();
		oldSection = "";
		newSection = Messages.getString(MessageCodes.ECRF_FIELD_SECTION_PRESET);
		bulkAddSection = Messages.getString(MessageCodes.ECRF_FIELD_SECTION_PRESET);
		bulkAddOptional = Settings.getBoolean(SettingCodes.ECRF_FIELD_OPTIONAL_PRESET, Bundle.SETTINGS, DefaultSettings.ECRF_FIELD_OPTIONAL_PRESET);
		bulkAddSeries = Settings.getBoolean(SettingCodes.ECRF_FIELD_SERIES_PRESET, Bundle.SETTINGS, DefaultSettings.ECRF_FIELD_SERIES_PRESET);
		bulkAddAuditTrail = Settings.getBoolean(SettingCodes.ECRF_FIELD_AUDIT_TRAIL_PRESET, Bundle.SETTINGS, DefaultSettings.ECRF_FIELD_AUDIT_TRAIL_PRESET);
		initIn();
		initSets();
		return CHANGE_OUTCOME;
	}

	public List<String> completeBulkAddSection(String query) {
		bulkAddSection = query;
		return getCompleteSectionList(query);
	}

	public List<String> completeNewSection(String query) {
		newSection = query;
		return getCompleteSectionList(query);
	}

	public List<String> completeSection(String query) {
		in.setSection(query);
		return getCompleteSectionList(query);
	}

	@Override
	public String deleteAction() {
		return deleteAction(in.getId());
	}

	@Override
	public String deleteAction(Long id) {
		try {
			out = WebUtil.getServiceLocator().getTrialService().deleteEcrfField(WebUtil.getAuthentication(), id,
					Settings.getBoolean(SettingCodes.ECRF_FIELD_DEFERRED_DELETE, Bundle.SETTINGS, DefaultSettings.ECRF_FIELD_DEFERRED_DELETE),
					false, deferredDeleteReason);
			initIn();
			initSets();
			if (!out.getDeferredDelete()) {
				addOperationSuccessMessage("ecrfFieldInputMessages", MessageCodes.DELETE_OPERATION_SUCCESSFUL);
			}
			out = null;
			return DELETE_OUTCOME;
		} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
			Messages.addMessageClientId("ecrfFieldInputMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessageClientId("ecrfFieldInputMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	public String getBulkAddSection() {
		return bulkAddSection;
	}

	private List<String> getCompleteSectionList(String query) {
		Collection<String> sections = null;
		if (in.getEcrfId() != null) {
			try {
				sections = WebUtil.getServiceLocator().getTrialService().getEcrfFieldSections(WebUtil.getAuthentication(), null, in.getEcrfId(), query, null);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		if (sections != null) {
			try {
				return ((List<String>) sections);
			} catch (ClassCastException e) {
			}
		}
		return new ArrayList<String>();
	}

	public String getDeferredDeleteReason() {
		return deferredDeleteReason;
	}

	public String getEcrfFieldListHeader() {
		if (trial != null) {
			if (ecrf == null) {
				return Messages.getString(MessageCodes.SELECT_ECRF);
			} else {
				return Messages.getMessage(MessageCodes.ECRF_FIELD_LIST_HEADER, ecrf.getName(), ecrfFieldModel.getRowCount());
			}
		}
		return "";
	}

	public EcrfFieldLazyModel getEcrfFieldModel() {
		return ecrfFieldModel;
	}

	public EcrfLazyModel getEcrfModel() {
		return ecrfModel;
	}

	public String getFieldName() {
		return WebUtil.inputFieldIdToName(in.getFieldId());
	}

	public ArrayList<SelectItem> getFilterProbandGroups() {
		return filterProbandGroups;
	}

	public ArrayList<SelectItem> getFilterSections() {
		return filterSections;
	}

	public ArrayList<SelectItem> getFilterVisits() {
		return filterVisits;
	}

	public ECRFFieldInVO getIn() {
		return in;
	}

	public InputFieldMultiPickerModel getInputFieldMultiPicker() {
		return inputFieldMultiPicker;
	}

	@Override
	public String getModifiedAnnotation() {
		if (out != null) {
			return WebUtil.getModifiedAnnotation(out.getVersion(), out.getModifiedUser(), out.getModifiedTimestamp());
		} else {
			return super.getModifiedAnnotation();
		}
	}

	public String getNewSection() {
		return newSection;
	}

	public String getOldSection() {
		return oldSection;
	}

	public ArrayList<SelectItem> getOldSections() {
		return oldSections;
	}

	public ECRFFieldOutVO getOut() {
		return out;
	}

	public IDVO getSelectedEcrf() {
		if (this.ecrf != null) {
			return IDVO.transformVo(this.ecrf);
		} else {
			return null;
		}
	}

	public IDVO getSelectedEcrfField() {
		if (this.out != null) {
			return IDVO.transformVo(this.out);
		} else {
			return null;
		}
	}

	@Override
	public String getTitle() {
		if (out != null) {
			return Messages.getMessage(MessageCodes.ECRF_FIELD_TITLE, Long.toString(out.getId()), out.getField().getName());
		} else {
			return Messages.getString(MessageCodes.CREATE_NEW_ECRF_FIELD);
		}
	}

	public void handleAuditTrailChange() {
		if (!in.getAuditTrail()) {
			in.setReasonForChangeRequired(false);
		}
	}

	public void handleBulkAddSectionSelect(SelectEvent event) {
		bulkAddSection = (String) event.getObject();
	}

	public void handleNewSectionSelect(SelectEvent event) {
		newSection = (String) event.getObject();
	}

	public void handleSectionSelect(SelectEvent event) {
		in.setSection((String) event.getObject());
	}

	@PostConstruct
	private void init() {
		Long id = WebUtil.getLongParamValue(GetParamNames.ECRF_FIELD_ID);
		if (id != null) {
			this.load(id);
		} else {
			initIn();
			initSets();
		}
	}

	private void initEcrfFieldModel() {
		if (in.getEcrfId() != null) {
			ecrfFieldModel.setTrialId(in.getTrialId());
		} else {
			ecrfFieldModel.setTrialId(null);
		}
		ecrfFieldModel.setEcrfId(in.getEcrfId());
		ecrfFieldModel.updateRowCount();
		Collection<String> sectionStrings = null;
		if (in.getTrialId() != null) {
			try {
				sectionStrings = WebUtil.getServiceLocator().getTrialService().getEcrfFieldSections(WebUtil.getAuthentication(), in.getTrialId(), in.getEcrfId(), null, null);
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
			}
		}
		if (sectionStrings != null) {
			filterSections = new ArrayList<SelectItem>(sectionStrings.size());
			oldSections = new ArrayList<SelectItem>(sectionStrings.size());
			Iterator<String> it = sectionStrings.iterator();
			while (it.hasNext()) {
				String section = it.next();
				filterSections.add(new SelectItem(section, section));
				oldSections.add(new SelectItem(section, section));
			}
		} else {
			filterSections = new ArrayList<SelectItem>();
			oldSections = new ArrayList<SelectItem>();
		}
		filterSections.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
	}

	private void initIn() {
		if (in == null) {
			in = new ECRFFieldInVO();
		}
		if (out != null) {
			copyEcrfFieldOutToIn(in, out);
			trialId = in.getTrialId();
			ecrfId = in.getEcrfId();
		} else {
			initEcrfFieldDefaultValues(in, trialId, ecrfId);
		}
	}

	private void initSets() {
		filterProbandGroups = WebUtil.getProbandGroups(in.getTrialId());
		filterProbandGroups.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		filterVisits = WebUtil.getVisits(in.getTrialId());
		filterVisits.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		initEcrfFieldModel();
		trial = WebUtil.getTrial(this.in.getTrialId());
		ecrf = WebUtil.getEcrf(this.in.getEcrfId());
		if (WebUtil.isTrialLocked(trial)) {
			Messages.addLocalizedMessageClientId("ecrfFieldInputMessages", FacesMessage.SEVERITY_WARN, MessageCodes.TRIAL_LOCKED);
		}
		deferredDeleteReason = (out == null ? null : out.getDeferredDeleteReason());
		if (out != null && out.isDeferredDelete()) {
			Messages.addLocalizedMessageClientId("ecrfFieldInputMessages", FacesMessage.SEVERITY_WARN, MessageCodes.MARKED_FOR_DELETION, deferredDeleteReason);
		}
		if (trial != null && ecrf == null) {
			Messages.addLocalizedMessageClientId("ecrfFieldInputMessages", FacesMessage.SEVERITY_INFO, MessageCodes.SELECT_ECRF);
		}
	}

	public boolean isBulkAddAuditTrail() {
		return bulkAddAuditTrail;
	}

	public boolean isBulkAddOptional() {
		return bulkAddOptional;
	}

	public boolean isBulkAddSeries() {
		return bulkAddSeries;
	}

	@Override
	public boolean isCreateable() {
		return ((in.getTrialId() == null || in.getEcrfId() == null) ? false : !WebUtil.isTrialLocked(trial));
	}

	@Override
	public boolean isCreated() {
		return out != null;
	}

	public boolean isDeferredDelete() {
		return Settings.getBoolean(SettingCodes.ECRF_FIELD_DEFERRED_DELETE, Bundle.SETTINGS, DefaultSettings.ECRF_FIELD_DEFERRED_DELETE);
	}

	@Override
	public boolean isEditable() {
		return isCreated() && !WebUtil.isTrialLocked(trial);
	}

	public boolean isInputFieldBulkCreateable() {
		return isCreateable() && inputFieldMultiPicker.getIsEnabled();
	}

	public boolean isInputVisible() {
		return isCreated() || (!WebUtil.isTrialLocked(trial) && ecrf != null);
	}

	@Override
	public boolean isRemovable() {
		return isCreated() && !WebUtil.isTrialLocked(trial);
	}

	public boolean isUpdateSectionsEditable() {
		return isCreateable();
	}

	@Override
	public String loadAction() {
		return loadAction(in.getId());
	}

	@Override
	public String loadAction(Long id) {
		out = null;
		try {
			out = WebUtil.getServiceLocator().getTrialService().getEcrfField(WebUtil.getAuthentication(), id);
			return LOAD_OUTCOME;
		} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
			Messages.addMessageClientId("ecrfFieldInputMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessageClientId("ecrfFieldInputMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} finally {
			initIn();
			initSets();
		}
		return ERROR_OUTCOME;
	}

	public void moveDown(Long ecrfFieldId) {
		try {
			out = WebUtil.getServiceLocator().getTrialService().moveEcrfField(WebUtil.getAuthentication(), ecrfFieldId, PositionMovement.DOWN);
			initIn();
			initSets();
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
	}

	public void moveFirst(Long ecrfFieldId) {
		try {
			out = WebUtil.getServiceLocator().getTrialService().moveEcrfField(WebUtil.getAuthentication(), ecrfFieldId, PositionMovement.FIRST);
			initIn();
			initSets();
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
	}

	public void moveLast(Long ecrfFieldId) {
		try {
			out = WebUtil.getServiceLocator().getTrialService().moveEcrfField(WebUtil.getAuthentication(), ecrfFieldId, PositionMovement.LAST);
			initIn();
			initSets();
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
	}

	public void moveTo() {
		Long ecrfFieldId = WebUtil.getLongParamValue(GetParamNames.ECRF_FIELD_ID);
		Long targetPosition = WebUtil.getLongParamValue(GetParamNames.TARGET_POSITION);
		if (ecrfFieldId != null && targetPosition != null) {
			try {
				Collection<ECRFFieldOutVO> updated = WebUtil.getServiceLocator().getTrialService().moveEcrfFieldTo(WebUtil.getAuthentication(), ecrfFieldId, targetPosition);
				out = null;
				initIn();
				initSets();
				Messages.addLocalizedMessageClientId("ecrfFieldInputMessages", FacesMessage.SEVERITY_INFO, MessageCodes.POSITIONS_UPDATE_SUCCESSFUL, updated.size());
			} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
				Messages.addMessageClientId("ecrfFieldInputMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
			} catch (AuthenticationException e) {
				Messages.addMessageClientId("ecrfFieldInputMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
				WebUtil.publishException(e);
			}
		}
	}

	public void moveUp(Long ecrfFieldId) {
		try {
			out = WebUtil.getServiceLocator().getTrialService().moveEcrfField(WebUtil.getAuthentication(), ecrfFieldId, PositionMovement.UP);
			initIn();
			initSets();
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
	}

	public final void normalizePositions() {
		actionPostProcess(normalizePositionsAction());
	}

	public String normalizePositionsAction() {
		try {
			Collection<ECRFFieldOutVO> updated = WebUtil.getServiceLocator().getTrialService().normalizeEcrfFieldPositions(WebUtil.getAuthentication(), in.getId());
			out = null;
			initIn();
			initSets();
			Messages.addLocalizedMessageClientId("ecrfFieldInputMessages", FacesMessage.SEVERITY_INFO, MessageCodes.POSITIONS_UPDATE_SUCCESSFUL, updated.size());
			return UPDATE_OUTCOME;
		} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
			Messages.addMessageClientId("ecrfFieldInputMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessageClientId("ecrfFieldInputMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
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

	private void sanitizeInVals() {
		if (!in.getAuditTrail()) {
			in.setReasonForChangeRequired(false);
		}
	}

	public void setBulkAddAuditTrail(boolean bulkAddAuditTrail) {
		this.bulkAddAuditTrail = bulkAddAuditTrail;
	}

	public void setBulkAddOptional(boolean bulkAddOptional) {
		this.bulkAddOptional = bulkAddOptional;
	}

	public void setBulkAddSection(String bulkAddSection) {
		this.bulkAddSection = bulkAddSection;
	}

	public void setBulkAddSeries(boolean bulkAddSeries) {
		this.bulkAddSeries = bulkAddSeries;
	}

	public void setDeferredDeleteReason(String deferredDeleteReason) {
		this.deferredDeleteReason = deferredDeleteReason;
	}

	public void setNewSection(String newSection) {
		this.newSection = newSection;
	}

	public void setOldSection(String oldSection) {
		this.oldSection = oldSection;
	}

	public void setSelectedEcrf(IDVO ecrf) {
		if (ecrf != null) {
			DataTable.clearFilters("ecrffield_list");
			this.ecrfId = ecrf.getId();
			out = null;
			this.initIn();
			initSets();
		}
	}

	public void setSelectedEcrfField(IDVO ecrfField) {
		if (ecrfField != null) {
			this.out = (ECRFFieldOutVO) ecrfField.getVo();
			this.initIn();
			initSets();
		}
	}

	@Override
	public String updateAction() {
		ECRFFieldInVO backup = new ECRFFieldInVO(in);
		sanitizeInVals();
		try {
			out = WebUtil.getServiceLocator().getTrialService().updateEcrfField(WebUtil.getAuthentication(), in);
			initIn();
			initSets();
			addOperationSuccessMessage("ecrfFieldInputMessages", MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
			return UPDATE_OUTCOME;
		} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
			in.copy(backup);
			Messages.addMessageClientId("ecrfFieldInputMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			in.copy(backup);
			Messages.addMessageClientId("ecrfFieldInputMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	public final void updateSections() {
		actionPostProcess(updateSectionsAction());
	}

	public String updateSectionsAction() {
		try {
			if (oldSection == null || oldSection.length() == 0) {
				Messages.addLocalizedMessageClientId("updateSectionsMessages", FacesMessage.SEVERITY_ERROR, MessageCodes.UPDATE_ECRF_FIELD_SECTIONS_OLD_SECTION_REQUIRED);
			} else if (newSection == null || newSection.length() == 0) {
				Messages.addLocalizedMessageClientId("updateSectionsMessages", FacesMessage.SEVERITY_ERROR, MessageCodes.UPDATE_ECRF_FIELD_SECTIONS_NEW_SECTION_REQUIRED);
			} else {
				Collection<ECRFFieldOutVO> ecrfFields = WebUtil.getServiceLocator().getTrialService()
						.updateEcrfFieldSections(WebUtil.getAuthentication(), ecrfId, oldSection, newSection);
				boolean initialized;
				if (ecrfFields.size() > 0) {
					initialized = false;
					if (in.getId() != null) {
						Iterator<ECRFFieldOutVO> it = ecrfFields.iterator();
						while (it.hasNext()) {
							ECRFFieldOutVO ecrfField = it.next();
							if (in.getId() == ecrfField.getId()) {
								out = ecrfField;
								initIn();
								initSets();
								initialized = true;
							}
						}
					}
					Messages.addLocalizedMessageClientId("updateSectionsMessages", FacesMessage.SEVERITY_INFO, MessageCodes.BULK_UPDATE_OPERATION_SUCCESSFUL, ecrfFields.size());
				} else {
					initialized = true;
					Messages.addLocalizedMessageClientId("updateSectionsMessages", FacesMessage.SEVERITY_WARN, MessageCodes.BULK_UPDATE_OPERATION_INCOMPLETE, ecrfFields.size());
				}
				if (!initialized) {
					initEcrfFieldModel();
				}
				return BULK_UPDATE_OUTCOME;
			}
		} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
			Messages.addMessageClientId("updateSectionsMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessageClientId("updateSectionsMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}
}
