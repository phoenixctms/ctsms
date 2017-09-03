package org.phoenixctms.ctsms.web.model.staff;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.phoenixctms.ctsms.web.model.ShiftDurationSummaryModel.ShiftDurationSummaryType;
import org.phoenixctms.ctsms.web.model.shared.ShiftDurationOverviewBeanBase;

@ManagedBean
@ViewScoped
public class StaffShiftDurationOverviewBean extends ShiftDurationOverviewBeanBase {

	public StaffShiftDurationOverviewBean() {
		super();
	}

	@Override
	protected ShiftDurationSummaryType getType() {
		return ShiftDurationSummaryType.STAFF_OVERVIEW;
	}
}
