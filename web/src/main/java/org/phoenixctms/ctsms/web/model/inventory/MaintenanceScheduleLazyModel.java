package org.phoenixctms.ctsms.web.model.inventory;

import java.util.ArrayList;
import java.util.Collection;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.MaintenanceScheduleItemOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class MaintenanceScheduleLazyModel extends LazyDataModelBase {

	private Long inventoryId;
	private Long responsiblePersonId;
	private Long departmentId;
	private Long inventoryCategoryId;
	private Boolean notify;

	public MaintenanceScheduleLazyModel() {
		super();
		setIdentityResponsiblePerson(Settings.getBoolean(SettingCodes.MAINTENANCE_SCHEDULE_IDENTITY_RESPONSIBLE_PERSON_PRESET, Bundle.SETTINGS,
				DefaultSettings.MAINTENANCE_SCHEDULE_IDENTITY_RESPONSIBLE_PERSON_PRESET));
		setShowNotifyOnly(Settings.getBoolean(SettingCodes.MAINTENANCE_SCHEDULE_SHOW_NOTIFY_ONLY_PRESET, Bundle.SETTINGS,
				DefaultSettings.MAINTENANCE_SCHEDULE_SHOW_NOTIFY_ONLY_PRESET));
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public Long getInventoryCategoryId() {
		return inventoryCategoryId;
	}

	public Long getInventoryId() {
		return inventoryId;
	}

	@Override
	protected Collection<MaintenanceScheduleItemOutVO> getLazyResult(PSFVO psf) {
		try {
			return WebUtil.getServiceLocator().getInventoryService()
					.getMaintenanceSchedule(WebUtil.getAuthentication(), null, inventoryId, departmentId, inventoryCategoryId, responsiblePersonId, notify, psf);
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
		return new ArrayList<MaintenanceScheduleItemOutVO>();
	}

	public Long getResponsiblePersonId() {
		return responsiblePersonId;
	}

	@Override
	protected MaintenanceScheduleItemOutVO getRowElement(Long id) {
		return WebUtil.getMaintenanceScheduleItem(id);
	}

	public boolean getShowNotifyOnly() {
		return notify != null;
	}

	public boolean isIdentityResponsiblePerson() {
		if (responsiblePersonId != null) {
			StaffOutVO identity = WebUtil.getUserIdentity();
			if (identity != null && responsiblePersonId.longValue() == identity.getId()) {
				return true;
			}
		}
		return false;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public void setIdentityResponsiblePerson(boolean identityResponsiblePerson) {
		if (identityResponsiblePerson) {
			StaffOutVO identity = WebUtil.getUserIdentity();
			if (identity != null) {
				responsiblePersonId = identity.getId();
			} else {
				responsiblePersonId = null;
			}
		} else {
			responsiblePersonId = null;
		}
	}

	public void setInventoryCategoryId(Long inventoryId) {
		this.inventoryCategoryId = inventoryId;
	}

	public void setInventoryId(Long inventoryId) {
		this.inventoryId = inventoryId;
	}

	public void setResponsiblePersonId(Long responsiblePersonId) {
		this.responsiblePersonId = responsiblePersonId;
	}

	public void setShowNotifyOnly(boolean notify) {
		this.notify = notify ? true : null;
	}
}
