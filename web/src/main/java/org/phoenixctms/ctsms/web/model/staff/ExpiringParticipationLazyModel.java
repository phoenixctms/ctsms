package org.phoenixctms.ctsms.web.model.staff;

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

public class ExpiringParticipationLazyModel extends LazyDataModelBase {

	private Long staffId;
	private Long reminderPeriodDays;
	private VariablePeriod reminderPeriod;

	public ExpiringParticipationLazyModel() {
		super();
		reminderPeriod = Settings.getVariablePeriod(SettingCodes.COURSE_EXPIRATION_REMINDER_PERIOD, Bundle.SETTINGS, DefaultSettings.COURSE_EXPIRATION_REMINDER_PERIOD);
		reminderPeriodDays = Settings.getLongNullable(SettingCodes.COURSE_EXPIRATION_REMINDER_PERIOD_DAYS, Bundle.SETTINGS, DefaultSettings.COURSE_EXPIRATION_REMINDER_PERIOD_DAYS);
	}

	@Override
	protected Collection<CourseParticipationStatusEntryOutVO> getLazyResult(PSFVO psf) {
		if (staffId != null) {
			try {
				return WebUtil.getServiceLocator().getStaffService().getExpiringParticipations(WebUtil.getAuthentication(), null, staffId, reminderPeriod, reminderPeriodDays, psf);
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
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

	public Long getStaffId() {
		return staffId;
	}

	public void setReminderPeriod(VariablePeriod reminderPeriod) {
		this.reminderPeriod = reminderPeriod;
	}

	public void setReminderPeriodDays(Long reminderPeriodDays) {
		this.reminderPeriodDays = reminderPeriodDays;
	}

	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}
}
