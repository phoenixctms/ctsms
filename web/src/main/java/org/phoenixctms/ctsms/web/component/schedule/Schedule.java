package org.phoenixctms.ctsms.web.component.schedule;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.context.FacesContext;

@ResourceDependencies({
		@ResourceDependency(library = "primefaces", name = "primefaces.css"),
		@ResourceDependency(library = "primefaces", name = "schedule/schedule.css"),
		@ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
		@ResourceDependency(library = "primefaces", name = "primefaces.js"),
		@ResourceDependency(library = "primefaces", name = "schedule/schedule.js")
})
@FacesComponent("ctsms.Schedule")
public class Schedule extends org.primefaces.component.schedule.Schedule {

	public static final String COMPONENT_TYPE = "ctsms.Schedule";
	private static final String DEFAULT_RENDERER = "ctsms.Schedule";

	protected enum PropertyKeys {

		widgetVar,
		value,
		locale,
		aspectRatio,
		view,
		initialDate,
		showWeekends,
		style,
		styleClass,
		draggable,
		resizable,
		showHeader,
		leftHeaderTemplate,
		centerHeaderTemplate,
		rightHeaderTemplate,
		allDaySlot,
		slotMinutes,
		firstHour,
		minTime,
		maxTime,
		axisFormat,
		timeFormat,
		timeZone,
		weekNumberOnClick,
		dateOnClick;

		String toString;

		PropertyKeys(String toString) {
			this.toString = toString;
		}

		PropertyKeys() {
		}

		public String toString() {
			return ((this.toString != null) ? this.toString : super.toString());
		}
	}

	public Schedule() {
		super();
		setRendererType(DEFAULT_RENDERER);
	}

	public java.lang.String getWeekNumberOnClick() {
		return (java.lang.String) getStateHelper().eval(PropertyKeys.weekNumberOnClick, null);
	}

	public void setWeekNumberOnClick(java.lang.String _weekNumberOnClick) {
		getStateHelper().put(PropertyKeys.weekNumberOnClick, _weekNumberOnClick);
		handleAttribute("weekNumberOnClick", _weekNumberOnClick);
	}

	public java.lang.String getDateOnClick() {
		return (java.lang.String) getStateHelper().eval(PropertyKeys.dateOnClick, null);
	}

	public void setDateOnClick(java.lang.String _dateOnClick) {
		getStateHelper().put(PropertyKeys.dateOnClick, _dateOnClick);
		handleAttribute("dateOnClick", _dateOnClick);
	}

	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	private java.util.Locale appropriateLocale;

	java.util.Locale calculateLocale(FacesContext facesContext) {
		if (appropriateLocale == null) {
			Object userLocale = getLocale();
			if (userLocale != null) {
				if (userLocale instanceof String)
					appropriateLocale = new java.util.Locale((String) userLocale, "");
				else if (userLocale instanceof java.util.Locale)
					appropriateLocale = (java.util.Locale) userLocale;
				else
					throw new IllegalArgumentException("Type:" + userLocale.getClass() + " is not a valid locale type for calendar:" + this.getClientId(facesContext));
			} else {
				appropriateLocale = facesContext.getViewRoot().getLocale();
			}
		}
		return appropriateLocale;
	}
}