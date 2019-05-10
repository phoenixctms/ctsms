package org.phoenixctms.ctsms.web.jersey.provider;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.phoenixctms.ctsms.exception.AuthorisationException;

@Provider
public class AuthorisationExceptionMapper extends ExceptionMapperBase implements
		ExceptionMapper<AuthorisationException> {

	public static Status STATUS = Status.FORBIDDEN;

	@Override
	public Response toResponse(AuthorisationException ex) {
		return buildJsonResponse(STATUS, ex).build();
	}
}