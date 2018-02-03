package org.phoenixctms.ctsms.web.jersey.provider;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.util.AuthorisationExceptionCodes;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;


@Provider
public class TrustedHostFilter extends ExceptionMapperBase implements ContainerRequestFilter {

	@Context
	javax.servlet.http.HttpServletRequest request;

	@Override
	public ContainerRequest filter(ContainerRequest request) {
		if (Settings.getBoolean(SettingCodes.API_TRUSTED_HOSTS_ONLY, Bundle.SETTINGS, DefaultSettings.API_TRUSTED_HOSTS_ONLY) && !WebUtil.isTrustedHost(this.request)) {
			AuthorisationException ex = new AuthorisationException(Messages.getMessage(MessageCodes.HOST_NOT_ALLOWED_OR_UNKNOWN_HOST, WebUtil.getRemoteHost(this.request)));
			ex.setErrorCode(AuthorisationExceptionCodes.HOST_NOT_ALLOWED_OR_UNKNOWN_HOST);
			throw new WebApplicationException(ex);
		} else {
			return request;
		}
	}
}
