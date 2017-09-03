package org.phoenixctms.ctsms.web.model.shared.search;

import java.util.ArrayList;

import org.phoenixctms.ctsms.vo.CriterionInVO;

public class CriterionRowIsRenderedList extends CriterionListBase<Boolean> {

	public CriterionRowIsRenderedList(ArrayList<CriterionInVO> criterionsIn) {
		super(criterionsIn);
	}

	@Override
	public Boolean get(int index) {
		return testCriterionIndex(index);
	}
}
