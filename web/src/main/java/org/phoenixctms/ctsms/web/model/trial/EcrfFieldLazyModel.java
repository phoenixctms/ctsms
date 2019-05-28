package org.phoenixctms.ctsms.web.model.trial;

import java.util.ArrayList;
import java.util.Collection;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.ECRFFieldOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class EcrfFieldLazyModel extends LazyDataModelBase<ECRFFieldOutVO> {

	private Long trialId;
	private Long ecrfId;

	public Long getEcrfId() {
		return ecrfId;
	}

	@Override
	protected Collection<ECRFFieldOutVO> getLazyResult(PSFVO psf) {
		if (trialId != null || ecrfId != null) { // trialId is always required
			try {
				return WebUtil.getServiceLocator().getTrialService().getEcrfFieldList(WebUtil.getAuthentication(), trialId, ecrfId, false, psf);
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return new ArrayList<ECRFFieldOutVO>();
	}

	@Override
	protected ECRFFieldOutVO getRowElement(Long id) {
		return WebUtil.getEcrfField(id);
	}

	public Long getTrialId() {
		return trialId;
	}

	public void setEcrfId(Long ecrfId) {
		this.ecrfId = ecrfId;
	}

	public void setTrialId(Long trialId) {
		this.trialId = trialId;
	}
}
