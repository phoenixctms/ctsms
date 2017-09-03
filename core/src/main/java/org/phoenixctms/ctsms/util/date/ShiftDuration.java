package org.phoenixctms.ctsms.util.date;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import org.phoenixctms.ctsms.domain.HolidayDao;
import org.phoenixctms.ctsms.util.DefaultSettings;
import org.phoenixctms.ctsms.util.SettingCodes;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.vo.DutyRosterTurnOutVO;

public class ShiftDuration extends DurationBase {

	private static ArrayList<DateInterval> getHolidayIntervals(GregorianCalendar date, HolidayDao holidayDao) throws Exception {
		ArrayList<DateInterval> holidayIntervals = new ArrayList<DateInterval>();
		if (DateCalc.isHoliday(date.getTime(), holidayDao)) {
			holidayIntervals.add(new DateInterval(date));
		}
		return holidayIntervals;
	}

	private static ArrayList<DateInterval> getNightIntervals(GregorianCalendar date) {
		ArrayList<DateInterval> nightIntervals = new ArrayList<DateInterval>();
		nightIntervals.add(new DateInterval(date, Settings.getInt(SettingCodes.NIGHT_INTERVAL_STOP_HOUR, Bundle.SETTINGS, DefaultSettings.NIGHT_INTERVAL_STOP_HOUR), Settings
				.getInt(SettingCodes.NIGHT_INTERVAL_STOP_MINUTE, Bundle.SETTINGS, DefaultSettings.NIGHT_INTERVAL_STOP_MINUTE), false));
		nightIntervals.add(new DateInterval(date, Settings.getInt(SettingCodes.NIGHT_INTERVAL_START_HOUR, Bundle.SETTINGS, DefaultSettings.NIGHT_INTERVAL_START_HOUR), Settings
				.getInt(SettingCodes.NIGHT_INTERVAL_START_MINUTE, Bundle.SETTINGS, DefaultSettings.NIGHT_INTERVAL_START_MINUTE), true));
		return nightIntervals;
	}

	private long holidaySecs;
	private long nightSecs;
	private long totalSecs;

	public ShiftDuration() {
		clear();
	}

	@Override
	protected void add(GregorianCalendar date, DateInterval interval, HolidayDao holidayDao) throws Exception {
		holidaySecs += interval.getIntervalOverlapSecs(getHolidayIntervals(date, holidayDao));
		nightSecs += interval.getIntervalOverlapSecs(getNightIntervals(date));
		totalSecs += interval.getDuration();
	}

	public void clear() {
		holidaySecs = 0;
		nightSecs = 0;
		totalSecs = 0;
	}

	public long getHolidayDuration() {
		return holidaySecs;
	}

	public long getNightDuration() {
		return nightSecs;
	}

	public long getTotalDuration() {
		return totalSecs;
	}

	public void updateDutyRosterTurn(DutyRosterTurnOutVO dutyRosterTurn) {
		dutyRosterTurn.setHolidayDuration(holidaySecs);
		dutyRosterTurn.setNightDuration(nightSecs);
		dutyRosterTurn.setTotalDuration(totalSecs);
		dutyRosterTurn.setExtraShift(holidaySecs > Settings.getInt(SettingCodes.HOLIDAY_SHIFT_THRESHOLD_SECS, Bundle.SETTINGS, DefaultSettings.HOLIDAY_SHIFT_THRESHOLD_SECS));
		dutyRosterTurn.setNightShift(nightSecs > Settings.getInt(SettingCodes.NIGHT_SHIFT_THRESHOLD_SECS, Bundle.SETTINGS, DefaultSettings.NIGHT_SHIFT_THRESHOLD_SECS));
		long extraSecs = totalSecs;
		if (dutyRosterTurn.getExtraShift() || dutyRosterTurn.getNightShift()) {
			extraSecs += Settings.getInt(SettingCodes.SHIFT_EXTRA_SECS, Bundle.SETTINGS, DefaultSettings.SHIFT_EXTRA_SECS);
		}
		dutyRosterTurn.setWeightedDuration(extraSecs);
	}
}