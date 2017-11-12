package org.phoenixctms.ctsms.web.model.trial;

import java.util.Date;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CommonUtil.EllipsisPlacement;
import org.phoenixctms.ctsms.vo.TimelineEventInVO;
import org.phoenixctms.ctsms.vo.TimelineEventOutVO;
import org.phoenixctms.ctsms.vo.TimelineEventTypeVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.web.model.ScheduleEventBase;
import org.phoenixctms.ctsms.web.util.DateUtil;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.phoenixctms.ctsms.web.util.WebUtil.ColorOpacity;
import org.primefaces.model.ScheduleEvent;

public class TimelineEvent extends ScheduleEventBase<TimelineEventInVO> {

	private TimelineEventOutVO out;
	private final static String EVENT_TITLE_HEAD_SEPARATOR = ": ";
	private final static String EVENT_TITLE_SEPARATOR = " | ";
	protected final static ColorOpacity COLOR_OPACITY = ColorOpacity.ALPHA25;
	public TimelineEvent() {
		super();
	}

	public TimelineEvent(ScheduleEvent event) {
		setEvent(event);
	}

	public TimelineEvent(TimelineEventInVO timelineEvent) {
		super(timelineEvent);
	}

	public TimelineEvent(TimelineEventOutVO timelineEvent) {
		setOut(timelineEvent);
	}

	@Override
	protected boolean copyOutToIn() {
		if (out != null) {
			TimelineEventBean.copyTimelineEventOutToIn(in, out, new Date());
			return true;
		}
		return false;
	}

	@Override
	protected void copyToIn(TimelineEventInVO source) {
		in.copy(source);
	}

	@Override
	protected void createIn() {
		in = new TimelineEventInVO();
	}

	@Override
	public Date getEndDate() {
		return in.getStop() == null ? getStartDate() : DateUtil.sanitizeScheduleDate(true, in.getStop());
	}

	public TimelineEventOutVO getOut() {
		return out;
	}

	@Override
	public Date getStartDate() {
		return DateUtil.sanitizeScheduleDate(true, in.getStart());
	}

	@Override
	public String getStyleClass() {
		// if (in.getTypeId() != null) {
		TimelineEventTypeVO eventType = WebUtil.getTimelineEventType(in.getTypeId());
		if (eventType != null) {
			StringBuilder sb = new StringBuilder();
			sb.append(WebUtil.colorToStyleClass(eventType.getColor(), COLOR_OPACITY));
			sb.append(" ");
			sb.append(WebUtil.SCHEDULE_EVENT_ICON_STYLECLASS);
			if (Settings.getBoolean(SettingCodes.SHOW_TIMELINE_EVENT_SCHEDULE_EVENT_ICONS, Bundle.SETTINGS, DefaultSettings.SHOW_TIMELINE_EVENT_SCHEDULE_EVENT_ICONS)) {
				sb.append(" ");
				sb.append(eventType.getNodeStyleClass());
			}
			return sb.toString();
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
			if (!CommonUtil.isEmptyString(in.getTitle()) || !CommonUtil.isEmptyString(in.getDescription())) {
				sb.append(EVENT_TITLE_HEAD_SEPARATOR);
			}
		}
		// }
		boolean appended = false;
		if (!CommonUtil.isEmptyString(in.getTitle())) {
			if (appended) {
				sb.append(EVENT_TITLE_SEPARATOR);
			}
			sb.append(in.getTitle());
			appended = true;
		}
		if (!CommonUtil.isEmptyString(in.getDescription())) {
			if (appended) {
				sb.append(EVENT_TITLE_SEPARATOR);
			}
			sb.append(CommonUtil.clipString(in.getDescription(), Settings.getInt(SettingCodes.COMMENT_CLIP_MAX_LENGTH, Bundle.SETTINGS, DefaultSettings.COMMENT_CLIP_MAX_LENGTH),
					CommonUtil.DEFAULT_ELLIPSIS, EllipsisPlacement.TRAILING));
		}
		return sb.toString();
	}

	@Override
	protected void initDefaultValues() {
		TimelineEventBean.initTimelineEventDefaultValues(in, null);
	}

	@Override
	public boolean isAllDay() {
		return true;
	}

	@Override
	public boolean isEditable() {
		return false;
	}

	public void setOut(TimelineEventOutVO out) {
		this.out = out;
		initIn(InitSource.OUT);
	}
}
