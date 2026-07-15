package org.phoenixctms.ctsms.web.jersey.provider;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.util.CommonUtil;

@Provider
public class AuthenticationExceptionMapper extends ExceptionMapperBase implements
		ExceptionMapper<AuthenticationException> {

	@Override
	public Response toResponse(AuthenticationException ex) {
		return buildJsonResponse(Status.UNAUTHORIZED, ex).header(HttpHeaders.WWW_AUTHENTICATE,
				"Basic realm=\"" + CommonUtil.API_REALM + "\"").build();
	}
}