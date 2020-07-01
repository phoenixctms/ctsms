package org.phoenixctms.ctsms.web.component.schedule;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import org.phoenixctms.ctsms.web.util.WebUtil;

@FacesRenderer(componentFamily = "org.primefaces", rendererType = "ctsms.Schedule")
public class ScheduleRenderer extends org.primefaces.component.schedule.ScheduleRenderer {

	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		Schedule schedule = (Schedule) component; //force org.primefaces.component.schedule.Schedule
		if (context.getExternalContext().getRequestParameterMap().containsKey(schedule.getClientId(context))) {
			encodeEvents(context, schedule);
		} else {
			encodeMarkup(context, schedule);
			encodeScript(context, schedule);
		}
	}

	protected void encodeScript(FacesContext context, Schedule schedule) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		String clientId = schedule.getClientId(context);
		startScript(writer, clientId);
		writer.write("$(function() {");
		writer.write("PrimeFaces.cw('Schedule','" + schedule.resolveWidgetVar() + "',{");
		writer.write("id:'" + clientId + "'");
		writer.write(",defaultView:'" + schedule.getView() + "'");
		writer.write(",locale:'" + schedule.calculateLocale(context) + "'");
		writer.write(",offset:" + schedule.calculateTimeZone().getRawOffset());
		if (schedule.getInitialDate() != null) {
			Calendar c = Calendar.getInstance();
			c.setTime((Date) schedule.getInitialDate());
			writer.write(",year: " + c.get(Calendar.YEAR));
			writer.write(",month: " + c.get(Calendar.MONTH));
			writer.write(",date: " + c.get(Calendar.DATE));
		}
		if (schedule.isShowHeader()) {
			writer.write(",header:{");
			writer.write("left:'" + schedule.getLeftHeaderTemplate() + "'");
			writer.write(",center:'" + schedule.getCenterHeaderTemplate() + "'");
			writer.write(",right:'" + schedule.getRightHeaderTemplate() + "'}");
		} else {
			writer.write(",header:false");
		}
		if (!schedule.isAllDaySlot())
			writer.write(",allDaySlot:false");
		if (schedule.getSlotMinutes() != 30)
			writer.write(",slotMinutes:" + schedule.getSlotMinutes());
		if (schedule.getFirstHour() != 6)
			writer.write(",firstHour:" + schedule.getFirstHour());
		if (schedule.getMinTime() != null)
			writer.write(",minTime:'" + schedule.getMinTime() + "'");
		if (schedule.getMaxTime() != null)
			writer.write(",maxTime:'" + schedule.getMaxTime() + "'");
		if (schedule.getAspectRatio() != null)
			writer.write(",aspectRatio: '" + schedule.getAspectRatio() + "'");
		if (!schedule.isShowWeekends())
			writer.write(",weekends:false");
		if (!schedule.isDraggable())
			writer.write(",disableDragging:true");
		if (!schedule.isResizable())
			writer.write(",disableResizing:true");
		if (schedule.getAxisFormat() != null)
			writer.write(",axisFormat:'" + schedule.getAxisFormat() + "'");
		if (schedule.getTimeFormat() != null)
			writer.write(",timeFormat:'" + schedule.getTimeFormat() + "'");
		if (schedule.getWeekNumberOnClick() != null)
			writer.write(",weekNumberOnClick:" + WebUtil.quoteJSString(schedule.getWeekNumberOnClick(), true));
		if (schedule.getDateOnClick() != null)
			writer.write(",dateOnClick:" + WebUtil.quoteJSString(schedule.getDateOnClick(), true));
		//behaviors
		encodeClientBehaviors(context, schedule);
		writer.write("});});");
		endScript(writer);
	}
}