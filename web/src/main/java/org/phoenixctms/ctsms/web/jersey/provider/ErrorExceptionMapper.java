package org.phoenixctms.ctsms.web.jersey.provider;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ErrorExceptionMapper extends ExceptionMapperBase implements
		ExceptionMapper<Error> {

	@Override
	public Response toResponse(Error ex) {
		return buildJsonResponse(Status.INTERNAL_SERVER_ERROR, ex).build();
	}
}