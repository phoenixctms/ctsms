package org.phoenixctms.ctsms.web.model.shared;

import java.util.ArrayList;
import java.util.Collection;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.VisitScheduleItemOutVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class VisitScheduleItemLazyModel extends LazyDataModelBase<VisitScheduleItemOutVO> {

	private Long trialId;
	private Long probandId;

	@Override
	protected Collection<VisitScheduleItemOutVO> getLazyResult(PSFVO psf) {
		if (trialId != null || probandId != null) {
			try {
				return WebUtil.getServiceLocator().getTrialService().getVisitScheduleItemList(WebUtil.getAuthentication(), trialId, null, null, probandId, psf);
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return new ArrayList<VisitScheduleItemOutVO>();
	}

	public Long getProbandId() {
		return probandId;
	}

	@Override
	protected VisitScheduleItemOutVO getRowElement(Long id) {
		return WebUtil.getVisitScheduleItem(id);
	}

	public Long getTrialId() {
		return trialId;
	}

	public void setProbandId(Long probandId) {
		this.probandId = probandId;
	}

	public void setTrialId(Long trialId) {
		this.trialId = trialId;
	}
}
