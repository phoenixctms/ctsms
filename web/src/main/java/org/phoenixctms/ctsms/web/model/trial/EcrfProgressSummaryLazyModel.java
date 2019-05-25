package org.phoenixctms.ctsms.web.model.trial;

import java.util.ArrayList;
import java.util.Collection;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class EcrfProgressSummaryLazyModel extends LazyDataModelBase<TrialOutVO> {

	private Long departmentId;
	// private String costType;
	// private Boolean paid;

	public EcrfProgressSummaryLazyModel() {
		super();
		// paid = Settings.getBooleanNullable(SettingCodes.TRIAL_MONEY_TRANSFER_SUMMARY_SHOW_PAID_PRESET, Bundle.SETTINGS,
		// DefaultSettings.TRIAL_MONEY_TRANSFER_SUMMARY_SHOW_PAID_PRESET);
		StaffOutVO identity = WebUtil.getUserIdentity();
		if (identity != null) {
			if (Settings.getBoolean(SettingCodes.ECRF_PROGRESS_SUMMARY_ACTIVE_IDENTITY_DEPARTMENT_PRESET, Bundle.SETTINGS,
					DefaultSettings.ECRF_PROGRESS_SUMMARY_ACTIVE_IDENTITY_DEPARTMENT_PRESET)) {
				setDepartmentId(identity.getDepartment().getId());
			}
		}
	}

	// public String getCostType() {
	// return costType;
	// }
	public Long getDepartmentId() {
		return departmentId;
	}

	@Override
	protected Collection<TrialOutVO> getLazyResult(PSFVO psf) {
		try {
			return WebUtil.getServiceLocator().getTrialService().getEcrfTrialList(WebUtil.getAuthentication(), departmentId, psf);
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
		return new ArrayList<TrialOutVO>();
	}

	// public Boolean getPaid() {
	// return paid;
	// }
	@Override
	protected TrialOutVO getRowElement(Long id) {
		return WebUtil.getTrial(id);
	}

	// public void setCostType(String costType) {
	// this.costType = costType;
	// }
	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}
	// public void setPaid(Boolean paid) {
	// this.paid = paid;
	// }
}