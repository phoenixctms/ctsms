
//var ecrfSectionToClear = null;

var oldProbandTabIndex = 0;

function handleProbandTabChange(index) {

	FieldCalculation.resetInputFieldVariables();
	switch (index) {
	case 0:

		switch (oldProbandTabIndex) {
		case 0:
			break;
		case 1:
			changeProbandByImage();
			break;
		case 2:
			changeProbandByTag();
			break;
		case 3:
			changeProbandByContactDetail();
			break;
		case 4:
			changeProbandByAddress();
			break;
		case 5:
			changeProbandByStatus();
			break;
		case 6:
			changeProbandByInventoryBooking();
			break;
		case 7:
			changeProbandByDiagnosis();
			break;
		case 8:
			changeProbandByProcedure();
			break;
		case 9:
			changeProbandByMedication();
			break;
		case 10:

			break;
		case 11:
			changeProbandByTrialParticipation();
			break;
		case 12:

			break;
		case 13:

			break;
		case 14:
			changeProbandByBankAccount();
			break;
		case 15:
			changeProbandByMoneyTransfer();
			break;


		case 16:
			changeProbandByFile();
			break;
		case 17:
			changeProbandByJournalEntry();
			break;

		default:
			break;
		}

		break;

	case 1:
		changeProbandImage();
		break;
	case 2:
		changeProbandTag();
		break;
	case 3:
		changeProbandContactDetail();
		break;
	case 4:
		changeProbandAddress();
		break;
	case 5:
		changeProbandStatus();
		break;
	case 6:
		changeProbandInventoryBooking();
		break;
	case 7:
		changeDiagnosis();
		break;
	case 8:
		changeProcedure();
		break;
	case 9:
		changeMedication();
		break;
	case 10:
		changeInquiryValue();
		break;
	case 11:
		changeTrialParticipation();
		break;
	case 12:
		changeProbandVisitSchedule();
		break;

	case 13:
		changeProbandEcrfStatusEntry();
		break;

	case 14:
		changeBankAccount();
		break;
	case 15:
		changeMoneyTransfer();
		break;


	case 16:
		changeProbandFile();
		break;

	case 17:
		changeProbandJournalEntry();
		break;

	default:
		break;
	}
	oldProbandTabIndex = index;
	return true;

}

function handleProbandChanged(xhr, status, args) {

	if (_testFlag(args, AJAX_OPERATION_SUCCESS) && _testPropertyExists(args, AJAX_WINDOW_TITLE_BASE64) && _testPropertyExists(args, AJAX_ROOT_ENTITY_CREATED)
	        && _testPropertyExists(args, AJAX_WINDOW_NAME)) {
		window.name = args[AJAX_WINDOW_NAME];
		enableTabs(probandTabView, 1, args[AJAX_ROOT_ENTITY_CREATED]);
		handleUpdateProbandTabTitles(xhr, status, args);
		var title = decodeBase64(args[AJAX_WINDOW_TITLE_BASE64]);
		document.title = title;
		setText(HEADLINE_ID, title);
		updateProbandEntityMenuBar();
	}

}

function handleUpdateTrialParticipationListTabTitles(xhr, status, args) {
	if (_testFlag(args, AJAX_OPERATION_SUCCESS) && _testPropertyExists(args, AJAX_PROBAND_LIST_TAB_TITLE_BASE64)) {
		var title = decodeBase64(args[AJAX_PROBAND_LIST_TAB_TITLE_BASE64]);
		trialParticipationListTabView.setTabTitle(0, title);
	}
}

function handleUpdateAuditTrailTabTitles(xhr, status, args) {

	if (_testFlag(args, AJAX_OPERATION_SUCCESS) && _testPropertyExists(args, AJAX_VALIDATION_ECRF_FIELD_STATUS_ENTRY_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_VALIDATION_ECRF_FIELD_STATUS_ENTRY_COUNT)) {
		auditTrailTabView.setTabTitle(1, decodeBase64(args[AJAX_VALIDATION_ECRF_FIELD_STATUS_ENTRY_TAB_TITLE_BASE64]));
		auditTrailTabView.emphasizeTab(1, args[AJAX_VALIDATION_ECRF_FIELD_STATUS_ENTRY_COUNT] == 0);
	}
	if (_testFlag(args, AJAX_OPERATION_SUCCESS) && _testPropertyExists(args, AJAX_QUERY_ECRF_FIELD_STATUS_ENTRY_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_QUERY_ECRF_FIELD_STATUS_ENTRY_COUNT)) {
		auditTrailTabView.setTabTitle(2, decodeBase64(args[AJAX_QUERY_ECRF_FIELD_STATUS_ENTRY_TAB_TITLE_BASE64]));
		auditTrailTabView.emphasizeTab(2, args[AJAX_QUERY_ECRF_FIELD_STATUS_ENTRY_COUNT] == 0);
	}
	if (_testFlag(args, AJAX_OPERATION_SUCCESS) && _testPropertyExists(args, AJAX_ANNOTATION_ECRF_FIELD_STATUS_ENTRY_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_ANNOTATION_ECRF_FIELD_STATUS_ENTRY_COUNT)) {
		auditTrailTabView.setTabTitle(3, decodeBase64(args[AJAX_ANNOTATION_ECRF_FIELD_STATUS_ENTRY_TAB_TITLE_BASE64]));
		auditTrailTabView.emphasizeTab(3, args[AJAX_ANNOTATION_ECRF_FIELD_STATUS_ENTRY_COUNT] == 0);
	}

}

function handleUpdateProbandTabTitles(xhr, status, args) {

	if (_testPropertyExists(args, AJAX_OPERATION_SUCCESS) && _testPropertyExists(args, AJAX_ROOT_ENTITY_CREATED)) {
		probandTabView.emphasizeTab(0, _testFlag(args, AJAX_OPERATION_SUCCESS) && !args[AJAX_ROOT_ENTITY_CREATED]);
	}

	if (_testPropertyExists(args, AJAX_PROBAND_IMAGE_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_PROBAND_IMAGE_COUNT)) {
		probandTabView.setTabTitle(1, decodeBase64(args[AJAX_PROBAND_IMAGE_TAB_TITLE_BASE64]));
		probandTabView.emphasizeTab(1, args[AJAX_PROBAND_IMAGE_COUNT] == 0);
	}
	if (_testPropertyExists(args, AJAX_PROBAND_TAG_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_PROBAND_TAG_VALUE_COUNT)) {
		probandTabView.setTabTitle(2, decodeBase64(args[AJAX_PROBAND_TAG_TAB_TITLE_BASE64]));
		probandTabView.emphasizeTab(2, args[AJAX_PROBAND_TAG_VALUE_COUNT] == 0);
	}
	if (_testPropertyExists(args, AJAX_PROBAND_CONTACT_DETAIL_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_PROBAND_CONTACT_DETAIL_VALUE_COUNT)) {
		probandTabView.setTabTitle(3, decodeBase64(args[AJAX_PROBAND_CONTACT_DETAIL_TAB_TITLE_BASE64]));
		probandTabView.emphasizeTab(3, args[AJAX_PROBAND_CONTACT_DETAIL_VALUE_COUNT] == 0);
	}
	if (_testPropertyExists(args, AJAX_PROBAND_ADDRESS_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_PROBAND_ADDRESS_COUNT)) {
		probandTabView.setTabTitle(4, decodeBase64(args[AJAX_PROBAND_ADDRESS_TAB_TITLE_BASE64]));
		probandTabView.emphasizeTab(4, args[AJAX_PROBAND_ADDRESS_COUNT] == 0);
	}

	if (_testPropertyExists(args, AJAX_PROBAND_STATUS_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_PROBAND_STATUS_ENTRY_COUNT)) {
		probandTabView.setTabTitle(5, decodeBase64(args[AJAX_PROBAND_STATUS_TAB_TITLE_BASE64]));
		probandTabView.emphasizeTab(5, args[AJAX_PROBAND_STATUS_ENTRY_COUNT] == 0);
	}
	if (_testPropertyExists(args, AJAX_PROBAND_INVENTORY_BOOKING_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_PROBAND_INVENTORY_BOOKING_COUNT)) {
		probandTabView.setTabTitle(6, decodeBase64(args[AJAX_PROBAND_INVENTORY_BOOKING_TAB_TITLE_BASE64]));
		probandTabView.emphasizeTab(6, args[AJAX_PROBAND_INVENTORY_BOOKING_COUNT] == 0);
	}

	if (_testPropertyExists(args, AJAX_DIAGNOSIS_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_DIAGNOSIS_COUNT)) {
		probandTabView.setTabTitle(7, decodeBase64(args[AJAX_DIAGNOSIS_TAB_TITLE_BASE64]));
		probandTabView.emphasizeTab(7, args[AJAX_DIAGNOSIS_COUNT] == 0);
	}
	if (_testPropertyExists(args, AJAX_PROCEDURE_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_PROCEDURE_COUNT)) {
		probandTabView.setTabTitle(8, decodeBase64(args[AJAX_PROCEDURE_TAB_TITLE_BASE64]));
		probandTabView.emphasizeTab(8, args[AJAX_PROCEDURE_COUNT] == 0);
	}
	if (_testPropertyExists(args, AJAX_MEDICATION_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_MEDICATION_COUNT)) {
		probandTabView.setTabTitle(9, decodeBase64(args[AJAX_MEDICATION_TAB_TITLE_BASE64]));
		probandTabView.emphasizeTab(9, args[AJAX_MEDICATION_COUNT] == 0);
	}

	if (_testPropertyExists(args, AJAX_INQUIRY_VALUE_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_INQUIRY_VALUE_COUNT)) {
		probandTabView.setTabTitle(10, decodeBase64(args[AJAX_INQUIRY_VALUE_TAB_TITLE_BASE64]));
		//inverse here
		probandTabView.emphasizeTab(10, args[AJAX_INQUIRY_VALUE_COUNT] > 0);
	}

	if (_testPropertyExists(args, AJAX_TRIAL_PARTICIPATION_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_TRIAL_PARTICIPATION_COUNT)) {
		probandTabView.setTabTitle(11, decodeBase64(args[AJAX_TRIAL_PARTICIPATION_TAB_TITLE_BASE64]));
		probandTabView.emphasizeTab(11, args[AJAX_TRIAL_PARTICIPATION_COUNT] == 0);
	}
	if (_testPropertyExists(args, AJAX_PROBAND_VISIT_SCHEDULE_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_PROBAND_VISIT_SCHEDULE_ITEM_COUNT)) {
		probandTabView.setTabTitle(12, decodeBase64(args[AJAX_PROBAND_VISIT_SCHEDULE_TAB_TITLE_BASE64]));

	}


	if (_testPropertyExists(args, AJAX_BANK_ACCOUNT_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_BANK_ACCOUNT_COUNT)) {
		probandTabView.setTabTitle(14, decodeBase64(args[AJAX_BANK_ACCOUNT_TAB_TITLE_BASE64]));
		probandTabView.emphasizeTab(14, args[AJAX_BANK_ACCOUNT_COUNT] == 0);
	}

	if (_testPropertyExists(args, AJAX_MONEY_TRANSFER_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_MONEY_TRANSFER_COUNT)) {
		probandTabView.setTabTitle(15, decodeBase64(args[AJAX_MONEY_TRANSFER_TAB_TITLE_BASE64]));
		probandTabView.emphasizeTab(15, args[AJAX_MONEY_TRANSFER_COUNT] == 0);
	}


	if (_testPropertyExists(args, AJAX_PROBAND_FILE_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_PROBAND_FILE_COUNT)) {
		probandTabView.setTabTitle(16, decodeBase64(args[AJAX_PROBAND_FILE_TAB_TITLE_BASE64]));
		probandTabView.emphasizeTab(16, args[AJAX_PROBAND_FILE_COUNT] == 0);
	}
	if (_testPropertyExists(args, AJAX_PROBAND_JOURNAL_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_PROBAND_JOURNAL_ENTRY_COUNT)) {
		probandTabView.setTabTitle(17, decodeBase64(args[AJAX_PROBAND_JOURNAL_TAB_TITLE_BASE64]));
		probandTabView.emphasizeTab(17, args[AJAX_PROBAND_JOURNAL_ENTRY_COUNT] == 0);
	}

}

var contactDetailNotifyPreset;

function setContactDetailNotify(apply) {

	if (apply) {
		contactDetailNotify.setValue(contactDetailNotifyPreset);
	}
	contactDetailNotifyPresetOverrideConfirmation.hide();
	contactDetailNotifyPreset = null;

}

function handleContactDetailTypeSelected(xhr, status, args) {

	if (_testPropertyExists(args, AJAX_CONTACT_DETAIL_TYPE_EMAIL) && _testPropertyExists(args, AJAX_CONTACT_DETAIL_TYPE_PHONE)
	        && _testPropertyExists(args, AJAX_CONTACT_DETAIL_NA) && _testPropertyExists(args, AJAX_CONTACT_DETAIL_TYPE_NOTIFY_PRESET)) {
		var contactDetailEmail = args[AJAX_CONTACT_DETAIL_TYPE_EMAIL];
		var contactDetailPhone = args[AJAX_CONTACT_DETAIL_TYPE_PHONE];
		var contactDetailNa = args[AJAX_CONTACT_DETAIL_NA];
		contactDetailNotifyPreset = args[AJAX_CONTACT_DETAIL_TYPE_NOTIFY_PRESET];
		if (!contactDetailNa && (contactDetailEmail || contactDetailPhone)) {
			contactDetailNotify.enable();
			if (contactDetailNotifyPreset && contactDetailNotifyPreset != contactDetailNotify.getValue()) {
				contactDetailNotifyPresetOverrideConfirmation.show();
				return;
			}
		} else {
			contactDetailNotify.setValue(false);
			contactDetailNotify.disable();
		}
	}
	contactDetailNotifyPreset = null;

}

var addressDeliverPreset;

function setAddressDeliver(apply) {

	if (apply) {
		addressDeliver.setValue(addressDeliverPreset);
	}
	addressDeliverPresetOverrideConfirmation.hide();
	addressDeliverPreset = null;

}

function handleAddressTypeSelected(xhr, status, args) {

	if (_testPropertyExists(args, AJAX_ADDRESS_TYPE_DELIVER_PRESET)) {
		addressDeliverPreset = args[AJAX_ADDRESS_TYPE_DELIVER_PRESET];
		if (addressDeliverPreset && addressDeliverPreset != addressDeliver.getValue()) {
			addressDeliverPresetOverrideConfirmation.show();
			return;
		}
	}
	addressDeliverPreset = null;

}

function setLocationDistance(distance, id, format, widget) {
	if (distance == null || isNaN(parseFloat(distance))) {
		distance = 0.0;
	}
	if (format == null || format.length == 0) {
		format = '%0.1f';
	}
	widget.setValue(sprintf(format,+distance / 1000.0));
}
