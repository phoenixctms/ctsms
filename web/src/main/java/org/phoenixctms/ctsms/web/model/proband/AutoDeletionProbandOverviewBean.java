package org.phoenixctms.ctsms.web.model.proband;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.VariablePeriod;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.PrivacyConsentStatusTypeVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.model.VariablePeriodSelector;
import org.phoenixctms.ctsms.web.model.VariablePeriodSelectorListener;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.model.StreamedContent;

@ManagedBean
@ViewScoped
public class AutoDeletionProbandOverviewBean extends ManagedBeanBase implements VariablePeriodSelectorListener {

	private static final int REMINDER_PERIOD_PROPERTY_ID = 1;
	private Date today;
	private AutoDeletionProbandLazyModel autoDeletionProbandModel;
	private ArrayList<SelectItem> filterPrivacyConsentStatusTypes;
	private HashMap<Long, ArrayList<SelectItem>> privacyConsentTransitionsMap;
	private VariablePeriodSelector reminder;

	public AutoDeletionProbandOverviewBean() {
		super();
		today = new Date();
		autoDeletionProbandModel = new AutoDeletionProbandLazyModel();
		setReminder(new VariablePeriodSelector(this, REMINDER_PERIOD_PROPERTY_ID));
	}

	public AutoDeletionProbandLazyModel getAutoDeletionProbandModel() {
		return autoDeletionProbandModel;
	}

	public ArrayList<SelectItem> getFilterPrivacyConsentStatusTypes() {
		return filterPrivacyConsentStatusTypes;
	}

	@Override
	public VariablePeriod getPeriod(int property) {
		switch (property) {
			case REMINDER_PERIOD_PROPERTY_ID:
				return autoDeletionProbandModel.getReminderPeriod();
			default:
				return VariablePeriodSelectorListener.NO_SELECTION_VARIABLE_PERIOD;
		}
	}

	public Map<Long, ArrayList<SelectItem>> getPrivacyConsentTransitionsMap() {
		return privacyConsentTransitionsMap;
	}

	public String getProbandDueInString(ProbandOutVO proband) {
		return WebUtil.getExpirationDueInString(today, proband.getAutoDeleteDeadline());
	}

	public StreamedContent getProbandLettersPdfStreamedContent(Long probandId) throws Exception {
		return WebUtil.getProbandLettersPdfStreamedContent(probandId);
	}


	public VariablePeriodSelector getReminder() {
		return reminder;
	}

	public void handlePrivacyConsentStatusTypeChange(ProbandOutVO proband) {
		if (proband != null) {
			try {
				WebUtil.getServiceLocator()
				.getProbandService()
				.updatePrivacyConsentStatus(WebUtil.getAuthentication(), proband.getId(), proband.getVersion(),
						autoDeletionProbandModel.getProbandPrivacyConsentTypeMap().get(proband.getId()));
				initIn();
				initSets();
				addOperationSuccessMessage(MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
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

	public void handleReminderChange() {
		initSets();
	}

	@PostConstruct
	private void init() {
		initIn();
		initSets();
	}

	private void initIn() {
	}

	private void initSets() {
		today = new Date();
		autoDeletionProbandModel.updateRowCount();
		LazyDataModelBase.clearFilters("autodeletionproband_list");
		if (filterPrivacyConsentStatusTypes == null || privacyConsentTransitionsMap == null) {
			Collection<PrivacyConsentStatusTypeVO> statusTypeVOs = null;
			try {
				statusTypeVOs = WebUtil.getServiceLocator().getSelectionSetService().getAllPrivacyConsentStatusTypes(WebUtil.getAuthentication());
			} catch (ServiceException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			} catch (AuthorisationException e) {
			} catch (IllegalArgumentException e) {
			}
			if (statusTypeVOs != null) {
				filterPrivacyConsentStatusTypes = new ArrayList<SelectItem>(statusTypeVOs.size());
				privacyConsentTransitionsMap = new HashMap<Long, ArrayList<SelectItem>>(statusTypeVOs.size());
				Iterator<PrivacyConsentStatusTypeVO> it = statusTypeVOs.iterator();
				while (it.hasNext()) {
					PrivacyConsentStatusTypeVO statusTypeVO = it.next();
					filterPrivacyConsentStatusTypes.add(new SelectItem(statusTypeVO.getId().toString(), statusTypeVO.getName()));
					ArrayList<SelectItem> transitionStatusTypes;
					Collection<PrivacyConsentStatusTypeVO> transitionStatusTypeVOs = null;
					try {
						transitionStatusTypeVOs = WebUtil.getServiceLocator().getSelectionSetService()
								.getPrivacyConsentStatusTypeTransitions(WebUtil.getAuthentication(), statusTypeVO.getId());
					} catch (ServiceException e) {
					} catch (AuthenticationException e) {
						WebUtil.publishException(e);
					} catch (AuthorisationException e) {
					} catch (IllegalArgumentException e) {
					}
					if (transitionStatusTypeVOs != null) {
						transitionStatusTypes = new ArrayList<SelectItem>(transitionStatusTypeVOs.size());
						Iterator<PrivacyConsentStatusTypeVO> transitionsIt = transitionStatusTypeVOs.iterator();
						while (transitionsIt.hasNext()) {
							PrivacyConsentStatusTypeVO transitionStatusTypeVO = transitionsIt.next();
							transitionStatusTypes.add(new SelectItem(transitionStatusTypeVO.getId().toString(), transitionStatusTypeVO.getName()));
						}
					} else {
						transitionStatusTypes = new ArrayList<SelectItem>();
					}
					privacyConsentTransitionsMap.put(statusTypeVO.getId(), transitionStatusTypes);
				}
			} else {
				filterPrivacyConsentStatusTypes = new ArrayList<SelectItem>();
				privacyConsentTransitionsMap = new HashMap<Long, ArrayList<SelectItem>>();
			}
			filterPrivacyConsentStatusTypes.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
			ArrayList<SelectItem> initialStatusTypes;
			Collection<PrivacyConsentStatusTypeVO> initialStatusTypeVOs = null;
			try {
				initialStatusTypeVOs = WebUtil.getServiceLocator().getSelectionSetService().getInitialPrivacyConsentStatusTypes(WebUtil.getAuthentication());
			} catch (ServiceException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			} catch (AuthorisationException e) {
			} catch (IllegalArgumentException e) {
			}
			if (initialStatusTypeVOs != null) {
				initialStatusTypes = new ArrayList<SelectItem>(initialStatusTypeVOs.size());
				Iterator<PrivacyConsentStatusTypeVO> initialStatesIt = initialStatusTypeVOs.iterator();
				while (initialStatesIt.hasNext()) {
					PrivacyConsentStatusTypeVO initialStatusTypeVO = initialStatesIt.next();
					initialStatusTypes.add(new SelectItem(initialStatusTypeVO.getId().toString(), initialStatusTypeVO.getName()));
				}
			} else {
				initialStatusTypes = new ArrayList<SelectItem>();
			}
			privacyConsentTransitionsMap.put(null, initialStatusTypes);
		}
	}

	@Override
	public boolean isCreateable() {
		return false;
	}

	@Override
	public boolean isCreated() {
		return false;
	}

	public boolean isReminderPeriodSpinnerEnabled() {
		return autoDeletionProbandModel.getReminderPeriod() == null || VariablePeriod.EXPLICIT.equals(autoDeletionProbandModel.getReminderPeriod());
	}

	@Override
	public String loadAction() {
		initIn();
		initSets();
		return LOAD_OUTCOME;
	}

	public String probandToColor(ProbandOutVO proband) {
		if (proband != null) {
			return WebUtil.expirationToColor(today, proband.getAutoDeleteDeadline(), Settings.getProbandAutoDeleteDueInColorMap(),
					Settings.getColor(SettingCodes.PROBAND_AUTO_DELETE_OVERDUE_COLOR, Bundle.SETTINGS, DefaultSettings.PROBAND_AUTO_DELETE_OVERDUE_COLOR));
		}
		return "";
	}

	public void resetAutoDeleteDeadline(ProbandOutVO proband) {
		if (proband != null) {
			try {
				WebUtil.getServiceLocator().getProbandService().resetAutoDeleteDeadline(WebUtil.getAuthentication(), proband.getId(), proband.getVersion());
				initIn();
				initSets();
				addOperationSuccessMessage(MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
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

	@Override
	public void setPeriod(int property, VariablePeriod period) {
		switch (property) {
			case REMINDER_PERIOD_PROPERTY_ID:
				autoDeletionProbandModel.setReminderPeriod(period);
				break;
			default:
		}
	}

	public void setReminder(VariablePeriodSelector reminder) {
		this.reminder = reminder;
	}
}
