package org.phoenixctms.ctsms.web.conversion;

import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.phoenixctms.ctsms.web.util.DateUtil;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;

@FacesConverter(value = DateConverter.CONVERTER_ID)
public class DateConverter extends DateTimeConverterBase {

	public static final String CONVERTER_ID = "ctsms.Date";

	public DateConverter() {
		super();
		String converterDatePattern = Settings.getString(SettingCodes.CONVERTER_DATE_PATTERN, Bundle.SETTINGS, DefaultSettings.CONVERTER_DATE_PATTERN);
		if (converterDatePattern != null && converterDatePattern.length() > 0) {
			try {
				this.setPattern(converterDatePattern);
			} catch (ConverterException e) {
				setDateStyleType();
			}
		} else {
			setDateStyleType();
		}
	}

	private void setDateStyleType() {
		this.setDateStyle(Settings.getString(SettingCodes.CONVERTER_DATE_STYLE, Bundle.SETTINGS, DefaultSettings.CONVERTER_DATE_STYLE));
		this.setType(DateUtil.ConverterType.DATE.toString());
	}
}
