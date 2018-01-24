package org.phoenixctms.ctsms.web.model.shared;

import java.util.Date;

import org.phoenixctms.ctsms.enumeration.Color;
import org.phoenixctms.ctsms.vo.DateCountVO;
import org.phoenixctms.ctsms.web.model.ScheduleEventBase;
import org.phoenixctms.ctsms.web.util.DateUtil;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.phoenixctms.ctsms.web.util.WebUtil.ColorOpacity;
import org.primefaces.model.ScheduleEvent;

public class StaffNaCountEvent extends ScheduleEventBase<DateCountVO> {

	// private final static String EVENT_STAFF_NA_COUNT_STYLECLASS = "ctsms-schedule-staffnacount-event";
	// private final static String EVENT_EXCEEDED_STAFF_NA_COUNT_STYLECLASS = "ctsms-schedule-exceeded-staffnacount-event";
	private final static Color EVENT_COLOR = Color.LIGHTGRAY;
	private final static Color EXCEEDED_EVENT_COLOR = Color.LIGHTPINK;
	protected final static ColorOpacity COLOR_OPACITY = ColorOpacity.ALPHA50;

	// private static void initStaffNaCountDefaultValues(DateCountVO data) {
	// if (data != null) {
	// data.setDate(null);
	// data.setCount(0l);
	// }
	// }

	public StaffNaCountEvent() {
		super();
	}

	public StaffNaCountEvent(DateCountVO count) {
		super(count);
	}

	public StaffNaCountEvent(ScheduleEvent event) {
		super(event);
	}

	@Override
	protected boolean copyOutToIn() {
		return false;
	}

	@Override
	protected void copyToIn(DateCountVO source) {
		in.copy(source);
	}

	@Override
	protected void createIn() {
		in = new DateCountVO();
	}

	@Override
	public Date getEndDate() {
		return this.getStartDate();
	}

	@Override
	public Date getStartDate() {
		return DateUtil.sanitizeScheduleDate(true, in.getDate());
	}

	@Override
	public String getStyleClass() {
		Long staffNaCountLimit = Settings.getLongNullable(SettingCodes.DUTY_ROSTER_SCHEDULE_STAFF_NA_COUNT_LIMIT, Bundle.SETTINGS,
				DefaultSettings.DUTY_ROSTER_SCHEDULE_STAFF_NA_COUNT_LIMIT);
		StringBuilder sb = new StringBuilder();
		if (staffNaCountLimit != null && staffNaCountLimit > 0l && in.getCount() >= staffNaCountLimit) {
			sb.append(WebUtil.colorToStyleClass(EXCEEDED_EVENT_COLOR, COLOR_OPACITY));
			sb.append(" ");
			sb.append(WebUtil.SCHEDULE_EVENT_ICON_STYLECLASS);
			sb.append(" ");
			sb.append(WebUtil.COLLISION_ICON_STYLECLASS);
		} else {
			sb.append(WebUtil.colorToStyleClass(EVENT_COLOR, COLOR_OPACITY));
			sb.append(" ");
			sb.append(WebUtil.SCHEDULE_EVENT_ICON_STYLECLASS);
		}
		return sb.toString();
	}

	@Override
	public String getTitle() {
		return Messages.getMessage(MessageCodes.STAFF_NA_COUNT_EVENT_TITLE, in.getCount());
	}

	@Override
	protected void initDefaultValues() {
		if (in != null) {
			in.setDate(null);
			in.setCount(0l);
		}
	}

	@Override
	public boolean isAllDay() {
		return true;
	}

	@Override
	public boolean isEditable() {
		return false;
	}
}
