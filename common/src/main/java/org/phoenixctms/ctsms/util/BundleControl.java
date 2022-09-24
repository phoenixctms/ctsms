package org.phoenixctms.ctsms.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;

public class BundleControl extends Control {

	private static final String BUNDLE_EXTENSION = "properties";
	private static final String ENCODING = "UTF-8"; // "ISO-8859-1"

	public static PropertyResourceBundle newBundle(java.io.File propertiesFile) throws IOException {
		InputStream stream = new FileInputStream(propertiesFile);
		try {
			return new PropertyResourceBundle(new InputStreamReader(stream, ENCODING));
		} finally {
			stream.close();
		}
	}

	@Override
	public Locale getFallbackLocale(String baseName, Locale locale) {
		if (baseName == null) {
			throw new NullPointerException();
		}
		Locale defaultLocale = new Locale("");
		return locale.equals(defaultLocale) ? null : defaultLocale;
	}

	@Override
	public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
			throws IllegalAccessException, InstantiationException, IOException {
		String bundleName = toBundleName(baseName, locale);
		String resourceName = toResourceName(bundleName, BUNDLE_EXTENSION);
		InputStream stream = null;
		ResourceBundle bundle = null;
		// The below code is copied from default Control#newBundle() implementation.
		// Only the PropertyResourceBundle line is changed to read the file as UTF-8.
		if (reload) {
			URL url = loader.getResource(resourceName);
			if (url != null) {
				URLConnection connection = url.openConnection();
				if (connection != null) {
					connection.setUseCaches(false);
					stream = connection.getInputStream();
				}
			}
		} else {
			stream = loader.getResourceAsStream(resourceName);
		}
		if (stream != null) {
			try {
				bundle = new PropertyResourceBundle(new InputStreamReader(stream, ENCODING));
			} finally {
				stream.close();
			}
		}
		Iterator<String> it = FileOverloads.PROPERTIES_SEARCH_PATHS.iterator();
		while (it.hasNext()) {
			try {
				stream = new FileInputStream(new java.io.File(it.next(), resourceName));
				try {
					bundle = new ChildPropertyResourceBundle(new InputStreamReader(stream, ENCODING), bundle);
				} catch (IOException e) {
				} finally {
					stream.close();
				}
			} catch (FileNotFoundException | SecurityException e) {
			}
		}
		return bundle;
	}
}