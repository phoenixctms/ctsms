package org.phoenixctms.ctsms.web.model.shared.search;

import java.util.ArrayList;
import java.util.HashMap;

import javax.faces.convert.Converter;

import org.phoenixctms.ctsms.enumeration.CriterionValueType;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.CriterionInVO;
import org.phoenixctms.ctsms.vo.CriterionPropertyVO;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class CriterionIsAutocompleteList extends CriterionListBase<Boolean> {

	private boolean voReturnType;
	private HashMap<Long, Converter> converterMap;

	public CriterionIsAutocompleteList(ArrayList<CriterionInVO> criterionsIn, HashMap<Long, CriterionPropertyVO> propertyVOsMap,
			HashMap<Long, org.phoenixctms.ctsms.enumeration.CriterionRestriction> restrictionVOsMap, HashMap<Long, Converter> converterMap, boolean voReturnType) {
		super(criterionsIn, propertyVOsMap, restrictionVOsMap);
		this.voReturnType = voReturnType;
		this.converterMap = (converterMap == null ? new HashMap<Long, Converter>() : converterMap);
	}

	@Override
	public Boolean get(int index) {
		CriterionInVO criterionIn = getCriterionIn(index);
		CriterionPropertyVO propertyVO = getPropertyVO(criterionIn);
		if (propertyVO != null) {
			if (voReturnType) {
				if (CriterionValueType.LONG.equals(propertyVO.getValueType()) || CriterionValueType.LONG_HASH.equals(propertyVO.getValueType())) {
					return converterMap.get(propertyVO.getId()) != null && testCompleteMethod(criterionIn, propertyVO);
				}
			} else {
				if (CriterionValueType.STRING.equals(propertyVO.getValueType()) || CriterionValueType.STRING_HASH.equals(propertyVO.getValueType())) {
					return testCompleteMethod(criterionIn, propertyVO);
				}
			}
		}
		return false;
	}

	private boolean testCompleteMethod(CriterionInVO criterionIn, CriterionPropertyVO propertyVO) {
		return !CommonUtil.isUnaryCriterionRestriction(getRestriction(criterionIn))
				&& !WebUtil.testSelectionSetServiceMethodName(propertyVO)
				&& !WebUtil.testFilterItemsMethodName(propertyVO)
				&& !WebUtil.testPicker(propertyVO)
				&& WebUtil.testCompleteMethodName(propertyVO);
	}
}
