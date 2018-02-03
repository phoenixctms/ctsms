package org.phoenixctms.ctsms.web.model.shared;

import org.phoenixctms.ctsms.vo.CourseOutVO;
import org.phoenixctms.ctsms.web.model.MultiPickerModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class CourseMultiPickerModel extends MultiPickerModelBase<CourseOutVO> {

	public CourseMultiPickerModel() {
		super(null);
	}

	public CourseMultiPickerModel(Integer limit) {
		super(limit);
	}

	@Override
	protected CourseOutVO loadSelectionElement(Long id) {
		return WebUtil.getCourse(id, null, null, null);
	}
}
