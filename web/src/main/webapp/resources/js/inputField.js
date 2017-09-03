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

		case 4:
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
		changeInputFieldAssociation();
		break;

	case 4:
		changeInputFieldJournalEntry();
		break;

	default:
		break;
	}
	oldInputFieldTabIndex = index;
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
		inputFieldTabView.emphasizeTab(0, _testFlag(args, AJAX_OPERATION_SUCCESS) && !args[AJAX_ROOT_ENTITY_CREATED]);
	}

	if (_testPropertyExists(args, AJAX_SELECTION_SET_VALUE_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_SELECTION_SET_VALUE_COUNT)) {
		inputFieldTabView.setTabTitle(1, decodeBase64(args[AJAX_SELECTION_SET_VALUE_TAB_TITLE_BASE64]));
		inputFieldTabView.emphasizeTab(1, args[AJAX_SELECTION_SET_VALUE_COUNT] == 0);
	}

	if (_testPropertyExists(args, AJAX_INPUT_FIELD_JOURNAL_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_INPUT_FIELD_JOURNAL_ENTRY_COUNT)) {
		inputFieldTabView.setTabTitle(4, decodeBase64(args[AJAX_INPUT_FIELD_JOURNAL_TAB_TITLE_BASE64]));
		inputFieldTabView.emphasizeTab(4, args[AJAX_INPUT_FIELD_JOURNAL_ENTRY_COUNT] == 0);
	}

}
