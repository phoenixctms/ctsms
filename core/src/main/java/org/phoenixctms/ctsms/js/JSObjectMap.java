package org.phoenixctms.ctsms.js;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import jdk.nashorn.api.scripting.AbstractJSObject;

//http://stackoverflow.com/questions/7519399/how-to-convert-java-map-to-a-basic-javascript-object
@SuppressWarnings("restriction")
public class JSObjectMap extends AbstractJSObject implements Map<String, Object> {

	public final Map<String, Object> map;

	public JSObjectMap(Map<String, Object> map) {
		this.map = map;
	}

	@Override
	public Object call(Object thiz, Object... args) {
		return super.call(thiz, args);
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	@Override
	public Set<Entry<String, Object>> entrySet() {
		return map.entrySet();
	}

	@Override
	public Object eval(String s) {
		return super.eval(s);
	}

	@Override
	public Object get(Object key) {
		return map.get(key);
	}

	@Override
	public String getClassName() {
		return super.getClassName();
	}

	@Override
	public Object getMember(String name) {
		return map.get(name);
	}

	@Override
	public Object getSlot(int index) {
		return map.get(Integer.toString(index));
	}

	@Override
	public boolean hasMember(String name) {
		return map.containsKey(name);
	}

	@Override
	public boolean hasSlot(int slot) {
		return map.containsKey(Integer.toString(slot));
	}

	@Override
	public boolean isArray() {
		return false;
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public boolean isFunction() {
		return false;
	}

	@Override
	public boolean isInstance(Object instance) {
		return super.isInstance(instance);
	}

	@Override
	public boolean isInstanceOf(Object clazz) {
		return super.isInstanceOf(clazz);
	}

	@Override
	public boolean isStrictFunction() {
		return false;
	}

	@Override
	public Set<String> keySet() {
		return map.keySet();
	}

	@Override
	public Object newObject(Object... args) {
		return super.newObject(args);
	}

	@Override
	public Object put(String key, Object value) {
		super.setMember((String) key, value);
		return map.put(key, value);
	}

	@Override
	public void putAll(Map m) {
		map.putAll(m);
	}

	@Override
	public Object remove(Object key) {
		super.removeMember((String) key);
		return map.remove(key);
	}

	@Override
	public void removeMember(String name) {
		map.remove(name);
		super.removeMember(name);
	}

	@Override
	public void setMember(String name, Object value) {
		map.put(name, value);
		super.setMember(name, value);
	}

	@Override
	public void setSlot(int index, Object value) {
		map.put(Integer.toString(index), value);
		super.setSlot(index, value);
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public double toNumber() {
		return super.toNumber();
	}

	@Override
	public String toString() {
		return map.toString();
	}

	@Override
	public Collection<Object> values() {
		return map.values();
	}
}
