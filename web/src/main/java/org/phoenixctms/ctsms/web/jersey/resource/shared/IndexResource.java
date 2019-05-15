package org.phoenixctms.ctsms.web.jersey.resource.shared;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang3.ClassUtils;
import org.phoenixctms.ctsms.util.AssociationPath;
import org.phoenixctms.ctsms.util.MethodTransfilter;
import org.phoenixctms.ctsms.vo.AnnouncementVO;
import org.phoenixctms.ctsms.vo.FilePDFVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.jersey.compare.ResourceClassComparator;
import org.phoenixctms.ctsms.web.jersey.compare.ResourceMethodComparator;
import org.phoenixctms.ctsms.web.jersey.compare.SubResourceMethodComparator;
import org.phoenixctms.ctsms.web.jersey.index.CompleteIndex;
import org.phoenixctms.ctsms.web.jersey.index.CourseListIndex;
import org.phoenixctms.ctsms.web.jersey.index.IndexBase;
import org.phoenixctms.ctsms.web.jersey.index.InputFieldListIndex;
import org.phoenixctms.ctsms.web.jersey.index.InventoryListIndex;
import org.phoenixctms.ctsms.web.jersey.index.MassMailListIndex;
import org.phoenixctms.ctsms.web.jersey.index.ProbandListIndex;
import org.phoenixctms.ctsms.web.jersey.index.ResourceIndex;
import org.phoenixctms.ctsms.web.jersey.index.SelectionSetIndex;
import org.phoenixctms.ctsms.web.jersey.index.StaffListIndex;
import org.phoenixctms.ctsms.web.jersey.index.TrialListIndex;
import org.phoenixctms.ctsms.web.jersey.index.UserListIndex;
import org.phoenixctms.ctsms.web.jersey.resource.ArgsUriPart;
import org.phoenixctms.ctsms.web.jersey.resource.NamedParameter;
import org.phoenixctms.ctsms.web.jersey.resource.PSFUriPart;
import org.phoenixctms.ctsms.web.jersey.resource.Page;
import org.phoenixctms.ctsms.web.jersey.resource.course.CourseResource;
import org.phoenixctms.ctsms.web.jersey.resource.inputfield.InputFieldResource;
import org.phoenixctms.ctsms.web.jersey.resource.inventory.InventoryResource;
import org.phoenixctms.ctsms.web.jersey.resource.massmail.MassMailResource;
import org.phoenixctms.ctsms.web.jersey.resource.proband.ProbandResource;
import org.phoenixctms.ctsms.web.jersey.resource.staff.StaffResource;
import org.phoenixctms.ctsms.web.jersey.resource.trial.TrialResource;
import org.phoenixctms.ctsms.web.jersey.resource.user.UserResource;
import org.phoenixctms.ctsms.web.jersey.wrapper.JsValuesOutVOPage;
import org.phoenixctms.ctsms.web.jersey.wrapper.NoShortcutSerializationWrapper;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.sun.jersey.api.model.AbstractResource;
import com.sun.jersey.api.model.AbstractResourceMethod;
import com.sun.jersey.api.model.AbstractSubResourceMethod;
import com.sun.jersey.api.model.Parameter;
import com.sun.jersey.server.impl.modelapi.annotation.IntrospectionModeller;

@Path("/")
public class IndexResource {

	// https://dzone.com/articles/jersey-listing-all-resources
	private final static String JS_TITLE_FIELD = "title";
	private final static String JS_INSTANCE_FIELD = "instance";
	private final static String JS_VERSION_FIELD = "version";
	private final static String JS_JVM_FIELD = "jvm";
	private final static String JS_REALM_FIELD = "realm";
	private final static String JS_ANNOUNCEMENT_FIELD = "announcement";
	private final static String JS_CLASSES_FIELD = "services";
	private final static String JS_CLASS_NAME_FIELD = "name";
	private final static String JS_CLASS_PATH_FIELD = "root";
	private final static String JS_RESOURCES_FIELD = "resources";
	private final static String JS_PATH_FIELD = "path";
	private final static String JS_METHODS_FIELD = "methods";
	private final static String JS_VERB_FIELD = "verb";
	private final static String JS_CONSUMES_FIELD = "mime_in";
	private final static String JS_PRODUCES_FIELD = "mime_out";
	private final static String JS_IN_VO_FIELD = "json_in";
	private final static String JS_OUT_VO_FIELD = "json_out";
	private final static String JS_JSON_VALUE = "json data";
	private final static String JS_INDEX_VALUE = "json index";
	private final static String JS_INDEX_FIELD = "index";
	private static final String JS_QUERY_PARAMS_FIELD = "query_params";
	private final static String JS_PAGE_PSF_FIELD = "psf";
	private final static String JS_PAGE_ROWS_FIELD = "rows";
	private final static String JS_PAGE_JS_ROWS_FIELD = "js_rows";
	private final static Comparator<Class<?>> RESOURCE_CLASS_COMPARATOR = new ResourceClassComparator();
	private final static Comparator<AbstractResourceMethod> RESOURCE_METHOD_COMPARATOR = new ResourceMethodComparator();
	private final static Comparator<AbstractSubResourceMethod> SUB_RESOURCE_METHOD_COMPARATOR = new SubResourceMethodComparator();
	private final static MethodTransfilter VO_METHOD_TRANSFILTER = MethodTransfilter.getVoMethodTransfilter(false);

	private static void addResource(JsonObject resourcesNode, String uriPrefix, AbstractResourceMethod method, String path) throws Exception {
		if (resourcesNode.get(uriPrefix) == null) {
			JsonObject resourceNode = new JsonObject();
			resourceNode.addProperty(JS_PATH_FIELD, path);
			resourceNode.add(JS_METHODS_FIELD, new JsonArray());
			resourcesNode.add(uriPrefix, resourceNode);
		}
		JsonObject methodNode = new JsonObject();
		methodNode.addProperty(JS_VERB_FIELD, method.getHttpMethod());
		if (method.areInputTypesDeclared()) {
			if (method.getSupportedInputTypes().contains(MediaType.valueOf(MediaType.APPLICATION_JSON))) {
				Iterator<Parameter> it = method.getParameters().iterator();
				while (it.hasNext()) {
					Parameter parameter = it.next();
					if (!parameter.isQualified()) {
						methodNode.add(JS_IN_VO_FIELD, createVOInputTypeNode(parameter.getParameterClass(), parameter.getParameterType()));
						// first only:
						break;
					}
				}
			} else {
				methodNode.add(JS_CONSUMES_FIELD, createMediaTypeNode(method.getSupportedInputTypes()));
			}
		}
		LinkedHashSet<NamedParameter> queryParams = new LinkedHashSet<NamedParameter>();
		boolean hasUriInfo = false;
		Iterator<Parameter> it = method.getParameters().iterator();
		while (it.hasNext()) {
			Parameter parameter = it.next();
			hasUriInfo |= UriInfo.class.isAssignableFrom(parameter.getParameterClass());
			if (parameter.isAnnotationPresent(QueryParam.class)) {
				queryParams.add(new NamedParameter(parameter.getAnnotation(QueryParam.class).value(), parameter.getParameterClass()));
			}
		}
		Class<?> returnType = method.getReturnType();
		if (method.areOutputTypesDeclared() && !Response.class.isAssignableFrom(returnType)) {
			JsonElement returnTypeNode;
			if (IndexBase.class.isAssignableFrom(returnType)) {
				if (CourseListIndex.class.equals(returnType)) {
					returnTypeNode = CourseResource.LIST_INDEX.getJson();
				} else if (InputFieldListIndex.class.equals(returnType)) {
					returnTypeNode = InputFieldResource.LIST_INDEX.getJson();
				} else if (InventoryListIndex.class.equals(returnType)) {
					returnTypeNode = InventoryResource.LIST_INDEX.getJson();
				} else if (ProbandListIndex.class.equals(returnType)) {
					returnTypeNode = ProbandResource.LIST_INDEX.getJson();
				} else if (StaffListIndex.class.equals(returnType)) {
					returnTypeNode = StaffResource.LIST_INDEX.getJson();
				} else if (TrialListIndex.class.equals(returnType)) {
					returnTypeNode = TrialResource.LIST_INDEX.getJson();
				} else if (MassMailListIndex.class.equals(returnType)) {
					returnTypeNode = MassMailResource.LIST_INDEX.getJson();
				} else if (UserListIndex.class.equals(returnType)) {
					returnTypeNode = UserResource.LIST_INDEX.getJson();
				} else if (SelectionSetIndex.class.equals(returnType)) {
					returnTypeNode = SelectionSetResource.INDEX.getJson();
				} else if (CompleteIndex.class.equals(returnType)) {
					returnTypeNode = ToolsResource.COMPLETE_INDEX.getJson();
				} else {
					returnTypeNode = new JsonPrimitive(JS_INDEX_VALUE);
				}
				methodNode.add(JS_INDEX_FIELD, returnTypeNode);
			} else if (JsonElement.class.isAssignableFrom(returnType)) {
				returnTypeNode = new JsonPrimitive(JS_JSON_VALUE);
				methodNode.add(JS_OUT_VO_FIELD, returnTypeNode);
			} else if (JsValuesOutVOPage.class.isAssignableFrom(returnType)) {
				try {
					returnType = (Class<?>) ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments()[0];
				} catch (Exception e) {
					returnType = Object.class;
				}
				Class<?> jsValuesReturnType;
				try {
					jsValuesReturnType = (Class<?>) ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments()[1];
				} catch (Exception e) {
					jsValuesReturnType = Object.class;
				}
				returnTypeNode = createJsValuesPageNode(returnType, jsValuesReturnType);
				if (hasUriInfo && HttpMethod.GET.equals(method.getHttpMethod())) {
					queryParams.addAll(PSFUriPart.SLURPED_NAMED_QUERY_PARAMETERS);
				}
				if (queryParams.size() > 0) {
					methodNode.add(JS_QUERY_PARAMS_FIELD, createQueryParameterNode(queryParams));
				}
				methodNode.add(JS_OUT_VO_FIELD, returnTypeNode);
			} else if (Page.class.isAssignableFrom(returnType)) {
				try {
					returnType = (Class<?>) ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments()[0];
				} catch (Exception e) {
					returnType = Object.class;
				}
				returnTypeNode = createPageNode(returnType);
				queryParams.addAll(PSFUriPart.SLURPED_NAMED_QUERY_PARAMETERS);
				methodNode.add(JS_QUERY_PARAMS_FIELD, createQueryParameterNode(queryParams));
				methodNode.add(JS_OUT_VO_FIELD, returnTypeNode);
			} else {
				returnTypeNode = createVOReturnTypeNode(returnType, method.getGenericReturnType());
				if (FilePDFVO.class.equals(returnType)
				) {
					queryParams.addAll(PSFUriPart.SLURPED_NAMED_QUERY_PARAMETERS);
				}
				if (queryParams.size() > 0) {
					methodNode.add(JS_QUERY_PARAMS_FIELD, createQueryParameterNode(queryParams));
				}
				methodNode.add(JS_OUT_VO_FIELD, returnTypeNode);
			}
		} else {
			// query params for aggregatepdf files not shown:
			if (queryParams.size() > 0) {
				methodNode.add(JS_QUERY_PARAMS_FIELD, createQueryParameterNode(queryParams));
			}
			methodNode.add(JS_PRODUCES_FIELD, createMediaTypeNode(method.getSupportedOutputTypes()));
		}
		resourcesNode.getAsJsonObject(uriPrefix).getAsJsonArray(JS_METHODS_FIELD).add(methodNode);
	}

	private static JsonObject createJsValuesPageNode(Class<?> pageValuesReturnType, Class<?> jsValuesReturnType) throws Exception {
		JsonObject pageNode = new JsonObject();
		JsonElement voNode;
		Collection<Method> fields = AssociationPath.listMethods(pageValuesReturnType, VO_METHOD_TRANSFILTER);
		if (isTerminalType(pageValuesReturnType) || fields.size() == 0) {
			voNode = new JsonPrimitive(ClassUtils.getSimpleName(pageValuesReturnType));
		} else {
			voNode = createVONode(AssociationPath.listMethods(pageValuesReturnType, VO_METHOD_TRANSFILTER), false);
		}
		pageNode.add(JS_PAGE_PSF_FIELD, createVONode(AssociationPath.listMethods(PSFVO.class, VO_METHOD_TRANSFILTER), false));
		pageNode.add(JS_PAGE_ROWS_FIELD, markMapCollection(voNode, null, false, true));
		fields = AssociationPath.listMethods(jsValuesReturnType, VO_METHOD_TRANSFILTER);
		if (isTerminalType(jsValuesReturnType) || fields.size() == 0) {
			voNode = new JsonPrimitive(ClassUtils.getSimpleName(jsValuesReturnType));
		} else {
			voNode = createVONode(AssociationPath.listMethods(jsValuesReturnType, VO_METHOD_TRANSFILTER), false);
		}
		pageNode.add(JS_PAGE_JS_ROWS_FIELD, markMapCollection(voNode, null, false, true));
		return pageNode;
	}

	private static JsonArray createMediaTypeNode(List<MediaType> types) {
		JsonArray typesNode = new JsonArray();
		if (types != null) {
			Iterator<MediaType> it = types.iterator();
			while (it.hasNext()) {
				typesNode.add(new JsonPrimitive(it.next().toString()));
			}
		}
		return typesNode;
	}

	private static JsonObject createPageNode(Class<?> returnType) throws Exception {
		JsonObject pageNode = new JsonObject();
		JsonElement voNode;
		Collection<Method> fields = AssociationPath.listMethods(returnType, VO_METHOD_TRANSFILTER);
		if (isTerminalType(returnType) || fields.size() == 0) {
			voNode = new JsonPrimitive(ClassUtils.getSimpleName(returnType));
		} else {
			voNode = createVONode(AssociationPath.listMethods(returnType, VO_METHOD_TRANSFILTER), false);
		}
		pageNode.add(JS_PAGE_PSF_FIELD, createVONode(AssociationPath.listMethods(PSFVO.class, VO_METHOD_TRANSFILTER), false));
		pageNode.add(JS_PAGE_ROWS_FIELD, markMapCollection(voNode, null, false, true));
		return pageNode;
	}

	private static JsonObject createQueryParameterNode(Collection<NamedParameter> params) {
		JsonObject queryParamsNode = new JsonObject();
		Iterator<NamedParameter> parametersIt = params.iterator();
		while (parametersIt.hasNext()) {
			NamedParameter namedParameter = parametersIt.next();
			queryParamsNode.addProperty(namedParameter.getName(), ClassUtils.getSimpleName(namedParameter.getType()));
		}
		return queryParamsNode;
	}

	private static JsonElement createVOInputTypeNode(Class<?> inputType, Type genericType) throws Exception {
		// no input maps:
		if (isTerminalType(inputType)) {
			return new JsonPrimitive(ClassUtils.getSimpleName(inputType));
		} else {
			boolean isCollection = Collection.class.isAssignableFrom(inputType);
			if (isCollection) {
				try {
					inputType = (Class<?>) ((ParameterizedType) genericType).getActualTypeArguments()[0];
				} catch (Exception e) {
					inputType = Object.class;
				}
			}
			Collection<Method> fields = AssociationPath.listMethods(inputType, VO_METHOD_TRANSFILTER);
			if (isTerminalType(inputType) || fields.size() == 0) {
				return markMapCollection(new JsonPrimitive(ClassUtils.getSimpleName(inputType)), null, false, isCollection);
			} else {
				return markMapCollection(createVONode(fields, true), null, false, isCollection);
			}
		}
	}

	private static JsonObject createVONode(Collection<Method> fields, boolean recursive) throws Exception {
		JsonObject voNode = new JsonObject();
		if (fields != null) {
			Iterator<Method> it = fields.iterator();
			while (it.hasNext()) {
				Method field = it.next();
				String fieldName = VO_METHOD_TRANSFILTER.transform(field.getName());
				if (recursive) {
					voNode.add(fieldName, createVOReturnTypeNode(field.getReturnType(), field.getGenericReturnType()));
				} else {
					voNode.addProperty(fieldName, ClassUtils.getSimpleName(field.getReturnType()));
				}
			}
		}
		return voNode;
	}

	private static JsonElement createVOReturnTypeNode(Class<?> returnType, Type genericReturnType) throws Exception {
		if (isTerminalType(returnType)) {
			return new JsonPrimitive(ClassUtils.getSimpleName(returnType));
		} else {
			boolean isCollection = Collection.class.isAssignableFrom(returnType);
			boolean isMap = Map.class.isAssignableFrom(returnType);
			Class<?> mapKeyType = null;
			if (isCollection) {
				try {
					returnType = (Class<?>) ((ParameterizedType) genericReturnType).getActualTypeArguments()[0];
				} catch (Exception e) {
					returnType = Object.class;
				}
			} else if (isMap) {
				try {
					mapKeyType = (Class<?>) ((ParameterizedType) genericReturnType).getActualTypeArguments()[0];
				} catch (Exception e) {
					mapKeyType = Object.class;
				}
				try {
					returnType = (Class<?>) ((ParameterizedType) genericReturnType).getActualTypeArguments()[1];
				} catch (Exception e) {
					returnType = Object.class;
				}
			}
			if (NoShortcutSerializationWrapper.class.isAssignableFrom(returnType)) {
				try {
					returnType = (Class<?>) ((ParameterizedType) genericReturnType).getActualTypeArguments()[0];
				} catch (Exception e) {
					returnType = Object.class;
				}
			}
			Collection<Method> fields = AssociationPath.listMethods(returnType, VO_METHOD_TRANSFILTER);
			if (isTerminalType(returnType) || fields.size() == 0) {
				return markMapCollection(new JsonPrimitive(ClassUtils.getSimpleName(returnType)), mapKeyType, isMap, isCollection);
			} else {
				return markMapCollection(createVONode(fields, false), mapKeyType, isMap, isCollection);
			}
		}
	}

	private static String getBasePath(HttpServletRequest request) {
		StringBuffer url = new StringBuffer(WebUtil.getBaseUrl(request));
		url.append(request.getContextPath()).append("/").append(WebUtil.REST_API_PATH);
		// return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/" + WebUtil.REST_API_PATH;
		return url.toString();
	}

	public static JsonObject getResourceIndexNode(Class<?> resourceClass, HttpServletRequest request) throws Exception { // String basePath) throws Exception {
		String basePath = getBasePath(request);
		AbstractResource resource = IntrospectionModeller.createResource(resourceClass);
		JsonObject classNode = new JsonObject();
		classNode.addProperty(JS_CLASS_NAME_FIELD, resourceClass.getName());
		classNode.addProperty(JS_CLASS_PATH_FIELD, resource.getPath().getValue());
		JsonObject resourcesNode = new JsonObject();
		String uriPrefix = resource.getPath().getValue();
		List<AbstractResourceMethod> resourceMethods = resource.getResourceMethods();
		Collections.sort(resourceMethods, RESOURCE_METHOD_COMPARATOR);
		Iterator<AbstractResourceMethod> resourceMethodsIt = resourceMethods.iterator();
		while (resourceMethodsIt.hasNext()) {
			AbstractResourceMethod method = resourceMethodsIt.next();
			addResource(resourcesNode, uriPrefix, method, joinUri(basePath, uriPrefix));
		}
		List<AbstractSubResourceMethod> subResourceMethods = resource.getSubResourceMethods();
		Collections.sort(subResourceMethods, SUB_RESOURCE_METHOD_COMPARATOR);
		Iterator<AbstractSubResourceMethod> subResourceMethodsIt = subResourceMethods.iterator();
		while (subResourceMethodsIt.hasNext()) {
			AbstractSubResourceMethod method = subResourceMethodsIt.next();
			String uri = uriPrefix + "/" + method.getPath().getValue();
			addResource(resourcesNode, uri, method, joinUri(basePath, uri));
		}
		classNode.add(JS_RESOURCES_FIELD, resourcesNode);
		return classNode;
	}

	public static JsonObject getResourceMethodIndexNode(String pathPrefix, Collection<Method> methods, ArgsUriPart args, boolean addPsfQueryParams) throws Exception {
		JsonObject indexNode = new JsonObject();
		if (methods != null) {
			Iterator<Method> it = methods.iterator();
			while (it.hasNext()) {
				Method field = it.next();
				JsonObject methodNode = new JsonObject();
				String resource = joinUri(pathPrefix, args.getMethodTransfilter().transform(field.getName()));
				Set<NamedParameter> queryParams = args.getNamedParameters(field.getName(), true);
				if (addPsfQueryParams) {
					queryParams.addAll(PSFUriPart.SLURPED_NAMED_QUERY_PARAMETERS);
				}
				methodNode.add(JS_QUERY_PARAMS_FIELD, createQueryParameterNode(queryParams));
				methodNode.add(JS_OUT_VO_FIELD, createVOReturnTypeNode(field.getReturnType(), field.getGenericReturnType()));
				indexNode.add(resource, methodNode);
			}
		}
		return indexNode;
	}

	private static boolean isAnnotatedResourceClass(Class<?> resourceClass) {
		if (resourceClass.isAnnotationPresent(Path.class)) {
			return true;
		}
		Class<?>[] interfaces = resourceClass.getInterfaces();
		for (int i = 0; i < interfaces.length; i++) {
			if (interfaces[i].isAnnotationPresent(Path.class)) {
				return true;
			}
		}
		return false;
	}

	private static boolean isTerminalType(Class<?> type) {
		return ClassUtils.isPrimitiveOrWrapper(type)
				|| type.isEnum()
				|| Date.class.isAssignableFrom(type)
				|| String.class.equals(type)
				|| Object.class.equals(type);
	}

	private static String joinUri(String basePath, String apiPath) throws MalformedURLException {
		return (!basePath.endsWith("/") ? basePath + "/" : basePath) + (apiPath.startsWith("/") ? apiPath.substring(1) : apiPath);
	}

	private static JsonElement markMapCollection(JsonElement voNode, Class<?> mapKeyType, boolean isMap, boolean isCollection) {
		if (isMap) {
			JsonObject voMapNode = new JsonObject();
			voMapNode.add(ClassUtils.getSimpleName(mapKeyType), voNode);
			voNode = voMapNode;
		}
		if (isCollection) {
			JsonArray voCollectionNode = new JsonArray();
			voCollectionNode.add(voNode);
			return voCollectionNode;
		}
		return voNode;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ResourceIndex index(@Context Application application,
			@Context HttpServletRequest request) throws Exception {
		JsonObject rootNode = new JsonObject();
		JsonArray classesNode = new JsonArray();
		rootNode.addProperty(JS_TITLE_FIELD, Settings.getString(SettingCodes.API_TITLE, Bundle.SETTINGS, DefaultSettings.API_TITLE));
		rootNode.addProperty(JS_INSTANCE_FIELD, WebUtil.getInstanceName());
		rootNode.addProperty(JS_VERSION_FIELD, Settings.getString(SettingCodes.API_VERSION, Bundle.SETTINGS, DefaultSettings.API_VERSION));
		rootNode.addProperty(JS_JVM_FIELD, System.getProperty("java.version"));
		rootNode.addProperty(JS_REALM_FIELD, Settings.getString(SettingCodes.API_REALM, Bundle.SETTINGS, DefaultSettings.API_REALM));
		AnnouncementVO announcement = WebUtil.getServiceLocator().getToolsService().getAnnouncement();
		if (announcement != null) {
			rootNode.addProperty(JS_ANNOUNCEMENT_FIELD, announcement.getMessage());
		}
		rootNode.add(JS_CLASSES_FIELD, classesNode);
		ArrayList<Class<?>> classes = new ArrayList<>(application.getClasses());
		Collections.sort(classes, RESOURCE_CLASS_COMPARATOR);
		Iterator<Class<?>> classesIt = classes.iterator();
		while (classesIt.hasNext()) {
			Class<?> resourceClass = classesIt.next();
			if (isAnnotatedResourceClass(resourceClass)) {
				classesNode.add(getResourceIndexNode(resourceClass, request)); // basePath));
			}
		}
		return new ResourceIndex(rootNode);
	}
}