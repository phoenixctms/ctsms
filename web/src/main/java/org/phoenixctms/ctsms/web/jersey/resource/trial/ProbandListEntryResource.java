package org.phoenixctms.ctsms.web.jersey.resource.trial;

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

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryInVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryOutVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagValueJsonVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagValueOutVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagValuesOutVO;
import org.phoenixctms.ctsms.vo.ProbandListStatusEntryOutVO;
import org.phoenixctms.ctsms.web.jersey.resource.PSFUriPart;
import org.phoenixctms.ctsms.web.jersey.resource.Page;
import org.phoenixctms.ctsms.web.jersey.wrapper.JsValuesOutVOPage;
import org.phoenixctms.ctsms.web.util.WebUtil;

import io.swagger.annotations.Api;

@Api(value="trial")
@Path("/probandlistentry")
public class ProbandListEntryResource {

	@Context
	AuthenticationVO auth;

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public ProbandListEntryOutVO addProbandListEntry(ProbandListEntryInVO in, @QueryParam("randomize") Boolean randomize, @QueryParam("createProband") Boolean createProband)
			throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getTrialService().addProbandListEntry(auth, createProband != null ? createProband.booleanValue() : false, false, randomize, in);
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("signup")
	public ProbandListEntryOutVO addProbandListEntrySignup(ProbandListEntryInVO in, @QueryParam("randomize") Boolean randomize, @QueryParam("createProband") Boolean createProband)
			throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getTrialService().addProbandListEntry(auth, createProband != null ? createProband.booleanValue() : false, true, randomize, in);
	}

	@DELETE
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}")
	public ProbandListEntryOutVO deleteProbandListEntry(@PathParam("id") Long id) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getTrialService().deleteProbandListEntry(auth, id);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}")
	public ProbandListEntryOutVO getProbandListEntry(@PathParam("id") Long id) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getTrialService().getProbandListEntry(auth, id);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Page<ProbandListEntryOutVO> getProbandListEntryList(@Context UriInfo uriInfo) throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<ProbandListEntryOutVO>(WebUtil.getServiceLocator().getTrialService().getProbandListEntryList(auth, null, null, null, true, psf = new PSFUriPart(uriInfo)),
				psf);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}/tagvalue/{tagId}")
	public JsValuesOutVOPage<ProbandListEntryTagValueOutVO, ProbandListEntryTagValueJsonVO> getProbandListEntryTagValue(@PathParam("id") Long id, @PathParam("tagId") Long tagId)
			throws AuthenticationException, AuthorisationException, ServiceException {
		ProbandListEntryTagValuesOutVO values = WebUtil.getServiceLocator().getTrialService()
				.getProbandListEntryTagValue(auth, id, tagId);
		return new JsValuesOutVOPage<ProbandListEntryTagValueOutVO, ProbandListEntryTagValueJsonVO>(values.getPageValues(), values.getJsValues(), null);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}/tagvalues")
	public JsValuesOutVOPage<ProbandListEntryTagValueOutVO, ProbandListEntryTagValueJsonVO> getProbandListEntryTagValues(@PathParam("id") Long id,
			@QueryParam("sort") Boolean sort, @QueryParam("load_all_js_values") Boolean loadAllJsValues, @Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf = new PSFUriPart(uriInfo, "sort", "load_all_js_values");
		ProbandListEntryTagValuesOutVO values = WebUtil.getServiceLocator().getTrialService()
				.getProbandListEntryTagValues(auth, id, sort, loadAllJsValues, psf);
		return new JsValuesOutVOPage<ProbandListEntryTagValueOutVO, ProbandListEntryTagValueJsonVO>(values.getPageValues(), values.getJsValues(), psf);
		// PSFUriPart psf;
		// return new ProbandListEntryTagValuesOutVOPage(WebUtil.getServiceLocator().getTrialService()
		// .getProbandListEntryTagValues(auth, id, false, false, psf = new PSFUriPart(uriInfo)),
		// psf);
		// return WebUtil.getServiceLocator().getTrialService().getProbandListEntryTagValues(auth, id, false, false, new PSFUriPart(uriInfo));
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}/laststatus")
	public ProbandListStatusEntryOutVO getProbandListStatusEntryList(@PathParam("id") Long id)
			throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getTrialService().getProbandListEntry(auth, id).getLastStatus();
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}/status")
	public Page<ProbandListStatusEntryOutVO> getProbandListStatusEntryList(@PathParam("id") Long id, @Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<ProbandListStatusEntryOutVO>(WebUtil.getServiceLocator().getTrialService().getProbandListStatusEntryList(auth, id, psf = new PSFUriPart(uriInfo)), psf);
	}

	@PUT
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public ProbandListEntryOutVO updateProbandListEntry(ProbandListEntryInVO in, @QueryParam("randomize") Boolean randomize)
			throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getTrialService().updateProbandListEntry(auth, in, null, randomize);
	}
	// @GET
	// @Produces({MediaType.APPLICATION_JSON})
	// @Path("{id}/tagvalues/{tagid}")
	// public ProbandListEntryTagValuesOutVO getProbandListEntryTagValues(@PathParam("id") Long id,@PathParam("tagid") Long tagId)
	// throws AuthenticationException, AuthorisationException, ServiceException {
	// return WebUtil.getServiceLocator().getTrialService().getProbandListEntryTagValue(auth, id, tagId);
	// }
}