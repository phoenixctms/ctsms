package org.phoenixctms.ctsms.web.conversion;

import javax.faces.convert.FacesConverter;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.web.util.WebUtil;

@FacesConverter(value = DateConverter.CONVERTER_ID)
public class DateConverter extends DateTimeConverterBase {

	public static final String CONVERTER_ID = "ctsms.Date";

	public DateConverter() {
		super();
	}

	@Override
	protected void setFormat() {
		this.setPattern(CommonUtil.getInputDatePattern(WebUtil.getDateFormat()));
	}
}
