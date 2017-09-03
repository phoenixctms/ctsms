package org.phoenixctms.ctsms.web.model.course;

import java.util.ArrayList;
import java.util.Collection;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.CourseOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class AdminUpcomingCourseLazyModel extends LazyDataModelBase {

	private Long departmentId;
	private Long courseCategoryId;

	public Long getCourseCategoryId() {
		return courseCategoryId;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	@Override
	protected Collection<CourseOutVO> getLazyResult(PSFVO psf) {
		try {
			return WebUtil.getServiceLocator().getCourseService().getUpcomingCourses(WebUtil.getAuthentication(), null, departmentId, courseCategoryId, psf);
		} catch (ServiceException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
		} catch (IllegalArgumentException e) {
		}
		return new ArrayList<CourseOutVO>();
	}

	@Override
	protected CourseOutVO getRowElement(Long id) {
		return WebUtil.getCourse(id, null, null, null);
	}

	public void setCourseCategoryId(Long courseCategoryId) {
		this.courseCategoryId = courseCategoryId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}
}
