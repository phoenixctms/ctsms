package org.phoenixctms.ctsms.js;


import java.util.Collection;
import java.util.Map;
import java.util.Set;

import sun.org.mozilla.javascript.internal.Scriptable;

//http://stackoverflow.com/questions/7519399/how-to-convert-java-map-to-a-basic-javascript-object
public class JsMap implements Scriptable, Map {

	public final Map map;

	public JsMap(Map map) {
		this.map = map;
	}

	public void clear() {
		map.clear();
	}

	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	@Override
	public void delete(int index) {
		map.remove(index);
	}

	@Override
	public void delete(String name) {
		map.remove(name);
	}

	public Set entrySet() {
		return map.entrySet();
	}

	public boolean equals(Object o) {
		return map.equals(o);
	}

	@Override
	public Object get(int index, Scriptable start) {
		return map.get(index);
	}

	public Object get(Object key) {
		return map.get(key);
	}

	@Override
	public Object get(String name, Scriptable start) {
		return map.get(name);
	}

	@Override
	public String getClassName() {
		return map.getClass().getName();
	}

	@Override
	public Object getDefaultValue(Class hint) {
		return toString();
	}

	@Override
	public Object[] getIds() {
		Object[] res = new Object[map.size()];
		int i = 0;
		for (Object k : map.keySet()) {
			res[i] = k;
			i++;
		}
		return res;
	}

	@Override
	public Scriptable getParentScope() {
		return null;
	}

	@Override
	public Scriptable getPrototype() {
		return null;
	}

	@Override
	public boolean has(int index, Scriptable start) {
		return map.containsKey(index);
	}

	@Override
	public boolean has(String name, Scriptable start) {
		return map.containsKey(name);
	}

	public int hashCode() {
		return map.hashCode();
	}

	@Override
	public boolean hasInstance(Scriptable instance) {
		return false;
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public Set keySet() {
		return map.keySet();
	}

	@Override
	public void put(int index, Scriptable start, Object value) {
		map.put(index, value);
	}

	public Object put(Object key, Object value) {
		return map.put(key, value);
	}

	@Override
	public void put(String name, Scriptable start, Object value) {
		map.put(name, value);
	}

	public void putAll(Map m) {
		map.putAll(m);
	}

	public Object remove(Object key) {
		return map.remove(key);
	}

	@Override
	public void setParentScope(Scriptable parent) {
	}

	@Override
	public void setPrototype(Scriptable prototype) {
	}

	public int size() {
		return map.size();
	}

	public Collection values() {
		return map.values();
	}
}
