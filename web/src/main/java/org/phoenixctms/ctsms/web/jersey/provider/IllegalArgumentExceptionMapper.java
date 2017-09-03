package org.phoenixctms.ctsms.web.jersey.provider;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class IllegalArgumentExceptionMapper extends ExceptionMapperBase implements
		ExceptionMapper<IllegalArgumentException> {

	@Override
	public Response toResponse(IllegalArgumentException ex) {
		return buildJsonResponse(Status.BAD_REQUEST, ex).build();
	}
}