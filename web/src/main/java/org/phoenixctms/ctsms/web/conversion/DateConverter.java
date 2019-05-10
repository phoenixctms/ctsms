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
		// String converterDatePattern = Settings.getString(SettingCodes.CONVERTER_DATE_PATTERN, Bundle.SETTINGS, DefaultSettings.CONVERTER_DATE_PATTERN);
		// if (converterDatePattern != null && converterDatePattern.length() > 0) {
		// try {
		// this.setPattern(converterDatePattern);
		// } catch (ConverterException e) {
		// setDateStyleType();
		// }
		// } else {
		// setDateStyleType();
		// }
	}
	// private void setDateStyleType() {
	// this.setDateStyle(Settings.getString(SettingCodes.CONVERTER_DATE_STYLE, Bundle.SETTINGS, DefaultSettings.CONVERTER_DATE_STYLE));
	// this.setType(DateUtil.ConverterType.DATE.toString());
	// }
}
