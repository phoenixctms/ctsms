package org.phoenixctms.ctsms.web.jersey.resource.proband;

import io.swagger.annotations.Api;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.ProbandAddressInVO;
import org.phoenixctms.ctsms.vo.ProbandAddressOutVO;
import org.phoenixctms.ctsms.vo.ProbandLetterPDFVO;
import org.phoenixctms.ctsms.web.util.WebUtil;

@Api
@Path("/probandaddress")
public class ProbandAddressResource {

	@Context
	AuthenticationVO auth;

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public ProbandAddressOutVO addProbandAddress(ProbandAddressInVO in) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getProbandService().addProbandAddress(auth, in);
	}

	@DELETE
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}")
	public ProbandAddressOutVO deleteProbandAddress(@PathParam("id") Long id) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getProbandService().deleteProbandAddress(auth, id);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}")
	public ProbandAddressOutVO getProbandAddress(@PathParam("id") Long id) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getProbandService().getProbandAddress(auth, id);
	}

	@GET
	@Path("{id}/probandletterpdf")
	public Response renderProbandLetterPDF(@PathParam("id") Long id) throws AuthenticationException,
			AuthorisationException, ServiceException {
		ProbandLetterPDFVO vo = WebUtil.getServiceLocator().getProbandService().renderProbandLetterPDF(auth, id);
		// http://stackoverflow.com/questions/9204287/how-to-return-a-png-image-from-jersey-rest-service-method-to-the-browser
		// non-streamed
		ResponseBuilder response = Response.ok(vo.getDocumentDatas(), vo.getContentType().getMimeType());
		response.header(HttpHeaders.CONTENT_LENGTH, vo.getSize());
		return response.build();
	}

	@HEAD
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}/probandletterpdf")
	public ProbandLetterPDFVO renderProbandLetterPDFHead(@PathParam("id") Long id)
			throws AuthenticationException, AuthorisationException, ServiceException {
		ProbandLetterPDFVO result = WebUtil.getServiceLocator().getProbandService().renderProbandLetterPDF(auth, id);
		result.setDocumentDatas(null);
		return result;
	}

	@PUT
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public ProbandAddressOutVO updateProbandAddress(ProbandAddressInVO in) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getProbandService().updateProbandAddress(auth, in);
	}
}
