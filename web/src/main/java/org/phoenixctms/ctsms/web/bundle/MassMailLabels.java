package org.phoenixctms.ctsms.web.bundle;

import org.phoenixctms.ctsms.web.util.Settings;

public class MassMailLabels extends FacesBundle {

	private static final String BUNDLE_NAME = Settings.WEB_ROOT_PACKAGE_NAME + ".massmail.labels";

	@Override
	protected String getBundleName() {
		return BUNDLE_NAME;
	}
}
