package org.phoenixctms.ctsms.web.model.shared.inputfield;

import org.phoenixctms.ctsms.vo.InputFieldOutVO;

public final class InputFieldOutVOConfig extends InputFieldOutVOConfigBase {

	private InputFieldOutVO inputField;

	public InputFieldOutVOConfig() {
	}

	public InputFieldOutVOConfig(InputFieldOutVO inputField) {
		this.inputField = inputField;
	}

	@Override
	public InputFieldOutVO getInputField() {
		return inputField;
	}

	public void setInputField(InputFieldOutVO inputField) {
		this.inputField = inputField;
	}

	@Override
	public String getTitle() {
		return super.getFieldTitle();
	}
}
