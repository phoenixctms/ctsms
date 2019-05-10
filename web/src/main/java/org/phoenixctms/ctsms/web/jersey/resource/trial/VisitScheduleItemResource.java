package org.phoenixctms.ctsms.web.jersey.resource.trial;

import io.swagger.annotations.Api;

import java.util.Collection;
import java.util.Date;

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

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.VisitScheduleItemInVO;
import org.phoenixctms.ctsms.vo.VisitScheduleItemOutVO;
import org.phoenixctms.ctsms.web.jersey.resource.StringConverter;
import org.phoenixctms.ctsms.web.util.WebUtil;

@Api
@Path("/visitscheduleitem")
public class VisitScheduleItemResource {

	@Context
	AuthenticationVO auth;

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public VisitScheduleItemOutVO addVisitScheduleItem(VisitScheduleItemInVO in) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getTrialService().addVisitScheduleItem(auth, in);
	}

	@DELETE
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}")
	public VisitScheduleItemOutVO deleteVisitScheduleItem(@PathParam("id") Long id) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getTrialService().deleteVisitScheduleItem(auth, id);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}")
	public VisitScheduleItemOutVO getVisitScheduleItem(@PathParam("id") Long id) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getTrialService().getVisitScheduleItem(auth, id);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("interval")
	public Collection<VisitScheduleItemOutVO> getVisitScheduleItemInterval(@QueryParam("trial_id") Long trialId, @QueryParam("from") String from,
			@QueryParam("to") String to, @QueryParam("sort") Boolean sort) throws Exception { // AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getTrialService().getVisitScheduleItemInterval(auth, trialId, null, null, null,
				(Date) (CommonUtil.isEmptyString(from) ? null : StringConverter.getConverter(Date.class).convert(from)),
				(Date) (CommonUtil.isEmptyString(to) ? null : StringConverter.getConverter(Date.class).convert(to)), null, sort);
	}

	@PUT
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public VisitScheduleItemOutVO updateVisitScheduleItem(VisitScheduleItemInVO in) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getTrialService().updateVisitScheduleItem(auth, in);
	}
}
