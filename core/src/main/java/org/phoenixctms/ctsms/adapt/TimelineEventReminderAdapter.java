package org.phoenixctms.ctsms.adapt;

import java.sql.Timestamp;
import java.util.Date;

import org.phoenixctms.ctsms.domain.TimelineEvent;
import org.phoenixctms.ctsms.enumeration.VariablePeriod;

public class TimelineEventReminderAdapter extends ReminderEntityAdapter {

	private TimelineEvent timelineEvent;

	public TimelineEventReminderAdapter(Object timelineEvent) {
		setItem(timelineEvent);
	}

	@Override
	public Date getDate() {
		return timelineEvent.getStart();
	}

	@Override
	public Timestamp getDismissedTimestamp() {
		return timelineEvent.getDismissedTimestamp();
	}

	@Override
	public Object getItem() {
		return timelineEvent;
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
		return timelineEvent.getReminderPeriod();
	}

	@Override
	public Long getReminderPeriodDays() {
		return timelineEvent.getReminderPeriodDays();
	}

	@Override
	public boolean isActive() {
		return true;
	}

	@Override
	public boolean isDismissable() {
		return true;
	}

	@Override
	public Boolean isDismissed() {
		return timelineEvent.isDismissed();
	}

	@Override
	public boolean isNotify() {
		return timelineEvent.isNotify();
	}

	@Override
	public boolean isRecurring() {
		return false;
	}

	@Override
	protected void setItem(Object timelineEvent) {
		this.timelineEvent = (TimelineEvent) timelineEvent;
	}
}
