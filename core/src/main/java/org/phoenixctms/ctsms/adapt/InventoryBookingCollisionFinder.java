package org.phoenixctms.ctsms.adapt;

import java.util.Collection;
import java.util.Date;

import org.hibernate.LockMode;

import org.phoenixctms.ctsms.domain.Inventory;
import org.phoenixctms.ctsms.domain.InventoryBooking;
import org.phoenixctms.ctsms.domain.InventoryBookingDao;
import org.phoenixctms.ctsms.domain.InventoryDao;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.InventoryBookingInVO;

public class InventoryBookingCollisionFinder extends OverlapFinder<InventoryBookingInVO, InventoryBooking, Inventory> {

	private InventoryDao inventoryDao;
	private InventoryBookingDao inventoryBookingDao;

	public InventoryBookingCollisionFinder(InventoryDao inventoryDao, InventoryBookingDao inventoryBookingDao) {
		this.inventoryBookingDao = inventoryBookingDao;
		this.inventoryDao = inventoryDao;
	}

	@Override
	protected Inventory aquireWriteLock(InventoryBookingInVO in, boolean lock)
			throws ServiceException {
		if (in.getInventoryId() != null) {
			if (lock) {
				return CheckIDUtil.checkInventoryId(in.getInventoryId(), inventoryDao, LockMode.PESSIMISTIC_WRITE);
			} else {
				return CheckIDUtil.checkInventoryId(in.getInventoryId(), inventoryDao);
			}
		}
		return null;
	}

	@Override
	protected boolean equals(InventoryBookingInVO in, InventoryBooking existing) {
		return in.getId().equals(existing.getId());
	}

	@Override
	protected Collection<InventoryBooking> getCollidingItems(
			InventoryBookingInVO in, Inventory root) {
		return inventoryBookingDao.findByInventoryCalendarInterval(in.getInventoryId(), null, CommonUtil.dateToTimestamp(in.getStart()), CommonUtil.dateToTimestamp(in.getStop()));
	}

	@Override
	protected Date getInStart(InventoryBookingInVO in) {
		return in.getStart();
	}

	@Override
	protected Date getInStop(InventoryBookingInVO in) {
		return in.getStop();
	}

	@Override
	protected long getMaxOverlappingEvents(Inventory root) throws ServiceException {
		if (root != null) {
			return root.getMaxOverlappingBookings();
		} else {
			return 1l;
		}
	}

	@Override
	protected Date getStart(InventoryBooking collidingItem) {
		return collidingItem.getStart();
	}

	@Override
	protected Date getStop(InventoryBooking collidingItem) {
		return collidingItem.getStop();
	}

	@Override
	protected boolean isNew(InventoryBookingInVO in) {
		return in.getId() == null;
	}
}
