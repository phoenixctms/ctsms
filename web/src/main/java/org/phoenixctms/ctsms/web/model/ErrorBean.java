package org.phoenixctms.ctsms.web.model;

import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@RequestScoped
public class ErrorBean {

	public ErrorBean() {
	}

	public String getExceptionMessage() {
		// Get the current JSF context
		FacesContext context = FacesContext.getCurrentInstance();
		Map requestMap = context.getExternalContext().getRequestMap();
		// Fetch the exception
		Throwable ex = (Throwable) requestMap.get("javax.servlet.error.exception");
		return ex != null ? ex.getLocalizedMessage() : "";
	}
}
