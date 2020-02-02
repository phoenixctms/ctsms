package org.phoenixctms.ctsms.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import org.phoenixctms.ctsms.util.Accessor.AccessorType;

public abstract class GraphEnumerator {

	protected static void appendProperties(Class clazz, ArrayList properties, Class graph, ArrayList<Accessor> getterChain, Object passThrough, int depth,
			MethodTransfilter transfilter,
			Comparator compareCollectionValues,
			boolean omitFields,
			HashMap<Class, HashSet<String>> excludedFieldMap,
			boolean enumerateReferences,
			boolean enumerateCollections,
			boolean enumerateMaps,
			String associationPathSeparator) throws Exception {
		if (graph != null && properties != null) {
			Method[] methods = graph.getMethods();
			if (methods != null) {
				ArrayList<Class> deferredReturnTypes = new ArrayList<Class>();
				ArrayList<ArrayList<Accessor>> deferredGetterChains = new ArrayList<ArrayList<Accessor>>();
				for (int i = 0; i < methods.length; i++) {
					if (transfilter.include(methods[i])
							&& !transfilter.exclude(methods[i])) {
						Class[] parameterTypes = methods[i].getParameterTypes();
						if (parameterTypes != null && parameterTypes.length == 0) {
							GraphEnumerator property = getInstance(clazz, new Accessor(methods[i], AccessorType.REFERENCE), getterChain, transfilter, associationPathSeparator);
							if (!property.isFieldExcluded(graph, property.fieldName, excludedFieldMap) && (!omitFields || !property.isFieldOmitted(graph, property.fieldName))) {
								if (!property.isTerminalType(passThrough)) {
									if (Collection.class.isAssignableFrom(property.returnType)) {
										if (enumerateCollections && depth > 1) {
											Class collectionType = property.lastGetter.getCollectionType();
											property.lastGetter.setType(AccessorType.COLLECTION);
											property.returnType = collectionType;
											if (property.isTerminalType(passThrough)) {
												properties.add(property);
											} else {
												deferredReturnTypes.add(collectionType);
												ArrayList<Accessor> collectionElementGetterChain = (ArrayList<Accessor>) getterChain.clone();
												collectionElementGetterChain.add(new Accessor(methods[i], AccessorType.COLLECTION, compareCollectionValues));
												deferredGetterChains.add(collectionElementGetterChain);
											}
										}
									} else if (Map.class.isAssignableFrom(property.returnType)) {
										if (enumerateMaps && depth > 1) {
											Class mapValueType = property.lastGetter.getMapValueType();
											property.lastGetter.setType(AccessorType.MAP);
											property.returnType = mapValueType;
											if (property.isTerminalType(passThrough)) {
												properties.add(property);
											} else {
												deferredReturnTypes.add(mapValueType);
												ArrayList<Accessor> mapValueElementGetterChain = (ArrayList<Accessor>) getterChain.clone();
												mapValueElementGetterChain.add(new Accessor(methods[i], AccessorType.MAP));
												deferredGetterChains.add(mapValueElementGetterChain);
											}
										}
									} else {
										if (enumerateReferences && depth > 0) {
											deferredReturnTypes.add(property.returnType);
											ArrayList<Accessor> associationGetterChain = (ArrayList<Accessor>) getterChain.clone();
											associationGetterChain.add(new Accessor(methods[i], AccessorType.REFERENCE));
											deferredGetterChains.add(associationGetterChain);
										}
									}
								} else {
									properties.add(property);
								}
							}
						}
					}
				}
				for (int i = 0; i < deferredReturnTypes.size(); i++) {
					appendProperties(clazz, properties, deferredReturnTypes.get(i), deferredGetterChains.get(i), passThrough, depth - 1,
							transfilter,
							compareCollectionValues,
							omitFields,
							excludedFieldMap,
							enumerateReferences,
							enumerateCollections,
							enumerateMaps,
							associationPathSeparator);
				}
			}
		}
	}

	private static Stack getIndexKeyStack(ArrayList indexesKeys) {
		Stack indexesKeysStack = new Stack();
		if (indexesKeys != null) {
			for (int i = indexesKeys.size() - 1; i >= 0; i--) {
				indexesKeysStack.push(indexesKeys.get(i));
			}
		}
		return indexesKeysStack;
	}

	private static GraphEnumerator getInstance(Class clazz, Accessor lastGetter, ArrayList<Accessor> getterChain,
			MethodTransfilter transfilter,
			String associationPathSeparator) throws Exception {
		GraphEnumerator instance = (GraphEnumerator) clazz.newInstance();
		instance.associationPathSeparator = associationPathSeparator;
		instance.transfilter = transfilter;
		instance.lastGetter = lastGetter;
		instance.getterChain = getterChain;
		instance.returnType = lastGetter.getReturnType();
		instance.fieldName = lastGetter.getFieldName(transfilter);
		return instance;
	}

	private static Object getValueOfHelper(Object graph, Accessor getter, Stack indexesKeys) throws Exception {
		switch (getter.getType()) {
			case COLLECTION:
				try {
					return getter.getCollectionValue(graph).get((Integer) indexesKeys.pop());
				} catch (IndexOutOfBoundsException e) {
					return null;
				}
			case MAP:
				return getter.getMapValue(graph).get(indexesKeys.pop());
			case REFERENCE:
			default:
				return getter.getReferenceValue(graph);
		}
	}

	// not thread safe because of Accessors using caching!
	private Accessor lastGetter;
	protected ArrayList<Accessor> getterChain;
	protected Class returnType;
	private String fieldName;
	private String associationPathSeparator;
	protected MethodTransfilter transfilter;

	protected final String getAssociationPath(ArrayList indexesKeys) throws Exception {
		Stack indexesKeysStack = getIndexKeyStack(indexesKeys);
		ArrayList<FieldKeyHelper> helpers = new ArrayList<FieldKeyHelper>();
		helpers.add(new FieldKeyHelper());
		if (getterChain != null) {
			Iterator<Accessor> getterChainIt = getterChain.iterator();
			while (getterChainIt.hasNext()) {
				helpers = updateHelpers(getterChainIt.next(), helpers, indexesKeysStack);
			}
		}
		ArrayList<String> result = new ArrayList<String>();
		Iterator<FieldKeyHelper> helpersIt = updateHelpers(lastGetter, helpers, indexesKeysStack).iterator();
		while (helpersIt.hasNext()) {
			result.add(helpersIt.next().toString());
		}
		return result.get(0);
	}

	protected final ArrayList<String> getAssociationPaths(Object rootGraph) throws Exception {
		ArrayList<FieldKeyHelper> helpers = new ArrayList<FieldKeyHelper>();
		helpers.add(new FieldKeyHelper(rootGraph));
		if (getterChain != null) {
			Iterator<Accessor> getterChainIt = getterChain.iterator();
			while (getterChainIt.hasNext()) {
				helpers = updateHelpers(getterChainIt.next(), helpers, null);
			}
		}
		ArrayList<String> result = new ArrayList<String>();
		Iterator<FieldKeyHelper> helpersIt = updateHelpers(lastGetter, helpers, null).iterator();
		while (helpersIt.hasNext()) {
			result.add(helpersIt.next().toString());
		}
		return result;
	}

	protected final ArrayList<ArrayList<Object>> getIndexKeyChains(Object rootGraph) throws Exception {
		ArrayList<FieldKeyHelper> helpers = new ArrayList<FieldKeyHelper>();
		helpers.add(new FieldKeyHelper(rootGraph));
		if (getterChain != null) {
			Iterator<Accessor> getterChainIt = getterChain.iterator();
			while (getterChainIt.hasNext()) {
				helpers = updateHelpers(getterChainIt.next(), helpers, null);
			}
		}
		ArrayList<ArrayList<Object>> result = new ArrayList<ArrayList<Object>>();
		Iterator<FieldKeyHelper> helpersIt = updateHelpers(lastGetter, helpers, null).iterator();
		while (helpersIt.hasNext()) {
			result.add(helpersIt.next().getIndexKeyChain());
		}
		return result;
	}

	protected final Object getValueOf(Object rootGraph, ArrayList indexesKeys) throws Exception {
		return getValueOf(rootGraph, getIndexKeyStack(indexesKeys));
	}

	private final Object getValueOf(Object rootGraph, Stack indexesKeys) throws Exception {
		Object graph = rootGraph;
		if (getterChain != null) {
			Iterator<Accessor> it = getterChain.iterator();
			while (it.hasNext()) {
				if (graph != null) {
					graph = getValueOfHelper(graph, it.next(), indexesKeys);
				} else {
					break;
				}
			}
		}
		if (graph != null) {
			return getValueOfHelper(graph, lastGetter, indexesKeys);
		} else {
			return null;
		}
	}

	private boolean isFieldExcluded(Class graph, String field, HashMap<Class, HashSet<String>> excludedFieldMap) {
		if (excludedFieldMap != null) {
			return excludedFieldMap.containsKey(graph) ? excludedFieldMap.get(graph).contains(field) : false;
		}
		return false;
	}

	protected abstract boolean isFieldOmitted(Class graph, String field);

	protected abstract boolean isTerminalType(Object passThrough);

	private final ArrayList<FieldKeyHelper> updateHelpers(Accessor getter, ArrayList<FieldKeyHelper> helpers, Stack indexesKeys) throws Exception {
		Iterator<FieldKeyHelper> helpersIt = helpers.iterator();
		ArrayList<FieldKeyHelper> newHelpers;
		switch (getter.getType()) {
			case COLLECTION:
				newHelpers = new ArrayList<FieldKeyHelper>();
				if (indexesKeys != null) {
					newHelpers
							.add(helpersIt.next().append(getter, ((Integer) indexesKeys.pop()).intValue(), transfilter, associationPathSeparator));
				} else {
					while (helpersIt.hasNext()) {
						FieldKeyHelper helper = helpersIt.next();
						for (int i = 0; i < helper.getCollectionSize(getter); i++) {
							newHelpers.add(helper.append(getter, i, transfilter, associationPathSeparator));
						}
					}
				}
				return newHelpers;
			case MAP:
				newHelpers = new ArrayList<FieldKeyHelper>();
				if (indexesKeys != null) {
					newHelpers.add(helpersIt.next().append(getter, indexesKeys.pop(), transfilter, associationPathSeparator));
				} else {
					while (helpersIt.hasNext()) {
						FieldKeyHelper helper = helpersIt.next();
						Iterator keysIt = helper.getKeys(getter).iterator();
						while (keysIt.hasNext()) {
							newHelpers.add(helper.append(getter, keysIt.next(), transfilter, associationPathSeparator));
						}
					}
				}
				return newHelpers;
			case REFERENCE:
			default:
				while (helpersIt.hasNext()) {
					helpersIt.next().append(getter, transfilter, associationPathSeparator);
				}
				return helpers;
		}
	}
}
