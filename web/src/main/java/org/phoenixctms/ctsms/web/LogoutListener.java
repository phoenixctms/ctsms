package org.phoenixctms.ctsms.web;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.phoenixctms.ctsms.web.model.ApplicationScopeBean;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class LogoutListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent event) {
		WebUtil.setSessionTimeout(event.getSession());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		if (event.getSession() != null) {
			ApplicationScopeBean.unregisterActiveSession(event.getSession().getId());
		}
	}
}
