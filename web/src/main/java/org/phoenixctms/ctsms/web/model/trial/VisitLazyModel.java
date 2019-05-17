package org.phoenixctms.ctsms.web.model.trial;

import java.util.ArrayList;
import java.util.Collection;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.VisitOutVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class VisitLazyModel extends LazyDataModelBase {

	private Long trialId;

	@Override
	protected Collection<VisitOutVO> getLazyResult(PSFVO psf) {
		if (trialId != null) {
			try {
				return WebUtil.getServiceLocator().getTrialService().getVisitList(WebUtil.getAuthentication(), trialId, psf);
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return new ArrayList<VisitOutVO>();
	}

	@Override
	protected VisitOutVO getRowElement(Long id) {
		return WebUtil.getVisit(id);
	}

	public Long getTrialId() {
		return trialId;
	}

	public void setTrialId(Long trialId) {
		this.trialId = trialId;
	}
}
