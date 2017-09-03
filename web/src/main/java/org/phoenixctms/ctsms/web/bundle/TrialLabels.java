package org.phoenixctms.ctsms.web.bundle;

import org.phoenixctms.ctsms.web.util.Settings;

public class TrialLabels extends FacesBundle {

	private static final String BUNDLE_NAME = Settings.WEB_ROOT_PACKAGE_NAME + ".trial.labels";

	@Override
	protected String getBundleName() {
		return BUNDLE_NAME;
	}
}
