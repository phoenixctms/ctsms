package org.phoenixctms.ctsms.web.model.shared.search;

import java.util.ArrayList;
import java.util.HashMap;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.CriterionInVO;
import org.phoenixctms.ctsms.vo.CriterionPropertyVO;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class CriterionSelectedItemList extends CriterionListBase<String> {

	public CriterionSelectedItemList(ArrayList<CriterionInVO> criterionsIn, HashMap<Long, CriterionPropertyVO> propertyVOsMap) {
		super(criterionsIn, propertyVOsMap);
	}

	@Override
	public String get(int index) {
		CriterionInVO criterionIn = this.getCriterionIn(index);
		CriterionPropertyVO propertyVO = this.getPropertyVO(criterionIn);
		if (criterionIn != null && propertyVO != null) {
			return CommonUtil.getCriterionValueAsString(criterionIn, propertyVO.getValueType(),
					WebUtil.getDateFormat(), WebUtil.getDecimalSeparator());
		}
		return CommonUtil.NO_SELECTION_VALUE;
	}

	@Override
	public String set(int index, String value) {
		CriterionInVO criterionIn = this.getCriterionIn(index);
		CriterionPropertyVO propertyVO = this.getPropertyVO(criterionIn);
		if (criterionIn != null && propertyVO != null) {
			String old = CommonUtil.getCriterionValueAsString(criterionIn, propertyVO.getValueType(), WebUtil.getDateFormat(), WebUtil.getDecimalSeparator());
			CommonUtil.setCriterionValueFromString(criterionIn, propertyVO.getValueType(), value, WebUtil.getDateFormat(), WebUtil.getDecimalSeparator());
			return old;
		}
		return CommonUtil.NO_SELECTION_VALUE;
	}
}
