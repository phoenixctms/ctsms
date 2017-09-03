package org.phoenixctms.ctsms.web.jersey.resource.trial;

import io.swagger.annotations.Api;

import java.util.Collection;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.ECRFFieldValueJsonVO;
import org.phoenixctms.ctsms.vo.ECRFFieldValueOutVO;
import org.phoenixctms.ctsms.vo.ECRFFieldValuesOutVO;
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

	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{listEntryId}/{ecrfId}")
	public ECRFStatusEntryVO setEcrfStatusEntry(@PathParam("listEntryId") Long listEntryId, @PathParam("ecrfId") Long ecrfId,
			@QueryParam("version") Long version, @QueryParam("ecrf_status_type_id") Long ecrfStatusTypeId, @QueryParam("proband_list_status_entry_id") Long probandListStatusEntryId)
					throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getTrialService().setEcrfStatusEntry(auth, ecrfId, listEntryId, version, ecrfStatusTypeId, probandListStatusEntryId);
	}

}