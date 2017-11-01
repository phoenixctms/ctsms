package org.phoenixctms.ctsms.web.jersey.resource.trial;

import io.swagger.annotations.Api;

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





@Api
@Path("/ecrfstatusentry")
public class EcrfStatusEntryResource {

	@Context
	AuthenticationVO auth;

	@DELETE
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{listEntryId}/{ecrfId}/ecrffieldvalues")
	public Collection<ECRFFieldValueOutVO> clearEcrfFieldValues(@PathParam("listEntryId") Long listEntryId, @PathParam("ecrfId") Long ecrfId)
			throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getTrialService().clearEcrfFieldValues(auth, ecrfId, listEntryId);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{listEntryId}/ecrffieldvalue/{ecrfFieldId}")
	public JsValuesOutVOPage<ECRFFieldValueOutVO, ECRFFieldValueJsonVO> getEcrfFieldValue(@PathParam("listEntryId") Long listEntryId, @PathParam("ecrfFieldId") Long ecrfFieldId)
			throws AuthenticationException, AuthorisationException, ServiceException {
		ECRFFieldValuesOutVO values = WebUtil.getServiceLocator().getTrialService().getEcrfFieldValue(auth, listEntryId, ecrfFieldId, null);
		return new JsValuesOutVOPage<ECRFFieldValueOutVO, ECRFFieldValueJsonVO>(values.getPageValues(), values.getJsValues(), null);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{listEntryId}/ecrffieldvalue/{ecrfFieldId}/{index}")
	public JsValuesOutVOPage<ECRFFieldValueOutVO, ECRFFieldValueJsonVO> getEcrfFieldValue(@PathParam("listEntryId") Long listEntryId, @PathParam("ecrfFieldId") Long ecrfFieldId,@PathParam("index") Long index)
			throws AuthenticationException, AuthorisationException, ServiceException {
		ECRFFieldValuesOutVO values = WebUtil.getServiceLocator().getTrialService().getEcrfFieldValue(auth, listEntryId, ecrfFieldId, index);
		return new JsValuesOutVOPage<ECRFFieldValueOutVO, ECRFFieldValueJsonVO>(values.getPageValues(), values.getJsValues(), null);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{listEntryId}/{ecrfId}/ecrffieldvalues")
	public JsValuesOutVOPage<ECRFFieldValueOutVO, ECRFFieldValueJsonVO> getEcrfFieldValues(@PathParam("listEntryId") Long listEntryId, @PathParam("ecrfId") Long ecrfId,
			@QueryParam("load_all_js_values") Boolean loadAllJsValues, @Context UriInfo uriInfo)
					throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf = new PSFUriPart(uriInfo, "load_all_js_values");
		ECRFFieldValuesOutVO values = WebUtil.getServiceLocator().getTrialService().getEcrfFieldValues(auth, ecrfId, listEntryId, false, loadAllJsValues, psf);
		return new JsValuesOutVOPage<ECRFFieldValueOutVO, ECRFFieldValueJsonVO>(values.getPageValues(), values.getJsValues(), psf);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{listEntryId}/{ecrfId}/ecrffieldvalues/maxindex")
	public Long getEcrfFieldValuesSectionMaxIndex(@PathParam("listEntryId") Long listEntryId, @PathParam("ecrfId") Long ecrfId,
			@QueryParam("section") String section)
					throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getTrialService().getEcrfFieldValuesSectionMaxIndex(auth, ecrfId, section, listEntryId);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{listEntryId}/{ecrfId}")
	public ECRFStatusEntryVO getEcrfStatusEntry(@PathParam("listEntryId") Long listEntryId, @PathParam("ecrfId") Long ecrfId)
			throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getTrialService().getEcrfStatusEntry(auth, ecrfId, listEntryId);
	}

	@GET
	@Path("{listEntryId}/ecrfpdf/{ecrfId}")
	public Response renderEcrf(@PathParam("listEntryId") Long listEntryId, @PathParam("ecrfId") Long ecrfId,
			@QueryParam("blank") Boolean blank) throws AuthenticationException,
			AuthorisationException, ServiceException {
		ECRFPDFVO vo = WebUtil.getServiceLocator().getTrialService().renderEcrf(auth, ecrfId, null, listEntryId, blank);
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
			@QueryParam("blank") Boolean blank) throws AuthenticationException, AuthorisationException, ServiceException {
		ECRFPDFVO result = WebUtil.getServiceLocator().getTrialService().renderEcrf(auth, ecrfId, null, listEntryId, blank);
		result.setDocumentDatas(null);
		return result;
	}

	@GET
	@Path("{listEntryId}/ecrfpdf")
	public Response renderEcrfs(@PathParam("listEntryId") Long listEntryId,
			@QueryParam("blank") Boolean blank) throws AuthenticationException, AuthorisationException, ServiceException {
		ECRFPDFVO vo = WebUtil.getServiceLocator().getTrialService().renderEcrfs(auth, null, listEntryId, null, blank);
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
		ECRFPDFVO result = WebUtil.getServiceLocator().getTrialService().renderEcrfs(auth, null, listEntryId, null, blank);
		result.setDocumentDatas(null);
		return result;
	}

	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{listEntryId}/{ecrfId}")
	public ECRFStatusEntryVO setEcrfStatusEntry(@PathParam("listEntryId") Long listEntryId, @PathParam("ecrfId") Long ecrfId,
			@QueryParam("version") Long version, @QueryParam("ecrf_status_type_id") Long ecrfStatusTypeId, @QueryParam("proband_list_status_entry_id") Long probandListStatusEntryId)
					throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getTrialService().setEcrfStatusEntry(auth, ecrfId, listEntryId, version, ecrfStatusTypeId, probandListStatusEntryId);
	}
}