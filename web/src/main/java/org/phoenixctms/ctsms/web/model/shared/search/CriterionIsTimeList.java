
package org.phoenixctms.ctsms.web.model.shared.search;

import java.util.ArrayList;
import java.util.HashMap;

import org.phoenixctms.ctsms.enumeration.CriterionValueType;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.CriterionInVO;
import org.phoenixctms.ctsms.vo.CriterionPropertyVO;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class CriterionIsTimeList extends CriterionListBase<Boolean> {

	public CriterionIsTimeList(ArrayList<CriterionInVO> criterionsIn, HashMap<Long, CriterionPropertyVO> propertyVOsMap,
			HashMap<Long, org.phoenixctms.ctsms.enumeration.CriterionRestriction> restrictionVOsMap) {
		super(criterionsIn, propertyVOsMap, restrictionVOsMap);
	}

	@Override
	public Boolean get(int index) {
		CriterionInVO criterionIn = getCriterionIn(index);
		CriterionPropertyVO propertyVO = getPropertyVO(criterionIn);
		if (propertyVO != null && (CriterionValueType.TIME.equals(propertyVO.getValueType()) || CriterionValueType.TIME_HASH.equals(propertyVO.getValueType()))) {
			if (!CommonUtil.isUnaryCriterionRestriction(getRestriction(criterionIn)) && !WebUtil.testSelectionSetServiceMethodName(propertyVO)
					&& !WebUtil.testCompleteMethodName(propertyVO) && !WebUtil.testPicker(propertyVO)) {
				return true;
			}
		}
		return false;
	}
}