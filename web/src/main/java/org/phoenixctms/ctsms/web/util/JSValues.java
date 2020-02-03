package org.phoenixctms.ctsms.web.util;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.phoenixctms.ctsms.js.JsUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.primefaces.extensions.converter.LocaleConverter;

public enum JSValues {
	ID_SEPARATOR_STRING(WebUtil.ID_SEPARATOR_STRING),
	AJAX_CANCEL("cancel"),
	AJAX_MAINTENANCE_TYPE_TITLE_PRESET_BASE64("maintenanceType"),
	AJAX_HYPERLINK_CATEGORY_TITLE_PRESET_BASE64("hyperlinkCategoryTitlePresetBase64"),
	AJAX_JOURNAL_CATEGORY_TITLE_PRESET_BASE64("journalCategoryTitlePresetBase64"),
	AJAX_CONTACT_DETAIL_TYPE_EMAIL("contactDetailTypeEmail"),
	AJAX_CONTACT_DETAIL_TYPE_PHONE("contactDetailTypePhone"),
	AJAX_CONTACT_DETAIL_NA("contactDetailNa"),
	AJAX_CONTACT_DETAIL_TYPE_NOTIFY_PRESET("contactDetailTypeNotifyPreset"),
	AJAX_ADDRESS_TYPE_DELIVER_PRESET("addressTypeDeliverPreset"),
	AJAX_CV_SECTION_SHOW_CV_PRESET("cvSectionShowCvPreset"),
	AJAX_CV_SECTION_TITLE_PRESET_BASE64("cvSectionTitlePresetBase64"),
	AJAX_TIMELINE_EVENT_TYPE_IMPORTANCE_PRESET("timelineEventTypeImportancePreset"),
	AJAX_TIMELINE_EVENT_TYPE_SHOW_PRESET("timelineEventTypeShowPreset"),
	AJAX_TIMELINE_EVENT_TYPE_NOTIFY_PRESET("timelineEventTypeNotifyPreset"),
	AJAX_TIMELINE_EVENT_TYPE_TITLE_PRESET_BASE64("timelineEventTypeTitlePresetBase64"),
	AJAX_TIMELINE_EVENT_TYPE_TITLE_PRESET_FIXED("timelineEventTypeTitlePresetFixed"),
	INVENTORY_ID(GetParamNames.INVENTORY_ID
			.toString()),
	STAFF_ID(GetParamNames.STAFF_ID.toString()),
	COURSE_ID(GetParamNames.COURSE_ID.toString()),
	USER_ID(GetParamNames.USER_ID
			.toString()),
	TRIAL_ID(GetParamNames.TRIAL_ID.toString()),
	PROBAND_ID(GetParamNames.PROBAND_ID.toString()),
	INPUT_FIELD_ID(GetParamNames.INPUT_FIELD_ID.toString()),
	CRITERIA_ID(GetParamNames.CRITERIA_ID
			.toString()),
	MASS_MAIL_ID(GetParamNames.MASS_MAIL_ID
			.toString()),
	ECRF_FIELD_STATUS_ENTRY_ID(GetParamNames.ECRF_FIELD_STATUS_ENTRY_ID
			.toString()),
	SOURCE_INDEX(GetParamNames.SOURCE_INDEX
			.toString()),
	TARGET_INDEX(GetParamNames.TARGET_INDEX
			.toString()),
	NO_SELECTION_VALUE(CommonUtil.NO_SELECTION_VALUE),
	PICK_TARGET_FIELD(GetParamNames.PICK_TARGET_FIELD
			.toString()),
	PICK_TARGET_LABEL(GetParamNames.PICK_TARGET_LABEL
			.toString()),
	START(GetParamNames.START
			.toString()),
	STOP(GetParamNames.STOP
			.toString()),
	PICK_AJAX(GetParamNames.PICK_AJAX
			.toString()),
	PICK_AJAX_UPDATE(GetParamNames.PICK_AJAX_UPDATE
			.toString()),
	PICK_ADD_REMOTE_COMMAND(GetParamNames.PICK_ADD_REMOTE_COMMAND
			.toString()),
	PICK_ON_CLICK(GetParamNames.PICK_ON_CLICK
			.toString()),
	PICK_CURRENT_PAGE_IDS("PICK_CURRENT_PAGE_IDS"),
	AJAX_OPERATION_SUCCESS("operationSuccess"),
	AJAX_WINDOW_TITLE_BASE64("windowTitleBase64"),
	AJAX_WINDOW_NAME("windowName"),
	AJAX_PICKER("picker"),
	AJAX_ROOT_ENTITY_CREATED("rootEntityCreated"),
	AJAX_CRITERION_ROW_COLORS_BASE64("criterionRowColorsBase64"),
	AJAX_CRITERION_TIE_MAP_BASE64("criterionTieMapBase64"),
	AJAX_INTERMEDIATE_SETS_BASE64("intermediateSetsBase64"),
	AJAX_CRITERIA_RESULT_ITEM_LABEL_BASE64("criteriaResultItemLabelBase64"),
	LIST_INITIAL_POSITION(""),
	AJAX_DIALOG_TITLE_BASE64("dialogTitleBase64"),
	AJAX_PROBAND_LIST_TAB_TITLE_BASE64("probandListTabTitleBase64"),
	AJAX_INVENTORY_TAG_TAB_TITLE_BASE64("inventoryTagTabTitleBase64"),
	AJAX_INVENTORY_TAG_VALUE_COUNT("inventoryTagValueCount"),
	AJAX_INVENTORY_STATUS_TAB_TITLE_BASE64("inventoryStatusTabTitleBase64"),
	AJAX_INVENTORY_STATUS_ENTRY_COUNT("inventoryStatusEntryCount"),
	AJAX_INVENTORY_MAINTENANCE_TAB_TITLE_BASE64("inventoryMaintenanceTabTitleBase64"),
	AJAX_INVENTORY_MAINTENANCE_ITEM_COUNT("inventoryMaintenanceItemCount"),
	AJAX_INVENTORY_BOOKING_TAB_TITLE_BASE64("inventoryBookingTabTitleBase64"),
	AJAX_INVENTORY_BOOKING_COUNT("inventoryBookingItemCount"),
	AJAX_INVENTORY_HYPERLINK_TAB_TITLE_BASE64("inventoryHyperlinkTabTitleBase64"),
	AJAX_INVENTORY_HYPERLINK_COUNT("inventoryHyperlinkCount"),
	AJAX_INVENTORY_FILE_TAB_TITLE_BASE64("inventoryFileTabTitleBase64"),
	AJAX_INVENTORY_FILE_COUNT("inventoryFileCount"),
	AJAX_INVENTORY_JOURNAL_TAB_TITLE_BASE64("inventoryJournalTabTitleBase64"),
	AJAX_INVENTORY_JOURNAL_ENTRY_COUNT("inventoryJournalEntryCount"),
	AJAX_STAFF_TAG_TAB_TITLE_BASE64("staffTagTabTitleBase64"),
	AJAX_STAFF_TAG_VALUE_COUNT("staffTagValueCount"),
	AJAX_STAFF_IMAGE_TAB_TITLE_BASE64("staffImageTabTitleBase64"),
	AJAX_STAFF_IMAGE_COUNT("staffImageCount"),
	AJAX_STAFF_CONTACT_DETAIL_TAB_TITLE_BASE64("staffContactDetailTabTitleBase64"),
	AJAX_STAFF_CONTACT_DETAIL_VALUE_COUNT("staffContactDetailValueCount"),
	AJAX_STAFF_ADDRESS_TAB_TITLE_BASE64("staffAddressTabTitleBase64"),
	AJAX_STAFF_ADDRESS_COUNT("staffAddressCount"),
	AJAX_STAFF_STATUS_TAB_TITLE_BASE64("staffStatusTabTitleBase64"),
	AJAX_STAFF_STATUS_ENTRY_COUNT("staffStatusEntryCount"),
	AJAX_STAFF_DUTY_ROSTER_TURN_TAB_TITLE_BASE64("staffDutyRosterTurnTabTitleBase64"),
	AJAX_STAFF_DUTY_ROSTER_TURN_COUNT("staffDutyRosterTurnCount"),
	AJAX_CV_POSITION_TAB_TITLE_BASE64("cvPositionTabTitleBase64"),
	AJAX_CV_POSITION_COUNT("cvPositionCount"),
	AJAX_COURSE_PARTICIPATION_STATUS_TAB_TITLE_BASE64("courseParticipationStatusTabTitleBase64"),
	AJAX_COURSE_PARTICIPATION_STATUS_ENTRY_COUNT("courseParticipationStatusEntryCount"),
	AJAX_STAFF_HYPERLINK_TAB_TITLE_BASE64("staffHyperlinkTabTitleBase64"),
	AJAX_STAFF_HYPERLINK_COUNT("staffHyperlinkCount"),
	AJAX_STAFF_FILE_TAB_TITLE_BASE64("staffFileTabTitleBase64"),
	AJAX_STAFF_FILE_COUNT("staffFileCount"),
	AJAX_STAFF_JOURNAL_TAB_TITLE_BASE64("staffJournalTabTitleBase64"),
	AJAX_STAFF_JOURNAL_ENTRY_COUNT("staffJournalEntryCount"),
	AJAX_LECTURER_TAB_TITLE_BASE64("lecturerTabTitleBase64"),
	AJAX_LECTURER_COUNT("lecturerCount"),
	AJAX_COURSE_INVENTORY_BOOKING_TAB_TITLE_BASE64("courseInventoryBookingTabTitleBase64"),
	AJAX_COURSE_INVENTORY_BOOKING_COUNT("courseInventoryBookingItemCount"),
	AJAX_ADMIN_COURSE_PARTICIPATION_STATUS_TAB_TITLE_BASE64("adminCourseParticipationStatusTabTitleBase64"),
	AJAX_ADMIN_COURSE_PARTICIPATION_STATUS_ENTRY_COUNT("adminCourseParticipationStatusEntryCount"),
	AJAX_COURSE_HYPERLINK_TAB_TITLE_BASE64("courseHyperlinkTabTitleBase64"),
	AJAX_COURSE_HYPERLINK_COUNT("courseHyperlinkCount"),
	AJAX_COURSE_FILE_TAB_TITLE_BASE64("courseFileTabTitleBase64"),
	AJAX_COURSE_FILE_COUNT("courseFileCount"),
	AJAX_COURSE_JOURNAL_TAB_TITLE_BASE64("courseJournalTabTitleBase64"),
	AJAX_COURSE_JOURNAL_ENTRY_COUNT("courseJournalEntryCount"),
	AJAX_TRIAL_TAG_TAB_TITLE_BASE64("trialTagTabTitleBase64"),
	AJAX_TRIAL_TAG_VALUE_COUNT("trialTagValueCount"),
	AJAX_TEAM_MEMBER_TAB_TITLE_BASE64("teamMemberTabTitleBase64"),
	AJAX_TEAM_MEMBER_COUNT("teamMemberCount"),
	AJAX_TIMELINE_EVENT_TAB_TITLE_BASE64("timelineEventTabTitleBase64"),
	AJAX_TIMELINE_EVENT_COUNT("timelineEventCount"),
	AJAX_TRIAL_INVENTORY_BOOKING_TAB_TITLE_BASE64("trialInventoryBookingTabTitleBase64"),
	AJAX_TRIAL_INVENTORY_BOOKING_COUNT("trialInventoryBookingItemCount"),
	AJAX_VISIT_TAB_TITLE_BASE64("visitTabTitleBase64"),
	AJAX_VISIT_COUNT("visitCount"),
	AJAX_PROBAND_GROUP_TAB_TITLE_BASE64("probandGroupTabTitleBase64"),
	AJAX_PROBAND_GROUP_COUNT("probandGroupCount"),
	AJAX_VISIT_SCHEDULE_TAB_TITLE_BASE64("visitScheduleTabTitleBase64"),
	AJAX_VISIT_SCHEDULE_ITEM_COUNT("visitScheduleItemCount"),
	AJAX_TRIAL_DUTY_ROSTER_TURN_TAB_TITLE_BASE64("trialDutyRosterTurnTabTitleBase64"),
	AJAX_TRIAL_DUTY_ROSTER_TURN_COUNT("trialDutyRosterTurnCount"),
	AJAX_PROBAND_LIST_ENTRY_TAG_TAB_TITLE_BASE64("probandListEntryTagTabTitleBase64"),
	AJAX_PROBAND_LIST_ENTRY_TAG_COUNT("probandListEntryTagCount"),
	AJAX_STRATIFICATION_RANDOMIZATION_LIST_TAB_TITLE_BASE64("stratificationRandomzationListTabTitleBase64"),
	AJAX_STRATIFICATION_RANDOMIZATION_LIST_COUNT("stratificationRandomizationListCount"),
	AJAX_INQUIRY_TAB_TITLE_BASE64("inquiryTabTitleBase64"),
	AJAX_INQUIRY_COUNT("inquiryCount"),
	AJAX_ECRF_TAB_TITLE_BASE64("ecrfTabTitleBase64"),
	AJAX_ECRF_COUNT("ecrfCount"),
	AJAX_ECRF_FIELD_TAB_TITLE_BASE64("ecrfFieldTabTitleBase64"),
	AJAX_ECRF_FIELD_COUNT("ecrfFieldCount"),
	AJAX_PROBAND_LIST_ENTRY_TAB_TITLE_BASE64("probandListEntryTabTitleBase64"),
	AJAX_PROBAND_LIST_ENTRY_COUNT("probandListEntryCount"),
	AJAX_ECRF_FIELD_STATUS_TAB_TITLE_BASE64("ecrfFieldStatusTabTitleBase64"),
	AJAX_ECRF_FIELD_STATUS_COUNT("ecrfFieldStatusCount"),
	AJAX_ANNOTATION_ECRF_FIELD_STATUS_ENTRY_TAB_TITLE_BASE64("annotationEcrfFieldStatusEntryTabTitleBase64"),
	AJAX_ANNOTATION_ECRF_FIELD_STATUS_ENTRY_COUNT("annotationEcrfFieldStatusEntryCount"),
	AJAX_VALIDATION_ECRF_FIELD_STATUS_ENTRY_TAB_TITLE_BASE64("validationEcrfFieldStatusEntryTabTitleBase64"),
	AJAX_VALIDATION_ECRF_FIELD_STATUS_ENTRY_COUNT("validationEcrfFieldStatusEntryCount"),
	AJAX_QUERY_ECRF_FIELD_STATUS_ENTRY_TAB_TITLE_BASE64("queryEcrfFieldStatusEntryTabTitleBase64"),
	AJAX_QUERY_ECRF_FIELD_STATUS_ENTRY_COUNT("queryEcrfFieldStatusEntryCount"),
	AJAX_TRIAL_HYPERLINK_TAB_TITLE_BASE64("trialHyperlinkTabTitleBase64"),
	AJAX_TRIAL_HYPERLINK_COUNT("trialHyperlinkCount"),
	AJAX_TRIAL_FILE_TAB_TITLE_BASE64("trialFileTabTitleBase64"),
	AJAX_TRIAL_FILE_COUNT("trialFileCount"),
	AJAX_TRIAL_JOB_TAB_TITLE_BASE64("trialJobTabTitleBase64"),
	AJAX_TRIAL_JOB_COUNT("trialJobCount"),
	AJAX_TRIAL_JOURNAL_TAB_TITLE_BASE64("trialJournalTabTitleBase64"),
	AJAX_TRIAL_JOURNAL_ENTRY_COUNT("trialJournalEntryCount"),
	AJAX_PROBAND_IMAGE_TAB_TITLE_BASE64("probandImageTabTitleBase64"),
	AJAX_PROBAND_IMAGE_COUNT("probandImageCount"),
	AJAX_PROBAND_TAG_TAB_TITLE_BASE64("probandTagTabTitleBase64"),
	AJAX_PROBAND_TAG_VALUE_COUNT("probandTagValueCount"),
	AJAX_PROBAND_CONTACT_DETAIL_TAB_TITLE_BASE64("probandContactDetailTabTitleBase64"),
	AJAX_PROBAND_CONTACT_DETAIL_VALUE_COUNT("probandContactDetailValueCount"),
	AJAX_PROBAND_RECIPIENT_TAB_TITLE_BASE64("probandRecipientTabTitleBase64"),
	AJAX_PROBAND_RECIPIENT_COUNT("probandRecipientCount"),
	AJAX_PROBAND_ADDRESS_TAB_TITLE_BASE64("probandAddressTabTitleBase64"),
	AJAX_PROBAND_ADDRESS_COUNT("probandAddressCount"),
	AJAX_PROBAND_STATUS_TAB_TITLE_BASE64("probandStatusTabTitleBase64"),
	AJAX_PROBAND_STATUS_ENTRY_COUNT("probandStatusEntryCount"),
	AJAX_DIAGNOSIS_TAB_TITLE_BASE64("diagnosisTabTitleBase64"),
	AJAX_DIAGNOSIS_COUNT("diagnosisCount"),
	AJAX_PROCEDURE_TAB_TITLE_BASE64("procedureTabTitleBase64"),
	AJAX_PROCEDURE_COUNT("procedureCount"),
	AJAX_MEDICATION_TAB_TITLE_BASE64("medicationTabTitleBase64"),
	AJAX_MEDICATION_COUNT("medicationCount"),
	AJAX_PROBAND_INVENTORY_BOOKING_TAB_TITLE_BASE64("probandInventoryBookingTabTitleBase64"),
	AJAX_PROBAND_INVENTORY_BOOKING_COUNT("probandInventoryBookingItemCount"),
	AJAX_BANK_ACCOUNT_TAB_TITLE_BASE64("bankAccountTabTitleBase64"),
	AJAX_BANK_ACCOUNT_COUNT("bankAccountCount"),
	AJAX_MONEY_TRANSFER_TAB_TITLE_BASE64("moneyTransferTabTitleBase64"),
	AJAX_MONEY_TRANSFER_COUNT("moneyTransferCount"),
	AJAX_TRIAL_PARTICIPATION_TAB_TITLE_BASE64("trialParticipationTabTitleBase64"),
	AJAX_TRIAL_PARTICIPATION_COUNT("trialParticipationCount"),
	AJAX_PROBAND_VISIT_SCHEDULE_TAB_TITLE_BASE64("probandVisitScheduleTabTitleBase64"),
	AJAX_PROBAND_VISIT_SCHEDULE_ITEM_COUNT("probandVisitScheduleItemCount"),
	AJAX_INQUIRY_VALUE_TAB_TITLE_BASE64("inquiryValueTabTitleBase64"),
	AJAX_INQUIRY_VALUE_COUNT("inquiryValueCount"),
	AJAX_PROBAND_FILE_TAB_TITLE_BASE64("probandFileTabTitleBase64"),
	AJAX_PROBAND_FILE_COUNT("probandFileCount"),
	AJAX_PROBAND_JOURNAL_TAB_TITLE_BASE64("probandJournalTabTitleBase64"),
	AJAX_PROBAND_JOURNAL_ENTRY_COUNT("probandJournalEntryCount"),
	AJAX_PROBAND_JOB_TAB_TITLE_BASE64("probandJobTabTitleBase64"),
	AJAX_PROBAND_JOB_COUNT("probandJobCount"),
	AJAX_MASS_MAIL_RECIPIENT_TAB_TITLE_BASE64("massMailRecipientTabTitleBase64"),
	AJAX_MASS_MAIL_RECIPIENT_COUNT("massMailRecipientCount"),
	AJAX_MASS_MAIL_FILE_TAB_TITLE_BASE64("massMailFileTabTitleBase64"),
	AJAX_MASS_MAIL_FILE_COUNT("massMailFileCount"),
	AJAX_MASS_MAIL_JOURNAL_TAB_TITLE_BASE64("massMailJournalTabTitleBase64"),
	AJAX_MASS_MAIL_JOURNAL_ENTRY_COUNT("massMailJournalEntryCount"),
	AJAX_PASSWORD_TAB_TITLE_BASE64("passwordTabTitleBase64"),
	AJAX_PASSWORD_COUNT("passwordCount"),
	AJAX_USER_PERMISSION_PROFILE_TAB_TITLE_BASE64("userPermissionProfileTabTitleBase64"),
	AJAX_USER_PERMISSION_PROFILE_COUNT("userPermissionProfileCount"),
	AJAX_USER_JOURNAL_TAB_TITLE_BASE64("userJournalTabTitleBase64"),
	AJAX_USER_JOURNAL_ENTRY_COUNT("userJournalEntryCount"),
	AJAX_SELECTION_SET_VALUE_TAB_TITLE_BASE64("selectionSetValueTabTitleBase64"),
	AJAX_SELECTION_SET_VALUE_COUNT("selectionSetValueCount"),
	AJAX_INPUT_FIELD_JOURNAL_TAB_TITLE_BASE64("inputFieldJournalTabTitleBase64"),
	AJAX_INPUT_FIELD_JOURNAL_ENTRY_COUNT("inputFieldJournalEntryCount"),
	AJAX_INPUT_FIELD_JOB_TAB_TITLE_BASE64("inputFieldJobTabTitleBase64"),
	AJAX_INPUT_FIELD_JOB_COUNT("inputFieldJobCount"),
	AJAX_INPUT_FIELD_VARIABLE_VALUES_BASE64("inputFieldVariableValuesBase64"),
	AJAX_INPUT_FIELD_PROBAND_BASE64("inputFieldProbandBase64"),
	AJAX_INPUT_FIELD_TRIAL_BASE64("inputFieldTrialBase64"),
	AJAX_INPUT_FIELD_PROBAND_ADDRESSES_BASE64("inputFieldProbandAddressesBase64"),
	AJAX_INPUT_FIELD_PROBAND_LIST_ENTRY_TAG_VALUES_BASE64("probandListEntryTagValuesBase64"),
	AJAX_INPUT_FIELD_PROBAND_LIST_ENTRY_BASE64("inputFieldProbandListEntryBase64"),
	AJAX_INPUT_FIELD_VISIT_SCHEDULE_ITEMS_BASE64("inputFieldVisitScheduleItemsBase64"),
	AJAX_INPUT_FIELD_PROBAND_GROUPS_BASE64("inputFieldProbandGroupsBase64"),
	AJAX_INPUT_FIELD_ACTIVE_USER_BASE64("activeUserBase64"),
	AJAX_INPUT_FIELD_LOCALE("inputFieldLocale"),
	AJAX_FIELD_DELTA_ERROR_MESSAGE_ID("fieldDeltaErrorMessageId"),
	INPUT_FIELD_DELTA_SUMMARY_MAX(""),
	INPUT_FIELD_OUTPUT_ID_PREFIX("inputfield_output_"),
	INPUT_FIELD_OUTPUT_ID_INDEX_SEPARATOR("_"),
	INPUT_FIELD_WIDGET_VAR_PREFIX("inputFieldWidget_"),
	INPUT_FIELD_WIDGET_VAR_INDEX_SEPARATOR("_"),
	INPUT_DATETIME_PATTERN(""),
	INPUT_TIME_PATTERN(""),
	INPUT_DATE_PATTERN(""),
	INPUT_DECIMAL_SEPARATOR(""),
	INPUT_TIMEZONE_ID(""),
	SYSTEM_TIMEZONE_ID(""),
	FIELD_CALCULATION_DEBUG_LEVEL(""),
	JQPLOT_DATE_PATTERN(DateUtil.JQPLOT_DATE_PATTERN),
	INPUT_JSON_DATETIME_PATTERN(JsUtil.INPUT_JSON_DATETIME_PATTERN),
	AJAX_CRITERIA_JOURNAL_TAB_TITLE_BASE64("criteriaJournalTabTitleBase64"),
	AJAX_CRITERIA_JOURNAL_ENTRY_COUNT("criteriaJournalEntryCount"),
	AJAX_CRITERIA_JOB_TAB_TITLE_BASE64("criteriaJobTabTitleBase64"),
	AJAX_CRITERIA_JOB_COUNT("criteriaJobCount"),
	INVENTORY_PICKER_URL(Urls.INVENTORY_PICKER.value()),
	STAFF_PICKER_URL(Urls.STAFF_PICKER.value()),
	COURSE_PICKER_URL(Urls.COURSE_PICKER.value()),
	USER_PICKER_URL(Urls.USER_PICKER.value()),
	TRIAL_PICKER_URL(Urls.TRIAL_PICKER.value()),
	PROBAND_PICKER_URL(Urls.PROBAND_PICKER.value()),
	INPUT_FIELD_PICKER_URL(Urls.INPUT_FIELD_PICKER.value()),
	TEAM_MEMBER_PICKER_URL(Urls.TEAM_MEMBER_PICKER.value()),
	MASS_MAIL_PICKER_URL(Urls.MASS_MAIL_PICKER.value()),
	ECRF_SECTION_URL(Urls.ECRF_SECTION.value()),
	INVENTORY_URL(Urls.INVENTORY.value()),
	STAFF_URL(Urls.STAFF.value()),
	COURSE_URL(Urls.COURSE.value()),
	USER_URL(Urls.USER.value()),
	TRIAL_URL(Urls.TRIAL
			.value()),
	PROBAND_URL(Urls.PROBAND.value()),
	INPUT_FIELD_URL(Urls.INPUT_FIELD.value()),
	MASS_MAIL_URL(Urls.MASS_MAIL.value()),
	INVENTORY_SEARCH_URL(Urls.INVENTORY_SEARCH.value()),
	STAFF_SEARCH_URL(Urls.STAFF_SEARCH.value()),
	COURSE_SEARCH_URL(Urls.COURSE_SEARCH.value()),
	USER_SEARCH_URL(Urls.USER_SEARCH.value()),
	TRIAL_SEARCH_URL(Urls.TRIAL_SEARCH.value()),
	PROBAND_SEARCH_URL(Urls.PROBAND_SEARCH.value()),
	INPUT_FIELD_SEARCH_URL(Urls.INPUT_FIELD_SEARCH.value()),
	MASS_MAIL_SEARCH_URL(Urls.MASS_MAIL_SEARCH.value()),
	LOGIN_URL(Urls.LOGIN.value()),
	PORTAL_URL(Urls.PORTAL
			.value()),
	CHANGE_PASSWORD_URL(Urls.CHANGE_PASSWORD
			.value()),
	CHANGE_SETTINGS_URL(Urls.CHANGE_SETTINGS
			.value()),
	ADMIN_UPCOMING_COURSE_OVERVIEW_URL(Urls.ADMIN_UPCOMING_COURSE_OVERVIEW
			.value()),
	EXPIRING_COURSE_OVERVIEW_URL(Urls.EXPIRING_COURSE_OVERVIEW
			.value()),
	ADMIN_EXPIRING_PARTICIPATION_OVERVIEW_URL(Urls.ADMIN_EXPIRING_PARTICIPATION_OVERVIEW
			.value()),
	INVENTORY_STATUS_OVERVIEW_URL(Urls.INVENTORY_STATUS_OVERVIEW
			.value()),
	INVENTORY_MAINTENANCE_OVERVIEW_URL(Urls.INVENTORY_MAINTENANCE_OVERVIEW
			.value()),
	INVENTORY_BOOKING_SCHEDULE_URL(Urls.INVENTORY_BOOKING_SCHEDULE
			.value()),
	INVENTORY_BOOKING_SUMMARY_OVERVIEW_URL(Urls.INVENTORY_BOOKING_SUMMARY_OVERVIEW
			.value()),
	PROBAND_STATUS_OVERVIEW_URL(Urls.PROBAND_STATUS_OVERVIEW
			.value()),
	AUTO_DELETION_PROBAND_OVERVIEW_URL(Urls.AUTO_DELETION_PROBAND_OVERVIEW
			.value()),
	STAFF_STATUS_OVERVIEW_URL(Urls.STAFF_STATUS_OVERVIEW
			.value()),
	STAFF_SHIFT_SUMMARY_OVERVIEW_URL(Urls.STAFF_SHIFT_SUMMARY_OVERVIEW
			.value()),
	UPCOMING_COURSE_OVERVIEW_URL(Urls.UPCOMING_COURSE_OVERVIEW
			.value()),
	EXPIRING_PARTICIPATION_OVERVIEW_URL(Urls.EXPIRING_PARTICIPATION_OVERVIEW
			.value()),
	TIMELINE_EVENT_OVERVIEW_URL(Urls.TIMELINE_EVENT_OVERVIEW
			.value()),
	MONEY_TRANSFER_OVERVIEW_URL(Urls.MONEY_TRANSFER_OVERVIEW
			.value()),
	ECRF_PROGRESS_OVERVIEW_URL(Urls.ECRF_PROGRESS_OVERVIEW
			.value()),
	RANDOMIZATION_CODE_OVERVIEW_URL(Urls.RANDOMIZATION_CODE_OVERVIEW
			.value()),
	TRIAL_SHIFT_SUMMARY_OVERVIEW_URL(Urls.TRIAL_SHIFT_SUMMARY_OVERVIEW
			.value()),
	RECIPIENT_OVERVIEW_URL(Urls.RECIPIENT_OVERVIEW
			.value()),
	TRIAL_TIMELINE_URL(Urls.TRIAL_TIMELINE
			.value()),
	DUTY_ROSTER_SCHEDULE_URL(Urls.DUTY_ROSTER_SCHEDULE
			.value()),
	INVENTORY_START_URL(Urls.INVENTORY_START
			.value()),
	STAFF_START_URL(Urls.STAFF_START
			.value()),
	COURSE_START_URL(Urls.COURSE_START
			.value()),
	USER_START_URL(Urls.USER_START
			.value()),
	TRIAL_START_URL(Urls.TRIAL_START
			.value()),
	PROBAND_START_URL(Urls.PROBAND_START
			.value()),
	INPUT_FIELD_START_URL(Urls.INPUT_FIELD_START
			.value()),
	MASS_MAIL_START_URL(Urls.MASS_MAIL_START
			.value()),
	REST_API_URL(""),
	// multiple window navigation:
	INVENTORY_ENTITY_WINDOW_NAME("inventory"),
	STAFF_ENTITY_WINDOW_NAME("staff"),
	COURSE_ENTITY_WINDOW_NAME("course"),
	USER_ENTITY_WINDOW_NAME("user"),
	TRIAL_ENTITY_WINDOW_NAME("trial"),
	PROBAND_ENTITY_WINDOW_NAME("proband"),
	INPUT_FIELD_ENTITY_WINDOW_NAME("inputfield"),
	MASS_MAIL_ENTITY_WINDOW_NAME("massmail"),
	NEW_ENTITY_WINDOW_NAME_SUFFIX(""),
	INVENTORY_HOME_WINDOW_NAME("inventory_home"), // NO minus IN FRAME NAMES! -> IE8
	STAFF_HOME_WINDOW_NAME("staff_home"),
	COURSE_HOME_WINDOW_NAME("course_home"),
	USER_HOME_WINDOW_NAME("user_home"),
	TRIAL_HOME_WINDOW_NAME("trial_home"),
	PROBAND_HOME_WINDOW_NAME("proband_home"),
	INPUT_FIELD_HOME_WINDOW_NAME("inputfield_home"),
	MASS_MAIL_HOME_WINDOW_NAME("massmail_home"),
	PORTAL_WINDOW_NAME("portal"),
	ECRF_SECTION_WINDOW_NAME("ecrf_issue"),
	TRUSTED_REFERER_HOSTS(""),
	HEADLINE_ID("headline"),
	CONTEXT_PATH(""),
	SESSION_TIMER_PATTERN(""),
	SESSION_EXPIRED_MESSAGE(""),
	APPLICATION_URL(""),
	EXPIRED(GetParamNames.EXPIRED.toString()),
	AUTHENTICATION_FAILED(GetParamNames.AUTHENTICATION_FAILED.toString()),
	AUTHENTICATION_FAILED_MESSAGE(GetParamNames.AUTHENTICATION_FAILED_MESSAGE.toString()),
	REFERER(GetParamNames.REFERER.toString()),
	AJAX_KEEP_ALIVE_JS_CALLBACK("ajaxKeepAliveJsCallback"),
	AJAX_KEEP_ALIVE_JS_CALLBACK_ARGS("ajaxKeepAliveJsCallbackArgs"),
	AJAX_TIMELINE_RANGE_START("timelineRangeStart"),
	AJAX_TIMELINE_RANGE_END("timelineRangeEnd"),
	AJAX_TIMELINE_EVENT_INDEX("timelineEventIndex"),
	AJAX_TIMELINE_EVENT_START("timelineEventStart"),
	AJAX_TIMELINE_EVENT_END("timelineEventEnd"),
	DEFAULT_GEOLOCATION_LATITUDE(""),
	DEFAULT_GEOLOCATION_LONGITUDE(""),
	ENABLE_GEOLOCATION_SERVICES(""),
	HIGHLIGHT_TEXT_INPUT(""),
	FORCE_DEFAULT_GEOLOCATION(""),
	CURRENCY_SYMBOL(""),
	PF_LOCALE_STRING(""),
	FIELD_CALCULATION_DECODE_BASE64(Boolean.toString(true));

	public static String getWindowNamePattern(String jsVarName) {
		return Settings.getString(jsVarName.toLowerCase(), Bundle.WINDOWS, jsVarName);
	}

	private final String value;

	private JSValues(final String value) {
		this.value = value;
	}

	public String toQuotedEscapedString() {
		switch (this) {
			case TRUSTED_REFERER_HOSTS:
				return WebUtil.escapeJSStringArray(Settings.getTrustetRefererHosts((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()), true,
						true);
			case INPUT_FIELD_DELTA_SUMMARY_MAX:
			case FIELD_CALCULATION_DEBUG_LEVEL:
			case ENABLE_GEOLOCATION_SERVICES:
			case FORCE_DEFAULT_GEOLOCATION:
			case DEFAULT_GEOLOCATION_LATITUDE:
			case DEFAULT_GEOLOCATION_LONGITUDE:
			case INPUT_DECIMAL_SEPARATOR:
			case HIGHLIGHT_TEXT_INPUT:
			case LIST_INITIAL_POSITION:
			case FIELD_CALCULATION_DECODE_BASE64:
				return this.toString();
			default:
				return WebUtil.quoteJSString(this.toString(), true);
		}
	}

	@Override
	public String toString() {
		switch (this) {
			case INVENTORY_PICKER_URL:
			case STAFF_PICKER_URL:
			case COURSE_PICKER_URL:
			case USER_PICKER_URL:
			case TRIAL_PICKER_URL:
			case PROBAND_PICKER_URL:
			case INPUT_FIELD_PICKER_URL:
			case MASS_MAIL_PICKER_URL:
			case TEAM_MEMBER_PICKER_URL:
			case ECRF_SECTION_URL:
			case INVENTORY_URL:
			case STAFF_URL:
			case COURSE_URL:
			case USER_URL:
			case TRIAL_URL:
			case PROBAND_URL:
			case INPUT_FIELD_URL:
			case MASS_MAIL_URL:
			case INVENTORY_SEARCH_URL:
			case STAFF_SEARCH_URL:
			case COURSE_SEARCH_URL:
			case USER_SEARCH_URL:
			case TRIAL_SEARCH_URL:
			case PROBAND_SEARCH_URL:
			case INPUT_FIELD_SEARCH_URL:
			case MASS_MAIL_SEARCH_URL:
			case ADMIN_UPCOMING_COURSE_OVERVIEW_URL:
			case EXPIRING_COURSE_OVERVIEW_URL:
			case ADMIN_EXPIRING_PARTICIPATION_OVERVIEW_URL:
			case INVENTORY_STATUS_OVERVIEW_URL:
			case INVENTORY_MAINTENANCE_OVERVIEW_URL:
			case INVENTORY_BOOKING_SCHEDULE_URL:
			case INVENTORY_BOOKING_SUMMARY_OVERVIEW_URL:
			case PROBAND_STATUS_OVERVIEW_URL:
			case AUTO_DELETION_PROBAND_OVERVIEW_URL:
			case STAFF_STATUS_OVERVIEW_URL:
			case STAFF_SHIFT_SUMMARY_OVERVIEW_URL:
			case UPCOMING_COURSE_OVERVIEW_URL:
			case EXPIRING_PARTICIPATION_OVERVIEW_URL:
			case TIMELINE_EVENT_OVERVIEW_URL:
			case MONEY_TRANSFER_OVERVIEW_URL:
			case ECRF_PROGRESS_OVERVIEW_URL:
			case RANDOMIZATION_CODE_OVERVIEW_URL:
			case TRIAL_SHIFT_SUMMARY_OVERVIEW_URL:
			case RECIPIENT_OVERVIEW_URL:
			case TRIAL_TIMELINE_URL:
			case DUTY_ROSTER_SCHEDULE_URL:
			case LOGIN_URL:
			case PORTAL_URL:
			case CHANGE_PASSWORD_URL:
			case CHANGE_SETTINGS_URL:
			case INVENTORY_START_URL:
			case STAFF_START_URL:
			case COURSE_START_URL:
			case USER_START_URL:
			case TRIAL_START_URL:
			case PROBAND_START_URL:
			case INPUT_FIELD_START_URL:
			case MASS_MAIL_START_URL:
				return Urls.viewIdToUrl((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest(), value);
			case REST_API_URL:
				return ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getContextPath() + "/" + WebUtil.REST_API_PATH + "/";
			case INVENTORY_ENTITY_WINDOW_NAME:
			case STAFF_ENTITY_WINDOW_NAME:
			case COURSE_ENTITY_WINDOW_NAME:
			case USER_ENTITY_WINDOW_NAME:
			case TRIAL_ENTITY_WINDOW_NAME:
			case PROBAND_ENTITY_WINDOW_NAME:
			case INPUT_FIELD_ENTITY_WINDOW_NAME:
			case MASS_MAIL_ENTITY_WINDOW_NAME:
			case INVENTORY_HOME_WINDOW_NAME:
			case STAFF_HOME_WINDOW_NAME:
			case COURSE_HOME_WINDOW_NAME:
			case USER_HOME_WINDOW_NAME:
			case TRIAL_HOME_WINDOW_NAME:
			case PROBAND_HOME_WINDOW_NAME:
			case INPUT_FIELD_HOME_WINDOW_NAME:
			case MASS_MAIL_HOME_WINDOW_NAME:
			case NEW_ENTITY_WINDOW_NAME_SUFFIX:
			case PORTAL_WINDOW_NAME:
			case ECRF_SECTION_WINDOW_NAME:
				return getWindowNamePattern(name());
			case ENABLE_GEOLOCATION_SERVICES:
				return Boolean.toString(Settings.getBoolean(SettingCodes.ENABLE_GEOLOCATION_SERVICES, Bundle.SETTINGS, DefaultSettings.ENABLE_GEOLOCATION_SERVICES));
			case FORCE_DEFAULT_GEOLOCATION:
				return Boolean.toString(Settings.getBoolean(SettingCodes.FORCE_DEFAULT_GEOLOCATION, Bundle.SETTINGS, DefaultSettings.FORCE_DEFAULT_GEOLOCATION));
			case DEFAULT_GEOLOCATION_LATITUDE:
				return Float.toString(Settings.getFloat(SettingCodes.DEFAULT_GEOLOCATION_LATITUDE, Bundle.SETTINGS, DefaultSettings.DEFAULT_GEOLOCATION_LATITUDE));
			case DEFAULT_GEOLOCATION_LONGITUDE:
				return Float.toString(Settings.getFloat(SettingCodes.DEFAULT_GEOLOCATION_LONGITUDE, Bundle.SETTINGS, DefaultSettings.DEFAULT_GEOLOCATION_LONGITUDE));
			case LIST_INITIAL_POSITION:
				return Long.toString(CommonUtil.LIST_INITIAL_POSITION);
			case HIGHLIGHT_TEXT_INPUT:
				return Boolean.toString(Settings.getBoolean(SettingCodes.HIGHLIGHT_TEXT_INPUT, Bundle.SETTINGS, DefaultSettings.HIGHLIGHT_TEXT_INPUT));
			case TRUSTED_REFERER_HOSTS:
				return Settings.getTrustedRefererHostsString((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest());
			case CONTEXT_PATH:
				return ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getContextPath();
			case SESSION_TIMER_PATTERN:
				return Messages.getString(MessageCodes.SESSION_TIMER_PATTERN);
			case SESSION_EXPIRED_MESSAGE:
				return Messages.getString(MessageCodes.SESSION_EXPIRED_MESSAGE);
			case APPLICATION_URL:
				return WebUtil.getApplicationRootUrl();
			case INPUT_FIELD_DELTA_SUMMARY_MAX:
				try {
					return Integer.toString(Settings.getIntNullable(SettingCodes.INPUT_FIELD_DELTA_SUMMARY_MAX, Bundle.SETTINGS, DefaultSettings.INPUT_FIELD_DELTA_SUMMARY_MAX));
				} catch (NullPointerException e) {
					return WebUtil.JS_NULL;
				}
			case CURRENCY_SYMBOL:
				return WebUtil.getCurrencySymbol();
			case INPUT_DATETIME_PATTERN:
				return CommonUtil.getInputDateTimePattern(WebUtil.getDateFormat());
			case INPUT_TIME_PATTERN:
				return CommonUtil.getInputTimePattern(WebUtil.getDateFormat());
			case INPUT_DATE_PATTERN:
				return CommonUtil.getInputDatePattern(WebUtil.getDateFormat());
			case INPUT_DECIMAL_SEPARATOR:
				try {
					return WebUtil.quoteJSString(WebUtil.getDecimalSeparator().toString(), true);
				} catch (NullPointerException e) {
					return WebUtil.JS_NULL;
				}
			case INPUT_TIMEZONE_ID:
				return CommonUtil.timeZoneToString(WebUtil.getTimeZone());
			case SYSTEM_TIMEZONE_ID:
				return CommonUtil.timeZoneToString(WebUtil.getDefaultTimeZone());
			case FIELD_CALCULATION_DEBUG_LEVEL:
				return Integer.toString(Settings.getInt(SettingCodes.FIELD_CALCULATION_DEBUG_LEVEL, Bundle.SETTINGS, DefaultSettings.FIELD_CALCULATION_DEBUG_LEVEL));
			case PF_LOCALE_STRING:
				try {
					return LocaleConverter.getLocaleString(WebUtil.getLocale(), '_');
				} catch (NullPointerException e) {
					return LocaleConverter.getLocaleString(WebUtil.getDefaultLocale(), '_');
				}
			default:
				return value;
		}
	}
}
