package org.phoenixctms.ctsms.web.model.trial;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.MassMailOutVO;
import org.phoenixctms.ctsms.vo.MassMailProgressVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class TrialMassMailLazyModel extends LazyDataModelBase {

	private HashMap<Long, MassMailProgressVO> massMailProgressCache;
	private Long trialId;

	public TrialMassMailLazyModel() {
		super();
		massMailProgressCache = new HashMap<Long, MassMailProgressVO>();
	}

	@Override
	protected Collection<MassMailOutVO> getLazyResult(PSFVO psf) {
		massMailProgressCache.clear();
		if (trialId != null) {
			try {
				return WebUtil.getServiceLocator().getTrialService().getTrialMassMailList(WebUtil.getAuthentication(), trialId, psf);
			} catch (ServiceException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			} catch (AuthorisationException e) {
			} catch (IllegalArgumentException e) {
			}
		}
		return new ArrayList<MassMailOutVO>();
	}

	public MassMailProgressVO getMassMailProgress(MassMailOutVO massMail) {
		if (massMail != null) {
			if (massMailProgressCache.containsKey(massMail.getId())) {
				return massMailProgressCache.get(massMail.getId());
			} else {
				MassMailProgressVO massMailProgress = WebUtil.getMassMailProgress(massMail.getId());
				massMailProgressCache.put(massMail.getId(), massMailProgress);
				return massMailProgress;
			}
		}
		return null;

	}

	@Override
	protected MassMailOutVO getRowElement(Long id) {
		return WebUtil.getMassMail(id);
	}

	public Long getTrialId() {
		return trialId;
	}
	public void setTrialId(Long trialId) {
		this.trialId = trialId;
	}
}
