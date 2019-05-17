package org.phoenixctms.ctsms.web.conversion;

import javax.faces.convert.FacesConverter;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.web.util.WebUtil;

@FacesConverter(value = LdapEntry2Converter.CONVERTER_ID)
public class LdapEntry2Converter extends LdapEntryConverter {

	public static final String CONVERTER_ID = "ctsms.LdapEntry2";

	@Override
	protected Object getLdapEntry(String username) {
		try {
			return WebUtil.getServiceLocator().getToolsService().getLdapEntry2(WebUtil.getAuthentication(), username);
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
		return null;
	}
}