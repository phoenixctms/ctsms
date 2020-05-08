package org.phoenixctms.ctsms.web.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.phoenixctms.ctsms.js.JsUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CommonUtil.EllipsisPlacement;
import org.phoenixctms.ctsms.util.JavaScriptCompressor;
import org.phoenixctms.ctsms.vo.CourseOutVO;
import org.phoenixctms.ctsms.vo.CourseParticipationStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.DutyRosterTurnOutVO;
import org.phoenixctms.ctsms.vo.ECRFFieldOutVO;
import org.phoenixctms.ctsms.vo.ECRFFieldStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.ECRFFieldValueOutVO;
import org.phoenixctms.ctsms.vo.ECRFOutVO;
import org.phoenixctms.ctsms.vo.ECRFStatusEntryVO;
import org.phoenixctms.ctsms.vo.InputFieldOutVO;
import org.phoenixctms.ctsms.vo.InquiryOutVO;
import org.phoenixctms.ctsms.vo.InventoryBookingOutVO;
import org.phoenixctms.ctsms.vo.JournalEntryOutVO;
import org.phoenixctms.ctsms.vo.MaintenanceScheduleItemOutVO;
import org.phoenixctms.ctsms.vo.MassMailOutVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagOutVO;
import org.phoenixctms.ctsms.vo.ProbandListStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.ProbandStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.TimelineEventOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.vo.UserOutVO;
import org.phoenixctms.ctsms.vo.VisitOutVO;
import org.phoenixctms.ctsms.web.adapt.EcrfFieldValueOutVOStringAdapter;
import org.phoenixctms.ctsms.web.util.DateUtil;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.GetParamNames;
import org.phoenixctms.ctsms.web.util.JSValues;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.Urls;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.context.RequestContext;

@ManagedBean(eager = true)
@ApplicationScoped
public class ApplicationScopeBean {

	private static final Map<Long, UserOutVO> activeUsers = new LinkedHashMap<Long, UserOutVO>();
	private final static Pattern ALLOWED_LABEL_EXPRESSIONS_PATTERN_REGEXP = Pattern.compile("^[a-z0-9_]+\\.[a-z0-9_]+$");

	public static String evalLabelEl(String labelEl) {
		if (ALLOWED_LABEL_EXPRESSIONS_PATTERN_REGEXP.matcher(labelEl).find()) {
			FacesContext context = FacesContext.getCurrentInstance();
			ELContext elContext = context.getELContext();
			ValueExpression ve = context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{" + labelEl + "}", String.class);
			return (String) ve.getValue(elContext);
		}
		return labelEl;
	}

	public static final void registerActiveUser(UserOutVO user) {
		synchronized (ApplicationScopeBean.class) {
			if (user != null) {
				activeUsers.put(user.getId(), user);
			}
		}
	}

	public static final void unregisterActiveUser(UserOutVO user) {
		synchronized (ApplicationScopeBean.class) {
			if (user != null) {
				activeUsers.remove(user.getId());
			}
		}
	}

	public ApplicationScopeBean() {
	}

	public String byteCountToString(long size) {
		return CommonUtil.humanReadableByteCount(size);
	}

	public String clipStringLeading(String string) {
		return CommonUtil.clipString(string, Settings.getInt(SettingCodes.DEFAULT_CLIP_MAX_LENGTH, Bundle.SETTINGS, DefaultSettings.DEFAULT_CLIP_MAX_LENGTH),
				CommonUtil.DEFAULT_ELLIPSIS, EllipsisPlacement.LEADING);
	}

	public String clipStringLeading(String string, int length) {
		return CommonUtil.clipString(string, length, CommonUtil.DEFAULT_ELLIPSIS, EllipsisPlacement.LEADING);
	}

	public String clipStringMid(String string) {
		return CommonUtil.clipString(string, Settings.getInt(SettingCodes.DEFAULT_CLIP_MAX_LENGTH, Bundle.SETTINGS, DefaultSettings.DEFAULT_CLIP_MAX_LENGTH),
				CommonUtil.DEFAULT_ELLIPSIS, EllipsisPlacement.MID);
	}

	public String clipStringMid(String string, int length) {
		return CommonUtil.clipString(string, length, CommonUtil.DEFAULT_ELLIPSIS, EllipsisPlacement.MID);
	}

	public String clipStringPicker(String string) {
		return WebUtil.clipStringPicker(string);
	}

	public String clipStringTrailing(String string) {
		return CommonUtil.clipString(string, Settings.getInt(SettingCodes.COMMENT_CLIP_MAX_LENGTH, Bundle.SETTINGS, DefaultSettings.COMMENT_CLIP_MAX_LENGTH),
				CommonUtil.DEFAULT_ELLIPSIS, EllipsisPlacement.TRAILING);
	}

	public String clipStringTrailing(String string, int length) {
		return CommonUtil.clipString(string, length, CommonUtil.DEFAULT_ELLIPSIS, EllipsisPlacement.TRAILING);
	}

	public String compressJS(String script) {
		return JavaScriptCompressor.compress(script);
	}

	public String courseParticipationToColor(CourseParticipationStatusEntryOutVO statusEntry) {
		if (statusEntry != null) {
			return WebUtil.colorToStyleClass(statusEntry.getStatus().getColor());
		}
		return "";
	}

	public String courseValidityPeriodToString(CourseOutVO course) {
		if (course != null && course.isExpires()) {
			return WebUtil.variablePeriodToString(course.getValidityPeriod(), course.getValidityPeriodDays());
		}
		return "";
	}

	public String decodeBase64(String base64String) {
		return JsUtil.decodeBase64(base64String);
	}

	public String ecrfFieldStatusEntryToColor(ECRFFieldStatusEntryOutVO status) {
		if (status != null) {
			return WebUtil.colorToStyleClass(status.getStatus().getColor());
		}
		return "";
	}

	public String ecrfStatusEntryToColor(ECRFStatusEntryVO status) {
		if (status != null) {
			return WebUtil.colorToStyleClass(status.getStatus().getColor());
		}
		return "";
	}

	public String encodeBase64(String string) {
		return JsUtil.encodeBase64(string, false);
	}

	public String escapeHtml(String string) { // workaround for pf datagrid
		return WebUtil.escapeHtml(string);
	}

	public String getActiveUsersString(int length) {
		synchronized (ApplicationScopeBean.class) {
			StringBuilder sb = new StringBuilder();
			int activeUsersCount;
			activeUsersCount = activeUsers.size();
			HashMap<Long, Integer> identityCountMap = new HashMap<Long, Integer>(activeUsersCount);
			Iterator<UserOutVO> activeUsersIt = activeUsers.values().iterator();
			activeUsersIt = activeUsers.values().iterator();
			while (activeUsersIt.hasNext()) {
				UserOutVO user = activeUsersIt.next();
				StaffOutVO identity = user.getIdentity();
				if (identity != null) {
					int count;
					if (identityCountMap.containsKey(identity.getId())) {
						count = identityCountMap.get(identity.getId());
					} else {
						count = 0;
					}
					identityCountMap.put(identity.getId(), count + 1);
				}
			}
			activeUsersIt = activeUsers.values().iterator();
			while (activeUsersIt.hasNext()) {
				UserOutVO user = activeUsersIt.next();
				if (sb.length() > 0) {
					sb.append(", ");
				}
				Integer count;
				StaffOutVO identity = user.getIdentity();
				if (identity != null && (count = identityCountMap.get(identity.getId())) != null && count > 1) {
					sb.append(WebUtil.getIdentityUserString(user));
				} else {
					sb.append(WebUtil.getIdentityString(user));
				}
			}
			return Messages.getMessage(MessageCodes.ACTIVE_USERS, activeUsersCount,
					CommonUtil.clipString(sb.toString(), length, CommonUtil.DEFAULT_ELLIPSIS, EllipsisPlacement.TRAILING));
		}
	}

	public String getApplicationInstance() {
		return Messages.getMessage(MessageCodes.APPLICATION_INSTANCE, WebUtil.getInstanceName());
	}

	public String getAuditTrailFieldValueString(ECRFFieldValueOutVO ecrfFieldValue) {
		return (new EcrfFieldValueOutVOStringAdapter(Settings.getInt(SettingCodes.AUDIT_TRAIL_FIELD_VALUE_TEXT_CLIP_MAX_LENGTH, Bundle.SETTINGS,
				DefaultSettings.AUDIT_TRAIL_FIELD_VALUE_TEXT_CLIP_MAX_LENGTH))).toString(ecrfFieldValue);
	}

	public String getBookingDurationString(Long duration) {
		if (duration != null) {
			return DateUtil.getBookingDurationString(duration);
		}
		return "";
	}

	public String getCalendarYearRange() {
		return Settings.getString(SettingCodes.CALENDAR_YEAR_RANGE, Bundle.SETTINGS, DefaultSettings.CALENDAR_YEAR_RANGE);
	}

	public String getContactEmail() {
		return Settings.getContactEmail();
	}

	public String getCurrencySymbol() {
		return WebUtil.getCurrencySymbol();
	}

	public TimeZone getDefaultTimeZone() {
		return WebUtil.getDefaultTimeZone();
	}

	public String getDurationString(long duration) {
		return DateUtil.getDurationString(duration);
	}

	public String getDutyRosterTurnDurationString(DutyRosterTurnOutVO dutyRosterTurn) {
		if (dutyRosterTurn != null) {
			return DateUtil.getShiftDurationString(dutyRosterTurn.getStart(), dutyRosterTurn.getStop());
		}
		return "";
	}

	public String getDutySelfAllocationLockedString(TrialOutVO trial) {
		if (trial != null && trial.getDutySelfAllocationLocked()) {
			if (trial.getDutySelfAllocationLockedUntil() != null && trial.getDutySelfAllocationLockedFrom() != null) {
				return Messages
						.getMessage(MessageCodes.DUTY_SELF_ALLOCATION_LOCKED_UNTIL_FROM_LABEL, DateUtil.getDateTimeFormat().format(trial.getDutySelfAllocationLockedUntil()),
								DateUtil.getDateTimeFormat().format(trial.getDutySelfAllocationLockedFrom()));
			} else if (trial.getDutySelfAllocationLockedUntil() != null) {
				return Messages.getMessage(MessageCodes.DUTY_SELF_ALLOCATION_LOCKED_UNTIL_LABEL, DateUtil.getDateTimeFormat().format(trial.getDutySelfAllocationLockedUntil()));
			} else if (trial.getDutySelfAllocationLockedFrom() != null) {
				return Messages.getMessage(MessageCodes.DUTY_SELF_ALLOCATION_LOCKED_FROM_LABEL, DateUtil.getDateTimeFormat().format(trial.getDutySelfAllocationLockedFrom()));
			}
		}
		return "";
	}

	public Long getEcrfFieldCount(ECRFOutVO ecrf) {
		if (ecrf != null) {
			return WebUtil.getEcrfFieldCount(null, ecrf.getId());
		}
		return null;
	}

	public Long getEcrfFieldValueCount(ECRFFieldOutVO ecrfField) {
		return WebUtil.getEcrfFieldValueCount(ecrfField, true);
	}

	public Integer getEcrfSectionHashCode(Long ecrfFieldStatusEntryId) {
		return WebUtil.getEcrfSectionHashCode(ecrfFieldStatusEntryId);
	}

	public String getFileNodeType() {
		return WebUtil.FILE_NODE_TYPE;
	}

	public String getFolderNodeType() {
		return WebUtil.FOLDER_NODE_TYPE;
	}

	public String getGoogleApiUrl() {
		return Settings.getString(SettingCodes.GOOGLE_API_URL, Bundle.SETTINGS, DefaultSettings.GOOGLE_API_URL);
	}

	public String getHomeWindowName(String homeWindowNameJsVarname) {
		return String.format(JSValues.getWindowNamePattern(homeWindowNameJsVarname),
				Urls.getHomeViewName((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()), WebUtil.getWindowNameUniqueToken());
	}

	public String getIbanMask() {
		return Settings.getString(SettingCodes.IBAN_MASK, Bundle.SETTINGS, DefaultSettings.IBAN_MASK);
	}

	public String getIdentityString(UserOutVO user) {
		return WebUtil.getIdentityString(user);
	}

	public String getInputFieldIcon(InputFieldOutVO inputField) {
		return WebUtil.getInputFieldIcon(inputField);
	}

	public Long getInquiryValueCount(InquiryOutVO inquiry) {
		return WebUtil.getInquiryValueCount(inquiry);
	}

	public int getIntegerMaxValue() {
		return Integer.MAX_VALUE;
	}

	public String getInventoryBookingDurationString(InventoryBookingOutVO booking) {
		if (booking != null) {
			return DateUtil.getBookingDurationString(booking.getStart(), booking.getStop());
		}
		return "";
	}

	// http://stackoverflow.com/questions/6001743/how-to-show-hashmap-values-in-jsf
	public Map<String, String> getJsValues() {
		JSValues[] values = JSValues.values();
		Map<String, String> result = new HashMap<String, String>(values.length);
		for (int i = 0; i < values.length; i++) {
			result.put(values[i].name(), values[i].toQuotedEscapedString());
		}
		return result;
	}

	public String getLeafNodeType() {
		return WebUtil.LEAF_NODE_TYPE;
	}

	public int getListSize(List<?> list) {
		if (list != null) {
			return list.size();
		}
		return 0;
	}

	public String getLoginPromptTitle() {
		return Messages.getMessage(MessageCodes.LOGIN_PROMPT_TITLE, WebUtil.getInstanceName());
	}

	public String getMetaDescription() {
		return MessageFormat.format(Settings.getString(SettingCodes.META_DESCRIPTION, Bundle.SETTINGS, DefaultSettings.META_DESCRIPTION), WebUtil.getInstanceName());
	}

	public String getNoCoursePickedMessage() {
		return WebUtil.getNoCoursePickedMessage();
	}

	public String getNoInputFieldPickedMessage() {
		return WebUtil.getNoInputFieldPickedMessage();
	}

	public String getNoInventoryPickedMessage() {
		return WebUtil.getNoInventoryPickedMessage();
	}

	public String getNoMassMailPickedMessage() {
		return WebUtil.getNoMassMailPickedMessage();
	}

	public String getNoProbandPickedMessage() {
		return WebUtil.getNoProbandPickedMessage();
	}

	public String getNoSelectionValue() {
		return CommonUtil.NO_SELECTION_VALUE;
	}

	public String getNoStaffPickedMessage() {
		return WebUtil.getNoStaffPickedMessage();
	}

	public String getNoTrialPickedMessage() {
		return WebUtil.getNoTrialPickedMessage();
	}

	public String getNoUserPickedMessage() {
		return WebUtil.getNoUserPickedMessage();
	}

	public String getNumberOfCourseParticipantsString(CourseOutVO course) {
		StringBuilder sb = new StringBuilder();
		if (course != null) {
			Long courseParticipationStatusEntryCount = WebUtil.getCourseParticipationStatusEntryCount(null, course.getId());
			sb.append(courseParticipationStatusEntryCount != null ? courseParticipationStatusEntryCount : "?");
			if (course.getSelfRegistration()) {
				sb.append("/");
				sb.append(course.getMaxNumberOfParticipants() != null ? course.getMaxNumberOfParticipants() : "-");
			}
		}
		return sb.toString();
	}

	public String getParentNodeType() {
		return WebUtil.PARENT_NODE_TYPE;
	}

	public String getPercentageString(Float value) {
		if (value != null) {
			return Messages.getMessage(MessageCodes.PERCENTAGE_LABEL, 100.0f * value);
		}
		return "";
	}

	public Long getProbandListEntryTagValueCount(ProbandListEntryTagOutVO probandListEntryTag) {
		return WebUtil.getProbandListEntryTagValueCount(probandListEntryTag);
	}

	public String getProbandStatusEntryStartStopString(ProbandStatusEntryOutVO statusEntry) {
		if (statusEntry != null) {
			return DateUtil.getDateTimeStartStopString(statusEntry.getStart(), statusEntry.getStop());
		}
		return "";
	}

	public String getRemoteHost() {
		return WebUtil.getRemoteHost();
	}

	public String getScheduleRightHeaderTemplate() {
		return Settings.getString(SettingCodes.SCHEDULE_RIGHT_HEADER_TEMPLATE, Bundle.SETTINGS, DefaultSettings.SCHEDULE_RIGHT_HEADER_TEMPLATE);
	}

	public String getScheduleTimePattern() {
		return Settings.getString(SettingCodes.SCHEDULE_TIME_PATTERN, Bundle.SETTINGS, DefaultSettings.SCHEDULE_TIME_PATTERN);
	}

	public String getShiftDurationString(long duration) {
		return DateUtil.getShiftDurationString(duration);
	}

	public String getStatusEntryDurationString(long duration) {
		return DateUtil.getStatusEntryDurationString(duration);
	}

	public String getStringDigest(String string) throws NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		if (string != null) {
			messageDigest.update(string.getBytes());
		}
		return CommonUtil.getHex(messageDigest.digest());
	}

	public int getTimePickerIntervalMinutes() {
		return Settings.getInt(SettingCodes.TIME_PICKER_INTERVAL_MINUTES, Bundle.SETTINGS, DefaultSettings.TIME_PICKER_INTERVAL_MINUTES);
	}

	public String getTimeSeparator() {
		return CommonUtil.TIME_SEPARATOR;
	}

	public Map<String, String> getUrls() {
		Urls[] values = Urls.values();
		Map<String, String> result = new HashMap<String, String>(values.length);
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		for (int i = 0; i < values.length; i++) {
			result.put(values[i].name(), values[i].toString(request));
		}
		return result;
	}

	public String getUserIdentityString(UserOutVO user) {
		return WebUtil.getUserIdentityString(user);
	}

	public Map<String, String> getViewIds() {
		Urls[] values = Urls.values();
		Map<String, String> result = new HashMap<String, String>(values.length);
		for (int i = 0; i < values.length; i++) {
			result.put(values[i].name(), values[i].value());
		}
		return result;
	}

	@PostConstruct
	private void init() {
		initSets();
	}

	private void initSets() {
		Locale.setDefault(WebUtil.getDefaultLocale());
		TimeZone.setDefault(WebUtil.getDefaultTimeZone());
	}

	public boolean isDateTimeUserTimeZone() {
		return Settings.getBoolean(SettingCodes.DATE_TIME_USER_TIME_ZONE, Bundle.SETTINGS, DefaultSettings.DATE_TIME_USER_TIME_ZONE);
	}

	public boolean isDateUserTimeZone() {
		return Settings.getBoolean(SettingCodes.DATE_USER_TIME_ZONE, Bundle.SETTINGS, DefaultSettings.DATE_USER_TIME_ZONE);
	}

	public boolean isEnableGeolocationServices() {
		return Settings.getBoolean(SettingCodes.ENABLE_GEOLOCATION_SERVICES, Bundle.SETTINGS, DefaultSettings.ENABLE_GEOLOCATION_SERVICES);
	}

	public boolean isEnableTooltips() {
		return Settings.getBoolean(SettingCodes.ENABLE_TOOLTIPS, Bundle.SETTINGS, DefaultSettings.ENABLE_TOOLTIPS);
	}

	public boolean isHighlightTextInput() {
		return Settings.getBoolean(SettingCodes.HIGHLIGHT_TEXT_INPUT, Bundle.SETTINGS, DefaultSettings.HIGHLIGHT_TEXT_INPUT);
	}

	public boolean isHtmlAuditTrailChangeComments() {
		return CommonUtil.HTML_AUDIT_TRAIL_CHANGE_COMMENTS;
	}

	public boolean isHtmlSystemMessageComments() {
		return CommonUtil.HTML_SYSTEM_MESSAGES_COMMENTS;
	}

	public boolean isShowProbandRating() {
		return Settings.getBoolean(SettingCodes.SHOW_PROBAND_RATING, Bundle.SETTINGS, DefaultSettings.SHOW_PROBAND_RATING);
	}

	public boolean isTimeUserTimeZone() {
		return Settings.getBoolean(SettingCodes.TIME_USER_TIME_ZONE, Bundle.SETTINGS, DefaultSettings.TIME_USER_TIME_ZONE);
	}

	public boolean isTrustedHost() {
		return WebUtil.isTrustedHost();
	}

	public String journalEntryToColor(JournalEntryOutVO journalEntry) {
		return ((journalEntry != null && !journalEntry.isSystemMessage()) ? WebUtil.colorToStyleClass(journalEntry.getCategory().getColor()) : "");
	}

	public String jsValue(String name) {
		return JSValues.valueOf(name).toString();
	}

	public void keepAliveCallback() {
		FacesContext context = FacesContext.getCurrentInstance();
		Map map = context.getExternalContext().getRequestParameterMap();
		String ajaxKeepAliveJsCallback = (String) map.get(JSValues.AJAX_KEEP_ALIVE_JS_CALLBACK.toString());
		String ajaxKeepAliveJsCallbackArgs = (String) map.get(JSValues.AJAX_KEEP_ALIVE_JS_CALLBACK_ARGS.toString());
		RequestContext requestContext = RequestContext.getCurrentInstance();
		if (requestContext != null) {
			requestContext.addCallbackParam(JSValues.AJAX_OPERATION_SUCCESS.toString(), true);
			requestContext.addCallbackParam(JSValues.AJAX_KEEP_ALIVE_JS_CALLBACK.toString(), ajaxKeepAliveJsCallback);
			if (ajaxKeepAliveJsCallbackArgs != null) {
				requestContext.addCallbackParam(JSValues.AJAX_KEEP_ALIVE_JS_CALLBACK_ARGS.toString(), ajaxKeepAliveJsCallbackArgs);
			}
		}
	}

	public String localeToDisplayString(String locale) {
		return CommonUtil.localeToDisplayString(CommonUtil.localeFromString(locale), WebUtil.getLocale());
	}

	public String maintenanceScheduleItemRecurringPeriodToString(MaintenanceScheduleItemOutVO maintenanceItem) {
		if (maintenanceItem != null && maintenanceItem.isRecurring()) {
			return WebUtil.variablePeriodToString(maintenanceItem.getRecurrencePeriod(), maintenanceItem.getRecurrencePeriodDays());
		}
		return "";
	}

	public String maintenanceScheduleItemReminderPeriodToString(MaintenanceScheduleItemOutVO maintenanceItem) {
		if (maintenanceItem != null) {
			return WebUtil.variablePeriodToString(maintenanceItem.getReminderPeriod(), maintenanceItem.getReminderPeriodDays());
		}
		return "";
	}

	public List<Map.Entry<?, ?>> mapToList(Map<?, ?> map) {
		return (map != null ? new ArrayList<Map.Entry<?, ?>>(map.entrySet()) : null);
	}

	public String massMailToColor(MassMailOutVO massMail) {
		return (massMail != null ? WebUtil.colorToStyleClass(massMail.getStatus().getColor()) : "");
	}

	public String parameterName(String name) {
		return GetParamNames.valueOf(name).toString();
	}

	public String probandListStatusToColor(ProbandListStatusEntryOutVO probandListStatusEntry) {
		if (probandListStatusEntry != null) {
			return WebUtil.colorToStyleClass(probandListStatusEntry.getStatus().getColor());
		}
		return "";
	}

	public String quoteJsString(String string) {
		return WebUtil.quoteJSString(string, true);
	}

	public String quoteJSString(String value, boolean quotes) {
		return WebUtil.quoteJSString(value, true);
	}

	public String repeatString(String string, String separator, int n) {
		StringBuilder result = new StringBuilder();
		boolean emptySeparator = CommonUtil.isEmptyString(separator);
		for (int i = 0; i < n; i++) {
			if (i > 0 && !emptySeparator) {
				result.append(separator);
			}
			result.append(string);
		}
		return result.toString();
	}

	public boolean resourceExists(String fileName) {
		return WebUtil.resourceExists(fileName);
	}

	public String staffToColor(StaffOutVO staff) {
		return (staff != null ? WebUtil.colorToStyleClass(staff.getCategory().getColor()) : "");
	}

	public String timelineEventReminderPeriodToString(TimelineEventOutVO timelineEvent) {
		if (timelineEvent != null) {
			return WebUtil.variablePeriodToString(timelineEvent.getReminderPeriod(), timelineEvent.getReminderPeriodDays());
		}
		return "";
	}

	public String timelineEventToColor(TimelineEventOutVO timelineEvent) {
		if (timelineEvent != null) {
			return WebUtil.colorToStyleClass(timelineEvent.getType().getColor());
		}
		return "";
	}

	public String timelineEventToStartStopString(TimelineEventOutVO timelineEvent) {
		if (timelineEvent != null) {
			Date start = timelineEvent.getStart();
			Date stop = timelineEvent.getStop();
			if (start != null) {
				if (stop != null) {
					return Messages.getMessage(MessageCodes.TIMELINE_TRIAL_EVENT_START_STOP_LABEL, DateUtil.getDateFormat().format(start), DateUtil.getDateFormat().format(stop));
				} else {
					return Messages.getMessage(MessageCodes.TIMELINE_TRIAL_EVENT_START_LABEL, DateUtil.getDateFormat().format(start));
				}
			}
		}
		return "";
	}

	public String url(String name) {
		return Urls.valueOf(name).toString((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest());
	}

	public String viewId(String name) {
		return Urls.valueOf(name).value();
	}

	public String visitToColor(VisitOutVO visit) {
		if (visit != null) {
			return WebUtil.colorToStyleClass(visit.getType().getColor());
		}
		return "";
	}

	private final static String TIMELINE_EVENT_SEPARATOR = ", ";

	public String timelineEventsToString(Collection<TimelineEventOutVO> events) {
		if (events != null) {
			Iterator<TimelineEventOutVO> it = events.iterator();
			StringBuilder sb = new StringBuilder();
			while (it.hasNext()) {
				if (sb.length() > 0) {
					sb.append(TIMELINE_EVENT_SEPARATOR);
				}
				sb.append(it.next().getTitle());
			}
			return sb.toString();
		}
		return null;
	}
}
