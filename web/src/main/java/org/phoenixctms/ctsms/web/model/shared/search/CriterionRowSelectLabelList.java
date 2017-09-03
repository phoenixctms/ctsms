package org.phoenixctms.ctsms.web.model.shared.search;

import java.util.ArrayList;
import java.util.HashMap;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.CriteriaInstantVO;
import org.phoenixctms.ctsms.vo.CriterionInVO;
import org.phoenixctms.ctsms.vo.CriterionInstantVO;

public class CriterionRowSelectLabelList extends CriterionListBase<String> {

	private HashMap<Long, CriterionInstantVO> instantCriterionsMap;
	private CriteriaInstantVO instantCriteria;
	private boolean parsed;

	public CriterionRowSelectLabelList(ArrayList<CriterionInVO> criterionsIn, CriteriaInstantVO instantCriteria, HashMap<Long, CriterionInstantVO> instantCriterionsMap,
			boolean parsed) {
		super(criterionsIn);
		this.instantCriteria = (instantCriteria == null ? new CriteriaInstantVO() : instantCriteria);
		this.instantCriterionsMap = (instantCriterionsMap == null ? new HashMap<Long, CriterionInstantVO>() : instantCriterionsMap);
		this.parsed = parsed;
	}

	@Override
	public String get(int index) {
		if (parsed) {
			CriterionInVO criterionIn = getCriterionIn(index);
			CriterionInstantVO instantCriterion = getInstantCriterion(criterionIn);
			if (instantCriterion != null) {
				Integer selectIndex = instantCriterion.getSelectStatementIndex();
				Integer selectCount = instantCriteria.getSelectStatementCount();
				if (selectIndex != null) {
					return CommonUtil.getSetOperationExpressionSelectLabel(selectIndex);
				} else if (selectCount != null && selectCount > 0) {
					return "";
				}
			}
			return "";
		} else {
			return "";
		}
	}

	private CriterionInstantVO getInstantCriterion(CriterionInVO criterionVO) {
		return (criterionVO == null ? null : instantCriterionsMap.get(criterionVO.getPosition()));
	}
}
