package org.phoenixctms.ctsms.web.jersey.resource.shared;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.service.shared.ToolsService;
import org.phoenixctms.ctsms.util.AssociationPath;
import org.phoenixctms.ctsms.util.MethodTransfilter;
import org.phoenixctms.ctsms.vo.AnnouncementVO;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.FileStreamOutVO;
import org.phoenixctms.ctsms.vo.PasswordPolicyVO;
import org.phoenixctms.ctsms.vo.UserOutVO;
import org.phoenixctms.ctsms.web.jersey.index.CompleteIndex;
import org.phoenixctms.ctsms.web.jersey.index.IndexBase;
import org.phoenixctms.ctsms.web.jersey.index.ResourceIndex;
import org.phoenixctms.ctsms.web.jersey.resource.ArgsUriPart;
import org.phoenixctms.ctsms.web.jersey.resource.ResourceUtils;
import org.phoenixctms.ctsms.web.jersey.wrapper.AddUserWrapper;
import org.phoenixctms.ctsms.web.jersey.wrapper.NoShortcutSerializationWrapper;
import org.phoenixctms.ctsms.web.util.WebUtil;

import com.google.gson.JsonElement;
import com.sun.jersey.api.NotFoundException;

import io.swagger.annotations.Api;

@Api(value="shared")
@Path("/tools")
public final class ToolsResource {

	private final static Pattern COMPLETE_METHOD_NAME_REGEXP = Pattern.compile("^complete");
	private static final MethodTransfilter COMPLETE_METHOD_NAME_TRANSFORMER = new MethodTransfilter() {

		@Override
		public boolean include(Method method) {
			return COMPLETE_METHOD_NAME_REGEXP.matcher(method.getName()).lookingAt();
		};

		@Override
		public String transform(String methodName) {
			if (methodName != null && methodName.length() > 0) {
				return COMPLETE_METHOD_NAME_REGEXP.matcher(methodName).replaceAll("").toLowerCase();
			}
			return methodName;
		}
	};
	private final static Class<?> SERVICE_INTERFACE = ToolsService.class;
	public final static CompleteIndex COMPLETE_INDEX = new CompleteIndex(getCompleteIndexNode(
			ResourceUtils.getMethodPath(ToolsResource.class, "complete").replaceFirst("/\\{resource\\}", ""), // "completeIndex"),
			getArgsUriPart("")));

	private static ArgsUriPart getArgsUriPart(String resource) {
		ArgsUriPart args = new ArgsUriPart(SERVICE_INTERFACE, resource, COMPLETE_METHOD_NAME_TRANSFORMER);
		return args;
	}

	private static ArgsUriPart getArgsUriPart(String resource, AuthenticationVO auth) {
		ArgsUriPart args = new ArgsUriPart(SERVICE_INTERFACE, resource, COMPLETE_METHOD_NAME_TRANSFORMER);
		args.getOverrides().put("auth", auth);
		return args;
	}

	private static JsonElement getCompleteIndexNode(String pathPrefix, ArgsUriPart args) {
		try {
			return IndexResource.getResourceMethodIndexNode(pathPrefix,
					AssociationPath.listMethods(SERVICE_INTERFACE, COMPLETE_METHOD_NAME_TRANSFORMER), args, false);
		} catch (Exception e) {
			return IndexBase.EMPTY_INDEX_NODE;
		}
	}

	@Context
	AuthenticationVO auth;

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("user")
	public NoShortcutSerializationWrapper<UserOutVO> addUser(AddUserWrapper in) throws Exception {
		return new NoShortcutSerializationWrapper<UserOutVO>(
				WebUtil.getServiceLocator().getToolsService().addUser(in.getUser(), in.getPassword(), in.getPlainDepartmentPassword()));
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("complete/{resource}")
	public Collection complete(@PathParam("resource") String resource, @Context UriInfo uriInfo) throws Throwable {
		if (AssociationPath.methodExists(ToolsService.class, resource, COMPLETE_METHOD_NAME_TRANSFORMER)) {
			ArgsUriPart args = getArgsUriPart(resource, auth); // );
			try {
				return (Collection<?>) AssociationPath.invoke(resource,
						WebUtil.getServiceLocator().getToolsService(),
						ToolsService.class,
						COMPLETE_METHOD_NAME_TRANSFORMER, true, args.getArgs(uriInfo));
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
	@Path("complete")
	public CompleteIndex completeIndex() throws Exception {
		return COMPLETE_INDEX;
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("announcement")
	public AnnouncementVO getAnnouncement() throws AuthorisationException, ServiceException, AuthenticationException {
		return WebUtil.getServiceLocator().getToolsService().getAnnouncement();
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("passwordpolicy")
	public PasswordPolicyVO getPasswordPolicy() throws AuthorisationException, ServiceException, AuthenticationException {
		return WebUtil.getServiceLocator().getToolsService().getPasswordPolicy(auth);
	}

	@GET
	@Path("file/{id}")
	public Response getPublicFileStream(@PathParam("id") Long id) throws AuthenticationException, AuthorisationException, ServiceException {
		FileStreamOutVO stream = WebUtil.getServiceLocator().getToolsService().getPublicFileStream(id);
		ResponseBuilder response = javax.ws.rs.core.Response.ok(stream.getStream(), stream.getContentType().getMimeType());
		response.header(HttpHeaders.CONTENT_LENGTH, stream.getSize());
		return response.build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ResourceIndex index(@Context Application application,
			@Context HttpServletRequest request) throws Exception {
		return new ResourceIndex(IndexResource.getResourceIndexNode(ToolsResource.class, request)); // basePath));
	}
}
