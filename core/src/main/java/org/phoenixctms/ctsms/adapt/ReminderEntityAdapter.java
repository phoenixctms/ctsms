package org.phoenixctms.ctsms.adapt;

import java.sql.Timestamp;
import java.util.Date;

import org.phoenixctms.ctsms.domain.MaintenanceScheduleItem;
import org.phoenixctms.ctsms.domain.TimelineEvent;
import org.phoenixctms.ctsms.domain.VisitScheduleItem;
import org.phoenixctms.ctsms.enumeration.VariablePeriod;
import org.phoenixctms.ctsms.util.DefaultMessages;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.MessageCodes;
import org.phoenixctms.ctsms.util.date.DateCalc;

public abstract class ReminderEntityAdapter {

	public final static ReminderEntityAdapter getInstance(Object reminderItem) {
		if (reminderItem instanceof MaintenanceScheduleItem) {
			return new MaintenanceItemReminderAdapter(reminderItem);
		} else if (reminderItem instanceof TimelineEvent) {
			return new TimelineEventReminderAdapter(reminderItem);
		} else if (reminderItem instanceof VisitScheduleItem) {
			return new VisitScheduleItemReminderAdapter(reminderItem);
		} else {
			throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.REMINDER_ENTITY_NOT_SUPPORTED, DefaultMessages.REMINDER_ENTITY_NOT_SUPPORTED,
					reminderItem == null ? null : reminderItem.getClass().toString())); // can cause nullptrexc..
		}
	}

	abstract public Date getDate();

	abstract public Timestamp getDismissedTimestamp();

	abstract public Object getItem();

	public Date getNextRecurrence(Date today, boolean forceFutureRecurrence) {
		Date date = new Date(getDate().getTime());
		Date nextRecurrence;
		if (isRecurring()) {
			if (today.compareTo(date) <= 0) {
				nextRecurrence = date;
			} else {
				nextRecurrence = DateCalc.getNextRecurrence(today, date, getRecurrencePeriod(), getRecurrencePeriodDays(), false);
				if (!forceFutureRecurrence) {
					Date reminderStart = DateCalc.subInterval(nextRecurrence, getReminderPeriod(), getReminderPeriodDays());
					if (today.compareTo(reminderStart) < 0) {
						nextRecurrence = DateCalc.getNextRecurrence(today, date, getRecurrencePeriod(), getRecurrencePeriodDays(), true);
					}
				}
			}
		} else {
			nextRecurrence = date;
		}
		return nextRecurrence;
	}

	abstract public VariablePeriod getRecurrencePeriod();

	abstract public Long getRecurrencePeriodDays();

	abstract public VariablePeriod getReminderPeriod();

	protected VariablePeriod getReminderPeriod(VariablePeriod consistentReminderPeriod) {
		if (consistentReminderPeriod != null) {
			return consistentReminderPeriod;
		}
		return getReminderPeriod();
	}

	abstract public Long getReminderPeriodDays();

	protected Long getReminderPeriodDays(VariablePeriod consistentReminderPeriod, Long consistentReminderPeriodDays) {
		if (consistentReminderPeriod != null) {
			return consistentReminderPeriodDays;
		}
		return getReminderPeriodDays();
	}

	public Date getReminderStart(Date today, boolean forceFutureRecurrence, VariablePeriod consistentReminderPeriod,
			Long consistentReminderPeriodDays) {
		Date date = new Date(getDate().getTime());
		Date reminderStart;
		if (isRecurring()) {
			if (today.compareTo(date) <= 0) {
				reminderStart = DateCalc.subInterval(date, getReminderPeriod(consistentReminderPeriod),
						getReminderPeriodDays(consistentReminderPeriod, consistentReminderPeriodDays));
			} else {
				Date nextRecurrence = DateCalc.getNextRecurrence(today, date, getRecurrencePeriod(), getRecurrencePeriodDays(), false);
				reminderStart = DateCalc.subInterval(nextRecurrence, getReminderPeriod(consistentReminderPeriod),
						getReminderPeriodDays(consistentReminderPeriod, consistentReminderPeriodDays));
				if (!forceFutureRecurrence && today.compareTo(reminderStart) < 0) {
					Date lastRecurrence = DateCalc.getNextRecurrence(today, date, getRecurrencePeriod(), getRecurrencePeriodDays(), true);
					reminderStart = DateCalc.subInterval(lastRecurrence, getReminderPeriod(consistentReminderPeriod),
							getReminderPeriodDays(consistentReminderPeriod, consistentReminderPeriodDays));
				}
			}
		} else {
			reminderStart = DateCalc.subInterval(date, getReminderPeriod(consistentReminderPeriod), getReminderPeriodDays(consistentReminderPeriod, consistentReminderPeriodDays));
		}
		return reminderStart;
	}

	abstract public boolean isActive();

	abstract public boolean isDismissable();

	abstract public Boolean isDismissed();

	abstract public boolean isNotify();

	public Boolean isRecurrenceDismissed(Date reminderStart) {
		return isDismissable() ? isDismissed() && reminderStart.compareTo(new Date(getDismissedTimestamp().getTime())) <= 0 : null;
	}

	abstract public boolean isRecurring();

	abstract protected void setItem(Object reminderItem);
}
