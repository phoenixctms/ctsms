package org.phoenixctms.ctsms.adapt;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.phoenixctms.ctsms.domain.Course;
import org.phoenixctms.ctsms.enumeration.VariablePeriod;
import org.phoenixctms.ctsms.util.date.DateCalc;

public abstract class CourseExpirationAdapterBase extends ExpirationEntityAdapter {

	protected static Course findNewest(Collection<Course> result) {
		Iterator<Course> it = result.iterator();
		Date maxExpirationDate = null;
		Course newest = null;
		boolean expires = true;
		while (it.hasNext()) {
			Course renewal = it.next();
			Date expirationDate;
			if (expires && renewal.isExpires()) {
				expirationDate = DateCalc.addInterval(renewal.getStop(), renewal.getValidityPeriod(), renewal.getValidityPeriodDays());
			} else {
				if (expires) {
					maxExpirationDate = null;
					expires = false;
				}
				expirationDate = renewal.getStop();
			}
			if (maxExpirationDate == null || expirationDate.compareTo(maxExpirationDate) > 0) {
				maxExpirationDate = expirationDate;
				newest = renewal;
			}
		}
		return newest;
	}

	private static Boolean getCourseExpired(Date today, Course course) {
		if (course.isExpires() && today.compareTo(DateCalc.addInterval(course.getStop(), course.getValidityPeriod(), course.getValidityPeriodDays())) > 0) {
			return true;
		}
		return false;
	}

	protected Course course;
	protected Course newest;

	@Override
	public Date getDate() {
		return course.getStop();
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
	public VariablePeriod getValidityPeriod() {
		return course.getValidityPeriod();
	}

	@Override
	public Long getValidityPeriodDays() {
		return course.getValidityPeriodDays();
	}

	@Override
	public boolean isActive() {
		if (course.equals(newest)) {
			return course.isExpires();
		} else {
			return getCourseExpired(today, newest);
		}
	}

	@Override
	public boolean isNotify() {
		return true;
	}
}
