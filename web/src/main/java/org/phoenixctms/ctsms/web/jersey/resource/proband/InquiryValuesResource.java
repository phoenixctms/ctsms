package org.phoenixctms.ctsms.web.jersey.resource.proband;

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
import org.phoenixctms.ctsms.vo.InquiryValueInVO;
import org.phoenixctms.ctsms.vo.InquiryValueJsonVO;
import org.phoenixctms.ctsms.vo.InquiryValueOutVO;
import org.phoenixctms.ctsms.vo.InquiryValuesOutVO;
import org.phoenixctms.ctsms.web.jersey.wrapper.JsValuesOutVOPage;
import org.phoenixctms.ctsms.web.util.WebUtil;

import io.swagger.annotations.Api;

@Api(value="proband")
@Path("/inquiryvalue")
public class InquiryValuesResource {

	@Context
	AuthenticationVO auth;

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}")
	public InquiryValueOutVO getInquiryValueById(@PathParam("id") Long id) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getProbandService().getInquiryValueById(auth, id);
	}

	@PUT
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public JsValuesOutVOPage<InquiryValueOutVO, InquiryValueJsonVO> setInquiryValues(Collection<InquiryValueInVO> in) throws AuthenticationException, AuthorisationException,
			ServiceException {
		InquiryValuesOutVO values = WebUtil.getServiceLocator().getProbandService().setInquiryValues(auth, new LinkedHashSet<InquiryValueInVO>(in));
		return new JsValuesOutVOPage<InquiryValueOutVO, InquiryValueJsonVO>(values.getPageValues(), values.getJsValues(), null);
	}
}
