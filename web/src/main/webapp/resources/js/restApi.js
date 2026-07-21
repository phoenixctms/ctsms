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

	function setBearerAuth(jqueryRequest, jwt) {
		jqueryRequest.beforeSend = function(xhr) {
			var token = (jwt != null && jwt.length > 0) ? jwt : sessionJwt;
			if (token != null && token.length > 0) {
				xhr.setRequestHeader('Authorization', 'Bearer ' + token);
			}
		};
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
			// Resolve session JWT at send time so on-request refresh is used.
			setBearerAuth(jqueryRequest, usernameOrJwt);
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

	// Persist debounce/XHR state on RestApi so FieldCalculation expressions
	// (evaluated with a fresh mask each time) can share timers across keystrokes.
	var debounceStates = {};

	function debounceStateKey(key) {
		return (key == null || key === '') ? '_default' : ('' + key);
	}

	function getDebounceState(key) {
		var stateKey = debounceStateKey(key);
		var state = debounceStates[stateKey];
		if (state == null) {
			state = debounceStates[stateKey] = { timer: null, seq: 0, xhr: null };
		}
		return state;
	}

	function abortTrackedRequest(state) {
		if (state.xhr != null && typeof state.xhr.abort === 'function') {
			try {
				state.xhr.abort();
			} catch (e) {
				// ignore
			}
		}
		state.xhr = null;
	}

	function debounce(key, delayMs, fn) {
		var state = getDebounceState(key);
		if (state.timer != null) {
			clearTimeout(state.timer);
		}
		var delay = (delayMs == null || isNaN(delayMs)) ? 300 : delayMs;
		state.timer = setTimeout(function() {
			state.timer = null;
			state.seq += 1;
			var seq = state.seq;
			abortTrackedRequest(state);
			if (typeof fn === 'function') {
				fn(seq);
			}
		}, delay);
		return state.seq;
	}

	function debounceIsCurrent(key, seq) {
		return getDebounceState(key).seq === seq;
	}

	function trackRequest(key, jqXHR) {
		var state = getDebounceState(key);
		if (state.xhr != null && state.xhr !== jqXHR) {
			abortTrackedRequest(state);
		}
		state.xhr = jqXHR;
		return jqXHR;
	}

	function abortRequest(key) {
		var state = getDebounceState(key);
		if (state.timer != null) {
			clearTimeout(state.timer);
			state.timer = null;
		}
		abortTrackedRequest(state);
		state.seq += 1;
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
	RestApi.dateTimeFormat = dateTimeFormat;
	RestApi.debounce = debounce;
	RestApi.debounceIsCurrent = debounceIsCurrent;
	RestApi.trackRequest = trackRequest;
	RestApi.abortRequest = abortRequest;

	if (debug_level >= 1) {
		console.log("rest api utilities loaded");
	}

})(window.RestApi);
