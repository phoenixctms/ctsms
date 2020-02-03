package org.phoenixctms.ctsms.web.conversion;

import javax.faces.convert.FacesConverter;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.web.util.WebUtil;

@FacesConverter(value = DateTimeConverter.CONVERTER_ID)
public class DateTimeConverter extends DateTimeConverterBase {

	public static final String CONVERTER_ID = "ctsms.DateTime";

	public DateTimeConverter() {
		super();
	}

	@Override
	protected void setFormat() {
		this.setPattern(CommonUtil.getInputDateTimePattern(WebUtil.getDateFormat()));
	}
}