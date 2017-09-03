package org.phoenixctms.ctsms.web.model.trial;

import java.util.Date;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CommonUtil.EllipsisPlacement;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.StaffStatusEntryInVO;
import org.phoenixctms.ctsms.vo.StaffStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.StaffStatusTypeVO;
import org.phoenixctms.ctsms.web.model.ScheduleEventBase;
import org.phoenixctms.ctsms.web.model.staff.StaffStatusBean;
import org.phoenixctms.ctsms.web.util.DateUtil;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.model.ScheduleEvent;

public class StaffStatusEvent extends ScheduleEventBase<StaffStatusEntryInVO> {

	private StaffStatusEntryOutVO out;
	private final static String EVENT_STYLECLASS = "ctsms-staff-schedule-status-event";
	private final static String EVENT_TITLE_HEAD_SEPARATOR = ": ";
	private final static String EVENT_TITLE_SEPARATOR = "\n";

	public StaffStatusEvent() {
		super();
	}

	public StaffStatusEvent(ScheduleEvent event) {
		super(event);
	}

	public StaffStatusEvent(StaffStatusEntryInVO statusEntry) {
		super(statusEntry);
	}

	public StaffStatusEvent(StaffStatusEntryOutVO statusEntry) {
		setOut(statusEntry);
	}

	@Override
	protected boolean copyOutToIn() {
		if (out != null) {
			StaffStatusBean.copyStatusEntryOutToIn(in, out);
			return true;
		}
		return false;
	}

	@Override
	protected void copyToIn(StaffStatusEntryInVO source) {
		in.copy(source);
	}

	@Override
	protected void createIn() {
		in = new StaffStatusEntryInVO();
	}

	@Override
	public Date getEndDate() {
		return getOpenStopTimestamp(in.getStop());
	}

	public StaffStatusEntryOutVO getOut() {
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
		// if (in.getStaffId() != null) {
		StaffOutVO staff = WebUtil.getStaff(in.getStaffId(), null, null);
		if (staff != null) {
			sb.append(CommonUtil.staffOutVOToString(staff));
			if (in.getTypeId() != null || !CommonUtil.isEmptyString(in.getComment())) {
				sb.append(EVENT_TITLE_HEAD_SEPARATOR);
			}
		}
		// }
		boolean appended = false;
		// if (in.getTypeId() != null) {
		StaffStatusTypeVO statusType = WebUtil.getStaffStatusType(in.getTypeId());
		if (statusType != null) {
			if (appended) {
				sb.append(EVENT_TITLE_SEPARATOR);
			}
			sb.append(statusType.getName());
			appended = true;
		}
		// }
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
		StaffStatusBean.initStatusEntryDefaultValues(in, null);
	}

	@Override
	public boolean isAllDay() {
		return false;
	}

	@Override
	public boolean isEditable() {
		return false;
	}

	public void setOut(StaffStatusEntryOutVO out) {
		this.out = out;
		this.initIn(InitSource.OUT);
	}
}
