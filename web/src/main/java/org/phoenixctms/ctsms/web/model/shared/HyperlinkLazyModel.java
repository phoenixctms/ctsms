package org.phoenixctms.ctsms.web.model.shared;

import java.util.ArrayList;
import java.util.Collection;

import org.phoenixctms.ctsms.enumeration.HyperlinkModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.HyperlinkOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class HyperlinkLazyModel extends LazyDataModelBase<HyperlinkOutVO> {

	private Long entityId;
	private HyperlinkModule module;

	public Long getEntityId() {
		return entityId;
	}

	@Override
	protected Collection<HyperlinkOutVO> getLazyResult(PSFVO psf) {
		if (module != null && entityId != null) {
			try {
				return WebUtil.getServiceLocator().getHyperlinkService().getHyperlinks(WebUtil.getAuthentication(), module, entityId, null, psf);
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return new ArrayList<HyperlinkOutVO>();
	}

	public HyperlinkModule getModule() {
		return module;
	}

	@Override
	protected HyperlinkOutVO getRowElement(Long id) {
		try {
			return WebUtil.getServiceLocator().getHyperlinkService().getHyperlink(WebUtil.getAuthentication(), id);
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
		return null;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public void setModule(HyperlinkModule module) {
		this.module = module;
	}
}
