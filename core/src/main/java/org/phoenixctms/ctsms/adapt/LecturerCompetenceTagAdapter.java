package org.phoenixctms.ctsms.adapt;

import java.util.Collection;

import org.hibernate.LockMode;

import org.phoenixctms.ctsms.domain.Course;
import org.phoenixctms.ctsms.domain.CourseDao;
import org.phoenixctms.ctsms.domain.Lecturer;
import org.phoenixctms.ctsms.domain.LecturerCompetence;
import org.phoenixctms.ctsms.domain.LecturerCompetenceDao;
import org.phoenixctms.ctsms.domain.StaffDao;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.vo.LecturerInVO;

public class LecturerCompetenceTagAdapter extends TagAdapter<Course, LecturerCompetence, Lecturer, LecturerInVO> {

	private CourseDao courseDao;
	private LecturerCompetenceDao lecturerCompetenceDao;
	private StaffDao staffDao;

	public LecturerCompetenceTagAdapter(CourseDao courseDao, LecturerCompetenceDao lecturerCompetenceDao) {
		this.courseDao = courseDao;
		this.lecturerCompetenceDao = lecturerCompetenceDao;
	}

	public LecturerCompetenceTagAdapter(CourseDao courseDao, LecturerCompetenceDao lecturerCompetenceDao, StaffDao staffDao) {
		this.courseDao = courseDao;
		this.lecturerCompetenceDao = lecturerCompetenceDao;
		this.staffDao = staffDao;
	}

	@Override
	protected Course checkRootId(Long rootId) throws ServiceException {
		return CheckIDUtil.checkCourseId(rootId, courseDao, LockMode.PESSIMISTIC_WRITE);
	}

	@Override
	protected void checkRootTagConstraints(Course root, LecturerCompetence tag, LecturerInVO tagValueIn)
			throws ServiceException {
		CheckIDUtil.checkStaffId(tagValueIn.getStaffId(), staffDao);
	}

	@Override
	protected LecturerCompetence checkTagId(Long tagId) throws ServiceException {
		return CheckIDUtil.checkLecturerCompetenceId(tagId, lecturerCompetenceDao);
	}

	@Override
	protected boolean checkValueAgainstRegExp(LecturerInVO tagValueIn) {
		return true;
	}

	@Override
	protected Collection<LecturerCompetence> findTagsIncludingId(Course root, Long tagId) {
		return lecturerCompetenceDao.findByVisibleId(true, tagId);
	}

	@Override
	protected Integer getMaxOccurrence(LecturerCompetence tag) {
		return tag.getMaxOccurrence();
	}

	@Override
	protected String getMismatchL10nKey(LecturerCompetence tag) {
		return null;
	}

	@Override
	protected String getNameL10nKey(LecturerCompetence tag) {
		return tag.getNameL10nKey();
	}

	@Override
	protected String getRegExp(LecturerCompetence tag) {
		return null;
	}

	@Override
	protected Long getRootId(LecturerInVO tagValueIn) {
		return tagValueIn.getCourseId();
	}

	@Override
	protected LecturerCompetence getTag(Lecturer tagValue) {
		return tagValue.getCompetence();
	}

	@Override
	protected Long getTagId(LecturerInVO tagValueIn) {
		return tagValueIn.getCompetenceId();
	}

	@Override
	protected Collection<Lecturer> getTagValues(Course root) {
		return root.getLecturers();
	}

	@Override
	protected String getValue(LecturerInVO tagValueIn) {
		return null;
	}

	@Override
	protected boolean same(Lecturer tagValue, LecturerInVO tagValueIn) {
		return tagValue.getId().equals(tagValueIn.getId());
	}

	@Override
	protected void toTagVOCollection(Collection<?> tags) {
		lecturerCompetenceDao.toLecturerCompetenceVOCollection(tags);
	}
}
