package org.phoenixctms.ctsms.web.conversion;

import java.util.TimeZone;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.phoenixctms.ctsms.web.util.WebUtil;

public abstract class DateTimeConverterBase extends javax.faces.convert.DateTimeConverter {

	// private boolean isConfigured;
	public DateTimeConverterBase() {
		super();
		// isConfigured = false;
	}

	private void configure(UIComponent component) {
		// if (!isConfigured) {
		setLocale(WebUtil.getLocale());
		Object timeZone = component.getAttributes().get("timeZone");
		if (timeZone != null) {
			if (timeZone instanceof String) {
				setTimeZone(TimeZone.getTimeZone((String) timeZone));
			} else if (timeZone instanceof TimeZone) {
				setTimeZone((TimeZone) timeZone);
			}
		}
		setFormat();
		// isConfigured = true;
		// }
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		configure(component);
		return super.getAsObject(context, component, value);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		configure(component);
		return super.getAsString(context, component, value);
	}

	protected abstract void setFormat();
}
