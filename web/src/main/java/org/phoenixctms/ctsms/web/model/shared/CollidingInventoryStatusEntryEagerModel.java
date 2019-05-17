package org.phoenixctms.ctsms.web.model.shared;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.InventoryBookingOutVO;
import org.phoenixctms.ctsms.vo.InventoryStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.model.EagerDataModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class CollidingInventoryStatusEntryEagerModel extends EagerDataModelBase {

	public static CollidingInventoryStatusEntryEagerModel getCachedCollidingInventoryStatusEntryModel(InventoryBookingOutVO booking, boolean load,
			HashMap<Long, CollidingInventoryStatusEntryEagerModel> collidingInventoryStatusEntryModelCache) {
		CollidingInventoryStatusEntryEagerModel model;
		if (booking != null && collidingInventoryStatusEntryModelCache != null) {
			long inventoryBookingId = booking.getId();
			if (collidingInventoryStatusEntryModelCache.containsKey(inventoryBookingId)) {
				model = collidingInventoryStatusEntryModelCache.get(inventoryBookingId);
			} else {
				if (load) {
					model = new CollidingInventoryStatusEntryEagerModel(inventoryBookingId);
					collidingInventoryStatusEntryModelCache.put(inventoryBookingId, model);
				} else {
					model = new CollidingInventoryStatusEntryEagerModel();
				}
			}
		} else {
			model = new CollidingInventoryStatusEntryEagerModel();
		}
		return model;
	}

	private Long inventoryBookingId;

	public CollidingInventoryStatusEntryEagerModel() {
		super();
		resetRows();
	}

	public CollidingInventoryStatusEntryEagerModel(Long inventoryBookingId) {
		resetRows();
		this.inventoryBookingId = inventoryBookingId;
	}

	@Override
	protected Collection<InventoryStatusEntryOutVO> getEagerResult(PSFVO psf) {
		if (inventoryBookingId != null) {
			try {
				return WebUtil.getServiceLocator().getInventoryService().getCollidingInventoryStatusEntries(WebUtil.getAuthentication(), inventoryBookingId);
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return new ArrayList<InventoryStatusEntryOutVO>();
	}

	public Long getInventoryBookingId() {
		return inventoryBookingId;
	}

	@Override
	protected InventoryStatusEntryOutVO getRowElement(Long id) {
		return WebUtil.getInventoryStatusEntry(id);
	}

	public void setInventoryBookingId(Long inventoryBookingId) {
		this.inventoryBookingId = inventoryBookingId;
	}
}
