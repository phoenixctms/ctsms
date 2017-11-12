package org.phoenixctms.ctsms.web.model.trial;

import org.phoenixctms.ctsms.vo.CourseOutVO;
import org.phoenixctms.ctsms.vo.InventoryBookingInVO;
import org.phoenixctms.ctsms.vo.InventoryBookingOutVO;
import org.phoenixctms.ctsms.web.model.shared.InventoryBookingEvent;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.phoenixctms.ctsms.web.util.WebUtil.ColorOpacity;
import org.primefaces.model.ScheduleEvent;

public class CourseInventoryBookingEvent extends InventoryBookingEvent {

	protected final static ColorOpacity COLOR_OPACITY = ColorOpacity.ALPHA25;

	public CourseInventoryBookingEvent() {
		super();
	}

	public CourseInventoryBookingEvent(InventoryBookingInVO booking) {
		super(booking);
	}

	public CourseInventoryBookingEvent(InventoryBookingOutVO booking) {
		super(booking);
	}

	public CourseInventoryBookingEvent(ScheduleEvent event) {
		super(event);
	}

	@Override
	public String getStyleClass() {
		// if (in.getCourseId() != null) {
		CourseOutVO course = WebUtil.getCourse(in.getCourseId(), null, null, null);
		if (course != null) {
			StringBuilder sb = new StringBuilder();
			sb.append(WebUtil.colorToStyleClass(course.getCategory().getColor(), COLOR_OPACITY));
			sb.append(" ");
			sb.append(WebUtil.SCHEDULE_EVENT_ICON_STYLECLASS);
			sb.append(" ");
			sb.append(course.getCategory().getNodeStyleClass());
			return sb.toString();
		}
		// }
		return "";
	}

	@Override
	public boolean isEditable() {
		return false;
	}
}
