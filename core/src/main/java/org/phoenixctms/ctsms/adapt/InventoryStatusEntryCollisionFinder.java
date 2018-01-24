package org.phoenixctms.ctsms.adapt;

import java.util.Collection;

import org.hibernate.LockMode;
import org.phoenixctms.ctsms.domain.Inventory;
import org.phoenixctms.ctsms.domain.InventoryDao;
import org.phoenixctms.ctsms.domain.InventoryStatusEntry;
import org.phoenixctms.ctsms.domain.InventoryStatusEntryDao;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.InventoryStatusEntryInVO;

public class InventoryStatusEntryCollisionFinder extends CollisionFinder<InventoryStatusEntryInVO, InventoryStatusEntry, Inventory> {

	private InventoryDao inventoryDao;
	private InventoryStatusEntryDao inventoryStatusEntryDao;

	public InventoryStatusEntryCollisionFinder(InventoryDao inventoryDao, InventoryStatusEntryDao inventoryStatusEntryDao) {
		this.inventoryStatusEntryDao = inventoryStatusEntryDao;
		this.inventoryDao = inventoryDao;
	}

	@Override
	protected Inventory aquireWriteLock(InventoryStatusEntryInVO in)
			throws ServiceException {
		return CheckIDUtil.checkInventoryId(in.getInventoryId(), inventoryDao, LockMode.PESSIMISTIC_WRITE);
	}

	@Override
	protected boolean equals(InventoryStatusEntryInVO in, InventoryStatusEntry existing) {
		return in.getId().equals(existing.getId());
	}

	@Override
	protected Collection<InventoryStatusEntry> getCollidingItems(
			InventoryStatusEntryInVO in, Inventory root) {
		return inventoryStatusEntryDao.findByInventoryInterval(in.getInventoryId(), CommonUtil.dateToTimestamp(in.getStart()), CommonUtil.dateToTimestamp(in.getStop()), false,
				null);
	}

	@Override
	protected boolean isNew(InventoryStatusEntryInVO in) {
		return in.getId() == null;
	}
}
