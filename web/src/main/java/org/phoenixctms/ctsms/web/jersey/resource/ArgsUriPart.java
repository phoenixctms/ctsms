package org.phoenixctms.ctsms.web.jersey.resource;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map.Entry;
import java.util.Set;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import org.andromda.spring.MethodParameterNames;
import org.phoenixctms.ctsms.util.AssociationPath;
import org.phoenixctms.ctsms.util.MethodTransfilter;

public class ArgsUriPart implements UriPart {

	private final static Comparator<String> PARAMETER_TO_ARGUMENT_SORTING = null;
	private boolean slurp;
	private String resource;
	private LinkedHashMap<String, Object> overrides;
	private LinkedHashMap<String, Object> defaults;
	private HashSet<String> excludePrimitiveConversion;
	private Class declaringInterface;
	private ArrayList<StringConverter> primitiveConversionPrecedence;
	private MethodTransfilter methodTransfilter;

	public ArgsUriPart(Class declaringInterface) {
		this(declaringInterface, null, MethodTransfilter.DEFAULT_TRANSFILTER);
	}

	public ArgsUriPart(Class declaringInterface, MethodTransfilter methodTransfilter) {
		this(declaringInterface, null, methodTransfilter);
	}

	public ArgsUriPart(Class declaringInterface, String resource) {
		this(declaringInterface, resource, MethodTransfilter.DEFAULT_TRANSFILTER);
	}

	public ArgsUriPart(Class declaringInterface, String resource, MethodTransfilter methodTransfilter) {
		excludePrimitiveConversion = new HashSet<String>();
		overrides = new LinkedHashMap<String, Object>();
		defaults = new LinkedHashMap<String, Object>();
		primitiveConversionPrecedence = new ArrayList<StringConverter>();
		reset(declaringInterface, resource, methodTransfilter);
	}

	private Method getAnnotatedMethod(String resource) throws Exception {
		if (declaringInterface != null) {
			if (methodTransfilter != null) {
				return AssociationPath.findMethod(resource, methodTransfilter, true, declaringInterface.getMethods());
			} else {
				return AssociationPath.findMethod(resource, true, declaringInterface.getMethods());
			}
		}
		return null;
	}

	private LinkedHashMap<String, Object> getArgMap(MultivaluedMap<String, String> queryParameters) throws Exception {
		LinkedHashMap<String, Object> arguments = new LinkedHashMap<String, Object>();
		Collection<NamedParameter> namedParameters = getNamedParameters(resource, false);
		if (namedParameters.size() > 0) {
			Iterator<NamedParameter> it = namedParameters.iterator();
			while (it.hasNext()) {
				NamedParameter namedParameter = it.next();
				arguments.put(namedParameter.getName(), getArgumentValue(namedParameter.getName(), queryParameters, namedParameter.getType()));
			}
		} else if (slurp && queryParameters.keySet().size() > 0) {
			ArrayList<String> parameters = new ArrayList<String>(queryParameters.keySet());
			Collections.sort(parameters, PARAMETER_TO_ARGUMENT_SORTING);
			Iterator<String> it = parameters.iterator();
			while (it.hasNext()) { // no concurrentmodexc
				String name = it.next();
				arguments.put(name, getArgumentValue(name, queryParameters, null));
			}
		}
		return arguments;
	}

	public LinkedHashMap<String, Object> getArgMap(UriInfo uriInfo) throws Exception {
		return getArgMap(uriInfo.getQueryParameters(true));
	}

	private Object[] getArgs(MultivaluedMap<String, String> queryParameters) throws Exception {
		return getArgMap(queryParameters).values().toArray();
	}

	public Object[] getArgs(UriInfo uriInfo) throws Exception {
		return getArgs(uriInfo.getQueryParameters(true));
	}

	private Object getArgumentValue(String parameterName, MultivaluedMap<String, String> queryParameters, Type type) throws Exception {
		if (overrides.containsKey(parameterName)) {
			return overrides.get(parameterName);
		} else if (queryParameters.containsKey(parameterName)) {
			String value = ResourceUtils.popQueryParamValue(parameterName, queryParameters);
			if (type != null) {
				return StringConverter.getConverter(type).convert(value);
			} else {
				if (excludePrimitiveConversion.contains(parameterName)) {
					return value;
				}
				return StringConverter.convert(value, primitiveConversionPrecedence);
			}
		} else if (defaults.containsKey(parameterName)) {
			return defaults.get(parameterName);
		} else {
			return null;
		}
	}

	public LinkedHashMap<String, Object> getDefaults() {
		return defaults;
	}

	public HashSet<String> getExcludePrimitiveConversion() {
		return excludePrimitiveConversion;
	}

	public MethodTransfilter getMethodTransfilter() {
		return methodTransfilter;
	}

	public LinkedHashSet<NamedParameter> getNamedParameters(String resource, boolean excludeOverriden) throws Exception {
		LinkedHashMap<String, NamedParameter> namedParameters = new LinkedHashMap<String, NamedParameter>();
		Method method = getAnnotatedMethod(resource);
		if (method != null) {
			MethodParameterNames annotation = method.getAnnotation(MethodParameterNames.class);
			String[] parameterNames = null;
			if (annotation != null) {
				parameterNames = annotation.value();
			}
			Class[] parameterTypes = method.getParameterTypes();
			if (parameterNames != null && parameterTypes != null) {
				for (int i = 0; i < parameterNames.length; i++) {
					namedParameters.put(parameterNames[i], new NamedParameter(parameterNames[i], parameterTypes[i]));
				}
			}
		}
		Iterator<Entry<String, Object>> it = defaults.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Object> defaultParameter = it.next();
			if (!namedParameters.containsKey(defaultParameter.getKey())) {
				namedParameters.put(defaultParameter.getKey(), new NamedParameter(defaultParameter.getKey(), defaultParameter.getValue().getClass()));
			}
		}
		it = overrides.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Object> overrideParameter = it.next();
			namedParameters.put(overrideParameter.getKey(), new NamedParameter(overrideParameter.getKey(), overrideParameter.getValue().getClass()));
		}
		if (excludeOverriden) {
			it = overrides.entrySet().iterator();
			while (it.hasNext()) {
				namedParameters.remove(it.next().getKey());
			}
		}
		return new LinkedHashSet<NamedParameter>(namedParameters.values());
	}

	public LinkedHashMap<String, Object> getOverrides() {
		return overrides;
	}

	public ArrayList<StringConverter> getPrimitiveConversionPrecedence() {
		return primitiveConversionPrecedence;
	}

	@Override
	public Set<NamedParameter> getStaticQueryParameterNames() throws Exception {
		return getNamedParameters(resource, true);
	}

	@Override
	public boolean isSlurpQueryParameter() throws Exception {
		return slurp && getNamedParameters(resource, false).size() == 0;
	}

	public void reset(Class declaringInterface, String resource, MethodTransfilter methodTransfilter) {
		excludePrimitiveConversion.clear();
		overrides.clear();
		defaults.clear();
		primitiveConversionPrecedence.clear();
		this.methodTransfilter = methodTransfilter;
		this.declaringInterface = declaringInterface;
		this.resource = resource;
		slurp = true;
	}

	public void setDeclaringInterface(Class declaringInterface) {
		this.declaringInterface = declaringInterface;
	}

	public void setMethodTransfilter(MethodTransfilter methodTransfilter) {
		this.methodTransfilter = methodTransfilter;
	}

	@Override
	public void setSlurpQueryParameter(boolean slurp) {
		this.slurp = slurp;
	}

	@Override
	public Object shiftParameters(MultivaluedMap<String, String> queryParameters) throws Exception {
		return getArgs(queryParameters);
	}
}
