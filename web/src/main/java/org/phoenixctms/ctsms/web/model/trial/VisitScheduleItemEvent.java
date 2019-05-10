package org.phoenixctms.ctsms.web.model.trial;

import java.util.Date;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CommonUtil.EllipsisPlacement;
import org.phoenixctms.ctsms.vo.ProbandGroupOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.vo.VisitOutVO;
import org.phoenixctms.ctsms.vo.VisitScheduleItemInVO;
import org.phoenixctms.ctsms.vo.VisitScheduleItemOutVO;
import org.phoenixctms.ctsms.web.model.ScheduleEventBase;
import org.phoenixctms.ctsms.web.util.DateUtil;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.phoenixctms.ctsms.web.util.WebUtil.ColorOpacity;
import org.primefaces.model.ScheduleEvent;

public class VisitScheduleItemEvent extends ScheduleEventBase<VisitScheduleItemInVO> {

	private VisitScheduleItemOutVO out;
	private final static String EVENT_TITLE_HEAD_SEPARATOR = ": ";
	private final static String TOKEN_SEPARATOR_STRING = ":";
	private final static String EVENT_TITLE_SEPARATOR = "\n";
	protected final static ColorOpacity COLOR_OPACITY = ColorOpacity.ALPHA25;

	public VisitScheduleItemEvent() {
		super();
	}

	public VisitScheduleItemEvent(ScheduleEvent event) {
		setEvent(event);
	}

	public VisitScheduleItemEvent(VisitScheduleItemInVO visitScheduleItem) {
		super(visitScheduleItem);
	}

	public VisitScheduleItemEvent(VisitScheduleItemOutVO visitScheduleItem) {
		setOut(visitScheduleItem);
	}

	@Override
	protected boolean copyOutToIn() {
		if (out != null) {
			VisitScheduleBean.copyVisitScheduleItemOutToIn(in, out);
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

	public VisitScheduleItemOutVO getOut() {
		return out;
	}

	@Override
	public Date getStartDate() {
		return DateUtil.sanitizeScheduleTimestamp(true, in.getStart());
	}

	@Override
	public String getStyleClass() {
		// if (in.getVisitId() != null) {
		VisitOutVO visit = WebUtil.getVisit(in.getVisitId());
		if (visit != null) {
			return WebUtil.colorToStyleClass(visit.getType().getColor(), COLOR_OPACITY);
		}
		// }
		return "";
	}

	@Override
	public String getTitle() {
		StringBuilder sb = new StringBuilder();
		// if (in.getTrialId() != null) {
		TrialOutVO trial = WebUtil.getTrial(in.getTrialId());
		if (trial != null) {
			sb.append(CommonUtil.trialOutVOToString(trial));
			if (in.getGroupId() != null || in.getVisitId() != null || !CommonUtil.isEmptyString(in.getToken())) {
				sb.append(EVENT_TITLE_HEAD_SEPARATOR);
			}
		}
		// }
		boolean appended = false;
		ProbandGroupOutVO probandGroup = null;
		// if (in.getGroupId() != null) {
		probandGroup = WebUtil.getProbandGroup(in.getGroupId());
		if (probandGroup != null) {
			if (appended) {
				sb.append(TOKEN_SEPARATOR_STRING);
			}
			sb.append(probandGroup.getToken());
			appended = true;
		}
		// }
		VisitOutVO visit = null;
		// if (in.getVisitId() != null) {
		visit = WebUtil.getVisit(in.getVisitId());
		if (visit != null) {
			if (appended) {
				sb.append(TOKEN_SEPARATOR_STRING);
			}
			sb.append(visit.getToken());
			appended = true;
		}
		// }
		if (!CommonUtil.isEmptyString(in.getToken())) {
			if (appended) {
				sb.append(TOKEN_SEPARATOR_STRING);
			}
			sb.append(in.getToken());
			appended = true;
		}
		if (probandGroup != null) {
			if (appended) {
				sb.append(EVENT_TITLE_SEPARATOR);
			}
			sb.append(Messages.getMessage(MessageCodes.SCHEDULE_EVENT_PROBAND_GROUP_TITLE, probandGroup.getTitle(), probandGroup.getSize(), probandGroup.getTotalSize()));
			if (!CommonUtil.isEmptyString(probandGroup.getDescription())) {
				sb.append(EVENT_TITLE_SEPARATOR);
				sb.append(CommonUtil.clipString(probandGroup.getDescription(),
						Settings.getInt(SettingCodes.COMMENT_CLIP_MAX_LENGTH, Bundle.SETTINGS, DefaultSettings.COMMENT_CLIP_MAX_LENGTH), CommonUtil.DEFAULT_ELLIPSIS,
						EllipsisPlacement.TRAILING));
			}
			appended = true;
		}
		if (visit != null) {
			if (appended) {
				sb.append(EVENT_TITLE_SEPARATOR);
			}
			sb.append(visit.getTitle());
			if (!CommonUtil.isEmptyString(visit.getDescription())) {
				sb.append(EVENT_TITLE_SEPARATOR);
				sb.append(CommonUtil.clipString(visit.getDescription(),
						Settings.getInt(SettingCodes.COMMENT_CLIP_MAX_LENGTH, Bundle.SETTINGS, DefaultSettings.COMMENT_CLIP_MAX_LENGTH), CommonUtil.DEFAULT_ELLIPSIS,
						EllipsisPlacement.TRAILING));
			}
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

	public void setOut(VisitScheduleItemOutVO out) {
		this.out = out;
		initIn(InitSource.OUT);
	}
}
