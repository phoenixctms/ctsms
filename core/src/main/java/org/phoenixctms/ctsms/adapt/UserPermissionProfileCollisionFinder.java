package org.phoenixctms.ctsms.adapt;

import java.util.Collection;

import org.hibernate.LockMode;

import org.phoenixctms.ctsms.domain.User;
import org.phoenixctms.ctsms.domain.UserDao;
import org.phoenixctms.ctsms.domain.UserPermissionProfile;
import org.phoenixctms.ctsms.domain.UserPermissionProfileDao;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.vo.UserPermissionProfileInVO;

public class UserPermissionProfileCollisionFinder extends CollisionFinder<UserPermissionProfileInVO, UserPermissionProfile, User> {

	private UserDao userDao;
	private UserPermissionProfileDao userPermissionProfileDao;

	public UserPermissionProfileCollisionFinder(UserDao userDao, UserPermissionProfileDao userPermissionProfileDao) {
		this.userPermissionProfileDao = userPermissionProfileDao;
		this.userDao = userDao;
	}

	@Override
	protected User aquireWriteLock(UserPermissionProfileInVO in)
			throws ServiceException {
		return CheckIDUtil.checkUserId(in.getUserId(), userDao, LockMode.PESSIMISTIC_WRITE);
	}

	@Override
	protected boolean equals(UserPermissionProfileInVO in, UserPermissionProfile existing) {
		return in.getId().equals(existing.getId());
	}

	@Override
	protected Collection<UserPermissionProfile> getCollidingItems(UserPermissionProfileInVO in, User root) {
		return userPermissionProfileDao.findByUserProfileGroup(in.getUserId(), in.getProfile(), null, null);
	}

	@Override
	protected boolean isNew(UserPermissionProfileInVO in) {
		return in.getId() == null;
	}
}
