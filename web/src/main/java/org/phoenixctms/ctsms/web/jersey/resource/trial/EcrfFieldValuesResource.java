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
import org.phoenixctms.ctsms.vo.ECRFFieldValueInVO;
import org.phoenixctms.ctsms.vo.ECRFFieldValueJsonVO;
import org.phoenixctms.ctsms.vo.ECRFFieldValueOutVO;
import org.phoenixctms.ctsms.vo.ECRFFieldValuesOutVO;
import org.phoenixctms.ctsms.web.jersey.resource.InputFieldValueFileParts;
import org.phoenixctms.ctsms.web.jersey.resource.ResourceUtils;
import org.phoenixctms.ctsms.web.jersey.wrapper.JsValuesOutVOPage;
import org.phoenixctms.ctsms.web.util.WebUtil;

import com.sun.jersey.multipart.FormDataMultiPart;

import io.swagger.annotations.Api;

@Api(value = "trial")
@Path("/ecrffieldvalue")
public class EcrfFieldValuesResource {

	private static final InputFieldValueFileParts.FileValueAccessor<ECRFFieldValueInVO> FILE_VALUE_ACCESSOR = new InputFieldValueFileParts.FileValueAccessor<ECRFFieldValueInVO>() {

		@Override
		public FileContentInVO getFileValue(ECRFFieldValueInVO value) {
			return value.getFileValue();
		}

		@Override
		public void setFileValue(ECRFFieldValueInVO value, FileContentInVO fileValue) {
			value.setFileValue(fileValue);
		}

		@Override
		public void setFileValueId(ECRFFieldValueInVO value, Long fileValueId) {
			value.setFileValueId(fileValueId);
		}
	};

	@Context
	AuthenticationVO auth;

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}")
	public ECRFFieldValueOutVO getEcrfFieldValueById(@PathParam("id") Long id) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getTrialService().getEcrfFieldValueById(auth, id);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}/file/head")
	public FileOutVO getFile(@PathParam("id") Long id) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getTrialService().getEcrfFieldValueById(auth, id).getFileValue();
	}

	@GET
	@Path("{id}/file")
	public Response getFileStream(@PathParam("id") Long id) throws AuthenticationException, AuthorisationException, ServiceException, UnsupportedEncodingException {
		FileStreamOutVO stream = WebUtil.getServiceLocator().getTrialService().getEcrfFieldValueFileStream(auth, id);
		return ResourceUtils.getAttachmentFileStreamResponse(stream);
	}

	@PUT
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public JsValuesOutVOPage<ECRFFieldValueOutVO, ECRFFieldValueJsonVO> setEcrfFieldValues(Collection<ECRFFieldValueInVO> in, @QueryParam("force") Boolean force)
			throws AuthenticationException, AuthorisationException,
			ServiceException {
		ECRFFieldValuesOutVO values = WebUtil.getServiceLocator().getTrialService().setEcrfFieldValues(auth, new LinkedHashSet<ECRFFieldValueInVO>(in), null,
				force != null ? force : false, null, null);
		return new JsValuesOutVOPage<ECRFFieldValueOutVO, ECRFFieldValueJsonVO>(values.getPageValues(), values.getJsValues(), null);
	}

	@PUT
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({ MediaType.APPLICATION_JSON })
	public JsValuesOutVOPage<ECRFFieldValueOutVO, ECRFFieldValueJsonVO> setEcrfFieldValuesMultipart(FormDataMultiPart multiPart, @QueryParam("force") Boolean force) throws Exception {
		ArrayList<ECRFFieldValueInVO> in = InputFieldValueFileParts.getValues(multiPart, ECRFFieldValueInVO[].class, FILE_VALUE_ACCESSOR);
		ECRFFieldValuesOutVO values = WebUtil.getServiceLocator().getTrialService().setEcrfFieldValues(auth, new LinkedHashSet<ECRFFieldValueInVO>(in), null,
				force != null ? force : false, null, null);
		return new JsValuesOutVOPage<ECRFFieldValueOutVO, ECRFFieldValueJsonVO>(values.getPageValues(), values.getJsValues(), null);
	}
}
