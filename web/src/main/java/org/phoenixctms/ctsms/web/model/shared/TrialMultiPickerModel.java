package org.phoenixctms.ctsms.web.model.shared;

import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.web.model.MultiPickerModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class TrialMultiPickerModel extends MultiPickerModelBase<TrialOutVO> {

	public TrialMultiPickerModel() {
		super();
	}

	@Override
	protected TrialOutVO loadSelectionElement(Long id) {
		return WebUtil.getTrial(id);
	}
}
