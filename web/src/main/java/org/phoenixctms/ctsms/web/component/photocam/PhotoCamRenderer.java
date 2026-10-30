package org.phoenixctms.ctsms.web.component.photocam;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.PhaseId;
import javax.faces.render.FacesRenderer;

import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.event.CaptureEvent;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.ComponentUtils;

@ResourceDependencies({
		@ResourceDependency(library = "primefaces", name = "primefaces.css"),
		@ResourceDependency(library = "css", name = "photoCam.min.css"),
		@ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
		@ResourceDependency(library = "primefaces", name = "primefaces.js"),
		@ResourceDependency(library = "js", name = "photoCam.min.js")
})
@FacesRenderer(componentFamily = "javax.faces.Input", rendererType = "ctsms.PhotoCam")
public class PhotoCamRenderer extends CoreRenderer {

	private final static String DATA_URL_PREFIX = "data:image/png;base64,";

	private static String getDataParameterName(PhotoCam cam, FacesContext context) {
		return cam.getClientId(context) + "_data";
	}

	private static void writeMessage(ResponseWriter writer, boolean first, String name, String messageCode) throws IOException {
		writer.write((first ? "" : ",") + name + ":" + WebUtil.quoteJSString(Messages.getString(messageCode), true));
	}

	@Override
	public void decode(FacesContext context, UIComponent component) {
		PhotoCam cam = (PhotoCam) component;
		Map<String, String> params = context.getExternalContext().getRequestParameterMap();
		String dataUrl = params.get(getDataParameterName(cam, context));
		if (dataUrl != null && dataUrl.startsWith(DATA_URL_PREFIX)) {
			String image = dataUrl.substring(DATA_URL_PREFIX.length());
			byte[] data;
			try {
				data = Base64.getMimeDecoder().decode(image);
			} catch (IllegalArgumentException e) {
				return;
			}
			if (data != null && data.length > 0) {
				CaptureEvent event = new CaptureEvent(cam, data, image);
				event.setPhaseId(PhaseId.INVOKE_APPLICATION);
				cam.queueEvent(event);
			}
		}
	}

	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		PhotoCam cam = (PhotoCam) component;
		encodeMarkup(context, cam);
		encodeScript(context, cam);
	}

	protected void encodeMarkup(FacesContext context, PhotoCam cam) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		String clientId = cam.getClientId(context);
		String styleClass = cam.getStyleClass();
		styleClass = styleClass == null ? PhotoCam.CONTAINER_CLASS : PhotoCam.CONTAINER_CLASS + " " + styleClass;
		writer.startElement("div", cam);
		writer.writeAttribute("id", clientId, "id");
		writer.writeAttribute("class", styleClass, "styleClass");
		if (cam.getStyle() != null) {
			writer.writeAttribute("style", cam.getStyle(), "style");
		}
		writer.startElement("select", null);
		writer.writeAttribute("id", clientId + "_devices", null);
		writer.writeAttribute("class", "ctsms-photocam-devices", null);
		writer.writeAttribute("style", "display:none", null);
		writer.endElement("select");
		writer.startElement("div", null);
		writer.writeAttribute("class", "ctsms-photocam-viewport", null);
		writer.writeAttribute("style", "width:" + cam.getDisplayWidth() + "px;aspect-ratio:" + cam.getWidth() + " / " + cam.getHeight() + ";", null);
		writer.startElement("video", null);
		writer.writeAttribute("id", clientId + "_video", null);
		writer.writeAttribute("class", "ctsms-photocam-video", null);
		writer.writeAttribute("autoplay", "autoplay", null);
		writer.writeAttribute("playsinline", "playsinline", null);
		writer.writeAttribute("muted", "muted", null);
		writer.writeAttribute("width", cam.getWidth(), null);
		writer.writeAttribute("height", cam.getHeight(), null);
		writer.endElement("video");
		writer.startElement("span", null);
		writer.writeAttribute("id", clientId + "_status", null);
		writer.writeAttribute("class", "ctsms-photocam-status", null);
		writer.writeAttribute("style", "display:none", null);
		writer.endElement("span");
		writer.endElement("div");
		writer.endElement("div");
	}

	protected void encodeScript(FacesContext context, PhotoCam cam) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		String clientId = cam.getClientId(context);
		startScript(writer, clientId);
		writer.write("$(function() {");
		writer.write("PrimeFaces.cw('PhotoCam','" + cam.resolveWidgetVar() + "',{");
		writer.write("id:'" + clientId + "'");
		writer.write(",dataParam:'" + getDataParameterName(cam, context) + "'");
		writer.write(",width:" + cam.getWidth());
		writer.write(",height:" + cam.getHeight());
		if (cam.getUpdate() != null) {
			writer.write(",update:'" + ComponentUtils.findClientIds(context, cam, cam.getUpdate()) + "'");
		}
		if (cam.getProcess() != null) {
			writer.write(",process:'" + ComponentUtils.findClientIds(context, cam, cam.getProcess()) + "'");
		}
		writer.write(",msgs:{");
		writeMessage(writer, true, "notSupported", MessageCodes.PHOTO_CAM_NOT_SUPPORTED);
		writeMessage(writer, false, "insecureContext", MessageCodes.PHOTO_CAM_INSECURE_CONTEXT);
		writeMessage(writer, false, "permissionDenied", MessageCodes.PHOTO_CAM_PERMISSION_DENIED);
		writeMessage(writer, false, "noDevice", MessageCodes.PHOTO_CAM_NO_DEVICE);
		writeMessage(writer, false, "notReadable", MessageCodes.PHOTO_CAM_NOT_READABLE);
		writeMessage(writer, false, "notReady", MessageCodes.PHOTO_CAM_NOT_READY);
		writeMessage(writer, false, "error", MessageCodes.PHOTO_CAM_ERROR);
		writeMessage(writer, false, "device", MessageCodes.PHOTO_CAM_DEVICE_LABEL);
		writer.write("}");
		writer.write("});});");
		endScript(writer);
	}
}
