package org.phoenixctms.ctsms.web.model.course;

import java.util.ArrayList;
import java.util.Collection;

import org.phoenixctms.ctsms.enumeration.VariablePeriod;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.CourseOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class ExpiringCourseLazyModel extends LazyDataModelBase<CourseOutVO> {

	private Long departmentId;
	private Long courseCategoryId;
	private Long reminderPeriodDays;
	private VariablePeriod reminderPeriod;

	public ExpiringCourseLazyModel() {
		super();
		reminderPeriod = Settings.getVariablePeriod(SettingCodes.ADMIN_COURSE_EXPIRATION_REMINDER_PERIOD, Bundle.SETTINGS, DefaultSettings.ADMIN_COURSE_EXPIRATION_REMINDER_PERIOD);
		reminderPeriodDays = Settings.getLongNullable(SettingCodes.ADMIN_COURSE_EXPIRATION_REMINDER_PERIOD_DAYS, Bundle.SETTINGS,
				DefaultSettings.ADMIN_COURSE_EXPIRATION_REMINDER_PERIOD_DAYS);
	}

	public Long getCourseCategoryId() {
		return courseCategoryId;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	@Override
	protected Collection<CourseOutVO> getLazyResult(PSFVO psf) {
		try {
			return WebUtil.getServiceLocator().getCourseService()
					.getExpiringCourses(WebUtil.getAuthentication(), null, departmentId, courseCategoryId, reminderPeriod, reminderPeriodDays, psf);
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
		return new ArrayList<CourseOutVO>();
	}

	public VariablePeriod getReminderPeriod() {
		return reminderPeriod;
	}

	public Long getReminderPeriodDays() {
		return reminderPeriodDays;
	}

	@Override
	protected CourseOutVO getRowElement(Long id) {
		return WebUtil.getCourse(id, null, null, null);
	}

	public void setCourseCategoryId(Long courseCategoryId) {
		this.courseCategoryId = courseCategoryId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public void setReminderPeriod(VariablePeriod reminderPeriod) {
		this.reminderPeriod = reminderPeriod;
	}

	public void setReminderPeriodDays(Long reminderPeriodDays) {
		this.reminderPeriodDays = reminderPeriodDays;
	}
}
