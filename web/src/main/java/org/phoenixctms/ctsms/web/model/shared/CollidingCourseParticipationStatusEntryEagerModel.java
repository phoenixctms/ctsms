package org.phoenixctms.ctsms.web.model.shared;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.CourseParticipationStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.InventoryBookingOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.model.EagerDataModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class CollidingCourseParticipationStatusEntryEagerModel extends EagerDataModelBase<CourseParticipationStatusEntryOutVO> {

	public static CollidingCourseParticipationStatusEntryEagerModel getCachedCollidingCourseParticipationStatusEntryModel(InventoryBookingOutVO courseBooking, boolean load,
			HashMap<Long, CollidingCourseParticipationStatusEntryEagerModel> collidingCourseParticipationStatusEntryModelCache) {
		CollidingCourseParticipationStatusEntryEagerModel model;
		if (courseBooking != null && collidingCourseParticipationStatusEntryModelCache != null) {
			long courseInventoryBookingId = courseBooking.getId();
			if (collidingCourseParticipationStatusEntryModelCache.containsKey(courseInventoryBookingId)) {
				model = collidingCourseParticipationStatusEntryModelCache.get(courseInventoryBookingId);
			} else {
				if (load) {
					model = new CollidingCourseParticipationStatusEntryEagerModel();
					model.setCourseInventoryBookingId(courseInventoryBookingId);
					collidingCourseParticipationStatusEntryModelCache.put(courseInventoryBookingId, model);
				} else {
					model = new CollidingCourseParticipationStatusEntryEagerModel();
				}
			}
		} else {
			model = new CollidingCourseParticipationStatusEntryEagerModel();
		}
		return model;
	}

	private Long courseInventoryBookingId;

	public CollidingCourseParticipationStatusEntryEagerModel() {
		super();
		resetRows();
	}

	public Long getCourseInventoryBookingId() {
		return courseInventoryBookingId;
	}

	@Override
	protected Collection<CourseParticipationStatusEntryOutVO> getEagerResult(PSFVO psf) {
		if (courseInventoryBookingId != null) {
			try {
				return WebUtil.getServiceLocator().getInventoryService().getCollidingCourseParticipationStatusEntries(WebUtil.getAuthentication(), courseInventoryBookingId, true);
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

	public void setCourseInventoryBookingId(Long courseInventoryBookingId) {
		this.courseInventoryBookingId = courseInventoryBookingId;
	}
}
