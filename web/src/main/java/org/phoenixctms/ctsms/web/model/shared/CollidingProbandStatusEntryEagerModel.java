package org.phoenixctms.ctsms.web.model.shared;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.InventoryBookingOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryOutVO;
import org.phoenixctms.ctsms.vo.ProbandStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.VisitScheduleItemOutVO;
import org.phoenixctms.ctsms.web.model.EagerDataModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class CollidingProbandStatusEntryEagerModel extends EagerDataModelBase {

	public static CollidingProbandStatusEntryEagerModel getCachedCollidingProbandStatusEntryModel(InventoryBookingOutVO probandBooking, boolean load,
			HashMap<Long, CollidingProbandStatusEntryEagerModel> collidingProbandStatusEntryModelCache) {
		CollidingProbandStatusEntryEagerModel model;
		if (probandBooking != null && collidingProbandStatusEntryModelCache != null) {
			long probandInventoryBookingId = probandBooking.getId();
			if (collidingProbandStatusEntryModelCache.containsKey(probandInventoryBookingId)) {
				model = collidingProbandStatusEntryModelCache.get(probandInventoryBookingId);
			} else {
				if (load) {
					model = new CollidingProbandStatusEntryEagerModel();
					model.setProbandInventoryBookingId(probandInventoryBookingId);
					collidingProbandStatusEntryModelCache.put(probandInventoryBookingId, model);
				} else {
					model = new CollidingProbandStatusEntryEagerModel();
				}
			}
		} else {
			model = new CollidingProbandStatusEntryEagerModel();
		}
		return model;
	}

	public static CollidingProbandStatusEntryEagerModel getCachedCollidingProbandStatusEntryModel(ProbandListEntryOutVO probandListEntry, boolean load,
			HashMap<Long, CollidingProbandStatusEntryEagerModel> collidingProbandStatusEntryModelCache) {
		CollidingProbandStatusEntryEagerModel model;
		if (probandListEntry != null && collidingProbandStatusEntryModelCache != null) {
			long probandListEntryId = probandListEntry.getId();
			if (collidingProbandStatusEntryModelCache.containsKey(probandListEntryId)) {
				model = collidingProbandStatusEntryModelCache.get(probandListEntryId);
			} else {
				if (load) {
					model = new CollidingProbandStatusEntryEagerModel();
					model.setProbandListEntryId(probandListEntryId);
					collidingProbandStatusEntryModelCache.put(probandListEntryId, model);
				} else {
					model = new CollidingProbandStatusEntryEagerModel();
				}
			}
		} else {
			model = new CollidingProbandStatusEntryEagerModel();
		}
		return model;
	}

	public static CollidingProbandStatusEntryEagerModel getCachedCollidingProbandStatusEntryModel(VisitScheduleItemOutVO visitScheduleItem, boolean load,
			HashMap<Long, CollidingProbandStatusEntryEagerModel> collidingProbandStatusEntryModelCache) {
		CollidingProbandStatusEntryEagerModel model;
		if (visitScheduleItem != null && collidingProbandStatusEntryModelCache != null) {
			long visitScheduleItemId = visitScheduleItem.getId();
			if (collidingProbandStatusEntryModelCache.containsKey(visitScheduleItemId)) {
				model = collidingProbandStatusEntryModelCache.get(visitScheduleItemId);
			} else {
				if (load) {
					model = new CollidingProbandStatusEntryEagerModel();
					model.setVisitScheduleItemId(visitScheduleItemId);
					collidingProbandStatusEntryModelCache.put(visitScheduleItemId, model);
				} else {
					model = new CollidingProbandStatusEntryEagerModel();
				}
			}
		} else {
			model = new CollidingProbandStatusEntryEagerModel();
		}
		return model;
	}

	private Long probandListEntryId;
	private Long visitScheduleItemId;
	private Long probandId;
	private Long probandInventoryBookingId;

	public CollidingProbandStatusEntryEagerModel() {
		super();
		resetRows();
	}

	@Override
	protected Collection<ProbandStatusEntryOutVO> getEagerResult(PSFVO psf) {
		if (probandListEntryId != null) {
			try {
				return WebUtil.getServiceLocator().getTrialService().getCollidingProbandStatusEntries(WebUtil.getAuthentication(), probandListEntryId);
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		} else if (visitScheduleItemId != null) {
			try {
				return WebUtil.getServiceLocator().getTrialService().getCollidingProbandStatusEntries(WebUtil.getAuthentication(), visitScheduleItemId, probandId);
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		} else if (probandInventoryBookingId != null) {
			try {
				return WebUtil.getServiceLocator().getInventoryService().getCollidingProbandStatusEntries(WebUtil.getAuthentication(), probandInventoryBookingId, true);
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return new ArrayList<ProbandStatusEntryOutVO>();
	}

	public Long getProbandId() {
		return probandId;
	}

	public Long getProbandInventoryBookingId() {
		return probandInventoryBookingId;
	}

	public Long getProbandListEntryId() {
		return probandListEntryId;
	}

	@Override
	protected ProbandStatusEntryOutVO getRowElement(Long id) {
		return WebUtil.getProbandStatusEntry(id);
	}

	public Long getVisitScheduleItemId() {
		return visitScheduleItemId;
	}

	public void setProbandId(Long probandId) {
		this.probandId = probandId;
	}

	public void setProbandInventoryBookingId(Long probandInventoryBookingId) {
		this.probandInventoryBookingId = probandInventoryBookingId;
	}

	public void setProbandListEntryId(Long probandListEntryId) {
		this.probandListEntryId = probandListEntryId;
	}

	public void setVisitScheduleItemId(Long visitScheduleItemId) {
		this.visitScheduleItemId = visitScheduleItemId;
	}
}
