package org.phoenixctms.ctsms.web.model.inputfield;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.FileModule;
import org.phoenixctms.ctsms.enumeration.InputFieldType;
import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.js.JsUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.InputFieldImageVO;
import org.phoenixctms.ctsms.vo.InputFieldInVO;
import org.phoenixctms.ctsms.vo.InputFieldOutVO;
import org.phoenixctms.ctsms.vo.InputFieldTypeVO;
import org.phoenixctms.ctsms.vo.MimeTypeVO;
import org.phoenixctms.ctsms.web.model.InputFieldTypeSelector;
import org.phoenixctms.ctsms.web.model.InputFieldTypeSelectorListener;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.model.shared.inputfield.InputFieldConfig;
import org.phoenixctms.ctsms.web.model.shared.inputfield.InputFieldInVOConfig;
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
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

@ManagedBean
@ViewScoped
public class InputFieldBean extends ManagedBeanBase implements InputFieldTypeSelectorListener {

	public static void copyInputFieldOutToIn(InputFieldInVO in, InputFieldOutVO out) {
		if (in != null && out != null) {
			InputFieldTypeVO fieldTypeVO = out.getFieldType();
			in.setBooleanPreset(out.getBooleanPreset());
			in.setComment(out.getCommentL10nKey());
			in.setDatePreset(out.getDatePreset());
			in.setFieldType(fieldTypeVO == null ? null : fieldTypeVO.getType());
			in.setFloatLowerLimit(out.getFloatLowerLimit());
			in.setFloatPreset(out.getFloatPreset());
			in.setFloatUpperLimit(out.getFloatUpperLimit());
			in.setId(out.getId());
			in.setLongLowerLimit(out.getLongLowerLimit());
			in.setLongPreset(out.getLongPreset());
			in.setLongUpperLimit(out.getLongUpperLimit());
			in.setMaxDate(out.getMaxDate());
			in.setMaxSelections(out.getMaxSelections());
			in.setMaxTimestamp(out.getMaxTimestamp());
			in.setMinDate(out.getMinDate());
			in.setMinSelections(out.getMinSelections());
			in.setMinTimestamp(out.getMinTimestamp());
			in.setLocalized(out.getLocalized());
			in.setName(out.getNameL10nKey());
			in.setExternalId(out.getExternalId());
			in.setCategory(out.getCategory());
			in.setRegExp(out.getRegExp());
			in.setTextPreset(out.getTextPresetL10nKey());
			in.setTimestampPreset(out.getTimestampPreset());
			in.setTitle(out.getTitleL10nKey());
			in.setValidationErrorMsg(out.getValidationErrorMsgL10nKey());
			in.setVersion(out.getVersion());
			in.setLearn(out.getLearn());
			in.setStrict(out.getStrict());
			in.setWidth(out.getWidth());
			in.setHeight(out.getHeight());
			in.setFileName(null);
			in.setMimeType(null);
			in.setDatas(null);
			in.setMinTime(out.getMinTime());
			in.setMaxTime(out.getMaxTime());
			in.setTimePreset(out.getTimePreset());
		}
	}

	public static void initInputFieldDefaultValues(InputFieldInVO in) {
		if (in != null) {
			in.setBooleanPreset(Settings.getBoolean(SettingCodes.INPUT_FIELD_BOOLEAN_PRESET_PRESET, Bundle.SETTINGS, DefaultSettings.INPUT_FIELD_BOOLEAN_PRESET_PRESET));
			in.setComment(Messages.getString(MessageCodes.INPUT_FIELD_COMMENT_PRESET));
			in.setDatePreset(DefaultSettings.INPUT_FIELD_DATE_PRESET_PRESET);
			in.setFieldType(Settings.getInputFieldType(SettingCodes.INPUT_FIELD_FIELD_TYPE_PRESET, Bundle.SETTINGS, DefaultSettings.INPUT_FIELD_FIELD_TYPE_PRESET));
			in.setFloatLowerLimit(Settings.getFloatNullable(SettingCodes.INPUT_FIELD_FLOAT_LOWER_LIMIT_PRESET, Bundle.SETTINGS,
					DefaultSettings.INPUT_FIELD_FLOAT_LOWER_LIMIT_PRESET));
			in.setFloatPreset(Settings.getFloatNullable(SettingCodes.INPUT_FIELD_FLOAT_PRESET_PRESET, Bundle.SETTINGS, DefaultSettings.INPUT_FIELD_FLOAT_PRESET_PRESET));
			in.setFloatUpperLimit(Settings.getFloatNullable(SettingCodes.INPUT_FIELD_FLOAT_UPPER_LIMIT_PRESET, Bundle.SETTINGS,
					DefaultSettings.INPUT_FIELD_FLOAT_UPPER_LIMIT_PRESET));
			in.setId(null);
			in.setLongLowerLimit(Settings.getLongNullable(SettingCodes.INPUT_FIELD_LONG_LOWER_LIMIT_PRESET, Bundle.SETTINGS, DefaultSettings.INPUT_FIELD_LONG_LOWER_LIMIT_PRESET));
			in.setLongPreset(Settings.getLongNullable(SettingCodes.INPUT_FIELD_LONG_PRESET_PRESET, Bundle.SETTINGS, DefaultSettings.INPUT_FIELD_LONG_PRESET_PRESET));
			in.setLongUpperLimit(Settings.getLongNullable(SettingCodes.INPUT_FIELD_LONG_UPPER_LIMIT_PRESET, Bundle.SETTINGS, DefaultSettings.INPUT_FIELD_LONG_UPPER_LIMIT_PRESET));
			in.setMaxDate(DefaultSettings.INPUT_FIELD_MAX_DATE_PRESET);
			in.setMaxSelections(Settings.getIntNullable(SettingCodes.INPUT_FIELD_MAX_SELECTIONS_PRESET, Bundle.SETTINGS, DefaultSettings.INPUT_FIELD_MAX_SELECTIONS_PRESET));
			in.setMaxTimestamp(DefaultSettings.INPUT_FIELD_MAX_TIMESTAMP_PRESET);
			in.setMinDate(DefaultSettings.INPUT_FIELD_MIN_DATE_PRESET);
			in.setMinSelections(Settings.getIntNullable(SettingCodes.INPUT_FIELD_MIN_SELECTIONS_PRESET, Bundle.SETTINGS, DefaultSettings.INPUT_FIELD_MIN_SELECTIONS_PRESET));
			in.setMinTimestamp(DefaultSettings.INPUT_FIELD_MIN_TIMESTAMP_PRESET);
			in.setLocalized(Settings.getBoolean(SettingCodes.INPUT_FIELD_LOCALIZED_PRESET, Bundle.SETTINGS, DefaultSettings.INPUT_FIELD_LOCALIZED_PRESET));
			in.setName(Messages.getString(MessageCodes.INPUT_FIELD_NAME_PRESET));
			in.setExternalId(Messages.getString(MessageCodes.INPUT_FIELD_EXTERNAL_ID_PRESET));
			in.setCategory(Messages.getString(MessageCodes.INPUT_FIELD_CATEGORY_PRESET));
			in.setRegExp(Settings.getString(SettingCodes.INPUT_FIELD_REGEXP_PRESET, Bundle.SETTINGS, DefaultSettings.INPUT_FIELD_REGEXP_PRESET));
			in.setTextPreset(Settings.getString(SettingCodes.INPUT_FIELD_TEXT_PRESET_PRESET, Bundle.SETTINGS, DefaultSettings.INPUT_FIELD_TEXT_PRESET_PRESET));
			in.setTimestampPreset(DefaultSettings.INPUT_FIELD_TIMESTAMP_PRESET_PRESET);
			in.setTitle(Messages.getString(MessageCodes.INPUT_FIELD_TITLE_PRESET));
			in.setValidationErrorMsg(Messages.getString(MessageCodes.INPUT_FIELD_VALIDATION_ERROR_MSG_PRESET));
			in.setLearn(Settings.getBoolean(SettingCodes.INPUT_FIELD_LEARN_PRESET, Bundle.SETTINGS, DefaultSettings.INPUT_FIELD_LEARN_PRESET));
			in.setStrict(Settings.getBoolean(SettingCodes.INPUT_FIELD_STRICT_PRESET, Bundle.SETTINGS, DefaultSettings.INPUT_FIELD_STRICT_PRESET));
			in.setVersion(null);
			in.setWidth(Settings.getLong(SettingCodes.INPUT_FIELD_SKETCH_WIDTH_PRESET, Bundle.SETTINGS, DefaultSettings.INPUT_FIELD_SKETCH_WIDTH_PRESET));
			in.setHeight(Settings.getLong(SettingCodes.INPUT_FIELD_SKETCH_HEIGHT_PRESET, Bundle.SETTINGS, DefaultSettings.INPUT_FIELD_SKETCH_HEIGHT_PRESET));
			in.setFileName(null);
			in.setMimeType(null);
			in.setDatas(null);
			in.setMinTime(DefaultSettings.INPUT_FIELD_MIN_TIME_PRESET);
			in.setMaxTime(DefaultSettings.INPUT_FIELD_MAX_TIME_PRESET);
			in.setTimePreset(DefaultSettings.INPUT_FIELD_TIME_PRESET_PRESET);
		}
	}

	private InputFieldInVO in;
	private InputFieldInVOConfig config;
	private InputFieldOutVO out;
	private ArrayList<SelectItem> booleans;
	private InputFieldTypeSelector fieldType;
	private HashMap<String, Long> tabCountMap;
	private HashMap<String, String> tabTitleMap;
	private String allowTypes;
	private Integer uploadSizeLimit;
	private static final int FIELD_TYPE_PROPERTY_ID = 1;

	public InputFieldBean() {
		super();
		tabCountMap = new HashMap<String, Long>();
		tabTitleMap = new HashMap<String, String>();
		config = new InputFieldInVOConfig();
		setFieldType(new InputFieldTypeSelector(this, FIELD_TYPE_PROPERTY_ID));
	}

	@Override
	public String addAction()
	{
		InputFieldInVO backup = new InputFieldInVO(in);
		in.setId(null);
		in.setVersion(null);
		clearLimitsAndPresets();
		sanitizeUnsetVals();
		try {
			out = WebUtil.getServiceLocator().getInputFieldService().addInputField(WebUtil.getAuthentication(), in);
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

	public final void addClone() {
		actionPostProcess(addCloneAction());
	}

	public String addCloneAction()
	{
		try {
			out = WebUtil.getServiceLocator().getInputFieldService().cloneInputField(WebUtil.getAuthentication(), in.getId(), in.getName());
			initIn();
			initSets();
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_INFO, MessageCodes.CLONE_ADD_OPERATION_SUCCESSFUL, out.getName());
			return CLONE_ADD_OUTCOME;
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
		RequestContext requestContext = RequestContext.getCurrentInstance();
		if (requestContext != null) {
			requestContext.addCallbackParam(JSValues.AJAX_WINDOW_TITLE_BASE64.toString(), JsUtil.encodeBase64(getTitle(operationSuccess), false));
			requestContext.addCallbackParam(JSValues.AJAX_WINDOW_NAME.toString(), getWindowName(operationSuccess));
			requestContext.addCallbackParam(JSValues.AJAX_OPERATION_SUCCESS.toString(), operationSuccess);
			requestContext.addCallbackParam(JSValues.AJAX_ROOT_ENTITY_CREATED.toString(), out != null);
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_SELECTION_SET_VALUE_TAB_TITLE_BASE64, JSValues.AJAX_SELECTION_SET_VALUE_COUNT,
					MessageCodes.SELECTION_SET_VALUES_TAB_TITLE, MessageCodes.SELECTION_SET_VALUES_TAB_TITLE_WITH_COUNT,
					tabCountMap.get(JSValues.AJAX_SELECTION_SET_VALUE_COUNT.toString()));
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_INPUT_FIELD_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_INPUT_FIELD_JOURNAL_ENTRY_COUNT,
					MessageCodes.INPUT_FIELD_JOURNAL_TAB_TITLE, MessageCodes.INPUT_FIELD_JOURNAL_TAB_TITLE_WITH_COUNT,
					tabCountMap.get(JSValues.AJAX_INPUT_FIELD_JOURNAL_ENTRY_COUNT.toString()));
		}
	}

	@Override
	protected String changeAction(Long id) {
		out = null;
		if (id != null) {
			try {
				out = WebUtil.getServiceLocator().getInputFieldService().getInputField(WebUtil.getAuthentication(), id);
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
		initIn();
		initSets();
		return CHANGE_OUTCOME;
	}

	public void clearImage() {
		in.setFileName(null);
		in.setMimeType(null);
		in.setDatas(null);
	}

	private void clearLimitsAndPresets() {
		InputFieldType fieldType = in.getFieldType();
		if (!(InputFieldType.SINGLE_LINE_TEXT.equals(fieldType) || InputFieldType.MULTI_LINE_TEXT.equals(fieldType))) {
			in.setRegExp(null);
			in.setTextPreset(null);
		}
		if (!(InputFieldType.SELECT_MANY_H.equals(fieldType) || InputFieldType.SELECT_MANY_V.equals(fieldType) || InputFieldType.SKETCH.equals(fieldType))) {
			in.setMinSelections(null);
			in.setMaxSelections(null);
		}
		if (!(InputFieldType.SELECT_ONE_DROPDOWN.equals(fieldType) || InputFieldType.SELECT_ONE_RADIO_H.equals(fieldType) || InputFieldType.SELECT_ONE_RADIO_V.equals(fieldType))) {
		} else {
			in.setValidationErrorMsg(null);
		}
		if (!(InputFieldType.AUTOCOMPLETE.equals(fieldType))) {
			in.setStrict(false);
			in.setLearn(false);
		} else {
			if (in.getLearn()) {
				in.setStrict(false);
			}
			in.setValidationErrorMsg(null);
			in.setTextPreset(null);
		}
		if (!(InputFieldType.CHECKBOX.equals(fieldType))) {
			in.setBooleanPreset(false);
		} else {
			in.setValidationErrorMsg(null);
		}
		if (!(InputFieldType.INTEGER.equals(fieldType))) {
			in.setLongLowerLimit(null);
			in.setLongUpperLimit(null);
			in.setLongPreset(null);
		}
		if (!(InputFieldType.FLOAT.equals(fieldType))) {
			in.setFloatLowerLimit(null);
			in.setFloatUpperLimit(null);
			in.setFloatPreset(null);
		}
		if (!(InputFieldType.DATE.equals(fieldType))) {
			in.setMinDate(null);
			in.setMaxDate(null);
			in.setDatePreset(null);
		}
		if (!(InputFieldType.TIME.equals(fieldType))) {
			in.setMinTime(null);
			in.setMaxTime(null);
			in.setTimePreset(null);
		}
		if (!(InputFieldType.TIMESTAMP.equals(fieldType))) {
			in.setMinTimestamp(null);
			in.setMaxTimestamp(null);
			in.setTimestampPreset(null);
		}
		if (!(InputFieldType.SKETCH.equals(fieldType))) {
			in.setWidth(null);
			in.setHeight(null);
			in.setFileName(null);
			in.setMimeType(null);
			in.setDatas(null);
		}
	}

	public List<String> completeCategory(String query) {
		in.setCategory(query);
		Collection<String> categories = null;
		try {
			categories = WebUtil.getServiceLocator().getSelectionSetService().getInputFieldCategories(WebUtil.getAuthentication(), null, query, null);
		} catch (ServiceException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
		} catch (IllegalArgumentException e) {
		}
		if (categories != null) {
			try {
				return ((List<String>) categories);
			} catch (ClassCastException e) {
			}
		}
		return new ArrayList<String>();
	}

	@Override
	public String deleteAction() {
		return deleteAction(in.getId());
	}

	@Override
	public String deleteAction(Long id) {
		try {
			out = WebUtil.getServiceLocator().getInputFieldService().deleteInputField(WebUtil.getAuthentication(), id,
					Settings.getBoolean(SettingCodes.INPUT_FIELD_DEFERRED_DELETE, Bundle.SETTINGS, DefaultSettings.INPUT_FIELD_DEFERRED_DELETE),
					false);
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

	public String getAllowTypes() {
		return allowTypes;
	}

	public ArrayList<SelectItem> getBooleans() {
		return booleans;
	}

	public InputFieldConfig getConfig() {
		return config;
	}

	public InputFieldTypeSelector getFieldType() {
		return fieldType;
	}

	public String getFileDownloadLinkLabel() {
		if (isHasImage()) {
			return in.getFileName();
		} else {
			return Messages.getString(MessageCodes.NO_FILE_LINK_LABEL);
		}
	}

	public StreamedContent getFileStreamedContent() throws Exception {
		if (isHasImage()) {
			return new DefaultStreamedContent(new ByteArrayInputStream(in.getDatas()), in.getMimeType(), in.getFileName());
		}
		return null;
	}

	public InputFieldInVO getIn() {
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

	public InputFieldOutVO getOut() {
		return out;
	}

	public String getTabTitle(String tab) {
		return tabTitleMap.get(tab);
	}

	@Override
	public String getTitle() {
		return getTitle(WebUtil.getLongParamValue(GetParamNames.INPUT_FIELD_ID) == null);
	}

	private String getTitle(boolean operationSuccess) {
		if (out != null) {
			return Messages.getMessage(out.getDeferredDelete() ? MessageCodes.DELETED_TITLE : MessageCodes.INPUT_FIELD_TITLE, Long.toString(out.getId()),
					CommonUtil.inputFieldOutVOToString(out));
		} else {
			return Messages.getString(operationSuccess ? MessageCodes.CREATE_NEW_INPUT_FIELD : MessageCodes.ERROR_LOADING_INPUT_FIELD);
		}
	}

	@Override
	public InputFieldType getType(int property) {
		switch (property) {
			case FIELD_TYPE_PROPERTY_ID:
				return this.in.getFieldType();
			default:
				return InputFieldTypeSelectorListener.NO_SELECTION_INPUT_FIELD_TYPE;
		}
	}

	public Integer getUploadSizeLimit() {
		return uploadSizeLimit;
	}

	@Override
	public String getWindowName() {
		return getWindowName(WebUtil.getLongParamValue(GetParamNames.INPUT_FIELD_ID) == null);
	}

	private String getWindowName(boolean operationSuccess) {
		if (out != null) {
			return String.format(JSValues.INPUT_FIELD_ENTITY_WINDOW_NAME.toString(), Long.toString(out.getId()), WebUtil.getWindowNameUniqueToken());
		} else {
			if (operationSuccess) {
				return String.format(JSValues.INPUT_FIELD_ENTITY_WINDOW_NAME.toString(), JSValues.NEW_ENTITY_WINDOW_NAME_SUFFIX.toString(), "");
			} else {
				Long inputFieldId = WebUtil.getLongParamValue(GetParamNames.INPUT_FIELD_ID);
				if (inputFieldId != null) {
					return String.format(JSValues.INPUT_FIELD_ENTITY_WINDOW_NAME.toString(), inputFieldId.toString(), WebUtil.getWindowNameUniqueToken());
				} else {
					return String.format(JSValues.INPUT_FIELD_ENTITY_WINDOW_NAME.toString(), JSValues.NEW_ENTITY_WINDOW_NAME_SUFFIX.toString(), "");
				}
			}
		}
	}

	public void handleCategorySelect(SelectEvent event) {
		in.setCategory((String) event.getObject());
	}

	public void handleFileUpload(FileUploadEvent event) {
		UploadedFile uploadedFile = event.getFile();
		in.setFileName(uploadedFile.getFileName());
		in.setMimeType(uploadedFile.getContentType());
		in.setDatas(uploadedFile.getContents());
		in.setWidth(null);
		in.setHeight(null);
		addOperationSuccessMessage(MessageCodes.UPLOAD_OPERATION_SUCCESSFUL);
	}

	public void handleLearnChange() {
		if (in.getLearn()) {
			in.setStrict(false);
		}
	}

	@PostConstruct
	private void init() {
		Long id = WebUtil.getLongParamValue(GetParamNames.INPUT_FIELD_ID);
		if (id != null) {
			this.load(id);
		} else {
			this.change();
		}
	}

	private void initIn() {
		if (in == null) {
			in = new InputFieldInVO();
		}
		if (out != null) {
			copyInputFieldOutToIn(in, out);
		} else {
			initInputFieldDefaultValues(in);
		}
	}

	private void initSets() {
		InputFieldImageVO image = null;
		if (in.getId() != null) {
			try {
				image = WebUtil.getServiceLocator().getToolsService().getInputFieldImage(in.getId());
			} catch (ServiceException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			} catch (AuthorisationException e) {
			} catch (IllegalArgumentException e) {
			}
			if (image != null) {
				MimeTypeVO contentTypeVO = image.getContentType();
				in.setFileName(image.getFileName());
				in.setMimeType(contentTypeVO == null ? null : contentTypeVO.getMimeType());
				in.setDatas(image.getDatas());
			}
		}
		allowTypes = WebUtil.getAllowedFileExtensionsPattern(FileModule.INPUT_FIELD_IMAGE, true);
		uploadSizeLimit = null;
		try {
			uploadSizeLimit = WebUtil.getServiceLocator().getToolsService().getInputFieldImageUploadSizeLimit();
		} catch (ServiceException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
		} catch (IllegalArgumentException e) {
		}
		tabCountMap.clear();
		tabTitleMap.clear();
		Long count = (out == null ? null : WebUtil.getSelectionSetValueCount(in.getId()));
		tabCountMap.put(JSValues.AJAX_SELECTION_SET_VALUE_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_SELECTION_SET_VALUE_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.SELECTION_SET_VALUES_TAB_TITLE, MessageCodes.SELECTION_SET_VALUES_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null : WebUtil.getJournalCount(JournalModule.INPUT_FIELD_JOURNAL, in.getId()));
		tabCountMap.put(JSValues.AJAX_INPUT_FIELD_JOURNAL_ENTRY_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_INPUT_FIELD_JOURNAL_ENTRY_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.INPUT_FIELD_JOURNAL_TAB_TITLE, MessageCodes.INPUT_FIELD_JOURNAL_TAB_TITLE_WITH_COUNT, count));
		config.setInputField(in);
		if (booleans == null) {
			booleans = WebUtil.getBooleans(false, false);
		}
		if (out != null && out.isDeferredDelete()) { // && Settings.getBoolean(SettingCodes.INPUT_FIELD_DEFERRED_DELETE, Bundle.SETTINGS,
			// DefaultSettings.INPUT_FIELD_DEFERRED_DELETE)) {
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.MARKED_FOR_DELETION);
		}
	}

	public boolean isCloneable() {
		return isCreated();
	}

	@Override
	public boolean isCreateable() {
		return true;
	}

	@Override
	public boolean isCreated() {
		return out != null;
	}

	public boolean isHasImage() {
		byte[] data = in.getDatas();
		if (data != null && data.length > 0) {
			return true;
		}
		return false;
	}

	public boolean isTabEmphasized(String tab) {
		return WebUtil.isTabCountEmphasized(tabCountMap.get(tab), JSValues.AJAX_INQUIRY_VALUE_COUNT.toString().equals(tab));
	}

	@Override
	public String loadAction() {
		return loadAction(in.getId());
	}

	@Override
	public String loadAction(Long id) {
		out = null;
		try {
			out = WebUtil.getServiceLocator().getInputFieldService().getInputField(WebUtil.getAuthentication(), id);
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

	private void sanitizeUnsetVals() {
		if (in.getRegExp() != null && in.getRegExp().length() == 0) {
			in.setRegExp(null);
		}
		if (in.getTextPreset() != null && in.getTextPreset().length() == 0) {
			in.setTextPreset(null);
		}
		if (in.getValidationErrorMsg() != null && in.getValidationErrorMsg().length() == 0) {
			in.setValidationErrorMsg(null);
		}
	}

	public void setFieldType(InputFieldTypeSelector fieldType) {
		this.fieldType = fieldType;
	}

	@Override
	public void setType(int property, InputFieldType fieldType) {
		switch (property) {
			case FIELD_TYPE_PROPERTY_ID:
				this.in.setFieldType(fieldType);
				break;
			default:
		}
	}

	@Override
	public String updateAction() {
		InputFieldInVO backup = new InputFieldInVO(in);
		clearLimitsAndPresets();
		sanitizeUnsetVals();
		try {
			out = WebUtil.getServiceLocator().getInputFieldService().updateInputField(WebUtil.getAuthentication(), in);
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
