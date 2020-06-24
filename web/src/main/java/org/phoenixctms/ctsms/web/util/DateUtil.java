package org.phoenixctms.ctsms.web.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.TimeZoneVO;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;

public final class DateUtil {

	public enum ConverterType {

		DATE("date"), TIME("time"), DATETIME("both");

		private final String value;

		/**
		 * @param text
		 */
		private ConverterType(final String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
		}
	}

	public enum DurationUnitOfTime {
		SECONDS, MINUTES, HOURS, DAYS, MONTHS, YEARS
	}

	public final static String JQPLOT_DATE_PATTERN = "%d.%m.%Y";
	public final static String JQPLOT_JS_DATE_PATTERN = "yyyy-MM-dd HH:mm";
	private final static String JS_DATE_UTC_PATTERN = "EEE, dd MMM yyyy HH:mm:ss z"; // rfc1123

	public static Date addDayMinuteDelta(Date date, int days, int minutes) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.DATE, days);
		cal.add(Calendar.MINUTE, minutes);
		return cal.getTime();
	}

	private static StringBuilder durationUnitOfTimeValueToStringBuilder(double value, int decimals, DurationUnitOfTime unitOfTime) {
		StringBuilder sb = new StringBuilder();
		String unitLabelPlural;
		String unitLabelSingular;
		switch (unitOfTime) {
			case SECONDS:
				unitLabelPlural = " " + Messages.getString(MessageCodes.SECONDS_LABEL_PLURAL);
				unitLabelSingular = " " + Messages.getString(MessageCodes.SECONDS_LABEL_SINGULAR);
				break;
			case MINUTES:
				unitLabelPlural = " " + Messages.getString(MessageCodes.MINUTES_LABEL_PLURAL);
				unitLabelSingular = " " + Messages.getString(MessageCodes.MINUTES_LABEL_SINGULAR);
				break;
			case HOURS:
				unitLabelPlural = " " + Messages.getString(MessageCodes.HOURS_LABEL_PLURAL);
				unitLabelSingular = " " + Messages.getString(MessageCodes.HOURS_LABEL_SINGULAR);
				break;
			case DAYS:
				unitLabelPlural = " " + Messages.getString(MessageCodes.DAYS_LABEL_PLURAL);
				unitLabelSingular = " " + Messages.getString(MessageCodes.DAYS_LABEL_SINGULAR);
				break;
			case MONTHS:
				unitLabelPlural = " " + Messages.getString(MessageCodes.MONTHS_LABEL_PLURAL);
				unitLabelSingular = " " + Messages.getString(MessageCodes.MONTHS_LABEL_SINGULAR);
				break;
			case YEARS:
				unitLabelPlural = " " + Messages.getString(MessageCodes.YEARS_LABEL_PLURAL);
				unitLabelSingular = " " + Messages.getString(MessageCodes.YEARS_LABEL_SINGULAR);
				break;
			default:
				unitLabelPlural = "";
				unitLabelSingular = "";
				break;
		}
		if (decimals < 1) {
			if (((long) value) == 1) {
				sb.append("1");
				sb.append(unitLabelSingular);
			} else {
				sb.append(((long) value));
				sb.append(unitLabelPlural);
			}
		} else {
			sb.append(String.format("%." + decimals + "f", value));
			sb.append(unitLabelPlural);
		}
		return sb;
	}

	public static String getBookingDurationString(Date start, Date stop) {
		return DateUtil.getDurationString(start, stop,
				Settings.getDurationUnitOfTime(SettingCodes.BOOKING_MOST_SIGNIFICANT_DURATION_UNIT_OF_TIME, Bundle.SETTINGS,
						DefaultSettings.BOOKING_MOST_SIGNIFICANT_DURATION_UNIT_OF_TIME),
				Settings.getDurationUnitOfTime(SettingCodes.BOOKING_LEAST_SIGNIFICANT_DURATION_UNIT_OF_TIME, Bundle.SETTINGS,
						DefaultSettings.BOOKING_LEAST_SIGNIFICANT_DURATION_UNIT_OF_TIME),
				Settings.getInt(SettingCodes.BOOKING_LEAST_SIGNIFICANT_DURATION_UNIT_OF_TIME_DECIMALS, Bundle.SETTINGS,
						DefaultSettings.BOOKING_LEAST_SIGNIFICANT_DURATION_UNIT_OF_TIME_DECIMALS));
	}

	public static String getBookingDurationString(long duration) {
		return DateUtil.getDurationString(duration,
				Settings.getDurationUnitOfTime(SettingCodes.BOOKING_MOST_SIGNIFICANT_DURATION_UNIT_OF_TIME, Bundle.SETTINGS,
						DefaultSettings.BOOKING_MOST_SIGNIFICANT_DURATION_UNIT_OF_TIME),
				Settings.getDurationUnitOfTime(SettingCodes.BOOKING_LEAST_SIGNIFICANT_DURATION_UNIT_OF_TIME, Bundle.SETTINGS,
						DefaultSettings.BOOKING_LEAST_SIGNIFICANT_DURATION_UNIT_OF_TIME),
				Settings.getInt(SettingCodes.BOOKING_LEAST_SIGNIFICANT_DURATION_UNIT_OF_TIME_DECIMALS, Bundle.SETTINGS,
						DefaultSettings.BOOKING_LEAST_SIGNIFICANT_DURATION_UNIT_OF_TIME_DECIMALS));
	}

	public static DateFormat getDateFormat() {
		return getDateFormat(Settings.getBoolean(SettingCodes.DATE_USER_TIME_ZONE, Bundle.SETTINGS, DefaultSettings.DATE_USER_TIME_ZONE));
	}

	public static DateFormat getDateFormat(boolean isUserTimeZone) {
		Locale locale = WebUtil.getLocale();
		TimeZone timeZone = WebUtil.getTimeZone();
		DateFormat dateFormat = new SimpleDateFormat(CommonUtil.getInputDatePattern(WebUtil.getDateFormat()), locale);
		if (isUserTimeZone) {
			dateFormat.setTimeZone(timeZone);
		}
		return dateFormat;
	}

	public static String getDateStartStopString(Date start, Date stop) {
		return CommonUtil.getDateStartStopString(start, stop, getDateFormat());
	}

	public static DateFormat getDateTimeFormat() {
		return getDateTimeFormat(Settings.getBoolean(SettingCodes.DATE_TIME_USER_TIME_ZONE, Bundle.SETTINGS, DefaultSettings.DATE_TIME_USER_TIME_ZONE));
	}

	public static DateFormat getDateTimeFormat(boolean isUserTimeZone) {
		Locale locale = WebUtil.getLocale();
		TimeZone timeZone = WebUtil.getTimeZone();
		DateFormat dateFormat = new SimpleDateFormat(CommonUtil.getInputDateTimePattern(WebUtil.getDateFormat()), locale);
		if (isUserTimeZone) {
			dateFormat.setTimeZone(timeZone);
		}
		return dateFormat;
	}

	public static String getDateTimeStartStopString(Date start, Date stop) {
		StringBuilder sb = new StringBuilder();
		if (start != null) {
			sb.append(getDateTimeFormat().format(start));
		} else {
			sb.append("?");
		}
		sb.append(" - ");
		if (stop != null) {
			sb.append(getDateTimeFormat().format(stop));
		} else {
			sb.append("?");
		}
		return sb.toString();
	}

	public static DateFormat getDigitsOnlyDateTimeFormat() {
		DateFormat dateFormat = new SimpleDateFormat(CommonUtil.DIGITS_ONLY_DATETIME_PATTERN);
		dateFormat.setTimeZone(WebUtil.getTimeZone());
		return dateFormat;
	}

	public static String getDurationString(Date start, Date stop) {
		return getDurationString(start, stop,
				Settings.getDurationUnitOfTime(SettingCodes.DEFAULT_MOST_SIGNIFICANT_DURATION_UNIT_OF_TIME, Bundle.SETTINGS,
						DefaultSettings.DEFAULT_MOST_SIGNIFICANT_DURATION_UNIT_OF_TIME),
				Settings.getDurationUnitOfTime(SettingCodes.DEFAULT_LEAST_SIGNIFICANT_DURATION_UNIT_OF_TIME, Bundle.SETTINGS,
						DefaultSettings.DEFAULT_LEAST_SIGNIFICANT_DURATION_UNIT_OF_TIME),
				Settings.getInt(SettingCodes.DEFAULT_LEAST_SIGNIFICANT_DURATION_UNIT_OF_TIME_DECIMALS, Bundle.SETTINGS,
						DefaultSettings.DEFAULT_LEAST_SIGNIFICANT_DURATION_UNIT_OF_TIME_DECIMALS));
	}

	public static String getDurationString(Date start, Date stop, DurationUnitOfTime mostSignificant, DurationUnitOfTime leastSignificant, int leastSignificantDecimals) {
		if (start != null && stop != null) {
			return getDurationString(CommonUtil.dateDeltaSecs(start, stop), mostSignificant, leastSignificant, leastSignificantDecimals);
		}
		return "";
	}

	public static String getSignSymbol(Integer integer) {
		if (integer != null) {
			return integer < 0 ? "-" : (integer > 0 ? "+" : "");
		}
		return "";
	}

	public static String getSignSymbol(Long lng) {
		if (lng != null) {
			return lng < 0l ? "-" : (lng > 0l ? "+" : "");
		}
		return "";
	}

	public static String getDurationString(long duration) {
		return getDurationString(duration,
				Settings.getDurationUnitOfTime(SettingCodes.DEFAULT_MOST_SIGNIFICANT_DURATION_UNIT_OF_TIME, Bundle.SETTINGS,
						DefaultSettings.DEFAULT_MOST_SIGNIFICANT_DURATION_UNIT_OF_TIME),
				Settings.getDurationUnitOfTime(SettingCodes.DEFAULT_LEAST_SIGNIFICANT_DURATION_UNIT_OF_TIME, Bundle.SETTINGS,
						DefaultSettings.DEFAULT_LEAST_SIGNIFICANT_DURATION_UNIT_OF_TIME),
				Settings.getInt(SettingCodes.DEFAULT_LEAST_SIGNIFICANT_DURATION_UNIT_OF_TIME_DECIMALS, Bundle.SETTINGS,
						DefaultSettings.DEFAULT_LEAST_SIGNIFICANT_DURATION_UNIT_OF_TIME_DECIMALS));
	}

	public static String getDurationString(long duration, DurationUnitOfTime mostSignificant, DurationUnitOfTime leastSignificant, int leastSignificantDecimals) {
		StringBuffer sb = new StringBuffer();
		double delta = Math.abs(duration);
		double seconds;
		double minutes;
		double hours;
		double days;
		double months;
		double years;
		if (!DurationUnitOfTime.SECONDS.equals(leastSignificant)) {
			delta = delta / 60; // minutes
			if (!DurationUnitOfTime.MINUTES.equals(leastSignificant)) {
				delta = delta / 60; // hours
				if (!DurationUnitOfTime.HOURS.equals(leastSignificant)) {
					delta = delta / 24; // days
					if (!DurationUnitOfTime.DAYS.equals(leastSignificant)) {
						delta = delta / 30; // months
						if (!DurationUnitOfTime.MONTHS.equals(leastSignificant)) {
							delta = delta / 12; // years
							if (!DurationUnitOfTime.YEARS.equals(leastSignificant)) {
								// unknown leastSignificant DurationUnitOfTime:
								return sb.toString();
							} else {
								seconds = 0.0;
								minutes = 0.0;
								hours = 0.0;
								days = 0.0;
								months = 0.0;
								if (DurationUnitOfTime.YEARS.equals(mostSignificant)) {
									years = delta;
								} else {
									// mostSignificant DurationUnitOfTime lower than leastSignificant DurationUnitOfTime:
									return sb.toString();
								}
							}
						} else {
							seconds = 0.0;
							minutes = 0.0;
							hours = 0.0;
							days = 0.0;
							years = 0.0;
							if (DurationUnitOfTime.MONTHS.equals(mostSignificant)) {
								months = delta;
							} else {
								months = (delta >= 12) ? delta % 12 : delta;
								delta = delta / 12;
								if (DurationUnitOfTime.YEARS.equals(mostSignificant)) {
									years = Math.floor(delta);
								} else {
									// mostSignificant DurationUnitOfTime lower than leastSignificant DurationUnitOfTime:
									return sb.toString();
								}
							}
						}
					} else {
						seconds = 0.0;
						minutes = 0.0;
						hours = 0.0;
						months = 0.0;
						years = 0.0;
						if (DurationUnitOfTime.DAYS.equals(mostSignificant)) {
							days = delta;
						} else {
							days = (delta >= 30) ? delta % 30 : delta;
							delta = delta / 30;
							if (DurationUnitOfTime.MONTHS.equals(mostSignificant)) {
								months = Math.floor(delta);
							} else {
								months = (delta >= 12) ? delta % 12 : delta;
								delta = delta / 12;
								if (DurationUnitOfTime.YEARS.equals(mostSignificant)) {
									years = Math.floor(delta);
								} else {
									// mostSignificant DurationUnitOfTime lower than leastSignificant DurationUnitOfTime:
									return sb.toString();
								}
							}
						}
					}
				} else {
					seconds = 0.0;
					minutes = 0.0;
					days = 0.0;
					months = 0.0;
					years = 0.0;
					if (DurationUnitOfTime.HOURS.equals(mostSignificant)) {
						hours = delta;
					} else {
						hours = (delta >= 24) ? delta % 24 : delta;
						delta = delta / 24;
						if (DurationUnitOfTime.DAYS.equals(mostSignificant)) {
							days = Math.floor(delta);
						} else {
							days = (delta >= 30) ? delta % 30 : delta;
							delta = delta / 30;
							if (DurationUnitOfTime.MONTHS.equals(mostSignificant)) {
								months = Math.floor(delta);
							} else {
								months = (delta >= 12) ? delta % 12 : delta;
								delta = delta / 12;
								if (DurationUnitOfTime.YEARS.equals(mostSignificant)) {
									years = Math.floor(delta);
								} else {
									// mostSignificant DurationUnitOfTime lower than leastSignificant DurationUnitOfTime:
									return sb.toString();
								}
							}
						}
					}
				}
			} else {
				seconds = 0.0;
				hours = 0.0;
				days = 0.0;
				months = 0.0;
				years = 0.0;
				if (DurationUnitOfTime.MINUTES.equals(mostSignificant)) {
					minutes = delta;
				} else {
					minutes = (delta >= 60) ? delta % 60 : delta;
					delta = delta / 60;
					if (DurationUnitOfTime.HOURS.equals(mostSignificant)) {
						hours = Math.floor(delta);
					} else {
						hours = (delta >= 24) ? delta % 24 : delta;
						delta = delta / 24;
						if (DurationUnitOfTime.DAYS.equals(mostSignificant)) {
							days = Math.floor(delta);
						} else {
							days = (delta >= 30) ? delta % 30 : delta;
							delta = delta / 30;
							if (DurationUnitOfTime.MONTHS.equals(mostSignificant)) {
								months = Math.floor(delta);
							} else {
								months = (delta >= 12) ? delta % 12 : delta;
								delta = delta / 12;
								if (DurationUnitOfTime.YEARS.equals(mostSignificant)) {
									years = Math.floor(delta);
								} else {
									// mostSignificant DurationUnitOfTime lower than leastSignificant DurationUnitOfTime:
									return sb.toString();
								}
							}
						}
					}
				}
			}
		} else {
			minutes = 0.0;
			hours = 0.0;
			days = 0.0;
			months = 0.0;
			years = 0.0;
			if (DurationUnitOfTime.SECONDS.equals(mostSignificant)) {
				seconds = delta;
			} else {
				seconds = (delta >= 60) ? delta % 60 : delta;
				delta = delta / 60;
				if (DurationUnitOfTime.MINUTES.equals(mostSignificant)) {
					minutes = Math.floor(delta);
				} else {
					minutes = (delta >= 60) ? delta % 60 : delta;
					delta = delta / 60;
					if (DurationUnitOfTime.HOURS.equals(mostSignificant)) {
						hours = Math.floor(delta);
					} else {
						hours = (delta >= 24) ? delta % 24 : delta;
						delta = delta / 24;
						if (DurationUnitOfTime.DAYS.equals(mostSignificant)) {
							days = Math.floor(delta);
						} else {
							days = (delta >= 30) ? delta % 30 : delta;
							delta = delta / 30;
							if (DurationUnitOfTime.MONTHS.equals(mostSignificant)) {
								months = Math.floor(delta);
							} else {
								months = (delta >= 12) ? delta % 12 : delta;
								delta = delta / 12;
								if (DurationUnitOfTime.YEARS.equals(mostSignificant)) {
									years = Math.floor(delta);
								} else {
									// mostSignificant DurationUnitOfTime lower than leastSignificant DurationUnitOfTime:
									return sb.toString();
								}
							}
						}
					}
				}
			}
		}
		if (years > 0) {
			if (months > 0 || days > 0 || hours > 0 || minutes > 0 || seconds > 0) {
				sb.append(durationUnitOfTimeValueToStringBuilder(years, 0, DurationUnitOfTime.YEARS));
			} else {
				sb.append(durationUnitOfTimeValueToStringBuilder(years, leastSignificantDecimals, DurationUnitOfTime.YEARS));
			}
		}
		if (months > 0) {
			if (years > 0) {
				sb.append(", ");
			}
			if (days > 0 || hours > 0 || minutes > 0 || seconds > 0) {
				sb.append(durationUnitOfTimeValueToStringBuilder(months, 0, DurationUnitOfTime.MONTHS));
			} else {
				sb.append(durationUnitOfTimeValueToStringBuilder(months, leastSignificantDecimals, DurationUnitOfTime.MONTHS));
			}
		}
		if (days > 0) {
			if (years > 0 || months > 0) {
				sb.append(", ");
			}
			if (hours > 0 || minutes > 0 || seconds > 0) {
				sb.append(durationUnitOfTimeValueToStringBuilder(days, 0, DurationUnitOfTime.DAYS));
			} else {
				sb.append(durationUnitOfTimeValueToStringBuilder(days, leastSignificantDecimals, DurationUnitOfTime.DAYS));
			}
		}
		if (hours > 0) {
			if (years > 0 || months > 0 || days > 0) {
				sb.append(", ");
			}
			if (minutes > 0 || seconds > 0) {
				sb.append(durationUnitOfTimeValueToStringBuilder(hours, 0, DurationUnitOfTime.HOURS));
			} else {
				sb.append(durationUnitOfTimeValueToStringBuilder(hours, leastSignificantDecimals, DurationUnitOfTime.HOURS));
			}
		}
		if (minutes > 0) {
			if (years > 0 || months > 0 || days > 0 || hours > 0) {
				sb.append(", ");
			}
			if (seconds > 0) {
				sb.append(durationUnitOfTimeValueToStringBuilder(minutes, 0, DurationUnitOfTime.MINUTES));
			} else {
				sb.append(durationUnitOfTimeValueToStringBuilder(minutes, leastSignificantDecimals, DurationUnitOfTime.MINUTES));
			}
		}
		if (seconds > 0) {
			if (years > 0 || months > 0 || days > 0 || hours > 0 || minutes > 0) {
				sb.append(", ");
			}
			sb.append(durationUnitOfTimeValueToStringBuilder(seconds, leastSignificantDecimals, DurationUnitOfTime.SECONDS));
		}
		if (sb.length() == 0) {
			sb.append(durationUnitOfTimeValueToStringBuilder(0.0, leastSignificantDecimals, leastSignificant));
		}
		return sb.toString();
	}

	public static String getExpirationDurationString(Date start, Date stop) {
		return getDurationString(start, stop,
				Settings.getDurationUnitOfTime(SettingCodes.EXPIRATION_MOST_SIGNIFICANT_DURATION_UNIT_OF_TIME, Bundle.SETTINGS,
						DefaultSettings.EXPIRATION_MOST_SIGNIFICANT_DURATION_UNIT_OF_TIME),
				Settings.getDurationUnitOfTime(SettingCodes.EXPIRATION_LEAST_SIGNIFICANT_DURATION_UNIT_OF_TIME, Bundle.SETTINGS,
						DefaultSettings.EXPIRATION_LEAST_SIGNIFICANT_DURATION_UNIT_OF_TIME),
				Settings.getInt(SettingCodes.EXPIRATION_LEAST_SIGNIFICANT_DURATION_UNIT_OF_TIME_DECIMALS, Bundle.SETTINGS,
						DefaultSettings.EXPIRATION_LEAST_SIGNIFICANT_DURATION_UNIT_OF_TIME_DECIMALS));
	}

	public static DateFormat getJqPlotJsDateFormat() {
		DateFormat dateFormat = new SimpleDateFormat(JQPLOT_JS_DATE_PATTERN);
		dateFormat.setTimeZone(WebUtil.getTimeZone());
		return dateFormat;
	}

	public static Date getScheduleMaxDateValue() {
		GregorianCalendar cal = new GregorianCalendar();
		cal.add(Calendar.YEAR, 100);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DATE, 1);
		return cal.getTime();
	}

	public static Date getScheduleMinDateValue() {
		GregorianCalendar cal = new GregorianCalendar(1900, 0, 1);
		return cal.getTime();
	}

	public static String getShiftDurationString(Date start, Date stop) {
		return DateUtil.getDurationString(start, stop,
				Settings.getDurationUnitOfTime(SettingCodes.SHIFT_MOST_SIGNIFICANT_DURATION_UNIT_OF_TIME, Bundle.SETTINGS,
						DefaultSettings.SHIFT_MOST_SIGNIFICANT_DURATION_UNIT_OF_TIME),
				Settings.getDurationUnitOfTime(SettingCodes.SHIFT_LEAST_SIGNIFICANT_DURATION_UNIT_OF_TIME, Bundle.SETTINGS,
						DefaultSettings.SHIFT_LEAST_SIGNIFICANT_DURATION_UNIT_OF_TIME),
				Settings.getInt(SettingCodes.SHIFT_LEAST_SIGNIFICANT_DURATION_UNIT_OF_TIME_DECIMALS, Bundle.SETTINGS,
						DefaultSettings.SHIFT_LEAST_SIGNIFICANT_DURATION_UNIT_OF_TIME_DECIMALS));
	}

	public static String getShiftDurationString(long duration) {
		return DateUtil.getDurationString(duration,
				Settings.getDurationUnitOfTime(SettingCodes.SHIFT_MOST_SIGNIFICANT_DURATION_UNIT_OF_TIME, Bundle.SETTINGS,
						DefaultSettings.SHIFT_MOST_SIGNIFICANT_DURATION_UNIT_OF_TIME),
				Settings.getDurationUnitOfTime(SettingCodes.SHIFT_LEAST_SIGNIFICANT_DURATION_UNIT_OF_TIME, Bundle.SETTINGS,
						DefaultSettings.SHIFT_LEAST_SIGNIFICANT_DURATION_UNIT_OF_TIME),
				Settings.getInt(SettingCodes.SHIFT_LEAST_SIGNIFICANT_DURATION_UNIT_OF_TIME_DECIMALS, Bundle.SETTINGS,
						DefaultSettings.SHIFT_LEAST_SIGNIFICANT_DURATION_UNIT_OF_TIME_DECIMALS));
	}

	public static String getStatusEntryDurationString(long duration) {
		return DateUtil.getDurationString(duration,
				Settings.getDurationUnitOfTime(SettingCodes.STATUS_ENTRY_MOST_SIGNIFICANT_DURATION_UNIT_OF_TIME, Bundle.SETTINGS,
						DefaultSettings.STATUS_ENTRY_MOST_SIGNIFICANT_DURATION_UNIT_OF_TIME),
				Settings.getDurationUnitOfTime(SettingCodes.STATUS_ENTRY_LEAST_SIGNIFICANT_DURATION_UNIT_OF_TIME, Bundle.SETTINGS,
						DefaultSettings.STATUS_ENTRY_LEAST_SIGNIFICANT_DURATION_UNIT_OF_TIME),
				Settings.getInt(SettingCodes.STATUS_ENTRY_LEAST_SIGNIFICANT_DURATION_UNIT_OF_TIME_DECIMALS, Bundle.SETTINGS,
						DefaultSettings.STATUS_ENTRY_LEAST_SIGNIFICANT_DURATION_UNIT_OF_TIME_DECIMALS));
	}

	public static DateFormat getTimeFormat() {
		return getTimeFormat(Settings.getBoolean(SettingCodes.TIME_USER_TIME_ZONE, Bundle.SETTINGS, DefaultSettings.TIME_USER_TIME_ZONE));
	}

	public static DateFormat getTimeFormat(boolean isUserTimeZone) {
		Locale locale = WebUtil.getLocale();
		TimeZone timeZone = WebUtil.getTimeZone();
		DateFormat dateFormat = new SimpleDateFormat(CommonUtil.getInputTimePattern(WebUtil.getDateFormat()), locale);
		if (isUserTimeZone) {
			dateFormat.setTimeZone(timeZone);
		}
		return dateFormat;
	}

	public static Map<Integer, ArrayList<TimeZoneVO>> getTimeZoneByOffsets(Collection<TimeZoneVO> timeZones) {
		TreeMap<Integer, ArrayList<TimeZoneVO>> result = new TreeMap<Integer, ArrayList<TimeZoneVO>>();
		if (timeZones != null) {
			Iterator<TimeZoneVO> it = timeZones.iterator();
			while (it.hasNext()) {
				TimeZoneVO timeZone = it.next();
				int timeZoneOffset = timeZone.getRawOffset();
				ArrayList<TimeZoneVO> offsetTimeZones;
				if (!result.containsKey(timeZoneOffset)) {
					offsetTimeZones = new ArrayList<TimeZoneVO>();
					result.put(timeZoneOffset, offsetTimeZones);
				} else {
					offsetTimeZones = result.get(timeZoneOffset);
				}
				offsetTimeZones.add(timeZone);
			}
		}
		return result;
	}

	public static Date parseJsUTCDate(String date) throws ParseException {
		return CommonUtil.parseDate(date, JS_DATE_UTC_PATTERN, Locale.ENGLISH);
	}

	public static Date sanitizeScheduleDate(boolean add, Date date) {
		return sanitizeScheduleTimestamp(add, date);
	}

	public static Date sanitizeScheduleTimestamp(boolean add, Date date) {
		if (date != null) {
			if (Settings.getBoolean(SettingCodes.ENABLE_PRIMEFACES_SCHEDULE_DST_WORKAROUND, Bundle.SETTINGS, DefaultSettings.ENABLE_PRIMEFACES_SCHEDULE_DST_WORKAROUND)) {
				TimeZone timeZone = WebUtil.getTimeZone();
				if (timeZone != null) {
					if (timeZone.inDaylightTime(new Date())) {
						GregorianCalendar cal = new GregorianCalendar();
						cal.setTime(date);
						cal.add(Calendar.MILLISECOND, (add ? 1 : -1) * timeZone.getDSTSavings());
						return cal.getTime();
					}
				} else {
					return null;
				}
			}
			return new Date(date.getTime());
		}
		return null;
	}

	public static Date sanitizeClientDate(boolean sub, Date date) {
		return sanitizeClientTimestamp(sub, date);
	}

	public static Date sanitizeClientTimestamp(boolean sub, Date date) {
		if (date != null) {
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(date);
			cal.add(Calendar.MILLISECOND, (sub ? -1 : 1) * TimeZone.getDefault().getOffset(date.getTime()));
			return cal.getTime();
		}
		return null;
	}

	public static Date sanitizeTimelineDate(boolean sub, Date date) {
		return sanitizeTimelineTimestamp(sub, date);
	}

	public static Date sanitizeTimelineTimestamp(boolean sub, Date date) {
		return sanitizeClientTimestamp(sub, date);
	}

	private DateUtil() {
	}
}
