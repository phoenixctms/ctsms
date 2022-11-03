package org.phoenixctms.ctsms.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.phoenixctms.ctsms.util.CommonUtil;

public class Random {

	private int year;
	private java.util.Random random;

	public Random() {
		random = new java.util.Random();
		year = Calendar.getInstance().get(Calendar.YEAR);
	}

	public int nextInt(int bound) {
		return random.nextInt(bound);
	}

	public long nextLong(int bound) {
		return new Long(random.nextInt(bound));
	}

	public <E> E getRandomElement(ArrayList<E> list) {
		if (list != null && list.size() > 0) {
			return list.get(random.nextInt(list.size()));
		}
		return null;
	}

	public <E> E getRandomElement(Collection<E> collection) {
		E result = null;
		if (collection != null && collection.size() > 0) {
			int index = random.nextInt(collection.size());
			Iterator<E> it = collection.iterator();
			for (int i = 0; i <= index; i++) {
				result = it.next();
			}
		}
		return result;
	}

	public <E> E getRandomElement(E[] array) {
		if (array != null && array.length > 0) {
			return array[random.nextInt(array.length)];
		}
		return null;
	}

	public float getRandomFloat(Float lowerLimit, Float upperLimit) {
		float lower = (lowerLimit == null ? 0f : lowerLimit.floatValue());
		return lower + random.nextFloat() * ((upperLimit == null ? Float.MAX_VALUE : upperLimit.longValue()) - lower);
	}

	public long getRandomLong(Long lowerLimit, Long upperLimit) {
		long lower = (lowerLimit == null ? 0l : lowerLimit.longValue());
		return lower + nextLong(random, (upperLimit == null ? Integer.MAX_VALUE : upperLimit.longValue()) - lower);
	}

	public Date getRandomTime(Date minTime, Date maxTime) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(minTime == null ? (new GregorianCalendar(1970, 0, 1, 0, 0, 0)).getTime() : minTime);
		cal.setTimeInMillis(cal.getTimeInMillis()
				+ nextLong(random, (maxTime == null ? (new GregorianCalendar(1970, 0, 1, 23, 59, 59)).getTime() : maxTime).getTime() - cal.getTimeInMillis()));
		return cal.getTime();
	}

	public Date getRandomTimestamp(Date minTimestamp, Date maxTimestamp) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(minTimestamp == null ? (new GregorianCalendar(1900, 0, 1, 0, 0, 0)).getTime() : minTimestamp);
		cal.setTimeInMillis(cal.getTimeInMillis()
				+ nextLong(random, (maxTimestamp == null ? (new GregorianCalendar(year, 11, 31)).getTime() : maxTimestamp).getTime() - cal.getTimeInMillis()));
		return cal.getTime();
	}

	public <E> ArrayList<E> getUniqueRandomElements(ArrayList<E> list, int n) {
		if (list != null) {
			int listSize = list.size();
			if (listSize > 0 && n > 0) {
				if (listSize <= n) {
					return new ArrayList<E>(list);
				} else {
					HashSet<E> result = new HashSet<E>(n);
					while (result.size() < n && result.size() < listSize) {
						result.add(list.get(random.nextInt(listSize)));
					}
					return new ArrayList<E>(result);
				}
			} else {
				return new ArrayList<E>();
			}
		}
		return null;
	}

	private static long dateDeltaDays(Date d1, Date d2) {
		long diff = d2.getTime() - d1.getTime();
		return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
	}

	public Date getRandomDate(Date minDate, Date maxDate) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(minDate == null ? (new GregorianCalendar(1900, 0, 1)).getTime() : minDate);
		cal = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		cal.add(Calendar.DAY_OF_YEAR, random.nextInt(CommonUtil.safeLongToInt(dateDeltaDays(minDate == null ? (new GregorianCalendar(1900, 0, 1)).getTime() : minDate,
				maxDate == null ? (new GregorianCalendar(year, 11, 31)).getTime() : maxDate))));
		return cal.getTime();
	}

	public Date getRandomDateAround(Date date, int maxDaysBefore, int maxDaysAfter) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, (getRandomBoolean() ? random.nextInt(maxDaysAfter + 1) : (-1 * random.nextInt(maxDaysBefore + 1))));
		return cal.getTime();
		//return DateCalc.addInterval(date, VariablePeriod.EXPLICIT, (long) (getRandomBoolean() ? random.nextInt(maxDaysAfter + 1) : (-1 * random.nextInt(maxDaysBefore + 1))));
	}

	public Date getRandomDateOfBirth() {
		return getRandomDate((new GregorianCalendar(year - 90, 0, 1)).getTime(), (new GregorianCalendar(year - 20, 0, 1)).getTime());
	}

	public boolean getRandomBoolean() {
		return random.nextBoolean();
	}

	public boolean getRandomBoolean(int p) {
		if (p <= 0) {
			return false;
		} else if (p >= 100) {
			return true;
		} else {
			return random.nextDouble() < ((p) / 100.0d);
		}
	}

	public <E> void shuffle(List<E> items) {
		Collections.shuffle(items, random);
	}

	public int getYear() {
		return year;
	}

	private long nextLong(java.util.Random rng, long n) {
		// error checking and 2^x checking removed for simplicity.
		if (n <= 0) {
			throw new IllegalArgumentException("n must be positive");
		}
		// if ((n & -n) == n) // i.e., n is a power of 2
		// return (int)((n * (long)next(31)) >> 31);
		long bits, val;
		do {
			bits = (rng.nextLong() << 1) >>> 1;
			val = bits % n;
		} while (bits - val + (n - 1) < 0L);
		return val;
	}
}
