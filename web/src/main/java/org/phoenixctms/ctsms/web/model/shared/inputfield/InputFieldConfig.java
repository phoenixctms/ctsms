package org.phoenixctms.ctsms.web.model.shared.inputfield;

import org.phoenixctms.ctsms.enumeration.InputFieldType;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;

public abstract class InputFieldConfig {

	public abstract String getFieldComment();

	public abstract String getName();

	public abstract String getTitle();

	public final boolean isAutocomplete() {
		return isInputFieldType(InputFieldType.AUTOCOMPLETE);
	}

	public final boolean isCheckBox() {
		return isInputFieldType(InputFieldType.CHECKBOX);
	}

	public final boolean isDate() {
		return isInputFieldType(InputFieldType.DATE);
	}

	public final boolean isFloat() {
		return isInputFieldType(InputFieldType.FLOAT);
	}

	protected abstract boolean isInputFieldType(InputFieldType inputFieldType);

	public final boolean isInteger() {
		return isInputFieldType(InputFieldType.INTEGER);
	}

	public boolean isLongTitle() {
		String title = getTitle();
		if (title != null && title.length() > Settings.getInt(SettingCodes.INPUT_MODEL_LONG_TITLE_LENGTH, Bundle.SETTINGS, DefaultSettings.INPUT_MODEL_LONG_TITLE_LENGTH)) {
			return true;
		}
		return false;
	}

	public final boolean isMultiLineText() {
		return isInputFieldType(InputFieldType.MULTI_LINE_TEXT);
	}

	public final boolean isSelect() {
		return isSelectOneDropdown() || isSelectOneRadio() || isSelectMany() || isAutocomplete() || isSketch();
	}

	public final boolean isSelectHorizontal() {
		return isInputFieldType(InputFieldType.SELECT_ONE_RADIO_H) || isInputFieldType(InputFieldType.SELECT_MANY_H);
	}

	public final boolean isSelectMany() {
		return isInputFieldType(InputFieldType.SELECT_MANY_H) || isInputFieldType(InputFieldType.SELECT_MANY_V);
	}

	public final boolean isSelectOneDropdown() {
		return isInputFieldType(InputFieldType.SELECT_ONE_DROPDOWN);
	}

	public final boolean isSelectOneRadio() {
		return isInputFieldType(InputFieldType.SELECT_ONE_RADIO_H) || isInputFieldType(InputFieldType.SELECT_ONE_RADIO_V);
	}

	public final boolean isSelectVertical() {
		return isInputFieldType(InputFieldType.SELECT_ONE_RADIO_V) || isInputFieldType(InputFieldType.SELECT_MANY_V);
	}

	public final boolean isSingleLineText() {
		return isInputFieldType(InputFieldType.SINGLE_LINE_TEXT);
	}

	public final boolean isSketch() {
		return isInputFieldType(InputFieldType.SKETCH);
	}

	public final boolean isText() {
		return isSingleLineText() || isMultiLineText();
	}

	public final boolean isTime() {
		return isInputFieldType(InputFieldType.TIME);
	}

	public final boolean isTimestamp() {
		return isInputFieldType(InputFieldType.TIMESTAMP);
	}

	public final boolean isValidationErrorMsg() {
		return isSingleLineText() ||
				isMultiLineText() ||
				isSelectMany() ||
				isInteger() ||
				isFloat() ||
				isDate() ||
				isTimestamp() ||
				isTime() ||
				isSketch();
	}
}
