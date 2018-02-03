package org.phoenixctms.ctsms.web.model.shared.search;

import java.util.ArrayList;
import java.util.HashMap;

import org.phoenixctms.ctsms.enumeration.DBModule;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.CriterionInVO;
import org.phoenixctms.ctsms.vo.CriterionPropertyVO;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class CriterionEntityNameList extends CriterionListBase<String> {

	private DBModule picker;

	public CriterionEntityNameList(ArrayList<CriterionInVO> criterionsIn, HashMap<Long, CriterionPropertyVO> propertyVOsMap,
			HashMap<Long, org.phoenixctms.ctsms.enumeration.CriterionRestriction> restrictionVOsMap, DBModule picker) {
		super(criterionsIn, propertyVOsMap, restrictionVOsMap);
		this.picker = picker;
	}

	@Override
	public String get(int index) {
		CriterionInVO criterionIn = getCriterionIn(index);
		CriterionPropertyVO propertyVO = getPropertyVO(criterionIn);
		if (propertyVO != null) {
			if (!CommonUtil.isUnaryCriterionRestriction(getRestriction(criterionIn)) && WebUtil.testPicker(propertyVO, picker)) {
				Long id = criterionIn.getLongValue();
				switch (picker) {
					case INVENTORY_DB:
						return WebUtil.inventoryIdToName(id);
					case STAFF_DB:
						return WebUtil.staffIdToName(id);
					case COURSE_DB:
						return WebUtil.courseIdToName(id);
					case USER_DB:
						return WebUtil.userIdToName(id);
					case TRIAL_DB:
						return WebUtil.trialIdToName(id);
					case PROBAND_DB:
						return WebUtil.probandIdToName(id);
					case INPUT_FIELD_DB:
						return WebUtil.inputFieldIdToName(id);
					case MASS_MAIL_DB:
						return WebUtil.massMailIdToName(id);
					default:
						break;
				}
			}
		}
		return "";
	}
}
