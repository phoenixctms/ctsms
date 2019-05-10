package org.phoenixctms.ctsms.web.conversion;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.util.WebUtil;

public abstract class IDVOConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
		if (submittedValue.trim().equals(CommonUtil.NO_SELECTION_VALUE)) {
			return null;
		} else {
			try {
				return IDVO.transformVo(getVo(Long.parseLong(submittedValue)));
			} catch (NumberFormatException exception) {
			}
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
		if (value == null) {
			return CommonUtil.NO_SELECTION_VALUE;
		} else if (value instanceof String) {
			if (CommonUtil.NO_SELECTION_VALUE.equals(value)) {
				return CommonUtil.NO_SELECTION_VALUE;
			} else {
				return WebUtil.stringToLong((String) value).toString();
			}
		} else if (value instanceof IDVO) {
			return ((IDVO) value).getId().toString();
		} else {
			return CommonUtil.NO_SELECTION_VALUE;
		}
	}

	public Map<String, String> getDetails(IDVO idvo) {
		return new LinkedHashMap<String, String>();
	}

	public abstract String getLabel(IDVO idvo);

	public String getName(IDVO idvo) {
		return "";
	}

	public abstract Object getVo(Long id);
}
