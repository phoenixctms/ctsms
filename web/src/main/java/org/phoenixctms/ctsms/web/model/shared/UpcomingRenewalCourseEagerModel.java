package org.phoenixctms.ctsms.web.model.shared;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.CourseOutVO;
import org.phoenixctms.ctsms.vo.CourseParticipationStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.model.EagerDataModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class UpcomingRenewalCourseEagerModel extends EagerDataModelBase<CourseOutVO> {

	public static UpcomingRenewalCourseEagerModel getCachedUpcomingRenewalCourseModel(CourseParticipationStatusEntryOutVO statusEntry,
			HashMap<Long, UpcomingRenewalCourseEagerModel> upcomingRenewalCourseModelCache) {
		UpcomingRenewalCourseEagerModel model;
		if (statusEntry != null && upcomingRenewalCourseModelCache != null) {
			long courseParticipationStatusEntryId = statusEntry.getId();
			if (upcomingRenewalCourseModelCache.containsKey(courseParticipationStatusEntryId)) {
				model = upcomingRenewalCourseModelCache.get(courseParticipationStatusEntryId);
			} else {
				model = new UpcomingRenewalCourseEagerModel();
				model.setCourseId(statusEntry.getCourse().getId());
				model.setStaffId(statusEntry.getStaff().getId());
				upcomingRenewalCourseModelCache.put(courseParticipationStatusEntryId, model);
			}
		} else {
			model = new UpcomingRenewalCourseEagerModel();
		}
		return model;
	}

	private Long courseId;
	private Long staffId;

	public UpcomingRenewalCourseEagerModel() {
		super();
		resetRows();
	}

	public Long getCourseId() {
		return courseId;
	}

	@Override
	protected Collection<CourseOutVO> getEagerResult(PSFVO psf) {
		if (courseId != null && staffId != null) {
			try {
				return WebUtil.getServiceLocator().getStaffService().getUpcomingRenewalCourses(WebUtil.getAuthentication(), null, courseId, staffId);
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return new ArrayList<CourseOutVO>();
	}

	@Override
	protected CourseOutVO getRowElement(Long id) {
		return WebUtil.getCourse(id, null, null, null);
	}

	public Long getStaffId() {
		return staffId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}
}
