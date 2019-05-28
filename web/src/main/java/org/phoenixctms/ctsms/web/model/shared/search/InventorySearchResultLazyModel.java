package org.phoenixctms.ctsms.web.model.shared.search;

import java.util.ArrayList;
import java.util.Collection;

import javax.faces.application.FacesMessage;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.InventoryOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class InventorySearchResultLazyModel extends SearchResultLazyModel<InventoryOutVO> {

	private static final Integer GRAPH_MAX_INVENTORY_INSTANCES = 2;

	@Override
	protected Collection<InventoryOutVO> getLazyResultWCount(PSFVO psf) {
		if (!noCriteriaSet) {
			try {
				return WebUtil.getServiceLocator().getSearchService().searchInventory(WebUtil.getAuthentication(), criteriaIn, criterionsIn, GRAPH_MAX_INVENTORY_INSTANCES, psf);
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
		return new ArrayList<InventoryOutVO>();
	}

	@Override
	protected InventoryOutVO getRowElement(Long id) {
		return WebUtil.getInventory(id, null, null, null);
	}
}
