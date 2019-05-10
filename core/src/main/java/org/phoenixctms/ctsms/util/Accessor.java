package org.phoenixctms.ctsms.util;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Accessor {

	public enum AccessorType {
		REFERENCE, COLLECTION, MAP
	}

	private Method method;
	private AccessorType type;
	private HashMap<Object, ArrayList> sortedCollectionsCache;
	private Comparator compare;

	public Accessor(Method method, AccessorType type) {
		this.method = method;
		this.type = type;
		this.sortedCollectionsCache = null;
	}

	public Accessor(Method method, AccessorType type, Comparator compare) {
		this(method, type);
		this.compare = compare;
	}

	public Class getCollectionType() {
		try {
			return (Class) ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments()[0];
		} catch (Exception e) {
			return Object.class;
		}
	}

	public List getCollectionValue(Object graph) throws Exception {
		if (AccessorType.COLLECTION.equals(type)) {
			if (sortedCollectionsCache == null) {
				sortedCollectionsCache = new HashMap<Object, ArrayList>();
			}
			if (sortedCollectionsCache.containsKey(graph)) {
				return sortedCollectionsCache.get(graph);
			} else {
				method.setAccessible(true);
				ArrayList value = new ArrayList((Collection) method.invoke(graph));
				if (compare != null) {
					Collections.sort(value, compare);
				}
				sortedCollectionsCache.put(graph, value);
				return value;
			}
		}
		throw new IllegalArgumentException();
	}

	public String getFieldName(MethodTransfilter transformer) { //Pattern getterMethodNameRegexp, boolean lowerCaseFieldNames) {
		return transformer.transform(method.getName());
	}

	public Map getMapValue(Object graph) throws Exception {
		if (AccessorType.MAP.equals(type)) {
			method.setAccessible(true);
			return (Map) method.invoke(graph);
		}
		throw new IllegalArgumentException();
	}

	public Class getMapValueType() {
		try {
			return (Class) ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments()[1];
		} catch (Exception e) {
			return Object.class;
		}
	}

	public Object getReferenceValue(Object graph) throws Exception {
		if (AccessorType.REFERENCE.equals(type)) {
			method.setAccessible(true);
			return method.invoke(graph);
		}
		throw new IllegalArgumentException();
	}

	//	public Method getMethod1() {
	//		return method;
	//	}
	public Class getReturnType() {
		return method.getReturnType();
	}

	public AccessorType getType() {
		return type;
	}

	public void setType(AccessorType type) {
		this.type = type;
	}
}
