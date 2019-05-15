package org.phoenixctms.ctsms.web.jersey.provider;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.phoenixctms.ctsms.exception.ServiceException;

@Provider
public class ServiceExceptionMapper extends ExceptionMapperBase implements
		ExceptionMapper<ServiceException> {

	private final static int UNPROCESSABLE_ENTITY_STATUS = 422;

	@Override
	public Response toResponse(ServiceException ex) {
		return buildJsonResponse(UNPROCESSABLE_ENTITY_STATUS, ex).build();
	}
}