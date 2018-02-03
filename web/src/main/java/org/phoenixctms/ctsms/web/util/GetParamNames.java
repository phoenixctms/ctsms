package org.phoenixctms.ctsms.web.util;

public enum GetParamNames {
	INVENTORY_ID("inventoryid"),
	INVENTORY_TAG_VALUE_ID("inventorytagvalueid"),
	INVENTORY_STATUS_ENTRY_ID("inventorystatusentryid"),
	MAINTENANCE_SCHEDULE_ITEM_ID("maintenancescheduleitemid"),
	INVENTORY_BOOKING_ID("inventorybookingid"),
	TIME_ZONE_OFFSET("timezoneoffset"),
	CRITERIA_ID("criteriaid"),
	PICK_TARGET_FIELD("picktargetfield"),
	PICK_TARGET_LABEL("picktargetlabel"),
	START("start"),
	STOP("stop"),
	SOURCE_INDEX("sourceindex"),
	TARGET_INDEX("targetindex"),
	TARGET_POSITION("targetposition"),
	PICK_AJAX("pickajax"),
	PICK_AJAX_UPDATE("pickajaxupdate"),
	PICK_ADD_REMOTE_COMMAND("pickaddremotecommand"),
	PICK_ON_CLICK("pickonclick"),
	STAFF_ID("staffid"),
	COURSE_ID("courseid"),
	USER_ID("userid"),
	TRIAL_ID("trialid"),
	PROBAND_ID("probandid"),
	INPUT_FIELD_ID("inputfieldid"),
	MASS_MAIL_ID("massmailid"),
	MASS_MAIL_RECIPIENT_ID("massmailrecipientid"),
	HYPERLINK_ID("hyperlinkid"),
	JOURNAL_ENTRY_ID("journalentryid"),
	SERIES_INDEX("seriesindex"),
	FILE_ID("fileid"),
	LOGICAL_PATH("logicalpath"),
	STAFF_TAG_VALUE_ID("stafftagvalueid"),
	STAFF_CONTACT_DETAIL_VALUE_ID("staffcontactdetailvalueid"),
	STAFF_ADDRESS_ID("staffaddressid"),
	STAFF_STATUS_ENTRY_ID("staffstatusentryid"),
	DUTY_ROSTER_TURN_ID("dutyrosterturnid"),
	CV_POSITION_ID("cvpositionid"),
	COURSE_PARTICIPATION_STATUS_ENTRY_ID("courseparticipationstatusentryid"),
	LECTURER_ID("lecturerid"),
	INPUT_FIELD_SELECTION_SET_VALUE_ID("inputfieldselectionsetvalueid"),
	TRIAL_TAG_VALUE_ID("trialtagvalueid"),
	TEAM_MEMBER_ID("teammemberid"),
	TIMELINE_EVENT_ID("timelineeventid"),
	VISIT_ID("visitid"),
	PROBAND_GROUP_ID("probandgroupid"),
	VISIT_SCHEDULE_ITEM_ID("visitscheduleitemid"),
	PROBAND_LIST_ENTRY_TAG_ID("probandlistentrytagid"),
	INQUIRY_ID("inquiryid"),
	ECRF_ID("ecrfid"),
	ECRF_FIELD_ID("ecrffieldid"),
	ECRF_STATUS_TYPE_ID("ecrfstatustypeid"),
	ECRF_FIELD_STATUS_ENTRY_ID("issueid"), // short, to prevent url wordwrap
	PROBAND_LIST_ENTRY_ID("probandlistentryid"),
	PROBAND_STATUS_ENTRY_ID("probandstatusentryid"),
	DIAGNOSIS_ID("diagnosisid"),
	PROCEDURE_ID("procedureid"),
	MEDICATION_ID("medicationid"),
	PROBAND_TAG_VALUE_ID("probandtagvalueid"),
	PROBAND_CONTACT_DETAIL_VALUE_ID("probandcontactdetailvalueid"),
	PROBAND_ADDRESS_ID("probandaddressid"),
	BANK_ACCOUNT_ID("bankaccountid"),
	MONEY_TRANSFER_ID("moneytransferid"),
	VERSION("version"),
	EXPIRED("expired"),
	AUTHENTICATION_FAILED("authenticationfailed"),
	AUTHENTICATION_FAILED_MESSAGE("authenticationfailedmessage"),
	REFERER("referer"),
	UUID("uuid"),
	VALIDATE("validate"), BEACON("beacon");

	private final String value;

	private GetParamNames(final String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}