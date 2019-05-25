package org.phoenixctms.ctsms.web.model.shared;

import java.util.ArrayList;
import java.util.Collection;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.CourseParticipationStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class CourseParticipationStatusEntryLazyModel extends LazyDataModelBase<CourseParticipationStatusEntryOutVO> {

	private Long staffId;
	private Long courseId;

	public Long getCourseId() {
		return courseId;
	}

	@Override
	protected Collection<CourseParticipationStatusEntryOutVO> getLazyResult(PSFVO psf) {
		if (staffId != null || courseId != null) {
			try {
				return WebUtil.getServiceLocator().getCourseService().getCourseParticipationStatusEntryList(WebUtil.getAuthentication(), staffId, courseId, null, psf);
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return new ArrayList<CourseParticipationStatusEntryOutVO>();
	}

	@Override
	protected CourseParticipationStatusEntryOutVO getRowElement(Long id) {
		return WebUtil.getCourseParticipationStatusEntry(id);
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
