package org.phoenixctms.ctsms.adapt;

import java.sql.Timestamp;
import java.util.Date;

import org.phoenixctms.ctsms.domain.VisitScheduleItem;
import org.phoenixctms.ctsms.enumeration.VariablePeriod;
import org.phoenixctms.ctsms.util.date.DateCalc;

public class VisitScheduleItemReminderAdapter extends ReminderEntityAdapter {

	private VisitScheduleItem visitScheduleItem;

	public VisitScheduleItemReminderAdapter(Object visitScheduleItem) {
		setItem(visitScheduleItem);
	}

	@Override
	public Date getDate() {
		return DateCalc.getStartOfDay(visitScheduleItem.getStart());
	}

	@Override
	public Timestamp getDismissedTimestamp() {
		return null;
	}

	@Override
	public Object getItem() {
		return visitScheduleItem;
	}

	@Override
	public VariablePeriod getRecurrencePeriod() {
		return null;
	}

	@Override
	public Long getRecurrencePeriodDays() {
		return null;
	}

	@Override
	public VariablePeriod getReminderPeriod() {
		return null;
	}

	@Override
	public Long getReminderPeriodDays() {
		return null;
	}

	@Override
	public boolean isActive() {
		return true;
	}

	@Override
	public boolean isDismissable() {
		return false;
	}

	@Override
	public Boolean isDismissed() {
		return null;
	}

	@Override
	public boolean isNotify() {
		return visitScheduleItem.isNotify();
	}

	@Override
	public boolean isRecurring() {
		return false;
	}

	@Override
	protected void setItem(Object visitSchduleItem) {
		this.visitScheduleItem = (VisitScheduleItem) visitSchduleItem;
	}
}
