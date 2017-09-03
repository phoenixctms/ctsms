package org.phoenixctms.ctsms.util.date;

import java.util.GregorianCalendar;

import org.phoenixctms.ctsms.domain.HolidayDao;
import org.phoenixctms.ctsms.vo.InventoryBookingOutVO;

public class BookingDuration extends DurationBase {

	private long totalSecs;

	public BookingDuration() {
		clear();
	}

	@Override
	protected void add(GregorianCalendar date, DateInterval interval, HolidayDao holidayDao) throws Exception {
		totalSecs += interval.getDuration();
	}

	public void clear() {
		totalSecs = 0;
	}

	public long getTotalDuration() {
		return totalSecs;
	}

	public void updateInventoryBooking(InventoryBookingOutVO booking) {
		booking.setTotalDuration(totalSecs);
	}
}
