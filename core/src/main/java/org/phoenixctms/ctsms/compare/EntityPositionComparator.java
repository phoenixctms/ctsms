package org.phoenixctms.ctsms.compare;

import org.phoenixctms.ctsms.util.CommonUtil;

public class EntityPositionComparator<T> extends PositionComparatorBase<T> {

	public EntityPositionComparator(boolean posDesc) {
		super(posDesc);
	}

	@Override
	protected Long getPosition(T item) throws Exception {
		return CommonUtil.getEntityPosition(item);
	}
}
