var Sketch = Sketch || {};

(function(Sketch) {
	var strokesIdFieldName = "strokes-id";

	var regionPenOpacity = 0.4;
	var regionFillOpacity = 0.2;

	var defaultInkPenOpacity = 1.0;

	var defaultInkColor = "3c55ee";
	var defaultRegionColor = "00ff00";

	var defaultRegionPenWidth = 2;
	var defaultInkPenWidth = 3;

	var inkPenWidth0 = 12;
	var inkPenWidth1 = 6;
	var inkPenWidth2 = defaultInkPenWidth;
	var inkPenWidth3 = 0.8;

	var inkPenOpacity0 = defaultInkPenOpacity;
	var inkPenOpacity1 = 0.6;
	var inkPenOpacity2 = 0.3;

	var sketchEpsilon = 0.01;

	var debug_level = 0;

	function getSketchValue(sketchpad) {

		var input = jQuery(PrimeFaces.escapeClientId(sketchpad.inputId()));
		return input.val();

	}

	function unpackValue(value) {
		var strokes = [];
		var ids = [];

		try {
			strokes = JSON.parse(value);
		} catch (e) {

		}

		if (strokes.length > 0 && typeof strokes[strokes.length - 1] == "string") {
			ids = strokes.pop().split(ID_SEPARATOR_STRING);
		}

		return { ids: ids, ink: value, strokes: strokes };
	}

	function setSketchValue(sketchpad,value) {

		var input = jQuery(PrimeFaces.escapeClientId(sketchpad.inputId()));
		input.val(value);

		_setSketchValue(sketchpad,value);

	}

	function _setSketchValue(sketchpad,value) {

		if (value != null && value.length > 0) {

			var ink = [];
			var unpackedValue = unpackValue(value);

			if (sketchpad.regionVisible()) {
				jQuery.merge(ink, sketchpad.regions());
			}
			jQuery.merge(ink, unpackedValue.strokes);

			sketchpad.strokes(ink);

		} else {
			sketchpad.clear();
		}
		sketchpad.freeze_history();

	}


	function initSketch(value, inputId, divId, width, height, backgroundImage, enabled, onChange, strokesId) {

		var input = jQuery(PrimeFaces.escapeClientId(inputId));
		var div = jQuery(PrimeFaces.escapeClientId(divId));
		input.off();
		div.off();

		var regionVisible = true;
		var regions = [];
		for ( var i = 9; i < arguments.length; i++) {
			var region = [];
			try {
				region = JSON.parse(arguments[i]);
			} catch (e) {

			}
			if (region) {
				jQuery.merge(regions, region);
			}
		}

		var sketchpad = Raphael.sketchpad(divId, {
		    height : height,
		    width : width,
		    editing : enabled,
		    _regions: regions,
		    regionVisible: regionVisible,
		    _inputId: inputId
		});

		var pen = sketchpad.pen();

		var defaultColor;
		var draw = enabled;

		var regionEdit = strokesId != null && strokesId.length > 0;

		var silent = false;

		_setSketchValue(sketchpad,value);

		if (enabled) {

			var undo = jQuery(PrimeFaces.escapeClientId(divId + "_undo"));
			undo.off();
			var redo = jQuery(PrimeFaces.escapeClientId(divId + "_redo"));
			redo.off();
			var clear = jQuery(PrimeFaces.escapeClientId(divId + "_clear"));
			clear.off();

			if (regionEdit) {

				defaultColor = defaultRegionColor;

				pen.width(defaultRegionPenWidth);
				pen.color("#" + defaultRegionColor);
				pen.opacity(regionPenOpacity);

				sketchpad.change(function() {
					if (!silent) {
						_updateButtons(sketchpad, undo, redo, clear, regions);
						input.val(sketchpad.json());
					}
				});

				sketchpad.strokedrawn(function(stroke) {
					stroke.path.push([ "Z" ]);
					stroke.fill = pen.color();
					stroke["fill-opacity"] = regionFillOpacity;

					stroke[strokesIdFieldName] = strokesId;
					return true; // redraw
				});
				sketchpad.patchstroke(function(stroke) {
					stroke[strokesIdFieldName] = strokesId;
					return false;
				});
			} else {

				defaultColor = defaultInkColor;

				pen.width(defaultInkPenWidth);
				pen.color("#" + defaultInkColor);
				pen.opacity(defaultInkPenOpacity);

				var intersectionsArray = [];
				var intersections = [];
				var containmentArray = [];
				var ids = [];
				var idMap = {};

				var regionToggler = jQuery(PrimeFaces.escapeClientId(divId + "_regionToggler"));
				regionToggler.off();

				var penWidth0 = jQuery(PrimeFaces.escapeClientId(divId + "_penWidth0"));
				penWidth0.off();
				var penWidth1 = jQuery(PrimeFaces.escapeClientId(divId + "_penWidth1"));
				penWidth1.off();
				var penWidth2 = jQuery(PrimeFaces.escapeClientId(divId + "_penWidth2"));
				penWidth2.off();
				var penWidth3 = jQuery(PrimeFaces.escapeClientId(divId + "_penWidth3"));
				penWidth3.off();

				var penOpacity0 = jQuery(PrimeFaces.escapeClientId(divId + "_penOpacity0"));
				penOpacity0.off();
				var penOpacity1 = jQuery(PrimeFaces.escapeClientId(divId + "_penOpacity1"));
				penOpacity1.off();
				var penOpacity2 = jQuery(PrimeFaces.escapeClientId(divId + "_penOpacity2"));
				penOpacity2.off();

				sketchpad.change(function() {

					if (!silent) {

						_updateButtons(sketchpad, undo, redo, clear, regions);
						regionVisible = _updateRegionToggler(sketchpad, regionToggler, regionVisible);
						sketchpad.regionVisible(regionVisible);

						var strokes = sketchpad.strokes();
						var ink = [];
						var inkPrev = [];

						var strokeBBs = [];

						for ( var i = 0; i < strokes.length; i++) {
							var stroke = strokes[i];
							if (!(strokesIdFieldName in stroke)) {
								ink.push(stroke);
							}
						}

						if (ink.length < intersectionsArray.length) {
							intersectionsArray = [];
							containmentArray = [];

							intersections = [];
							ids = [];
							idMap = {};
							if (debug_level >= 1) {
								console.log("strokes removed");
							}
						} else {
							if (debug_level >= 1) {
								console.log("strokes added");
							}
						}
						_redimArray2D(intersectionsArray, ink.length);
						_redimArray2D(containmentArray, regions.length);

						if (regions.length > 0) {
							for (var i = 0; i < ink.length; i++) {
								var stroke = ink[i];

								var strokeBB = _getBoundingBox(stroke);
								for ( var j = intersectionsArray[i]; j < inkPrev.length; j++) {

									if (Raphael.isBBoxIntersect(strokeBBs[j], strokeBB)) {
										var intersection = _interPathHelper(inkPrev[j], stroke.path);
										if (intersection.length > 0) {
											jQuery.merge(intersections, intersection);
											if (debug_level >= 1) {
												console.log("intersections stroke " + i + " and stroke " + j + ": " + intersection);
											}
										}
									}

								}
								intersectionsArray[i] = inkPrev.length;

								inkPrev.push(stroke.path);
								strokeBBs.push(strokeBB);

							}
						}
						for (var i = 0; i < regions.length; i++) {
							var region = regions[i];
							var selectionSetValueStrokesId = region[strokesIdFieldName];
							if (selectionSetValueStrokesId && !(selectionSetValueStrokesId in idMap)) {

								var regionBB = _getBoundingBox(region);
								for (var j = containmentArray[i]; j < intersections.length; j++) {
									if (!(selectionSetValueStrokesId in idMap)) {
										var intersection = intersections[j];
										var x = intersection[0];
										var y = intersection[1];
										if (Raphael.isPointInsideBBox(regionBB, x, y) && _isPointInsidePath(region.path, x, y)) { // Raphael.isPointInsidePath(regionSVG,
																																	// x,
																																	// y))
																																	// {
											ids.push(selectionSetValueStrokesId);
											idMap[selectionSetValueStrokesId] = true;
											if (debug_level >= 1) {
												console.log("intersection x=" + x + " y=" + y + " hits region " + selectionSetValueStrokesId);
											}
										} else {
											if (debug_level >= 1) {
												console.log("intersection x=" + x + " y=" + y + " not within region " + selectionSetValueStrokesId);
											}
										}

									}
								}
								containmentArray[i] = intersections.length;
							}
						}

						ink.push(ids.join(ID_SEPARATOR_STRING));
						if (debug_level >= 1) {
							console.log("selected region: " + ids.join(ID_SEPARATOR_STRING));
							console.log(ids.length + " regions");
						}

						input.val(JSON.stringify(ink));

//						if (onChange != null && onChange.length > 0) {
//							(new Function(onChange)).call({
//							    'sketchpad' : sketchpad,
//							    'ids' : ids,
//							    'ink' : input.val()
//							});
//						}

						if (onChange != null && onChange.length > 0) {
							(new Function(onChange)).call(sketchpad);
						}

					}
				});

				sketchpad.patchstroke(function(stroke) {
					var strokes = sketchpad.strokes();

					for ( var i = 0; i < strokes.length; i++) {
						var s = strokes[i];
						if (strokesIdFieldName in s) {
							if (equiv(s.path, stroke.path)) {
								return true;
							}
						}
					}

					return false;
				});

				regionToggler.mousedown(function() {
					regionToggler[0].className = "sketch-region-toggler-pressed";
				});
				regionToggler.mouseup(function() {
					regionVisible = !regionVisible;
					sketchpad.regionVisible(regionVisible);

					var ink = [];
					if (regionVisible) {
						jQuery.merge(ink, regions);
					}
					var strokes = sketchpad.strokes();
					for ( var i = 0; i < strokes.length; i++) {
						var stroke = strokes[i];
						if (!(strokesIdFieldName in stroke)) {
							ink.push(stroke);
						}
					}

					sketchpad.strokes(ink);

				});

				regionVisible = _updateRegionToggler(sketchpad, regionToggler, regionVisible);
				sketchpad.regionVisible(regionVisible);

				penWidth0.mousedown(function() {
					penWidth0[0].className = "sketch-pen-width-pressed";
				});
				penWidth0.mouseup(function() {
					pen.width(inkPenWidth0);
					penWidth0[0].className = "sketch-pen-width-0";
					penWidth1[0].className = "sketch-pen-width-1-disabled";
					penWidth2[0].className = "sketch-pen-width-2-disabled";
					penWidth3[0].className = "sketch-pen-width-3-disabled";
				});

				penWidth1.mousedown(function() {
					penWidth1[0].className = "sketch-pen-width-pressed";
				});
				penWidth1.mouseup(function() {
					pen.width(inkPenWidth1);
					penWidth0[0].className = "sketch-pen-width-0-disabled";
					penWidth1[0].className = "sketch-pen-width-1";
					penWidth2[0].className = "sketch-pen-width-2-disabled";
					penWidth3[0].className = "sketch-pen-width-3-disabled";
				});

				penWidth2.mousedown(function() {
					penWidth2[0].className = "sketch-pen-width-pressed";
				});
				penWidth2.mouseup(function() {
					pen.width(inkPenWidth2);
					penWidth0[0].className = "sketch-pen-width-0-disabled";
					penWidth1[0].className = "sketch-pen-width-1-disabled";
					penWidth2[0].className = "sketch-pen-width-2";
					penWidth3[0].className = "sketch-pen-width-3-disabled";
				});

				penWidth3.mousedown(function() {
					penWidth3[0].className = "sketch-pen-width-pressed";
				});
				penWidth3.mouseup(function() {
					pen.width(inkPenWidth3);
					penWidth0[0].className = "sketch-pen-width-0-disabled";
					penWidth1[0].className = "sketch-pen-width-1-disabled";
					penWidth2[0].className = "sketch-pen-width-2-disabled";
					penWidth3[0].className = "sketch-pen-width-3";
				});

				penOpacity0.mousedown(function() {
					penOpacity0[0].className = "sketch-pen-opacity-pressed";
				});
				penOpacity0.mouseup(function() {
					pen.opacity(inkPenOpacity0);
					penOpacity0[0].className = "sketch-pen-opacity-0";
					penOpacity1[0].className = "sketch-pen-opacity-1-disabled";
					penOpacity2[0].className = "sketch-pen-opacity-2-disabled";
				});

				penOpacity1.mousedown(function() {
					penOpacity1[0].className = "sketch-pen-opacity-pressed";
				});
				penOpacity1.mouseup(function() {
					pen.opacity(inkPenOpacity1);
					penOpacity0[0].className = "sketch-pen-opacity-0-disabled";
					penOpacity1[0].className = "sketch-pen-opacity-1";
					penOpacity2[0].className = "sketch-pen-opacity-2-disabled";
				});

				penOpacity2.mousedown(function() {
					penOpacity2[0].className = "sketch-pen-opacity-pressed";
				});
				penOpacity2.mouseup(function() {
					pen.opacity(inkPenOpacity2);
					penOpacity0[0].className = "sketch-pen-opacity-0-disabled";
					penOpacity1[0].className = "sketch-pen-opacity-1-disabled";
					penOpacity2[0].className = "sketch-pen-opacity-2";
				});

			}

			var colorPicker = jQuery(PrimeFaces.escapeClientId(inputId + "_colorPicker"));
			colorPicker.off();
			colorPicker.colorPicker({
			    pickerDefault : defaultColor,
			    addColors : [ defaultColor ],
			    transparency : false,

			});

			colorPicker.change(function() {
				pen.color(colorPicker.val());
			});

			var drawEraseMode = jQuery(PrimeFaces.escapeClientId(divId + "_drawEraseMode"));
			drawEraseMode.off();
			drawEraseMode.mousedown(function() {
				drawEraseMode[0].className = "sketch-draw-mode-pressed";
			});
			drawEraseMode.mouseup(function() {
				draw = !draw;
				if (draw) {
					sketchpad.editing(true);
				} else {
					sketchpad.editing("erase");
				}
				if (draw) {
					drawEraseMode[0].className = "sketch-draw-mode";
				} else {
					drawEraseMode[0].className = "sketch-erase-mode";
				}
			});

			undo.mousedown(function() {
				if (sketchpad.undoable()) {
					undo[0].className = "sketch-undo-pressed";
				}
			});
			undo.mouseup(function() {
				if (sketchpad.undoable()) {
					sketchpad.undo();

				}
			});

			redo.mousedown(function() {
				if (sketchpad.redoable()) {
					redo[0].className = "sketch-redo-pressed";
				}
			});
			redo.mouseup(function() {
				if (sketchpad.redoable()) {
					sketchpad.redo();

				}
			});

			clear.mousedown(function() {
				if (sketchpad.strokes().length > 0) {
					clear[0].className = "sketch-clear-pressed";
				}
			});
			clear.mouseup(function() {
				if (sketchpad.strokes().length > 0) {

					sketchpad.clear();

				}
			});

			_updateButtons(sketchpad, undo, redo, clear, regions);

		}

		div.css('width', width + 'px');
		div.css('height', height + 'px');
		if (backgroundImage != null && backgroundImage.length > 0) {
			div.css('background-image', 'url(' + backgroundImage + ')');
		}

		return sketchpad;
	}

	function _getBoundingBox(stroke) {
		if (typeof stroke.path == "string") {
			stroke.path = _stringToSvgPath(stroke.path);
		}
		var path = stroke.path;
		var minX = Number.POSITIVE_INFINITY;
		var maxX = 0.0;
		var minY = Number.POSITIVE_INFINITY;
		var maxY = 0.0;
		for ( var i = 0, ii = path.length; i < ii; i++) {
			var pi = path[i];
			if (pi.length == 3) {
				minX = Math.min(minX, pi[1]);
				maxX = Math.max(maxX, pi[1]);
				minY = Math.min(minY, pi[2]);
				maxY = Math.max(maxY, pi[2]);
			}
		}
		return {
		    x : minX,
		    y : minY,
		    x2 : maxX,
		    y2 : maxY,
		    width : maxX - minX,
		    height : maxY - minY
		};
	}

	// from sketchpad.
	function _stringToSvgPath(str) {
		var path = [];
		var tokens = str.split("L");

		if (tokens.length > 0) {
			var token = tokens[0].replace("M", "");
			var point = token.split(",");
			path.push(["M", parseInt(point[0]), parseInt(point[1])]);

			for (var i = 1, n = tokens.length; i < n - 1; i++) {
				token = tokens[i];
				point = token.split(",");
				path.push(["L", parseInt(point[0]), parseInt(point[1])]);
			}

			token = tokens[tokens.length - 1];
			if (tokens.length >= 2) {
				var closePath = false;
				if (token.indexOf("Z") == (token.length - 1)) {
					token = token.substring(0,token.length - 1);
					closePath = true;
				}
				point = token.split(",");
				path.push(["L", parseInt(point[0]), parseInt(point[1])]);
				if (closePath) {
					path.push(["Z"]);
				}
			}
		}

		return path;
	}

	function _isPointInsidePath(region, x, y) {
		// Choose coordinates that are definitely outside the shape.
		// Make a line from that point to your actual point in question.
		// Count, how often the line intersects with the path.
		// if the number of intersections is odd, then your point is inside. If
		// it's even, the point is outside.

		// return Raphael.isPointInsidePath(regionSVG, x, y);

		var regionPath = _closePath(region);
		var testPath = [ [ "M", -1.0, -1.0 ], [ "L", x, y ] ];

		return (_interPathHelper(regionPath, testPath).length % 2) == 1;

	}

	function _closePath(region) {
		var regionPath = jQuery.merge([], region);
		var start = regionPath[0];
		var end = regionPath[regionPath.length - 1];

		if (end.length != 3) {
			end = regionPath[regionPath.length - 2];
			regionPath.pop();
		}
		regionPath.push([ "L", start[1], start[2] ]);
		return regionPath;
	}

	function _interPathHelper(path1, path2) {
		var ax1, ay1, ax2, ay2;
		var bx1, by1, bx2, by2;
		var res = [];
		for ( var i = 0, ii = path1.length; i < ii; i++) {
			var pi = path1[i];
			if (pi[0] == "M") {
				ax1 = pi[1];
				ay1 = pi[2];
			} else {
				ax2 = pi[1];
				ay2 = pi[2];
				for ( var j = 0, jj = path2.length; j < jj; j++) {
					var pj = path2[j];
					if (pj[0] == "M") {
						bx1 = pj[1];
						by1 = pj[2];
					} else {
						bx2 = pj[1];
						by2 = pj[2];
						var intr = _getIntersection(ax1, ax2, ay1, ay2, bx1, bx2, by1, by2);
						if (intr.length > 0) {
							res.push(intr);
						}
						bx1 = bx2;
						by1 = by2;
					}
				}
				ax1 = ax2;
				ay1 = ay2;
			}
		}
		return res;
	}

	function _getIntersection(ax1, ax2, ay1, ay2, bx1, bx2, by1, by2) {

		var A1 = ay2 - ay1;
		var B1 = ax1 - ax2;
		var C1 = A1 * ax1 + B1 * ay1;

		var A2 = by2 - by1;
		var B2 = bx1 - bx2;
		var C2 = A2 * bx1 + B2 * by1;

		var delta = A1 * B2 - A2 * B1;
		if (delta != 0.0) {
			var x = (B2 * C1 - B1 * C2) / delta;
			var y = (A1 * C2 - A2 * C1) / delta;
			if (Math.min(ax1, ax2) - x <= sketchEpsilon && x - Math.max(ax1, ax2) <= sketchEpsilon && Math.min(ay1, ay2) - y <= sketchEpsilon
			        && y - Math.max(ay1, ay2) <= sketchEpsilon && Math.min(bx1, bx2) - x <= sketchEpsilon && x - Math.max(bx1, bx2) <= sketchEpsilon
			        && Math.min(by1, by2) - y <= sketchEpsilon && y - Math.max(by1, by2) <= sketchEpsilon) {
				return [ x, y ];
			}
		}
		return [];

	}

	function _updateButtons(sketchpad, undo, redo, clear, regions) {
		if (sketchpad.redoable()) {
			redo[0].className = "sketch-redo";
		} else {
			redo[0].className = "sketch-redo-disabled";
		}
		if (sketchpad.undoable()) {
			undo[0].className = "sketch-undo";
		} else {
			undo[0].className = "sketch-undo-disabled";
		}
		if (sketchpad.strokes().length > 0) {
			clear[0].className = "sketch-clear";
		} else {
			clear[0].className = "sketch-clear-disabled";
		}

	}

	function _updateRegionToggler(sketchpad, regionToggler, regionVisible) {
		var on = regionVisible;
		if (sketchpad.strokes().length == 0) {
			on = false;
		}
		if (on) {
			regionToggler[0].className = "sketch-region-toggler-on";
		} else {
			regionToggler[0].className = "sketch-region-toggler-off";
		}
		return on;
	}

	function _redimArray2D(array, x) {
		for ( var i = 0; i < x; i++) {
			if (!array[i]) {
				array[i] = 0;
			}
		}
	}

//	function _pointsToSVG(path) {
//		var svgStr = "";
//		for ( var i = 0; i < path.length; i++) {
//			var point = path[i];
//			if (point.length == 3) {
//				svgStr += point[0] + point[1] + "," + point[2];
//			} else {
//				svgStr += point[0];
//			}
//		}
//		return svgStr;
//	}

	Sketch.initSketch = initSketch;
	Sketch.setSketchValue = setSketchValue;
	Sketch.getSketchValue = getSketchValue;
	Sketch.unpackValue = unpackValue;

	if (debug_level >= 1) {
		console.log("sketch utilities loaded");
	}

})(window.Sketch);
