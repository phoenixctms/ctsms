package org.phoenixctms.ctsms.web.model.trial;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.phoenixctms.ctsms.domain.StratificationRandomizationList;
import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.InputFieldOutVO;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueOutVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagOutVO;
import org.phoenixctms.ctsms.vo.StratificationRandomizationListInVO;
import org.phoenixctms.ctsms.vo.StratificationRandomizationListOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.web.adapt.ProbandListEntryTagValueOutVOStringAdapter;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.model.IDVOList;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
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
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

@ManagedBean
@ViewScoped
public class RandomizationListBean extends ManagedBeanBase {

	public static void copyStratificationRandomizationListOutToIn(StratificationRandomizationListInVO in, StratificationRandomizationListOutVO out) {
		if (in != null && out != null) {
			TrialOutVO trialVO = out.getTrial();
			in.setId(out.getId());
			in.setVersion(out.getVersion());
			in.setTrialId(trialVO == null ? null : trialVO.getId());
			in.setRandomizationList(out.getRandomizationList());
			in.getSelectionSetValueIds().clear();
			Iterator<InputFieldSelectionSetValueOutVO> it = out.getSelectionSetValues().iterator();
			while (it.hasNext()) {
				in.getSelectionSetValueIds().add(it.next().getId());
			}
		}
	}

	public static void initStratificationRandomizationListDefaultValues(StratificationRandomizationListInVO in, Long trialId) {
		if (in != null) {
			in.setId(null);
			in.setVersion(null);
			in.setTrialId(trialId);
			in.setRandomizationList(Settings.getString(SettingCodes.STRATIFICATION_RANDOMIZATION_LIST_RANDOMIZATION_LIST_PRESET, Bundle.SETTINGS,
					DefaultSettings.STRATIFICATION_RANDOMIZATION_LIST_RANDOMIZATION_LIST_PRESET));
			in.getSelectionSetValueIds().clear();
		}
	}

	private StratificationRandomizationListInVO in;
	private StratificationRandomizationListOutVO out;
	private Long trialId;
	private TrialOutVO trial;
	private RandomizationListLazyModel randomizationListModel;
	private Collection<ProbandListEntryTagOutVO> stratificationProbandListEntryTagVOs;
	private ArrayList<Long> requiredFields;
	private List<InputFieldSelectionSetValueOutVO> selectionSetValues;

	// private ArrayList<SelectItem> availableTags;
	public RandomizationListBean() {
		super();
		randomizationListModel = new RandomizationListLazyModel();
		requiredFields = new ArrayList<Long>();
		selectionSetValues = new ArrayList<InputFieldSelectionSetValueOutVO>();
	}

	@Override
	public String addAction() {
		StratificationRandomizationListInVO backup = new StratificationRandomizationListInVO(in);
		// Long idBackup = in.getId();
		// Long versionBackup = in.getVersion();
		in.setId(null);
		in.setVersion(null);
		sanitizeInVals();
		try {
			out = WebUtil.getServiceLocator().getTrialService().addStratificationRandomizationList(WebUtil.getAuthentication(), in);
			initIn();
			initSets();
			addOperationSuccessMessage(MessageCodes.ADD_OPERATION_SUCCESSFUL);
			return ADD_OUTCOME;
		} catch (ServiceException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (IllegalArgumentException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
		return ERROR_OUTCOME;
	}

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
		RequestContext requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null,
				JSValues.AJAX_STRATIFICATION_RANDOMIZATION_LIST_TAB_TITLE_BASE64, JSValues.AJAX_STRATIFICATION_RANDOMIZATION_LIST_COUNT,
				MessageCodes.STRATIFICATION_RANDOMIZATION_LISTS_TAB_TITLE, MessageCodes.STRATIFICATION_RANDOMIZATION_LISTS_TAB_TITLE_WITH_COUNT, new Long(randomizationListModel.getRowCount()));
		if (operationSuccess && in.getTrialId() != null) {
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_TRIAL_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_TRIAL_JOURNAL_ENTRY_COUNT,
					MessageCodes.TRIAL_JOURNAL_TAB_TITLE, MessageCodes.TRIAL_JOURNAL_TAB_TITLE_WITH_COUNT,
					WebUtil.getJournalCount(JournalModule.TRIAL_JOURNAL, in.getTrialId()));
		}
	}

	@Override
	protected String changeAction(Long id) {
		LazyDataModelBase.clearFilters("stratificationlist_list");
		out = null;
		this.trialId = id;
		initIn();
		initSets();
		return CHANGE_OUTCOME;
	}


	public List<IDVO> completeSelectionSetValue(String query) {
		Iterator<Long> it = requiredFields.iterator();
		if (it.hasNext()) {
			try {
				Collection selectionSetValueVOs = WebUtil.getServiceLocator().getToolsService().completeInputFieldSelectionSetValue(WebUtil.getAuthentication(), query, it.next(),
						null);
				IDVO.transformVoCollection(selectionSetValueVOs);
				return (List<IDVO>) selectionSetValueVOs;
			} catch (ClassCastException e) {
			} catch (ServiceException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			} catch (AuthorisationException e) {
			} catch (IllegalArgumentException e) {
			}
		}
		return new ArrayList<IDVO>();
	}

	@Override
	public String deleteAction() {
		return deleteAction(in.getId());
	}

	@Override
	public String deleteAction(Long id) {
		try {
			out = WebUtil.getServiceLocator().getTrialService().deleteStratificationRandomizationList(WebUtil.getAuthentication(), id);
			initIn();
			initSets();
			out = null;
			addOperationSuccessMessage(MessageCodes.DELETE_OPERATION_SUCCESSFUL);
			return DELETE_OUTCOME;
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
		return ERROR_OUTCOME;
	}

	// public ArrayList<SelectItem> getAvailableTags() {
	// return availableTags;
	// }
	public StratificationRandomizationListInVO getIn() {
		return in;
	}

	@Override
	public String getModifiedAnnotation() {
		if (out != null) {
			return WebUtil.getModifiedAnnotation(out.getVersion(), out.getModifiedUser(), out.getModifiedTimestamp());
		} else {
			return super.getModifiedAnnotation();
		}
	}


	public StratificationRandomizationListOutVO getOut() {
		return out;
	}

	public RandomizationListLazyModel getRandomizationListModel() {
		return randomizationListModel;
	}

	public IDVO getSelectedStratificationRandomizationList() {
		if (this.out != null) {
			return IDVO.transformVo(this.out);
		} else {
			return null;
		}
	}

	public List<IDVO> getSelectionSetValues() {
		return  new IDVOList(selectionSetValues) ;
	}

	@Override
	public String getTitle() {
		if (out != null) {
			return Messages.getMessage(MessageCodes.STRATIFICATION_RANDOMIZATION_LIST_TITLE, Long.toString(out.getId()),
					(new ProbandListEntryTagValueOutVOStringAdapter(Settings.getInt(SettingCodes.PROBAND_LIST_PROBAND_LIST_ENTRY_TAG_VALUE_TEXT_CLIP_MAX_LENGTH, Bundle.SETTINGS,
							DefaultSettings.PROBAND_LIST_PROBAND_LIST_ENTRY_TAG_VALUE_TEXT_CLIP_MAX_LENGTH)))
					.selectionSetValuesToString(out.getSelectionSetValues()));
		} else {
			return Messages.getString(MessageCodes.CREATE_NEW_STRATIFICATION_RANDOMIZATION_LIST);
		}
	}

	public void handleSelectionSetValueSelect( SelectEvent event) {
		// System.out.println("select");
		updateRequiredFields();
	}

	public void handleSelectionSetValueUnselect( UnselectEvent event) {
		// System.out.println("unselect");
		updateRequiredFields();
	}

	@PostConstruct
	private void init() {
		Long id = WebUtil.getLongParamValue(GetParamNames.STRATIFICATION_RANDOMIZATION_LIST_ID);
		if (id != null) {
			this.load(id);
		} else {
			initIn();
			initSets();
		}
	}

	private void initIn() {
		if (in == null) {
			in = new StratificationRandomizationListInVO();
		}
		if (out != null) {
			copyStratificationRandomizationListOutToIn(in, out);
			trialId = in.getTrialId();
		} else {
			initStratificationRandomizationListDefaultValues(in, trialId);
		}
	}

	private void initSets() {
		randomizationListModel.setTrialId(in.getTrialId());
		randomizationListModel.updateRowCount();

		stratificationProbandListEntryTagVOs = null;
		if (trialId != null) {
			try {
				stratificationProbandListEntryTagVOs = WebUtil.getServiceLocator().getTrialService().getProbandListEntryTagList(WebUtil.getAuthentication(), trialId, null, true);
			} catch (ServiceException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			} catch (AuthorisationException e) {
			} catch (IllegalArgumentException e) {
			}
		}
		loadSelectionSetValues();
		updateRequiredFields();

		trial = WebUtil.getTrial(this.in.getTrialId());
		if (WebUtil.isTrialLocked(trial)) {
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.TRIAL_LOCKED);
		}
	}

	@Override
	public boolean isCreateable() {
		return (in.getTrialId() == null ? false : !WebUtil.isTrialLocked(trial));
	}

	@Override
	public boolean isCreated() {
		return out != null;
	}

	@Override
	public boolean isEditable() {
		return isCreated() && !WebUtil.isTrialLocked(trial);
	}

	public boolean isInputVisible() {
		return isCreated() || !WebUtil.isTrialLocked(trial);
	}

	@Override
	public boolean isRemovable() {
		return isCreated() && !WebUtil.isTrialLocked(trial);
	}

	@Override
	public String loadAction() {
		return loadAction(in.getId());
	}

	@Override
	public String loadAction(Long id) {
		out = null;
		try {
			out = WebUtil.getServiceLocator().getTrialService().getStratificationRandomizationList(WebUtil.getAuthentication(), id);
			StratificationRandomizationList LOAD_OUTCOME;
		} catch (ServiceException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} finally {
			initIn();
			initSets();
		}
		return ERROR_OUTCOME;
	}

	private void loadSelectionSetValues() {
		selectionSetValues.clear();
		Iterator<Long> it = in.getSelectionSetValueIds().iterator();
		while (it.hasNext()) {
			InputFieldSelectionSetValueOutVO selectionSetValue = WebUtil.getInputFieldSelectionSetValue(it.next());
			if (selectionSetValue != null) {
				selectionSetValues.add(selectionSetValue);
			}
		}
	}

	@Override
	public String resetAction() {
		out = null;
		initIn();
		initSets();
		return RESET_OUTCOME;
	}

	private void sanitizeInVals() {
		in.getSelectionSetValueIds().clear();
		Iterator<InputFieldSelectionSetValueOutVO> it = selectionSetValues.iterator();

		while (it.hasNext()) {
			InputFieldSelectionSetValueOutVO selectionSetValue = it.next();
			if (selectionSetValue != null) {
				in.getSelectionSetValueIds().add(selectionSetValue.getId());
			}
		}
	}

	public void setSelectedStratificationRandomizationList(IDVO randomizationList) {
		if (randomizationList != null) {
			this.out = (StratificationRandomizationListOutVO) randomizationList.getVo();
			this.initIn();
			initSets();
		}
	}

	public void setSelectionSetValues(List<IDVO> selectionSetValues) {
		if (selectionSetValues != null) {
			ArrayList<InputFieldSelectionSetValueOutVO> selectionSetValuesCopy = new ArrayList<InputFieldSelectionSetValueOutVO>(selectionSetValues.size());
			Iterator<IDVO> it = selectionSetValues.iterator();
			while (it.hasNext()) {
				IDVO idVo = it.next();
				if (idVo != null) {
					selectionSetValuesCopy.add((InputFieldSelectionSetValueOutVO) idVo.getVo());
				}
			}
			this.selectionSetValues.clear();
			this.selectionSetValues.addAll(selectionSetValuesCopy);
		} else {
			this.selectionSetValues.clear();
		}
	}

	@Override
	public String updateAction() {
		StratificationRandomizationListInVO backup = new StratificationRandomizationListInVO(in);
		sanitizeInVals();
		try {
			out = WebUtil.getServiceLocator().getTrialService().updateStratificationRandomizationList(WebUtil.getAuthentication(), in);
			initIn();
			initSets();
			addOperationSuccessMessage(MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
			return UPDATE_OUTCOME;
		} catch (ServiceException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (IllegalArgumentException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
		return ERROR_OUTCOME;
	}

	private void updateRequiredFields() {
		requiredFields.clear();
		HashSet<Long> usedFields = new HashSet<Long>();
		if (stratificationProbandListEntryTagVOs != null) {
			// if (in != null) {
			Iterator<InputFieldSelectionSetValueOutVO> it = selectionSetValues.iterator();
			// Iterator<InputFieldSelectionSetValueOutVO> it = ProbandListEntryTagValueOutVOStringAdapter.getSelectionSetValuesFromIds(in.getSelectionSetValueIds()).iterator();
			while (it.hasNext()) {
				usedFields.add(it.next().getField().getId());
			}
			// }
			Iterator<ProbandListEntryTagOutVO> tagIt = stratificationProbandListEntryTagVOs.iterator();
			while (tagIt.hasNext()) {
				InputFieldOutVO field = tagIt.next().getField();
				if (!usedFields.contains(field.getId())) {
					requiredFields.add(field.getId());
				}
			}
		}
	}
}

