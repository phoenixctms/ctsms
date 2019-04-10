package org.phoenixctms.ctsms.web.model.trial;

import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;

import org.phoenixctms.ctsms.enumeration.RandomizationMode;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;

public abstract class GenerateRandomListBean extends ManagedBeanBase {

	private int n;
	private String randomizationList;

	protected GenerateRandomListBean() {
		resetGenerateRandomizationList();
	}

	protected abstract void addGenerateRandomizationListWarnMessage();

	public void appendGenerateRandomizationList() {
		if (this.randomizationList != null && this.randomizationList.length() > 0) {
			StringBuilder sb;
			if (getRandomizationList() != null && getRandomizationList().length() > 0) {
				sb = new StringBuilder(getRandomizationList());
			} else {
				sb = new StringBuilder();
			}
			sb.append(this.randomizationList);
			setRandomizationList(sb.toString());
		}
	}

	public void clearGenerateRandomizationList() {
		resetGenerateRandomizationList();
		addGenerateRandomizationListWarnMessage();
	}

	public void copyGenerateRandomizationList() {
		setRandomizationList(this.randomizationList);
	}

	public String getGeneratedRandomizationList() {
		return randomizationList;
	}

	public String getGenerateRandomizationListDialogTitle() {
		if (getRandomizationMode() != null) {
			switch (getRandomizationMode()) {
				case GROUP_LIST:
				case GROUP_STRATIFIED:
					return Messages.getString(MessageCodes.GENERATE_GROUP_RANDOMIZATION_LIST_TITLE);
				case TAG_SELECT_LIST:
				case TAG_SELECT_STRATIFIED:
					return Messages.getString(MessageCodes.GENERATE_TAG_RANDOMIZATION_LIST_TITLE);
				default:
					break;
			}
		}
		return null;
	}

	public int getN() {
		return n;
	}


	protected abstract String getRandomizationList();


	protected abstract RandomizationMode getRandomizationMode();


	protected abstract Long getTrialId();

	public boolean isGenerateRandomizationListEnabled() {
		if (getRandomizationMode() != null) {
			switch (getRandomizationMode()) {
				case GROUP_LIST:
				case GROUP_STRATIFIED:
				case TAG_SELECT_LIST:
				case TAG_SELECT_STRATIFIED:
					return true;
				default:
					break;
			}
		}
		return false;
	}

	protected void resetGenerateRandomizationList() {
		this.randomizationList = null;
		this.n = Settings.getInt(SettingCodes.GENERATE_RANDOMIZATION_LIST_N_PRESET, Bundle.SETTINGS, DefaultSettings.GENERATE_RANDOMIZATION_LIST_N_PRESET);
	}

	public void setGeneratedRandomizationList(String randomizationList) {
		this.randomizationList = randomizationList;
	}

	public void setN(int n) {
		this.n = n;
	}

	protected abstract void setRandomizationList(String randomizationList);

	public void updateRandomizationList(ActionEvent event) {
		randomizationList = null;
		try {
			randomizationList = WebUtil.getServiceLocator().getTrialService().generateRandomizationList(WebUtil.getAuthentication(), getTrialId(), getRandomizationMode(), n);
			// System.out.println("balh");
			addOperationSuccessMessage(MessageCodes.GENERATE_RANDOMIZATION_LIST_OPERATION_SUCCESSFUL);
		} catch (ServiceException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());

		}
	}

}
