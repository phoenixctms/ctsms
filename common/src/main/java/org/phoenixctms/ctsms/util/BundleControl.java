package org.phoenixctms.ctsms.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
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
		// the layers of this locale, lowest first: classpath copy, CTSMS_PROPERTIES overloads, overlay provider
		ArrayList<PropertyResourceBundle> layers = new ArrayList<PropertyResourceBundle>();
		if (stream != null) {
			try {
				layers.add(new PropertyResourceBundle(new InputStreamReader(stream, ENCODING)));
			} finally {
				stream.close();
			}
		}
		Iterator<String> it = FileOverloads.PROPERTIES_SEARCH_PATHS.iterator();
		while (it.hasNext()) {
			try {
				stream = new FileInputStream(new java.io.File(it.next(), resourceName));
				try {
					layers.add(new PropertyResourceBundle(new InputStreamReader(stream, ENCODING)));
				} catch (IOException e) {
				} finally {
					stream.close();
				}
			} catch (FileNotFoundException | SecurityException e) {
			}
		}
		BundleOverlayProvider provider = overlayProvider;
		if (provider != null) {
			String overlay = provider.getOverlay(baseName, locale);
			if (overlay != null) {
				layers.add(new PropertyResourceBundle(new StringReader(overlay)));
			}
		}
		if (layers.isEmpty()) {
			return null;
		} else if (layers.size() == 1) {
			return layers.get(0);
		}
		// merged into one bundle without a parent, so the locale fallback (e.g. "de" -> root) is
		// attached to it by ResourceBundle and keys missing in all layers still fall back:
		return new LayeredResourceBundle(layers);
	}

	private static volatile BundleOverlayProvider overlayProvider = null;

	public static BundleOverlayProvider getOverlayProvider() {
		return overlayProvider;
	}

	/**
	 * Sets the provider of the top layer for all bundles loaded with this control. Bundles already cached
	 * are not affected until the ResourceBundle cache is cleared.
	 */
	public static void setOverlayProvider(BundleOverlayProvider provider) {
		overlayProvider = provider;
	}
}