// Generated by: hibernate/SpringHibernateDaoImpl.vsl in andromda-spring-cartridge.
// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package org.phoenixctms.ctsms.domain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.phoenixctms.ctsms.enumeration.DBModule;
import org.phoenixctms.ctsms.enumeration.VariablePeriod;
import org.phoenixctms.ctsms.query.CriteriaUtil;
import org.phoenixctms.ctsms.query.QueryUtil;
import org.phoenixctms.ctsms.query.SubCriteriaMap;
import org.phoenixctms.ctsms.util.date.DateCalc;
import org.phoenixctms.ctsms.vo.CourseCategoryVO;
import org.phoenixctms.ctsms.vo.CourseInVO;
import org.phoenixctms.ctsms.vo.CourseOutVO;
import org.phoenixctms.ctsms.vo.CriteriaInstantVO;
import org.phoenixctms.ctsms.vo.CvSectionVO;
import org.phoenixctms.ctsms.vo.DepartmentVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.TrainingRecordSectionVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.vo.UserOutVO;
import org.phoenixctms.ctsms.vocycle.CourseReflexionGraph;

/**
 * @see Course
 */
public class CourseDaoImpl
		extends CourseDaoBase {

	private static void findUpcomingRenewals(Timestamp now, Course course, Set<Course> result, boolean skip, Set<Long> participatingCourseIds, Set<Long> courseIdchecked) {
		if (!courseIdchecked.add(course.getId())) {
			return;
		}
		if (!skip) {
			boolean valid;
			Date today = new Date(now.getTime());
			if (course.isExpires()) {
				if (today.compareTo(DateCalc.addInterval(course.getStop(), course.getValidityPeriod(), course.getValidityPeriodDays())) > 0) {
					valid = false;
				} else {
					valid = true;
				}
			} else {
				valid = true;
			}
			if (valid) {
				if ((course.isSelfRegistration() && ((course.getParticipationDeadline() == null && today.compareTo(course.getStop()) <= 0) ||
						(course.getParticipationDeadline() != null && now.compareTo(course.getParticipationDeadline()) <= 0)))
						|| ((!course.isSelfRegistration() &&
								today.compareTo(course.getStop()) <= 0) &&
								(participatingCourseIds == null || participatingCourseIds.contains(course.getId())))) {
					result.add(course);
				}
			}
		}
		Collection<Course> renewals = course.getRenewals();
		if (renewals != null && renewals.size() > 0) {
			Iterator<Course> it = renewals.iterator();
			while (it.hasNext()) {
				findUpcomingRenewals(now, it.next(), result, false, participatingCourseIds, courseIdchecked);
			}
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public Course courseInVOToEntity(CourseInVO courseInVO) {
		Course entity = this.loadCourseFromCourseInVO(courseInVO);
		this.courseInVOToEntity(courseInVO, entity, true);
		return entity;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void courseInVOToEntity(
			CourseInVO source,
			Course target,
			boolean copyIfNull) {
		super.courseInVOToEntity(source, target, copyIfNull);
		Long departmentId = source.getDepartmentId();
		Long categoryId = source.getCategoryId();
		Long cvSectionPresetId = source.getCvSectionPresetId();
		Long trainingRecordSectionPresetId = source.getTrainingRecordSectionPresetId();
		Long institutionId = source.getInstitutionId();
		Long trialId = source.getTrialId();
		if (departmentId != null) {
			target.setDepartment(this.getDepartmentDao().load(departmentId));
		} else if (copyIfNull) {
			target.setDepartment(null);
		}
		if (categoryId != null) {
			target.setCategory(this.getCourseCategoryDao().load(categoryId));
		} else if (copyIfNull) {
			target.setCategory(null);
		}
		if (cvSectionPresetId != null) {
			target.setCvSectionPreset(this.getCvSectionDao().load(cvSectionPresetId));
		} else if (copyIfNull) {
			target.setCvSectionPreset(null);
		}
		if (trainingRecordSectionPresetId != null) {
			target.setTrainingRecordSectionPreset(this.getTrainingRecordSectionDao().load(trainingRecordSectionPresetId));
		} else if (copyIfNull) {
			target.setTrainingRecordSectionPreset(null);
		}
		if (institutionId != null) {
			Staff institution = this.getStaffDao().load(institutionId);
			target.setInstitution(institution);
			institution.addInstitutionCourses(target);
		} else if (copyIfNull) {
			Staff institution = target.getInstitution();
			target.setInstitution(null);
			if (institution != null) {
				institution.removeInstitutionCourses(target);
			}
		}
		if (trialId != null) {
			Trial trial = this.getTrialDao().load(trialId);
			target.setTrial(trial);
			trial.addCourses(target);
		} else if (copyIfNull) {
			Trial trial = target.getTrial();
			target.setTrial(null);
			if (trial != null) {
				trial.removeCourses(target);
			}
		}
		if (copyIfNull || source.getPrecedingCourseIds().size() > 0) {
			Iterator<Course> it = target.getPrecedingCourses().iterator();
			while (it.hasNext()) {
				it.next().removeRenewals(target);
			}
			target.setPrecedingCourses(toCourseSet(source.getPrecedingCourseIds()));
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public Course courseOutVOToEntity(CourseOutVO courseOutVO) {
		Course entity = this.loadCourseFromCourseOutVO(courseOutVO);
		this.courseOutVOToEntity(courseOutVO, entity, true);
		return entity;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void courseOutVOToEntity(
			CourseOutVO source,
			Course target,
			boolean copyIfNull) {
		super.courseOutVOToEntity(source, target, copyIfNull);
		CourseCategoryVO categoryVO = source.getCategory();
		DepartmentVO departmentVO = source.getDepartment();
		UserOutVO modifiedUserVO = source.getModifiedUser();
		CvSectionVO cvSectionPresetVO = source.getCvSectionPreset();
		TrainingRecordSectionVO trainingRecordPresetVO = source.getTrainingRecordSectionPreset();
		StaffOutVO institutionVO = source.getInstitution();
		TrialOutVO trialVO = source.getTrial();
		if (categoryVO != null) {
			target.setCategory(this.getCourseCategoryDao().courseCategoryVOToEntity(categoryVO));
		} else if (copyIfNull) {
			target.setCategory(null);
		}
		if (departmentVO != null) {
			target.setDepartment(this.getDepartmentDao().departmentVOToEntity(departmentVO));
		} else if (copyIfNull) {
			target.setDepartment(null);
		}
		if (modifiedUserVO != null) {
			target.setModifiedUser(this.getUserDao().userOutVOToEntity(modifiedUserVO));
		} else if (copyIfNull) {
			target.setModifiedUser(null);
		}
		if (cvSectionPresetVO != null) {
			target.setCvSectionPreset(this.getCvSectionDao().cvSectionVOToEntity(cvSectionPresetVO));
		} else if (copyIfNull) {
			target.setCvSectionPreset(null);
		}
		if (trainingRecordPresetVO != null) {
			target.setTrainingRecordSectionPreset(this.getTrainingRecordSectionDao().trainingRecordSectionVOToEntity(trainingRecordPresetVO));
		} else if (copyIfNull) {
			target.setTrainingRecordSectionPreset(null);
		}
		if (institutionVO != null) {
			Staff institution = this.getStaffDao().staffOutVOToEntity(institutionVO);
			target.setInstitution(institution);
			institution.addInstitutionCourses(target);
		} else if (copyIfNull) {
			Staff institution = target.getInstitution();
			target.setInstitution(null);
			if (institution != null) {
				institution.removeInstitutionCourses(target);
			}
		}
		if (trialVO != null) {
			Trial trial = this.getTrialDao().trialOutVOToEntity(trialVO);
			target.setTrial(trial);
			trial.addCourses(target);
		} else if (copyIfNull) {
			Trial trial = target.getTrial();
			target.setTrial(null);
			if (trial != null) {
				trial.removeCourses(target);
			}
		}
	}

	private org.hibernate.Criteria createCourseCriteria(String alias) {
		org.hibernate.Criteria courseCriteria;
		if (alias != null && alias.length() > 0) {
			courseCriteria = this.getSession().createCriteria(Course.class, alias);
		} else {
			courseCriteria = this.getSession().createCriteria(Course.class);
		}
		return courseCriteria;
	}

	@Override
	protected Collection<Course> handleFindByCriteria(
			CriteriaInstantVO criteria, PSFVO psf) throws Exception {
		Query query = QueryUtil.createSearchQuery(
				criteria,
				DBModule.COURSE_DB,
				psf,
				this.getSessionFactory(),
				this.getCriterionTieDao(),
				this.getCriterionPropertyDao(),
				this.getCriterionRestrictionDao());
		return query.list();
	}

	@Override
	protected Collection<Course> handleFindByDepartmentCategoryInterval(
			Long departmentId, Long courseCategoryId, Timestamp from, Timestamp to)
			throws Exception {
		org.hibernate.Criteria courseCriteria = createCourseCriteria(null);
		CriteriaUtil.applyStartOptionalIntervalCriterion(courseCriteria, from, to, null, true);
		if (departmentId != null) {
			courseCriteria.add(Restrictions.eq("department.id", departmentId.longValue()));
		}
		if (courseCategoryId != null) {
			courseCriteria.add(Restrictions.eq("category.id", courseCategoryId.longValue()));
		}
		return courseCriteria.list();
	}

	@Override
	protected Collection<Course> handleFindByIdDepartment(Long courseId,
			Long departmentId, PSFVO psf) throws Exception {
		org.hibernate.Criteria courseCriteria = createCourseCriteria(null);
		SubCriteriaMap criteriaMap = new SubCriteriaMap(Course.class, courseCriteria);
		CriteriaUtil.applyIdDepartmentCriterion(courseCriteria, courseId, departmentId);
		CriteriaUtil.applyPSFVO(criteriaMap, psf);
		return courseCriteria.list();
	}

	@Override
	protected Collection<Course> handleFindByInstitution(Long institutionId,
			PSFVO psf) throws Exception {
		org.hibernate.Criteria courseCriteria = createCourseCriteria(null);
		SubCriteriaMap criteriaMap = new SubCriteriaMap(Course.class, courseCriteria);
		if (institutionId != null) {
			courseCriteria.add(Restrictions.eq("institution.id", institutionId.longValue()));
		}
		CriteriaUtil.applyPSFVO(criteriaMap, psf);
		return courseCriteria.list();
	}

	@Override
	protected Collection<Course> handleFindByTrial(Long trialId, PSFVO psf)
			throws Exception {
		org.hibernate.Criteria courseCriteria = createCourseCriteria(null);
		SubCriteriaMap criteriaMap = new SubCriteriaMap(Course.class, courseCriteria);
		if (trialId != null) {
			courseCriteria.add(Restrictions.eq("trial.id", trialId.longValue()));
		}
		CriteriaUtil.applyPSFVO(criteriaMap, psf);
		return courseCriteria.list();
	}

	@Override
	protected Collection<Course> handleFindExpiring(Date today,
			Long departmentId, Long courseCategoryId,
			VariablePeriod reminderPeriod, Long reminderPeriodDays, boolean includeAlreadyPassed, PSFVO psf)
			throws Exception {
		org.hibernate.Criteria courseCriteria = createCourseCriteria(null);
		SubCriteriaMap criteriaMap = new SubCriteriaMap(Course.class, courseCriteria);
		if (departmentId != null) {
			courseCriteria.add(Restrictions.eq("department.id", departmentId.longValue()));
		}
		if (courseCategoryId != null) {
			courseCriteria.add(Restrictions.eq("category.id", courseCategoryId.longValue()));
		}
		courseCriteria.add(Restrictions.eq("expires", true)); // performance only...
		if (psf != null) {
			PSFVO sorterFilter = new PSFVO();
			sorterFilter.setFilters(psf.getFilters());
			sorterFilter.setFilterTimeZone(psf.getFilterTimeZone());
			sorterFilter.setSortField(psf.getSortField());
			sorterFilter.setSortOrder(psf.getSortOrder());
			CriteriaUtil.applyPSFVO(criteriaMap, sorterFilter);
		}
		ArrayList<Course> resultSet = CriteriaUtil.listExpirations(courseCriteria, today, null, includeAlreadyPassed, null, null, reminderPeriod, reminderPeriodDays);
		return CriteriaUtil.applyPVO(resultSet, psf, true); // eliminated dupes
	}

	@Override
	protected Collection<Course> handleFindUpcoming(Timestamp currentTimestamp,
			Long departmentId, Long courseCategoryId, PSFVO psf)
			throws Exception {
		org.hibernate.Criteria courseCriteria = createCourseCriteria(null);
		SubCriteriaMap criteriaMap = new SubCriteriaMap(Course.class, courseCriteria);
		Timestamp now;
		if (currentTimestamp == null) {
			now = new Timestamp(System.currentTimeMillis());
		} else {
			now = currentTimestamp;
		}
		courseCriteria.add(Restrictions.or(
				Restrictions.and(
						Restrictions.eq("selfRegistration", true),
						Restrictions.or(Restrictions.and(Restrictions.isNull("participationDeadline"), Restrictions.ge("stop", new Date(now.getTime()))),
								Restrictions.ge("participationDeadline", now))),
				Restrictions.and(Restrictions.ge("selfRegistration", false), Restrictions.ge("stop", new Date(now.getTime())))));
		if (departmentId != null) {
			courseCriteria.add(Restrictions.eq("department.id", departmentId.longValue()));
		}
		if (courseCategoryId != null) {
			courseCriteria.add(Restrictions.eq("category.id", courseCategoryId.longValue()));
		}
		CriteriaUtil.applyPSFVO(criteriaMap, psf);
		return courseCriteria.list();
	}

	@Override
	protected Collection<Course> handleFindUpcoming(
			Timestamp currentTimestamp, Long staffId, PSFVO psf)
			throws Exception {
		org.hibernate.Criteria courseCriteria = createCourseCriteria("course0");
		SubCriteriaMap criteriaMap = new SubCriteriaMap(Course.class, courseCriteria);
		Timestamp now;
		if (currentTimestamp == null) {
			now = new Timestamp(System.currentTimeMillis());
		} else {
			now = currentTimestamp;
		}
		// will only work for count statement if removing the order-by attribute from course.hbm.xml->participations....:
		// criteriaMap.createCriteria("participations",CriteriaSpecification.LEFT_JOIN).add(
		// Restrictions.or(
		// Restrictions.and(
		// Restrictions.eq("staff.id",staffId.longValue()),
		// Restrictions.and(Restrictions.eq("course0.selfRegistration", false), Restrictions.ge("course0.stop", new Date(now.getTime()) ))
		// ),
		// Restrictions.and(
		// Restrictions.or(
		// Restrictions.eq("staff.id",staffId.longValue()),
		// Restrictions.isNull("staff")
		// ),
		// Restrictions.and(Restrictions.eq("course0.selfRegistration", true), Restrictions.or(Restrictions.isNull("course0.participationDeadline"),
		// Restrictions.ge("course0.participationDeadline", now)))
		// )
		// ));
		DetachedCriteria subQuery = DetachedCriteria.forClass(CourseParticipationStatusEntryImpl.class, "courseParticipationStatusEntry"); // IMPL!!!!
		subQuery.setProjection(Projections.id());
		subQuery.add(Restrictions.eqProperty("course.id", "course0.id"));
		subQuery.add(Restrictions.eq("staff.id", staffId.longValue()));
		courseCriteria.add(Restrictions.or(
				Restrictions.and(
						Restrictions.eq("selfRegistration", true),
						Restrictions.or(
								Restrictions.and(
										Restrictions.isNull("participationDeadline"),
										Restrictions.ge("stop", new Date(now.getTime()))),
								Restrictions.ge("participationDeadline", now))),
				Restrictions.and(
						Restrictions.and(Restrictions.eq("selfRegistration", false), Restrictions.ge("stop", new Date(now.getTime()))),
						Subqueries.exists(subQuery))));
		CriteriaUtil.applyPSFVO(criteriaMap, psf); // unique participant staff per course
		return courseCriteria.list();
	}

	@Override
	protected Collection<Course> handleFindUpcomingRenewals(Timestamp currentTimestamp, Long courseId, Long staffId)
			throws Exception {
		Timestamp now;
		if (currentTimestamp == null) {
			now = new Timestamp(System.currentTimeMillis());
		} else {
			now = currentTimestamp;
		}
		LinkedHashSet<Course> result = new LinkedHashSet<Course>();
		HashSet<Long> participatingCourseIds;
		if (staffId != null) {
			participatingCourseIds = new HashSet<Long>();
			Iterator<CourseParticipationStatusEntry> participationsIt = this.getStaffDao().load(staffId).getParticipations().iterator();
			while (participationsIt.hasNext()) {
				participatingCourseIds.add(participationsIt.next().getCourse().getId());
			}
		} else {
			participatingCourseIds = null;
		}
		findUpcomingRenewals(now, this.load(courseId), result, true, participatingCourseIds, new HashSet<Long>());
		return new ArrayList<Course>(result);
	}

	@Override
	protected long handleGetCountByCriteria(CriteriaInstantVO criteria, PSFVO psf) throws Exception {
		return QueryUtil.getSearchQueryResultCount(
				criteria,
				DBModule.COURSE_DB,
				psf,
				this.getSessionFactory(),
				this.getCriterionTieDao(),
				this.getCriterionPropertyDao(),
				this.getCriterionRestrictionDao());
	}

	@Override
	protected long handleGetPrecedingCoursesCount(Long courseId) throws Exception {
		org.hibernate.Criteria courseCriteria = createCourseCriteria(null);
		courseCriteria.add(Restrictions.idEq(courseId.longValue()));
		org.hibernate.Criteria precedingCoursesCriteria = courseCriteria.createCriteria("precedingCourses", CriteriaSpecification.INNER_JOIN);
		return (Long) courseCriteria.setProjection(Projections.rowCount()).uniqueResult();
	}

	@Override
	protected long handleGetRenewalsCount(Long courseId) throws Exception {
		org.hibernate.Criteria courseCriteria = createCourseCriteria(null);
		courseCriteria.add(Restrictions.idEq(courseId.longValue()));
		org.hibernate.Criteria renewalsCriteria = courseCriteria.createCriteria("renewals", CriteriaSpecification.INNER_JOIN);
		return (Long) courseCriteria.setProjection(Projections.rowCount()).uniqueResult();
	}

	/**
	 * Retrieves the entity object that is associated with the specified value object
	 * from the object store. If no such entity object exists in the object store,
	 * a new, blank entity is created
	 */
	private Course loadCourseFromCourseInVO(CourseInVO courseInVO) {
		Course course = null;
		Long id = courseInVO.getId();
		if (id != null) {
			course = this.load(id);
		}
		if (course == null) {
			course = Course.Factory.newInstance();
		}
		return course;
	}

	/**
	 * Retrieves the entity object that is associated with the specified value object
	 * from the object store. If no such entity object exists in the object store,
	 * a new, blank entity is created
	 */
	private Course loadCourseFromCourseOutVO(CourseOutVO courseOutVO) {
		throw new UnsupportedOperationException("out value object to recursive entity not supported");
	}

	private ArrayList<Long> toCourseIdCollection(Collection<Course> courses) { // lazyload persistentset prevention
		ArrayList<Long> result = new ArrayList<Long>(courses.size());
		Iterator<Course> it = courses.iterator();
		while (it.hasNext()) {
			result.add(it.next().getId());
		}
		Collections.sort(result); // InVO ID sorting
		return result;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public CourseInVO toCourseInVO(final Course entity) {
		return super.toCourseInVO(entity);
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void toCourseInVO(
			Course source,
			CourseInVO target) {
		super.toCourseInVO(source, target);
		Department department = source.getDepartment();
		CourseCategory category = source.getCategory();
		CvSection cvSectionPreset = source.getCvSectionPreset();
		TrainingRecordSection trainingRecordSectionPreset = source.getTrainingRecordSectionPreset();
		Staff institution = source.getInstitution();
		Trial trial = source.getTrial();
		if (department != null) {
			target.setDepartmentId(department.getId());
		}
		if (category != null) {
			target.setCategoryId(category.getId());
		}
		if (cvSectionPreset != null) {
			target.setCvSectionPresetId(cvSectionPreset.getId());
		}
		if (trainingRecordSectionPreset != null) {
			target.setTrainingRecordSectionPresetId(trainingRecordSectionPreset.getId());
		}
		if (institution != null) {
			target.setInstitutionId(institution.getId());
		}
		if (trial != null) {
			target.setTrialId(trial.getId());
		}
		target.setPrecedingCourseIds(toCourseIdCollection(source.getPrecedingCourses()));
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public CourseOutVO toCourseOutVO(final Course entity) {
		return super.toCourseOutVO(entity);
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void toCourseOutVO(
			Course source,
			CourseOutVO target) {
		(new CourseReflexionGraph(this, this.getCourseCategoryDao(), this.getDepartmentDao(), this.getCvSectionDao(), this.getTrainingRecordSectionDao(), this.getStaffDao(),
				this.getTrialDao(), this.getUserDao()))
						.toVOHelper(source, target, new HashMap<Class, HashMap<Long, Object>>());
	}

	@Override
	public void toCourseOutVO(
			Course source,
			CourseOutVO target, Integer... maxInstances) {
		(new CourseReflexionGraph(this, this.getCourseCategoryDao(), this.getDepartmentDao(), this.getCvSectionDao(), this.getTrainingRecordSectionDao(), this.getStaffDao(),
				this.getTrialDao(), this.getUserDao(),
				maxInstances)).toVOHelper(source, target, new HashMap<Class, HashMap<Long, Object>>());
	}

	private HashSet<Course> toCourseSet(Collection<Long> courseIds) {
		HashSet<Course> result = new HashSet<Course>(courseIds.size());
		Iterator<Long> it = courseIds.iterator();
		while (it.hasNext()) {
			result.add(this.load(it.next()));
		}
		return result;
	}
}