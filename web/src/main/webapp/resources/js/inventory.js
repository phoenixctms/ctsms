var oldInventoryTabIndex = 0;

function handleInventoryTabChange(index) {

	switch (index) {
	case 0:

		switch (oldInventoryTabIndex) {
		case 0:
			break;
		case 1:
			changeInventoryByTag();
			break;
		case 2:
			changeInventoryByStatus();
			break;
		case 3:
			changeInventoryByMaintenance();
			break;
		case 4:
			changeInventoryByBooking();
			break;

		case 6:
			changeInventoryByHyperlink();
			break;
		case 7:
			changeInventoryByFile();
			break;
		case 8:
			changeInventoryByJournalEntry();
			break;
		default:
			break;
		}

		break;

	case 1:
		changeInventoryTag();
		break;

	case 2:
		changeInventoryStatus();
		break;

	case 3:
		changeInventoryMaintenance();
		break;

	case 4:
		changeInventoryBooking();
		break;

	case 5:
		changeInventoryBookingDuration();
		break;

	case 6:
		changeInventoryHyperlink();
		break;

	case 7:
		changeInventoryFile();
		break;

	case 8:
		changeInventoryJournalEntry();
		break;

	default:
		break;
	}
	oldInventoryTabIndex = index;
	return true;

}

function handleInventoryChanged(xhr, status, args) {

	if (_testFlag(args, AJAX_OPERATION_SUCCESS) && _testPropertyExists(args, AJAX_WINDOW_TITLE_BASE64) && _testPropertyExists(args, AJAX_ROOT_ENTITY_CREATED)
	        && _testPropertyExists(args, AJAX_WINDOW_NAME)) {
		window.name = args[AJAX_WINDOW_NAME];
		enableTabs(inventoryTabView, 1, args[AJAX_ROOT_ENTITY_CREATED]);
		handleUpdateInventoryTabTitles(xhr, status, args);
		var title = decodeBase64(args[AJAX_WINDOW_TITLE_BASE64]);
		document.title = title;
		setText(HEADLINE_ID, title);
		updateInventoryEntityMenuBar();
	}

}

function handleUpdateInventoryTabTitles(xhr, status, args) {
	if (_testPropertyExists(args, AJAX_OPERATION_SUCCESS) && _testPropertyExists(args, AJAX_ROOT_ENTITY_CREATED)) {
		inventoryTabView.emphasizeTab(0, _testFlag(args, AJAX_OPERATION_SUCCESS) && !args[AJAX_ROOT_ENTITY_CREATED]);
	}
	if (_testPropertyExists(args, AJAX_INVENTORY_TAG_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_INVENTORY_TAG_VALUE_COUNT)) {
		inventoryTabView.setTabTitle(1, decodeBase64(args[AJAX_INVENTORY_TAG_TAB_TITLE_BASE64]));
		inventoryTabView.emphasizeTab(1, args[AJAX_INVENTORY_TAG_VALUE_COUNT] == 0);
	}
	if (_testPropertyExists(args, AJAX_INVENTORY_STATUS_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_INVENTORY_STATUS_ENTRY_COUNT)) {
		inventoryTabView.setTabTitle(2, decodeBase64(args[AJAX_INVENTORY_STATUS_TAB_TITLE_BASE64]));
		inventoryTabView.emphasizeTab(2, args[AJAX_INVENTORY_STATUS_ENTRY_COUNT] == 0);
	}

	if (_testPropertyExists(args, AJAX_INVENTORY_MAINTENANCE_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_INVENTORY_MAINTENANCE_ITEM_COUNT)) {
		inventoryTabView.setTabTitle(3, decodeBase64(args[AJAX_INVENTORY_MAINTENANCE_TAB_TITLE_BASE64]));
		inventoryTabView.emphasizeTab(3, args[AJAX_INVENTORY_MAINTENANCE_ITEM_COUNT] == 0);
	}
	if (_testPropertyExists(args, AJAX_INVENTORY_BOOKING_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_INVENTORY_BOOKING_COUNT)) {
		inventoryTabView.setTabTitle(4, decodeBase64(args[AJAX_INVENTORY_BOOKING_TAB_TITLE_BASE64]));
		inventoryTabView.emphasizeTab(4, args[AJAX_INVENTORY_BOOKING_COUNT] == 0);
	}

	if (_testPropertyExists(args, AJAX_INVENTORY_HYPERLINK_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_INVENTORY_HYPERLINK_COUNT)) {
		inventoryTabView.setTabTitle(6, decodeBase64(args[AJAX_INVENTORY_HYPERLINK_TAB_TITLE_BASE64]));
		inventoryTabView.emphasizeTab(6, args[AJAX_INVENTORY_HYPERLINK_COUNT] == 0);
	}
	if (_testPropertyExists(args, AJAX_INVENTORY_FILE_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_INVENTORY_FILE_COUNT)) {
		inventoryTabView.setTabTitle(7, decodeBase64(args[AJAX_INVENTORY_FILE_TAB_TITLE_BASE64]));
		inventoryTabView.emphasizeTab(7, args[AJAX_INVENTORY_FILE_COUNT] == 0);
	}
	if (_testPropertyExists(args, AJAX_INVENTORY_JOURNAL_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_INVENTORY_JOURNAL_ENTRY_COUNT)) {
		inventoryTabView.setTabTitle(8, decodeBase64(args[AJAX_INVENTORY_JOURNAL_TAB_TITLE_BASE64]));
		inventoryTabView.emphasizeTab(8, args[AJAX_INVENTORY_JOURNAL_ENTRY_COUNT] == 0);
	}

}

var maintenanceItemTitlePreset;

function setMaintenanceItemTitle(apply) {

	if (apply) {
		maintenanceItemTitle.setValue(maintenanceItemTitlePreset);
	}
	maintenanceItemTitlePresetOverrideConfirmation.hide();
	maintenanceItemTitlePreset = null;

}

function handleMaintenanceTypeSelected(xhr, status, args) {

	if (_testPropertyExists(args, AJAX_MAINTENANCE_TYPE_TITLE_PRESET_BASE64)) {
		maintenanceItemTitlePreset = decodeBase64(args[AJAX_MAINTENANCE_TYPE_TITLE_PRESET_BASE64]);
		if (maintenanceItemTitlePreset != null && maintenanceItemTitlePreset.length > 0) {
			if (maintenanceItemTitle.getValue().length > 0 && maintenanceItemTitlePreset != maintenanceItemTitle.getValue()) {
				maintenanceItemTitlePresetOverrideConfirmation.show();
				return;
			} else {
				maintenanceItemTitle.setValue(maintenanceItemTitlePreset);
			}
		}
	}
	maintenanceItemTitlePreset = null;

}

function handleBookingDepartmentCategorySelected(xhr, status, args) {

	updateBookingSchedule();

}

function updateBookingSchedule() {

	//ajaxWait = false;
	showWaitDialog();
	bookingSchedule.update();
	hideWaitDialog();

}

function handleBookingScheduleEventSelected(xhr, status, args) {

	if (!_testFlag(args, AJAX_CANCEL)) {
		bookingDialog.show();
	} else {
		bookingDialog.hide();
		updateBookingSchedule();
	}

}

function handleBookingScheduleEventMoved(xhr, status, args) {

	updateBookingSchedule();

}

function handleBookingScheduleEventResized(xhr, status, args) {

	updateBookingSchedule();

}
