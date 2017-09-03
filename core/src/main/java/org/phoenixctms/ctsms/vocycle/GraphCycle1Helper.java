package org.phoenixctms.ctsms.vocycle;

import java.util.HashMap;

public abstract class GraphCycle1Helper<A, AVO, B, BVO> extends GraphCycleHelperBase<A, AVO, B, BVO> {

	protected abstract B getBOfA(A source);

	@Override
	protected void setBVO(A source, AVO target, HashMap<Class, HashMap<Long, Object>> voMap) {
		setBVOOfAVO(target, getBVO(getBOfA(source), voMap));
	}

	protected abstract void setBVOOfAVO(AVO target, BVO bVO);
}
