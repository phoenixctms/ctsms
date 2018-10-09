package org.phoenixctms.ctsms.util.date;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.phoenixctms.ctsms.domain.Holiday;
import org.phoenixctms.ctsms.domain.HolidayDao;
import org.phoenixctms.ctsms.enumeration.VariablePeriod;
import org.phoenixctms.ctsms.enumeration.Weekday;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.DefaultMessages;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.MessageCodes;
import org.phoenixctms.ctsms.vo.CalendarWeekVO;
import org.phoenixctms.ctsms.vo.HolidayVO;

public final class DateCalc {

	private final static int MONTH_MIN_DAYS = 30;
	private final static int FEBRUARY_MIN_DAYS = 28;
	private final static int YEAR_MIN_DAYS = 365;
	private final static int LEAP_YEAR_MIN_DAYS = 366;
	private final static int YEAR_MIN_WEEKS = 52;
	/**
	 * Returns the Julian day number that begins at noon of
	 * this day, Positive year signifies A.D., negative year B.C.
	 * Remember that the year after 1 B.C. was 1 A.D.
	 *
	 * ref :
	 *  Numerical Recipes in C, 2nd ed., Cambridge University Press 1992
	 */
	// Gregorian Calendar adopted Oct. 15, 1582 (2299161)
	private static int JGREG = 15 + 31 * (10 + 12 * 1582);
	private static double HALFSECOND = 0.5;
	private static HashMap<Integer, HashMap<Date, HolidayVO>> allHolidayTableMap = new HashMap<Integer, HashMap<Date, HolidayVO>>();
	private static HashMap<Integer, HashMap<Date, HolidayVO>> holidayHolidayTableMap = new HashMap<Integer, HashMap<Date, HolidayVO>>();
	private static HashMap<Integer, HashMap<Date, HolidayVO>> noHolidayTableMap = new HashMap<Integer, HashMap<Date, HolidayVO>>();

	public static Date addInterval(Date date, VariablePeriod period, Long explicitDays) {
		return addInterval(date, period, explicitDays, 1);
	}

	private static Date addInterval(Date date, VariablePeriod period, Long explicitDays, int mult) {
		if (date != null) {
			if (period != null) {
				GregorianCalendar cal = new GregorianCalendar();
				cal.setTime(date);
				switch (period) {
					case EXPLICIT:
						cal.add(GregorianCalendar.DATE, mult * CommonUtil.safeLongToInt(explicitDays));
						break;
					case MONTH:
						cal.add(GregorianCalendar.MONTH, mult * 1);
						break;
					case TWO_MONTHS:
						cal.add(GregorianCalendar.MONTH, mult * 2);
						break;
					case THREE_MONTHS:
						cal.add(GregorianCalendar.MONTH, mult * 3);
						break;
					case FOUR_MONTHS:
						cal.add(GregorianCalendar.MONTH, mult * 4);
						break;
					case FIVE_MONTHS:
						cal.add(GregorianCalendar.MONTH, mult * 5);
						break;
					case SIX_MONTHS:
						cal.add(GregorianCalendar.MONTH, mult * 6);
						break;
					case SEVEN_MONTHS:
						cal.add(GregorianCalendar.MONTH, mult * 7);
						break;
					case EIGHT_MONTHS:
						cal.add(GregorianCalendar.MONTH, mult * 8);
						break;
					case NINE_MONTHS:
						cal.add(GregorianCalendar.MONTH, mult * 9);
						break;
					case TEN_MONTHS:
						cal.add(GregorianCalendar.MONTH, mult * 10);
						break;
					case ELEVEN_MONTHS:
						cal.add(GregorianCalendar.MONTH, mult * 11);
						break;
					case YEAR:
						cal.add(GregorianCalendar.YEAR, mult * 1);
						break;
					case EIGHTEEN_MONTHS:
						cal.add(GregorianCalendar.MONTH, mult * 18);
						break;
					case TWO_YEARS:
						cal.add(GregorianCalendar.YEAR, mult * 2);
						break;
					case THREE_YEARS:
						cal.add(GregorianCalendar.YEAR, mult * 3);
						break;
					case FOUR_YEARS:
						cal.add(GregorianCalendar.YEAR, mult * 4);
						break;
					case FIVE_YEARS:
						cal.add(GregorianCalendar.YEAR, mult * 5);
						break;
					case SIX_YEARS:
						cal.add(GregorianCalendar.YEAR, mult * 6);
						break;
					case SEVEN_YEARS:
						cal.add(GregorianCalendar.YEAR, mult * 7);
						break;
					case EIGHT_YEARS:
						cal.add(GregorianCalendar.YEAR, mult * 8);
						break;
					case NINE_YEARS:
						cal.add(GregorianCalendar.YEAR, mult * 9);
						break;
					case TEN_YEARS:
						cal.add(GregorianCalendar.YEAR, mult * 10);
						break;
					default:
						throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.UNSUPPORTED_VARIABLE_PERIOD, DefaultMessages.UNSUPPORTED_VARIABLE_PERIOD, period));
				}
				return cal.getTime();
			} else {
				return date;
			}
		} else {
			return null;
		}
	}

	public static Date addIntervals(Date date, VariablePeriod period, Long explicitDays, int mult) {
		return addInterval(date, period, explicitDays, Math.abs(mult));
	}

	private static HolidayVO cloneAndTranslateHoliday(HolidayVO holiday) {
		ArrayList<String> nameL10nKeys = new ArrayList<String>(holiday.getNameL10nKeys().size());
		ArrayList<String> names = new ArrayList<String>(holiday.getNameL10nKeys().size());
		Iterator<String> it = holiday.getNameL10nKeys().iterator();
		while (it.hasNext()) {
			String nameL10nKey = it.next();
			nameL10nKeys.add(nameL10nKey);
			names.add(L10nUtil.getHolidayName(Locales.USER, nameL10nKey));
		}
		HolidayVO result = new HolidayVO(holiday);
		result.setNameL10nKeys(nameL10nKeys);
		result.setNames(names);
		return result;
	}

	public static int compareVariablePeriodMinDays(VariablePeriod periodA, Long explicitDaysA, VariablePeriod periodB, Long explicitDaysB) {
		int delta = getVariablePeriodMinDays(periodA, explicitDaysA) - getVariablePeriodMinDays(periodB, explicitDaysB);
		if (delta > 0) {
			return 1;
		} else if (delta < 0) {
			return -1;
		} else {
			return 0;
		}
	}

	public static int dateDeltaDays(Date before, Date after) {
		return toJulian(after) - toJulian(before);
	}

	public static CalendarWeekVO getCalendarWeek(Date date) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		int year = cal.get(GregorianCalendar.YEAR);
		int period;
		switch ((new GregorianCalendar(year, 0, 1)).get(GregorianCalendar.DAY_OF_WEEK)) {
			case GregorianCalendar.SUNDAY:
				period = -1;
				break;
			case GregorianCalendar.MONDAY:
				period = 0;
				break;
			case GregorianCalendar.TUESDAY:
				period = 1;
				break;
			case GregorianCalendar.WEDNESDAY:
				period = 2;
				break;
			case GregorianCalendar.THURSDAY:
				period = 3;
				break;
			case GregorianCalendar.FRIDAY:
				period = -3;
				break;
			case GregorianCalendar.SATURDAY:
				period = -2;
				break;
			default:
				period = 0;
		}
		int week = (int) ((cal.get(GregorianCalendar.DAY_OF_YEAR) - 1 + period) / 7d + 1);
		if (week > getWeeksOfYear(year)) {
			return new CalendarWeekVO(cal.getTime(), 1, year + 1);
		} else if (week == 0) {
			return new CalendarWeekVO(cal.getTime(), getWeeksOfYear(year - 1), year - 1);
		} else {
			return new CalendarWeekVO(cal.getTime(), week, year);
		}
	}

	private static Date getEasterDate(int year) {
		// http://www.oremus.org/liturgy/etc/ktf/app/easter.html
		// Book of Common Prayer
		if (year > 1752) {
			int golden = year % 19 + 1;
			int solarCorrection = (int) (Math.floor((year - 1600d) / 100d) - Math.floor((year - 1600d) / 400d));
			int lunarCorrection = (int) Math.floor(((Math.floor((year - 1400d) / 100d)) * 8d) / 25d);
			int falsePaschalFullMoon = mod(3 - 11 * golden + solarCorrection - lunarCorrection, 30);
			int paschalFullMoon;
			if ((falsePaschalFullMoon == 29) || (falsePaschalFullMoon == 28 && golden > 11)) {
				paschalFullMoon = falsePaschalFullMoon - 1;
			} else {
				paschalFullMoon = falsePaschalFullMoon;
			}
			int dominicalNumber = mod((int) (year + Math.floor(year / 4d) - Math.floor(year / 100d) + Math.floor(year / 400d)), 7);
			int xDays = mod(mod(8 - dominicalNumber, 7) - mod(80 + paschalFullMoon, 7) - 1, 7) + 1;
			int eDays = paschalFullMoon + xDays;
			GregorianCalendar cal;
			if (eDays < 11) {
				eDays += 21;
				cal = new GregorianCalendar(year, 2, eDays);
			} else {
				eDays -= 10;
				cal = new GregorianCalendar(year, 3, eDays);
			}
			return cal.getTime();
		} else {
			throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.EASTER_DATE_YEAR_UNSUPPORTED, DefaultMessages.EASTER_DATE_YEAR_UNSUPPORTED, Integer.toString(year)));
		}
	}

	public static Date getEndOfDay(Date date) {
		if (date != null) {
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(date);
			cal = new GregorianCalendar(cal.get(GregorianCalendar.YEAR), cal.get(GregorianCalendar.MONTH), cal.get(GregorianCalendar.DAY_OF_MONTH), 23, 59, 59);
			cal.set(GregorianCalendar.MILLISECOND, 999);
			return cal.getTime();
		}
		return null;
	}

	public static Date getEndOfYear(Date date) {
		if (date != null) {
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(date);
			cal = new GregorianCalendar(cal.get(GregorianCalendar.YEAR), 11, 31, 23, 59, 59);
			cal.set(GregorianCalendar.MILLISECOND, 999);
			return cal.getTime();
		}
		return null;
	}

	public static Collection<HolidayVO> getHolidays(Date start, Date stop, Boolean isHoliday, HolidayDao holidayDao) throws Exception {
		GregorianCalendar calStart = new GregorianCalendar();
		calStart.setTime(start);
		GregorianCalendar calStop = new GregorianCalendar();
		calStop.setTime(stop);
		int startYear = calStart.get(GregorianCalendar.YEAR);
		int stopYear = calStop.get(GregorianCalendar.YEAR);
		ArrayList<HolidayVO> holidays;
		if (startYear < stopYear) {
			holidays = new ArrayList<HolidayVO>((stopYear - startYear + 1) * YEAR_MIN_DAYS); // assume each day is a holiday for hashmap initial load...
			Iterator<HolidayVO> it = getHolidayTable(startYear, isHoliday, holidayDao).values().iterator();
			while (it.hasNext()) {
				HolidayVO holiday = it.next();
				if (start.compareTo(holiday.getDate()) <= 0) {
					holidays.add(cloneAndTranslateHoliday(holiday));
				}
			}
			for (int year = startYear + 1; year < stopYear; year++) {
				it = getHolidayTable(year, isHoliday, holidayDao).values().iterator();
				while (it.hasNext()) {
					holidays.add(cloneAndTranslateHoliday(it.next()));
				}
			}
			it = getHolidayTable(stopYear, isHoliday, holidayDao).values().iterator();
			while (it.hasNext()) {
				HolidayVO holiday = it.next();
				if (stop.compareTo(holiday.getDate()) >= 0) {
					holidays.add(cloneAndTranslateHoliday(holiday));
				}
			}
		} else if (startYear == stopYear && start.compareTo(stop) <= 0) {
			holidays = new ArrayList<HolidayVO>(YEAR_MIN_DAYS);
			Iterator<HolidayVO> it = getHolidayTable(startYear, isHoliday, holidayDao).values().iterator();
			while (it.hasNext()) {
				HolidayVO holiday = it.next();
				if (start.compareTo(holiday.getDate()) <= 0 && stop.compareTo(holiday.getDate()) >= 0) {
					holidays.add(cloneAndTranslateHoliday(holiday));
				}
			}
		} else {
			holidays = new ArrayList<HolidayVO>();
		}
		return holidays;
	}

	private static HashMap<Date, HolidayVO> getHolidayTable(int year, Boolean isHoliday, HolidayDao holidayDao) throws Exception {
		Map<Integer, HashMap<Date, HolidayVO>> holidayTableMap;
		if (isHoliday == null) {
			holidayTableMap = allHolidayTableMap;
		} else if (isHoliday == false) {
			holidayTableMap = noHolidayTableMap;
		} else {
			holidayTableMap = holidayHolidayTableMap;
		}
		HashMap<Date, HolidayVO> holidayMap;
		Exception error = null;
		synchronized (holidayTableMap) {
			if (!holidayTableMap.containsKey(year)) {
				holidayMap = null;
				try {
					Collection<Holiday> holidays = holidayDao.findByBaseHolidayActive(null, isHoliday, true);
					holidayMap = new HashMap<Date, HolidayVO>(holidays.size());
					Date easterDate = getEasterDate(year);
					GregorianCalendar cal = new GregorianCalendar();
					Iterator<Holiday> it = holidays.iterator();
					while (it.hasNext()) {
						Holiday holiday = it.next();
						cal.clear();
						cal.setLenient(false);
						int offset = CommonUtil.safeLongToInt(holiday.getOffsetDays());
						switch (holiday.getBase()) {
							case STATIC_DATE:
								if (holiday.getMonth() == null) {
									throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.HOLIDAY_MONTH_UNDEFINED, DefaultMessages.HOLIDAY_MONTH_UNDEFINED));
								}
								if (holiday.getDay() == null) {
									throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.HOLIDAY_DAY_UNDEFINED, DefaultMessages.HOLIDAY_DAY_UNDEFINED));
								}
								cal.set(year, holiday.getMonth() - 1, holiday.getDay());
								try {
									cal.add(GregorianCalendar.DAY_OF_MONTH, offset);
									storeHoliday(year, cal, holiday, holidayMap);
								} catch (IllegalArgumentException e) {
									if (!(holiday.getMonth() == 2 && holiday.getDay() == 29)) {
										throw e;
									}
								}
								break;
							case EASTER_DATE:
								cal.setTime(easterDate);
								cal.add(GregorianCalendar.DAY_OF_MONTH, offset);
								storeHoliday(year, cal, holiday, holidayMap);
								break;
							case NTH_WEEKDAY_AFTER_DATE:
								if (holiday.getMonth() == null) {
									throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.HOLIDAY_MONTH_UNDEFINED, DefaultMessages.HOLIDAY_MONTH_UNDEFINED));
								}
								if (holiday.getDay() == null) {
									throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.HOLIDAY_DAY_UNDEFINED, DefaultMessages.HOLIDAY_DAY_UNDEFINED));
								}
								if (holiday.getWeekday() == null) {
									throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.HOLIDAY_WEEKDAY_UNDEFINED, DefaultMessages.HOLIDAY_WEEKDAY_UNDEFINED));
								}
								if (holiday.getN() == null) {
									throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.HOLIDAY_N_UNDEFINED, DefaultMessages.HOLIDAY_N_UNDEFINED));
								}
								cal.set(year, holiday.getMonth() - 1, holiday.getDay());
								try {
									cal.setTime(getNthWeekdayAfterDate(cal.getTime(), holiday.getWeekday(), holiday.getN()));
									cal.add(GregorianCalendar.DAY_OF_MONTH, offset);
									storeHoliday(year, cal, holiday, holidayMap);
								} catch (IllegalArgumentException e) {
									if (!(holiday.getMonth() == 2 && holiday.getDay() == 29)) {
										throw e;
									}
								}
								break;
							case NTH_WEEKDAY_BEFORE_DATE:
								if (holiday.getMonth() == null) {
									throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.HOLIDAY_MONTH_UNDEFINED, DefaultMessages.HOLIDAY_MONTH_UNDEFINED));
								}
								if (holiday.getDay() == null) {
									throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.HOLIDAY_DAY_UNDEFINED, DefaultMessages.HOLIDAY_DAY_UNDEFINED));
								}
								if (holiday.getWeekday() == null) {
									throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.HOLIDAY_WEEKDAY_UNDEFINED, DefaultMessages.HOLIDAY_WEEKDAY_UNDEFINED));
								}
								if (holiday.getN() == null) {
									throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.HOLIDAY_N_UNDEFINED, DefaultMessages.HOLIDAY_N_UNDEFINED));
								}
								cal.set(year, holiday.getMonth() - 1, holiday.getDay());
								try {
									cal.setTime(getNthWeekdayBeforeDate(cal.getTime(), holiday.getWeekday(), holiday.getN()));
									cal.add(GregorianCalendar.DAY_OF_MONTH, offset);
									storeHoliday(year, cal, holiday, holidayMap);
								} catch (IllegalArgumentException e) {
									if (!(holiday.getMonth() == 2 && holiday.getDay() == 29)) {
										throw e;
									}
								}
								break;
							case WEEKDAY:
								if (holiday.getWeekday() == null) {
									throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.HOLIDAY_WEEKDAY_UNDEFINED, DefaultMessages.HOLIDAY_WEEKDAY_UNDEFINED));
								}
								cal.set(year, 0, 1);
								cal.setTime(getNthWeekdayAfterDate(cal.getTime(), holiday.getWeekday(), 1));
								GregorianCalendar nextYear = new GregorianCalendar(year + 1, 0, 1);
								while (cal.before(nextYear)) {
									storeHoliday(year, cal, holiday, holidayMap);
									cal.add(GregorianCalendar.DAY_OF_MONTH, 7);
								}
								break;
							default:
								throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.UNSUPPORTED_HOLIDAY_BASE_DATE, DefaultMessages.UNSUPPORTED_HOLIDAY_BASE_DATE,
										holiday.getBase()));
						}
					}
					holidayTableMap.put(year, holidayMap); // insert table only if no error occured
				} catch (Exception e) {
					error = e;
				}
			} else {
				holidayMap = holidayTableMap.get(year);
			}
		}
		if (error != null) {
			throw error;
		}
		return holidayMap;
	}

	public static Integer getHour(Date date) {
		if (date != null) {
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(date);
			return cal.get(Calendar.HOUR_OF_DAY);
		}
		return null;
	}

	public static Date getMillisCleared(Date date) {
		if (date != null) {
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(date);
			//cal = new GregorianCalendar(cal.get(GregorianCalendar.YEAR), cal.get(GregorianCalendar.MONTH), cal.get(GregorianCalendar.DAY_OF_MONTH), cal.get(GregorianCalendar.HOUR), cal.get(GregorianCalendar.MINUTE), cal.get(GregorianCalendar.SECOND));
			cal.set(GregorianCalendar.MILLISECOND, 0);
			return cal.getTime();
		}
		return null;
	}

	public static Integer getMinute(Date date) {
		if (date != null) {
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(date);
			return cal.get(Calendar.MINUTE);
		}
		return null;
	}

	public static Date getNextRecurrence(Date today, Date date, VariablePeriod period, Long explicitDays, boolean previous) {
		int delta;
		int sign;
		if (today.compareTo(date) < 0) {
			delta = dateDeltaDays(today, date);
			sign = -1;
		} else {
			delta = dateDeltaDays(date, today);
			sign = 1;
		}
		int variablePeriodMinDays = getVariablePeriodMinDays(period, explicitDays);
		int recurrencesMin = (int) Math.floor(((double) delta) / ((double) variablePeriodMinDays));
		Date result = addInterval(date, period, explicitDays, recurrencesMin);
		while (sign * result.compareTo(today) < 0) {
			recurrencesMin += sign;
			result = addInterval(date, period, explicitDays, recurrencesMin);
		}
		if (previous && recurrencesMin > 0) {
			recurrencesMin -= sign;
			result = addInterval(date, period, explicitDays, recurrencesMin);
		}
		return result;
	}

	private static Date getNthWeekdayAfterDate(Date date, Weekday weekday, int n) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		int delta = getWeekdayNum(weekday) - cal.get(GregorianCalendar.DAY_OF_WEEK); // sunday ..1, monday ..2, ...
		if (delta < 0) {
			delta += 7;
		}
		cal.add(Calendar.DATE, delta + (n - 1) * 7);
		return cal.getTime();
	}

	private static Date getNthWeekdayBeforeDate(Date date, Weekday weekday, int n) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		int delta = getWeekdayNum(weekday) - cal.get(GregorianCalendar.DAY_OF_WEEK); // sunday ..1, monday ..2, ...
		if (delta > 0) {
			delta -= 7;
		}
		cal.add(Calendar.DATE, delta - (n - 1) * 7);
		return cal.getTime();
	}

	public static Date getStartOfDay(Date date) {
		if (date != null) {
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(date);
			return (new GregorianCalendar(cal.get(GregorianCalendar.YEAR), cal.get(GregorianCalendar.MONTH), cal.get(GregorianCalendar.DAY_OF_MONTH), 0, 0, 0)).getTime();
		}
		return null;
	}

	public static Date getStartOfYear(Date date) {
		if (date != null) {
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(date);
			return (new GregorianCalendar(cal.get(GregorianCalendar.YEAR), 0, 1, 0, 0, 0)).getTime();
		}
		return null;
	}

	private static int getVariablePeriodMinDays(VariablePeriod period, Long explicitDays) {
		switch (period) {
			case EXPLICIT:
				return CommonUtil.safeLongToInt(explicitDays);
			case MONTH:
				return FEBRUARY_MIN_DAYS;
			case TWO_MONTHS:
				return MONTH_MIN_DAYS + FEBRUARY_MIN_DAYS;
			case THREE_MONTHS:
				return 2 * MONTH_MIN_DAYS + FEBRUARY_MIN_DAYS;
			case FOUR_MONTHS:
				return 3 * MONTH_MIN_DAYS + FEBRUARY_MIN_DAYS;
			case FIVE_MONTHS:
				return 4 * MONTH_MIN_DAYS + FEBRUARY_MIN_DAYS;
			case SIX_MONTHS:
				return 5 * MONTH_MIN_DAYS + FEBRUARY_MIN_DAYS;
			case SEVEN_MONTHS:
				return 6 * MONTH_MIN_DAYS + FEBRUARY_MIN_DAYS;
			case EIGHT_MONTHS:
				return 7 * MONTH_MIN_DAYS + FEBRUARY_MIN_DAYS;
			case NINE_MONTHS:
				return 8 * MONTH_MIN_DAYS + FEBRUARY_MIN_DAYS;
			case TEN_MONTHS:
				return 9 * MONTH_MIN_DAYS + FEBRUARY_MIN_DAYS;
			case ELEVEN_MONTHS:
				return 10 * MONTH_MIN_DAYS + FEBRUARY_MIN_DAYS;
			case YEAR:
				return YEAR_MIN_DAYS;
			case EIGHTEEN_MONTHS:
				return YEAR_MIN_DAYS + 5 * MONTH_MIN_DAYS + FEBRUARY_MIN_DAYS;
			case TWO_YEARS:
				return 2 * YEAR_MIN_DAYS;
			case THREE_YEARS:
				return 3 * YEAR_MIN_DAYS;
			case FOUR_YEARS:
				return 4 * YEAR_MIN_DAYS;
			case FIVE_YEARS:
				return 5 * YEAR_MIN_DAYS;
			case SIX_YEARS:
				return 6 * YEAR_MIN_DAYS;
			case SEVEN_YEARS:
				return 7 * YEAR_MIN_DAYS;
			case EIGHT_YEARS:
				return 7 * YEAR_MIN_DAYS + LEAP_YEAR_MIN_DAYS;
			case NINE_YEARS:
				return 8 * YEAR_MIN_DAYS + LEAP_YEAR_MIN_DAYS;
			case TEN_YEARS:
				return 9 * YEAR_MIN_DAYS + LEAP_YEAR_MIN_DAYS;
			default:
				throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.UNSUPPORTED_VARIABLE_PERIOD, DefaultMessages.UNSUPPORTED_VARIABLE_PERIOD, period));
		}
	}

	private static int getWeekdayNum(Weekday weekday) {
		switch (weekday) {
			case SUNDAY:
				return GregorianCalendar.SUNDAY;
			case MONDAY:
				return GregorianCalendar.MONDAY;
			case TUESDAY:
				return GregorianCalendar.TUESDAY;
			case WEDNESDAY:
				return GregorianCalendar.WEDNESDAY;
			case THURSDAY:
				return GregorianCalendar.THURSDAY;
			case FRIDAY:
				return GregorianCalendar.FRIDAY;
			case SATURDAY:
				return GregorianCalendar.SATURDAY;
			default:
				throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.UNSUPPORTED_WEEKDAY, DefaultMessages.UNSUPPORTED_WEEKDAY, weekday));
		}
	}

	private static int getWeeksOfYear(int year) {
		// Get the number of calendar weeks of a given year. DIN 1355:
		// 'Ein Jahr hat 53 Wochen wenn es an einem Do beginnt oder endet'
		int weeks = YEAR_MIN_WEEKS;
		if ((new GregorianCalendar(year, 0, 1)).get(GregorianCalendar.DAY_OF_WEEK) == GregorianCalendar.THURSDAY ||
				(new GregorianCalendar(year, 11, 31)).get(GregorianCalendar.DAY_OF_WEEK) == GregorianCalendar.THURSDAY) {
			weeks++;
		}
		return weeks;
	}

	public static Boolean isDatetime(Date date) {
		if (date != null) {
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(date);
			if (cal.get(GregorianCalendar.HOUR) != 0 || cal.get(GregorianCalendar.MINUTE) != 0 || cal.get(GregorianCalendar.SECOND) != 0
					|| cal.get(GregorianCalendar.MILLISECOND) != 0) {
				return true;
			} else {
				return false;
			}
		}
		return null;
	}

	public static boolean isHoliday(Date date, HolidayDao holidayDao) throws Exception {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		HolidayVO holidayVO = getHolidayTable(cal.get(GregorianCalendar.YEAR), true, holidayDao).get(date);
		if (holidayVO != null && holidayVO.isHoliday()) {
			return true;
		}
		return false;
	}

	public static Boolean isTime(Date date) {
		if (date != null) {
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(date);
			if (cal.get(GregorianCalendar.DAY_OF_MONTH) == 1 && cal.get(GregorianCalendar.MONTH) == 0 && cal.get(GregorianCalendar.YEAR) == 1970) {
				return true;
			} else {
				return false;
			}
		}
		return null;
	}

	private static int mod(int a, int b) {
		int result = a % b; // b must be > 0 !
		if (result < 0) {
			result += b;
		}
		return result;
		// return (a % b) + (a < 0 ? b : 0);
	}

	private static boolean storeHoliday(int year, GregorianCalendar cal, Holiday holiday, HashMap<Date, HolidayVO> holidayMap) {
		boolean inserted;
		if (cal.get(GregorianCalendar.YEAR) == year) {
			HolidayVO holidayVO;
			Date date = cal.getTime();
			if (holidayMap.containsKey(date)) {
				holidayVO = holidayMap.get(date);
				inserted = false;
			} else {
				holidayVO = new HolidayVO();
				holidayMap.put(date, holidayVO);
				holidayVO.setDate(date);
				holidayVO.setHoliday(false);
				inserted = true;
			}
			holidayVO.getNameL10nKeys().add(holiday.getNameL10nKey());
			holidayVO.setHoliday(holidayVO.getHoliday() || holiday.isHoliday());
		} else {
			inserted = false;
		}
		return inserted;
	}

	public static Date subInterval(Date date, VariablePeriod period, Long explicitDays) {
		return addInterval(date, period, explicitDays, -1);
	}

	public static Date subIntervals(Date date, VariablePeriod period, Long explicitDays, int mult) {
		return addInterval(date, period, explicitDays, Math.abs(mult) * -1);
	}

	private static int toJulian(Date date) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		int year = cal.get(GregorianCalendar.YEAR) - 1900;
		int month = cal.get(GregorianCalendar.MONTH) + 1; // jan=1, feb=2,...
		int day = cal.get(GregorianCalendar.DAY_OF_MONTH);
		int julianYear = year;
		if (year < 0) {
			julianYear++;
		}
		int julianMonth = month;
		if (month > 2) {
			julianMonth++;
		} else {
			julianYear--;
			julianMonth += 13;
		}
		double julian = (java.lang.Math.floor(365.25 * julianYear)
				+ java.lang.Math.floor(30.6001 * julianMonth) + day + 1720995.0);
		if (day + 31 * (month + 12 * year) >= JGREG) {
			// change over to Gregorian calendar
			int ja = (int) (0.01 * julianYear);
			julian += 2 - ja + (0.25 * ja);
		}
		return (int) java.lang.Math.floor(julian);
	}

	private DateCalc() {
	}
}
