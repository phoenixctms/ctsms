package org.phoenixctms.ctsms.web.jersey.resource.shared;

import io.swagger.annotations.Api;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.phoenixctms.ctsms.service.shared.SelectionSetService;
import org.phoenixctms.ctsms.util.AssociationPath;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.MethodTransfilter;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.web.jersey.index.IndexBase;
import org.phoenixctms.ctsms.web.jersey.index.SelectionSetIndex;
import org.phoenixctms.ctsms.web.jersey.resource.ArgsUriPart;
import org.phoenixctms.ctsms.web.jersey.resource.ResourceUtils;
import org.phoenixctms.ctsms.web.util.WebUtil;

import com.google.gson.JsonElement;
import com.sun.jersey.api.NotFoundException;

@Api(value="shared")
@Path("/selectionset")
public final class SelectionSetResource {

	@Context
	AuthenticationVO auth;
	private static final MethodTransfilter SELECTION_SET_SERVICE_METHOD_NAME_TRANSFORMER = new MethodTransfilter() {

		@Override
		public boolean include(Method method) {
			Class<?>[] paramTypes = method.getParameterTypes();
			return (paramTypes != null && paramTypes.length >= 1 && AuthenticationVO.class.equals(paramTypes[0]));
		}

		@Override
		public String transform(String methodName) {
			if (methodName != null && methodName.length() > 0) {
				return CommonUtil.VO_GETTER_METHOD_NAME_REGEXP.matcher(methodName).replaceAll("").toLowerCase();
			}
			return methodName;
		}
	};
	private final static Class<?> SERVICE_INTERFACE = SelectionSetService.class;
	public final static SelectionSetIndex INDEX = new SelectionSetIndex(getIndexNode(
			ResourceUtils.getMethodPath(SelectionSetResource.class, "get").replaceFirst("/\\{resource\\}", ""), // "index"),
			getArgsUriPart("", new AuthenticationVO())));

	private static ArgsUriPart getArgsUriPart(String resource, AuthenticationVO auth) {
		ArgsUriPart args = new ArgsUriPart(SERVICE_INTERFACE, resource, SELECTION_SET_SERVICE_METHOD_NAME_TRANSFORMER);
		args.getOverrides().put("auth", auth);
		return args;
	}

	private static JsonElement getIndexNode(String pathPrefix, ArgsUriPart args) {
		try {
			return IndexResource.getResourceMethodIndexNode(pathPrefix,
					AssociationPath.listMethods(SERVICE_INTERFACE, SELECTION_SET_SERVICE_METHOD_NAME_TRANSFORMER), args, false);
		} catch (Exception e) {
			return IndexBase.EMPTY_INDEX_NODE;
		}
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{resource}")
	public Object get(@PathParam("resource") String resource, @Context UriInfo uriInfo) throws Throwable {
		if (AssociationPath.methodExists(SelectionSetService.class, resource, SELECTION_SET_SERVICE_METHOD_NAME_TRANSFORMER)) {
			ArgsUriPart args = getArgsUriPart(resource, auth);
			try {
				return AssociationPath.invoke(resource,
						WebUtil.getServiceLocator().getSelectionSetService(),
						SelectionSetService.class,
						SELECTION_SET_SERVICE_METHOD_NAME_TRANSFORMER, true, args.getArgs(uriInfo));
			} catch (InvocationTargetException e) {
				throw e.getCause();
			} catch (Exception e) {
				throw e;
			}
		}
		throw new NotFoundException();
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public SelectionSetIndex index() throws Exception {
		return INDEX;
	}
}
