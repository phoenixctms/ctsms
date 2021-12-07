package org.phoenixctms.ctsms.web.model.trial;

import org.phoenixctms.ctsms.vo.InventoryBookingInVO;
import org.phoenixctms.ctsms.vo.InventoryBookingOutVO;
import org.phoenixctms.ctsms.vo.InventoryOutVO;
import org.phoenixctms.ctsms.web.model.shared.InventoryBookingEvent;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.phoenixctms.ctsms.web.util.WebUtil.ColorOpacity;
import org.primefaces.model.ScheduleEvent;

public class TrialInventoryBookingEvent extends InventoryBookingEvent {

	protected final static ColorOpacity COLOR_OPACITY = ColorOpacity.ALPHA25;

	public TrialInventoryBookingEvent() {
		super();
	}

	public TrialInventoryBookingEvent(InventoryBookingInVO booking) {
		super(booking);
	}

	public TrialInventoryBookingEvent(InventoryBookingOutVO booking) {
		super(booking);
	}

	public TrialInventoryBookingEvent(ScheduleEvent event) {
		super(event);
	}

	@Override
	public String getStyleClass() {
		InventoryOutVO inventory = (out != null ? out.getInventory() : null);
		if (inventory != null && in.getInventoryId() != null && !in.getInventoryId().equals(inventory.getId())
				|| (inventory == null && in.getInventoryId() != null)) {
			inventory = WebUtil.getInventory(in.getInventoryId(), null, null, null);
		} else if (inventory != null && in.getInventoryId() == null) {
			inventory = null;
		}
		if (inventory != null) {
			StringBuilder sb = new StringBuilder();
			appendTrialColorStyleClass(inventory, sb, COLOR_OPACITY);
			sb.append(" ");
			sb.append(WebUtil.SCHEDULE_EVENT_ICON_STYLECLASS);
			if (Settings.getBoolean(SettingCodes.SHOW_DUTY_ROSTER_TURN_SCHEDULE_EVENT_INVENTORY_ICONS, Bundle.SETTINGS,
					DefaultSettings.SHOW_DUTY_ROSTER_TURN_SCHEDULE_EVENT_INVENTORY_ICONS)) {
				sb.append(" ");
				sb.append(inventory.getCategory().getNodeStyleClass());
			}
			return sb.toString();
		}
		return "";
	}

	@Override
	public boolean isEditable() {
		return false;
	}
}
