package org.phoenixctms.ctsms.adapt;

import java.util.Date;
import java.util.Map;

import org.phoenixctms.ctsms.domain.Course;
import org.phoenixctms.ctsms.domain.CourseParticipationStatusEntry;
import org.phoenixctms.ctsms.domain.Password;
import org.phoenixctms.ctsms.domain.Proband;
import org.phoenixctms.ctsms.enumeration.VariablePeriod;
import org.phoenixctms.ctsms.util.date.DateCalc;

public abstract class ExpirationEntityAdapter {

	public final static ExpirationEntityAdapter getInstance(Object reminderItem, Date today, Map... caches) {
		if (reminderItem instanceof Course) {
			return new CourseExpirationAdapter(reminderItem, today, caches);
		} else if (reminderItem instanceof CourseParticipationStatusEntry) {
			return new CourseParticipationStatusEntryExpirationAdapter(reminderItem, today, caches);
		} else if (reminderItem instanceof Proband) {
			return new ProbandAutoDeleteExpirationAdapter(reminderItem, today, caches);
		} else if (reminderItem instanceof Password) {
			return new PasswordExpirationAdapter(reminderItem, today, caches);
		}
		return null;
	}

	protected Date today;

	abstract public Date getDate();

	public Date getExpiry(VariablePeriod consistentValidityPeriod, Long consistentValidityPeriodDays) {
		Date date = new Date(getDate().getTime());
		Date expiry;
		if (consistentValidityPeriod != null) {
			expiry = DateCalc.addInterval(date, consistentValidityPeriod, consistentValidityPeriodDays);
		} else {
			VariablePeriod validityPeriod = getValidityPeriod();
			if (validityPeriod != null) {
				expiry = DateCalc.addInterval(date, getValidityPeriod(), getValidityPeriodDays());
			} else {
				expiry = date;
			}
		}
		return expiry;
	}

	abstract public Object getItem();

	abstract public VariablePeriod getReminderPeriod();

	abstract public Long getReminderPeriodDays();

	public Date getReminderStart(VariablePeriod consistentValidityPeriod, Long consistentValidityPeriodDays, VariablePeriod consistentReminderPeriod,
			Long consistentReminderPeriodDays) {
		Date reminderStart;
		Date expiry = getExpiry(consistentValidityPeriod, consistentValidityPeriodDays);
		if (consistentReminderPeriod != null) {
			reminderStart = DateCalc.subInterval(expiry, consistentReminderPeriod, consistentReminderPeriodDays);
		} else {
			VariablePeriod reminderPeriod = getReminderPeriod();
			if (reminderPeriod != null) {
				reminderStart = DateCalc.subInterval(expiry, getReminderPeriod(), getReminderPeriodDays());
			} else {
				reminderStart = expiry;
			}
		}
		return reminderStart;
	}

	abstract public VariablePeriod getValidityPeriod();

	abstract public Long getValidityPeriodDays();

	abstract public boolean isActive();

	abstract public boolean isNotify();

	abstract public void setCaches(Map... caches);

	abstract protected void setItem(Object reminderItem);

	public void setToday(Date today) {
		this.today = today;
	}
}
