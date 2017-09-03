package org.phoenixctms.ctsms.web.model.shared.search;

import java.util.ArrayList;

import org.phoenixctms.ctsms.vo.CriterionInVO;

public class CriterionIsMoveCriterionDownEnabledList extends CriterionListBase<Boolean> {

	public CriterionIsMoveCriterionDownEnabledList(ArrayList<CriterionInVO> criterionsIn) {
		super(criterionsIn);
	}

	@Override
	public Boolean get(int index) {
		return (index > 0 && index < size());
	}
}
