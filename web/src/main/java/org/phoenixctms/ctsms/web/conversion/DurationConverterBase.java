package org.phoenixctms.ctsms.web.conversion;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.util.CommonUtil;

public abstract class DurationConverterBase implements Converter {

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
		} else if (value instanceof SelectItem) {
			return Integer.toString((Integer) ((SelectItem) value).getValue());
		} else {
			return CommonUtil.NO_SELECTION_VALUE;
		}
	}
}
