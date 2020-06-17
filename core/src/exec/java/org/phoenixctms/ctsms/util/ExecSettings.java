package org.phoenixctms.ctsms.util;

import java.util.ArrayList;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.phoenixctms.ctsms.enumeration.RangePeriod;
import org.phoenixctms.ctsms.enumeration.VariablePeriod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class ExecSettings {

	private static String bundleBasename = null;
	private static PropertyResourceBundle bundle = null;

	public static boolean getBoolean(String key, boolean defaultValue) {
		return CommonUtil.getValue(key, getBundle(false), defaultValue);
	}

	private static ResourceBundle getBundle(boolean raiseException) {
		if (bundle != null) {
			return bundle;
		} else {
			return getBundle(bundleBasename, raiseException);
		}
	}

	private static ResourceBundle getBundle(String baseName, boolean raiseException) {
		if (raiseException) {
			return CommonUtil.getBundle(baseName, Locale.ROOT);
		} else {
			try {
				return CommonUtil.getBundle(baseName, Locale.ROOT);
			} catch (Throwable t) {
			}
			return null;
		}
	}

	public static float getFloat(String key, float defaultValue) {
		return CommonUtil.getValue(key, getBundle(false), defaultValue);
	}

	public static int getInt(String key, int defaultValue) {
		return CommonUtil.getValue(key, getBundle(false), defaultValue);
	}

	public static Integer getIntNullable(String key, Integer defaultValue) {
		return CommonUtil.getValueNullable(key, getBundle(false), defaultValue);
	}

	public static long getLong(String key, long defaultValue) {
		return CommonUtil.getValue(key, getBundle(false), defaultValue);
	}

	public static Long getLongNullable(String key, Long defaultValue) {
		return CommonUtil.getValueNullable(key, getBundle(false), defaultValue);
	}

	public static String getString(String key, String defaultValue) {
		return CommonUtil.getValue(key, getBundle(false), defaultValue);
	}

	public static ArrayList<String> getStringList(String key, ArrayList<String> defaultValue) {
		return CommonUtil.getValueStringList(key, getBundle(false), defaultValue);
	}

	public static VariablePeriod getVariablePeriod(String key, VariablePeriod defaultValue) {
		String value = CommonUtil.getValue(key, getBundle(false), defaultValue == null ? null : defaultValue.name());
		if (value != null && value.length() > 0) {
			return VariablePeriod.fromString(value); // illegal arg exc!
		} else {
			return null;
		}
	}

	public static RangePeriod getRangePeriod(String key, RangePeriod defaultValue) {
		String value = CommonUtil.getValue(key, getBundle(false), defaultValue == null ? null : defaultValue.name());
		if (value != null && value.length() > 0) {
			return RangePeriod.fromString(value); // illegal arg exc!
		} else {
			return null;
		}
	}

	public static void setBundleBasename(String basename) {
		bundleBasename = basename;
		bundle = null;
		getBundle(basename, true);
	}

	public static void setBundleFilename(String pathname) throws Exception {
		bundle = null;
		bundle = BundleControl.newBundle(new java.io.File(pathname));
		bundleBasename = null;
	}

	private ExecSettings() {
	}

	@Autowired(required = true)
	public void setDefaultBundleBasename(
			String defaultBundleBasename) {
		setBundleBasename(defaultBundleBasename);
	}
}