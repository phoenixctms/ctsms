var window = this;
var jQuery = {};
jQuery.extend = function(def) {
    for(var key in def)
        if(def.hasOwnProperty(key))
        	jQuery[key] = def[key];
    return jQuery;
};
if (!String.prototype.trim) {
    (function() {
        // Make sure we trim BOM and NBSP
        var rtrim = /^[\s\uFEFF\xA0]+|[\s\uFEFF\xA0]+$/g;
        String.prototype.trim = function() {
            return this.replace(rtrim, '');
        };
    })();
}
jQuery.trim = function(str) {
	return (str != null ? str.trim() : null);
};
var console = {
	log : function(msg) {
		java.lang.System.out.println(msg);
	}
};

var floatEpsilon = 0.000001;

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

function decodeBase64(base64String) {

	var encoded = base64String.replace(/-/g, '+').replace(/_/g, '/');
	while (encoded.length % 4)
		encoded += '=';
	return jQuery.base64Decode(encoded);

}

function _getElement(pickTargetField) {

	return null;

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

function openInventory() {
	//no-op
}
function openStaff() {
	//no-op
}
function openCourse() {
	//no-op
}
function openTrial() {
	//no-op
}
function openProband() {
	//no-op
}
function openInputField() {
	//no-op
}
function openUser() {
	//no-op
}
function openMassMail() {
	//no-op
}

function showErrorMessage(error_message_id) {

}

function hideErrorMessage(error_message_id) {

}

function setErrorMessageText(error_message_id,output) {

}

function setErrorMessageTexts(error_message_id,msgs) {

}


function _getOutputId(value) {
    return INPUT_FIELD_OUTPUT_ID_PREFIX + value.inquiry.id;
}


var INPUT_JSON_DATETIME_PATTERN = 'yyyy-MM-dd HH:mm';
var INPUT_DATE_PATTERN = 'yyyy-MM-dd';
var INPUT_TIME_PATTERN = 'HH:mm';
var INPUT_DATETIME_PATTERN = INPUT_DATE_PATTERN + ' ' + INPUT_TIME_PATTERN;
var INPUT_DECIMAL_SEPARATOR = null;
var INPUT_TIMEZONE_ID;
var SYSTEM_TIMEZONE_ID;
var FIELD_CALCULATION_DEBUG_LEVEL = 0;

var AJAX_OPERATION_SUCCESS = 'operationSuccess';
var AJAX_FIELD_DELTA_ERROR_MESSAGE_ID = 'fieldDeltaErrorMessageId';
var AJAX_INPUT_FIELD_PROBAND_BASE64 = 'inputFieldProbandBase64';
var AJAX_INPUT_FIELD_TRIAL_BASE64 = 'inputFieldTrialBase64';

var AJAX_INPUT_FIELD_PROBAND_ADDRESSES_BASE64 = 'inputFieldProbandAddressesBase64';
var AJAX_INPUT_FIELD_PROBAND_LIST_ENTRY_BASE64 = 'inputFieldProbandListEntryBase64';
var AJAX_INPUT_FIELD_VISIT_SCHEDULE_ITEMS_BASE64 = 'inputFieldVisitScheduleItemsBase64';
var AJAX_INPUT_FIELD_PROBAND_GROUPS_BASE64 = 'inputFieldProbandGroupsBase64';
var AJAX_INPUT_FIELD_ACTIVE_USER_BASE64 = 'activeUserBase64';
var AJAX_INPUT_FIELD_LOCALE = "inputFieldLocale";

var AJAX_INPUT_FIELD_VARIABLE_VALUES_BASE64 = 'inputFieldVariableValuesBase64';
var AJAX_INPUT_FIELD_PROBAND_LIST_ENTRY_TAG_VALUES_BASE64 = "probandListEntryTagValuesBase64";

var INPUT_FIELD_OUTPUT_ID_PREFIX = 'inputfield_output_';
var INPUT_FIELD_OUTPUT_ID_INDEX_SEPARATOR = '_';

var INPUT_FIELD_WIDGET_VAR_PREFIX = 'inputFieldWidget_';
var INPUT_FIELD_WIDGET_VAR_INDEX_SEPARATOR = '_';

var DEFAULT_GEOLOCATION_LATITUDE = null;
var DEFAULT_GEOLOCATION_LONGITUDE = null;
var ENABLE_GEOLOCATION_SERVICES = false;
var FORCE_DEFAULT_GEOLOCATION = false;

var REST_API_URL = null;

var INPUT_FIELD_DELTA_SUMMARY_MAX = null;
var FIELD_CALCULATION_DECODE_BASE64 = false;

function initInputFieldVariables(args) {
	return FieldCalculation.handleInitInputFieldVariables(null,null,args);
}
function updateInputFieldVariables(args) {
	return FieldCalculation.handleUpdateInputFieldVariables(null,null,args);
}