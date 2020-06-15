package org.phoenixctms.ctsms.compare;

import java.util.Date;

import org.phoenixctms.ctsms.vo.InventoryBookingOutVO;

public class InventoryBookingIntervalComparator extends IntervalComparatorBase<InventoryBookingOutVO> {

	public InventoryBookingIntervalComparator(boolean desc) {
		super(desc);
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
