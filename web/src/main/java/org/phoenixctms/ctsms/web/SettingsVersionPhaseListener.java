package org.phoenixctms.ctsms.web;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.web.model.SessionScopeBean;
import org.phoenixctms.ctsms.web.util.WebUtil;

/**
 * Reloads pages that were rendered with user settings (locale, time zone, date format, theme, ...) which were changed
 * meanwhile, e.g. in another browser window of the same session.
 * <p>
 * Each view is stamped with the settings version of the session when it is rendered. A postback of a view with an
 * older settings version is not processed, but redirected to the page again (like an expired view), so it is rendered
 * with the current settings. This happens after the view is restored, before any submitted value is converted, so values
 * entered in the old date format or time zone are never parsed with the new ones.
 */
public class SettingsVersionPhaseListener implements PhaseListener {

	private static final long serialVersionUID = 1L;
	private static final String SETTINGS_VERSION_VIEW_MAP_KEY = "ctsmsSettingsVersion";

	private static SessionScopeBean getLoggedInSessionScopeBean(FacesContext context) {
		Object session = context.getExternalContext().getSession(false);
		if (session instanceof HttpSession) {
			SessionScopeBean sessionScopeBean = WebUtil.getSessionScopeBean((HttpSession) session);
			if (sessionScopeBean != null && sessionScopeBean.isLoggedIn()) {
				return sessionScopeBean;
			}
		}
		return null;
	}

	@Override
	public void afterPhase(PhaseEvent event) {
		if (PhaseId.RESTORE_VIEW.equals(event.getPhaseId())) {
			FacesContext context = event.getFacesContext();
			if (context.getResponseComplete() || !context.isPostback()) {
				return;
			}
			UIViewRoot viewRoot = context.getViewRoot();
			if (viewRoot == null) {
				return;
			}
			Map<String, Object> viewMap = viewRoot.getViewMap(false);
			if (viewMap == null) {
				return;
			}
			Object viewSettingsVersion = viewMap.get(SETTINGS_VERSION_VIEW_MAP_KEY);
			if (!(viewSettingsVersion instanceof Long)) {
				return;
			}
			SessionScopeBean sessionScopeBean = getLoggedInSessionScopeBean(context);
			if (sessionScopeBean != null && ((Long) viewSettingsVersion).longValue() < sessionScopeBean.getSettingsVersion()) {
				reload(context);
			}
		}
	}

	@Override
	public void beforePhase(PhaseEvent event) {
		if (PhaseId.RENDER_RESPONSE.equals(event.getPhaseId())) {
			FacesContext context = event.getFacesContext();
			UIViewRoot viewRoot = context.getViewRoot();
			if (viewRoot == null) {
				return;
			}
			SessionScopeBean sessionScopeBean = getLoggedInSessionScopeBean(context);
			if (sessionScopeBean != null) {
				Map<String, Object> viewMap = viewRoot.getViewMap();
				if (!viewMap.containsKey(SETTINGS_VERSION_VIEW_MAP_KEY)) {
					viewMap.put(SETTINGS_VERSION_VIEW_MAP_KEY, sessionScopeBean.getSettingsVersion());
				}
			}
		}
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.ANY_PHASE;
	}

	private static void reload(FacesContext context) {
		ExternalContext externalContext = context.getExternalContext();
		HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
		// the referer is the page with its GET parameters, the request url of a postback misses them:
		String url = request.getHeader("Referer");
		if (CommonUtil.isEmptyString(url) || !WebUtil.isTrustedReferer(url, request) || WebUtil.isLoginUrl(url, request)) {
			url = request.getRequestURL().toString();
		}
		try {
			if (!externalContext.isResponseCommitted()) {
				externalContext.redirect(url);
			}
		} catch (IOException | IllegalStateException e) {
		}
		context.responseComplete();
	}
}
