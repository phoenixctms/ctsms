package org.phoenixctms.ctsms.web.model.trial;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.ECRFInVO;
import org.phoenixctms.ctsms.vo.ECRFOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.ProbandGroupOutVO;
import org.phoenixctms.ctsms.vo.ProbandListStatusTypeVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.vo.VisitOutVO;
import org.phoenixctms.ctsms.web.component.datatable.DataTable;
import org.phoenixctms.ctsms.web.model.GroupVisitMatrix;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.model.IDVOList;
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
public class EcrfBean extends ManagedBeanBase {

	public static void copyEcrfOutToIn(ECRFInVO in, ECRFOutVO out) {
		if (in != null && out != null) {
			TrialOutVO trialVO = out.getTrial();
			ProbandListStatusTypeVO probandListStatus = out.getProbandListStatus();
			in.getGroupIds().clear();
			Iterator it = out.getGroups().iterator();
			while (it.hasNext()) {
				in.getGroupIds().add(((ProbandGroupOutVO) it.next()).getId());
			}
			in.getVisitIds().clear();
			it = out.getVisits().iterator();
			while (it.hasNext()) {
				in.getVisitIds().add(((VisitOutVO) it.next()).getId());
			}
			in.setId(out.getId());
			in.setTrialId(trialVO == null ? null : trialVO.getId());
			in.setVersion(out.getVersion());
			in.setProbandListStatusId(probandListStatus == null ? null : probandListStatus.getId());
			in.setActive(out.getActive());
			in.setDescription(out.getDescription());
			in.setDisabled(out.getDisabled());
			in.setEnableBrowserFieldCalculation(out.getEnableBrowserFieldCalculation());
			in.setCharge(out.getCharge());
			in.setName(out.getName());
			in.setRevision(out.getRevision());
			in.setTitle(out.getTitle());
			in.setExternalId(out.getExternalId());
		}
	}

	public static void initEcrfDefaultValues(ECRFInVO in, Long trialId) {
		if (in != null) {
			in.setId(null);
			in.setTrialId(trialId);
			in.setVersion(null);
			in.getVisitIds().clear();
			in.getGroupIds().clear();
			in.setProbandListStatusId(null);
			in.setRevision(Messages.getString(MessageCodes.ECRF_REVISION_PRESET));
			in.setActive(Settings.getBoolean(SettingCodes.ECRF_ACTIVE_PRESET, Bundle.SETTINGS, DefaultSettings.ECRF_ACTIVE_PRESET));
			in.setDescription(Messages.getString(MessageCodes.ECRF_DESCRIPTION_PRESET));
			in.setDisabled(Settings.getBoolean(SettingCodes.ECRF_DISABLED_PRESET, Bundle.SETTINGS, DefaultSettings.ECRF_DISABLED_PRESET));
			in.setEnableBrowserFieldCalculation(Settings.getBoolean(SettingCodes.ECRF_ENABLE_BROWSER_FIELD_CALCULATION_PRESET, Bundle.SETTINGS,
					DefaultSettings.ECRF_ENABLE_BROWSER_FIELD_CALCULATION_PRESET));
			in.setCharge(Settings.getFloatNullable(SettingCodes.ECRF_CHARGE_PRESET, Bundle.SETTINGS, DefaultSettings.ECRF_CHARGE_PRESET));
			in.setName(Messages.getString(MessageCodes.ECRF_NAME_PRESET));
			in.setTitle(Messages.getString(MessageCodes.ECRF_TITLE_PRESET));
			in.setExternalId(Messages.getString(MessageCodes.ECRF_EXTERNAL_ID_PRESET));
		}
	}

	private ECRFInVO in;
	private ECRFOutVO out;
	private Long trialId;
	private TrialOutVO trial;
	private ArrayList<SelectItem> probandListStatusTypes;
	private EcrfLazyModel ecrfModel;
	private GroupVisitMatrix<ECRFOutVO> matrix;
	private Long cloneAddTrialId;
	private String deferredDeleteReason;
	private List<ProbandGroupOutVO> groups;
	private List<VisitOutVO> visits;

	public EcrfBean() {
		super();
		ecrfModel = new EcrfLazyModel();
		groups = new ArrayList<ProbandGroupOutVO>();
		visits = new ArrayList<VisitOutVO>();
		matrix = new GroupVisitMatrix<ECRFOutVO>() {

			@Override
			protected Collection<ProbandGroupOutVO> getGroupsFromItem(ECRFOutVO item) {
				return item.getGroups();
			}

			@Override
			protected Long getItemCount(Long trialId) {
				return WebUtil.getEcrfCount(trialId);
			}

			@Override
			protected String getItemLabel(ECRFOutVO item) {
				return item.getName();
			}

			@Override
			protected Collection<ECRFOutVO> getItemsPage(Long trialId, PSFVO psf) {
				try {
					return WebUtil.getServiceLocator().getTrialService()
							.getEcrfList(WebUtil.getAuthentication(), trialId, null, true, psf);
				} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
				} catch (AuthenticationException e) {
					WebUtil.publishException(e);
				}
				return null;
			}

			@Override
			protected String getPaginatorFirstPageButtonLabel() {
				return MessageCodes.ECRF_MATRIX_FIRST_PAGE_BUTTON_LABEL;
			}

			@Override
			protected String getPaginatorLoadLabel() {
				return MessageCodes.ECRF_MATRIX_LOAD_LABEL;
			}

			@Override
			protected String getPaginatorPageButtonLabel() {
				return MessageCodes.ECRF_MATRIX_PAGE_BUTTON_LABEL;
			}

			@Override
			protected Collection<VisitOutVO> getVisitsFromItem(ECRFOutVO item) {
				return item.getVisits();
			}

			@Override
			protected void setGroupId(Long groupId) {
				ProbandGroupOutVO group = WebUtil.getProbandGroup(groupId);
				if (group != null && !groups.contains(group)) {
					groups.add(group);
				}
			}

			@Override
			protected void setVisitId(Long visitId) {
				VisitOutVO visit = WebUtil.getVisit(visitId);
				if (visit != null && !visits.contains(visit)) {
					visits.add(visit);
				}
			}
		};
	}

	@Override
	public String addAction() {
		ECRFInVO backup = new ECRFInVO(in);
		in.setId(null);
		in.setVersion(null);
		sanitizeInVals();
		try {
			out = WebUtil.getServiceLocator().getTrialService().addEcrf(WebUtil.getAuthentication(), in);
			initIn();
			initSets();
			matrix.loadMatrix();
			addOperationSuccessMessage(MessageCodes.ADD_OPERATION_SUCCESSFUL);
			return ADD_OUTCOME;
		} catch (ServiceException | IllegalArgumentException | AuthorisationException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	public final void addClone() {
		actionPostProcess(addCloneAction());
	}

	public String addCloneAction() {
		try {
			if (cloneAddTrialId != null) {
				if (isCloneAddTrialEnabled() || cloneAddTrialId.equals(trialId)) {
					ECRFOutVO clone = WebUtil.getServiceLocator().getTrialService().cloneEcrf(WebUtil.getAuthentication(), in.getId(), cloneAddTrialId);
					ecrfModel.updateRowCount();
					Messages.addLocalizedMessageClientId("cloneEcrfMessages", FacesMessage.SEVERITY_INFO, MessageCodes.CLONE_ADD_OPERATION_SUCCESSFUL, clone.getUniqueName());
					return CLONE_ADD_OUTCOME;
				} else {
					Messages.addLocalizedMessageClientId("cloneEcrfMessages", FacesMessage.SEVERITY_ERROR, MessageCodes.WRONG_CLONE_ADD_ECRF_TRIAL_ID);
				}
			} else {
				Messages.addLocalizedMessageClientId("cloneEcrfMessages", FacesMessage.SEVERITY_ERROR, MessageCodes.CLONE_ADD_ECRF_TRIAL_ID_REQUIRED);
			}
		} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
			Messages.addMessageClientId("cloneEcrfMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessageClientId("cloneEcrfMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
		RequestContext requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_ECRF_TAB_TITLE_BASE64,
				JSValues.AJAX_ECRF_COUNT, MessageCodes.ECRFS_TAB_TITLE, MessageCodes.ECRFS_TAB_TITLE_WITH_COUNT,
				new Long(ecrfModel.getRowCount()));
		if (operationSuccess && in.getTrialId() != null) {
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_TRIAL_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_TRIAL_JOURNAL_ENTRY_COUNT,
					MessageCodes.TRIAL_JOURNAL_TAB_TITLE, MessageCodes.TRIAL_JOURNAL_TAB_TITLE_WITH_COUNT,
					WebUtil.getJournalCount(JournalModule.TRIAL_JOURNAL, in.getTrialId()));
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_ECRF_FIELD_TAB_TITLE_BASE64, JSValues.AJAX_ECRF_FIELD_COUNT,
					MessageCodes.ECRF_FIELDS_TAB_TITLE, MessageCodes.ECRF_FIELDS_TAB_TITLE_WITH_COUNT,
					WebUtil.getEcrfFieldCount(in.getTrialId(), null));
		}
	}

	@Override
	protected String changeAction(Long id) {
		DataTable.clearFilters("ecrf_list");
		out = null;
		this.trialId = id;
		matrix.change(id);
		initIn();
		initSets();
		matrix.loadMatrix();
		cloneAddTrialId = id;
		return CHANGE_OUTCOME;
	}

	@Override
	public String deleteAction() {
		return deleteAction(in.getId());
	}

	@Override
	public String deleteAction(Long id) {
		try {
			out = WebUtil.getServiceLocator().getTrialService().deleteEcrf(WebUtil.getAuthentication(), id,
					Settings.getBoolean(SettingCodes.ECRF_DEFERRED_DELETE, Bundle.SETTINGS, DefaultSettings.ECRF_DEFERRED_DELETE),
					false, deferredDeleteReason);
			initIn();
			initSets();
			matrix.loadMatrix();
			if (!out.getDeferredDelete()) {
				addOperationSuccessMessage(MessageCodes.DELETE_OPERATION_SUCCESSFUL);
			}
			out = null;
			return DELETE_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	public Long getCloneAddTrialId() {
		return cloneAddTrialId;
	}

	public String getCloneAddTrialName() {
		return WebUtil.trialIdToName(cloneAddTrialId);
	}

	public String getDeferredDeleteReason() {
		return deferredDeleteReason;
	}

	public EcrfLazyModel getEcrfModel() {
		return ecrfModel;
	}

	public ECRFInVO getIn() {
		return in;
	}

	public GroupVisitMatrix getMatrix() {
		return matrix;
	}

	@Override
	public String getModifiedAnnotation() {
		if (out != null) {
			return WebUtil.getModifiedAnnotation(out.getVersion(), out.getModifiedUser(), out.getModifiedTimestamp());
		} else {
			return super.getModifiedAnnotation();
		}
	}

	public ECRFOutVO getOut() {
		return out;
	}

	public ArrayList<SelectItem> getProbandListStatusTypes() {
		return probandListStatusTypes;
	}

	public IDVO getSelectedEcrf() {
		if (this.out != null) {
			return IDVO.transformVo(this.out);
		} else {
			return null;
		}
	}

	@Override
	public String getTitle() {
		if (out != null) {
			return Messages.getMessage(MessageCodes.ECRF_TITLE, Long.toString(out.getId()), out.getName());
		} else {
			return Messages.getString(MessageCodes.CREATE_NEW_ECRF);
		}
	}

	public String getTrialName() {
		return WebUtil.trialOutVOToString(trial);
	}

	@PostConstruct
	private void init() {
		Long id = WebUtil.getLongParamValue(GetParamNames.ECRF_ID);
		if (id != null) {
			this.load(id);
		} else {
			initIn();
			initSets();
		}
	}

	private void initIn() {
		if (in == null) {
			in = new ECRFInVO();
		}
		if (out != null) {
			copyEcrfOutToIn(in, out);
			trialId = in.getTrialId();
		} else {
			initEcrfDefaultValues(in, trialId);
		}
	}

	private void initSets() {
		ecrfModel.setTrialId(in.getTrialId());
		ecrfModel.updateRowCount();
		loadProbandGroups();
		loadVisits();
		trial = WebUtil.getTrial(this.in.getTrialId());
		probandListStatusTypes = WebUtil.getAllProbandListStatusTypes(trial != null ? trial.getType().getPerson() : null);
		matrix.initPages();
		if (WebUtil.isTrialLocked(trial)) {
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.TRIAL_LOCKED);
		}
		deferredDeleteReason = (out == null ? null : out.getDeferredDeleteReason());
		if (out != null && out.isDeferredDelete()) {
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.MARKED_FOR_DELETION, deferredDeleteReason);
		}
	}

	public boolean isCloneable() {
		return isCreated();
	}

	public boolean isCloneAddTrialEnabled() {
		if (trialId == null) {
			return false;
		} else {
			return Settings.getBoolean(SettingCodes.ECRF_CLONE_ADD_OTHER_TRIAL_ENABLED, Bundle.SETTINGS, DefaultSettings.ECRF_CLONE_ADD_OTHER_TRIAL_ENABLED);
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

	public boolean isDeferredDelete() {
		return Settings.getBoolean(SettingCodes.ECRF_DEFERRED_DELETE, Bundle.SETTINGS, DefaultSettings.ECRF_DEFERRED_DELETE);
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
			out = WebUtil.getServiceLocator().getTrialService().getEcrf(WebUtil.getAuthentication(), id);
			return LOAD_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} finally {
			initIn();
			initSets();
			matrix.loadMatrix();
		}
		return ERROR_OUTCOME;
	}

	@Override
	public String resetAction() {
		out = null;
		initIn();
		initSets();
		matrix.loadMatrix();
		return RESET_OUTCOME;
	}

	public void setCloneAddTrialId(Long cloneAddTrialId) {
		this.cloneAddTrialId = cloneAddTrialId;
	}

	public void setDeferredDeleteReason(String deferredDeleteReason) {
		this.deferredDeleteReason = deferredDeleteReason;
	}

	public void setSelectedEcrf(IDVO ecrf) {
		if (ecrf != null) {
			this.out = (ECRFOutVO) ecrf.getVo();
			this.initIn();
			initSets();
		}
	}

	@Override
	public String updateAction() {
		ECRFInVO backup = new ECRFInVO(in);
		sanitizeInVals();
		try {
			out = WebUtil.getServiceLocator().getTrialService().updateEcrf(WebUtil.getAuthentication(), in);
			initIn();
			initSets();
			matrix.loadMatrix();
			addOperationSuccessMessage(MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
			return UPDATE_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	public List<IDVO> completeGroup(String query) {
		try {
			Collection probandGroupVOs = WebUtil.getServiceLocator().getToolsService().completeProbandGroup(WebUtil.getAuthentication(), query, query, trialId, null);
			IDVO.transformVoCollection(probandGroupVOs);
			return (List<IDVO>) probandGroupVOs;
		} catch (ClassCastException e) {
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
		return new ArrayList<IDVO>();
	}

	public List<IDVO> getGroups() {
		return new IDVOList(groups);
	}

	public void handleGroupSelect(SelectEvent event) {
	}

	public void handleGroupUnselect(UnselectEvent event) {
	}

	private void loadProbandGroups() {
		groups.clear();
		Iterator<Long> it = in.getGroupIds().iterator();
		while (it.hasNext()) {
			ProbandGroupOutVO group = WebUtil.getProbandGroup(it.next());
			if (group != null) {
				groups.add(group);
			}
		}
	}

	public void setGroups(List<IDVO> groups) {
		if (groups != null) {
			ArrayList<ProbandGroupOutVO> groupsCopy = new ArrayList<ProbandGroupOutVO>(groups.size());
			Iterator<IDVO> it = groups.iterator();
			while (it.hasNext()) {
				IDVO idVo = it.next();
				if (idVo != null) {
					groupsCopy.add((ProbandGroupOutVO) idVo.getVo());
				}
			}
			this.groups.clear();
			this.groups.addAll(groupsCopy);
		} else {
			this.groups.clear();
		}
	}

	public List<IDVO> completeVisit(String query) {
		try {
			Collection visitVOs = WebUtil.getServiceLocator().getToolsService().completeVisit(WebUtil.getAuthentication(), query, query, trialId, null);
			IDVO.transformVoCollection(visitVOs);
			return (List<IDVO>) visitVOs;
		} catch (ClassCastException e) {
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
		return new ArrayList<IDVO>();
	}

	public List<IDVO> getVisits() {
		return new IDVOList(visits);
	}

	public void handleVisitSelect(SelectEvent event) {
	}

	public void handleVisitUnselect(UnselectEvent event) {
	}

	private void loadVisits() {
		visits.clear();
		Iterator<Long> it = in.getVisitIds().iterator();
		while (it.hasNext()) {
			VisitOutVO visit = WebUtil.getVisit(it.next());
			if (visit != null) {
				visits.add(visit);
			}
		}
	}

	public void setVisits(List<IDVO> visits) {
		if (visits != null) {
			ArrayList<VisitOutVO> visitsCopy = new ArrayList<VisitOutVO>(visits.size());
			Iterator<IDVO> it = visits.iterator();
			while (it.hasNext()) {
				IDVO idVo = it.next();
				if (idVo != null) {
					visitsCopy.add((VisitOutVO) idVo.getVo());
				}
			}
			this.visits.clear();
			this.visits.addAll(visitsCopy);
		} else {
			this.visits.clear();
		}
	}

	private void sanitizeInVals() {
		LinkedHashSet<Long> groupIds = new LinkedHashSet<Long>(groups.size()); //force unique items to prevent confusion when unselecting a duplicate item
		Iterator it = groups.iterator();
		while (it.hasNext()) {
			ProbandGroupOutVO group = (ProbandGroupOutVO) it.next();
			if (group != null) {
				groupIds.add(group.getId());
			}
		}
		in.setGroupIds(new ArrayList<Long>(groupIds));
		LinkedHashSet<Long> visitIds = new LinkedHashSet<Long>(visits.size()); //force unique items to prevent confusion when unselecting a duplicate item
		it = visits.iterator();
		while (it.hasNext()) {
			VisitOutVO visit = (VisitOutVO) it.next();
			if (visit != null) {
				visitIds.add(visit.getId());
			}
		}
		in.setVisitIds(new ArrayList<Long>(visitIds));
	}
}
