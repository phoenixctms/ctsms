package org.phoenixctms.ctsms.web.model.shared;

import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;

import org.phoenixctms.ctsms.enumeration.RandomizationMode;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.WebUtil;

public abstract class GenerateRandomListBean extends ManagedBeanBase {

	private int n;
	private String randomizationList;

	protected GenerateRandomListBean() {
		clearRandomizationList();
	}

	protected void clearRandomizationList() {
		this.randomizationList = null;
		this.n = 0;
	}


	public int getN() {
		return n;
	}


	public String getRandomizationList() {
		return randomizationList;
	}


	protected abstract RandomizationMode getRandomizationMode();

	protected abstract Long getTrialId();

	public void setN(int n) {
		this.n = n;
	}

	public void setRandomizationList(String randomizationList) {
		this.randomizationList = randomizationList;
	}

	public void updateRandomizationList(ActionEvent event) {
		randomizationList = null;
		try {
			randomizationList = WebUtil.getServiceLocator().getTrialService().generateRandomizationList(WebUtil.getAuthentication(), getTrialId(), getRandomizationMode(), n);
			// System.out.println("balh");
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
