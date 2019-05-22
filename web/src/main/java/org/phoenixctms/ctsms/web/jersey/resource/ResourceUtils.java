package org.phoenixctms.ctsms.web.jersey.resource;

import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.core.MultivaluedMap;

import org.phoenixctms.ctsms.util.AssociationPath;
import org.phoenixctms.ctsms.util.MethodTransfilter;

public final class ResourceUtils {

	public static final Integer LIST_GRAPH_MAX_INVENTORY_INSTANCES = 2;
	public static final Integer LIST_GRAPH_MAX_STAFF_INSTANCES = 2;
	public static final Integer LIST_GRAPH_MAX_COURSE_INSTANCES = 1;
	public static final Integer LIST_GRAPH_MAX_USER_INSTANCES = 1;
	public static final Integer LIST_GRAPH_MAX_PROBAND_INSTANCES = 1;
	private static final MethodTransfilter RESOURCE_METHOD_TRANSFILTER = MethodTransfilter.DEFAULT_TRANSFILTER;

	public static String getMethodPath(Class<?> resourceClass, String methodName) {
		StringBuilder sb = new StringBuilder(((Path) resourceClass.getAnnotation(Path.class)).value());
		sb.append("/");
		try {
			Path annotation = AssociationPath.findMethod(methodName, RESOURCE_METHOD_TRANSFILTER, true, resourceClass.getMethods()).getAnnotation(Path.class);
			if (annotation != null) {
				sb.append(annotation.value());
			}
		} catch (Exception e) {
			sb.append(methodName);
		}
		return sb.toString();
	}

	public static String popQueryParamValue(String parameter, MultivaluedMap<String, String> queryParameters) {
		List<String> values = queryParameters.remove(parameter);
		if (values != null && values.iterator().hasNext()) {
			return values.iterator().next();
		}
		return null;
	}

	private ResourceUtils() {
	}
}
