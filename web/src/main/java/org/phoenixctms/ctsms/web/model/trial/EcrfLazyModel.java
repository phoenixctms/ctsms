package org.phoenixctms.ctsms.web.model.trial;

import java.util.ArrayList;
import java.util.Collection;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.ECRFOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class EcrfLazyModel extends LazyDataModelBase {

	private Long trialId;
	private Long probandGroupId;
	private Boolean active;
	private Long probandListEntryId;

	public Boolean getActive() {
		return active;
	}

	@Override
	protected Collection<ECRFOutVO> getLazyResult(PSFVO psf) {
		if (probandListEntryId != null) {
			try {
				return WebUtil.getServiceLocator().getTrialService().getEcrfList(WebUtil.getAuthentication(), probandListEntryId, active, psf);
			} catch (ServiceException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			} catch (AuthorisationException e) {
			} catch (IllegalArgumentException e) {
			}
		} else if (trialId != null) { // trialId is always required
			try {
				return WebUtil.getServiceLocator().getTrialService().getEcrfList(WebUtil.getAuthentication(), trialId, probandGroupId, null, active, false, psf);
			} catch (ServiceException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			} catch (AuthorisationException e) {
			} catch (IllegalArgumentException e) {
			}
		}
		return new ArrayList<ECRFOutVO>();
	}

	public Long getProbandGroupId() {
		return probandGroupId;
	}

	public Long getProbandListEntryId() {
		return probandListEntryId;
	}

	@Override
	protected ECRFOutVO getRowElement(Long id) {
		return WebUtil.getEcrf(id);
	}

	public Long getTrialId() {
		return trialId;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public void setProbandGroupId(Long probandGroupId) {
		this.probandGroupId = probandGroupId;
	}

	public void setProbandListEntryId(Long probandListEntryId) {
		this.probandListEntryId = probandListEntryId;
	}

	public void setTrialId(Long trialId) {
		this.trialId = trialId;
	}
}
