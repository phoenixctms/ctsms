package org.phoenixctms.ctsms.web.model.proband;

import java.io.ByteArrayInputStream;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.ProbandListEntryInVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagsPDFVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.web.model.shared.ProbandListEntryBeanBase;
import org.phoenixctms.ctsms.web.model.shared.ProbandListEntryModel;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.GetParamNames;
import org.phoenixctms.ctsms.web.util.JSValues;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean
@ViewScoped
public class TrialParticipationBean extends ProbandListEntryBeanBase {

	public static void initProbandListEntryDefaultValues(ProbandListEntryInVO in, Long probandId) {
		if (in != null) {
			in.setGroupId(null);
			in.setId(null);
			in.setPosition(CommonUtil.LIST_INITIAL_POSITION);
			in.setProbandId(probandId);
			in.setTrialId(null);
			in.setVersion(null);
			in.setRating(Settings.getLongNullable(SettingCodes.PROBAND_LIST_ENTRY_RATING_PRESET, Bundle.SETTINGS, DefaultSettings.PROBAND_LIST_ENTRY_RATING_PRESET));
			in.setRatingMax(Settings.getLongNullable(SettingCodes.PROBAND_LIST_ENTRY_RATING_MAX_PRESET, Bundle.SETTINGS, DefaultSettings.PROBAND_LIST_ENTRY_RATING_MAX_PRESET));
		}
	}

	private Long probandId;

	public TrialParticipationBean() {
		super();
	}

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
		RequestContext requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_TRIAL_PARTICIPATION_TAB_TITLE_BASE64,
				JSValues.AJAX_TRIAL_PARTICIPATION_COUNT, MessageCodes.TRIAL_PARTICIPATIONS_TAB_TITLE, MessageCodes.TRIAL_PARTICIPATIONS_TAB_TITLE_WITH_COUNT, new Long(
						probandListEntryModel.getRowCount()));
		if (operationSuccess && in.getProbandId() != null) {
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_PROBAND_VISIT_SCHEDULE_TAB_TITLE_BASE64,
					JSValues.AJAX_PROBAND_VISIT_SCHEDULE_ITEM_COUNT, MessageCodes.PROBAND_VISIT_SCHEDULE_TAB_TITLE, MessageCodes.PROBAND_VISIT_SCHEDULE_TAB_TITLE_WITH_COUNT,
					WebUtil.getVisitScheduleItemCount(null, in.getProbandId()));
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_PROBAND_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_PROBAND_JOURNAL_ENTRY_COUNT,
					MessageCodes.PROBAND_JOURNAL_TAB_TITLE, MessageCodes.PROBAND_JOURNAL_TAB_TITLE_WITH_COUNT,
					WebUtil.getJournalCount(JournalModule.PROBAND_JOURNAL, in.getProbandId()));
		}
		this.getProbandListEntryTagValueBean().appendRequestContextCallbackArgs(operationSuccess);
	}

	@Override
	protected void changeSpecific(Long id) {
		this.probandId = id;
	}

	@Override
	protected ProbandListEntryModel createProbandListEntryModel() {
		return new ProbandListEntryModel(
				Settings.getBoolean(SettingCodes.TRIAL_PARTICIPATIONS_SHOW_PROBAND_LIST_ENTRY_TAG_COLUMN, Bundle.SETTINGS,
						DefaultSettings.TRIAL_PARTICIPATIONS_SHOW_PROBAND_LIST_ENTRY_TAG_COLUMN),
				Settings.getBoolean(SettingCodes.TRIAL_PARTICIPATIONS_SHOW_INQUIRY_COLUMN, Bundle.SETTINGS, DefaultSettings.TRIAL_PARTICIPATIONS_SHOW_INQUIRY_COLUMN), true);
	}

	@Override
	protected String getDataTableId() {
		return "trialparticipation_list";
	}

	@Override
	public String getMainTabTitle() {
		if (trial != null) {
			return Messages.getMessage(MessageCodes.TRIAL_PARTICIPATION_LIST_MAIN_TAB_LABEL, WebUtil.trialOutVOToString(trial));
		} else {
			return Messages.getString(MessageCodes.TRIAL_PARTICIPATION_LIST_MAIN_TAB_BLANK_LABEL);
		}
	}

	public ProbandOutVO getProband() {
		return proband;
	}

	public String getProbandListEntryTagPdfButtonLabel(boolean blank) {
		if (blank) {
			return Messages.getMessage(MessageCodes.BLANK_PROBAND_LIST_ENTRY_TAG_PDF_BUTTON_LABEL, CommonUtil.trialOutVOToString(trial));
		} else {
			return Messages.getMessage(MessageCodes.PROBAND_LIST_ENTRY_TAG_PDF_BUTTON_LABEL, CommonUtil.trialOutVOToString(trial));
		}
	}

	public StreamedContent getProbandListEntryTagsPdfStreamedContent(boolean blank) throws Exception {
		if (probandId != null) {
			try {
				ProbandListEntryTagsPDFVO probandListEntryTagsPdf = WebUtil.getServiceLocator().getTrialService()
						.renderProbandListEntryTags(WebUtil.getAuthentication(), null, probandId, blank);
				return new DefaultStreamedContent(new ByteArrayInputStream(probandListEntryTagsPdf.getDocumentDatas()), probandListEntryTagsPdf.getContentType().getMimeType(),
						probandListEntryTagsPdf.getFileName());
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
				throw e;
			} catch (AuthorisationException e) {
				throw e;
			} catch (ServiceException e) {
				throw e;
			} catch (IllegalArgumentException e) {
				throw e;
			}
		}
		return null;
	}

	@Override
	public String getTitle() {
		if (out != null) {
			return Messages.getMessage(MessageCodes.PROBAND_LIST_ENTRY_TITLE, Long.toString(out.getId()), CommonUtil.trialOutVOToString(out.getTrial()));
		} else {
			return Messages.getString(MessageCodes.CREATE_NEW_PROBAND_LIST_ENTRY);
		}
	}

	public Long getTrialId() {
		return in.getTrialId();
	}

	public String getTrialName() {
		return WebUtil.trialOutVOToString(trial);
	}

	@PostConstruct
	private void init() {
		Long id = WebUtil.getLongParamValue(GetParamNames.PROBAND_LIST_ENTRY_ID);
		if (id != null) {
			this.load(id);
		} else {
			initIn();
			initSets();
		}
	}

	@Override
	protected void initIn() {
		if (in == null) {
			in = new ProbandListEntryInVO();
		}
		if (out != null) {
			copyProbandListEntryOutToIn(in, out);
			probandId = in.getProbandId();
		} else {
			initProbandListEntryDefaultValues(in, probandId);
		}
	}

	@Override
	protected void initSpecificSets(boolean reset, boolean deleted, boolean select) {
		probandListEntryModel.setProbandId(in.getProbandId());
		probandListEntryModel.initSets(reset);
		proband = WebUtil.getProband(in.getProbandId(), null, null, null);
		onTrialChanged();
	}

	@Override
	public boolean isCreateable() {
		if (in.getProbandId() != null) {
			if (out != null) {
				if (trial != null) {
					if (trial.getId() != out.getTrial().getId()) {
						return !isTrialLocked() && !isProbandLocked();
					}
				} else {
					return !isTrialLocked() && !isProbandLocked();
				}
			} else {
				return !isTrialLocked() && !isProbandLocked();
			}
		}
		return false;
	}

	private void onTrialChanged() {
		probandGroups = WebUtil.getProbandGroups(in.getTrialId());
		if (!isCreated()) {
			in.setPosition(getInitialPosition(in.getTrialId()));
		}
		trial = WebUtil.getTrial(this.in.getTrialId());
		if (isProbandLocked()) {
			Messages.addLocalizedMessageClientId("probandListEntryMessages", FacesMessage.SEVERITY_WARN, MessageCodes.PROBAND_LOCKED);
		} else if (isTrialLocked()) {
			Messages.addLocalizedMessageClientId("probandListEntryMessages", FacesMessage.SEVERITY_WARN, MessageCodes.TRIAL_LOCKED);
		}
	}

	public void setTrialId(Long trialId) {
		in.setTrialId(trialId);
		onTrialChanged();
	}
}
