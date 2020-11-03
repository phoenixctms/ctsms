package org.phoenixctms.ctsms.web.model.trial;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

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

	public TrialEcrfStatusEntryBean() {
		super();
	}

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
		RequestContext requestContext = null;
		if (operationSuccess && probandListEntry != null) {
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext,
					JSValues.AJAX_ECRF_FIELD_STATUS_TAB_TITLE_BASE64,
					JSValues.AJAX_ECRF_FIELD_STATUS_COUNT,
					MessageCodes.ECRF_FIELD_STATUS_TAB_TITLE, MessageCodes.ECRF_FIELD_STATUS_TAB_TITLE_WITH_COUNT,
					WebUtil.getEcrfFieldStatusEntryCount(Settings.getEcrfFieldStatusQueue(SettingCodes.ECRF_FIELD_STATUS_QUEUE, Bundle.SETTINGS,
							DefaultSettings.ECRF_FIELD_STATUS_QUEUE), probandListEntry.getTrial().getId(), null, null, null, true));
		}
	}

	@Override
	protected void changeSpecific(Long id) {
		this.trialId = id;
		probandListEntryModel.setTrialId(id);
		probandListEntryModel.initSets(true);
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
			return Messages.getMessage(MessageCodes.ECRF_LIST_HEADER, probandListEntry.getProband().getName(), ecrfVisitModel.getRowCount());
		} else {
			return Messages.getString(MessageCodes.SELECT_PROBAND_LIST_ENTRY);
		}
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
		initSets(false);
	}

	@Override
	protected void initSpecificSets() {
		trial = WebUtil.getTrial(this.trialId);
	}
}
