package org.phoenixctms.ctsms.web.jersey.resource.trial;

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

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.ECRFFieldValueInVO;
import org.phoenixctms.ctsms.vo.ECRFFieldValueJsonVO;
import org.phoenixctms.ctsms.vo.ECRFFieldValueOutVO;
import org.phoenixctms.ctsms.vo.ECRFFieldValuesOutVO;
import org.phoenixctms.ctsms.web.jersey.wrapper.JsValuesOutVOPage;
import org.phoenixctms.ctsms.web.util.WebUtil;

import io.swagger.annotations.Api;

@Api(value = "trial")
@Path("/ecrffieldvalue")
public class EcrfFieldValuesResource {

	@Context
	AuthenticationVO auth;

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}")
	public ECRFFieldValueOutVO getEcrfFieldValueById(@PathParam("id") Long id) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getTrialService().getEcrfFieldValueById(auth, id);
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
}
