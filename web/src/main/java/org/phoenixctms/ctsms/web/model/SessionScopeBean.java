package org.phoenixctms.ctsms.web.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.js.JsUtil;
import org.phoenixctms.ctsms.util.AuthenticationExceptionCodes;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CommonUtil.EllipsisPlacement;
import org.phoenixctms.ctsms.util.MaxSizeHashMap;
import org.phoenixctms.ctsms.vo.AnnouncementVO;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.LocaleVO;
import org.phoenixctms.ctsms.vo.PasswordOutVO;
import org.phoenixctms.ctsms.vo.PasswordPolicyVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.TimeZoneVO;
import org.phoenixctms.ctsms.vo.UserOutVO;
import org.phoenixctms.ctsms.web.util.DateUtil;
import org.phoenixctms.ctsms.web.util.DateUtil.DurationUnitOfTime;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.GetParamNames;
import org.phoenixctms.ctsms.web.util.JSValues;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.Urls;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.component.submenu.Submenu;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultMenuModel;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.MenuModel;
import org.primefaces.model.StreamedContent;

@ManagedBean
@SessionScoped
public class SessionScopeBean {

	private static final String MENUITEM_CHECKED_STYLECLASS = "ui-icon ui-icon-check";

	private static AnnouncementVO getAnnouncement() {
		try {
			return WebUtil.getServiceLocator().getToolsService().getAnnouncement();
		} catch (ServiceException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
		} catch (IllegalArgumentException e) {
		}
		return null;
	}

	private static Collection<LocaleVO> getLocales() {
		Collection<LocaleVO> locales = null;
		try {
			locales = WebUtil.getServiceLocator().getSelectionSetService().getLocales(WebUtil.getAuthentication());
		} catch (ServiceException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
		} catch (IllegalArgumentException e) {
		}
		if (locales == null) {
			locales = new ArrayList<LocaleVO>();
		}
		return locales;
	}

	private static String getLoginOutcome(boolean success) {
		String refererBase64 = WebUtil.getParamValue(GetParamNames.REFERER);
		String referer = JsUtil.decodeBase64(refererBase64);
		HttpServletRequest request = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest());
		StringBuilder viewId;
		if (success) {
			if (WebUtil.isTrustedReferer(referer, request)) {
				try {
					viewId = new StringBuilder((new URL(referer)).getFile().substring(request.getContextPath().length()));
					if (viewId.toString().contains("?")) {
						viewId.append("&");
					} else {
						viewId.append("?");
					}
				} catch (MalformedURLException e) {
					viewId = new StringBuilder(Urls.PORTAL.value());
					viewId.append("?");
				}
			} else {
				viewId = new StringBuilder(Urls.PORTAL.value()); // + "?faces-redirect=true";
				viewId.append("?");
			}
		} else {
			viewId = new StringBuilder(Urls.LOGIN.value());
			if (WebUtil.isTrustedReferer(referer, request)) {
				viewId.append("?");
				viewId.append(GetParamNames.REFERER);
				viewId.append("=");
				viewId.append(refererBase64);
				viewId.append("&");
			} else {
				viewId.append("?");
			}
		}
		viewId.append("faces-redirect=true&includeViewParams=true");
		return viewId.toString();
	}

	public static Collection<TimeZoneVO> getTimeZones() {
		Collection<TimeZoneVO> timeZones = null;
		try {
			timeZones = WebUtil.getServiceLocator().getSelectionSetService().getTimeZones(WebUtil.getAuthentication());
		} catch (ServiceException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
		} catch (IllegalArgumentException e) {
		}
		if (timeZones == null) {
			timeZones = new ArrayList<TimeZoneVO>();
		}
		return timeZones;
	}

	private HashMap<String, HashMap<String, Map<String, String>>> filterMaps;
	private MaxSizeHashMap<Object, StreamedContent> imageStore;
	// private HashMap<Class, HashMap<Object, Object>> selectionSetServiceCache;
	private ArrayList<SelectItem> filterBooleans;
	private ArrayList<SelectItem> filterInverseBooleans;
	private ArrayList<SelectItem> filterInputFieldTypes;
	private ArrayList<SelectItem> filterSexes;
	private ArrayList<SelectItem> filterRandomizationModes;
	private ArrayList<SelectItem> filterVariablePeriods;
	private ArrayList<SelectItem> filterEventImportances;
	private ArrayList<SelectItem> filterPaymentMethods;
	private ArrayList<SelectItem> filterDepartments;
	private ArrayList<SelectItem> filterInventoryCategories;
	private ArrayList<SelectItem> filterStaffCategories;
	private ArrayList<SelectItem> filterCourseCategories;
	private ArrayList<SelectItem> filterProbandCategories;
	private ArrayList<SelectItem> filterCourseParticipationStatusTypes;
	private ArrayList<SelectItem> filterTrialStatusTypes;
	private ArrayList<SelectItem> filterTrialTypes;
	private ArrayList<SelectItem> filterSponsoringTypes;
	private ArrayList<SelectItem> filterSurveyStatusTypes;
	private ArrayList<SelectItem> filterCvSections;
	private ArrayList<SelectItem> filterContactDetailTypes;
	private ArrayList<SelectItem> filterLecturerCompetences;
	private ArrayList<SelectItem> filterTeamMemberRoles;
	private ArrayList<SelectItem> filterInventoryStatusTypes;
	private ArrayList<SelectItem> filterStaffStatusTypes;
	private ArrayList<SelectItem> filterProbandStatusTypes;
	private ArrayList<SelectItem> filterMaintenanceTypes;
	private ArrayList<SelectItem> filterTimelineEventTypes;
	private ArrayList<SelectItem> filterPersonProbandListStatusTypes;
	private ArrayList<SelectItem> filterAnimalProbandListStatusTypes;
	private ArrayList<SelectItem> filterProbandListStatusTypes;
	private ArrayList<SelectItem> filterVisitTypes;
	private ArrayList<SelectItem> filterPrivacyConsentStatusTypes;
	private ArrayList<SelectItem> filterAuthenticationTypes;
	private ArrayList<SelectItem> filterEcrfFieldStatusTypes;
	private ArrayList<SelectItem> filterMassMailStatusTypes;
	private ArrayList<SelectItem> filterMassMailTypes;
	private ArrayList<SelectItem> filterLocales;
	private PasswordPolicyVO policy;
	// http://stackoverflow.com/questions/3841361/jsf-http-session-login
	private AuthenticationVO auth;
	private PasswordOutVO logon;
	private String newPassword;
	private String oldPassword;
	private int failedAttempts;
	private boolean authenticationFailed;
	private boolean localPasswordRequired;
	private String authenticationFailedMessage;
	private MenuModel userMenuModel;

	public SessionScopeBean() {
		auth = new AuthenticationVO();
		imageStore = new MaxSizeHashMap<Object, StreamedContent>(WebUtil.IMAGE_STORE_MAX_SIZE);
		// selectionSetServiceCache = new HashMap<Class, HashMap<Object, Object>>();
		logon = null;
		failedAttempts = 0;
		authenticationFailed = false;
		localPasswordRequired = false;
		authenticationFailedMessage = null;
	}

	public synchronized void changePassword() {
		try {
			logon = WebUtil.getServiceLocator().getUserService().setPassword(auth, newPassword, oldPassword);
			auth.setPassword(newPassword);
			initSets();
			logout(JsUtil.encodeBase64(WebUtil.createViewUrl(Urls.USER, false, GetParamNames.USER_ID, logon.getUser().getId()), true));
		} catch (ServiceException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}

	private void clearauthenticationFailedMessage() {
		authenticationFailed = false;
		localPasswordRequired = false;
		authenticationFailedMessage = null;
	}

	public synchronized AuthenticationVO getAuthentication() {
		return auth;
	}

	public synchronized String getAuthenticationFailedMessage() {
		// i know, hack again....
		String authenticationFailedMessage = this.authenticationFailedMessage;
		clearauthenticationFailedMessage();
		return authenticationFailedMessage;
	}

	public synchronized MenuModel getCourseEntityMenuModel() {
		return DynamicEntityMenu.getCourseEntityMenu().createMenuModel(this,
				Settings.getInt(SettingCodes.MAX_RECENT_ENTITIES, Bundle.SETTINGS, DefaultSettings.MAX_RECENT_ENTITIES));
	}

	public synchronized MenuModel getCourseHomeMenuModel() {
		return DynamicHomeMenu.getCourseHomeMenu().createMenuModel(this, Settings.getInt(SettingCodes.MAX_RECENT_ENTITIES, Bundle.SETTINGS, DefaultSettings.MAX_RECENT_ENTITIES));
	}

	public synchronized ArrayList<SelectItem> getFilterAnimalProbandListStatusTypes() {
		if (filterAnimalProbandListStatusTypes == null) {
			filterAnimalProbandListStatusTypes = WebUtil.getAllProbandListStatusTypes(false);
			filterAnimalProbandListStatusTypes.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		}
		return filterAnimalProbandListStatusTypes;
	}

	public synchronized ArrayList<SelectItem> getFilterAuthenticationTypes() {
		if (filterAuthenticationTypes == null) {
			filterAuthenticationTypes = WebUtil.getAuthenticationTypes();
			filterAuthenticationTypes.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		}
		return filterAuthenticationTypes;
	}

	public synchronized ArrayList<SelectItem> getFilterBooleans() {
		if (filterBooleans == null) {
			filterBooleans = WebUtil.getBooleans(false, false);
			filterBooleans.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		}
		return filterBooleans;
	}

	private HashMap<String, Map<String, String>> getFilterComponentMap() {
		if (filterMaps == null) {
			filterMaps = new HashMap<String, HashMap<String, Map<String, String>>>();
		}
		String viewMapId = WebUtil.getViewMapId();
		HashMap<String, Map<String, String>> componentMap;
		if (filterMaps.containsKey(viewMapId)) {
			componentMap = filterMaps.get(viewMapId);
		} else {
			componentMap = new HashMap<String, Map<String, String>>();
			filterMaps.put(viewMapId, componentMap);
		}
		return componentMap;
	}

	public synchronized ArrayList<SelectItem> getFilterContactDetailTypes() {
		if (filterContactDetailTypes == null) {
			filterContactDetailTypes = WebUtil.getAllContactDetailTypes();
			filterContactDetailTypes.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		}
		return filterContactDetailTypes;
	}

	public synchronized ArrayList<SelectItem> getFilterCourseCategories() {
		if (filterCourseCategories == null) {
			filterCourseCategories = WebUtil.getAllCourseCategories();
			filterCourseCategories.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		}
		return filterCourseCategories;
	}

	public synchronized ArrayList<SelectItem> getFilterCourseParticipationStatusTypes() {
		if (filterCourseParticipationStatusTypes == null) {
			filterCourseParticipationStatusTypes = WebUtil.getAllCourseParticipationStatusTypes();
			filterCourseParticipationStatusTypes.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		}
		return filterCourseParticipationStatusTypes;
	}

	public synchronized ArrayList<SelectItem> getFilterCvSections() {
		if (filterCvSections == null) {
			filterCvSections = WebUtil.getAllCvSections();
			filterCvSections.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		}
		return filterCvSections;
	}

	public synchronized ArrayList<SelectItem> getFilterDepartments() {
		if (filterDepartments == null) {
			filterDepartments = WebUtil.getAllDepartments();
			filterDepartments.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		}
		return filterDepartments;
	}

	public synchronized ArrayList<SelectItem> getFilterEcrfFieldStatusTypes() {
		if (filterEcrfFieldStatusTypes == null) {
			filterEcrfFieldStatusTypes = WebUtil.getAllEcrfFieldStatusTypes();
			filterEcrfFieldStatusTypes.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		}
		return filterEcrfFieldStatusTypes;
	}

	public synchronized ArrayList<SelectItem> getFilterEventImportances() {
		if (filterEventImportances == null) {
			filterEventImportances = WebUtil.getEventImportances();
			filterEventImportances.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		}
		return filterEventImportances;
	}

	public synchronized ArrayList<SelectItem> getFilterInputFieldTypes() {
		if (filterInputFieldTypes == null) {
			filterInputFieldTypes = WebUtil.getInputFieldTypes();
			filterInputFieldTypes.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		}
		return filterInputFieldTypes;
	}

	public synchronized ArrayList<SelectItem> getFilterInventoryCategories() {
		if (filterInventoryCategories == null) {
			filterInventoryCategories = WebUtil.getAllInventoryCategories();
			filterInventoryCategories.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		}
		return filterInventoryCategories;
	}

	public synchronized ArrayList<SelectItem> getFilterInventoryStatusTypes() {
		if (filterInventoryStatusTypes == null) {
			filterInventoryStatusTypes = WebUtil.getAllInventoryStatusTypes();
			filterInventoryStatusTypes.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		}
		return filterInventoryStatusTypes;
	}

	public synchronized ArrayList<SelectItem> getFilterInverseBooleans() {
		if (filterInverseBooleans == null) {
			filterInverseBooleans = WebUtil.getBooleans(true, false);
			filterInverseBooleans.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		}
		return filterInverseBooleans;
	}

	public synchronized ArrayList<SelectItem> getFilterLecturerCompetences() {
		if (filterLecturerCompetences == null) {
			filterLecturerCompetences = WebUtil.getAllLecturerCompetences();
			filterLecturerCompetences.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		}
		return filterLecturerCompetences;
	}

	public synchronized ArrayList<SelectItem> getFilterLocales() {
		if (filterLocales == null) {
			filterLocales = WebUtil.getLocales();
			filterLocales.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		}
		return filterLocales;
	}

	public synchronized ArrayList<SelectItem> getFilterMaintenanceTypes() {
		if (filterMaintenanceTypes == null) {
			filterMaintenanceTypes = WebUtil.getAllMaintenanceTypes();
			filterMaintenanceTypes.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		}
		return filterMaintenanceTypes;
	}

	public synchronized Map<String, String> getFilterMap(String id) {
		HashMap<String, Map<String, String>> componentMap = getFilterComponentMap();
		Map<String, String> filterMap;
		if (componentMap.containsKey(id)) {
			filterMap = componentMap.get(id);
		} else {
			filterMap = new HashMap<String, String>();
			componentMap.put(id, filterMap);
		}
		return filterMap;
	}

	public synchronized ArrayList<SelectItem> getFilterMassMailStatusTypes() {
		if (filterMassMailStatusTypes == null) {
			filterMassMailStatusTypes = WebUtil.getAllMassMailStatusTypes();
			filterMassMailStatusTypes.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		}
		return filterMassMailStatusTypes;
	}

	public synchronized ArrayList<SelectItem> getFilterMassMailTypes() {
		if (filterMassMailTypes == null) {
			filterMassMailTypes = WebUtil.getAllMassMailTypes();
			filterMassMailTypes.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		}
		return filterMassMailTypes;
	}

	public synchronized ArrayList<SelectItem> getFilterPaymentMethods() {
		if (filterPaymentMethods == null) {
			filterPaymentMethods = WebUtil.getPaymentMethods();
			filterPaymentMethods.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		}
		return filterPaymentMethods;
	}

	public synchronized ArrayList<SelectItem> getFilterPersonProbandListStatusTypes() {
		if (filterPersonProbandListStatusTypes == null) {
			filterPersonProbandListStatusTypes = WebUtil.getAllProbandListStatusTypes(true);
			filterPersonProbandListStatusTypes.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		}
		return filterPersonProbandListStatusTypes;
	}

	public synchronized ArrayList<SelectItem> getFilterPrivacyConsentStatusTypes() {
		if (filterPrivacyConsentStatusTypes == null) {
			filterPrivacyConsentStatusTypes = WebUtil.getAllPrivacyConsentStatusTypes();
			filterPrivacyConsentStatusTypes.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		}
		return filterPrivacyConsentStatusTypes;
	}

	public synchronized ArrayList<SelectItem> getFilterProbandCategories() {
		if (filterProbandCategories == null) {
			filterProbandCategories = WebUtil.getAllProbandCategories();
			filterProbandCategories.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		}
		return filterProbandCategories;
	}

	public synchronized ArrayList<SelectItem> getFilterProbandListStatusTypes() {
		if (filterProbandListStatusTypes == null) {
			filterProbandListStatusTypes = WebUtil.getAllProbandListStatusTypes(null);
			filterProbandListStatusTypes.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		}
		return filterProbandListStatusTypes;
	}

	public synchronized ArrayList<SelectItem> getFilterProbandStatusTypes() {
		if (filterProbandStatusTypes == null) {
			filterProbandStatusTypes = WebUtil.getAllProbandStatusTypes();
			filterProbandStatusTypes.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		}
		return filterProbandStatusTypes;
	}

	public synchronized ArrayList<SelectItem> getFilterRandomizationModes() {
		if (filterRandomizationModes == null) {
			filterRandomizationModes = WebUtil.getRandomizationModes();
			filterRandomizationModes.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		}
		return filterRandomizationModes;
	}

	public synchronized ArrayList<SelectItem> getFilterSexes() {
		if (filterSexes == null) {
			filterSexes = WebUtil.getSexes();
			filterSexes.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		}
		return filterSexes;
	}

	public synchronized ArrayList<SelectItem> getFilterSponsoringTypes() {
		if (filterSponsoringTypes == null) {
			filterSponsoringTypes = WebUtil.getAllSponsoringTypes();
			filterSponsoringTypes.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		}
		return filterSponsoringTypes;
	}

	public synchronized ArrayList<SelectItem> getFilterStaffCategories() {
		if (filterStaffCategories == null) {
			filterStaffCategories = WebUtil.getAllStaffCategories();
			filterStaffCategories.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		}
		return filterStaffCategories;
	}

	public synchronized ArrayList<SelectItem> getFilterStaffStatusTypes() {
		if (filterStaffStatusTypes == null) {
			filterStaffStatusTypes = WebUtil.getAllStaffStatusTypes();
			filterStaffStatusTypes.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		}
		return filterStaffStatusTypes;
	}

	public synchronized ArrayList<SelectItem> getFilterSurveyStatusTypes() {
		if (filterSurveyStatusTypes == null) {
			filterSurveyStatusTypes = WebUtil.getAllSurveyStatusTypes();
			filterSurveyStatusTypes.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		}
		return filterSurveyStatusTypes;
	}

	public synchronized ArrayList<SelectItem> getFilterTeamMemberRoles() {
		if (filterTeamMemberRoles == null) {
			filterTeamMemberRoles = WebUtil.getAllTeamMemberRoles();
			filterTeamMemberRoles.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		}
		return filterTeamMemberRoles;
	}

	public synchronized ArrayList<SelectItem> getFilterTimelineEventTypes() {
		if (filterTimelineEventTypes == null) {
			filterTimelineEventTypes = WebUtil.getAllTimelineEventTypes();
			filterTimelineEventTypes.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		}
		return filterTimelineEventTypes;
	}

	public synchronized ArrayList<SelectItem> getFilterTrialStatusTypes() {
		if (filterTrialStatusTypes == null) {
			filterTrialStatusTypes = WebUtil.getAllTrialStatusTypes();
			filterTrialStatusTypes.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		}
		return filterTrialStatusTypes;
	}

	public synchronized ArrayList<SelectItem> getFilterTrialTypes() {
		if (filterTrialTypes == null) {
			filterTrialTypes = WebUtil.getAllTrialTypes();
			filterTrialTypes.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		}
		return filterTrialTypes;
	}

	public synchronized ArrayList<SelectItem> getFilterVariablePeriods() {
		if (filterVariablePeriods == null) {
			filterVariablePeriods = WebUtil.getVariablePeriods();
			filterVariablePeriods.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		}
		return filterVariablePeriods;
	}

	public synchronized ArrayList<SelectItem> getFilterVisitTypes() {
		if (filterVisitTypes == null) {
			filterVisitTypes = WebUtil.getAllVisitTypes();
			filterVisitTypes.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		}
		return filterVisitTypes;
	}

	public synchronized boolean getHasUserIdentity() {
		if (logon != null) {
			return logon.getUser().getIdentity() != null;
		}
		return false;
	}

	public synchronized StreamedContent getImage() {
		FacesContext context = FacesContext.getCurrentInstance();
		if (PhaseId.RENDER_RESPONSE.equals(context.getCurrentPhaseId())) {
			// So, we're rendering the view. Return a stub StreamedContent so that it will generate right URL.
			return new DefaultStreamedContent();
		} else {
			// So, browser is requesting the image. Return a real StreamedContent with the image bytes.
			String uuid = WebUtil.getParamValue(GetParamNames.UUID);
			if (imageStore.containsKey(uuid)) {
				return imageStore.get(uuid);
			}
			return new DefaultStreamedContent();
		}
	}

	public synchronized String getInputDatePattern() {
		return CommonUtil.getInputDatePattern(logon != null ? logon.getUser().getDateFormat() : null);
	}

	public synchronized String getInputDateTimePattern() {
		return CommonUtil.getInputDateTimePattern(logon != null ? logon.getUser().getDateFormat() : null);
	}

	public synchronized MenuModel getInputFieldEntityMenuModel() {
		return DynamicEntityMenu.getInputFieldEntityMenu().createMenuModel(this,
				Settings.getInt(SettingCodes.MAX_RECENT_ENTITIES, Bundle.SETTINGS, DefaultSettings.MAX_RECENT_ENTITIES));
	}

	public synchronized MenuModel getInputFieldHomeMenuModel() {
		return DynamicHomeMenu.getInputFieldHomeMenu().createMenuModel(this,
				Settings.getInt(SettingCodes.MAX_RECENT_ENTITIES, Bundle.SETTINGS, DefaultSettings.MAX_RECENT_ENTITIES));
	}

	public synchronized MenuModel getInventoryEntityMenuModel() {
		return DynamicEntityMenu.getInventoryEntityMenu().createMenuModel(this,
				Settings.getInt(SettingCodes.MAX_RECENT_ENTITIES, Bundle.SETTINGS, DefaultSettings.MAX_RECENT_ENTITIES));
	}

	// public String getInputTimePattern() {
	// return CommonUtil.INPUT_TIME_PATTERN;
	// }
	public synchronized MenuModel getInventoryHomeMenuModel() {
		return DynamicHomeMenu.getInventoryHomeMenu()
				.createMenuModel(this, Settings.getInt(SettingCodes.MAX_RECENT_ENTITIES, Bundle.SETTINGS, DefaultSettings.MAX_RECENT_ENTITIES));
	}

	public synchronized Locale getLocale() {
		if (logon != null) {
			return CommonUtil.localeFromString(logon.getUser().getLocale());
		}
		return Locale.getDefault(); // defaultLocale;
	}

	public synchronized String getLocalPassword() {
		return auth.getLocalPassword();
	}

	public synchronized PasswordOutVO getLogon() {
		return logon;
	}

	public synchronized MenuModel getMassMailEntityMenuModel() {
		return DynamicEntityMenu.getMassMailEntityMenu().createMenuModel(this,
				Settings.getInt(SettingCodes.MAX_RECENT_ENTITIES, Bundle.SETTINGS, DefaultSettings.MAX_RECENT_ENTITIES));
	}

	public synchronized MenuModel getMassMailHomeMenuModel() {
		return DynamicHomeMenu.getMassMailHomeMenu().createMenuModel(this,
				Settings.getInt(SettingCodes.MAX_RECENT_ENTITIES, Bundle.SETTINGS, DefaultSettings.MAX_RECENT_ENTITIES));
	}

	public synchronized String getNewPassword() {
		return newPassword;
	}

	public synchronized String getOldPassword() {
		return oldPassword;
	}

	public synchronized String getPassword() {
		return auth.getPassword();
	}

	public synchronized PasswordPolicyVO getPasswordPolicy() {
		if (policy == null) {
			try {
				policy = WebUtil.getServiceLocator().getToolsService().getPasswordPolicy(WebUtil.getAuthentication());
			} catch (ServiceException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			} catch (AuthorisationException e) {
			} catch (IllegalArgumentException e) {
			}
			if (policy == null) {
				policy = new PasswordPolicyVO();
			}
		}
		return policy;
	}

	public synchronized MenuModel getProbandEntityMenuModel() {
		return DynamicEntityMenu.getProbandEntityMenu().createMenuModel(this,
				Settings.getInt(SettingCodes.MAX_RECENT_ENTITIES, Bundle.SETTINGS, DefaultSettings.MAX_RECENT_ENTITIES));
	}

	public synchronized MenuModel getProbandHomeMenuModel() {
		return DynamicHomeMenu.getProbandHomeMenu().createMenuModel(this, Settings.getInt(SettingCodes.MAX_RECENT_ENTITIES, Bundle.SETTINGS, DefaultSettings.MAX_RECENT_ENTITIES));
	}

	public int getSessionTimeout() {
		FacesContext context = FacesContext.getCurrentInstance();
		return context.getExternalContext().getSessionMaxInactiveInterval();
	}

	public synchronized MenuModel getStaffEntityMenuModel() {
		return DynamicEntityMenu.getStaffEntityMenu()
				.createMenuModel(this, Settings.getInt(SettingCodes.MAX_RECENT_ENTITIES, Bundle.SETTINGS, DefaultSettings.MAX_RECENT_ENTITIES));
	}

	// public Object getSelectionSetServiceCache(Class type, Object key) {
	// if (selectionSetServiceCache != null && type != null) {
	// if (selectionSetServiceCache.containsKey(type)) {
	// HashMap<Object, Object> cache = selectionSetServiceCache.get(type);
	// if (cache != null && cache.containsKey(key)) {
	// return cache.get(key);
	// }
	// }
	// }
	// return null;
	// }
	public synchronized MenuModel getStaffHomeMenuModel() {
		return DynamicHomeMenu.getStaffHomeMenu().createMenuModel(this, Settings.getInt(SettingCodes.MAX_RECENT_ENTITIES, Bundle.SETTINGS, DefaultSettings.MAX_RECENT_ENTITIES));
	}

	public synchronized String getStatusBarInfo() {
		StringBuilder result = new StringBuilder();
		if (logon != null) {
			AnnouncementVO announcementVO = getAnnouncement();
			boolean announcementShown = false;
			if (announcementVO != null) {
				result.append(announcementVO.getMessage());
				announcementShown = true;
			}
			boolean passwordExpiryShown = false;
			boolean logonLimitShown = false;
			if (isLocalAuthMethod()) {
				Boolean isPasswordExpired = WebUtil
						.isPasswordExpired(DateUtil.addDayMinuteDelta(new Date(),
								Settings.getInt(SettingCodes.EXPIRATION_INFO_DAYS, Bundle.SETTINGS, DefaultSettings.EXPIRATION_INFO_DAYS), 0), logon);
				if (isPasswordExpired != null && isPasswordExpired) {
					if (announcementShown) {
						result.append(Messages.getString(MessageCodes.ANNOUNCEMENT_INFO_SEPARATOR));
					}
					result.append(Messages.getMessage(MessageCodes.PASSWORD_EXPIRATION_INFO, DateUtil.getDateTimeFormat().format(logon.getExpiration())));
					passwordExpiryShown = true;
				}
				if (logon.getLimitLogons()) {
					long logonsLeft = logon.getMaxSuccessfulLogons() - logon.getSuccessfulLogons();
					if (logonsLeft <= Settings.getLong(SettingCodes.EXPIRATION_INFO_LOGONS_LEFT, Bundle.SETTINGS, DefaultSettings.EXPIRATION_INFO_LOGONS_LEFT) && logonsLeft >= 0) {
						if (announcementShown && !passwordExpiryShown) {
							result.append(Messages.getString(MessageCodes.ANNOUNCEMENT_INFO_SEPARATOR));
						} else if (passwordExpiryShown) {
							result.append(Messages.getString(MessageCodes.PASSWORD_EXPIRATION_INFO_SEPARATOR));
							result.append(" ");
						}
						result.append(Messages.getMessage(MessageCodes.PASSWORD_LOGONS_LEFT_INFO, logonsLeft));
						logonLimitShown = true;
					}
				}
			}
			if (passwordExpiryShown || logonLimitShown) {
				if (logon.getProlongable()) {
					result.append(Messages.getString(MessageCodes.PASSWORD_EXPIRATION_INFO_SEPARATOR));
					if (!announcementShown) {
						result.append(Messages.getString(MessageCodes.ANNOUNCEMENT_INFO_SEPARATOR));
					} else {
						result.append(" ");
					}
					result.append(Messages.getString(MessageCodes.PASSWORD_EXPIRATION_INFO_RENEWAL_ADVICE));
					// } else {
					// if (!announcementShown) {
					// result.append(Messages.getString(MessageCodes.ANNOUNCEMENT_INFO_SEPARATOR));
					// }
				}
			} else if (announcementShown) {
				result.append(Messages.getString(MessageCodes.ANNOUNCEMENT_INFO_SEPARATOR));
				result.append(Messages.getMessage(MessageCodes.ANNOUNCEMENT_TIMESTAMP_INFO, DateUtil.getDateTimeFormat().format(announcementVO.getTimestamp())));
			}
		}
		return result.toString();
	}

	public synchronized String getTheme() {
		String theme = null;
		if (logon != null) {
			theme = logon.getUser().getTheme();
		}
		if (theme != null && theme.length() > 0) {
			return theme;
		} else {
			return Settings.getString(SettingCodes.DEFAULT_THEME, Bundle.SETTINGS, DefaultSettings.THEME);
		}
	}

	public synchronized TimeZone getTimeZone() {
		if (logon != null) {
			return CommonUtil.timeZoneFromString(logon.getUser().getTimeZone());
		}
		return TimeZone.getDefault(); // defaultTimeZone;
	}

	public synchronized MenuModel getTrialEntityMenuModel() {
		return DynamicEntityMenu.getTrialEntityMenu()
				.createMenuModel(this, Settings.getInt(SettingCodes.MAX_RECENT_ENTITIES, Bundle.SETTINGS, DefaultSettings.MAX_RECENT_ENTITIES));
	}

	public synchronized MenuModel getTrialHomeMenuModel() {
		return DynamicHomeMenu.getTrialHomeMenu().createMenuModel(this, Settings.getInt(SettingCodes.MAX_RECENT_ENTITIES, Bundle.SETTINGS, DefaultSettings.MAX_RECENT_ENTITIES));
	}

	public synchronized UserOutVO getUser() {
		if (logon != null) {
			return logon.getUser();
		}
		return null;
	}

	public synchronized MenuModel getUserEntityMenuModel() {
		return DynamicEntityMenu.getUserEntityMenu().createMenuModel(this, Settings.getInt(SettingCodes.MAX_RECENT_ENTITIES, Bundle.SETTINGS, DefaultSettings.MAX_RECENT_ENTITIES));
	}

	public synchronized MenuModel getUserHomeMenuModel() {
		return DynamicHomeMenu.getUserHomeMenu().createMenuModel(this, Settings.getInt(SettingCodes.MAX_RECENT_ENTITIES, Bundle.SETTINGS, DefaultSettings.MAX_RECENT_ENTITIES));
	}

	public synchronized StaffOutVO getUserIdentity() {
		if (logon != null) {
			return logon.getUser().getIdentity();
		}
		return null;
	}

	public synchronized String getUserIdentityName() {
		UserOutVO user;
		if (logon != null && (user = logon.getUser()) != null) {
			StaffOutVO identity = user.getIdentity();
			if (identity != null) {
				return CommonUtil.staffOutVOToString(identity);
			}
		}
		return "";
	}

	public synchronized String getUserIdentityString() {
		if (logon != null) {
			return WebUtil.getUserIdentityString(logon.getUser());
		}
		return "";
	}

	public synchronized MenuModel getUserMenuModel() {
		return userMenuModel;
	}

	public synchronized String getUsername() {
		return auth.getUsername();
	}

	@PostConstruct
	private void init() {
		initSets();
	}

	private void initSets() {
		resetChangePasswordInputs();
		loadUserMenuModel();
		filterBooleans = null;
		filterInverseBooleans = null;
		filterInputFieldTypes = null;
		filterSexes = null;
		filterRandomizationModes = null;
		filterVariablePeriods = null;
		filterEventImportances = null;
		filterPaymentMethods = null;
		filterDepartments = null;
		filterInventoryCategories = null;
		filterStaffCategories = null;
		filterCourseCategories = null;
		filterProbandCategories = null;
		filterCourseParticipationStatusTypes = null;
		filterTrialStatusTypes = null;
		filterTrialTypes = null;
		filterSponsoringTypes = null;
		filterSurveyStatusTypes = null;
		filterCvSections = null;
		filterContactDetailTypes = null;
		filterLecturerCompetences = null;
		filterTeamMemberRoles = null;
		filterInventoryStatusTypes = null;
		filterStaffStatusTypes = null;
		filterProbandStatusTypes = null;
		filterMaintenanceTypes = null;
		filterTimelineEventTypes = null;
		filterPersonProbandListStatusTypes = null;
		filterAnimalProbandListStatusTypes = null;
		filterProbandListStatusTypes = null;
		filterVisitTypes = null;
		filterPrivacyConsentStatusTypes = null;
		filterAuthenticationTypes = null;
		filterEcrfFieldStatusTypes = null;
		filterMassMailStatusTypes = null;
		filterMassMailTypes = null;
		filterLocales = null;
		policy = null;
		// selectionSetServiceCache.clear();
	}

	public synchronized boolean isAuthenticationFailed() {
		return authenticationFailed;
	}

	public boolean isCaptchaRequired() {
		int logonAttemptsCaptchaRequiredLimit;
		if (WebUtil.isTrustedHost()) {
			logonAttemptsCaptchaRequiredLimit = Settings.getInt(SettingCodes.TRUSTED_HOST_LOGON_ATTEMPTS_CAPTCHA_REQUIRED_LIMIT, Bundle.SETTINGS,
					DefaultSettings.TRUSTED_HOST_LOGON_ATTEMPTS_CAPTCHA_REQUIRED_LIMIT);
		} else {
			logonAttemptsCaptchaRequiredLimit = Settings.getInt(SettingCodes.UNTRUSTED_HOST_LOGON_ATTEMPTS_CAPTCHA_REQUIRED_LIMIT, Bundle.SETTINGS,
					DefaultSettings.UNTRUSTED_HOST_LOGON_ATTEMPTS_CAPTCHA_REQUIRED_LIMIT);
		}
		return logonAttemptsCaptchaRequiredLimit >= 0 && failedAttempts >= logonAttemptsCaptchaRequiredLimit;
	}

	public synchronized boolean isLocalAuthMethod() {
		if (logon != null) {
			Boolean isLocal = WebUtil.isLocalAuthMethod(logon.getUser());
			if (isLocal != null) {
				return isLocal;
			}
		}
		return true;
	}

	public synchronized boolean isLocalPasswordRequired() {
		return localPasswordRequired;
	}

	public synchronized boolean isLoggedIn() {
		return logon != null;
	}

	public synchronized boolean isShowStatusBarInfo() {
		if (logon != null) {
			if (getAnnouncement() != null) {
				return true;
			}
			if (isLocalAuthMethod()) {
				Boolean isPasswordExpired = WebUtil.isPasswordExpired(
						DateUtil.addDayMinuteDelta(new Date(), Settings.getInt(SettingCodes.EXPIRATION_INFO_DAYS, Bundle.SETTINGS, DefaultSettings.EXPIRATION_INFO_DAYS), 0),
						logon);
				if (isPasswordExpired != null && isPasswordExpired) {
					return true;
				}
				if (logon.getLimitLogons()) {
					long logonsLeft = logon.getMaxSuccessfulLogons() - logon.getSuccessfulLogons();
					if (logonsLeft <= Settings.getLong(SettingCodes.EXPIRATION_INFO_LOGONS_LEFT, Bundle.SETTINGS, DefaultSettings.EXPIRATION_INFO_LOGONS_LEFT) && logonsLeft >= 0) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public synchronized boolean isShowTooltips() {
		if (logon != null) {
			return logon.getUser().getShowTooltips();
		}
		return true;
	}

	private void loadUserMenuModel() {
		userMenuModel = new DefaultMenuModel();
		if (logon != null) {
			int menuItemLabelClipMaxLength = Settings.getInt(SettingCodes.MENU_ITEM_LABEL_CLIP_MAX_LENGTH, Bundle.SETTINGS, DefaultSettings.MENU_ITEM_LABEL_CLIP_MAX_LENGTH);
			Submenu userMenu = new Submenu();
			userMenu.setLabel(CommonUtil.clipString(getUserIdentityString(), menuItemLabelClipMaxLength, CommonUtil.DEFAULT_ELLIPSIS, EllipsisPlacement.TRAILING));
			userMenu.setIcon(WebUtil.MENUBAR_ICON_STYLECLASS + " ctsms-icon-activeuser");
			// http://forum.primefaces.org/viewtopic.php?f=3&t=14924&p=67335#p67335
			userMenu.setId("userMenu");
			userMenuModel.addSubmenu(userMenu);
			StaffOutVO identity = getUserIdentity();
			if (identity != null) {
				MenuItem identityMenuItem = new MenuItem();
				identityMenuItem.setValue(CommonUtil.clipString(identity.getNameWithTitles(), menuItemLabelClipMaxLength, CommonUtil.DEFAULT_ELLIPSIS, EllipsisPlacement.TRAILING));
				identityMenuItem.setOnclick(MessageFormat.format("openStaff({0})", Long.toString(identity.getId())));
				identityMenuItem.setUrl("#");
				identityMenuItem.setIcon(WebUtil.MENUBAR_ICON_STYLECLASS + " ctsms-icon-identity");
				identityMenuItem.setId("identityMenuItem");
				userMenu.getChildren().add(identityMenuItem);
			}
			int i;
			Locale userLocale = null;
			if (Settings.getBoolean(SettingCodes.SHOW_LOCALE_MENU, Bundle.SETTINGS, DefaultSettings.SHOW_LOCALE_MENU)) {
				if (userLocale == null) {
					userLocale = getLocale();
				}
				Submenu localesMenu = new Submenu();
				localesMenu.setLabel(Messages.getMessage(MessageCodes.CURRENT_LOCALE_LABEL, CommonUtil.localeToDisplayString(userLocale, userLocale)));
				localesMenu.setIcon(WebUtil.MENUBAR_ICON_STYLECLASS + " ctsms-icon-locale");
				localesMenu.setId("localesMenu");
				userMenu.getChildren().add(localesMenu);
				i = 0;
				Iterator<LocaleVO> localesIt = getLocales().iterator();
				while (localesIt.hasNext()) {
					LocaleVO locale = localesIt.next();
					MenuItem localeMenuItem = new MenuItem();
					localeMenuItem.setValue(CommonUtil.clipString(locale.getName(), menuItemLabelClipMaxLength, CommonUtil.DEFAULT_ELLIPSIS,
							EllipsisPlacement.TRAILING));
					localeMenuItem.setActionListener(WebUtil.createActionListenerMethodBinding("#{sessionScopeBean.updateLocale('" + locale.getLanguage() + "')}"));
					localeMenuItem.setOncomplete("handleReload(xhr, status, args)");
					localeMenuItem.setId("localeMenuItem_" + Integer.toString(i));
					if (CommonUtil.localeFromString(locale.getLanguage()).equals(userLocale)) {
						localeMenuItem.setIcon(MENUITEM_CHECKED_STYLECLASS);
					}
					localesMenu.getChildren().add(localeMenuItem);
					i++;
				}
			}
			if (Settings.getBoolean(SettingCodes.SHOW_TIMEZONE_MENU, Bundle.SETTINGS, DefaultSettings.SHOW_TIMEZONE_MENU)) {
				if (userLocale == null) {
					userLocale = getLocale();
				}
				TimeZone userTimeZone = getTimeZone();
				Submenu timeZonesMenu = new Submenu();
				timeZonesMenu.setLabel(Messages.getMessage(MessageCodes.CURRENT_TIME_ZONE_LABEL, CommonUtil.timeZoneToDisplayString(userTimeZone, userLocale))); // .getDisplayName(false,TimeZone.LONG,userLocale)));
				timeZonesMenu.setIcon(WebUtil.MENUBAR_ICON_STYLECLASS + " ctsms-icon-timezone");
				timeZonesMenu.setId("timeZonesMenu");
				userMenu.getChildren().add(timeZonesMenu);
				Submenu westTimeZonesMenu = new Submenu();
				westTimeZonesMenu.setLabel(Messages.getString(MessageCodes.WEST_TIME_ZONE_LABEL));
				westTimeZonesMenu.setId("westTimeZonesMenu");
				timeZonesMenu.getChildren().add(westTimeZonesMenu);
				Submenu eastTimeZonesMenu = new Submenu();
				eastTimeZonesMenu.setLabel(Messages.getString(MessageCodes.EAST_TIME_ZONE_LABEL));
				eastTimeZonesMenu.setId("eastTimeZonesMenu");
				timeZonesMenu.getChildren().add(eastTimeZonesMenu);
				i = 0;
				Map<Integer, ArrayList<TimeZoneVO>> timeZonesByOffset = DateUtil.getTimeZoneByOffsets(getTimeZones());
				Iterator<Integer> it = timeZonesByOffset.keySet().iterator();
				while (it.hasNext()) {
					Integer timeZoneOffset = it.next();
					Submenu offsetTimeZonesMenu = new Submenu();
					offsetTimeZonesMenu.setLabel(Messages.getMessage(MessageCodes.OFFSET_TIME_ZONE_LABEL, timeZoneOffset < 0 ? "-" : (timeZoneOffset > 0 ? "+" : ""),
							DateUtil.getDurationString(timeZoneOffset / 1000, DurationUnitOfTime.HOURS, DurationUnitOfTime.MINUTES, 0)));
					offsetTimeZonesMenu.setId("offsetTimeZonesMenu_" + Integer.toString(i));
					int j = 0;
					boolean timeZoneFound = false;
					Iterator<TimeZoneVO> timeZonesIt = timeZonesByOffset.get(timeZoneOffset).iterator();
					while (timeZonesIt.hasNext()) {
						TimeZoneVO timeZone = timeZonesIt.next();
						MenuItem timeZoneMenuItem = new MenuItem();
						timeZoneMenuItem.setValue(CommonUtil.clipString(timeZone.getName(), menuItemLabelClipMaxLength,
								CommonUtil.DEFAULT_ELLIPSIS, EllipsisPlacement.TRAILING)); // .getDisplayName(true,TimeZone.LONG,userLocale));
						timeZoneMenuItem.setActionListener(WebUtil.createActionListenerMethodBinding("#{sessionScopeBean.updateTimeZone('" + timeZone.getTimeZoneID()
								+ "')}"));
						timeZoneMenuItem.setOncomplete("handleReload(xhr, status, args)");
						timeZoneMenuItem.setId("timeZoneMenuItem_" + Integer.toString(i) + "_" + Integer.toString(j));
						if (CommonUtil.timeZoneFromString(timeZone.getTimeZoneID()).equals(userTimeZone)) {
							timeZoneMenuItem.setIcon(MENUITEM_CHECKED_STYLECLASS);
							timeZoneFound = true;
						}
						offsetTimeZonesMenu.getChildren().add(timeZoneMenuItem);
						j++;
					}
					if (timeZoneFound) {
						offsetTimeZonesMenu.setIcon(MENUITEM_CHECKED_STYLECLASS);
					}
					Submenu hemispehreTimeZoneMenu = null;
					if (timeZoneOffset < 0) {
						hemispehreTimeZoneMenu = westTimeZonesMenu;
					} else { // if (timeZoneOffset >= 0) {
						hemispehreTimeZoneMenu = eastTimeZonesMenu;
					}
					hemispehreTimeZoneMenu.getChildren().add(offsetTimeZonesMenu);
					if (timeZoneFound) {
						hemispehreTimeZoneMenu.setIcon(MENUITEM_CHECKED_STYLECLASS);
					}
					i++;
				}
			}
			String userTheme = getTheme();
			Submenu themesMenu = new Submenu();
			themesMenu.setIcon(WebUtil.MENUBAR_ICON_STYLECLASS + " ctsms-icon-theme");
			themesMenu.setId("themesMenu");
			userMenu.getChildren().add(themesMenu);
			i = 0;
			Map<String, String> themeMap = Settings.getThemes();
			Iterator<String> themesIt = themeMap.keySet().iterator();
			while (themesIt.hasNext()) {
				String themeName = themesIt.next();
				String themeDisplayName = themeMap.get(themeName);
				MenuItem themeMenuItem = new MenuItem();
				themeMenuItem.setValue(CommonUtil.clipString(themeDisplayName, menuItemLabelClipMaxLength, CommonUtil.DEFAULT_ELLIPSIS, EllipsisPlacement.TRAILING));
				themeMenuItem.setActionListener(WebUtil.createActionListenerMethodBinding("#{sessionScopeBean.updateTheme('" + themeName + "')}"));
				themeMenuItem.setOncomplete("handleReload(xhr, status, args)");
				themeMenuItem.setId("themeMenuItem_" + Integer.toString(i));
				if (themeName.equals(userTheme)) {
					themeMenuItem.setIcon(MENUITEM_CHECKED_STYLECLASS);
					themesMenu.setLabel(Messages.getMessage(MessageCodes.CURRENT_THEME_LABEL, themeDisplayName));
				}
				themesMenu.getChildren().add(themeMenuItem);
				i++;
			}
			boolean showTooltips = isShowTooltips();
			MenuItem tooltipMenuItem = new MenuItem();
			tooltipMenuItem.setValue(CommonUtil.clipString(showTooltips ? Messages.getString(MessageCodes.HIDE_TOOLTIPS) : Messages.getString(MessageCodes.SHOW_TOOLTIPS),
					menuItemLabelClipMaxLength, CommonUtil.DEFAULT_ELLIPSIS, EllipsisPlacement.TRAILING));
			tooltipMenuItem.setActionListener(WebUtil.createActionListenerMethodBinding("#{sessionScopeBean.updateShowTooltips(" + Boolean.toString(!showTooltips) + ")}"));
			tooltipMenuItem.setOncomplete("handleReload(xhr, status, args)");
			tooltipMenuItem.setIcon(WebUtil.MENUBAR_ICON_STYLECLASS + " ctsms-icon-tooltip");
			tooltipMenuItem.setId("tooltipMenuItem");
			userMenu.getChildren().add(tooltipMenuItem);
			if (isLocalAuthMethod()) {
				MenuItem changePasswordMenuItem = new MenuItem();
				changePasswordMenuItem.setValue(Messages.getString(MessageCodes.CHANGE_PASSWORD_MENU_ITEM_LABEL));
				changePasswordMenuItem.setOnclick("openChangePassword()");
				changePasswordMenuItem.setUrl("#");
				changePasswordMenuItem.setIcon(WebUtil.MENUBAR_ICON_STYLECLASS + " ctsms-icon-key");
				changePasswordMenuItem.setId("changePasswordMenuItem");
				userMenu.getChildren().add(changePasswordMenuItem);
			}
			MenuItem logoutMenuItem = new MenuItem();
			logoutMenuItem.setValue(CommonUtil.clipString(Messages.getMessage(MessageCodes.LOGOUT_LABEL, logon.getUser().getName()), menuItemLabelClipMaxLength,
					CommonUtil.DEFAULT_ELLIPSIS, EllipsisPlacement.MID));
			logoutMenuItem.setActionListener(WebUtil.createActionListenerMethodBinding("#{sessionScopeBean.logout()}")); // empty brackets required here...
			// logoutMenuItem.setOncomplete("handleLogout(xhr, status, args)");
			logoutMenuItem.setIcon(WebUtil.MENUBAR_ICON_STYLECLASS + " ctsms-icon-exit");
			logoutMenuItem.setId("logoutMenuItem");
			userMenu.getChildren().add(logoutMenuItem);
		}
	}

	public synchronized String login() {
		logon = null;
		auth.setHost(WebUtil.getRemoteHost());
		String outcome; // = Urls.LOGIN.value();
		try {
			logon = WebUtil.getServiceLocator().getToolsService().logon(auth);
			ApplicationScopeBean.registerActiveUser(logon.getUser());
			WebUtil.setSessionTimeout();
			failedAttempts = 0;
			auth.setLocalPassword(null);
			clearauthenticationFailedMessage();
			outcome = getLoginOutcome(true);
		} catch (ServiceException e) {
			failedAttempts++;
			authenticationFailed = true;
			authenticationFailedMessage = e.getMessage();
			outcome = getLoginOutcome(false);
		} catch (AuthenticationException e) {
			authenticationFailed = true;
			if (AuthenticationExceptionCodes.LOCAL_PASSWORD_REQUIRED.equals(e.getErrorCode())) {
				failedAttempts = 0;
				localPasswordRequired = true;
				authenticationFailedMessage = e.getMessage();
			} else {
				failedAttempts++;
				if (Settings.getBoolean(SettingCodes.HIDE_DETAILED_AUTHENTICATION_ERROR, Bundle.SETTINGS, DefaultSettings.HIDE_DETAILED_AUTHENTICATION_ERROR)) {
					authenticationFailedMessage = Messages.getString(MessageCodes.OPAQUE_AUTHENTICATION_ERROR_MESSAGE);
				} else {
					authenticationFailedMessage = e.getMessage();
				}
			}
			outcome = getLoginOutcome(false);
		} catch (AuthorisationException e) {
			failedAttempts++;
			authenticationFailed = true;
			authenticationFailedMessage = e.getMessage();
			outcome = getLoginOutcome(false);
		} catch (IllegalArgumentException e) {
			failedAttempts++;
			authenticationFailed = true;
			authenticationFailedMessage = e.getMessage();
			outcome = getLoginOutcome(false);
		} finally {
			initSets();
		}
		return outcome;
	}

	public synchronized void logout() {
		FacesContext context = FacesContext.getCurrentInstance();
		logout(WebUtil.getRefererBase64((HttpServletRequest) context.getExternalContext().getRequest()));
	}

	private synchronized void logout(String redirectUrlBase64) {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
		StringBuilder url = new StringBuilder(Urls.LOGIN.toString(request));
		url.append("?");
		url.append(GetParamNames.REFERER);
		url.append("=");
		url.append(redirectUrlBase64);
		externalContext.invalidateSession();
		try {
			externalContext.redirect(url.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// WebUtil.appendRefererParameter(url, request, "?");
		//
		//
		//
		// ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		// RequestContext requestContext = RequestContext.getCurrentInstance();
		// if (requestContext != null) {
		// requestContext.addCallbackParam(JSValues.AJAX_OPERATION_SUCCESS.toString(), true);
		// requestContext.addCallbackParam(JSValues.AJAX_LOGGED_OUT.toString(), true);
		// requestContext.addCallbackParam(JSValues.AJAX_REFERER_BASE64.toString(), redirectUrl);
		// }
		// externalContext.invalidateSession();
		//
		//
		//
		// ajax = false;
		// // final ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		// // externalContext.invalidateSession();
		// if (ajax) {
		// RequestContext requestContext = RequestContext.getCurrentInstance();
		// if (requestContext != null) {
		// requestContext.addCallbackParam(JSValues.AJAX_OPERATION_SUCCESS.toString(), true);
		// requestContext.addCallbackParam(JSValues.AJAX_LOGGED_OUT.toString(), true);
		// requestContext.addCallbackParam(JSValues.AJAX_REFERER_BASE64.toString(), redirectUrl);
		// }
		// // http://www.icesoft.org/JForum/posts/list/15/7658.page#sthash.XPD8yMMZ.dpbs
		// new Thread() {
		//
		// public void run() {
		// try {
		// Thread.sleep(10000);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		// externalContext.invalidateSession();
		// // HttpSession session = (HttpSession) externalContext.getSession(false);
		// // session.invalidate();
		// }
		// }.start();
		// } else {
		// ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		// externalContext.invalidateSession();
		// try {
		// externalContext.redirect(redirectUrl);
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
	}

	public synchronized StreamedContent putImage(Object key, byte[] image, String mimeType) {
		if (image != null && image.length > 0 && mimeType != null && mimeType.length() > 0) {
			return imageStore.put(key, new DefaultStreamedContent(new ByteArrayInputStream(image), mimeType));
		} else {
			return imageStore.remove(key);
		}
	}

	public synchronized void resetChangePasswordInputs() {
		newPassword = null;
		oldPassword = null;
	}

	public synchronized String resetLoginInputs() {
		auth.setUsername(null);
		auth.setPassword(null);
		auth.setLocalPassword(null);
		clearauthenticationFailedMessage();
		return getLoginOutcome(false);
	}

	public synchronized void setFilterMap(String id, Map<String, String> filterMap) {
		getFilterComponentMap().put(id, filterMap);
	}

	// public void putSelectionSetServiceCache(Object key, Object value) {
	// if (selectionSetServiceCache != null && value != null) {
	// Class type = value.getClass();
	// HashMap<Object, Object> cache;
	// if (selectionSetServiceCache.containsKey(type)) {
	// cache = selectionSetServiceCache.get(type);
	// } else {
	// cache = new HashMap<Object, Object>();
	// selectionSetServiceCache.put(type, cache);
	// }
	// cache.put(key, value);
	// }
	// }
	public synchronized void setLocalPassword(String localPassword) {
		auth.setLocalPassword(localPassword);
	}

	public synchronized void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public synchronized void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public synchronized void setPassword(String password) {
		auth.setPassword(password);
	}

	public synchronized void setUsername(String username) {
		auth.setUsername(username);
	}

	public synchronized boolean testPassword(String passwordToTest) {
		if (CommonUtil.isEmptyString(passwordToTest) && CommonUtil.isEmptyString(auth.getPassword())) {
			return true;
		} else if (CommonUtil.isEmptyString(passwordToTest) && !CommonUtil.isEmptyString(auth.getPassword())) {
			return false;
		} else if (!CommonUtil.isEmptyString(passwordToTest) && CommonUtil.isEmptyString(auth.getPassword())) {
			return false;
		} else {
			return passwordToTest.equals(auth.getPassword());
		}
	}

	public synchronized void updateLocale(String locale) {
		boolean success = false;
		try {
			logon = WebUtil.getServiceLocator().getUserService().updateLocale(auth, locale);
			success = true;
		} catch (ServiceException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
		} catch (IllegalArgumentException e) {
		} finally {
			initSets();
		}
		RequestContext requestContext = RequestContext.getCurrentInstance();
		if (requestContext != null) {
			requestContext.addCallbackParam(JSValues.AJAX_OPERATION_SUCCESS.toString(), success);
		}
	}

	public synchronized void updateShowTooltips(boolean showTooltips) {
		boolean success = false;
		try {
			logon = WebUtil.getServiceLocator().getUserService().updateShowTooltips(auth, showTooltips);
			success = true;
		} catch (ServiceException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
		} catch (IllegalArgumentException e) {
		} finally {
			initSets();
		}
		RequestContext requestContext = RequestContext.getCurrentInstance();
		if (requestContext != null) {
			requestContext.addCallbackParam(JSValues.AJAX_OPERATION_SUCCESS.toString(), success);
		}
	}

	public synchronized void updateTheme(String theme) {
		boolean success = false;
		try {
			logon = WebUtil.getServiceLocator().getUserService().updateTheme(auth, theme);
			success = true;
		} catch (ServiceException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
		} catch (IllegalArgumentException e) {
		} finally {
			initSets();
		}
		RequestContext requestContext = RequestContext.getCurrentInstance();
		if (requestContext != null) {
			requestContext.addCallbackParam(JSValues.AJAX_OPERATION_SUCCESS.toString(), success);
		}
	}

	public synchronized void updateTimeZone(String timeZone) {
		boolean success = false;
		try {
			logon = WebUtil.getServiceLocator().getUserService().updateTimeZone(auth, timeZone);
			success = true;
		} catch (ServiceException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
		} catch (IllegalArgumentException e) {
		} finally {
			initSets();
		}
		RequestContext requestContext = RequestContext.getCurrentInstance();
		if (requestContext != null) {
			requestContext.addCallbackParam(JSValues.AJAX_OPERATION_SUCCESS.toString(), success);
		}
	}
}
