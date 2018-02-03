package org.phoenixctms.ctsms.util;

import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

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

	public static void setBundleBasename(String basename) { // synchronized
		bundleBasename = basename;
		bundle = null;
		getBundle(basename, true);
	}

	public static void setBundleFilename(String pathname) throws Exception { // synchronized
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