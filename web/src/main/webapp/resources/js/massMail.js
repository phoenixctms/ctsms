var oldMassMailTabIndex = 0;

function handleMassMailTabChange(index) {

	switch (index) {
	case 0:

		switch (oldMassMailTabIndex) {
		case 0:
			break;
		case 1:
			changeMassMailByMassMailRecipient();
			break;
		case 2:
			changeMassMailByFile();
			break;
		case 3:
			changeMassMailByJournalEntry();
			break;
		default:
			break;
		}

		break;

	case 1:
		changeMassMailRecipient();
		break;

	case 2:
		changeMassMailFile();
		break;

	case 3:
		changeMassMailJournalEntry();
		break;

	default:
		break;
	}
	oldMassMailTabIndex = index;
	storeTabIndexParam(index);
	return true;

}

function handleMassMailChanged(xhr, status, args) {

	if (_testFlag(args, AJAX_OPERATION_SUCCESS) && _testPropertyExists(args, AJAX_WINDOW_TITLE_BASE64) && _testPropertyExists(args, AJAX_ROOT_ENTITY_CREATED)
	        && _testPropertyExists(args, AJAX_WINDOW_NAME)) {
		window.name = args[AJAX_WINDOW_NAME];
		enableTabs(massMailTabView, 1, args[AJAX_ROOT_ENTITY_CREATED]);
		handleUpdateMassMailTabTitles(xhr, status, args);
		var title = decodeBase64(args[AJAX_WINDOW_TITLE_BASE64]);
		document.title = title;
		setText(HEADLINE_ID, title);
		updateMassMailEntityMenuBar();
	}

}

function handleUpdateMassMailTabTitles(xhr, status, args) {

	if (_testPropertyExists(args, AJAX_OPERATION_SUCCESS) && _testPropertyExists(args, AJAX_ROOT_ENTITY_CREATED)) {
		massMailTabView.emphasizeTab(reverseTabIndex(0), _testFlag(args, AJAX_OPERATION_SUCCESS) && !args[AJAX_ROOT_ENTITY_CREATED]);
	}

	if (_testPropertyExists(args, AJAX_MASS_MAIL_RECIPIENT_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_MASS_MAIL_RECIPIENT_COUNT)) {
		massMailTabView.setTabTitle(reverseTabIndex(1), decodeBase64(args[AJAX_MASS_MAIL_RECIPIENT_TAB_TITLE_BASE64]));
		massMailTabView.emphasizeTab(reverseTabIndex(1), args[AJAX_MASS_MAIL_RECIPIENT_COUNT] == 0);
	}
	
	if (_testPropertyExists(args, AJAX_MASS_MAIL_FILE_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_MASS_MAIL_FILE_COUNT)) {
		massMailTabView.setTabTitle(reverseTabIndex(2), decodeBase64(args[AJAX_MASS_MAIL_FILE_TAB_TITLE_BASE64]));
		massMailTabView.emphasizeTab(reverseTabIndex(2), args[AJAX_MASS_MAIL_FILE_COUNT] == 0);
	}

	if (_testPropertyExists(args, AJAX_MASS_MAIL_JOURNAL_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_MASS_MAIL_JOURNAL_ENTRY_COUNT)) {
		massMailTabView.setTabTitle(reverseTabIndex(3), decodeBase64(args[AJAX_MASS_MAIL_JOURNAL_TAB_TITLE_BASE64]));
		massMailTabView.emphasizeTab(reverseTabIndex(3), args[AJAX_MASS_MAIL_JOURNAL_ENTRY_COUNT] == 0);
	}

}

// EXPERIMENTAL_TEMPLATE_VARIABLES: suggest the mass mail template variables (see MassMailBean)
var massMailTemplateVariables = null;

function getMassMailTemplateVariables() {
	if (massMailTemplateVariables == null) {
		var variables = [];
		try {
			variables = JSON.parse(jQuery(".ctsms-mass-mail-template-variables").first().text() || "[]") || [];
		} catch (e) {
		}
		// "proband_tagvalues.tag_name" lists the key tag_name of the items of $proband_tagvalues
		massMailTemplateVariables = {
			names : [],
			members : {}
		};
		for (var i = 0; i < variables.length; i++) {
			var separator = variables[i].indexOf(".");
			if (separator < 0) {
				massMailTemplateVariables.names.push(variables[i]);
			} else {
				var name = variables[i].substring(0, separator);
				if (!massMailTemplateVariables.members.hasOwnProperty(name)) {
					massMailTemplateVariables.members[name] = [];
				}
				massMailTemplateVariables.members[name].push(variables[i].substring(separator + 1));
			}
		}
	}
	return massMailTemplateVariables;
}

// names starting with the query, or if there are none, the names containing it
function filterMassMailTemplateVariables(candidates, query) {
	var lowerCaseQuery = query.toLowerCase();
	var starting = [];
	var containing = [];
	for (var i = 0; i < candidates.length; i++) {
		var index = candidates[i].toLowerCase().indexOf(lowerCaseQuery);
		if (index == 0) {
			starting.push(candidates[i]);
		} else if (index > 0) {
			containing.push(candidates[i]);
		}
	}
	return starting.length > 0 ? starting : containing;
}

// the variables declared in the template by #foreach($x in ...) and #set($x = ...)
function getMassMailTemplateLocalVariables(template) {
	var localVariables = [];
	var declarationPattern = /#\{?(?:foreach|set)\}?\s*\(\s*\$!?\{?([A-Za-z0-9_]+)/g;
	var match;
	while ((match = declarationPattern.exec(template)) != null) {
		if (jQuery.inArray(match[1], localVariables) < 0) {
			localVariables.push(match[1]);
		}
	}
	return localVariables;
}

function getMassMailTemplateVariableMembers(name, template) {
	var variables = getMassMailTemplateVariables();
	if (variables.members.hasOwnProperty(name)) {
		return variables.members[name];
	}
	// the loop variable of a #foreach over a collection, eg. #foreach($tv in $proband_tagvalues)
	var foreachPattern = new RegExp("#\\{?foreach\\}?\\s*\\(\\s*\\$!?\\{?" + name + "\\}?\\s+in\\s+\\$!?\\{?([A-Za-z0-9_]+)", "g");
	var match;
	var collection = null;
	while ((match = foreachPattern.exec(template)) != null) {
		collection = match[1];
	}
	return collection != null && variables.members.hasOwnProperty(collection) ? variables.members[collection] : [];
}

// the suggestions for the text before the caret, or null if it does not end with a variable reference such as "$proband_fi", "${tv." or "$!vis"
function getMassMailTemplateSuggestions(textBefore, nextChar, template) {
	var match = /\$!?(\{?)([A-Za-z0-9_]*)(\.([A-Za-z0-9_]*))?$/.exec(textBefore);
	if (match == null) {
		return null;
	}
	var member = match[3] ? true : false;
	var query = member ? (match[4] || "") : match[2];
	var candidates = member ? getMassMailTemplateVariableMembers(match[2], template) : getMassMailTemplateLocalVariables(template).concat(
	        getMassMailTemplateVariables().names);
	var closingBrace = match[1] == "{" && nextChar != "}" ? "}" : "";
	var list = filterMassMailTemplateVariables(candidates, query);
	for (var i = 0; i < list.length; i++) {
		list[i] = list[i] + closingBrace;
	}
	return {
		list : list,
		start : textBefore.length - query.length
	};
}

function isMassMailTemplateHintTrigger(typed) {
	return typed == "$" || typed == "{" || typed == "!" || typed == ".";
}

function massMailTemplateHint(cm) {
	var cursor = cm.getCursor();
	var line = cm.getLine(cursor.line);
	var suggestions = getMassMailTemplateSuggestions(line.substring(0, cursor.ch), line.charAt(cursor.ch), cm.getValue());
	if (suggestions == null) {
		return null;
	}
	return {
		list : suggestions.list,
		from : {
			line : cursor.line,
			ch : suggestions.start
		},
		to : cursor
	};
}

function showMassMailTemplateHint(cm, typed) {
	// a single suggestion is inserted right away on Ctrl-Space only, not while typing
	CodeMirror.simpleHint(cm, massMailTemplateHint, {
		completeSingle : !typed
	});
}

function handleMassMailTextTemplateKeyEvent(cm, event) {
	if (event.type == "keypress" && isMassMailTemplateHintTrigger(String.fromCharCode(event.charCode || event.keyCode))) {
		// show the suggestions once the typed character is in the editor
		window.setTimeout(function() {
			if (massMailTemplateHint(cm) != null) {
				showMassMailTemplateHint(cm, true);
			}
		}, 50);
	}
	return false;
}

function initMassMailTextTemplateEditor(attempt) {
	attempt = attempt || 0;
	massMailTemplateVariables = null;
	if (typeof massMailTextTemplateEditor !== "undefined" && massMailTextTemplateEditor && massMailTextTemplateEditor.instance) {
		massMailTextTemplateEditor.instance.setOption("onKeyEvent", handleMassMailTextTemplateKeyEvent);
	} else if (attempt < 40) {
		// the codeMirror widget may initialize after this script ran
		window.setTimeout(function() {
			initMassMailTextTemplateEditor(attempt + 1);
		}, 50);
	}
}

// the html editor (CLEditor) gets suggestions like the codeMirror ones, in a popup at the caret
var massMailTemplateHtmlHint = null;

function getMassMailTemplateHtmlSuggestions(editor) {
	var selection = editor.doc.getSelection ? editor.doc.getSelection() : null;
	if (selection == null || selection.rangeCount == 0 || !selection.isCollapsed) {
		return null;
	}
	var range = selection.getRangeAt(0);
	if (range.startContainer.nodeType != 3) {
		return null;
	}
	var node = range.startContainer;
	var suggestions = getMassMailTemplateSuggestions(node.data.substring(0, range.startOffset), node.data.charAt(range.startOffset), jQuery(editor.doc.body).text());
	if (suggestions == null || suggestions.list.length == 0) {
		return null;
	}
	suggestions.node = node;
	suggestions.end = range.startOffset;
	return suggestions;
}

function closeMassMailTemplateHtmlHint() {
	if (massMailTemplateHtmlHint != null) {
		massMailTemplateHtmlHint.popup.remove();
		massMailTemplateHtmlHint = null;
	}
}

function pickMassMailTemplateHtmlHint(editor, suggestions, value) {
	closeMassMailTemplateHtmlHint();
	var node = suggestions.node;
	node.data = node.data.substring(0, suggestions.start) + value + node.data.substring(suggestions.end);
	editor.$frame[0].contentWindow.focus();
	var range = editor.doc.createRange();
	range.setStart(node, suggestions.start + value.length);
	range.collapse(true);
	var selection = editor.doc.getSelection();
	selection.removeAllRanges();
	selection.addRange(range);
	editor.updateTextArea();
}

function showMassMailTemplateHtmlHint(editor, typed) {
	closeMassMailTemplateHtmlHint();
	var suggestions = getMassMailTemplateHtmlSuggestions(editor);
	if (suggestions == null) {
		return;
	}
	// a single suggestion is inserted right away on Ctrl-Space only, not while typing
	if (!typed && suggestions.list.length == 1) {
		pickMassMailTemplateHtmlHint(editor, suggestions, suggestions.list[0]);
		return;
	}
	var popup = jQuery("<div class=\"ctsms-mass-mail-template-hint\"><select multiple=\"multiple\"></select></div>");
	var select = popup.children("select")[0];
	for (var i = 0; i < suggestions.list.length; i++) {
		jQuery("<option></option>").text(suggestions.list[i]).appendTo(select);
	}
	select.size = Math.min(10, suggestions.list.length);
	select.selectedIndex = 0;
	// below the typed reference, which includes at least the "$"
	var range = editor.doc.createRange();
	range.setStart(suggestions.node, Math.max(0, suggestions.start - 1));
	range.setEnd(suggestions.node, suggestions.end);
	var rect = range.getBoundingClientRect();
	var frameOffset = editor.$frame.offset();
	popup.css({
		left : (frameOffset.left + rect.left) + "px",
		top : (frameOffset.top + rect.bottom) + "px"
	}).appendTo(document.body);
	jQuery(select).on("click", function() {
		if (select.selectedIndex >= 0) {
			pickMassMailTemplateHtmlHint(editor, suggestions, select.options[select.selectedIndex].text);
		}
	});
	massMailTemplateHtmlHint = {
		popup : popup,
		select : select,
		suggestions : suggestions
	};
}

function moveMassMailTemplateHtmlHintSelection(delta) {
	var select = massMailTemplateHtmlHint.select;
	select.selectedIndex = Math.min(select.options.length - 1, Math.max(0, select.selectedIndex + delta));
	var option = select.options[select.selectedIndex];
	if (option.offsetTop < select.scrollTop) {
		select.scrollTop = option.offsetTop;
	} else if (option.offsetTop + option.offsetHeight > select.scrollTop + select.clientHeight) {
		select.scrollTop = option.offsetTop + option.offsetHeight - select.clientHeight;
	}
}

function handleMassMailTemplateHtmlKeyDown(editor, event) {
	if (massMailTemplateHtmlHint != null) {
		var select = massMailTemplateHtmlHint.select;
		var handled = true;
		switch (event.which) {
		case 38:
			moveMassMailTemplateHtmlHintSelection(-1);
			break;
		case 40:
			moveMassMailTemplateHtmlHintSelection(1);
			break;
		case 33:
			moveMassMailTemplateHtmlHintSelection(-select.size);
			break;
		case 34:
			moveMassMailTemplateHtmlHintSelection(select.size);
			break;
		case 9:
		case 13:
			pickMassMailTemplateHtmlHint(editor, massMailTemplateHtmlHint.suggestions, select.options[select.selectedIndex].text);
			break;
		case 27:
			closeMassMailTemplateHtmlHint();
			break;
		default:
			handled = false;
			break;
		}
		if (handled) {
			event.preventDefault();
			return;
		}
	}
	if (event.ctrlKey && event.which == 32) {
		event.preventDefault();
		showMassMailTemplateHtmlHint(editor, false);
	}
}

function handleMassMailTemplateHtmlKeyPress(editor, event) {
	if (isMassMailTemplateHintTrigger(String.fromCharCode(event.which))) {
		// show the suggestions once the typed character is in the editor
		window.setTimeout(function() {
			showMassMailTemplateHtmlHint(editor, true);
		}, 0);
	}
}

function handleMassMailTemplateHtmlKeyUp(editor, event) {
	// narrow the open suggestions down to the typed text, or close them
	if (massMailTemplateHtmlHint != null && jQuery.inArray(event.which, [ 9, 13, 16, 17, 18, 27, 33, 34, 38, 40 ]) < 0) {
		showMassMailTemplateHtmlHint(editor, true);
	}
}

function bindMassMailTemplateHtmlEditor(editor) {
	if (editor.doc == null || editor.doc.ctsmsMassMailTemplateHint) {
		return;
	}
	editor.doc.ctsmsMassMailTemplateHint = true;
	jQuery(editor.doc).on("keydown", function(event) {
		handleMassMailTemplateHtmlKeyDown(editor, event);
	}).on("keypress", function(event) {
		handleMassMailTemplateHtmlKeyPress(editor, event);
	}).on("keyup", function(event) {
		handleMassMailTemplateHtmlKeyUp(editor, event);
	}).on("mousedown", closeMassMailTemplateHtmlHint);
}

function initMassMailTextTemplateHtmlEditor(attempt) {
	attempt = attempt || 0;
	massMailTemplateVariables = null;
	closeMassMailTemplateHtmlHint();
	if (typeof massMailTextTemplateHtmlEditor !== "undefined" && massMailTextTemplateHtmlEditor && massMailTextTemplateHtmlEditor.editor
	        && massMailTextTemplateHtmlEditor.editor.doc) {
		var editor = massMailTextTemplateHtmlEditor.editor;
		bindMassMailTemplateHtmlEditor(editor);
		// CLEditor replaces its iframe document when it is refreshed
		editor.$main.off("mouseenter.ctsmsMassMailTemplateHint").on("mouseenter.ctsmsMassMailTemplateHint", function() {
			bindMassMailTemplateHtmlEditor(editor);
		});
		jQuery(document).off("mousedown.ctsmsMassMailTemplateHint").on("mousedown.ctsmsMassMailTemplateHint", function(event) {
			if (jQuery(event.target).closest(".ctsms-mass-mail-template-hint").length == 0) {
				closeMassMailTemplateHtmlHint();
			}
		});
	} else if (attempt < 40) {
		// the editor widget may initialize after this script ran
		window.setTimeout(function() {
			initMassMailTextTemplateHtmlEditor(attempt + 1);
		}, 50);
	}
}
