package org.phoenixctms.ctsms.web.jersey.resource.trial;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.FileContentInVO;
import org.phoenixctms.ctsms.vo.FileOutVO;
import org.phoenixctms.ctsms.vo.FileStreamOutVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagValueInVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagValueJsonVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagValueOutVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagValuesOutVO;
import org.phoenixctms.ctsms.web.jersey.resource.InputFieldValueFileParts;
import org.phoenixctms.ctsms.web.jersey.resource.ResourceUtils;
import org.phoenixctms.ctsms.web.jersey.wrapper.JsValuesOutVOPage;
import org.phoenixctms.ctsms.web.util.WebUtil;

import com.sun.jersey.multipart.FormDataMultiPart;

import io.swagger.annotations.Api;

@Api(value = "trial")
@Path("/probandlistentrytagvalue")
public class ProbandListEntryTagValuesResource {

	private static final InputFieldValueFileParts.FileValueAccessor<ProbandListEntryTagValueInVO> FILE_VALUE_ACCESSOR = new InputFieldValueFileParts.FileValueAccessor<ProbandListEntryTagValueInVO>() {

		@Override
		public FileContentInVO getFileValue(ProbandListEntryTagValueInVO value) {
			return value.getFileValue();
		}

		@Override
		public void setFileValue(ProbandListEntryTagValueInVO value, FileContentInVO fileValue) {
			value.setFileValue(fileValue);
		}

		@Override
		public void setFileValueId(ProbandListEntryTagValueInVO value, Long fileValueId) {
			value.setFileValueId(fileValueId);
		}
	};

	@Context
	AuthenticationVO auth;

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}")
	public ProbandListEntryTagValueOutVO getProbandListEntryTagValueById(@PathParam("id") Long id) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getTrialService().getProbandListEntryTagValueById(auth, id);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}/file/head")
	public FileOutVO getFile(@PathParam("id") Long id) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getTrialService().getProbandListEntryTagValueById(auth, id).getFileValue();
	}

	@GET
	@Path("{id}/file")
	public Response getFileStream(@PathParam("id") Long id) throws AuthenticationException, AuthorisationException, ServiceException, UnsupportedEncodingException {
		FileStreamOutVO stream = WebUtil.getServiceLocator().getTrialService().getProbandListEntryTagValueFileStream(auth, id);
		return ResourceUtils.getAttachmentFileStreamResponse(stream);
	}

	@PUT
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public JsValuesOutVOPage<ProbandListEntryTagValueOutVO, ProbandListEntryTagValueJsonVO> setProbandListEntryTagValues(Collection<ProbandListEntryTagValueInVO> in,
			@QueryParam("force") Boolean force)
			throws AuthenticationException, AuthorisationException,
			ServiceException {
		ProbandListEntryTagValuesOutVO values = WebUtil.getServiceLocator().getTrialService().setProbandListEntryTagValues(auth,
				new LinkedHashSet<ProbandListEntryTagValueInVO>(in), force != null ? force : false);
		return new JsValuesOutVOPage<ProbandListEntryTagValueOutVO, ProbandListEntryTagValueJsonVO>(values.getPageValues(), values.getJsValues(), null);
	}

	@PUT
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({ MediaType.APPLICATION_JSON })
	public JsValuesOutVOPage<ProbandListEntryTagValueOutVO, ProbandListEntryTagValueJsonVO> setProbandListEntryTagValuesMultipart(FormDataMultiPart multiPart, @QueryParam("force") Boolean force) throws Exception {
		ArrayList<ProbandListEntryTagValueInVO> in = InputFieldValueFileParts.getValues(multiPart, ProbandListEntryTagValueInVO[].class, FILE_VALUE_ACCESSOR);
		ProbandListEntryTagValuesOutVO values = WebUtil.getServiceLocator().getTrialService().setProbandListEntryTagValues(auth,
				new LinkedHashSet<ProbandListEntryTagValueInVO>(in), force != null ? force : false);
		return new JsValuesOutVOPage<ProbandListEntryTagValueOutVO, ProbandListEntryTagValueJsonVO>(values.getPageValues(), values.getJsValues(), null);
	}
}
