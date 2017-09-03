package org.phoenixctms.ctsms.web.component.sketchpad;

import java.util.List;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIInput;

@FacesComponent("ctsms.SketchPad")
public class SketchPad extends UIInput {

	enum PropertyKeys {
		width,
		height,
		disabled,
		sketchPadStyle,
		backgroundImage,
		styleClass,
		strokesId,
		onChange,
		widgetVar,
		regions
	}

	public static final String COMPONENT_TYPE = "ctsms.SketchPad";
	public static final String DEFAULT_RENDERER = "ctsms.SketchPad";
	public final static String CONTAINER_CLASS = "sketch_container";

	public SketchPad() {
		setRendererType(DEFAULT_RENDERER);
	}

	public String getBackgroundImage() {
		return (java.lang.String) getStateHelper().eval(PropertyKeys.backgroundImage);
	}

	public int getHeight() {
		return (Integer) getStateHelper().eval(PropertyKeys.height, 200);
	}

	public String getOnChange() {
		return (java.lang.String) getStateHelper().eval(PropertyKeys.onChange);
	}

	public List<String> getRegions() {
		return (List<String>) getStateHelper().eval(PropertyKeys.regions);
	}

	public String getSketchPadStyle() {
		return (java.lang.String) getStateHelper().eval(PropertyKeys.sketchPadStyle);
	}

	public String getStrokesId() {
		return (java.lang.String) getStateHelper().eval(PropertyKeys.strokesId);
	}

	public String getStyleClass() {
		return (java.lang.String) getStateHelper().eval(PropertyKeys.styleClass);
	}

	public String getWidgetVar() {
		return (java.lang.String) getStateHelper().eval(PropertyKeys.widgetVar);
	}

	public int getWidth() {
		return (Integer) getStateHelper().eval(PropertyKeys.width, 200);
	}

	public boolean isDisabled() {
		return (Boolean) getStateHelper().eval(PropertyKeys.disabled, false);
	}

	public void setBackgroundImage(String backgroundImage) {
		getStateHelper().put(PropertyKeys.backgroundImage, backgroundImage);
	}

	public void setDisabled(boolean disabled) {
		getStateHelper().put(PropertyKeys.disabled, disabled);
	}

	public void setHeight(int height) {
		getStateHelper().put(PropertyKeys.height, height);
	}

	public void setOnChange(String onChange) {
		getStateHelper().put(PropertyKeys.onChange, onChange);
	}

	public void setRegions(List<String> regions) {
		getStateHelper().put(PropertyKeys.regions, regions);
	}

	public void setSketchPadStyle(String sketchPadStyle) {
		getStateHelper().put(PropertyKeys.sketchPadStyle, sketchPadStyle);
	}

	public void setStrokesId(String strokesId) {
		getStateHelper().put(PropertyKeys.strokesId, strokesId);
	}

	public void setStyleClass(String styleClass) {
		getStateHelper().put(PropertyKeys.styleClass, styleClass);
	}

	public void setWidgetVar(String widgetVar) {
		getStateHelper().put(PropertyKeys.widgetVar, widgetVar);
	}

	public void setWidth(int width) {
		getStateHelper().put(PropertyKeys.width, width);
	}
}
