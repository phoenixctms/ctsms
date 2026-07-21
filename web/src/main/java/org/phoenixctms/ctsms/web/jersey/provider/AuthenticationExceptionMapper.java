package org.phoenixctms.ctsms.web.jersey.provider;

import java.util.List;

import javax.ws.rs.core.Context;
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

	private final static String BASIC_AUTHENTICATION_SCHEME = "Basic";
	private final static String BEARER_AUTHENTICATION_SCHEME = "Bearer";

	@Context
	private HttpHeaders headers;

	@Override
	public Response toResponse(AuthenticationException ex) {
		return buildJsonResponse(Status.UNAUTHORIZED, ex).header(HttpHeaders.WWW_AUTHENTICATE,
				getWwwAuthenticateValue()).build();
	}

	private String getWwwAuthenticateValue() {
		String authHeaderValue = null;
		if (headers != null) {
			List<String> authHeaders = headers.getRequestHeader(HttpHeaders.AUTHORIZATION);
			if (authHeaders != null && !authHeaders.isEmpty()) {
				authHeaderValue = authHeaders.get(0);
			}
		}
		if (authHeaderValue != null
				&& authHeaderValue.toLowerCase().startsWith(BEARER_AUTHENTICATION_SCHEME.toLowerCase() + " ")) {
			// Avoid browser HTTP Basic password prompts on failed JWT (XHR) requests.
			return BEARER_AUTHENTICATION_SCHEME + " realm=\"" + CommonUtil.API_REALM + "\"";
		}
		return BASIC_AUTHENTICATION_SCHEME + " realm=\"" + CommonUtil.API_REALM + "\"";
	}
}
