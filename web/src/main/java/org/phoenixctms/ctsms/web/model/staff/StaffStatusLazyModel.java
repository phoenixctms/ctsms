package org.phoenixctms.ctsms.web.model.staff;

import java.util.ArrayList;
import java.util.Collection;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.StaffStatusEntryOutVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class StaffStatusLazyModel extends LazyDataModelBase {

	private Long staffId;
	private Long departmentId;
	private Long staffCategoryId;
	private Boolean staffActive;
	private Boolean hideAvailability;

	public StaffStatusLazyModel() {
		super();
		setShowInActiveOnly(Settings.getBoolean(SettingCodes.STAFF_STATUS_SHOW_INACTIVE_ONLY_PRESET, Bundle.SETTINGS, DefaultSettings.STAFF_STATUS_SHOW_INACTIVE_ONLY_PRESET));
		setIgnoreObsolete(Settings.getBoolean(SettingCodes.STAFF_STATUS_IGNORE_OBSOLETE_PRESET, Bundle.SETTINGS, DefaultSettings.STAFF_STATUS_IGNORE_OBSOLETE_PRESET));
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public boolean getIgnoreObsolete() {
		return hideAvailability != null;
	}

	@Override
	protected Collection<StaffStatusEntryOutVO> getLazyResult(PSFVO psf) {
		try {
			return WebUtil.getServiceLocator().getStaffService()
					.getStaffStatus(WebUtil.getAuthentication(), null, staffId, departmentId, staffCategoryId, staffActive, hideAvailability, psf);
		} catch (ServiceException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
		} catch (IllegalArgumentException e) {
		}
		return new ArrayList<StaffStatusEntryOutVO>();
	}

	@Override
	protected StaffStatusEntryOutVO getRowElement(Long id) {
		return WebUtil.getStaffStatusEntry(id);
	}

	public boolean getShowInActiveOnly() {
		return staffActive != null;
	}

	public Long getStaffCategoryId() {
		return staffCategoryId;
	}

	public Long getStaffId() {
		return staffId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public void setIgnoreObsolete(boolean ignoreObsolete) {
		this.hideAvailability = ignoreObsolete ? false : null;
	}

	public void setShowInActiveOnly(boolean active) {
		this.staffActive = active ? false : null;
	}

	public void setStaffCategoryId(Long staffId) {
		this.staffCategoryId = staffId;
	}

	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}
}
