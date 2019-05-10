package org.phoenixctms.ctsms.web.model.trial;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.web.model.shared.EcrfStatusEntryBeanBase;
import org.phoenixctms.ctsms.web.model.shared.ProbandListEntryModel;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.JSValues;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.context.RequestContext;

@ManagedBean
@ViewScoped
public class TrialEcrfStatusEntryBean extends EcrfStatusEntryBeanBase {

	private Long trialId;
	private TrialOutVO trial;
	private ArrayList<SelectItem> filterProbandGroups;

	public TrialEcrfStatusEntryBean() {
		super();
		filterProbandGroups = new ArrayList<SelectItem>();
	}

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
		RequestContext requestContext = null;
		if (operationSuccess && probandListEntry != null) { // && ecrf != null) {
			// long t = System.currentTimeMillis();
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext,
					JSValues.AJAX_ECRF_FIELD_STATUS_TAB_TITLE_BASE64,
					JSValues.AJAX_ECRF_FIELD_STATUS_COUNT,
					MessageCodes.ECRF_FIELD_STATUS_TAB_TITLE, MessageCodes.ECRF_FIELD_STATUS_TAB_TITLE_WITH_COUNT,
					WebUtil.getEcrfFieldStatusEntryCount(Settings.getEcrfFieldStatusQueue(SettingCodes.ECRF_FIELD_STATUS_QUEUE, Bundle.SETTINGS,
							DefaultSettings.ECRF_FIELD_STATUS_QUEUE), probandListEntry.getTrial().getId(), null, null, true));
			// WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_TRIAL_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_TRIAL_JOURNAL_ENTRY_COUNT,
			// MessageCodes.TRIAL_JOURNAL_TAB_TITLE, MessageCodes.TRIAL_JOURNAL_TAB_TITLE_WITH_COUNT,
			// WebUtil.getJournalCount(JournalModule.TRIAL_JOURNAL, probandListEntry.getTrial().getId()));
			// t = WebUtil.perfDebug("appendcontext trialstatusbean: ", t);
		}
	}

	@Override
	protected void changeSpecific(Long id) {
		// probandMultiPicker.clear();
		// bulkAddGroupId = null;
		// shuffle = Settings.getBoolean(SettingCodes.PROBAND_LIST_BULK_ADD_SHUFFLE, Bundle.SETTINGS, DefaultSettings.PROBAND_LIST_BULK_ADD_SHUFFLE);
		// limit = Settings.getLongNullable(SettingCodes.PROBAND_LIST_BULK_ADD_LIMIT, Bundle.SETTINGS, DefaultSettings.PROBAND_LIST_BULK_ADD_LIMIT);
		this.trialId = id;
		probandListEntryModel.setTrialId(id);
		probandListEntryModel.initSets(true);
		filterProbandGroups = WebUtil.getProbandGroups(id);
		filterProbandGroups.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
	}

	@Override
	protected ProbandListEntryModel createProbandListEntryModel() {
		return new ProbandListEntryModel(
				Settings.getBoolean(SettingCodes.TRIAL_ECRF_DATA_ENTRIES_SHOW_PROBAND_LIST_ENTRY_TAG_COLUMN, Bundle.SETTINGS,
						DefaultSettings.TRIAL_ECRF_DATA_ENTRIES_SHOW_PROBAND_LIST_ENTRY_TAG_COLUMN),
				Settings.getBoolean(SettingCodes.TRIAL_ECRF_DATA_ENTRIES_SHOW_INQUIRY_COLUMN, Bundle.SETTINGS, DefaultSettings.TRIAL_ECRF_DATA_ENTRIES_SHOW_INQUIRY_COLUMN), true);
	}

	@Override
	protected String getDeltaErrorMessageId() {
		return "tabView:trialecrfstatusentry_form:delta_error_msg";
	}

	@Override
	protected String getEcrfDataTableId() {
		return "trialecrfstatus_ecrf_list";
	}

	public String getEcrfListHeader() {
		if (probandListEntry != null) {
			return Messages.getMessage(MessageCodes.ECRF_LIST_HEADER, probandListEntry.getProband().getName(), ecrfModel.getRowCount());
		} else {
			return Messages.getString(MessageCodes.SELECT_PROBAND_LIST_ENTRY);
		}
	}

	@Override
	public ArrayList<SelectItem> getFilterProbandGroups() {
		return filterProbandGroups;
	}

	@Override
	protected String getProbandListEntryDataTableId() {
		return "trialecrfstatus_proband_list";
	}

	public TrialOutVO getTrial() {
		return trial;
	}

	@PostConstruct
	private void init() {
		// initIn();
		initSets(false);
	}

	// @Override
	// protected void initIn() {
	// // TODO Auto-generated method stub
	// }
	@Override
	protected void initSpecificSets() {
		// clearCaches(select);
		// value count caches......
		trial = WebUtil.getTrial(this.trialId);
	}
}
