package org.phoenixctms.ctsms.web.model.shared.search;

import java.util.ArrayList;
import java.util.HashMap;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.CriterionInVO;
import org.phoenixctms.ctsms.vo.CriterionTieVO;

public class CriterionPropertyIsRenderedList extends CriterionListBase<Boolean> {

	private HashMap<Long, CriterionTieVO> tieVOsMap;

	public CriterionPropertyIsRenderedList(ArrayList<CriterionInVO> criterionsIn, HashMap<Long, CriterionTieVO> tieVOsMap) {
		super(criterionsIn);
		this.tieVOsMap = (tieVOsMap == null ? new HashMap<Long, CriterionTieVO>() : tieVOsMap);
	}

	@Override
	public Boolean get(int index) {
		CriterionInVO criterionIn = getCriterionIn(index);
		CriterionTieVO tieVO = getTieVO(criterionIn);
		if (tieVO == null || !CommonUtil.isBlankCriterionTie(tieVO.getTie())) {
			return true;
		}
		return false;
	}

	private CriterionTieVO getTieVO(CriterionInVO criterionVO) {
		return (criterionVO == null ? null : tieVOsMap.get(criterionVO.getTieId()));
	}
}
