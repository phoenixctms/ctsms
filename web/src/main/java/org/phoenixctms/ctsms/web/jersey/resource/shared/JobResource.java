package org.phoenixctms.ctsms.web.jersey.resource.shared;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.phoenixctms.ctsms.enumeration.HyperlinkModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.HyperlinkInVO;
import org.phoenixctms.ctsms.vo.HyperlinkOutVO;
import org.phoenixctms.ctsms.vo.JobInVO;
import org.phoenixctms.ctsms.vo.JobOutVO;
import org.phoenixctms.ctsms.web.jersey.index.ResourceIndex;
import org.phoenixctms.ctsms.web.jersey.resource.PSFUriPart;
import org.phoenixctms.ctsms.web.jersey.resource.Page;
import org.phoenixctms.ctsms.web.util.WebUtil;

import io.swagger.annotations.Api;

@Api(value = "shared")
@Path("/job")
public final class JobResource {

	@Context
	AuthenticationVO auth;

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public JobOutVO addJob(JobInVO in) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getJobService().addJob(auth, in);
	}

	@DELETE
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}")
	public HyperlinkOutVO deleteHyperlink(@PathParam("id") Long id) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getHyperlinkService().deleteHyperlink(auth, id);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("course")
	public Page<HyperlinkOutVO> getCourseLinks(@Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<HyperlinkOutVO>(WebUtil.getServiceLocator().getHyperlinkService()
				.getHyperlinks(auth, HyperlinkModule.COURSE_HYPERLINK, null, null, psf = new PSFUriPart(uriInfo)), psf);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}")
	public HyperlinkOutVO getHyperlink(@PathParam("id") Long id) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getHyperlinkService().getHyperlink(auth, id);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("inventory")
	public Page<HyperlinkOutVO> getInventoryLinks(@Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<HyperlinkOutVO>(WebUtil.getServiceLocator().getHyperlinkService()
				.getHyperlinks(auth, HyperlinkModule.INVENTORY_HYPERLINK, null, null, psf = new PSFUriPart(uriInfo)), psf);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("staff")
	public Page<HyperlinkOutVO> getStaffLinks(@Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<HyperlinkOutVO>(WebUtil.getServiceLocator().getHyperlinkService()
				.getHyperlinks(auth, HyperlinkModule.STAFF_HYPERLINK, null, null, psf = new PSFUriPart(uriInfo)), psf);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("trial")
	public Page<HyperlinkOutVO> getTrialLinks(@Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<HyperlinkOutVO>(WebUtil.getServiceLocator().getHyperlinkService()
				.getHyperlinks(auth, HyperlinkModule.TRIAL_HYPERLINK, null, null, psf = new PSFUriPart(uriInfo)), psf);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ResourceIndex index(@Context Application application,
			@Context HttpServletRequest request) throws Exception {
		// String basePath = request.getRequestURL().toString();
		return new ResourceIndex(IndexResource.getResourceIndexNode(HyperlinkResource.class, request)); // basePath));
	}

	@PUT
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public HyperlinkOutVO updateHyperlink(HyperlinkInVO in) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getHyperlinkService().updateHyperlink(auth, in);
	}
}
