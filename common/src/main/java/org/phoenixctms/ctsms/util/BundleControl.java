package org.phoenixctms.ctsms.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;
import java.util.regex.Pattern;

public class BundleControl extends Control {

	private static final String BUNDLE_EXTENSION = "properties";
	private static final String ENCODING = "UTF-8"; // "ISO-8859-1"
	private final static Pattern PATH_ENVIRONMENT_VARIABLE_REGEXP = Pattern.compile("\\s*[,;]\\s*");
	private final static String PROPERTIES_PATH_ENVIRONMENT_PROPERTIES_VARIABLE = "CTSMS_PROPERTIES";
	public final static LinkedHashSet<String> PROPERTIES_SEARCH_PATHS = getPropertiesSearchPaths();

	private static LinkedHashSet<String> getPropertiesSearchPaths() {
		String[] searchPaths = null;
		String envVar = null;
		LinkedHashSet<String> result = new LinkedHashSet<String>();
		try {
			envVar = System.getProperty(PROPERTIES_PATH_ENVIRONMENT_PROPERTIES_VARIABLE);
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		if (!(envVar != null && envVar.length() > 0)) {
			try {
				envVar = System.getenv(PROPERTIES_PATH_ENVIRONMENT_PROPERTIES_VARIABLE);
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
		if (envVar != null && envVar.length() > 0) {
			System.out.println(PROPERTIES_PATH_ENVIRONMENT_PROPERTIES_VARIABLE + ": " + envVar);
			searchPaths = PATH_ENVIRONMENT_VARIABLE_REGEXP.split(envVar);
		}
		if (searchPaths != null && searchPaths.length > 0) {
			for (int i = 0; i < searchPaths.length; i++) {
				String searchPath = searchPaths[i];
				if (searchPath != null && searchPath.length() > 0) {
					File file = new File(searchPath);
					if (file.exists()) {
						if (file.isDirectory()) {
							if (file.isAbsolute()) {
								if (file.canRead()) {
									try {
										result.add(file.getCanonicalPath());
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							}
						}
					}
				}
			}
		}
		return result;
	}

	public static PropertyResourceBundle newBundle(java.io.File propertiesFile) throws IOException {
		InputStream stream = new FileInputStream(propertiesFile);
		try {
			// stream = new FileInputStream(propertiesFile);
			return new PropertyResourceBundle(new InputStreamReader(stream, ENCODING));
			// } catch (IOException e) {
			// // e.printStackTrace();
		} finally {
			// try {
			stream.close();
			// } catch (IOException e) {
			// }
			// if (stream != null) {
			// stream.close();
			// }
		}
	}

	@Override
	public Locale getFallbackLocale(String baseName, Locale locale) {
		// return new Locale("");
		// return null;
		if (baseName == null) {
			throw new NullPointerException();
		}
		Locale defaultLocale = new Locale("");
		return locale.equals(defaultLocale) ? null : defaultLocale;
	}

	@Override
	public ResourceBundle newBundle
	(String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
			throws IllegalAccessException, InstantiationException, IOException
			{
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
		Iterator<String> it = PROPERTIES_SEARCH_PATHS.iterator();
		while (it.hasNext()) {
			try {
				stream = new FileInputStream(new java.io.File(it.next(), resourceName));
				try {
					bundle = new ChildPropertyResourceBundle(new InputStreamReader(stream, ENCODING), bundle);
				} catch (IOException e) {
					// e.printStackTrace();
				} finally {
					stream.close();
				}
			} catch (FileNotFoundException e) {
				// e.printStackTrace();
			} catch (SecurityException e) {
				// e.printStackTrace();
			}
		}
		return bundle;
			}
}