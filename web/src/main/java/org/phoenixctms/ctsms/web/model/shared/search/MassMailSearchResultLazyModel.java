package org.phoenixctms.ctsms.web.model.shared.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.faces.application.FacesMessage;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.MassMailOutVO;
import org.phoenixctms.ctsms.vo.MassMailProgressVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class MassMailSearchResultLazyModel extends SearchResultLazyModel {

	private HashMap<Long, MassMailProgressVO> massMailProgressCache;

	public MassMailSearchResultLazyModel() {
		super();
		massMailProgressCache = new HashMap<Long, MassMailProgressVO>();
	}

	@Override
	protected Collection<MassMailOutVO> getLazyResultWCount(PSFVO psf) {
		massMailProgressCache.clear();
		if (!noCriteriaSet) {
			try {
				return WebUtil.getServiceLocator().getSearchService().searchMassMail(WebUtil.getAuthentication(), criteriaIn, criterionsIn, psf);
			} catch (ServiceException e) {
				Messages.addCriterionMessages(e.getData(), FacesMessage.SEVERITY_WARN, e.getMessage());
			} catch (AuthenticationException e) {
				Messages.addMessage(FacesMessage.SEVERITY_WARN, e.getMessage());
				WebUtil.publishException(e);
			} catch (AuthorisationException e) {
				Messages.addCriterionMessages(e.getData(), FacesMessage.SEVERITY_WARN, e.getMessage());
			} catch (IllegalArgumentException e) {
				Messages.addMessage(FacesMessage.SEVERITY_WARN, e.getMessage());
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
}