package org.phoenixctms.ctsms.web.conversion;

import javax.faces.convert.FacesConverter;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.web.util.WebUtil;

@FacesConverter(value = TimeConverter.CONVERTER_ID)
public class TimeConverter extends DateTimeConverterBase {

	public static final String CONVERTER_ID = "ctsms.Time";

	public TimeConverter() {
		super();
	}

	@Override
	protected void setFormat() {
		this.setPattern(CommonUtil.getInputTimePattern(WebUtil.getDateFormat()));
	}
}
