package org.phoenixctms.ctsms.web.model.inventory;

import java.util.ArrayList;
import java.util.Collection;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.MaintenanceScheduleItemOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class MaintenanceScheduleItemLazyModel extends LazyDataModelBase {

	private Long inventoryId;

	public Long getInventoryId() {
		return inventoryId;
	}

	@Override
	protected Collection<MaintenanceScheduleItemOutVO> getLazyResult(PSFVO psf) {
		if (inventoryId != null) {
			try {
				return WebUtil.getServiceLocator().getInventoryService().getMaintenanceScheduleItemList(WebUtil.getAuthentication(), inventoryId, null, psf);
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return new ArrayList<MaintenanceScheduleItemOutVO>();
	}

	@Override
	protected MaintenanceScheduleItemOutVO getRowElement(Long id) {
		return WebUtil.getMaintenanceScheduleItem(id);
	}

	public void setInventoryId(Long inventoryId) {
		this.inventoryId = inventoryId;
	}
}
