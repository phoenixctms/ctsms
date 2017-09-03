package org.phoenixctms.ctsms.web.model.shared.search;

import java.util.ArrayList;
import java.util.HashMap;

import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.vo.CriterionInVO;
import org.phoenixctms.ctsms.vo.CriterionPropertyVO;

public class CriterionSelectionItemsList extends CriterionListBase<ArrayList<SelectItem>> {

	private HashMap<Long, ArrayList<SelectItem>> selectionItemsMap;

	public CriterionSelectionItemsList(ArrayList<CriterionInVO> criterionsIn, HashMap<Long, CriterionPropertyVO> propertyVOsMap,
			HashMap<Long, ArrayList<SelectItem>> selectionItemsMap) {
		super(criterionsIn, propertyVOsMap);
		this.selectionItemsMap = (selectionItemsMap == null ? new HashMap<Long, ArrayList<SelectItem>>() : selectionItemsMap);
	}

	@Override
	public ArrayList<SelectItem> get(int index) {
		CriterionPropertyVO propertyVO = getPropertyVO(index);
		if (propertyVO != null && selectionItemsMap.containsKey(propertyVO.getId())) {
			return selectionItemsMap.get(propertyVO.getId());
		} else {
			return new ArrayList<SelectItem>();
		}
	}
}
