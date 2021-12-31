package org.phoenixctms.ctsms.web.jersey.provider;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.phoenixctms.ctsms.exception.AuthorisationException;

@Provider
public class WebApplicationExceptionMapper extends ExceptionMapperBase implements
		ExceptionMapper<WebApplicationException> {

	@Override
	public Response toResponse(WebApplicationException ex) {
		if (ex != null) {
			if (ex.getCause() instanceof AuthorisationException) {
				return buildJsonResponse(AuthorisationExceptionMapper.STATUS, ex.getCause()).build();
			} else if (ex.getResponse() != null) {
				return buildJsonResponse(ex.getResponse().getStatus(), ex).build();
			}
		}
		return buildJsonResponse(Status.INTERNAL_SERVER_ERROR, ex).build();
	}
}