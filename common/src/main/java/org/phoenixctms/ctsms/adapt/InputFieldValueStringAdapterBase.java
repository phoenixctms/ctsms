package org.phoenixctms.ctsms.adapt;

import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.phoenixctms.ctsms.enumeration.InputFieldType;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CommonUtil.EllipsisPlacement;
import org.phoenixctms.ctsms.vo.InputFieldOutVO;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueOutVO;
import org.phoenixctms.ctsms.vo.InputFieldTypeVO;

public abstract class InputFieldValueStringAdapterBase<VALUEVO> {

	protected abstract boolean getBooleanValue(VALUEVO value);

	protected abstract String getCheckboxString(boolean value);

	protected abstract DateFormat getDateFormat();

	protected abstract DateFormat getDateTimeFormat();

	protected abstract Date getDateValue(VALUEVO value);

	protected abstract Float getFloatValue(VALUEVO value);

	protected abstract Long getLongValue(VALUEVO value);

	protected abstract Collection<InputFieldSelectionSetValueOutVO> getSelectionSetValues(VALUEVO value);

	protected abstract String getSelectionSetValuesSeparator();

	protected abstract Integer getTextClipMaxLength();

	protected abstract String getTextValue(VALUEVO value);

	protected abstract DateFormat getTimeFormat();

	protected abstract Date getTimestampValue(VALUEVO value);

	protected abstract Date getTimeValue(VALUEVO value);

	private String selectionSetValuesToString(Collection<InputFieldSelectionSetValueOutVO> selectionSetValues) {
		StringBuilder sb = new StringBuilder();
		if (selectionSetValues != null && selectionSetValues.size() > 0) {
			Iterator<InputFieldSelectionSetValueOutVO> it = selectionSetValues.iterator();
			while (it.hasNext()) {
				InputFieldSelectionSetValueOutVO selectionSetValue = it.next();
				if (sb.length() > 0) {
					// sb.append(SELECTION_SET_VALUES_SEPARATOR);
					sb.append(getSelectionSetValuesSeparator());
				}
				if (selectionSetValue != null) {
					sb.append(selectionSetValue.getName());
				}
			}
		}
		return sb.toString();
	}

	public String toString(InputFieldOutVO inputField, VALUEVO value) {
		if (inputField != null && value != null) {
			InputFieldTypeVO fieldTypeVO = inputField.getFieldType();
			if (fieldTypeVO != null) {
				InputFieldType fieldType = fieldTypeVO.getType();
				Integer textClipMaxLength;
				if (fieldType != null) {
					switch (fieldType) {
						case SINGLE_LINE_TEXT:
							textClipMaxLength = getTextClipMaxLength();
							return textClipMaxLength != null ? CommonUtil.clipString(getTextValue(value), textClipMaxLength, CommonUtil.DEFAULT_ELLIPSIS,
									EllipsisPlacement.TRAILING) : getTextValue(value);
						case MULTI_LINE_TEXT:
							textClipMaxLength = getTextClipMaxLength();
							return textClipMaxLength != null ? CommonUtil.clipString(getTextValue(value), textClipMaxLength, CommonUtil.DEFAULT_ELLIPSIS,
									EllipsisPlacement.TRAILING) : getTextValue(value);
						case SELECT_ONE_DROPDOWN:
						case SELECT_ONE_RADIO_H:
						case SELECT_ONE_RADIO_V:
							textClipMaxLength = getTextClipMaxLength();
							return textClipMaxLength != null ? CommonUtil.clipString(selectionSetValuesToString(getSelectionSetValues(value)), textClipMaxLength,
									CommonUtil.DEFAULT_ELLIPSIS,
									EllipsisPlacement.MID) : selectionSetValuesToString(getSelectionSetValues(value));
						case AUTOCOMPLETE:
							textClipMaxLength = getTextClipMaxLength();
							return textClipMaxLength != null ? CommonUtil.clipString(getTextValue(value), textClipMaxLength, CommonUtil.DEFAULT_ELLIPSIS,
									EllipsisPlacement.TRAILING) : getTextValue(value);
						case SELECT_MANY_H:
						case SELECT_MANY_V:
							textClipMaxLength = getTextClipMaxLength();
							return textClipMaxLength != null ? CommonUtil.clipString(selectionSetValuesToString(getSelectionSetValues(value)), textClipMaxLength,
									CommonUtil.DEFAULT_ELLIPSIS,
									EllipsisPlacement.MID) : selectionSetValuesToString(getSelectionSetValues(value));
						case CHECKBOX:
							return getCheckboxString(getBooleanValue(value));
						case INTEGER:
							Long longValue = getLongValue(value);
							if (longValue != null) {
								return longValue.toString();
							}
							break;
						case FLOAT:
							Float floatValue = getFloatValue(value);
							if (floatValue != null) {
								return floatValue.toString();
							}
							break;
						case DATE:
							Date dateValue = getDateValue(value);
							if (dateValue != null) {
								return getDateFormat().format(dateValue);
							}
							break;
						case TIME:
							Date timeValue = getTimeValue(value);
							if (timeValue != null) {
								return getTimeFormat().format(timeValue);
							}
							break;
						case TIMESTAMP:
							Date timestampValue = getTimestampValue(value);
							if (timestampValue != null) {
								return getDateTimeFormat().format(timestampValue);
							}
							break;
						case SKETCH:
							textClipMaxLength = getTextClipMaxLength();
							return textClipMaxLength != null ? CommonUtil.clipString(selectionSetValuesToString(getSelectionSetValues(value)), textClipMaxLength,
									CommonUtil.DEFAULT_ELLIPSIS,
									EllipsisPlacement.MID) : selectionSetValuesToString(getSelectionSetValues(value));
						default:
					}
				}
			}
		}
		return "";
	}
}
