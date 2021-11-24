package org.phoenixctms.ctsms.web.model.inventory;

import java.util.Date;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CommonUtil.EllipsisPlacement;
import org.phoenixctms.ctsms.vo.InventoryOutVO;
import org.phoenixctms.ctsms.vo.InventoryStatusEntryInVO;
import org.phoenixctms.ctsms.vo.InventoryStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.InventoryStatusTypeVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.web.model.ScheduleEventBase;
import org.phoenixctms.ctsms.web.util.DateUtil;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.model.ScheduleEvent;

public class InventoryStatusEvent extends ScheduleEventBase<InventoryStatusEntryInVO> {

	private final static String EVENT_STYLECLASS = "ctsms-inventory-schedule-status-event";
	private final static String EVENT_TITLE_HEAD_SEPARATOR = ": ";
	private final static String EVENT_TITLE_SEPARATOR = "\n";
	private InventoryStatusEntryOutVO out;

	public InventoryStatusEvent() {
		super();
	}

	public InventoryStatusEvent(InventoryStatusEntryInVO statusEntry) {
		super(statusEntry);
	}

	public InventoryStatusEvent(InventoryStatusEntryOutVO statusEntry) {
		setOut(statusEntry);
	}

	public InventoryStatusEvent(ScheduleEvent event) {
		super(event);
	}

	@Override
	protected boolean copyOutToIn() {
		if (out != null) {
			InventoryStatusBean.copyStatusEntryOutToIn(in, out);
			return true;
		}
		return false;
	}

	@Override
	protected void copyToIn(InventoryStatusEntryInVO source) {
		in.copy(source);
	}

	@Override
	protected void createIn() {
		in = new InventoryStatusEntryInVO();
	}

	@Override
	public Date getEndDate() {
		return getOpenStopTimestamp(in.getStop());
	}

	public InventoryStatusEntryOutVO getOut() {
		return out;
	}

	@Override
	public Date getStartDate() {
		return DateUtil.sanitizeScheduleTimestamp(true, in.getStart());
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
			if (in.getTypeId() != null || in.getAddresseeId() != null || !CommonUtil.isEmptyString(in.getComment())) {
				sb.append(EVENT_TITLE_HEAD_SEPARATOR);
			}
		}
		boolean appended = false;
		InventoryStatusTypeVO statusType = (out != null ? out.getType() : null);
		if (statusType != null && in.getTypeId() != null && !in.getTypeId().equals(statusType.getId())
				|| (statusType == null && in.getTypeId() != null)) {
			statusType = WebUtil.getInventoryStatusType(in.getTypeId());
		} else if (statusType != null && in.getTypeId() == null) {
			statusType = null;
		}
		if (statusType != null) {
			if (appended) {
				sb.append(EVENT_TITLE_SEPARATOR);
			}
			sb.append(statusType.getName());
			appended = true;
		}
		StaffOutVO addressee = (out != null ? out.getAddressee() : null);
		if ((addressee != null && in.getAddresseeId() != null && !in.getAddresseeId().equals(addressee.getId()))
				|| (addressee == null && in.getAddresseeId() != null)) {
			addressee = WebUtil.getStaff(in.getAddresseeId(), null, null, null);
		} else if (addressee != null && in.getAddresseeId() == null) {
			addressee = null;
		}
		if (addressee != null) {
			if (appended) {
				sb.append(EVENT_TITLE_SEPARATOR);
			}
			sb.append(CommonUtil.staffOutVOToString(addressee));
			appended = true;
		}
		if (!CommonUtil.isEmptyString(in.getComment())) {
			if (appended) {
				sb.append(EVENT_TITLE_SEPARATOR);
			}
			sb.append(CommonUtil.clipString(in.getComment(), Settings.getInt(SettingCodes.COMMENT_CLIP_MAX_LENGTH, Bundle.SETTINGS, DefaultSettings.COMMENT_CLIP_MAX_LENGTH),
					CommonUtil.DEFAULT_ELLIPSIS, EllipsisPlacement.TRAILING));
		}
		return sb.toString();
	}

	@Override
	protected void initDefaultValues() {
		InventoryStatusBean.initStatusEntryDefaultValues(in, null);
	}

	@Override
	public boolean isAllDay() {
		return false;
	}

	@Override
	public boolean isEditable() {
		return false;
	}

	public void setOut(InventoryStatusEntryOutVO out) {
		this.out = out;
		this.initIn(InitSource.OUT);
	}
}
