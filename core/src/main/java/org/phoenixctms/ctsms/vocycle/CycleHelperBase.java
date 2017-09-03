package org.phoenixctms.ctsms.vocycle;

import java.util.HashMap;

public abstract class CycleHelperBase<A, AVO> {

	protected static boolean voMapContainsKey(Class clazz, Long id, HashMap<Class, HashMap<Long, Object>> voMap) {
		if (voMap.containsKey(clazz)) {
			return voMap.get(clazz).containsKey(id);
		}
		return false;
	}

	protected static Object voMapGet(Class clazz, Long id, HashMap<Class, HashMap<Long, Object>> voMap) {
		if (voMap.containsKey(clazz)) {
			DeferredVO deferredVO = (DeferredVO) voMap.get(clazz).get(id);
			return deferredVO == null ? null : deferredVO.getVo();
		}
		return null;
	}

	protected static Object voMapPut(Class clazz, Long id, Object vo, boolean deferred, HashMap<Class, HashMap<Long, Object>> voMap) {
		HashMap<Long, Object> map;
		if (voMap.containsKey(clazz)) {
			map = voMap.get(clazz);
		} else {
			map = new HashMap<Long, Object>();
			voMap.put(clazz, map);
		}
		return map.put(id, new DeferredVO(deferred, vo));
	}

	protected static int voMapSize(Class clazz, HashMap<Class, HashMap<Long, Object>> voMap) {
		if (voMap.containsKey(clazz)) {
			return voMap.get(clazz).size();
		}
		return 0;
	}

	protected abstract Class getAVOClass();

	public abstract void toVOHelper(A source, AVO target, HashMap<Class, HashMap<Long, Object>> voMap);

	protected abstract void toVORemainingFields(A source, AVO target, HashMap<Class, HashMap<Long, Object>> voMap);

	protected final boolean voMapContainsKey(Long id, HashMap<Class, HashMap<Long, Object>> voMap) {
		return voMapContainsKey(getAVOClass(), id, voMap);
	}

	protected final AVO voMapGet(Long id, HashMap<Class, HashMap<Long, Object>> voMap) {
		return (AVO) voMapGet(getAVOClass(), id, voMap);
	}

	protected final AVO voMapPut(Long id, AVO vo, boolean deferred, HashMap<Class, HashMap<Long, Object>> voMap) {
		return (AVO) voMapPut(getAVOClass(), id, vo, deferred, voMap);
	}

	protected final AVO voMapPut(Long id, AVO vo, HashMap<Class, HashMap<Long, Object>> voMap) {
		return (AVO) voMapPut(getAVOClass(), id, vo, false, voMap);
	}

	protected final int voMapSize(HashMap<Class, HashMap<Long, Object>> voMap) {
		return voMapSize(getAVOClass(), voMap);
	}
}
