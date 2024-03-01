package org.phoenixctms.ctsms.web.bundle;

public class Messages extends FacesBundle {

	private static final String BUNDLE_NAME = org.phoenixctms.ctsms.web.util.Messages.MESSAGE_BUNDLE_DEFAULT;

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
