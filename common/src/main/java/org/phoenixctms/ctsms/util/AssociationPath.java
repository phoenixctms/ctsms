package org.phoenixctms.ctsms.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import org.andromda.spring.MethodParameterNames;

public class AssociationPath {

	// public static final MethodTransfilter MethodTransfilter.DEFAULT_TRANSFILTER = new MethodTransfilter();
	public final static String ASSOCIATION_PATH_SEPARATOR = ".";
	private final static Pattern ASSOCIATION_PATH_SEPARATOR_REGEXP = Pattern.compile(Pattern.quote(ASSOCIATION_PATH_SEPARATOR));
	public final static String ASSOCIATION_PATH_PATTERN = "([a-zA-Z_][a-zA-Z0-9_]*)(" + Pattern.quote(ASSOCIATION_PATH_SEPARATOR) + "[a-zA-Z_][a-zA-Z0-9_]*)*";

	public static Method findMethod(String methodName, boolean sortMethods, Method[] methods) throws Exception {
		return findMethod(methodName, MethodTransfilter.DEFAULT_TRANSFILTER, sortMethods, methods);
	}

	public static Method findMethod(String methodName, MethodTransfilter filter, boolean sortMethods, Method[] methods) throws Exception {
		if (methods != null && !filter.isTransformedEmpty(methodName)) {
			if (sortMethods) {
				Arrays.sort(methods, filter.getMethodComparator());
			}
			for (int i = 0; i < methods.length; i++) {
				if (filter.include(methods[i])
						&& !filter.exclude(methods[i])
						&& filter.isTransformedEqual(methodName, methods[i].getName())) {
					return methods[i];
				}
			}
		}
		throw new NoSuchMethodException(filter.transform(methodName));
	}

	public static Map<String, Integer> getArgumentIndexMap(Method method) {
		HashMap<String, Integer> argIndexMap;
		String[] parameterNames = method.getAnnotation(MethodParameterNames.class).value();
		if (parameterNames != null) {
			argIndexMap = new HashMap<String, Integer>(parameterNames.length);
			for (int i = 0; i < parameterNames.length; i++) {
				argIndexMap.put(parameterNames[i], i);
			}
		} else {
			argIndexMap = new HashMap<String, Integer>();
		}
		return argIndexMap;
	}

	public static Object invoke(String methodPath, Object root, boolean padNullArgs, Object... args) throws Exception {
		return invoke(methodPath, root, null, MethodTransfilter.DEFAULT_TRANSFILTER, padNullArgs, args);
	}

	public static Object invoke(String methodPath, Object root, Class rootInterface, boolean padNullArgs, Object... args) throws Exception {
		return invoke(methodPath, root, rootInterface, MethodTransfilter.DEFAULT_TRANSFILTER, padNullArgs, args);
	}

	public static Object invoke(String methodPath, Object root, Class rootInterface, MethodTransfilter filter, boolean padNullArgs, Object... args) throws Exception {
		return (new AssociationPath(methodPath)).invoke(root, rootInterface, filter, padNullArgs, args);
	}

	// public static Collection<String> listMethodNames(Class propertyInterface) throws Exception {
	// return listMethodNames(propertyInterface, MethodTransfilter.DEFAULT_TRANSFILTER);
	// }
	//
	// public static Collection<String> listMethodNames(Class propertyInterface, MethodTransfilter filter) throws Exception {
	// Collection methods = listMethods(propertyInterface, filter);
	// CollectionUtils.transform(methods, new Transformer() {
	// public Object transform(Object input) {
	// return ((Method) input).getName();
	// }
	// });
	// return methods;
	// }

	public static Collection<Method> listMethods(Class propertyInterface) throws Exception {
		return listMethods(propertyInterface, MethodTransfilter.DEFAULT_TRANSFILTER);
	}

	public static Collection<Method> listMethods(Class propertyInterface, MethodTransfilter filter) throws Exception {
		ArrayList<Method> result = new ArrayList<Method>();
		HashSet<String> dupeCheck = new HashSet<String>();
		if (propertyInterface != null) {
			Method[] methods = propertyInterface.getMethods();
			Arrays.sort(methods, filter.getMethodComparator());
			for (int i = 0; i < methods.length; i++) {
				if (filter.include(methods[i])
						&& !filter.exclude(methods[i])
						&& !filter.isTransformedEmpty(methods[i].getName())
						&& dupeCheck.add(filter.transform(methods[i].getName()))) {
					result.add(methods[i]);
				}
			}
		}
		return result;
	}

	public static boolean methodExists(Class propertyInterface, String methodName) {
		return methodExists(propertyInterface, methodName, MethodTransfilter.DEFAULT_TRANSFILTER);
	}

	public static boolean methodExists(Class propertyInterface, String methodName, MethodTransfilter filter) {
		try {
			return findMethod(methodName, filter, false, propertyInterface.getMethods()) != null;
		} catch (Exception e) {
			return false;
		}
	}

	private ArrayList<String> associationPath;
	private String joinAlias;
	private int joinOrder;
	private String aliasedPathString;
	private boolean valid;

	public AssociationPath() {
		valid = false;
		associationPath = new ArrayList<String>();
	}

	public AssociationPath(String fullQualifiedPropertyName) {
		valid = false;
		associationPath = new ArrayList<String>();
		setFullQualifiedPropertyName(fullQualifiedPropertyName);
	}

	public AssociationPath(String fullQualifiedPropertyName, String aliasedPathString) {
		valid = false;
		associationPath = new ArrayList<String>();
		this.aliasedPathString = aliasedPathString;
		setFullQualifiedPropertyName(fullQualifiedPropertyName);
	}

	public void clear() {
		valid = false;
		this.joinAlias = null;
		this.joinOrder = 0;
		this.aliasedPathString = null;
		associationPath.clear();
	}

	public void dropFirstPathElement() {
		if (associationPath.size() > 1) {
			associationPath.remove(0);
			if (associationPath.size() == 0) {
				valid = false;
			}
		}
	}

	public String getAliasedPathString() {
		return this.aliasedPathString;
	}

	public ArrayList<String> getFullPath() {
		return associationPath;
	}

	public String getFullQualifiedPropertyName() {
		StringBuilder result = new StringBuilder();
		Iterator<String> it = associationPath.iterator();
		while (it.hasNext()) {
			result.append(it.next());
			if (it.hasNext()) {
				result.append(ASSOCIATION_PATH_SEPARATOR);
			}
		}
		return result.toString();
	}

	public String getJoinAlias() {
		return this.joinAlias;
	}

	public int getJoinOrder() {
		return joinOrder;
	}

	public ArrayList<String> getPath() {
		ArrayList<String> result = new ArrayList<String>();
		for (int i = 0; i < getPathDepth(); i++) {
			result.add(associationPath.get(i));
		}
		return result;
	}

	public int getPathDepth() {
		return associationPath.size() - 1;
	}

	public String getPathElement(int i) {
		if (i >= 0 && i < getPathDepth()) {
			return associationPath.get(i);
		} else {
			return null;
		}
	}

	public String getPathString() {
		return getPathString(getPathDepth());
	}

	public String getPathString(int pathDepth) {
		StringBuilder result = new StringBuilder();
		int depth;
		if (pathDepth > getPathDepth()) {
			depth = getPathDepth();
		} else {
			depth = pathDepth;
		}
		for (int i = 0; i < depth; i++) {
			result.append(result.length() == 0 ? associationPath.get(i) : ASSOCIATION_PATH_SEPARATOR + associationPath.get(i));
		}
		return result.toString();
	}

	public String getPropertyName() {
		if (associationPath.size() > 0) {
			return associationPath.get(associationPath.size() - 1);
		} else {
			return null;
		}
	}

	public String getRootEntityName() {
		if (associationPath.size() > 0) {
			return associationPath.get(0);
		} else {
			return null;
		}
	}

	public Object invoke(Object root, boolean padNullArgs, Object... args) throws Exception {
		return invoke(root, null, MethodTransfilter.DEFAULT_TRANSFILTER, padNullArgs, args);
	}

	public Object invoke(Object root, Class rootInterface, boolean padNullArgs, Object... args) throws Exception {
		return invoke(root, rootInterface, MethodTransfilter.DEFAULT_TRANSFILTER, padNullArgs, args);
	}

	public Object invoke(Object root, Class rootInterface, MethodTransfilter filter, boolean padNullArgs, Object... args) throws Exception {
		if (valid) {
			Object property = root;
			boolean isRoot = rootInterface != null;
			Iterator<String> it = associationPath.iterator();
			while (it.hasNext()) {
				String methodName = it.next(); // "get" + capitalizeFirstChar(it.next());
				Method[] methods;
				if (isRoot) {
					methods = rootInterface.getMethods();
				} else {
					methods = property.getClass().getMethods();
				}
				Method method = findMethod(methodName, filter, isRoot, methods);
				if (it.hasNext()) {
					property = method.invoke(property);
				} else {
					if (padNullArgs) {
						Object[] paddedArgs = new Object[method.getParameterTypes().length];
						for (int i = 0; i < paddedArgs.length; i++) {
							if (args != null && i < args.length) {
								paddedArgs[i] = args[i];
							} else {
								paddedArgs[i] = null;
							}
						}
						return method.invoke(property, paddedArgs);
					} else {
						return method.invoke(property, args);
					}
				}
				isRoot = false;
			}
		}
		throw new IllegalArgumentException(getFullQualifiedPropertyName());
	}

	public boolean isValid() {
		return valid;
	}

	public void prependPathElement(String entityName) {
		if (associationPath.size() > 0 && entityName != null && entityName.length() > 0) {
			associationPath.add(0, entityName);
		}
	}

	public void setAliasedPathString(String aliasedPathString) {
		this.aliasedPathString = aliasedPathString;
	}

	public void setFullQualifiedPropertyName(String fullQualifiedPropertyName) {
		valid = false;
		associationPath.clear();
		if (fullQualifiedPropertyName != null && fullQualifiedPropertyName.length() > 0) {
			String[] associations = ASSOCIATION_PATH_SEPARATOR_REGEXP.split(fullQualifiedPropertyName, -1);
			for (int i = 0; i < associations.length; i++) {
				if (associations[i].length() > 0) {
					associationPath.add(associations[i]);
				} else {
					associationPath.clear();
					return;
				}
			}
			valid = true;
		}
	}

	public void setJoinAlias(String joinAlias) {
		this.joinAlias = joinAlias;
	}

	public void setJoinOrder(int joinOrder) {
		this.joinOrder = joinOrder;
	}
}
