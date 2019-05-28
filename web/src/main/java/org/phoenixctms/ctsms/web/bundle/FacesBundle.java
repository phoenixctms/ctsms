package org.phoenixctms.ctsms.web.bundle;

import java.util.Enumeration;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

import org.phoenixctms.ctsms.util.CommonUtil;

public abstract class FacesBundle extends ResourceBundle {

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
}
