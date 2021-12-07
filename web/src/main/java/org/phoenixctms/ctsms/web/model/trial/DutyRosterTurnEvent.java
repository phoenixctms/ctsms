package org.phoenixctms.ctsms.web.model.trial;

import java.util.Date;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CommonUtil.EllipsisPlacement;
import org.phoenixctms.ctsms.vo.DutyRosterTurnInVO;
import org.phoenixctms.ctsms.vo.DutyRosterTurnOutVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.vo.VisitScheduleItemOutVO;
import org.phoenixctms.ctsms.web.model.ScheduleEventBase;
import org.phoenixctms.ctsms.web.model.shared.DutyRosterTurnBeanBase;
import org.phoenixctms.ctsms.web.model.staff.StaffDutyRosterTurnBean;
import org.phoenixctms.ctsms.web.util.DateUtil;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.model.ScheduleEvent;

public class DutyRosterTurnEvent extends ScheduleEventBase<DutyRosterTurnInVO> {

	private final static String EVENT_TITLE_HEAD_SEPARATOR = " ";
	private final static String EVENT_TITLE_SEPARATOR = "\n";
	private DutyRosterTurnOutVO out;
	int collidingStaffStatusEntryCount = 0;
	int collidingInventoryBookingCount = 0;

	public DutyRosterTurnEvent() {
		super();
	}

	public DutyRosterTurnEvent(DutyRosterTurnInVO dutyRosterTurn) {
		super(dutyRosterTurn);
	}

	public DutyRosterTurnEvent(DutyRosterTurnOutVO dutyRosterTurn) {
		setOut(dutyRosterTurn);
	}

	public DutyRosterTurnEvent(ScheduleEvent event) {
		setEvent(event);
	}

	@Override
	protected boolean copyOutToIn() {
		if (out != null) {
			DutyRosterTurnBeanBase.copyDutyRosterTurnOutToIn(in, out);
			return true;
		}
		return false;
	}

	@Override
	protected void copyToIn(DutyRosterTurnInVO source) {
		in.copy(source);
	}

	@Override
	protected void createIn() {
		in = new DutyRosterTurnInVO();
	}

	@Override
	public Date getEndDate() {
		return DateUtil.sanitizeScheduleTimestamp(true, in.getStop());
	}

	public DutyRosterTurnOutVO getOut() {
		return out;
	}

	@Override
	public Date getStartDate() {
		return DateUtil.sanitizeScheduleTimestamp(true, in.getStart());
	}

	@Override
	public String getStyleClass() {
		boolean isDutySelfAllocationLocked = !in.isSelfAllocatable();
		if (!isDutySelfAllocationLocked && in.getTrialId() != null) {
			TrialOutVO trial = (out != null ? out.getTrial() : null);
			if (trial != null && in.getTrialId() != null && !in.getTrialId().equals(trial.getId())
					|| (trial == null && in.getTrialId() != null)) {
				trial = WebUtil.getTrial(in.getTrialId());
			} else if (trial != null && in.getTrialId() == null) {
				trial = null;
			}
			isDutySelfAllocationLocked = WebUtil.isDutySelfAllocationLocked(trial, in.getStart(), in.getStaffId() != null);
		}
		if (in.getStaffId() != null) {
			StaffOutVO staff = (out != null ? out.getStaff() : null);
			if ((staff != null && in.getStaffId() != null && !in.getStaffId().equals(staff.getId()))
					|| (staff == null && in.getStaffId() != null)) {
				staff = WebUtil.getStaff(in.getStaffId(), null, null, null);
			} else if (staff != null && in.getStaffId() == null) {
				staff = null;
			}
			if (staff != null) {
				StringBuilder sb = new StringBuilder();
				if (isDutySelfAllocationLocked) {
					sb.append(WebUtil.colorToStyleClass(Settings.getColor(SettingCodes.DUTY_SELF_ALLOCATION_LOCKED_COLOR, Bundle.SETTINGS,
							DefaultSettings.DUTY_SELF_ALLOCATION_LOCKED_COLOR), DEFAULT_COLOR_OPACITY));
				} else {
					sb.append(WebUtil.colorToStyleClass(staff.getCategory().getColor(), DEFAULT_COLOR_OPACITY));
				}
				sb.append(" ");
				sb.append(WebUtil.SCHEDULE_EVENT_ICON_STYLECLASS);
				if (collidingStaffStatusEntryCount > 0 || collidingInventoryBookingCount > 0) {
					sb.append(" ");
					sb.append(WebUtil.COLLISION_ICON_STYLECLASS);
				} else if (Settings
						.getBoolean(SettingCodes.SHOW_DUTY_ROSTER_TURN_SCHEDULE_EVENT_STAFF_ICONS, Bundle.SETTINGS,
								DefaultSettings.SHOW_DUTY_ROSTER_TURN_SCHEDULE_EVENT_STAFF_ICONS)) {
					sb.append(" ");
					sb.append(staff.getCategory().getNodeStyleClass());
				}
				return sb.toString();
			}
		} else if (isDutySelfAllocationLocked) {
			return WebUtil.colorToStyleClass(Settings.getColor(SettingCodes.DUTY_SELF_ALLOCATION_LOCKED_COLOR, Bundle.SETTINGS, DefaultSettings.DUTY_SELF_ALLOCATION_LOCKED_COLOR),
					DEFAULT_COLOR_OPACITY);
		}
		return "";
	}

	@Override
	public String getTitle() {
		StringBuilder sb = new StringBuilder();
		boolean appended = false;
		TrialOutVO trial = (out != null ? out.getTrial() : null);
		if ((trial != null && in.getTrialId() != null && !in.getTrialId().equals(trial.getId()))
				|| (trial == null && in.getTrialId() != null)) {
			trial = WebUtil.getTrial(in.getTrialId());
		} else if (trial != null && in.getTrialId() == null) {
			trial = null;
		}
		if (trial != null) {
			sb.append(CommonUtil.trialOutVOToString(trial));
			appended = true;
		}
		VisitScheduleItemOutVO visitScheduleItem = (out != null ? out.getVisitScheduleItem() : null);
		if ((visitScheduleItem != null && in.getVisitScheduleItemId() != null && !in.getVisitScheduleItemId().equals(visitScheduleItem.getId()))
				|| (visitScheduleItem == null && in.getVisitScheduleItemId() != null)) {
			visitScheduleItem = WebUtil.getVisitScheduleItem(in.getVisitScheduleItemId());
		} else if (visitScheduleItem != null && in.getVisitScheduleItemId() == null) {
			visitScheduleItem = null;
		}
		if (visitScheduleItem != null) {
			if (appended) {
				sb.append(EVENT_TITLE_HEAD_SEPARATOR);
			} else {
				sb.append(CommonUtil.trialOutVOToString(visitScheduleItem.getTrial()));
				sb.append(EVENT_TITLE_HEAD_SEPARATOR);
			}
			sb.append(visitScheduleItem.getName());
			appended = true;
		}
		if (!CommonUtil.isEmptyString(in.getTitle())) {
			if (appended) {
				sb.append(EVENT_TITLE_SEPARATOR);
			}
			sb.append(in.getTitle());
			appended = true;
		}
		StaffOutVO staff = (out != null ? out.getStaff() : null);
		if ((staff != null && in.getStaffId() != null && !in.getStaffId().equals(staff.getId()))
				|| (staff == null && in.getStaffId() != null)) {
			staff = WebUtil.getStaff(in.getStaffId(), null, null, null);
		} else if (staff != null && in.getStaffId() == null) {
			staff = null;
		}
		if (staff != null) {
			if (appended) {
				sb.append(EVENT_TITLE_SEPARATOR);
			}
			sb.append(CommonUtil.staffOutVOToString(staff));
			appended = true;
		}
		if (!CommonUtil.isEmptyString(in.getComment())) {
			if (appended) {
				sb.append(EVENT_TITLE_SEPARATOR);
			}
			sb.append(CommonUtil.clipString(in.getComment(), Settings.getInt(SettingCodes.COMMENT_CLIP_MAX_LENGTH, Bundle.SETTINGS, DefaultSettings.COMMENT_CLIP_MAX_LENGTH),
					CommonUtil.DEFAULT_ELLIPSIS, EllipsisPlacement.TRAILING));
		}
		if (sb.length() == 0) {
			sb.append(Messages.getString(MessageCodes.EMPTY_DUTY_ROSTER_TURN_LABEL));
		}
		return sb.toString();
	}

	@Override
	protected void initDefaultValues() {
		StaffOutVO identity = WebUtil.getUserIdentity();
		StaffDutyRosterTurnBean.initDutyRosterTurnDefaultValues(in, identity != null ? identity.getId() : null);
	}

	@Override
	public boolean isAllDay() {
		return false;
	}

	@Override
	public boolean isEditable() {
		return true;
	}

	public void setCollidingInventoryBookingCount(int collidingInventoryBookingCount) {
		this.collidingInventoryBookingCount = collidingInventoryBookingCount;
	}

	public void setCollidingStaffStatusEntryCount(int collidingStaffStatusEntryCount) {
		this.collidingStaffStatusEntryCount = collidingStaffStatusEntryCount;
	}

	public void setOut(DutyRosterTurnOutVO out) {
		this.out = out;
		initIn(InitSource.OUT);
	}
}
