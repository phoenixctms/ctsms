package org.phoenixctms.ctsms.web.model.trial;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.VisitScheduleAppointmentVO;
import org.phoenixctms.ctsms.vo.VisitScheduleItemOutVO;
import org.phoenixctms.ctsms.web.model.EagerDataModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class VisitScheduleAppointmentEagerModel extends EagerDataModelBase<VisitScheduleAppointmentVO> {

	public static VisitScheduleAppointmentEagerModel getCachedVisitScheduleAppointmentModel(VisitScheduleItemOutVO visitScheduleItem, boolean load,
			HashMap<Long, VisitScheduleAppointmentEagerModel> visitScheduleAppointmentModelCache) {
		VisitScheduleAppointmentEagerModel model;
		if (visitScheduleItem != null && visitScheduleAppointmentModelCache != null) {
			long visitScheduleItemId = visitScheduleItem.getId();
			if (visitScheduleAppointmentModelCache.containsKey(visitScheduleItemId)) {
				model = visitScheduleAppointmentModelCache.get(visitScheduleItemId);
			} else {
				if (load) {
					model = new VisitScheduleAppointmentEagerModel();
					model.setVisitScheduleItemId(visitScheduleItemId);
					visitScheduleAppointmentModelCache.put(visitScheduleItemId, model);
				} else {
					model = new VisitScheduleAppointmentEagerModel();
				}
			}
		} else {
			model = new VisitScheduleAppointmentEagerModel();
		}
		return model;
	}

	private Long visitScheduleItemId;
	private Long probandId;

	public VisitScheduleAppointmentEagerModel() {
		super();
		resetRows();
	}

	@Override
	protected Collection<VisitScheduleAppointmentVO> getEagerResult(PSFVO psf) {
		if (visitScheduleItemId != null) {
			try {
				return WebUtil.getServiceLocator().getTrialService().getVisitScheduleAppointmentList(WebUtil.getAuthentication(), visitScheduleItemId, probandId, null);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return new ArrayList<VisitScheduleAppointmentVO>();
	}

	@Override
	protected VisitScheduleAppointmentVO getRowElement(Long id) {
		return null; //not selectable.
	}

	public Long getVisitScheduleItemId() {
		return visitScheduleItemId;
	}

	public void setVisitScheduleItemId(Long visitScheduleItemId) {
		this.visitScheduleItemId = visitScheduleItemId;
	}

	public Long getProbandId() {
		return probandId;
	}

	public void setProbandId(Long probandId) {
		this.probandId = probandId;
	}
}
