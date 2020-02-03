package org.phoenixctms.ctsms.web.model.trial;

import javax.faces.application.FacesMessage;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.RandomizationListCodeOutVO;
import org.phoenixctms.ctsms.vo.StratificationRandomizationListOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.web.adapt.ProbandListEntryTagValueOutVOStringAdapter;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.GetParamNames;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class CodeBreakBean extends ManagedBeanBase {

	private RandomizationListCodeOutVO out;
	private RandomizationListCodeOutVO blinded;
	private String reasonForBreak;
	private Long probandListEntryId;

	public CodeBreakBean() {
		super();
		change();
	}

	public String getReasonForBreak() {
		return reasonForBreak;
	}

	public void setReasonForBreak(String reasonForBreak) {
		this.reasonForBreak = reasonForBreak;
	}

	public String getStratificationRandomizationListSelectionSetValuesString() {
		StratificationRandomizationListOutVO randomizationList = (getOut() != null ? getOut().getStratificationRandomizationList() : null);
		if (randomizationList != null) {
			return (new ProbandListEntryTagValueOutVOStringAdapter(
					Settings.getInt(SettingCodes.STRATIFICATION_RANDOMIZATION_LIST_PROBAND_LIST_ENTRY_TAG_VALUE_TEXT_CLIP_MAX_LENGTH, Bundle.SETTINGS,
							DefaultSettings.STRATIFICATION_RANDOMIZATION_LIST_PROBAND_LIST_ENTRY_TAG_VALUE_TEXT_CLIP_MAX_LENGTH)))
									.selectionSetValuesToString(randomizationList.getSelectionSetValues(), true);
		}
		return null;
	}

	@Override
	public String getTitle() {
		if (blinded != null) {
			if (blinded.getListEntry() != null) {
				return Messages.getMessage(MessageCodes.CODE_BREAK_PROBAND_TITLE, blinded.getCode(),
						CommonUtil.trialOutVOToString(WebUtil.getRandomizationCodeTrial(blinded)),
						CommonUtil.probandOutVOToString(blinded.getListEntry().getProband()));
			} else {
				return Messages.getMessage(MessageCodes.CODE_BREAK_TITLE, blinded.getCode(),
						CommonUtil.trialOutVOToString(WebUtil.getRandomizationCodeTrial(blinded)));
			}
		}
		return null;
	}

	public void changeRandomizationCode() {
		changeRootEntity(WebUtil.getLongParamValue(GetParamNames.PROBAND_LIST_ENTRY_ID));
	}

	private void initIn() {
		reasonForBreak = null;
	}

	private void initSets() {
		if (out != null) {
			reasonForBreak = out.getReasonForBreak();
		}
		blinded = null;
		try {
			blinded = WebUtil.getServiceLocator().getTrialService().getRandomizationListCode(WebUtil.getAuthentication(), this.probandListEntryId, false, null);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
	}

	public TrialOutVO getTrial() {
		return WebUtil.getRandomizationCodeTrial(getOut());
	}

	public RandomizationListCodeOutVO getOut() {
		if (out != null) {
			return out;
		} else if (blinded != null) {
			return blinded;
		}
		return null;
	}

	@Override
	protected String changeAction(Long id) {
		out = null;
		this.probandListEntryId = id;
		initIn();
		initSets();
		return CHANGE_OUTCOME;
	}

	@Override
	public String loadAction() {
		return loadAction(probandListEntryId);
	}

	@Override
	public String loadAction(Long id) {
		out = null;
		String reasonForBreakBackup = reasonForBreak;
		try {
			out = WebUtil.getServiceLocator().getTrialService().getRandomizationListCode(WebUtil.getAuthentication(), id, true, reasonForBreak);
			initIn();
			initSets();
			return LOAD_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			reasonForBreak = reasonForBreakBackup;
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			reasonForBreak = reasonForBreakBackup;
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	@Override
	public boolean isCreateable() {
		return false;
	}

	@Override
	public boolean isCreated() {
		return true;
	}

	public boolean isUnblinded() {
		return out != null;
	}
}
