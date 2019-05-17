package org.phoenixctms.ctsms.web.model.inventory;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.MaintenanceScheduleItemOutVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;

@ManagedBean
@ViewScoped
public class InventoryMaintenanceOverviewBean extends ManagedBeanBase {

	private Date today;
	private MaintenanceScheduleLazyModel maintenanceScheduleModel;

	public InventoryMaintenanceOverviewBean() {
		super();
		today = new Date();
		maintenanceScheduleModel = new MaintenanceScheduleLazyModel();
	}

	public void dismiss(MaintenanceScheduleItemOutVO maintenanceItem) {
		if (maintenanceItem != null) {
			try {
				WebUtil.getServiceLocator().getInventoryService()
						.setMaintenaceScheduleItemDismissed(WebUtil.getAuthentication(), maintenanceItem.getId(), maintenanceItem.getVersion(), true);
				initIn();
				initSets();
				addOperationSuccessMessage(MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
				Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			} catch (AuthenticationException e) {
				Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
				WebUtil.publishException(e);
			}
		}
	}

	public String getMaintenanceScheduleItemDueInString(MaintenanceScheduleItemOutVO maintenanceItem) {
		return WebUtil.getExpirationDueInString(today, maintenanceItem.getNextRecurrence());
	}

	public MaintenanceScheduleLazyModel getMaintenanceScheduleModel() {
		return maintenanceScheduleModel;
	}

	public void handleIdentityResponsiblePersonChange() {
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
		maintenanceScheduleModel.updateRowCount();
		LazyDataModelBase.clearFilters("inventorymaintenance_list");
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

	public String maintenanceItemToColor(MaintenanceScheduleItemOutVO maintenanceItem) {
		if (maintenanceItem != null) {
			return WebUtil.expirationToColor(today, maintenanceItem.getNextRecurrence(), Settings.getInventoryMaintenanceDueInColorMap(),
					Settings.getColor(SettingCodes.INVENTORY_MAINTENANCE_OVERDUE_COLOR, Bundle.SETTINGS, DefaultSettings.INVENTORY_MAINTENANCE_OVERDUE_COLOR));
		}
		return "";
	}
}
