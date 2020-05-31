package org.phoenixctms.ctsms.web.model.trial;

import java.util.Date;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CommonUtil.EllipsisPlacement;
import org.phoenixctms.ctsms.vo.ProbandGroupOutVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.vo.VisitOutVO;
import org.phoenixctms.ctsms.vo.VisitScheduleAppointmentVO;
import org.phoenixctms.ctsms.vo.VisitScheduleItemInVO;
import org.phoenixctms.ctsms.web.model.ScheduleEventBase;
import org.phoenixctms.ctsms.web.util.DateUtil;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.phoenixctms.ctsms.web.util.WebUtil.ColorOpacity;
import org.primefaces.model.ScheduleEvent;

public class VisitScheduleAppointmentEvent extends ScheduleEventBase<VisitScheduleItemInVO> {

	private VisitScheduleAppointmentVO out;
	private final static String EVENT_TITLE_HEAD_SEPARATOR = ": ";
	private final static String TOKEN_SEPARATOR_STRING = ":";
	private final static String EVENT_TITLE_SEPARATOR = "\n";
	protected final static ColorOpacity COLOR_OPACITY = ColorOpacity.ALPHA25;

	public VisitScheduleAppointmentEvent() {
		super();
	}

	public VisitScheduleAppointmentEvent(ScheduleEvent event) {
		setEvent(event);
	}

	public VisitScheduleAppointmentEvent(VisitScheduleItemInVO visitScheduleAppointment) {
		super(visitScheduleAppointment);
	}

	public VisitScheduleAppointmentEvent(VisitScheduleAppointmentVO visitScheduleAppointment) {
		setOut(visitScheduleAppointment);
	}

	@Override
	protected boolean copyOutToIn() {
		if (out != null) {
			VisitScheduleBean.copyVisitScheduleAppointmentToIn(in, out);
			return true;
		}
		return false;
	}

	@Override
	protected void copyToIn(VisitScheduleItemInVO source) {
		in.copy(source);
	}

	@Override
	protected void createIn() {
		in = new VisitScheduleItemInVO();
	}

	@Override
	public Date getEndDate() {
		return DateUtil.sanitizeScheduleTimestamp(true, in.getStop());
	}

	public VisitScheduleAppointmentVO getOut() {
		return out;
	}

	@Override
	public Date getStartDate() {
		return DateUtil.sanitizeScheduleTimestamp(true, in.getStart());
	}

	@Override
	public String getStyleClass() {
		VisitOutVO visit = WebUtil.getVisit(in.getVisitId());
		if (visit != null) {
			return WebUtil.colorToStyleClass(visit.getType().getColor(), COLOR_OPACITY);
		}
		return "";
	}

	@Override
	public String getTitle() {
		StringBuilder sb = new StringBuilder();
		TrialOutVO trial = WebUtil.getTrial(in.getTrialId());
		if (trial != null) {
			sb.append(CommonUtil.trialOutVOToString(trial));
			if (in.getGroupId() != null || in.getVisitId() != null || !CommonUtil.isEmptyString(in.getToken())) {
				sb.append(EVENT_TITLE_HEAD_SEPARATOR);
			}
		}
		boolean appended = false;
		ProbandGroupOutVO probandGroup = null;
		probandGroup = WebUtil.getProbandGroup(in.getGroupId());
		if (probandGroup != null) {
			if (appended) {
				sb.append(TOKEN_SEPARATOR_STRING);
			}
			sb.append(probandGroup.getToken());
			appended = true;
		}
		VisitOutVO visit = null;
		visit = WebUtil.getVisit(in.getVisitId());
		if (visit != null) {
			if (appended) {
				sb.append(TOKEN_SEPARATOR_STRING);
			}
			sb.append(visit.getToken());
			appended = true;
		}
		if (!CommonUtil.isEmptyString(in.getToken())) {
			if (appended) {
				sb.append(TOKEN_SEPARATOR_STRING);
			}
			sb.append(in.getToken());
			appended = true;
		}
		ProbandOutVO proband = null;
		proband = (out != null ? out.getProband() : null);
		if (proband != null) {
			if (appended) {
				sb.append(EVENT_TITLE_SEPARATOR);
			}
			sb.append(CommonUtil.probandOutVOToString(proband));
			appended = true;
		}
		if (probandGroup != null && !CommonUtil.isEmptyString(probandGroup.getDescription())) {
			if (appended) {
				sb.append(EVENT_TITLE_SEPARATOR);
			}
			sb.append(CommonUtil.clipString(probandGroup.getDescription(),
					Settings.getInt(SettingCodes.COMMENT_CLIP_MAX_LENGTH, Bundle.SETTINGS, DefaultSettings.COMMENT_CLIP_MAX_LENGTH), CommonUtil.DEFAULT_ELLIPSIS,
					EllipsisPlacement.TRAILING));
			appended = true;
		}
		if (visit != null && !CommonUtil.isEmptyString(visit.getDescription())) {
			if (appended) {
				sb.append(EVENT_TITLE_SEPARATOR);
			}
			sb.append(CommonUtil.clipString(visit.getDescription(),
					Settings.getInt(SettingCodes.COMMENT_CLIP_MAX_LENGTH, Bundle.SETTINGS, DefaultSettings.COMMENT_CLIP_MAX_LENGTH), CommonUtil.DEFAULT_ELLIPSIS,
					EllipsisPlacement.TRAILING));
		}
		return sb.toString();
	}

	@Override
	protected void initDefaultValues() {
		VisitScheduleBean.initVisitScheduleItemDefaultValues(in, null);
	}

	@Override
	public boolean isAllDay() {
		return false;
	}

	@Override
	public boolean isEditable() {
		return false;
	}

	public void setOut(VisitScheduleAppointmentVO out) {
		this.out = out;
		initIn(InitSource.OUT);
	}
}
