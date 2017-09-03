package org.phoenixctms.ctsms.web.model.proband;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.phoenixctms.ctsms.enumeration.VariablePeriod;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class AutoDeletionProbandLazyModel extends LazyDataModelBase {

	private Long departmentId;
	private Long probandCategoryId;
	private Long reminderPeriodDays;
	private VariablePeriod reminderPeriod;
	private HashMap<Long, Long> probandPrivacyConsentTypeMap;

	public AutoDeletionProbandLazyModel() {
		super();
		reminderPeriod = Settings.getVariablePeriod(SettingCodes.PROBAND_AUTODELETE_REMINDER_PERIOD, Bundle.SETTINGS, DefaultSettings.PROBAND_AUTODELETE_REMINDER_PERIOD);
		reminderPeriodDays = Settings.getLongNullable(SettingCodes.PROBAND_AUTODELETE_REMINDER_PERIOD_DAYS, Bundle.SETTINGS,
				DefaultSettings.PROBAND_AUTODELETE_REMINDER_PERIOD_DAYS);
		probandPrivacyConsentTypeMap = new HashMap<Long, Long>();
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	@Override
	protected Collection<ProbandOutVO> getLazyResult(PSFVO psf) {
		probandPrivacyConsentTypeMap.clear();
		Collection<ProbandOutVO> result = null;
		try {
			result = WebUtil.getServiceLocator().getProbandService()
					.getAutoDeletionProbands(WebUtil.getAuthentication(), null, departmentId, probandCategoryId, reminderPeriod, reminderPeriodDays, psf);
		} catch (ServiceException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
		} catch (IllegalArgumentException e) {
		}
		if (result != null) {
			Iterator<ProbandOutVO> probandIt = result.iterator();
			while (probandIt.hasNext()) {
				ProbandOutVO probandVO = probandIt.next();
				probandPrivacyConsentTypeMap.put(probandVO.getId(), probandVO.getPrivacyConsentStatus().getId());
			}
			return result;
		} else {
			return new ArrayList<ProbandOutVO>();
		}
	}

	public Long getProbandCategoryId() {
		return probandCategoryId;
	}

	public Map<Long, Long> getProbandPrivacyConsentTypeMap() {
		return probandPrivacyConsentTypeMap;
	}

	public VariablePeriod getReminderPeriod() {
		return reminderPeriod;
	}

	public Long getReminderPeriodDays() {
		return reminderPeriodDays;
	}

	@Override
	protected ProbandOutVO getRowElement(Long id) {
		return WebUtil.getProband(id, null, null, null);
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public void setProbandCategoryId(Long probandCategoryId) {
		this.probandCategoryId = probandCategoryId;
	}

	public void setReminderPeriod(VariablePeriod reminderPeriod) {
		this.reminderPeriod = reminderPeriod;
	}

	public void setReminderPeriodDays(Long reminderPeriodDays) {
		this.reminderPeriodDays = reminderPeriodDays;
	}

	@Override
	public void updateRowCount() {
		super.updateRowCount();
		probandPrivacyConsentTypeMap.clear();
	}
}
