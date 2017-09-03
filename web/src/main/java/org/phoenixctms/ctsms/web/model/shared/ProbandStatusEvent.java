package org.phoenixctms.ctsms.web.model.shared;

import java.util.Date;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CommonUtil.EllipsisPlacement;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.vo.ProbandStatusEntryInVO;
import org.phoenixctms.ctsms.vo.ProbandStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.ProbandStatusTypeVO;
import org.phoenixctms.ctsms.web.model.ScheduleEventBase;
import org.phoenixctms.ctsms.web.model.proband.ProbandStatusBean;
import org.phoenixctms.ctsms.web.util.DateUtil;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.model.ScheduleEvent;

public class ProbandStatusEvent extends ScheduleEventBase<ProbandStatusEntryInVO> {

	private ProbandStatusEntryOutVO out;
	private final static String EVENT_STYLECLASS = "ctsms-proband-schedule-status-event";
	private final static String EVENT_TITLE_HEAD_SEPARATOR = ": ";
	private final static String EVENT_TITLE_SEPARATOR = "\n";

	public ProbandStatusEvent() {
		super();
	}

	public ProbandStatusEvent(ProbandStatusEntryInVO statusEntry) {
		super(statusEntry);
	}

	public ProbandStatusEvent(ProbandStatusEntryOutVO statusEntry) {
		setOut(statusEntry);
	}

	public ProbandStatusEvent(ScheduleEvent event) {
		super(event);
	}

	@Override
	protected boolean copyOutToIn() {
		if (out != null) {
			ProbandStatusBean.copyStatusEntryOutToIn(in, out);
			return true;
		}
		return false;
	}

	@Override
	protected void copyToIn(ProbandStatusEntryInVO source) {
		in.copy(source);
	}

	@Override
	protected void createIn() {
		in = new ProbandStatusEntryInVO();
	}

	@Override
	public Date getEndDate() {
		return getOpenStopTimestamp(in.getStop());
	}

	public ProbandStatusEntryOutVO getOut() {
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
		// if (in.getProbandId() != null) {
		ProbandOutVO proband = WebUtil.getProband(in.getProbandId(), null, null, null);
		if (proband != null) {
			sb.append(CommonUtil.probandOutVOToString(proband));
			if (in.getTypeId() != null || !CommonUtil.isEmptyString(in.getComment())) {
				sb.append(EVENT_TITLE_HEAD_SEPARATOR);
			}
		}
		// }
		boolean appended = false;
		// if (in.getTypeId() != null) {
		ProbandStatusTypeVO statusType = WebUtil.getProbandStatusType(in.getTypeId());
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
		ProbandStatusBean.initStatusEntryDefaultValues(in, null);
	}

	@Override
	public boolean isAllDay() {
		return false;
	}

	@Override
	public boolean isEditable() {
		return false;
	}

	public void setOut(ProbandStatusEntryOutVO out) {
		this.out = out;
		this.initIn(InitSource.OUT);
	}
}
