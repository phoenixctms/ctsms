
var oldCourseTabIndex = 0;

function handleCourseTabChange(index) {

	switch (index) {
	case 0:

		switch (oldCourseTabIndex) {
		case 0:
			break;
		case 1:
			changeCourseByLecturer();
			break;
		case 2:
			changeCourseByInventoryBooking();
			break;
		case 3:
			changeCourseByAdminCourseParticipationStatus();
			break;
		case 4:
			changeCourseByHyperlink();
			break;
		case 5:
			changeCourseByFile();
			break;
		case 6:
			changeCourseByJournalEntry();
			break;
		default:
			break;
		}

		break;

	case 1:
		changeLecturer();
		break;
	case 2:
		changeCourseInventoryBooking();
		break;
	case 3:
		changeAdminCourseParticipationStatus();
		break;
	case 4:
		changeCourseHyperlink();
		break;
	case 5:
		changeCourseFile();
		break;
	case 6:
		changeCourseJournalEntry();
		break;
	default:
		break;
	}
	oldCourseTabIndex = index;
	return true;

}

function handleCourseChanged(xhr, status, args) {

	if (_testFlag(args, AJAX_OPERATION_SUCCESS) && _testPropertyExists(args, AJAX_WINDOW_TITLE_BASE64) && _testPropertyExists(args, AJAX_ROOT_ENTITY_CREATED)
	        && _testPropertyExists(args, AJAX_WINDOW_NAME)) {
		window.name = args[AJAX_WINDOW_NAME];
		enableTabs(courseTabView, 1, args[AJAX_ROOT_ENTITY_CREATED]);
		handleUpdateCourseTabTitles(xhr, status, args);
		var title = decodeBase64(args[AJAX_WINDOW_TITLE_BASE64]);
		document.title = title;
		setText(HEADLINE_ID, title);
		updateCourseEntityMenuBar();
	}

}

function handleUpdateCourseTabTitles(xhr, status, args) {
	if (_testPropertyExists(args, AJAX_OPERATION_SUCCESS) && _testPropertyExists(args, AJAX_ROOT_ENTITY_CREATED)) {
		courseTabView.emphasizeTab(0, _testFlag(args, AJAX_OPERATION_SUCCESS) && !args[AJAX_ROOT_ENTITY_CREATED]);
	}
	if (_testPropertyExists(args, AJAX_LECTURER_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_LECTURER_COUNT)) {
		courseTabView.setTabTitle(1, decodeBase64(args[AJAX_LECTURER_TAB_TITLE_BASE64]));
		courseTabView.emphasizeTab(1, args[AJAX_LECTURER_COUNT] == 0);
	}

	if (_testPropertyExists(args, AJAX_COURSE_INVENTORY_BOOKING_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_COURSE_INVENTORY_BOOKING_COUNT)) {
		courseTabView.setTabTitle(2, decodeBase64(args[AJAX_COURSE_INVENTORY_BOOKING_TAB_TITLE_BASE64]));
		courseTabView.emphasizeTab(2, args[AJAX_COURSE_INVENTORY_BOOKING_COUNT] == 0);
	}
	if (_testPropertyExists(args, AJAX_ADMIN_COURSE_PARTICIPATION_STATUS_TAB_TITLE_BASE64)
	        && _testPropertyExists(args, AJAX_ADMIN_COURSE_PARTICIPATION_STATUS_ENTRY_COUNT)) {
		courseTabView.setTabTitle(3, decodeBase64(args[AJAX_ADMIN_COURSE_PARTICIPATION_STATUS_TAB_TITLE_BASE64]));
		courseTabView.emphasizeTab(3, args[AJAX_ADMIN_COURSE_PARTICIPATION_STATUS_ENTRY_COUNT] == 0);
	}

	if (_testPropertyExists(args, AJAX_COURSE_HYPERLINK_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_COURSE_HYPERLINK_COUNT)) {
		courseTabView.setTabTitle(4, decodeBase64(args[AJAX_COURSE_HYPERLINK_TAB_TITLE_BASE64]));
		courseTabView.emphasizeTab(4, args[AJAX_COURSE_HYPERLINK_COUNT] == 0);
	}
	if (_testPropertyExists(args, AJAX_COURSE_FILE_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_COURSE_FILE_COUNT)) {
		courseTabView.setTabTitle(5, decodeBase64(args[AJAX_COURSE_FILE_TAB_TITLE_BASE64]));
		courseTabView.emphasizeTab(5, args[AJAX_COURSE_FILE_COUNT] == 0);
	}
	if (_testPropertyExists(args, AJAX_COURSE_JOURNAL_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_COURSE_JOURNAL_ENTRY_COUNT)) {
		courseTabView.setTabTitle(6, decodeBase64(args[AJAX_COURSE_JOURNAL_TAB_TITLE_BASE64]));
		courseTabView.emphasizeTab(6, args[AJAX_COURSE_JOURNAL_ENTRY_COUNT] == 0);
	}

}

var courseShowCvPresetPreset;

function setCourseShowCvPreset(apply) {

	if (apply) {
		showCvPreset.setValue(courseShowCvPresetPreset);
		if (courseShowCvPresetPreset) {
			showCommentCvPreset.enable();
		} else {
			showCommentCvPreset.setValue(false);
			showCommentCvPreset.disable();
		}
	}
	courseShowCvPresetPresetOverrideConfirmation.hide();
	courseShowCvPresetPreset = null;

}

function handleCvSectionPresetSelected(xhr, status, args) {

	if (_testPropertyExists(args, AJAX_CV_SECTION_SHOW_CV_PRESET)) {
		courseShowCvPresetPreset = args[AJAX_CV_SECTION_SHOW_CV_PRESET];
		if (courseShowCvPresetPreset && courseShowCvPresetPreset != showCvPreset.getValue()) {
			courseShowCvPresetPresetOverrideConfirmation.show();
			return;
		}
	}
	courseShowCvPresetPreset = null;

}

var courseShowTrainingRecordPresetPreset;

function setCourseShowTrainingRecordPreset(apply) {

	if (apply) {
		showTrainingRecordPreset.setValue(courseShowTrainingRecordPresetPreset);
	}
	courseShowTrainingRecordPresetPresetOverrideConfirmation.hide();
	courseShowTrainingRecordPresetPreset = null;

}

function handleTrainingRecordSectionPresetSelected(xhr, status, args) {

	if (_testPropertyExists(args, AJAX_TRAINING_RECORD_SECTION_SHOW_TRAINING_RECORD_PRESET)) {
		courseShowTrainingRecordPresetPreset = args[AJAX_TRAINING_RECORD_SECTION_SHOW_TRAINING_RECORD_PRESET];
		if (courseShowTrainingRecordPresetPreset && courseShowTrainingRecordPresetPreset != showTrainingRecordPreset.getValue()) {
			courseShowTrainingRecordPresetPresetOverrideConfirmation.show();
			return;
		}
	}
	courseShowTrainingRecordPresetPreset = null;

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
