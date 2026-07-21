var RestApi = RestApi || {};

(function(RestApi) {

	var debug_level = 1;

	var apiJsonDateTimePattern = typeof API_JSON_DATETIME_PATTERN !== 'undefined'
		? API_JSON_DATETIME_PATTERN
		: 'yyyy-MM-dd HH:mm:ss';

	function unsupported(name) {
		console.log('RestApi.' + name + ': HTTP not supported in server-side FieldCalculation');
		return null;
	}

	function getApiJsonDateTimePattern() {
		return apiJsonDateTimePattern;
	}

	function normalizeApiDateTimeString(value) {
		if (value == null || value.length === 0) {
			return null;
		}
		if (/^\d{4}-\d{2}-\d{2}$/.test(value)) {
			return value + ' 00:00:00';
		}
		return value;
	}

	function dateTimeFormat(value) {
		if (value == null) {
			return null;
		}
		if (typeof value === 'string') {
			return normalizeApiDateTimeString(value);
		}
		if (typeof JSJoda !== 'undefined') {
			var dateTime;
			if (value instanceof JSJoda.LocalDate) {
				dateTime = JSJoda.LocalDateTime.of(value, JSJoda.LocalTime.of(0, 0, 0));
			} else if (value instanceof JSJoda.LocalDateTime) {
				dateTime = value;
			} else if (value instanceof JSJoda.ZonedDateTime) {
				dateTime = value.toLocalDateTime();
			} else if (typeof moment !== 'undefined' && moment.isMoment && moment.isMoment(value)) {
				dateTime = JSJoda.LocalDateTime.of(
					JSJoda.LocalDate.from(JSJoda.nativeJs(value.toDate())),
					JSJoda.LocalTime.of(0, 0, 0)
				);
			} else if (value instanceof Date) {
				dateTime = JSJoda.LocalDateTime.of(
					JSJoda.LocalDate.from(JSJoda.nativeJs(value)),
					JSJoda.LocalTime.of(0, 0, 0)
				);
			}
			if (dateTime != null) {
				return dateTime.format(JSJoda.DateTimeFormatter.ofPattern(apiJsonDateTimePattern));
			}
		}
		if (typeof moment !== 'undefined' && moment.isMoment && moment.isMoment(value)) {
			return value.format('YYYY-MM-DD') + ' 00:00:00';
		}
		if (value instanceof Date) {
			var year = value.getFullYear();
			var month = ('0' + (value.getMonth() + 1)).slice(-2);
			var day = ('0' + value.getDate()).slice(-2);
			var hour = ('0' + value.getHours()).slice(-2);
			var minute = ('0' + value.getMinutes()).slice(-2);
			var second = ('0' + value.getSeconds()).slice(-2);
			return year + '-' + month + '-' + day + ' ' + hour + ':' + minute + ':' + second;
		}
		return null;
	}

	function createRequest() {
		return unsupported('createRequest');
	}

	function createSessionRequest() {
		return unsupported('createSessionRequest');
	}

	function executeRequest() {
		return unsupported('executeRequest');
	}

	function ajaxGet() {
		return unsupported('ajaxGet');
	}

	function ajaxGetSync() {
		return unsupported('ajaxGetSync');
	}

	function ajaxPost() {
		return unsupported('ajaxPost');
	}

	function loadSearchMaps() {
		return unsupported('loadSearchMaps');
	}

	function searchByCriteria() {
		return unsupported('searchByCriteria');
	}

	RestApi.createRequest = createRequest;
	RestApi.createSessionRequest = createSessionRequest;
	RestApi.executeRequest = executeRequest;
	RestApi.ajaxGet = ajaxGet;
	RestApi.ajaxGetSync = ajaxGetSync;
	RestApi.ajaxPost = ajaxPost;
	RestApi.loadSearchMaps = loadSearchMaps;
	RestApi.searchByCriteria = searchByCriteria;
	RestApi.getApiJsonDateTimePattern = getApiJsonDateTimePattern;
	RestApi.dateTimeFormat = dateTimeFormat;

	if (debug_level >= 1) {
		console.log("rest api utilities loaded (server-side stubs for HTTP)");
	}

})(window.RestApi);
