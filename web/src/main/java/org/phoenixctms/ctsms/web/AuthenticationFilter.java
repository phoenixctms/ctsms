package org.phoenixctms.ctsms.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.phoenixctms.ctsms.js.JsUtil;
import org.phoenixctms.ctsms.web.model.SessionScopeBean;
import org.phoenixctms.ctsms.web.util.GetParamNames;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.Urls;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class AuthenticationFilter implements Filter {

	private final static String GET_METHOD = "GET";
	// http://stackoverflow.com/questions/3841361/jsf-http-session-login
	private FilterConfig config;

	@Override
	public void destroy() {
		config = null;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		if (request.getMethod().equals(GET_METHOD)) {
			HttpServletResponse response = (HttpServletResponse) res;
			SessionScopeBean sessionScopeBean = WebUtil.getSessionScopeBean(request);
			if (sessionScopeBean == null || !sessionScopeBean.isLoggedIn()) {
				StringBuilder url = new StringBuilder(Urls.LOGIN.toString(request));
				if (!(request.getRequestURI().equals(url.toString()) || request.getRequestURI().equals(Urls.PORTAL.toString(request)))) {
					url.append("?");
					url.append(GetParamNames.AUTHENTICATION_FAILED);
					url.append("=true&");
					url.append(GetParamNames.AUTHENTICATION_FAILED_MESSAGE);
					url.append("=");
					url.append(JsUtil.encodeBase64(Messages.getMessage(MessageCodes.AUTHENTICATION_REQUIRED_ERROR_MESSAGE), true));
					WebUtil.appendRefererParameter(url, request, "&");
				}
				response.sendRedirect(url.toString());
				return;
			}
		}
		chain.doFilter(req, res);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		this.config = config;
	}
}
