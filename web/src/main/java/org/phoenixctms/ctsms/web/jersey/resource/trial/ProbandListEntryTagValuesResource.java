package org.phoenixctms.ctsms.web.jersey.resource.trial;

import java.util.Collection;
import java.util.LinkedHashSet;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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
import org.phoenixctms.ctsms.vo.ProbandListEntryTagValueInVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagValueJsonVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagValueOutVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagValuesOutVO;
import org.phoenixctms.ctsms.web.jersey.wrapper.JsValuesOutVOPage;
import org.phoenixctms.ctsms.web.util.WebUtil;

import io.swagger.annotations.Api;

@Api(value="trial")
@Path("/probandlistentrytagvalue")
public class ProbandListEntryTagValuesResource {

	@Context
	AuthenticationVO auth;

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}")
	public ProbandListEntryTagValueOutVO getProbandListEntryTagValueById(@PathParam("id") Long id) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getTrialService().getProbandListEntryTagValueById(auth, id);
	}

	@PUT
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public JsValuesOutVOPage<ProbandListEntryTagValueOutVO, ProbandListEntryTagValueJsonVO> setProbandListEntryTagValues(Collection<ProbandListEntryTagValueInVO> in)
			throws AuthenticationException, AuthorisationException,
			ServiceException {
		ProbandListEntryTagValuesOutVO values = WebUtil.getServiceLocator().getTrialService().setProbandListEntryTagValues(auth,
				new LinkedHashSet<ProbandListEntryTagValueInVO>(in));
		return new JsValuesOutVOPage<ProbandListEntryTagValueOutVO, ProbandListEntryTagValueJsonVO>(values.getPageValues(), values.getJsValues(), null);
	}
}
