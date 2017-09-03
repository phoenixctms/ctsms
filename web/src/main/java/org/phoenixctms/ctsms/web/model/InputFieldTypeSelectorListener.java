package org.phoenixctms.ctsms.web.model;

import org.phoenixctms.ctsms.enumeration.InputFieldType;

public interface InputFieldTypeSelectorListener {

	public final static InputFieldType NO_SELECTION_INPUT_FIELD_TYPE = null;

	public InputFieldType getType(int property);

	public void setType(int property, InputFieldType fieldType);
}
