package org.phoenixctms.ctsms.web.jersey.provider;

import java.lang.reflect.Type;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.js.JsUtil;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.web.util.WebUtil;

import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.server.impl.inject.AbstractHttpContextInjectable;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;

@Provider
public class AuthenticationProvider
		extends AbstractHttpContextInjectable<AuthenticationVO>
		implements InjectableProvider<Context, Type> {

	private final static String BASIC_AUTHENTICATION_SCHEME = "Basic";
	private final static String BEARER_AUTHENTICATION_SCHEME = "Bearer";
	@Context
	javax.servlet.http.HttpServletRequest request;

	@Override
	public Injectable<AuthenticationVO> getInjectable(ComponentContext ic, Context a, Type c) {
		if (c.equals(AuthenticationVO.class)) {
			return this;
		}
		return null;
	}

	@Override
	public ComponentScope getScope() {
		return ComponentScope.PerRequest;
	}

	@Override
	public AuthenticationVO getValue(HttpContext c) {
		String authHeaderValue = c.getRequest().getHeaderValue(HttpHeaders.AUTHORIZATION);
		String[] credentials = null;
		if (authHeaderValue != null) {
			if (authHeaderValue.toLowerCase().startsWith(BASIC_AUTHENTICATION_SCHEME.toLowerCase() + " ")) {
				credentials = JsUtil.decodeBase64(authHeaderValue.substring(BASIC_AUTHENTICATION_SCHEME.length()).trim()).split(":", 2);
			} else if (authHeaderValue.toLowerCase().startsWith(BEARER_AUTHENTICATION_SCHEME.toLowerCase() + " ")) {
				String token = authHeaderValue.substring(BEARER_AUTHENTICATION_SCHEME.length()).trim();
				try {
					credentials = WebUtil.getServiceLocator().getToolsService().verifyJwt(token);
				} catch (AuthenticationException | AuthorisationException | ServiceException e) {
					throw new WebApplicationException(e);
				}
			}
		}
		AuthenticationVO result;
		if (credentials != null && credentials.length == 2) {
			result = new AuthenticationVO(credentials[0], credentials[1], null, null, WebUtil.getRemoteHost(request));
		} else {
			result = new AuthenticationVO();
			result.setHost(WebUtil.getRemoteHost(request));
		}
		return result;
	}
}
