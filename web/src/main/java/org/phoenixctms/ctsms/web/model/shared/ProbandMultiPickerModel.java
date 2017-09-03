package org.phoenixctms.ctsms.web.model.shared;

import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.web.model.MultiPickerModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class ProbandMultiPickerModel extends MultiPickerModelBase<ProbandOutVO> {

	public ProbandMultiPickerModel() {
		super();
	}

	@Override
	protected ProbandOutVO loadSelectionElement(Long id) {
		return WebUtil.getProband(id, null, null, null);
	}
}
