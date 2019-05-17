package org.phoenixctms.ctsms.web.model.course;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.phoenixctms.ctsms.enumeration.VariablePeriod;
import org.phoenixctms.ctsms.vo.CourseOutVO;
import org.phoenixctms.ctsms.web.component.datatable.DataTable;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.model.VariablePeriodSelector;
import org.phoenixctms.ctsms.web.model.VariablePeriodSelectorListener;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;

@ManagedBean
@ViewScoped
public class ExpiringCourseOverviewBean extends ManagedBeanBase implements VariablePeriodSelectorListener {

	private static final int REMINDER_PERIOD_PROPERTY_ID = 1;
	private Date today;
	private ExpiringCourseLazyModel expiringCourseModel;
	private VariablePeriodSelector reminder;

	public ExpiringCourseOverviewBean() {
		super();
		today = new Date();
		expiringCourseModel = new ExpiringCourseLazyModel();
		setReminder(new VariablePeriodSelector(this, REMINDER_PERIOD_PROPERTY_ID));
	}

	public String courseToColor(CourseOutVO course) {
		if (course != null) {
			return WebUtil.expirationToColor(today, course.getExpiration(), Settings.getCourseExpirationDueInColorMap(),
					Settings.getColor(SettingCodes.COURSE_EXPIRATION_OVERDUE_COLOR, Bundle.SETTINGS, DefaultSettings.COURSE_EXPIRATION_OVERDUE_COLOR));
		}
		return "";
	}

	public String getCourseDueInString(CourseOutVO course) {
		return WebUtil.getExpirationDueInString(today, course == null ? null : course.getExpiration());
	}

	public ExpiringCourseLazyModel getExpiringCourseModel() {
		return expiringCourseModel;
	}

	@Override
	public VariablePeriod getPeriod(int property) {
		switch (property) {
			case REMINDER_PERIOD_PROPERTY_ID:
				return expiringCourseModel.getReminderPeriod();
			default:
				return VariablePeriodSelectorListener.NO_SELECTION_VARIABLE_PERIOD;
		}
	}

	public VariablePeriodSelector getReminder() {
		return reminder;
	}

	public void handleReminderChange() {
		initSets();
	}

	@PostConstruct
	private void init() {
		initIn();
		initSets();
	}

	private void initIn() {
	}

	private void initSets() {
		today = new Date();
		expiringCourseModel.updateRowCount();
		DataTable.clearFilters("expiringcourse_list");
	}

	@Override
	public boolean isCreateable() {
		return false;
	}

	@Override
	public boolean isCreated() {
		return false;
	}

	public boolean isReminderPeriodSpinnerEnabled() {
		return expiringCourseModel.getReminderPeriod() == null || VariablePeriod.EXPLICIT.equals(expiringCourseModel.getReminderPeriod());
	}

	@Override
	public String loadAction() {
		initIn();
		initSets();
		return LOAD_OUTCOME;
	}

	@Override
	public void setPeriod(int property, VariablePeriod period) {
		switch (property) {
			case REMINDER_PERIOD_PROPERTY_ID:
				expiringCourseModel.setReminderPeriod(period);
				break;
			default:
		}
	}

	public void setReminder(VariablePeriodSelector reminder) {
		this.reminder = reminder;
	}
}
