package org.phoenixctms.ctsms;

import java.security.Principal;
import java.util.HashMap;

public abstract class VOCacheContext implements Principal {

	private static final long serialVersionUID = 1L;
	private HashMap<Class, HashMap<Long, HashMap<Class, Object>>> entityVoMap;

	protected VOCacheContext() {
		super();
	}

	private HashMap<Class, HashMap<Long, HashMap<Class, Object>>> getEntityVoMap() {
		if (entityVoMap == null) {
			entityVoMap = new HashMap<Class, HashMap<Long, HashMap<Class, Object>>>();
		}
		return entityVoMap;
	}

	protected void reset() {
		voMapClear();
	}

	public void voMapClear() {
		if (entityVoMap != null) {
			entityVoMap.clear();
		}
	}

	public boolean voMapContainsKey(Class entityClass, Class voClass, Long id) {
		if (getEntityVoMap().containsKey(entityClass)) {
			HashMap<Long, HashMap<Class, Object>> voMap = getEntityVoMap().get(entityClass);
			if (voMap.containsKey(id)) {
				return voMap.get(id).containsKey(voClass);
			}
		}
		return false;
	}

	public void voMapEvict(Class entityClass, Long id) {
		if (getEntityVoMap().containsKey(entityClass)) {
			getEntityVoMap().get(entityClass).remove(id);
		}
	}

	public Object voMapGet(Class entityClass, Class voClass, Long id) {
		if (getEntityVoMap().containsKey(entityClass)) {
			HashMap<Long, HashMap<Class, Object>> voMap = getEntityVoMap().get(entityClass);
			if (voMap.containsKey(id)) {
				return voMap.get(id).get(voClass);
			}
		}
		return null;
	}

	public Object voMapPut(Class entityClass, Class voClass, Long id, Object vo) {
		HashMap<Long, HashMap<Class, Object>> voMap;
		if (getEntityVoMap().containsKey(entityClass)) {
			voMap = getEntityVoMap().get(entityClass);
		} else {
			voMap = new HashMap<Long, HashMap<Class, Object>>();
			getEntityVoMap().put(entityClass, voMap);
		}
		HashMap<Class, Object> map;
		if (voMap.containsKey(id)) {
			map = voMap.get(id);
		} else {
			map = new HashMap<Class, Object>();
			voMap.put(id, map);
		}
		return map.put(voClass, vo);
	}

}
