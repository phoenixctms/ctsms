package org.phoenixctms.ctsms.web.model.shared;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.model.ShiftDurationSummaryModel;
import org.phoenixctms.ctsms.web.model.ShiftDurationSummaryModel.ShiftDurationSummaryType;

public abstract class ShiftDurationOverviewBeanBase extends ManagedBeanBase {

	private Date now;
	private ShiftDurationSummaryModel shiftDurationModel;

	public ShiftDurationOverviewBeanBase() {
		super();
		now = new Date();
		shiftDurationModel = new ShiftDurationSummaryModel(getType());
	}

	public ShiftDurationSummaryModel getShiftDurationModel() {
		return shiftDurationModel;
	}

	protected abstract ShiftDurationSummaryType getType();

	@PostConstruct
	private void init() {
		initIn();
		initSets();
	}

	private void initIn() {
	}

	private void initSets() {
		now = new Date();
		shiftDurationModel.reset(now, getType(), null);
	}

	@Override
	public boolean isCreateable() {
		return false;
	}

	@Override
	public boolean isCreated() {
		return false;
	}
}
