package org.phoenixctms.ctsms.web.conversion;

import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.phoenixctms.ctsms.web.util.DateUtil;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;

@FacesConverter(value = TimeConverter.CONVERTER_ID)
public class TimeConverter extends DateTimeConverterBase {

	public static final String CONVERTER_ID = "ctsms.Time";

	public TimeConverter() {
		super();
		String converterTimePattern = Settings.getString(SettingCodes.CONVERTER_TIME_PATTERN, Bundle.SETTINGS, DefaultSettings.CONVERTER_TIME_PATTERN);
		if (converterTimePattern != null && converterTimePattern.length() > 0) {
			try {
				this.setPattern(converterTimePattern);
			} catch (ConverterException e) {
				setTimeStyleType();
			}
		} else {
			setTimeStyleType();
		}
	}

	private void setTimeStyleType() {
		this.setTimeStyle(Settings.getString(SettingCodes.CONVERTER_TIME_STYLE, Bundle.SETTINGS, DefaultSettings.CONVERTER_TIME_STYLE));
		this.setType(DateUtil.ConverterType.TIME.toString());
	}
}
