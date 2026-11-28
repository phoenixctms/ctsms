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

// a generated query waits in the editor: Apply in query builder shows the active color of the theme until it is clicked or a new run starts.
// PrimeFaces takes ui-state-active off a button on mouseout and mouseup, so the marker class puts it back.
var QUERY_TEXT_APPLY_READY_CLASS = "ctsms-query-text-apply-ready";

function highlightQueryTextApply(ready) {
	jQuery(".ctsms-query-text-apply").toggleClass(QUERY_TEXT_APPLY_READY_CLASS + " ui-state-active", ready);
}

jQuery(document).on("mouseout mouseup", "." + QUERY_TEXT_APPLY_READY_CLASS, function() {
	jQuery(this).addClass("ui-state-active");
});

function setQueryTextBusy(busy) {
	if (busy) {
		highlightQueryTextApply(false);
	}
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

// opening the tab loads the builder's query into the editor, which re-renders the editor panel: the editor is read-only until then,
// so nothing typed in the meantime gets replaced. The re-rendered editor is editable again
var queryTextLoading = false;

function startLoadQueryText() {
	queryTextLoading = true;
	if (!_lockQueryTextEditor() && typeof searchTabView !== "undefined" && searchTabView && typeof searchTabView.addOnshowHandler === "function") {
		// an editor in a hidden tab creates its CodeMirror only when the tab shows, in its on-show handler: the tab change that
		// starts this load comes before that. A handler added now runs after the editor's, in the same tab show, and only once.
		searchTabView.addOnshowHandler(function() {
			_lockQueryTextEditor();
			return true;
		});
	}
	loadQueryText();
}

// makes the editor read-only while the load is going on; false if the editor has no CodeMirror yet
function _lockQueryTextEditor() {
	if (typeof queryTextEditor === "undefined" || !queryTextEditor || !queryTextEditor.instance) {
		return false;
	}
	if (queryTextLoading) {
		queryTextEditor.instance.setOption("readOnly", "nocursor");
		jQuery(queryTextEditor.instance.getWrapperElement()).addClass("ctsms-query-text-loading");
	}
	return true;
}

// also after a failed request, which leaves the old editor in place
function endLoadQueryText() {
	queryTextLoading = false;
	if (typeof queryTextEditor !== "undefined" && queryTextEditor && queryTextEditor.instance) {
		queryTextEditor.instance.setOption("readOnly", false);
		jQuery(queryTextEditor.instance.getWrapperElement()).removeClass("ctsms-query-text-loading");
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
		var valueStart = skipQueryTextBlanks(text, restrictionEnd);
		var value = scanQueryTextValueEnd(text, valueStart, hints);
		if (value.tie == null) {
			if (restriction.unary || property.noValue) {
				return null;
			}
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

// the innermost JSON object around offset, skipping the braces in strings
function getQueryTextJsonObjectBounds(text, offset) {
	var opens = [];
	var inString = false;
	var start = 0;
	for (var i = 0; i < text.length; i++) {
		var c = text.charAt(i);
		if (i == offset) {
			start = opens.length > 0 ? opens[opens.length - 1] : 0;
		}
		if (inString) {
			if (c == "\\") {
				i++;
			} else if (c == "\"") {
				inString = false;
			}
		} else if (c == "\"") {
			inString = true;
		} else if (c == "{") {
			opens.push(i);
		} else if (c == "}") {
			if (i >= offset && opens.length > 0 && opens[opens.length - 1] == start) {
				return {
					start : start,
					end : i
				};
			}
			opens.pop();
		}
	}
	if (offset >= text.length) {
		start = opens.length > 0 ? opens[opens.length - 1] : 0;
	}
	return {
		start : start,
		end : text.length
	};
}

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
	var bounds = getQueryTextJsonObjectBounds(before + after, before.length);
	var object = (before + after).substring(bounds.start, bounds.end);
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
		// the tab was left while the request to start the run was on its way: stop the run, as leaving the tab does
		if (searchTabLogicalIndex(oldSearchTabIndex) != 1 && typeof cancelQueryTextRun === "function") {
			cancelQueryTextRun();
		}
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
	highlightQueryTextApply(args.queryTextGenerated == true);
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

// a run is going on while its poll is scheduled or waiting for its answer
function isQueryTextRunPolled() {
	return queryTextPollTimer != null || queryTextPollPending;
}

function handleSearchTabChange(index) {

	_deleteIntermediateSetsChartTooltip();
	// leaving the Query Agent tab stops the agent; the poll goes on until it sees the run end
	if (searchTabLogicalIndex(oldSearchTabIndex) == 1 && searchTabLogicalIndex(index) != 1 && isQueryTextRunPolled()
	        && typeof cancelQueryTextRun === "function") {
		cancelQueryTextRun();
	}
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
		startLoadQueryText();
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

var intermediateSetsChartTooltip = null;
var cssColorMap = null;
var canvasD3;
var tooltipD3;
var canvas;
var tooltip;
// the tints of combined sets, generated by CriterionRowColorList.getStyles()
var INTERMEDIATE_SET_TINTS = 8;
var INTERMEDIATE_SET_STEP_HEIGHT = 220;
var INTERMEDIATE_SET_SMALL_STEP_HEIGHT = 150;
var INTERMEDIATE_SET_MANY_STEPS = 6;
// a circle of an empty set is drawn this fraction of the other circle, dashed
var INTERMEDIATE_SET_EMPTY_SIZE = 0.04;

function handleUpdateIntermediateSets(xhr, status, args) {

	if (_testFlag(args, AJAX_OPERATION_SUCCESS)) {
		var intermediateSets = null;
		if (_testPropertyExists(args, AJAX_INTERMEDIATE_SETS_BASE64)) {
			intermediateSets = JSON.parse(decodeBase64(args[AJAX_INTERMEDIATE_SETS_BASE64]));
		}

		var intermediateSetSteps = null;
		if (_testPropertyExists(args, AJAX_INTERMEDIATE_SET_STEPS_BASE64)) {
			intermediateSetSteps = JSON.parse(decodeBase64(args[AJAX_INTERMEDIATE_SET_STEPS_BASE64]));
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

		if (_createIntermediateSetChart()) {
			var maps = _updateIntermediateSetChart(intermediateSets, intermediateSetSteps, criterionRowColors, criterionTieMap, resultItemLabel);
			_styleIntermediateSetChart(maps);
			_registerIntermediateSetChartTooltip(maps);
		}
	}

}

function _formatCountCircle(count) {
	return numeral(count).format('0[.]0a');
}
function _formatCountTooltipText(count) {
	return numeral(count).format('0,0');
}

// the drawn size: an empty set keeps a small circle, so it does not vanish
function _getIntermediateSetDisplaySize(count, otherCount) {
	if (count > 0) {
		return count;
	}
	return Math.max(1, otherCount * INTERMEDIATE_SET_EMPTY_SIZE);
}

/**
 * One small diagram per set step, numbered like the step table below. A query without set operator gets one diagram of its single set.
 */
function _updateIntermediateSetChart(intermediateSets, intermediateSetSteps, criterionColorClasses, criterionTieMap, resultItemLabel) {
	var colorMap = {};
	var tooltipMap = {};
	var isIntermediateMap = {};
	var emptyMap = {};
	var intermediateSetMap = {};
	var stepNumberMap = {};
	var stepRowMap = {};
	var maps = {
		"criterionTieMap" : criterionTieMap
	};
	if (intermediateSetSteps != null) {
		for ( var s = 0; s < intermediateSetSteps.length; s++) {
			stepRowMap[intermediateSetSteps[s].index] = intermediateSetSteps[s];
		}
	}
	canvasD3.text('');
	canvasD3.classed('ctsms-intermediatesets-chart-grid', true);
	var diagrams = [];
	if (intermediateSets != null && intermediateSets.length > 0) {
		var stepCount = 0;
		for ( var i = 0; i < intermediateSets.length; i++) {
			if (intermediateSets[i].operator != null) {
				stepCount++;
				stepNumberMap[intermediateSets[i].criteria.setOperationExpression] = stepCount;
				intermediateSetMap[intermediateSets[i].criteria.setOperationExpression] = intermediateSets[i];
			}
		}
		maps["intermediateSetMap"] = intermediateSetMap;
		for ( var i = 0; i < intermediateSets.length; i++) {
			var intermediateSet = intermediateSets[i];
			var sets = [];
			if (intermediateSet.operator != null) {
				var stepRow = stepRowMap[i];
				var a = intermediateSet.a;
				var b = intermediateSet.b;
				var aCount = intermediateSet.aCount != null ? intermediateSet.aCount : 0;
				var bCount = intermediateSet.bCount != null ? intermediateSet.bCount : 0;
				var intersectionCount = intermediateSet.intersectionCount != null ? intermediateSet.intersectionCount : 0;
				var aSet = [ 'a' + i ];
				var bSet = [ 'b' + i ];
				var abSet = aSet.concat(bSet);
				colorMap[aSet.toString()] = _getOperandColor(a, criterionColorClasses, stepNumberMap);
				colorMap[bSet.toString()] = _getOperandColor(b, criterionColorClasses, stepNumberMap);
				colorMap[abSet.toString()] = (a.selectStatementCount == 1 ? colorMap[aSet.toString()] : (b.selectStatementCount == 1 ? colorMap[bSet.toString()]
				        : colorMap[aSet.toString()]));

				isIntermediateMap[aSet.toString()] = (a.selectStatementCount > 1);
				isIntermediateMap[bSet.toString()] = (b.selectStatementCount > 1);
				isIntermediateMap[abSet.toString()] = (a.selectStatementCount > 1) || (b.selectStatementCount > 1);
				emptyMap[aSet.toString()] = (aCount == 0);
				emptyMap[bSet.toString()] = (bCount == 0);

				var aName = (stepRow != null ? stepRow.a : a.setOperationExpression);
				var bName = (stepRow != null ? stepRow.b : b.setOperationExpression);
				sets.push({
				    sets : aSet,
				    size : _getIntermediateSetDisplaySize(aCount, bCount),
				    label : aName + ": " + _formatCountCircle(aCount)
				});
				sets.push({
				    sets : bSet,
				    size : _getIntermediateSetDisplaySize(bCount, aCount),
				    label : bName + ": " + _formatCountCircle(bCount)
				});
				sets.push({
				    sets : abSet,
				    size : (aCount == 0 || bCount == 0 ? 0 : intersectionCount),
				    label : ((intersectionCount != aCount && intersectionCount != bCount) ? _formatCountCircle(intersectionCount) : "")
				});
				tooltipMap[aSet.toString()] = _getIntermediateSetTooltipText(a, aCount, resultItemLabel, maps, true);
				tooltipMap[bSet.toString()] = _getIntermediateSetTooltipText(b, bCount, resultItemLabel, maps, true);
				tooltipMap[abSet.toString()] = _getIntermediateSetTooltipText(intermediateSet.intersection, intersectionCount, resultItemLabel, maps, false);
				diagrams.push({
				    sets : sets,
				    title : (stepRow != null ? stepRow.number + ". " + stepRow.title : intermediateSet.criteria.setOperationExpression),
				    expression : intermediateSet.criteria.setOperationExpression,
				    warning : (stepRow != null ? stepRow.warning : null)
				});
			} else if (stepCount == 0) {
				// the total of a query with set operators is the result of its last step, which the table gives
				var criteria = intermediateSet.criteria;
				var criteriaSet = [ 'criteria' + i ];
				var count = intermediateSet.criteriaCount != null ? intermediateSet.criteriaCount : 0;
				colorMap[criteriaSet.toString()] = _getSelectColor(criteria, criterionColorClasses);
				isIntermediateMap[criteriaSet.toString()] = (criteria.selectStatementCount > 1);
				emptyMap[criteriaSet.toString()] = (count == 0);
				var expression = (criteria.setOperationExpression != null && criteria.setOperationExpression.length > 0 ? criteria.setOperationExpression : resultItemLabel);
				sets.push({
				    sets : criteriaSet,
				    size : _getIntermediateSetDisplaySize(count, 1),
				    label : expression + ": " + _formatCountCircle(count)
				});
				tooltipMap[criteriaSet.toString()] = _getIntermediateSetTooltipText(criteria, count, resultItemLabel, maps,
				        criteria.criterions != null && criteria.criterions.length > 0);
				diagrams.push({
				    sets : sets,
				    title : expression,
				    expression : expression,
				    warning : null
				});
			}
		}
	}
	// lay out all cells first, so each diagram gets the width of its cell. Long chains of steps get smaller diagrams.
	var height = (diagrams.length > INTERMEDIATE_SET_MANY_STEPS ? INTERMEDIATE_SET_SMALL_STEP_HEIGHT : INTERMEDIATE_SET_STEP_HEIGHT);
	var stepCanvases = [];
	for ( var d = 0; d < diagrams.length; d++) {
		var cell = canvasD3.append('div').attr('class', 'ctsms-intermediatesets-step');
		cell.append('div').attr('class', 'ctsms-intermediatesets-step-title').attr('title', diagrams[d].expression).text(diagrams[d].title);
		stepCanvases.push(cell.append('div').attr('class', 'ctsms-intermediatesets-step-canvas').style('height', height + 'px'));
		if (diagrams[d].warning != null && diagrams[d].warning.length > 0) {
			cell.append('div').attr('class', 'ctsms-intermediatesets-step-warning').text(diagrams[d].warning);
		}
	}
	for ( var d = 0; d < diagrams.length; d++) {
		var width = stepCanvases[d].node().getBoundingClientRect().width;
		stepCanvases[d].datum(diagrams[d].sets).call(_createIntermediateSetVennDiagram(width > 0 ? width : 300, height));
	}
	maps["colorMap"] = colorMap;
	maps["tooltipMap"] = tooltipMap;
	maps["isIntermediateMap"] = isIntermediateMap;
	maps["emptyMap"] = emptyMap;
	return maps;

}

// a combined set has the tint of the step it results from, so it looks the same in each diagram
function _getOperandColor(criteria, criterionColorClasses, stepNumberMap) {
	if (criteria != null && criteria.selectStatementCount > 1) {
		var number = stepNumberMap[criteria.setOperationExpression];
		if (number != null) {
			var tint = _getCssColor('ctsms-search-intermediateset-intermediate-color-' + ((number - 1) % INTERMEDIATE_SET_TINTS));
			if (tint != null) {
				return tint;
			}
		}
		return _getCssColor('ctsms-search-intermediateset-intermediate-color');
	}
	return _getSelectColor(criteria, criterionColorClasses);
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
				case 'INTERSECT':
					result += _formatCountTooltipText(intermediateSet.aCount) + " " + operator.name + " " + _formatCountTooltipText(intermediateSet.bCount) + " = ";
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
	if (showExpression && criteria.criterionExpression != null && criteria.criterionExpression.length > 0) {
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
		return true;
	}
	return false;
}

function _createIntermediateSetVennDiagram(width, height) {
	var chart = venn.VennDiagram();
	chart.padding(20); // border only
	chart.duration(0); // each update draws new diagrams
	chart.normalize(true);
	chart.wrap(true); // labels
	chart.styled(false);
	chart.orientation(2 * Math.PI / 6);
	// consistent operand order:
	chart.orientationOrder(function(a, b) {
		return 0.0;
	});
	chart.layoutFunction(function(d) {
		return venn.venn(d, {
			initialLayout : venn.classicMDSLayout
		});
	});
	chart.width(width);
	chart.height(height);
	return chart;
}

function _styleIntermediateSetChart(maps) {
	d3.selectAll("#intermediateSetsChartCanvas .venn-area path").style("fill", function(d, i) {
		return maps.colorMap[d.sets.toString()];
	}).style("fill-opacity", function(d, i) {
		if (d.sets.length == 1) {
			return maps.emptyMap[d.sets.toString()] ? 0 : 0.75;
		}
		return 0.1;
	}).style("stroke-width", 2).style("stroke", function(d, i) {
		return maps.colorMap[d.sets.toString()];
	}).style("stroke-dasharray", function(d, i) {
		return (d.sets.length == 1 && maps.emptyMap[d.sets.toString()]) ? "4,3" : null;
	}).style("stroke-opacity", function(d, i) {
		return d.sets.length == 1 ? 1 : 0.4;
	}).style("font-size", "12px").style("font-weight", "100");
}

function _getColorLuminance(color) {
	if (color == null || !/^#[0-9A-F]{6}$/i.test(color)) {
		return null;
	}
	return 0.2126 * parseInt(color.substr(1, 2), 16) + 0.7152 * parseInt(color.substr(3, 2), 16) + 0.0722 * parseInt(color.substr(5, 2), 16);
}

function _registerIntermediateSetChartTooltip(maps) {
	var svgBb = null;
	var tooltipBb = null;
	var tooltipBbMap = {};
	var highlightCircleStrokeColor = _getCssColor('ctsms-search-intermediateset-highlight-circle-stroke-color');
	var highlightCircleDarkStrokeColor = _getCssColor('ctsms-search-intermediateset-highlight-circle-stroke-dark-color');
	var highlightCircleIntermediateStrokeColor = _getCssColor('ctsms-search-intermediateset-highlight-circle-intermediate-stroke-color');
	var highlightIntersectionStrokeColor = _getCssColor('ctsms-search-intermediateset-highlight-intersection-stroke-color');
	var highlightLuminance = _getColorLuminance(highlightCircleStrokeColor);
	// a dark circle gets the light stroke, so the highlight stays visible
	var getCircleStrokeColor = function(setId) {
		if (maps.isIntermediateMap[setId]) {
			return highlightCircleIntermediateStrokeColor;
		}
		var luminance = _getColorLuminance(maps.colorMap[setId]);
		if (highlightCircleDarkStrokeColor != null && luminance != null && highlightLuminance != null && luminance <= highlightLuminance) {
			return highlightCircleDarkStrokeColor;
		}
		return highlightCircleStrokeColor;
	};
	canvasD3.selectAll("g").on(
	        "mouseover",
	        function(d, i) {
	        	var setId = d.sets.toString();
		        // sort the areas of this diagram relative to the current item
	        	if (this.ownerSVGElement != null) {
	        		venn.sortAreas(d3.select(this.ownerSVGElement.parentNode), d);
	        		svgBb = this.ownerSVGElement.getBoundingClientRect();
	        	}

		        // Display a tooltip with the current size
		        tooltipD3.transition().duration(400).style("opacity", .8);
		        tooltipD3.text(maps.tooltipMap[setId]);
		        if (setId in tooltipBbMap) {
		        	tooltipBb = tooltipBbMap[setId];
		        } else {
		        	tooltipBb = tooltip.getBoundingClientRect();
		        	tooltipBbMap[setId] = tooltipBb;
		        }

		        // highlight the current path
		        var selection = d3.select(this).transition("tooltip_transition").duration(400);
		        selection.select("path").style("stroke", function(d, i) {
			        return d.sets.length == 1 ? getCircleStrokeColor(d.sets.toString()) : highlightIntersectionStrokeColor;
		        });
	        })

	.on("mousemove", function() {
		if (svgBb == null || tooltipBb == null) {
			return;
		}
		// the side away from the middle of the diagram, unless the tooltip does not fit into the window there
		var viewportWidth = document.documentElement.clientWidth;
		var viewportHeight = document.documentElement.clientHeight;
		var toLeft = d3.event.clientX > svgBb.left + (svgBb.width / 2.0);
		if (toLeft && d3.event.clientX - tooltipBb.width < 0) {
			toLeft = false;
		} else if (!toLeft && d3.event.clientX + tooltipBb.width > viewportWidth && d3.event.clientX - tooltipBb.width >= 0) {
			toLeft = true;
		}
		var above = d3.event.clientY > svgBb.top + (svgBb.height / 2.0);
		if (above && d3.event.clientY - tooltipBb.height < 0) {
			above = false;
		} else if (!above && d3.event.clientY + tooltipBb.height > viewportHeight && d3.event.clientY - tooltipBb.height >= 0) {
			above = true;
		}
		var left = toLeft ? d3.event.pageX - tooltipBb.width : d3.event.pageX;
		var top = above ? d3.event.pageY - tooltipBb.height : d3.event.pageY;
		// when it fits on neither side, keep it inside the window anyway
		var scrollX = d3.event.pageX - d3.event.clientX;
		var scrollY = d3.event.pageY - d3.event.clientY;
		left = Math.max(scrollX, Math.min(left, scrollX + viewportWidth - tooltipBb.width));
		top = Math.max(scrollY, Math.min(top, scrollY + viewportHeight - tooltipBb.height));

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
