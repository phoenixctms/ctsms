package org.phoenixctms.ctsms.web.model.shared.search;

import java.util.ArrayList;

import org.phoenixctms.ctsms.vo.CriterionInVO;

public class CriterionIsMoveCriterionUpEnabledList extends CriterionListBase<Boolean> {

	public CriterionIsMoveCriterionUpEnabledList(ArrayList<CriterionInVO> criterionsIn) {
		super(criterionsIn);
	}

	@Override
	public Boolean get(int index) {
		return ((index < (size() - 1)) && index >= 0);
	}
}
