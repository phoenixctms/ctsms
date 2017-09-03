package org.phoenixctms.ctsms.web.model;

import org.phoenixctms.ctsms.enumeration.AuthenticationType;

public interface AuthenticationTypeSelectorListener {

	public final static AuthenticationType NO_SELECTION_AUTHENTICATION_TYPE = AuthenticationType.LOCAL;

	public AuthenticationType getAuthenticationType(int property);

	public void setAuthenticationType(int property, AuthenticationType method);
}
