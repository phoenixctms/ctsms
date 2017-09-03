package org.phoenixctms.ctsms.web.model.inventory;

import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.phoenixctms.ctsms.vo.InventoryStatusEntryOutVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.model.shared.CollidingInventoryBookingEagerModel;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;

@ManagedBean
@ViewScoped
public class InventoryStatusOverviewBean extends ManagedBeanBase {

	private InventoryStatusLazyModel inventoryStatusModel;
	private HashMap<Long, CollidingInventoryBookingEagerModel> collidingInventoryBookingModelCache;

	public InventoryStatusOverviewBean() {
		super();
		collidingInventoryBookingModelCache = new HashMap<Long, CollidingInventoryBookingEagerModel>();
		inventoryStatusModel = new InventoryStatusLazyModel();
	}

	public CollidingInventoryBookingEagerModel getCollidingInventoryBookingModel(InventoryStatusEntryOutVO statusEntry) {
		return CollidingInventoryBookingEagerModel.getCachedCollidingInventoryBookingModel(statusEntry, true, collidingInventoryBookingModelCache);
	}

	public InventoryStatusLazyModel getInventoryStatusModel() {
		return inventoryStatusModel;
	}

	public void handleIgnoreObsoleteChange() {
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
		collidingInventoryBookingModelCache.clear();
		inventoryStatusModel.updateRowCount();
		LazyDataModelBase.clearFilters("inventorystatus_list");
	}

	public String inventoryStatusToColor(InventoryStatusEntryOutVO statusEntry) {
		if (statusEntry != null) {
			if (!statusEntry.getType().getInventoryActive()) {
				return WebUtil.colorToStyleClass(Settings.getColor(SettingCodes.NA_STATUS_COLOR, Bundle.SETTINGS, DefaultSettings.NA_STATUS_COLOR));
			}
		}
		return "";
	}

	@Override
	public boolean isCreateable() {
		return false;
	}

	@Override
	public boolean isCreated() {
		return false;
	}

	@Override
	public String loadAction() {
		initIn();
		initSets();
		return LOAD_OUTCOME;
	}
}
