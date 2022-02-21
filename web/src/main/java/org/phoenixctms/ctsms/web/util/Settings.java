package org.phoenixctms.ctsms.web.util;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.phoenixctms.ctsms.enumeration.AuthenticationType;
import org.phoenixctms.ctsms.enumeration.Color;
import org.phoenixctms.ctsms.enumeration.ECRFFieldStatusQueue;
import org.phoenixctms.ctsms.enumeration.EventImportance;
import org.phoenixctms.ctsms.enumeration.InputFieldType;
import org.phoenixctms.ctsms.enumeration.PaymentMethod;
import org.phoenixctms.ctsms.enumeration.RandomizationMode;
import org.phoenixctms.ctsms.enumeration.Sex;
import org.phoenixctms.ctsms.enumeration.VariablePeriod;
import org.phoenixctms.ctsms.enumeration.VisitScheduleDateMode;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.web.util.DateUtil.DurationUnitOfTime;

public final class Settings {

	public enum Bundle {

		SETTINGS("settings"), WINDOWS("windows"), THEMES("themes");

		private final String var;

		private Bundle(final String var) {
			this.var = var;
		}

		public String getVar() {
			return var;
		}
	}

	private static Map<String, String> themes;
	private static Map<Long, Color> inventoryMaintenanceDueInColorMap;
	private static Map<Long, Color> courseExpirationDueInColorMap;
	private static Map<Long, Color> timelineEventDueInColorMap;
	private static Map<Long, Color> probandAutoDeleteDueInColorMap;
	public final static String WEB_ROOT_PACKAGE_NAME = "org.phoenixctms.ctsms.web";

	private static Map<Long, Color> createColorMap(ArrayList<String> durations, ArrayList<String> colors) {
		HashMap<Long, Color> colorMap = new HashMap<Long, Color>(Math.min(durations.size(), colors.size()));
		for (int i = 0; i < durations.size() && i < colors.size(); i++) {
			try {
				colorMap.put(Long.parseLong(durations.get(i)), Color.fromString(colors.get(i)));
			} catch (NumberFormatException e) {
			}
		}
		return colorMap;
	}

	public static AuthenticationType getAuthenticationType(String key, Bundle bundle, AuthenticationType defaultValue) {
		String value = CommonUtil.getValue(key, getBundle(bundle), defaultValue == null ? null : defaultValue.name());
		if (value != null && value.length() > 0) {
			return AuthenticationType.fromString(value); // illegal arg exc!
		} else {
			return null;
		}
	}

	public static boolean getBoolean(String key, Bundle bundle, boolean defaultValue) {
		return CommonUtil.getValue(key, getBundle(bundle), defaultValue);
	}

	public static Boolean getBooleanNullable(String key, Bundle bundle, Boolean defaultValue) {
		return CommonUtil.getValueNullable(key, getBundle(bundle), defaultValue);
	}

	private static ResourceBundle getBundle(Bundle bundle) {
		ResourceBundle result = null;
		try {
			result = CommonUtil.getBundle(WEB_ROOT_PACKAGE_NAME + "." + bundle.getVar(), Locale.ROOT);
		} catch (MissingResourceException e) {
		}
		if (result == null) {
			FacesContext context = FacesContext.getCurrentInstance();
			if (context != null) {
				result = context.getApplication().getResourceBundle(context, bundle.getVar());
			}
		}
		return result;
	}

	public static Color getColor(String key, Bundle bundle, Color defaultValue) {
		String value = CommonUtil.getValue(key, getBundle(bundle), defaultValue == null ? null : defaultValue.name());
		if (value != null && value.length() > 0) {
			return Color.fromString(value); // illegal arg exc!
		} else {
			return null;
		}
	}

	public static List<Color> getColorList(String key, Bundle bundle, ArrayList<String> defaultValue) {
		ArrayList<String> colors = CommonUtil.getValueStringList(key, getBundle(bundle), defaultValue);
		ArrayList<Color> result = new ArrayList<Color>(colors.size());
		Iterator<String> it = colors.iterator();
		while (it.hasNext()) {
			result.add(Color.fromString(it.next()));
		}
		return result;
	}

	public static String getContactEmail() {
		return MessageFormat.format(Settings.getString(SettingCodes.CONTACT_EMAIL, Bundle.SETTINGS, DefaultSettings.CONTACT_EMAIL), WebUtil.getEmailDomainName());
	}

	public static synchronized Map<Long, Color> getCourseExpirationDueInColorMap() {
		if (courseExpirationDueInColorMap == null) {
			courseExpirationDueInColorMap = createColorMap(
					CommonUtil.getValueStringList(SettingCodes.COURSE_EXPIRATION_DUE_IN_DURATIONS, getBundle(Bundle.SETTINGS), DefaultSettings.COURSE_EXPIRATION_DUE_IN_DURATIONS),
					CommonUtil.getValueStringList(SettingCodes.COURSE_EXPIRATION_DUE_IN_COLORS, getBundle(Bundle.SETTINGS), DefaultSettings.COURSE_EXPIRATION_DUE_IN_COLORS));
		}
		return courseExpirationDueInColorMap;
	}

	public static double getDouble(String key, Bundle bundle, double defaultValue) {
		return CommonUtil.getValue(key, getBundle(bundle), defaultValue);
	}

	public static Double getDoubleNullable(String key, Bundle bundle, Double defaultValue) {
		return CommonUtil.getValueNullable(key, getBundle(bundle), defaultValue);
	}

	public static DurationUnitOfTime getDurationUnitOfTime(String key, Bundle bundle, DurationUnitOfTime defaultValue) {
		String value = CommonUtil.getValue(key, getBundle(bundle), defaultValue == null ? null : defaultValue.name());
		if (value != null && value.length() > 0) {
			return DurationUnitOfTime.valueOf(value); // illegal arg exc!
		} else {
			return null;
		}
	}

	public static ECRFFieldStatusQueue getEcrfFieldStatusQueue(String key, Bundle bundle, ECRFFieldStatusQueue defaultValue) {
		String value = CommonUtil.getValue(key, getBundle(bundle), defaultValue == null ? null : defaultValue.name());
		if (value != null && value.length() > 0) {
			return ECRFFieldStatusQueue.fromString(value); // illegal arg exc!
		} else {
			return null;
		}
	}

	public static EventImportance getEventImportance(String key, Bundle bundle, EventImportance defaultValue) {
		String value = CommonUtil.getValue(key, getBundle(bundle), defaultValue == null ? null : defaultValue.name());
		if (value != null && value.length() > 0) {
			return EventImportance.fromString(value); // illegal arg exc!
		} else {
			return null;
		}
	}

	public static float getFloat(String key, Bundle bundle, float defaultValue) {
		return CommonUtil.getValue(key, getBundle(bundle), defaultValue);
	}

	public static Float getFloatNullable(String key, Bundle bundle, Float defaultValue) {
		return CommonUtil.getValueNullable(key, getBundle(bundle), defaultValue);
	}

	public static InputFieldType getInputFieldType(String key, Bundle bundle, InputFieldType defaultValue) {
		String value = CommonUtil.getValue(key, getBundle(bundle), defaultValue == null ? null : defaultValue.name());
		if (value != null && value.length() > 0) {
			return InputFieldType.fromString(value); // illegal arg exc!
		} else {
			return null;
		}
	}

	public static ArrayList<SelectItem> getDurationList(String key, Bundle bundle, ArrayList<String> defaultValue, boolean signed, DurationUnitOfTime mostSignificant,
			DurationUnitOfTime leastSignificant, int leastSignificantDecimals, Set<Integer> append) {
		ArrayList<SelectItem> durations = new ArrayList<SelectItem>();
		Iterator it = CommonUtil.getValueStringList(key, getBundle(bundle), defaultValue).iterator();
		LinkedHashSet<Integer> appendDurations = new LinkedHashSet<Integer>();
		if (append != null) {
			appendDurations.addAll(append);
		}
		while (it.hasNext()) {
			SelectItem duration = null;
			try {
				duration = DateUtil.getDurationItem((String) it.next(), signed, mostSignificant,
						leastSignificant, leastSignificantDecimals);
			} catch (NumberFormatException e) {
			}
			if (duration != null) {
				durations.add(duration);
				if (append != null && append.contains(duration.getValue())) {
					appendDurations.remove(duration.getValue());
				}
			}
		}
		it = appendDurations.iterator();
		while (it.hasNext()) {
			SelectItem duration = DateUtil.getDurationItem((Integer) it.next(), signed, mostSignificant,
					leastSignificant, leastSignificantDecimals);
			durations.add(duration);
		}
		return durations;
	}

	public static int getInt(String key, Bundle bundle, int defaultValue) {
		return CommonUtil.getValue(key, getBundle(bundle), defaultValue);
	}

	public static Integer getIntNullable(String key, Bundle bundle, Integer defaultValue) {
		return CommonUtil.getValueNullable(key, getBundle(bundle), defaultValue);
	}

	public static synchronized Map<Long, Color> getInventoryMaintenanceDueInColorMap() {
		if (inventoryMaintenanceDueInColorMap == null) {
			inventoryMaintenanceDueInColorMap = createColorMap(CommonUtil.getValueStringList(SettingCodes.INVENTORY_MAINTENANCE_DUE_IN_DURATIONS, getBundle(Bundle.SETTINGS),
					DefaultSettings.INVENTORY_MAINTENANCE_DUE_IN_DURATIONS),
					CommonUtil
							.getValueStringList(SettingCodes.INVENTORY_MAINTENANCE_DUE_IN_COLORS, getBundle(Bundle.SETTINGS), DefaultSettings.INVENTORY_MAINTENANCE_DUE_IN_COLORS));
		}
		return inventoryMaintenanceDueInColorMap;
	}

	public static long getLong(String key, Bundle bundle, long defaultValue) {
		return CommonUtil.getValue(key, getBundle(bundle), defaultValue);
	}

	public static Long getLongNullable(String key, Bundle bundle, Long defaultValue) {
		return CommonUtil.getValueNullable(key, getBundle(bundle), defaultValue);
	}

	public static PaymentMethod getPaymentMethod(String key, Bundle bundle, PaymentMethod defaultValue) {
		String value = CommonUtil.getValue(key, getBundle(bundle), defaultValue == null ? null : defaultValue.name());
		if (value != null && value.length() > 0) {
			return PaymentMethod.fromString(value); // illegal arg exc!
		} else {
			return null;
		}
	}

	public static VisitScheduleDateMode getVisitScheduleDateMode(String key, Bundle bundle, VisitScheduleDateMode defaultValue) {
		String value = CommonUtil.getValue(key, getBundle(bundle), defaultValue == null ? null : defaultValue.name());
		if (value != null && value.length() > 0) {
			return VisitScheduleDateMode.fromString(value); // illegal arg exc!
		} else {
			return null;
		}
	}

	public static synchronized Map<Long, Color> getProbandAutoDeleteDueInColorMap() {
		if (probandAutoDeleteDueInColorMap == null) {
			probandAutoDeleteDueInColorMap = createColorMap(CommonUtil.getValueStringList(SettingCodes.PROBAND_AUTO_DELETE_DUE_IN_DURATIONS, getBundle(Bundle.SETTINGS),
					DefaultSettings.PROBAND_AUTO_DELETE_DUE_IN_DURATIONS),
					CommonUtil.getValueStringList(SettingCodes.PROBAND_AUTO_DELETE_DUE_IN_COLORS, getBundle(Bundle.SETTINGS), DefaultSettings.PROBAND_AUTO_DELETE_DUE_IN_COLORS));
		}
		return probandAutoDeleteDueInColorMap;
	}

	public static RandomizationMode getRandomizationMode(String key, Bundle bundle, RandomizationMode defaultValue) {
		String value = CommonUtil.getValue(key, getBundle(bundle), defaultValue == null ? null : defaultValue.name());
		if (value != null && value.length() > 0) {
			return RandomizationMode.fromString(value); // illegal arg exc!
		} else {
			return null;
		}
	}

	public static Sex getSex(String key, Bundle bundle, Sex defaultValue) {
		String value = CommonUtil.getValue(key, getBundle(bundle), defaultValue == null ? null : defaultValue.name());
		if (value != null && value.length() > 0) {
			return Sex.fromString(value); // illegal arg exc!
		} else {
			return null;
		}
	}

	public static String getString(String key, Bundle bundle, String defaultValue) {
		return CommonUtil.getValue(key, getBundle(bundle), defaultValue);
	}

	public static ArrayList<String> getStringList(String key, Bundle bundle, ArrayList<String> defaultValue) {
		return CommonUtil.getValueStringList(key, getBundle(bundle), defaultValue);
	}

	public synchronized static Map<String, String> getThemes() {
		if (themes == null) {
			themes = CommonUtil.getBundleSymbolMap(getBundle(Bundle.THEMES), true);
		}
		return themes;
	}

	public static synchronized Map<Long, Color> getTimelineEventDueInColorMap() {
		if (timelineEventDueInColorMap == null) {
			timelineEventDueInColorMap = createColorMap(
					CommonUtil.getValueStringList(SettingCodes.TIMELINE_EVENT_DUE_IN_DURATIONS, getBundle(Bundle.SETTINGS), DefaultSettings.TIMELINE_EVENT_DUE_IN_DURATIONS),
					CommonUtil.getValueStringList(SettingCodes.TIMELINE_EVENT_DUE_IN_COLORS, getBundle(Bundle.SETTINGS), DefaultSettings.TIMELINE_EVENT_DUE_IN_COLORS));
		}
		return timelineEventDueInColorMap;
	}

	public static String getTrustedRefererHostsString(HttpServletRequest request) {
		return CommonUtil.stringListToString(getTrustetRefererHosts(request));
	}

	public static ArrayList<String> getTrustetRefererHosts(HttpServletRequest request) {
		ArrayList<String> referers = CommonUtil.getValueStringList(SettingCodes.TRUSTED_REFERER_HOSTS, getBundle(Bundle.SETTINGS), DefaultSettings.TRUSTED_REFERER_HOSTS);
		ArrayList<String> result = new ArrayList<String>(referers.size());
		Iterator<String> it = referers.iterator();
		while (it.hasNext()) {
			String referer = it.next();
			String substitution;
			if (referer.equalsIgnoreCase(DefaultSettings.LOCAL_ADDR_REFERER)) {
				if (request != null) {
					substitution = request.getLocalAddr();
					if (!CommonUtil.isEmptyString(substitution)) {
						result.add(substitution);
					}
				}
			} else if (referer.equalsIgnoreCase(DefaultSettings.LOCAL_NAME_REFERER)) {
				if (request != null) {
					substitution = request.getLocalName();
					if (!CommonUtil.isEmptyString(substitution)) {
						result.add(substitution);
					}
				}
			} else if (referer.equalsIgnoreCase(DefaultSettings.HTTP_DOMAIN_REFERER)) {
				substitution = WebUtil.getHttpDomainName();
				if (!CommonUtil.isEmptyString(substitution)) {
					result.add(substitution);
				}
			} else {
				result.add(referer);
			}
		}
		return result;
	}

	public static VariablePeriod getVariablePeriod(String key, Bundle bundle, VariablePeriod defaultValue) {
		String value = CommonUtil.getValue(key, getBundle(bundle), defaultValue == null ? null : defaultValue.name());
		if (value != null && value.length() > 0) {
			return VariablePeriod.fromString(value); // illegal arg exc!
		} else {
			return null;
		}
	}

	private Settings() {
	}
}
