package org.phoenixctms.ctsms.web.model.course;

import java.util.ArrayList;
import java.util.Collection;

import org.phoenixctms.ctsms.enumeration.VariablePeriod;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.CourseParticipationStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class AdminExpiringParticipationLazyModel extends LazyDataModelBase<CourseParticipationStatusEntryOutVO> {

	private Long reminderPeriodDays;
	private VariablePeriod reminderPeriod;
	private Long courseDepartmentId;
	private Long courseCategoryId;
	private Long staffDepartmentId;
	private Long staffCategoryId;

	public AdminExpiringParticipationLazyModel() {
		super();
		reminderPeriod = Settings.getVariablePeriod(SettingCodes.ADMIN_COURSE_EXPIRATION_REMINDER_PERIOD, Bundle.SETTINGS, DefaultSettings.ADMIN_COURSE_EXPIRATION_REMINDER_PERIOD);
		reminderPeriodDays = Settings.getLongNullable(SettingCodes.ADMIN_COURSE_EXPIRATION_REMINDER_PERIOD_DAYS, Bundle.SETTINGS,
				DefaultSettings.ADMIN_COURSE_EXPIRATION_REMINDER_PERIOD_DAYS);
	}

	public Long getCourseCategoryId() {
		return courseCategoryId;
	}

	public Long getCourseDepartmentId() {
		return courseDepartmentId;
	}

	@Override
	protected Collection<CourseParticipationStatusEntryOutVO> getLazyResult(PSFVO psf) {
		try {
			return WebUtil
					.getServiceLocator()
					.getCourseService()
					.getExpiringParticipations(WebUtil.getAuthentication(), null, courseDepartmentId, courseCategoryId, staffDepartmentId, staffCategoryId, reminderPeriod,
							reminderPeriodDays, psf);
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
		return new ArrayList<CourseParticipationStatusEntryOutVO>();
	}

	public VariablePeriod getReminderPeriod() {
		return reminderPeriod;
	}

	public Long getReminderPeriodDays() {
		return reminderPeriodDays;
	}

	@Override
	protected CourseParticipationStatusEntryOutVO getRowElement(Long id) {
		return WebUtil.getCourseParticipationStatusEntry(id);
	}

	public Long getStaffCategoryId() {
		return staffCategoryId;
	}

	public Long getStaffDepartmentId() {
		return staffDepartmentId;
	}

	public void setCourseCategoryId(Long courseCategoryId) {
		this.courseCategoryId = courseCategoryId;
	}

	public void setCourseDepartmentId(Long courseDepartmentId) {
		this.courseDepartmentId = courseDepartmentId;
	}

	public void setReminderPeriod(VariablePeriod reminderPeriod) {
		this.reminderPeriod = reminderPeriod;
	}

	public void setReminderPeriodDays(Long reminderPeriodDays) {
		this.reminderPeriodDays = reminderPeriodDays;
	}

	public void setStaffCategoryId(Long staffCategoryId) {
		this.staffCategoryId = staffCategoryId;
	}

	public void setStaffDepartmentId(Long staffDepartmentId) {
		this.staffDepartmentId = staffDepartmentId;
	}
}
