package org.phoenixctms.ctsms.web.model.shared.search;

import java.util.ArrayList;
import java.util.HashMap;

import org.phoenixctms.ctsms.enumeration.DBModule;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.CriterionInVO;
import org.phoenixctms.ctsms.vo.CriterionPropertyVO;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class CriterionIsPickerList extends CriterionListBase<Boolean> {

	private DBModule picker;

	public CriterionIsPickerList(ArrayList<CriterionInVO> criterionsIn, HashMap<Long, CriterionPropertyVO> propertyVOsMap,
			HashMap<Long, org.phoenixctms.ctsms.enumeration.CriterionRestriction> restrictionVOsMap) {
		super(criterionsIn, propertyVOsMap, restrictionVOsMap);
		picker = null;
	}

	public CriterionIsPickerList(ArrayList<CriterionInVO> criterionsIn, HashMap<Long, CriterionPropertyVO> propertyVOsMap,
			HashMap<Long, org.phoenixctms.ctsms.enumeration.CriterionRestriction> restrictionVOsMap, DBModule picker) {
		super(criterionsIn, propertyVOsMap, restrictionVOsMap);
		this.picker = picker;
	}

	@Override
	public Boolean get(int index) {
		CriterionInVO criterionIn = getCriterionIn(index);
		CriterionPropertyVO propertyVO = getPropertyVO(criterionIn);
		if (propertyVO != null) {
			if (!CommonUtil.isUnaryCriterionRestriction(getRestriction(criterionIn)) && WebUtil.testPicker(propertyVO, picker)) {
				return true;
			}
		}
		return false;
	}
}
