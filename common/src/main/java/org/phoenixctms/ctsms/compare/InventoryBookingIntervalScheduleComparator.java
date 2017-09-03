package org.phoenixctms.ctsms.compare;

import java.util.Date;

import org.phoenixctms.ctsms.vo.InventoryBookingOutVO;

public class InventoryBookingIntervalScheduleComparator extends IntervalScheduleComparatorBase<InventoryBookingOutVO> {

	public InventoryBookingIntervalScheduleComparator(boolean intDesc) {
		super(intDesc);
	}

	@Override
	protected Date getStart(InventoryBookingOutVO item) {
		return item.getStart();
	}

	@Override
	protected Date getStop(InventoryBookingOutVO item) {
		return item.getStop();
	}
}
