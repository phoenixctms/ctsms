package org.phoenixctms.ctsms.util;

import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

public class ResourceBundleConverter {

	public static void copyToSystem(String basename) {
		populateProperties(System.getProperties(), basename, Locale.ROOT);
	}

	public static Properties populateProperties(Properties properties, String basename, Locale locale) {
		ResourceBundle bundle = CommonUtil.getBundle(basename, locale);
		if (bundle != null && properties != null) {
			Enumeration<String> keys = bundle.getKeys();
			while (keys.hasMoreElements()) {
				String key = keys.nextElement();
				try {
					properties.setProperty(key, bundle.getString(key));
				} catch (MissingResourceException|ClassCastException e) {
				}
			}
		}
		return properties;
	}

	private String basename;

	public String getBasename() {
		return basename;
	}

	public Properties getProperties() {
		return populateProperties(new Properties(), basename, Locale.ROOT);
	}

	public void setBasename(String basename) {
		this.basename = basename;
	}
}