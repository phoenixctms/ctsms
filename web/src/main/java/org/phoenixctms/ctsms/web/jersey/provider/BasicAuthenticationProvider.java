package org.phoenixctms.ctsms.web.jersey.provider;

import java.lang.reflect.Type;

import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import org.phoenixctms.ctsms.js.JsUtil;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.web.util.WebUtil;

import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.api.core.HttpRequestContext;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.server.impl.inject.AbstractHttpContextInjectable;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;

@Provider
public class BasicAuthenticationProvider
extends AbstractHttpContextInjectable<AuthenticationVO>
implements InjectableProvider<Context, Type> {

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
		// if (c.getRequest().getAuthenticationScheme() == HttpRequestContext.BASIC_AUTH) {
		String authHeaderValue = c.getRequest().getHeaderValue(HttpRequestContext.AUTHORIZATION);
		String[] credentials = null;
		if (authHeaderValue != null) {
			credentials = JsUtil.decodeBase64(authHeaderValue.replaceFirst("[B|b]asic ", "")).split(":", 2);
		}
		AuthenticationVO result;
		if (credentials != null && credentials.length == 2) {
			result = new AuthenticationVO(credentials[0], credentials[1], null, WebUtil.getRemoteHost(request));
		} else {
			result = new AuthenticationVO();
			result.setHost(WebUtil.getRemoteHost(request));
		}
		return result;
	}
}
