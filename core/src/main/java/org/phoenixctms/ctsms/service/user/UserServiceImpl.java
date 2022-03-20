// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 * TEMPLATE:    SpringServiceImpl.vsl in andromda-spring cartridge
 * MODEL CLASS: AndroMDAModel::ctsms::org.phoenixctms.ctsms::service::user::UserService
 * STEREOTYPE:  Service
 */
package org.phoenixctms.ctsms.service.user;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.hibernate.LockMode;
import org.phoenixctms.ctsms.adapt.UserPermissionProfileCollisionFinder;
import org.phoenixctms.ctsms.domain.DataTableColumn;
import org.phoenixctms.ctsms.domain.DataTableColumnDao;
import org.phoenixctms.ctsms.domain.Department;
import org.phoenixctms.ctsms.domain.JournalEntry;
import org.phoenixctms.ctsms.domain.JournalEntryDao;
import org.phoenixctms.ctsms.domain.KeyPair;
import org.phoenixctms.ctsms.domain.Notification;
import org.phoenixctms.ctsms.domain.NotificationDao;
import org.phoenixctms.ctsms.domain.NotificationRecipient;
import org.phoenixctms.ctsms.domain.NotificationRecipientDao;
import org.phoenixctms.ctsms.domain.Password;
import org.phoenixctms.ctsms.domain.PasswordDao;
import org.phoenixctms.ctsms.domain.Staff;
import org.phoenixctms.ctsms.domain.StaffDao;
import org.phoenixctms.ctsms.domain.User;
import org.phoenixctms.ctsms.domain.UserDao;
import org.phoenixctms.ctsms.domain.UserPermissionProfile;
import org.phoenixctms.ctsms.domain.UserPermissionProfileDao;
import org.phoenixctms.ctsms.email.NotificationMessageTemplateParameters;
import org.phoenixctms.ctsms.enumeration.AuthenticationType;
import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.enumeration.PermissionProfileGroup;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.security.CryptoUtil;
import org.phoenixctms.ctsms.security.PasswordPolicy;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.ServiceExceptionCodes;
import org.phoenixctms.ctsms.util.ServiceUtil;
import org.phoenixctms.ctsms.util.SystemMessageCodes;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.DataTableColumnVO;
import org.phoenixctms.ctsms.vo.DataTableColumnsVO;
import org.phoenixctms.ctsms.vo.NotificationVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.PasswordInVO;
import org.phoenixctms.ctsms.vo.PasswordOutVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.UserInVO;
import org.phoenixctms.ctsms.vo.UserInheritedVO;
import org.phoenixctms.ctsms.vo.UserOutVO;
import org.phoenixctms.ctsms.vo.UserPermissionProfileInVO;
import org.phoenixctms.ctsms.vo.UserPermissionProfileOutVO;
import org.phoenixctms.ctsms.vo.UserSettingsInVO;
import org.phoenixctms.ctsms.vocycle.UserReflexionGraph;

/**
 * @see org.phoenixctms.ctsms.service.user.UserService
 */
public class UserServiceImpl
		extends UserServiceBase {

	private static void checkActivityCount(Long userId, JournalEntryDao journalEntryDao) throws ServiceException {
		Long activityCount = journalEntryDao.getActivityCount(null, userId, null, null, false);
		if (activityCount != null && activityCount > 0) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.USER_EXISTING_JOURNAL_ENTRIES, activityCount.toString());
		}
	}

	private static void checkPermissionProfileGroupConsistency(User user, UserPermissionProfileDao userPermissionProfileDao) throws ServiceException {
		PermissionProfileGroup[] profileGroups = PermissionProfileGroup.values();
		HashMap<Long, HashSet<PermissionProfileGroup>> inheritPermissionProfileGroupMap = new HashMap<Long, HashSet<PermissionProfileGroup>>();
		for (int i = 0; i < profileGroups.length; i++) {
			if (ServiceUtil.getInheritedUserPermissionProfiles(user, profileGroups[i],
					true, inheritPermissionProfileGroupMap, userPermissionProfileDao).size() != 1) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.INCONSISTENT_GROUP_PERMISSION_PROFILES, user.getName(),
						L10nUtil.getPermissionProfileGroupName(Locales.USER, profileGroups[i].name()));
			}
		}
	}

	private static String getPlainDepartmentPassword() throws Exception {
		return CoreUtil.getUserContext().getPlainDepartmentPassword();
	}

	private static JournalEntry logSystemMessage(Staff staff, UserOutVO userVO, Timestamp now, User modified, String systemMessageCode, Object result, Object original,
			JournalEntryDao journalEntryDao) throws Exception {
		return journalEntryDao.addSystemMessage(staff, now, modified, systemMessageCode, new Object[] { CommonUtil.userOutVOToString(userVO) },
				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, !CommonUtil.getUseJournalEncryption(JournalModule.STAFF_JOURNAL, null)) });
	}

	private UserPermissionProfileOutVO addUpdateUserPermissionProfile(UserPermissionProfileInVO userPermissionProfileIn, Timestamp now, User user) throws Exception {
		UserPermissionProfileDao userPermissionProfileDao = this.getUserPermissionProfileDao();
		Long id = userPermissionProfileIn.getId();
		UserPermissionProfile userPermissionProfile;
		UserPermissionProfileOutVO result;
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		if (id == null) {
			checkUserPermissionProfileInput(userPermissionProfileIn);
			userPermissionProfile = userPermissionProfileDao.userPermissionProfileInVOToEntity(userPermissionProfileIn);
			CoreUtil.modifyVersion(userPermissionProfile, now, user);
			userPermissionProfile = userPermissionProfileDao.create(userPermissionProfile);
			result = userPermissionProfileDao.toUserPermissionProfileOutVO(userPermissionProfile);
			ServiceUtil.logSystemMessage(userPermissionProfile.getUser(), result.getUser(), now, user, SystemMessageCodes.USER_PERMISSION_PROFILE_CREATED, result, null,
					journalEntryDao);
		} else {
			UserPermissionProfile originalUserPermissionProfile = CheckIDUtil.checkUserPermissionProfileId(id, userPermissionProfileDao);
			checkUserPermissionProfileInput(userPermissionProfileIn); // access original associations before evict
			if (!(originalUserPermissionProfile.getUser().getId().equals(userPermissionProfileIn.getUserId()) &&
					originalUserPermissionProfile.isActive() == userPermissionProfileIn.isActive() && originalUserPermissionProfile.getProfile().equals(userPermissionProfileIn
							.getProfile()))) {
				UserPermissionProfileOutVO original = userPermissionProfileDao.toUserPermissionProfileOutVO(originalUserPermissionProfile);
				userPermissionProfileDao.evict(originalUserPermissionProfile);
				userPermissionProfile = userPermissionProfileDao.userPermissionProfileInVOToEntity(userPermissionProfileIn);
				CoreUtil.modifyVersion(originalUserPermissionProfile, userPermissionProfile, now, user);
				userPermissionProfileDao.update(userPermissionProfile);
				result = userPermissionProfileDao.toUserPermissionProfileOutVO(userPermissionProfile);
				ServiceUtil.logSystemMessage(userPermissionProfile.getUser(), result.getUser(), now, user, SystemMessageCodes.USER_PERMISSION_PROFILE_UPDATED, result, original,
						journalEntryDao);
			} else {
				result = userPermissionProfileDao.toUserPermissionProfileOutVO(originalUserPermissionProfile);
			}
		}
		return result;
	}

	private void checkUserPermissionProfileInput(UserPermissionProfileInVO userPermissionProfileIn) throws ServiceException {
		User user = CheckIDUtil.checkUserId(userPermissionProfileIn.getUserId(), this.getUserDao());
		if ((new UserPermissionProfileCollisionFinder(this.getUserDao(), this.getUserPermissionProfileDao())).collides(userPermissionProfileIn)) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.USER_PERMISSION_PROFILE_ALREADY_EXISTS, userPermissionProfileIn.getProfile(), user.getName());
		}
	}

	@Override
	protected UserOutVO handleAddUser(AuthenticationVO auth, UserInVO newUser, String plainDepartmentPassword, Integer maxInstances, Integer maxParentDepth,
			Integer maxChildrenDepth) throws Exception {
		UserDao userDao = this.getUserDao();
		ServiceUtil.checkUsernameExists(newUser.getName(), userDao);
		if (plainDepartmentPassword == null) {
			plainDepartmentPassword = getPlainDepartmentPassword();
		}
		ServiceUtil.checkUserInput(newUser, null, plainDepartmentPassword, this.getDepartmentDao(), this.getStaffDao(), this.getUserDao());
		User user = userDao.userInVOToEntity(newUser);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User modified = CoreUtil.getUser();
		CoreUtil.modifyVersion(user, now, modified);
		ServiceUtil.createKeyPair(user, plainDepartmentPassword, this.getKeyPairDao());
		user = userDao.create(user);
		notifyUserAccount(user, null, now);
		UserOutVO result = userDao.toUserOutVO(user, maxInstances, maxParentDepth, maxChildrenDepth);
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		ServiceUtil.logSystemMessage(user, result, now, modified, SystemMessageCodes.USER_CREATED, result, null, journalEntryDao);
		Staff identity = user.getIdentity();
		if (identity != null) {
			logSystemMessage(identity, result, now, modified, SystemMessageCodes.USER_CREATED, result, null, journalEntryDao);
		}
		return result;
	}

	@Override
	protected PasswordOutVO handleAdminSetPassword(AuthenticationVO auth, Long userId,
			PasswordInVO newPassword, String plainDepartmentPassword, boolean resetOtpSecret) throws Exception {
		User user = CheckIDUtil.checkUserId(userId, this.getUserDao(), LockMode.PESSIMISTIC_WRITE);
		if (plainDepartmentPassword == null) {
			plainDepartmentPassword = getPlainDepartmentPassword();
		}
		if (!CryptoUtil.checkDepartmentPassword(user.getDepartment(), plainDepartmentPassword)) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.DEPARTMENT_PASSWORD_WRONG);
		}
		PasswordDao passwordDao = this.getPasswordDao();
		Password lastPassword = passwordDao.findLastPassword(user.getId());
		if (!PasswordPolicy.USER.isAdminIgnorePolicy()) {
			PasswordPolicy.USER.checkStrength(newPassword.getPassword());
			PasswordPolicy.USER.checkHistoryDistance(newPassword.getPassword(), lastPassword, plainDepartmentPassword);
		}
		ServiceUtil.checkLogonLimitations(newPassword);
		return ServiceUtil.createPassword(true, resetOtpSecret, passwordDao.passwordInVOToEntity(newPassword), user, new Timestamp(System.currentTimeMillis()), lastPassword,
				newPassword.getPassword(),
				plainDepartmentPassword, passwordDao, this.getJournalEntryDao());
	}

	@Override
	protected UserOutVO handleDeleteUser(AuthenticationVO auth, Long userId, boolean defer, boolean force, String deferredDeleteReason, Integer maxInstances,
			Integer maxParentDepth, Integer maxChildrenDepth) throws Exception {
		UserDao userDao = this.getUserDao();
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		User modified = CoreUtil.getUser();
		Timestamp now = new Timestamp(System.currentTimeMillis());
		UserOutVO result;
		if (!force && defer) {
			User originalUser = CheckIDUtil.checkUserId(userId, userDao);
			if (originalUser.equals(modified)) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DELETE_ACTIVE_USER);
			}
			UserOutVO original = userDao.toUserOutVO(originalUser, maxInstances, maxParentDepth, maxChildrenDepth);
			userDao.evict(originalUser);
			User user = CheckIDUtil.checkUserId(userId, userDao, LockMode.PESSIMISTIC_WRITE);
			if (CommonUtil.isEmptyString(deferredDeleteReason)) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.DEFERRED_DELETE_REASON_REQUIRED);
			}
			user.setDeferredDelete(true);
			user.setDeferredDeleteReason(deferredDeleteReason);
			CoreUtil.modifyVersion(user, user.getVersion(), now, user); // no opt. locking
			userDao.update(user);
			result = userDao.toUserOutVO(user, maxInstances, maxParentDepth, maxChildrenDepth);
			ServiceUtil.logSystemMessage(user, result, now, modified, SystemMessageCodes.USER_MARKED_FOR_DELETION, result, original, journalEntryDao);
			Staff identity = user.getIdentity();
			if (identity != null) {
				logSystemMessage(identity, result, now, modified, SystemMessageCodes.USER_MARKED_FOR_DELETION, result, original, journalEntryDao);
			}
		} else {
			User user = CheckIDUtil.checkUserId(userId, userDao, LockMode.PESSIMISTIC_WRITE);
			if (user.equals(modified)) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DELETE_ACTIVE_USER);
			}
			checkActivityCount(userId, journalEntryDao);
			result = userDao.toUserOutVO(user, maxInstances, maxParentDepth, maxChildrenDepth);
			NotificationDao notificationDao = this.getNotificationDao();
			NotificationRecipientDao notificationRecipientDao = this.getNotificationRecipientDao();
			Iterator<JournalEntry> journalEntriesIt = user.getJournalEntries().iterator();
			while (journalEntriesIt.hasNext()) {
				JournalEntry journalEntry = journalEntriesIt.next();
				journalEntry.setUser(null);
				journalEntryDao.remove(journalEntry);
			}
			user.getJournalEntries().clear();
			checkActivityCount(userId, journalEntryDao);
			PasswordDao passwordDao = this.getPasswordDao();
			Password password = passwordDao.findLastPassword(userId);
			while (password != null) {
				Password previousPassword = password.getPreviousPassword();
				password.setUser(null);
				ServiceUtil.removeNotifications(password.getNotifications(), notificationDao, notificationRecipientDao);
				passwordDao.remove(password);
				password = previousPassword;
			}
			user.getPasswords().clear();
			UserPermissionProfileDao userPermissionProfileDao = this.getUserPermissionProfileDao();
			Iterator<UserPermissionProfile> permissionProfilesIt = user.getPermissionProfiles().iterator();
			while (permissionProfilesIt.hasNext()) {
				UserPermissionProfile permissionProfile = permissionProfilesIt.next();
				permissionProfile.setUser(null);
				userPermissionProfileDao.remove(permissionProfile);
			}
			user.getPermissionProfiles().clear();
			DataTableColumnDao dataTableColumnDao = this.getDataTableColumnDao();
			Iterator<DataTableColumn> tableColumnsIt = user.getTableColumns().iterator();
			while (tableColumnsIt.hasNext()) {
				DataTableColumn tableColumn = tableColumnsIt.next();
				tableColumn.setUser(null);
				dataTableColumnDao.remove(tableColumn);
			}
			user.getTableColumns().clear();
			StaffDao staffDao = this.getStaffDao();
			Staff identity = user.getIdentity();
			if (identity != null) {
				identity.removeAccounts(user);
				user.setIdentity(null);
				staffDao.update(identity);
				StaffOutVO identityVO = staffDao.toStaffOutVO(identity);
				logSystemMessage(identity, result, now, modified, SystemMessageCodes.USER_DELETED_IDENTITY_REMOVED, null, identityVO, journalEntryDao);
			}
			ServiceUtil.removeNotifications(user.getNotifications(), notificationDao, notificationRecipientDao);
			KeyPair keyPair = user.getKeyPair();
			Iterator<User> childrenIt = user.getChildren().iterator();
			while (childrenIt.hasNext()) {
				User child = childrenIt.next();
				child.setParent(null);
				CoreUtil.modifyVersion(child, child.getVersion(), now, user);
				userDao.update(child);
				UserOutVO childVO = userDao.toUserOutVO(child);
			}
			user.getChildren().clear();
			User parent = user.getParent();
			if (parent != null) {
				parent.removeChildren(user);
				user.setParent(null);
				userDao.update(parent);
			}
			userDao.remove(user);
			this.getKeyPairDao().remove(keyPair);
			ServiceUtil.logSystemMessage(modified, result, now, modified, SystemMessageCodes.USER_DELETED, result, null, journalEntryDao);
		}
		return result;
	}

	@Override
	protected NotificationVO handleGetNotification(AuthenticationVO auth,
			Long notificationId) throws Exception {
		NotificationDao notificationDao = this.getNotificationDao();
		Notification notification = CheckIDUtil.checkNotificationId(notificationId, notificationDao);
		notificationDao.refresh(notification);
		User user = CoreUtil.getUser();
		Staff identity = user.getIdentity();
		if (identity != null) {
			boolean found = false;
			Iterator<NotificationRecipient> recipientsIt = notification.getRecipients().iterator();
			while (recipientsIt.hasNext()) {
				if (identity.equals(recipientsIt.next().getStaff())) {
					found = true;
					break;
				}
			}
			if (!found) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.NOT_A_NOTIFICATION_RECIPIENT_STAFF);
			}
		} else {
			Department department = notification.getDepartment();
			if (department == null || !user.getDepartment().equals(department)) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.NOT_A_NOTIFICATION_RECIPIENT_USER);
			}
		}
		NotificationVO result = notificationDao.toNotificationVO(notification);
		return result;
	}

	@Override
	protected long handleGetNotificationCountByDay(AuthenticationVO auth,
			Date today, Boolean obsolete, Boolean send, Boolean show, Boolean sent, Boolean dropped) throws Exception {
		User user = CoreUtil.getUser();
		Staff identity = user.getIdentity();
		NotificationDao notificationDao = this.getNotificationDao();
		Collection notifications;
		if (identity != null) {
			return notificationDao.getCountByDay(today, identity.getId(), null, obsolete, send, show, sent, dropped);
		} else {
			return notificationDao.getCountByDay(today, null, user.getDepartment().getId(), obsolete, send, show, sent, dropped);
		}
	}

	@Override
	protected Collection<NotificationVO> handleGetNotificationList(AuthenticationVO auth,
			Boolean obsolete, Boolean send, Boolean show, Boolean sent, Boolean dropped, PSFVO psf) throws Exception {
		User user = CoreUtil.getUser();
		Staff identity = user.getIdentity();
		NotificationDao notificationDao = this.getNotificationDao();
		Collection notifications;
		if (identity != null) {
			notifications = notificationDao.findByRecipient(identity.getId(), null, obsolete, send, show, sent, dropped, psf);
		} else {
			notifications = notificationDao.findByRecipient(null, user.getDepartment().getId(), obsolete, send, show, sent, dropped, psf);
		}
		notificationDao.toNotificationVOCollection(notifications);
		return notifications;
	}

	@Override
	protected PasswordOutVO handleGetPassword(AuthenticationVO auth, Long userId) throws Exception {
		User user = CheckIDUtil.checkUserId(userId, this.getUserDao());
		PasswordDao passwordDao = this.getPasswordDao();
		return passwordDao.toPasswordOutVO(passwordDao.findLastPassword(user.getId()));
	}

	@Override
	protected Collection<UserPermissionProfileOutVO> handleGetPermissionProfiles(
			AuthenticationVO auth, Long userId, PermissionProfileGroup profileGroup, Boolean active, boolean inherited) throws Exception {
		User user = CheckIDUtil.checkUserId(userId, this.getUserDao());
		UserPermissionProfileDao userPermissionProfileDao = this.getUserPermissionProfileDao();
		Collection userPermissionProfiles;
		if (inherited) {
			userPermissionProfiles = new ArrayList<UserPermissionProfile>();
			HashMap<Long, HashSet<PermissionProfileGroup>> inheritPermissionProfileGroupMap = new HashMap<Long, HashSet<PermissionProfileGroup>>();
			for (int i = 0; i < PermissionProfileGroup.values().length; i++) {
				userPermissionProfiles.addAll(ServiceUtil.getInheritedUserPermissionProfiles(user, PermissionProfileGroup.values()[i],
						active, inheritPermissionProfileGroupMap, userPermissionProfileDao));
			}
		} else {
			userPermissionProfiles = userPermissionProfileDao.findByUserProfileGroup(user.getId(), null, profileGroup, active);
		}
		userPermissionProfileDao.toUserPermissionProfileOutVOCollection(userPermissionProfiles);
		return userPermissionProfiles;
	}

	@Override
	protected UserOutVO handleGetUser(AuthenticationVO auth, Long userId, Integer maxInstances, Integer maxParentDepth, Integer maxChildrenDepth) throws Exception {
		UserDao userDao = this.getUserDao();
		User user = CheckIDUtil.checkUserId(userId, userDao);
		UserOutVO result = userDao.toUserOutVO(user, maxInstances, maxParentDepth, maxChildrenDepth);
		return result;
	}

	@Override
	protected Collection<UserOutVO> handleGetUserList(AuthenticationVO auth, Long userId, Long departmentId, Integer maxInstances, PSFVO psf) throws Exception {
		UserDao userDao = this.getUserDao();
		if (userId != null) {
			CheckIDUtil.checkUserId(userId, userDao);
		}
		if (departmentId != null) {
			CheckIDUtil.checkDepartmentId(departmentId, this.getDepartmentDao());
		}
		Collection users = userDao.findByIdDepartment(userId, departmentId, psf);
		ArrayList<UserOutVO> result = new ArrayList<UserOutVO>(users.size());
		Iterator<User> userIt = users.iterator();
		while (userIt.hasNext()) {
			result.add(userDao.toUserOutVO(userIt.next(), maxInstances));
		}
		return result;
	}

	@Override
	protected PasswordOutVO handleSetPassword(AuthenticationVO auth, String plainNewPassword, String plainOldPassword, boolean resetOtpSecret) throws Exception {
		Password lastPassword = CoreUtil.getLastPassword();
		User user = CoreUtil.getUser();
		this.getUserDao().lock(user, LockMode.PESSIMISTIC_WRITE);
		if (!AuthenticationType.LOCAL.equals(user.getAuthMethod())) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.AUTHENTICATION_TYPE_NOT_LOCAL);
		}
		if (!CryptoUtil.checkPassword(lastPassword, plainOldPassword)) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.OLD_PASSWORD_WRONG);
		}
		PasswordPolicy.USER.checkStrength(plainNewPassword);
		PasswordPolicy.USER.checkHistoryDistance(plainNewPassword, lastPassword, getPlainDepartmentPassword());
		PasswordInVO newPassword = new PasswordInVO();
		ServiceUtil.applyLogonLimitations(newPassword, lastPassword);
		ServiceUtil.checkLogonLimitations(newPassword);
		PasswordDao passwordDao = this.getPasswordDao();
		return ServiceUtil.createPassword(false, resetOtpSecret, passwordDao.passwordInVOToEntity(newPassword), user, new Timestamp(System.currentTimeMillis()), lastPassword,
				plainNewPassword,
				getPlainDepartmentPassword(), passwordDao, this.getJournalEntryDao());
	}

	@Override
	protected Collection<UserPermissionProfileOutVO> handleSetPermissionProfiles(
			AuthenticationVO auth, Set<UserPermissionProfileInVO> userPermissionProfilesIn, Set<PermissionProfileGroup> inheritedPermissionProfileGroups)
			throws Exception {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User modified = CoreUtil.getUser();
		ArrayList<UserPermissionProfileOutVO> result;
		ServiceException firstException = null;
		HashMap<String, String> errorMessagesMap = new HashMap<String, String>();
		UserDao userDao = this.getUserDao();
		HashSet<Long> userIds = new HashSet<Long>();
		if (userPermissionProfilesIn != null && userPermissionProfilesIn.size() > 0) {
			result = new ArrayList<UserPermissionProfileOutVO>(userPermissionProfilesIn.size());
			Iterator<UserPermissionProfileInVO> userPermissionProfilesInIt = userPermissionProfilesIn.iterator();
			while (userPermissionProfilesInIt.hasNext()) {
				UserPermissionProfileInVO userPermissionProfileIn = userPermissionProfilesInIt.next();
				try {
					result.add(addUpdateUserPermissionProfile(userPermissionProfileIn, now, modified));
					userIds.add(userPermissionProfileIn.getUserId());
				} catch (ServiceException e) {
					if (firstException == null) {
						firstException = e;
					}
					errorMessagesMap.put(userPermissionProfileIn.getProfile().name(), e.getMessage());
				}
			}
			if (firstException != null) {
				firstException.setData(errorMessagesMap);
				throw firstException;
			}
		} else {
			result = new ArrayList<UserPermissionProfileOutVO>();
		}
		UserPermissionProfileDao userPermissionProfileDao = this.getUserPermissionProfileDao();
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		Iterator<Long> userIdsIt = userIds.iterator();
		while (userIdsIt.hasNext()) {
			User user = userDao.load(userIdsIt.next(), LockMode.PESSIMISTIC_WRITE);
			UserOutVO original = userDao.toUserOutVO(user);
			user.setInheritedPermissionProfileGroupList(UserReflexionGraph.toInheritedPermissionProfileGroupList(inheritedPermissionProfileGroups));
			checkPermissionProfileGroupConsistency(user, userPermissionProfileDao);
			CoreUtil.modifyVersion(user, user.getVersion(), now, modified);
			userDao.update(user);
			UserOutVO userVO = userDao.toUserOutVO(user);
			ServiceUtil.logSystemMessage(user, userVO, now, modified, SystemMessageCodes.USER_UPDATED, userVO, original, journalEntryDao);
		}
		return result;
	}

	@Override
	protected boolean handleIsPropertiesAncestor(AuthenticationVO auth,
			Long userId, Long parentId) throws Exception {
		UserDao userDao = this.getUserDao();
		User user = CheckIDUtil.checkUserId(userId, userDao, LockMode.PESSIMISTIC_WRITE);
		User parent = CheckIDUtil.checkUserId(parentId, userDao);
		do {
			if (parent.equals(user)) {
				return true;
			}
			if (CommonUtil.isEmptyString(user.getInheritedPropertyList())) {
				user = null;
			} else {
				user = user.getParent();
			}
		} while (user != null);
		return false;
	}

	@Override
	protected boolean handleIsPermissionProfileGroupsAncestor(AuthenticationVO auth,
			Long userId, Long parentId) throws Exception {
		UserDao userDao = this.getUserDao();
		User user = CheckIDUtil.checkUserId(userId, userDao, LockMode.PESSIMISTIC_WRITE);
		User parent = CheckIDUtil.checkUserId(parentId, userDao);
		do {
			if (parent.equals(user)) {
				return true;
			}
			if (CommonUtil.isEmptyString(user.getInheritedPermissionProfileGroupList())) {
				user = null;
			} else {
				user = user.getParent();
			}
		} while (user != null);
		return false;
	}

	@Override
	protected PasswordOutVO handleUpdateLocale(AuthenticationVO auth,
			String newLocale) throws Exception {
		Password lastPassword = CoreUtil.getLastPassword();
		UserDao userDao = this.getUserDao();
		User originalUser = CoreUtil.getUser();
		ServiceUtil.checkLocale(newLocale);
		UserOutVO original = userDao.toUserOutVO(originalUser);
		userDao.evict(originalUser);
		User user = lastPassword.getUser();
		user.setLocale(newLocale);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User modified = user;
		CoreUtil.modifyVersion(originalUser, user, now, modified);
		userDao.update(user);
		UserOutVO modifiedVO = userDao.toUserOutVO(modified);
		PasswordOutVO result = this.getPasswordDao().toPasswordOutVO(lastPassword);
		ServiceUtil.logSystemMessage(user, modifiedVO, now, modified, SystemMessageCodes.LOCALE_UPDATED, modifiedVO, original, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected PasswordOutVO handleUpdateShowTooltips(AuthenticationVO auth,
			boolean newShowTooltips) throws Exception {
		Password lastPassword = CoreUtil.getLastPassword();
		UserDao userDao = this.getUserDao();
		User originalUser = CoreUtil.getUser();
		UserOutVO original = userDao.toUserOutVO(originalUser);
		userDao.evict(originalUser);
		User user = lastPassword.getUser();
		user.setShowTooltips(newShowTooltips);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User modified = user;
		CoreUtil.modifyVersion(originalUser, user, now, modified);
		userDao.update(user);
		UserOutVO modifiedVO = userDao.toUserOutVO(modified);
		PasswordOutVO result = this.getPasswordDao().toPasswordOutVO(lastPassword);
		ServiceUtil.logSystemMessage(user, modifiedVO, now, modified, SystemMessageCodes.SHOW_TOOLTIPS_UPDATED, modifiedVO, original, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected PasswordOutVO handleUpdateTheme(AuthenticationVO auth,
			String newTheme) throws Exception {
		Password lastPassword = CoreUtil.getLastPassword();
		UserDao userDao = this.getUserDao();
		User originalUser = CoreUtil.getUser();
		UserOutVO original = userDao.toUserOutVO(originalUser);
		userDao.evict(originalUser);
		User user = lastPassword.getUser();
		user.setTheme(newTheme);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User modified = user;
		CoreUtil.modifyVersion(originalUser, user, now, modified);
		userDao.update(user);
		UserOutVO modifiedVO = userDao.toUserOutVO(modified);
		PasswordOutVO result = this.getPasswordDao().toPasswordOutVO(lastPassword);
		ServiceUtil.logSystemMessage(user, modifiedVO, now, modified, SystemMessageCodes.THEME_UPDATED, modifiedVO, original, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected PasswordOutVO handleUpdateTimeZone(AuthenticationVO auth,
			String newTimeZone) throws Exception {
		Password lastPassword = CoreUtil.getLastPassword();
		UserDao userDao = this.getUserDao();
		User originalUser = CoreUtil.getUser();
		ServiceUtil.checkTimeZone(newTimeZone);
		UserOutVO original = userDao.toUserOutVO(originalUser);
		userDao.evict(originalUser);
		User user = lastPassword.getUser();
		user.setTimeZone(newTimeZone);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User modified = user;
		CoreUtil.modifyVersion(originalUser, user, now, modified);
		userDao.update(user);
		UserOutVO modifiedVO = userDao.toUserOutVO(modified);
		PasswordOutVO result = this.getPasswordDao().toPasswordOutVO(lastPassword);
		ServiceUtil.logSystemMessage(user, modifiedVO, now, modified, SystemMessageCodes.TIME_ZONE_UPDATED, modifiedVO, original, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected UserOutVO handleUpdateUser(AuthenticationVO auth, UserInVO modifiedUser, String plainNewDepartmentPassword, String plainOldDepartmentPassword, Integer maxInstances,
			Integer maxParentDepth, Integer maxChildrenDepth)
			throws Exception {
		UserDao userDao = this.getUserDao();
		User originalUser = CheckIDUtil.checkUserId(modifiedUser.getId(), userDao, LockMode.PESSIMISTIC_WRITE);
		if (plainNewDepartmentPassword == null) {
			plainNewDepartmentPassword = getPlainDepartmentPassword();
		}
		if (plainOldDepartmentPassword == null) {
			plainOldDepartmentPassword = getPlainDepartmentPassword();
		}
		ServiceUtil.checkUserInput(modifiedUser, originalUser, plainNewDepartmentPassword, this.getDepartmentDao(), this.getStaffDao(), this.getUserDao());
		boolean departmentChanged = !originalUser.getDepartment().getId().equals(modifiedUser.getDepartmentId());
		if (departmentChanged) {
			if (!CryptoUtil.checkDepartmentPassword(originalUser.getDepartment(), plainOldDepartmentPassword)) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.OLD_DEPARTMENT_PASSWORD_WRONG);
			}
		}
		UserOutVO original = userDao.toUserOutVO(originalUser, maxInstances, maxParentDepth, maxChildrenDepth);
		userDao.evict(originalUser);
		User user = userDao.userInVOToEntity(modifiedUser);
		checkUserLoop(user);
		if (departmentChanged) {
			ServiceUtil.updateUserDepartmentPassword(user, plainNewDepartmentPassword, plainOldDepartmentPassword, this.getKeyPairDao(), this.getPasswordDao());
		}
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User modified = CoreUtil.getUser();
		CoreUtil.modifyVersion(originalUser, user, now, modified);
		userDao.update(user);
		notifyUserAccount(user, originalUser, now);
		UserOutVO result = userDao.toUserOutVO(user, maxInstances, maxParentDepth, maxChildrenDepth);
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		ServiceUtil.logSystemMessage(user, result, now, modified, SystemMessageCodes.USER_UPDATED, result, original, journalEntryDao);
		Staff identity = user.getIdentity();
		if (identity != null) {
			logSystemMessage(identity, result, now, modified, SystemMessageCodes.USER_UPDATED, result, original, journalEntryDao);
		}
		return result;
	}

	private void checkUserLoop(User user) throws ServiceException {
		(new UserReflexionGraph(this.getUserDao())).checkGraphLoop(user, true, false);
	}

	private void notifyUserAccount(User user, User originalUser, Date now) throws Exception {
		Long passwordCount;
		if (!user.isLocked() && user.getIdentity() != null &&
				((passwordCount = this.getPasswordDao().getCount(user.getId())) == 0 || originalUser == null || !user.getAuthMethod().equals(originalUser.getAuthMethod()))) {
			Map messageParameters = CoreUtil.createEmptyTemplateModel();
			messageParameters.put(NotificationMessageTemplateParameters.NEW_USER, originalUser == null || passwordCount == 0);
			messageParameters.put(NotificationMessageTemplateParameters.LOCAL_AUTH_METHOD, AuthenticationType.LOCAL.equals(user.getAuthMethod()));
			messageParameters.put(NotificationMessageTemplateParameters.LDAP_AUTH_METHOD,
					AuthenticationType.LDAP1.equals(user.getAuthMethod()) || AuthenticationType.LDAP2.equals(user.getAuthMethod()));
			this.getNotificationDao().addNotification(user, now, messageParameters);
		} else {
			ServiceUtil.cancelNotifications(user.getNotifications(), this.getNotificationDao(), org.phoenixctms.ctsms.enumeration.NotificationType.USER_ACCOUNT);
		}
	}

	@Override
	protected DataTableColumnsVO handleSetDataTableColumns(AuthenticationVO auth, Long userId, Set<DataTableColumnVO> columns) throws Exception {
		User user = CheckIDUtil.checkUserId(userId, this.getUserDao());
		Timestamp now = new Timestamp(System.currentTimeMillis());
		DataTableColumnDao dataTableColumnDao = this.getDataTableColumnDao();
		Iterator<DataTableColumnVO> columnVOIt = columns.iterator();
		DataTableColumnsVO result = new DataTableColumnsVO();
		DataTableColumnsVO original = new DataTableColumnsVO();
		HashSet<String> tableNames = new HashSet<String>();
		while (columnVOIt.hasNext()) {
			DataTableColumnVO columnVO = columnVOIt.next();
			if (tableNames.add(columnVO.getTableName())) {
				original.getColumns().addAll(deleteDataTableColumns(user, columnVO.getTableName(), null));
			}
			DataTableColumn column = dataTableColumnDao.dataTableColumnVOToEntity(columnVO);
			column.setUser(user);
			user.addTableColumns(column);
			dataTableColumnDao.create(column);
			result.getColumns().add(dataTableColumnDao.toDataTableColumnVO(column));
		}
		ServiceUtil.logSystemMessage(user, this.getUserDao().toUserOutVO(user), now, user, SystemMessageCodes.USER_DATA_TABLE_COLUMNS_UPDATED, result, original,
				this.getJournalEntryDao());
		return result;
	}

	@Override
	protected DataTableColumnsVO handleGetDataTableColumns(AuthenticationVO auth, Long userId, String tableName, String columnName) throws Exception {
		User user = CheckIDUtil.checkUserId(userId, this.getUserDao());
		DataTableColumnDao dataTableColumnDao = this.getDataTableColumnDao();
		DataTableColumnsVO result = new DataTableColumnsVO();
		Collection columns = dataTableColumnDao.findByUserTableColumn(userId, tableName, columnName);
		dataTableColumnDao.toDataTableColumnVOCollection(columns);
		result.getColumns().addAll(columns);
		return result;
	}

	private ArrayList<DataTableColumnVO> deleteDataTableColumns(User user, String tableName, String columnName) throws Exception {
		DataTableColumnDao dataTableColumnDao = this.getDataTableColumnDao();
		Collection<DataTableColumn> columns = dataTableColumnDao.findByUserTableColumn(user.getId(), tableName, columnName);
		ArrayList<DataTableColumnVO> result = new ArrayList<DataTableColumnVO>(columns.size());
		Iterator<DataTableColumn> it = columns.iterator();
		while (it.hasNext()) {
			DataTableColumn dataTableColumn = it.next();
			result.add(dataTableColumnDao.toDataTableColumnVO(dataTableColumn));
			user.removeTableColumns(dataTableColumn);
			dataTableColumn.setUser(null);
			dataTableColumnDao.remove(dataTableColumn);
		}
		return result;
	}

	@Override
	protected DataTableColumnsVO handleClearDataTableColumns(AuthenticationVO auth, Long userId, String tableName, String columnName) throws Exception {
		User user = CheckIDUtil.checkUserId(userId, this.getUserDao());
		Timestamp now = new Timestamp(System.currentTimeMillis());
		DataTableColumnsVO result = new DataTableColumnsVO();
		result.getColumns().addAll(deleteDataTableColumns(user, tableName, columnName));
		ServiceUtil.logSystemMessage(user, this.getUserDao().toUserOutVO(user), now, user, SystemMessageCodes.USER_DATA_TABLE_COLUMNS_CLEARED, result, null,
				this.getJournalEntryDao());
		return result;
	}

	@Override
	protected long handleGetDataTableColumnCount(AuthenticationVO auth, Long userId, String tableName, String columnName) throws Exception {
		User user = CheckIDUtil.checkUserId(userId, this.getUserDao());
		return this.getDataTableColumnDao().getCount(userId, tableName, columnName);
	}

	@Override
	protected UserOutVO handleUpdateUserSettings(AuthenticationVO auth, UserSettingsInVO modifiedSettings) throws Exception {
		UserDao userDao = this.getUserDao();
		User originalUser = CheckIDUtil.checkUserId(modifiedSettings.getId(), userDao, LockMode.PESSIMISTIC_WRITE);
		ServiceUtil.checkUserSettingsInput(modifiedSettings, originalUser);
		UserOutVO original = userDao.toUserOutVO(originalUser);
		userDao.evict(originalUser);
		User user = userDao.userSettingsInVOToEntity(modifiedSettings);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User modified = CoreUtil.getUser();
		CoreUtil.modifyVersion(originalUser, user, now, modified);
		userDao.update(user);
		notifyUserAccount(user, originalUser, now);
		UserOutVO result = userDao.toUserOutVO(user);
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		ServiceUtil.logSystemMessage(user, result, now, modified, SystemMessageCodes.USER_SETTINGS_UPDATED, result, original, journalEntryDao);
		return result;
	}

	@Override
	protected UserInheritedVO handleGetInheritedUser(AuthenticationVO auth, Long userId) throws Exception {
		UserDao userDao = this.getUserDao();
		User user = CheckIDUtil.checkUserId(userId, userDao);
		UserInheritedVO result = userDao.toUserInheritedVO(user);
		return result;
	}

	@Override
	protected String handleGetOTPSecret(AuthenticationVO auth, Long userId, String plainDepartmentPassword) throws Exception {
		User user = CheckIDUtil.checkUserId(userId, this.getUserDao());
		if (plainDepartmentPassword == null) {
			plainDepartmentPassword = getPlainDepartmentPassword();
		}
		Password password = this.getPasswordDao().findLastPassword(user.getId());
		if (password != null) {
			return CryptoUtil.decryptOtpSecret(password, CryptoUtil.decryptPassword(password, plainDepartmentPassword));
		}
		return null;
	}
}