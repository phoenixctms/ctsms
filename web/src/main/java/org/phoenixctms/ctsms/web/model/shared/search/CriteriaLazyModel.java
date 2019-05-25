package org.phoenixctms.ctsms.web.model.shared.search;

import java.util.ArrayList;
import java.util.Collection;

import org.phoenixctms.ctsms.enumeration.DBModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.CriteriaOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class CriteriaLazyModel extends LazyDataModelBase<CriteriaOutVO> {

	private String category;
	private DBModule module;

	public String getCategory() {
		return category;
	}

	@Override
	protected Collection<CriteriaOutVO> getLazyResult(PSFVO psf) {
		try {
			return WebUtil.getServiceLocator().getSearchService().getCriteriaList(WebUtil.getAuthentication(), module, category, psf);
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
		return new ArrayList<CriteriaOutVO>();
	}

	public DBModule getModule() {
		return module;
	}

	@Override
	protected CriteriaOutVO getRowElement(Long id) {
		try {
			return WebUtil.getServiceLocator().getSearchService().getCriteria(WebUtil.getAuthentication(), id);
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
		return null;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setModule(DBModule module) {
		this.module = module;
	}
}
