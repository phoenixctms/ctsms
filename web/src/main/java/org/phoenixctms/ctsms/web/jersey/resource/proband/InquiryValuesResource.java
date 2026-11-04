package org.phoenixctms.ctsms.web.jersey.resource.proband;

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
import org.phoenixctms.ctsms.vo.InquiryValueInVO;
import org.phoenixctms.ctsms.vo.InquiryValueJsonVO;
import org.phoenixctms.ctsms.vo.InquiryValueOutVO;
import org.phoenixctms.ctsms.vo.InquiryValuesOutVO;
import org.phoenixctms.ctsms.web.jersey.resource.InputFieldValueFileParts;
import org.phoenixctms.ctsms.web.jersey.resource.ResourceUtils;
import org.phoenixctms.ctsms.web.jersey.wrapper.JsValuesOutVOPage;
import org.phoenixctms.ctsms.web.util.WebUtil;

import com.sun.jersey.multipart.FormDataMultiPart;

import io.swagger.annotations.Api;

@Api(value = "proband")
@Path("/inquiryvalue")
public final class InquiryValuesResource {

	private static final InputFieldValueFileParts.FileValueAccessor<InquiryValueInVO> FILE_VALUE_ACCESSOR = new InputFieldValueFileParts.FileValueAccessor<InquiryValueInVO>() {

		@Override
		public FileContentInVO getFileValue(InquiryValueInVO value) {
			return value.getFileValue();
		}

		@Override
		public void setFileValue(InquiryValueInVO value, FileContentInVO fileValue) {
			value.setFileValue(fileValue);
		}

		@Override
		public void setFileValueId(InquiryValueInVO value, Long fileValueId) {
			value.setFileValueId(fileValueId);
		}
	};

	@Context
	AuthenticationVO auth;

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}")
	public InquiryValueOutVO getInquiryValueById(@PathParam("id") Long id) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getProbandService().getInquiryValueById(auth, id);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}/file/head")
	public FileOutVO getFile(@PathParam("id") Long id) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getProbandService().getInquiryValueById(auth, id).getFileValue();
	}

	@GET
	@Path("{id}/file")
	public Response getFileStream(@PathParam("id") Long id) throws AuthenticationException, AuthorisationException, ServiceException, UnsupportedEncodingException {
		FileStreamOutVO stream = WebUtil.getServiceLocator().getProbandService().getInquiryValueFileStream(auth, id);
		return ResourceUtils.getAttachmentFileStreamResponse(stream);
	}

	@PUT
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public JsValuesOutVOPage<InquiryValueOutVO, InquiryValueJsonVO> setInquiryValues(Collection<InquiryValueInVO> in, @QueryParam("force") Boolean force)
			throws AuthenticationException, AuthorisationException,
			ServiceException {
		InquiryValuesOutVO values = WebUtil.getServiceLocator().getProbandService().setInquiryValues(auth, new LinkedHashSet<InquiryValueInVO>(in), force != null ? force : false);
		return new JsValuesOutVOPage<InquiryValueOutVO, InquiryValueJsonVO>(values.getPageValues(), values.getJsValues(), null);
	}

	@PUT
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({ MediaType.APPLICATION_JSON })
	public JsValuesOutVOPage<InquiryValueOutVO, InquiryValueJsonVO> setInquiryValuesMultipart(FormDataMultiPart multiPart, @QueryParam("force") Boolean force) throws Exception {
		ArrayList<InquiryValueInVO> in = InputFieldValueFileParts.getValues(multiPart, InquiryValueInVO[].class, FILE_VALUE_ACCESSOR);
		InquiryValuesOutVO values = WebUtil.getServiceLocator().getProbandService().setInquiryValues(auth, new LinkedHashSet<InquiryValueInVO>(in),
				force != null ? force : false);
		return new JsValuesOutVOPage<InquiryValueOutVO, InquiryValueJsonVO>(values.getPageValues(), values.getJsValues(), null);
	}
}
