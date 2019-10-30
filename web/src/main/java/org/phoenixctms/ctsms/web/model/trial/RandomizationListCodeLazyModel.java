package org.phoenixctms.ctsms.web.model.trial;

import java.util.ArrayList;
import java.util.Collection;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.RandomizationListCodeOutVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class RandomizationListCodeLazyModel extends LazyDataModelBase<RandomizationListCodeOutVO> {

	private Long trialId;

	@Override
	protected Collection<RandomizationListCodeOutVO> getLazyResult(PSFVO psf) {
		//if (trialId != null) {
		try {
			return WebUtil.getServiceLocator().getTrialService().getRandomizationListCodeList(WebUtil.getAuthentication(), trialId, psf);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
		//}
		return new ArrayList<RandomizationListCodeOutVO>();
	}

	@Override
	protected RandomizationListCodeOutVO getRowElement(Long id) {
		try {
			return WebUtil.getServiceLocator().getTrialService().getRandomizationListCodeById(WebUtil.getAuthentication(), id);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
		return null;
	}

	public Long getTrialId() {
		return trialId;
	}

	public void setTrialId(Long trialId) {
		this.trialId = trialId;
	}

	public String getTrialName() {
		return WebUtil.trialIdToName(trialId);
	}
}
