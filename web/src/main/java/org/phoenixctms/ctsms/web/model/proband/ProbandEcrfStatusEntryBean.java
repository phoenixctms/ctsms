package org.phoenixctms.ctsms.web.model.proband;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.web.model.shared.EcrfStatusEntryBeanBase;
import org.phoenixctms.ctsms.web.model.shared.ProbandListEntryModel;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.context.RequestContext;

@ManagedBean
@ViewScoped
public class ProbandEcrfStatusEntryBean extends EcrfStatusEntryBeanBase {

	private Long probandId;
	private ProbandOutVO proband;

	public ProbandEcrfStatusEntryBean() {
		super();
	}

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
		RequestContext requestContext = null;
		if (operationSuccess && probandListEntry != null) {
			// WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_PROBAND_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_PROBAND_JOURNAL_ENTRY_COUNT,
			// MessageCodes.PROBAND_JOURNAL_TAB_TITLE, MessageCodes.PROBAND_JOURNAL_TAB_TITLE_WITH_COUNT,
			// WebUtil.getJournalCount(JournalModule.PROBAND_JOURNAL, probandListEntry.getProband().getId()));
		}
	}

	@Override
	protected void changeSpecific(Long id) {
		this.probandId = id;
		probandListEntryModel.setProbandId(id);
		probandListEntryModel.initSets(true);
	}

	@Override
	protected ProbandListEntryModel createProbandListEntryModel() {
		return new ProbandListEntryModel(
				Settings.getBoolean(SettingCodes.PROBAND_ECRF_DATA_ENTRIES_SHOW_PROBAND_LIST_ENTRY_TAG_COLUMN, Bundle.SETTINGS,
						DefaultSettings.TRIAL_ECRF_DATA_ENTRIES_SHOW_PROBAND_LIST_ENTRY_TAG_COLUMN),
				Settings.getBoolean(SettingCodes.TRIAL_ECRF_DATA_ENTRIES_SHOW_INQUIRY_COLUMN, Bundle.SETTINGS, DefaultSettings.TRIAL_ECRF_DATA_ENTRIES_SHOW_INQUIRY_COLUMN), true);
	}

	@Override
	protected String getDeltaErrorMessageId() {
		return "tabView:probandecrfstatusentry_form:delta_error_msg";
	}

	@Override
	protected String getEcrfDataTableId() {
		return "probandecrfstatus_ecrf_list";
	}

	public String getEcrfListHeader() {
		if (probandListEntry != null) {
			return Messages.getMessage(MessageCodes.ECRF_LIST_HEADER, probandListEntry.getTrial().getName(), ecrfVisitModel.getRowCount());
		} else {
			return Messages.getString(MessageCodes.SELECT_PROBAND_LIST_ENTRY);
		}
	}

	public ProbandOutVO getProband() {
		return proband;
	}

	@Override
	protected String getProbandListEntryDataTableId() {
		return "probandecrfstatus_proband_list";
	}

	@PostConstruct
	private void init() {
		initSets(false);
	}

	@Override
	protected void initSpecificSets() {
		proband = WebUtil.getProband(this.probandId, null, null, null);
	}
}
