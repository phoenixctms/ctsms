package org.phoenixctms.ctsms.web.bundle;

import org.phoenixctms.ctsms.web.util.Settings;

public class InventoryLabels extends FacesBundle {

	private static final String BUNDLE_NAME = Settings.WEB_ROOT_PACKAGE_NAME + ".inventory.labels";

	@Override
	protected String getBundleName() {
		return BUNDLE_NAME;
	}
}
