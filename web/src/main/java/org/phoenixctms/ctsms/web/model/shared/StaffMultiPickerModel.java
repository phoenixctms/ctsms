package org.phoenixctms.ctsms.web.model.shared;

import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.web.model.MultiPickerModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class StaffMultiPickerModel extends MultiPickerModelBase<StaffOutVO> {

	public StaffMultiPickerModel() {
		super(null);
	}

	@Override
	protected StaffOutVO loadSelectionElement(Long id) {
		return WebUtil.getStaff(id, null, null, null);
	}
}
