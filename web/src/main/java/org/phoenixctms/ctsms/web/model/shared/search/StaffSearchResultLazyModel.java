package org.phoenixctms.ctsms.web.model.shared.search;

import java.util.ArrayList;
import java.util.Collection;

import javax.faces.application.FacesMessage;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class StaffSearchResultLazyModel extends SearchResultLazyModel {

	private static final Integer GRAPH_MAX_STAFF_INSTANCES = 2;

	@Override
	protected Collection<StaffOutVO> getLazyResultWCount(PSFVO psf) {
		if (!noCriteriaSet) {
			try {
				return WebUtil.getServiceLocator().getSearchService().searchStaff(WebUtil.getAuthentication(), criteriaIn, criterionsIn, GRAPH_MAX_STAFF_INSTANCES, psf);
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
		return new ArrayList<StaffOutVO>();
	}

	@Override
	protected StaffOutVO getRowElement(Long id) {
		return WebUtil.getStaff(id, null, null, null);
	}
}
