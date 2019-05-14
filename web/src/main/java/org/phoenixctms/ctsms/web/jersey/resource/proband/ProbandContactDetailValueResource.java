package org.phoenixctms.ctsms.web.jersey.resource.proband;

import io.swagger.annotations.Api;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.ProbandContactDetailValueInVO;
import org.phoenixctms.ctsms.vo.ProbandContactDetailValueOutVO;
import org.phoenixctms.ctsms.web.util.WebUtil;

@Api(value="proband")
@Path("/probandcontactdetailvalue")
public class ProbandContactDetailValueResource {

	@Context
	AuthenticationVO auth;

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public ProbandContactDetailValueOutVO addProbandContactDetailValue(ProbandContactDetailValueInVO in) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getProbandService().addProbandContactDetailValue(auth, in);
	}

	@DELETE
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}")
	public ProbandContactDetailValueOutVO deleteProbandContactDetailValue(@PathParam("id") Long id) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getProbandService().deleteProbandContactDetailValue(auth, id);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}")
	public ProbandContactDetailValueOutVO getProbandContactDetailValue(@PathParam("id") Long id) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getProbandService().getProbandContactDetailValue(auth, id);
	}

	@PUT
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public ProbandContactDetailValueOutVO updateProbandGroup(ProbandContactDetailValueInVO in) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getProbandService().updateProbandContactDetailValue(auth, in);
	}
}
