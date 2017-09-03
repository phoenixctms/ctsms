package org.phoenixctms.ctsms.web.model;

import java.util.ArrayList;

import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.InputFieldType;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class InputFieldTypeSelector {

	private InputFieldTypeSelectorListener bean;
	private int property;
	private ArrayList<SelectItem> fieldTypes;
	private final static String NO_SELECTION_STRING = CommonUtil.NO_SELECTION_VALUE;

	public InputFieldTypeSelector(InputFieldTypeSelectorListener bean, int property) {
		this.bean = bean;
		this.property = property;
		fieldTypes = WebUtil.getInputFieldTypes();
	}

	public InputFieldTypeSelectorListener getBean() {
		return bean;
	}

	public ArrayList<SelectItem> getInputFieldTypes() {
		return fieldTypes;
	}

	public String getType() {
		if (bean != null) {
			InputFieldType fieldType = bean.getType(property);
			if (fieldType != null) {
				return fieldType.name();
			} else {
				return NO_SELECTION_STRING;
			}
		}
		return NO_SELECTION_STRING;
	}

	public void setBean(InputFieldTypeSelectorListener bean) {
		this.bean = bean;
	}

	public void setType(String name) {
		if (bean != null) {
			InputFieldType fieldType;
			if (name != null && !name.equals(NO_SELECTION_STRING)) {
				try {
					fieldType = InputFieldType.fromString(name);
				} catch (Exception e) {
					fieldType = InputFieldTypeSelectorListener.NO_SELECTION_INPUT_FIELD_TYPE;
				}
			} else {
				fieldType = InputFieldTypeSelectorListener.NO_SELECTION_INPUT_FIELD_TYPE;
			}
			bean.setType(property, fieldType);
		}
	}
}
