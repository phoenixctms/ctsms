package org.phoenixctms.ctsms.util.date;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import org.phoenixctms.ctsms.compare.DateIntervalComparator;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.DefaultMessages;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.MessageCodes;

public class DateInterval {

	private final static DateIntervalComparator COMPARATOR = new DateIntervalComparator(false);

	public static long getIntervalOverlapSecs(Date intervalAStart, Date intervalAStop, Date intervalBStart, Date intervalBStop) throws Exception {
		if (intervalAStart != null && intervalAStop != null) { // closed interval
			long durationA = CommonUtil.dateDeltaSecs(intervalAStart, intervalAStop);
			if (durationA >= 0) {
				if (intervalBStart != null && intervalBStop != null) { // closed duration
					long durationB = CommonUtil.dateDeltaSecs(intervalBStart, intervalBStop);
					if (durationB >= 0) {
						if (intervalBStart.compareTo(intervalAStart) <= 0) {
							if (intervalBStop.compareTo(intervalAStop) >= 0) { // duration contains interval entirely
								return durationA;
							} else if (intervalBStop.after(intervalAStart)) { // duration overlaps interval at intervalstart
								return CommonUtil.dateDeltaSecs(intervalAStart, intervalBStop);
							} else { // duration doesn't overlap and is entirely before interval
								return 0;
							}
						} else if (intervalBStart.before(intervalAStop)) {
							if (intervalBStop.compareTo(intervalAStop) >= 0) { // duration overlaps interval at intervalend
								return CommonUtil.dateDeltaSecs(intervalBStart, intervalAStop);
							} else if (intervalBStop.after(intervalAStart)) { // duration is contained within interval entirely
								return durationB;
							} else { // not possible
								throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.INVALID_DATE_INTERVAL, DefaultMessages.INVALID_DATE_INTERVAL));
							}
						} else { // duration doesn't overlap and is entirely after interval
							return 0;
						}
					} else {
						throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.INVALID_DATE_INTERVAL, DefaultMessages.INVALID_DATE_INTERVAL));
					}
				} else if (intervalBStart == null && intervalBStop != null) {
					if (intervalBStop.compareTo(intervalAStop) >= 0) { // duration contains interval entirely
						return durationA;
					} else if (intervalBStop.after(intervalAStart)) { // duration overlaps interval at intervalstart
						return CommonUtil.dateDeltaSecs(intervalAStart, intervalBStop);
					} else { // duration doesn't overlap and is entirely before interval
						return 0;
					}
				} else if (intervalBStart != null && intervalBStop == null) {
					if (intervalBStart.compareTo(intervalAStart) <= 0) { // duration contains interval entirely
						return durationA;
					} else if (intervalBStart.before(intervalAStop)) { // duration overlaps interval at intervalend
						return CommonUtil.dateDeltaSecs(intervalBStart, intervalAStop);
					} else { // duration doesn't overlap and is entirely after interval
						return 0;
					}
				} else {
					return durationA;
				}
			} else {
				throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.INVALID_DATE_INTERVAL, DefaultMessages.INVALID_DATE_INTERVAL));
			}
		} else if (intervalAStart == null && intervalAStop != null) {
			if (intervalBStart != null && intervalBStop != null) {
				long durationB = CommonUtil.dateDeltaSecs(intervalBStart, intervalBStop);
				if (durationB >= 0) {
					if (intervalBStart.before(intervalAStop)) {
						if (intervalBStop.compareTo(intervalAStop) >= 0) {
							return CommonUtil.dateDeltaSecs(intervalBStart, intervalAStop);
						} else {
							return durationB;
						}
					} else {
						return 0;
					}
				} else {
					throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.INVALID_DATE_INTERVAL, DefaultMessages.INVALID_DATE_INTERVAL));
				}
			} else if (intervalBStart == null && intervalBStop != null) {
				return Long.MAX_VALUE;
			} else if (intervalBStart != null && intervalBStop == null) {
				if (intervalBStart.before(intervalAStop)) {
					return CommonUtil.dateDeltaSecs(intervalBStart, intervalAStop);
				} else {
					return 0;
				}
			} else {
				return Long.MAX_VALUE;
			}
		} else if (intervalAStart != null && intervalAStop == null) {
			if (intervalBStart != null && intervalBStop != null) {
				long durationB = CommonUtil.dateDeltaSecs(intervalBStart, intervalBStop);
				if (durationB >= 0) {
					if (intervalBStart.compareTo(intervalAStart) <= 0) {
						if (intervalBStop.after(intervalAStart)) {
							return CommonUtil.dateDeltaSecs(intervalAStart, intervalBStop);
						} else {
							return 0;
						}
					} else {
						return durationB;
					}
				} else {
					throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.INVALID_DATE_INTERVAL, DefaultMessages.INVALID_DATE_INTERVAL));
				}
			} else if (intervalBStart == null && intervalBStop != null) {
				if (intervalBStop.after(intervalAStart)) {
					return CommonUtil.dateDeltaSecs(intervalAStart, intervalBStop);
				} else {
					return 0;
				}
			} else if (intervalBStart != null && intervalBStop == null) {
				return Long.MAX_VALUE;
			} else {
				return Long.MAX_VALUE;
			}
		} else {
			return Long.MAX_VALUE;
		}
	}

	public static long getIntervalOverlapSecs(DateInterval intervalA, DateInterval intervalB) throws Exception {
		return getIntervalOverlapSecs(intervalA.getStart(), intervalA.getStop(), intervalB.getStart(), intervalB.getStop());
	}

	public static ArrayList<DateInterval> mergeIntervals(ArrayList<DateInterval> intervals) throws Exception {
		ArrayList<DateInterval> result = new ArrayList<DateInterval>();
		if (intervals != null && intervals.size() > 0) {
			if (intervals.size() > 1) {
				ArrayList<DateInterval> sortedIntervals = new ArrayList<DateInterval>(intervals);
				Collections.sort(sortedIntervals, COMPARATOR);
				result.add(sortedIntervals.get(0));
				for (int i = 1; i < sortedIntervals.size(); i++) {
					result.addAll(mergeIntervals(result.remove(result.size() - 1), sortedIntervals.get(i)));
				}
			} else {
				result.addAll(intervals);
			}
		}
		return result;
	}

	public static ArrayList<DateInterval> mergeIntervals(DateInterval intervalA, DateInterval intervalB) throws Exception {
		Date intervalAStart = intervalA.getStart();
		Date intervalAStop = intervalA.getStop();
		Date intervalBStart = intervalB.getStart();
		Date intervalBStop = intervalB.getStop();
		ArrayList<DateInterval> result = new ArrayList<DateInterval>(2);
		if (intervalAStart != null && intervalAStop != null) { // closed interval
			long durationA = CommonUtil.dateDeltaSecs(intervalAStart, intervalAStop);
			if (durationA >= 0) {
				if (intervalBStart != null && intervalBStop != null) { // closed duration
					long durationB = CommonUtil.dateDeltaSecs(intervalBStart, intervalBStop);
					if (durationB >= 0) {
						if (intervalBStart.compareTo(intervalAStart) <= 0) {
							if (intervalBStop.compareTo(intervalAStop) >= 0) { // duration contains interval entirely
								// return durationA;
								result.add(intervalB);
							} else if (intervalBStop.after(intervalAStart)) { // duration overlaps interval at intervalstart
								// return CommonUtil.dateDeltaSecs(intervalAStart, intervalBStop);
								result.add(new DateInterval(intervalB.start, intervalA.stop));
							} else { // duration doesn't overlap and is entirely before interval
								// return 0;
								result.add(intervalB);
								result.add(intervalA);
							}
						} else if (intervalBStart.before(intervalAStop)) {
							if (intervalBStop.compareTo(intervalAStop) >= 0) { // duration overlaps interval at intervalend
								// return CommonUtil.dateDeltaSecs(intervalBStart, intervalAStop);
								result.add(new DateInterval(intervalA.start, intervalB.stop));
							} else if (intervalBStop.after(intervalAStart)) { // duration is contained within interval entirely
								// return durationB;
								result.add(intervalA);
							} else { // not possible
								throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.INVALID_DATE_INTERVAL, DefaultMessages.INVALID_DATE_INTERVAL));
							}
						} else { // duration doesn't overlap and is entirely after interval
							// return 0;
							result.add(intervalA);
							result.add(intervalB);
						}
					} else {
						throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.INVALID_DATE_INTERVAL, DefaultMessages.INVALID_DATE_INTERVAL));
					}
				} else if (intervalBStart == null && intervalBStop != null) {
					if (intervalBStop.compareTo(intervalAStop) >= 0) { // duration contains interval entirely
						// return durationA;
						result.add(intervalB);
					} else if (intervalBStop.after(intervalAStart)) { // duration overlaps interval at intervalstart
						// return CommonUtil.dateDeltaSecs(intervalAStart, intervalBStop);
						result.add(new DateInterval(null, intervalA.stop));
					} else { // duration doesn't overlap and is entirely before interval
						// return 0;
						result.add(intervalB);
						result.add(intervalA);
					}
				} else if (intervalBStart != null && intervalBStop == null) {
					if (intervalBStart.compareTo(intervalAStart) <= 0) { // duration contains interval entirely
						// return durationA;
						result.add(intervalB);
					} else if (intervalBStart.before(intervalAStop)) { // duration overlaps interval at intervalend
						// return CommonUtil.dateDeltaSecs(intervalBStart, intervalAStop);
						result.add(new DateInterval(intervalA.start, null));
					} else { // duration doesn't overlap and is entirely after interval
						// return 0;
						result.add(intervalA);
						result.add(intervalB);
					}
				} else {
					// return durationA;
					result.add(intervalB);
				}
			} else {
				throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.INVALID_DATE_INTERVAL, DefaultMessages.INVALID_DATE_INTERVAL));
			}
		} else if (intervalAStart == null && intervalAStop != null) {
			if (intervalBStart != null && intervalBStop != null) {
				long durationB = CommonUtil.dateDeltaSecs(intervalBStart, intervalBStop);
				if (durationB >= 0) {
					if (intervalBStart.before(intervalAStop)) {
						if (intervalBStop.compareTo(intervalAStop) >= 0) {
							// return CommonUtil.dateDeltaSecs(intervalBStart, intervalAStop);
							result.add(new DateInterval(null, intervalB.stop));
						} else {
							// return durationB;
							result.add(intervalA);
						}
					} else {
						// return 0;
						result.add(intervalA);
						result.add(intervalB);
					}
				} else {
					throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.INVALID_DATE_INTERVAL, DefaultMessages.INVALID_DATE_INTERVAL));
				}
			} else if (intervalBStart == null && intervalBStop != null) {
				// return Long.MAX_VALUE;
				if (intervalBStop.before(intervalAStop)) {
					result.add(intervalA);
				} else {
					result.add(intervalB);
				}
			} else if (intervalBStart != null && intervalBStop == null) {
				if (intervalBStart.before(intervalAStop)) {
					// return CommonUtil.dateDeltaSecs(intervalBStart, intervalAStop);
					result.add(new DateInterval(null, null));
				} else {
					// return 0;
					result.add(intervalA);
					result.add(intervalB);
				}
			} else {
				// return Long.MAX_VALUE;
				result.add(intervalB);
			}
		} else if (intervalAStart != null && intervalAStop == null) {
			if (intervalBStart != null && intervalBStop != null) {
				long durationB = CommonUtil.dateDeltaSecs(intervalBStart, intervalBStop);
				if (durationB >= 0) {
					if (intervalBStart.compareTo(intervalAStart) <= 0) {
						if (intervalBStop.after(intervalAStart)) {
							// return CommonUtil.dateDeltaSecs(intervalAStart, intervalBStop);
							result.add(new DateInterval(intervalB.start, null));
						} else {
							// return 0;
							result.add(intervalB);
							result.add(intervalA);
						}
					} else {
						// return durationB;
						result.add(intervalA);
					}
				} else {
					throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.INVALID_DATE_INTERVAL, DefaultMessages.INVALID_DATE_INTERVAL));
				}
			} else if (intervalBStart == null && intervalBStop != null) {
				if (intervalBStop.after(intervalAStart)) {
					// return CommonUtil.dateDeltaSecs(intervalAStart, intervalBStop);
					result.add(new DateInterval(null, null));
				} else {
					// return 0;
					result.add(intervalB);
					result.add(intervalA);
				}
			} else if (intervalBStart != null && intervalBStop == null) {
				// return Long.MAX_VALUE;
				if (intervalBStart.before(intervalAStart)) {
					result.add(intervalB);
				} else {
					result.add(intervalA);
				}
			} else {
				// return Long.MAX_VALUE;
				result.add(intervalB);
			}
		} else {
			// return Long.MAX_VALUE;
			result.add(intervalA);
		}
		return result;
	}
	private Date start;
	private Date stop;

	public DateInterval() {
	}

	public DateInterval(Date dateTime, boolean ascending) {
		GregorianCalendar start;
		GregorianCalendar stop;
		if (ascending) {
			start = new GregorianCalendar();
			start.setTime(dateTime);
			stop = new GregorianCalendar(start.get(GregorianCalendar.YEAR), start.get(GregorianCalendar.MONTH), start.get(GregorianCalendar.DAY_OF_MONTH), 0, 0, 0);
			stop.add(GregorianCalendar.DAY_OF_MONTH, 1);
		} else {
			stop = new GregorianCalendar();
			stop.setTime(dateTime);
			start = new GregorianCalendar(stop.get(GregorianCalendar.YEAR), stop.get(GregorianCalendar.MONTH), stop.get(GregorianCalendar.DAY_OF_MONTH), 0, 0, 0);
		}
		this.start = start.getTime();
		this.stop = stop.getTime();
	}

	public DateInterval(Date start, Date stop) {
		this.start = start;
		this.stop = stop;
	}

	public DateInterval(GregorianCalendar date) {
		GregorianCalendar start = new GregorianCalendar(date.get(GregorianCalendar.YEAR), date.get(GregorianCalendar.MONTH), date.get(GregorianCalendar.DAY_OF_MONTH), 0, 0, 0);
		GregorianCalendar stop = new GregorianCalendar(date.get(GregorianCalendar.YEAR), date.get(GregorianCalendar.MONTH), date.get(GregorianCalendar.DAY_OF_MONTH), 0, 0, 0);
		stop.add(GregorianCalendar.DAY_OF_MONTH, 1);
		this.start = start.getTime();
		this.stop = stop.getTime();
	}

	public DateInterval(GregorianCalendar date, int hour, int minute, boolean ascending) {
		GregorianCalendar start;
		GregorianCalendar stop;
		if (ascending) {
			start = new GregorianCalendar(date.get(GregorianCalendar.YEAR), date.get(GregorianCalendar.MONTH), date.get(GregorianCalendar.DAY_OF_MONTH), hour, minute, 0);
			stop = new GregorianCalendar(date.get(GregorianCalendar.YEAR), date.get(GregorianCalendar.MONTH), date.get(GregorianCalendar.DAY_OF_MONTH), 0, 0, 0);
			stop.add(GregorianCalendar.DAY_OF_MONTH, 1);
		} else {
			start = new GregorianCalendar(date.get(GregorianCalendar.YEAR), date.get(GregorianCalendar.MONTH), date.get(GregorianCalendar.DAY_OF_MONTH), 0, 0, 0);
			stop = new GregorianCalendar(date.get(GregorianCalendar.YEAR), date.get(GregorianCalendar.MONTH), date.get(GregorianCalendar.DAY_OF_MONTH), hour, minute, 0);
		}
		this.start = start.getTime();
		this.stop = stop.getTime();
	}

	public DateInterval(GregorianCalendar date, int hourStart, int minuteStart, int hourEnd, int minuteEnd) {
		GregorianCalendar start = new GregorianCalendar(date.get(GregorianCalendar.YEAR), date.get(GregorianCalendar.MONTH), date.get(GregorianCalendar.DAY_OF_MONTH), hourStart,
				minuteStart, 0);
		GregorianCalendar stop = new GregorianCalendar(date.get(GregorianCalendar.YEAR), date.get(GregorianCalendar.MONTH), date.get(GregorianCalendar.DAY_OF_MONTH), hourEnd,
				minuteEnd, 0);
		this.start = start.getTime();
		this.stop = stop.getTime();
	}

	public boolean contains(Date now) {
		if (start != null && stop != null) {
			return (start.compareTo(now) <= 0 && stop.compareTo(now) > 0);
		} else if (start != null && stop == null) {
			return (start.compareTo(now) <= 0);
		} else if (start == null && stop != null) {
			return (stop.compareTo(now) > 0);
		} else {
			return true;
		}
	}

	public long getDuration() {
		if (start != null && stop != null) {
			return CommonUtil.dateDeltaSecs(start, stop);
		} else {
			return Long.MAX_VALUE;
		}
	}

	public ArrayList<Date> getEnumeratedDates() {
		ArrayList<Date> result = new ArrayList<Date>();
		if (start != null && stop != null && start.compareTo(stop) <= 0) {
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(start);
			cal = new GregorianCalendar(cal.get(GregorianCalendar.YEAR), cal.get(GregorianCalendar.MONTH), cal.get(GregorianCalendar.DAY_OF_MONTH), 0, 0, 0);
			while (cal.getTime().compareTo(stop) <= 0) {
				result.add(cal.getTime());
				cal.add(GregorianCalendar.DAY_OF_MONTH, 1);
			}
		}
		return result;
	}

	public long getIntervalOverlapSecs(ArrayList<DateInterval> intervals) throws Exception {
		long secs = 0;
		if (intervals != null && intervals.size() > 0) {
			Iterator<DateInterval> it = intervals.iterator();
			while (it.hasNext()) {
				secs += it.next().getIntervalOverlapSecs(this);
			}
		}
		return secs;
	}

	public long getIntervalOverlapSecs(Date intervalStart, Date intervalStop) throws Exception {
		return getIntervalOverlapSecs(this.getStart(), this.getStop(), intervalStart, intervalStop);
	}

	public long getIntervalOverlapSecs(DateInterval interval) throws Exception {
		return getIntervalOverlapSecs(this.getStart(), this.getStop(), interval.getStart(), interval.getStop());
	}

	public Date getStart() {
		return start;
	}

	public Date getStop() {
		return stop;
	}

	public boolean isInfinite() {
		return start == null || stop == null;
	}

	public boolean isOver(Date now) {
		if (start != null && stop != null) {
			return (start.compareTo(now) <= 0 && stop.compareTo(now) <= 0);
		} else if (start != null && stop == null) {
			return false;
		} else if (start == null && stop != null) {
			return (stop.compareTo(now) <= 0);
		} else {
			return false;
		}
	}

	public ArrayList<DateInterval> merge(DateInterval interval) throws Exception {
		if (interval != null) {
			return mergeIntervals(this, interval);
		} else {
			ArrayList<DateInterval> result = new ArrayList<DateInterval>();
			result.add(this);
			return result;
		}
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public void setStop(Date stop) {
		this.stop = stop;
	}
}
