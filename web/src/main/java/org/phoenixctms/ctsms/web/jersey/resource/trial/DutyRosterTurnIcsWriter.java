package org.phoenixctms.ctsms.web.jersey.resource.trial;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.TimeZone;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.DutyRosterTurnOutVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.vo.VisitScheduleItemOutVO;

final class DutyRosterTurnIcsWriter {

	private static final String CRLF = "\r\n";
	private static final String TITLE_SEPARATOR = " - ";
	private static final TimeZone UTC = TimeZone.getTimeZone("UTC");

	static String toIcalendar(Collection<DutyRosterTurnOutVO> dutyRosterTurns, String host) {
		StringBuilder sb = new StringBuilder();
		SimpleDateFormat utc = newUtcFormat();
		Date now = new Date();
		sb.append("BEGIN:VCALENDAR").append(CRLF);
		sb.append("VERSION:2.0").append(CRLF);
		sb.append("PRODID:-//Phoenix CTMS//Duty Roster//EN").append(CRLF);
		sb.append("CALSCALE:GREGORIAN").append(CRLF);
		sb.append("METHOD:PUBLISH").append(CRLF);
		sb.append("X-WR-CALNAME:Phoenix Duty Roster").append(CRLF);
		if (dutyRosterTurns != null) {
			Iterator<DutyRosterTurnOutVO> it = dutyRosterTurns.iterator();
			while (it.hasNext()) {
				appendEvent(sb, it.next(), host, utc, now);
			}
		}
		sb.append("END:VCALENDAR").append(CRLF);
		return sb.toString();
	}

	private static void appendEvent(StringBuilder sb, DutyRosterTurnOutVO turn, String host, SimpleDateFormat utc, Date now) {
		if (turn == null || turn.getId() == null || turn.getStart() == null || turn.getStop() == null) {
			return;
		}
		sb.append("BEGIN:VEVENT").append(CRLF);
		sb.append("UID:dutyrosterturn-").append(turn.getId()).append("@").append(sanitizeHost(host)).append(CRLF);
		sb.append("DTSTAMP:").append(utc.format(now)).append(CRLF);
		sb.append("DTSTART:").append(utc.format(turn.getStart())).append(CRLF);
		sb.append("DTEND:").append(utc.format(turn.getStop())).append(CRLF);
		appendTextLine(sb, "SUMMARY", getSummary(turn));
		appendTextLine(sb, "DESCRIPTION", getDescription(turn));
		appendTextLine(sb, "CATEGORIES", turn.getCalendar());
		sb.append("END:VEVENT").append(CRLF);
	}

	private static void appendTextLine(StringBuilder sb, String name, String value) {
		if (CommonUtil.isEmptyString(value)) {
			return;
		}
		sb.append(name).append(":").append(escapeText(value)).append(CRLF);
	}

	private static String getSummary(DutyRosterTurnOutVO turn) {
		StringBuilder sb = new StringBuilder();
		TrialOutVO trial = turn.getTrial();
		if (trial != null) {
			sb.append(CommonUtil.trialOutVOToString(trial));
		}
		VisitScheduleItemOutVO visitScheduleItem = turn.getVisitScheduleItem();
		if (visitScheduleItem != null && !CommonUtil.isEmptyString(visitScheduleItem.getName())) {
			appendSeparated(sb, visitScheduleItem.getName());
		}
		if (!CommonUtil.isEmptyString(turn.getTitle())) {
			appendSeparated(sb, turn.getTitle());
		}
		StaffOutVO staff = turn.getStaff();
		if (staff != null) {
			appendSeparated(sb, CommonUtil.staffOutVOToString(staff));
		}
		return sb.toString();
	}

	private static String getDescription(DutyRosterTurnOutVO turn) {
		StringBuilder sb = new StringBuilder();
		if (!CommonUtil.isEmptyString(turn.getCalendar())) {
			sb.append(turn.getCalendar());
		}
		if (!CommonUtil.isEmptyString(turn.getComment())) {
			if (sb.length() > 0) {
				sb.append("\n");
			}
			sb.append(turn.getComment());
		}
		return sb.toString();
	}

	private static void appendSeparated(StringBuilder sb, String value) {
		if (sb.length() > 0) {
			sb.append(TITLE_SEPARATOR);
		}
		sb.append(value);
	}

	private static String escapeText(String value) {
		return value.replace("\\", "\\\\").replace(";", "\\;").replace(",", "\\,").replace("\r\n", "\\n").replace("\n", "\\n");
	}

	private static String sanitizeHost(String host) {
		if (CommonUtil.isEmptyString(host)) {
			return "phoenixctms";
		}
		return host.replace(':', '.').replace('/', '.');
	}

	private static SimpleDateFormat newUtcFormat() {
		SimpleDateFormat utc = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
		utc.setTimeZone(UTC);
		return utc;
	}

	private DutyRosterTurnIcsWriter() {
	}
}
