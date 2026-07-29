package org.phoenixctms.ctsms.web.jersey.provider;

import java.lang.reflect.Type;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;

import org.phoenixctms.ctsms.js.JsUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
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
		String host = WebUtil.getRemoteHost(request);
		boolean otpRequired = Settings.getBoolean(SettingCodes.API_TRUSTED_HOST_2FA_REQUIRED, Bundle.SETTINGS, DefaultSettings.API_TRUSTED_HOST_2FA_REQUIRED)
				|| !WebUtil.isTrustedHost(request);
		if (authHeaderValue != null) {
			if (authHeaderValue.toLowerCase().startsWith(BASIC_AUTHENTICATION_SCHEME.toLowerCase() + " ")) {
				String[] credentials = JsUtil.decodeBase64(authHeaderValue.substring(BASIC_AUTHENTICATION_SCHEME.length()).trim()).split(":", 2);
				if (credentials.length == 2) {
					return new AuthenticationVO(credentials[0], credentials[1], null, null, host, CommonUtil.API_REALM, null, otpRequired);
				}
			} else if (authHeaderValue.toLowerCase().startsWith(BEARER_AUTHENTICATION_SCHEME.toLowerCase() + " ")) {
				AuthenticationVO result = new AuthenticationVO();
				result.setHost(host);
				result.setJwt(authHeaderValue.substring(BEARER_AUTHENTICATION_SCHEME.length()).trim());
				result.setRealm(CommonUtil.API_REALM);
				result.setOtpRequired(otpRequired);
				return result;
			}
		}
		AuthenticationVO result = new AuthenticationVO();
		result.setHost(host);
		result.setRealm(CommonUtil.API_REALM);
		result.setOtpRequired(otpRequired);
		return result;
	}
}
