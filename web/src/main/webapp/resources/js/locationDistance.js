var LocationDistance = LocationDistance || {};

(function(LocationDistance) {
	var directionsService = null;
	var currentLocation = null;
	var geocoder = null;

	var currentLocationSet = false;
	var currentLocationCb = null;
	var currentLocationCbArgs = null;

	var debug_level = 0;

	var defaultPosition = {
		coords : {
		    latitude : DEFAULT_GEOLOCATION_LATITUDE,
		    longitude : DEFAULT_GEOLOCATION_LONGITUDE
		}
	};

	function findCurrentLocation(cb) {
		currentLocationSet = true;
		if (cb) {
			currentLocationCb = cb;
			var args = [].slice.call(arguments);
			args.shift();
			currentLocationCbArgs = args;
		}
		if (ENABLE_GEOLOCATION_SERVICES && !FORCE_DEFAULT_GEOLOCATION) {
			if ("geolocation" in navigator) {
				navigator.geolocation.getCurrentPosition(_setCurrentLocation, function(err) {
					if (debug_level >= 1) {
						switch (error.code) {
						case error.PERMISSION_DENIED:
							console.log("User denied the request for Geolocation.");
							break;
						case error.POSITION_UNAVAILABLE:
							console.log("Location information is unavailable.");
							break;
						case error.TIMEOUT:
							console.log("The request to get user location timed out.");
							break;
						case error.UNKNOWN_ERROR:
							console.log("An unknown error occurred.");
							break;
						}
					}
					_setCurrentLocation(defaultPosition);
				});
			} else {
				if (debug_level >= 1) {
					console.log("navigator.geolocation not supported");
				}
				_setCurrentLocation(defaultPosition);
			}
		} else {
			_setCurrentLocation(defaultPosition);
		}
	}

	function _setCurrentLocation(pos) {
		currentLocation = null;
		LocationDistance.currentLocation = null;
		LocationDistance.currentStreetAddresses = null;
		LocationDistance.currentLocality = null;
		LocationDistance.currentSubLocality = null;
		LocationDistance.currentPostalCode = null;
		if (ENABLE_GEOLOCATION_SERVICES && pos != null) {
			currentLocation = new google.maps.LatLng(pos.coords.latitude, pos.coords.longitude);
			LocationDistance.currentLocation = currentLocation;
			if (geocoder == null) {
				try {
					geocoder = new google.maps.Geocoder();
				} catch (e) {
					if (debug_level >= 1) {
						console.log(e);
					}
				}
			}
			if (geocoder != null) {
				geocoder.geocode({
					'latLng' : currentLocation
				}, function(results, status) {
					if (debug_level >= 1) {
						console.log("geocoder.geocode response: " + status);
					}
					if (status == google.maps.GeocoderStatus.OK) {
						LocationDistance.currentLocationAddresses = results;
						LocationDistance.currentStreetAddresses = _getFirstFormattedAddress(filterAddressByType(results, 'street_address'));
						LocationDistance.currentLocality = _getFirstFormattedAddress(filterAddressByType(results, 'locality'));
						LocationDistance.currentSubLocality = _getFirstFormattedAddress(filterAddressByType(results, 'sublocality'));
						LocationDistance.currentPostalCode = _getFirstFormattedAddress(filterAddressByType(results, 'postal_code'));
					}
					if (currentLocationCb) {
						currentLocationCb.apply(this, currentLocationCbArgs);
					}
				});
			} else {
				if (currentLocationCb) {
					return currentLocationCb.apply(this, currentLocationCbArgs);
				}
			}

		} else {
			if (currentLocationCb) {
				return currentLocationCb.apply(this, currentLocationCbArgs);
			}
		}
	}

	function _filterGeocodeAddresses(addresses, condition) {
		var result = [];
		if (addresses && _testFunction(condition)) {
			for ( var i = 0; i < addresses.length; i++) {
				if (condition(addresses[i])) {
					result.push(addresses[i]);
				}
			}
		}
		return result;
	}

	function _getFirstFormattedAddress(addresses) {
		if (addresses && addresses.length > 0) {
			return addresses[0].formatted_address;
		}
		return '?';
	}

	function filterAddressByType(addresses, type) {
		return _filterGeocodeAddresses(addresses, function(address) {
			var i = address.types.length;
			while (i--) {
				if (address.types[i] === type) {
					return true;
				}
			}
			return false;
		});
	}

	function calcRouteDistance(start, end, setDistance, id, format) {
		if (!currentLocationSet) {
			findCurrentLocation(calcRouteDistance, start, end, setDistance, id, format);
		} else {
			if (start == null && currentLocation != null) {
				start = currentLocation;
			}
			if (end == null && currentLocation != null) {
				end = currentLocation;
			}
			if (start != null && end != null) {
				var request = null;
				try {
					request = {
					    origin : start,
					    destination : end,
					    travelMode : google.maps.DirectionsTravelMode.DRIVING
					};
					if (directionsService == null) {
						directionsService = new google.maps.DirectionsService();
					}
				} catch (e) {
					if (debug_level >= 1) {
						console.log(e);
					}
				}
				if (request != null && directionsService != null) {
					directionsService.route(request, function(response, status) {
						if (debug_level >= 1) {
							console.log("directionsService.route response: " + status);
						}
						if (status == google.maps.DirectionsStatus.OK) {
							setDistance(response.routes[0].legs[0].distance.value, id, format);
						} else {
							setDistance(null, id, format);
						}
					});
				} else {
					setDistance(null, id, format);
				}
			} else {
				setDistance(null, id, format);
			}
		}
	}

	function printLocationDistance(distance, id, format) {
		var targetLabelElement = _getElement(id);
		if (targetLabelElement != null) {
			var label = '';
			if (distance != null) {
				label = sprintf(format, distance / 1000.0);
			}
			var textElement = targetLabelElement.contents()[0];
			if (typeof textElement.textContent !== 'undefined') {
				textElement.textContent = label;
			} else {
				textElement.innerText = label;
			}
		}
	}

	LocationDistance.findCurrentLocation = findCurrentLocation;
	LocationDistance.filterAddressByType = filterAddressByType;
	LocationDistance.calcRouteDistance = calcRouteDistance;
	LocationDistance.printLocationDistance = printLocationDistance;

	if (debug_level >= 1) {
		console.log("location utilities loaded");
	}

})(window.LocationDistance);