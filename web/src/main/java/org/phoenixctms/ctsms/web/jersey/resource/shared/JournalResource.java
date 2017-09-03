package org.phoenixctms.ctsms.web.jersey.resource.shared;

import io.swagger.annotations.Api;

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

import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.JournalEntryInVO;
import org.phoenixctms.ctsms.vo.JournalEntryOutVO;
import org.phoenixctms.ctsms.web.jersey.index.ResourceIndex;
import org.phoenixctms.ctsms.web.jersey.resource.PSFUriPart;
import org.phoenixctms.ctsms.web.jersey.resource.Page;
import org.phoenixctms.ctsms.web.util.WebUtil;

@Api
@Path("/journal")
public class JournalResource {

	@Context
	AuthenticationVO auth;

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public JournalEntryOutVO addJournalEntry(JournalEntryInVO in) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getJournalService().addJournalEntry(auth, in);
	}

	@DELETE
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}")
	public JournalEntryOutVO deleteJournalEntry(@PathParam("id") Long id) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getJournalService().deleteJournalEntry(auth, id);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("course")
	public Page<JournalEntryOutVO> getCourseJournal(@Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<JournalEntryOutVO>(WebUtil.getServiceLocator().getJournalService().getJournal(auth, JournalModule.COURSE_JOURNAL, null, psf = new PSFUriPart(uriInfo)), psf);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("criteria")
	public Page<JournalEntryOutVO> getCriteriaJournal(@Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<JournalEntryOutVO>(WebUtil.getServiceLocator().getJournalService().getJournal(auth, JournalModule.CRITERIA_JOURNAL, null, psf = new PSFUriPart(uriInfo)),
				psf);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("inputfield")
	public Page<JournalEntryOutVO> getInputFieldJournal(@Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<JournalEntryOutVO>(
				WebUtil.getServiceLocator().getJournalService().getJournal(auth, JournalModule.INPUT_FIELD_JOURNAL, null, psf = new PSFUriPart(uriInfo)), psf);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("inventory")
	public Page<JournalEntryOutVO> getInventoryJournal(@Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<JournalEntryOutVO>(WebUtil.getServiceLocator().getJournalService().getJournal(auth, JournalModule.INVENTORY_JOURNAL, null, psf = new PSFUriPart(uriInfo)),
				psf);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}")
	public JournalEntryOutVO getJournalEntry(@PathParam("id") Long id) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getJournalService().getJournalEntry(auth, id);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("proband")
	public Page<JournalEntryOutVO> getProbandJournal(@Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<JournalEntryOutVO>(WebUtil.getServiceLocator().getJournalService().getJournal(auth, JournalModule.PROBAND_JOURNAL, null, psf = new PSFUriPart(uriInfo)),
				psf);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("staff")
	public Page<JournalEntryOutVO> getStaffJournal(@Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<JournalEntryOutVO>(WebUtil.getServiceLocator().getJournalService().getJournal(auth, JournalModule.STAFF_JOURNAL, null, psf = new PSFUriPart(uriInfo)), psf);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("trial")
	public Page<JournalEntryOutVO> getTrialJournal(@Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<JournalEntryOutVO>(WebUtil.getServiceLocator().getJournalService().getJournal(auth, JournalModule.TRIAL_JOURNAL, null, psf = new PSFUriPart(uriInfo)), psf);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("user")
	public Page<JournalEntryOutVO> getUserJournal(@Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<JournalEntryOutVO>(WebUtil.getServiceLocator().getJournalService().getJournal(auth, JournalModule.USER_JOURNAL, null, psf = new PSFUriPart(uriInfo)), psf);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ResourceIndex index(@Context Application application,
			@Context HttpServletRequest request) throws Exception {
		// String basePath = request.getRequestURL().toString();
		return new ResourceIndex(IndexResource.getResourceIndexNode(JournalResource.class, request)); // basePath));
	}

	@PUT
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public JournalEntryOutVO updateTrial(JournalEntryInVO in) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getJournalService().updateJournalEntry(auth, in);
	}
	// @GET
	// @Produces({MediaType.APPLICATION_JSON})
	// public Page<JournalEntryOutVO> getJournal(@Context UriInfo uriInfo)
	// throws AuthenticationException, AuthorisationException, ServiceException {
	// PSFUriPart psf;
	// return new Page<JournalEntryOutVO>(WebUtil.getServiceLocator().getJournalService().getJournal(auth, null, null, psf = new PSFUriPart(uriInfo)),psf);
	// }
}
