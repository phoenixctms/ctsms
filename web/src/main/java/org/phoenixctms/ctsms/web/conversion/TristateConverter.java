package org.phoenixctms.ctsms.web.conversion;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.web.util.WebUtil;

@FacesConverter(value = TristateConverter.CONVERTER_ID)
public class TristateConverter implements Converter {

	public static final String CONVERTER_ID = "ctsms.Tristate";

	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
		return WebUtil.stringToBoolean(submittedValue);
	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
		if (value == null) {
			return CommonUtil.NO_SELECTION_VALUE;
		} else if (value instanceof String) {
			if (CommonUtil.NO_SELECTION_VALUE.equals(value)) {
				return CommonUtil.NO_SELECTION_VALUE;
			} else {
				return WebUtil.stringToBoolean((String) value).toString();
			}
		} else if (value instanceof Boolean) {
			return ((Boolean) value).toString();
		} else {
			return CommonUtil.NO_SELECTION_VALUE;
		}
	}
}