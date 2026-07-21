var RestApi = RestApi || {};

(function(RestApi) {

	var url = REST_API_URL;

	var debug_level = 0;

	var searchMapsCache = {};

	var apiJsonDateTimePattern = typeof API_JSON_DATETIME_PATTERN !== 'undefined'
		? API_JSON_DATETIME_PATTERN
		: 'yyyy-MM-dd HH:mm:ss';

	var sessionJwt = typeof REST_API_JWT !== 'undefined' ? REST_API_JWT : null;
	var sessionJwtExpires = null;
	var sessionJwtValiditySecs = null;
	var refreshingJwt = false;
	var jwtRefreshTimer = null;

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

	function base64UrlDecode(value) {
		var base64 = value.replace(/-/g, '+').replace(/_/g, '/');
		while (base64.length % 4) {
			base64 += '=';
		}
		try {
			if (typeof atob === 'function') {
				return atob(base64);
			}
		} catch (e) {
			// ignore
		}
		return null;
	}

	function parseJwtClaims(jwt) {
		if (jwt == null || jwt.length === 0) {
			return null;
		}
		try {
			var parts = jwt.split('.');
			if (parts.length < 2) {
				return null;
			}
			var json = base64UrlDecode(parts[1]);
			if (json == null || json.length === 0) {
				return null;
			}
			return JSON.parse(json);
		} catch (e) {
			return null;
		}
	}

	function applySessionJwt(jwt) {
		sessionJwt = jwt;
		if (typeof REST_API_JWT !== 'undefined') {
			REST_API_JWT = jwt;
		}
		var claims = parseJwtClaims(jwt);
		if (claims != null && claims.exp != null) {
			sessionJwtExpires = claims.exp * 1000;
			if (claims.iat != null) {
				sessionJwtValiditySecs = claims.exp - claims.iat;
			}
		} else {
			sessionJwtExpires = null;
		}
		scheduleJwtRefresh();
	}

	function sessionJwtNeedsRefresh() {
		if (sessionJwt == null || sessionJwt.length === 0) {
			return false;
		}
		if (sessionJwtExpires == null) {
			return false;
		}
		return Date.now() >= (sessionJwtExpires - JWT_REFRESH_SKEW_SECS * 1000);
	}

	function refreshSessionJwtIfRequired() {
		if (!sessionJwtNeedsRefresh() || refreshingJwt || url == null || url.length === 0) {
			return;
		}
		refreshingJwt = true;
		try {
			var path = 'tools/login';
			var validitySecs = sessionJwtValiditySecs;
			if (validitySecs != null) {
				path += '?validity_secs=' + encodeURIComponent(validitySecs);
			}
			var req = {};
			req.url = url + path;
			req.type = 'POST';
			req.dataType = 'json';
			req.async = false;
			setBearerAuth(req, sessionJwt);
			req.success = function(result) {
				if (typeof result === 'string' && result.length > 0) {
					if (debug_level >= 1) {
						console.log('rest api jwt refreshed');
					}
					applySessionJwt(result);
				}
			};
			if (debug_level >= 1) {
				console.log('rest api request: ' + req.url);
			}
			jQuery.ajax(req);
		} finally {
			refreshingJwt = false;
		}
	}

	function scheduleJwtRefresh() {
		if (jwtRefreshTimer != null) {
			clearTimeout(jwtRefreshTimer);
			jwtRefreshTimer = null;
		}
		if (sessionJwtExpires == null) {
			return;
		}
		var delay = Math.max(1000, sessionJwtExpires - Date.now() - JWT_REFRESH_SKEW_SECS * 1000);
		jwtRefreshTimer = setTimeout(function() {
			refreshSessionJwtIfRequired();
		}, delay);
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
				jwt = sessionJwt;
			}
			setBearerAuth(jqueryRequest, jwt);
		}
		return jqueryRequest;
	}

	function createSessionRequest(method, path) {
		return createRequest(method, path, null);
	}

	function ajaxRequest(jqueryRequest) {
		if (!refreshingJwt) {
			refreshSessionJwtIfRequired();
		}
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
		var data = null;
		var req = createSessionRequest('GET', path);
		req.dataType = 'json';
		req.async = false;
		req.success = function(result) {
			data = result;
		};
		var jqXHR = ajaxRequest(req);
		if (data != null) {
			return data;
		}
		if (jqXHR && jqXHR.status === 200) {
			if (jqXHR.responseJSON != null) {
				return jqXHR.responseJSON;
			}
			if (jqXHR.responseText != null && jqXHR.responseText.length > 0) {
				try {
					return JSON.parse(jqXHR.responseText);
				} catch (e) {
					// ignore
				}
			}
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

	if (sessionJwt != null && sessionJwt.length > 0) {
		applySessionJwt(sessionJwt);
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
