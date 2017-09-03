package org.phoenixctms.ctsms.web.model.shared.search;

import java.util.ArrayList;
import java.util.HashMap;

import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.vo.CriterionInVO;
import org.phoenixctms.ctsms.vo.CriterionPropertyVO;

public class CriterionRestrictionsList extends CriterionListBase<ArrayList<SelectItem>> {

	private HashMap<Long, ArrayList<SelectItem>> restrictionsMap;

	public CriterionRestrictionsList(ArrayList<CriterionInVO> criterionsIn, HashMap<Long, CriterionPropertyVO> propertyVOsMap,
			HashMap<Long, ArrayList<SelectItem>> restrictionsMap) {
		super(criterionsIn, propertyVOsMap);
		this.restrictionsMap = (restrictionsMap == null ? new HashMap<Long, ArrayList<SelectItem>>() : restrictionsMap);
	}

	@Override
	public ArrayList<SelectItem> get(int index) {
		CriterionPropertyVO propertyVO = getPropertyVO(index);
		if (propertyVO != null && restrictionsMap.containsKey(propertyVO.getId())) {
			return restrictionsMap.get(propertyVO.getId());
		} else {
			return new ArrayList<SelectItem>();
		}
	}
}
