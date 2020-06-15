package org.phoenixctms.ctsms.web.model;

import org.phoenixctms.ctsms.enumeration.VisitScheduleDateMode;

public interface VisitScheduleDateModeSelectorListener {

	public final static VisitScheduleDateMode NO_SELECTION_VISIT_SCHEDULE_DATE_MODE = null;

	public VisitScheduleDateMode getVisitScheduleDateMode(int property);

	public void setVisitScheduleDateMode(int property, VisitScheduleDateMode visitScheduleDateMode);
}
