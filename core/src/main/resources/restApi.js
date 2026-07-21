var RestApi = RestApi || {};

(function(RestApi) {

	var debug_level = 0;

	var apiJsonDateTimePattern = typeof API_JSON_DATETIME_PATTERN !== 'undefined'
		? API_JSON_DATETIME_PATTERN
		: 'yyyy-MM-dd HH:mm:ss';

	function unsupported(name) {
		console.log('RestApi.' + name + ': HTTP not supported in server-side FieldCalculation');
		return null;
	}

	function dateTimeFormat(value) {
		if (value == null || (typeof value === 'string' && value.length === 0)) {
			return null;
		}
		if (typeof JSJoda !== 'undefined') {
			var formatter = JSJoda.DateTimeFormatter.ofPattern(apiJsonDateTimePattern);
			var dateTime = null;
			if (typeof value === 'string') {
				if (/^\d{4}-\d{2}-\d{2}$/.test(value)) {
					dateTime = JSJoda.LocalDateTime.of(JSJoda.LocalDate.parse(value), JSJoda.LocalTime.of(0, 0, 0));
				} else {
					try {
						dateTime = JSJoda.LocalDateTime.parse(value, formatter);
					} catch (e) {
						try {
							dateTime = JSJoda.LocalDateTime.parse(value);
						} catch (e2) {
							dateTime = null;
						}
					}
				}
			} else if (value instanceof JSJoda.LocalDate) {
				dateTime = JSJoda.LocalDateTime.of(value, JSJoda.LocalTime.of(0, 0, 0));
			} else if (value instanceof JSJoda.LocalDateTime) {
				dateTime = value;
			} else if (value instanceof JSJoda.ZonedDateTime) {
				dateTime = value.toLocalDateTime();
			} else if (typeof moment !== 'undefined' && moment.isMoment && moment.isMoment(value)) {
				dateTime = JSJoda.LocalDateTime.from(JSJoda.nativeJs(value.toDate()));
			} else if (value instanceof Date) {
				dateTime = JSJoda.LocalDateTime.from(JSJoda.nativeJs(value));
			}
			if (dateTime != null) {
				return dateTime.format(formatter);
			}
		}
		if (typeof value === 'string') {
			if (/^\d{4}-\d{2}-\d{2}$/.test(value)) {
				return value + ' 00:00:00';
			}
			return value;
		}
		if (typeof moment !== 'undefined' && moment.isMoment && moment.isMoment(value)) {
			return value.format('YYYY-MM-DD HH:mm:ss');
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
	RestApi.dateTimeFormat = dateTimeFormat;

	if (debug_level >= 1) {
		console.log("rest api utilities loaded (server-side stubs for HTTP)");
	}

})(window.RestApi);
