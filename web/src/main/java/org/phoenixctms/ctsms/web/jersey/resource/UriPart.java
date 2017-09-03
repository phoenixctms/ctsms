package org.phoenixctms.ctsms.web.jersey.resource;

import java.util.Set;

import javax.ws.rs.core.MultivaluedMap;

public interface UriPart {

	public Set<NamedParameter> getStaticQueryParameterNames() throws Exception;

	public boolean isSlurpQueryParameter() throws Exception;

	public void setSlurpQueryParameter(boolean slurp);

	public Object shiftParameters(MultivaluedMap<String, String> queryParameters) throws Exception;
}
