package org.phoenixctms.ctsms.web.conversion;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.LdapEntryVO;

public abstract class LdapEntryConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
		if (submittedValue.trim().equals(CommonUtil.NO_SELECTION_VALUE)) {
			return null;
		} else {
			return getLdapEntry(submittedValue);
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
		} else if (value instanceof LdapEntryVO) {
			return ((LdapEntryVO) value).getUsername();
		} else {
			return CommonUtil.NO_SELECTION_VALUE;
		}
	}

	protected abstract Object getLdapEntry(String username);
}
