package org.phoenixctms.ctsms.web.jersey.resource.inputfield;

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
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueInVO;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueOutVO;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;

import io.swagger.annotations.Api;

@Api(value="inputfield")
@Path("/inputfieldselectionsetvalue")
public class SelectionSetValueResource {

	@Context
	AuthenticationVO auth;

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public InputFieldSelectionSetValueOutVO addSelectionSetValue(InputFieldSelectionSetValueInVO in) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getInputFieldService().addSelectionSetValue(auth, in);
	}

	@DELETE
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}")
	public InputFieldSelectionSetValueOutVO deleteSelectionSetValue(@PathParam("id") Long id, @QueryParam("reason") String reason)
			throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getInputFieldService()
				.deleteSelectionSetValue(auth, id,
						Settings.getBoolean(SettingCodes.SELECTION_SET_VALUE_DEFERRED_DELETE, Bundle.SETTINGS, DefaultSettings.SELECTION_SET_VALUE_DEFERRED_DELETE), false, reason);
	}

	// @GET
	// @Produces({ MediaType.APPLICATION_JSON })
	// @Path("{id}/valuecount")
	// public long getInputFieldValueCount(@PathParam("id") Long id)
	// throws AuthenticationException, AuthorisationException, ServiceException {
	// return WebUtil.getServiceLocator().getInputFieldService().getInputFieldValueCount(auth, id);
	// }
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}")
	public InputFieldSelectionSetValueOutVO getSelectionSetValue(@PathParam("id") Long id) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getInputFieldService().getSelectionSetValue(auth, id);
	}

	// @GET
	// @Produces({ MediaType.APPLICATION_JSON })
	// public Page<InputFieldSelectionSetValueOutVO> getSelectionSetValueList(@Context UriInfo uriInfo) throws AuthenticationException, AuthorisationException, ServiceException {
	// PSFUriPart psf;
	// return new Page<InputFieldSelectionSetValueOutVO>(WebUtil.getServiceLocator().getInputFieldService().getSelectionSetValueList(auth, null, psf = new PSFUriPart(uriInfo)),
	// psf);
	// }
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public InputFieldSelectionSetValueOutVO updateSelectionSetValue(InputFieldSelectionSetValueInVO in) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getInputFieldService().updateSelectionSetValue(auth, in);
	}
}
