package org.phoenixctms.ctsms.util;

import java.util.Locale;

/**
 * Supplies the text of a .properties layer kept outside of files (e.g. in the database), which
 * {@link BundleControl} puts on top of the classpath copy and the CTSMS_PROPERTIES overloads.
 */
public interface BundleOverlayProvider {

	/**
	 * @param baseName
	 *            the bundle base name, e.g. "ctsms-inputfieldtitles"
	 * @param locale
	 *            the exact locale of the bundle being loaded ({@link Locale#ROOT} for the root bundle)
	 * @return the .properties text for exactly this base name and locale, or null if there is none
	 */
	String getOverlay(String baseName, Locale locale);
}
