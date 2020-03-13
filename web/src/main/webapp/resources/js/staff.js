
var oldStaffTabIndex = 0;

function handleStaffTabChange(index) {

	switch (index) {
	case 0:

		switch (oldStaffTabIndex) {
		case 0:
			break;
		case 1:
			changeStaffByImage();
			break;
		case 2:
			changeStaffByTag();
			break;
		case 3:
			changeStaffByContactDetail();
			break;
		case 4:
			changeStaffByAddress();
			break;
		case 5:
			changeStaffByStatus();
			break;
		case 6:
			changeStaffByDutyRosterTurn();
			break;
		case 7:
			changeStaffByCvPosition();
			break;
		case 8:
			changeStaffByCourseParticipationStatus();
			break;

		case 9:
			break;
		case 10:
			changeStaffByHyperlink();
			break;
		case 11:
			changeStaffByFile();
			break;
		case 12:
			changeStaffByJournalEntry();
			break;

		default:
			break;
		}

		break;

	case 1:
		changeStaffImage();
		break;
	case 2:
		changeStaffTag();
		break;
	case 3:
		changeStaffContactDetail();
		break;
	case 4:
		changeStaffAddress();
		break;
	case 5:
		changeStaffStatus();
		break;
	case 6:
		changeStaffDutyRosterTurn();
		break;
	case 7:
		changeCvPosition();
		break;
	case 8:
		changeCourseParticipationStatus();
		break;

	case 9:
		changeStaffAssociation();
		break;

	case 10:
		changeStaffHyperlink();
		break;

	case 11:
		changeStaffFile();
		break;

	case 12:
		changeStaffJournalEntry();
		break;

	default:
		break;
	}
	oldStaffTabIndex = index;
	return true;

}

function handleStaffChanged(xhr, status, args) {

	if (_testFlag(args, AJAX_OPERATION_SUCCESS) && _testPropertyExists(args, AJAX_WINDOW_TITLE_BASE64)
	        && _testPropertyExists(args, AJAX_ROOT_ENTITY_CREATED) && _testPropertyExists(args, AJAX_WINDOW_NAME)) {
		window.name = args[AJAX_WINDOW_NAME];
		enableTabs(staffTabView, 1, args[AJAX_ROOT_ENTITY_CREATED]);
		handleUpdateStaffTabTitles(xhr, status, args);
		var title = decodeBase64(args[AJAX_WINDOW_TITLE_BASE64]);
		document.title = title;
		setText(HEADLINE_ID, title);
		updateStaffEntityMenuBar();
	}

}

function handleUpdateStaffTabTitles(xhr, status, args) {
	if (_testPropertyExists(args, AJAX_OPERATION_SUCCESS) && _testPropertyExists(args, AJAX_ROOT_ENTITY_CREATED)) {
		staffTabView.emphasizeTab(0, _testFlag(args, AJAX_OPERATION_SUCCESS) && !args[AJAX_ROOT_ENTITY_CREATED]);
	}
	if (_testPropertyExists(args, AJAX_STAFF_IMAGE_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_STAFF_IMAGE_COUNT)) {
		staffTabView.setTabTitle(1, decodeBase64(args[AJAX_STAFF_IMAGE_TAB_TITLE_BASE64]));
		staffTabView.emphasizeTab(1, args[AJAX_STAFF_IMAGE_COUNT] == 0);
	}
	if (_testPropertyExists(args, AJAX_STAFF_TAG_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_STAFF_TAG_VALUE_COUNT)) {
		staffTabView.setTabTitle(2, decodeBase64(args[AJAX_STAFF_TAG_TAB_TITLE_BASE64]));
		staffTabView.emphasizeTab(2, args[AJAX_STAFF_TAG_VALUE_COUNT] == 0);
	}
	if (_testPropertyExists(args, AJAX_STAFF_CONTACT_DETAIL_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_STAFF_CONTACT_DETAIL_VALUE_COUNT)) {
		staffTabView.setTabTitle(3, decodeBase64(args[AJAX_STAFF_CONTACT_DETAIL_TAB_TITLE_BASE64]));
		staffTabView.emphasizeTab(3, args[AJAX_STAFF_CONTACT_DETAIL_VALUE_COUNT] == 0);
	}
	if (_testPropertyExists(args, AJAX_STAFF_ADDRESS_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_STAFF_ADDRESS_COUNT)) {
		staffTabView.setTabTitle(4, decodeBase64(args[AJAX_STAFF_ADDRESS_TAB_TITLE_BASE64]));
		staffTabView.emphasizeTab(4, args[AJAX_STAFF_ADDRESS_COUNT] == 0);
	}
	if (_testPropertyExists(args, AJAX_STAFF_STATUS_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_STAFF_STATUS_ENTRY_COUNT)) {
		staffTabView.setTabTitle(5, decodeBase64(args[AJAX_STAFF_STATUS_TAB_TITLE_BASE64]));
		staffTabView.emphasizeTab(5, args[AJAX_STAFF_STATUS_ENTRY_COUNT] == 0);
	}
	if (_testPropertyExists(args, AJAX_STAFF_DUTY_ROSTER_TURN_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_STAFF_DUTY_ROSTER_TURN_COUNT)) {
		staffTabView.setTabTitle(6, decodeBase64(args[AJAX_STAFF_DUTY_ROSTER_TURN_TAB_TITLE_BASE64]));
		staffTabView.emphasizeTab(6, args[AJAX_STAFF_DUTY_ROSTER_TURN_COUNT] == 0);
	}
	if (_testPropertyExists(args, AJAX_CV_POSITION_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_CV_POSITION_COUNT)) {
		staffTabView.setTabTitle(7, decodeBase64(args[AJAX_CV_POSITION_TAB_TITLE_BASE64]));
		staffTabView.emphasizeTab(7, args[AJAX_CV_POSITION_COUNT] == 0);
	}
	if (_testPropertyExists(args, AJAX_COURSE_PARTICIPATION_STATUS_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_COURSE_PARTICIPATION_STATUS_ENTRY_COUNT)) {
		staffTabView.setTabTitle(8, decodeBase64(args[AJAX_COURSE_PARTICIPATION_STATUS_TAB_TITLE_BASE64]));

	}

	if (_testPropertyExists(args, AJAX_STAFF_HYPERLINK_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_STAFF_HYPERLINK_COUNT)) {
		staffTabView.setTabTitle(10, decodeBase64(args[AJAX_STAFF_HYPERLINK_TAB_TITLE_BASE64]));
		staffTabView.emphasizeTab(10, args[AJAX_STAFF_HYPERLINK_COUNT] == 0);
	}
	if (_testPropertyExists(args, AJAX_STAFF_FILE_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_STAFF_FILE_COUNT)) {
		staffTabView.setTabTitle(11, decodeBase64(args[AJAX_STAFF_FILE_TAB_TITLE_BASE64]));
		staffTabView.emphasizeTab(11, args[AJAX_STAFF_FILE_COUNT] == 0);
	}
	if (_testPropertyExists(args, AJAX_STAFF_JOURNAL_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_STAFF_JOURNAL_ENTRY_COUNT)) {
		staffTabView.setTabTitle(12, decodeBase64(args[AJAX_STAFF_JOURNAL_TAB_TITLE_BASE64]));
		staffTabView.emphasizeTab(12, args[AJAX_STAFF_JOURNAL_ENTRY_COUNT] == 0);
	}

}

var contactDetailNotifyPreset;

function setContactDetailNotify(apply) {

	if (apply) {
		contactDetailNotify.setValue(contactDetailNotifyPreset);
	}
	contactDetailNotifyPresetOverrideConfirmation.hide();
	contactDetailNotifyPreset = null;

}

function handleContactDetailTypeSelected(xhr, status, args) {

	if (_testPropertyExists(args, AJAX_CONTACT_DETAIL_TYPE_EMAIL) && _testPropertyExists(args, AJAX_CONTACT_DETAIL_TYPE_PHONE)
	        && _testPropertyExists(args, AJAX_CONTACT_DETAIL_NA) && _testPropertyExists(args, AJAX_CONTACT_DETAIL_TYPE_NOTIFY_PRESET)) {
		var contactDetailEmail = args[AJAX_CONTACT_DETAIL_TYPE_EMAIL];
		var contactDetailPhone = args[AJAX_CONTACT_DETAIL_TYPE_PHONE];
		var contactDetailNa = args[AJAX_CONTACT_DETAIL_NA];
		contactDetailNotifyPreset = args[AJAX_CONTACT_DETAIL_TYPE_NOTIFY_PRESET];
		if (!contactDetailNa && (contactDetailEmail || contactDetailPhone)) {
			contactDetailNotify.enable();
			if (contactDetailNotifyPreset && contactDetailNotifyPreset != contactDetailNotify.getValue()) {
				contactDetailNotifyPresetOverrideConfirmation.show();
				return;
			}
		} else {
			contactDetailNotify.setValue(false);
			contactDetailNotify.disable();
		}
	}
	contactDetailNotifyPreset = null;

}

var addressDeliverPreset;

function setAddressDeliver(apply) {

	if (apply) {
		addressDeliver.setValue(addressDeliverPreset);
	}
	addressDeliverPresetOverrideConfirmation.hide();
	addressDeliverPreset = null;

}

function handleAddressTypeSelected(xhr, status, args) {

	if (_testPropertyExists(args, AJAX_ADDRESS_TYPE_DELIVER_PRESET)) {
		addressDeliverPreset = args[AJAX_ADDRESS_TYPE_DELIVER_PRESET];
		if (addressDeliverPreset && addressDeliverPreset != addressDeliver.getValue()) {
			addressDeliverPresetOverrideConfirmation.show();
			return;
		}
	}
	addressDeliverPreset = null;

}

var cvPositionCvSectionShowCvPreset;
var cvPositionCvSectionTitlePreset;

function setCvPositionShowCv(apply) {

	if (apply) {
		cvPositionShowCv.setValue(cvPositionCvSectionShowCvPreset);
		if (cvPositionCvSectionShowCvPreset) {
			cvPositionShowCommentCv.enable();
		} else {
			cvPositionShowCommentCv.setValue(false);
			cvPositionShowCommentCv.disable();
		}
	}
	cvPositionCvSectionShowCvPresetOverrideConfirmation.hide();
	cvPositionCvSectionShowCvPreset = null;

}
function setCvPositionTitle(apply) {

	if (apply) {
		cvPositionTitle.setValue(cvPositionCvSectionTitlePreset);
	}
	cvPositionCvSectionTitlePresetOverrideConfirmation.hide();
	cvPositionCvSectionTitlePreset = null;

}

function handleCvPositionCvSectionSelected(xhr, status, args) {

	var confirm = false;

	if (_testPropertyExists(args, AJAX_CV_SECTION_SHOW_CV_PRESET) && _testPropertyExists(args, AJAX_CV_SECTION_TITLE_PRESET_BASE64)) {
		cvPositionCvSectionShowCvPreset = args[AJAX_CV_SECTION_SHOW_CV_PRESET];
		cvPositionCvSectionTitlePreset = decodeBase64(args[AJAX_CV_SECTION_TITLE_PRESET_BASE64]);
		if (cvPositionCvSectionTitlePreset != null && cvPositionCvSectionTitlePreset.length > 0) {
			if (cvPositionTitle.getValue().length > 0 && cvPositionCvSectionTitlePreset != cvPositionTitle.getValue()) {
				cvPositionCvSectionTitlePresetOverrideConfirmation.show();
				confirm = true;
			} else {
				cvPositionTitle.setValue(cvPositionCvSectionTitlePreset);
			}
		}
		if (cvPositionCvSectionShowCvPreset && cvPositionCvSectionShowCvPreset != cvPositionShowCv.getValue()) {
			cvPositionCvSectionShowCvPresetOverrideConfirmation.show();
			confirm = true;
		}
	}
	if (!confirm) {
		cvPositionCvSectionShowCvPreset = null;
		cvPositionCvSectionTitlePreset = null;
	}

}

var courseParticipationStatusEntryCvSectionShowCvPreset;

function setCourseParticipationStatusEntryCvSectionShowCv(apply) {

	if (apply) {
		courseParticipationStatusEntryShowCv.setValue(courseParticipationStatusEntryCvSectionShowCvPreset);
		if (courseParticipationStatusEntryCvSectionShowCvPreset) {
			courseParticipationStatusEntryShowCommentCv.enable();
		} else {
			courseParticipationStatusEntryShowCommentCv.setValue(false);
			courseParticipationStatusEntryShowCommentCv.disable();
		}
	}
	courseParticipationStatusEntryCvSectionShowCvPresetOverrideConfirmation.hide();
	courseParticipationStatusEntryCvSectionShowCvPreset = null;

}

function handleCourseParticipationStatusEntryCvSectionSelected(xhr, status, args) {

	if (_testPropertyExists(args, AJAX_CV_SECTION_SHOW_CV_PRESET)) {
		courseParticipationStatusEntryCvSectionShowCvPreset = args[AJAX_CV_SECTION_SHOW_CV_PRESET];
		if (courseParticipationStatusEntryCvSectionShowCvPreset
		        && courseParticipationStatusEntryCvSectionShowCvPreset != courseParticipationStatusEntryShowCv.getValue()) {
			courseParticipationStatusEntryCvSectionShowCvPresetOverrideConfirmation.show();
			return;
		}
	}
	courseParticipationStatusEntryCvSectionShowCvPreset = null;

}

var courseParticipationStatusEntryTrainingRecordSectionShowTrainingRecordPreset;

function setCourseParticipationStatusEntryTrainingRecordSectionShowTrainingRecord(apply) {

	if (apply) {
		courseParticipationStatusEntryShowTrainingRecord.setValue(courseParticipationStatusEntryTrainingRecordSectionShowTrainingRecordPreset);
	}
	courseParticipationStatusEntryTrainingRecordSectionShowTrainingRecordPresetOverrideConfirmation.hide();
	courseParticipationStatusEntryTrainingRecordSectionShowTrainingRecordPreset = null;

}

function handleCourseParticipationStatusEntryTrainingRecordSectionSelected(xhr, status, args) {

	if (_testPropertyExists(args, AJAX_TRAINING_RECORD_SECTION_SHOW_TRAINING_RECORD_PRESET)) {
		courseParticipationStatusEntryTrainingRecordSectionShowTrainingRecordPreset = args[AJAX_TRAINING_RECORD_SECTION_SHOW_TRAINING_RECORD_PRESET];
		if (courseParticipationStatusEntryTrainingRecordSectionShowTrainingRecordPreset
		        && courseParticipationStatusEntryTrainingRecordSectionShowTrainingRecordPreset != courseParticipationStatusEntryShowTrainingRecord.getValue()) {
			courseParticipationStatusEntryTrainingRecordSectionShowTrainingRecordPresetOverrideConfirmation.show();
			return;
		}
	}
	courseParticipationStatusEntryTrainingRecordSectionShowTrainingRecordPreset = null;

}

function handleDutyRosterDepartmentCategorySelected(xhr, status, args) {

	updateDutyRosterSchedule();

}

function updateDutyRosterSchedule() {

	showWaitDialog();
	dutyRosterSchedule.update();
	hideWaitDialog();

}

function handleDutyRosterScheduleEventSelected(xhr, status, args) {

	if (!_testFlag(args, AJAX_CANCEL)) {
		dutyRosterTurnDialog.show();
	} else {
		dutyRosterTurnDialog.hide();
		updateDutyRosterSchedule();
	}

}

function handleDutyRosterScheduleEventMoved(xhr, status, args) {

	updateDutyRosterSchedule();

}

function handleDutyRosterScheduleEventResized(xhr, status, args) {

	updateDutyRosterSchedule();

}
