package org.phoenixctms.ctsms.web.model.shared;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.CourseParticipationStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.DutyRosterTurnOutVO;
import org.phoenixctms.ctsms.vo.InventoryBookingOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.StaffStatusEntryOutVO;
import org.phoenixctms.ctsms.web.model.EagerDataModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class CollidingDutyRosterTurnEagerModel extends EagerDataModelBase {

	public static CollidingDutyRosterTurnEagerModel getCachedCollidingDutyRosterTurnModel(CourseParticipationStatusEntryOutVO courseParticipation, boolean load,
			HashMap<Long, CollidingDutyRosterTurnEagerModel> collidingDutyRosterTurnModelCache) {
		CollidingDutyRosterTurnEagerModel model;
		if (courseParticipation != null && collidingDutyRosterTurnModelCache != null) {
			long courseParticipationStatusEntryId = courseParticipation.getId();
			if (collidingDutyRosterTurnModelCache.containsKey(courseParticipationStatusEntryId)) {
				model = collidingDutyRosterTurnModelCache.get(courseParticipationStatusEntryId);
			} else {
				if (load) {
					model = new CollidingDutyRosterTurnEagerModel();
					model.setCourseParticipationStatusEntryId(courseParticipationStatusEntryId);
					collidingDutyRosterTurnModelCache.put(courseParticipationStatusEntryId, model);
				} else {
					model = new CollidingDutyRosterTurnEagerModel();
				}
			}
		} else {
			model = new CollidingDutyRosterTurnEagerModel();
		}
		return model;
	}

	public static CollidingDutyRosterTurnEagerModel getCachedCollidingDutyRosterTurnModel(InventoryBookingOutVO courseBooking, boolean load,
			HashMap<Long, CollidingDutyRosterTurnEagerModel> collidingDutyRosterTurnModelCache) {
		CollidingDutyRosterTurnEagerModel model;
		if (courseBooking != null && collidingDutyRosterTurnModelCache != null) {
			long courseInventoryBookingId = courseBooking.getId();
			if (collidingDutyRosterTurnModelCache.containsKey(courseInventoryBookingId)) {
				model = collidingDutyRosterTurnModelCache.get(courseInventoryBookingId);
			} else {
				if (load) {
					model = new CollidingDutyRosterTurnEagerModel();
					model.setCourseInventoryBookingId(courseInventoryBookingId);
					collidingDutyRosterTurnModelCache.put(courseInventoryBookingId, model);
				} else {
					model = new CollidingDutyRosterTurnEagerModel();
				}
			}
		} else {
			model = new CollidingDutyRosterTurnEagerModel();
		}
		return model;
	}

	public static CollidingDutyRosterTurnEagerModel getCachedCollidingDutyRosterTurnModel(StaffStatusEntryOutVO statusEntry, boolean load,
			HashMap<Long, CollidingDutyRosterTurnEagerModel> collidingDutyRosterTurnModelCache) {
		CollidingDutyRosterTurnEagerModel model;
		if (statusEntry != null && collidingDutyRosterTurnModelCache != null) {
			long staffStatusEntryId = statusEntry.getId();
			if (collidingDutyRosterTurnModelCache.containsKey(staffStatusEntryId)) {
				model = collidingDutyRosterTurnModelCache.get(staffStatusEntryId);
			} else {
				if (load) {
					model = new CollidingDutyRosterTurnEagerModel();
					model.setStaffStatusEntryId(staffStatusEntryId);
					collidingDutyRosterTurnModelCache.put(staffStatusEntryId, model);
				} else {
					model = new CollidingDutyRosterTurnEagerModel();
				}
			}
		} else {
			model = new CollidingDutyRosterTurnEagerModel();
		}
		return model;
	}

	private Long staffStatusEntryId;
	private Long courseInventoryBookingId;
	private Long courseParticipationStatusEntryId;

	public CollidingDutyRosterTurnEagerModel() {
		super();
		resetRows();
	}

	public Long getCourseInventoryBookingId() {
		return courseInventoryBookingId;
	}

	public Long getCourseParticipationStatusEntryId() {
		return courseParticipationStatusEntryId;
	}

	@Override
	protected Collection<DutyRosterTurnOutVO> getEagerResult(PSFVO psf) {
		if (staffStatusEntryId != null) {
			try {
				return WebUtil.getServiceLocator().getStaffService().getCollidingDutyRosterTurns(WebUtil.getAuthentication(), staffStatusEntryId);
			} catch (ServiceException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			} catch (AuthorisationException e) {
			} catch (IllegalArgumentException e) {
			}
		} else if (courseInventoryBookingId != null) {
			try {
				return WebUtil.getServiceLocator().getInventoryService().getCollidingDutyRosterTurns(WebUtil.getAuthentication(), courseInventoryBookingId, true);
			} catch (ServiceException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			} catch (AuthorisationException e) {
			} catch (IllegalArgumentException e) {
			}
		} else if (courseParticipationStatusEntryId != null) {
			try {
				return WebUtil.getServiceLocator().getCourseService().getCollidingDutyRosterTurns(WebUtil.getAuthentication(), courseParticipationStatusEntryId, true);
			} catch (ServiceException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			} catch (AuthorisationException e) {
			} catch (IllegalArgumentException e) {
			}
		}
		return new ArrayList<DutyRosterTurnOutVO>();
	}

	@Override
	protected DutyRosterTurnOutVO getRowElement(Long id) {
		return WebUtil.getDutyRosterTurn(id);
	}

	public Long getStaffStatusEntryId() {
		return staffStatusEntryId;
	}

	public void setCourseInventoryBookingId(Long courseInventoryBookingId) {
		this.courseInventoryBookingId = courseInventoryBookingId;
	}

	public void setCourseParticipationStatusEntryId(
			Long courseParticipationStatusEntryId) {
		this.courseParticipationStatusEntryId = courseParticipationStatusEntryId;
	}

	public void setStaffStatusEntryId(Long staffStatusEntryId) {
		this.staffStatusEntryId = staffStatusEntryId;
	}
}
