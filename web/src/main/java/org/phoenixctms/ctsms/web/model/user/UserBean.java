package org.phoenixctms.ctsms.web.model.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.AuthenticationType;
import org.phoenixctms.ctsms.enumeration.DBModule;
import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.js.JsUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.AuthenticationTypeVO;
import org.phoenixctms.ctsms.vo.DepartmentVO;
import org.phoenixctms.ctsms.vo.LdapEntryVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.TimeZoneVO;
import org.phoenixctms.ctsms.vo.UserInVO;
import org.phoenixctms.ctsms.vo.UserOutVO;
import org.phoenixctms.ctsms.vo.UserPermissionProfileOutVO;
import org.phoenixctms.ctsms.web.model.AuthenticationTypeSelector;
import org.phoenixctms.ctsms.web.model.AuthenticationTypeSelectorListener;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.GetParamNames;
import org.phoenixctms.ctsms.web.util.JSValues;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

@ManagedBean
@ViewScoped
public class UserBean extends ManagedBeanBase implements AuthenticationTypeSelectorListener {

	private static final int AUTH_METHOD_PROPERTY_ID = 1;
	private final static Integer MAX_GRAPH_USER_INSTANCES = 2;

	public static void copyUserOutToIn(UserInVO in, UserOutVO out) {
		if (in != null && out != null) {
			DepartmentVO departmentVO = out.getDepartment();
			StaffOutVO identityVO = out.getIdentity();
			AuthenticationTypeVO methodVO = out.getAuthMethod();
			in.setDepartmentId(departmentVO == null ? null : departmentVO.getId());
			in.setId(out.getId());
			in.setIdentityId(identityVO == null ? null : identityVO.getId());
			in.setLocale(out.getLocale());
			in.setShowTooltips(out.getShowTooltips());
			in.setLocked(out.getLocked());
			in.setDecrypt(out.getDecrypt());
			in.setAuthMethod(methodVO == null ? null : methodVO.getMethod());
			in.setName(out.getName());
			in.setTimeZone(out.getTimeZone());
			in.setDateFormat(out.getDateFormat());
			in.setDecimalSeparator(out.getDecimalSeparator());
			in.setVersion(out.getVersion());
			in.setTheme(out.getTheme());
		}
	}

	public static void initUserDefaultValues(UserInVO in, UserOutVO user) {
		if (in != null) {
			in.setDepartmentId(user == null ? null : user.getDepartment().getId());
			in.setId(null);
			in.setIdentityId(null);
			in.setLocale(Settings.getString(SettingCodes.USER_LOCALE_PRESET, Bundle.SETTINGS, DefaultSettings.USER_LOCALE_PRESET));
			in.setShowTooltips(Settings.getBoolean(SettingCodes.USER_SHOW_TOOLTIPS_PRESET, Bundle.SETTINGS, DefaultSettings.USER_SHOW_TOOLTIPS_PRESET));
			in.setLocked(Settings.getBoolean(SettingCodes.USER_LOCKED_PRESET, Bundle.SETTINGS, DefaultSettings.USER_LOCKED_PRESET));
			in.setDecrypt(Settings.getBoolean(SettingCodes.USER_DECRYPT_PRESET, Bundle.SETTINGS, DefaultSettings.USER_DECRYPT_PRESET));
			in.setAuthMethod(Settings.getAuthenticationType(SettingCodes.USER_AUTH_METHOD_PRESET, Bundle.SETTINGS, DefaultSettings.USER_AUTH_METHOD_PRESET));
			in.setName(Messages.getString(MessageCodes.USER_NAME_PRESET));
			in.setTimeZone(Settings.getString(SettingCodes.USER_TIME_ZONE_PRESET, Bundle.SETTINGS, DefaultSettings.USER_TIME_ZONE_PRESET));
			in.setDateFormat(Messages.getString(MessageCodes.USER_DATE_FORMAT_PRESET));
			in.setDecimalSeparator(Messages.getString(MessageCodes.USER_DECIMAL_SEPARATOR_PRESET));
			in.setVersion(null);
			in.setTheme(Settings.getString(SettingCodes.USER_THEME_PRESET, Bundle.SETTINGS, DefaultSettings.USER_THEME_PRESET));
		}
	}

	private UserInVO in;
	private UserOutVO out;
	private String remoteUserMessage;
	private Boolean remoteUserOk;
	private LdapEntryVO ldapEntry1;
	private LdapEntryVO ldapEntry2;
	private ArrayList<SelectItem> departments;
	private ArrayList<SelectItem> locales;
	private TimeZoneVO timeZone;
	private ArrayList<SelectItem> themes;
	private ArrayList<SelectItem> dateFormats;
	private ArrayList<SelectItem> decimalSeparators;
	private AuthenticationTypeSelector authMethod;
	private HashMap<String, Long> tabCountMap;
	private HashMap<String, String> tabTitleMap;
	private String deferredDeleteReason;

	public UserBean() {
		super();
		tabCountMap = new HashMap<String, Long>();
		tabTitleMap = new HashMap<String, String>();
		setAuthMethod(new AuthenticationTypeSelector(this, AUTH_METHOD_PROPERTY_ID));
		remoteUserMessage = null;
		remoteUserOk = null;
		ldapEntry1 = null;
		ldapEntry2 = null;
		timeZone = null;
	}

	@Override
	public String addAction() {
		UserInVO backup = new UserInVO(in);
		// Long idBackup = in.getId();
		// Long versionBackup = in.getVersion();
		in.setId(null);
		in.setVersion(null);
		sanitizeInVals();
		try {
			out = WebUtil.getServiceLocator().getUserService().addUser(WebUtil.getAuthentication(), in, MAX_GRAPH_USER_INSTANCES);
			initIn();
			initSets();
			addOperationSuccessMessage(MessageCodes.ADD_OPERATION_SUCCESSFUL);
			return ADD_OUTCOME;
		} catch (ServiceException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (IllegalArgumentException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
		return ERROR_OUTCOME;
	}

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess, String outcome) {
		if ((UPDATE_OUTCOME.equals(outcome) || DELETE_OUTCOME.equals(outcome)) && WebUtil.isUserIdLoggedIn(in.getId())) {
			WebUtil.logout();
		} else {
			RequestContext requestContext = RequestContext.getCurrentInstance();
			if (requestContext != null) {
				requestContext.addCallbackParam(JSValues.AJAX_WINDOW_TITLE_BASE64.toString(), JsUtil.encodeBase64(getTitle(operationSuccess), false));
				requestContext.addCallbackParam(JSValues.AJAX_WINDOW_NAME.toString(), getWindowName(operationSuccess));
				requestContext.addCallbackParam(JSValues.AJAX_OPERATION_SUCCESS.toString(), operationSuccess);
				requestContext.addCallbackParam(JSValues.AJAX_ROOT_ENTITY_CREATED.toString(), out != null);
				WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_PASSWORD_TAB_TITLE_BASE64, JSValues.AJAX_PASSWORD_COUNT,
						MessageCodes.PASSWORD_TAB_TITLE, MessageCodes.PASSWORD_TAB_TITLE_WITH_COUNT, tabCountMap.get(JSValues.AJAX_PASSWORD_COUNT.toString()));
				WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_USER_PERMISSION_PROFILE_TAB_TITLE_BASE64,
						JSValues.AJAX_USER_PERMISSION_PROFILE_COUNT, MessageCodes.USER_PERMISSION_PROFILES_TAB_TITLE, MessageCodes.USER_PERMISSION_PROFILES_TAB_TITLE_WITH_COUNT,
						tabCountMap.get(JSValues.AJAX_USER_PERMISSION_PROFILE_COUNT.toString()));
				WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_USER_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_USER_JOURNAL_ENTRY_COUNT,
						MessageCodes.USER_JOURNAL_TAB_TITLE, MessageCodes.USER_JOURNAL_TAB_TITLE_WITH_COUNT, tabCountMap.get(JSValues.AJAX_USER_JOURNAL_ENTRY_COUNT.toString()));
			}
		}
	}

	@Override
	protected String changeAction(Long id) {
		out = null;
		if (id != null) {
			try {
				out = WebUtil.getServiceLocator().getUserService().getUser(WebUtil.getAuthentication(), id, MAX_GRAPH_USER_INSTANCES);
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
		initIn();
		initSets();
		return CHANGE_OUTCOME;
	}

	public List<LdapEntryVO> completeLdapEntry1(String query) {
		try {
			return (List<LdapEntryVO>) WebUtil.getServiceLocator().getToolsService().completeLdapEntry1(WebUtil.getAuthentication(), query, null);
		} catch (ServiceException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
		} catch (IllegalArgumentException e) {
		}
		return new ArrayList<LdapEntryVO>();
	}

	public List<LdapEntryVO> completeLdapEntry2(String query) {
		try {
			return (List<LdapEntryVO>) WebUtil.getServiceLocator().getToolsService().completeLdapEntry2(WebUtil.getAuthentication(), query, null);
		} catch (ServiceException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
		} catch (IllegalArgumentException e) {
		}
		return new ArrayList<LdapEntryVO>();
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

	@Override
	public String deleteAction() {
		return deleteAction(in.getId());
	}

	@Override
	public String deleteAction(Long id) {
		try {
			out = WebUtil.getServiceLocator().getUserService().deleteUser(WebUtil.getAuthentication(), id,
					Settings.getBoolean(SettingCodes.USER_DEFERRED_DELETE, Bundle.SETTINGS, DefaultSettings.USER_DEFERRED_DELETE),
					false, deferredDeleteReason,
					MAX_GRAPH_USER_INSTANCES);
			initIn();
			initSets();
			if (!out.getDeferredDelete()) {
				addOperationSuccessMessage(MessageCodes.DELETE_OPERATION_SUCCESSFUL);
			}
			out = null;
			return DELETE_OUTCOME;
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
		return ERROR_OUTCOME;
	}

	@Override
	public AuthenticationType getAuthenticationType(int property) {
		switch (property) {
			case AUTH_METHOD_PROPERTY_ID:
				return this.in.getAuthMethod();
			default:
				return AuthenticationTypeSelectorListener.NO_SELECTION_AUTHENTICATION_TYPE;
		}
	}

	public AuthenticationTypeSelector getAuthMethod() {
		return authMethod;
	}

	public ArrayList<SelectItem> getDateFormats() {
		return dateFormats;
	}

	public ArrayList<SelectItem> getDecimalSeparators() {
		return decimalSeparators;
	}

	public String getDeferredDeleteReason() {
		return deferredDeleteReason;
	}

	public ArrayList<SelectItem> getDepartments() {
		return departments;
	}

	public String getIdentityName() {
		return WebUtil.staffIdToName(in.getIdentityId());
	}

	public UserInVO getIn() {
		return in;
	}

	public LdapEntryVO getLdapEntry1() {
		return ldapEntry1;
	}

	public LdapEntryVO getLdapEntry2() {
		return ldapEntry2;
	}

	public ArrayList<SelectItem> getLocales() {
		return locales;
	}

	@Override
	public String getModifiedAnnotation() {
		if (out != null) {
			return WebUtil.getModifiedAnnotation(out.getVersion(), out.getModifiedUser(), out.getModifiedTimestamp());
		} else {
			return super.getModifiedAnnotation();
		}
	}

	public UserOutVO getOut() {
		return out;
	}

	public String getRemoteUserMessage() {
		return remoteUserMessage;
	}

	public Boolean getRemoteUserOk() {
		return remoteUserOk;
	}

	public String getTabTitle(String tab) {
		return tabTitleMap.get(tab);
	}

	public ArrayList<SelectItem> getThemes() {
		return themes;
	}

	public TimeZoneVO getTimeZone() {
		return timeZone;
	}

	@Override
	public String getTitle() {
		return getTitle(WebUtil.getLongParamValue(GetParamNames.USER_ID) == null);
	}

	// public ArrayList<SelectItem> getTimeZones() {
	// return timeZones;
	// }
	private String getTitle(boolean operationSuccess) {
		if (out != null) {
			return Messages.getMessage(out.getDeferredDelete() ? MessageCodes.DELETED_TITLE : MessageCodes.USER_TITLE, Long.toString(out.getId()),
					CommonUtil.userOutVOToString(out));
		} else {
			return Messages.getString(operationSuccess ? MessageCodes.CREATE_NEW_USER : MessageCodes.ERROR_LOADING_USER);
		}
	}

	@Override
	public String getWindowName() {
		return getWindowName(WebUtil.getLongParamValue(GetParamNames.USER_ID) == null);
	}

	private String getWindowName(boolean operationSuccess) {
		if (out != null) {
			return String.format(JSValues.USER_ENTITY_WINDOW_NAME.toString(), Long.toString(out.getId()), WebUtil.getWindowNameUniqueToken());
		} else {
			if (operationSuccess) {
				return String.format(JSValues.USER_ENTITY_WINDOW_NAME.toString(), JSValues.NEW_ENTITY_WINDOW_NAME_SUFFIX.toString(), "");
			} else {
				Long userId = WebUtil.getLongParamValue(GetParamNames.USER_ID);
				if (userId != null) {
					return String.format(JSValues.USER_ENTITY_WINDOW_NAME.toString(), userId.toString(), WebUtil.getWindowNameUniqueToken());
				} else {
					return String.format(JSValues.USER_ENTITY_WINDOW_NAME.toString(), JSValues.NEW_ENTITY_WINDOW_NAME_SUFFIX.toString(), "");
				}
			}
		}
	}

	public void handleAuthMethodChange() {
		loadRemoteUserInfo();
		ldapEntry1 = null;
		ldapEntry2 = null;
	}

	public void handleLdapEntry1Select() {
		if (ldapEntry1 != null) {
			in.setName(ldapEntry1.getUsername());
		}
		loadRemoteUserInfo();
	}

	public void handleLdapEntry2Select() {
		if (ldapEntry2 != null) {
			in.setName(ldapEntry2.getUsername());
		}
		loadRemoteUserInfo();
	}

	public void handleNameKeyUp() {
		loadRemoteUserInfo();
	}

	//	public void handleTimeZoneSelect() {
	//		if (timeZone != null) {
	//			in.setTimeZone(timeZone.getTimeZoneID());
	//		}
	//	}
	public void handleTimeZoneSelect(SelectEvent event) {
	}

	public void handleTimeZoneUnselect(UnselectEvent event) {
	}

	@PostConstruct
	private void init() {
		Long id = WebUtil.getLongParamValue(GetParamNames.USER_ID);
		if (id != null) {
			this.load(id);
		} else {
			this.change();
		}
	}

	private void initIn() {
		if (in == null) {
			in = new UserInVO();
		}
		if (out != null) {
			copyUserOutToIn(in, out);
		} else {
			initUserDefaultValues(in, WebUtil.getUser());
		}
	}

	private void initSets() {
		tabCountMap.clear();
		tabTitleMap.clear();
		PSFVO psf = new PSFVO();
		psf.setPageSize(0);
		Long count = null;
		if (out != null) {
			try {
				count = WebUtil.getServiceLocator().getUserService().getPassword(WebUtil.getAuthentication(), in.getId()) != null ? 1l : 0l;
			} catch (ServiceException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			} catch (AuthorisationException e) {
			} catch (IllegalArgumentException e) {
			}
		}
		tabCountMap.put(JSValues.AJAX_PASSWORD_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_PASSWORD_COUNT.toString(), WebUtil.getTabTitleString(MessageCodes.PASSWORD_TAB_TITLE, MessageCodes.PASSWORD_TAB_TITLE_WITH_COUNT, count));
		count = null;
		if (out != null) {
			try {
				Collection<UserPermissionProfileOutVO> userPermissionProfilesOut = WebUtil.getServiceLocator().getUserService()
						.getPermissionProfiles(WebUtil.getAuthentication(), in.getId(), null, null);
				count = userPermissionProfilesOut == null ? null : new Long(userPermissionProfilesOut.size());
			} catch (ServiceException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			} catch (AuthorisationException e) {
			} catch (IllegalArgumentException e) {
			}
		}
		tabCountMap.put(JSValues.AJAX_USER_PERMISSION_PROFILE_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_USER_PERMISSION_PROFILE_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.USER_PERMISSION_PROFILES_TAB_TITLE, MessageCodes.USER_PERMISSION_PROFILES_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null : WebUtil.getJournalCount(JournalModule.USER_JOURNAL, in.getId()));
		tabCountMap.put(JSValues.AJAX_USER_JOURNAL_ENTRY_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_USER_JOURNAL_ENTRY_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.USER_JOURNAL_TAB_TITLE, MessageCodes.USER_JOURNAL_TAB_TITLE_WITH_COUNT, count));
		ldapEntry1 = null;
		ldapEntry2 = null;
		loadRemoteUserInfo();
		departments = WebUtil.getVisibleDepartments(in.getDepartmentId());
		// Locale userLocale = null;
		if (this.locales == null) {
			// if (userLocale == null) {
			// userLocale = WebUtil.getLocale();
			// }
			this.locales = WebUtil.getLocales();
			// Collection<Locale> locales = WebUtil.getSupportedLocales();
			// this.locales = new ArrayList<SelectItem>(locales.size());
			// Iterator<Locale> it = locales.iterator();
			// while (it.hasNext()) {
			// Locale locale = it.next();
			// this.locales.add(new SelectItem(CommonUtil.localeToString(locale), CommonUtil.localeToDisplayString(locale, userLocale)));
			// }
		}
		//timeZone = null;
		loadTimeZone();
		// if (this.timeZones == null) {
		// // if (userLocale == null) {
		// // userLocale = WebUtil.getLocale();
		// // }
		// this.timeZones = WebUtil.getTimeZones();
		// // this.timeZones = new ArrayList<SelectItem>(timeZones.size());
		// // Iterator<TimeZone> it = timeZones.iterator();
		// // while (it.hasNext()) {
		// // TimeZone timeZone = it.next();
		// // this.timeZones.add(new SelectItem(CommonUtil.timeZoneToString(timeZone), CommonUtil.timeZoneToDisplayString(timeZone, userLocale)));
		// // }
		// }
		if (this.themes == null) {
			Map<String, String> themeMap = Settings.getThemes();
			this.themes = new ArrayList<SelectItem>(themeMap.size());
			Iterator<String> it = themeMap.keySet().iterator();
			while (it.hasNext()) {
				String themeName = it.next();
				this.themes.add(new SelectItem(themeName, themeMap.get(themeName)));
			}
		}
		this.dateFormats = WebUtil.getDateFormats(this.in.getDateFormat());
		if (this.decimalSeparators == null) {
			this.decimalSeparators = WebUtil.getDecimalSeparators();
		}
		if (WebUtil.isUserIdLoggedIn(in.getId())) {
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.EDITING_ACTIVE_USER);
		}
		deferredDeleteReason = (out == null ? null : out.getDeferredDeleteReason());
		if (out != null && out.isDeferredDelete()) { // && Settings.getBoolean(SettingCodes.USER_DEFERRED_DELETE, Bundle.SETTINGS, DefaultSettings.USER_DEFERRED_DELETE)) {
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.MARKED_FOR_DELETION, deferredDeleteReason);
		}
		ArrayList<String> messageCodes = new ArrayList<String>();
		if (WebUtil.getUserAuthMessages(out, null, messageCodes)) {
			Iterator<String> it = messageCodes.iterator();
			while (it.hasNext()) {
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, it.next());
			}
		} else {
			Iterator<String> it = messageCodes.iterator();
			while (it.hasNext()) {
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_INFO, it.next());
			}
		}
	}

	@Override
	public boolean isCreateable() {
		return WebUtil.getModuleEnabled(DBModule.USER_DB);
	}

	@Override
	public boolean isCreated() {
		return out != null;
	}

	public boolean isDeferredDelete() {
		return Settings.getBoolean(SettingCodes.USER_DEFERRED_DELETE, Bundle.SETTINGS, DefaultSettings.USER_DEFERRED_DELETE);
	}

	@Override
	public boolean isEditable() {
		return WebUtil.getModuleEnabled(DBModule.USER_DB) && super.isEditable();
	}

	public boolean isLdap1() {
		return AuthenticationType.LDAP1.equals(in.getAuthMethod());
	}

	public boolean isLdap2() {
		return AuthenticationType.LDAP2.equals(in.getAuthMethod());
	}

	@Override
	public boolean isRemovable() {
		return WebUtil.getModuleEnabled(DBModule.USER_DB) && isCreated() && !WebUtil.isUserIdLoggedIn(in.getId());
	}

	public boolean isTabEmphasized(String tab) {
		return WebUtil.isTabCountEmphasized(tabCountMap.get(tab));
	}

	@Override
	public String loadAction() {
		return loadAction(in.getId());
	}

	@Override
	public String loadAction(Long id) {
		out = null;
		try {
			out = WebUtil.getServiceLocator().getUserService().getUser(WebUtil.getAuthentication(), id, MAX_GRAPH_USER_INSTANCES);
			return LOAD_OUTCOME;
		} catch (ServiceException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} finally {
			initIn();
			initSets();
		}
		return ERROR_OUTCOME;
	}

	private void loadRemoteUserInfo() {
		remoteUserMessage = null;
		remoteUserOk = null;
		if (in.getAuthMethod() != null) {
			switch (in.getAuthMethod()) {
				case LOCAL:
					break;
				case LDAP1:
				case LDAP2:
					try {
						LdapEntryVO ldapEntry = null;
						if (AuthenticationType.LDAP1.equals(in.getAuthMethod())) {
							ldapEntry = WebUtil.getServiceLocator().getToolsService().getLdapEntry1(WebUtil.getAuthentication(), in.getName());
						} else if (AuthenticationType.LDAP2.equals(in.getAuthMethod())) {
							ldapEntry = WebUtil.getServiceLocator().getToolsService().getLdapEntry2(WebUtil.getAuthentication(), in.getName());
						}
						if (ldapEntry != null) {
							remoteUserMessage = Messages.getMessage(MessageCodes.LDAP_ENTRY_FOUND, ldapEntry.getAbsoluteDn());
							remoteUserOk = true;
						}
					} catch (ServiceException e) {
						remoteUserMessage = e.getMessage();
						remoteUserOk = CommonUtil.isEmptyString(e.getErrorCode()) ? null : false;
					} catch (AuthenticationException e) {
						remoteUserMessage = e.getMessage();
						WebUtil.publishException(e);
					} catch (AuthorisationException e) {
						remoteUserMessage = e.getMessage();
					} catch (IllegalArgumentException e) {
						remoteUserMessage = e.getMessage();
					}
					break;
				default:
			}
		}
	}

	private void loadTimeZone() {
		this.timeZone = WebUtil.getTimeZone(in.getTimeZone());
	}

	@Override
	public String resetAction() {
		out = null;
		initIn();
		initSets();
		return RESET_OUTCOME;
	}

	private void sanitizeInVals() {
		if (timeZone != null) {
			in.setTimeZone(timeZone.getTimeZoneID());
		} else {
			in.setTimeZone(null);
		}
		if (CommonUtil.NO_SELECTION_VALUE.equals(in.getDateFormat())) {
			in.setDateFormat(null);
		}
		if (CommonUtil.NO_SELECTION_VALUE.equals(in.getDecimalSeparator())) {
			in.setDecimalSeparator(null);
		}
	}

	@Override
	public void setAuthenticationType(int property, AuthenticationType method) {
		switch (property) {
			case AUTH_METHOD_PROPERTY_ID:
				this.in.setAuthMethod(method);
				break;
			default:
		}
	}

	public void setAuthMethod(AuthenticationTypeSelector method) {
		this.authMethod = method;
	}

	public void setDeferredDeleteReason(String deferredDeleteReason) {
		this.deferredDeleteReason = deferredDeleteReason;
	}

	public void setLdapEntry1(LdapEntryVO ldapEntry) {
		this.ldapEntry1 = ldapEntry;
	}

	public void setLdapEntry2(LdapEntryVO ldapEntry) {
		this.ldapEntry2 = ldapEntry;
	}

	public void setRemoteUserMessage(String remoteUserMessage) {
		this.remoteUserMessage = remoteUserMessage;
	}

	public void setRemoteUserOk(Boolean remoteUserOk) {
		this.remoteUserOk = remoteUserOk;
	}

	public void setTimeZone(TimeZoneVO timeZone) {
		this.timeZone = timeZone;
	}

	@Override
	public String updateAction() {
		UserInVO backup = new UserInVO(in);
		sanitizeInVals();
		try {
			out = WebUtil.getServiceLocator().getUserService().updateUser(WebUtil.getAuthentication(), in, MAX_GRAPH_USER_INSTANCES);
			initIn();
			initSets();
			addOperationSuccessMessage(MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
			return UPDATE_OUTCOME;
		} catch (ServiceException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (IllegalArgumentException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
		return ERROR_OUTCOME;
	}
}
