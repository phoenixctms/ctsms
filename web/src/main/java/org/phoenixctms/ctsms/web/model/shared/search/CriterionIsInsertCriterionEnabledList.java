package org.phoenixctms.ctsms.web.model.shared.search;

import java.util.ArrayList;

import org.phoenixctms.ctsms.vo.CriterionInVO;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;

public class CriterionIsInsertCriterionEnabledList extends CriterionListBase<Boolean> {

	public CriterionIsInsertCriterionEnabledList(ArrayList<CriterionInVO> criterionsIn) {
		super(criterionsIn);
	}

	@Override
	public Boolean get(int index) {
		return ((size() < Settings.getInt(SettingCodes.MAX_CRITERIONS, Bundle.SETTINGS, DefaultSettings.MAX_CRITERIONS)) && testCriterionIndex(index));
	}
}
