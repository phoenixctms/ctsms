package org.phoenixctms.ctsms.web.jersey.provider;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.phoenixctms.ctsms.exception.ServiceException;

@Provider
public class ServiceExceptionMapper extends ExceptionMapperBase implements
		ExceptionMapper<ServiceException> {

	@Override
	public Response toResponse(ServiceException ex) {
		return buildJsonResponse(Status.CONFLICT, ex).build();
	}
}