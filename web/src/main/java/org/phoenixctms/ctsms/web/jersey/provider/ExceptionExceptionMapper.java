package org.phoenixctms.ctsms.web.jersey.provider;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ExceptionExceptionMapper extends ExceptionMapperBase implements
		ExceptionMapper<Exception> {

	@Override
	public Response toResponse(Exception ex) {
		return buildJsonResponse(Status.INTERNAL_SERVER_ERROR, ex).build();
	}
}