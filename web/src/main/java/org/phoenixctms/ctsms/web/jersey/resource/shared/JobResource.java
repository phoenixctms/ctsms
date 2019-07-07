package org.phoenixctms.ctsms.web.jersey.resource.shared;

import java.io.ByteArrayInputStream;
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

import org.phoenixctms.ctsms.enumeration.JobModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.JobAddVO;
import org.phoenixctms.ctsms.vo.JobFileVO;
import org.phoenixctms.ctsms.vo.JobOutVO;
import org.phoenixctms.ctsms.vo.JobUpdateVO;
import org.phoenixctms.ctsms.web.jersey.index.ResourceIndex;
import org.phoenixctms.ctsms.web.jersey.resource.PSFUriPart;
import org.phoenixctms.ctsms.web.jersey.resource.Page;
import org.phoenixctms.ctsms.web.util.WebUtil;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataParam;

import io.swagger.annotations.Api;

@Api(value = "shared")
@Path("/job")
public final class JobResource {

	@Context
	AuthenticationVO auth;

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({ MediaType.APPLICATION_JSON })
	public JobOutVO addJob(@FormDataParam("json") FormDataBodyPart json,
			@FormDataParam("data") FormDataBodyPart content,
			@FormDataParam("data") FormDataContentDisposition contentDisposition,
			@FormDataParam("data") final InputStream input) throws Exception {
		json.setMediaType(MediaType.APPLICATION_JSON_TYPE);
		JobAddVO in = json.getValueAs(JobAddVO.class);
		in.setDatas(CommonUtil.inputStreamToByteArray(input));
		in.setMimeType(content.getMediaType().toString());
		in.setFileName(contentDisposition.getFileName());
		return WebUtil.getServiceLocator().getJobService().addJob(auth, in);
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public JobOutVO addJob(JobAddVO in) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getJobService().addJob(auth, in);
	}

	@DELETE
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}")
	public JobOutVO deleteJob(@PathParam("id") Long id) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getJobService().deleteJob(auth, id);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("trial")
	public Page<JobOutVO> getTrialJobs(@Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<JobOutVO>(WebUtil.getServiceLocator().getJobService()
				.getJobs(auth, JobModule.TRIAL_JOB, null, psf = new PSFUriPart(uriInfo)), psf);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("proband")
	public Page<JobOutVO> getProbandJobs(@Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<JobOutVO>(WebUtil.getServiceLocator().getJobService()
				.getJobs(auth, JobModule.PROBAND_JOB, null, psf = new PSFUriPart(uriInfo)), psf);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("inputfield")
	public Page<JobOutVO> getInputFieldJobs(@Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<JobOutVO>(WebUtil.getServiceLocator().getJobService()
				.getJobs(auth, JobModule.INPUT_FIELD_JOB, null, psf = new PSFUriPart(uriInfo)), psf);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("criteria/inventory")
	public Page<JobOutVO> getInventoryCriteriaJobs(@Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<JobOutVO>(WebUtil.getServiceLocator().getJobService()
				.getJobs(auth, JobModule.INVENTORY_CRITERIA_JOB, null, psf = new PSFUriPart(uriInfo)), psf);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("criteria/staff")
	public Page<JobOutVO> getStaffCriteriaJobs(@Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<JobOutVO>(WebUtil.getServiceLocator().getJobService()
				.getJobs(auth, JobModule.STAFF_CRITERIA_JOB, null, psf = new PSFUriPart(uriInfo)), psf);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("criteria/course")
	public Page<JobOutVO> getCourseCriteriaJobs(@Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<JobOutVO>(WebUtil.getServiceLocator().getJobService()
				.getJobs(auth, JobModule.COURSE_CRITERIA_JOB, null, psf = new PSFUriPart(uriInfo)), psf);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("criteria/trial")
	public Page<JobOutVO> getTrialCriteriaJobs(@Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<JobOutVO>(WebUtil.getServiceLocator().getJobService()
				.getJobs(auth, JobModule.TRIAL_CRITERIA_JOB, null, psf = new PSFUriPart(uriInfo)), psf);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("criteria/inputfield")
	public Page<JobOutVO> getInputFieldCriteriaJobs(@Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<JobOutVO>(WebUtil.getServiceLocator().getJobService()
				.getJobs(auth, JobModule.INPUT_FIELD_CRITERIA_JOB, null, psf = new PSFUriPart(uriInfo)), psf);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("criteria/proband")
	public Page<JobOutVO> getProbandCriteriaJobs(@Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<JobOutVO>(WebUtil.getServiceLocator().getJobService()
				.getJobs(auth, JobModule.PROBAND_CRITERIA_JOB, null, psf = new PSFUriPart(uriInfo)), psf);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("criteria/user")
	public Page<JobOutVO> getUserCriteriaJobs(@Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<JobOutVO>(WebUtil.getServiceLocator().getJobService()
				.getJobs(auth, JobModule.USER_CRITERIA_JOB, null, psf = new PSFUriPart(uriInfo)), psf);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("criteria/massmail")
	public Page<JobOutVO> getMassMailCriteriaJobs(@Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<JobOutVO>(WebUtil.getServiceLocator().getJobService()
				.getJobs(auth, JobModule.MASS_MAIL_CRITERIA_JOB, null, psf = new PSFUriPart(uriInfo)), psf);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}")
	public JobOutVO getJob(@PathParam("id") Long id) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getJobService().getJob(auth, id);
	}

	@GET
	@Path("{id}/file")
	public Response getJobFile(@PathParam("id") Long id) throws AuthenticationException, AuthorisationException, ServiceException {
		JobFileVO out = WebUtil.getServiceLocator().getJobService().getJobFile(auth, id);
		ResponseBuilder response = javax.ws.rs.core.Response.ok(new ByteArrayInputStream(out.getDatas()), out.getContentType().getMimeType());
		response.header(HttpHeaders.CONTENT_LENGTH, out.getFileSize());
		return response.build();
	}

	// @HEAD
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}/file/head")
	public JobFileVO getJobFileHead(@PathParam("id") Long id) throws AuthenticationException, AuthorisationException, ServiceException {
		JobFileVO out = WebUtil.getServiceLocator().getJobService().getJobFile(auth, id);
		out.setDatas(null);
		return out;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ResourceIndex index(@Context Application application,
			@Context HttpServletRequest request) throws Exception {
		// String basePath = request.getRequestURL().toString();
		return new ResourceIndex(IndexResource.getResourceIndexNode(JobResource.class, request)); // basePath));
	}

	@PUT
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({ MediaType.APPLICATION_JSON })
	public JobOutVO updateJob(//@FormDataParam("json") JobUpdateVO in,
			@FormDataParam("json") FormDataBodyPart json,
			@FormDataParam("data") FormDataBodyPart content,
			@FormDataParam("data") FormDataContentDisposition contentDisposition,
			@FormDataParam("data") final InputStream input) throws Exception {
		//https://stackoverflow.com/questions/27609569/file-upload-along-with-other-object-in-jersey-restful-web-service/27614403
		json.setMediaType(MediaType.APPLICATION_JSON_TYPE);
		JobUpdateVO in = json.getValueAs(JobUpdateVO.class);
		in.setDatas(CommonUtil.inputStreamToByteArray(input));
		in.setMimeType(content.getMediaType().toString());
		in.setFileName(contentDisposition.getFileName());
		return WebUtil.getServiceLocator().getJobService().updateJob(auth, in);
	}

	@PUT
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public JobOutVO updateJob(JobUpdateVO in) throws AuthenticationException, AuthorisationException, ServiceException {
		//		InputFieldImageVO out = WebUtil.getServiceLocator().getToolsService().getInputFieldImage(in.getId());
		//		in.setDatas(out.getDatas());
		//		in.setMimeType(out.getContentType().getMimeType());
		//		in.setFileName(out.getFileName());
		return WebUtil.getServiceLocator().getJobService().updateJob(auth, in);
	}
}
