package org.phoenixctms.ctsms.web.jersey.provider;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;

@Provider
public class AuthenticationExceptionMapper extends ExceptionMapperBase implements
		ExceptionMapper<AuthenticationException> {

	@Override
	public Response toResponse(AuthenticationException ex) {
		return buildJsonResponse(Status.UNAUTHORIZED, ex).header(HttpHeaders.WWW_AUTHENTICATE,
				"Basic realm=\"" + Settings.getString(SettingCodes.API_REALM, Bundle.SETTINGS, DefaultSettings.API_REALM) + "\"").build();
	}
}