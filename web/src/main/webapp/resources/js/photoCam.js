/**
 * HTML5 (getUserMedia) replacement for the Flash based PrimeFaces PhotoCam widget.
 */
(function(window, $) {

	var DEVICE_ID_STORAGE_KEY = 'ctsms.photoCam.deviceId';
	var STATUS_TIMEOUT = 3000;
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
			this.requestCount = (this.requestCount || 0) + 1;
			this.statusTimer = null;

			stopStream(this.stream);
			this.stream = null;
			stopStream(activeStreams[this.id]);
			delete activeStreams[this.id];

			var _self = this;
			this.devices.change(function() {
				var deviceId = $(this).val();
				storeDeviceId(deviceId);
				_self.start(deviceId);
			});
			var eventNamespace = '.photocam_' + this.id.replace(/[^A-Za-z0-9_]/g, '_');
			$(window).off('pagehide' + eventNamespace).on('pagehide' + eventNamespace, function() {
				_self.stop();
			});

			if (window.isSecureContext === false) {
				this.showStatus(this.msgs.insecureContext);
			} else if (!navigator.mediaDevices || !navigator.mediaDevices.getUserMedia) {
				this.showStatus(this.msgs.notSupported);
			} else {
				this.start(loadDeviceId());
			}
		},

		start : function(deviceId) {
			this.stop();
			this.showStatus(null);
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
			navigator.mediaDevices.getUserMedia({
				video : constraints,
				audio : false
			}).then(function(stream) {
				if (request !== _self.requestCount || !$.contains(document.documentElement, _self.video)) {
					stopStream(stream);
					return;
				}
				_self.stream = stream;
				activeStreams[_self.id] = stream;
				_self.video.srcObject = stream;
				var playing = _self.video.play();
				if (playing && playing.then) {
					playing.then(null, function() {
					});
				}
				_self.listDevices(getStreamDeviceId(stream) || deviceId);
			}, function(error) {
				if (request !== _self.requestCount) {
					return;
				}
				if (deviceId && error && (error.name === 'OverconstrainedError' || error.name === 'NotFoundError')) {
					storeDeviceId(null);
					_self.start(null);
				} else {
					_self.showError(error);
				}
			});
		},

		stop : function() {
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
			var width = this.cfg.width;
			var height = this.cfg.height;
			var sourceWidth = video.videoWidth;
			var sourceHeight = video.videoHeight;
			var targetRatio = width / height;
			var sx = 0, sy = 0, sw = sourceWidth, sh = sourceHeight;
			if (sourceWidth / sourceHeight > targetRatio) {
				sw = sourceHeight * targetRatio;
				sx = (sourceWidth - sw) / 2;
			} else {
				sh = sourceWidth / targetRatio;
				sy = (sourceHeight - sh) / 2;
			}
			var canvas = document.createElement('canvas');
			canvas.width = width;
			canvas.height = height;
			canvas.getContext('2d').drawImage(video, sx, sy, sw, sh, 0, 0, width, height);
			PrimeFaces.ajax.AjaxRequest({
				source : this.id,
				formId : this.form.attr('id'),
				process : this.cfg.process ? this.cfg.process + ' ' + this.id : this.id,
				update : this.cfg.update,
				params : [ {
					name : this.cfg.dataParam,
					value : canvas.toDataURL('image/png')
				} ]
			});
		}

	});

})(window, jQuery);
