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

//var ajaxWait = true;
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
			//console.log(method + " dequeued " + count + " times");
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
		//var textElement = errorMessageSpanElement.contents()[0];

		//if (typeof textElement.textContent !== 'undefined') {
		//	textElement.textContent = msg;
		//} else {
		//	textElement.innerText = msg;
		//}
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
		var sessionExpiry = (new Date()); //.addSeconds(sessionMaxInactiveInterval);
		sessionExpiry.setSeconds(sessionExpiry.getSeconds() + sessionMaxInactiveInterval);
		jQuery('#session_timer').countdown(sessionExpiry, { //.toString('yyyy/MM/dd HH:mm:ss'), {
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
		var sessionExpiry = (new Date()); //.addSeconds(sessionMaxInactiveInterval);
		sessionExpiry.setSeconds(sessionExpiry.getSeconds() + sessionMaxInactiveInterval);
		jQuery('#session_timer').countdown(sessionExpiry); //.toString('yyyy/MM/dd HH:mm:ss'));
	}

	if (window.opener && !window.opener.closed && window.opener.resetSessionTimers && depth < 10) {
		try {
			window.opener.resetSessionTimers(depth + 1);
		} catch (e) {
			//console.log(e);
		}
	}
//	if (PORTAL_WINDOW_NAME != '_self' && window.name != PORTAL_WINDOW_NAME && typeof window['IS_LOGIN_WINDOW'] === 'undefined') {
//		if (!!navigator.userAgent.match(/Version\/[\d\.]+.*Safari/)) {
//
//		} else if (_isTrustedReferrer(document.referrer)) { // (!jQuery.browser.safari) {
//			window.open('javascript:if(window.resetSessionTimers){resetSessionTimers();}else{window.location.href=' + JSON.stringify(APPLICATION_URL + PORTAL_URL)
//		        + ';}', PORTAL_WINDOW_NAME);
//		//} else {
//		//	//alert("safari");
//		}
//	}
	
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

//function setExpanding(tableWidget) {
//	if (tableWidget) {
//		tableWidget.expanding = true;
//		alert("expand");
//	}
//}
//
//function suppressExpandingItemSelect(y,tableWidget) {
//	if (tableWidget) {
//		var expanding = tableWidget.expanding;
//		tableWidget.expanding = false;
//		alert("select");
//		if (y && expanding) {
//			console.log(y.source);
//			//xhr.abort();
//		}
//	}
//}

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
		//console.log("value '" + value + "' set, val is " + targetFieldElement.val());
	//} else {
		//console.log("warning: field " + targetField + " not found");
	}
}

//function handleLogout(xhr, status, args) {
//
//	if (_testFlag(args, AJAX_OPERATION_SUCCESS) && _testFlag(args, AJAX_LOGGED_OUT)) {
//
//		if (_testPropertyExists(args, AJAX_REFERER_BASE64)) {
//			_redirect(LOGIN_URL + '?' + REFERER + '=' + args[AJAX_REFERER_BASE64]);
//		} else {
//			_redirect(LOGIN_URL);
//		}
//	}
//
//}

function handleReload(xhr, status, args) {

	if (_testFlag(args, AJAX_OPERATION_SUCCESS)) {
		location.reload(true);
	}

}

function handleKeepAliveCallback(xhr, status, args) {

	if (_testFlag(args, AJAX_OPERATION_SUCCESS) && _testPropertyExists(args, AJAX_KEEP_ALIVE_JS_CALLBACK)) {
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

function ajaxRequest(source, process, update, oncomplete) {
	var ajaxCfg = {
	    source : _stripLeadingColons(source),
	    process : _stripLeadingColons(process),
	    event : 'valueChange'
	};
	if (update != null && update.length > 0) {
		ajaxCfg['update'] = _stripLeadingColons(update);
	}
	if (_testFunction(window[oncomplete])) {
		ajaxCfg['oncomplete'] = function(xhr, status) {
			window[oncomplete](xhr, status, null);
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

	//alert(window);
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

function _isTrustedReferrer(url) {
	if (!url) {
		return false;
	} else {
		var referer = document.createElement('a');
		referer.href = url;

		if (referer && jQuery.inArray(referer.hostname, TRUSTED_REFERER_HOSTS) >= 0 && _prependSlash(referer.pathname).indexOf(CONTEXT_PATH) == 0) {
			return true;
		}
		return false;
	}
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
			var url = decodeBase64(REFERER_URL_BASE64);
			if (_isTrustedReferrer(url)) {
				_redirect(url);
			} else {
				_redirect(PORTAL_URL);
			}
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

	//var openerWindow = window.open(decodeBase64(args.url), args.window);
	//openerWindow.focus();
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

// var fNewDoc = false;
// var EditDocumentButton = null;
// try {
// EditDocumentButton = new ActiveXObject('SharePoint.OpenDocuments.3');
// if (EditDocumentButton != null) { fNewDoc = true; }
// } catch(e) {
// console.log(e);
// }
//
// var L_EditDocumentError_Text = "Editing not supported.";
// var L_EditDocumentRuntimeError_Text = "Sorry, couldn't open the document.";
// function openEditOfficeDocument(strDocument) {
//
// if (fNewDoc) {
// if (!EditDocumentButton.EditDocument(strDocument)) {
// alert(L_EditDocumentRuntimeError_Text);
// }
// } else {
// try {
// var hownowPlugin = document.getElementById("winFirefoxPlugin");
// hownowPlugin.EditDocument(strDocument, null);
// } catch (e) {
// console.log(e);
// alert(L_EditDocumentError_Text);
// }
// }
//
// }

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
		//printWindow.document.write('<script type="text/javascript">window.print();<' + '/script>');
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
					//console.log("SecurityError. Cant readd: " + styleSheets[i].href);
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
