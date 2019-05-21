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
import org.phoenixctms.ctsms.vo.InventoryStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.ProbandStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.StaffStatusEntryOutVO;
import org.phoenixctms.ctsms.web.model.EagerDataModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class CollidingInventoryBookingEagerModel extends EagerDataModelBase {

	public static CollidingInventoryBookingEagerModel getCachedCollidingInventoryBookingModel(CourseParticipationStatusEntryOutVO courseParticipation, boolean load,
			HashMap<Long, CollidingInventoryBookingEagerModel> collidingInventoryBookingModelCache) {
		CollidingInventoryBookingEagerModel model;
		if (courseParticipation != null && collidingInventoryBookingModelCache != null) {
			long courseParticipationStatusEntryId = courseParticipation.getId();
			if (collidingInventoryBookingModelCache.containsKey(courseParticipationStatusEntryId)) {
				model = collidingInventoryBookingModelCache.get(courseParticipationStatusEntryId);
			} else {
				if (load) {
					model = new CollidingInventoryBookingEagerModel();
					model.setCourseParticipationStatusEntryId(courseParticipationStatusEntryId);
					collidingInventoryBookingModelCache.put(courseParticipationStatusEntryId, model);
				} else {
					model = new CollidingInventoryBookingEagerModel();
				}
			}
		} else {
			model = new CollidingInventoryBookingEagerModel();
		}
		return model;
	}

	public static CollidingInventoryBookingEagerModel getCachedCollidingInventoryBookingModel(DutyRosterTurnOutVO dutyRosterTurn, boolean load,
			HashMap<Long, CollidingInventoryBookingEagerModel> collidingInventoryBookingModelCache) {
		CollidingInventoryBookingEagerModel model;
		if (dutyRosterTurn != null && collidingInventoryBookingModelCache != null) {
			long dutyRosterTurnId = dutyRosterTurn.getId();
			if (collidingInventoryBookingModelCache.containsKey(dutyRosterTurnId)) {
				model = collidingInventoryBookingModelCache.get(dutyRosterTurnId);
			} else {
				if (load) {
					model = new CollidingInventoryBookingEagerModel();
					model.setDutyRosterTurnId(dutyRosterTurnId);
					collidingInventoryBookingModelCache.put(dutyRosterTurnId, model);
				} else {
					model = new CollidingInventoryBookingEagerModel();
				}
			}
		} else {
			model = new CollidingInventoryBookingEagerModel();
		}
		return model;
	}

	public static CollidingInventoryBookingEagerModel getCachedCollidingInventoryBookingModel(InventoryStatusEntryOutVO statusEntry, boolean load,
			HashMap<Long, CollidingInventoryBookingEagerModel> collidingInventoryBookingModelCache) {
		CollidingInventoryBookingEagerModel model;
		if (statusEntry != null && collidingInventoryBookingModelCache != null) {
			long inventoryStatusEntryId = statusEntry.getId();
			if (collidingInventoryBookingModelCache.containsKey(inventoryStatusEntryId)) {
				model = collidingInventoryBookingModelCache.get(inventoryStatusEntryId);
			} else {
				if (load) {
					model = new CollidingInventoryBookingEagerModel();
					model.setInventoryStatusEntryId(inventoryStatusEntryId);
					collidingInventoryBookingModelCache.put(inventoryStatusEntryId, model);
				} else {
					model = new CollidingInventoryBookingEagerModel();
				}
			}
		} else {
			model = new CollidingInventoryBookingEagerModel();
		}
		return model;
	}

	public static CollidingInventoryBookingEagerModel getCachedCollidingInventoryBookingModel(ProbandStatusEntryOutVO statusEntry, boolean load,
			HashMap<Long, CollidingInventoryBookingEagerModel> collidingInventoryBookingModelCache) {
		CollidingInventoryBookingEagerModel model;
		if (statusEntry != null && collidingInventoryBookingModelCache != null) {
			long probandStatusEntryId = statusEntry.getId();
			if (collidingInventoryBookingModelCache.containsKey(probandStatusEntryId)) {
				model = collidingInventoryBookingModelCache.get(probandStatusEntryId);
			} else {
				if (load) {
					model = new CollidingInventoryBookingEagerModel();
					model.setProbandStatusEntryId(probandStatusEntryId);
					collidingInventoryBookingModelCache.put(probandStatusEntryId, model);
				} else {
					model = new CollidingInventoryBookingEagerModel();
				}
			}
		} else {
			model = new CollidingInventoryBookingEagerModel();
		}
		return model;
	}

	public static CollidingInventoryBookingEagerModel getCachedCollidingInventoryBookingModel(StaffStatusEntryOutVO statusEntry, boolean load,
			HashMap<Long, CollidingInventoryBookingEagerModel> collidingInventoryBookingModelCache) {
		CollidingInventoryBookingEagerModel model;
		if (statusEntry != null && collidingInventoryBookingModelCache != null) {
			long staffStatusEntryId = statusEntry.getId();
			if (collidingInventoryBookingModelCache.containsKey(staffStatusEntryId)) {
				model = collidingInventoryBookingModelCache.get(staffStatusEntryId);
			} else {
				if (load) {
					model = new CollidingInventoryBookingEagerModel();
					model.setStaffStatusEntryId(staffStatusEntryId);
					collidingInventoryBookingModelCache.put(staffStatusEntryId, model);
				} else {
					model = new CollidingInventoryBookingEagerModel();
				}
			}
		} else {
			model = new CollidingInventoryBookingEagerModel();
		}
		return model;
	}

	private Long inventoryStatusEntryId;
	private Long courseParticipationStatusEntryId;
	private Long staffStatusEntryId;
	private Long probandStatusEntryId;
	private Long dutyRosterTurnId;

	public CollidingInventoryBookingEagerModel() {
		super();
		resetRows();
	}

	public Long getCourseParticipationStatusEntryId() {
		return courseParticipationStatusEntryId;
	}

	public Long getDutyRosterTurnId() {
		return dutyRosterTurnId;
	}

	@Override
	protected Collection<InventoryBookingOutVO> getEagerResult(PSFVO psf) {
		if (inventoryStatusEntryId != null) {
			try {
				return WebUtil.getServiceLocator().getInventoryService().getCollidingInventoryBookings(WebUtil.getAuthentication(), inventoryStatusEntryId);
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		} else if (staffStatusEntryId != null) {
			try {
				return WebUtil.getServiceLocator().getStaffService().getCollidingCourseInventoryBookingsByStaffStatusEntry(WebUtil.getAuthentication(), staffStatusEntryId, true);
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		} else if (dutyRosterTurnId != null) {
			try {
				return WebUtil.getServiceLocator().getStaffService().getCollidingCourseInventoryBookingsByDutyRosterTurn(WebUtil.getAuthentication(), dutyRosterTurnId, true);
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		} else if (probandStatusEntryId != null) {
			try {
				return WebUtil.getServiceLocator().getProbandService().getCollidingProbandInventoryBookings(WebUtil.getAuthentication(), probandStatusEntryId, true);
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		} else if (courseParticipationStatusEntryId != null) {
			try {
				return WebUtil.getServiceLocator().getStaffService()
						.getCollidingCourseInventoryBookingsByCourseParticipationStatusEntry(WebUtil.getAuthentication(), courseParticipationStatusEntryId, true);
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return new ArrayList<InventoryBookingOutVO>();
	}

	public Long getInventoryStatusEntryId() {
		return inventoryStatusEntryId;
	}

	public Long getProbandStatusEntryId() {
		return probandStatusEntryId;
	}

	@Override
	protected InventoryBookingOutVO getRowElement(Long id) {
		return WebUtil.getInventoryBooking(id);
	}

	public Long getStaffStatusEntryId() {
		return staffStatusEntryId;
	}

	public void setCourseParticipationStatusEntryId(
			Long courseParticipationStatusEntryId) {
		this.courseParticipationStatusEntryId = courseParticipationStatusEntryId;
	}

	public void setDutyRosterTurnId(Long dutyRosterTurnId) {
		this.dutyRosterTurnId = dutyRosterTurnId;
	}

	public void setInventoryStatusEntryId(Long inventoryStatusEntryId) {
		this.inventoryStatusEntryId = inventoryStatusEntryId;
	}

	public void setProbandStatusEntryId(Long probandStatusEntryId) {
		this.probandStatusEntryId = probandStatusEntryId;
	}

	public void setStaffStatusEntryId(Long staffStatusEntryId) {
		this.staffStatusEntryId = staffStatusEntryId;
	}
}
