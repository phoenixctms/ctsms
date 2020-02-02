package org.phoenixctms.ctsms.vocycle;

import java.util.Collection;
import java.util.HashMap;

import org.phoenixctms.ctsms.domain.Department;
import org.phoenixctms.ctsms.domain.DepartmentDao;
import org.phoenixctms.ctsms.domain.Staff;
import org.phoenixctms.ctsms.domain.User;
import org.phoenixctms.ctsms.domain.UserDaoImpl;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.UserOutVO;

public class UserReflexionGraph extends ReflexionCycleHelper<User, UserOutVO> {

	private static final boolean LIMIT_INSTANCES = true;
	private static final boolean LIMIT_PARENTS_DEPTH = true;
	private static final boolean LIMIT_CHILDREN_DEPTH = true;
	private int maxInstances;
	private int parentDepth;
	private int childrenDepth;
	private UserDaoImpl userDaoImpl;
	private DepartmentDao departmentDao;
	public final static int DEFAULT_MAX_INSTANCES = 1;
	private final static int DEFAULT_PARENT_DEPTH = Integer.MAX_VALUE >> 1;
	private final static int DEFAULT_CHILDREN_DEPTH = Integer.MAX_VALUE >> 1;

	public UserReflexionGraph(UserDaoImpl userDaoImpl, DepartmentDao departmentDao, Integer... maxInstances) {
		this.maxInstances = maxInstances != null && maxInstances.length > 0 ? (maxInstances[0] == null ? DEFAULT_MAX_INSTANCES : maxInstances[0]) : DEFAULT_MAX_INSTANCES;
		this.parentDepth = maxInstances != null && maxInstances.length > 1 ? (maxInstances[1] == null ? DEFAULT_PARENT_DEPTH : maxInstances[1]) : DEFAULT_PARENT_DEPTH;
		this.childrenDepth = maxInstances != null && maxInstances.length > 2 ? (maxInstances[2] == null ? DEFAULT_CHILDREN_DEPTH : maxInstances[2]) : DEFAULT_CHILDREN_DEPTH;
		this.userDaoImpl = userDaoImpl;
		this.departmentDao = departmentDao;
	}

	@Override
	protected User aquireWriteLock(Long id) throws ServiceException {
		return null;
	}

	@Override
	protected Class getAVOClass() {
		return UserOutVO.class;
	}

	@Override
	protected User getChild(User source) {
		return null;
	}

	@Override
	protected Collection<User> getEntityChildren(User source) {
		return null;
	}

	@Override
	protected Long getEntityId(User source) {
		return source.getId();
	}

	@Override
	protected Collection<User> getEntityParents(User source) {
		return null;
	}

	@Override
	protected ReflexionDepth getInitialReflexionDepth() {
		return new ReflexionDepth(parentDepth, childrenDepth);
	}

	@Override
	protected int getMaxInstances() {
		return maxInstances;
	}

	@Override
	protected User getParent(User source) {
		return source.getModifiedUser();
	}

	@Override
	protected Collection<UserOutVO> getVOChildren(UserOutVO target) {
		return null;
	}

	@Override
	protected Collection<UserOutVO> getVOParents(UserOutVO target) {
		return null;
	}

	@Override
	protected boolean limitChildrenDepth() {
		return LIMIT_CHILDREN_DEPTH;
	}

	@Override
	protected boolean limitInstances() {
		return LIMIT_INSTANCES; // load entire modified user graph, traverse back to root user (created by self)
	}

	@Override
	protected boolean limitParentsDepth() {
		return LIMIT_PARENTS_DEPTH;
	}

	@Override
	protected UserOutVO newVO() {
		return new UserOutVO();
	}

	@Override
	protected void setChild(UserOutVO target, UserOutVO childVO) {
	}

	@Override
	protected void setParent(UserOutVO target, UserOutVO parentVO) {
		target.setModifiedUser(parentVO);
	}

	@Override
	protected void toVORemainingFields(User source, UserOutVO target, HashMap<Class, HashMap<Long, Object>> voMap) {
		Staff identity = source.getIdentity();
		if (identity != null) {
			if (!voMapContainsKey(StaffOutVO.class, identity.getId(), voMap)) {
				voMapPut(StaffOutVO.class, identity.getId(), new StaffOutVO(), true, voMap);
			}
			target.setIdentity((StaffOutVO) voMapGet(StaffOutVO.class, identity.getId(), voMap));
		}
		userDaoImpl.toUserOutVOBase(source, target);
		Department department = source.getDepartment();
		if (department != null) {
			target.setDepartment(departmentDao.toDepartmentVO(department));
		}
		target.setAuthMethod(L10nUtil.createAuthenticationTypeVO(Locales.USER, source.getAuthMethod()));
	}
}