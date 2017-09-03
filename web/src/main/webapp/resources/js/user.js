var oldUserTabIndex = 0;

function handleUserTabChange(index) {

	switch (index) {
	case 0:

		switch (oldUserTabIndex) {
		case 0:
			break;

		case 4:
			changeUserByJournalEntry();
			break;

		default:
			break;
		}

		break;

	case 1:
		changePassword();
		break;
	case 2:
		changeUserPermissionProfiles();
		break;

	case 3:
		changeUserActivity();
		break;

	case 4:
		changeUserJournalEntry();
		break;

	default:
		break;
	}
	oldUserTabIndex = index;
	return true;

}

function handleUserChanged(xhr, status, args) {

	if (_testFlag(args, AJAX_LOGGED_OUT)) {
		handleLogout(xhr, status, args);
	} else if (_testFlag(args, AJAX_OPERATION_SUCCESS) && _testPropertyExists(args, AJAX_WINDOW_TITLE_BASE64)
	        && _testPropertyExists(args, AJAX_ROOT_ENTITY_CREATED) && _testPropertyExists(args, AJAX_WINDOW_NAME)) {
		window.name = args[AJAX_WINDOW_NAME];
		enableTabs(userTabView, 1, args[AJAX_ROOT_ENTITY_CREATED]);
		handleUpdateUserTabTitles(xhr, status, args);
		var title = decodeBase64(args[AJAX_WINDOW_TITLE_BASE64]);
		document.title = title;
		setText(HEADLINE_ID, title);
		updateUserEntityMenuBar();
	}

}

function handleUpdateUserTabTitles(xhr, status, args) {
	if (_testFlag(args, AJAX_LOGGED_OUT)) {
		handleLogout(xhr, status, args);
	} else {
		if (_testPropertyExists(args, AJAX_OPERATION_SUCCESS) && _testPropertyExists(args, AJAX_ROOT_ENTITY_CREATED)) {
			userTabView.emphasizeTab(0, _testFlag(args, AJAX_OPERATION_SUCCESS) && !args[AJAX_ROOT_ENTITY_CREATED]);
		}

		if (_testPropertyExists(args, AJAX_PASSWORD_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_PASSWORD_COUNT)) {
			userTabView.setTabTitle(1, decodeBase64(args[AJAX_PASSWORD_TAB_TITLE_BASE64]));
			userTabView.emphasizeTab(1, args[AJAX_PASSWORD_COUNT] == 0);
		}

		if (_testPropertyExists(args, AJAX_USER_PERMISSION_PROFILE_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_USER_PERMISSION_PROFILE_COUNT)) {
			userTabView.setTabTitle(2, decodeBase64(args[AJAX_USER_PERMISSION_PROFILE_TAB_TITLE_BASE64]));
			userTabView.emphasizeTab(2, args[AJAX_USER_PERMISSION_PROFILE_COUNT] == 0);
		}

		if (_testPropertyExists(args, AJAX_USER_JOURNAL_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_USER_JOURNAL_ENTRY_COUNT)) {
			userTabView.setTabTitle(4, decodeBase64(args[AJAX_USER_JOURNAL_TAB_TITLE_BASE64]));
			userTabView.emphasizeTab(4, args[AJAX_USER_JOURNAL_ENTRY_COUNT] == 0);
		}
	}

}
