package org.phoenixctms.ctsms.web.bundle;

import org.phoenixctms.ctsms.web.util.Settings;

public class CourseLabels extends FacesBundle {

	private static final String BUNDLE_NAME = Settings.WEB_ROOT_PACKAGE_NAME + ".course.labels";

	@Override
	protected String getBundleName() {
		return BUNDLE_NAME;
	}
}
