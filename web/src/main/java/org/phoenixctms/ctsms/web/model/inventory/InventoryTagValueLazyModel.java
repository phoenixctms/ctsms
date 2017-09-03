package org.phoenixctms.ctsms.web.model.inventory;

import java.util.ArrayList;
import java.util.Collection;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.InventoryTagValueOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class InventoryTagValueLazyModel extends LazyDataModelBase {

	private Long inventoryId;

	public Long getInventoryId() {
		return inventoryId;
	}

	@Override
	protected Collection<InventoryTagValueOutVO> getLazyResult(PSFVO psf) {
		if (inventoryId != null) {
			try {
				return WebUtil.getServiceLocator().getInventoryService().getInventoryTagValueList(WebUtil.getAuthentication(), inventoryId, psf);
			} catch (ServiceException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			} catch (AuthorisationException e) {
			} catch (IllegalArgumentException e) {
			}
		}
		return new ArrayList<InventoryTagValueOutVO>();
	}

	@Override
	protected InventoryTagValueOutVO getRowElement(Long id) {
		try {
			return WebUtil.getServiceLocator().getInventoryService().getInventoryTagValue(WebUtil.getAuthentication(), id);
		} catch (ServiceException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
		} catch (IllegalArgumentException e) {
		}
		return null;
	}

	public void setInventoryId(Long inventoryId) {
		this.inventoryId = inventoryId;
	}
}
