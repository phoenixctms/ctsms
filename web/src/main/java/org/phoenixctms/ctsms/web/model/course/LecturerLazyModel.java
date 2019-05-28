package org.phoenixctms.ctsms.web.model.course;

import java.util.ArrayList;
import java.util.Collection;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.LecturerOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class LecturerLazyModel extends LazyDataModelBase<LecturerOutVO> {

	private Long courseId;

	public Long getCourseId() {
		return courseId;
	}

	@Override
	protected Collection<LecturerOutVO> getLazyResult(PSFVO psf) {
		if (courseId != null) {
			try {
				return WebUtil.getServiceLocator().getCourseService().getLecturerList(WebUtil.getAuthentication(), courseId, null, psf);
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return new ArrayList<LecturerOutVO>();
	}

	@Override
	protected LecturerOutVO getRowElement(Long id) {
		return WebUtil.getLecturer(id);
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}
}
