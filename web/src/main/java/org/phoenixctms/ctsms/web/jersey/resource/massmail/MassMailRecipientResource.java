package org.phoenixctms.ctsms.web.jersey.resource.massmail;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Iterator;

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
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.EmailAttachmentVO;
import org.phoenixctms.ctsms.vo.EmailMessageVO;
import org.phoenixctms.ctsms.vo.MassMailRecipientInVO;
import org.phoenixctms.ctsms.vo.MassMailRecipientOutVO;
import org.phoenixctms.ctsms.web.jersey.resource.PSFUriPart;
import org.phoenixctms.ctsms.web.jersey.resource.Page;
import org.phoenixctms.ctsms.web.util.WebUtil;

import io.swagger.annotations.Api;

@Api
@Path("/massmailrecipient")
public class MassMailRecipientResource {

	@Context
	AuthenticationVO auth;

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public MassMailRecipientOutVO addMassMailRecipient(MassMailRecipientInVO in) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getMassMailService().addMassMailRecipient(auth, in);
	}

	@DELETE
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}")
	public MassMailRecipientOutVO deleteMassMailRecipient(@PathParam("id") Long id) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getMassMailService().deleteMassMailRecipient(auth, id);
	}

	@GET
	@Path("{id}/mail/attachments/{index}")
	public Response getEmailAttachment(@PathParam("id") Long id, @PathParam("index") Integer index) throws AuthenticationException, AuthorisationException, ServiceException {
		EmailAttachmentVO out = (new ArrayList<EmailAttachmentVO>(WebUtil.getServiceLocator().getMassMailService().getEmailMessage(auth, id).getAttachments())).get(index);
		ResponseBuilder response = javax.ws.rs.core.Response.ok(new ByteArrayInputStream(out.getDatas()), out.getContentType().getMimeType());
		response.header(HttpHeaders.CONTENT_LENGTH, out.getFileSize());
		return response.build();
	}

	// @HEAD
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}/mail/attachments/{index}/head")
	public EmailAttachmentVO getEmailAttachmentHead(@PathParam("id") Long id, @PathParam("index") Integer index)
			throws AuthenticationException, AuthorisationException, ServiceException {
		EmailAttachmentVO out = (new ArrayList<EmailAttachmentVO>(WebUtil.getServiceLocator().getMassMailService().getEmailMessage(auth, id).getAttachments())).get(index);
		out.setDatas(null);
		return out;
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}/mail")
	public EmailMessageVO getEmailMessage(@PathParam("id") Long id) throws AuthenticationException, AuthorisationException, ServiceException {
		EmailMessageVO out = WebUtil.getServiceLocator().getMassMailService().getEmailMessage(auth, id);
		Iterator<EmailAttachmentVO> it = out.getAttachments().iterator();
		while (it.hasNext()) {
			it.next().setDatas(null);
		}
		return out;
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}")
	public MassMailRecipientOutVO getMassMailRecipient(@PathParam("id") Long id) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getMassMailService().getMassMailRecipient(auth, id);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Page<MassMailRecipientOutVO> getMassMailRecipientList(@Context UriInfo uriInfo) throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<MassMailRecipientOutVO>(WebUtil.getServiceLocator().getMassMailService().getMassMailRecipientList(auth, null, null, psf = new PSFUriPart(uriInfo)), psf);
	}

	@PUT
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}/reset")
	public MassMailRecipientOutVO resetMassMailRecipient(@PathParam("id") Long id, @QueryParam("version") Long version)
			throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getMassMailService().resetMassMailRecipient(auth, id, version);
	}
}
