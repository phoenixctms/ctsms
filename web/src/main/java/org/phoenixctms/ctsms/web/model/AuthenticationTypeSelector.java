package org.phoenixctms.ctsms.web.model;

import java.util.ArrayList;

import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.AuthenticationType;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class AuthenticationTypeSelector {

	private AuthenticationTypeSelectorListener bean;
	private int property;
	private ArrayList<SelectItem> authenticationTypes;
	private final static String NO_SELECTION_STRING = CommonUtil.NO_SELECTION_VALUE;

	public AuthenticationTypeSelector(AuthenticationTypeSelectorListener bean, int property) {
		this.bean = bean;
		this.property = property;
		authenticationTypes = WebUtil.getAuthenticationTypes();
	}

	public String getAuthenticationType() {
		if (bean != null) {
			AuthenticationType method = bean.getAuthenticationType(property);
			if (method != null) {
				return method.name();
			} else {
				return NO_SELECTION_STRING;
			}
		}
		return NO_SELECTION_STRING;
	}

	public ArrayList<SelectItem> getAuthenticationTypes() {
		return authenticationTypes;
	}

	public AuthenticationTypeSelectorListener getBean() {
		return bean;
	}

	public void setAuthenticationType(String name) {
		if (bean != null) {
			AuthenticationType method;
			if (name != null && !name.equals(NO_SELECTION_STRING)) {
				try {
					method = AuthenticationType.fromString(name);
				} catch (Exception e) {
					method = AuthenticationTypeSelectorListener.NO_SELECTION_AUTHENTICATION_TYPE;
				}
			} else {
				method = AuthenticationTypeSelectorListener.NO_SELECTION_AUTHENTICATION_TYPE;
			}
			bean.setAuthenticationType(property, method);
		}
	}

	public void setBean(AuthenticationTypeSelectorListener bean) {
		this.bean = bean;
	}
}
