package org.phoenixctms.ctsms.web.component.photocam;

import javax.el.MethodExpression;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;

import org.primefaces.event.CaptureEvent;

@FacesComponent("ctsms.PhotoCam")
public class PhotoCam extends UIInput implements org.primefaces.component.api.Widget {

	protected enum PropertyKeys {
		widgetVar, style, styleClass, process, update, listener, width, height, displayWidth;

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

	public static final String COMPONENT_TYPE = "ctsms.PhotoCam";
	private static final String DEFAULT_RENDERER = "ctsms.PhotoCam";
	public final static String CONTAINER_CLASS = "ctsms-photocam";
	public final static int DEFAULT_WIDTH = 640;
	public final static int DEFAULT_HEIGHT = 480;

	public PhotoCam() {
		setRendererType(DEFAULT_RENDERER);
	}

	@Override
	public void broadcast(FacesEvent event) throws AbortProcessingException {
		super.broadcast(event);
		FacesContext facesContext = FacesContext.getCurrentInstance();
		MethodExpression me = getListener();
		if (me != null && event instanceof CaptureEvent) {
			me.invoke(facesContext.getELContext(), new Object[] { event });
		}
	}

	@Override
	protected FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	public int getDisplayWidth() {
		return (java.lang.Integer) getStateHelper().eval(PropertyKeys.displayWidth, getWidth());
	}

	public int getHeight() {
		return (java.lang.Integer) getStateHelper().eval(PropertyKeys.height, DEFAULT_HEIGHT);
	}

	public MethodExpression getListener() {
		return (MethodExpression) getStateHelper().eval(PropertyKeys.listener, null);
	}

	public java.lang.String getProcess() {
		return (java.lang.String) getStateHelper().eval(PropertyKeys.process, null);
	}

	public java.lang.String getStyle() {
		return (java.lang.String) getStateHelper().eval(PropertyKeys.style, null);
	}

	public java.lang.String getStyleClass() {
		return (java.lang.String) getStateHelper().eval(PropertyKeys.styleClass, null);
	}

	public java.lang.String getUpdate() {
		return (java.lang.String) getStateHelper().eval(PropertyKeys.update, null);
	}

	public java.lang.String getWidgetVar() {
		return (java.lang.String) getStateHelper().eval(PropertyKeys.widgetVar, null);
	}

	public int getWidth() {
		return (java.lang.Integer) getStateHelper().eval(PropertyKeys.width, DEFAULT_WIDTH);
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

	public void setDisplayWidth(int _displayWidth) {
		getStateHelper().put(PropertyKeys.displayWidth, _displayWidth);
	}

	public void setHeight(int _height) {
		getStateHelper().put(PropertyKeys.height, _height);
	}

	public void setListener(MethodExpression _listener) {
		getStateHelper().put(PropertyKeys.listener, _listener);
	}

	public void setProcess(java.lang.String _process) {
		getStateHelper().put(PropertyKeys.process, _process);
	}

	public void setStyle(java.lang.String _style) {
		getStateHelper().put(PropertyKeys.style, _style);
	}

	public void setStyleClass(java.lang.String _styleClass) {
		getStateHelper().put(PropertyKeys.styleClass, _styleClass);
	}

	public void setUpdate(java.lang.String _update) {
		getStateHelper().put(PropertyKeys.update, _update);
	}

	public void setWidgetVar(java.lang.String _widgetVar) {
		getStateHelper().put(PropertyKeys.widgetVar, _widgetVar);
	}

	public void setWidth(int _width) {
		getStateHelper().put(PropertyKeys.width, _width);
	}
}
