package org.phoenixctms.ctsms.web.conversion;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.phoenixctms.ctsms.util.CommonUtil;

@FacesConverter(value = StringConverter.CONVERTER_ID)
public class StringConverter implements Converter {

	public static final String CONVERTER_ID = "ctsms.String";

	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
		return submittedValue;
	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
		if (value == null) {
			return CommonUtil.NO_SELECTION_VALUE;
		} else if (value instanceof String) {
			return (String) value;
		} else {
			return CommonUtil.NO_SELECTION_VALUE;
		}
	}
}