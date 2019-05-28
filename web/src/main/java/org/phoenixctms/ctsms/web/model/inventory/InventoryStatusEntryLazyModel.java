package org.phoenixctms.ctsms.web.model.inventory;

import java.util.ArrayList;
import java.util.Collection;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.InventoryStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class InventoryStatusEntryLazyModel extends LazyDataModelBase<InventoryStatusEntryOutVO> {

	private Long inventoryId;

	public Long getInventoryId() {
		return inventoryId;
	}

	@Override
	protected Collection<InventoryStatusEntryOutVO> getLazyResult(PSFVO psf) {
		if (inventoryId != null) {
			try {
				return WebUtil.getServiceLocator().getInventoryService().getInventoryStatusEntryList(WebUtil.getAuthentication(), inventoryId, psf);
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return new ArrayList<InventoryStatusEntryOutVO>();
	}

	@Override
	protected InventoryStatusEntryOutVO getRowElement(Long id) {
		return WebUtil.getInventoryStatusEntry(id);
	}

	public void setInventoryId(Long inventoryId) {
		this.inventoryId = inventoryId;
	}
}
