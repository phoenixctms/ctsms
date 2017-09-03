package org.phoenixctms.ctsms.adapt;

import java.sql.Timestamp;
import java.util.Date;

import org.phoenixctms.ctsms.domain.MaintenanceScheduleItem;
import org.phoenixctms.ctsms.enumeration.VariablePeriod;

public class MaintenanceItemReminderAdapter extends ReminderEntityAdapter {

	private MaintenanceScheduleItem maintenanceItem;

	public MaintenanceItemReminderAdapter(Object maintenanceItem) {
		setItem(maintenanceItem);
	}

	@Override
	public Date getDate() {
		return maintenanceItem.getDate();
	}

	@Override
	public Timestamp getDismissedTimestamp() {
		return maintenanceItem.getDismissedTimestamp();
	}

	@Override
	public Object getItem() {
		return maintenanceItem;
	}

	@Override
	public VariablePeriod getRecurrencePeriod() {
		return maintenanceItem.getRecurrencePeriod();
	}

	@Override
	public Long getRecurrencePeriodDays() {
		return maintenanceItem.getRecurrencePeriodDays();
	}

	@Override
	public VariablePeriod getReminderPeriod() {
		return maintenanceItem.getReminderPeriod();
	}

	@Override
	public Long getReminderPeriodDays() {
		return maintenanceItem.getReminderPeriodDays();
	}

	@Override
	public boolean isActive() {
		return maintenanceItem.isActive();
	}

	@Override
	public boolean isDismissable() {
		return true;
	}

	@Override
	public Boolean isDismissed() {
		return maintenanceItem.isDismissed();
	}

	@Override
	public boolean isNotify() {
		return maintenanceItem.isNotify();
	}

	@Override
	public boolean isRecurring() {
		return maintenanceItem.isRecurring();
	}

	@Override
	protected void setItem(Object maintenanceItem) {
		this.maintenanceItem = (MaintenanceScheduleItem) maintenanceItem;
	}
}
