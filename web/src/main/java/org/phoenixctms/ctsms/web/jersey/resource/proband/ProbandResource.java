package org.phoenixctms.ctsms.web.jersey.resource.proband;

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
import org.phoenixctms.ctsms.service.proband.ProbandService;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.MethodTransfilter;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.FileOutVO;
import org.phoenixctms.ctsms.vo.FilePDFVO;
import org.phoenixctms.ctsms.vo.InquiriesPDFVO;
import org.phoenixctms.ctsms.vo.InquiryValueJsonVO;
import org.phoenixctms.ctsms.vo.InquiryValueOutVO;
import org.phoenixctms.ctsms.vo.InquiryValuesOutVO;
import org.phoenixctms.ctsms.vo.JournalEntryOutVO;
import org.phoenixctms.ctsms.vo.ProbandImageInVO;
import org.phoenixctms.ctsms.vo.ProbandImageOutVO;
import org.phoenixctms.ctsms.vo.ProbandInVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.web.jersey.index.ProbandListIndex;
import org.phoenixctms.ctsms.web.jersey.resource.PSFUriPart;
import org.phoenixctms.ctsms.web.jersey.resource.Page;
import org.phoenixctms.ctsms.web.jersey.resource.ResourceUtils;
import org.phoenixctms.ctsms.web.jersey.resource.ServiceResourceBase;
import org.phoenixctms.ctsms.web.jersey.wrapper.JsValuesOutVOPage;
import org.phoenixctms.ctsms.web.jersey.wrapper.SetProbandCategoryWrapper;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataParam;

import io.swagger.annotations.Api;

@Api(value="proband")
@Path("/proband")
public final class ProbandResource extends ServiceResourceBase {

	private final static FileModule fileModule = FileModule.PROBAND_DOCUMENT;
	private final static JournalModule journalModule = JournalModule.PROBAND_JOURNAL;
	private final static Class<?> SERVICE_INTERFACE = ProbandService.class;
	private final static String ROOT_ENTITY_ID_METHOD_PARAM_NAME = "probandId";
	private static final MethodTransfilter GET_LIST_METHOD_NAME_TRANSFORMER = getGetListMethodNameTransformer(ROOT_ENTITY_ID_METHOD_PARAM_NAME, ProbandOutVO.class);
	public final static ProbandListIndex LIST_INDEX = new ProbandListIndex(getListIndexNode(
			ResourceUtils.getMethodPath(ProbandResource.class, "list").replaceFirst("/\\{resource\\}", ""), // "listIndex"),
			SERVICE_INTERFACE, GET_LIST_METHOD_NAME_TRANSFORMER,
			getArgsUriPart(SERVICE_INTERFACE, "", new AuthenticationVO(), ROOT_ENTITY_ID_METHOD_PARAM_NAME, GET_LIST_METHOD_NAME_TRANSFORMER, 0l, new PSFUriPart())));
	@Context
	AuthenticationVO auth;

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public ProbandOutVO addProband(ProbandInVO in) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getProbandService().addProband(auth, in,
				Settings.getIntNullable(SettingCodes.API_GRAPH_MAX_PROBAND_INSTANCES, Bundle.SETTINGS, DefaultSettings.API_GRAPH_MAX_PROBAND_INSTANCES),
				Settings.getIntNullable(SettingCodes.API_GRAPH_MAX_PROBAND_PARENTS_DEPTH, Bundle.SETTINGS, DefaultSettings.API_GRAPH_MAX_PROBAND_PARENTS_DEPTH),
				Settings.getIntNullable(SettingCodes.API_GRAPH_MAX_PROBAND_CHILDREN_DEPTH, Bundle.SETTINGS, DefaultSettings.API_GRAPH_MAX_PROBAND_CHILDREN_DEPTH));
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

	// @HEAD
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
	public ProbandOutVO deleteProband(@PathParam("id") Long id, @QueryParam("reason") String reason) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getProbandService()
				.deleteProband(auth, id, Settings.getBoolean(SettingCodes.PROBAND_DEFERRED_DELETE, Bundle.SETTINGS, DefaultSettings.PROBAND_DEFERRED_DELETE), false, reason,
						Settings.getIntNullable(SettingCodes.API_GRAPH_MAX_PROBAND_INSTANCES, Bundle.SETTINGS, DefaultSettings.API_GRAPH_MAX_PROBAND_INSTANCES),
						Settings.getIntNullable(SettingCodes.API_GRAPH_MAX_PROBAND_PARENTS_DEPTH, Bundle.SETTINGS, DefaultSettings.API_GRAPH_MAX_PROBAND_PARENTS_DEPTH),
						Settings.getIntNullable(SettingCodes.API_GRAPH_MAX_PROBAND_CHILDREN_DEPTH, Bundle.SETTINGS, DefaultSettings.API_GRAPH_MAX_PROBAND_CHILDREN_DEPTH));
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
	@Path("{id}/inquiryvalue/{inquiryId}")
	public JsValuesOutVOPage<InquiryValueOutVO, InquiryValueJsonVO> getInquiryValue(@PathParam("id") Long id, @PathParam("inquiryId") Long inquiryId)
			throws AuthenticationException, AuthorisationException, ServiceException {
		InquiryValuesOutVO values = WebUtil.getServiceLocator().getProbandService().getInquiryValue(auth, id, inquiryId);
		return new JsValuesOutVOPage<InquiryValueOutVO, InquiryValueJsonVO>(values.getPageValues(), values.getJsValues(), null);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}/inquiryvalues/{trialId}")
	public JsValuesOutVOPage<InquiryValueOutVO, InquiryValueJsonVO> getInquiryValues(@PathParam("id") Long id, @PathParam("trialId") Long trialId,
			@QueryParam("active") Boolean active, @QueryParam("active_signup") Boolean activeSignup, @QueryParam("sort") Boolean sort,
			@QueryParam("load_all_js_values") Boolean loadAllJsValues, @Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf = new PSFUriPart(uriInfo, "active", "active_signup", "sort", "load_all_js_values");
		InquiryValuesOutVO values = WebUtil.getServiceLocator().getProbandService().getInquiryValues(auth, trialId, active, activeSignup, id, sort, loadAllJsValues, psf);
		return new JsValuesOutVOPage<InquiryValueOutVO, InquiryValueJsonVO>(values.getPageValues(), values.getJsValues(), psf);
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
	public ProbandOutVO getProband(@PathParam("id") Long id) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getProbandService().getProband(auth, id,
				Settings.getIntNullable(SettingCodes.API_GRAPH_MAX_PROBAND_INSTANCES, Bundle.SETTINGS, DefaultSettings.API_GRAPH_MAX_PROBAND_INSTANCES),
				Settings.getIntNullable(SettingCodes.API_GRAPH_MAX_PROBAND_PARENTS_DEPTH, Bundle.SETTINGS, DefaultSettings.API_GRAPH_MAX_PROBAND_PARENTS_DEPTH),
				Settings.getIntNullable(SettingCodes.API_GRAPH_MAX_PROBAND_CHILDREN_DEPTH, Bundle.SETTINGS, DefaultSettings.API_GRAPH_MAX_PROBAND_CHILDREN_DEPTH));
	}

	@GET
	@Path("{id}/image")
	public Response getProbandImage(@PathParam("id") Long id) throws AuthenticationException, AuthorisationException, ServiceException {
		ProbandImageOutVO out = WebUtil.getServiceLocator().getProbandService().getProbandImage(auth, id);
		ResponseBuilder response = javax.ws.rs.core.Response.ok(new ByteArrayInputStream(out.getDatas()), out.getContentType().getMimeType());
		response.header(HttpHeaders.CONTENT_LENGTH, out.getFileSize());
		return response.build();
	}

	// @HEAD
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}/image/head")
	public ProbandImageOutVO getProbandImageHead(@PathParam("id") Long id) throws AuthenticationException, AuthorisationException, ServiceException {
		ProbandImageOutVO out = WebUtil.getServiceLocator().getProbandService().getProbandImage(auth, id);
		out.setDatas(null);
		return out;
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Page<ProbandOutVO> getProbandList(@Context UriInfo uriInfo, @QueryParam("department_id") Long departmentId) // , @QueryParam("proband_id") Long probandId)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf; // = new PSFUriPart(uriInfo,"department_id","proband_id");
		return new Page<ProbandOutVO>(WebUtil.getServiceLocator().getProbandService()
				.getProbandList(auth, null, departmentId, ResourceUtils.LIST_GRAPH_MAX_PROBAND_INSTANCES, psf = new PSFUriPart(uriInfo, "department_id")), psf);
	}

	@Override
	protected String getRootEntityIdMethodParamName() {
		return ROOT_ENTITY_ID_METHOD_PARAM_NAME;
	}

	@Override
	protected Object getService() {
		return WebUtil.getServiceLocator().getProbandService();
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
	public ProbandListIndex listIndex() throws Exception {
		return LIST_INDEX;
	}

	@GET
	@Path("{id}/inquiryvalues/pdf")
	public Response renderInquiries(@PathParam("id") Long id, @QueryParam("active") Boolean active, @QueryParam("active_signup") Boolean activeSignup,
			@QueryParam("blank") boolean blank) throws AuthenticationException,
			AuthorisationException, ServiceException {
		InquiriesPDFVO vo = WebUtil.getServiceLocator().getProbandService().renderInquiries(auth, null, id, active, activeSignup, blank);
		// http://stackoverflow.com/questions/9204287/how-to-return-a-png-image-from-jersey-rest-service-method-to-the-browser
		// non-streamed
		ResponseBuilder response = Response.ok(vo.getDocumentDatas(), vo.getContentType().getMimeType());
		response.header(HttpHeaders.CONTENT_LENGTH, vo.getSize());
		return response.build();
	}

	@GET
	@Path("{id}/inquiryvalues/{trialId}/pdf")
	public Response renderInquiries(@PathParam("id") Long id, @PathParam("trialId") Long trialId, @QueryParam("active") Boolean active,
			@QueryParam("active_signup") Boolean activeSignup, @QueryParam("blank") boolean blank) throws AuthenticationException,
			AuthorisationException, ServiceException {
		InquiriesPDFVO vo = WebUtil.getServiceLocator().getProbandService().renderInquiries(auth, trialId, id, active, activeSignup, blank);
		// http://stackoverflow.com/questions/9204287/how-to-return-a-png-image-from-jersey-rest-service-method-to-the-browser
		// non-streamed
		ResponseBuilder response = Response.ok(vo.getDocumentDatas(), vo.getContentType().getMimeType());
		response.header(HttpHeaders.CONTENT_LENGTH, vo.getSize());
		return response.build();
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}/inquiryvalues/pdf/head")
	public InquiriesPDFVO renderInquiriesHead(@PathParam("id") Long id, @QueryParam("active") Boolean active, @QueryParam("active_signup") Boolean activeSignup,
			@QueryParam("blank") Boolean blank)
			throws AuthenticationException, AuthorisationException, ServiceException {
		InquiriesPDFVO result = WebUtil.getServiceLocator().getProbandService().renderInquiries(auth, null, id, active, activeSignup, blank);
		result.setDocumentDatas(null);
		return result;
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}/inquiryvalues/{trialId}/pdf/head")
	public InquiriesPDFVO renderInquiriesHead(@PathParam("id") Long id, @PathParam("trialId") Long trialId, @QueryParam("active") Boolean active,
			@QueryParam("active_signup") Boolean activeSignup, @QueryParam("blank") Boolean blank)
			throws AuthenticationException, AuthorisationException, ServiceException {
		InquiriesPDFVO result = WebUtil.getServiceLocator().getProbandService().renderInquiries(auth, trialId, id, active, activeSignup, blank);
		result.setDocumentDatas(null);
		return result;
	}

	@GET
	@Path("{id}/inquiryvalues/signuppdf")
	public Response renderInquiriesSignup(@PathParam("id") Long id, @QueryParam("department_id") Long departmentId, @QueryParam("active_signup") Boolean activeSignup)
			throws AuthenticationException,
			AuthorisationException, ServiceException {
		InquiriesPDFVO vo = WebUtil.getServiceLocator().getProbandService().renderInquiriesSignup(auth, departmentId, id, activeSignup);
		// http://stackoverflow.com/questions/9204287/how-to-return-a-png-image-from-jersey-rest-service-method-to-the-browser
		// non-streamed
		ResponseBuilder response = Response.ok(vo.getDocumentDatas(), vo.getContentType().getMimeType());
		response.header(HttpHeaders.CONTENT_LENGTH, vo.getSize());
		return response.build();
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}/inquiryvalues/signuppdf/head")
	public InquiriesPDFVO renderInquiriesSignupHead(@PathParam("id") Long id, @QueryParam("department_id") Long departmentId, @QueryParam("active_signup") Boolean activeSignup)
			throws AuthenticationException,
			AuthorisationException, ServiceException {
		InquiriesPDFVO result = WebUtil.getServiceLocator().getProbandService().renderInquiriesSignup(auth, departmentId, id, activeSignup);
		result.setDocumentDatas(null);
		return result;
	}

	@PUT
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({ MediaType.APPLICATION_JSON })
	public ProbandImageOutVO setProbandImage(@FormDataParam("json") ProbandImageInVO in,
			@FormDataParam("data") FormDataBodyPart content,
			@FormDataParam("data") FormDataContentDisposition contentDisposition,
			@FormDataParam("data") final InputStream input) throws Exception {
		in.setDatas(CommonUtil.inputStreamToByteArray(input));
		in.setMimeType(content.getMediaType().toString());
		in.setFileName(contentDisposition.getFileName());
		return WebUtil.getServiceLocator().getProbandService().setProbandImage(auth, in);
	}

	@PUT
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public ProbandOutVO updateProband(ProbandInVO in) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getProbandService().updateProband(auth, in,
				Settings.getIntNullable(SettingCodes.API_GRAPH_MAX_PROBAND_INSTANCES, Bundle.SETTINGS, DefaultSettings.API_GRAPH_MAX_PROBAND_INSTANCES),
				Settings.getIntNullable(SettingCodes.API_GRAPH_MAX_PROBAND_PARENTS_DEPTH, Bundle.SETTINGS, DefaultSettings.API_GRAPH_MAX_PROBAND_PARENTS_DEPTH),
				Settings.getIntNullable(SettingCodes.API_GRAPH_MAX_PROBAND_CHILDREN_DEPTH, Bundle.SETTINGS, DefaultSettings.API_GRAPH_MAX_PROBAND_CHILDREN_DEPTH));
	}

	@PUT
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}/category")
	public ProbandOutVO updateProbandCategory(@PathParam("id") Long id, SetProbandCategoryWrapper in)
			throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getProbandService().updateProbandCategory(auth, id, in.getVersion(), in.getCategoryId(), in.getComment());
	}
}
