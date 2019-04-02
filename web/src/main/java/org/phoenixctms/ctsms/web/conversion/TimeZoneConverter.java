package org.phoenixctms.ctsms.web.conversion;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.TimeZoneVO;
import org.phoenixctms.ctsms.web.util.WebUtil;

@FacesConverter(value = TimeZoneConverter.CONVERTER_ID)
public class TimeZoneConverter implements Converter {

	public static final String CONVERTER_ID = "ctsms.TimeZone";

	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
		if (submittedValue.trim().equals(CommonUtil.NO_SELECTION_VALUE)) {
			return null;
		} else {
			return getTimeZone(submittedValue);
		}
	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
		if (value == null) {
			return CommonUtil.NO_SELECTION_VALUE;
		} else if (value instanceof String) {
			if (CommonUtil.NO_SELECTION_VALUE.equals(value)) {
				return CommonUtil.NO_SELECTION_VALUE;
			} else {
				return (String) value;
			}
		} else if (value instanceof TimeZoneVO) {
			return ((TimeZoneVO) value).getTimeZoneID();
		} else {
			return CommonUtil.NO_SELECTION_VALUE;
		}
	}

	protected Object getTimeZone(String timeZoneID) {
		return WebUtil.getTimeZone(timeZoneID);
	}
}

