package org.phoenixctms.ctsms.web.bundle;

import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.web.util.WebUtil;

public abstract class FacesBundle extends ResourceBundle {

	private static final String DEFAULT_FACES_LABEL = "<label>";

	public FacesBundle() {
		setParent(ResourceBundle.getBundle(getBundleName(), FacesContext.getCurrentInstance().getViewRoot().getLocale(), CommonUtil.BUNDLE_CONTROL));
	}

	protected abstract String getBundleName();

	@Override
	public Enumeration<String> getKeys() {
		return parent.getKeys();
	}

	@Override
	protected Object handleGetObject(String key) {
		return parent.getObject(key);
	}

	protected static String getMessageLocalized(String baseName, String l10nKey, Object... args) {
		return CommonUtil.getMessage(l10nKey, args, getResourceBundleLocalized(baseName), DEFAULT_FACES_LABEL);
	}

	protected static String getStringLocalized(String baseName, String l10nKey) {
		return CommonUtil.getString(l10nKey, getResourceBundleLocalized(baseName), DEFAULT_FACES_LABEL);
	}

	private static ResourceBundle getResourceBundleLocalized(String baseName) {
		Locale locale;
		try {
			locale = WebUtil.getLocale();
		} catch (Exception e) {
			locale = Locale.getDefault();
		}
		return CommonUtil.getBundle(baseName, locale);
	}
}
