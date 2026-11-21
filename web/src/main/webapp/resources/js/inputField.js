var oldInputFieldTabIndex = 0;

function handleInputFieldTabChange(index) {

	FieldCalculation.resetInputFieldVariables();
	switch (index) {
	case 0:

		switch (oldInputFieldTabIndex) {
		case 0:
			break;
		case 1:
			changeInputFieldBySelectionSetValue();
			break;

		case 5:
			changeInputFieldByJob();
			break;
		case 6:
			changeInputFieldByJournalEntry();
			break;
		default:
			break;
		}

		break;

	case 1:
		changeSelectionSetValue();
		break;

	case 2:
		changeDummy();
		break;

	case 3:
		changeInputFieldL10n();
		break;

	case 4:
		changeInputFieldAssociation();
		break;

	case 5:
		changeInputFieldJob();
		break;

	case 6:
		changeInputFieldJournalEntry();
		break;

	default:
		break;
	}
	oldInputFieldTabIndex = index;
	storeTabIndexParam(index);
	return true;

}

function handleInputFieldChanged(xhr, status, args) {

	if (_testFlag(args, AJAX_OPERATION_SUCCESS) && _testPropertyExists(args, AJAX_WINDOW_TITLE_BASE64) && _testPropertyExists(args, AJAX_ROOT_ENTITY_CREATED)
	        && _testPropertyExists(args, AJAX_WINDOW_NAME)) {
		window.name = args[AJAX_WINDOW_NAME];
		enableTabs(inputFieldTabView, 1, args[AJAX_ROOT_ENTITY_CREATED]);
		handleUpdateInputFieldTabTitles(xhr, status, args);
		var title = decodeBase64(args[AJAX_WINDOW_TITLE_BASE64]);
		document.title = title;
		setText(HEADLINE_ID, title);
		updateInputFieldEntityMenuBar();
	}

}

function handleUpdateInputFieldTabTitles(xhr, status, args) {

	if (_testPropertyExists(args, AJAX_OPERATION_SUCCESS) && _testPropertyExists(args, AJAX_ROOT_ENTITY_CREATED)) {
		inputFieldTabView.emphasizeTab(reverseTabIndex(0), _testFlag(args, AJAX_OPERATION_SUCCESS) && !args[AJAX_ROOT_ENTITY_CREATED]);
	}

	if (_testPropertyExists(args, AJAX_SELECTION_SET_VALUE_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_SELECTION_SET_VALUE_COUNT)) {
		inputFieldTabView.setTabTitle(reverseTabIndex(1), decodeBase64(args[AJAX_SELECTION_SET_VALUE_TAB_TITLE_BASE64]));
		inputFieldTabView.emphasizeTab(reverseTabIndex(1), args[AJAX_SELECTION_SET_VALUE_COUNT] == 0);
	}

	if (_testPropertyExists(args, AJAX_INPUT_FIELD_JOB_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_INPUT_FIELD_JOB_COUNT)) {
		inputFieldTabView.setTabTitle(reverseTabIndex(5), decodeBase64(args[AJAX_INPUT_FIELD_JOB_TAB_TITLE_BASE64]));
		inputFieldTabView.emphasizeTab(reverseTabIndex(5), args[AJAX_INPUT_FIELD_JOB_COUNT] == 0);
	}
	
	if (_testPropertyExists(args, AJAX_INPUT_FIELD_JOURNAL_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_INPUT_FIELD_JOURNAL_ENTRY_COUNT)) {
		inputFieldTabView.setTabTitle(reverseTabIndex(6), decodeBase64(args[AJAX_INPUT_FIELD_JOURNAL_TAB_TITLE_BASE64]));
		inputFieldTabView.emphasizeTab(reverseTabIndex(6), args[AJAX_INPUT_FIELD_JOURNAL_ENTRY_COUNT] == 0);
	}

}

// localization tab: an editor created in a hidden tab has no size until it is refreshed
function refreshInputFieldL10nEditor(index) {
	var editor = window['widget_tabView_inputfieldl10n_form_l10nbundletabs_l10ntext_' + index];
	if (editor && editor.instance) {
		editor.instance.refresh();
	}
}

// sketch selection set values: the regions sketch and its JSON editor follow each other while editing

var SELECTION_SET_VALUE_REGIONS_JSON_DELAY = 300;
var SELECTION_SET_VALUE_REGIONS_JSON_MIN_HEIGHT = 120;
var SELECTION_SET_VALUE_REGIONS_JSON_MAX_HEIGHT = 300;

var selectionSetValueRegionsSyncing = false;
var selectionSetValueRegionsJsonTimer = null;

function _getSelectionSetValueRegionsSketch() {
	if (typeof selectionSetValueRegionsSketch !== "undefined" && selectionSetValueRegionsSketch) {
		return selectionSetValueRegionsSketch;
	}
	return null;
}

function _getSelectionSetValueRegionsJsonEditor() {
	if (typeof selectionSetValueRegionsJsonEditor !== "undefined" && selectionSetValueRegionsJsonEditor && selectionSetValueRegionsJsonEditor.instance) {
		return selectionSetValueRegionsJsonEditor.instance;
	}
	return null;
}

function _beautifySelectionSetValueRegionsJson(value) {
	if (value == null || value.length == 0) {
		return "";
	}
	try {
		return JSON.stringify(JSON.parse(value), null, 2);
	} catch (e) {
		return value;
	}
}

function handleSelectionSetValueRegionsSketchChange(sketchpad) {
	var editor = _getSelectionSetValueRegionsJsonEditor();
	if (selectionSetValueRegionsSyncing || editor == null) {
		return;
	}
	if (selectionSetValueRegionsJsonTimer != null) {
		// a drawing wins over pending JSON edits
		window.clearTimeout(selectionSetValueRegionsJsonTimer);
		selectionSetValueRegionsJsonTimer = null;
	}
	selectionSetValueRegionsSyncing = true;
	try {
		editor.setValue(_beautifySelectionSetValueRegionsJson(Sketch.getSketchValue(sketchpad)));
	} finally {
		selectionSetValueRegionsSyncing = false;
	}
}

function _applySelectionSetValueRegionsJson() {
	selectionSetValueRegionsJsonTimer = null;
	var sketchpad = _getSelectionSetValueRegionsSketch();
	var editor = _getSelectionSetValueRegionsJsonEditor();
	if (sketchpad == null || editor == null) {
		return;
	}
	var text = jQuery.trim(editor.getValue());
	var strokes;
	if (text.length == 0) {
		strokes = [];
	} else {
		try {
			strokes = JSON.parse(text);
		} catch (e) {
			return; // still typing: keep the sketch as it is
		}
		if (!jQuery.isArray(strokes)) {
			return;
		}
		for (var i = 0; i < strokes.length; i++) {
			if (strokes[i] == null || typeof strokes[i] !== "object" || !strokes[i].path) {
				return;
			}
		}
	}
	var current = [];
	try {
		current = JSON.parse(Sketch.getSketchValue(sketchpad) || "[]");
	} catch (e) {
	}
	if (JSON.stringify(strokes) == JSON.stringify(current)) {
		return; // only the formatting changed
	}
	selectionSetValueRegionsSyncing = true;
	try {
		if (strokes.length > 0) {
			sketchpad.strokes(strokes);
		} else {
			sketchpad.clear();
		}
	} catch (e) {
	} finally {
		selectionSetValueRegionsSyncing = false;
	}
}

function _handleSelectionSetValueRegionsJsonChange() {
	if (selectionSetValueRegionsSyncing) {
		return;
	}
	if (selectionSetValueRegionsJsonTimer != null) {
		window.clearTimeout(selectionSetValueRegionsJsonTimer);
	}
	selectionSetValueRegionsJsonTimer = window.setTimeout(_applySelectionSetValueRegionsJson, SELECTION_SET_VALUE_REGIONS_JSON_DELAY);
}

function initSelectionSetValueRegionsSync(attempt) {
	attempt = attempt || 0;
	if (attempt == 0 && selectionSetValueRegionsJsonTimer != null) {
		window.clearTimeout(selectionSetValueRegionsJsonTimer);
		selectionSetValueRegionsJsonTimer = null;
	}
	var sketchpad = _getSelectionSetValueRegionsSketch();
	var editor = _getSelectionSetValueRegionsJsonEditor();
	if (sketchpad == null || editor == null) {
		if (attempt < 40) {
			// the codeMirror widget may initialize after this script ran
			window.setTimeout(function() {
				initSelectionSetValueRegionsSync(attempt + 1);
			}, 50);
		}
		return;
	}
	if (!editor.selectionSetValueRegionsSync) {
		editor.selectionSetValueRegionsSync = true;
		// keep the widget's handler, which copies the text to the textarea that gets submitted
		var onChange = editor.getOption("onChange");
		editor.setOption("onChange", function() {
			if (onChange) {
				onChange.apply(this, arguments);
			}
			_handleSelectionSetValueRegionsJsonChange();
		});
	}
	// next to the drawing area, as high as it: the label stays on top, the space below it
	// takes the height of the sketch toolbar (the editor's frame is drawn by its table cell)
	var sketch = jQuery(PrimeFaces.escapeClientId(sketchpad.inputId())).parent();
	var pad = sketch.children(".sketchpad");
	var height = Math.min(SELECTION_SET_VALUE_REGIONS_JSON_MAX_HEIGHT, Math.max(SELECTION_SET_VALUE_REGIONS_JSON_MIN_HEIGHT, pad.outerHeight()));
	editor.getScrollerElement().style.height = height + "px";
	editor.refresh();
	var editorCell = jQuery(editor.getWrapperElement()).closest("td");
	var labelCell = editorCell.closest("tr").prev("tr").children("td").first();
	if (pad.length > 0 && labelCell.length > 0) {
		labelCell.css("padding-bottom", "");
		var offset = Math.round(pad.offset().top - editorCell.offset().top);
		labelCell.css("padding-bottom", Math.max(0, parseInt(labelCell.css("padding-bottom"), 10) + offset) + "px");
	}
}
