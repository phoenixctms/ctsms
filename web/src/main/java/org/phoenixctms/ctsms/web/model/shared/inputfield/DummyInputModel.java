package org.phoenixctms.ctsms.web.model.shared.inputfield;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.phoenixctms.ctsms.enumeration.Color;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.InputFieldOutVO;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueOutVO;
import org.phoenixctms.ctsms.vo.InputFieldValueVO;
import org.phoenixctms.ctsms.web.adapt.InputFieldValueVOStringAdapter;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;

public final class DummyInputModel extends InputModel {

	private InputFieldValueVO inputFieldValue;
	private boolean disabled;

	public DummyInputModel() {
		super();
		disabled = false;
		setInputField(null);
	}

	public DummyInputModel(InputFieldOutVO inputField) {
		super();
		disabled = false;
		setInputField(inputField);
	}

	@Override
	public void applyPresets() {
		if (inputField != null && inputFieldValue != null) {
			inputFieldValue.setBooleanValue(inputField.getBooleanPreset());
			inputFieldValue.setDateValue(inputField.getDatePreset());
			inputFieldValue.setTimeValue(inputField.getTimePreset());
			inputFieldValue.setFloatValue(inputField.getFloatPreset());
			inputFieldValue.setLongValue(inputField.getLongPreset());
			inputFieldValue.setTimestampValue(inputField.getTimestampPreset());
			inputFieldValue.setInkValues(null);
			inputFieldValue.getSelectionValueIds().clear();
			inputFieldValue.setTextValue(inputField.getTextPreset());
			Iterator<InputFieldSelectionSetValueOutVO> it = inputField.getSelectionSetValues().iterator();
			while (it.hasNext()) {
				InputFieldSelectionSetValueOutVO selectionValue = it.next();
				if (selectionValue.isPreset()) {
					inputFieldValue.getSelectionValueIds().add(selectionValue.getId());
				}
			}
		}
	}

	@Override
	protected Object check() {
		return null;
	}

	@Override
	public boolean getBooleanValue() {
		return inputFieldValue == null ? null : inputFieldValue.getBooleanValue();
	}

	@Override
	public String getComment() {
		return null;
	}

	@Override
	public Date getDateValue() {
		return inputFieldValue == null ? null : inputFieldValue.getDateValue();
	}

	@Override
	public Color getFieldColor() {
		return Settings.getColor(SettingCodes.INPUT_MODEL_DUMMY_COLOR, Bundle.SETTINGS, DefaultSettings.INPUT_MODEL_DUMMY_COLOR);
	}

	@Override
	public Float getFloatValue() {
		return inputFieldValue == null ? null : inputFieldValue.getFloatValue();
	}

	@Override
	public String getInkValue() throws UnsupportedEncodingException {
		return inputFieldValue == null ? null : CommonUtil.inkValueToString(inputFieldValue.getInkValues());
	}

	public InputFieldValueVO getInputFieldValue() {
		return inputFieldValue;
	}

	@Override
	public String getJsOutputExpression() {
		return null;
	}

	@Override
	public String getJsValueExpression() {
		return null;
	}

	@Override
	public String getJsVariableName() {
		return "";
	}

	@Override
	public Long getLongValue() {
		return inputFieldValue == null ? null : inputFieldValue.getLongValue();
	}

	@Override
	public String getModifiedAnnotation() {
		return "";
	}

	@Override
	public String getOutputId() {
		return "";
	}

	@Override
	public String getReasonForChange() {
		return null;
	}

	@Override
	public Long getSelectionValueId() {
		if (inputFieldValue != null) {
			Collection<Long> selectionValueIds = inputFieldValue.getSelectionValueIds();
			if (selectionValueIds != null && selectionValueIds.size() > 0) {
				return selectionValueIds.iterator().next();
			}
		}
		return null;
	}

	@Override
	public List<String> getSelectionValueIds() {
		ArrayList<String> result;
		if (inputFieldValue != null) {
			Collection<Long> selectionValueIds = inputFieldValue.getSelectionValueIds();
			result = new ArrayList<String>(selectionValueIds.size());
			Iterator<Long> it = selectionValueIds.iterator();
			while (it.hasNext()) {
				result.add(it.next().toString());
			}
		} else {
			result = new ArrayList<String>();
		}
		return result;
	}

	@Override
	public Long getSeriesIndex() {
		return null;
	}

	@Override
	public String getStatusComment() {
		return null;
	}

	@Override
	public String getTextValue() {
		return inputFieldValue == null ? null : inputFieldValue.getTextValue();
	}

	@Override
	public Date getTimestampValue() {
		return inputFieldValue == null ? null : inputFieldValue.getTimestampValue();
	}

	@Override
	public Date getTimeValue() {
		return inputFieldValue == null ? null : inputFieldValue.getTimeValue();
	}

	@Override
	public String getUniqueName() {
		return super.getName();
	}

	@Override
	public String getValueString() {
		return (new InputFieldValueVOStringAdapter(Settings.getInt(SettingCodes.INPUT_MODEL_VALUE_TEXT_CLIP_MAX_LENGTH, Bundle.SETTINGS,
				DefaultSettings.INPUT_MODEL_VALUE_TEXT_CLIP_MAX_LENGTH))).toString(inputField, inputFieldValue);
	}

	@Override
	protected Long getVersion() {
		return null;
	}

	@Override
	public String getWidgetVar() {
		return "";
	}

	@Override
	public boolean isAuditTrail() {
		return false;
	}

	@Override
	public boolean isBooleanValue() {
		return inputFieldValue == null ? null : inputFieldValue.getBooleanValue();
	}

	@Override
	public boolean isCollapsed() {
		return false;
	}

	@Override
	public boolean isCreated() {
		return false;
	}

	@Override
	public boolean isDisabled() {
		return disabled;
	}

	@Override
	public boolean isDummy() {
		return true;
	}

	@Override
	public boolean isEditable() {
		return true;
	}

	@Override
	public boolean isJsVariable() {
		return false;
	}

	@Override
	public boolean isOptional() {
		return inputFieldValue == null ? false : inputFieldValue.isOptional();
	}

	@Override
	public boolean isReasonForChangeRequired() {
		return false;
	}

	@Override
	public boolean isSeries() {
		return false;
	}

	@Override
	public boolean isShowToolbar() {
		return false;
	}

	@Override
	public Object load() {
		return null;
	}

	@Override
	public Object reset() {
		return null;
	}

	@Override
	public void setBooleanValue(boolean value) {
		if (inputFieldValue != null) {
			inputFieldValue.setBooleanValue(value);
		}
	}

	@Override
	public void setDateValue(Date value) {
		if (inputFieldValue != null) {
			inputFieldValue.setDateValue(value);
		}
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	@Override
	public void setFloatValue(Float value) {
		if (inputFieldValue != null) {
			inputFieldValue.setFloatValue(value);
		}
	}

	@Override
	public void setInkValue(String value) throws UnsupportedEncodingException {
		if (inputFieldValue != null) {
			inputFieldValue.setInkValues(CommonUtil.stringToInkValue(value));
		}
	}

	public void setInputField(InputFieldOutVO inputField) {
		inputFieldValue = new InputFieldValueVO();
		super.setField(inputField);
	}

	@Override
	public void setLongValue(Long value) {
		if (inputFieldValue != null) {
			inputFieldValue.setLongValue(value);
		}
	}

	@Override
	public void setReasonForChange(String reasonForChange) {
	}

	@Override
	public void setSelectionValueId(Long value) {
		if (inputFieldValue != null) {
			ArrayList<Long> selectionValueIds;
			if (value != null) {
				selectionValueIds = new ArrayList<Long>(1);
				selectionValueIds.add(value);
			} else {
				selectionValueIds = new ArrayList<Long>();
			}
			inputFieldValue.setSelectionValueIds(selectionValueIds);
		}
	}

	@Override
	public void setSelectionValueIds(List<String> value) {
		if (inputFieldValue != null) {
			ArrayList<Long> selectionValueIds;
			if (value != null && value.size() > 0) {
				selectionValueIds = new ArrayList<Long>(value.size());
				Iterator<String> it = value.iterator();
				while (it.hasNext()) {
					Long id = WebUtil.stringToLong(it.next());
					if (id != null) {
						selectionValueIds.add(id);
					}
				}
			} else {
				selectionValueIds = new ArrayList<Long>();
			}
			inputFieldValue.setSelectionValueIds(selectionValueIds);
		}
	}

	@Override
	public void setTextValue(String value) {
		if (inputFieldValue != null) {
			inputFieldValue.setTextValue(value);
		}
	}

	@Override
	public void setTimestampValue(Date value) {
		if (inputFieldValue != null) {
			inputFieldValue.setTimestampValue(value);
		}
	}

	@Override
	public void setTimeValue(Date value) {
		if (inputFieldValue != null) {
			inputFieldValue.setTimeValue(value);
		}
	}

	@Override
	public Object update() {
		return null;
	}

	@Override
	public Object forceUpdate() {
		return null;
	}

	@Override
	protected String getInputTitle() {
		return null;
	}
}
