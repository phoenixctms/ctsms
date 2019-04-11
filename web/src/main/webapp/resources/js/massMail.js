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
		massMailTabView.emphasizeTab(0, _testFlag(args, AJAX_OPERATION_SUCCESS) && !args[AJAX_ROOT_ENTITY_CREATED]);
	}

	if (_testPropertyExists(args, AJAX_MASS_MAIL_RECIPIENT_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_MASS_MAIL_RECIPIENT_COUNT)) {
		massMailTabView.setTabTitle(1, decodeBase64(args[AJAX_MASS_MAIL_RECIPIENT_TAB_TITLE_BASE64]));
		massMailTabView.emphasizeTab(1, args[AJAX_MASS_MAIL_RECIPIENT_COUNT] == 0);
	}
	
	if (_testPropertyExists(args, AJAX_MASS_MAIL_FILE_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_MASS_MAIL_FILE_COUNT)) {
		massMailTabView.setTabTitle(2, decodeBase64(args[AJAX_MASS_MAIL_FILE_TAB_TITLE_BASE64]));
		massMailTabView.emphasizeTab(2, args[AJAX_MASS_MAIL_FILE_COUNT] == 0);
	}

	if (_testPropertyExists(args, AJAX_MASS_MAIL_JOURNAL_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_MASS_MAIL_JOURNAL_ENTRY_COUNT)) {
		massMailTabView.setTabTitle(3, decodeBase64(args[AJAX_MASS_MAIL_JOURNAL_TAB_TITLE_BASE64]));
		massMailTabView.emphasizeTab(3, args[AJAX_MASS_MAIL_JOURNAL_ENTRY_COUNT] == 0);
	}

}