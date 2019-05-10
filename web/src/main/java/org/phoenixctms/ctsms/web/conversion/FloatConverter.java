package org.phoenixctms.ctsms.web.conversion;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.web.util.WebUtil;

@FacesConverter(value = FloatConverter.CONVERTER_ID)
public class FloatConverter extends javax.faces.convert.FloatConverter {

	public static final String CONVERTER_ID = "ctsms.Float";
	private boolean isConfigured;
	private String decimalSeparator;

	public FloatConverter() {
		super();
		isConfigured = false;
		decimalSeparator = null;
	}

	private void configure() {
		if (!isConfigured) {
			decimalSeparator = WebUtil.getDecimalSeparator();
			isConfigured = true;
		}
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		configure();
		return super.getAsObject(context, component, CommonUtil.parseDecimal(value, decimalSeparator));
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		configure();
		return CommonUtil.formatDecimal(super.getAsString(context, component, value), decimalSeparator);
	}
}
