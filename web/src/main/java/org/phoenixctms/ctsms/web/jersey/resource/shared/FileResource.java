package org.phoenixctms.ctsms.web.jersey.resource.shared;

import io.swagger.annotations.Api;

import java.io.InputStream;

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
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import org.phoenixctms.ctsms.enumeration.FileModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.FileInVO;
import org.phoenixctms.ctsms.vo.FileOutVO;
import org.phoenixctms.ctsms.vo.FileStreamInVO;
import org.phoenixctms.ctsms.vo.FileStreamOutVO;
import org.phoenixctms.ctsms.web.jersey.index.ResourceIndex;
import org.phoenixctms.ctsms.web.jersey.resource.PSFUriPart;
import org.phoenixctms.ctsms.web.jersey.resource.Page;
import org.phoenixctms.ctsms.web.util.WebUtil;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataParam;

@Api
@Path("/file")
public class FileResource {

	@Context
	AuthenticationVO auth;

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({ MediaType.APPLICATION_JSON })
	public FileOutVO addFile(@FormDataParam("json") FormDataBodyPart json,
			@FormDataParam("data") FormDataBodyPart content,
			@FormDataParam("data") FormDataContentDisposition contentDisposition,
			@FormDataParam("data") final InputStream input) throws AuthenticationException, AuthorisationException, ServiceException {
		// in.setTrialId(trialId);
		// in.setModule(FileModule.TRIAL_DOCUMENT);
		// https://stackoverflow.com/questions/27609569/file-upload-along-with-other-object-in-jersey-restful-web-service
		json.setMediaType(MediaType.APPLICATION_JSON_TYPE);
		FileInVO in = json.getValueAs(FileInVO.class);
		FileStreamInVO stream = new FileStreamInVO();
		stream.setStream(input);
		stream.setMimeType(content.getMediaType().toString()); // .getType());
		stream.setSize(contentDisposition.getSize());
		stream.setFileName(contentDisposition.getFileName());
		return WebUtil.getServiceLocator().getFileService().addFile(auth, in, stream);
	}

	@DELETE
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}")
	public FileOutVO deleteFile(@PathParam("id") Long id) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getFileService().deleteFile(auth, id);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("course")
	public Page<FileOutVO> getCourseFiles(@Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<FileOutVO>(WebUtil.getServiceLocator().getFileService().getFiles(auth, FileModule.COURSE_DOCUMENT, null, null, null, psf = new PSFUriPart(uriInfo)), psf);
	}

	// @HEAD
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}/head")
	public FileOutVO getFile(@PathParam("id") Long id) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getFileService().getFile(auth, id);
	}

	@GET
	@Path("{id}")
	public Response getFileStream(@PathParam("id") Long id) throws AuthenticationException, AuthorisationException, ServiceException {
		// FileStreamOutVO f = WebUtil.getServiceLocator().getFileService().getFileStream(auth, fileId);
		// return Response.ok(f.getStream(), f.getContentType().getMimeType()).build();
		FileStreamOutVO stream = WebUtil.getServiceLocator().getFileService().getFileStream(auth, id);
		ResponseBuilder response = javax.ws.rs.core.Response.ok(stream.getStream(), stream.getContentType().getMimeType());
		response.header(HttpHeaders.CONTENT_LENGTH, stream.getSize());
		return response.build();
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("inventory")
	public Page<FileOutVO> getInventoryFiles(@Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<FileOutVO>(WebUtil.getServiceLocator().getFileService().getFiles(auth, FileModule.INVENTORY_DOCUMENT, null, null, null, psf = new PSFUriPart(uriInfo)), psf);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("proband")
	public Page<FileOutVO> getProbandFiles(@Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<FileOutVO>(WebUtil.getServiceLocator().getFileService().getFiles(auth, FileModule.PROBAND_DOCUMENT, null, null, null, psf = new PSFUriPart(uriInfo)), psf);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("staff")
	public Page<FileOutVO> getStaffFiles(@Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<FileOutVO>(WebUtil.getServiceLocator().getFileService().getFiles(auth, FileModule.STAFF_DOCUMENT, null, null, null, psf = new PSFUriPart(uriInfo)), psf);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("trial")
	public Page<FileOutVO> getTrialFiles(@Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<FileOutVO>(WebUtil.getServiceLocator().getFileService().getFiles(auth, FileModule.TRIAL_DOCUMENT, null, null, null, psf = new PSFUriPart(uriInfo)), psf);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ResourceIndex index(@Context Application application,
			@Context HttpServletRequest request) throws Exception {
		// String basePath = request.getRequestURL().toString();
		return new ResourceIndex(IndexResource.getResourceIndexNode(FileResource.class, request)); // basePath));
	}

	@PUT
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({ MediaType.APPLICATION_JSON })
	public FileOutVO updateFile(@FormDataParam("json") FormDataBodyPart json,
			@FormDataParam("data") FormDataBodyPart content,
			@FormDataParam("data") FormDataContentDisposition contentDisposition,
			@FormDataParam("data") final InputStream input) throws AuthenticationException, AuthorisationException, ServiceException {
		// in.setId(fileId);
		json.setMediaType(MediaType.APPLICATION_JSON_TYPE);
		FileInVO in = json.getValueAs(FileInVO.class);
		FileStreamInVO stream = new FileStreamInVO();
		stream.setStream(input);
		stream.setMimeType(content.getMediaType().toString()); // .getType());
		stream.setSize(contentDisposition.getSize());
		stream.setFileName(contentDisposition.getFileName());
		return WebUtil.getServiceLocator().getFileService().updateFile(auth, in, stream);
	}
	// @GET
	// @Produces({MediaType.APPLICATION_JSON})
	// public Page<FileOutVO> getFiles(@Context UriInfo uriInfo)
	// throws AuthenticationException, AuthorisationException, ServiceException {
	// PSFUriPart psf;
	// return new Page<FileOutVO>(WebUtil.getServiceLocator().getFileService().getFiles(auth, null, null, null, null, psf = new PSFUriPart(uriInfo)),psf);
	// }
}
