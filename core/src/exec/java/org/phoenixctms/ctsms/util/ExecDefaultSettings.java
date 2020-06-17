package org.phoenixctms.ctsms.util;

import java.util.ArrayList;

import org.phoenixctms.ctsms.enumeration.RangePeriod;
import org.phoenixctms.ctsms.enumeration.VariablePeriod;

public class ExecDefaultSettings {

	public final static String DEFAULT_ENCODING = "UTF-8";
	public final static String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public final static String DATE_PATTERN = "yyyy-MM-dd";
	public final static String TIME_PATTERN = "HH:mm:ss";
	public static final String DBTOOL_LOCK_FILE_NAME = null;
	public static final String DATA_PROVIDER_CLASS = null;
	public static final ArrayList<String> DATA_PROVIDER_SOURCE_FILES = null;
	public static final VariablePeriod EXPORT_INVENTORY_BOOKINGS_OFFSET_PERIOD = VariablePeriod.EXPLICIT;
	public static final long EXPORT_INVENTORY_BOOKINGS_OFFSET_PERIOD_DAYS = 0l;
	public static final RangePeriod EXPORT_INVENTORY_BOOKINGS_RANGE_PERIOD = RangePeriod.DAY;
	public static final VariablePeriod EXPORT_VISIT_SCHEDULE_APPOINTMENTS_OFFSET_PERIOD = VariablePeriod.EXPLICIT;
	public static final long EXPORT_VISIT_SCHEDULE_APPOINTMENTS_OFFSET_PERIOD_DAYS = 0l;
	public static final RangePeriod EXPORT_VISIT_SCHEDULE_APPOINTMENTS_RANGE_PERIOD = RangePeriod.DAY;

	private ExecDefaultSettings() {
	}
}
