package org.phoenixctms.ctsms.web.model.shared;

import java.util.Date;

import org.phoenixctms.ctsms.vo.CourseCategoryVO;
import org.phoenixctms.ctsms.vo.CourseInVO;
import org.phoenixctms.ctsms.vo.CourseOutVO;
import org.phoenixctms.ctsms.web.model.ScheduleEventBase;
import org.phoenixctms.ctsms.web.model.course.CourseBean;
import org.phoenixctms.ctsms.web.util.DateUtil;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.phoenixctms.ctsms.web.util.WebUtil.ColorOpacity;
import org.primefaces.model.ScheduleEvent;

public class CourseEvent extends ScheduleEventBase<CourseInVO> {

	private CourseOutVO out;
	protected final static ColorOpacity COLOR_OPACITY = ColorOpacity.ALPHA25;

	public CourseEvent() {
		super();
	}

	public CourseEvent(CourseInVO course) {
		super(course);
	}

	public CourseEvent(CourseOutVO course) {
		setOut(course);
	}

	public CourseEvent(ScheduleEvent event) {
		setEvent(event);
	}

	@Override
	protected boolean copyOutToIn() {
		if (out != null) {
			CourseBean.copyCourseOutToIn(in, out);
			return true;
		}
		return false;
	}

	@Override
	protected void copyToIn(CourseInVO source) {
		in.copy(source);
	}

	@Override
	protected void createIn() {
		in = new CourseInVO();
	}

	@Override
	public Date getEndDate() {
		return DateUtil.sanitizeScheduleDate(true, in.getStop());
	}

	public CourseOutVO getOut() {
		return out;
	}

	@Override
	public Date getStartDate() {
		return in.getStart() == null ? getEndDate() : DateUtil.sanitizeScheduleDate(true, in.getStart());
	}

	@Override
	public String getStyleClass() {
		CourseCategoryVO category = (out != null ? out.getCategory() : null);
		if (category != null && in.getCategoryId() != null && !in.getCategoryId().equals(category.getId())
				|| (category == null && in.getCategoryId() != null)) {
			category = WebUtil.getCourseCategory(in.getCategoryId());
		} else if (category != null && in.getCategoryId() == null) {
			category = null;
		}
		if (category != null) {
			StringBuilder sb = new StringBuilder();
			sb.append(WebUtil.colorToStyleClass(category.getColor(), COLOR_OPACITY));
			sb.append(" ");
			sb.append(WebUtil.SCHEDULE_EVENT_ICON_STYLECLASS);
			if (Settings.getBoolean(SettingCodes.SHOW_COURSE_SCHEDULE_EVENT_ICONS, Bundle.SETTINGS, DefaultSettings.SHOW_COURSE_SCHEDULE_EVENT_ICONS)) {
				sb.append(" ");
				sb.append(category.getNodeStyleClass());
			}
			return sb.toString();
		}
		return "";
	}

	@Override
	public String getTitle() {
		return in.getName();
	}

	@Override
	protected void initDefaultValues() {
		CourseBean.initCourseDefaultValues(in, null);
	}

	@Override
	public boolean isAllDay() {
		return true;
	}

	@Override
	public boolean isEditable() {
		return false;
	}

	public void setOut(CourseOutVO out) {
		this.out = out;
		initIn(InitSource.OUT);
	}
}
