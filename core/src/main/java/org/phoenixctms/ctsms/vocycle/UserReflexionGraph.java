package org.phoenixctms.ctsms.vocycle;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.hibernate.LockMode;
import org.phoenixctms.ctsms.compare.UserComparator;
import org.phoenixctms.ctsms.domain.Department;
import org.phoenixctms.ctsms.domain.DepartmentDao;
import org.phoenixctms.ctsms.domain.Staff;
import org.phoenixctms.ctsms.domain.User;
import org.phoenixctms.ctsms.domain.UserDao;
import org.phoenixctms.ctsms.domain.UserDaoImpl;
import org.phoenixctms.ctsms.enumeration.PermissionProfileGroup;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.DefaultMessages;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.MessageCodes;
import org.phoenixctms.ctsms.util.ServiceExceptionCodes;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.UserOutVO;

public class UserReflexionGraph extends ReflexionCycleHelper<User, UserOutVO> {

	private static final boolean LIMIT_INSTANCES = true;
	private static final boolean LIMIT_PARENTS_DEPTH = true;
	private static final boolean LIMIT_CHILDREN_DEPTH = true;
	private int maxInstances;
	private int parentDepth;
	private int childrenDepth;
	private UserDao userDao;
	private UserDaoImpl userDaoImpl;
	private DepartmentDao departmentDao;
	public final static int DEFAULT_MAX_INSTANCES = 1;
	private final static int DEFAULT_PARENT_DEPTH = Integer.MAX_VALUE >> 1;
	private final static int DEFAULT_CHILDREN_DEPTH = Integer.MAX_VALUE >> 1;
	private final static String STRING_LIST_SEPARATOR = ",";
	private final static Pattern STRING_LIST_REGEXP = Pattern.compile(Pattern.quote(STRING_LIST_SEPARATOR));

	public UserReflexionGraph(UserDao userDao) {
		this.userDao = userDao;
	}

	public UserReflexionGraph(UserDaoImpl userDaoImpl, DepartmentDao departmentDao, Integer... maxInstances) {
		this.maxInstances = maxInstances != null && maxInstances.length > 0 ? (maxInstances[0] == null ? DEFAULT_MAX_INSTANCES : maxInstances[0]) : DEFAULT_MAX_INSTANCES;
		this.parentDepth = maxInstances != null && maxInstances.length > 1 ? (maxInstances[1] == null ? DEFAULT_PARENT_DEPTH : maxInstances[1]) : DEFAULT_PARENT_DEPTH;
		this.childrenDepth = maxInstances != null && maxInstances.length > 2 ? (maxInstances[2] == null ? DEFAULT_CHILDREN_DEPTH : maxInstances[2]) : DEFAULT_CHILDREN_DEPTH;
		this.userDaoImpl = userDaoImpl;
		this.departmentDao = departmentDao;
	}

	@Override
	protected User aquireWriteLock(Long id) throws ServiceException {
		return userDao.load(id, LockMode.PESSIMISTIC_WRITE);
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
		Collection<User> children = source.getChildren();
		if (children.size() > 1) {
			TreeSet<User> result = new TreeSet<User>(new UserComparator());
			result.addAll(children);
			return result;
		} else {
			return children;
		}
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
		return source.getParent();
	}

	@Override
	protected Collection<UserOutVO> getVOChildren(UserOutVO target) {
		return target.getChildren();
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
		return LIMIT_INSTANCES;
		// load entire modified user graph, traverse back to root user (created by self)
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
		target.setParent(parentVO);
	}

	@Override
	protected void throwGraphLoopException(List<User> path) throws ServiceException {
		Iterator<User> it = path.iterator();
		StringBuilder sb = new StringBuilder();
		while (it.hasNext()) {
			appendLoopPath(sb, L10nUtil.getMessage(MessageCodes.LOOP_PATH_USER_LABEL, DefaultMessages.LOOP_PATH_USER_LABEL,
					CommonUtil.userOutVOToString(userDao.toUserOutVO(it.next()))));
		}
		throw L10nUtil.initServiceException(ServiceExceptionCodes.USER_GRAPH_LOOP, sb.toString());
	}

	private void toVORemainingFields(User source, UserOutVO target, boolean loadModifiedUser, HashMap<Class, HashMap<Long, Object>> voMap) {
		Staff identity = source.getIdentity();
		if (identity != null) {
			if (!voMapContainsKey(StaffOutVO.class, identity.getId(), voMap)) {
				voMapPut(StaffOutVO.class, identity.getId(), new StaffOutVO(), true, voMap);
			}
			target.setIdentity((StaffOutVO) voMapGet(StaffOutVO.class, identity.getId(), voMap));
		}
		if (loadModifiedUser) { //cut modifiedUser cycle
			User modifiedUser = source.getModifiedUser();
			if (modifiedUser != null) {
				UserOutVO modifiedUserVO = newVO();
				toVORemainingFields(modifiedUser, modifiedUserVO, false, voMap);
				target.setModifiedUser(modifiedUserVO);
			}
		}
		userDaoImpl.toUserOutVOBase(source, target);
		Department department = source.getDepartment();
		if (department != null) {
			target.setDepartment(departmentDao.toDepartmentVO(department));
		}
		target.setAuthMethod(L10nUtil.createAuthenticationTypeVO(Locales.USER, source.getAuthMethod()));
		target.setChildrenCount(userDaoImpl.getChildrenCount(source.getId()));
		target.setInheritedProperties(getInheritedProperties(source.getInheritedPropertyList()));
		target.setInheritedPermissionProfileGroups(getInheritedPermissionProfileGroups(source.getInheritedPermissionProfileGroupList()));
	}

	public static String toInheritedPropertyList(Collection<String> inheritedProperties) {
		StringBuilder sb = new StringBuilder();
		if (inheritedProperties != null) {
			Iterator<String> it = inheritedProperties.iterator();
			while (it.hasNext()) {
				if (sb.length() > 0) {
					sb.append(STRING_LIST_SEPARATOR);
				}
				String inheritedProperty = it.next();
				sb.append(inheritedProperty);
			}
		}
		return sb.toString();
	}

	public static String toInheritedPermissionProfileGroupList(Collection<PermissionProfileGroup> inheritedPermissionProfileGroups) {
		StringBuilder sb = new StringBuilder();
		if (inheritedPermissionProfileGroups != null) {
			Iterator<PermissionProfileGroup> it = inheritedPermissionProfileGroups.iterator();
			while (it.hasNext()) {
				if (sb.length() > 0) {
					sb.append(STRING_LIST_SEPARATOR);
				}
				PermissionProfileGroup inheritedPermissionProfileGroup = it.next();
				sb.append(inheritedPermissionProfileGroup.value());
			}
		}
		return sb.toString();
	}

	public static HashSet<String> getInheritedProperties(String inheritedPropertyList) {
		HashSet<String> inheritedProperties;
		if (!CommonUtil.isEmptyString(inheritedPropertyList)) {
			String[] list = STRING_LIST_REGEXP.split(inheritedPropertyList, -1);
			inheritedProperties = new HashSet<String>(list.length);
			for (int i = 0; i < list.length; i++) {
				inheritedProperties.add(list[i]);
			}
		} else {
			inheritedProperties = new HashSet<String>();
		}
		return inheritedProperties;
	}

	public static HashSet<PermissionProfileGroup> getInheritedPermissionProfileGroups(String inheritedPermissionProfileGroupList) {
		HashSet<PermissionProfileGroup> inheritedPermissionProfileGroups;
		if (!CommonUtil.isEmptyString(inheritedPermissionProfileGroupList)) {
			String[] list = STRING_LIST_REGEXP.split(inheritedPermissionProfileGroupList, -1);
			inheritedPermissionProfileGroups = new HashSet<PermissionProfileGroup>(list.length);
			for (int i = 0; i < list.length; i++) {
				inheritedPermissionProfileGroups.add(PermissionProfileGroup.fromString(list[i]));
			}
		} else {
			inheritedPermissionProfileGroups = new HashSet<PermissionProfileGroup>();
		}
		return inheritedPermissionProfileGroups;
	}

	@Override
	protected void toVORemainingFields(User source, UserOutVO target, HashMap<Class, HashMap<Long, Object>> voMap) {
		toVORemainingFields(source, target, this.maxInstances > 1, voMap);
	}
}