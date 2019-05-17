package org.phoenixctms.ctsms.web.conversion;

import javax.faces.convert.FacesConverter;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.web.util.WebUtil;

@FacesConverter(value = LdapEntry1Converter.CONVERTER_ID)
public class LdapEntry1Converter extends LdapEntryConverter {

	public static final String CONVERTER_ID = "ctsms.LdapEntry1";

	@Override
	protected Object getLdapEntry(String username) {
		try {
			return WebUtil.getServiceLocator().getToolsService().getLdapEntry1(WebUtil.getAuthentication(), username);
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
		}
		return null;
	}
}