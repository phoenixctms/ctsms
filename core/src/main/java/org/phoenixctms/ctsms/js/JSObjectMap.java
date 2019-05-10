package org.phoenixctms.ctsms.js;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import jdk.nashorn.api.scripting.AbstractJSObject;

//http://stackoverflow.com/questions/7519399/how-to-convert-java-map-to-a-basic-javascript-object
public class JSObjectMap extends AbstractJSObject implements Map {

	public final Map map;

	public JSObjectMap(Map map) {
		this.map = map;
	}

	@Override
	public Object call(Object thiz, Object... args) {
		// System.out.println("call");
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
	public Set entrySet() {
		return map.entrySet();
	}

	@Override
	public Object eval(String s) {
		//System.out.println("eval");
		return super.eval(s);
	}

	@Override
	public Object get(Object key) {
		return map.get(key);
	}

	@Override
	public String getClassName() {
		//System.out.println("getClassName");
		return super.getClassName();
	}

	@Override
	public Object getMember(String name) {
		return map.get(name);
		//        System.out.println("getMember " + name);
		//        return name.equals("length") ? arrayValues.size() : arrayValues.get(Integer.valueOf(name));
	}

	@Override
	public Object getSlot(int index) {
		return map.get(Integer.toString(index));
		//System.out.println("getSlot");
		//return arrayValues.get(index);
	}

	@Override
	public boolean hasMember(String name) {
		return map.containsKey(name);
		//System.out.println("hasMember");
		//return super.hasMember(name);
	}

	@Override
	public boolean hasSlot(int slot) {
		return map.containsKey(Integer.toString(slot));
		//        System.out.println("hasSlot");
		//        return super.hasSlot(slot);
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
		//System.out.println("isInstance");
		return super.isInstance(instance);
	}

	@Override
	public boolean isInstanceOf(Object clazz) {
		//System.out.println("isINstanceOf");
		return super.isInstanceOf(clazz);
	}

	@Override
	public boolean isStrictFunction() {
		return false;
	}

	@Override
	public Set<String> keySet() {
		return map.keySet();
		//System.out.println("keySet");
		//return arrayValues.keySet().stream().map(k -> "" + k).collect(Collectors.toSet());
	}

	@Override
	public Object newObject(Object... args) {
		//System.out.println("new Object");
		return super.newObject(args);
	}

	@Override
	public Object put(Object key, Object value) {
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
		//System.out.println("removeMember");
		map.remove(name);
		super.removeMember(name);
	}

	@Override
	public void setMember(String name, Object value) {
		//System.out.println("setMember");
		map.put(name, value);
		super.setMember(name, value);
	}

	@Override
	public void setSlot(int index, Object value) {
		//System.out.println("setSlot");
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
		//System.out.println("values");
		//return arrayValues.values();
	}
}
