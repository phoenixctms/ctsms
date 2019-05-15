package org.phoenixctms.ctsms.web.jersey.resource.inputfield;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

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
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import org.phoenixctms.ctsms.enumeration.FileModule;
import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.service.shared.InputFieldService;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.MethodTransfilter;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.InputFieldImageVO;
import org.phoenixctms.ctsms.vo.InputFieldInVO;
import org.phoenixctms.ctsms.vo.InputFieldOutVO;
import org.phoenixctms.ctsms.vo.JournalEntryOutVO;
import org.phoenixctms.ctsms.web.jersey.index.InputFieldListIndex;
import org.phoenixctms.ctsms.web.jersey.resource.PSFUriPart;
import org.phoenixctms.ctsms.web.jersey.resource.Page;
import org.phoenixctms.ctsms.web.jersey.resource.ResourceUtils;
import org.phoenixctms.ctsms.web.jersey.resource.ServiceResourceBase;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataParam;

import io.swagger.annotations.Api;

@Api(value="inputfield")
@Path("/inputfield")
public class InputFieldResource extends ServiceResourceBase {

	private final static JournalModule journalModule = JournalModule.INPUT_FIELD_JOURNAL;
	private final static Class SERVICE_INTERFACE = InputFieldService.class;
	private final static String ROOT_ENTITY_ID_METHOD_PARAM_NAME = "fieldId";
	private static final MethodTransfilter GET_LIST_METHOD_NAME_TRANSFORMER = getGetListMethodNameTransformer(ROOT_ENTITY_ID_METHOD_PARAM_NAME, InputFieldOutVO.class);
	public final static InputFieldListIndex LIST_INDEX = new InputFieldListIndex(getListIndexNode(
			ResourceUtils.getMethodPath(InputFieldResource.class, "list").replaceFirst("/\\{resource\\}", ""), // "listIndex"),
			SERVICE_INTERFACE, GET_LIST_METHOD_NAME_TRANSFORMER,
			getArgsUriPart(SERVICE_INTERFACE, "", new AuthenticationVO(), ROOT_ENTITY_ID_METHOD_PARAM_NAME, GET_LIST_METHOD_NAME_TRANSFORMER, 0l, new PSFUriPart())));
	@Context
	AuthenticationVO auth;

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public InputFieldOutVO addInputField(InputFieldInVO in) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getInputFieldService().addInputField(auth, in);
	}

	@DELETE
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}")
	public InputFieldOutVO deleteInputField(@PathParam("id") Long id, @QueryParam("reason") String reason)
			throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getInputFieldService()
				.deleteInputField(auth, id, Settings.getBoolean(SettingCodes.INPUT_FIELD_DEFERRED_DELETE, Bundle.SETTINGS, DefaultSettings.INPUT_FIELD_DEFERRED_DELETE), false,
						reason);
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
	@Path("{id}")
	public InputFieldOutVO getInputField(@PathParam("id") Long id) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getInputFieldService().getInputField(auth, id);
	}

	@GET
	@Path("{id}/image")
	public Response getInputFieldImage(@PathParam("id") Long id) throws AuthenticationException, AuthorisationException, ServiceException {
		InputFieldImageVO out = WebUtil.getServiceLocator().getToolsService().getInputFieldImage(id);
		ResponseBuilder response = javax.ws.rs.core.Response.ok(new ByteArrayInputStream(out.getDatas()), out.getContentType().getMimeType());
		response.header(HttpHeaders.CONTENT_LENGTH, out.getFileSize());
		return response.build();
	}

	// @HEAD
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}/image/head")
	public InputFieldImageVO getInputFieldImageHead(@PathParam("id") Long id) throws AuthenticationException, AuthorisationException, ServiceException {
		InputFieldImageVO out = WebUtil.getServiceLocator().getToolsService().getInputFieldImage(id);
		out.setDatas(null);
		return out;
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Page<InputFieldOutVO> getInputFieldList(@Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<InputFieldOutVO>(WebUtil.getServiceLocator().getInputFieldService().getInputFieldList(auth, null, psf = new PSFUriPart(uriInfo)), psf);
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
		return WebUtil.getServiceLocator().getInputFieldService();
	}

	@Override
	protected Class getServiceInterface() {
		return SERVICE_INTERFACE;
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
	public InputFieldListIndex listIndex() throws Exception {
		return LIST_INDEX;
	}

	@PUT
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public InputFieldOutVO updateInputField(InputFieldInVO in) throws AuthenticationException, AuthorisationException, ServiceException {
		InputFieldImageVO out = WebUtil.getServiceLocator().getToolsService().getInputFieldImage(in.getId());
		in.setDatas(out.getDatas());
		in.setMimeType(out.getContentType().getMimeType());
		in.setFileName(out.getFileName());
		return WebUtil.getServiceLocator().getInputFieldService().updateInputField(auth, in);
	}
}
