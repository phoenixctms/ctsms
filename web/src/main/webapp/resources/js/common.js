var pickerWindowNamePrefix = 'picker_';

var floatEpsilon = 0.000001;

//if (!window.console) console = {log: function() {}};

function blink() {
	var blinks = document.getElementsByTagName('blink');
	for ( var i = blinks.length - 1; i >= 0; i--) {
		var s = blinks[i];
		s.style.visibility = (s.style.visibility === 'visible') ? 'hidden' : 'visible';
	}
	window.setTimeout(blink, 1000);
}

var onKeyUpDelay = (function() {
    var timer = 0;
    return function(callback, ms) {
        clearTimeout (timer);
        timer = setTimeout(callback, ms);
    };
})();

// moves (initially hidden) buttons into the button bar of a p:fileUpload, next to its "choose" button. call it whenever both are rendered:
function moveIntoFileUploadButtonBar(fileUploadSelector, buttonsSelector) {
	$(buttonsSelector).appendTo($(fileUploadSelector).find('.fileupload-buttonbar')).css('display', '');
}

// moves (initially hidden) elements to the top of the content area of a p:fileUpload. call it whenever both are rendered:
function moveIntoFileUploadContent(fileUploadSelector, elementsSelector) {
	$(elementsSelector).prependTo($(fileUploadSelector).find('.fileupload-content')).css('display', '');
}

function noop() {

}

function zeroFill(integer,digits) {
    var result;
    var numberOfZeroes;
    if (integer == null || (integer + '').length == 0) {
        result = '';
        numberOfZeroes = digits;
    } else {
        result = integer + '';
        numberOfZeroes = digits - (integer + '').length;
    }
    for (var i = 0; i < numberOfZeroes; i++) {
        result = '0' + result;
    }
    return result;
}

var suppressWait = false;
var showDialogQueue = [];
function setSupressWait() {
	suppressWait = true;
}

function dequeueShowDialog() {
	var method;
	var count = 1;
	if (typeof waitDialog !== 'undefined') {
		method = showDialogQueue.shift();
		if (method) {
			while (showDialogQueue[0] == method) {
				showDialogQueue.shift();
				count++;
			}
			waitDialog[method]();
		}
	}
}

function showWaitDialog() {
	if (!suppressWait) {
		if (typeof waitDialog !== 'undefined') {
			showDialogQueue.push('show');
			dequeueShowDialog();
		}
	}

}
function hideWaitDialog() {

	if (typeof waitDialog !== 'undefined') {
		showDialogQueue.push('hide');
		dequeueShowDialog();
	}

	suppressWait = false;
}

function showErrorMessage(error_message_id) {
	var errorMessageDivElement = _getElement(error_message_id + '_div');
	if (errorMessageDivElement != null) {
		errorMessageDivElement.show();
	}
}

function hideErrorMessage(error_message_id) {
	var errorMessageDivElement = _getElement(error_message_id + '_div');
	if (errorMessageDivElement != null) {
		errorMessageDivElement.hide();
	}
}

function setErrorMessageText(error_message_id,output) {
	var errorMessageSpanElement = _getElement(error_message_id);
	if (errorMessageSpanElement != null) {
		errorMessageSpanElement.html(output);
	}
}

function setErrorMessageTexts(error_message_id,msgs) {
	var msgsLength = msgs.length;
	var errorMessageDivElement;
	var ulElement;
	var liElement;
	var spanId;
	var errorMessageSpanElement;
	var i;
	if (msgsLength > 0) {
		setErrorMessageText(error_message_id, msgs[0].output);
		errorMessageDivElement = _getElement(error_message_id + '_div');
		if (errorMessageDivElement != null) {
			ulElement = errorMessageDivElement.find('ul').first();
			ulElement.find('li:not(:first)').remove();
			if (msgsLength > 1) {
				liElement = ulElement.find('li').first();
				errorMessageSpanElement = liElement.find('span').first();
				spanId = errorMessageSpanElement.attr('id');
				for (i = 1; i < msgsLength; i++) {
					liElement = liElement.clone();
					errorMessageSpanElement = liElement.find('span').first();
					errorMessageSpanElement.html(msgs[i].output);
					errorMessageSpanElement.attr('id', spanId + '_' + i);
					ulElement.append(liElement);
				}
			}
		}
	} else {
		setErrorMessageText(error_message_id, '');
		errorMessageDivElement = _getElement(error_message_id + '_div');
		if (errorMessageDivElement != null) {
			ulElement = errorMessageDivElement.find('ul').first();
			ulElement.find('li:not(:first)').remove();
		}
	}
}

var sessionMaxInactiveInterval = null;
function createSessionTimer(duration) {
	if (duration != null && duration > 0) {
		sessionMaxInactiveInterval = +duration;
		var sessionExpiry = (new Date()); 
		sessionExpiry.setSeconds(sessionExpiry.getSeconds() + sessionMaxInactiveInterval);
		jQuery('#session_timer').countdown(sessionExpiry, { 
		    elapse : false, // Allow to continue after finishes
		    precision : 1000, // The update rate in milliseconds
		}).on('update.countdown', function(event) {
			jQuery(this).html(event.strftime(SESSION_TIMER_PATTERN));
		}).on('finish.countdown', function(event) {

			sessionMaxInactiveInterval = null;
			jQuery(this).html(SESSION_EXPIRED_MESSAGE);
		});
	} else {
		sessionMaxInactiveInterval = null;
	}
}

function resetSessionTimers(depth) {
	if (depth == null) {
		depth = 0;
	}
	if (sessionMaxInactiveInterval != null) {
		var sessionExpiry = (new Date()); 
		sessionExpiry.setSeconds(sessionExpiry.getSeconds() + sessionMaxInactiveInterval);
		jQuery('#session_timer').countdown(sessionExpiry); 
	}

	if (window.opener && !window.opener.closed && window.opener.resetSessionTimers && depth < 10) {
		try {
			window.opener.resetSessionTimers(depth + 1);
		} catch (e) {

		}
	}
}

function enableTabs(tabView, firstIndex, enable) {

	if (tabView && _testFunction(tabView['getLength']) && _testFunction(tabView['enable']) && _testFunction(tabView['disable'])) {
		for ( var i = (firstIndex < 0 ? 0 : firstIndex); i < tabView.getLength(); i++) {
			if (enable) {
				tabView.enable(i);
			} else {
				tabView.disable(i);
			}
		}
	}

}


function setText(id, text) {

	var element = _getElement(id);
	if (element != null) {
		element.text(text);
	}

}

function setHiddenField(targetField,value) {
	var targetFieldElement = _getElement(targetField);
	if (targetFieldElement != null) {
		targetFieldElement.val(value);
	}
}


function handleReload(xhr, status, args) {

	if (!handleSettingsChanged(args) && _testFlag(args, AJAX_OPERATION_SUCCESS)) {
		location.reload(true);
	}

}

function handleSettingsChanged(args) {
	// the settings of the active user changed: reload this window, and tell the other windows to reload:
	if (_testFlag(args, AJAX_OPERATION_SUCCESS) && _testPropertyExists(args, AJAX_SETTINGS_VERSION)) {
		_broadcastSettingsVersion(args[AJAX_SETTINGS_VERSION]);
		if (_testPropertyExists(args, AJAX_SETTINGS_RELOAD_URL) && args[AJAX_SETTINGS_RELOAD_URL]) {
			location.href = args[AJAX_SETTINGS_RELOAD_URL];
		} else {
			location.reload(true);
		}
		return true;
	}
	return false;
}

function _getSettingsVersionStorageKey() {
	return 'ctsms_settings_version' + (typeof CONTEXT_PATH !== 'undefined' && CONTEXT_PATH != null ? CONTEXT_PATH : '');
}

function _broadcastSettingsVersion(settingsVersion) {
	// the other windows of the session get a 'storage' event and reload with the new settings:
	try {
		window.localStorage.setItem(_getSettingsVersionStorageKey(), '' + settingsVersion);
	} catch (e) {
		// no local storage: the other windows are reloaded with their next request (SettingsVersionPhaseListener)
	}
}

(function() {
	try {
		window.addEventListener('storage', function(event) {
			if (event.key === _getSettingsVersionStorageKey() && event.newValue != null
					&& typeof SETTINGS_VERSION !== 'undefined' && SETTINGS_VERSION != null
					&& +event.newValue > SETTINGS_VERSION) {
				SETTINGS_VERSION = +event.newValue;
				location.reload(true);
			}
		});
	} catch (e) {

	}
})();

function handleKeepAliveCallback(xhr, status, args) {

	if (_testFlag(args, AJAX_OPERATION_SUCCESS)) {
		applyRestApiJwtFromArgs(args);
		if (_testPropertyExists(args, AJAX_KEEP_ALIVE_JS_CALLBACK)) {
			var ajaxKeepAliveJsCallback = args[AJAX_KEEP_ALIVE_JS_CALLBACK];
			if (_testFunction(window[ajaxKeepAliveJsCallback])) {
				if (_testPropertyExists(args, AJAX_KEEP_ALIVE_JS_CALLBACK_ARGS)) {
					var ajaxKeepAliveJsCallbackArgs = JSON.parse(args[AJAX_KEEP_ALIVE_JS_CALLBACK_ARGS]);
					window[ajaxKeepAliveJsCallback](ajaxKeepAliveJsCallbackArgs);
				} else {
					window[ajaxKeepAliveJsCallback]();
				}
			}
		}
	}

}

function applyRestApiJwtFromArgs(args) {
	if (args == null || !_testPropertyExists(args, AJAX_REST_API_JWT)) {
		return;
	}
	var jwt = args[AJAX_REST_API_JWT];
	if (jwt == null || jwt.length === 0) {
		return;
	}
	if (typeof REST_API_JWT !== 'undefined') {
		REST_API_JWT = jwt;
	}
	if (typeof RestApi !== 'undefined' && typeof RestApi.applySessionJwt === 'function') {
		RestApi.applySessionJwt(jwt);
	}
}

function keepAliveExec(functionName, args) {

	if (_testFunction(window[functionName])) {

		var keepAliveCallbackParams = {};
		keepAliveCallbackParams[AJAX_KEEP_ALIVE_JS_CALLBACK] = functionName;
		if (args) {
			keepAliveCallbackParams[AJAX_KEEP_ALIVE_JS_CALLBACK_ARGS] = JSON.stringify(args);
		}
		if (keepAliveCallback) {
			keepAliveCallback(prepareRemoteCommandParameters(keepAliveCallbackParams));
		}

	}

}

function prepareRemoteCommandParameters(keyValues) {

	var result = [];
	if (keyValues && typeof keyValues === "object") {
		for ( var key in keyValues) {
			var param = {};
			param.name = key;
			param.value = keyValues[key];
			result.push(param);
		}
	}
	return result;

}

function _testFlag(args, flagName) {

	if (args && flagName) {
		if (typeof args[flagName] === 'undefined' || args[flagName] == false) {
			return false;
		} else {
			return true;
		}
	}
	return false;

}

function _testPropertyExists(args, propertyName) {

	if (args && propertyName) {
		if (typeof args[propertyName] === 'undefined' || args[propertyName] == null) {
			return false;
		} else {
			return true;
		}
	}
	return false;
}

function _testFunction(func) {
	var getType = {};
	return func && getType.toString.call(func) == '[object Function]';
}

function _openTeamMemberPickerWindow(url) {

	_openPickerWindow(url, Math.min(screen.availWidth, 1280), Math.min(screen.availHeight, 768));
}
function _openSearchPickerWindow(url) {

	_openPickerWindow(url, Math.min(screen.availWidth, 1280), Math.min(screen.availHeight, 768));
}

function _openPickerWindowCallback(args) {

	var depth = 0;
	if (window.opener && !window.opener.closed) {
		var depthString = window.name.substr(pickerWindowNamePrefix.length);
		if (depthString.length > 0 && !isNaN(depthString)) {
			depth = parseInt(depthString) + 1;
		}
	}
	var pickerWindow = window.open(decodeBase64(args.url), pickerWindowNamePrefix + depth,
	        'dependent=yes,location=0,menubar=0,resizable=1,status=1,toolbar=0,titlebar=1,scrollbars=1,height=' + args.height + ',width=' + args.width);

	pickerWindow.focus();
}

function _openPickerWindow(url, width, height) {

	keepAliveExec('_openPickerWindowCallback', {
	    'url' : encodeBase64(url, false),
	    'width' : width,
	    'height' : height
	});

}

function _getPickerUrl(url, pickTargetField, onclick) {

	var uri = url + '?' + PICK_TARGET_FIELD + '=' + encodeURIComponent(pickTargetField);
	if (onclick != null && onclick.length > 0) {
		uri += '&' + PICK_ON_CLICK + '=' + encodeURIComponent(onclick);
	}
	return uri;
}

function _getInventoryPickerUrl(pickTargetField, onclick) {

	return _getPickerUrl(INVENTORY_PICKER_URL, pickTargetField, onclick);

}
function openInventoryPicker(pickTargetField, pickTargetLabel, onclick) {

	_openSearchPickerWindow(_getInventoryPickerUrl(pickTargetField, onclick) + '&' + PICK_TARGET_LABEL + '=' + encodeURIComponent(pickTargetLabel));

}
function openInventoryPickerAjax(pickTargetField, pickTargetLabel, onclick) {

	_openSearchPickerWindow(_getInventoryPickerUrl(pickTargetField, onclick) + '&' + PICK_TARGET_LABEL + '=' + encodeURIComponent(pickTargetLabel) + '&'
	        + PICK_AJAX + '=true');

}
function openInventoryPickerAjaxUpdate(pickTargetField, pickTargetLabel, update, onclick) {

	_openSearchPickerWindow(_getInventoryPickerUrl(pickTargetField, onclick) + '&' + PICK_TARGET_LABEL + '=' + encodeURIComponent(pickTargetLabel) + '&'
	        + PICK_AJAX + '=true' + '&' + PICK_AJAX_UPDATE + '=' + encodeURIComponent(update));

}
function openInventoryMultiPicker(pickTargetField, addRemoteCommand, onclick) {

	_openSearchPickerWindow(_getInventoryPickerUrl(pickTargetField, onclick) + '&' + PICK_ADD_REMOTE_COMMAND + '=' + encodeURIComponent(addRemoteCommand));

}

function _getStaffPickerUrl(pickTargetField, onclick) {

	return _getPickerUrl(STAFF_PICKER_URL, pickTargetField, onclick);

}
function openStaffPicker(pickTargetField, pickTargetLabel, onclick) {

	_openSearchPickerWindow(_getStaffPickerUrl(pickTargetField, onclick) + '&' + PICK_TARGET_LABEL + '=' + encodeURIComponent(pickTargetLabel));

}
function openStaffPickerAjax(pickTargetField, pickTargetLabel, onclick) {

	_openSearchPickerWindow(_getStaffPickerUrl(pickTargetField, onclick) + '&' + PICK_TARGET_LABEL + '=' + encodeURIComponent(pickTargetLabel) + '&'
	        + PICK_AJAX + '=true');

}
function openStaffPickerAjaxUpdate(pickTargetField, pickTargetLabel, update, onclick) {

	_openSearchPickerWindow(_getStaffPickerUrl(pickTargetField, onclick) + '&' + PICK_TARGET_LABEL + '=' + encodeURIComponent(pickTargetLabel) + '&'
	        + PICK_AJAX + '=true' + '&' + PICK_AJAX_UPDATE + '=' + encodeURIComponent(update));

}
function openStaffMultiPicker(pickTargetField, addRemoteCommand, onclick) {

	_openSearchPickerWindow(_getStaffPickerUrl(pickTargetField, onclick) + '&' + PICK_ADD_REMOTE_COMMAND + '=' + encodeURIComponent(addRemoteCommand));

}

function _getCoursePickerUrl(pickTargetField, onclick) {

	return _getPickerUrl(COURSE_PICKER_URL, pickTargetField, onclick);

}
function openCoursePicker(pickTargetField, pickTargetLabel, onclick) {

	_openSearchPickerWindow(_getCoursePickerUrl(pickTargetField, onclick) + '&' + PICK_TARGET_LABEL + '=' + encodeURIComponent(pickTargetLabel));

}
function openCoursePickerAjax(pickTargetField, pickTargetLabel, onclick) {

	_openSearchPickerWindow(_getCoursePickerUrl(pickTargetField, onclick) + '&' + PICK_TARGET_LABEL + '=' + encodeURIComponent(pickTargetLabel) + '&'
	        + PICK_AJAX + '=true');

}
function openCoursePickerAjaxUpdate(pickTargetField, pickTargetLabel, update, onclick) {

	_openSearchPickerWindow(_getCoursePickerUrl(pickTargetField, onclick) + '&' + PICK_TARGET_LABEL + '=' + encodeURIComponent(pickTargetLabel) + '&'
	        + PICK_AJAX + '=true' + '&' + PICK_AJAX_UPDATE + '=' + encodeURIComponent(update));

}
function openCourseMultiPicker(pickTargetField, addRemoteCommand, onclick) {

	_openSearchPickerWindow(_getCoursePickerUrl(pickTargetField, onclick) + '&' + PICK_ADD_REMOTE_COMMAND + '=' + encodeURIComponent(addRemoteCommand));

}

function _getUserPickerUrl(pickTargetField, onclick) {

	return _getPickerUrl(USER_PICKER_URL, pickTargetField, onclick);

}
function openUserPicker(pickTargetField, pickTargetLabel, onclick) {

	_openSearchPickerWindow(_getUserPickerUrl(pickTargetField, onclick) + '&' + PICK_TARGET_LABEL + '=' + encodeURIComponent(pickTargetLabel));

}
function openUserPickerAjax(pickTargetField, pickTargetLabel, onclick) {

	_openSearchPickerWindow(_getUserPickerUrl(pickTargetField, onclick) + '&' + PICK_TARGET_LABEL + '=' + encodeURIComponent(pickTargetLabel) + '&' + PICK_AJAX
	        + '=true');

}
function openUserPickerAjaxUpdate(pickTargetField, pickTargetLabel, update, onclick) {

	_openSearchPickerWindow(_getUserPickerUrl(pickTargetField, onclick) + '&' + PICK_TARGET_LABEL + '=' + encodeURIComponent(pickTargetLabel) + '&' + PICK_AJAX
	        + '=true' + '&' + PICK_AJAX_UPDATE + '=' + encodeURIComponent(update));

}
function openUserMultiPicker(pickTargetField, addRemoteCommand, onclick) {

	_openSearchPickerWindow(_getUserPickerUrl(pickTargetField, onclick) + '&' + PICK_ADD_REMOTE_COMMAND + '=' + encodeURIComponent(addRemoteCommand));

}

function _getTrialPickerUrl(pickTargetField, onclick) {

	return _getPickerUrl(TRIAL_PICKER_URL, pickTargetField, onclick);

}
function openTrialPicker(pickTargetField, pickTargetLabel, onclick) {

	_openSearchPickerWindow(_getTrialPickerUrl(pickTargetField, onclick) + '&' + PICK_TARGET_LABEL + '=' + encodeURIComponent(pickTargetLabel));

}
function openTrialPickerAjax(pickTargetField, pickTargetLabel, onclick) {

	_openSearchPickerWindow(_getTrialPickerUrl(pickTargetField, onclick) + '&' + PICK_TARGET_LABEL + '=' + encodeURIComponent(pickTargetLabel) + '&'
	        + PICK_AJAX + '=true');

}
function openTrialPickerAjaxUpdate(pickTargetField, pickTargetLabel, update, onclick) {

	_openSearchPickerWindow(_getTrialPickerUrl(pickTargetField, onclick) + '&' + PICK_TARGET_LABEL + '=' + encodeURIComponent(pickTargetLabel) + '&'
	        + PICK_AJAX + '=true' + '&' + PICK_AJAX_UPDATE + '=' + encodeURIComponent(update));

}
function openTrialMultiPicker(pickTargetField, addRemoteCommand, onclick) {

	_openSearchPickerWindow(_getTrialPickerUrl(pickTargetField, onclick) + '&' + PICK_ADD_REMOTE_COMMAND + '=' + encodeURIComponent(addRemoteCommand));

}

function _getProbandPickerUrl(pickTargetField, onclick) {

	return _getPickerUrl(PROBAND_PICKER_URL, pickTargetField, onclick);

}
function openProbandPicker(pickTargetField, pickTargetLabel, onclick) {

	_openSearchPickerWindow(_getProbandPickerUrl(pickTargetField, onclick) + '&' + PICK_TARGET_LABEL + '=' + encodeURIComponent(pickTargetLabel));

}
function openProbandPickerAjax(pickTargetField, pickTargetLabel, onclick) {

	_openSearchPickerWindow(_getProbandPickerUrl(pickTargetField, onclick) + '&' + PICK_TARGET_LABEL + '=' + encodeURIComponent(pickTargetLabel) + '&'
	        + PICK_AJAX + '=true');

}
function openProbandPickerAjaxUpdate(pickTargetField, pickTargetLabel, update, onclick) {

	_openSearchPickerWindow(_getProbandPickerUrl(pickTargetField, onclick) + '&' + PICK_TARGET_LABEL + '=' + encodeURIComponent(pickTargetLabel) + '&'
	        + PICK_AJAX + '=true' + '&' + PICK_AJAX_UPDATE + '=' + encodeURIComponent(update));

}
function openProbandMultiPicker(pickTargetField, addRemoteCommand, onclick) {

	_openSearchPickerWindow(_getProbandPickerUrl(pickTargetField, onclick) + '&' + PICK_ADD_REMOTE_COMMAND + '=' + encodeURIComponent(addRemoteCommand));

}

function _getInputFieldPickerUrl(pickTargetField, onclick) {

	return _getPickerUrl(INPUT_FIELD_PICKER_URL, pickTargetField, onclick);

}
function openInputFieldPicker(pickTargetField, pickTargetLabel, onclick) {

	_openSearchPickerWindow(_getInputFieldPickerUrl(pickTargetField, onclick) + '&' + PICK_TARGET_LABEL + '=' + encodeURIComponent(pickTargetLabel));

}
function openInputFieldPickerAjax(pickTargetField, pickTargetLabel, onclick) {

	_openSearchPickerWindow(_getInputFieldPickerUrl(pickTargetField, onclick) + '&' + PICK_TARGET_LABEL + '=' + encodeURIComponent(pickTargetLabel) + '&'
	        + PICK_AJAX + '=true');

}
function openInputFieldPickerAjaxUpdate(pickTargetField, pickTargetLabel, update, onclick) {

	_openSearchPickerWindow(_getInputFieldPickerUrl(pickTargetField, onclick) + '&' + PICK_TARGET_LABEL + '=' + encodeURIComponent(pickTargetLabel) + '&'
	        + PICK_AJAX + '=true' + '&' + PICK_AJAX_UPDATE + '=' + encodeURIComponent(update));

}
function openInputFieldMultiPicker(pickTargetField, addRemoteCommand, onclick) {

	_openSearchPickerWindow(_getInputFieldPickerUrl(pickTargetField, onclick) + '&' + PICK_ADD_REMOTE_COMMAND + '=' + encodeURIComponent(addRemoteCommand));

}


function _getMassMailPickerUrl(pickTargetField, onclick) {

	return _getPickerUrl(MASS_MAIL_PICKER_URL, pickTargetField, onclick);

}
function openMassMailPicker(pickTargetField, pickTargetLabel, onclick) {

	_openSearchPickerWindow(_getMassMailPickerUrl(pickTargetField, onclick) + '&' + PICK_TARGET_LABEL + '=' + encodeURIComponent(pickTargetLabel));

}
function openMassMailPickerAjax(pickTargetField, pickTargetLabel, onclick) {

	_openSearchPickerWindow(_getMassMailPickerUrl(pickTargetField, onclick) + '&' + PICK_TARGET_LABEL + '=' + encodeURIComponent(pickTargetLabel) + '&'
	        + PICK_AJAX + '=true');

}
function openMassMailPickerAjaxUpdate(pickTargetField, pickTargetLabel, update, onclick) {

	_openSearchPickerWindow(_getMassMailPickerUrl(pickTargetField, onclick) + '&' + PICK_TARGET_LABEL + '=' + encodeURIComponent(pickTargetLabel) + '&'
	        + PICK_AJAX + '=true' + '&' + PICK_AJAX_UPDATE + '=' + encodeURIComponent(update));

}
function openMassMailMultiPicker(pickTargetField, addRemoteCommand, onclick) {

	_openSearchPickerWindow(_getMassMailPickerUrl(pickTargetField, onclick) + '&' + PICK_ADD_REMOTE_COMMAND + '=' + encodeURIComponent(addRemoteCommand));

}


function _getTeamMemberPickerUrl(trialId, start, stop, pickTargetField, onclick) {

	var uri = TEAM_MEMBER_PICKER_URL + '?' + TRIAL_ID + '=' + encodeURIComponent(trialId) + '&';
	if (start != null) {
		uri += START + '=' + encodeURIComponent(start.getTime()) + '&';
	}
	if (stop != null) {
		uri += STOP + '=' + encodeURIComponent(stop.getTime()) + '&';
	}
	uri += PICK_TARGET_FIELD + '=' + encodeURIComponent(pickTargetField);
	if (onclick != null && onclick.length > 0) {
		uri += '&' + PICK_ON_CLICK + '=' + encodeURIComponent(onclick);
	}
	return uri;

}

function openTeamMemberPicker(trialId, start, stop, pickTargetField, pickTargetLabel, onclick) {

	_openTeamMemberPickerWindow(_getTeamMemberPickerUrl(trialId, start, stop, pickTargetField, onclick) + '&' + PICK_TARGET_LABEL + '='
	        + encodeURIComponent(pickTargetLabel));

}
function openTrialTeamMemberPicker(trialTargetField, start, stop, pickTargetField, pickTargetLabel, onclick) {
	var trialTargetFieldElement = _getElement(trialTargetField);
	if (trialTargetFieldElement != null) {
		openTeamMemberPicker(trialTargetFieldElement.val(), start, stop, pickTargetField, pickTargetLabel, onclick);
	}
}
function openTeamMemberPickerAjax(trialId, start, stop, pickTargetField, pickTargetLabel, onclick) {

	_openTeamMemberPickerWindow(_getTeamMemberPickerUrl(trialId, start, stop, pickTargetField, onclick) + '&' + PICK_TARGET_LABEL + '='
	        + encodeURIComponent(pickTargetLabel) + '&' + PICK_AJAX + '=true');

}
function openTrialTeamMemberPickerAjax(trialTargetField, start, stop, pickTargetField, pickTargetLabel, onclick) {
	var trialTargetFieldElement = _getElement(trialTargetField);
	if (trialTargetFieldElement != null) {
		openTeamMemberPickerAjax(trialTargetFieldElement.val(), start, stop, pickTargetField, pickTargetLabel, onclick);
	}
}
function openTeamMemberPickerAjaxUpdate(trialId, start, stop, pickTargetField, pickTargetLabel, update, onclick) {

	_openTeamMemberPickerWindow(_getTeamMemberPickerUrl(trialId, start, stop, pickTargetField, onclick) + '&' + PICK_TARGET_LABEL + '='
	        + encodeURIComponent(pickTargetLabel) + '&' + PICK_AJAX + '=true' + '&' + PICK_AJAX_UPDATE + '=' + encodeURIComponent(update));

}
function openTrialTeamMemberPickerAjaxUpdate(trialTargetField, start, stop, pickTargetField, pickTargetLabel, update, onclick) {

	var trialTargetFieldElement = _getElement(trialTargetField);
	if (trialTargetFieldElement != null) {
		openTeamMemberPickerAjaxUpdate(trialTargetFieldElement.val(), start, stop, pickTargetField, pickTargetLabel, update, onclick);
	}

}

function openTeamMemberMultiPicker(trialId, start, stop, pickTargetField, addRemoteCommand, onclick) {

	_openTeamMemberPickerWindow(_getTeamMemberPickerUrl(trialId, start, stop, pickTargetField, onclick) + '&' + PICK_ADD_REMOTE_COMMAND + '='
	        + encodeURIComponent(addRemoteCommand));

}
function openTrialTeamMemberMultiPicker(trialTargetField, start, stop, pickTargetField, addRemoteCommand, onclick) {

	var trialTargetFieldElement = _getElement(trialTargetField);
	if (trialTargetFieldElement != null) {
		openTeamMemberMultiPicker(trialTargetFieldElement.val(), start, stop, pickTargetField, addRemoteCommand, onclick);
	}

}

// ext: the ext argument a pe:javascript behavior passes to its execute function. A dropdown's
// change behavior gets an ext.oncomplete there, which releases its reopen guard (see
// primefacesOverrides.js), so it must be called when the request completed.
function ajaxRequest(source, process, update, oncomplete, ext) {
	var ajaxCfg = {
	    source : _stripLeadingColons(source),
	    process : _stripLeadingColons(process),
	    event : 'valueChange'
	};
	if (update != null && update.length > 0) {
		ajaxCfg['update'] = _stripLeadingColons(update);
	}
	var extOncomplete = (ext != null && _testFunction(ext.oncomplete)) ? ext.oncomplete : null;
	if (_testFunction(window[oncomplete]) || extOncomplete != null) {
		ajaxCfg['oncomplete'] = function(xhr, status, args) {
			if (_testFunction(window[oncomplete])) {
				window[oncomplete](xhr, status, null);
			}
			if (extOncomplete != null) {
				extOncomplete.call(this, xhr, status, args);
			}
		};
	}
	setSupressWait();
	PrimeFaces.ajax.AjaxRequest(ajaxCfg);
}

function _getElement(pickTargetField) {

	if (pickTargetField != null && pickTargetField.length > 0) {
		var targetField = jQuery(PrimeFaces.escapeClientId(_stripLeadingColons(pickTargetField)));
		if (typeof targetField !== 'undefined' && targetField != null && targetField.length) {
			return targetField;
		}
	}
	return null;

}

function _stripLeadingColons(id) { // pf 3.3
	if (id != null) {
		var result = [];
		var ids = id.split(/[,\s]+/g);
		for ( var i = 0; i < ids.length; i++) {
			if (ids[i].startsWith(":")) {
				result.push(ids[i].substr(1));
			} else if (ids[i].length > 0) {
				result.push(ids[i]);
			}
		}
		return result.join(' ');
	}
	return id;
}

function _setPickedID(isPickerWindow, pickTargetField, pickTargetLabel, ID, label, ajax, update, onclick) {

	if (isPickerWindow) {
		if (window.opener && !window.opener.closed) {
			window.opener._setPickedID(false, pickTargetField, pickTargetLabel, ID, label, ajax, update, onclick);

		}
		self.close();
	} else {

		var pickTargetFieldElement = _getElement(pickTargetField);
		if (pickTargetFieldElement != null) {
			pickTargetFieldElement.val(ID);
			if (ajax) {
				ajaxRequest(pickTargetField, pickTargetField, update, null);

			}
			if (_testFunction(window[onclick])) {
				window[onclick]();
			}
		}

		var pickTargetLabelElement = _getElement(pickTargetLabel);
		if (pickTargetLabelElement != null) {
			var textElement = pickTargetLabelElement.contents()[1];

			if (typeof textElement.textContent !== 'undefined') {
				textElement.textContent = label;
			} else {
				textElement.innerText = label;
			}
		}

	}

}

function _addPickedID(isPickerWindow, pickTargetField, IDs, addRemoteCommand, onclick) {

	if (isPickerWindow) {
		if (window.opener && !window.opener.closed) {
			window.opener._addPickedID(false, pickTargetField, IDs, addRemoteCommand, onclick);
		} else {
			self.close();
		}
	} else {
		var pickTargetFieldElement = _getElement(pickTargetField);
		if (pickTargetFieldElement != null) {
			pickTargetFieldElement.val(IDs);
			if (_testFunction(window[addRemoteCommand])) {
				window[addRemoteCommand]();
			}
			if (_testFunction(window[onclick])) {
				window[onclick]();
			}
		}

	}

}

function setPickedID(pickTargetField, ID, pickTargetLabel, label, onclick) {

	_setPickedID(true, pickTargetField, pickTargetLabel, ID, label, false, null, onclick);

}
function setPickedIDAjax(pickTargetField, ID, pickTargetLabel, label, onclick) {

	_setPickedID(true, pickTargetField, pickTargetLabel, ID, label, true, null, onclick);

}
function setPickedIDAjaxUpdate(pickTargetField, ID, pickTargetLabel, label, update, onclick) {

	_setPickedID(true, pickTargetField, pickTargetLabel, ID, label, true, update, onclick);

}

function setID(pickTargetField, ID, pickTargetLabel, label, onclick) {

	_setPickedID(false, pickTargetField, pickTargetLabel, ID, label, false, null, onclick);

}
function setIDAjax(pickTargetField, ID, pickTargetLabel, label, onclick) {

	_setPickedID(false, pickTargetField, pickTargetLabel, ID, label, true, null, onclick);

}
function setIDAjaxUpdate(pickTargetField, ID, pickTargetLabel, label, update, onclick) {

	_setPickedID(false, pickTargetField, pickTargetLabel, ID, label, true, update, onclick);

}

function clearID(pickTargetField, pickTargetLabel, noSelectionLabel, onclick) {

	_setPickedID(false, pickTargetField, pickTargetLabel, NO_SELECTION_VALUE, noSelectionLabel, false, null, onclick);

}
function clearIDAjax(pickTargetField, pickTargetLabel, noSelectionLabel, onclick) {

	_setPickedID(false, pickTargetField, pickTargetLabel, NO_SELECTION_VALUE, noSelectionLabel, true, null, onclick);

}
function clearIDAjaxUpdate(pickTargetField, pickTargetLabel, noSelectionLabel, update, onclick) {

	_setPickedID(false, pickTargetField, pickTargetLabel, NO_SELECTION_VALUE, noSelectionLabel, true, update, onclick);

}

function addPickedID(pickTargetField, IDs, addRemoteCommand, onclick) {

	_addPickedID(true, pickTargetField, IDs, addRemoteCommand, onclick);

}

function handlePickCurrentPage(xhr, status, args) {

	if (_testPropertyExists(args, PICK_TARGET_FIELD) && _testPropertyExists(args, PICK_CURRENT_PAGE_IDS) && _testPropertyExists(args, PICK_ADD_REMOTE_COMMAND)) {
		_addPickedID(true, args[PICK_TARGET_FIELD], args[PICK_CURRENT_PAGE_IDS], args[PICK_ADD_REMOTE_COMMAND], args[PICK_ON_CLICK]);
	}

}

function _openEntityCallback(args) {

	var openerWindow = window.open(decodeBase64(args.url), args.window);
	openerWindow.focus();

}

function _openEntity(url, window) {

	keepAliveExec('_openEntityCallback', {
	    'url' : encodeBase64(url, false),
	    'window' : window
	});

}

function getWindowNameUniqueToken() {
	return '_' + (new Date()).getTime();
}

function openPickedInventory(pickTargetField) {
	var pickTargetFieldElement = _getElement(pickTargetField);
	if (pickTargetFieldElement != null) {
		openInventory(pickTargetFieldElement.val());
	}
}

function openInventory(inventoryId) {

	if (typeof inventoryId !== 'undefined' && inventoryId) {
		_openEntity(INVENTORY_URL + '?' + INVENTORY_ID + '=' + encodeURIComponent(inventoryId), sprintf(INVENTORY_ENTITY_WINDOW_NAME, inventoryId, getWindowNameUniqueToken()));
	}

}
function openNewInventory() {

	_openEntity(INVENTORY_URL, sprintf(INVENTORY_ENTITY_WINDOW_NAME, NEW_ENTITY_WINDOW_NAME_SUFFIX, ""));

}

function openPickedStaff(pickTargetField) {
	var pickTargetFieldElement = _getElement(pickTargetField);
	if (pickTargetFieldElement != null) {
		openStaff(pickTargetFieldElement.val());
	}
}
function openStaff(staffId) {

	if (typeof staffId !== 'undefined' && staffId) {
		_openEntity(STAFF_URL + '?' + STAFF_ID + '=' + encodeURIComponent(staffId), sprintf(STAFF_ENTITY_WINDOW_NAME, staffId, getWindowNameUniqueToken()));
	}

}
function openNewStaff() {

	_openEntity(STAFF_URL, sprintf(STAFF_ENTITY_WINDOW_NAME, NEW_ENTITY_WINDOW_NAME_SUFFIX, ""));

}
function openPickedCourse(pickTargetField) {
	var pickTargetFieldElement = _getElement(pickTargetField);
	if (pickTargetFieldElement != null) {
		openCourse(pickTargetFieldElement.val());
	}
}
function openCourse(courseId) {

	if (typeof courseId !== 'undefined' && courseId) {
		_openEntity(COURSE_URL + '?' + COURSE_ID + '=' + encodeURIComponent(courseId), sprintf(COURSE_ENTITY_WINDOW_NAME, courseId, getWindowNameUniqueToken()));
	}

}
function openNewCourse() {

	_openEntity(COURSE_URL, sprintf(COURSE_ENTITY_WINDOW_NAME, NEW_ENTITY_WINDOW_NAME_SUFFIX, ""));

}
function openPickedUser(pickTargetField) {
	var pickTargetFieldElement = _getElement(pickTargetField);
	if (pickTargetFieldElement != null) {
		openUser(pickTargetFieldElement.val());
	}
}
function openUser(userId) {

	if (typeof userId !== 'undefined' && userId) {
		_openEntity(USER_URL + '?' + USER_ID + '=' + encodeURIComponent(userId), sprintf(USER_ENTITY_WINDOW_NAME, userId, getWindowNameUniqueToken()));
	}

}
function openNewUser() {

	_openEntity(USER_URL, sprintf(USER_ENTITY_WINDOW_NAME, NEW_ENTITY_WINDOW_NAME_SUFFIX, ""));

}
function openPickedTrial(pickTargetField) {
	var pickTargetFieldElement = _getElement(pickTargetField);
	if (pickTargetFieldElement != null) {
		openTrial(pickTargetFieldElement.val());
	}
}
function openTrial(trialId) {

	if (typeof trialId !== 'undefined' && trialId) {
		_openEntity(TRIAL_URL + '?' + TRIAL_ID + '=' + encodeURIComponent(trialId), sprintf(TRIAL_ENTITY_WINDOW_NAME, trialId, getWindowNameUniqueToken()));
	}

}
function openNewTrial() {

	_openEntity(TRIAL_URL, sprintf(TRIAL_ENTITY_WINDOW_NAME, NEW_ENTITY_WINDOW_NAME_SUFFIX, ""));

}
function openPickedProband(pickTargetField) {
	var pickTargetFieldElement = _getElement(pickTargetField);
	if (pickTargetFieldElement != null) {
		openProband(pickTargetFieldElement.val());
	}
}
function openProband(probandId) {

	if (typeof probandId !== 'undefined' && probandId) {
		_openEntity(PROBAND_URL + '?' + PROBAND_ID + '=' + encodeURIComponent(probandId), sprintf(PROBAND_ENTITY_WINDOW_NAME, probandId, getWindowNameUniqueToken()));
	}

}
function openNewProband() {

	_openEntity(PROBAND_URL, sprintf(PROBAND_ENTITY_WINDOW_NAME, NEW_ENTITY_WINDOW_NAME_SUFFIX, ""));

}
function openPickedInputField(pickTargetField) {
	var pickTargetFieldElement = _getElement(pickTargetField);
	if (pickTargetFieldElement != null) {
		openInputField(pickTargetFieldElement.val());
	}
}
function openInputField(inputFieldId) {

	if (typeof inputFieldId !== 'undefined' && inputFieldId) {
		_openEntity(INPUT_FIELD_URL + '?' + INPUT_FIELD_ID + '=' + encodeURIComponent(inputFieldId), sprintf(INPUT_FIELD_ENTITY_WINDOW_NAME, inputFieldId, getWindowNameUniqueToken()));
	}

}
function openNewInputField() {

	_openEntity(INPUT_FIELD_URL, sprintf(INPUT_FIELD_ENTITY_WINDOW_NAME, NEW_ENTITY_WINDOW_NAME_SUFFIX, ""));

}


function openPickedMassMail(pickTargetField) {
	var pickTargetFieldElement = _getElement(pickTargetField);
	if (pickTargetFieldElement != null) {
		openMassMail(pickTargetFieldElement.val());
	}
}
function openMassMail(massMailId) {

	if (typeof massMailId !== 'undefined' && massMailId) {
		_openEntity(MASS_MAIL_URL + '?' + MASS_MAIL_ID + '=' + encodeURIComponent(massMailId), sprintf(MASS_MAIL_ENTITY_WINDOW_NAME, massMailId, getWindowNameUniqueToken()));
	}

}
function openNewMassMail() {

	_openEntity(MASS_MAIL_URL, sprintf(MASS_MAIL_ENTITY_WINDOW_NAME, NEW_ENTITY_WINDOW_NAME_SUFFIX, ""));

}


function _openHomeCallback(args) {

	var homeWindow = window.open(decodeBase64(args.url), args.homeWindowName);
	homeWindow.focus();

}

function _getHomeViewName(url) {
	if (url) {
		var lastSlash = url.lastIndexOf("/");
		var queryBegin = url.indexOf("?");
		var name;
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
				name = url;
			}
		}
		var extBegin = name.indexOf(".");
		if (extBegin >= 0) {
			name = name.substring(0, extBegin);
		}
		return name.toLowerCase();
	}
	return url;
}

function _openHome(url, homeWindowName) {


	keepAliveExec('_openHomeCallback', {
	    'url' : encodeBase64(url, false),
	    'homeWindowName' : sprintf(homeWindowName,_getHomeViewName(url),getWindowNameUniqueToken())
	});

}

function openInventoryHome() {

	_openHome(INVENTORY_START_URL, INVENTORY_HOME_WINDOW_NAME);

}
function openStaffHome() {

	_openHome(STAFF_START_URL, STAFF_HOME_WINDOW_NAME);

}
function openCourseHome() {

	_openHome(COURSE_START_URL, COURSE_HOME_WINDOW_NAME);

}
function openUserHome() {

	_openHome(USER_START_URL, USER_HOME_WINDOW_NAME);

}
function openTrialHome() {

	_openHome(TRIAL_START_URL, TRIAL_HOME_WINDOW_NAME);

}
function openDutyRosterSchedule() {

	_openHome(DUTY_ROSTER_SCHEDULE_URL, TRIAL_HOME_WINDOW_NAME);

}

function openProbandHome() {

	_openHome(PROBAND_START_URL, PROBAND_HOME_WINDOW_NAME);

}
function openInputFieldHome() {

	_openHome(INPUT_FIELD_START_URL, INPUT_FIELD_HOME_WINDOW_NAME);

}

function openMassMailHome() {

	_openHome(MASS_MAIL_START_URL, MASS_MAIL_HOME_WINDOW_NAME);

}

function openChangePassword() {

	_openHome(CHANGE_PASSWORD_URL, USER_HOME_WINDOW_NAME);

}

function openChangeSettings() {

	_openHome(CHANGE_SETTINGS_URL, USER_HOME_WINDOW_NAME);

}

function openUpcomigCourseOverview() {

	_openHome(UPCOMING_COURSE_OVERVIEW_URL, STAFF_HOME_WINDOW_NAME);

}

function openRecipientOverview() {

	_openHome(RECIPIENT_OVERVIEW_URL, MASS_MAIL_HOME_WINDOW_NAME);

}

function openInventorySearch(criteriaId) {

	if (typeof criteriaId !== 'undefined' && criteriaId) {
		_openEntity(INVENTORY_SEARCH_URL + '?' + CRITERIA_ID + '=' + encodeURIComponent(criteriaId), INVENTORY_HOME_WINDOW_NAME);
	} else {
		_openHome(INVENTORY_SEARCH_URL, INVENTORY_HOME_WINDOW_NAME);
	}

}
function openStaffSearch(criteriaId) {

	if (typeof criteriaId !== 'undefined' && criteriaId) {
		_openEntity(STAFF_SEARCH_URL + '?' + CRITERIA_ID + '=' + encodeURIComponent(criteriaId), STAFF_HOME_WINDOW_NAME);
	} else {
		_openHome(STAFF_SEARCH_URL, STAFF_HOME_WINDOW_NAME);
	}

}
function openCourseSearch(criteriaId) {

	if (typeof criteriaId !== 'undefined' && criteriaId) {
		_openEntity(COURSE_SEARCH_URL + '?' + CRITERIA_ID + '=' + encodeURIComponent(criteriaId), COURSE_HOME_WINDOW_NAME);
	} else {
		_openHome(COURSE_SEARCH_URL, COURSE_HOME_WINDOW_NAME);
	}

}
function openTrialSearch(criteriaId) {

	if (typeof criteriaId !== 'undefined' && criteriaId) {
		_openEntity(TRIAL_SEARCH_URL + '?' + CRITERIA_ID + '=' + encodeURIComponent(criteriaId), TRIAL_HOME_WINDOW_NAME);
	} else {
		_openHome(TRIAL_SEARCH_URL, TRIAL_HOME_WINDOW_NAME);
	}

}
function openProbandSearch(criteriaId) {

	if (typeof criteriaId !== 'undefined' && criteriaId) {
		_openEntity(PROBAND_SEARCH_URL + '?' + CRITERIA_ID + '=' + encodeURIComponent(criteriaId), PROBAND_HOME_WINDOW_NAME);
	} else {
		_openHome(PROBAND_SEARCH_URL, PROBAND_HOME_WINDOW_NAME);
	}

}
function openUserSearch(criteriaId) {

	if (typeof criteriaId !== 'undefined' && criteriaId) {
		_openEntity(USER_SEARCH_URL + '?' + CRITERIA_ID + '=' + encodeURIComponent(criteriaId), USER_HOME_WINDOW_NAME);
	} else {
		_openHome(USER_SEARCH_URL, USER_HOME_WINDOW_NAME);
	}

}
function openInputFieldSearch(criteriaId) {

	if (typeof criteriaId !== 'undefined' && criteriaId) {
		_openEntity(INPUT_FIELD_SEARCH_URL + '?' + CRITERIA_ID + '=' + encodeURIComponent(criteriaId), INPUT_FIELD_HOME_WINDOW_NAME);
	} else {
		_openHome(INPUT_FIELD_SEARCH_URL, INPUT_FIELD_HOME_WINDOW_NAME);
	}

}

function openMassMailSearch(criteriaId) {

	if (typeof criteriaId !== 'undefined' && criteriaId) {
		_openEntity(MASS_MAIL_SEARCH_URL + '?' + CRITERIA_ID + '=' + encodeURIComponent(criteriaId), MASS_MAIL_HOME_WINDOW_NAME);
	} else {
		_openHome(MASS_MAIL_SEARCH_URL, MASS_MAIL_HOME_WINDOW_NAME);
	}

}

function openInventoryStatusOverview() {

	_openHome(INVENTORY_STATUS_OVERVIEW_URL, INVENTORY_HOME_WINDOW_NAME);

}

function openInventoryMaintenanceOverview() {

	_openHome(INVENTORY_MAINTENANCE_OVERVIEW_URL, INVENTORY_HOME_WINDOW_NAME);

}

function openInventoryBookingSchedule() {

	_openHome(INVENTORY_BOOKING_SCHEDULE_URL, INVENTORY_HOME_WINDOW_NAME);

}

function openBookingSummaryOverview() {

	_openHome(INVENTORY_BOOKING_SUMMARY_OVERVIEW_URL, INVENTORY_HOME_WINDOW_NAME);

}

function openStaffStatusOverview() {

	_openHome(STAFF_STATUS_OVERVIEW_URL, STAFF_HOME_WINDOW_NAME);

}

function openStaffShiftSummaryOverview() {

	_openHome(STAFF_SHIFT_SUMMARY_OVERVIEW_URL, STAFF_HOME_WINDOW_NAME);

}

function openUpcomingCourseOverview() {

	_openHome(UPCOMING_COURSE_OVERVIEW_URL, STAFF_HOME_WINDOW_NAME);

}

function openExpiringParticipationOverview() {

	_openHome(EXPIRING_PARTICIPATION_OVERVIEW_URL, STAFF_HOME_WINDOW_NAME);

}

function openAdminUpcomingCourseOverview() {

	_openHome(ADMIN_UPCOMING_COURSE_OVERVIEW_URL, COURSE_HOME_WINDOW_NAME);

}

function openExpiringCourseOverview() {

	_openHome(EXPIRING_COURSE_OVERVIEW_URL, COURSE_HOME_WINDOW_NAME);

}

function openAdminExpiringParticipationOverview() {

	_openHome(ADMIN_EXPIRING_PARTICIPATION_OVERVIEW_URL, COURSE_HOME_WINDOW_NAME);

}

function openTimelineEventOverview() {

	_openHome(TIMELINE_EVENT_OVERVIEW_URL, TRIAL_HOME_WINDOW_NAME);

}

function openMoneyTransferOverview() {

	_openHome(MONEY_TRANSFER_OVERVIEW_URL, TRIAL_HOME_WINDOW_NAME);

}

function openEcrfProgressOverview() {

	_openHome(ECRF_PROGRESS_OVERVIEW_URL, TRIAL_HOME_WINDOW_NAME);

}

function openRandomizationCodeOverview() {

	_openHome(RANDOMIZATION_CODE_OVERVIEW_URL, TRIAL_HOME_WINDOW_NAME);

}

function openTrialShiftSummaryOverview() {

	_openHome(TRIAL_SHIFT_SUMMARY_OVERVIEW_URL, TRIAL_HOME_WINDOW_NAME);

}

function openTrialTimeline() {

	_openHome(TRIAL_TIMELINE_URL, TRIAL_HOME_WINDOW_NAME);

}

function openProbandStatusOverview() {

	_openHome(PROBAND_STATUS_OVERVIEW_URL, PROBAND_HOME_WINDOW_NAME);

}

function openAutoDeletionProbandOverview() {

	_openHome(AUTO_DELETION_PROBAND_OVERVIEW_URL, PROBAND_HOME_WINDOW_NAME);

}

function _parseReferrer(url) {
	if (!url) {
		return null;
	} else {
		var referer = document.createElement('a');
		referer.href = url;
		if (referer && (referer.protocol == 'http:' || referer.protocol == 'https:') && jQuery.inArray(referer.hostname, TRUSTED_REFERER_HOSTS) >= 0) {
			var pathname = _prependSlash(referer.pathname);
			// stay inside the context, and never end up with a protocol-relative "//host/..." url:
			if (pathname == CONTEXT_PATH || (pathname.indexOf(CONTEXT_PATH + '/') == 0 && pathname.indexOf(CONTEXT_PATH + '//') != 0)) {
				return referer;
			}
		}
		return null;
	}
}

function _isTrustedReferrer(url) {
	return _parseReferrer(url) != null;
}

function _prependSlash(pathname) { // IE ...
	if (pathname && pathname.length > 0) {
		if (pathname.indexOf('/') != 0) {
			return '/' + pathname;
		}
	}
	return pathname;
}

function openReferer(open, windowMessage, popupMessage) {
	if (open && REFERER_URL_BASE64.length > 0) {
		if (window.name.indexOf(pickerWindowNamePrefix) == 0) {
			if (popupMessage != null && popupMessage.length > 0) {
				alert(popupMessage);
			}
			window.close();

		} else {
			if (windowMessage != null && windowMessage.length > 0) {
				alert(windowMessage);
			}
			var referer = _parseReferrer(decodeBase64(REFERER_URL_BASE64));
			if (referer != null && _prependSlash(referer.pathname) != LOGIN_URL) {
				// same origin, only the path and query of the referer are taken:
				_redirect(_prependSlash(referer.pathname) + referer.search);
			} else {
				_redirect(PORTAL_URL);
			}
		}
	}
}

function _setSearchParam(search, name, value) {
	var params = (search.indexOf('?') == 0 ? search.substring(1) : search).split('&');
	var result = [];
	for (var i = 0; i < params.length; i++) {
		if (params[i].length > 0 && params[i].split('=')[0] != name) {
			result.push(params[i]);
		}
	}
	if (value != null) {
		result.push(name + '=' + encodeURIComponent(value));
	}
	return result.length > 0 ? '?' + result.join('&') : '';
}

function storeTabIndexParam(index) {
	// keep the shown tab in the url, so reloading or restoring the page after re-login opens it again:
	if (window.history && window.history.replaceState) {
		try {
			window.history.replaceState(window.history.state, '', location.pathname + _setSearchParam(location.search, TAB, index > 0 ? index : null) + location.hash);
		} catch (e) {

		}
	}
}

var _buildTabWidgetVar = null;
var _sendingBuildTab = null;

function _getBuildTab(source) {
	// the tab holding the source of the request, else the shown tab:
	var tabView = _buildTabWidgetVar != null ? window[_buildTabWidgetVar] : null;
	if (tabView && tabView.panelContainer) {
		var panels = tabView.panelContainer.children();
		var panel = null;
		if (source != null) {
			try {
				panel = (typeof source == 'string' ? jQuery(PrimeFaces.escapeClientId(source)) : jQuery(source)).closest(panels);
			} catch (e) {
				panel = null;
			}
		}
		if (panel == null || panel.length == 0) {
			panel = panels.eq(tabView.getActiveIndex());
		}
		var id = panel.attr('id');
		if (id) {
			return id.substring(id.lastIndexOf(':') + 1);
		}
	}
	return null;
}

function sendBuildTabParam(tabViewWidgetVar) {
	// the server builds only the shown tab into the component tree (ApplicationScopeBean.isTabBuilt),
	// so every postback of the page tells which tab it belongs to. without the parameter, all tabs are built.
	if (_buildTabWidgetVar != null) {
		_buildTabWidgetVar = tabViewWidgetVar;
		return;
	}
	_buildTabWidgetVar = tabViewWidgetVar;
	// take the tab when a request is created, not when it is sent: a request can wait in the queue
	// while another tab is selected, and still needs the components of its own tab.
	var ajaxRequest = PrimeFaces.ajax.AjaxRequest;
	PrimeFaces.ajax.AjaxRequest = function(cfg, ext) {
		if (cfg && cfg.buildTab === undefined) {
			cfg.buildTab = _getBuildTab(cfg.source);
		}
		return ajaxRequest.apply(this, arguments);
	};
	var send = PrimeFaces.ajax.AjaxUtils.send;
	PrimeFaces.ajax.AjaxUtils.send = function(cfg) {
		_sendingBuildTab = (cfg && cfg.buildTab != null) ? cfg.buildTab : null;
		try {
			return send.apply(this, arguments);
		} finally {
			_sendingBuildTab = null;
		}
	};
	jQuery.ajaxPrefilter(function(options) {
		var tab = _sendingBuildTab != null ? _sendingBuildTab : _getBuildTab();
		if (tab != null && options.url && options.type && options.type.toUpperCase() == 'POST') {
			options.url = options.url + (options.url.indexOf('?') < 0 ? '?' : '&') + BUILD_TAB + '=' + encodeURIComponent(tab);
		}
	});
	jQuery(document).on('submit', 'form', function() {
		// non-ajax submits, e.g. file downloads:
		var tab = _getBuildTab(this);
		if (tab != null) {
			var input = jQuery('<input type="hidden"/>').attr('name', BUILD_TAB).val(tab);
			jQuery(this).append(input);
			setTimeout(function() {
				input.remove();
			}, 0);
		}
	});
}

function restoreTabIndexParam(tabView) {
	var match = new RegExp('[?&]' + TAB + '=(\\d+)(&|$)').exec(location.search);
	if (match && tabView) {
		var index = reverseTabIndex(parseInt(match[1], 10));
		if (index != null && index > 0 && index < tabView.getLength() && !tabView.navContainer.children().eq(index).hasClass('ui-state-disabled')) {
			tabView.select(index);
		}
	}
}

function _openPortalCallback() {

	var portalWindow = window.open(PORTAL_URL, PORTAL_WINDOW_NAME);
	portalWindow.focus();

}

function openPortal() {

	keepAliveExec('_openPortalCallback');

}

function _openEcrfSectionCallback(args) {

	var openerWindow = window.open(decodeBase64(args.url), args.window,
			'dependent=yes,location=0,menubar=0,resizable=1,status=1,toolbar=0,titlebar=1,scrollbars=1,height=' + args.height + ',width=' + args.width);
	openerWindow.focus();

}

function openEcrfSection(ecrfFieldStatusEntryId, ecrfSectionHashCode) {

	if (typeof ecrfFieldStatusEntryId !== 'undefined' && ecrfFieldStatusEntryId) {
		if (ecrfSectionHashCode == null) {
			ecrfSectionHashCode = ecrfFieldStatusEntryId;
		}
		keepAliveExec('_openEcrfSectionCallback', {
			'url' : encodeBase64(ECRF_SECTION_URL + '?' + ECRF_FIELD_STATUS_ENTRY_ID + '=' + encodeURIComponent(ecrfFieldStatusEntryId), false),
		    'window' : sprintf(ECRF_SECTION_WINDOW_NAME, ecrfSectionHashCode, getWindowNameUniqueToken()),
		    'width' : Math.min(screen.availWidth, 1280),
		    'height' : Math.min(screen.availHeight, 768)
		});
	}

}

function handleUpdateAuditTrailTabTitles(xhr, status, args) {

	if (_testFlag(args, AJAX_OPERATION_SUCCESS) && _testPropertyExists(args, AJAX_VALIDATION_ECRF_FIELD_STATUS_ENTRY_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_VALIDATION_ECRF_FIELD_STATUS_ENTRY_COUNT)) {
		auditTrailTabView.setTabTitle(1, decodeBase64(args[AJAX_VALIDATION_ECRF_FIELD_STATUS_ENTRY_TAB_TITLE_BASE64]));
		auditTrailTabView.emphasizeTab(1, args[AJAX_VALIDATION_ECRF_FIELD_STATUS_ENTRY_COUNT] == 0);
	}
	if (_testFlag(args, AJAX_OPERATION_SUCCESS) && _testPropertyExists(args, AJAX_QUERY_ECRF_FIELD_STATUS_ENTRY_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_QUERY_ECRF_FIELD_STATUS_ENTRY_COUNT)) {
		auditTrailTabView.setTabTitle(2, decodeBase64(args[AJAX_QUERY_ECRF_FIELD_STATUS_ENTRY_TAB_TITLE_BASE64]));
		auditTrailTabView.emphasizeTab(2, args[AJAX_QUERY_ECRF_FIELD_STATUS_ENTRY_COUNT] == 0);
	}
	if (_testFlag(args, AJAX_OPERATION_SUCCESS) && _testPropertyExists(args, AJAX_ANNOTATION_ECRF_FIELD_STATUS_ENTRY_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_ANNOTATION_ECRF_FIELD_STATUS_ENTRY_COUNT)) {
		auditTrailTabView.setTabTitle(3, decodeBase64(args[AJAX_ANNOTATION_ECRF_FIELD_STATUS_ENTRY_TAB_TITLE_BASE64]));
		auditTrailTabView.emphasizeTab(3, args[AJAX_ANNOTATION_ECRF_FIELD_STATUS_ENTRY_COUNT] == 0);
	}

}

function handleUpdateEcrfSection(xhr, status, args) {

	if (_testFlag(args, AJAX_OPERATION_SUCCESS) && _testPropertyExists(args, AJAX_WINDOW_TITLE_BASE64)
	        && _testPropertyExists(args, AJAX_WINDOW_NAME)) {
		window.name = args[AJAX_WINDOW_NAME];
		var title = decodeBase64(args[AJAX_WINDOW_TITLE_BASE64]);
		document.title = title;
	}

}


var hyperlinkTitlePreset;

function setHyperlinkTitle(apply) {

	if (apply) {
		hyperlinkTitle.setValue(hyperlinkTitlePreset);
	}
	hyperlinkTitlePresetOverrideConfirmation.hide();
	hyperlinkTitlePreset = null;

}

function handleHyperlinkCategorySelected(xhr, status, args) {

	if (_testPropertyExists(args, AJAX_HYPERLINK_CATEGORY_TITLE_PRESET_BASE64)) {
		hyperlinkTitlePreset = decodeBase64(args[AJAX_HYPERLINK_CATEGORY_TITLE_PRESET_BASE64]);
		if (hyperlinkTitlePreset != null && hyperlinkTitlePreset.length > 0) {
			if (hyperlinkTitle.getValue().length > 0 && hyperlinkTitlePreset != hyperlinkTitle.getValue()) {
				hyperlinkTitlePresetOverrideConfirmation.show();
				return;
			} else {
				hyperlinkTitle.setValue(hyperlinkTitlePreset);
			}
		}
	}
	hyperlinkTitlePreset = null;

}

var journalEntryTitlePreset;

function setJournalEntryTitle(apply) {

	if (apply) {
		journalEntryTitle.setValue(journalEntryTitlePreset);
	}
	journalEntryTitlePresetOverrideConfirmation.hide();
	journalEntryTitlePreset = null;

}

function handleJournalCategorySelected(xhr, status, args) {

	if (_testPropertyExists(args, AJAX_JOURNAL_CATEGORY_TITLE_PRESET_BASE64)) {
		journalEntryTitlePreset = decodeBase64(args[AJAX_JOURNAL_CATEGORY_TITLE_PRESET_BASE64]);
		if (journalEntryTitlePreset != null && journalEntryTitlePreset.length > 0) {
			if (journalEntryTitle.getValue().length > 0 && journalEntryTitlePreset != journalEntryTitle.getValue()) {
				journalEntryTitlePresetOverrideConfirmation.show();
				return;
			} else {
				journalEntryTitle.setValue(journalEntryTitlePreset);
			}
		}
	}
	journalEntryTitlePreset = null;

}

function decodeBase64(base64String) {

	var encoded = base64String.replace(/-/g, '+').replace(/_/g, '/');
	while (encoded.length % 4)
		encoded += '=';
	return jQuery.base64Decode(encoded);

}
function encodeBase64(string, urlSafe) {

	var encoded = jQuery.base64Encode(string);
	if (urlSafe) {
		return encoded.replace(/\+/g, '-').replace(/\//g, '_').replace(/=+$/, '');
	}
	return encoded;

}

function _redirect(url) {

	location.href = url;
}

function calendarPrintPreview(title) {

	var toPrint = document.getElementsByClassName("ctsms-schedule");
	if (toPrint != null && toPrint.length > 0) {
		toPrint = toPrint[0].cloneNode(true);

		var leftHeader = toPrint.getElementsByClassName("fc-header-left")[0];
		while (leftHeader.firstChild) {
			leftHeader.removeChild(leftHeader.firstChild);
		}
		var rightHeader = toPrint.getElementsByClassName("fc-header-right")[0];
		while (rightHeader.firstChild) {
			rightHeader.removeChild(rightHeader.firstChild);
		}

		var linkElements = document.getElementsByTagName('link');
		var link = '';
		for ( var i = 0, length = linkElements.length; i < length; i++) {
			link = link + linkElements[i].outerHTML;
		}

		var styleElements = document.getElementsByTagName('style');
		var styles = '';
		for ( var i = 0, length = styleElements.length; i < length; i++) {
			styles = styles + styleElements[i].innerHTML;
		}

		var printWindow = window.open('', '_blank');
		printWindow.document.open();
		printWindow.document.write('<html moznomarginboxes mozdisallowselectionprint><head><title>' + title + '</title>' + link + '<style>' + styles
		        + '</style></head><body onload="setTimeout(window.print(),500);">')
		printWindow.document.write(toPrint.innerHTML);
		printWindow.document.write('</body></html>');
		printWindow.document.close();
	}

}

var cssTextRegExp = /([^{]+)\s*\{\s*([^}]+)\s*\}/; // beware of g in loops!

function getCssRuleMap(classNameRegexp) {

	var result = {};
    var styleSheets = window.document.styleSheets;
    for(var i = 0; i < styleSheets.length; i++){
    	var classes;
		if (styleSheets[i].rules) {
			classes = styleSheets[i].rules;
		} else {
			try {
				if (!styleSheets[i].cssRules) {
					continue;
				}
			}
			// Note that SecurityError exception is specific to Firefox.
			catch (e) {
				if (e.name == 'SecurityError') {
					continue;
				}
			}
			classes = styleSheets[i].cssRules;
		}
        if (!classes)
            continue;
        for (var j = 0; j < classes.length; j++) {
            if (classNameRegexp == null || classNameRegexp.test(classes[j].selectorText)) {
                var cssText;
                if(classes[j].cssText){
                	cssText = classes[j].cssText;
                } else {
                	cssText = classes[j].style.cssText;
                }
                if(cssText.indexOf(classes[j].selectorText) == -1){
                	cssText = classes[j].selectorText + "{" + cssText + "}";
                }
				if (cssTextRegExp.test(cssText)) {
					var matches = cssTextRegExp.exec(cssText);
					var keyValues = matches[2].split(";");
					var properties = {};
					for ( var k = 0; k < keyValues.length; k++) {
						var keyValue = keyValues[k].trim();
						if (keyValue.length > 0 && keyValue.indexOf(":") > -1) {
							var property = keyValue.split(":", 2);
							var key = property[0].trim();
							if (key.length > 0) {
								properties[key] = property[1].trim();
							}
						}
					}
					result[classes[j].selectorText] = properties;
				}
            }
        }
    }
    return result;

}

function rgb2hex(rgb) {
	//http://stackoverflow.com/questions/1740700/how-to-get-hex-color-value-rather-than-rgb-value
    if (/^#[0-9A-F]{6}$/i.test(rgb)) return rgb;

    rgb = rgb.match(/rgb\(\s*(\d+)\s*,\s*(\d+)\s*,\s*(\d+)\s*\)/);
    function hex(x) {
        return ("0" + parseInt(x).toString(16)).slice(-2);
    }
    return "#" + hex(rgb[1]) + hex(rgb[2]) + hex(rgb[3]);
}

var AUTOFILL_SKIP_TYPES = {
	hidden : true,
	submit : true,
	button : true,
	checkbox : true,
	radio : true,
	file : true,
	image : true,
	reset : true
};

var AUTOFILL_READONLY_TYPES = {
	text : true,
	email : true,
	tel : true,
	search : true,
	url : true,
	password : true,
	number : true,
	date : true,
	datetime : true,
	'datetime-local' : true,
	month : true,
	week : true,
	time : true
};

function disableBrowserAutofill(root, reinit) {
	if (typeof IS_LOGIN_WINDOW !== 'undefined' && IS_LOGIN_WINDOW) {
		return;
	}
	var $root = root ? jQuery(root) : jQuery(document);
	$root.find('form').attr('autocomplete', 'off');
	$root.find('input, textarea, select').each(function() {
		var el = this;
		var $el = jQuery(el);
		var tag = (el.tagName || '').toLowerCase();
		var type = (el.type || '').toLowerCase();
		if (tag === 'input' && AUTOFILL_SKIP_TYPES[type]) {
			return;
		}
		$el.attr('autocomplete', 'ctsms-off');
		var useReadonly = tag === 'textarea'
				|| (tag === 'input' && (!type || AUTOFILL_READONLY_TYPES[type]));
		if (!useReadonly) {
			return;
		}
		if (reinit) {
			$el.removeData('ctsmsAutofillGuard');
			$el.removeData('ctsmsAutofillUnlocked');
			$el.off('focus.ctsmsAutofill');
		}
		// User already unlocked this field; keep editable across AJAX (e.g. autocomplete).
		if ($el.data('ctsmsAutofillUnlocked')) {
			return;
		}
		// Stale guard: marked protected but editable again without unlock (reused/reinit).
		if ($el.data('ctsmsAutofillGuard') && !$el.prop('readonly')) {
			$el.removeData('ctsmsAutofillGuard');
			$el.off('focus.ctsmsAutofill');
		}
		if ($el.data('ctsmsAutofillGuard')) {
			return;
		}
		$el.data('ctsmsAutofillGuard', true);
		if (!$el.prop('readonly') && $el[0] !== document.activeElement) {
			$el.prop('readonly', true);
			$el.one('focus.ctsmsAutofill', function() {
				jQuery(this)
					.prop('readonly', false)
					.removeData('ctsmsAutofillGuard')
					.data('ctsmsAutofillUnlocked', true);
			});
		}
	});
}

jQuery(function() {
	disableBrowserAutofill();
});
jQuery(document).ajaxComplete(function() {
	disableBrowserAutofill();
});

var JOB_LIST_POLL_INTERVAL_MS = 3000;
var JOB_LIST_POLL_MAX_COUNT = 600; // 30 min at the 3 s poll interval
var jobListPollCount = 0;
var jobListPollTimer = null;
var jobListPollPending = false;

function updateJobListPoll(polled) {
	if (typeof pollJobList !== "function") {
		return;
	}
	if (polled) {
		jobListPollPending = false;
		jobListPollCount++;
	} else {
		// a user action (start, load, paging, ...) gives a stuck job a fresh time budget
		jobListPollCount = 0;
	}
	if (jQuery(".ctsms-job-running").length > 0 && jobListPollCount < JOB_LIST_POLL_MAX_COUNT) {
		// the next poll is scheduled only after the previous one completed,
		// so polls never pile up in the ajax queue
		if (jobListPollTimer == null && !jobListPollPending) {
			jobListPollTimer = setTimeout(function() {
				jobListPollTimer = null;
				jobListPollPending = true;
				pollJobList();
			}, JOB_LIST_POLL_INTERVAL_MS);
		}
	} else if (jobListPollTimer != null) {
		clearTimeout(jobListPollTimer);
		jobListPollTimer = null;
	}
}

// code editor (codeMirror) suggestions, shared by the value and output expression editors (trial.js) and the mass mail text template editor (massMail.js)

// the names starting with the query, or if there are none, the names containing it
function filterHintNames(candidates, query) {
	var lowerCaseQuery = query.toLowerCase();
	var starting = [];
	var containing = [];
	for (var i = 0; i < candidates.length; i++) {
		var index = candidates[i].toLowerCase().indexOf(lowerCaseQuery);
		if (index == 0) {
			starting.push(candidates[i]);
		} else if (index > 0) {
			containing.push(candidates[i]);
		}
	}
	return starting.length > 0 ? starting : containing;
}

// the codeMirror whose suggestions were opened by showCodeMirrorHint
var codeMirrorHintEditor = null;

// show the suggestions (Ctrl-Space): in them, Tab picks one and Enter closes them and starts a new line
function showCodeMirrorHint(cm, getHints, options) {
	codeMirrorHintEditor = cm;
	CodeMirror.simpleHint(cm, getHints, options);
}

// show the suggestions once the typed character is in the editor, unless the open suggestions already got it.
// a single suggestion is not inserted right away while typing
function showTypedCodeMirrorHint(cm, getHints) {
	window.setTimeout(function() {
		if (jQuery(".CodeMirror-completions").length == 0 && !cm.somethingSelected() && getHints(cm) != null) {
			showCodeMirrorHint(cm, getHints, {
				completeSingle : false
			});
		}
	}, 50);
}

// captured before simpleHint's own keydown handler of the suggestions, which is also reached by the suggestions it re-opens while typing
function handleCodeMirrorHintKeyDown(event) {
	var code = event.keyCode;
	var cm = codeMirrorHintEditor;
	if ((code != 9 && code != 13) || cm == null || !jQuery(event.target).is(".CodeMirror-completions select")) {
		return;
	}
	event.preventDefault();
	event.stopPropagation();
	if (code == 9) {
		// simpleHint picks on double click
		var dblclick = document.createEvent("MouseEvents");
		dblclick.initEvent("dblclick", true, true);
		event.target.dispatchEvent(dblclick);
	} else {
		// focusing the editor closes the suggestions (blur)
		cm.focus();
		cm.execCommand("newlineAndIndent");
	}
}

if (document.addEventListener) {
	document.addEventListener("keydown", handleCodeMirrorHintKeyDown, true);
}

// value forms: mark a field as entered but not saved as soon as its value changes.
// The server renders this state with each update of the field rows. Widgets report changes in
// different ways (change, blur, dateSelect, timeSelect, itemSelect, drawing), so the inputs of a
// field are compared with their state when the field was rendered.
function updateUnsavedInputFields(root) {
	var selector = "fieldset.ctsms-input-unsaved-tracked";
	var context = jQuery(root || document);
	var fieldsets = context.find(selector);
	if (context.is(selector)) { // jQuery 1.7: no addBack
		fieldsets = fieldsets.add(context);
	}
	fieldsets.each(function() {
		var fieldset = jQuery(this);
		// file uploads and photo captures only re-render the file label:
		// (not the collapsed state of a toggleable field row, which is an input too)
		var values = fieldset.find(":input").not("[name$='_collapsed']").serialize() + "|" + fieldset.find(".ctsms-input-file-link").text();
		var renderedValues = fieldset.data("ctsmsRenderedValues");
		if (renderedValues === undefined) {
			fieldset.data("ctsmsRenderedValues", values);
			fieldset.data("ctsmsRenderedUnsaved", fieldset.hasClass("ctsms-input-unsaved"));
		} else {
			fieldset.toggleClass("ctsms-input-unsaved", values !== renderedValues || fieldset.data("ctsmsRenderedUnsaved") === true);
		}
		markUnsavedInputField(fieldset);
	});
}

// show the field name and the field's own save button of an unsaved field in the theme's active
// state, like a selected tab (not a disabled save button, e.g. a draft left in a form that is no
// longer editable), and draw a bar on the row's left edge in the active legend's border color
// (CSS cannot take the color from the theme):
function markUnsavedInputField(fieldset) {
	var unsaved = fieldset.hasClass("ctsms-input-unsaved");
	var legend = fieldset.children(".ui-fieldset-legend").toggleClass("ui-state-active", unsaved);
	fieldset.css("box-shadow", unsaved && legend.length > 0 ? "inset 4px 0 0 " + legend.css("border-top-color") : "");
	fieldset.find(".ctsms-input-update-button").each(function() {
		var button = jQuery(this);
		button.toggleClass("ui-state-active", unsaved && !button.hasClass("ui-state-disabled"));
	});
}

jQuery(document).ready(function() {
	updateUnsavedInputFields();
});
jQuery(document).ajaxComplete(function() {
	updateUnsavedInputFields();
});
jQuery(document).on("change input keyup mouseup touchend", "fieldset.ctsms-input-unsaved-tracked", function() {
	var fieldset = this;
	setTimeout(function() {
		updateUnsavedInputFields(fieldset);
	}, 0);
});
// PrimeFaces buttons drop ui-state-active on mouseout, mouseup and keyup, and a toggleable
// fieldset legend toggles it on mousedown and mouseup, so restore it afterwards:
jQuery(document).on("mouseout mouseup keyup", "fieldset.ctsms-input-unsaved-tracked .ctsms-input-update-button, fieldset.ctsms-input-unsaved-tracked > .ui-fieldset-legend", function() {
	var fieldset = jQuery(this).closest("fieldset.ctsms-input-unsaved-tracked");
	setTimeout(function() {
		markUnsavedInputField(fieldset);
	}, 0);
});
