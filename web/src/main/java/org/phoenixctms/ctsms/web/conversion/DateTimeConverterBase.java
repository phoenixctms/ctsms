package org.phoenixctms.ctsms.web.conversion;

import java.util.TimeZone;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.phoenixctms.ctsms.web.util.WebUtil;

public abstract class DateTimeConverterBase extends javax.faces.convert.DateTimeConverter {

	public DateTimeConverterBase() {
		super();
	}

	private void configure(UIComponent component) {
		setLocale(WebUtil.getLocale());
		setTimeZone(null);
		Object timeZone = component.getAttributes().get("timeZone");
		if (timeZone != null) {
			if (timeZone instanceof String) {
				setTimeZone(TimeZone.getTimeZone((String) timeZone));
			} else if (timeZone instanceof TimeZone) {
				setTimeZone((TimeZone) timeZone);
			}
		}
		setFormat();
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
