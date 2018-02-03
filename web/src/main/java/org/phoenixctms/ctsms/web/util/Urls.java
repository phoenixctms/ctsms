package org.phoenixctms.ctsms.web.util;

import javax.servlet.http.HttpServletRequest;

import org.phoenixctms.ctsms.enumeration.DBModule;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;

public enum Urls {
	INVENTORY_PICKER("/shared/inventoryPicker.jsf"),
	STAFF_PICKER("/shared/staffPicker.jsf"),
	COURSE_PICKER("/shared/coursePicker.jsf"),
	USER_PICKER("/shared/userPicker.jsf"),
	TRIAL_PICKER("/shared/trialPicker.jsf"),
	PROBAND_PICKER("/shared/probandPicker.jsf"),
	INPUT_FIELD_PICKER("/shared/inputFieldPicker.jsf"),
	TEAM_MEMBER_PICKER("/shared/teamMemberPicker.jsf"),
	MASS_MAIL_PICKER("/shared/massMailPicker.jsf"),
	ECRF_SECTION("/shared/ecrfSection.jsf"),
	INVENTORY("/inventory/inventory.jsf"),
	STAFF("/staff/staff.jsf"),
	COURSE("/course/course.jsf"),
	USER("/user/user.jsf"),
	TRIAL("/trial/trial.jsf"),
	PROBAND("/proband/proband.jsf"),
	INPUT_FIELD("/inputfield/inputField.jsf"),
	MASS_MAIL("/massmail/massMail.jsf"),
	LOGIN("/login.jsf"),
	PORTAL("/portal.jsf"),
	ERROR("/error.jsf"), UNSUBSCRIBE("/unsubscribe.jsf"),
	INVENTORY_START(Settings.getString(SettingCodes.INVENTORY_START_URL, Bundle.SETTINGS, DefaultSettings.INVENTORY_START_URL)), // "/inventory/inventorySearch.jsf"),
	STAFF_START(Settings.getString(SettingCodes.STAFF_START_URL, Bundle.SETTINGS, DefaultSettings.STAFF_START_URL)), // "/staff/staffSearch.jsf"),
	COURSE_START(Settings.getString(SettingCodes.COURSE_START_URL, Bundle.SETTINGS, DefaultSettings.COURSE_START_URL)), // "/course/courseSearch.jsf"),
	USER_START(Settings.getString(SettingCodes.USER_START_URL, Bundle.SETTINGS, DefaultSettings.USER_START_URL)), // "/user/userSearch.jsf"),
	TRIAL_START(Settings.getString(SettingCodes.TRIAL_START_URL, Bundle.SETTINGS, DefaultSettings.TRIAL_START_URL)), // "/trial/trialSearch.jsf"),
	PROBAND_START(Settings.getString(SettingCodes.PROBAND_START_URL, Bundle.SETTINGS, DefaultSettings.PROBAND_START_URL)), // "/proband/probandSearch.jsf"),
	INPUT_FIELD_START(Settings.getString(SettingCodes.INPUT_FIELD_START_URL, Bundle.SETTINGS, DefaultSettings.INPUT_FIELD_START_URL)), // "/inputfield/inputFieldSearch.jsf"),
	MASS_MAIL_START(Settings.getString(SettingCodes.MASS_MAIL_START_URL, Bundle.SETTINGS, DefaultSettings.MASS_MAIL_START_URL)), // "/inputfield/inputFieldSearch.jsf"),
	INVENTORY_SEARCH("/inventory/inventorySearch.jsf"),
	STAFF_SEARCH("/staff/staffSearch.jsf"),
	COURSE_SEARCH("/course/courseSearch.jsf"),
	USER_SEARCH("/user/userSearch.jsf"),
	TRIAL_SEARCH("/trial/trialSearch.jsf"),
	PROBAND_SEARCH("/proband/probandSearch.jsf"),
	INPUT_FIELD_SEARCH("/inputfield/inputFieldSearch.jsf"),
	MASS_MAIL_SEARCH("/massmail/massMailSearch.jsf"),
	CHANGE_PASSWORD("/user/changePassword.jsf"),
	ADMIN_UPCOMING_COURSE_OVERVIEW("/course/adminUpcomingCourseOverview.jsf"),
	EXPIRING_COURSE_OVERVIEW("/course/expiringCourseOverview.jsf"),
	ADMIN_EXPIRING_PARTICIPATION_OVERVIEW("/course/adminExpiringParticipationOverview.jsf"),
	INVENTORY_STATUS_OVERVIEW("/inventory/inventoryStatusOverview.jsf"),
	INVENTORY_MAINTENANCE_OVERVIEW("/inventory/inventoryMaintenanceOverview.jsf"),
	INVENTORY_BOOKING_SCHEDULE("/inventory/inventoryBookingSchedule.jsf"),
	INVENTORY_BOOKING_SUMMARY_OVERVIEW("/inventory/bookingSummaryOverview.jsf"),
	PROBAND_STATUS_OVERVIEW("/proband/probandStatusOverview.jsf"),
	AUTO_DELETION_PROBAND_OVERVIEW("/proband/autoDeletionProbandOverview.jsf"),
	STAFF_STATUS_OVERVIEW("/staff/staffStatusOverview.jsf"),
	STAFF_SHIFT_SUMMARY_OVERVIEW("/staff/staffShiftSummaryOverview.jsf"),
	UPCOMING_COURSE_OVERVIEW("/staff/upcomingCourseOverview.jsf"),
	EXPIRING_PARTICIPATION_OVERVIEW("/staff/expiringParticipationOverview.jsf"),
	TIMELINE_EVENT_OVERVIEW("/trial/timelineEventOverview.jsf"),
	TRIAL_TIMELINE("/trial/trialTimeline.jsf"),
	TRIAL_SHIFT_SUMMARY_OVERVIEW("/trial/trialShiftSummaryOverview.jsf"),
	RECIPIENT_OVERVIEW("/massmail/recipientOverview.jsf"),
	MONEY_TRANSFER_OVERVIEW("/trial/moneyTransferOverview.jsf"),
	DUTY_ROSTER_SCHEDULE("/trial/dutyRosterSchedule.jsf");

	private static Urls fromValue(String viewId) {
		if (viewId != null) {
			String viewPrefix;
			try {
				viewPrefix = viewId.substring(0, viewId.lastIndexOf('.')) + ".";
			} catch (IndexOutOfBoundsException e) {
				viewPrefix = viewId;
			}
			for (Urls url : Urls.values()) {
				if (url.viewId != null && url.viewId.startsWith(viewPrefix)) {
					return url;
				}
			}
		}
		return null;
	}

	public static DBModule getCurrentSearchModule() {
		switch (Urls.fromValue(WebUtil.getViewId())) {
			case INVENTORY_START:
			case INVENTORY_PICKER:
			case INVENTORY_SEARCH:
				return DBModule.INVENTORY_DB;
			case STAFF_START:
			case STAFF_PICKER:
			case STAFF_SEARCH:
				return DBModule.STAFF_DB;
			case COURSE_START:
			case COURSE_PICKER:
			case COURSE_SEARCH:
				return DBModule.COURSE_DB;
			case TRIAL_START:
			case TRIAL_PICKER:
			case TRIAL_SEARCH:
				return DBModule.TRIAL_DB;
			case PROBAND_START:
			case PROBAND_PICKER:
			case PROBAND_SEARCH:
				return DBModule.PROBAND_DB;
			case USER_START:
			case USER_PICKER:
			case USER_SEARCH:
				return DBModule.USER_DB;
			case INPUT_FIELD_START:
			case INPUT_FIELD_PICKER:
			case INPUT_FIELD_SEARCH:
				return DBModule.INPUT_FIELD_DB;
			case MASS_MAIL_START:
			case MASS_MAIL_PICKER:
			case MASS_MAIL_SEARCH:
				return DBModule.MASS_MAIL_DB;
			default:
				return null;
		}
	}

	public static String getHomeViewName(HttpServletRequest request) {
		// String url = request.getRequestURL().toString();
		StringBuffer url = request.getRequestURL();
		if (!CommonUtil.isEmptyString(request.getQueryString())) {
			url.append('?').append(request.getQueryString());
		}
		int lastSlash = url.lastIndexOf("/");
		int queryBegin = url.indexOf("?");
		String name;
		if (lastSlash >= 0) {
			if (queryBegin >= lastSlash) {
				name = url.substring(lastSlash + 1, queryBegin);
			} else {
				name = url.substring(lastSlash + 1);
			}
		} else {
			if (queryBegin >= 0) {
				name = url.substring(0, queryBegin);
			} else {
				name = url.toString();
			}
		}
		int extBegin = name.indexOf(".");
		if (extBegin >= 0) {
			name = name.substring(0, extBegin);
		}
		return name.toLowerCase();
	}

	public static String viewIdToUrl(HttpServletRequest request, String viewId) {
		StringBuilder sb = new StringBuilder(request.getContextPath());
		sb.append(viewId);
		return sb.toString();
	}

	private final String viewId;

	private Urls(final String viewId) {
		this.viewId = viewId;
	}

	@Override
	public String toString() {
		return viewId;
	}

	public String toString(HttpServletRequest request) {
		return viewIdToUrl(request, viewId);
	}

	public String value() {
		return viewId;
	}
}
