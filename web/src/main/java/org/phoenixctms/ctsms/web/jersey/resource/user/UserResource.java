package org.phoenixctms.ctsms.web.jersey.resource.user;

import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.phoenixctms.ctsms.enumeration.FileModule;
import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.service.user.UserService;
import org.phoenixctms.ctsms.util.MethodTransfilter;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.JournalEntryOutVO;
import org.phoenixctms.ctsms.vo.UserOutVO;
import org.phoenixctms.ctsms.web.jersey.index.UserListIndex;
import org.phoenixctms.ctsms.web.jersey.resource.PSFUriPart;
import org.phoenixctms.ctsms.web.jersey.resource.Page;
import org.phoenixctms.ctsms.web.jersey.resource.ResourceUtils;
import org.phoenixctms.ctsms.web.jersey.resource.ServiceResourceBase;
import org.phoenixctms.ctsms.web.jersey.wrapper.AddUserWrapper;
import org.phoenixctms.ctsms.web.jersey.wrapper.NoShortcutSerializationWrapper;
import org.phoenixctms.ctsms.web.jersey.wrapper.UpdateUserWrapper;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "user")
@Path("/user")
public class UserResource extends ServiceResourceBase {

	private final static Integer MAX_GRAPH_USER_INSTANCES = 2;
	private final static JournalModule journalModule = JournalModule.USER_JOURNAL;
	private final static Class<?> SERVICE_INTERFACE = UserService.class;
	private final static String ROOT_ENTITY_ID_METHOD_PARAM_NAME = "userId";
	private static final MethodTransfilter GET_LIST_METHOD_NAME_TRANSFORMER = getGetListMethodNameTransformer(ROOT_ENTITY_ID_METHOD_PARAM_NAME, UserOutVO.class);
	public final static UserListIndex LIST_INDEX = new UserListIndex(
			getListIndexNode(ResourceUtils.getMethodPath(UserResource.class, "list").replaceFirst("/\\{resource\\}", ""), SERVICE_INTERFACE, GET_LIST_METHOD_NAME_TRANSFORMER,
					getArgsUriPart(SERVICE_INTERFACE, "", new AuthenticationVO(), ROOT_ENTITY_ID_METHOD_PARAM_NAME, GET_LIST_METHOD_NAME_TRANSFORMER, 0l, new PSFUriPart())));
	@Context
	AuthenticationVO auth;

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public NoShortcutSerializationWrapper<UserOutVO> addUser(AddUserWrapper in) throws AuthenticationException, AuthorisationException, ServiceException {
		return new NoShortcutSerializationWrapper<UserOutVO>(
				WebUtil.getServiceLocator().getUserService().addUser(auth, in.getUser(), in.getPlainDepartmentPassword(), MAX_GRAPH_USER_INSTANCES));
	}

	@DELETE
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}")
	public NoShortcutSerializationWrapper<UserOutVO> deleteUser(@PathParam("id") Long id, @QueryParam("force") Boolean force, @QueryParam("reason") String reason)
			throws AuthenticationException, AuthorisationException, ServiceException {
		return new NoShortcutSerializationWrapper<UserOutVO>(WebUtil.getServiceLocator().getUserService()
				.deleteUser(auth, id, Settings.getBoolean(SettingCodes.USER_DEFERRED_DELETE, Bundle.SETTINGS, DefaultSettings.USER_DEFERRED_DELETE), force != null ? force : false,
						reason,
						MAX_GRAPH_USER_INSTANCES));
	}

	@Override
	protected AuthenticationVO getAuth() {
		return auth;
	}

	@Override
	protected FileModule getFileModule() {
		return null;
	}

	@Override
	protected MethodTransfilter getGetListMethodNameTransformer() {
		return GET_LIST_METHOD_NAME_TRANSFORMER;
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}/journal")
	public Page<JournalEntryOutVO> getJournal(@PathParam("id") Long id, @Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<JournalEntryOutVO>(WebUtil.getServiceLocator().getJournalService().getJournal(auth, journalModule, id, psf = new PSFUriPart(uriInfo)), psf);
	}

	@Override
	protected String getRootEntityIdMethodParamName() {
		return ROOT_ENTITY_ID_METHOD_PARAM_NAME;
	}

	@Override
	protected Object getService() {
		return WebUtil.getServiceLocator().getUserService();
	}

	@Override
	protected Class<?> getServiceInterface() {
		return SERVICE_INTERFACE;
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}")
	public NoShortcutSerializationWrapper<UserOutVO> getUser(@PathParam("id") Long id) throws AuthenticationException, AuthorisationException, ServiceException {
		return new NoShortcutSerializationWrapper<UserOutVO>(WebUtil.getServiceLocator().getUserService().getUser(auth, id, MAX_GRAPH_USER_INSTANCES));
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Page<NoShortcutSerializationWrapper<UserOutVO>> getUserList(@Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		Collection result = WebUtil.getServiceLocator().getUserService().getUserList(auth, null, null, ResourceUtils.LIST_GRAPH_MAX_USER_INSTANCES, psf = new PSFUriPart(uriInfo));
		NoShortcutSerializationWrapper.transformVoCollection(result);
		return new Page<NoShortcutSerializationWrapper<UserOutVO>>(result, psf);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}/list/{resource}")
	public Page list(@PathParam("id") Long id, @PathParam("resource") String resource, @Context UriInfo uriInfo) throws Throwable {
		return list(auth, id, resource, uriInfo);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("list")
	@ApiOperation(value = "list", hidden = true)
	public UserListIndex listIndex() throws Exception {
		return LIST_INDEX;
	}

	@PUT
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public NoShortcutSerializationWrapper<UserOutVO> updateUser(UpdateUserWrapper in) throws AuthenticationException, AuthorisationException, ServiceException {
		return new NoShortcutSerializationWrapper<UserOutVO>(WebUtil.getServiceLocator().getUserService().updateUser(auth, in.getUser(), in.getPlainNewDepartmentPassword(),
				in.getPlainOldDepartmentPassword(), MAX_GRAPH_USER_INSTANCES));
	}
}
