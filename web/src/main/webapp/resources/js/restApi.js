
var RestApi = RestApi || {};

(function(RestApi) {

	var url = REST_API_URL;

	var debug_level = 0;

	var searchMapsCache = {};

	var apiJsonDateTimePattern = typeof API_JSON_DATETIME_PATTERN !== 'undefined'
		? API_JSON_DATETIME_PATTERN
		: 'yyyy-MM-dd HH:mm:ss';

	function getApiJsonDateTimePattern() {
		return apiJsonDateTimePattern;
	}

	function dateTimeFormat(value) {
		if (value == null) {
			return null;
		}
		if (typeof value === 'string') {
			return value;
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

	function setBearerAuth(jqueryRequest, jwt) {
		if (jwt != null && jwt.length > 0) {
			jqueryRequest.beforeSend = function(xhr) {
				xhr.setRequestHeader('Authorization', 'Bearer ' + jwt);
			};
		}
	}

	function createRequest(method, path, usernameOrJwt, password) {
		var jqueryRequest = {};
		jqueryRequest.url = url + path;
		jqueryRequest.type = method;
		if (password !== undefined) {
			if (usernameOrJwt != null && usernameOrJwt.length > 0) {
				jqueryRequest.username = usernameOrJwt;
				jqueryRequest.password = password;
			}
		} else if (usernameOrJwt !== undefined) {
			var jwt = usernameOrJwt;
			if (jwt == null || jwt.length === 0) {
				jwt = typeof REST_API_JWT !== 'undefined' ? REST_API_JWT : null;
			}
			setBearerAuth(jqueryRequest, jwt);
		}
		return jqueryRequest;
	}

	function createSessionRequest(method, path) {
		return createRequest(method, path, null);
	}

	function ajaxRequest(jqueryRequest) {
		if (debug_level >= 1) {
			console.log("rest api request: " + jqueryRequest.url);
		}
		if (url != null && url.length > 0) {
			return jQuery.ajax(jqueryRequest);
		}
		return null;
	}

	function executeRequest(jqueryRequest) {
		ajaxRequest(jqueryRequest);
	}

	function unwrapAjaxData(result) {
		if (jQuery.isArray(result) && result.length >= 1 && typeof result[1] === 'string') {
			return result[0];
		}
		return result;
	}

	function mapByField(items, field) {
		var map = {};
		if (items) {
			for (var i = 0; i < items.length; i++) {
				map[items[i][field]] = items[i].id;
			}
		}
		return map;
	}

	function ajaxGet(path) {
		var req = createSessionRequest('GET', path);
		req.dataType = 'json';
		return ajaxRequest(req);
	}

	function ajaxGetSync(path) {
		if (url == null || url.length === 0) {
			return null;
		}
		var req = createSessionRequest('GET', path);
		req.dataType = 'json';
		req.async = false;
		var jqXHR = ajaxRequest(req);
		if (jqXHR && jqXHR.status === 200 && jqXHR.responseJSON != null) {
			return jqXHR.responseJSON;
		}
		return null;
	}

	function ajaxPost(path, body) {
		var req = createSessionRequest('POST', path);
		req.contentType = 'application/json';
		req.dataType = 'json';
		req.data = JSON.stringify(body);
		return ajaxRequest(req);
	}

	function loadSearchMaps(module) {
		if (searchMapsCache[module]) {
			var cached = jQuery.Deferred();
			cached.resolve(searchMapsCache[module]);
			return cached.promise();
		}
		var deferred = jQuery.Deferred();
		jQuery.when(
			ajaxGet('selectionset/criterionproperties?module=' + encodeURIComponent(module)),
			ajaxGet('selectionset/allcriteriarestrictions'),
			ajaxGet('selectionset/allcriterionties')
		).done(function(propResult, restrResult, tieResult) {
			var maps = {
				properties: mapByField(unwrapAjaxData(propResult), 'nameL10nKey'),
				restrictions: mapByField(unwrapAjaxData(restrResult), 'nameL10nKey'),
				ties: mapByField(unwrapAjaxData(tieResult), 'nameL10nKey')
			};
			searchMapsCache[module] = maps;
			deferred.resolve(maps);
		}).fail(function() {
			deferred.rejectWith(this, arguments);
		});
		return deferred.promise();
	}

	function searchByCriteria(entity, criteria, pageQuery) {
		var path = 'search/' + entity + '/search';
		if (pageQuery) {
			var query = [];
			if (pageQuery.p != null) {
				query.push('p=' + encodeURIComponent(pageQuery.p));
			}
			if (pageQuery.s != null) {
				query.push('s=' + encodeURIComponent(pageQuery.s));
			}
			if (query.length > 0) {
				path += '?' + query.join('&');
			}
		}
		return ajaxPost(path, criteria);
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
		console.log("rest api utilities loaded");
	}

})(window.RestApi);
