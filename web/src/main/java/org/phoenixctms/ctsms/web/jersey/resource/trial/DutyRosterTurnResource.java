package org.phoenixctms.ctsms.web.jersey.resource.trial;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.DutyRosterTurnOutVO;
import org.phoenixctms.ctsms.web.jersey.resource.StringConverter;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;

import io.swagger.annotations.Api;

@Api(value = "trial")
@Path("/dutyrosterturn")
public class DutyRosterTurnResource {

	public static final String ICS_PATH = "/dutyrosterturn/ics";
	public static final String JWT_QUERY_PARAM = "jwt";
	public static final String TEXT_CALENDAR = "text/calendar";
	private static final String ICS_FILENAME = "dutyroster.ics";

	@Context
	AuthenticationVO auth;

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("interval")
	public Collection<DutyRosterTurnOutVO> getDutyRosterInterval(
			@QueryParam("department_id") Long departmentId,
			@QueryParam("status_id") Long statusId,
			@QueryParam("staff_id") Long staffId,
			@QueryParam("unassigned") Boolean unassigned,
			@QueryParam("trial_id") Long trialId,
			@QueryParam("calendar") List<String> calendar,
			@QueryParam("from") String from,
			@QueryParam("to") String to,
			@QueryParam("sort") Boolean sort) throws Exception {
		return loadDutyRosterInterval(departmentId, statusId, staffId, unassigned, trialId, calendar, from, to, sort, false);
	}

	@GET
	@Produces({ TEXT_CALENDAR })
	@Path("ics")
	public Response getDutyRosterIcs(
			@QueryParam("department_id") Long departmentId,
			@QueryParam("status_id") Long statusId,
			@QueryParam("staff_id") Long staffId,
			@QueryParam("unassigned") Boolean unassigned,
			@QueryParam("trial_id") Long trialId,
			@QueryParam("calendar") List<String> calendar,
			@QueryParam("from") String from,
			@QueryParam("to") String to,
			@QueryParam("sort") Boolean sort) throws Exception {
		Collection<DutyRosterTurnOutVO> dutyRosterTurns = loadDutyRosterInterval(departmentId, statusId, staffId, unassigned, trialId, calendar, from, to, sort, true);
		String ics = DutyRosterTurnIcsWriter.toIcalendar(dutyRosterTurns, WebUtil.getHttpHost());
		ResponseBuilder response = Response.ok(ics, TEXT_CALENDAR + ";charset=UTF-8");
		response.header("Content-Disposition", "inline; filename=\"" + ICS_FILENAME + "\"");
		return response.build();
	}

	private Collection<DutyRosterTurnOutVO> loadDutyRosterInterval(Long departmentId, Long statusId, Long staffId, Boolean unassigned, Long trialId,
			List<String> calendar, String from, String to, Boolean sort, boolean defaultInterval) throws Exception {
		Date fromDate = parseDate(from);
		Date toDate = parseDate(to);
		if (defaultInterval) {
			if (fromDate == null) {
				fromDate = shiftDay(Settings.getInt(SettingCodes.API_DUTYROSTER_ICS_PAST_DAYS, Bundle.SETTINGS, DefaultSettings.API_DUTYROSTER_ICS_PAST_DAYS) * -1);
			}
			if (toDate == null) {
				toDate = shiftDay(Settings.getInt(SettingCodes.API_DUTYROSTER_ICS_FUTURE_DAYS, Bundle.SETTINGS, DefaultSettings.API_DUTYROSTER_ICS_FUTURE_DAYS));
			}
		}
		Set<String> calendars = null;
		if (calendar != null && calendar.size() > 0) {
			calendars = new LinkedHashSet<String>(calendar);
		}
		boolean includeUnassigned = unassigned == null ? true : unassigned.booleanValue();
		boolean sorted = sort == null ? true : sort.booleanValue();
		return WebUtil.getServiceLocator().getTrialService().getDutyRosterInterval(auth, departmentId, statusId, staffId, includeUnassigned, trialId, calendars, fromDate, toDate,
				sorted);
	}

	private static Date parseDate(String value) throws Exception {
		if (CommonUtil.isEmptyString(value)) {
			return null;
		}
		return (Date) StringConverter.getConverter(Date.class).convert(value);
	}

	private static Date shiftDay(int days) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, days);
		return cal.getTime();
	}
}
