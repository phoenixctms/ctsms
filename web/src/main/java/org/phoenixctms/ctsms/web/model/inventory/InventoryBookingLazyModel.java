package org.phoenixctms.ctsms.web.model.inventory;

import java.util.ArrayList;
import java.util.Collection;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.InventoryBookingOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class InventoryBookingLazyModel extends LazyDataModelBase {

	private Long inventoryId;

	public Long getInventoryId() {
		return inventoryId;
	}

	@Override
	protected Collection<InventoryBookingOutVO> getLazyResult(PSFVO psf) {
		if (inventoryId != null) {
			try {
				return WebUtil.getServiceLocator().getInventoryService().getInventoryBookingList(WebUtil.getAuthentication(), inventoryId, psf);
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return new ArrayList<InventoryBookingOutVO>();
	}

	@Override
	protected InventoryBookingOutVO getRowElement(Long id) {
		return WebUtil.getInventoryBooking(id);
	}

	public void setInventoryId(Long inventoryId) {
		this.inventoryId = inventoryId;
	}
}
