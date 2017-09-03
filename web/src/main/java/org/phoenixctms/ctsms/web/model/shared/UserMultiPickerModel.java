package org.phoenixctms.ctsms.web.model.shared;

import org.phoenixctms.ctsms.vo.UserOutVO;
import org.phoenixctms.ctsms.web.model.MultiPickerModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class UserMultiPickerModel extends MultiPickerModelBase<UserOutVO> {

	public UserMultiPickerModel() {
		super();
	}

	@Override
	protected UserOutVO loadSelectionElement(Long id) {
		return WebUtil.getUser(id, null);
	}
}
