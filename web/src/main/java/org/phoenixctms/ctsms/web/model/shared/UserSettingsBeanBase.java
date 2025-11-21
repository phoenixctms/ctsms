package org.phoenixctms.ctsms.web.model.shared;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CommonUtil.EllipsisPlacement;
import org.phoenixctms.ctsms.vo.TimeZoneVO;
import org.phoenixctms.ctsms.vo.UserInheritedVO;
import org.phoenixctms.ctsms.web.bundle.Labels;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

public abstract class UserSettingsBeanBase extends ManagedBeanBase {

	private ArrayList<SelectItem> locales;
	private TimeZoneVO timeZone;
	private ArrayList<SelectItem> themes;
	private ArrayList<SelectItem> dateFormats;
	private ArrayList<SelectItem> decimalSeparators;
	private long tableColumnCount;
	private UserInheritedVO parent;
	private Map<String, String> inheritedPropertiesMap;
	private HashMap<String, String> inheritedPropertiesTooltipsCache;
	protected Set<String> dutyRosterCalendarFilters;
	protected Set<String> inventoryBookingCalendarFilters;

	protected UserSettingsBeanBase() {
		timeZone = null;
		tableColumnCount = 0l;
		inheritedPropertiesMap = new HashMap<String, String>();
		inheritedPropertiesTooltipsCache = new HashMap<String, String>();
		dutyRosterCalendarFilters = new LinkedHashSet<String>();
		inventoryBookingCalendarFilters = new LinkedHashSet<String>();
	}

	public ArrayList<SelectItem> getDateFormats() {
		return dateFormats;
	}

	public ArrayList<SelectItem> getDecimalSeparators() {
		return decimalSeparators;
	}

	public ArrayList<SelectItem> getLocales() {
		return locales;
	}

	public ArrayList<SelectItem> getThemes() {
		return themes;
	}

	public TimeZoneVO getTimeZone() {
		return timeZone;
	}

	public void handleTimeZoneSelect(SelectEvent event) {
	}

	public void handleTimeZoneUnselect(UnselectEvent event) {
	}

	protected abstract void initSpecificSets();

	public Map<String, String> getInheritedPropertiesMap() {
		return inheritedPropertiesMap;
	}

	protected final void initSets() {
		if (this.locales == null) {
			this.locales = WebUtil.getLocales();
		}
		loadTimeZone();
		if (this.themes == null) {
			Map<String, String> themeMap = Settings.getThemes();
			this.themes = new ArrayList<SelectItem>(themeMap.size());
			Iterator<String> it = themeMap.keySet().iterator();
			while (it.hasNext()) {
				String themeName = it.next();
				this.themes.add(new SelectItem(themeName, themeMap.get(themeName)));
			}
		}
		this.dateFormats = WebUtil.getDateFormats(getDateFormat());
		if (this.decimalSeparators == null) {
			this.decimalSeparators = WebUtil.getDecimalSeparators();
		}
		dutyRosterCalendarFilters.clear();
		Collection<String> filters = getUserDutyRosterCalendarFilters();
		if (filters != null) {
			dutyRosterCalendarFilters.addAll(filters);
		}
		inventoryBookingCalendarFilters.clear();
		filters = getUserInventoryBookingCalendarFilters();
		if (filters != null) {
			inventoryBookingCalendarFilters.addAll(filters);
		}
		loadInheritedPropertiesMap();
		clearInheritedPropertiesTooltips();
		loadTableColumnCount();
		initSpecificSets();
	}

	private void loadTableColumnCount() {
		tableColumnCount = 0l;
		if (getUserId() != null) {
			try {
				tableColumnCount = WebUtil.getServiceLocator().getUserService().getDataTableColumnCount(WebUtil.getAuthentication(),
						getUserId(), null, null);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
	}

	private void loadTimeZone() {
		this.timeZone = WebUtil.getTimeZone(getUserTimeZone());
	}

	private void setInheritedProperties(Collection<String> inheritedProperties) {
		inheritedProperties.clear();
		if (inheritedProperties != null && getParentId() != null) {
			Iterator<Entry<String, String>> it = inheritedPropertiesMap.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, String> inheritedProperty = it.next();
				if (Boolean.parseBoolean(inheritedProperty.getValue())) {
					inheritedProperties.add(inheritedProperty.getKey());
				}
			}
		}
	}

	protected void sanitizeInVals() {
		setInheritedProperties(getInheritedProperties());
		if (timeZone != null) {
			setUserTimeZone(timeZone.getTimeZoneID());
		} else {
			setUserTimeZone(null);
		}
		if (CommonUtil.NO_SELECTION_VALUE.equals(getDateFormat())) {
			setDateFormat(null);
		}
		if (CommonUtil.NO_SELECTION_VALUE.equals(getDecimalSeparator())) {
			setDecimalSeparator(null);
		}
		setUserDutyRosterCalendarFilters(dutyRosterCalendarFilters);
		setUserInventoryBookingCalendarFilters(inventoryBookingCalendarFilters);
	}

	public void setTimeZone(TimeZoneVO timeZone) {
		this.timeZone = timeZone;
	}

	public final void clearTableColumns() {
		actionPostProcess(clearTableColumnsAction());
	}

	public String clearTableColumnsAction() {
		try {
			WebUtil.getServiceLocator().getUserService().clearDataTableColumns(WebUtil.getAuthentication(), getUserId(), null, null);
			loadTableColumnCount();
			addOperationSuccessMessage(MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
			return UPDATE_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	public long getTableColumnCount() {
		return tableColumnCount;
	}

	public List<TimeZoneVO> completeTimeZone(String query) {
		try {
			return (List<TimeZoneVO>) WebUtil.getServiceLocator().getToolsService().completeTimeZone(WebUtil.getAuthentication(), query, null);
		} catch (ServiceException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
		} catch (IllegalArgumentException e) {
		}
		return new ArrayList<TimeZoneVO>();
	}

	protected abstract Long getUserId();

	protected abstract Long getParentId();

	protected abstract Collection<String> getInheritedProperties();

	protected abstract Collection<String> getUserDutyRosterCalendarFilters();

	protected abstract void setUserDutyRosterCalendarFilters(Collection<String> filters);

	protected abstract Collection<String> getUserInventoryBookingCalendarFilters();

	protected abstract void setUserInventoryBookingCalendarFilters(Collection<String> filters);

	protected abstract String getUserTimeZone();

	public abstract String getLocale();

	public abstract String getDateFormat();

	public abstract String getDecimalSeparator();

	public abstract String getTheme();

	public abstract String getTabOrientation();

	public abstract boolean isShowTooltips();

	protected abstract void setUserTimeZone(String timeZoneID);

	public abstract void setLocale(String locale);

	public abstract void setDateFormat(String dateFormat);

	public abstract void setDecimalSeparator(String decimalSeparator);

	public abstract void setTheme(String theme);

	public abstract void setTabOrientation(String tabOrientation);

	public abstract void setShowTooltips(boolean showTooltips);

	private void loadInheritedPropertiesMap() {
		setInheritedPropertiesMap(true, getInheritedProperties());
	}

	protected void setInheritedPropertiesMap(boolean inherit, Collection<String> inheritedProperties) {
		inheritedPropertiesMap.clear();
		if (inherit && inheritedProperties != null) {
			Iterator<String> it = inheritedProperties.iterator();
			while (it.hasNext()) {
				inheritedPropertiesMap.put(it.next(), Boolean.TRUE.toString());
			}
		}
	}

	public void clearInheritedPropertiesTooltips() {
		inheritedPropertiesTooltipsCache.clear();
		parent = WebUtil.getInheritedUser(getParentId());
	}

	private static String visibleTabListToString(Map tabTitles, List<String> visibleTabList, String separator) {
		StringBuilder sb = new StringBuilder();
		if (visibleTabList != null) {
			Iterator<String> it = visibleTabList.iterator();
			while (it.hasNext()) {
				if (sb.length() > 0) {
					sb.append(separator);
				}
				sb.append(tabTitles.get(it.next()));
			}
		}
		return sb.toString();
	}

	public String getInheritedPropertyTooltip(String propertyName) {
		if (!inheritedPropertiesTooltipsCache.containsKey(propertyName)) {
			Object inheritedValue = null;
			try {
				inheritedValue = CommonUtil.getPropertyGetter(parent.getClass(), propertyName).invoke(parent);
				if (inheritedValue != null) {
					if (inheritedValue instanceof Boolean) {
						if ((boolean) inheritedValue) {
							inheritedValue = Messages.getString(MessageCodes.CHECKBOX_CHECKED);
						} else {
							inheritedValue = Messages.getString(MessageCodes.CHECKBOX_UNCHECKED);
						}
					} else if (inheritedValue instanceof Collection) {
						inheritedValue = String.join(", ", (Collection) inheritedValue);
					} else if ("visibleInventoryTabList".equals(propertyName)) {
						inheritedValue = visibleTabListToString(WebUtil.getSessionScopeBean().getInventoryTabTitles(), visibleTabListToList((String) inheritedValue), ", ");
					} else if ("visibleStaffTabList".equals(propertyName)) {
						inheritedValue = visibleTabListToString(WebUtil.getSessionScopeBean().getStaffTabTitles(), visibleTabListToList((String) inheritedValue), ", ");
					} else if ("visibleCourseTabList".equals(propertyName)) {
						inheritedValue = visibleTabListToString(WebUtil.getSessionScopeBean().getCourseTabTitles(), visibleTabListToList((String) inheritedValue), ", ");
					} else if ("visibleTrialTabList".equals(propertyName)) {
						inheritedValue = visibleTabListToString(WebUtil.getSessionScopeBean().getTrialTabTitles(), visibleTabListToList((String) inheritedValue), ", ");
					} else if ("visibleInputFieldTabList".equals(propertyName)) {
						inheritedValue = visibleTabListToString(WebUtil.getSessionScopeBean().getInputFieldTabTitles(), visibleTabListToList((String) inheritedValue),
								", ");
					} else if ("visibleProbandTabList".equals(propertyName)) {
						inheritedValue = visibleTabListToString(WebUtil.getSessionScopeBean().getProbandTabTitles(), visibleTabListToList((String) inheritedValue), ", ");
					} else if ("visibleMassMailTabList".equals(propertyName)) {
						inheritedValue = visibleTabListToString(WebUtil.getSessionScopeBean().getMassMailTabTitles(), visibleTabListToList((String) inheritedValue), ", ");
					} else if ("visibleUserTabList".equals(propertyName)) {
						inheritedValue = visibleTabListToString(WebUtil.getSessionScopeBean().getUserTabTitles(), visibleTabListToList((String) inheritedValue), ", ");
					} else if ("locale".equals(propertyName)) {
						inheritedValue = WebUtil.getLocale((String) inheritedValue).getName();
					} else if ("timeZone".equals(propertyName)) {
						inheritedValue = WebUtil.getTimeZone((String) inheritedValue).getName();
					} else if ("theme".equals(propertyName)) {
						inheritedValue = Settings.getThemes().get(inheritedValue);
					} else if ("tabOrientation".equals(propertyName)) {
						inheritedValue = Labels.getStringLocalized(inheritedValue + "_label");
					}
				}
			} catch (Exception e) {
			}
			if (CommonUtil.isEmptyString((String) inheritedValue)) {
				inheritedPropertiesTooltipsCache.put(propertyName, Messages.getString(MessageCodes.INHERIT_USER_PROPERTY_TOOLTIP));
			} else {
				inheritedPropertiesTooltipsCache.put(propertyName,
						Messages.getMessage(MessageCodes.INHERITED_USER_PROPERTY_TOOLTIP,
								CommonUtil.clipString((String) inheritedValue,
										Settings.getInt(SettingCodes.TAB_TITLE_LIST_CLIP_MAX_LENGTH, Bundle.SETTINGS, DefaultSettings.TAB_TITLE_LIST_CLIP_MAX_LENGTH),
										CommonUtil.DEFAULT_ELLIPSIS, EllipsisPlacement.MID)));
			}
		}
		return inheritedPropertiesTooltipsCache.get(propertyName);
	}

	protected static List<String> visibleTabListToList(String tabList) {
		ArrayList<String> result = new ArrayList<String>();
		if (tabList != null && tabList.length() > 0) {
			String[] tabIds = WebUtil.TAB_ID_SEPARATOR_REGEXP.split(tabList, -1);
			for (int i = 0; i < tabIds.length; i++) {
				if (tabIds[i].trim().length() > 0) {
					result.add(tabIds[i].trim());
				}
			}
		}
		return result;
	}

	public List<String> getParentInventoryVisibleTabList() {
		return visibleTabListToList(parent != null ? parent.getVisibleInventoryTabList() : null);
	}

	public List<String> getParentStaffVisibleTabList() {
		return visibleTabListToList(parent != null ? parent.getVisibleStaffTabList() : null);
	}

	public List<String> getParentCourseVisibleTabList() {
		return visibleTabListToList(parent != null ? parent.getVisibleCourseTabList() : null);
	}

	public List<String> getParentTrialVisibleTabList() {
		return visibleTabListToList(parent != null ? parent.getVisibleTrialTabList() : null);
	}

	public List<String> getParentInputFieldVisibleTabList() {
		return visibleTabListToList(parent != null ? parent.getVisibleInputFieldTabList() : null);
	}

	public List<String> getParentProbandVisibleTabList() {
		return visibleTabListToList(parent != null ? parent.getVisibleProbandTabList() : null);
	}

	public List<String> getParentMassMailVisibleTabList() {
		return visibleTabListToList(parent != null ? parent.getVisibleMassMailTabList() : null);
	}

	public List<String> getParentUserVisibleTabList() {
		return visibleTabListToList(parent != null ? parent.getVisibleUserTabList() : null);
	}

	public List<String> getDutyRosterCalendarFilters() {
		List<String> filters = new ArrayList<String>();
		filters.addAll(dutyRosterCalendarFilters);
		return filters;
	}
	//	public void handleDutyRosterCalendarFilterSelect(SelectEvent event) {
	//	}
	//
	//	public void handleDutyRosterCalendarFilterUnselect(UnselectEvent event) {
	//	}

	public void setDutyRosterCalendarFilters(List<String> filters) {
		dutyRosterCalendarFilters.clear();
		if (filters != null) {
			dutyRosterCalendarFilters.addAll(filters);
		}
	}

	public List<String> completeDutyRosterCalendar(String query) {
		Collection<String> calendars = null;
		try {
			calendars = WebUtil.getServiceLocator().getTrialService().getCalendars(WebUtil.getAuthentication(),
					null, null, null, query, null); // let permission argument override decide...
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
		if (calendars != null) {
			try {
				return ((List<String>) calendars);
			} catch (ClassCastException e) {
			}
		}
		return new ArrayList<String>();
	}

	public List<String> getInventoryBookingCalendarFilters() {
		List<String> filters = new ArrayList<String>();
		filters.addAll(inventoryBookingCalendarFilters);
		return filters;
	}
	//	public void handleInventoryBookingCalendarFilterSelect(SelectEvent event) {
	//	}
	//
	//	public void handleInventoryBookingCalendarFilterUnselect(UnselectEvent event) {
	//	}

	public void setInventoryBookingCalendarFilters(List<String> filters) {
		inventoryBookingCalendarFilters.clear();
		if (filters != null) {
			inventoryBookingCalendarFilters.addAll(filters);
		}
	}

	public List<String> completeInventoryBookingCalendar(String query) {
		Collection<String> calendars = null;
		try {
			calendars = WebUtil.getServiceLocator().getInventoryService().getCalendars(WebUtil.getAuthentication(),
					null, null, null, null, null, query, null); // let permission argument override decide...
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
		if (calendars != null) {
			try {
				return ((List<String>) calendars);
			} catch (ClassCastException e) {
			}
		}
		return new ArrayList<String>();
	}
}
