package org.phoenixctms.ctsms.compare;

import org.phoenixctms.ctsms.util.CommonUtil;


public class EntityIDComparator<T> extends IDComparatorBase<T> {

	public EntityIDComparator(boolean idDesc) {
		super(idDesc);
	}

	@Override
	protected Long getId(T item) throws Exception {
		return CommonUtil.getEntityId(item);
	}

}
