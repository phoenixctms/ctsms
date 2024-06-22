
var oldTrialTabIndex = 0;

function handleTrialTabChange(index) {

	FieldCalculation.resetInputFieldVariables();
	switch (index) {
	case 0:

		switch (oldTrialTabIndex) {
		case 0:
			break;
		case 1:
			changeTrialByTag();
			break;
		case 2:
			changeTrialByTeamMember();
			break;
		case 3:
			changeTrialByTimelineEvent();
			break;
		case 4:
			changeTrialByInventoryBooking();
			break;
		case 5:
			changeTrialByVisit();
			break;
		case 6:
			changeTrialByProbandGroup();
			break;
		case 7:
			changeTrialByVisitSchedule();
			break;
		case 8:
			changeTrialByDutyRosterTurn();
			break;
		case 9:
			changeTrialByProbandListEntryTag();
			break;
		case 10:
			changeTrialByStratificationRandomizationList();
			break;			
		case 11:
			changeTrialByInquiry();
			break;
		case 12:
			break;
		case 13:
			changeTrialByEcrf();
			break;
		case 14:
			changeTrialByEcrfField();
			break;
		case 15:
			changeTrialByProbandListEntry();
			break;
		case 16:
			break;
		case 17:
			break;
		case 18:
			break;

		case 19:
			break;

		case 20:
			changeTrialByHyperlink();
			break;
		case 21:
			changeTrialByJob();
			break;			
		case 22:
			changeTrialByFile();
			break;
		case 23:
			changeTrialByJournalEntry();
			break;
		default:
			break;
		}

		break;

	case 1:
		changeTrialTag();
		break;

	case 2:
		changeTeamMember();
		break;

	case 3:
		changeTimelineEvent();
		break;

	case 4:
		changeTrialInventoryBooking();
		break;

	case 5:
		changeVisit();
		break;

	case 6:
		changeProbandGroup();
		break;

	case 7:
		changeVisitSchedule();
		break;

	case 8:
		changeTrialDutyRosterTurn();
		break;

	case 9:
		changeProbandListEntryTag();
		break;

	case 10:
		changeStratificationRandomizationList();
		break;
		
	case 11:
		changeInquiry();
		break;

	case 12:
		changeInquiryValueDummy();
		break;

	case 13:
		changeEcrf();
		break;

	case 14:
		changeEcrfField();
		break;

	case 15:
		changeProbandListEntry();
		break;

	case 16:
		changeTrialEcrfStatusEntry();
		break;

	case 17:
		changeEcrfFieldStatus();
		break;

	case 18:
		changeReimbursements();
		break;


	case 19:
		changeTrialAssociation();
		break;

	case 20:
		changeTrialHyperlink();
		break;

	case 21:
		changeTrialJob();
		break;
		
	case 22:
		changeTrialFile();
		break;

	case 23:
		changeTrialJournalEntry();
		break;

	default:
		break;
	}
	oldTrialTabIndex = index;
	return true;

}

function handleTrialChanged(xhr, status, args) {

	if (_testFlag(args, AJAX_OPERATION_SUCCESS) && _testPropertyExists(args, AJAX_WINDOW_TITLE_BASE64) && _testPropertyExists(args, AJAX_ROOT_ENTITY_CREATED)
	        && _testPropertyExists(args, AJAX_WINDOW_NAME)) {
		window.name = args[AJAX_WINDOW_NAME];
		enableTabs(trialTabView, 1, args[AJAX_ROOT_ENTITY_CREATED]);
		handleUpdateTrialTabTitles(xhr, status, args);
		var title = decodeBase64(args[AJAX_WINDOW_TITLE_BASE64]);
		document.title = title;
		setText(HEADLINE_ID, title);
		updateTrialEntityMenuBar();
	}

}

function handleUpdateProbandListTabTitles(xhr, status, args) {
	if (_testFlag(args, AJAX_OPERATION_SUCCESS) && _testPropertyExists(args, AJAX_PROBAND_LIST_TAB_TITLE_BASE64)) {
		var title = decodeBase64(args[AJAX_PROBAND_LIST_TAB_TITLE_BASE64]);
		probandListTabView.setTabTitle(0, title);
	}
}

function handleUpdateTrialTabTitles(xhr, status, args) {

	if (_testPropertyExists(args, AJAX_OPERATION_SUCCESS) && _testPropertyExists(args, AJAX_ROOT_ENTITY_CREATED)) {
		trialTabView.emphasizeTab(reverseTabIndex(0), _testFlag(args, AJAX_OPERATION_SUCCESS) && !args[AJAX_ROOT_ENTITY_CREATED]);
	}

	if (_testPropertyExists(args, AJAX_TRIAL_TAG_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_TRIAL_TAG_VALUE_COUNT)) {
		trialTabView.setTabTitle(reverseTabIndex(1), decodeBase64(args[AJAX_TRIAL_TAG_TAB_TITLE_BASE64]));
		trialTabView.emphasizeTab(reverseTabIndex(1), args[AJAX_TRIAL_TAG_VALUE_COUNT] == 0);
	}
	if (_testPropertyExists(args, AJAX_TEAM_MEMBER_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_TEAM_MEMBER_COUNT)) {
		trialTabView.setTabTitle(reverseTabIndex(2), decodeBase64(args[AJAX_TEAM_MEMBER_TAB_TITLE_BASE64]));
		trialTabView.emphasizeTab(reverseTabIndex(2), args[AJAX_TEAM_MEMBER_COUNT] == 0);
	}
	if (_testPropertyExists(args, AJAX_TIMELINE_EVENT_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_TIMELINE_EVENT_COUNT)) {
		trialTabView.setTabTitle(reverseTabIndex(3), decodeBase64(args[AJAX_TIMELINE_EVENT_TAB_TITLE_BASE64]));
		trialTabView.emphasizeTab(reverseTabIndex(3), args[AJAX_TIMELINE_EVENT_COUNT] == 0);
	}
	if (_testPropertyExists(args, AJAX_TRIAL_INVENTORY_BOOKING_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_TRIAL_INVENTORY_BOOKING_COUNT)) {
		trialTabView.setTabTitle(reverseTabIndex(4), decodeBase64(args[AJAX_TRIAL_INVENTORY_BOOKING_TAB_TITLE_BASE64]));
		trialTabView.emphasizeTab(reverseTabIndex(4), args[AJAX_TRIAL_INVENTORY_BOOKING_COUNT] == 0);
	}
	if (_testPropertyExists(args, AJAX_VISIT_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_VISIT_COUNT)) {
		trialTabView.setTabTitle(reverseTabIndex(5), decodeBase64(args[AJAX_VISIT_TAB_TITLE_BASE64]));
		trialTabView.emphasizeTab(reverseTabIndex(5), args[AJAX_VISIT_COUNT] == 0);
	}
	if (_testPropertyExists(args, AJAX_PROBAND_GROUP_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_PROBAND_GROUP_COUNT)) {
		trialTabView.setTabTitle(reverseTabIndex(6), decodeBase64(args[AJAX_PROBAND_GROUP_TAB_TITLE_BASE64]));
		trialTabView.emphasizeTab(reverseTabIndex(6), args[AJAX_PROBAND_GROUP_COUNT] == 0);
	}
	if (_testPropertyExists(args, AJAX_VISIT_SCHEDULE_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_VISIT_SCHEDULE_ITEM_COUNT)) {
		trialTabView.setTabTitle(reverseTabIndex(7), decodeBase64(args[AJAX_VISIT_SCHEDULE_TAB_TITLE_BASE64]));
		trialTabView.emphasizeTab(reverseTabIndex(7), args[AJAX_VISIT_SCHEDULE_ITEM_COUNT] == 0);
	}
	if (_testPropertyExists(args, AJAX_TRIAL_DUTY_ROSTER_TURN_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_TRIAL_DUTY_ROSTER_TURN_COUNT)) {
		trialTabView.setTabTitle(reverseTabIndex(8), decodeBase64(args[AJAX_TRIAL_DUTY_ROSTER_TURN_TAB_TITLE_BASE64]));
		trialTabView.emphasizeTab(reverseTabIndex(8), args[AJAX_TRIAL_DUTY_ROSTER_TURN_COUNT] == 0);
	}
	if (_testPropertyExists(args, AJAX_PROBAND_LIST_ENTRY_TAG_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_PROBAND_LIST_ENTRY_TAG_COUNT)) {
		trialTabView.setTabTitle(reverseTabIndex(9), decodeBase64(args[AJAX_PROBAND_LIST_ENTRY_TAG_TAB_TITLE_BASE64]));
		trialTabView.emphasizeTab(reverseTabIndex(9), args[AJAX_PROBAND_LIST_ENTRY_TAG_COUNT] == 0);
	}
	if (_testPropertyExists(args, AJAX_STRATIFICATION_RANDOMIZATION_LIST_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_STRATIFICATION_RANDOMIZATION_LIST_COUNT)) {
		trialTabView.setTabTitle(reverseTabIndex(10), decodeBase64(args[AJAX_STRATIFICATION_RANDOMIZATION_LIST_TAB_TITLE_BASE64]));
		trialTabView.emphasizeTab(reverseTabIndex(10), args[AJAX_STRATIFICATION_RANDOMIZATION_LIST_COUNT] == 0);
	}	
	if (_testPropertyExists(args, AJAX_INQUIRY_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_INQUIRY_COUNT)) {
		trialTabView.setTabTitle(reverseTabIndex(11), decodeBase64(args[AJAX_INQUIRY_TAB_TITLE_BASE64]));
		trialTabView.emphasizeTab(reverseTabIndex(11), args[AJAX_INQUIRY_COUNT] == 0);
	}
	if (_testPropertyExists(args, AJAX_ECRF_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_ECRF_COUNT)) {
		trialTabView.setTabTitle(reverseTabIndex(13), decodeBase64(args[AJAX_ECRF_TAB_TITLE_BASE64]));
		trialTabView.emphasizeTab(reverseTabIndex(13), args[AJAX_ECRF_COUNT] == 0);
	}
	if (_testPropertyExists(args, AJAX_ECRF_FIELD_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_ECRF_FIELD_COUNT)) {
		trialTabView.setTabTitle(reverseTabIndex(14), decodeBase64(args[AJAX_ECRF_FIELD_TAB_TITLE_BASE64]));
		trialTabView.emphasizeTab(reverseTabIndex(14), args[AJAX_ECRF_FIELD_COUNT] == 0);
	}
	if (_testPropertyExists(args, AJAX_PROBAND_LIST_ENTRY_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_PROBAND_LIST_ENTRY_COUNT)) {
		trialTabView.setTabTitle(reverseTabIndex(15), decodeBase64(args[AJAX_PROBAND_LIST_ENTRY_TAB_TITLE_BASE64]));
		trialTabView.emphasizeTab(reverseTabIndex(15), args[AJAX_PROBAND_LIST_ENTRY_COUNT] == 0);
	}

	if (_testPropertyExists(args, AJAX_ECRF_FIELD_STATUS_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_ECRF_FIELD_STATUS_COUNT)) {
		trialTabView.setTabTitle(reverseTabIndex(17), decodeBase64(args[AJAX_ECRF_FIELD_STATUS_TAB_TITLE_BASE64]));
		trialTabView.emphasizeTab(reverseTabIndex(17), args[AJAX_ECRF_FIELD_STATUS_COUNT] == 0);
	}

	if (_testPropertyExists(args, AJAX_TRIAL_HYPERLINK_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_TRIAL_HYPERLINK_COUNT)) {
		trialTabView.setTabTitle(reverseTabIndex(20), decodeBase64(args[AJAX_TRIAL_HYPERLINK_TAB_TITLE_BASE64]));
		trialTabView.emphasizeTab(reverseTabIndex(20), args[AJAX_TRIAL_HYPERLINK_COUNT] == 0);
	}
	if (_testPropertyExists(args, AJAX_TRIAL_JOB_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_TRIAL_JOB_COUNT)) {
		trialTabView.setTabTitle(reverseTabIndex(21), decodeBase64(args[AJAX_TRIAL_JOB_TAB_TITLE_BASE64]));
		trialTabView.emphasizeTab(reverseTabIndex(21), args[AJAX_TRIAL_JOB_COUNT] == 0);
	}
	if (_testPropertyExists(args, AJAX_TRIAL_FILE_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_TRIAL_FILE_COUNT)) {
		trialTabView.setTabTitle(reverseTabIndex(22), decodeBase64(args[AJAX_TRIAL_FILE_TAB_TITLE_BASE64]));
		trialTabView.emphasizeTab(reverseTabIndex(22), args[AJAX_TRIAL_FILE_COUNT] == 0);
	}
	if (_testPropertyExists(args, AJAX_TRIAL_JOURNAL_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_TRIAL_JOURNAL_ENTRY_COUNT)) {
		trialTabView.setTabTitle(reverseTabIndex(23), decodeBase64(args[AJAX_TRIAL_JOURNAL_TAB_TITLE_BASE64]));
		trialTabView.emphasizeTab(reverseTabIndex(23), args[AJAX_TRIAL_JOURNAL_ENTRY_COUNT] == 0);
	}

}

var timelineEventNotifyPreset;
var timelineEventShowPreset;
var timelineEventImportancePreset;
var timelineEventTitlePresetFixed;
var timelineEventTitlePreset;

function setTimelineEventNotify(apply) {

	if (apply) {
		timelineEventNotify.setValue(timelineEventNotifyPreset);
	}
	timelineEventNotifyOverrideConfirmation.hide();
	timelineEventNotifyPreset = null;

}
function setTimelineEventShow(apply) {

	if (apply) {
		timelineEventShow.setValue(timelineEventShowPreset);
	}
	timelineEventShowOverrideConfirmation.hide();
	timelineEventShowPreset = null;

}
function setTimelineEventImportance(apply) {

	if (apply) {
		timelineEventImportance.setValue(timelineEventImportancePreset);
	}
	timelineEventImportanceOverrideConfirmation.hide();
	timelineEventImportancePreset = null;

}

function setTimelineEventTitle(apply) {

	if (apply) {
		timelineEventTitle.setValue(timelineEventTitlePreset);
	}
	timelineEventTitleOverrideConfirmation.hide();
	timelineEventTitlePreset = null;

}

function handleTimelineEventTypeSelected(xhr, status, args) {

	var confirm = false;
	if (_testPropertyExists(args, AJAX_TIMELINE_EVENT_TYPE_IMPORTANCE_PRESET) && _testPropertyExists(args, AJAX_TIMELINE_EVENT_TYPE_SHOW_PRESET)
	        && _testPropertyExists(args, AJAX_TIMELINE_EVENT_TYPE_NOTIFY_PRESET) && _testPropertyExists(args, AJAX_TIMELINE_EVENT_TYPE_TITLE_PRESET_FIXED)) {
		timelineEventNotifyPreset = args[AJAX_TIMELINE_EVENT_TYPE_NOTIFY_PRESET];
		timelineEventShowPreset = args[AJAX_TIMELINE_EVENT_TYPE_SHOW_PRESET];
		timelineEventImportancePreset = args[AJAX_TIMELINE_EVENT_TYPE_IMPORTANCE_PRESET];
		timelineEventTitlePresetFixed = args[AJAX_TIMELINE_EVENT_TYPE_TITLE_PRESET_FIXED];
		if (_testPropertyExists(args, AJAX_TIMELINE_EVENT_TYPE_TITLE_PRESET_BASE64)) {
			timelineEventTitlePreset = decodeBase64(args[AJAX_TIMELINE_EVENT_TYPE_TITLE_PRESET_BASE64]);
		} else {
			timelineEventTitlePreset = null;
		}

		if (timelineEventTitlePresetFixed) {
			timelineEventTitle.disable();
			timelineEventTitle.setValue(timelineEventTitlePreset);
		} else {
			timelineEventTitle.enable();
			if (timelineEventTitlePreset != null && timelineEventTitlePreset.length > 0) {
				if (timelineEventTitle.getValue().length > 0 && timelineEventTitlePreset != timelineEventTitle.getValue()) {
					timelineEventTitleOverrideConfirmation.show();
					confirm = true;
				} else {
					timelineEventTitle.setValue(timelineEventTitlePreset);
				}
			}
		}

		if (timelineEventImportancePreset != null && timelineEventImportancePreset.length > 0) {
			if (timelineEventImportance.getValue().length > 0 && timelineEventImportancePreset != timelineEventImportance.getValue()) {
				timelineEventImportanceOverrideConfirmation.show();
				confirm = true;
			} else {
				timelineEventImportance.setValue(timelineEventImportancePreset);
			}
		}
		if (timelineEventNotifyPreset && timelineEventNotifyPreset != timelineEventNotify.getValue()) {
			timelineEventNotifyOverrideConfirmation.show();
			confirm = true;
		}
		if (timelineEventShowPreset && timelineEventShowPreset != timelineEventShow.getValue()) {
			timelineEventShowOverrideConfirmation.show();
			confirm = true;
		}

	}
	if (!confirm) {
		timelineEventTitlePreset = null;
		timelineEventTitlePreset = null;
		timelineEventNotifyPreset = null;
		timelineEventShowPreset = null;
		timelineEventImportancePreset = null;
	}

}

function handleTrialTimelineRangeChanged() {
	var range = trialTimeline.getVisibleRange();
	var params = {};
	params[AJAX_TIMELINE_RANGE_START] = range.start.toUTCString();
	params[AJAX_TIMELINE_RANGE_END] = range.end.toUTCString();
	updateTrialTimelineRange(prepareRemoteCommandParameters(params));
	return range.extended;
}

function handleTrialTimelineAdd() {

	var range = trialTimeline.getVisibleRange();
	trialTimeline.cancelAdd();
	var start = new Date((range.start.valueOf() + range.end.valueOf()) / 2);
	var params = {};
	params[AJAX_TIMELINE_EVENT_START] = start.toUTCString();
	params[AJAX_TIMELINE_EVENT_END] = null;
	addTrialTimelineEvent(prepareRemoteCommandParameters(params));

}

function handleAddEditTrialTimelineEvent(xhr, status, args) {

	if (!_testFlag(args, AJAX_CANCEL)) {
		if (_testPropertyExists(args, AJAX_DIALOG_TITLE_BASE64)) {
			trialTimelineDialog.setTitle(decodeBase64(args[AJAX_DIALOG_TITLE_BASE64]));
		}
		trialTimelineDialog.show();
	} else {
		trialTimelineDialog.hide();
	}

}

function handleUpdateTrialTimelineDialogTitle(xhr, status, args) {

	if (_testPropertyExists(args, AJAX_OPERATION_SUCCESS) && _testPropertyExists(args, AJAX_DIALOG_TITLE_BASE64)) {
		trialTimelineDialog.setTitle(decodeBase64(args[AJAX_DIALOG_TITLE_BASE64]));
	}

}

function handleTrialTimelineDelete() {

	var params = {};
	var index = trialTimeline.getSelectedIndex();
	trialTimeline.cancelDelete();
	if (index == -1) {
		// no event was selected
		return;
	}
	params[AJAX_TIMELINE_EVENT_INDEX] = index;
	deleteTrialTimelineEvent(prepareRemoteCommandParameters(params));

}


function handleTrialAssociationTabChange(index) {

	switch (index) {
	case 0:
		changeTrialCourses();
		break;
	case 1:
		changeTrialMassMails();
		break;
	case 2:
		changeShiftDurationSummary();
		break;
	case 3:
		changeBookingDurationSummary();
		break;
	case 4:
		changeEnrollmentChart();
		break;
	case 5:
		changeDepartmentChart();
		break;		
	case 6:
		changeRandomizationListCodes();
		break;

	default:
		break;
	}
	return true;

}
