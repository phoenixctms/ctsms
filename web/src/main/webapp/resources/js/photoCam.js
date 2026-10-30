/**
 * HTML5 (getUserMedia) replacement for the Flash based PrimeFaces PhotoCam widget.
 */
(function(window, $) {

	var DEVICE_ID_STORAGE_KEY = 'ctsms.photoCam.deviceId';
	var STATUS_TIMEOUT = 3000;
	var START_RETRIES = 3;
	var START_RETRY_DELAY = 1000;
	var JPEG_QUALITY = 0.92;
	var activeStreams = {};

	function stopStream(stream) {
		if (stream && stream.getTracks) {
			var tracks = stream.getTracks();
			for (var i = 0; i < tracks.length; i++) {
				tracks[i].stop();
			}
		}
	}

	function getStreamDeviceId(stream) {
		var tracks = stream.getVideoTracks ? stream.getVideoTracks() : [];
		if (tracks.length > 0 && tracks[0].getSettings) {
			return tracks[0].getSettings().deviceId;
		}
		return null;
	}

	function loadDeviceId() {
		try {
			return window.localStorage.getItem(DEVICE_ID_STORAGE_KEY);
		} catch (e) {
			return null;
		}
	}

	function storeDeviceId(deviceId) {
		try {
			if (deviceId) {
				window.localStorage.setItem(DEVICE_ID_STORAGE_KEY, deviceId);
			} else {
				window.localStorage.removeItem(DEVICE_ID_STORAGE_KEY);
			}
		} catch (e) {
		}
	}

	PrimeFaces.widget.PhotoCam = PrimeFaces.widget.BaseWidget.extend({

		init : function(cfg) {
			this._super(cfg);
			this.msgs = cfg.msgs || {};
			this.form = this.jq.parents('form:first');
			this.video = $(this.jqId + '_video')[0];
			this.devices = $(this.jqId + '_devices');
			this.status = $(this.jqId + '_status');
			this.requestCount = this.requestCount || 0;
			this.statusTimer = null;

			var _self = this;
			$(this.video).off('loadedmetadata resize').on('loadedmetadata resize', function() {
				if (this.videoWidth && this.videoHeight) {
					this.parentNode.style.aspectRatio = this.videoWidth + ' / ' + this.videoHeight;
				}
			});
			this.devices.change(function() {
				var deviceId = $(this).val();
				storeDeviceId(deviceId);
				_self.start(deviceId);
			});
			var eventNamespace = '.photocam_' + this.id.replace(/[^A-Za-z0-9_]/g, '_');
			$(window).off('pagehide' + eventNamespace).on('pagehide' + eventNamespace, function() {
				_self.stop();
			});
			$(document).off('visibilitychange' + eventNamespace).on('visibilitychange' + eventNamespace, function() {
				if (document.hidden) {
					if (_self.stream || _self.pending) {
						_self.resume = true;
						_self.stop();
					}
				} else if (_self.resume) {
					_self.resume = false;
					if ($.contains(document.documentElement, _self.video)) {
						_self.start(loadDeviceId());
					}
				}
			});

			if (window.isSecureContext === false) {
				this.showStatus(this.msgs.insecureContext);
			} else if (!navigator.mediaDevices || !navigator.mediaDevices.getUserMedia) {
				this.showStatus(this.msgs.notSupported);
			} else {
				var stream = activeStreams[this.id];
				if (stream && stream.active) {
					this.attach(stream, null);
				} else if (!this.pending) {
					this.start(loadDeviceId());
				}
			}
		},

		attach : function(stream, deviceId) {
			this.stream = stream;
			activeStreams[this.id] = stream;
			this.video.srcObject = stream;
			var playing = this.video.play();
			if (playing && playing.then) {
				playing.then(null, function() {
				});
			}
			this.listDevices(getStreamDeviceId(stream) || deviceId);
		},

		start : function(deviceId, retries) {
			this.stop();
			this.showStatus(null);
			if (retries === undefined) {
				retries = START_RETRIES;
			}
			var constraints = {
				width : {
					ideal : this.cfg.width
				},
				height : {
					ideal : this.cfg.height
				}
			};
			if (deviceId) {
				constraints.deviceId = {
					exact : deviceId
				};
			}
			var _self = this;
			var request = ++this.requestCount;
			this.pending = true;
			navigator.mediaDevices.getUserMedia({
				video : constraints,
				audio : false
			}).then(function(stream) {
				if (request !== _self.requestCount) {
					stopStream(stream);
					return;
				}
				_self.pending = false;
				if (!$.contains(document.documentElement, _self.video)) {
					stopStream(stream);
					return;
				}
				_self.attach(stream, deviceId);
			}, function(error) {
				if (request !== _self.requestCount) {
					return;
				}
				_self.pending = false;
				var retrying = retries < START_RETRIES;
				if (retries > 0 && error && (error.name === 'NotReadableError' || error.name === 'TrackStartError' || error.name === 'AbortError' || (retrying && error.name === 'NotFoundError'))) {
					_self.pending = true;
					window.setTimeout(function() {
						if (request === _self.requestCount) {
							_self.start(deviceId, retries - 1);
						}
					}, START_RETRY_DELAY);
				} else if (retrying && error && error.name === 'NotFoundError') {
					_self.showStatus(_self.msgs.notReadable);
				} else if (deviceId && error && (error.name === 'OverconstrainedError' || error.name === 'NotFoundError')) {
					storeDeviceId(null);
					_self.start(null);
				} else {
					_self.showError(error);
				}
			});
		},

		stop : function() {
			this.requestCount = (this.requestCount || 0) + 1;
			this.pending = false;
			if (this.stream) {
				stopStream(this.stream);
				if (activeStreams[this.id] === this.stream) {
					delete activeStreams[this.id];
				}
				this.stream = null;
			}
			if (this.video) {
				this.video.srcObject = null;
			}
		},

		listDevices : function(currentDeviceId) {
			if (!navigator.mediaDevices.enumerateDevices) {
				return;
			}
			var _self = this;
			navigator.mediaDevices.enumerateDevices().then(function(devices) {
				var cameras = [];
				for (var i = 0; i < devices.length; i++) {
					if (devices[i].kind === 'videoinput') {
						cameras.push(devices[i]);
					}
				}
				_self.devices.empty();
				if (cameras.length < 2) {
					_self.devices.hide();
					return;
				}
				for (var j = 0; j < cameras.length; j++) {
					$('<option/>').val(cameras[j].deviceId).text(cameras[j].label || (_self.msgs.device + ' ' + (j + 1))).appendTo(_self.devices);
				}
				if (currentDeviceId) {
					_self.devices.val(currentDeviceId);
				}
				_self.devices.show();
			}, function() {
				_self.devices.hide();
			});
		},

		showStatus : function(text, timeout) {
			if (this.statusTimer) {
				window.clearTimeout(this.statusTimer);
				this.statusTimer = null;
			}
			if (text) {
				this.status.text(text).show();
				if (timeout) {
					var _self = this;
					this.statusTimer = window.setTimeout(function() {
						_self.statusTimer = null;
						_self.status.hide();
					}, timeout);
				}
			} else {
				this.status.hide().text('');
			}
		},

		showError : function(error) {
			var name = error && error.name ? error.name : String(error);
			switch (name) {
			case 'NotAllowedError':
			case 'PermissionDeniedError':
			case 'SecurityError':
				this.showStatus(this.msgs.permissionDenied);
				break;
			case 'NotFoundError':
			case 'DevicesNotFoundError':
			case 'OverconstrainedError':
				this.showStatus(this.msgs.noDevice);
				break;
			case 'NotReadableError':
			case 'TrackStartError':
				this.showStatus(this.msgs.notReadable);
				break;
			default:
				this.showStatus((this.msgs.error || '{0}').replace('{0}', name));
			}
		},

		capture : function() {
			var video = this.video;
			if (!this.stream || !video || video.readyState < 2 || !video.videoWidth || !video.videoHeight) {
				if (this.stream || !this.status.is(':visible')) {
					this.showStatus(this.msgs.notReady, STATUS_TIMEOUT);
				}
				return;
			}
			var canvas = document.createElement('canvas');
			canvas.width = video.videoWidth;
			canvas.height = video.videoHeight;
			canvas.getContext('2d').drawImage(video, 0, 0, canvas.width, canvas.height);
			PrimeFaces.ajax.AjaxRequest({
				source : this.id,
				formId : this.form.attr('id'),
				process : this.cfg.process ? this.cfg.process + ' ' + this.id : this.id,
				update : this.cfg.update,
				params : [ {
					name : this.cfg.dataParam,
					value : canvas.toDataURL('image/jpeg', JPEG_QUALITY)
				} ]
			});
		}

	});

})(window, jQuery);
