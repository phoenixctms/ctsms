package org.phoenixctms.ctsms.web.model.shared;

import org.phoenixctms.ctsms.vo.InputFieldOutVO;
import org.phoenixctms.ctsms.web.model.MultiPickerModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class InputFieldMultiPickerModel extends MultiPickerModelBase<InputFieldOutVO> {

	public InputFieldMultiPickerModel() {
		super(null);
	}

	@Override
	protected InputFieldOutVO loadSelectionElement(Long id) {
		return WebUtil.getInputField(id);
	}
}
