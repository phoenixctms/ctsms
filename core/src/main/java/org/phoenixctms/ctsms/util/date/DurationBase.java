package org.phoenixctms.ctsms.util.date;

import java.util.Date;
import java.util.GregorianCalendar;

import org.phoenixctms.ctsms.domain.HolidayDao;

public abstract class DurationBase {

	public void add(Date start, Date stop, HolidayDao holidayDao) throws Exception {
		if (start != null && stop != null) {
			GregorianCalendar date = new GregorianCalendar();
			GregorianCalendar begin;
			GregorianCalendar end;
			if (stop.before(start)) {
				date.setTime(start);
				end = new GregorianCalendar(date.get(GregorianCalendar.YEAR), date.get(GregorianCalendar.MONTH), date.get(GregorianCalendar.DAY_OF_MONTH));
				date.setTime(stop);
				begin = new GregorianCalendar(date.get(GregorianCalendar.YEAR), date.get(GregorianCalendar.MONTH), date.get(GregorianCalendar.DAY_OF_MONTH));
			} else {
				date.setTime(stop);
				end = new GregorianCalendar(date.get(GregorianCalendar.YEAR), date.get(GregorianCalendar.MONTH), date.get(GregorianCalendar.DAY_OF_MONTH));
				date.setTime(start);
				begin = new GregorianCalendar(date.get(GregorianCalendar.YEAR), date.get(GregorianCalendar.MONTH), date.get(GregorianCalendar.DAY_OF_MONTH));
			}
			if (begin.equals(end)) {
				this.add(begin, new DateInterval(start, stop), holidayDao);
			} else {
				date.clear();
				for (date.set(begin.get(GregorianCalendar.YEAR), begin.get(GregorianCalendar.MONTH), begin.get(GregorianCalendar.DAY_OF_MONTH)); date.compareTo(end) <= 0; date
						.add(GregorianCalendar.DAY_OF_MONTH, 1)) {
					if (date.equals(begin)) {
						this.add(date, new DateInterval(start, true), holidayDao);
					} else if (date.equals(end)) {
						this.add(date, new DateInterval(stop, false), holidayDao);
					} else {
						this.add(date, new DateInterval(date), holidayDao);
					}
				}
			}
		}
	}

	protected abstract void add(GregorianCalendar date, DateInterval interval, HolidayDao holidayDao) throws Exception;
}
