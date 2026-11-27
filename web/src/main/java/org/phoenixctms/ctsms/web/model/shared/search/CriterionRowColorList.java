package org.phoenixctms.ctsms.web.model.shared.search;

import java.util.ArrayList;
import java.util.HashMap;

import org.phoenixctms.ctsms.vo.CriteriaInstantVO;
import org.phoenixctms.ctsms.vo.CriterionInVO;
import org.phoenixctms.ctsms.vo.CriterionInstantVO;

public class CriterionRowColorList extends CriterionListBase<String> {

	private static final String ROWS_INVALID_STYLECLASS = "ctsms-search-criterion-rows-invalid-color";
	private static final String ROWS_UNCHECKED_STYLECLASS = "ctsms-search-criterion-rows-unchecked-color";
	private static final String ROW_SELECT_COLOR_STYLECLASS_PREFIX = "ctsms-search-criterion-row-select-color-";
	private static final String ROW_SET_OPERATION_STYLECLASS = "ctsms-search-criterion-row-set-operation";
	// the number of ctsms-search-criterion-row-select-color-<n> classes in search.css, which repeat for more result sets
	private static final int ROW_SELECT_COLOR_COUNT = 17;
	private HashMap<Long, CriterionInstantVO> instantCriterionsMap;
	private CriteriaInstantVO instantCriteria;
	private boolean parsed;

	public CriterionRowColorList(ArrayList<CriterionInVO> criterionsIn, CriteriaInstantVO instantCriteria, HashMap<Long, CriterionInstantVO> instantCriterionsMap, boolean parsed) {
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
					return ROW_SELECT_COLOR_STYLECLASS_PREFIX + Integer.toString(selectIndex.intValue() % ROW_SELECT_COLOR_COUNT);
				} else if (selectCount != null && selectCount > 0) {
					return ROW_SET_OPERATION_STYLECLASS;
				}
			}
			return ROWS_INVALID_STYLECLASS;
		} else {
			return ROWS_UNCHECKED_STYLECLASS;
		}
	}

	private CriterionInstantVO getInstantCriterion(CriterionInVO criterionVO) {
		return (criterionVO == null ? null : instantCriterionsMap.get(criterionVO.getPosition()));
	}
}
