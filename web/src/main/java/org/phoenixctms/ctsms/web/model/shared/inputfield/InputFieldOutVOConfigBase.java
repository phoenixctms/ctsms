package org.phoenixctms.ctsms.web.model.shared.inputfield;

import org.phoenixctms.ctsms.enumeration.InputFieldType;
import org.phoenixctms.ctsms.vo.InputFieldOutVO;
import org.phoenixctms.ctsms.vo.InputFieldTypeVO;

public abstract class InputFieldOutVOConfigBase extends InputFieldConfig {

	@Override
	public String getFieldComment() {
		InputFieldOutVO inputField = getInputField();
		return inputField == null ? null : inputField.getComment();
	}

	public abstract InputFieldOutVO getInputField();

	@Override
	public String getName() {
		InputFieldOutVO inputField = getInputField();
		return inputField == null ? null : inputField.getName();
	}

	// @Override
	public String getFieldTitle() {
		InputFieldOutVO inputField = getInputField();
		return inputField == null ? null : inputField.getTitle();
	}

	@Override
	protected boolean isInputFieldType(InputFieldType inputFieldType) {
		InputFieldOutVO inputField = getInputField();
		InputFieldTypeVO fieldType = (inputField == null ? null : inputField.getFieldType());
		if (fieldType != null && fieldType.getType().equals(inputFieldType)) { // fieldType.getType() == inputFieldType) {
			return true;
		}
		return false;
	}
}
