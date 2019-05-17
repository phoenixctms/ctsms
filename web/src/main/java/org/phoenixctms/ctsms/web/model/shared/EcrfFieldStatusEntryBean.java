package org.phoenixctms.ctsms.web.model.shared;

import org.phoenixctms.ctsms.enumeration.ECRFFieldStatusQueue;
import org.phoenixctms.ctsms.web.component.datatable.DataTable;
import org.phoenixctms.ctsms.web.util.JSValues;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.context.RequestContext;

public class EcrfFieldStatusEntryBean extends EcrfFieldStatusEntryBeanBase {

	private EcrfFieldStatusEntryLazyModel ecrfFieldStatusEntryModel;

	public EcrfFieldStatusEntryBean(ECRFFieldStatusQueue queue) {
		super();
		ecrfFieldStatusEntryModel = new EcrfFieldStatusEntryLazyModel(queue);
		this.change();
	}

	@Override
	protected void addMessages() {
		if (ecrfField != null) { // suppresse messages if dialog not open...
			super.addMessages();
		}
	}

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
		RequestContext requestContext = RequestContext.getCurrentInstance();
		if (requestContext != null) {
			requestContext.addCallbackParam(JSValues.AJAX_OPERATION_SUCCESS.toString(), operationSuccess);
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext,
					JSValues.valueOf("AJAX_" + getQueue().name() + "_ECRF_FIELD_STATUS_ENTRY_TAB_TITLE_BASE64"),
					JSValues.valueOf("AJAX_" + getQueue().name() + "_ECRF_FIELD_STATUS_ENTRY_COUNT"),
					MessageCodes.ECRF_FIELD_STATUS_ENTRY_TAB_TITLE, MessageCodes.ECRF_FIELD_STATUS_ENTRY_TAB_TITLE_WITH_COUNT, new Long(ecrfFieldStatusEntryModel.getRowCount()),
					getQueueName());
		}
		//
		//
		// WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_ECRF_FIELD_STATUS_ENTRY_TAB_TITLE_BASE64, JSValues.AJAX_ECRF_FIELD_STATUS_ENTRY_COUNT,
		// MessageCodes.TRIAL_JOURNAL_TAB_TITLE, MessageCodes.TRIAL_JOURNAL_TAB_TITLE_WITH_COUNT,
		// WebUtil.getJournalCount(JournalModule.TRIAL_JOURNAL, in.getTrialId()));
		// if (operationSuccess && in.getTrialId() != null) {
		// WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_TRIAL_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_TRIAL_JOURNAL_ENTRY_COUNT,
		// MessageCodes.TRIAL_JOURNAL_TAB_TITLE, MessageCodes.TRIAL_JOURNAL_TAB_TITLE_WITH_COUNT,
		// WebUtil.getJournalCount(JournalModule.TRIAL_JOURNAL, in.getTrialId()));
		// }
	}

	@Override
	protected String changeAction(Long id) {
		DataTable.clearFilters("ecrffieldstatus_" + getQueue().getValue() + "_list");
		out = null;
		this.ecrfFieldId = id;
		initIn();
		initSets();
		return CHANGE_OUTCOME;
	}

	// protected String getDataTableId() {
	// return "ecrffieldstatus_" + ecrfFieldStatusEntryModel.getQueue().getValue() + "_list";
	// }
	public EcrfFieldStatusEntryLazyModel getEcrfFieldStatusEntryModel() {
		return ecrfFieldStatusEntryModel;
	}

	@Override
	protected String getMessagesId() {
		return "ecrffieldstatus_" + getQueue().getValue() + "_messages";
	}

	@Override
	public ECRFFieldStatusQueue getQueue() {
		return ecrfFieldStatusEntryModel.getQueue();
	}

	public String getTabTitle() {
		return WebUtil.getTabTitleString(MessageCodes.ECRF_FIELD_STATUS_ENTRY_TAB_TITLE, MessageCodes.ECRF_FIELD_STATUS_ENTRY_TAB_TITLE_WITH_COUNT,
				new Long(ecrfFieldStatusEntryModel.getRowCount()), getQueueName());
		//return Messages.getMessage(MessageCodes.ECRF_FIELD_STATUS_ENTRY_TAB_TITLE, getQueueName(), ecrfFieldStatusEntryModel.getRowCount());
	}

	@Override
	public String getTitle() {
		if (out != null) {
			return Messages.getMessage(MessageCodes.ECRF_FIELD_STATUS_ENTRY_TITLE, getQueueName(), Long.toString(out.getId()), out.getStatus().getName());
		} else {
			return Messages.getMessage(MessageCodes.CREATE_NEW_ECRF_FIELD_STATUS_ENTRY, getQueueName());
		}
	}

	@Override
	protected void initSpecificSets() {
		ecrfFieldStatusEntryModel.setListEntryId(in.getListEntryId());
		ecrfFieldStatusEntryModel.setEcrfFieldId(in.getEcrfFieldId());
		ecrfFieldStatusEntryModel.setIndex(in.getIndex());
		ecrfFieldStatusEntryModel.updateRowCount();
	}

	public void setIndex(Long index) {
		this.index = index;
	}

	public void setListEntryId(Long listEntryId) {
		this.listEntryId = listEntryId;
	}
}
