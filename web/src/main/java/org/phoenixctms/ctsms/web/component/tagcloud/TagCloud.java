package org.phoenixctms.ctsms.web.component.tagcloud;

/*
 * Generated, Do Not Modify
 */
/*
 * Copyright 2010 Prime Technology.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;

@FacesComponent("ctsms.TagCloud")
public class TagCloud extends UIOutput implements org.primefaces.component.api.Widget {

	protected enum PropertyKeys {
		widgetVar
		, model
		, style
		, styleClass;

		String toString;

		PropertyKeys() {
		}

		PropertyKeys(String toString) {
			this.toString = toString;
		}

		@Override
		public String toString() {
			return ((this.toString != null) ? this.toString : super.toString());
		}
	}

	public static final String COMPONENT_TYPE = "ctsms.TagCloud";
	private static final String DEFAULT_RENDERER = "ctsms.TagCloud";
	public final static String STYLE_CLASS = "ui-tagcloud ui-widget ui-widget-content ui-corner-all";

	public TagCloud() {
		setRendererType(DEFAULT_RENDERER);
	}

	@Override
	protected FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	public TagCloudModel getModel() {
		return (TagCloudModel) getStateHelper().eval(PropertyKeys.model, null);
	}

	public java.lang.String getStyle() {
		return (java.lang.String) getStateHelper().eval(PropertyKeys.style, null);
	}

	public java.lang.String getStyleClass() {
		return (java.lang.String) getStateHelper().eval(PropertyKeys.styleClass, null);
	}

	public java.lang.String getWidgetVar() {
		return (java.lang.String) getStateHelper().eval(PropertyKeys.widgetVar, null);
	}

	@Override
	public String resolveWidgetVar() {
		FacesContext context = FacesContext.getCurrentInstance();
		String userWidgetVar = (String) getAttributes().get("widgetVar");
		if (userWidgetVar != null)
			return userWidgetVar;
		else
			return "widget_" + getClientId(context).replaceAll("-|" + UINamingContainer.getSeparatorChar(context), "_");
	}

	public void setModel(TagCloudModel _model) {
		getStateHelper().put(PropertyKeys.model, _model);
	}

	public void setStyle(java.lang.String _style) {
		getStateHelper().put(PropertyKeys.style, _style);
	}

	public void setStyleClass(java.lang.String _styleClass) {
		getStateHelper().put(PropertyKeys.styleClass, _styleClass);
	}

	public void setWidgetVar(java.lang.String _widgetVar) {
		getStateHelper().put(PropertyKeys.widgetVar, _widgetVar);
	}
}
