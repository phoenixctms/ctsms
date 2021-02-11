

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
			changeProbandByRecipient();
			break;			
		case 5:
			changeProbandByAddress();
			break;
		case 6:
			changeProbandByStatus();
			break;
		case 7:
			changeProbandByInventoryBooking();
			break;
		case 8:
			changeProbandByDiagnosis();
			break;
		case 9:
			changeProbandByProcedure();
			break;
		case 10:
			changeProbandByMedication();
			break;
		case 11:

			break;
		case 12:
			changeProbandByTrialParticipation();
			break;
		case 13:

			break;
		case 14:

			break;
		case 15:
			changeProbandByBankAccount();
			break;
		case 16:
			changeProbandByMoneyTransfer();
			break;


		case 17:
			changeProbandByJob();
			break;
		case 18:
			changeProbandByFile();
			break;
		case 19:
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
		changeProbandRecipient();
		break;		
	case 5:
		changeProbandAddress();
		break;
	case 6:
		changeProbandStatus();
		break;
	case 7:
		changeProbandInventoryBooking();
		break;
	case 8:
		changeDiagnosis();
		break;
	case 9:
		changeProcedure();
		break;
	case 10:
		changeMedication();
		break;
	case 11:
		changeInquiryValue();
		break;
	case 12:
		changeTrialParticipation();
		break;
	case 13:
		changeProbandVisitSchedule();
		break;

	case 14:
		changeProbandEcrfStatusEntry();
		break;

	case 15:
		changeBankAccount();
		break;
	case 16:
		changeMoneyTransfer();
		break;

	case 17:
		changeProbandJob();
		break;
	case 18:
		changeProbandFile();
		break;

	case 19:
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

function handleUpdateProbandTabTitles(xhr, status, args) {

	if (_testPropertyExists(args, AJAX_OPERATION_SUCCESS) && _testPropertyExists(args, AJAX_ROOT_ENTITY_CREATED)) {
		probandTabView.emphasizeTab(reverseTabIndex(0), _testFlag(args, AJAX_OPERATION_SUCCESS) && !args[AJAX_ROOT_ENTITY_CREATED]);
	}

	if (_testPropertyExists(args, AJAX_PROBAND_IMAGE_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_PROBAND_IMAGE_COUNT)) {
		probandTabView.setTabTitle(reverseTabIndex(1), decodeBase64(args[AJAX_PROBAND_IMAGE_TAB_TITLE_BASE64]));
		probandTabView.emphasizeTab(reverseTabIndex(1), args[AJAX_PROBAND_IMAGE_COUNT] == 0);
	}
	if (_testPropertyExists(args, AJAX_PROBAND_TAG_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_PROBAND_TAG_VALUE_COUNT)) {
		probandTabView.setTabTitle(reverseTabIndex(2), decodeBase64(args[AJAX_PROBAND_TAG_TAB_TITLE_BASE64]));
		probandTabView.emphasizeTab(reverseTabIndex(2), args[AJAX_PROBAND_TAG_VALUE_COUNT] == 0);
	}
	if (_testPropertyExists(args, AJAX_PROBAND_CONTACT_DETAIL_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_PROBAND_CONTACT_DETAIL_VALUE_COUNT)) {
		probandTabView.setTabTitle(reverseTabIndex(3), decodeBase64(args[AJAX_PROBAND_CONTACT_DETAIL_TAB_TITLE_BASE64]));
		probandTabView.emphasizeTab(reverseTabIndex(3), args[AJAX_PROBAND_CONTACT_DETAIL_VALUE_COUNT] == 0);
	}
	if (_testPropertyExists(args, AJAX_PROBAND_RECIPIENT_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_PROBAND_RECIPIENT_COUNT)) {
		probandTabView.setTabTitle(reverseTabIndex(4), decodeBase64(args[AJAX_PROBAND_RECIPIENT_TAB_TITLE_BASE64]));
		probandTabView.emphasizeTab(reverseTabIndex(4), args[AJAX_PROBAND_RECIPIENT_COUNT] == 0);
	}	
	if (_testPropertyExists(args, AJAX_PROBAND_ADDRESS_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_PROBAND_ADDRESS_COUNT)) {
		probandTabView.setTabTitle(reverseTabIndex(5), decodeBase64(args[AJAX_PROBAND_ADDRESS_TAB_TITLE_BASE64]));
		probandTabView.emphasizeTab(reverseTabIndex(5), args[AJAX_PROBAND_ADDRESS_COUNT] == 0);
	}

	if (_testPropertyExists(args, AJAX_PROBAND_STATUS_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_PROBAND_STATUS_ENTRY_COUNT)) {
		probandTabView.setTabTitle(reverseTabIndex(6), decodeBase64(args[AJAX_PROBAND_STATUS_TAB_TITLE_BASE64]));
		probandTabView.emphasizeTab(reverseTabIndex(6), args[AJAX_PROBAND_STATUS_ENTRY_COUNT] == 0);
	}
	if (_testPropertyExists(args, AJAX_PROBAND_INVENTORY_BOOKING_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_PROBAND_INVENTORY_BOOKING_COUNT)) {
		probandTabView.setTabTitle(reverseTabIndex(7), decodeBase64(args[AJAX_PROBAND_INVENTORY_BOOKING_TAB_TITLE_BASE64]));
		probandTabView.emphasizeTab(reverseTabIndex(7), args[AJAX_PROBAND_INVENTORY_BOOKING_COUNT] == 0);
	}

	if (_testPropertyExists(args, AJAX_DIAGNOSIS_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_DIAGNOSIS_COUNT)) {
		probandTabView.setTabTitle(reverseTabIndex(8), decodeBase64(args[AJAX_DIAGNOSIS_TAB_TITLE_BASE64]));
		probandTabView.emphasizeTab(reverseTabIndex(8), args[AJAX_DIAGNOSIS_COUNT] == 0);
	}
	if (_testPropertyExists(args, AJAX_PROCEDURE_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_PROCEDURE_COUNT)) {
		probandTabView.setTabTitle(reverseTabIndex(9), decodeBase64(args[AJAX_PROCEDURE_TAB_TITLE_BASE64]));
		probandTabView.emphasizeTab(reverseTabIndex(9), args[AJAX_PROCEDURE_COUNT] == 0);
	}
	if (_testPropertyExists(args, AJAX_MEDICATION_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_MEDICATION_COUNT)) {
		probandTabView.setTabTitle(reverseTabIndex(10), decodeBase64(args[AJAX_MEDICATION_TAB_TITLE_BASE64]));
		probandTabView.emphasizeTab(reverseTabIndex(10), args[AJAX_MEDICATION_COUNT] == 0);
	}

	if (_testPropertyExists(args, AJAX_INQUIRY_VALUE_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_INQUIRY_VALUE_COUNT)) {
		probandTabView.setTabTitle(reverseTabIndex(11), decodeBase64(args[AJAX_INQUIRY_VALUE_TAB_TITLE_BASE64]));
		//inverse here
		probandTabView.emphasizeTab(reverseTabIndex(11), args[AJAX_INQUIRY_VALUE_COUNT] > 0);
	}

	if (_testPropertyExists(args, AJAX_TRIAL_PARTICIPATION_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_TRIAL_PARTICIPATION_COUNT)) {
		probandTabView.setTabTitle(reverseTabIndex(12), decodeBase64(args[AJAX_TRIAL_PARTICIPATION_TAB_TITLE_BASE64]));
		probandTabView.emphasizeTab(reverseTabIndex(12), args[AJAX_TRIAL_PARTICIPATION_COUNT] == 0);
	}
	if (_testPropertyExists(args, AJAX_PROBAND_VISIT_SCHEDULE_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_PROBAND_VISIT_SCHEDULE_ITEM_COUNT)) {
		probandTabView.setTabTitle(reverseTabIndex(13), decodeBase64(args[AJAX_PROBAND_VISIT_SCHEDULE_TAB_TITLE_BASE64]));

	}


	if (_testPropertyExists(args, AJAX_BANK_ACCOUNT_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_BANK_ACCOUNT_COUNT)) {
		probandTabView.setTabTitle(reverseTabIndex(15), decodeBase64(args[AJAX_BANK_ACCOUNT_TAB_TITLE_BASE64]));
		probandTabView.emphasizeTab(reverseTabIndex(15), args[AJAX_BANK_ACCOUNT_COUNT] == 0);
	}

	if (_testPropertyExists(args, AJAX_MONEY_TRANSFER_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_MONEY_TRANSFER_COUNT)) {
		probandTabView.setTabTitle(reverseTabIndex(16), decodeBase64(args[AJAX_MONEY_TRANSFER_TAB_TITLE_BASE64]));
		probandTabView.emphasizeTab(reverseTabIndex(16), args[AJAX_MONEY_TRANSFER_COUNT] == 0);
	}


	if (_testPropertyExists(args, AJAX_PROBAND_JOB_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_PROBAND_JOB_COUNT)) {
		probandTabView.setTabTitle(reverseTabIndex(17), decodeBase64(args[AJAX_PROBAND_JOB_TAB_TITLE_BASE64]));
		probandTabView.emphasizeTab(reverseTabIndex(17), args[AJAX_PROBAND_JOB_COUNT] == 0);
	}
	if (_testPropertyExists(args, AJAX_PROBAND_FILE_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_PROBAND_FILE_COUNT)) {
		probandTabView.setTabTitle(reverseTabIndex(18), decodeBase64(args[AJAX_PROBAND_FILE_TAB_TITLE_BASE64]));
		probandTabView.emphasizeTab(reverseTabIndex(18), args[AJAX_PROBAND_FILE_COUNT] == 0);
	}
	if (_testPropertyExists(args, AJAX_PROBAND_JOURNAL_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_PROBAND_JOURNAL_ENTRY_COUNT)) {
		probandTabView.setTabTitle(reverseTabIndex(19), decodeBase64(args[AJAX_PROBAND_JOURNAL_TAB_TITLE_BASE64]));
		probandTabView.emphasizeTab(reverseTabIndex(19), args[AJAX_PROBAND_JOURNAL_ENTRY_COUNT] == 0);
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
