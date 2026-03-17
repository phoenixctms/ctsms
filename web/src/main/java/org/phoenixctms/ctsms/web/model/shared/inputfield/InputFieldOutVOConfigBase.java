package org.phoenixctms.ctsms.web.model.shared.inputfield;

import org.phoenixctms.ctsms.enumeration.InputFieldType;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.InputFieldOutVO;
import org.phoenixctms.ctsms.vo.InputFieldTypeVO;

public abstract class InputFieldOutVOConfigBase extends InputFieldConfig {

	@Override
	public String getTopComment() {
		InputFieldOutVO inputField = getInputField();
		return inputField == null ? null : inputField.getTopComment();
	}

	@Override
	public String getBottomComment() {
		InputFieldOutVO inputField = getInputField();
		return inputField == null ? null : inputField.getBottomComment();
	}

	public abstract InputFieldOutVO getInputField();

	@Override
	public String getName() {
		InputFieldOutVO inputField = getInputField();
		//return inputField == null ? null : inputField.getName();
		if (inputField == null) {
			return null;
		} else {
			return CommonUtil.isEmptyString(inputField.getExternalId()) ? inputField.getName() : inputField.getExternalId();
		}
	}

	public String getFieldTitle() {
		InputFieldOutVO inputField = getInputField();
		return inputField == null ? null : inputField.getTitle();
	}

	@Override
	protected boolean isInputFieldType(InputFieldType inputFieldType) {
		InputFieldOutVO inputField = getInputField();
		InputFieldTypeVO fieldType = (inputField == null ? null : inputField.getFieldType());
		if (fieldType != null && fieldType.getType().equals(inputFieldType)) {
			return true;
		}
		return false;
	}
}
