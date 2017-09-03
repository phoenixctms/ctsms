package org.phoenixctms.ctsms.web.jersey.provider;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class WebApplicationExceptionMapper extends ExceptionMapperBase implements
		ExceptionMapper<WebApplicationException> {

	@Override
	public Response toResponse(WebApplicationException ex) {
		return buildJsonResponse(Status.INTERNAL_SERVER_ERROR, ex).build();
	}
}