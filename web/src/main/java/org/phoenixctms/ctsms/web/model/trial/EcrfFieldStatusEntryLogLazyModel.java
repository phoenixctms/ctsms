package org.phoenixctms.ctsms.web.model.trial;

import java.util.ArrayList;
import java.util.Collection;

import org.phoenixctms.ctsms.enumeration.ECRFFieldStatusQueue;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.ECRFFieldStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class EcrfFieldStatusEntryLogLazyModel extends LazyDataModelBase {

	private Long trialId;
	private ECRFFieldStatusQueue queue;

	public EcrfFieldStatusEntryLogLazyModel() {
		super();
	}

	@Override
	protected Collection<ECRFFieldStatusEntryOutVO> getLazyResult(PSFVO psf) {
		if (trialId != null) {
			try {
				return WebUtil
						.getServiceLocator()
						.getTrialService()
						.getEcrfFieldStatusEntryLog(WebUtil.getAuthentication(), queue, trialId, null, null, true, psf == null || CommonUtil.isEmptyString(psf.getSortField()), psf);
			} catch (ServiceException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			} catch (AuthorisationException e) {
			} catch (IllegalArgumentException e) {
			}
		}
		return new ArrayList<ECRFFieldStatusEntryOutVO>();
	}

	public ECRFFieldStatusQueue getQueue() {
		return queue;
	}

	@Override
	protected ECRFFieldStatusEntryOutVO getRowElement(Long id) {
		return WebUtil.getEcrfFieldStatusEntry(id);
	}

	public Long getTrialId() {
		return trialId;
	}

	public void setQueue(ECRFFieldStatusQueue queue) {
		this.queue = queue;
	}

	public void setTrialId(Long trialId) {
		this.trialId = trialId;
	}
}
