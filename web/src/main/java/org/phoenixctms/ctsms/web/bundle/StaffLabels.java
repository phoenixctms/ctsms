package org.phoenixctms.ctsms.web.bundle;

import org.phoenixctms.ctsms.web.util.Settings;

public class StaffLabels extends FacesBundle {

	private static final String BUNDLE_NAME = Settings.WEB_ROOT_PACKAGE_NAME + ".staff.labels";

	@Override
	protected String getBundleName() {
		return BUNDLE_NAME;
	}

	public static String getMessageLocalized(String l10nKey, Object... args) {
		return getMessageLocalized(BUNDLE_NAME, l10nKey, args);
	}

	public static String getStringLocalized(String l10nKey) {
		return getStringLocalized(BUNDLE_NAME, l10nKey);
	}
}
