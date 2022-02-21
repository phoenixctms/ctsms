package org.phoenixctms.ctsms.web.conversion;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.web.util.DateUtil;
import org.phoenixctms.ctsms.web.util.DateUtil.DurationUnitOfTime;

public abstract class DurationConverterBase implements Converter {

	@Override
	public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
		if (value == null) {
			return CommonUtil.NO_SELECTION_VALUE;
		} else if (value instanceof String) {
			if (CommonUtil.NO_SELECTION_VALUE.equals(value)) {
				return CommonUtil.NO_SELECTION_VALUE;
			} else {
				Long seconds = DateUtil.getDurationFromString((String) value);
				if (seconds != null) {
					return Integer.toString(CommonUtil.safeLongToInt(seconds));
				} else {
					return CommonUtil.NO_SELECTION_VALUE;
				}
			}
		} else if (value instanceof SelectItem) {
			return DateUtil.getDurationString((Integer) ((SelectItem) value).getValue(), DurationUnitOfTime.SECONDS, DurationUnitOfTime.SECONDS, 0);
		} else {
			return CommonUtil.NO_SELECTION_VALUE;
		}
	}
}
