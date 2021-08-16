package org.phoenixctms.ctsms.web.model.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.enumeration.PermissionProfile;
import org.phoenixctms.ctsms.enumeration.PermissionProfileGroup;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.PermissionProfileVO;
import org.phoenixctms.ctsms.vo.UserInheritedVO;
import org.phoenixctms.ctsms.vo.UserOutVO;
import org.phoenixctms.ctsms.vo.UserPermissionProfileInVO;
import org.phoenixctms.ctsms.vo.UserPermissionProfileOutVO;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.util.GetParamNames;
import org.phoenixctms.ctsms.web.util.JSValues;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.context.RequestContext;

@ManagedBean
@ViewScoped
public class UserPermissionProfileBean extends ManagedBeanBase {

	public static void copyUserPermissionProfileOutToIn(UserPermissionProfileInVO in, UserPermissionProfileOutVO out) {
		if (in != null && out != null) {
			UserOutVO userVO = out.getUser();
			PermissionProfileVO profileVO = out.getProfile();
			in.setActive(out.getActive());
			in.setId(out.getId());
			in.setProfile(profileVO == null ? null : profileVO.getProfile());
			in.setUserId(userVO == null ? null : userVO.getId());
			in.setVersion(out.getVersion());
		}
	}

	public static void initUserPermissionProfileDefaultValues(UserPermissionProfileInVO in, Long userId, PermissionProfile profile) {
		if (in != null) {
			in.setActive(false);
			in.setId(null);
			in.setProfile(profile);
			in.setUserId(userId);
			in.setVersion(null);
		}
	}

	private static final Integer GRAPH_MAX_USER_INSTANCES = 2;
	private static final Integer GRAPH_MAX_USER_PARENT_DEPTH = 1;
	private static final Integer GRAPH_MAX_USER_CHILDREN_DEPTH = 0;
	private UserOutVO user;
	private UserInheritedVO parent;
	private Collection<UserPermissionProfileOutVO> userPermissionProfilesOut;
	private UserPermissionProfileModel userPermissionProfileModel;
	private Long userId;
	private Map<String, String> inheritedPermissionProfileGroupsMap;
	private Set<String> parentPermissionProfiles;
	private Map<String, PermissionProfileVO> parentPermissionProfileVOMap;

	public UserPermissionProfileBean() {
		super();
		inheritedPermissionProfileGroupsMap = new HashMap<String, String>();
		parentPermissionProfileVOMap = new HashMap<String, PermissionProfileVO>();
		parentPermissionProfiles = new HashSet<String>();
		userPermissionProfileModel = new UserPermissionProfileModel();
	}

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess, String outcome) {
		if (UPDATE_OUTCOME.equals(outcome) && WebUtil.isPermissionProfileGroupsDescendantLoggedIn(userId)) {
			WebUtil.logout();
		} else {
			RequestContext requestContext = RequestContext.getCurrentInstance();
			if (requestContext != null) {
				requestContext.addCallbackParam(JSValues.AJAX_OPERATION_SUCCESS.toString(), operationSuccess);
				if (operationSuccess && userId != null) {
					WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_USER_PERMISSION_PROFILE_TAB_TITLE_BASE64,
							JSValues.AJAX_USER_PERMISSION_PROFILE_COUNT, MessageCodes.USER_PERMISSION_PROFILES_TAB_TITLE,
							MessageCodes.USER_PERMISSION_PROFILES_TAB_TITLE_WITH_COUNT, userPermissionProfilesOut == null ? null : new Long(userPermissionProfilesOut.size()));
					WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_USER_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_USER_JOURNAL_ENTRY_COUNT,
							MessageCodes.USER_JOURNAL_TAB_TITLE, MessageCodes.USER_JOURNAL_TAB_TITLE_WITH_COUNT,
							WebUtil.getJournalCount(JournalModule.USER_JOURNAL, userId));
				}
			}
		}
	}

	@Override
	protected String changeAction(Long id) {
		userPermissionProfilesOut = null;
		try {
			userPermissionProfilesOut = WebUtil.getServiceLocator().getUserService().getPermissionProfiles(WebUtil.getAuthentication(), id, null, null, false);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		this.userId = id;
		initIn();
		initSets();
		return CHANGE_OUTCOME;
	}

	@Override
	public String getModifiedAnnotation() {
		if (userPermissionProfilesOut != null && userPermissionProfilesOut.size() > 0) {
			Long version = null;
			UserOutVO modifiedUser = null;
			Date modifiedTimestamp = null;
			Iterator<UserPermissionProfileOutVO> it = userPermissionProfilesOut.iterator();
			while (it.hasNext()) {
				UserPermissionProfileOutVO userPermissionProfilesOutVO = it.next();
				Date tagValueModifiedTimestamp = userPermissionProfilesOutVO.getModifiedTimestamp();
				if (modifiedTimestamp == null || (tagValueModifiedTimestamp != null && modifiedTimestamp.compareTo(tagValueModifiedTimestamp) < 0)) {
					modifiedUser = userPermissionProfilesOutVO.getModifiedUser();
					modifiedTimestamp = tagValueModifiedTimestamp;
				}
				if (version == null) {
					version = userPermissionProfilesOutVO.getVersion();
				} else {
					version = Math.max(version.longValue(), userPermissionProfilesOutVO.getVersion());
				}
			}
			if (version == null || modifiedUser == null || modifiedTimestamp == null) {
				return super.getModifiedAnnotation();
			} else {
				return WebUtil.getModifiedAnnotation(version.longValue(), modifiedUser, modifiedTimestamp);
			}
		} else {
			return super.getModifiedAnnotation();
		}
	}

	public UserPermissionProfileModel getUserPermissionProfileModel() {
		return userPermissionProfileModel;
	}

	@PostConstruct
	private void init() {
		Long id = WebUtil.getLongParamValue(GetParamNames.USER_ID);
		if (id != null) {
			this.load(id);
		} else {
			initIn();
			initSets();
		}
	}

	private void initIn() {
		if (isCreated()) {
			userPermissionProfileModel.copyUserPermissionProfilesOutToIn(userId, userPermissionProfilesOut);
		} else {
			userPermissionProfileModel.initUserPermissionProfilesDefaultValues(userId);
		}
	}

	private void initSets() {
		user = WebUtil.getUser(userId, GRAPH_MAX_USER_INSTANCES, GRAPH_MAX_USER_PARENT_DEPTH, GRAPH_MAX_USER_CHILDREN_DEPTH);
		parent = null;
		inheritedPermissionProfileGroupsMap.clear();
		parentPermissionProfiles.clear();
		parentPermissionProfileVOMap.clear();
		if (user != null) {
			parent = WebUtil.getInheritedUser(user.getParent() != null ? user.getParent().getId() : null);
			Iterator<PermissionProfileGroup> profileGroupIt = user.getInheritedPermissionProfileGroups().iterator();
			while (profileGroupIt.hasNext()) {
				inheritedPermissionProfileGroupsMap.put(profileGroupIt.next().name(), Boolean.TRUE.toString());
			}
			if (parent != null) {
				Collection<UserPermissionProfileOutVO> parentUserPermissionProfiles = null;
				try {
					parentUserPermissionProfiles = WebUtil.getServiceLocator().getUserService().getPermissionProfiles(WebUtil.getAuthentication(), parent.getId(), null, true,
							true);
				} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
				} catch (AuthenticationException e) {
					WebUtil.publishException(e);
				}
				if (parentUserPermissionProfiles != null) {
					Iterator<UserPermissionProfileOutVO> PermissionProfilesIt = parentUserPermissionProfiles.iterator();
					while (PermissionProfilesIt.hasNext()) {
						UserPermissionProfileOutVO permissionProfile = PermissionProfilesIt.next();
						parentPermissionProfiles.add(permissionProfile.getProfile().getProfile().name());
						parentPermissionProfileVOMap.put(permissionProfile.getProfile().getProfileGroup().getProfileGroup().name(), permissionProfile.getProfile());
					}
				}
			}
		}
		if (Messages.getMessageList().isEmpty()) {
			if (WebUtil.isPermissionProfileGroupsDescendantLoggedIn(userId)) {
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.EDITING_ANCESTOR_OF_ACTIVE_USER);
			}
			ArrayList<String> messageCodes = new ArrayList<String>();
			if (WebUtil.getUserAuthMessages(user, null, messageCodes)) {
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
	}

	@Override
	public boolean isCreateable() {
		return false;
	}

	@Override
	public boolean isCreated() {
		return userPermissionProfilesOut != null && userPermissionProfilesOut.size() > 0;
	}

	@Override
	public boolean isEditable() {
		return userId != null;
	}

	@Override
	public boolean isRemovable() {
		return false;
	}

	@Override
	public String loadAction() {
		return loadAction(this.userId);
	}

	@Override
	public String loadAction(Long id) {
		userPermissionProfilesOut = null;
		try {
			userPermissionProfilesOut = WebUtil.getServiceLocator().getUserService().getPermissionProfiles(WebUtil.getAuthentication(), id, null, null, false);
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

	private Set<PermissionProfileGroup> getInheritedPermissionProfileGroups() {
		HashSet<PermissionProfileGroup> result = new HashSet<PermissionProfileGroup>();
		if (parent != null) {
			Iterator<Entry<String, String>> it = inheritedPermissionProfileGroupsMap.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, String> inheritedPermissionProfileGroup = it.next();
				if (Boolean.parseBoolean(inheritedPermissionProfileGroup.getValue())) {
					result.add(PermissionProfileGroup.fromString(inheritedPermissionProfileGroup.getKey()));
				}
			}
		}
		return result;
	}

	public Map<String, String> getInheritedPermissionProfileGroupsMap() {
		return inheritedPermissionProfileGroupsMap;
	}

	@Override
	public String updateAction() {
		try {
			userPermissionProfilesOut = WebUtil.getServiceLocator().getUserService()
					.setPermissionProfiles(WebUtil.getAuthentication(), userPermissionProfileModel.getPermissionProfilesIn(), getInheritedPermissionProfileGroups());
			initIn();
			initSets();
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

	public Set<String> getParentPermissionProfiles() {
		return parentPermissionProfiles;
	}

	public String getInheritedPermissionProfileGroupTooltip(String profileGroup) {
		if (parentPermissionProfileVOMap.containsKey(profileGroup)) {
			return Messages.getMessage(MessageCodes.INHERITED_USER_PERMISSION_PROFILE_TOOLTIP, parentPermissionProfileVOMap.get(profileGroup).getProfileName());
		}
		return Messages.getString(MessageCodes.INHERIT_USER_PERMISSION_PROFILE_TOOLTIP);
	}
}
