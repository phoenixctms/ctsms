package org.phoenixctms.ctsms.web.model.trial;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.enumeration.PositionMovement;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
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

@ManagedBean
@ViewScoped
public class EcrfBean extends ManagedBeanBase {

	public static void copyEcrfOutToIn(ECRFInVO in, ECRFOutVO out) {
		if (in != null && out != null) {
			ProbandGroupOutVO probandGroupVO = out.getGroup();
			VisitOutVO visitVO = out.getVisit();
			TrialOutVO trialVO = out.getTrial();
			ProbandListStatusTypeVO probandListStatus = out.getProbandListStatus();
			in.setGroupId(probandGroupVO == null ? null : probandGroupVO.getId());
			in.setId(out.getId());
			// in.setStart(out.getStart());
			// in.setStop(out.getStop());
			// in.setToken(out.getToken());
			in.setTrialId(trialVO == null ? null : trialVO.getId());
			in.setVersion(out.getVersion());
			in.setVisitId(visitVO == null ? null : visitVO.getId());
			in.setProbandListStatusId(probandListStatus == null ? null : probandListStatus.getId());
			in.setActive(out.getActive());
			in.setDescription(out.getDescription());
			in.setDisabled(out.getDisabled());
			in.setEnableBrowserFieldCalculation(out.getEnableBrowserFieldCalculation());
			in.setCharge(out.getCharge());
			in.setName(out.getName());
			in.setPosition(out.getPosition());
			in.setTitle(out.getTitle());
			in.setExternalId(out.getExternalId());
		}
	}

	public static void initEcrfDefaultValues(ECRFInVO in, Long trialId) {
		if (in != null) {
			Long position = null;
			if (trialId != null) {
				try {
					position = WebUtil.getServiceLocator().getTrialService().getEcrfMaxPosition(WebUtil.getAuthentication(), trialId, null);
				} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
				} catch (AuthenticationException e) {
					WebUtil.publishException(e);
				}
			}
			in.setId(null);
			// in.setStart(null);
			// in.setStop(null);
			// in.setToken(Messages.getString(MessageCodes.VISIT_SCHEDULE_ITEM_TOKEN_PRESET));
			in.setTrialId(trialId);
			in.setVersion(null);
			in.setVisitId(null);
			in.setGroupId(null);
			in.setProbandListStatusId(null);
			in.setPosition(position == null ? CommonUtil.LIST_INITIAL_POSITION : position + 1L);
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
	private ArrayList<SelectItem> visits;
	private ArrayList<SelectItem> filterVisits;
	private ArrayList<SelectItem> probandGroups;
	private ArrayList<SelectItem> filterProbandGroups;
	private ArrayList<SelectItem> probandListStatusTypes;
	private EcrfLazyModel ecrfModel;
	private GroupVisitMatrix<ECRFOutVO> matrix;
	private Long cloneAddTrialId;
	private String deferredDeleteReason;

	public EcrfBean() {
		super();
		ecrfModel = new EcrfLazyModel();
		matrix = new GroupVisitMatrix<ECRFOutVO>() {

			@Override
			protected ProbandGroupOutVO getGroupFromItem(ECRFOutVO item) {
				return item.getGroup();
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
				} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
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
			protected VisitOutVO getVisitFromItem(ECRFOutVO item) {
				return item.getVisit();
			}

			@Override
			protected void setGroupId(Long groupId) {
				in.setGroupId(groupId);
			}

			@Override
			protected void setVisitId(Long visitId) {
				in.setVisitId(visitId);
			}
		};
	}

	@Override
	public String addAction() {
		ECRFInVO backup = new ECRFInVO(in);
		// Long idBackup = in.getId();
		// Long versionBackup = in.getVersion();
		in.setId(null);
		in.setVersion(null);
		try {
			out = WebUtil.getServiceLocator().getTrialService().addEcrf(WebUtil.getAuthentication(), in);
			initIn();
			initSets();
			matrix.loadMatrix();
			addOperationSuccessMessage(MessageCodes.ADD_OPERATION_SUCCESSFUL);
			return ADD_OUTCOME;
		} catch (ServiceException|IllegalArgumentException|AuthorisationException e) {
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
		} catch (AuthorisationException|ServiceException|IllegalArgumentException e) {
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
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
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

	public ArrayList<SelectItem> getFilterProbandGroups() {
		return filterProbandGroups;
	}

	public ArrayList<SelectItem> getFilterVisits() {
		return filterVisits;
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

	public ArrayList<SelectItem> getProbandGroups() {
		return probandGroups;
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

	public ArrayList<SelectItem> getVisits() {
		return visits;
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
		visits = WebUtil.getVisits(in.getTrialId());
		probandGroups = WebUtil.getProbandGroups(in.getTrialId());
		trial = WebUtil.getTrial(this.in.getTrialId());
		probandListStatusTypes = WebUtil.getAllProbandListStatusTypes(trial != null ? trial.getType().getPerson() : null);
		filterProbandGroups = new ArrayList<SelectItem>(probandGroups);
		filterProbandGroups.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		filterVisits = new ArrayList<SelectItem>(visits);
		filterVisits.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		matrix.initPages();
		// trial = WebUtil.getTrial(this.in.getTrialId());
		if (WebUtil.isTrialLocked(trial)) {
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.TRIAL_LOCKED);
		}
		deferredDeleteReason = (out == null ? null : out.getDeferredDeleteReason());
		if (out != null && out.isDeferredDelete()) { // && Settings.getBoolean(SettingCodes.ECRF_DEFERRED_DELETE, Bundle.SETTINGS, DefaultSettings.ECRF_DEFERRED_DELETE)) {
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
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
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

	public void moveDown(Long ecrfId) {
		try {
			out = WebUtil.getServiceLocator().getTrialService().moveEcrf(WebUtil.getAuthentication(), ecrfId, PositionMovement.DOWN);
			initIn();
			initSets();
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
	}

	public void moveFirst(Long ecrfId) {
		try {
			out = WebUtil.getServiceLocator().getTrialService().moveEcrf(WebUtil.getAuthentication(), ecrfId, PositionMovement.FIRST);
			initIn();
			initSets();
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
	}

	public void moveLast(Long ecrfId) {
		try {
			out = WebUtil.getServiceLocator().getTrialService().moveEcrf(WebUtil.getAuthentication(), ecrfId, PositionMovement.LAST);
			initIn();
			initSets();
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
	}

	public void moveTo() {
		Long ecrfId = WebUtil.getLongParamValue(GetParamNames.ECRF_ID);
		Long targetPosition = WebUtil.getLongParamValue(GetParamNames.TARGET_POSITION);
		if (ecrfId != null && targetPosition != null) {
			try {
				Collection<ECRFOutVO> updated = WebUtil.getServiceLocator().getTrialService().moveEcrfTo(WebUtil.getAuthentication(), ecrfId, targetPosition);
				out = null;
				initIn();
				initSets();
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_INFO, MessageCodes.POSITIONS_UPDATE_SUCCESSFUL, updated.size());
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
				Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			} catch (AuthenticationException e) {
				Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
				WebUtil.publishException(e);
			}
		}
	}

	public void moveUp(Long ecrfId) {
		try {
			out = WebUtil.getServiceLocator().getTrialService().moveEcrf(WebUtil.getAuthentication(), ecrfId, PositionMovement.UP);
			initIn();
			initSets();
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
	}

	public final void normalizePositions() {
		actionPostProcess(normalizePositionsAction());
	}

	public String normalizePositionsAction() {
		try {
			Collection<ECRFOutVO> updated = WebUtil.getServiceLocator().getTrialService().normalizeEcrfPositions(WebUtil.getAuthentication(), in.getId());
			out = null;
			initIn();
			initSets();
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_INFO, MessageCodes.POSITIONS_UPDATE_SUCCESSFUL, updated.size());
			return UPDATE_OUTCOME;
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
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
		try {
			out = WebUtil.getServiceLocator().getTrialService().updateEcrf(WebUtil.getAuthentication(), in);
			initIn();
			initSets();
			matrix.loadMatrix();
			addOperationSuccessMessage(MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
			return UPDATE_OUTCOME;
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}
}
