package org.phoenixctms.ctsms.web.model.shared.search;

import java.util.ArrayList;
import java.util.HashMap;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.CriterionInVO;
import org.phoenixctms.ctsms.vo.CriterionPropertyVO;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class CriterionIsSelectionList extends CriterionListBase<Boolean> {

	public CriterionIsSelectionList(ArrayList<CriterionInVO> criterionsIn, HashMap<Long, CriterionPropertyVO> propertyVOsMap,
			HashMap<Long, org.phoenixctms.ctsms.enumeration.CriterionRestriction> restrictionVOsMap) {
		super(criterionsIn, propertyVOsMap, restrictionVOsMap);
	}

	@Override
	public Boolean get(int index) {
		CriterionInVO criterionIn = getCriterionIn(index);
		CriterionPropertyVO propertyVO = getPropertyVO(criterionIn);
		if (propertyVO != null) {
			if (!CommonUtil.isUnaryCriterionRestriction(getRestriction(criterionIn))
					&& WebUtil.testSelectionSetServiceMethodName(propertyVO)) {
				return true;
			}
		}
		return false;
	}
}
