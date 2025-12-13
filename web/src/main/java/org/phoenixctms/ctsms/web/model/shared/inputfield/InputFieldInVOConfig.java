package org.phoenixctms.ctsms.web.model.shared.inputfield;

import org.phoenixctms.ctsms.enumeration.InputFieldType;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.InputFieldInVO;

public final class InputFieldInVOConfig extends InputFieldConfig {

	private InputFieldInVO inputField;

	public InputFieldInVOConfig() {
	}

	public InputFieldInVOConfig(InputFieldInVO inputField) {
		this.inputField = inputField;
	}

	@Override
	public String getFieldComment() {
		return inputField == null ? null : inputField.getComment();
	}

	public InputFieldInVO getInputField() {
		return inputField;
	}

	@Override
	public String getName() {
		if (inputField == null) {
			return null;
		} else {
			return CommonUtil.isEmptyString(inputField.getExternalId()) ? inputField.getName() : inputField.getExternalId();
		}
		//return inputField == null ? null : inputField.getName();
	}
	//	@Override
	//	protected String getExternalId() {
	//		return inputField == null ? null : inputField.getExternalId();
	//	}

	@Override
	public String getTitle() {
		return inputField == null ? null : inputField.getTitle();
	}

	@Override
	protected boolean isInputFieldType(InputFieldType inputFieldType) {
		InputFieldType fieldType = inputField == null ? null : inputField.getFieldType();
		if (fieldType != null && fieldType.equals(inputFieldType)) {
			return true;
		}
		return false;
	}

	public void setInputField(InputFieldInVO inputField) {
		this.inputField = inputField;
	}
}
