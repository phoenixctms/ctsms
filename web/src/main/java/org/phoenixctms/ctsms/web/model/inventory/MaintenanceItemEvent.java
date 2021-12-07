package org.phoenixctms.ctsms.web.model.inventory;

import java.util.Date;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.InventoryOutVO;
import org.phoenixctms.ctsms.vo.MaintenanceScheduleItemInVO;
import org.phoenixctms.ctsms.vo.MaintenanceScheduleItemOutVO;
import org.phoenixctms.ctsms.vo.MaintenanceTypeVO;
import org.phoenixctms.ctsms.web.model.ScheduleEventBase;
import org.phoenixctms.ctsms.web.util.DateUtil;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.model.ScheduleEvent;

public class MaintenanceItemEvent extends ScheduleEventBase<MaintenanceScheduleItemInVO> {

	private final static String EVENT_STYLECLASS = "ctsms-inventory-schedule-maintenance-event";
	private final static String EVENT_TITLE_HEAD_SEPARATOR = ": ";
	private final static String EVENT_TITLE_SEPARATOR = " | ";
	private MaintenanceScheduleItemOutVO out;
	private Date date;

	public MaintenanceItemEvent() {
		super();
	}

	public MaintenanceItemEvent(MaintenanceScheduleItemInVO maintenanceItem) {
		super(maintenanceItem);
	}

	public MaintenanceItemEvent(MaintenanceScheduleItemOutVO maintenanceItem) {
		setOut(maintenanceItem);
	}

	public MaintenanceItemEvent(ScheduleEvent event) {
		setEvent(event);
	}

	@Override
	protected boolean copyOutToIn() {
		if (out != null) {
			InventoryMaintenanceBean.copyMaintenanceItemOutToIn(in, out, new Date());
			date = null;
			return true;
		}
		return false;
	}

	@Override
	protected void copyToIn(MaintenanceScheduleItemInVO source) {
		in.copy(source);
		date = null;
	}

	@Override
	protected void createIn() {
		in = new MaintenanceScheduleItemInVO();
		date = null;
	}

	public Date getDate() {
		return date;
	}

	@Override
	public Date getEndDate() {
		return getStartDate();
	}

	public MaintenanceScheduleItemOutVO getOut() {
		return out;
	}

	@Override
	public Date getStartDate() {
		return DateUtil.sanitizeScheduleDate(true, date == null ? in.getDate() : date);
	}

	@Override
	public String getStyleClass() {
		return EVENT_STYLECLASS;
	}

	@Override
	public String getTitle() {
		StringBuilder sb = new StringBuilder();
		InventoryOutVO inventory = (out != null ? out.getInventory() : null);
		if (inventory != null && in.getInventoryId() != null && !in.getInventoryId().equals(inventory.getId())
				|| (inventory == null && in.getInventoryId() != null)) {
			inventory = WebUtil.getInventory(in.getInventoryId(), null, null, null);
		} else if (inventory != null && in.getInventoryId() == null) {
			inventory = null;
		}
		if (inventory != null) {
			sb.append(CommonUtil.inventoryOutVOToString(inventory));
			if (in.getTypeId() != null || !CommonUtil.isEmptyString(in.getTitle())) {
				sb.append(EVENT_TITLE_HEAD_SEPARATOR);
			}
		}
		boolean appended = false;
		MaintenanceTypeVO maintenanceType = (out != null ? out.getType() : null);
		if (maintenanceType != null && in.getTypeId() != null && !in.getTypeId().equals(maintenanceType.getId())
				|| (maintenanceType == null && in.getTypeId() != null)) {
			maintenanceType = WebUtil.getMaintenanceType(in.getTypeId());
		} else if (maintenanceType != null && in.getTypeId() == null) {
			maintenanceType = null;
		}
		if (maintenanceType != null) {
			if (appended) {
				sb.append(EVENT_TITLE_SEPARATOR);
			}
			sb.append(maintenanceType.getName());
			appended = true;
		}
		if (!CommonUtil.isEmptyString(in.getTitle())) {
			if (appended) {
				sb.append(EVENT_TITLE_SEPARATOR);
			}
			sb.append(in.getTitle());
		}
		return sb.toString();
	}

	@Override
	protected void initDefaultValues() {
		InventoryMaintenanceBean.initMaintenanceItemDefaultValues(in, null, WebUtil.getUserIdentity());
		date = null;
	}

	@Override
	public boolean isAllDay() {
		return true;
	}

	@Override
	public boolean isEditable() {
		return false;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public void setEvent(ScheduleEvent event) {
		super.setEvent(event);
		date = null;
	}

	public void setOut(MaintenanceScheduleItemOutVO out) {
		this.out = out;
		initIn(InitSource.OUT);
	}
}
