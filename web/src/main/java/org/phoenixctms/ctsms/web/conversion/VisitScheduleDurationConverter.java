package org.phoenixctms.ctsms.web.conversion;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;

import org.phoenixctms.ctsms.web.util.DateUtil;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;

@FacesConverter(value = VisitScheduleDurationConverter.CONVERTER_ID)
public class VisitScheduleDurationConverter extends DurationConverterBase {

	public static final String CONVERTER_ID = "ctsms.VisitScheduleDuration";

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		try {
			return DateUtil.getDurationItem(value, false,
					Settings.getDurationUnitOfTime(SettingCodes.VISIT_SCHEDULE_ITEM_DURATION_MOST_SIGNIFICANT_DURATION_UNIT_OF_TIME, Bundle.SETTINGS,
							DefaultSettings.VISIT_SCHEDULE_ITEM_DURATION_MOST_SIGNIFICANT_DURATION_UNIT_OF_TIME),
					Settings.getDurationUnitOfTime(SettingCodes.VISIT_SCHEDULE_ITEM_DURATION_LEAST_SIGNIFICANT_DURATION_UNIT_OF_TIME, Bundle.SETTINGS,
							DefaultSettings.VISIT_SCHEDULE_ITEM_DURATION_LEAST_SIGNIFICANT_DURATION_UNIT_OF_TIME),
					Settings.getInt(SettingCodes.VISIT_SCHEDULE_ITEM_DURATION_LEAST_SIGNIFICANT_DURATION_UNIT_OF_TIME_DECIMALS, Bundle.SETTINGS,
							DefaultSettings.VISIT_SCHEDULE_ITEM_DURATION_LEAST_SIGNIFICANT_DURATION_UNIT_OF_TIME_DECIMALS));
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
