package org.phoenixctms.ctsms.web.model.shared;

import java.util.Date;
import java.util.Map;

import org.phoenixctms.ctsms.enumeration.Color;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CommonUtil.EllipsisPlacement;
import org.phoenixctms.ctsms.vo.CourseOutVO;
import org.phoenixctms.ctsms.vo.InventoryBookingInVO;
import org.phoenixctms.ctsms.vo.InventoryBookingOutVO;
import org.phoenixctms.ctsms.vo.InventoryOutVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.web.model.ScheduleEventBase;
import org.phoenixctms.ctsms.web.model.inventory.InventoryBookingBean;
import org.phoenixctms.ctsms.web.util.DateUtil;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.phoenixctms.ctsms.web.util.WebUtil.ColorOpacity;
import org.primefaces.model.ScheduleEvent;

public class InventoryBookingEvent extends ScheduleEventBase<InventoryBookingInVO> {

	private final static String EVENT_TITLE_SEPARATOR = "\n";
	protected InventoryBookingOutVO out;
	private int collidingInventoryStatusEntryCount = 0;
	private int collidingStaffStatusEntryCount = 0;
	private int collidingDutyRosterTurnCount = 0;
	private int collidingProbandStatusEntryCount = 0;
	private int collidingCourseParticipationStatusEntryCount = 0;
	private Map<Long, Color> trialColorMap = null;

	public InventoryBookingEvent() {
		super();
	}

	public InventoryBookingEvent(InventoryBookingInVO booking) {
		super(booking);
	}

	public InventoryBookingEvent(InventoryBookingOutVO booking) {
		setOut(booking);
	}

	public InventoryBookingEvent(ScheduleEvent event) {
		super(event);
	}

	@Override
	protected boolean copyOutToIn() {
		if (out != null) {
			InventoryBookingBeanBase.copyBookingOutToIn(in, out);
			return true;
		}
		return false;
	}

	@Override
	protected void copyToIn(InventoryBookingInVO source) {
		in.copy(source);
	}

	@Override
	protected void createIn() {
		in = new InventoryBookingInVO();
	}

	@Override
	public Date getEndDate() {
		return DateUtil.sanitizeScheduleTimestamp(true, in.getStop());
	}

	public InventoryBookingOutVO getOut() {
		return out;
	}

	@Override
	public Date getStartDate() {
		return DateUtil.sanitizeScheduleTimestamp(true, in.getStart());
	}

	protected void appendTrialColorStyleClass(InventoryOutVO inventory, StringBuilder sb, ColorOpacity alpha) {
		if (in.getTrialId() != null && trialColorMap != null && trialColorMap.size() > 0) {
			sb.append(WebUtil.colorToStyleClass(trialColorMap.get(in.getTrialId()), alpha));
		} else {
			sb.append(WebUtil.colorToStyleClass(inventory.getCategory().getColor(), alpha));
		}
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
			appendTrialColorStyleClass(inventory, sb, DEFAULT_COLOR_OPACITY);
			sb.append(" ");
			sb.append(WebUtil.SCHEDULE_EVENT_ICON_STYLECLASS);
			if (collidingInventoryStatusEntryCount > 0 ||
					collidingStaffStatusEntryCount > 0 ||
					collidingDutyRosterTurnCount > 0 ||
					collidingProbandStatusEntryCount > 0 ||
					collidingCourseParticipationStatusEntryCount > 0) {
				sb.append(" ");
				sb.append(WebUtil.COLLISION_ICON_STYLECLASS);
			} else if (Settings.getBoolean(SettingCodes.SHOW_INVENTORY_BOOKING_SCHEDULE_EVENT_INVENTORY_ICONS, Bundle.SETTINGS,
					DefaultSettings.SHOW_INVENTORY_BOOKING_SCHEDULE_EVENT_INVENTORY_ICONS)) {
				sb.append(" ");
				sb.append(inventory.getCategory().getNodeStyleClass());
			}
			return sb.toString();
		}
		return "";
	}

	@Override
	public String getTitle() {
		StringBuilder sb = new StringBuilder();
		boolean appended = false;
		ProbandOutVO proband = (out != null ? out.getProband() : null);
		if (proband != null && in.getProbandId() != null && !in.getProbandId().equals(proband.getId())
				|| (proband == null && in.getProbandId() != null)) {
			proband = WebUtil.getProband(in.getProbandId(), null, null, null);
		} else if (proband != null && in.getProbandId() == null) {
			proband = null;
		}
		if (proband != null) {
			if (appended) {
				sb.append(EVENT_TITLE_SEPARATOR);
			}
			sb.append(CommonUtil.probandOutVOToString(proband));
			appended = true;
		}
		TrialOutVO trial = (out != null ? out.getTrial() : null);
		if (trial != null && in.getTrialId() != null && !in.getTrialId().equals(trial.getId())
				|| (trial == null && in.getTrialId() != null)) {
			trial = WebUtil.getTrial(in.getTrialId());
		} else if (trial != null && in.getTrialId() == null) {
			trial = null;
		}
		if (trial != null) {
			if (appended) {
				sb.append(EVENT_TITLE_SEPARATOR);
			}
			sb.append(CommonUtil.trialOutVOToString(trial));
			appended = true;
		}
		CourseOutVO course = (out != null ? out.getCourse() : null);
		if (course != null && in.getCourseId() != null && !in.getCourseId().equals(course.getId())
				|| (course == null && in.getCourseId() != null)) {
			course = WebUtil.getCourse(in.getCourseId(), null, null, null);
		} else if (course != null && in.getCourseId() == null) {
			course = null;
		}
		if (course != null) {
			if (appended) {
				sb.append(EVENT_TITLE_SEPARATOR);
			}
			sb.append(CommonUtil.courseOutVOToString(course));
			appended = true;
		}
		InventoryOutVO inventory = (out != null ? out.getInventory() : null);
		if (inventory != null && in.getInventoryId() != null && !in.getInventoryId().equals(inventory.getId())
				|| (inventory == null && in.getInventoryId() != null)) {
			inventory = WebUtil.getInventory(in.getInventoryId(), null, null, null);
		} else if (inventory != null && in.getInventoryId() == null) {
			inventory = null;
		}
		if (inventory != null) {
			if (appended) {
				sb.append(EVENT_TITLE_SEPARATOR);
			}
			sb.append(CommonUtil.inventoryOutVOToString(inventory));
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
		InventoryBookingBean.initBookingDefaultValues(in, null, WebUtil.getUserIdentity());
	}

	@Override
	public boolean isAllDay() {
		return false;
	}

	@Override
	public boolean isEditable() {
		return true;
	}

	public void setCollidingCourseParticipationStatusEntryCount(
			int collidingCourseParticipationStatusEntryCount) {
		this.collidingCourseParticipationStatusEntryCount = collidingCourseParticipationStatusEntryCount;
	}

	public void setCollidingDutyRosterTurnCount(int collidingDutyRosterTurnCount) {
		this.collidingDutyRosterTurnCount = collidingDutyRosterTurnCount;
	}

	public void setCollidingInventoryStatusEntryCount(
			int collidingInventoryStatusEntryCount) {
		this.collidingInventoryStatusEntryCount = collidingInventoryStatusEntryCount;
	}

	public void setCollidingProbandStatusEntryCount(
			int collidingProbandStatusEntryCount) {
		this.collidingProbandStatusEntryCount = collidingProbandStatusEntryCount;
	}

	public void setCollidingStaffStatusEntryCount(int collidingStaffStatusEntryCount) {
		this.collidingStaffStatusEntryCount = collidingStaffStatusEntryCount;
	}

	public void setOut(InventoryBookingOutVO out) {
		this.out = out;
		initIn(InitSource.OUT);
	}

	public void setTrialColorMap(Map<Long, Color> trialColorMap) {
		this.trialColorMap = trialColorMap;
	}
}
