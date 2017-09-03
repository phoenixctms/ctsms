package org.phoenixctms.ctsms.web;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.BeanValidator;
import javax.faces.validator.FacesValidator;

@FacesValidator(value = SkipBeanValidator.VALIDATOR_ID)
public class SkipBeanValidator extends BeanValidator {

	public static final String VALIDATOR_ID = "ctsms.SkipBean";

	@Override
	public void validate(final FacesContext context, final UIComponent component, final Object value) {
		if (!ValidatorUtil.skipValidation(context)) {
			super.validate(context, component, value);
		}
	}
}