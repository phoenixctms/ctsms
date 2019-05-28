package org.phoenixctms.ctsms.web.model.shared;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.CourseParticipationStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.DutyRosterTurnOutVO;
import org.phoenixctms.ctsms.vo.InventoryBookingOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.StaffStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.VisitScheduleItemOutVO;
import org.phoenixctms.ctsms.web.model.EagerDataModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class CollidingStaffStatusEntryEagerModel extends EagerDataModelBase<StaffStatusEntryOutVO> {

	public static CollidingStaffStatusEntryEagerModel getCachedCollidingStaffStatusEntryModel(CourseParticipationStatusEntryOutVO courseParticipation, boolean load,
			HashMap<Long, CollidingStaffStatusEntryEagerModel> collidingStaffStatusEntryModelCache) {
		CollidingStaffStatusEntryEagerModel model;
		if (courseParticipation != null && collidingStaffStatusEntryModelCache != null) {
			long courseParticipationStatusEntryId = courseParticipation.getId();
			if (collidingStaffStatusEntryModelCache.containsKey(courseParticipationStatusEntryId)) {
				model = collidingStaffStatusEntryModelCache.get(courseParticipationStatusEntryId);
			} else {
				if (load) {
					model = new CollidingStaffStatusEntryEagerModel();
					model.setCourseParticipationStatusEntryId(courseParticipationStatusEntryId);
					collidingStaffStatusEntryModelCache.put(courseParticipationStatusEntryId, model);
				} else {
					model = new CollidingStaffStatusEntryEagerModel();
				}
			}
		} else {
			model = new CollidingStaffStatusEntryEagerModel();
		}
		return model;
	}

	public static CollidingStaffStatusEntryEagerModel getCachedCollidingStaffStatusEntryModel(DutyRosterTurnOutVO dutyRosterTurn, boolean load,
			HashMap<Long, CollidingStaffStatusEntryEagerModel> collidingStaffStatusEntryModelCache) {
		CollidingStaffStatusEntryEagerModel model;
		if (dutyRosterTurn != null && collidingStaffStatusEntryModelCache != null) {
			long dutyRosterTurnId = dutyRosterTurn.getId();
			if (collidingStaffStatusEntryModelCache.containsKey(dutyRosterTurnId)) {
				model = collidingStaffStatusEntryModelCache.get(dutyRosterTurnId);
			} else {
				if (load) {
					model = new CollidingStaffStatusEntryEagerModel();
					model.setDutyRosterTurnId(dutyRosterTurnId);
					collidingStaffStatusEntryModelCache.put(dutyRosterTurnId, model);
				} else {
					model = new CollidingStaffStatusEntryEagerModel();
				}
			}
		} else {
			model = new CollidingStaffStatusEntryEagerModel();
		}
		return model;
	}

	public static CollidingStaffStatusEntryEagerModel getCachedCollidingStaffStatusEntryModel(InventoryBookingOutVO courseBooking, boolean load,
			HashMap<Long, CollidingStaffStatusEntryEagerModel> collidingStaffStatusEntryModelCache) {
		CollidingStaffStatusEntryEagerModel model;
		if (courseBooking != null && collidingStaffStatusEntryModelCache != null) {
			long courseInventoryBookingId = courseBooking.getId();
			if (collidingStaffStatusEntryModelCache.containsKey(courseInventoryBookingId)) {
				model = collidingStaffStatusEntryModelCache.get(courseInventoryBookingId);
			} else {
				if (load) {
					model = new CollidingStaffStatusEntryEagerModel();
					model.setCourseInventoryBookingId(courseInventoryBookingId);
					collidingStaffStatusEntryModelCache.put(courseInventoryBookingId, model);
				} else {
					model = new CollidingStaffStatusEntryEagerModel();
				}
			}
		} else {
			model = new CollidingStaffStatusEntryEagerModel();
		}
		return model;
	}

	public static CollidingStaffStatusEntryEagerModel getCachedCollidingStaffStatusEntryModel(StaffOutVO staff, Date start, Date stop, boolean load,
			HashMap<Long, CollidingStaffStatusEntryEagerModel> collidingStaffStatusEntryModelCache) {
		CollidingStaffStatusEntryEagerModel model;
		if (staff != null && collidingStaffStatusEntryModelCache != null) {
			long staffId = staff.getId();
			if (collidingStaffStatusEntryModelCache.containsKey(staffId)) {
				model = collidingStaffStatusEntryModelCache.get(staffId);
			} else {
				if (load) {
					model = new CollidingStaffStatusEntryEagerModel();
					model.setStaffId(staffId);
					model.setStart(start);
					model.setStop(stop);
					collidingStaffStatusEntryModelCache.put(staffId, model);
				} else {
					model = new CollidingStaffStatusEntryEagerModel();
				}
			}
		} else {
			model = new CollidingStaffStatusEntryEagerModel();
		}
		return model;
	}

	public static CollidingStaffStatusEntryEagerModel getCachedCollidingStaffStatusEntryModel(VisitScheduleItemOutVO visitScheduleItem, boolean load,
			HashMap<Long, CollidingStaffStatusEntryEagerModel> collidingStaffStatusEntryModelCache) {
		CollidingStaffStatusEntryEagerModel model;
		if (visitScheduleItem != null && collidingStaffStatusEntryModelCache != null) {
			long visitScheduleItemId = visitScheduleItem.getId();
			if (collidingStaffStatusEntryModelCache.containsKey(visitScheduleItemId)) {
				model = collidingStaffStatusEntryModelCache.get(visitScheduleItemId);
			} else {
				if (load) {
					model = new CollidingStaffStatusEntryEagerModel();
					model.setVisitScheduleItemId(visitScheduleItemId);
					collidingStaffStatusEntryModelCache.put(visitScheduleItemId, model);
				} else {
					model = new CollidingStaffStatusEntryEagerModel();
				}
			}
		} else {
			model = new CollidingStaffStatusEntryEagerModel();
		}
		return model;
	}

	private Long visitScheduleItemId;
	private Long dutyRosterTurnId;
	private Long courseInventoryBookingId;
	private Long courseParticipationStatusEntryId;
	private Long staffId;
	private Date start;
	private Date stop;

	public CollidingStaffStatusEntryEagerModel() {
		super();
		resetRows();
	}

	public Long getCourseInventoryBookingId() {
		return courseInventoryBookingId;
	}

	public Long getCourseParticipationStatusEntryId() {
		return courseParticipationStatusEntryId;
	}

	public Long getDutyRosterTurnId() {
		return dutyRosterTurnId;
	}

	@Override
	protected Collection<StaffStatusEntryOutVO> getEagerResult(PSFVO psf) {
		if (dutyRosterTurnId != null) {
			try {
				return WebUtil.getServiceLocator().getStaffService().getCollidingStaffStatusEntries(WebUtil.getAuthentication(), dutyRosterTurnId);
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		} else if (courseInventoryBookingId != null) {
			try {
				return WebUtil.getServiceLocator().getInventoryService().getCollidingStaffStatusEntries(WebUtil.getAuthentication(), courseInventoryBookingId, true);
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		} else if (courseParticipationStatusEntryId != null) {
			try {
				return WebUtil.getServiceLocator().getCourseService().getCollidingStaffStatusEntries(WebUtil.getAuthentication(), courseParticipationStatusEntryId, true);
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		} else if (visitScheduleItemId != null) {
			try {
				return WebUtil.getServiceLocator().getTrialService().getCollidingStaffStatusEntries(WebUtil.getAuthentication(), visitScheduleItemId, staffId);
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		} else if (staffId != null && start != null && stop != null) {
			try {
				return WebUtil.getServiceLocator().getStaffService().getCollidingStaffStatusEntriesInterval(WebUtil.getAuthentication(), staffId, start, stop);
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return new ArrayList<StaffStatusEntryOutVO>();
	}

	@Override
	protected StaffStatusEntryOutVO getRowElement(Long id) {
		return WebUtil.getStaffStatusEntry(id);
	}

	public Long getStaffId() {
		return staffId;
	}

	public Date getStart() {
		return start;
	}

	public Date getStop() {
		return stop;
	}

	public Long getVisitScheduleItemId() {
		return visitScheduleItemId;
	}

	public void setCourseInventoryBookingId(Long courseInventoryBookingId) {
		this.courseInventoryBookingId = courseInventoryBookingId;
	}

	public void setCourseParticipationStatusEntryId(
			Long courseParticipationStatusEntryId) {
		this.courseParticipationStatusEntryId = courseParticipationStatusEntryId;
	}

	public void setDutyRosterTurnId(Long dutyRosterTurnId) {
		this.dutyRosterTurnId = dutyRosterTurnId;
	}

	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public void setStop(Date stop) {
		this.stop = stop;
	}

	public void setVisitScheduleItemId(Long visitScheduleItemId) {
		this.visitScheduleItemId = visitScheduleItemId;
	}
}
