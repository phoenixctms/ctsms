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
		// String converterDatetimePattern = Settings.getString(SettingCodes.CONVERTER_DATETIME_PATTERN, Bundle.SETTINGS, DefaultSettings.CONVERTER_DATETIME_PATTERN);
		// if (converterDatetimePattern != null && converterDatetimePattern.length() > 0) {
		// try {
		// this.setPattern(converterDatetimePattern);
		// } catch (ConverterException e) {
		// setDateTimeStyleType();
		// }
		// } else {
		// setDateTimeStyleType();
		// }
	}
	// private void setDateTimeStyleType() {
	// this.setDateStyle(Settings.getString(SettingCodes.CONVERTER_DATE_STYLE, Bundle.SETTINGS, DefaultSettings.CONVERTER_DATE_STYLE));
	// this.setTimeStyle(Settings.getString(SettingCodes.CONVERTER_TIME_STYLE, Bundle.SETTINGS, DefaultSettings.CONVERTER_TIME_STYLE));
	// this.setType(DateUtil.ConverterType.DATETIME.toString());
	// }
}