package org.phoenixctms.ctsms.web.jersey.resource.massmail;

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
import org.phoenixctms.ctsms.service.massmail.MassMailService;
import org.phoenixctms.ctsms.util.MethodTransfilter;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.FileOutVO;
import org.phoenixctms.ctsms.vo.FilePDFVO;
import org.phoenixctms.ctsms.vo.JournalEntryOutVO;
import org.phoenixctms.ctsms.vo.MassMailInVO;
import org.phoenixctms.ctsms.vo.MassMailOutVO;
import org.phoenixctms.ctsms.web.jersey.index.MassMailListIndex;
import org.phoenixctms.ctsms.web.jersey.resource.PSFUriPart;
import org.phoenixctms.ctsms.web.jersey.resource.Page;
import org.phoenixctms.ctsms.web.jersey.resource.ResourceUtils;
import org.phoenixctms.ctsms.web.jersey.resource.ServiceResourceBase;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "massmail")
@Path("/massmail")
public final class MassMailResource extends ServiceResourceBase {

	private final static FileModule fileModule = FileModule.MASS_MAIL_DOCUMENT;
	private final static JournalModule journalModule = JournalModule.MASS_MAIL_JOURNAL;
	private final static Class<?> SERVICE_INTERFACE = MassMailService.class;
	private final static String ROOT_ENTITY_ID_METHOD_PARAM_NAME = "massMailId";
	private static final MethodTransfilter GET_LIST_METHOD_NAME_TRANSFORMER = getGetListMethodNameTransformer(ROOT_ENTITY_ID_METHOD_PARAM_NAME, MassMailOutVO.class);
	public final static MassMailListIndex LIST_INDEX = new MassMailListIndex(getListIndexNode(
			ResourceUtils.getMethodPath(MassMailResource.class, "list").replaceFirst("/\\{resource\\}", ""), SERVICE_INTERFACE, GET_LIST_METHOD_NAME_TRANSFORMER,
			getArgsUriPart(SERVICE_INTERFACE, "", new AuthenticationVO(), ROOT_ENTITY_ID_METHOD_PARAM_NAME, GET_LIST_METHOD_NAME_TRANSFORMER, 0l, new PSFUriPart())));
	@Context
	AuthenticationVO auth;

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public MassMailOutVO addMassMail(MassMailInVO in) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getMassMailService().addMassMail(auth, in);
	}

	@GET
	@Path("{id}/files/pdf")
	public Response aggregatePDFFiles(@PathParam("id") Long id, @Context UriInfo uriInfo) throws AuthenticationException, AuthorisationException, ServiceException {
		FilePDFVO vo = WebUtil.getServiceLocator().getFileService().aggregatePDFFiles(auth, fileModule, id, null, false, null, null, new PSFUriPart(uriInfo));
		// http://stackoverflow.com/questions/9204287/how-to-return-a-png-image-from-jersey-rest-service-method-to-the-browser
		// non-streamed
		ResponseBuilder response = Response.ok(vo.getDocumentDatas(), vo.getContentType().getMimeType());
		response.header(HttpHeaders.CONTENT_LENGTH, vo.getSize());
		return response.build();
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}/files/pdf/head")
	public FilePDFVO aggregatePDFFilesHead(@PathParam("id") Long id, @Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		FilePDFVO result = WebUtil.getServiceLocator().getFileService().aggregatePDFFiles(auth, fileModule, id, null, false, null, null, new PSFUriPart(uriInfo));
		result.setDocumentDatas(null);
		return result;
	}

	@DELETE
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}")
	public MassMailOutVO deleteMassMail(@PathParam("id") Long id, @QueryParam("force") Boolean force, @QueryParam("reason") String reason)
			throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getMassMailService()
				.deleteMassMail(auth, id, Settings.getBoolean(SettingCodes.MASS_MAIL_DEFERRED_DELETE, Bundle.SETTINGS, DefaultSettings.MASS_MAIL_DEFERRED_DELETE),
						force != null ? force : false, reason);
	}

	@Override
	protected AuthenticationVO getAuth() {
		return auth;
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}/files/folders")
	public Page<String> getFileFolders(@PathParam("id") Long id, @Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<String>(WebUtil.getServiceLocator().getFileService().getFileFolders(auth, fileModule, id, null, false, null, null, psf = new PSFUriPart(uriInfo)), psf);
	}

	@Override
	protected FileModule getFileModule() {
		return fileModule;
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}/files")
	public Page<FileOutVO> getFiles(@PathParam("id") Long id, @Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<FileOutVO>(WebUtil.getServiceLocator().getFileService().getFiles(auth, fileModule, id, null, false, null, null, psf = new PSFUriPart(uriInfo)), psf);
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

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}")
	public MassMailOutVO getMassMail(@PathParam("id") Long id) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getMassMailService().getMassMail(auth, id);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Page<MassMailOutVO> getMassMailList(@Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<MassMailOutVO>(WebUtil.getServiceLocator().getMassMailService().getMassMailList(auth, null, null, psf = new PSFUriPart(uriInfo)), psf);
	}

	@Override
	protected String getRootEntityIdMethodParamName() {
		return ROOT_ENTITY_ID_METHOD_PARAM_NAME;
	}

	@Override
	protected Object getService() {
		return WebUtil.getServiceLocator().getMassMailService();
	}

	@Override
	protected Class<?> getServiceInterface() {
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
	@ApiOperation(value = "list", hidden = true)
	public MassMailListIndex listIndex() throws Exception {
		return LIST_INDEX;
	}

	@PUT
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public MassMailOutVO updateMassMail(MassMailInVO in) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getMassMailService().updateMassMail(auth, in);
	}
}
