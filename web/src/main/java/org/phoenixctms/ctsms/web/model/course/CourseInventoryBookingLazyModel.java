package org.phoenixctms.ctsms.web.model.course;

import java.util.ArrayList;
import java.util.Collection;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.InventoryBookingOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class CourseInventoryBookingLazyModel extends LazyDataModelBase {

	private Long courseId;

	public Long getCourseId() {
		return courseId;
	}

	@Override
	protected Collection<InventoryBookingOutVO> getLazyResult(PSFVO psf) {
		if (courseId != null) {
			try {
				return WebUtil.getServiceLocator().getCourseService().getCourseInventoryBookingList(WebUtil.getAuthentication(), courseId, psf);
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return new ArrayList<InventoryBookingOutVO>();
	}

	@Override
	protected InventoryBookingOutVO getRowElement(Long id) {
		return WebUtil.getInventoryBooking(id);
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}
}
