package org.phoenixctms.ctsms.web;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.RequiredValidator;

@FacesValidator(value = SkipRequiredValidator.VALIDATOR_ID)
public class SkipRequiredValidator extends RequiredValidator {

	public static final String VALIDATOR_ID = "ctsms.SkipRequired";

	private static boolean isRequired(UIComponent component) {
		if (component instanceof EditableValueHolder) {
			return ((EditableValueHolder) component).isRequired();
		}
		return false;
	}

	@Override
	public void validate(final FacesContext context, final UIComponent component, final Object value) {
		if (isRequired(component) && !ValidatorUtil.skipValidation(context)) {
			super.validate(context, component, value);
		}
	}
}