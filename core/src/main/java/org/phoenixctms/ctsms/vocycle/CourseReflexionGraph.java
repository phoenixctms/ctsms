package org.phoenixctms.ctsms.vocycle;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.hibernate.LockMode;
import org.phoenixctms.ctsms.compare.CourseComparator;
import org.phoenixctms.ctsms.domain.Course;
import org.phoenixctms.ctsms.domain.CourseCategory;
import org.phoenixctms.ctsms.domain.CourseCategoryDao;
import org.phoenixctms.ctsms.domain.CourseDao;
import org.phoenixctms.ctsms.domain.CourseDaoImpl;
import org.phoenixctms.ctsms.domain.CvSection;
import org.phoenixctms.ctsms.domain.CvSectionDao;
import org.phoenixctms.ctsms.domain.Department;
import org.phoenixctms.ctsms.domain.DepartmentDao;
import org.phoenixctms.ctsms.domain.Staff;
import org.phoenixctms.ctsms.domain.StaffDao;
import org.phoenixctms.ctsms.domain.TrainingRecordSection;
import org.phoenixctms.ctsms.domain.TrainingRecordSectionDao;
import org.phoenixctms.ctsms.domain.Trial;
import org.phoenixctms.ctsms.domain.TrialDao;
import org.phoenixctms.ctsms.domain.User;
import org.phoenixctms.ctsms.domain.UserDao;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.DefaultMessages;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.MessageCodes;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.ServiceExceptionCodes;
import org.phoenixctms.ctsms.util.date.DateCalc;
import org.phoenixctms.ctsms.vo.CourseOutVO;

public class CourseReflexionGraph extends ReflexionCycleHelper<Course, CourseOutVO> {

	private static final boolean LIMIT_INSTANCES = true;
	private static final boolean LIMIT_PARENTS_DEPTH = true;
	private static final boolean LIMIT_CHILDREN_DEPTH = true;
	private int maxInstances;
	private int renewalsDepth;
	private int precedingCoursesDepth;
	private CourseDaoImpl courseDaoImpl;
	private CourseDao courseDao;
	private CourseCategoryDao courseCategoryDao;
	private DepartmentDao departmentDao;
	private CvSectionDao cvSectionDao;
	private TrainingRecordSectionDao trainingRecordSectionDao;
	private StaffDao staffDao;
	private TrialDao trialDao;
	private UserDao userDao;
	private final static int DEFAULT_MAX_INSTANCES = 1;
	private final static int DEFAULT_RENEWALS_DEPTH = Integer.MAX_VALUE >> 1;
	private final static int DEFAULT_PRECEDING_COURSES_DEPTH = Integer.MAX_VALUE >> 1;

	public CourseReflexionGraph(CourseDao courseDao) {
		this.courseDao = courseDao;
	}

	public CourseReflexionGraph(CourseDaoImpl courseDaoImpl,
			CourseCategoryDao courseCategoryDao, DepartmentDao departmentDao,
			CvSectionDao cvSectionDao, TrainingRecordSectionDao trainingRecordSectionDao, StaffDao staffDao, TrialDao trialDao, UserDao userDao, Integer... maxInstances) {
		this.maxInstances = maxInstances != null && maxInstances.length > 0 ? (maxInstances[0] == null ? DEFAULT_MAX_INSTANCES : maxInstances[0]) : DEFAULT_MAX_INSTANCES;
		this.precedingCoursesDepth = maxInstances != null && maxInstances.length > 1 ? (maxInstances[1] == null ? DEFAULT_PRECEDING_COURSES_DEPTH : maxInstances[1])
				: DEFAULT_PRECEDING_COURSES_DEPTH;
		this.renewalsDepth = maxInstances != null && maxInstances.length > 2 ? (maxInstances[2] == null ? DEFAULT_RENEWALS_DEPTH : maxInstances[2])
				: DEFAULT_RENEWALS_DEPTH;
		this.courseDaoImpl = courseDaoImpl;
		this.courseCategoryDao = courseCategoryDao;
		this.departmentDao = departmentDao;
		this.cvSectionDao = cvSectionDao;
		this.trainingRecordSectionDao = trainingRecordSectionDao;
		this.staffDao = staffDao;
		this.trialDao = trialDao;
		this.userDao = userDao;
	}

	@Override
	protected Course aquireWriteLock(Long id) throws ServiceException {
		return courseDao.load(id, LockMode.PESSIMISTIC_WRITE);
	}

	@Override
	protected Class getAVOClass() {
		return CourseOutVO.class;
	}

	@Override
	protected Course getChild(Course source) {
		return null;
	}

	@Override
	protected Collection<Course> getEntityChildren(Course source) {
		Collection<Course> renewals = source.getRenewals();
		if (renewals.size() > 1) {
			TreeSet<Course> result = new TreeSet<Course>(new CourseComparator());
			result.addAll(renewals);
			return result;
		} else {
			return renewals;
		}
	}

	@Override
	protected Long getEntityId(Course source) {
		return source.getId();
	}

	@Override
	protected Collection<Course> getEntityParents(Course source) {
		Collection<Course> precedingCourses = source.getPrecedingCourses();
		if (precedingCourses.size() > 1) {
			TreeSet<Course> result = new TreeSet<Course>(new CourseComparator());
			result.addAll(precedingCourses);
			return result;
		} else {
			return precedingCourses;
		}
	}

	@Override
	protected ReflexionDepth getInitialReflexionDepth() {
		return new ReflexionDepth(precedingCoursesDepth, renewalsDepth);
	}

	@Override
	protected int getMaxInstances() {
		return maxInstances;
	}

	@Override
	protected Course getParent(Course source) {
		return null;
	}

	@Override
	protected Collection<CourseOutVO> getVOChildren(CourseOutVO target) {
		return target.getRenewals();
	}

	@Override
	protected Collection<CourseOutVO> getVOParents(CourseOutVO target) {
		return target.getPrecedingCourses();
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
	protected CourseOutVO newVO() {
		return new CourseOutVO();
	}

	@Override
	protected void setChild(CourseOutVO target, CourseOutVO childVO) {
	}

	@Override
	protected void setParent(CourseOutVO target, CourseOutVO parentVO) {
	}

	@Override
	protected void throwGraphLoopException(List<Course> path) throws ServiceException {
		Iterator<Course> it = path.iterator();
		StringBuilder sb = new StringBuilder();
		while (it.hasNext()) {
			appendLoopPath(sb, L10nUtil.getMessage(MessageCodes.LOOP_PATH_COURSE_LABEL,DefaultMessages.LOOP_PATH_COURSE_LABEL,CommonUtil.courseOutVOToString(courseDao.toCourseOutVO(it.next()))));
		}
		throw L10nUtil
				.initServiceException(ServiceExceptionCodes.COURSE_GRAPH_LOOP, sb.toString());
	}

	@Override
	protected void toVORemainingFields(Course source, CourseOutVO target, HashMap<Class, HashMap<Long, Object>> voMap) {
		courseDaoImpl.toCourseOutVOBase(source, target);
		CourseCategory category = source.getCategory();
		Department department = source.getDepartment();
		CvSection cvSectionPreset = source.getCvSectionPreset();
		TrainingRecordSection trainingRecordSectionPreset = source.getTrainingRecordSectionPreset();
		Staff institution = source.getInstitution();
		Trial trial = source.getTrial();
		User modifiedUser = source.getModifiedUser();
		if (category != null) {
			target.setCategory(courseCategoryDao.toCourseCategoryVO(category));
		}
		if (department != null) {
			target.setDepartment(departmentDao.toDepartmentVO(department));
		}
		if (cvSectionPreset != null) {
			target.setCvSectionPreset(cvSectionDao.toCvSectionVO(cvSectionPreset));
		}
		if (trainingRecordSectionPreset != null) {
			target.setTrainingRecordSectionPreset(trainingRecordSectionDao.toTrainingRecordSectionVO(trainingRecordSectionPreset));
		}
		if (institution != null) {
			target.setInstitution(staffDao.toStaffOutVO(institution));
		}
		if (trial != null) {
			target.setTrial(trialDao.toTrialOutVO(trial));
		}
		if (modifiedUser != null) {
			target.setModifiedUser(userDao.toUserOutVO(modifiedUser));
		}
		target.setValidityPeriod(L10nUtil.createVariablePeriodVO(Locales.USER, source.getValidityPeriod()));
		if (target.getExpires()) {
			target.setExpiration(DateCalc.addInterval(target.getStop(), source.getValidityPeriod(), source.getValidityPeriodDays()));
		}
		target.setPrecedingCoursesCount(courseDaoImpl.getPrecedingCoursesCount(source.getId()));
		target.setRenewalsCount(courseDaoImpl.getRenewalsCount(source.getId()));
	}
}