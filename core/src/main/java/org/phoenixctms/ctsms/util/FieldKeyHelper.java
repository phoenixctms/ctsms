package org.phoenixctms.ctsms.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FieldKeyHelper {

	private StringBuilder associationPath;
	private ArrayList<Object> indexesKeys;
	private Object graph;

	public FieldKeyHelper() {
		this(null, null);
	}

	public FieldKeyHelper(Object rootGraph) {
		this(null, rootGraph);
	}

	public FieldKeyHelper(StringBuilder associationPath) {
		this(associationPath, null);
	}

	public FieldKeyHelper(StringBuilder associationPath, Object rootGraph) {
		this.associationPath = associationPath == null ? new StringBuilder() : new StringBuilder(associationPath);
		indexesKeys = null;
		graph = rootGraph;
	}

	public FieldKeyHelper append(Accessor getter, int index, MethodTransfilter transfilter, String associationPathSeparator) throws Exception {
		FieldKeyHelper newHelper = new FieldKeyHelper(associationPath);
		if (graph != null) {
			newHelper.graph = getter.getCollectionValue(graph).get(index);
		}
		newHelper.getIndexKeyChain().addAll(getIndexKeyChain());
		newHelper.getIndexKeyChain().add(index);
		CommonUtil.appendString(newHelper.associationPath, getter.getFieldName(transfilter), associationPathSeparator);
		CommonUtil.appendString(newHelper.associationPath, Integer.toString(index), associationPathSeparator);
		return newHelper;
	}

	public void append(Accessor getter, MethodTransfilter transfilter, String associationPathSeparator) throws Exception {
		CommonUtil.appendString(associationPath, getter.getFieldName(transfilter), associationPathSeparator);
		if (graph != null) {
			graph = getter.getReferenceValue(graph);
		}
	}

	public FieldKeyHelper append(Accessor getter, Object key, MethodTransfilter transfilter, String associationPathSeparator) throws Exception {
		FieldKeyHelper newHelper = new FieldKeyHelper(associationPath);
		if (graph != null) {
			newHelper.graph = getter.getMapValue(graph).get(key);
		}
		newHelper.getIndexKeyChain().addAll(getIndexKeyChain());
		newHelper.getIndexKeyChain().add(key);
		CommonUtil.appendString(newHelper.associationPath, getter.getFieldName(transfilter), associationPathSeparator);
		CommonUtil.appendString(newHelper.associationPath, key.toString(), associationPathSeparator);
		return newHelper;
	}

	public int getCollectionSize(Accessor getter) throws Exception {
		List collection = getter.getCollectionValue(graph);
		if (collection != null) {
			return collection.size();
		} else {
			return 0;
		}
	}

	public ArrayList<Object> getIndexKeyChain() {
		if (indexesKeys == null) {
			indexesKeys = new ArrayList<Object>();
		}
		return indexesKeys;
	}

	public Set getKeys(Accessor getter) throws Exception {
		Map map = getter.getMapValue(graph);
		if (map != null) {
			return map.keySet();
		} else {
			return new HashSet();
		}
	}

	@Override
	public String toString() {
		return associationPath.toString();
	}
}
