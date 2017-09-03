package org.phoenixctms.ctsms.vocycle;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public abstract class GraphCycleNHelper<A, AVO, B, BVO> extends GraphCycleHelperBase<A, AVO, B, BVO> {

	protected abstract A getAOfB(B source);

	protected abstract Collection<B> getBsOfA(A source);

	protected abstract Collection<BVO> getBVOsOfAVO(AVO target);

	protected abstract void setAVOOfBVO(BVO bVO, AVO aVO);

	@Override
	protected void setBVO(A source, AVO target, HashMap<Class, HashMap<Long, Object>> voMap) {
		Collection<B> bs = getBsOfA(source);
		if (bs != null && bs.size() > 0) {
			Iterator<B> it = bs.iterator();
			while (it.hasNext()) {
				BVO bVO = getBVO(it.next(), voMap);
				if (bVO != null) {
					getBVOsOfAVO(target).add(bVO);
				} else {
					break;
				}
			}
		}
	}
}
