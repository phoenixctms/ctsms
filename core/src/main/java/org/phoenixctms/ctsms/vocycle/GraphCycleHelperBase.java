package org.phoenixctms.ctsms.vocycle;

import java.util.HashMap;

public abstract class GraphCycleHelperBase<A, AVO, B, BVO> extends CycleHelperBase<A, AVO> {

	protected abstract Long getAId(A source);

	protected abstract Long getBId(B source);

	protected final BVO getBVO(B b, HashMap<Class, HashMap<Long, Object>> voMap) {
		if (b != null) {
			if (!voMapContainsKey(getBVOClass(), getBId(b), voMap)) {
				toBVO(b, newBVO(), voMap);
			}
			return (BVO) voMapGet(getBVOClass(), getBId(b), voMap);
		} else {
			return null;
		}
	}

	protected abstract Class getBVOClass();

	protected abstract int getMaxAInstances();

	protected abstract boolean limitAInstances();

	protected abstract BVO newBVO();

	protected abstract void setBVO(A source, AVO target, HashMap<Class, HashMap<Long, Object>> voMap);

	protected abstract void toBVO(B source, BVO target, HashMap<Class, HashMap<Long, Object>> voMap);

	@Override
	public void toVOHelper(A source, AVO target, HashMap<Class, HashMap<Long, Object>> voMap) {
		if (!voMapContainsKey(getAId(source), voMap)) {
			if (!limitAInstances() || voMapSize(voMap) < getMaxAInstances()) {
				voMapPut(getAId(source), target, voMap);
			} else {
				voMapPut(getAId(source), null, voMap);
				return;
			}
		}
		setBVO(source, target, voMap);
		toVORemainingFields(source, target, voMap);
	}
}
