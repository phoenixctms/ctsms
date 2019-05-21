package org.phoenixctms.ctsms.web.model.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.enumeration.PermissionProfile;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.PermissionProfileVO;
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

	private Collection<UserPermissionProfileOutVO> userPermissionProfilesOut;
	UserPermissionProfileModel userPermissionProfileModel;
	private Long userId;

	public UserPermissionProfileBean() {
		super();
		userPermissionProfileModel = new UserPermissionProfileModel();
	}

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess, String outcome) {
		if (UPDATE_OUTCOME.equals(outcome) && WebUtil.isUserIdLoggedIn(userId)) {
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
			userPermissionProfilesOut = WebUtil.getServiceLocator().getUserService().getPermissionProfiles(WebUtil.getAuthentication(), id, null, null);
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
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
		if (Messages.getMessageList().isEmpty()) {
			// if (!isCreated() && userId != null) {
			// Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.NO_PERMISSION_PROFILES_SET_YET);
			// } else if (isCreated() && WebUtil.isUserLocked(userId)) {
			// Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.USER_LOCKED_LABEL);
			// } else if (WebUtil.isUserIdLoggedIn(userId)) {
			// Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.EDITING_ACTIVE_USER);
			// }
			if (WebUtil.isUserIdLoggedIn(userId)) {
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.EDITING_ACTIVE_USER);
			}
			UserOutVO user = WebUtil.getUser(userId, null);
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
			userPermissionProfilesOut = WebUtil.getServiceLocator().getUserService().getPermissionProfiles(WebUtil.getAuthentication(), id, null, null);
			return LOAD_OUTCOME;
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
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

	@Override
	public String updateAction() {
		try {
			userPermissionProfilesOut = WebUtil.getServiceLocator().getUserService()
					.setPermissionProfiles(WebUtil.getAuthentication(), userPermissionProfileModel.getPermissionProfilesIn());
			initIn();
			initSets();
			addOperationSuccessMessage(MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
			return UPDATE_OUTCOME;
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}
}
