package org.phoenixctms.ctsms.web.jersey.resource;

import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.phoenixctms.ctsms.enumeration.DBModule;
import org.phoenixctms.ctsms.enumeration.ECRFFieldStatusQueue;

public abstract class StringConverter<T> {

	private final static String QUERY_PARAM_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	private static final String INVALID_BOOLEAN_STRING = "cannot convert '{0}' to boolean value";
	private static final String UNSUPPORTED_RESULT_TYPE = "converting to {0} not supported";
	public final static ArrayList<StringConverter<?>> BOOL_INT_FLOAT = new ArrayList<StringConverter<?>>();
	public final static ArrayList<StringConverter<?>> BOOL_LONG = new ArrayList<StringConverter<?>>();
	static {
		BOOL_INT_FLOAT.add(getBooleanConverter());
		BOOL_INT_FLOAT.add(getIntegerConverter());
		BOOL_INT_FLOAT.add(getFloatConverter());
		BOOL_LONG.add(getBooleanConverter());
		BOOL_LONG.add(getLongConverter());
	}

	public static Object convert(String s, ArrayList<StringConverter<?>> converters) {
		Iterator<StringConverter<?>> it = converters.iterator();
		while (it.hasNext()) {
			try {
				return it.next().convert(s);
			} catch (Exception e) {
			}
		}
		return s;
	}

	private static StringConverter<Boolean> getBooleanConverter() {
		return new StringConverter<Boolean>() {

			@Override
			public Boolean convert(String s) throws Exception {
				if (s == null || (!"true".equals(s.toLowerCase()) && !"false".equals(s.toLowerCase()))) {
					throw new IllegalArgumentException(MessageFormat.format(INVALID_BOOLEAN_STRING, s));
				}
				return Boolean.parseBoolean(s);
			}
		};
	}

	public static StringConverter<?> getConverter(Type type) throws Exception {
		if (Integer.class.equals(type) || Integer.TYPE.equals(type)) {
			return getIntegerConverter();
		} else if (Long.class.equals(type) || Long.TYPE.equals(type)) {
			return getLongConverter();
		} else if (Float.class.equals(type) || Float.TYPE.equals(type)) {
			return getFloatConverter();
		} else if (Double.class.equals(type) || Double.TYPE.equals(type)) {
			return getDoubleConverter();
		} else if (Boolean.class.equals(type) || Boolean.TYPE.equals(type)) {
			return getBooleanConverter();
		} else if (Date.class.equals(type)) {
			return getDateConverter();
		} else if (String.class.equals(type)) {
			return getStringConverter();
		} else if (ECRFFieldStatusQueue.class.equals(type)) {
			return getEcrfFieldStatusQueueConverter();
		} else if (DBModule.class.equals(type)) {
			return getDBModuleConverter();
		}
		throw new IllegalArgumentException(MessageFormat.format(UNSUPPORTED_RESULT_TYPE, type));
	}

	private static StringConverter<Date> getDateConverter() {
		return new StringConverter<Date>() {

			@Override
			public Date convert(String s) throws Exception {
				SimpleDateFormat sdf = new SimpleDateFormat(QUERY_PARAM_DATETIME_PATTERN);
				return sdf.parse(s);
			}
		};
	}

	private static StringConverter<DBModule> getDBModuleConverter() {
		return new StringConverter<DBModule>() {

			@Override
			public DBModule convert(String s) throws Exception {
				return DBModule.valueOf(s);
			}
		};
	}

	private static StringConverter<Double> getDoubleConverter() {
		return new StringConverter<Double>() {

			@Override
			public Double convert(String s) throws Exception {
				return Double.parseDouble(s);
			}
		};
	}

	private static StringConverter<ECRFFieldStatusQueue> getEcrfFieldStatusQueueConverter() {
		return new StringConverter<ECRFFieldStatusQueue>() {

			@Override
			public ECRFFieldStatusQueue convert(String s) throws Exception {
				return ECRFFieldStatusQueue.valueOf(s);
			}
		};
	}

	private static StringConverter<Float> getFloatConverter() {
		return new StringConverter<Float>() {

			@Override
			public Float convert(String s) throws Exception {
				return Float.parseFloat(s);
			}
		};
	}

	private static StringConverter<Integer> getIntegerConverter() {
		return new StringConverter<Integer>() {

			@Override
			public Integer convert(String s) throws Exception {
				return Integer.parseInt(s);
			}
		};
	}

	private static StringConverter<Long> getLongConverter() {
		return new StringConverter<Long>() {

			@Override
			public Long convert(String s) throws Exception {
				return Long.parseLong(s);
			}
		};
	}

	private static StringConverter<String> getStringConverter() {
		return new StringConverter<String>() {

			@Override
			public String convert(String s) throws Exception {
				return s;
			}
		};
	}

	private StringConverter() {
	}

	public abstract T convert(String s) throws Exception;
}