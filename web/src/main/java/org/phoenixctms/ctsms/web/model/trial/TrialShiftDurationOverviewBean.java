package org.phoenixctms.ctsms.web.model.trial;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.phoenixctms.ctsms.web.model.ShiftDurationSummaryModel.ShiftDurationSummaryType;
import org.phoenixctms.ctsms.web.model.shared.ShiftDurationOverviewBeanBase;

@ManagedBean
@ViewScoped
public class TrialShiftDurationOverviewBean extends ShiftDurationOverviewBeanBase {

	public TrialShiftDurationOverviewBean() {
		super();
	}

	@Override
	protected ShiftDurationSummaryType getType() {
		return ShiftDurationSummaryType.TRIAL_OVERVIEW;
	}
}
