package org.phoenixctms.ctsms.vocycle;

import java.util.Collection;
import java.util.HashMap;
import java.util.TreeSet;

import org.hibernate.LockMode;
import org.phoenixctms.ctsms.compare.StaffComparator;
import org.phoenixctms.ctsms.domain.Department;
import org.phoenixctms.ctsms.domain.DepartmentDao;
import org.phoenixctms.ctsms.domain.OrganisationContactParticulars;
import org.phoenixctms.ctsms.domain.PersonContactParticulars;
import org.phoenixctms.ctsms.domain.Staff;
import org.phoenixctms.ctsms.domain.StaffCategory;
import org.phoenixctms.ctsms.domain.StaffCategoryDao;
import org.phoenixctms.ctsms.domain.StaffDao;
import org.phoenixctms.ctsms.domain.StaffDaoImpl;
import org.phoenixctms.ctsms.domain.User;
import org.phoenixctms.ctsms.domain.UserDao;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.ServiceExceptionCodes;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.UserOutVO;

public class StaffReflexionGraph extends ReflexionCycleHelper<Staff, StaffOutVO> {

	// private static final String getStaffName(StaffOutVO staff, boolean withTitles) {
	// StringBuilder sb = new StringBuilder();
	// if (staff != null) {
	// if (staff.isPerson()) {
	// if (withTitles) {
	// CommonUtil.appendString(sb, staff.getPrefixedTitle1(), null);
	// CommonUtil.appendString(sb, staff.getPrefixedTitle2(), " ");
	// CommonUtil.appendString(sb, staff.getPrefixedTitle3(), " ");
	// CommonUtil.appendString(sb, staff.getFirstName(), " ");
	// CommonUtil.appendString(sb, staff.getLastName(), " ", "?");
	// CommonUtil.appendString(sb, staff.getPostpositionedTitle1(), ", ");
	// CommonUtil.appendString(sb, staff.getPostpositionedTitle2(), ", ");
	// CommonUtil.appendString(sb, staff.getPostpositionedTitle3(), ", ");
	// } else {
	// CommonUtil.appendString(sb, staff.getFirstName(), null);
	// CommonUtil.appendString(sb, staff.getLastName(), " ", "?");
	// }
	// } else {
	// sb.append(staff.getOrganisationName());
	// }
	// }
	// return sb.toString();
	// }
	//
	// private static final String getStaffInitials(StaffOutVO staff) {
	// StringBuilder sb = new StringBuilder();
	// if (staff != null) {
	//
	//
	// if (staff.isPerson()) {
	// String firstName = staff.getFirstName();
	// if (firstName != null && firstName.trim().length() > 0) {
	// sb.append(firstName.trim().substring(0, 1).toUpperCase());
	// }
	// String lastName = staff.getLastName();
	// if (lastName != null && lastName.trim().length() > 0) {
	// sb.append(lastName.trim().substring(0, 1).toUpperCase());
	// }
	// } else {
	// String organisationName = staff.getOrganisationName();
	// if (organisationName != null && organisationName.trim().length() > 0) {
	// sb.append(organisationName.trim().substring(0, 3).toUpperCase());
	// }
	// }
	//
	//
	// }
	// return sb.toString();
	// }

	private StaffDaoImpl staffDaoImpl;
	private StaffDao staffDao;
	private StaffCategoryDao staffCategoryDao;
	private DepartmentDao departmentDao;
	private UserDao userDao;
	private static final boolean LIMIT_INSTANCES = true;
	private static final boolean LIMIT_PARENTS_DEPTH = true;
	private static final boolean LIMIT_CHILDREN_DEPTH = true;
	private int maxInstances;
	private int parentDepth;
	private int childrenDepth;
	public final static int DEFAULT_MAX_INSTANCES = 1;
	private final static int DEFAULT_PARENT_DEPTH = Integer.MAX_VALUE >> 1;
	private final static int DEFAULT_CHILDREN_DEPTH = Integer.MAX_VALUE >> 1;

	public StaffReflexionGraph(StaffDao staffDao) {
		this.staffDao = staffDao;
	}

	public StaffReflexionGraph(StaffDaoImpl staffDaoImpl,
			StaffCategoryDao staffCategoryDao, DepartmentDao departmentDao,
			UserDao userDao, Integer... maxInstances) {
		this.maxInstances = maxInstances != null && maxInstances.length > 0 ? (maxInstances[0] == null ? DEFAULT_MAX_INSTANCES : maxInstances[0]) : DEFAULT_MAX_INSTANCES;
		this.parentDepth = maxInstances != null && maxInstances.length > 1 ? (maxInstances[1] == null ? DEFAULT_PARENT_DEPTH : maxInstances[1]) : DEFAULT_PARENT_DEPTH;
		this.childrenDepth = maxInstances != null && maxInstances.length > 2 ? (maxInstances[2] == null ? DEFAULT_CHILDREN_DEPTH : maxInstances[2]) : DEFAULT_CHILDREN_DEPTH;
		this.staffDaoImpl = staffDaoImpl;
		this.staffCategoryDao = staffCategoryDao;
		this.departmentDao = departmentDao;
		this.userDao = userDao;
	}

	@Override
	protected Staff aquireWriteLock(Long id) throws ServiceException {
		return staffDao.load(id, LockMode.PESSIMISTIC_WRITE);
	}

	@Override
	protected Class getAVOClass() {
		return StaffOutVO.class;
	}

	@Override
	protected Staff getChild(Staff source) {
		return null;
	}

	@Override
	protected Collection<Staff> getEntityChildren(Staff source) {
		Collection<Staff> children = source.getChildren();
		if (children.size() > 1) {
			TreeSet<Staff> result = new TreeSet<Staff>(new StaffComparator());
			result.addAll(children);
			return result;
		} else {
			return children;
		}
	}

	@Override
	protected Long getEntityId(Staff source) {
		return source.getId();
	}

	@Override
	protected Collection<Staff> getEntityParents(Staff source) {
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
	protected Staff getParent(Staff source) {
		return source.getParent();
	}

	@Override
	protected Collection<StaffOutVO> getVOChildren(StaffOutVO target) {
		return target.getChildren();
	}

	@Override
	protected Collection<StaffOutVO> getVOParents(StaffOutVO target) {
		return null;
	}

	@Override
	protected boolean limitChildrenDepth() {
		return LIMIT_CHILDREN_DEPTH;
	}

	@Override
	protected boolean limitInstances() {
		return LIMIT_INSTANCES;
	}

	@Override
	protected boolean limitParentsDepth() {
		return LIMIT_PARENTS_DEPTH;
	}

	@Override
	protected StaffOutVO newVO() {
		return new StaffOutVO();
	}

	@Override
	protected void setChild(StaffOutVO target, StaffOutVO childVO) {
	}

	@Override
	protected void setParent(StaffOutVO target, StaffOutVO parentVO) {
		target.setParent(parentVO);
	}

	@Override
	protected void throwGraphLoopException(Staff entity) throws ServiceException {
		throw L10nUtil.initServiceException(ServiceExceptionCodes.STAFF_GRAPH_LOOP, entity.getId().toString(), CommonUtil.staffOutVOToString(staffDao.toStaffOutVO(entity)));
	}

	@Override
	protected void toVORemainingFields(Staff source, StaffOutVO target, HashMap<Class, HashMap<Long, Object>> voMap) {
		User modifiedUser = source.getModifiedUser();
		if (modifiedUser != null) {
			if (!voMapContainsKey(UserOutVO.class, modifiedUser.getId(), voMap)) {
				voMapPut(UserOutVO.class, modifiedUser.getId(), new UserOutVO(), true, voMap);
			}
			target.setModifiedUser((UserOutVO) voMapGet(UserOutVO.class, modifiedUser.getId(), voMap));
		}
		staffDaoImpl.toStaffOutVOBase(source, target);
		StaffCategory category = source.getCategory();
		Department department = source.getDepartment();
		if (category != null) {
			target.setCategory(staffCategoryDao.toStaffCategoryVO(category));
		}
		if (department != null) {
			target.setDepartment(departmentDao.toDepartmentVO(department));
		}
		if (source.isPerson()) {
			PersonContactParticulars personParticulars = source.getPersonParticulars();
			if (personParticulars != null) {
				target.setPrefixedTitle1(personParticulars.getPrefixedTitle1());
				target.setPrefixedTitle2(personParticulars.getPrefixedTitle2());
				target.setPrefixedTitle3(personParticulars.getPrefixedTitle3());
				target.setFirstName(personParticulars.getFirstName());
				target.setLastName(personParticulars.getLastName());
				target.setPostpositionedTitle1(personParticulars.getPostpositionedTitle1());
				target.setPostpositionedTitle2(personParticulars.getPostpositionedTitle2());
				target.setPostpositionedTitle3(personParticulars.getPostpositionedTitle3());
				target.setCvAcademicTitle(personParticulars.getCvAcademicTitle());
				target.setDateOfBirth(personParticulars.getDateOfBirth());
				target.setGender(L10nUtil.createSexVO(Locales.USER, personParticulars.getGender()));
				target.setCitizenship(personParticulars.getCitizenship());
				target.setAge(CommonUtil.getAge(target.getDateOfBirth()));
				target.setImageShowCv(personParticulars.getShowCv() == null ? false : personParticulars.getShowCv().booleanValue());
				target.setHasImage(personParticulars.getFileSize() != null && personParticulars.getFileSize() > 0l);
			}
		} else {
			OrganisationContactParticulars organisationParticulars = source.getOrganisationParticulars();
			if (organisationParticulars != null) {
				target.setOrganisationName(organisationParticulars.getOrganisationName());
				target.setCvOrganisationName(organisationParticulars.getCvOrganisationName());
			}
		}
		target.setInitials(CommonUtil.getStaffInitials(target));
		target.setName(CommonUtil.getStaffName(target, false));
		target.setNameWithTitles(CommonUtil.getStaffName(target, true));
		target.setNameSortable(CommonUtil.getNameSortable(target));
		target.setChildrenCount(staffDaoImpl.getChildrenCount(source.getId())); // source.getChildren().Xsize());
	}
}