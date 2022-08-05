package org.phoenixctms.ctsms.web.model.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

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
import org.phoenixctms.ctsms.vo.UserInVO;
import org.phoenixctms.ctsms.vo.UserOutVO;
import org.phoenixctms.ctsms.vo.UserPermissionProfileOutVO;
import org.phoenixctms.ctsms.web.model.AuthenticationTypeSelector;
import org.phoenixctms.ctsms.web.model.AuthenticationTypeSelectorListener;
import org.phoenixctms.ctsms.web.model.DefaultTreeNode;
import org.phoenixctms.ctsms.web.model.IDVOTreeNode;
import org.phoenixctms.ctsms.web.model.shared.UserSettingsBeanBase;
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
import org.primefaces.model.TreeNode;

@ManagedBean
@ViewScoped
public class UserBean extends UserSettingsBeanBase implements AuthenticationTypeSelectorListener {

	private static final int AUTH_METHOD_PROPERTY_ID = 1;
	//private final static Integer MAX_GRAPH_USER_INSTANCES = 2;

	public static void copyUserOutToIn(UserInVO in, UserOutVO out) {
		if (in != null && out != null) {
			DepartmentVO departmentVO = out.getDepartment();
			StaffOutVO identityVO = out.getIdentity();
			UserOutVO parentVO = out.getParent();
			AuthenticationTypeVO methodVO = out.getAuthMethod();
			in.setDepartmentId(departmentVO == null ? null : departmentVO.getId());
			in.setId(out.getId());
			in.setIdentityId(identityVO == null ? null : identityVO.getId());
			in.setLocale(out.getLocale());
			in.setShowTooltips(out.getShowTooltips());
			in.setLocked(out.getLocked());
			in.setLockedUntrusted(out.getLockedUntrusted());
			in.setDecrypt(out.getDecrypt());
			in.setDecryptUntrusted(out.getDecryptUntrusted());
			in.setEnableInventoryModule(out.getEnableInventoryModule());
			in.setVisibleInventoryTabList(out.getVisibleInventoryTabList());
			in.setEnableStaffModule(out.getEnableStaffModule());
			in.setVisibleStaffTabList(out.getVisibleStaffTabList());
			in.setEnableCourseModule(out.getEnableCourseModule());
			in.setVisibleCourseTabList(out.getVisibleCourseTabList());
			in.setEnableTrialModule(out.getEnableTrialModule());
			in.setVisibleTrialTabList(out.getVisibleTrialTabList());
			in.setEnableInputFieldModule(out.getEnableInputFieldModule());
			in.setVisibleInputFieldTabList(out.getVisibleInputFieldTabList());
			in.setEnableProbandModule(out.getEnableProbandModule());
			in.setVisibleProbandTabList(out.getVisibleProbandTabList());
			in.setEnableMassMailModule(out.getEnableMassMailModule());
			in.setVisibleMassMailTabList(out.getVisibleMassMailTabList());
			in.setEnableUserModule(out.getEnableUserModule());
			in.setVisibleUserTabList(out.getVisibleUserTabList());
			in.setAuthMethod(methodVO == null ? null : methodVO.getMethod());
			in.setName(out.getName());
			in.setParentId(parentVO == null ? null : parentVO.getId());
			in.setTimeZone(out.getTimeZone());
			in.setDateFormat(out.getDateFormat());
			in.setDecimalSeparator(out.getDecimalSeparator());
			in.setVersion(out.getVersion());
			in.setTheme(out.getTheme());
			in.getInheritedProperties().clear();
			Iterator<String> it = out.getInheritedProperties().iterator();
			while (it.hasNext()) {
				String property = it.next();
				if (CommonUtil.USER_INHERITABLE_PROPERTIES.contains(property)) {
					in.getInheritedProperties().add(property);
				}
			}
			in.getInheritedPermissionProfileGroups().clear();
			in.getInheritedPermissionProfileGroups().addAll(out.getInheritedPermissionProfileGroups());
		}
	}

	private static UserOutVO createUserOutFromIn(UserInVO in) {
		UserOutVO result = new UserOutVO();
		if (in != null) {
			AuthenticationTypeVO authMethodVO = null;
			try {
				authMethodVO = WebUtil.getServiceLocator().getToolsService().getLocalizedAuthenticationType(WebUtil.getAuthentication(), in.getAuthMethod());
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
			result.setName(in.getName());
			result.setName(CommonUtil.getUserName(result));
			result.setAuthMethod(authMethodVO);
			result.setLocale(in.getLocale());
			result.setShowTooltips(in.getShowTooltips());
			result.setLocked(in.getLocked());
			result.setLockedUntrusted(in.getLockedUntrusted());
			result.setDecrypt(in.getDecrypt());
			result.setDecryptUntrusted(in.getDecryptUntrusted());
			result.setEnableInventoryModule(in.getEnableInventoryModule());
			result.setVisibleInventoryTabList(in.getVisibleInventoryTabList());
			result.setEnableStaffModule(in.getEnableStaffModule());
			result.setVisibleStaffTabList(in.getVisibleStaffTabList());
			result.setEnableCourseModule(in.getEnableCourseModule());
			result.setVisibleCourseTabList(in.getVisibleCourseTabList());
			result.setEnableTrialModule(in.getEnableTrialModule());
			result.setVisibleTrialTabList(in.getVisibleTrialTabList());
			result.setEnableInputFieldModule(in.getEnableInputFieldModule());
			result.setVisibleInputFieldTabList(in.getVisibleInputFieldTabList());
			result.setEnableProbandModule(in.getEnableProbandModule());
			result.setVisibleProbandTabList(in.getVisibleProbandTabList());
			result.setEnableMassMailModule(in.getEnableMassMailModule());
			result.setVisibleMassMailTabList(in.getVisibleMassMailTabList());
			result.setEnableUserModule(in.getEnableUserModule());
			result.setVisibleUserTabList(in.getVisibleUserTabList());
			//result.setAuthMethod(methodVO == null ? null : methodVO.getMethod());
			result.setTimeZone(in.getTimeZone());
			result.setDateFormat(in.getDateFormat());
			result.setDecimalSeparator(in.getDecimalSeparator());
			result.setTheme(in.getTheme());
			result.getInheritedProperties().clear();
			result.getInheritedProperties().addAll(in.getInheritedProperties());
			result.getInheritedPermissionProfileGroups().clear();
			result.getInheritedPermissionProfileGroups().addAll(in.getInheritedPermissionProfileGroups());
		}
		return result;
	}

	public static void initUserDefaultValues(UserInVO in, UserOutVO user) {
		if (in != null) {
			in.setDepartmentId(user == null ? null : user.getDepartment().getId());
			in.setId(null);
			in.setIdentityId(null);
			in.setParentId(null);
			in.setLocale(Settings.getString(SettingCodes.USER_LOCALE_PRESET, Bundle.SETTINGS, DefaultSettings.USER_LOCALE_PRESET));
			in.setShowTooltips(Settings.getBoolean(SettingCodes.USER_SHOW_TOOLTIPS_PRESET, Bundle.SETTINGS, DefaultSettings.USER_SHOW_TOOLTIPS_PRESET));
			in.setLocked(Settings.getBoolean(SettingCodes.USER_LOCKED_PRESET, Bundle.SETTINGS, DefaultSettings.USER_LOCKED_PRESET));
			in.setLockedUntrusted(Settings.getBoolean(SettingCodes.USER_LOCKED_UNTRUSTED_PRESET, Bundle.SETTINGS, DefaultSettings.USER_LOCKED_UNTRUSTED_PRESET));
			in.setDecrypt(Settings.getBoolean(SettingCodes.USER_DECRYPT_PRESET, Bundle.SETTINGS, DefaultSettings.USER_DECRYPT_PRESET));
			in.setDecryptUntrusted(Settings.getBoolean(SettingCodes.USER_DECRYPT_UNTRUSTED_PRESET, Bundle.SETTINGS, DefaultSettings.USER_DECRYPT_UNTRUSTED_PRESET));
			in.setEnableInventoryModule(
					Settings.getBoolean(SettingCodes.USER_ENABLE_INVENTORY_MODULE_PRESET, Bundle.SETTINGS, DefaultSettings.USER_ENABLE_INVENTORY_MODULE_PRESET));
			in.setVisibleInventoryTabList(Settings.getString(SettingCodes.VISIBLE_INVENTORY_TAB_LIST_PRESET, Bundle.SETTINGS, DefaultSettings.VISIBLE_INVENTORY_TAB_LIST_PRESET));
			in.setEnableStaffModule(Settings.getBoolean(SettingCodes.USER_ENABLE_STAFF_MODULE_PRESET, Bundle.SETTINGS, DefaultSettings.USER_ENABLE_STAFF_MODULE_PRESET));
			in.setVisibleStaffTabList(Settings.getString(SettingCodes.VISIBLE_STAFF_TAB_LIST_PRESET, Bundle.SETTINGS, DefaultSettings.VISIBLE_STAFF_TAB_LIST_PRESET));
			in.setEnableCourseModule(Settings.getBoolean(SettingCodes.USER_ENABLE_COURSE_MODULE_PRESET, Bundle.SETTINGS, DefaultSettings.USER_ENABLE_COURSE_MODULE_PRESET));
			in.setVisibleCourseTabList(Settings.getString(SettingCodes.VISIBLE_COURSE_TAB_LIST_PRESET, Bundle.SETTINGS, DefaultSettings.VISIBLE_COURSE_TAB_LIST_PRESET));
			in.setEnableTrialModule(Settings.getBoolean(SettingCodes.USER_ENABLE_TRIAL_MODULE_PRESET, Bundle.SETTINGS, DefaultSettings.USER_ENABLE_TRIAL_MODULE_PRESET));
			in.setVisibleTrialTabList(Settings.getString(SettingCodes.VISIBLE_TRIAL_TAB_LIST_PRESET, Bundle.SETTINGS, DefaultSettings.VISIBLE_TRIAL_TAB_LIST_PRESET));
			in.setEnableInputFieldModule(
					Settings.getBoolean(SettingCodes.USER_ENABLE_INPUT_FIELD_MODULE_PRESET, Bundle.SETTINGS, DefaultSettings.USER_ENABLE_INPUT_FIELD_MODULE_PRESET));
			in.setVisibleInputFieldTabList(
					Settings.getString(SettingCodes.VISIBLE_INPUT_FIELD_TAB_LIST_PRESET, Bundle.SETTINGS, DefaultSettings.VISIBLE_INPUT_FIELD_TAB_LIST_PRESET));
			in.setEnableProbandModule(Settings.getBoolean(SettingCodes.USER_ENABLE_PROBAND_MODULE_PRESET, Bundle.SETTINGS, DefaultSettings.USER_ENABLE_PROBAND_MODULE_PRESET));
			in.setVisibleProbandTabList(Settings.getString(SettingCodes.VISIBLE_PROBAND_TAB_LIST_PRESET, Bundle.SETTINGS, DefaultSettings.VISIBLE_PROBAND_TAB_LIST_PRESET));
			in.setEnableMassMailModule(Settings.getBoolean(SettingCodes.USER_ENABLE_MASS_MAIL_MODULE_PRESET, Bundle.SETTINGS, DefaultSettings.USER_ENABLE_MASS_MAIL_MODULE_PRESET));
			in.setVisibleMassMailTabList(Settings.getString(SettingCodes.VISIBLE_MASS_MAIL_TAB_LIST_PRESET, Bundle.SETTINGS, DefaultSettings.VISIBLE_MASS_MAIL_TAB_LIST_PRESET));
			in.setEnableUserModule(Settings.getBoolean(SettingCodes.USER_ENABLE_USER_MODULE_PRESET, Bundle.SETTINGS, DefaultSettings.USER_ENABLE_USER_MODULE_PRESET));
			in.setVisibleUserTabList(Settings.getString(SettingCodes.VISIBLE_USER_TAB_LIST_PRESET, Bundle.SETTINGS, DefaultSettings.VISIBLE_USER_TAB_LIST_PRESET));
			in.setAuthMethod(Settings.getAuthenticationType(SettingCodes.USER_AUTH_METHOD_PRESET, Bundle.SETTINGS, DefaultSettings.USER_AUTH_METHOD_PRESET));
			in.setName(Messages.getString(MessageCodes.USER_NAME_PRESET));
			in.setTimeZone(Settings.getString(SettingCodes.USER_TIME_ZONE_PRESET, Bundle.SETTINGS, DefaultSettings.USER_TIME_ZONE_PRESET));
			in.setDateFormat(Messages.getString(MessageCodes.USER_DATE_FORMAT_PRESET));
			in.setDecimalSeparator(Messages.getString(MessageCodes.USER_DECIMAL_SEPARATOR_PRESET));
			in.setVersion(null);
			in.setTheme(Settings.getString(SettingCodes.USER_THEME_PRESET, Bundle.SETTINGS, DefaultSettings.USER_THEME_PRESET));
			in.getInheritedProperties().clear();
			in.getInheritedPermissionProfileGroups().clear();
		}
	}

	private UserInVO in;
	private UserOutVO out;
	private String remoteUserMessage;
	private Boolean remoteUserOk;
	private LdapEntryVO ldapEntry1;
	private LdapEntryVO ldapEntry2;
	private ArrayList<SelectItem> departments;
	private AuthenticationTypeSelector authMethod;
	private HashMap<String, Long> tabCountMap;
	private HashMap<String, String> tabTitleMap;
	private String deferredDeleteReason;
	private String newDepartmentPassword;
	private String oldDepartmentPassword;
	private boolean decryptFromUntrustedHosts;
	private TreeNode userRoot;

	public UserBean() {
		super();
		tabCountMap = new HashMap<String, Long>();
		tabTitleMap = new HashMap<String, String>();
		setAuthMethod(new AuthenticationTypeSelector(this, AUTH_METHOD_PROPERTY_ID));
		remoteUserMessage = null;
		remoteUserOk = null;
		ldapEntry1 = null;
		ldapEntry2 = null;
		decryptFromUntrustedHosts = false;
		DefaultTreeNode userRoot = new DefaultTreeNode("user_root", null);
		userRoot.setExpanded(true);
		userRoot.setType(WebUtil.PARENT_NODE_TYPE);
		this.userRoot = userRoot;
		try {
			decryptFromUntrustedHosts = WebUtil.getServiceLocator().getToolsService().isDecryptFromUntrustedHosts();
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
	}

	public boolean isDecryptFromUntrustedHosts() {
		return decryptFromUntrustedHosts;
	}

	@Override
	public String addAction() {
		UserInVO backup = new UserInVO(in);
		in.setId(null);
		in.setVersion(null);
		sanitizeInVals();
		try {
			out = WebUtil.getServiceLocator().getUserService().addUser(WebUtil.getAuthentication(), in, newDepartmentPassword,
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_USER_INSTANCES, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_USER_INSTANCES),
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_USER_PARENT_DEPTH, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_USER_PARENT_DEPTH),
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_USER_CHILDREN_DEPTH, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_USER_CHILDREN_DEPTH));
			initIn();
			initSets();
			addOperationSuccessMessage(MessageCodes.ADD_OPERATION_SUCCESSFUL);
			return ADD_OUTCOME;
		} catch (ServiceException | IllegalArgumentException | AuthorisationException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} finally {
			newDepartmentPassword = null;
			oldDepartmentPassword = null;
		}
		return ERROR_OUTCOME;
	}

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess, String outcome) {
		if ((UPDATE_OUTCOME.equals(outcome) || DELETE_OUTCOME.equals(outcome)) && WebUtil.isPropertiesDescendantLoggedIn(in.getId())) {
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
				out = WebUtil.getServiceLocator().getUserService().getUser(WebUtil.getAuthentication(), id,
						Settings.getIntNullable(SettingCodes.GRAPH_MAX_USER_INSTANCES, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_USER_INSTANCES),
						Settings.getIntNullable(SettingCodes.GRAPH_MAX_USER_PARENT_DEPTH, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_USER_PARENT_DEPTH),
						Settings.getIntNullable(SettingCodes.GRAPH_MAX_USER_CHILDREN_DEPTH, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_USER_CHILDREN_DEPTH));
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
				Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			} catch (AuthenticationException e) {
				Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
				WebUtil.publishException(e);
			}
		}
		initIn();
		initSets();
		return CHANGE_OUTCOME;
	}

	public List<LdapEntryVO> completeLdapEntry1(String query) {
		try {
			return (List<LdapEntryVO>) WebUtil.getServiceLocator().getToolsService().completeLdapEntry1(WebUtil.getAuthentication(), query, null);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
		return new ArrayList<LdapEntryVO>();
	}

	public List<LdapEntryVO> completeLdapEntry2(String query) {
		try {
			return (List<LdapEntryVO>) WebUtil.getServiceLocator().getToolsService().completeLdapEntry2(WebUtil.getAuthentication(), query, null);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
		return new ArrayList<LdapEntryVO>();
	}

	public void changeByNode() {
		Long userId = WebUtil.getLongParamValue(GetParamNames.USER_ID);
		if (userId != null) {
			change(userId.toString());
		} else {
			this.out = null;
			this.initIn();
			initSets();
			appendRequestContextCallbackArgs(true);
		}
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
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_USER_INSTANCES, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_USER_INSTANCES),
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_USER_PARENT_DEPTH, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_USER_PARENT_DEPTH),
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_USER_CHILDREN_DEPTH, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_USER_CHILDREN_DEPTH));
			initIn();
			initSets();
			if (!out.getDeferredDelete()) {
				addOperationSuccessMessage(MessageCodes.DELETE_OPERATION_SUCCESSFUL);
			}
			out = null;
			return DELETE_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	private UserOutVO findUserRoot(UserOutVO user) {
		if (user.getParent() == null) {
			return user;
		} else {
			return findUserRoot(user.getParent());
		}
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

	public TreeNode getUserRoot() {
		return userRoot;
	}

	public String getUserTreeLabel() {
		Integer graphMaxUserInstances = Settings.getIntNullable(SettingCodes.GRAPH_MAX_USER_INSTANCES, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_USER_INSTANCES);
		Integer graphMaxUserParentDepth = Settings.getIntNullable(SettingCodes.GRAPH_MAX_USER_PARENT_DEPTH, Bundle.SETTINGS,
				DefaultSettings.GRAPH_MAX_USER_PARENT_DEPTH);
		Integer graphMaxUserChildrenDepth = Settings.getIntNullable(SettingCodes.GRAPH_MAX_USER_CHILDREN_DEPTH, Bundle.SETTINGS,
				DefaultSettings.GRAPH_MAX_USER_CHILDREN_DEPTH);
		if (graphMaxUserInstances == null && graphMaxUserParentDepth == null && graphMaxUserChildrenDepth == null) {
			return Messages.getString(MessageCodes.USER_TREE_LABEL);
		} else if (graphMaxUserInstances != null && graphMaxUserParentDepth == null && graphMaxUserChildrenDepth == null) {
			return Messages.getMessage(MessageCodes.USER_TREE_MAX_LABEL, graphMaxUserInstances);
		} else if (graphMaxUserInstances == null && (graphMaxUserParentDepth != null || graphMaxUserChildrenDepth != null)) {
			return Messages.getMessage(MessageCodes.USER_TREE_LEVELS_LABEL, graphMaxUserParentDepth != null ? graphMaxUserParentDepth : "\u221E",
					graphMaxUserChildrenDepth != null ? graphMaxUserChildrenDepth : "\u221E");
		} else {
			return Messages.getMessage(MessageCodes.USER_TREE_MAX_LEVELS_LABEL, graphMaxUserInstances,
					graphMaxUserParentDepth != null ? graphMaxUserParentDepth : "\u221E",
					graphMaxUserChildrenDepth != null ? graphMaxUserChildrenDepth : "\u221E");
		}
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

	public String getParentName() {
		return WebUtil.userIdToName(in.getParentId());
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

	@Override
	public String getTitle() {
		return getTitle(WebUtil.getLongParamValue(GetParamNames.USER_ID) == null);
	}

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

	protected void initSpecificSets() {
		newDepartmentPassword = null;
		oldDepartmentPassword = null;
		tabCountMap.clear();
		tabTitleMap.clear();
		PSFVO psf = new PSFVO();
		psf.setPageSize(0);
		Long count = null;
		if (out != null) {
			try {
				count = WebUtil.getServiceLocator().getUserService().getPassword(WebUtil.getAuthentication(), in.getId()) != null ? 1l : 0l;
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		tabCountMap.put(JSValues.AJAX_PASSWORD_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_PASSWORD_COUNT.toString(), WebUtil.getTabTitleString(MessageCodes.PASSWORD_TAB_TITLE, MessageCodes.PASSWORD_TAB_TITLE_WITH_COUNT, count));
		count = null;
		if (out != null) {
			try {
				Collection<UserPermissionProfileOutVO> userPermissionProfilesOut = WebUtil.getServiceLocator().getUserService()
						.getPermissionProfiles(WebUtil.getAuthentication(), in.getId(), null, null, true);
				count = userPermissionProfilesOut == null ? null : new Long(userPermissionProfilesOut.size());
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		tabCountMap.put(JSValues.AJAX_USER_PERMISSION_PROFILE_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_USER_PERMISSION_PROFILE_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.USER_PERMISSION_PROFILES_TAB_TITLE, MessageCodes.USER_PERMISSION_PROFILES_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null : WebUtil.getJournalCount(JournalModule.USER_JOURNAL, in.getId()));
		tabCountMap.put(JSValues.AJAX_USER_JOURNAL_ENTRY_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_USER_JOURNAL_ENTRY_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.USER_JOURNAL_TAB_TITLE, MessageCodes.USER_JOURNAL_TAB_TITLE_WITH_COUNT, count));
		userRoot.getChildren().clear();
		if (out != null) {
			Integer maxDepth = Settings.getIntNullable(SettingCodes.GRAPH_MAX_USER_PARENT_DEPTH, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_USER_PARENT_DEPTH);
			try {
				maxDepth += Settings.getIntNullable(SettingCodes.GRAPH_MAX_USER_CHILDREN_DEPTH, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_USER_CHILDREN_DEPTH);
			} catch (NullPointerException e) {
				maxDepth = null;
			}
			userOutVOtoTreeNode(findUserRoot(out), userRoot, out, new ArrayList<IDVOTreeNode>(),
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_USER_INSTANCES, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_USER_INSTANCES),
					maxDepth, null, 0);
		} else {
			IDVOTreeNode loose = new IDVOTreeNode(createUserOutFromIn(in), userRoot);
			loose.setType(WebUtil.LEAF_NODE_TYPE);
		}
		ldapEntry1 = null;
		ldapEntry2 = null;
		loadRemoteUserInfo();
		departments = WebUtil.getVisibleDepartments(in.getDepartmentId());
		if (WebUtil.isPropertiesDescendantLoggedIn(in.getId())) {
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.EDITING_ANCESTOR_OF_ACTIVE_USER);
		}
		deferredDeleteReason = (out == null ? null : out.getDeferredDeleteReason());
		if (out != null && out.isDeferredDelete()) {
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

	private IDVOTreeNode userOutVOtoTreeNode(UserOutVO user, TreeNode parent, UserOutVO selected, ArrayList<IDVOTreeNode> nodes, Integer limit,
			Integer maxDepth, ArrayList<Object[]> deferred, int depth) {
		if ((limit == null || nodes.size() < limit.intValue()) && (maxDepth == null || depth <= maxDepth.intValue())) {
			IDVOTreeNode node = new IDVOTreeNode(user, parent);
			nodes.add(node);
			if (selected != null && user.getId() == selected.getId()) {
				node.setSelected(true);
				parent.setExpanded(true);
			}
			if (user.getChildrenCount() > 0L) {
				node.setType(WebUtil.PARENT_NODE_TYPE);
			} else {
				node.setType(WebUtil.LEAF_NODE_TYPE);
			}
			node.setSelectable(true);
			Collection<UserOutVO> children = user.getChildren();
			Iterator<UserOutVO> it = children.iterator();
			if (Settings.getBoolean(SettingCodes.GRAPH_USER_BREADTH_FIRST, Bundle.SETTINGS, DefaultSettings.GRAPH_USER_BREADTH_FIRST)) {
				if (deferred == null) {
					deferred = new ArrayList<Object[]>(children.size());
					while (it.hasNext()) {
						userOutVOtoTreeNode(it.next(), node, selected, nodes, limit, maxDepth, deferred, depth + 1);
					}
					Iterator<Object[]> deferredIt = deferred.iterator();
					while (deferredIt.hasNext()) {
						Object[] newNode = deferredIt.next();
						userOutVOtoTreeNode((UserOutVO) newNode[0], (IDVOTreeNode) newNode[1], selected, nodes, limit, maxDepth, null, (Integer) newNode[2]);
					}
				} else {
					while (it.hasNext()) {
						Object[] newNode = new Object[3];
						newNode[0] = it.next();
						newNode[1] = node;
						newNode[2] = depth + 1;
						deferred.add(newNode);
					}
				}
			} else {
				while (it.hasNext()) {
					userOutVOtoTreeNode(it.next(), node, selected, nodes, limit, maxDepth, null, depth + 1);
				}
			}
			return node;
		}
		return null;
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
		return WebUtil.getModuleEnabled(DBModule.USER_DB) && isCreated() && !WebUtil.isPropertiesDescendantLoggedIn(in.getId());
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
			out = WebUtil.getServiceLocator().getUserService().getUser(WebUtil.getAuthentication(), id,
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_USER_INSTANCES, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_USER_INSTANCES),
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_USER_PARENT_DEPTH, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_USER_PARENT_DEPTH),
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_USER_CHILDREN_DEPTH, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_USER_CHILDREN_DEPTH));
			return LOAD_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
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
					} catch (AuthorisationException | IllegalArgumentException e) {
						remoteUserMessage = e.getMessage();
					}
					break;
				default:
			}
		}
	}

	@Override
	public String resetAction() {
		out = null;
		initIn();
		initSets();
		return RESET_OUTCOME;
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

	@Override
	public String updateAction() {
		UserInVO backup = new UserInVO(in);
		sanitizeInVals();
		try {
			out = WebUtil.getServiceLocator().getUserService().updateUser(WebUtil.getAuthentication(), in, newDepartmentPassword, oldDepartmentPassword,
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_USER_INSTANCES, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_USER_INSTANCES),
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_USER_PARENT_DEPTH, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_USER_PARENT_DEPTH),
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_USER_CHILDREN_DEPTH, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_USER_CHILDREN_DEPTH));
			initIn();
			initSets();
			addOperationSuccessMessage(MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
			return UPDATE_OUTCOME;
		} catch (ServiceException | IllegalArgumentException | AuthorisationException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} finally {
			newDepartmentPassword = null;
			oldDepartmentPassword = null;
		}
		return ERROR_OUTCOME;
	}

	@Override
	protected Long getUserId() {
		return in != null ? in.getId() : null;
	}

	@Override
	protected Long getParentId() {
		return in != null ? in.getParentId() : null;
	}

	@Override
	protected String getUserTimeZone() {
		return in != null ? in.getTimeZone() : null;
	}

	@Override
	public String getLocale() {
		return in != null ? in.getLocale() : null;
	}

	@Override
	public String getDateFormat() {
		return in != null ? in.getDateFormat() : null;
	}

	@Override
	public String getDecimalSeparator() {
		return in != null ? in.getDecimalSeparator() : null;
	}

	@Override
	public String getTheme() {
		return in != null ? in.getTheme() : null;
	}

	@Override
	public boolean isShowTooltips() {
		return in != null ? in.getShowTooltips() : false;
	}

	@Override
	protected void setUserTimeZone(String timeZoneID) {
		if (in != null) {
			in.setTimeZone(timeZoneID);
		}
	}

	@Override
	public void setLocale(String locale) {
		if (in != null) {
			in.setLocale(locale);
		}
	}

	@Override
	public void setDateFormat(String dateFormat) {
		if (in != null) {
			in.setDateFormat(dateFormat);
		}
	}

	@Override
	public void setDecimalSeparator(String decimalSeparator) {
		if (in != null) {
			in.setDecimalSeparator(decimalSeparator);
		}
	}

	@Override
	public void setTheme(String theme) {
		if (in != null) {
			in.setTheme(theme);
		}
	}

	@Override
	public void setShowTooltips(boolean showTooltips) {
		if (in != null) {
			in.setShowTooltips(showTooltips);
		}
	}

	public String getNewDepartmentPassword() {
		return newDepartmentPassword;
	}

	public void setNewDepartmentPassword(String newDepartmentPassword) {
		this.newDepartmentPassword = newDepartmentPassword;
	}

	public String getOldDepartmentPassword() {
		return oldDepartmentPassword;
	}

	public void setOldDepartmentPassword(String oldDepartmentPassword) {
		this.oldDepartmentPassword = oldDepartmentPassword;
	}

	public boolean isOldDepartmentPasswordRequired() {
		return out != null
				? !out.getDepartment().getId().equals(in != null ? in.getDepartmentId() : null)
						&& !out.getDepartment().getId().equals(WebUtil.getUser().getDepartment().getId())
				: false;
	}

	public boolean isNewDepartmentPasswordRequired() {
		return in != null ? !WebUtil.getUser().getDepartment().getId().equals(in.getDepartmentId()) : false;
	}

	protected void sanitizeInVals() {
		super.sanitizeInVals();
		if (!isNewDepartmentPasswordRequired()) {
			newDepartmentPassword = null;
		}
		if (!isOldDepartmentPasswordRequired()) {
			if (out != null && out.getDepartment().getId().equals(in != null ? in.getDepartmentId() : null)) {
				oldDepartmentPassword = newDepartmentPassword;
			} else {
				oldDepartmentPassword = null;
			}
		}
	}

	public String getNewDepartmentPasswordLabel() {
		return Messages.getMessage(MessageCodes.USER_NEW_DEPARTMENT_PASSWORD_LABEL,
				(in != null && in.getDepartmentId() != null) ? WebUtil.getDepartment(in.getDepartmentId()).getName() : null);
	}

	public String getOldDepartmentPasswordLabel() {
		return Messages.getMessage(MessageCodes.USER_OLD_DEPARTMENT_PASSWORD_LABEL, out != null ? out.getDepartment().getName() : null);
	}

	public List<String> getInventoryVisibleTabList() {
		return visibleTabListToList(in != null ? in.getVisibleInventoryTabList() : null);
	}

	public void setInventoryVisibleTabList(List<String> tabList) {
		if (in != null) {
			in.setVisibleInventoryTabList(visibleTabListToString(tabList));
		}
	}

	public List<String> getStaffVisibleTabList() {
		return visibleTabListToList(in != null ? in.getVisibleStaffTabList() : null);
	}

	public void setStaffVisibleTabList(List<String> tabList) {
		if (in != null) {
			in.setVisibleStaffTabList(visibleTabListToString(tabList));
		}
	}

	public List<String> getCourseVisibleTabList() {
		return visibleTabListToList(in != null ? in.getVisibleCourseTabList() : null);
	}

	public void setCourseVisibleTabList(List<String> tabList) {
		if (in != null) {
			in.setVisibleCourseTabList(visibleTabListToString(tabList));
		}
	}

	public List<String> getTrialVisibleTabList() {
		return visibleTabListToList(in != null ? in.getVisibleTrialTabList() : null);
	}

	public void setTrialVisibleTabList(List<String> tabList) {
		if (in != null) {
			in.setVisibleTrialTabList(visibleTabListToString(tabList));
		}
	}

	public List<String> getInputFieldVisibleTabList() {
		return visibleTabListToList(in != null ? in.getVisibleInputFieldTabList() : null);
	}

	public void setInputFieldVisibleTabList(List<String> tabList) {
		if (in != null) {
			in.setVisibleInputFieldTabList(visibleTabListToString(tabList));
		}
	}

	public List<String> getProbandVisibleTabList() {
		return visibleTabListToList(in != null ? in.getVisibleProbandTabList() : null);
	}

	public void setProbandVisibleTabList(List<String> tabList) {
		if (in != null) {
			in.setVisibleProbandTabList(visibleTabListToString(tabList));
		}
	}

	public List<String> getMassMailVisibleTabList() {
		return visibleTabListToList(in != null ? in.getVisibleMassMailTabList() : null);
	}

	public void setMassMailVisibleTabList(List<String> tabList) {
		if (in != null) {
			in.setVisibleMassMailTabList(visibleTabListToString(tabList));
		}
	}

	public List<String> getUserVisibleTabList() {
		return visibleTabListToList(in != null ? in.getVisibleUserTabList() : null);
	}

	public void setUserVisibleTabList(List<String> tabList) {
		if (in != null) {
			in.setVisibleUserTabList(visibleTabListToString(tabList));
		}
	}

	private static String visibleTabListToString(List<String> tabList) {
		StringBuilder result = new StringBuilder();
		if (tabList != null && tabList.size() > 0) {
			Iterator<String> it = tabList.iterator();
			while (it.hasNext()) {
				if (result.length() > 0) {
					result.append(WebUtil.TAB_ID_SEPARATOR_STRING);
				}
				result.append(it.next());
			}
		}
		return result.toString();
	}

	@Override
	protected Collection<String> getInheritedProperties() {
		return in != null ? in.getInheritedProperties() : null;
	}
}
