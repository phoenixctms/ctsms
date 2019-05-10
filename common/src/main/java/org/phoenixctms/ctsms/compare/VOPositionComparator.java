package org.phoenixctms.ctsms.compare;

import org.phoenixctms.ctsms.util.CommonUtil;

public class VOPositionComparator<T> extends PositionComparatorBase<T> {

	public VOPositionComparator(boolean posDesc) {
		super(posDesc);
	}

	@Override
	protected Long getPosition(T item) throws Exception {
		return CommonUtil.getVOPosition(item);
	}
}
