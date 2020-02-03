package org.phoenixctms.ctsms.web;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.phoenixctms.ctsms.web.util.GetParamNames;

public final class ValidatorUtil {

	// http://javalabor.blogspot.co.at/2012/02/jsf-2-conditional-validation.html
	private final static ArrayList<String> VALIDATION_REQUIRED_COMPONENT_ID_PREFIXES = new ArrayList<String>();
	static {
		VALIDATION_REQUIRED_COMPONENT_ID_PREFIXES.add(":update");
		VALIDATION_REQUIRED_COMPONENT_ID_PREFIXES.add(":add");
		VALIDATION_REQUIRED_COMPONENT_ID_PREFIXES.add(":bulk");
		VALIDATION_REQUIRED_COMPONENT_ID_PREFIXES.add(":check");
	}

	public static boolean skipValidation() {
		return skipValidation(FacesContext.getCurrentInstance());
	}

	public static boolean skipValidation(final FacesContext context) {
		final ExternalContext externalContext = context.getExternalContext();
		final Object validate = externalContext.getRequestMap().get(GetParamNames.VALIDATE.toString());
		if (validate != null) {
			return !(Boolean) validate;
		}
		Iterator<Entry<String, String[]>> parameterValuesIt = externalContext.getRequestParameterValuesMap()
				.entrySet().iterator();
		while (parameterValuesIt.hasNext()) {
			Entry<String, String[]> parameterValues = parameterValuesIt.next();
			final String key = parameterValues.getKey();
			Iterator<String> idPrefixIt = VALIDATION_REQUIRED_COMPONENT_ID_PREFIXES.iterator();
			while (idPrefixIt.hasNext()) {
				String idPrefix = idPrefixIt.next();
				if (key.contains(idPrefix)) {
					externalContext.getRequestMap().put(GetParamNames.VALIDATE.toString(), Boolean.TRUE);
					return false;
				}
			}
		}
		externalContext.getRequestMap().put(GetParamNames.VALIDATE.toString(), Boolean.FALSE);
		return true;
	}
}