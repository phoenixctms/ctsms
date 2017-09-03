package org.phoenixctms.ctsms.web.conversion;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.phoenixctms.ctsms.web.util.WebUtil;

public abstract class DateTimeConverterBase extends javax.faces.convert.DateTimeConverter {

	private boolean isLocaleTimeZoneSet;

	public DateTimeConverterBase() {
		super();
		isLocaleTimeZoneSet = false;
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		setLocaleTimeZone();
		return super.getAsObject(context, component, value);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		setLocaleTimeZone();
		return super.getAsString(context, component, value);
	}

	private void setLocaleTimeZone() {
		if (!isLocaleTimeZoneSet) {
			setLocale(WebUtil.getLocale());
			setTimeZone(WebUtil.getTimeZone());
			isLocaleTimeZoneSet = true;
		}
	}
}
