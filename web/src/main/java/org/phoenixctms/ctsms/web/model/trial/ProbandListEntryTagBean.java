package org.phoenixctms.ctsms.web.model.trial;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.enumeration.PositionMovement;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.InputFieldOutVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagInVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagOutVO;
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

@ManagedBean
@ViewScoped
public class ProbandListEntryTagBean extends ManagedBeanBase {

	public static void copyProbandListEntryTagOutToIn(ProbandListEntryTagInVO in, ProbandListEntryTagOutVO out) {
		if (in != null && out != null) {
			InputFieldOutVO fieldVO = out.getField();
			TrialOutVO trialVO = out.getTrial();
			in.setDisabled(out.getDisabled());
			in.setFieldId(fieldVO == null ? null : fieldVO.getId());
			in.setTitle(out.getTitle());
			in.setId(out.getId());
			in.setOptional(out.getOptional());
			in.setExcelValue(out.getExcelValue());
			in.setEcrfValue(out.getEcrfValue());
			in.setStratification(out.getStratification());
			in.setRandomize(out.getRandomize());
			in.setExternalId(out.getExternalId());
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

	public static void initProbandListEntryTagDefaultValues(ProbandListEntryTagInVO in, Long trialId) {
		if (in != null) {
			Long position = null;
			if (trialId != null) {
				try {
					position = WebUtil.getServiceLocator().getTrialService().getProbandListEntryTagMaxPosition(WebUtil.getAuthentication(), trialId);
				} catch (ServiceException e) {
				} catch (AuthenticationException e) {
					WebUtil.publishException(e);
				} catch (AuthorisationException e) {
				} catch (IllegalArgumentException e) {
				}
			}
			in.setDisabled(Settings.getBoolean(SettingCodes.PROBAND_LIST_ENTRY_TAG_DISABLED_PRESET, Bundle.SETTINGS, DefaultSettings.PROBAND_LIST_ENTRY_TAG_DISABLED_PRESET));
			in.setFieldId(null);
			in.setTitle(Messages.getString(MessageCodes.PROBAND_LIST_ENTRY_TAG_TITLE_PRESET));
			in.setId(null);
			in.setOptional(Settings.getBoolean(SettingCodes.PROBAND_LIST_ENTRY_TAG_OPTIONAL_PRESET, Bundle.SETTINGS, DefaultSettings.PROBAND_LIST_ENTRY_TAG_OPTIONAL_PRESET));
			in.setExcelValue(Settings.getBoolean(SettingCodes.PROBAND_LIST_ENTRY_TAG_EXCEL_PRESET, Bundle.SETTINGS, DefaultSettings.PROBAND_LIST_ENTRY_TAG_EXCEL_PRESET));
			in.setEcrfValue(Settings.getBoolean(SettingCodes.PROBAND_LIST_ENTRY_TAG_ECRF_PRESET, Bundle.SETTINGS, DefaultSettings.PROBAND_LIST_ENTRY_TAG_ECRF_PRESET));
			in.setStratification(
					Settings.getBoolean(SettingCodes.PROBAND_LIST_ENTRY_TAG_STRATIFICATION_PRESET, Bundle.SETTINGS, DefaultSettings.PROBAND_LIST_ENTRY_TAG_STRATIFICATION_PRESET));
			in.setRandomize(
					Settings.getBoolean(SettingCodes.PROBAND_LIST_ENTRY_TAG_RANDOMIZE_PRESET, Bundle.SETTINGS, DefaultSettings.PROBAND_LIST_ENTRY_TAG_RANDOMIZE_PRESET));
			in.setExcelDate(Settings.getBoolean(SettingCodes.PROBAND_LIST_ENTRY_TAG_EXCEL_PRESET, Bundle.SETTINGS, DefaultSettings.PROBAND_LIST_ENTRY_TAG_EXCEL_PRESET));
			in.setExternalId(Messages.getString(MessageCodes.PROBAND_LIST_ENTRY_TAG_EXTERNAL_ID_PRESET));
			in.setPosition(position == null ? CommonUtil.LIST_INITIAL_POSITION : position + 1L);
			in.setTrialId(trialId);
			in.setComment(Messages.getString(MessageCodes.PROBAND_LIST_ENTRY_TAG_COMMENT_PRESET));
			in.setVersion(null);
			in.setJsVariableName(Messages.getString(MessageCodes.PROBAND_LIST_ENTRY_TAG_JS_VARIABLE_NAME_PRESET));
			in.setJsValueExpression(Messages.getString(MessageCodes.PROBAND_LIST_ENTRY_TAG_JS_VALUE_EXPRESSION_PRESET));
			in.setJsOutputExpression(Messages.getString(MessageCodes.PROBAND_LIST_ENTRY_TAG_JS_OUTPUT_EXPRESSION_PRESET));
		}
	}

	private ProbandListEntryTagInVO in;
	private ProbandListEntryTagOutVO out;
	private Long trialId;
	private TrialOutVO trial;
	private ProbandListEntryTagLazyModel probandListEntryTagModel;
	private InputFieldMultiPickerModel inputFieldMultiPicker;
	private boolean bulkAddOptional;
	private boolean bulkAddExcel;
	private boolean bulkAddEcrf;
	private boolean bulkAddStratification;
	private boolean bulkAddRandomize;

	public ProbandListEntryTagBean() {
		super();
		probandListEntryTagModel = new ProbandListEntryTagLazyModel();
		inputFieldMultiPicker = new InputFieldMultiPickerModel();
	}

	@Override
	public String addAction() {
		ProbandListEntryTagInVO backup = new ProbandListEntryTagInVO(in);
		// Long idBackup = in.getId();
		// Long versionBackup = in.getVersion();
		in.setId(null);
		in.setVersion(null);
		try {
			out = WebUtil.getServiceLocator().getTrialService().addProbandListEntryTag(WebUtil.getAuthentication(), in);
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

	public final void addBulk() {
		actionPostProcess(addBulkAction());
	}

	public String addBulkAction() {
		try {
			Set<Long> ids = this.inputFieldMultiPicker.getSelectionIds();
			Iterator<ProbandListEntryTagOutVO> it = WebUtil.getServiceLocator().getTrialService()
					.addProbandListEntryTags(WebUtil.getAuthentication(), trialId, bulkAddOptional, bulkAddExcel, bulkAddEcrf, bulkAddStratification, bulkAddRandomize, ids)
					.iterator();
			while (it.hasNext()) {
				this.inputFieldMultiPicker.removeId(it.next().getField().getId());
			}
			int itemsLeft = inputFieldMultiPicker.getSelection().size();
			if (itemsLeft > 0) {
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.BULK_ADD_OPERATION_INCOMPLETE, ids.size() - itemsLeft, ids.size());
			} else {
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_INFO, MessageCodes.BULK_ADD_OPERATION_SUCCESSFUL, ids.size(), ids.size());
			}
			probandListEntryTagModel.updateRowCount();
			return BULK_ADD_OUTCOME;
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

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
		RequestContext requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_PROBAND_LIST_ENTRY_TAG_TAB_TITLE_BASE64,
				JSValues.AJAX_PROBAND_LIST_ENTRY_TAG_COUNT, MessageCodes.PROBAND_LIST_ENTRY_TAGS_TAB_TITLE, MessageCodes.PROBAND_LIST_ENTRY_TAGS_TAB_TITLE_WITH_COUNT, new Long(
						probandListEntryTagModel.getRowCount()));
		if (operationSuccess && in.getTrialId() != null) {
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_TRIAL_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_TRIAL_JOURNAL_ENTRY_COUNT,
					MessageCodes.TRIAL_JOURNAL_TAB_TITLE, MessageCodes.TRIAL_JOURNAL_TAB_TITLE_WITH_COUNT,
					WebUtil.getJournalCount(JournalModule.TRIAL_JOURNAL, in.getTrialId()));
		}
	}

	@Override
	protected String changeAction(Long id) {
		DataTable.clearFilters("probandlistentrytag_list");
		out = null;
		this.trialId = id;
		this.inputFieldMultiPicker.clear();
		bulkAddOptional = Settings.getBoolean(SettingCodes.PROBAND_LIST_ENTRY_TAG_OPTIONAL_PRESET, Bundle.SETTINGS, DefaultSettings.PROBAND_LIST_ENTRY_TAG_OPTIONAL_PRESET);
		bulkAddExcel = Settings.getBoolean(SettingCodes.PROBAND_LIST_ENTRY_TAG_EXCEL_PRESET, Bundle.SETTINGS, DefaultSettings.PROBAND_LIST_ENTRY_TAG_EXCEL_PRESET);
		bulkAddEcrf = Settings.getBoolean(SettingCodes.PROBAND_LIST_ENTRY_TAG_ECRF_PRESET, Bundle.SETTINGS, DefaultSettings.PROBAND_LIST_ENTRY_TAG_ECRF_PRESET);
		bulkAddStratification = Settings.getBoolean(SettingCodes.PROBAND_LIST_ENTRY_TAG_STRATIFICATION_PRESET, Bundle.SETTINGS,
				DefaultSettings.PROBAND_LIST_ENTRY_TAG_STRATIFICATION_PRESET);
		bulkAddRandomize = Settings.getBoolean(SettingCodes.PROBAND_LIST_ENTRY_TAG_RANDOMIZE_PRESET, Bundle.SETTINGS,
				DefaultSettings.PROBAND_LIST_ENTRY_TAG_RANDOMIZE_PRESET);
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
			out = WebUtil.getServiceLocator().getTrialService().deleteProbandListEntryTag(WebUtil.getAuthentication(), id);
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

	public String getFieldName() {
		return WebUtil.inputFieldIdToName(in.getFieldId());
	}

	public ProbandListEntryTagInVO getIn() {
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

	public ProbandListEntryTagOutVO getOut() {
		return out;
	}

	public ProbandListEntryTagLazyModel getProbandListEntryTagModel() {
		return probandListEntryTagModel;
	}

	public IDVO getSelectedProbandListEntryTag() {
		if (this.out != null) {
			return IDVO.transformVo(this.out);
		} else {
			return null;
		}
	}

	@Override
	public String getTitle() {
		if (out != null) {
			return Messages.getMessage(MessageCodes.PROBAND_LIST_ENTRY_TAG_TITLE, Long.toString(out.getId()), out.getField().getName());
		} else {
			return Messages.getString(MessageCodes.CREATE_NEW_PROBAND_LIST_ENTRY_TAG);
		}
	}

	@PostConstruct
	private void init() {
		Long id = WebUtil.getLongParamValue(GetParamNames.PROBAND_LIST_ENTRY_TAG_ID);
		if (id != null) {
			this.load(id);
		} else {
			initIn();
			initSets();
		}
	}

	private void initIn() {
		if (in == null) {
			in = new ProbandListEntryTagInVO();
		}
		if (out != null) {
			copyProbandListEntryTagOutToIn(in, out);
			trialId = in.getTrialId();
		} else {
			initProbandListEntryTagDefaultValues(in, trialId);
		}
	}

	private void initSets() {
		probandListEntryTagModel.setTrialId(in.getTrialId());
		probandListEntryTagModel.updateRowCount();
		trial = WebUtil.getTrial(this.in.getTrialId());
		if (WebUtil.isTrialLocked(trial)) {
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.TRIAL_LOCKED);
		}
	}

	public boolean isBulkAddEcrf() {
		return bulkAddEcrf;
	}

	public boolean isBulkAddExcel() {
		return bulkAddExcel;
	}

	public boolean isBulkAddOptional() {
		return bulkAddOptional;
	}

	public boolean isBulkAddRandomize() {
		return bulkAddRandomize;
	}

	public boolean isBulkAddStratification() {
		return bulkAddStratification;
	}

	@Override
	public boolean isCreateable() {
		return (in.getTrialId() == null ? false : !WebUtil.isTrialLocked(trial));
	}

	@Override
	public boolean isCreated() {
		return out != null;
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
			out = WebUtil.getServiceLocator().getTrialService().getProbandListEntryTag(WebUtil.getAuthentication(), id);
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

	public void moveDown(Long probandListEntryTagId) {
		try {
			out = WebUtil.getServiceLocator().getTrialService().moveProbandListEntryTag(WebUtil.getAuthentication(), probandListEntryTagId, PositionMovement.DOWN);
			initIn();
			initSets();
		} catch (ServiceException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
		} catch (IllegalArgumentException e) {
		}
	}

	public void moveFirst(Long probandListEntryTagId) {
		try {
			out = WebUtil.getServiceLocator().getTrialService().moveProbandListEntryTag(WebUtil.getAuthentication(), probandListEntryTagId, PositionMovement.FIRST);
			initIn();
			initSets();
		} catch (ServiceException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
		} catch (IllegalArgumentException e) {
		}
	}

	public void moveLast(Long probandListEntryTagId) {
		try {
			out = WebUtil.getServiceLocator().getTrialService().moveProbandListEntryTag(WebUtil.getAuthentication(), probandListEntryTagId, PositionMovement.LAST);
			initIn();
			initSets();
		} catch (ServiceException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
		} catch (IllegalArgumentException e) {
		}
	}

	public void moveTo() {
		Long probandListEntryTagId = WebUtil.getLongParamValue(GetParamNames.PROBAND_LIST_ENTRY_TAG_ID);
		Long targetPosition = WebUtil.getLongParamValue(GetParamNames.TARGET_POSITION);
		if (probandListEntryTagId != null && targetPosition != null) {
			try {
				Collection<ProbandListEntryTagOutVO> updated = WebUtil.getServiceLocator().getTrialService()
						.moveProbandListEntryTagTo(WebUtil.getAuthentication(), probandListEntryTagId, targetPosition);
				out = null;
				initIn();
				initSets();
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_INFO, MessageCodes.POSITIONS_UPDATE_SUCCESSFUL, updated.size());
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

	public void moveUp(Long probandListEntryTagId) {
		try {
			out = WebUtil.getServiceLocator().getTrialService().moveProbandListEntryTag(WebUtil.getAuthentication(), probandListEntryTagId, PositionMovement.UP);
			initIn();
			initSets();
		} catch (ServiceException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
		} catch (IllegalArgumentException e) {
		}
	}

	public final void normalizePositions() {
		actionPostProcess(normalizePositionsAction());
	}

	public String normalizePositionsAction() {
		try {
			Collection<ProbandListEntryTagOutVO> updated = WebUtil.getServiceLocator().getTrialService()
					.normalizeProbandListEntryTagPositions(WebUtil.getAuthentication(), trialId);
			out = null;
			initIn();
			initSets();
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_INFO, MessageCodes.POSITIONS_UPDATE_SUCCESSFUL, updated.size());
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

	@Override
	public String resetAction() {
		out = null;
		initIn();
		initSets();
		return RESET_OUTCOME;
	}

	public void setBulkAddEcrf(boolean bulkAddEcrf) {
		this.bulkAddEcrf = bulkAddEcrf;
	}

	public void setBulkAddExcel(boolean bulkAddExcel) {
		this.bulkAddExcel = bulkAddExcel;
	}

	public void setBulkAddOptional(boolean bulkAddOptional) {
		this.bulkAddOptional = bulkAddOptional;
	}

	public void setBulkAddRandomize(boolean bulkAddRandomize) {
		this.bulkAddRandomize = bulkAddRandomize;
	}

	public void setBulkAddStratification(boolean bulkAddStratification) {
		this.bulkAddStratification = bulkAddStratification;
	}

	public void setSelectedProbandListEntryTag(IDVO probandListEntryTag) {
		if (probandListEntryTag != null) {
			this.out = (ProbandListEntryTagOutVO) probandListEntryTag.getVo();
			this.initIn();
			initSets();
		}
	}

	@Override
	public String updateAction() {
		try {
			out = WebUtil.getServiceLocator().getTrialService().updateProbandListEntryTag(WebUtil.getAuthentication(), in);
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
