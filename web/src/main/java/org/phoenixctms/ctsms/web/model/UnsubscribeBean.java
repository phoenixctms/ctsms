package org.phoenixctms.ctsms.web.model;


import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.web.util.GetParamNames;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.WebUtil;


@ManagedBean
@RequestScoped
public class UnsubscribeBean {

	private String beacon;
	private Throwable ex;

	public UnsubscribeBean() {
	}

	public String getBeacon() {
		return beacon;
	}

	public String getExceptionMessage() {
		return ex != null ? ex.getLocalizedMessage() : "";
	}

	@PostConstruct
	private void init() {
		beacon = WebUtil.getParamValue(GetParamNames.BEACON);
		// System.out.println("xxx");
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		if (!WebUtil.isTrustedReferer(request)) {
			try {
				WebUtil.getServiceLocator().getToolsService().unsubscribeProbandEmail(beacon);
			} catch (ServiceException e) {
				ex = e;
			} catch (AuthenticationException e) {
				// response.sendError(HttpServletResponse.SC_FORBIDDEN);
				// return null;
				ex = e;
			} catch (AuthorisationException e) {
				// response.sendError(HttpServletResponse.SC_FORBIDDEN);
				// return null;
				ex = e;
			} catch (IllegalArgumentException e) {
				ex = e;
			}
		} else {
			ex = new Exception(Messages.getMessage(MessageCodes.REQUEST_FROM_TRUSTED_REFERER));
		}
	}
}
