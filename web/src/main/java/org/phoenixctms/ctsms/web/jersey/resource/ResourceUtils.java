package org.phoenixctms.ctsms.web.jersey.resource;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.phoenixctms.ctsms.util.AssociationPath;
import org.phoenixctms.ctsms.util.MethodTransfilter;
import org.phoenixctms.ctsms.vo.FileStreamOutVO;

public final class ResourceUtils {

	public static final Integer LIST_GRAPH_MAX_INVENTORY_INSTANCES = 2;
	public static final Integer LIST_GRAPH_MAX_STAFF_INSTANCES = 2;
	public static final Integer LIST_GRAPH_MAX_COURSE_INSTANCES = 1;
	public static final Integer LIST_GRAPH_MAX_USER_INSTANCES = 2;
	public static final Integer LIST_GRAPH_MAX_PROBAND_INSTANCES = 1;
	private static final MethodTransfilter RESOURCE_METHOD_TRANSFILTER = MethodTransfilter.DEFAULT_TRANSFILTER;
	private static final String CONTENT_DISPOSITION = "Content-Disposition";
	private static final String X_CONTENT_TYPE_OPTIONS = "X-Content-Type-Options";

	// serve uploaded files for download only, so the browser never renders them (e.g. html, svg) under the application's origin:
	public static Response getAttachmentFileStreamResponse(FileStreamOutVO stream) throws UnsupportedEncodingException {
		if (stream.getStream() == null) {
			// no readable content (e.g. a file without data), so do not announce a body that is never sent:
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		ResponseBuilder response = Response.ok(stream.getStream(), stream.getContentType().getMimeType());
		response.header(HttpHeaders.CONTENT_LENGTH, stream.getSize());
		String fileName = stream.getFileName();
		if (fileName != null && fileName.length() > 0) {
			response.header(CONTENT_DISPOSITION, "attachment; filename=\"" + fileName.replaceAll("[^\\x20-\\x7E]|[\"\\\\]", "_") + "\"; filename*=UTF-8''"
					+ URLEncoder.encode(fileName, "UTF-8").replace("+", "%20"));
		} else {
			response.header(CONTENT_DISPOSITION, "attachment");
		}
		response.header(X_CONTENT_TYPE_OPTIONS, "nosniff");
		return response.build();
	}

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

	private static String getQueryParamValue(List<String> values) {
		if (values != null && values.iterator().hasNext()) {
			return values.iterator().next();
		}
		return null;
	}

	public static String getQueryParamValue(String parameter, MultivaluedMap<String, String> queryParameters) {
		return getQueryParamValue(queryParameters.get(parameter));
	}

	public static String popQueryParamValue(String parameter, MultivaluedMap<String, String> queryParameters) {
		return getQueryParamValue(queryParameters.remove(parameter));
	}

	private ResourceUtils() {
	}
}
