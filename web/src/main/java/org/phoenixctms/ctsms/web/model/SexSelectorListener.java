package org.phoenixctms.ctsms.web.model;

import org.phoenixctms.ctsms.enumeration.Sex;

public interface SexSelectorListener {

	public final static Sex NO_SELECTION_SEX = null;

	public Sex getSex(int property);

	public void setSex(int property, Sex sex);
}
