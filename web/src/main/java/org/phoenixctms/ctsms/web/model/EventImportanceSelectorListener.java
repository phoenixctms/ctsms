package org.phoenixctms.ctsms.web.model;

import org.phoenixctms.ctsms.enumeration.EventImportance;

public interface EventImportanceSelectorListener {

	public final static EventImportance NO_SELECTION_EVENT_IMPORTANCE = null;

	public EventImportance getImportance(int property);

	public void setImportance(int property, EventImportance importance);
}
