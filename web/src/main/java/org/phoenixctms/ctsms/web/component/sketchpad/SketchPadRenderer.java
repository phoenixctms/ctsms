package org.phoenixctms.ctsms.web.component.sketchpad;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;

import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.component.tooltip.Tooltip;

@ResourceDependencies({
		@ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
		@ResourceDependency(library = "js", name = "json2.min.js", target = "head"),
		@ResourceDependency(library = "js", name = "raphael-2.2.0.min.js", target = "head"),
		@ResourceDependency(library = "js", name = "raphael.sketchpad.min.js", target = "head"),
		@ResourceDependency(library = "js", name = "jquery.colorPicker.min.js", target = "head"),
		@ResourceDependency(library = "js", name = "sketch.min.js", target = "head"),
		@ResourceDependency(library = "css", name = "colorPicker.min.css"),
		@ResourceDependency(library = "css", name = "sketch.min.css") })
@FacesRenderer(componentFamily = "javax.faces.Input", rendererType = "ctsms.SketchPad")
public class SketchPadRenderer extends Renderer {

	private static void endScript(ResponseWriter writer) throws IOException {
		writer.endElement("script");
	}

	private static void startScript(ResponseWriter writer, String id) throws IOException {
		writer.startElement("script", null);
		writer.writeAttribute("id", id, null);
		writer.writeAttribute("type", "text/javascript", null);
	}

	@Override
	public void decode(FacesContext context, UIComponent component) {
		SketchPad sketchPad = (SketchPad) component;
		if (sketchPad.isDisabled()) {
			return;
		}
		String submittedValue = context.getExternalContext().getRequestParameterMap().get(getInputId(sketchPad, context));
		if (submittedValue != null) {
			sketchPad.setSubmittedValue(submittedValue);
		}
	}

	private void encodeButton(FacesContext context, SketchPad sketchPad, String id, String styleClass, String tooltipText) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		String buttonId = getDivId(sketchPad, context) + "_" + id;
		writer.startElement("td", null);
		writer.startElement("div", null);
		writer.writeAttribute("id", buttonId, null);
		writer.writeAttribute("class", styleClass, null);
		writer.endElement("div");
		encodeTooltip(context, buttonId, tooltipText);
		writer.endElement("td");
	}

	private void encodeColorPicker(FacesContext context, SketchPad sketchPad) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		String colorPickerId = getInputId(sketchPad, context) + "_colorPicker";
		writer.startElement("td", null);
		writer.startElement("input", null);
		writer.writeAttribute("type", "text", null);
		writer.writeAttribute("id", colorPickerId, null);
		writer.writeAttribute("name", colorPickerId, null);
		writer.endElement("input");
		writer.endElement("td");
	}

	private void encodeDiv(FacesContext context, SketchPad sketchPad) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("div", null);
		writer.writeAttribute("id", getDivId(sketchPad, context), null);
		writer.writeAttribute("class", "sketchpad", null);
		writer.writeAttribute(
				"style",
				MessageFormat.format("width:{0}px;height:{1}px;{2}", Integer.toString(sketchPad.getWidth()), Integer.toString(sketchPad.getHeight()),
						sketchPad.getSketchPadStyle() != null ? sketchPad.getSketchPadStyle() : ""),
				null);
		writer.endElement("div");
	}

	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		SketchPad sketchPad = (SketchPad) component;
		encodeMarkup(context, sketchPad);
		encodeScript(context, sketchPad);
	}

	private void encodeInput(FacesContext context, SketchPad sketchPad) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		String inputId = getInputId(sketchPad, context);
		writer.startElement("input", sketchPad);
		writer.writeAttribute("type", "hidden", null);
		writer.writeAttribute("id", inputId, null);
		writer.writeAttribute("name", inputId, null);
		Object value = getValue(context, sketchPad);
		if (value != null) {
			writer.writeAttribute("value", value.toString(), null);
		}
		writer.endElement("input");
	}

	protected void encodeMarkup(FacesContext context, SketchPad sketchPad)
			throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		String clientId = sketchPad.getClientId(context);
		String styleClass = sketchPad.getStyleClass();
		styleClass = styleClass == null ? SketchPad.CONTAINER_CLASS
				: SketchPad.CONTAINER_CLASS + " " + styleClass;
		writer.startElement("span", null);
		writer.writeAttribute("id", clientId, null);
		writer.writeAttribute("class", styleClass, null);
		encodeInput(context, sketchPad);
		if (!sketchPad.isDisabled()) {
			writer.startElement("table", null);
			writer.writeAttribute("class", "sketch-toolbar-table", null);
			writer.startElement("tr", null);
			String strokesId = sketchPad.getStrokesId();
			boolean regionMode = true;
			if (!(strokesId != null && strokesId.length() > 0)) {
				encodeButton(context, sketchPad, "regionToggler",
						Settings.getBoolean(SettingCodes.SKETCH_REGIONS_VISIBLE, Bundle.SETTINGS, DefaultSettings.SKETCH_REGIONS_VISIBLE) ? "sketch-region-toggler-off"
								: "sketch-region-toggler-on",
						Messages.getString(MessageCodes.SKETCH_TOGGLE_REGION_TOOLTIP));
				regionMode = false;
			}
			encodeButton(context, sketchPad, "drawEraseMode", "sketch-draw-mode", Messages.getString(MessageCodes.SKETCH_DRAW_MODE_TOOLTIP));
			encodeButton(context, sketchPad, "undo", "sketch-undo-disabled", Messages.getString(MessageCodes.SKETCH_UNDO_TOOLTIP));
			encodeButton(context, sketchPad, "redo", "sketch-redo-disabled", Messages.getString(MessageCodes.SKETCH_REDO_TOOLTIP));
			encodeButton(context, sketchPad, "clear", "sketch-clear-disabled", Messages.getString(MessageCodes.SKETCH_CLEAR_TOOLTIP));
			writer.startElement("td", null);
			writer.writeText(" ", null);
			writer.endElement("td");
			encodeColorPicker(context, sketchPad);
			if (!regionMode) {
				encodeButton(context, sketchPad, "penWidth0", "sketch-pen-width-0-disabled", Messages.getString(MessageCodes.SKETCH_PEN_WIDTH_0_TOOLTIP));
				encodeButton(context, sketchPad, "penWidth1", "sketch-pen-width-1-disabled", Messages.getString(MessageCodes.SKETCH_PEN_WIDTH_1_TOOLTIP));
				encodeButton(context, sketchPad, "penWidth2", "sketch-pen-width-2", Messages.getString(MessageCodes.SKETCH_PEN_WIDTH_2_TOOLTIP));
				encodeButton(context, sketchPad, "penWidth3", "sketch-pen-width-3-disabled", Messages.getString(MessageCodes.SKETCH_PEN_WIDTH_3_TOOLTIP));
				encodeButton(context, sketchPad, "penOpacity0", "sketch-pen-opacity-0", Messages.getString(MessageCodes.SKETCH_PEN_OPACITY_0_TOOLTIP));
				encodeButton(context, sketchPad, "penOpacity1", "sketch-pen-opacity-1-disabled", Messages.getString(MessageCodes.SKETCH_PEN_OPACITY_1_TOOLTIP));
				encodeButton(context, sketchPad, "penOpacity2", "sketch-pen-opacity-2-disabled", Messages.getString(MessageCodes.SKETCH_PEN_OPACITY_2_TOOLTIP));
			}
			writer.endElement("tr");
			writer.endElement("table");
		}
		encodeDiv(context, sketchPad);
		writer.endElement("span");
	}

	private void encodeScript(FacesContext context, SketchPad sketchPad) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		startScript(writer, getScriptId(sketchPad, context));
		Object value = getValue(context, sketchPad);
		List<String> regions = sketchPad.getRegions();
		String strokesId = sketchPad.getStrokesId();
		String backgroundImage = sketchPad.getBackgroundImage();
		String onChange = sketchPad.getOnChange();
		String widgetVar = sketchPad.getWidgetVar();
		writer.write(MessageFormat.format(
				(widgetVar != null && widgetVar.length() > 0 ? "window[''" + widgetVar + "''] = " : "")
						+ "Sketch.initSketch(''{0}'', ''{1}'', ''{2}'', {3}, {4},{5}, {6}, {7}, {8}{9});",
				value == null ? "" : value.toString(),
				getInputId(sketchPad, context),
				getDivId(sketchPad, context),
				Integer.toString(sketchPad.getWidth()),
				Integer.toString(sketchPad.getHeight()),
				backgroundImage != null && backgroundImage.length() > 0 ? WebUtil.quoteJSString(backgroundImage, true) : "''",
				!sketchPad.isDisabled(),
				onChange != null && onChange.length() > 0 ? WebUtil.quoteJSString(onChange, true) : "''",
				strokesId != null && strokesId.length() > 0 ? WebUtil.quoteJSString(strokesId, true) : "''",
				regions != null && regions.size() > 0 ? ", " + WebUtil.escapeJSStringArray(regions, false, true) : ""));
		endScript(writer);
	}

	private void encodeTooltip(FacesContext context, String targetId, String tooltipText) throws IOException {
		if (Settings.getBoolean(SettingCodes.ENABLE_TOOLTIPS, Bundle.SETTINGS, DefaultSettings.ENABLE_TOOLTIPS)) {
			String tooltipId = targetId + "_tooltip";
			ResponseWriter writer = context.getResponseWriter();
			writer.startElement("div", null);
			writer.writeAttribute("id", tooltipId, null);
			writer.writeAttribute("class", Tooltip.CONTAINER_CLASS, null);
			writer.writeText(tooltipText, null);
			writer.endElement("div");
			startScript(writer, tooltipId + "_script");
			writer.write("$(function() {");
			writer.write("PrimeFaces.cw('Tooltip','" + "widget_" + tooltipId + "',{");
			writer.write("id:'" + tooltipId + "'");
			writer.write(",target:'" + targetId + "'");
			writer.write(",showEffect:'fade'");
			writer.write(",hideEffect:'fade'");
			writer.write("});});");
			endScript(writer);
		}
	}

	@Override
	public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue)
			throws ConverterException {
		Converter converter = getConverter(context, component);
		if (converter != null) {
			return converter.getAsObject(context, component, (String) submittedValue);
		} else {
			return submittedValue;
		}
	}

	private Converter getConverter(FacesContext context, UIComponent component) {
		Converter converter = ((UIInput) component).getConverter();
		if (converter != null) {
			return converter;
		}
		ValueExpression exp = component.getValueExpression("value");
		if (exp == null) {
			return null;
		}
		Class valueType = exp.getType(context.getELContext());
		if (valueType == null) {
			return null;
		}
		return context.getApplication().createConverter(valueType);
	}

	public String getDivId(SketchPad sketchPad, FacesContext context) {
		return sketchPad.getClientId(context) + "_div";
	}

	public String getInputId(SketchPad sketchPad, FacesContext context) {
		return sketchPad.getClientId(context) + "_input";
	}

	public String getScriptId(SketchPad sketchPad, FacesContext context) {
		return sketchPad.getClientId(context) + "_script";
	}

	private Object getValue(FacesContext context, SketchPad sketchPad) {
		Object submittedValue = sketchPad.getSubmittedValue();
		if (submittedValue != null) {
			return submittedValue;
		}
		Object value = sketchPad.getValue();
		Converter converter = getConverter(context, sketchPad);
		if (converter != null) {
			return converter.getAsString(context, sketchPad, value);
		} else if (value != null) {
			return value.toString();
		} else {
			return "";
		}
	}
}
