package org.phoenixctms.ctsms.web.model.shared;

import java.util.Date;
import java.util.Iterator;

import org.phoenixctms.ctsms.vo.HolidayVO;
import org.phoenixctms.ctsms.web.model.ScheduleEventBase;
import org.phoenixctms.ctsms.web.util.DateUtil;
import org.primefaces.model.ScheduleEvent;

public class HolidayEvent extends ScheduleEventBase<HolidayVO> {

	private final static String EVENT_HOLIDAY_STYLECLASS = "ctsms-schedule-holiday-event";
	private final static String EVENT_NO_HOLIDAY_STYLECLASS = "ctsms-schedule-no-holiday-event";
	private final static String HOLIDAY_NAME_SEPARATOR = ", ";

	// private static void initHolidayDefaultValues(HolidayVO data) {
	// if (data != null) {
	// data.setDate(null);
	// data.setHoliday(false);
	// data.getNameL10nKeys().clear();
	// data.getNames().clear();
	// }
	// }

	public HolidayEvent() {
		super();
	}

	public HolidayEvent(HolidayVO holiday) {
		super(holiday);
	}

	public HolidayEvent(ScheduleEvent event) {
		super(event);
	}

	@Override
	protected boolean copyOutToIn() {
		return false;
	}

	@Override
	protected void copyToIn(HolidayVO source) {
		in.copy(source);
	}

	@Override
	protected void createIn() {
		in = new HolidayVO();
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
		if (in.isHoliday()) {
			return EVENT_HOLIDAY_STYLECLASS;
		} else {
			return EVENT_NO_HOLIDAY_STYLECLASS;
		}
	}

	@Override
	public String getTitle() {
		StringBuilder sb = new StringBuilder();
		Iterator<String> it = in.getNames().iterator();
		while (it.hasNext()) {
			if (sb.length() > 0) {
				sb.append(HOLIDAY_NAME_SEPARATOR);
			}
			sb.append(it.next());
		}
		return sb.toString();
	}

	@Override
	protected void initDefaultValues() {
		if (in != null) {
			in.setDate(null);
			in.setHoliday(false);
			in.getNameL10nKeys().clear();
			in.getNames().clear();
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
