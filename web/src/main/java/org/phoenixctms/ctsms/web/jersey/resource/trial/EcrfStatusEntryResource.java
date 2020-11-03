package org.phoenixctms.ctsms.web.jersey.resource.trial;

import java.util.Collection;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
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

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.ECRFFieldValueJsonVO;
import org.phoenixctms.ctsms.vo.ECRFFieldValueOutVO;
import org.phoenixctms.ctsms.vo.ECRFFieldValuesOutVO;
import org.phoenixctms.ctsms.vo.ECRFPDFVO;
import org.phoenixctms.ctsms.vo.ECRFStatusEntryVO;
import org.phoenixctms.ctsms.web.jersey.resource.PSFUriPart;
import org.phoenixctms.ctsms.web.jersey.wrapper.JsValuesOutVOPage;
import org.phoenixctms.ctsms.web.util.WebUtil;

import io.swagger.annotations.Api;

@Api(value = "trial")
@Path("/ecrfstatusentry")
public class EcrfStatusEntryResource {

	@Context
	AuthenticationVO auth;

	@DELETE
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{listEntryId}/{ecrfId}/ecrffieldvalues")
	public Collection<ECRFFieldValueOutVO> clearEcrfFieldValues(@PathParam("listEntryId") Long listEntryId,
			@PathParam("ecrfId") Long ecrfId, @QueryParam("visit_id") Long visitId)
			throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getTrialService().clearEcrfFieldValues(auth, listEntryId, ecrfId, visitId);
	}
	//@GET
	//@Produces({ MediaType.APPLICATION_JSON })
	//@Path("{listEntryId}/ecrffieldvalue/{ecrfFieldId}")
	//public JsValuesOutVOPage<ECRFFieldValueOutVO, ECRFFieldValueJsonVO> getEcrfFieldValue(
	//		@PathParam("listEntryId") Long listEntryId, @PathParam("ecrfFieldId") Long ecrfFieldId, 
	//		@QueryParam("visit_id") Long visitId)
	//		throws AuthenticationException, AuthorisationException, ServiceException {
	//	ECRFFieldValuesOutVO values = WebUtil.getServiceLocator().getTrialService().getEcrfFieldValue(auth, listEntryId, ecrfFieldId, null);
	//	return new JsValuesOutVOPage<ECRFFieldValueOutVO, ECRFFieldValueJsonVO>(values.getPageValues(), values.getJsValues(), null);
	//}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{listEntryId}/ecrffieldvalue/{ecrfFieldId}") ///{index}")
	public JsValuesOutVOPage<ECRFFieldValueOutVO, ECRFFieldValueJsonVO> getEcrfFieldValue(
			@PathParam("listEntryId") Long listEntryId, @PathParam("ecrfFieldId") Long ecrfFieldId,
			@QueryParam("visit_id") Long visitId, @QueryParam("index") Long index)
			throws AuthenticationException, AuthorisationException, ServiceException {
		ECRFFieldValuesOutVO values = WebUtil.getServiceLocator().getTrialService().getEcrfFieldValue(auth, listEntryId, visitId, ecrfFieldId, index);
		return new JsValuesOutVOPage<ECRFFieldValueOutVO, ECRFFieldValueJsonVO>(values.getPageValues(), values.getJsValues(), null);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{listEntryId}/{ecrfId}/ecrffieldvalues")
	public JsValuesOutVOPage<ECRFFieldValueOutVO, ECRFFieldValueJsonVO> getEcrfFieldValues(@PathParam("listEntryId") Long listEntryId, @PathParam("ecrfId") Long ecrfId,
			@QueryParam("visit_id") Long visitId, @QueryParam("load_all_js_values") Boolean loadAllJsValues, @QueryParam("query") String query, @Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf = new PSFUriPart(uriInfo, "visit_id", "load_all_js_values", "query");
		ECRFFieldValuesOutVO values = WebUtil.getServiceLocator().getTrialService().getEcrfFieldValues(auth, listEntryId, ecrfId, visitId, false, loadAllJsValues, query, psf);
		return new JsValuesOutVOPage<ECRFFieldValueOutVO, ECRFFieldValueJsonVO>(values.getPageValues(), values.getJsValues(), psf);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{listEntryId}/{ecrfId}/ecrffieldvalues/maxindex")
	public Long getEcrfFieldValuesSectionMaxIndex(@PathParam("listEntryId") Long listEntryId, @PathParam("ecrfId") Long ecrfId,
			@QueryParam("visit_id") Long visitId, @QueryParam("section") String section)
			throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getTrialService().getEcrfFieldValuesSectionMaxIndex(auth, listEntryId, ecrfId, visitId, section);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{listEntryId}/{ecrfId}")
	public ECRFStatusEntryVO getEcrfStatusEntry(@PathParam("listEntryId") Long listEntryId,
			@PathParam("ecrfId") Long ecrfId, @QueryParam("visit_id") Long visitId)
			throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getTrialService().getEcrfStatusEntry(auth, listEntryId, ecrfId, visitId);
	}

	@GET
	@Path("{listEntryId}/ecrfpdf/{ecrfId}")
	public Response renderEcrf(@PathParam("listEntryId") Long listEntryId, @PathParam("ecrfId") Long ecrfId,
			@QueryParam("visit_id") Long visitId, @QueryParam("blank") Boolean blank) throws AuthenticationException,
			AuthorisationException, ServiceException {
		ECRFPDFVO vo = WebUtil.getServiceLocator().getTrialService().renderEcrf(auth, listEntryId, ecrfId, visitId, null, blank);
		// http://stackoverflow.com/questions/9204287/how-to-return-a-png-image-from-jersey-rest-service-method-to-the-browser
		// non-streamed
		ResponseBuilder response = Response.ok(vo.getDocumentDatas(), vo.getContentType().getMimeType());
		response.header(HttpHeaders.CONTENT_LENGTH, vo.getSize());
		return response.build();
	}

	@HEAD
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{listEntryId}/ecrfpdf/{ecrfId}")
	public ECRFPDFVO renderEcrfHead(@PathParam("listEntryId") Long listEntryId, @PathParam("ecrfId") Long ecrfId,
			@QueryParam("visit_id") Long visitId, @QueryParam("blank") Boolean blank) throws AuthenticationException, AuthorisationException, ServiceException {
		ECRFPDFVO result = WebUtil.getServiceLocator().getTrialService().renderEcrf(auth, listEntryId, ecrfId, visitId, null, blank);
		result.setDocumentDatas(null);
		return result;
	}

	@GET
	@Path("{listEntryId}/ecrfpdf")
	public Response renderEcrfs(@PathParam("listEntryId") Long listEntryId,
			@QueryParam("blank") Boolean blank) throws AuthenticationException, AuthorisationException, ServiceException {
		ECRFPDFVO vo = WebUtil.getServiceLocator().getTrialService().renderEcrfs(auth, null, listEntryId, null, null, blank);
		// http://stackoverflow.com/questions/9204287/how-to-return-a-png-image-from-jersey-rest-service-method-to-the-browser
		// non-streamed
		ResponseBuilder response = Response.ok(vo.getDocumentDatas(), vo.getContentType().getMimeType());
		response.header(HttpHeaders.CONTENT_LENGTH, vo.getSize());
		return response.build();
	}

	@HEAD
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{listEntryId}/ecrfpdf")
	public ECRFPDFVO renderEcrfsHead(@PathParam("listEntryId") Long listEntryId,
			@QueryParam("blank") Boolean blank) throws AuthenticationException, AuthorisationException, ServiceException {
		ECRFPDFVO result = WebUtil.getServiceLocator().getTrialService().renderEcrfs(auth, null, listEntryId, null, null, blank);
		result.setDocumentDatas(null);
		return result;
	}

	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{listEntryId}/{ecrfId}")
	public ECRFStatusEntryVO setEcrfStatusEntry(@PathParam("listEntryId") Long listEntryId, @PathParam("ecrfId") Long ecrfId,
			@QueryParam("visit_id") Long visitId, @QueryParam("version") Long version, @QueryParam("ecrf_status_type_id") Long ecrfStatusTypeId,
			@QueryParam("proband_list_status_entry_id") Long probandListStatusEntryId)
			throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getTrialService().setEcrfStatusEntry(auth, listEntryId, ecrfId, visitId, version, ecrfStatusTypeId, probandListStatusEntryId);
	}
}