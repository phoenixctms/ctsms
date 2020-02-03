package org.phoenixctms.ctsms.web.model.shared.inputfield;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.Color;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.js.JsUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.JavaScriptCompressor;
import org.phoenixctms.ctsms.vo.InputFieldOutVO;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueOutVO;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
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

public abstract class InputModel extends InputFieldOutVOConfigBase {

	private final static String INPUT_FIELD_CREATED_COLOR_STYLECLASS_PREFIX = "ctsms-inputfield-created-color-";
	private final static long MAX_INPUT_FIELD_CREATED_COLORS = 16l;
	private int rowIndex;
	private String errorMessage;
	protected InputFieldOutVO inputField;

	protected InputModel() {
		errorMessage = null;
		inputField = null;
	}

	protected void actionPostProcess(Object out) {
		if (out == null) {
		} else if (out instanceof Collection) {
			if (((Collection) out).size() > 0) {
				appendRequestContextCallbackArgs(out);
			}
		} else {
			ArrayList collection = new ArrayList();
			collection.add(out);
			appendRequestContextCallbackArgs(collection);
		}
	}

	private void appendRequestContextCallbackArgs(Object out) {
		RequestContext requestContext = RequestContext.getCurrentInstance();
		if (requestContext != null) {
			requestContext.addCallbackParam(JSValues.AJAX_INPUT_FIELD_VARIABLE_VALUES_BASE64.toString(),
					JsUtil.encodeBase64(JsUtil.inputFieldVariableValueToJson(out), false));
		}
	}

	public abstract void applyPresets();

	protected abstract Object check();

	public final void check(ActionEvent actionEvent) {
		actionPostProcess(check());
	}

	public List<String> completeValue(String query) {
		setTextValue(query);
		Collection<String> values = null;
		if (inputField != null && isAutocomplete()) {
			try {
				values = WebUtil.getServiceLocator().getToolsService().completeInputFieldSelectionSetValueValue(WebUtil.getAuthentication(), query, inputField.getId(), null);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
			if (values != null) {
				try {
					return ((List<String>) values);
				} catch (ClassCastException e) {
				}
			}
		}
		return new ArrayList<String>();
	}

	public abstract boolean getBooleanValue();

	public String getColor(boolean optional) {
		Color fieldColor = getFieldColor();
		if (fieldColor != null || (isEditable() && !isDisabled())) {
			if (fieldColor != null) {
				return WebUtil.colorToStyleClass(fieldColor);
			} else {
				if (isCreated()) {
					Color createdColor = Settings.getColor(SettingCodes.INPUT_MODEL_CREATED_COLOR, Bundle.SETTINGS, DefaultSettings.INPUT_MODEL_CREATED_COLOR);
					if (createdColor != null) {
						return WebUtil.colorToStyleClass(createdColor);
					} else {
						return INPUT_FIELD_CREATED_COLOR_STYLECLASS_PREFIX + Math.min(getVersion() == null ? 0l : getVersion(), MAX_INPUT_FIELD_CREATED_COLORS);
					}
				} else {
					if (optional) {
						return "";
					} else {
						return WebUtil.colorToStyleClass(Settings.getColor(SettingCodes.INPUT_MODEL_NEW_COLOR, Bundle.SETTINGS, DefaultSettings.INPUT_MODEL_NEW_COLOR));
					}
				}
			}
		} else {
			return WebUtil.colorToStyleClass(Settings.getColor(SettingCodes.INPUT_MODEL_NOT_EDITABLE_COLOR, Bundle.SETTINGS, DefaultSettings.INPUT_MODEL_NOT_EDITABLE_COLOR));
		}
	}

	public abstract String getComment();

	public abstract Date getDateValue();

	public String getErrorMessage() {
		return errorMessage;
	}

	public String getEventJSCall(String functionName) {
		if (isJsVariable()) {
			StringBuilder sb = new StringBuilder(functionName);
			sb.append("('");
			sb.append(getJsVariableName());
			sb.append("',");
			sb.append(getSeriesIndex());
			sb.append(',');
			sb.append(getWidgetVar());
			sb.append(",");
			sb.append(WebUtil.quoteJSString(getOutputId(), true));
			sb.append(")");
			return sb.toString();
		}
		return null;
	}

	protected Color getFieldColor() {
		return null;
	}

	public abstract Float getFloatValue();

	public int getHeight() {
		if (inputField != null) {
			Long height = inputField.getHeight();
			if (height != null) {
				return CommonUtil.safeLongToInt(height);
			}
		}
		return 0;
	}

	public List<String> getInkRegions() throws UnsupportedEncodingException {
		if (inputField != null) {
			Collection<InputFieldSelectionSetValueOutVO> selectionSetValueVOs = inputField.getSelectionSetValues();
			ArrayList<String> inkRegions = new ArrayList<String>(selectionSetValueVOs.size());
			Iterator<InputFieldSelectionSetValueOutVO> it = selectionSetValueVOs.iterator();
			while (it.hasNext()) {
				inkRegions.add(CommonUtil.inkValueToString(it.next().getInkRegions()));
			}
			return inkRegions;
		}
		return new ArrayList<String>();
	}

	protected abstract String getInkValue() throws UnsupportedEncodingException;

	public String getInkValueWithSelections() throws JSONException, UnsupportedEncodingException {
		JSONArray inkValueJson;
		String inkValue = getInkValue();
		if (inkValue != null && inkValue.length() > 0) {
			inkValueJson = new JSONArray(inkValue);
		} else {
			inkValueJson = new JSONArray();
		}
		StringBuilder selectionSetValueIds = new StringBuilder();
		HashMap<String, String> selectionSetValueIdToStrokesIdMap = getSelectionSetValueIdToStrokesIdMap();
		Iterator<String> it = getSelectionValueIds().iterator();
		while (it.hasNext()) {
			if (selectionSetValueIds.length() > 0) {
				selectionSetValueIds.append(WebUtil.ID_SEPARATOR_STRING);
			}
			String selectionSetValueId = it.next();
			if (selectionSetValueIdToStrokesIdMap.containsKey(selectionSetValueId)) {
				selectionSetValueIds.append(selectionSetValueIdToStrokesIdMap.get(selectionSetValueId));
			}
		}
		inkValueJson.put(selectionSetValueIds.toString());
		StringWriter output = new StringWriter();
		inkValueJson.write(output);
		return output.toString();
	}

	@Override
	public InputFieldOutVO getInputField() {
		return inputField;
	}

	public abstract String getJsOutputExpression();

	public abstract String getJsValueExpression();

	public abstract String getJsVariableName();

	protected abstract String getInputTitle();

	public final String getLabel() {
		String title = getInputTitle();
		if (CommonUtil.isEmptyString(title)) {
			return super.getFieldTitle();
		} else {
			return title;
		}
	}

	public abstract Long getLongValue();

	public abstract String getModifiedAnnotation();

	public abstract String getOutputId();

	public abstract String getReasonForChange();

	public final String getRequiredMessage() {
		if (inputField != null) {
			if (!this.isOptional()) {
				return Messages.getMessage(MessageCodes.INPUT_FIELD_REQUIRED_MESSAGE, inputField.getName());
			}
		}
		return null;
	}

	public int getRowIndex() {
		return rowIndex;
	}

	private final HashMap<String, String> getSelectionSetValueIdToStrokesIdMap() {
		if (inputField != null) {
			Collection<InputFieldSelectionSetValueOutVO> selectionSetValueVOs = inputField.getSelectionSetValues();
			HashMap<String, String> selectionSetValueIdToStrokesIdMap = new HashMap<String, String>(selectionSetValueVOs.size());
			Iterator<InputFieldSelectionSetValueOutVO> it = selectionSetValueVOs.iterator();
			while (it.hasNext()) {
				InputFieldSelectionSetValueOutVO selectionSetValueVO = it.next();
				selectionSetValueIdToStrokesIdMap.put(Long.toString(selectionSetValueVO.getId()), selectionSetValueVO.getStrokesId());
			}
			return selectionSetValueIdToStrokesIdMap;
		}
		return new HashMap<String, String>();
	}

	public final ArrayList<SelectItem> getSelectionSetValues() {
		if (inputField != null) {
			Collection<InputFieldSelectionSetValueOutVO> selectionSetValueVOs = inputField.getSelectionSetValues();
			ArrayList<SelectItem> selectionSetValues = new ArrayList<SelectItem>(selectionSetValueVOs.size());
			Iterator<InputFieldSelectionSetValueOutVO> it = selectionSetValueVOs.iterator();
			while (it.hasNext()) {
				InputFieldSelectionSetValueOutVO selectionSetValueVO = it.next();
				selectionSetValues.add(new SelectItem(Long.toString(selectionSetValueVO.getId()), selectionSetValueVO.getName()));
			}
			return selectionSetValues;
		}
		return new ArrayList<SelectItem>();
	}

	private final HashMap<String, String> getSelectionSetValueStrokesIdToIdMap() {
		if (inputField != null) {
			Collection<InputFieldSelectionSetValueOutVO> selectionSetValueVOs = inputField.getSelectionSetValues();
			HashMap<String, String> selectionSetValueStrokesIdToIdMap = new HashMap<String, String>(selectionSetValueVOs.size());
			Iterator<InputFieldSelectionSetValueOutVO> it = selectionSetValueVOs.iterator();
			while (it.hasNext()) {
				InputFieldSelectionSetValueOutVO selectionSetValueVO = it.next();
				selectionSetValueStrokesIdToIdMap.put(selectionSetValueVO.getStrokesId(), Long.toString(selectionSetValueVO.getId()));
			}
			return selectionSetValueStrokesIdToIdMap;
		}
		return new HashMap<String, String>();
	}

	public abstract Long getSelectionValueId();

	public abstract List<String> getSelectionValueIds();

	public abstract Long getSeriesIndex();

	public abstract String getStatusComment();

	public abstract String getTextValue();

	public abstract Date getTimestampValue();

	public abstract Date getTimeValue();

	@Override
	public final String getTitle() {
		String title = getLabel();
		if (isOptional()) {
			return Messages.getMessage(MessageCodes.OPTIONAL_INPUT_FIELD_TITLE, title);
		} else {
			return Messages.getMessage(MessageCodes.REQUIRED_INPUT_FIELD_TITLE, title);
		}
	}

	public final String getTooltip() {
		if (inputField != null) {
			String validationErrorMsg = inputField.getValidationErrorMsg();
			String fieldTypeName;
			if (isTimestamp()) {
				if (inputField.getUserTimeZone()) {
					fieldTypeName = Messages.getMessage(MessageCodes.INPUT_FIELD_TOOLTIP_FIELDTYPE_USER_TIMEZONE, inputField.getFieldType().getName(),
							CommonUtil.timeZoneToDisplayString(WebUtil.getTimeZone(), WebUtil.getLocale()));
				} else {
					fieldTypeName = Messages.getMessage(MessageCodes.INPUT_FIELD_TOOLTIP_FIELDTYPE_SYSTEM_TIMEZONE, inputField.getFieldType().getName(),
							CommonUtil.timeZoneToDisplayString(WebUtil.getDefaultTimeZone(), WebUtil.getLocale()));
				}
			} else {
				fieldTypeName = Messages.getMessage(MessageCodes.INPUT_FIELD_TOOLTIP_FIELDTYPE, inputField.getFieldType().getName());
			}
			if (this.isOptional()) {
				if (validationErrorMsg != null && validationErrorMsg.length() > 0) {
					return Messages.getMessage(MessageCodes.INPUT_FIELD_OPTIONAL_VALIDATION_TOOLTIP, fieldTypeName, validationErrorMsg);
				} else {
					return Messages.getMessage(MessageCodes.INPUT_FIELD_OPTIONAL_NO_VALIDATION_TOOLTIP, fieldTypeName);
				}
			} else {
				if (validationErrorMsg != null && validationErrorMsg.length() > 0) {
					return Messages.getMessage(MessageCodes.INPUT_FIELD_REQUIRED_VALIDATION_TOOLTIP, fieldTypeName, validationErrorMsg);
				} else {
					return Messages.getMessage(MessageCodes.INPUT_FIELD_REQUIRED_NO_VALIDATION_TOOLTIP, fieldTypeName);
				}
			}
		}
		return "";
	}

	public abstract String getUniqueName();

	public abstract String getValueString();

	protected abstract Long getVersion();

	public abstract String getWidgetVar();

	public int getWidth() {
		if (inputField != null) {
			Long width = inputField.getWidth();
			if (width != null) {
				return CommonUtil.safeLongToInt(width);
			}
		}
		return 0;
	}

	public abstract boolean isAuditTrail();

	public abstract boolean isBooleanValue();

	public abstract boolean isCollapsed();

	public final boolean isCommentEmpty() {
		return CommonUtil.isEmptyString(getComment());
	}

	public abstract boolean isCreated();

	public boolean isDeferredDelete() {
		if (inputField != null) {
			return inputField.getDeferredDelete();
		}
		return false;
	}

	public abstract boolean isDisabled();

	public abstract boolean isDummy();

	public boolean isEditable() {
		return !isDisabled();
	}

	public boolean isError() {
		if (errorMessage != null && errorMessage.length() > 0) {
			return true;
		}
		return false;
	}

	public final boolean isFieldCommentEmpty() {
		return CommonUtil.isEmptyString(getFieldComment());
	}

	public final boolean isHasOutput() {
		return !CommonUtil.isEmptyString(getJsVariableName());
	}

	public final boolean isJsValueExpressionEmpty() {
		return CommonUtil.isEmptyString(JavaScriptCompressor.compress(getJsValueExpression()));
	}

	public abstract boolean isJsVariable();

	public abstract boolean isOptional();

	public abstract boolean isReasonForChangeRequired();

	public abstract boolean isSeries();

	public abstract boolean isShowToolbar();

	public final boolean isShowTooltip() {
		return !isCheckBox();
	}

	public final boolean isStatusCommentEmpty() {
		return CommonUtil.isEmptyString(getStatusComment());
	}

	protected abstract Object load();

	public final void load(ActionEvent actionEvent) {
		actionPostProcess(load());
	}

	protected abstract Object reset();

	public final void reset(ActionEvent actionEvent) {
		actionPostProcess(reset());
	}

	public abstract void setBooleanValue(boolean value);

	public abstract void setDateValue(Date value);

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	protected void setErrorMessageFromServiceException(Object data, String exceptionMessage) {
		HashMap<Long, String> errorMessagesMap;
		try {
			errorMessagesMap = (HashMap<Long, String>) data;
		} catch (ClassCastException e) {
			errorMessagesMap = null;
		}
		if (errorMessagesMap != null && errorMessagesMap.size() > 0) {
			String errorMessage = errorMessagesMap.values().iterator().next();
			setErrorMessage(errorMessage != null && errorMessage.length() > 0 ? errorMessage : exceptionMessage);
		} else {
			setErrorMessage(null);
		}
	}

	protected void setField(InputFieldOutVO inputField) {
		this.inputField = inputField;
	}

	public abstract void setFloatValue(Float value);

	protected abstract void setInkValue(String value) throws UnsupportedEncodingException;

	public void setInkValueWithSelections(String value) throws JSONException, UnsupportedEncodingException {
		if (value != null && value.length() > 0) {
			JSONArray inkValueJson = new JSONArray(value);
			if (inkValueJson.length() > 0) {
				String strokesIdsString;
				try {
					inkValueJson.getJSONObject(inkValueJson.length() - 1);
					strokesIdsString = null;
				} catch (JSONException e) {
					strokesIdsString = inkValueJson.getString(inkValueJson.length() - 1);
					inkValueJson.remove(inkValueJson.length() - 1);
				}
				StringWriter inkValue = new StringWriter();
				inkValueJson.write(inkValue);
				setInkValue(inkValue.toString());
				ArrayList<String> selectionSetValueIds = new ArrayList<String>();
				HashMap<String, String> selectionSetValueStrokesIdToIdMap = getSelectionSetValueStrokesIdToIdMap();
				if (strokesIdsString != null && strokesIdsString.length() > 0) {
					String[] strokesIds = WebUtil.ID_SEPARATOR_REGEXP.split(strokesIdsString, -1);
					for (int i = 0; i < strokesIds.length; i++) {
						if (strokesIds[i].length() > 0 && selectionSetValueStrokesIdToIdMap.containsKey(strokesIds[i])) {
							selectionSetValueIds.add(selectionSetValueStrokesIdToIdMap.get(strokesIds[i]));
						}
					}
				}
				setSelectionValueIds(selectionSetValueIds);
				return;
			}
		}
		setInkValue(null);
		setSelectionValueIds(null);
	}

	public abstract void setLongValue(Long value);

	public abstract void setReasonForChange(String reasonForChange);

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

	public abstract void setSelectionValueId(Long value);

	public abstract void setSelectionValueIds(List<String> value);

	public abstract void setTextValue(String value);

	public abstract void setTimestampValue(Date value);

	public abstract void setTimeValue(Date value);

	protected abstract Object update();

	public final void update(ActionEvent actionEvent) {
		actionPostProcess(update());
	}

	protected abstract Object forceUpdate();

	public final void forceUpdate(ActionEvent actionEvent) {
		actionPostProcess(forceUpdate());
	}
}
