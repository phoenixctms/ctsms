package org.phoenixctms.ctsms.web.model.inventory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.enumeration.VariablePeriod;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.js.JsUtil;
import org.phoenixctms.ctsms.vo.InventoryOutVO;
import org.phoenixctms.ctsms.vo.MaintenanceScheduleItemInVO;
import org.phoenixctms.ctsms.vo.MaintenanceScheduleItemOutVO;
import org.phoenixctms.ctsms.vo.MaintenanceTypeVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.VariablePeriodVO;
import org.phoenixctms.ctsms.web.component.datatable.DataTable;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.model.VariablePeriodSelector;
import org.phoenixctms.ctsms.web.model.VariablePeriodSelectorListener;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.GetParamNames;
import org.phoenixctms.ctsms.web.util.JSValues;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.context.RequestContext;

@ManagedBean
@ViewScoped
public class InventoryMaintenanceBean extends ManagedBeanBase implements VariablePeriodSelectorListener {

	private static final int RECURRENCE_PERIOD_PROPERTY_ID = 1;
	private static final int REMINDER_PERIOD_PROPERTY_ID = 2;

	public static void copyMaintenanceItemOutToIn(MaintenanceScheduleItemInVO in, MaintenanceScheduleItemOutVO out, Date today) {
		if (in != null && out != null) {
			MaintenanceTypeVO maintenanceTypeVO = out.getType();
			InventoryOutVO inventoryVO = out.getInventory();
			StaffOutVO responsiblePersonVO = out.getResponsiblePerson();
			StaffOutVO responsiblePersonProxyVO = out.getResponsiblePersonProxy();
			StaffOutVO companyContactVO = out.getCompanyContact();
			VariablePeriodVO recurrencePeriodVO = out.getRecurrencePeriod();
			VariablePeriodVO reminderPeriodVO = out.getReminderPeriod();
			in.setId(out.getId());
			in.setVersion(out.getVersion());
			in.setInventoryId(inventoryVO == null ? null : inventoryVO.getId());
			in.setActive(out.getActive());
			in.setCharge(out.getCharge());
			in.setDate(out.getDate());
			in.setRecurrencePeriod(recurrencePeriodVO == null ? null : recurrencePeriodVO.getPeriod());
			in.setRecurrencePeriodDays(out.getRecurrencePeriodDays());
			in.setRecurring(out.getRecurring());
			in.setNotify(out.getNotify());
			in.setReminderPeriod(reminderPeriodVO == null ? null : reminderPeriodVO.getPeriod());
			in.setReminderPeriodDays(out.getReminderPeriodDays());
			in.setDismissed(getDismissed(out, today));
			in.setResponsiblePersonId(responsiblePersonVO == null ? null : responsiblePersonVO.getId());
			in.setResponsiblePersonProxyId(responsiblePersonProxyVO == null ? null : responsiblePersonProxyVO.getId());
			in.setCompanyContactId(companyContactVO == null ? null : companyContactVO.getId());
			in.setTitle(out.getTitle());
			in.setTypeId(maintenanceTypeVO == null ? null : maintenanceTypeVO.getId());
			in.setComment(out.getComment());
		}
	}

	private static boolean getDismissed(MaintenanceScheduleItemOutVO maintenanceItem, Date today) {
		boolean dismissed = false;
		Date reminderStart = (maintenanceItem == null ? null : maintenanceItem.getReminderStart());
		if (reminderStart != null && today != null) {
			if (today.compareTo(reminderStart) >= 0) {
				Date dismissedTimestamp = maintenanceItem.getDismissedTimestamp();
				if (dismissedTimestamp != null && reminderStart.compareTo(dismissedTimestamp) <= 0) {
					dismissed = maintenanceItem.isDismissed();
				}
			}
		}
		return dismissed;
	}

	public static void initMaintenanceItemDefaultValues(MaintenanceScheduleItemInVO in, Long inventoryId, StaffOutVO identity) {
		if (in != null) {
			in.setId(null);
			in.setVersion(null);
			in.setInventoryId(inventoryId);
			in.setActive(Settings.getBoolean(SettingCodes.INVENTORY_MAINTENANCE_ACTIVE_PRESET, Bundle.SETTINGS, DefaultSettings.INVENTORY_MAINTENANCE_ACTIVE_PRESET));
			in.setCharge(Settings.getFloatNullable(SettingCodes.INVENTORY_MAINTENANCE_CHARGE_PRESET, Bundle.SETTINGS, DefaultSettings.INVENTORY_MAINTENANCE_CHARGE_PRESET));
			in.setDate(new Date());
			in.setRecurrencePeriod(Settings.getVariablePeriod(SettingCodes.INVENTORY_MAINTENANCE_RECURRENCE_PERIOD_PRESET, Bundle.SETTINGS,
					DefaultSettings.INVENTORY_MAINTENANCE_RECURRENCE_PERIOD_PRESET));
			in.setRecurrencePeriodDays(Settings.getLongNullable(SettingCodes.INVENTORY_MAINTENANCE_RECURRENCE_PERIOD_DAYS_PRESET, Bundle.SETTINGS,
					DefaultSettings.INVENTORY_MAINTENANCE_RECURRENCE_PERIOD_DAYS_PRESET));
			in.setRecurring(Settings.getBoolean(SettingCodes.INVENTORY_MAINTENANCE_RECURRING_PRESET, Bundle.SETTINGS, DefaultSettings.INVENTORY_MAINTENANCE_RECURRING_PRESET));
			in.setNotify(Settings.getBoolean(SettingCodes.INVENTORY_MAINTENANCE_NOTIFY_PRESET, Bundle.SETTINGS, DefaultSettings.INVENTORY_MAINTENANCE_NOTIFY_PRESET));
			in.setReminderPeriod(Settings.getVariablePeriod(SettingCodes.INVENTORY_MAINTENANCE_REMINDER_PERIOD_PRESET, Bundle.SETTINGS,
					DefaultSettings.INVENTORY_MAINTENANCE_REMINDER_PERIOD_PRESET));
			in.setReminderPeriodDays(Settings.getLongNullable(SettingCodes.INVENTORY_MAINTENANCE_REMINDER_PERIOD_DAYS_PRESET, Bundle.SETTINGS,
					DefaultSettings.INVENTORY_MAINTENANCE_REMINDER_PERIOD_DAYS_PRESET));
			in.setDismissed(Settings.getBoolean(SettingCodes.INVENTORY_MAINTENANCE_DISMISSED_PRESET, Bundle.SETTINGS, DefaultSettings.INVENTORY_MAINTENANCE_DISMISSED_PRESET));
			in.setResponsiblePersonId(identity != null ? identity.getId() : null);
			in.setResponsiblePersonProxyId(null);
			in.setCompanyContactId(null);
			in.setTitle(Messages.getString(MessageCodes.MAINTENANCE_ITEM_TITLE_PRESET));
			in.setTypeId(null);
			in.setComment(Messages.getString(MessageCodes.MAINTENANCE_ITEM_COMMENT_PRESET));
		}
	}

	private Date today;
	private MaintenanceScheduleItemInVO in;
	private MaintenanceScheduleItemOutVO out;
	private Long inventoryId;
	private ArrayList<SelectItem> maintenanceTypes;
	private MaintenanceScheduleItemLazyModel maintenanceItemModel;
	private MaintenanceTypeVO maintenanceType;
	private VariablePeriodSelector recurrence;
	private VariablePeriodSelector reminder;

	public InventoryMaintenanceBean() {
		super();
		today = new Date();
		maintenanceItemModel = new MaintenanceScheduleItemLazyModel();
		setRecurrence(new VariablePeriodSelector(this, RECURRENCE_PERIOD_PROPERTY_ID));
		setReminder(new VariablePeriodSelector(this, REMINDER_PERIOD_PROPERTY_ID));
	}

	@Override
	public String addAction() {
		MaintenanceScheduleItemInVO backup = new MaintenanceScheduleItemInVO(in);
		// Long idBackup = in.getId();
		// Long versionBackup = in.getVersion();
		in.setId(null);
		in.setVersion(null);
		try {
			out = WebUtil.getServiceLocator().getInventoryService().addMaintenanceScheduleItem(WebUtil.getAuthentication(), in);
			initIn();
			initSets();
			addOperationSuccessMessage(MessageCodes.ADD_OPERATION_SUCCESSFUL);
			return ADD_OUTCOME;
		} catch (ServiceException | IllegalArgumentException | AuthorisationException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
		RequestContext requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_INVENTORY_MAINTENANCE_TAB_TITLE_BASE64,
				JSValues.AJAX_INVENTORY_MAINTENANCE_ITEM_COUNT, MessageCodes.MAINTENANCE_ITEMS_TAB_TITLE, MessageCodes.MAINTENANCE_ITEMS_TAB_TITLE_WITH_COUNT, new Long(
						maintenanceItemModel.getRowCount()));
		if (operationSuccess && in.getInventoryId() != null) {
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_INVENTORY_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_INVENTORY_JOURNAL_ENTRY_COUNT,
					MessageCodes.INVENTORY_JOURNAL_TAB_TITLE, MessageCodes.INVENTORY_JOURNAL_TAB_TITLE_WITH_COUNT,
					WebUtil.getJournalCount(JournalModule.INVENTORY_JOURNAL, in.getInventoryId()));
		}
	}

	@Override
	protected String changeAction(Long id) {
		DataTable.clearFilters("inventorymaintenance_list");
		out = null;
		this.inventoryId = id;
		initIn();
		initSets();
		return CHANGE_OUTCOME;
	}

	@Override
	public String deleteAction() {
		return deleteAction(in.getId());
	}

	@Override
	public String deleteAction(Long id) {
		try {
			out = WebUtil.getServiceLocator().getInventoryService().deleteMaintenanceScheduleItem(WebUtil.getAuthentication(), id);
			initIn();
			initSets();
			out = null;
			addOperationSuccessMessage(MessageCodes.DELETE_OPERATION_SUCCESSFUL);
			return DELETE_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	public String getCompanyContactName() {
		return WebUtil.staffIdToName(in.getCompanyContactId());
	}

	public boolean getDismissed(MaintenanceScheduleItemOutVO maintenanceItem) {
		return getDismissed(maintenanceItem, today);
	}

	public MaintenanceScheduleItemInVO getIn() {
		return in;
	}

	public MaintenanceScheduleItemLazyModel getMaintenanceItemModel() {
		return maintenanceItemModel;
	}

	public ArrayList<SelectItem> getMaintenanceTypes() {
		return maintenanceTypes;
	}

	@Override
	public String getModifiedAnnotation() {
		if (out != null) {
			return WebUtil.getModifiedAnnotation(out.getVersion(), out.getModifiedUser(), out.getModifiedTimestamp());
		} else {
			return super.getModifiedAnnotation();
		}
	}

	public MaintenanceScheduleItemOutVO getOut() {
		return out;
	}

	@Override
	public VariablePeriod getPeriod(int property) {
		switch (property) {
			case RECURRENCE_PERIOD_PROPERTY_ID:
				return this.in.getRecurrencePeriod();
			case REMINDER_PERIOD_PROPERTY_ID:
				return this.in.getReminderPeriod();
			default:
				return VariablePeriodSelectorListener.NO_SELECTION_VARIABLE_PERIOD;
		}
	}

	public VariablePeriodSelector getRecurrence() {
		return recurrence;
	}

	public VariablePeriodSelector getReminder() {
		return reminder;
	}

	public String getResponsiblePersonName() {
		return WebUtil.staffIdToName(in.getResponsiblePersonId());
	}

	public String getResponsiblePersonProxyName() {
		return WebUtil.staffIdToName(in.getResponsiblePersonProxyId());
	}

	public IDVO getSelectedMaintenanceScheduleItem() {
		if (this.out != null) {
			return IDVO.transformVo(this.out);
		} else {
			return null;
		}
	}

	@Override
	public String getTitle() {
		if (out != null) {
			return Messages.getMessage(MessageCodes.INVENTORY_MAINTENANCE_ITEM_TITLE, Long.toString(out.getId()), out.getTitle());
		} else {
			return Messages.getString(MessageCodes.CREATE_NEW_INVENTORY_MAINTENANCE_ITEM);
		}
	}

	public void handleTypeChange() {
		loadSelectedType();
		RequestContext requestContext = RequestContext.getCurrentInstance();
		if (requestContext != null && maintenanceType != null) {
			requestContext.addCallbackParam(JSValues.AJAX_MAINTENANCE_TYPE_TITLE_PRESET_BASE64.toString(), JsUtil.encodeBase64(maintenanceType.getTitlePreset(), false));
		}
	}

	@PostConstruct
	private void init() {
		Long id = WebUtil.getLongParamValue(GetParamNames.MAINTENANCE_SCHEDULE_ITEM_ID);
		if (id != null) {
			this.load(id);
		} else {
			initIn();
			initSets();
		}
	}

	private void initIn() {
		if (in == null) {
			in = new MaintenanceScheduleItemInVO();
		}
		if (out != null) {
			copyMaintenanceItemOutToIn(in, out, today);
			inventoryId = in.getInventoryId();
		} else {
			initMaintenanceItemDefaultValues(in, inventoryId, WebUtil.getUserIdentity());
		}
	}

	private void initSets() {
		today = new Date();
		maintenanceItemModel.setInventoryId(in.getInventoryId());
		maintenanceItemModel.updateRowCount();
		Collection<MaintenanceTypeVO> maintenanceTypeVOs = null;
		try {
			maintenanceTypeVOs = WebUtil.getServiceLocator().getSelectionSetService().getMaintenanceTypes(WebUtil.getAuthentication(), in.getTypeId());
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
		if (maintenanceTypeVOs != null) {
			maintenanceTypes = new ArrayList<SelectItem>(maintenanceTypeVOs.size());
			Iterator<MaintenanceTypeVO> it = maintenanceTypeVOs.iterator();
			while (it.hasNext()) {
				MaintenanceTypeVO tagVO = it.next();
				maintenanceTypes.add(new SelectItem(tagVO.getId().toString(), tagVO.getName()));
			}
		} else {
			maintenanceTypes = new ArrayList<SelectItem>();
		}
		loadSelectedType();
	}

	@Override
	public boolean isCreateable() {
		return (in.getInventoryId() == null ? false : true);
	}

	@Override
	public boolean isCreated() {
		return out != null;
	}

	public boolean isRecurrencePeriodSelectorEnabled() {
		return this.in.getRecurring();
	}

	public boolean isRecurrencePeriodSpinnerEnabled() {
		return (this.in.getRecurring() && (this.in.getRecurrencePeriod() == null || VariablePeriod.EXPLICIT.equals(this.in.getRecurrencePeriod())));
	}

	public boolean isReminderPeriodSelectorEnabled() {
		return true;
	}

	public boolean isReminderPeriodSpinnerEnabled() {
		return this.in.getReminderPeriod() == null || VariablePeriod.EXPLICIT.equals(this.in.getReminderPeriod());
	}

	@Override
	public String loadAction() {
		return loadAction(in.getId());
	}

	@Override
	public String loadAction(Long id) {
		out = null;
		try {
			out = WebUtil.getServiceLocator().getInventoryService().getMaintenanceScheduleItem(WebUtil.getAuthentication(), id);
			return LOAD_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} finally {
			initIn();
			initSets();
		}
		return ERROR_OUTCOME;
	}

	private void loadSelectedType() {
		maintenanceType = WebUtil.getMaintenanceType(in.getTypeId());
	}

	@Override
	public String resetAction() {
		out = null;
		initIn();
		initSets();
		return RESET_OUTCOME;
	}

	@Override
	public void setPeriod(int property, VariablePeriod period) {
		switch (property) {
			case RECURRENCE_PERIOD_PROPERTY_ID:
				this.in.setRecurrencePeriod(period);
				break;
			case REMINDER_PERIOD_PROPERTY_ID:
				this.in.setReminderPeriod(period);
				break;
			default:
		}
	}

	public void setRecurrence(VariablePeriodSelector recurrence) {
		this.recurrence = recurrence;
	}

	public void setReminder(VariablePeriodSelector reminder) {
		this.reminder = reminder;
	}

	public void setSelectedMaintenanceScheduleItem(IDVO maintenanceScheduleItem) {
		if (maintenanceScheduleItem != null) {
			this.out = (MaintenanceScheduleItemOutVO) maintenanceScheduleItem.getVo();
			this.initIn();
			initSets();
		}
	}

	@Override
	public String updateAction() {
		try {
			out = WebUtil.getServiceLocator().getInventoryService().updateMaintenanceScheduleItem(WebUtil.getAuthentication(), in);
			initIn();
			initSets();
			addOperationSuccessMessage(MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
			return UPDATE_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}
}
