package org.phoenixctms.ctsms.web.model.inventory;

import java.util.ArrayList;
import java.util.Collection;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.InventoryStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class InventoryStatusLazyModel extends LazyDataModelBase<InventoryStatusEntryOutVO> {

	private Long inventoryId;
	private Long departmentId;
	private Long inventoryCategoryId;
	private Boolean inventoryActive;
	private Boolean hideAvailability;

	public InventoryStatusLazyModel() {
		super();
		setShowInActiveOnly(Settings.getBoolean(SettingCodes.INVENTORY_STATUS_SHOW_INACTIVE_ONLY_PRESET, Bundle.SETTINGS,
				DefaultSettings.INVENTORY_STATUS_SHOW_INACTIVE_ONLY_PRESET));
		setIgnoreObsolete(Settings.getBoolean(SettingCodes.INVENTORY_STATUS_IGNORE_OBSOLETE_PRESET, Bundle.SETTINGS, DefaultSettings.INVENTORY_STATUS_IGNORE_OBSOLETE_PRESET));
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public boolean getIgnoreObsolete() {
		return hideAvailability != null;
	}

	public Long getInventoryCategoryId() {
		return inventoryCategoryId;
	}

	public Long getInventoryId() {
		return inventoryId;
	}

	@Override
	protected Collection<InventoryStatusEntryOutVO> getLazyResult(PSFVO psf) {
		try {
			return WebUtil.getServiceLocator().getInventoryService()
					.getInventoryStatus(WebUtil.getAuthentication(), null, inventoryId, departmentId, inventoryCategoryId, inventoryActive, hideAvailability, psf);
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
		return new ArrayList<InventoryStatusEntryOutVO>();
	}

	@Override
	protected InventoryStatusEntryOutVO getRowElement(Long id) {
		return WebUtil.getInventoryStatusEntry(id);
	}

	public boolean getShowInActiveOnly() {
		return inventoryActive != null;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public void setIgnoreObsolete(boolean ignoreObsolete) {
		this.hideAvailability = ignoreObsolete ? false : null;
	}

	public void setInventoryCategoryId(Long inventoryId) {
		this.inventoryCategoryId = inventoryId;
	}

	public void setInventoryId(Long inventoryId) {
		this.inventoryId = inventoryId;
	}

	public void setShowInActiveOnly(boolean active) {
		this.inventoryActive = active ? false : null;
	}
}
