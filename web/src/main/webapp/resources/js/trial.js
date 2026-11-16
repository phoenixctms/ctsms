
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
	storeTabIndexParam(index);
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
		changeStratificationChart();
		break;		
	case 7:
		changeRandomizationListCodes();
		break;

	default:
		break;
	}
	return true;

}

// what a value or output expression can use besides its arguments, as fieldCalculation.js exports it: the names, and the parameter
// names of the functions (from their source; none if the source is minified, eg. "openTrial(a)")
var expressionMaskNames = null;
var expressionSignatures = null;

function initExpressionMask() {
	expressionMaskNames = [];
	expressionSignatures = {};
	try {
		if (typeof FieldCalculation !== "undefined" && typeof FieldCalculation.getExpressionMask === "function") {
			var mask = FieldCalculation.getExpressionMask();
			for ( var name in mask) {
				expressionMaskNames.push(name);
				var parameters = typeof mask[name] === "function" ? getFunctionParameters(mask[name]) : null;
				if (parameters != null) {
					expressionSignatures[name] = parameters;
				}
			}
			expressionMaskNames.sort();
		}
	} catch (e) {
	}
}

// the parameter names of a function, or null for a native function or minified (all one letter) parameter names
function getFunctionParameters(f) {
	var source = Function.prototype.toString.call(f);
	var match = /^[^(]*\(([^)]*)\)/.exec(source.replace(/\/\*[\s\S]*?\*\//g, ""));
	if (match == null || /\[native code\]/.test(source)) {
		return null;
	}
	var parameters = [];
	var minified = true;
	var names = match[1].split(",");
	for (var i = 0; i < names.length; i++) {
		var parameter = jQuery.trim(names[i]);
		if (parameter.length > 0) {
			parameters.push(parameter);
			minified = minified && parameter.length == 1;
		}
	}
	return parameters.length > 0 && minified ? null : parameters;
}

function getExpressionMaskNames() {
	if (expressionMaskNames == null) {
		initExpressionMask();
	}
	return expressionMaskNames;
}

function getExpressionSignatures() {
	if (expressionSignatures == null) {
		initExpressionMask();
	}
	return expressionSignatures;
}

// json next to the editor: the variable names of the other fields (eCRF fields of the eCRF, inquiries or proband list attributes of the trial),
// and the callbacks of the expression functions (see ApplicationScopeBean.getExpressionCallbacksJson)
function getExpressionEditorJson(cm, styleClass, empty) {
	try {
		var json = JSON.parse(jQuery(cm.getWrapperElement()).closest("fieldset").find("." + styleClass).first().text() || empty);
		if (json != null) {
			return json;
		}
	} catch (e) {
	}
	return JSON.parse(empty);
}

function getExpressionVariableNames(cm) {
	return getExpressionEditorJson(cm, "ctsms-expression-variable-names", "[]");
}

function getExpressionCallbacks(cm) {
	return getExpressionEditorJson(cm, "ctsms-expression-callbacks", "{}");
}

// a global the expression cannot see: fieldCalculation.js hides the window's enumerable properties
// (for (var p in this) mask[p] = undefined), unless it exports them again or the expression declares them
function isHiddenExpressionGlobal(name, maskNames, localVars) {
	return Object.prototype.propertyIsEnumerable.call(window, name) && jQuery.inArray(name, maskNames) < 0 && jQuery.inArray(name, localVars) < 0;
}

function splitExpressionParameters(list) {
	var parameters = [];
	var names = list.split(",");
	for (var i = 0; i < names.length; i++) {
		var name = jQuery.trim(names[i]);
		if (name.length > 0) {
			parameters.push(name);
		}
	}
	return parameters;
}

// the open brackets before the caret of an expression editor, innermost last: calls { name (null for member calls such as "Math.max("),
// index of the argument }, function bodies { parameters, callback: the call the function is an argument of, as { name, index } } and other
// brackets {}. a function body is "function(v) {", "(v) => {" or "v => ..." (up to the next "," ")" or ";").
// strings and comments are skipped; null if the caret is in a string or comment
function getExpressionFrames(text) {
	var stack = [];
	var pending = null;
	var top = function() {
		return stack.length > 0 ? stack[stack.length - 1] : null;
	};
	var enclosingCall = function() {
		var frame = top();
		return frame != null && frame.hasOwnProperty("index") ? {
			name : frame.name,
			index : frame.index
		} : null;
	};
	var i = 0;
	while (i < text.length) {
		var c = text.charAt(i);
		if (c == "/" && text.charAt(i + 1) == "/") {
			var lineEnd = text.indexOf("\n", i);
			if (lineEnd < 0) {
				return null;
			}
			i = lineEnd;
		} else if (c == "/" && text.charAt(i + 1) == "*") {
			var commentEnd = text.indexOf("*/", i + 2);
			if (commentEnd < 0) {
				return null;
			}
			i = commentEnd + 1;
		} else if (c == "\"" || c == "'" || c == "`") {
			var j = i + 1;
			while (j < text.length && text.charAt(j) != c) {
				j += (text.charAt(j) == "\\" ? 2 : 1);
			}
			if (j >= text.length) {
				return null;
			}
			i = j;
		} else if (c == "=" && text.charAt(i + 1) == ">") {
			var before = text.substring(0, i);
			var arrow = /\(([^()]*)\)\s*$/.exec(before) || /([\w$]+)\s*$/.exec(before);
			var arrowFunction = {
				parameters : splitExpressionParameters(arrow != null ? arrow[1] : ""),
				callback : enclosingCall()
			};
			i++;
			if (/^\s*\{/.test(text.substring(i + 1))) {
				pending = arrowFunction;
			} else {
				arrowFunction.concise = true;
				stack.push(arrowFunction);
			}
		} else if (c == "(") {
			var callee = /(\.\s*)?([\w$]+)\s*$/.exec(text.substring(0, i));
			stack.push({
				name : callee != null && !callee[1] ? callee[2] : null,
				index : 0
			});
		} else if (c == "{") {
			var declaration = /function\s*[\w$]*\s*\(([^()]*)\)\s*$/.exec(text.substring(0, i));
			if (pending != null) {
				stack.push(pending);
				pending = null;
			} else if (declaration != null) {
				stack.push({
					parameters : splitExpressionParameters(declaration[1]),
					callback : enclosingCall()
				});
			} else {
				stack.push({});
			}
		} else if (c == "[") {
			stack.push({});
		} else if (c == ")" || c == "]" || c == "}" || c == "," || c == ";") {
			while (top() != null && top().concise) {
				stack.pop();
			}
			if (c == ",") {
				if (top() != null && top().hasOwnProperty("index")) {
					top().index++;
				}
			} else if (c != ";") {
				stack.pop();
			}
		}
		i++;
	}
	return stack;
}

function getExpressionFramesBefore(cm, cursor) {
	return getExpressionFrames(cm.getRange({
		line : 0,
		ch : 0
	}, cursor));
}

// the properties of the value a callback parameter is, eg. of v in "findInquiryValues(function(v) { return v.", or null
function getExpressionCallbackProperties(cm, cursor, name) {
	var frames = getExpressionFramesBefore(cm, cursor);
	if (frames != null) {
		var callbacks = getExpressionCallbacks(cm);
		for (var i = frames.length - 1; i >= 0; i--) {
			var position = frames[i].parameters != null ? jQuery.inArray(name, frames[i].parameters) : -1;
			if (position >= 0) {
				// the innermost declaration of the name
				var callback = frames[i].callback;
				return position == 0 && callback != null && callback.index == 0 && callbacks.hasOwnProperty(callback.name) ? callbacks[callback.name].properties : null;
			}
		}
	}
	return null;
}

// the suggestions: after a callback parameter and a ".", the properties of its value. otherwise the other fields' variable names and the
// expression names, then the javascript hint (keywords, the expression's own variables) without the hidden globals.
// while typing, there are suggestions for a name or a callback parameter's property only, not in strings, comments or after other "."
function expressionHint(cm, typed) {
	var cursor = cm.getCursor();
	var token = cm.getTokenAt(cursor);
	var inText = token.className == "string" || token.className == "comment";
	var textBefore = cm.getLine(cursor.line).substring(0, cursor.ch);
	var word = /[\w$]*$/.exec(textBefore)[0];
	var memberOf = /(?:^|[^\w$.\s])\s*([\w$]+)\s*\.\s*$/.exec(textBefore.substring(0, textBefore.length - word.length));
	if (memberOf != null && !inText) {
		var properties = getExpressionCallbackProperties(cm, cursor, memberOf[1]);
		if (properties != null) {
			var propertyList = filterHintNames(properties, word);
			if (typed && (propertyList.length == 0 || (propertyList.length == 1 && propertyList[0] == word))) {
				return null;
			}
			return {
				list : propertyList,
				from : {
					line : cursor.line,
					ch : cursor.ch - word.length
				},
				to : cursor
			};
		}
	}
	var hint = CodeMirror.javascriptHint(cm);
	if (hint == null) {
		return null;
	}
	var line = cm.getLine(hint.from.line);
	var query = line.substring(hint.from.ch, cursor.ch);
	var member = /\.\s*$/.test(line.substring(0, hint.from.ch));
	if (typed) {
		if (member || query.length == 0 || inText || token.className == "number") {
			return null;
		}
	}
	var maskNames = getExpressionMaskNames();
	var localVars = [];
	for (var v = token.state != null ? token.state.localVars : null; v; v = v.next) {
		localVars.push(v.name);
	}
	var i;
	if (member) {
		// the members of a hidden global, eg. "document.", are no use either
		var root = /([\w$]+)(?:\s*\.\s*[\w$]+)*\s*\.\s*$/.exec(line.substring(0, hint.from.ch));
		if (root != null && isHiddenExpressionGlobal(root[1], maskNames, localVars)) {
			return null;
		}
	} else {
		var list = filterHintNames(getExpressionVariableNames(cm), query).concat(filterHintNames(maskNames, query));
		for (i = 0; i < hint.list.length; i++) {
			if (jQuery.inArray(hint.list[i], list) < 0 && !isHiddenExpressionGlobal(hint.list[i], maskNames, localVars)) {
				list.push(hint.list[i]);
			}
		}
		hint.list = list;
	}
	if (typed && (hint.list.length == 0 || (hint.list.length == 1 && hint.list[0] == query))) {
		return null;
	}
	return hint;
}

function typedExpressionHint(cm) {
	return expressionHint(cm, true);
}

// Ctrl-Space
function showExpressionHint(cm) {
	showCodeMirrorHint(cm, expressionHint);
}

// the suggestions pop up while typing a name, or a "." after a callback parameter, in a value or output expression editor
function handleExpressionEditorKeyPress(event) {
	if (!/[\w$.]/.test(String.fromCharCode(event.charCode || event.which || 0))) {
		return;
	}
	var cm = getExpressionEditor(this);
	if (cm != null) {
		showTypedCodeMirrorHint(cm, typedExpressionHint);
	}
}

jQuery(document).on("keypress", ".CodeMirror textarea", handleExpressionEditorKeyPress);

var expressionCallTip = null;

function hideExpressionCallTip() {
	if (expressionCallTip != null) {
		expressionCallTip.remove();
		expressionCallTip = null;
	}
}

function showExpressionCallTip(cm, name, parameters, index) {
	hideExpressionCallTip();
	expressionCallTip = jQuery("<div class=\"ctsms-expression-call-tip\"></div>").text(name + "(");
	for (var i = 0; i < parameters.length; i++) {
		if (i > 0) {
			expressionCallTip.append(document.createTextNode(", "));
		}
		var parameter = jQuery("<span></span>").text(parameters[i]);
		if (i == index) {
			parameter.addClass("ctsms-expression-call-tip-current");
		}
		expressionCallTip.append(parameter);
	}
	expressionCallTip.append(document.createTextNode(")"));
	jQuery(document.body).append(expressionCallTip);
	var pos = cm.cursorCoords();
	expressionCallTip.css({
		left : pos.x + "px",
		top : Math.max(0, pos.y - expressionCallTip.outerHeight() - 2) + "px"
	});
}

// above the caret: the parameters of the fieldCalculation.js function whose arguments are typed, the current one in bold,
// or what a callback of it gets, eg. "function(inquiryValue)" in "findInquiryValues(function(" or "findInquiryValues(("
function updateExpressionCallTip(cm) {
	var frames = cm.somethingSelected() ? null : getExpressionFramesBefore(cm, cm.getCursor());
	var call = frames != null && frames.length > 0 && frames[frames.length - 1].hasOwnProperty("index") ? frames[frames.length - 1] : null;
	if (call != null) {
		var signatures = getExpressionSignatures();
		if (call.name != null && signatures.hasOwnProperty(call.name)) {
			showExpressionCallTip(cm, call.name, signatures[call.name], call.index);
			return;
		}
		var outer = frames.length > 1 ? frames[frames.length - 2] : null;
		var callbacks = getExpressionCallbacks(cm);
		if ((call.name == null || call.name == "function") && outer != null && outer.hasOwnProperty("index") && outer.index == 0 && outer.name != null
				&& callbacks.hasOwnProperty(outer.name)) {
			showExpressionCallTip(cm, "function", [ callbacks[outer.name].argument ], call.index);
			return;
		}
	}
	hideExpressionCallTip();
}

function getExpressionEditor(element) {
	var wrapper = jQuery(element).closest(".CodeMirror");
	var cm = wrapper.length > 0 ? wrapper[0].CodeMirror : null;
	return cm != null && wrapper.closest("fieldset").find(".ctsms-expression-variable-names").length > 0 ? cm : null;
}

// the call tip follows the caret (keys and clicks), and closes when the editor loses the focus
jQuery(document).on("keyup", ".CodeMirror textarea", function() {
	var cm = getExpressionEditor(this);
	if (cm != null) {
		updateExpressionCallTip(cm);
	}
}).on("mouseup", ".CodeMirror", function() {
	var cm = getExpressionEditor(this);
	if (cm != null) {
		window.setTimeout(function() {
			updateExpressionCallTip(cm);
		}, 0);
	}
}).on("focusout", ".CodeMirror textarea", hideExpressionCallTip);
