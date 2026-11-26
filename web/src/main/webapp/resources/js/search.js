var oldSearchTabIndex = 0;

function syncQueryTextEditor() {
	if (typeof queryTextEditor !== "undefined" && queryTextEditor && queryTextEditor.instance) {
		queryTextEditor.instance.save();
	}
}

function queryTextEditorValue() {
	if (typeof queryTextEditor !== "undefined" && queryTextEditor && queryTextEditor.instance) {
		return queryTextEditor.instance.getValue() || "";
	}
	return "";
}

function updateQueryTextActionButtons() {
	var empty = queryTextEditorValue().replace(/^\s+|\s+$/g, "").length === 0;
	var buttons = document.getElementsByClassName("ctsms-query-text-requires-content");
	for (var i = 0; i < buttons.length; i++) {
		buttons[i].disabled = empty;
		var className = buttons[i].className;
		if (empty) {
			if (className.indexOf("ui-state-disabled") < 0) {
				buttons[i].className = className + " ui-state-disabled";
			}
		} else {
			buttons[i].className = className.replace(/(^|\s)ui-state-disabled(\s|$)/g, " ");
		}
	}
}

function enableQueryTextButtons(styleClass, enabled) {
	var buttons = document.getElementsByClassName(styleClass);
	for (var i = 0; i < buttons.length; i++) {
		buttons[i].disabled = !enabled;
		if (enabled) {
			buttons[i].className = buttons[i].className.replace(/\s*ui-state-disabled/g, "");
		} else if (buttons[i].className.indexOf("ui-state-disabled") < 0) {
			buttons[i].className = buttons[i].className + " ui-state-disabled";
		}
	}
}

function setQueryTextBusy(busy) {
	if (typeof queryTextEditor !== "undefined" && queryTextEditor && queryTextEditor.instance) {
		queryTextEditor.instance.setOption("readOnly", busy);
	}
	// the stop button is only usable while a generation runs, apply only while none does
	enableQueryTextButtons("ctsms-query-text-cancel", busy);
	enableQueryTextButtons("ctsms-query-text-apply", !busy);
	if (!busy) {
		updateQueryTextActionButtons();
		return;
	}
	var buttons = document.getElementsByClassName("ctsms-query-text-requires-content");
	for (var i = 0; i < buttons.length; i++) {
		buttons[i].disabled = true;
		if (buttons[i].className.indexOf("ui-state-disabled") < 0) {
			buttons[i].className = buttons[i].className + " ui-state-disabled";
		}
	}
}

function bindQueryTextActionButtons() {
	if (typeof queryTextEditor === "undefined" || !queryTextEditor || !queryTextEditor.instance) {
		return;
	}
	var cm = queryTextEditor.instance;
	if (!cm.ctsmsQueryTextButtonsBound) {
		cm.ctsmsQueryTextButtonsBound = true;
		cm.setOption("onKeyEvent", handleQueryTextKeyEvent);
		if (typeof cm.on === "function") {
			cm.on("change", updateQueryTextActionButtons);
			cm.on("change", clearQueryTextErrorLine);
		} else if (typeof cm.setOption === "function") {
			var previous = typeof cm.getOption === "function" ? cm.getOption("onChange") : null;
			cm.setOption("onChange", function(editor, change) {
				if (typeof previous === "function") {
					previous(editor, change);
				}
				updateQueryTextActionButtons();
				clearQueryTextErrorLine();
			});
		}
	}
	updateQueryTextActionButtons();
}

var QUERY_TEXT_EDITOR_MIN_HEIGHT = 120;
var QUERY_TEXT_EDITOR_MAX_HEIGHT = 360;

// gives the editor the height the format row, the step log and the toolbar leave in the panel, so the panel does not scroll
function sizeQueryTextEditor() {
	if (typeof queryTextEditor === "undefined" || !queryTextEditor || !queryTextEditor.instance) {
		return;
	}
	var wrapper = queryTextEditor.instance.getWrapperElement();
	var panels = document.getElementsByClassName("ctsms-query-text-panel");
	if (!wrapper || panels.length === 0 || wrapper.offsetParent === null) {
		// the tab is hidden, nothing to measure
		return;
	}
	var panel = panels[0];
	var style = window.getComputedStyle(panel);
	var maxHeight = parseFloat(style.maxHeight);
	if (!isNaN(maxHeight)) {
		// max-height covers the content box unless the panel is border-box, scrollHeight includes the padding
		var padding = parseFloat(style.paddingTop) + parseFloat(style.paddingBottom);
		var available = style.boxSizing === "border-box" ? maxHeight - padding - parseFloat(style.borderTopWidth) - parseFloat(style.borderBottomWidth) : maxHeight;
		var others = panel.scrollHeight - padding - wrapper.offsetHeight;
		setQueryTextEditorHeight(wrapper, available - others - 2);
	}
	// with display scaling, fractional pixels can leave a pixel of overflow, which must not show a scrollbar
	panel.style.overflowY = panel.scrollHeight - panel.clientHeight > 2 ? "auto" : "hidden";
}

function setQueryTextEditorHeight(wrapper, height) {
	height = Math.max(QUERY_TEXT_EDITOR_MIN_HEIGHT, Math.min(QUERY_TEXT_EDITOR_MAX_HEIGHT, Math.floor(height)));
	if (height !== wrapper.offsetHeight) {
		// height is the outer height, the style height excludes the border of the editor
		wrapper.style.height = (height - (wrapper.offsetHeight - wrapper.clientHeight)) + "px";
		queryTextEditor.instance.refresh();
	}
	return height;
}

var queryTextEditorResizeBound = false;
function bindQueryTextEditorResize() {
	if (!queryTextEditorResizeBound) {
		queryTextEditorResizeBound = true;
		jQuery(window).on("resize", sizeQueryTextEditor);
	}
}

var QUERY_TEXT_EXPRESSION_MODE = "ctsmsquerytext";

// the javascript mode marks # as an error. In a query expression, # followed by a blank starts a comment, and #Diagnoses is a property name.
function defineQueryTextExpressionMode() {
	if (typeof CodeMirror === "undefined" || typeof CodeMirror.defineMode !== "function" || CodeMirror.modes == null) {
		return false;
	}
	if (!CodeMirror.modes[QUERY_TEXT_EXPRESSION_MODE]) {
		CodeMirror.defineMode(QUERY_TEXT_EXPRESSION_MODE, function(config) {
			var base = CodeMirror.getMode(config, "javascript");
			return {
				startState : function(indentation) {
					return base.startState(indentation);
				},
				copyState : function(state) {
					return CodeMirror.copyState(base, state);
				},
				token : function(stream, state) {
					if (stream.peek() === "#") {
						if (stream.match(/^#(\s|$)/, false)) {
							stream.skipToEnd();
							return "comment";
						}
						stream.next();
						return "variable";
					}
					return base.token(stream, state);
				},
				indent : function(state, textAfter) {
					return typeof base.indent === "function" ? base.indent(state, textAfter) : 0;
				},
				electricChars : base.electricChars
			};
		});
	}
	return true;
}

function useQueryTextExpressionMode() {
	if (typeof queryTextEditor === "undefined" || !queryTextEditor || !queryTextEditor.instance) {
		return;
	}
	var cm = queryTextEditor.instance;
	// the JSON format has an object as mode and keeps the javascript mode, where // comments already work
	if (cm.getOption("mode") === "javascript" && defineQueryTextExpressionMode()) {
		cm.setOption("mode", QUERY_TEXT_EXPRESSION_MODE);
	}
}

// query text editor suggestions: the names of SearchBeanBase.getQueryTextHintsJson, the expression ones or the JSON keys.
// They are loaded once the first suggestions are requested, which are shown when they arrive
var queryTextHints = null;
var queryTextHintsLoading = false;
var queryTextHintsPending = null;

function getQueryTextHints(cm, typed) {
	if (queryTextHints == null) {
		queryTextHintsPending = {
			cm : cm,
			typed : typed
		};
		if (!queryTextHintsLoading && typeof loadQueryTextHints === "function") {
			queryTextHintsLoading = true;
			loadQueryTextHints();
		}
	}
	return queryTextHints;
}

function handleQueryTextHintsLoaded(xhr, status, args) {
	queryTextHintsLoading = false;
	var data = null;
	if (args && args.queryTextHintsBase64) {
		try {
			data = JSON.parse(decodeBase64(args.queryTextHintsBase64));
		} catch (e) {
		}
	}
	if (data == null) {
		return;
	}
	// the suggestions keep the order of the query builder, names are matched against the ...ByLength lists
	var restrictions = data.restrictions || [];
	queryTextHints = {
		ties : data.ties || [],
		properties : data.properties || []
	};
	queryTextHints.tiesByLength = sortQueryTextHintsByLength(queryTextHints.ties);
	queryTextHints.propertiesByLength = sortQueryTextHintsByLength(queryTextHints.properties);
	for (var i = 0; i < queryTextHints.properties.length; i++) {
		var property = queryTextHints.properties[i];
		var indexes = property.restrictions || [];
		property.restrictions = [];
		for (var j = 0; j < indexes.length; j++) {
			if (restrictions[indexes[j]] != null) {
				property.restrictions.push(restrictions[indexes[j]]);
			}
		}
		property.restrictionsByLength = sortQueryTextHintsByLength(property.restrictions);
		property.values = property.values || [];
	}
	var pending = queryTextHintsPending;
	queryTextHintsPending = null;
	if (pending != null && document.activeElement === pending.cm.getInputField()) {
		if (pending.typed) {
			showTypedCodeMirrorHint(pending.cm, typedQueryTextHint);
		} else {
			showQueryTextHint(pending.cm);
		}
	}
}

// the values the complete methods of the properties found, by property id and then by the value typed
var queryTextValueHints = {};
var queryTextValueHintsLoading = false;
var queryTextValueHintsRequest = null;
var queryTextValueHintsTimer = null;
var QUERY_TEXT_VALUE_HINTS_DELAY_MS = 300;

// until the values of the typed text are loaded, those of the longest typed text before it, which the suggestions filter further
function getQueryTextValueHints(cm, typed, property, query) {
	var cached = queryTextValueHints[property.id];
	if (cached == null) {
		cached = queryTextValueHints[property.id] = {};
	}
	if (cached.hasOwnProperty(query)) {
		return cached[query];
	}
	queryTextValueHintsRequest = {
		cm : cm,
		typed : typed,
		id : property.id,
		query : query
	};
	if (queryTextValueHintsTimer != null) {
		window.clearTimeout(queryTextValueHintsTimer);
	}
	queryTextValueHintsTimer = window.setTimeout(sendQueryTextValueHints, QUERY_TEXT_VALUE_HINTS_DELAY_MS);
	var lowerCaseQuery = query.toLowerCase();
	var best = null;
	for ( var known in cached) {
		if (cached.hasOwnProperty(known) && lowerCaseQuery.indexOf(known.toLowerCase()) == 0 && (best == null || known.length > best.length)) {
			best = known;
		}
	}
	return best != null ? cached[best] : null;
}

// one request at a time, the latest value typed is sent once it is back
function sendQueryTextValueHints() {
	queryTextValueHintsTimer = null;
	var request = queryTextValueHintsRequest;
	if (request == null || queryTextValueHintsLoading || typeof loadQueryTextValueHints !== "function") {
		return;
	}
	queryTextValueHintsLoading = true;
	loadQueryTextValueHints([ {
		name : "criterionpropertyid",
		value : request.id
	}, {
		name : "valuequery",
		value : request.query
	} ]);
}

function handleQueryTextValueHintsLoaded(xhr, status, args) {
	queryTextValueHintsLoading = false;
	var data = null;
	if (args && args.queryTextValueHintsBase64) {
		try {
			data = JSON.parse(decodeBase64(args.queryTextValueHintsBase64));
		} catch (e) {
		}
	}
	var request = queryTextValueHintsRequest;
	if (data == null || data.id == null) {
		queryTextValueHintsRequest = null;
		return;
	}
	var query = data.query || "";
	if (queryTextValueHints[data.id] == null) {
		queryTextValueHints[data.id] = {};
	}
	queryTextValueHints[data.id][query] = data.values || [];
	if (request == null) {
		return;
	}
	if (request.id != data.id || request.query != query) {
		if (queryTextValueHintsTimer == null) {
			sendQueryTextValueHints();
		}
		return;
	}
	queryTextValueHintsRequest = null;
	if (document.activeElement === request.cm.getInputField()) {
		if (request.typed) {
			showTypedCodeMirrorHint(request.cm, typedQueryTextHint);
		} else {
			showQueryTextHint(request.cm);
		}
	}
}

// longest first, so a name is not taken for the start of a longer one, like the server's parser does
function sortQueryTextHintsByLength(hints) {
	var sorted = hints.slice(0);
	sorted.sort(function(a, b) {
		return (b.name || "").length - (a.name || "").length;
	});
	return sorted;
}

function getQueryTextHintFields(hints, field) {
	var names = [];
	for (var i = 0; i < hints.length; i++) {
		if (hints[i][field] != null && jQuery.inArray(hints[i][field], names) < 0) {
			names.push(hints[i][field]);
		}
	}
	return names;
}

function isQueryTextBoundary(c) {
	return /[\s<()>=!#]/.test(c);
}

// the hint whose name is at offset of the text, the longest one, followed by the end of the text or a boundary (see CriterionText.matchLongest)
function matchQueryTextHint(text, offset, hints) {
	for (var i = 0; i < hints.length; i++) {
		var name = hints[i].name || "";
		var end = offset + name.length;
		if (name.length == 0 || end > text.length || text.substring(offset, end).toLowerCase() != name.toLowerCase()) {
			continue;
		}
		if (end == text.length || isQueryTextBoundary(text.charAt(end)) || (/[=<>!]$/.test(name) && /[\w".+\-]/.test(text.charAt(end)))) {
			return hints[i];
		}
	}
	return null;
}

function skipQueryTextBlanks(text, i) {
	while (i < text.length && /[ \t]/.test(text.charAt(i))) {
		i++;
	}
	return i;
}

// the end of the value starting at i: a tie other than a parenthesis ends it (see CriterionText.scanValueEnd)
function scanQueryTextValueEnd(text, i, hints) {
	for (var end = i; end < text.length; end++) {
		if (end == i || isQueryTextBoundary(text.charAt(end - 1))) {
			var tie = matchQueryTextHint(text, end, hints.tiesByLength);
			if (tie != null && tie.key != "LEFT_PARENTHESIS" && tie.key != "RIGHT_PARENTHESIS") {
				return {
					end : end,
					tie : tie
				};
			}
		}
	}
	return {
		end : text.length,
		tie : null
	};
}

function queryTextHintResult(cursor, start, query, candidates, typed) {
	var list = filterHintNames(candidates, query);
	if (typed && (list.length == 0 || (list.length == 1 && list[0].toLowerCase() == query.toLowerCase()))) {
		return null;
	}
	return {
		list : list,
		from : {
			line : cursor.line,
			ch : start
		},
		to : cursor
	};
}

// expression lines: [position:] [tie] property operator value, where a tie can also start another criterion on the line
function queryTextExpressionHint(cm, typed) {
	var cursor = cm.getCursor();
	var token = cm.getTokenAt(cursor);
	if (token.className == "comment") {
		return null;
	}
	var text = cm.getLine(cursor.line).substring(0, cursor.ch);
	var hints = getQueryTextHints(cm, typed);
	if (hints == null) {
		return null;
	}
	var i = 0;
	while (true) {
		// the separators before a criterion: blanks, a position such as "1:", ties and parentheses
		var tie;
		var position;
		while (i < text.length) {
			if (/[\s<>()]/.test(text.charAt(i))) {
				i++;
			} else if ((position = /^\d+:/.exec(text.substring(i))) != null) {
				i += position[0].length;
			} else if ((tie = matchQueryTextHint(text, i, hints.tiesByLength)) != null) {
				i += tie.name.length;
			} else {
				break;
			}
		}
		var property = matchQueryTextHint(text, i, hints.propertiesByLength);
		var propertyEnd = property != null ? i + property.name.length : -1;
		if (property == null || propertyEnd == text.length) {
			// a property name, which can be the start of a longer one
			var query = text.substring(i);
			if (typed && query.length == 0) {
				return null;
			}
			var candidates = getQueryTextHintFields(hints.properties, "name");
			if (query.length == 0) {
				candidates = candidates.concat(getQueryTextHintFields(hints.ties, "name"));
			}
			return queryTextHintResult(cursor, i, query, candidates, typed);
		}
		var restrictionStart = skipQueryTextBlanks(text, propertyEnd);
		var restriction = matchQueryTextHint(text, restrictionStart, property.restrictionsByLength);
		var restrictionEnd = restriction != null ? restrictionStart + restriction.name.length : -1;
		if (restriction == null || restrictionEnd == text.length) {
			return queryTextHintResult(cursor, restrictionStart, text.substring(restrictionStart), getQueryTextHintFields(property.restrictions, "name"), typed);
		}
		if (restriction.unary || property.noValue) {
			return null;
		}
		var valueStart = skipQueryTextBlanks(text, restrictionEnd);
		var value = scanQueryTextValueEnd(text, valueStart, hints);
		if (value.tie == null) {
			var valueQuery = text.substring(valueStart);
			var values = property.values;
			if (values.length == 0 && property.lookup) {
				values = getQueryTextValueHints(cm, typed, property, valueQuery) || [];
			}
			return queryTextHintResult(cursor, valueStart, valueQuery, getQueryTextHintFields(values, "name"), typed);
		}
		i = value.end + value.tie.name.length;
	}
}

var QUERY_TEXT_JSON_KEYS = [ "position", "tie", "property", "restriction", "stringValue", "longValue", "floatValue", "booleanValue", "dateValue", "timeValue",
		"timestampValue" ];

// the property of the JSON object around the cursor, by its key or name
function getQueryTextJsonProperty(cm, cursor, hints) {
	var before = cm.getRange({
		line : 0,
		ch : 0
	}, cursor);
	var after = cm.getRange(cursor, {
		line : cm.lineCount(),
		ch : 0
	});
	var open = before.lastIndexOf("{");
	var close = after.indexOf("}");
	var object = (open >= 0 ? before.substring(open) : before) + (close >= 0 ? after.substring(0, close) : after);
	var match = /"property"\s*:\s*"([^"]*)"/.exec(object);
	if (match != null) {
		var name = match[1].toLowerCase();
		for (var i = 0; i < hints.properties.length; i++) {
			if ((hints.properties[i].key || "").toLowerCase() == name || (hints.properties[i].name || "").toLowerCase() == name) {
				return hints.properties[i];
			}
		}
	}
	return null;
}

// JSON criterions: the keys, and in the strings of tie, property, restriction and the values the keys of those
function queryTextJsonHint(cm, typed) {
	var cursor = cm.getCursor();
	var text = cm.getLine(cursor.line).substring(0, cursor.ch);
	var hints = getQueryTextHints(cm, typed);
	if (hints == null) {
		return null;
	}
	var field = /"(\w+)"\s*:\s*("?)([^"\\,]*)$/.exec(text);
	if (field != null) {
		var query = field[3];
		var start = cursor.ch - query.length;
		var property;
		if (field[1] == "tie" && field[2]) {
			return queryTextHintResult(cursor, start, query, getQueryTextHintFields(hints.ties, "key"), typed);
		} else if (field[1] == "property" && field[2]) {
			return queryTextHintResult(cursor, start, query, getQueryTextHintFields(hints.properties, "key"), typed);
		} else if (field[1] == "restriction" && field[2]) {
			property = getQueryTextJsonProperty(cm, cursor, hints);
			return property != null ? queryTextHintResult(cursor, start, query, getQueryTextHintFields(property.restrictions, "key"), typed) : null;
		} else if (field[1] == "value" || /Value$/.test(field[1])) {
			property = getQueryTextJsonProperty(cm, cursor, hints);
			if (property == null) {
				return null;
			}
			var values = property.values;
			// entity values are ids in JSON, which are not looked up by name
			if (values.length == 0 && property.lookup == "name" && field[2]) {
				values = getQueryTextValueHints(cm, typed, property, query) || [];
			}
			return queryTextHintResult(cursor, start, query, getQueryTextHintFields(values, "key"), typed);
		}
		return null;
	}
	var key = /(^|[{,])\s*"(\w*)$/.exec(text);
	if (key != null) {
		return queryTextHintResult(cursor, cursor.ch - key[2].length, key[2], QUERY_TEXT_JSON_KEYS, typed);
	}
	return null;
}

function queryTextHint(cm, typed) {
	if (cm.getOption("readOnly")) {
		return null;
	}
	// the JSON format has an object as mode
	return typeof cm.getOption("mode") === "object" ? queryTextJsonHint(cm, typed) : queryTextExpressionHint(cm, typed);
}

function typedQueryTextHint(cm) {
	return queryTextHint(cm, true);
}

// Ctrl-Space
function showQueryTextHint(cm) {
	showCodeMirrorHint(cm, function(editor) {
		return queryTextHint(editor, false);
	});
}

function handleQueryTextKeyEvent(cm, event) {
	if (event.type == "keypress" && !event.ctrlKey && !event.altKey && (event.charCode || event.keyCode) >= 32) {
		showTypedCodeMirrorHint(cm, typedQueryTextHint);
	}
	return false;
}

function refreshQueryTextEditor() {
	useQueryTextExpressionMode();
	bindQueryTextEditorResize();
	sizeQueryTextEditor();
	if (typeof queryTextEditor !== "undefined" && queryTextEditor && queryTextEditor.instance) {
		queryTextEditor.instance.refresh();
		window.setTimeout(function() {
			if (typeof queryTextEditor !== "undefined" && queryTextEditor && queryTextEditor.instance) {
				sizeQueryTextEditor();
				queryTextEditor.instance.refresh();
			}
			bindQueryTextActionButtons();
		}, 50);
	}
	bindQueryTextActionButtons();
}

var QUERY_TEXT_ERROR_LINE_CLASS = "ctsms-query-text-error-line";

// highlights the 1-based line where the query failed the syntax check, until the text is edited
function markQueryTextErrorLine(line) {
	if (typeof queryTextEditor === "undefined" || !queryTextEditor || !queryTextEditor.instance) {
		return;
	}
	var cm = queryTextEditor.instance;
	clearQueryTextErrorLine();
	var index = parseInt(line, 10) - 1;
	if (isNaN(index) || index < 0 || index >= cm.lineCount()) {
		return;
	}
	if (typeof cm.addLineClass === "function") {
		cm.ctsmsErrorLine = cm.addLineClass(index, "background", QUERY_TEXT_ERROR_LINE_CLASS);
	} else if (typeof cm.setLineClass === "function") {
		cm.ctsmsErrorLine = cm.setLineClass(index, null, QUERY_TEXT_ERROR_LINE_CLASS);
	}
	cm.setCursor({
		line : index,
		ch : 0
	});
}

function clearQueryTextErrorLine() {
	if (typeof queryTextEditor === "undefined" || !queryTextEditor || !queryTextEditor.instance) {
		return;
	}
	var cm = queryTextEditor.instance;
	var handle = cm.ctsmsErrorLine;
	if (handle == null) {
		return;
	}
	cm.ctsmsErrorLine = null;
	if (typeof cm.removeLineClass === "function") {
		cm.removeLineClass(handle, "background", QUERY_TEXT_ERROR_LINE_CLASS);
	} else if (typeof cm.setLineClass === "function") {
		cm.setLineClass(handle, null, null);
	}
}

function handleQueryTextApplied(xhr, status, args) {
	refreshQueryTextEditor();
	if (args && args.queryTextErrorLine) {
		markQueryTextErrorLine(args.queryTextErrorLine);
	}
}

function scrollQueryTextProgress() {
	var steps = document.getElementsByClassName("ctsms-query-text-progress-steps");
	for (var i = 0; i < steps.length; i++) {
		steps[i].scrollTop = steps[i].scrollHeight;
	}
}

var QUERY_TEXT_POLL_INTERVAL_MS = 2000;
var queryTextPollTimer = null;
var queryTextPollPending = false;

// the next poll is scheduled only after the previous one completed, so polls never pile up in the ajax queue
function scheduleQueryTextPoll() {
	if (typeof pollQueryText !== "function" || queryTextPollTimer != null || queryTextPollPending) {
		return;
	}
	queryTextPollTimer = setTimeout(function() {
		queryTextPollTimer = null;
		queryTextPollPending = true;
		pollQueryText();
	}, QUERY_TEXT_POLL_INTERVAL_MS);
}

function stopQueryTextPoll() {
	if (queryTextPollTimer != null) {
		clearTimeout(queryTextPollTimer);
		queryTextPollTimer = null;
	}
}

function handleQueryTextGenerateStarted(xhr, status, args) {
	if (args && args.queryTextRunning && typeof pollQueryText === "function") {
		// stays busy until the poll sees the run finish
		scheduleQueryTextPoll();
		// the step log may just have appeared, which leaves less room for the editor
		sizeQueryTextEditor();
		return;
	}
	setQueryTextBusy(false);
	refreshQueryTextEditor();
}

function handleQueryTextPollComplete(xhr, status, args) {
	queryTextPollPending = false;
	scrollQueryTextProgress();
	if (!(args && args.queryTextDone)) {
		scheduleQueryTextPoll();
		return;
	}
	stopQueryTextPoll();
	setQueryTextBusy(false);
	refreshQueryTextEditor();
	if (args.queryTextErrorLine) {
		markQueryTextErrorLine(args.queryTextErrorLine);
	}
}

function searchTabLogicalIndex(index) {
	if (typeof searchQueryTextTabVisible !== "undefined" && !searchQueryTextTabVisible && index >= 1) {
		return index + 1;
	}
	return index;
}

function searchTabVisibleIndex(index) {
	if (typeof searchQueryTextTabVisible !== "undefined" && !searchQueryTextTabVisible && index >= 1) {
		return index - 1;
	}
	return index;
}

// SelectOneMenu pins the width it measured. On a hidden query tab that width is ~0.
// Returns true when the menus are still hidden, so the caller can try once more after the tab is shown.
function restoreCriterionMenuWidths() {
	var menus = document.querySelectorAll(".ctsms-criterion-tie, .ctsms-criterion-property, .ctsms-criterion-restriction");
	var hidden = false;
	for (var i = 0; i < menus.length; i++) {
		if (!menus[i].offsetParent) {
			hidden = true;
			continue;
		}
		menus[i].style.width = "";
		var label = menus[i].querySelector(".ui-selectonemenu-label");
		if (label) {
			label.style.width = "";
		}
	}
	return hidden && menus.length > 0;
}

function handleSearchTabChange(index) {

	_deleteIntermediateSetsChartTooltip();
	switch (searchTabLogicalIndex(index)) {
	case 0:
		// onTabChange runs before the panel is shown, so drop the collapsed widths once it is visible
		window.setTimeout(function() {
			if (restoreCriterionMenuWidths()) {
				window.setTimeout(restoreCriterionMenuWidths, 50);
			}
		}, 0);

		switch (oldSearchTabIndex) {
		case 0:
			break;
		case 1:
			break;
		case 2:
			break;
		case 3:
			break;
		default:
			break;
		}

		break;

	case 1:
		loadQueryText();
		break;

	case 2:
		changeIntermediateSets();
		break;

	case 3:
		changeCriteriaJob();
		break;
		
	case 4:
		changeCriteriaJournalEntry();
		break;		

	default:
		break;
	}
	oldSearchTabIndex = index;
	return true;

}

var droppingCriterion = false;
function handleCriterionDropped(xhr, status, args) {

	droppingCriterion = false;

}

function criterionRowHandleDrop(event, ui) {

	if (!droppingCriterion) {
		droppingCriterion = true;
		var indexRe = /(\d+)$/;
		var sourceId = ui.draggable.attr("id");
		var targetId = this.jq.attr("id");

		var index_source = null;
		if (indexRe.test(sourceId)) {
			index_source = indexRe.exec(sourceId);
		}
		var index_target = null;
		if (indexRe.test(targetId)) {
			index_target = indexRe.exec(targetId);
		}
		if (index_source != null && index_target != null) {
			var params = {};
			params[SOURCE_INDEX] = index_source[1];
			params[TARGET_INDEX] = index_target[1];
			dropCriterionRow(prepareRemoteCommandParameters(params));
		}
	}
}

function handleCriteriaChanged(xhr, status, args) {

	if (_testFlag(args, AJAX_OPERATION_SUCCESS) && _testPropertyExists(args, AJAX_WINDOW_TITLE_BASE64) && _testPropertyExists(args, AJAX_ROOT_ENTITY_CREATED)) {
		if (!args[AJAX_ROOT_ENTITY_CREATED]) {
			searchTabView.select(0);
		}
		enableTabs(searchTabView, searchTabVisibleIndex(3), args[AJAX_ROOT_ENTITY_CREATED]);
		handleUpdateCriteriaTabTitles(xhr, status, args);
		if (!_testFlag(args, AJAX_PICKER)) {
			document.title = decodeBase64(args[AJAX_WINDOW_TITLE_BASE64]);
		}
	}

}

function handleUpdateCriteriaTabTitles(xhr, status, args) {

	if (_testPropertyExists(args, AJAX_OPERATION_SUCCESS) && _testPropertyExists(args, AJAX_ROOT_ENTITY_CREATED)) {
		searchTabView.emphasizeTab(0, _testFlag(args, AJAX_OPERATION_SUCCESS) && !args[AJAX_ROOT_ENTITY_CREATED]);
	}

	if (_testPropertyExists(args, AJAX_CRITERIA_JOB_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_CRITERIA_JOB_COUNT)) {
		searchTabView.setTabTitle(searchTabVisibleIndex(3), decodeBase64(args[AJAX_CRITERIA_JOB_TAB_TITLE_BASE64]));
		searchTabView.emphasizeTab(searchTabVisibleIndex(3), args[AJAX_CRITERIA_JOB_COUNT] == 0);
	}
	if (_testPropertyExists(args, AJAX_CRITERIA_JOURNAL_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_CRITERIA_JOURNAL_ENTRY_COUNT)) {
		searchTabView.setTabTitle(searchTabVisibleIndex(4), decodeBase64(args[AJAX_CRITERIA_JOURNAL_TAB_TITLE_BASE64]));
		searchTabView.emphasizeTab(searchTabVisibleIndex(4), args[AJAX_CRITERIA_JOURNAL_ENTRY_COUNT] == 0);
	}

}

var intermediateSetsChart = null;
var intermediateSetsChartTooltip = null;
var cssColorMap = null;
var canvasD3;
var tooltipD3;
var canvas;
var tooltip;

function handleUpdateIntermediateSets(xhr, status, args) {

	if (_testFlag(args, AJAX_OPERATION_SUCCESS)) {
		var intermediateSets = null;
		if (_testPropertyExists(args, AJAX_INTERMEDIATE_SETS_BASE64)) {
			intermediateSets = JSON.parse(decodeBase64(args[AJAX_INTERMEDIATE_SETS_BASE64]));
		}

		var criterionRowColors = null;
		if (_testPropertyExists(args, AJAX_CRITERION_ROW_COLORS_BASE64)) {
			criterionRowColors = JSON.parse(decodeBase64(args[AJAX_CRITERION_ROW_COLORS_BASE64]));
		}

		var criterionTieMap = null;
		if (_testPropertyExists(args, AJAX_CRITERION_TIE_MAP_BASE64)) {
			criterionTieMap = JSON.parse(decodeBase64(args[AJAX_CRITERION_TIE_MAP_BASE64]));
		}

		var resultItemLabel;
		if (_testPropertyExists(args, AJAX_CRITERIA_RESULT_ITEM_LABEL_BASE64)) {
			resultItemLabel = decodeBase64(args[AJAX_CRITERIA_RESULT_ITEM_LABEL_BASE64]);
		} else {
			resultItemLabel = "item(s)";
		}

		_createIntermediateSetChart();
		_updateIntermediateSetChartCanvasSize(intermediateSets);

		var maps = _updateIntermediateSetChart(intermediateSets, criterionRowColors, criterionTieMap, resultItemLabel);
		_styleIntermediateSetChart(maps);
		_registerIntermediateSetChartTooltip(maps);
	}

}

function _formatCountCircle(count) {
	return numeral(count).format('0[.]0a');
}
function _formatCountTooltipText(count) {
	return numeral(count).format('0,0');
}

function _updateIntermediateSetChart(intermediateSets, criterionColorClasses, criterionTieMap, resultItemLabel) {
	var colorMap = {};
	var tooltipMap = {};
	var isIntermediateMap = {};
	var intermediateSetMap = {};
	var maps = {
		"criterionTieMap" : criterionTieMap
	};
	if (intermediateSetsChart != null) {
	if (intermediateSets != null && intermediateSets.length > 0) {
		var sets = [];
		for ( var i = 0; i < intermediateSets.length; i++) {
			var intermediateSet = intermediateSets[i];
			if (intermediateSet.operator != null) {
				var a = intermediateSet.a;
				var b = intermediateSet.b;
				var aSet = [ 'a' + i ];
				var bSet = [ 'b' + i ];
				var abSet = aSet.concat(bSet);
				colorMap[aSet.toString()] = _getSelectColor(a, criterionColorClasses);
				colorMap[bSet.toString()] = _getSelectColor(b, criterionColorClasses);
				colorMap[abSet.toString()] = (a.selectStatementCount == 1 ? colorMap[aSet.toString()]
				        : (b.selectStatementCount == 1 ? colorMap[bSet.toString()] : _getCssColor('ctsms-search-intermediateset-intermediate-color')));

				isIntermediateMap[aSet.toString()] = (a.selectStatementCount > 1);
				isIntermediateMap[bSet.toString()] = (b.selectStatementCount > 1);
				isIntermediateMap[abSet.toString()] = (a.selectStatementCount > 1) || (b.selectStatementCount > 1);

				sets.push({
				    sets : aSet,
				    size : intermediateSet.aCount,
				    label : a.setOperationExpression + ": " + _formatCountCircle(intermediateSet.aCount)
				});
				sets.push({
				    sets : bSet,
				    size : intermediateSet.bCount,
				    label : b.setOperationExpression + ": " + _formatCountCircle(intermediateSet.bCount)
				});
				sets.push({
				    sets : abSet,
				    size : intermediateSet.intersectionCount,
				    label : ((intermediateSet.intersectionCount != intermediateSet.aCount && intermediateSet.intersectionCount != intermediateSet.bCount) ?
				            _formatCountCircle(intermediateSet.intersectionCount) : "")
				});
				intermediateSetMap[intermediateSet.criteria.setOperationExpression] = intermediateSet;
			} else {
				var criteria = intermediateSet.criteria;
				var criteriaSet = [ 'criteria' + i ];
				colorMap[criteriaSet.toString()] = _getSelectColor(criteria, criterionColorClasses);
				isIntermediateMap[criteriaSet.toString()] = (criteria.selectStatementCount > 1);
				sets.push({
				    sets : criteriaSet,
				    size : intermediateSet.criteriaCount,
				    label : criteria.setOperationExpression + ": " + _formatCountCircle(intermediateSet.criteriaCount)
				});
			}
		}
		maps["intermediateSetMap"] = intermediateSetMap;

		for ( var i = 0; i < intermediateSets.length; i++) {
			var intermediateSet = intermediateSets[i];
			if (intermediateSet.operator != null) {
				var a = intermediateSet.a;
				var b = intermediateSet.b;
				var aSet = [ 'a' + i ];
				var bSet = [ 'b' + i ];
				var abSet = aSet.concat(bSet);
				tooltipMap[aSet.toString()] = _getIntermediateSetTooltipText(a, intermediateSet.aCount, resultItemLabel, maps, true);
				tooltipMap[bSet.toString()] = _getIntermediateSetTooltipText(b, intermediateSet.bCount, resultItemLabel, maps, true);
				tooltipMap[abSet.toString()] = _getIntermediateSetTooltipText(intermediateSet.intersection, intermediateSet.intersectionCount, resultItemLabel,
				        maps, false);
			} else {
				var criteria = intermediateSet.criteria;
				var criteriaSet = [ 'criteria' + i ];
				tooltipMap[criteriaSet.toString()] = _getIntermediateSetTooltipText(criteria, intermediateSet.criteriaCount, resultItemLabel, maps,
				        criteria.criterions.length > 0);
			}
		}

		canvasD3.datum(sets).call(intermediateSetsChart);
	} else {
		canvasD3.text('');
	}
	}
	maps["colorMap"] = colorMap;
	maps["tooltipMap"] = tooltipMap;
	maps["isIntermediateMap"] = isIntermediateMap;
	return maps;

}

function _getIntermediateSetTooltipText(criteria, count, resultItemLabel, maps, showExpression) {

	var result = criteria.setOperationExpression + "\n";
	if (maps.criterionTieMap != null) {
		var intermediateSet = maps.intermediateSetMap[criteria.setOperationExpression];
		if (intermediateSet != null && intermediateSet.operator != null) {
			var operator = maps.criterionTieMap[intermediateSet.operator.tieId];
			if (operator != null) {
				switch (operator.tie) {
				case 'UNION':
					result += _formatCountTooltipText(intermediateSet.aCount - intermediateSet.intersectionCount) + " + " + _formatCountTooltipText(intermediateSet.intersectionCount) + " + "
					        + _formatCountTooltipText(intermediateSet.bCount - intermediateSet.intersectionCount) + " = ";
					break;
				case 'EXCEPT':
					result += _formatCountTooltipText(intermediateSet.aCount) + " - " + _formatCountTooltipText(intermediateSet.intersectionCount) + " = ";
					break;
				default:
					break;
				}
			}
		}
	}
	result += _formatCountTooltipText(count) + " " + resultItemLabel;
	if (showExpression && criteria.criterionExpression.length > 0) {
		result += "\n\n" + criteria.criterionExpression;
	}
	return result;

}

function _deleteIntermediateSetsChartTooltip() {
	var intermediateSetsChartTooltip = document.getElementById('intermediateSetsChartTooltip');
	if (intermediateSetsChartTooltip != null) {
		intermediateSetsChartTooltip.parentNode.removeChild(intermediateSetsChartTooltip);
	}
}

function _createIntermediateSetChart() {
	canvas = document.getElementById('intermediateSetsChartCanvas');
	_deleteIntermediateSetsChartTooltip();
	tooltip = null;
	if (canvas != null) {
		canvasD3 = d3.select("#intermediateSetsChartCanvas");
		tooltipD3 = d3.select("body").append("div")
		.attr("id", "intermediateSetsChartTooltip")
		.attr("class", "ui-widget ctsms-intermediatesets-tooltip");
		tooltip = document.getElementById('intermediateSetsChartTooltip');
		if (intermediateSetsChart == null) {
			intermediateSetsChart = venn.VennDiagram();
			intermediateSetsChart.padding(30); // border only
			intermediateSetsChart.duration(1000); // transition fx
			intermediateSetsChart.normalize(true); 
			intermediateSetsChart.wrap(true); // labels
			intermediateSetsChart.styled(false);
			intermediateSetsChart.orientation(2 * Math.PI / 6);
			// consistent operand order:
			intermediateSetsChart.orientationOrder(function(a, b) {
				return 0.0;
			}); 
			intermediateSetsChart.layoutFunction(function(d) {
				return venn.venn(d, {
					initialLayout : venn.classicMDSLayout
				});
			});
			return 1;
		}
	}
	return 0;
}

function _updateIntermediateSetChartCanvasSize(intermediateSets) {
	if (intermediateSetsChart != null) {
		var rows = (intermediateSets != null && intermediateSets.length > 0 ? intermediateSets.length : 0);
		var bb = canvas.getBoundingClientRect();
		var height = Math.min(bb.width, Math.ceil(380.0 * Math.max(1.0, rows / 2.5)));
		intermediateSetsChart.width(bb.width);
		intermediateSetsChart.height(height);
	}
}

function _styleIntermediateSetChart(maps) {
	if (intermediateSetsChart != null) {
		d3.selectAll("#intermediateSetsChartCanvas .venn-area path").style("fill", function(d, i) {
			return maps.colorMap[d.sets.toString()];
		}).style("fill-opacity", function(d, i) {
			return d.sets.length == 1 ? 0.75 : 0.1;
		}).style("stroke-width", 2).style("stroke", function(d, i) {
			return maps.colorMap[d.sets.toString()];
		}).style("stroke-opacity", function(d, i) {
			return d.sets.length == 1 ? 1 : 0.4;
		}).style("font-size", "12px").style("font-weight", "100");
	}
}

function _registerIntermediateSetChartTooltip(maps) {
	if (intermediateSetsChart != null) {
		var svgBb = null; 
		var tooltipBb = null; 
		var tooltipBbMap = {};
		var highlightCircleStrokeColor = _getCssColor('ctsms-search-intermediateset-highlight-circle-stroke-color');
		var highlightCircleIntermediateStrokeColor = _getCssColor('ctsms-search-intermediateset-highlight-circle-intermediate-stroke-color');
		var highlightIntersectionStrokeColor = _getCssColor('ctsms-search-intermediateset-highlight-intersection-stroke-color');
		canvasD3.selectAll("g").on( 
		        "mouseover",
		        function(d, i) {
		        	var setId = d.sets.toString();
			        // sort all the areas relative to the current item
			        venn.sortAreas(canvasD3, d);

			        // Display a tooltip with the current size
			        tooltipD3.transition().duration(400).style("opacity", .8);
			        tooltipD3.text(maps.tooltipMap[setId]);
			        if (svgBb == null) {
			        	svgBb = canvasD3.select("svg").node().getBoundingClientRect();
			        }
			        if (setId in tooltipBbMap) {
			        	tooltipBb = tooltipBbMap[setId];
			        } else {
			        	tooltipBb = tooltip.getBoundingClientRect();
			        	tooltipBbMap[setId] = tooltipBb;
			        }

			        // highlight the current path
			        var selection = d3.select(this).transition("tooltip_transition").duration(400);
			        selection.select("path").style(
			                "stroke",
			                function(d, i) {
				                return d.sets.length == 1 ? (maps.isIntermediateMap[d.sets.toString()] ? highlightCircleIntermediateStrokeColor
				                        : highlightCircleStrokeColor) : highlightIntersectionStrokeColor;
			                });
		        })

		.on("mousemove", function() {

			var left;
			var top;
			if (d3.event.pageX > svgBb.left + (svgBb.width / 2.0)) { //right
				left = d3.event.pageX - tooltipBb.width;
				if (d3.event.pageY > svgBb.top + (svgBb.height / 2.0)) { //bottom
					top = d3.event.pageY - tooltipBb.height;
				} else { //top
					top = d3.event.pageY;
				}
			} else { //left
				left = d3.event.pageX;
				if (d3.event.pageY > svgBb.top + (svgBb.height / 2.0)) { //bottom
					top = d3.event.pageY - tooltipBb.height;
				} else { //top
					top = d3.event.pageY;
				}
			}

			tooltipD3.style("left", left + "px")
			 .style("top", top + "px");
		})

		.on("mouseout", function(d, i) {
			tooltipD3.transition().duration(400).style("opacity", 0);
			var selection = d3.select(this).transition("tooltip_transition").duration(400);
			selection.select("path").style("stroke", function(d, i) {
				return maps.colorMap[d.sets.toString()];
			});
		});
	}
}

function _getSelectColor(criteria, criterionColorClasses) {
	if (criteria != null) {
		if (criterionColorClasses != null && criterionColorClasses.length > 0) {
			if (criteria.selectStatementCount > 1) {
				return _getCssColor('ctsms-search-intermediateset-intermediate-color');
			} else {
				var position = null;
				if (criteria.criterions != null && criteria.criterions.length > 0) {
					for ( var i = 0; i < criteria.criterions.length; i++) {
						position = criteria.criterions[i].position;
						if (position != null) {
							break;
						}
					}
				}
				if (position != null && criterionColorClasses != null && criterionColorClasses.length > 0) {
					return _getCssColor(criterionColorClasses[position - LIST_INITIAL_POSITION]);
				}
			}
		} else {
			return _getCssColor('ctsms-search-intermediateset-blank-color');
		}
	}
	return null;
}

function _getCssColor(colorClassName) {
	if (cssColorMap == null) {
		cssColorMap = getCssRuleMap(/(\.ctsms-search-criterion-row|\.ctsms-color-|\.ctsms-search-intermediateset-)/);
	}
	if (colorClassName != null && colorClassName.length > 0) {
		var colorClass = cssColorMap["." + colorClassName];
		if (colorClass != null) {
			var colorValue = colorClass['background-color'];
			if (colorValue == null) {
				colorValue = colorClass['background'];
			}
			if (colorValue != null) {
				return rgb2hex(colorValue);
			}

		}
	}
	return null;
}
