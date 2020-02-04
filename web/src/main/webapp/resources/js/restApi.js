
var RestApi = RestApi || {};

(function(RestApi) {

	var url = REST_API_URL;

	var debug_level = 0;

	function createRequest(method, path, username, password) {
		var jqueryRequest = {};
		jqueryRequest.url = url + path;
		jqueryRequest.type = method;
		if (username != null && username.length > 0) {
			jqueryRequest.username = username;
			jqueryRequest.password = password;
		}
		return jqueryRequest;
	}

	function executeRequest(jqueryRequest) {
		if (debug_level >= 1) {
			console.log("rest api request: " + jqueryRequest);
		}
		if (url != null && url.length > 0) {
			jQuery.ajax(jqueryRequest);
		}
	}


	RestApi.createRequest = createRequest;
	RestApi.executeRequest = executeRequest;

	if (debug_level >= 1) {
		console.log("rest api utilities loaded");
	}

})(window.RestApi);