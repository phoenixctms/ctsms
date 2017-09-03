package org.phoenixctms.ctsms.web.model;

import org.phoenixctms.ctsms.enumeration.VariablePeriod;

public interface VariablePeriodSelectorListener {

	public final static VariablePeriod NO_SELECTION_VARIABLE_PERIOD = VariablePeriod.EXPLICIT;

	public VariablePeriod getPeriod(int property);

	public void setPeriod(int property, VariablePeriod period);
}
