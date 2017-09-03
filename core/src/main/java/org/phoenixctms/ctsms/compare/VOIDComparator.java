package org.phoenixctms.ctsms.compare;

import org.phoenixctms.ctsms.util.CommonUtil;


public class VOIDComparator<T> extends IDComparatorBase<T> {

	public VOIDComparator(boolean idDesc) {
		super(idDesc);
	}

	@Override
	protected Long getId(T item) throws Exception {
		return CommonUtil.getVOId(item);
	}
}
