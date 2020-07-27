package org.phoenixctms.ctsms.web.model.trial;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.enumeration.PositionMovement;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.ProbandContactDetailValueOutVO;
import org.phoenixctms.ctsms.vo.ProbandGroupOutVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryInVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryOutVO;
import org.phoenixctms.ctsms.vo.ProbandListExcelVO;
import org.phoenixctms.ctsms.vo.ProbandListStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.vo.ProbandStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.ProbandTagValueOutVO;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.model.shared.ProbandListEntryBeanBase;
import org.phoenixctms.ctsms.web.model.shared.ProbandListEntryModel;
import org.phoenixctms.ctsms.web.model.shared.ProbandMultiPickerModel;
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
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean
@ViewScoped
public class ProbandListEntryBean extends ProbandListEntryBeanBase {

	public static void initProbandListEntryDefaultValues(ProbandListEntryInVO in, Long trialId) {
		if (in != null) {
			in.setGroupId(null);
			in.setId(null);
			in.setPosition(getInitialPosition(trialId));
			in.setProbandId(null);
			in.setTrialId(trialId);
			in.setVersion(null);
			in.setRating(Settings.getLongNullable(SettingCodes.PROBAND_LIST_ENTRY_RATING_PRESET, Bundle.SETTINGS, DefaultSettings.PROBAND_LIST_ENTRY_RATING_PRESET));
			in.setRatingMax(Settings.getLongNullable(SettingCodes.PROBAND_LIST_ENTRY_RATING_MAX_PRESET, Bundle.SETTINGS, DefaultSettings.PROBAND_LIST_ENTRY_RATING_MAX_PRESET));
		}
	}

	private Long trialId;
	private Long bulkAddRating;
	private Long bulkAddRatingMax;
	private ProbandGroupOutVO bulkAddGroup;
	private boolean shuffle;
	private boolean randomize;
	private Long limit;
	private ProbandMultiPickerModel probandMultiPicker;
	private HashMap<Long, Collection<IDVO>> probandTagValueCache;
	private HashMap<Long, Collection<IDVO>> probandContactDetailValueCache;
	private HashMap<Long, Collection<IDVO>> probandAddressCache;
	private HashMap<Long, Collection<IDVO>> probandStatusCache;
	private HashMap<Long, Collection<IDVO>> probandListStatusCache;

	public ProbandListEntryBean() {
		super();
		probandTagValueCache = new HashMap<Long, Collection<IDVO>>();
		probandContactDetailValueCache = new HashMap<Long, Collection<IDVO>>();
		probandAddressCache = new HashMap<Long, Collection<IDVO>>();
		probandStatusCache = new HashMap<Long, Collection<IDVO>>();
		probandListStatusCache = new HashMap<Long, Collection<IDVO>>();
		probandMultiPicker = new ProbandMultiPickerModel();
		shuffle = Settings.getBoolean(SettingCodes.PROBAND_LIST_BULK_ADD_SHUFFLE, Bundle.SETTINGS, DefaultSettings.PROBAND_LIST_BULK_ADD_SHUFFLE);
		randomize = Settings.getBoolean(SettingCodes.PROBAND_LIST_BULK_ADD_RANDOMIZE, Bundle.SETTINGS, DefaultSettings.PROBAND_LIST_BULK_ADD_RANDOMIZE);
		limit = Settings.getLongNullable(SettingCodes.PROBAND_LIST_BULK_ADD_LIMIT, Bundle.SETTINGS, DefaultSettings.PROBAND_LIST_BULK_ADD_LIMIT);
	}

	public final void addBulk() {
		actionPostProcess(addBulkAction());
	}

	public String addBulkAction() {
		try {
			Set<Long> ids = this.probandMultiPicker.getSelectionIds();
			Iterator<ProbandListEntryOutVO> it = WebUtil.getServiceLocator().getTrialService()
					.addProbandListEntries(WebUtil.getAuthentication(), trialId, randomize && isRandomization(), bulkAddGroup != null ? bulkAddGroup.getId() : null, bulkAddRating,
							bulkAddRatingMax, ids, shuffle,
							limit)
					.iterator();
			while (it.hasNext()) {
				this.probandMultiPicker.removeId(it.next().getProband().getId());
			}
			int itemsLeft = probandMultiPicker.getSelection().size();
			if (itemsLeft > 0) {
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.BULK_ADD_OPERATION_INCOMPLETE, ids.size() - itemsLeft, ids.size());
			} else {
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_INFO, MessageCodes.BULK_ADD_OPERATION_SUCCESSFUL, ids.size(), ids.size());
			}
			probandListEntryModel.updateRowCount();
			return BULK_ADD_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
		RequestContext requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_PROBAND_LIST_ENTRY_TAB_TITLE_BASE64,
				JSValues.AJAX_PROBAND_LIST_ENTRY_COUNT, MessageCodes.PROBAND_LIST_TAB_TITLE, MessageCodes.PROBAND_LIST_TAB_TITLE_WITH_COUNT,
				new Long(probandListEntryModel.getRowCount()));
		if (operationSuccess && in.getTrialId() != null) {
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_TRIAL_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_TRIAL_JOURNAL_ENTRY_COUNT,
					MessageCodes.TRIAL_JOURNAL_TAB_TITLE, MessageCodes.TRIAL_JOURNAL_TAB_TITLE_WITH_COUNT,
					WebUtil.getJournalCount(JournalModule.TRIAL_JOURNAL, in.getTrialId()));
		}
		this.getProbandListEntryTagValueBean().appendRequestContextCallbackArgs(operationSuccess);
	}

	@Override
	protected void changeSpecific(Long id) {
		probandMultiPicker.clear();
		bulkAddGroup = null;
		shuffle = Settings.getBoolean(SettingCodes.PROBAND_LIST_BULK_ADD_SHUFFLE, Bundle.SETTINGS, DefaultSettings.PROBAND_LIST_BULK_ADD_SHUFFLE);
		randomize = Settings.getBoolean(SettingCodes.PROBAND_LIST_BULK_ADD_RANDOMIZE, Bundle.SETTINGS, DefaultSettings.PROBAND_LIST_BULK_ADD_RANDOMIZE);
		limit = Settings.getLongNullable(SettingCodes.PROBAND_LIST_BULK_ADD_LIMIT, Bundle.SETTINGS, DefaultSettings.PROBAND_LIST_BULK_ADD_LIMIT);
		bulkAddRating = Settings.getLongNullable(SettingCodes.PROBAND_LIST_ENTRY_RATING_PRESET, Bundle.SETTINGS, DefaultSettings.PROBAND_LIST_ENTRY_RATING_PRESET);
		bulkAddRatingMax = Settings.getLongNullable(SettingCodes.PROBAND_LIST_ENTRY_RATING_MAX_PRESET, Bundle.SETTINGS, DefaultSettings.PROBAND_LIST_ENTRY_RATING_MAX_PRESET);
		this.trialId = id;
	}

	private void clearCaches(boolean select) {
		if (!select) {
			probandTagValueCache.clear();
			probandContactDetailValueCache.clear();
			probandAddressCache.clear();
			probandStatusCache.clear();
		}
		probandListStatusCache.clear();
	}

	@Override
	protected ProbandListEntryModel createProbandListEntryModel() {
		return new ProbandListEntryModel(
				Settings.getBoolean(SettingCodes.PROBAND_LIST_SHOW_PROBAND_LIST_ENTRY_TAG_COLUMN, Bundle.SETTINGS, DefaultSettings.PROBAND_LIST_SHOW_PROBAND_LIST_ENTRY_TAG_COLUMN),
				Settings.getBoolean(SettingCodes.PROBAND_LIST_SHOW_INQUIRY_COLUMN, Bundle.SETTINGS, DefaultSettings.PROBAND_LIST_SHOW_INQUIRY_COLUMN), true);
	}

	public final void deleteBulk() {
		actionPostProcess(deleteBulkAction());
	}

	public String deleteBulkAction() {
		try {
			Set<Long> ids = this.probandMultiPicker.getSelectionIds();
			Iterator<ProbandListEntryOutVO> it = WebUtil.getServiceLocator().getTrialService()
					.deleteProbandListEntries(WebUtil.getAuthentication(), trialId, ids, shuffle, limit).iterator();
			while (it.hasNext()) {
				this.probandMultiPicker.removeId(it.next().getProband().getId());
			}
			int itemsLeft = probandMultiPicker.getSelection().size();
			if (itemsLeft > 0) {
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.BULK_DELETE_OPERATION_INCOMPLETE, ids.size() - itemsLeft, ids.size());
			} else {
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_INFO, MessageCodes.BULK_DELETE_OPERATION_SUCCESSFUL, ids.size(), ids.size());
			}
			probandListEntryModel.updateRowCount();
			return BULK_DELETE_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	public Long getBulkAddLimit() {
		return limit;
	}

	public Long getBulkAddRating() {
		return bulkAddRating;
	}

	public Long getBulkAddRatingMax() {
		return bulkAddRatingMax;
	}

	@Override
	protected String getDataTableId() {
		return "proband_list";
	}

	public boolean getEnableExports() {
		return Settings.getBoolean(SettingCodes.ENABLE_PROBAND_LIST_EXPORTS, Bundle.SETTINGS, DefaultSettings.ENABLE_PROBAND_LIST_EXPORTS);
	}

	@Override
	public String getMainTabTitle() {
		if (out != null) {
			return Messages.getMessage(MessageCodes.PROBAND_LIST_MAIN_TAB_LABEL, CommonUtil.probandOutVOToString(out.getProband()));
		} else {
			return Messages.getString(MessageCodes.PROBAND_LIST_MAIN_TAB_BLANK_LABEL);
		}
	}

	public Collection<IDVO> getProbandAddresses(ProbandOutVO proband) {
		if (proband != null) {
			if (!probandAddressCache.containsKey(proband.getId())) {
				Collection probandAddresses = null;
				try {
					probandAddresses = WebUtil.getServiceLocator().getProbandService().getProbandAddressList(WebUtil.getAuthentication(), proband.getId(), null);
				} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
				} catch (AuthenticationException e) {
					WebUtil.publishException(e);
				}
				if (probandAddresses == null) {
					probandAddresses = new ArrayList<IDVO>();
				}
				IDVO.transformVoCollection(probandAddresses);
				probandAddressCache.put(proband.getId(), probandAddresses);
				return probandAddresses;
			} else {
				return probandAddressCache.get(proband.getId());
			}
		}
		return new ArrayList<IDVO>();
	}

	public Collection<IDVO> getProbandContactDetailValues(ProbandOutVO proband) {
		if (proband != null) {
			if (!probandContactDetailValueCache.containsKey(proband.getId())) {
				Collection probandContactDetailValues = null;
				try {
					probandContactDetailValues = WebUtil.getServiceLocator().getProbandService()
							.getProbandContactDetailValueList(WebUtil.getAuthentication(), proband.getId(), false, null);
				} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
				} catch (AuthenticationException e) {
					WebUtil.publishException(e);
				}
				if (probandContactDetailValues == null) {
					probandContactDetailValues = new ArrayList<ProbandContactDetailValueOutVO>();
				}
				IDVO.transformVoCollection(probandContactDetailValues);
				probandContactDetailValueCache.put(proband.getId(), probandContactDetailValues);
				return probandContactDetailValues;
			} else {
				return probandContactDetailValueCache.get(proband.getId());
			}
		}
		return new ArrayList<IDVO>();
	}

	public StreamedContent getProbandListExcelStreamedContent() throws Exception {
		try {
			ProbandListExcelVO excel = WebUtil.getServiceLocator().getTrialService().exportProbandList(WebUtil.getAuthentication(), trialId, null);
			return new DefaultStreamedContent(new ByteArrayInputStream(excel.getDocumentDatas()), excel.getContentType().getMimeType(), excel.getFileName());
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
			throw e;
		} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
			throw e;
		}
	}

	public Collection<IDVO> getProbandListStatus(ProbandOutVO proband) {
		if (proband != null) {
			if (!probandListStatusCache.containsKey(proband.getId())) {
				Collection<ProbandListStatusEntryOutVO> probandListStatus = null;
				Collection probandListStatusFiltered = new ArrayList<ProbandListStatusEntryOutVO>();
				try {
					probandListStatus = WebUtil.getServiceLocator().getTrialService().getProbandListStatus(WebUtil.getAuthentication(), trialId, proband.getId(), false, null);
				} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
				} catch (AuthenticationException e) {
					WebUtil.publishException(e);
				}
				if (probandListStatus != null) {
					Iterator<ProbandListStatusEntryOutVO> it = probandListStatus.iterator();
					while (it.hasNext()) {
						ProbandListStatusEntryOutVO status = it.next();
						if (!status.isDecrypted() || !CommonUtil.isEmptyString(status.getReason())) {
							probandListStatusFiltered.add(status);
						}
					}
				}
				IDVO.transformVoCollection(probandListStatusFiltered);
				probandListStatusCache.put(proband.getId(), probandListStatusFiltered);
				return probandListStatusFiltered;
			} else {
				return probandListStatusCache.get(proband.getId());
			}
		}
		return new ArrayList<IDVO>();
	}

	public ProbandMultiPickerModel getProbandMultiPicker() {
		return probandMultiPicker;
	}

	public String getProbandName() {
		return WebUtil.probandIdToName(in.getProbandId());
	}

	public Collection<IDVO> getProbandStatus(ProbandOutVO proband) {
		if (proband != null) {
			if (!probandStatusCache.containsKey(proband.getId())) {
				Collection probandStatus = null;
				try {
					probandStatus = WebUtil.getServiceLocator().getProbandService().getProbandStatusEntryList(WebUtil.getAuthentication(), proband.getId(), null);
				} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
				} catch (AuthenticationException e) {
					WebUtil.publishException(e);
				}
				if (probandStatus == null) {
					probandStatus = new ArrayList<ProbandStatusEntryOutVO>();
				}
				IDVO.transformVoCollection(probandStatus);
				probandStatusCache.put(proband.getId(), probandStatus);
				return probandStatus;
			} else {
				return probandStatusCache.get(proband.getId());
			}
		}
		return new ArrayList<IDVO>();
	}

	public Collection<IDVO> getProbandTagValues(ProbandOutVO proband) {
		if (proband != null) {
			if (!probandTagValueCache.containsKey(proband.getId())) {
				Collection probandTagValues = null;
				try {
					probandTagValues = WebUtil.getServiceLocator().getProbandService().getProbandTagValueList(WebUtil.getAuthentication(), proband.getId(), null);
				} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
				} catch (AuthenticationException e) {
					WebUtil.publishException(e);
				}
				if (probandTagValues == null) {
					probandTagValues = new ArrayList<ProbandTagValueOutVO>();
				}
				IDVO.transformVoCollection(probandTagValues);
				probandTagValueCache.put(proband.getId(), probandTagValues);
				return probandTagValues;
			} else {
				return probandTagValueCache.get(proband.getId());
			}
		}
		return new ArrayList<IDVO>();
	}

	@Override
	public String getTitle() {
		if (out != null) {
			return Messages.getMessage(MessageCodes.PROBAND_LIST_ENTRY_TITLE, Long.toString(out.getId()), CommonUtil.probandOutVOToString(out.getProband()));
		} else {
			return Messages.getString(MessageCodes.CREATE_NEW_PROBAND_LIST_ENTRY);
		}
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
			trialId = in.getTrialId();
		} else {
			initProbandListEntryDefaultValues(in, trialId);
		}
	}

	@Override
	protected void initSpecificSets(boolean reset, boolean deleted, boolean select) {
		clearCaches(select);
		probandListEntryModel.setTrialId(in.getTrialId());
		probandListEntryModel.initSets(reset);
		trial = WebUtil.getTrial(this.in.getTrialId());
		proband = WebUtil.getProband(this.in.getProbandId(), null, null, null);
		if (isTrialLocked()) {
			Messages.addLocalizedMessageClientId("probandListEntryMessages", FacesMessage.SEVERITY_WARN, MessageCodes.TRIAL_LOCKED);
		} else if (isProbandLocked()) {
			Messages.addLocalizedMessageClientId("probandListEntryMessages", FacesMessage.SEVERITY_WARN, MessageCodes.PROBAND_LOCKED);
		}
	}

	public boolean isBulkAddRandomize() {
		return randomize;
	}

	public boolean isBulkAddShuffle() {
		return shuffle;
	}

	@Override
	public boolean isCreateable() {
		return (in.getTrialId() == null ? false : !isTrialLocked());
	}

	public boolean isProbandBulkCreateable() {
		return isCreateable() && probandMultiPicker.getIsEnabled();
	}

	public boolean isProbandBulkRemovable() {
		return isCreateable() && probandMultiPicker.getIsEnabled();
	}

	public void moveDown(Long probandListEntryId) {
		try {
			out = WebUtil.getServiceLocator().getTrialService().moveProbandListEntry(WebUtil.getAuthentication(), probandListEntryId, PositionMovement.DOWN);
			initIn();
			initSets();
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
	}

	public void moveFirst(Long probandListEntryId) {
		try {
			out = WebUtil.getServiceLocator().getTrialService().moveProbandListEntry(WebUtil.getAuthentication(), probandListEntryId, PositionMovement.FIRST);
			initIn();
			initSets();
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
	}

	public void moveLast(Long probandListEntryId) {
		try {
			out = WebUtil.getServiceLocator().getTrialService().moveProbandListEntry(WebUtil.getAuthentication(), probandListEntryId, PositionMovement.LAST);
			initIn();
			initSets();
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
	}

	public void moveTo() {
		Long probandListEntryId = WebUtil.getLongParamValue(GetParamNames.PROBAND_LIST_ENTRY_ID);
		Long targetPosition = WebUtil.getLongParamValue(GetParamNames.TARGET_POSITION);
		if (probandListEntryId != null && targetPosition != null) {
			try {
				Collection<ProbandListEntryOutVO> updated = WebUtil.getServiceLocator().getTrialService()
						.moveProbandListEntryTo(WebUtil.getAuthentication(), probandListEntryId, targetPosition);
				out = null;
				initIn();
				initSets();
				Messages.addLocalizedMessageClientId("probandListEntryMessages", FacesMessage.SEVERITY_INFO, MessageCodes.POSITIONS_UPDATE_SUCCESSFUL, updated.size());
			} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
				Messages.addMessageClientId("probandListEntryMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
			} catch (AuthenticationException e) {
				Messages.addMessageClientId("probandListEntryMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
				WebUtil.publishException(e);
			}
		}
	}

	public boolean isCreateProband() {
		return (trial != null ? !CommonUtil.isEmptyString(trial.getProbandAliasFormat()) : false);
	}

	public void moveUp(Long probandListEntryId) {
		try {
			out = WebUtil.getServiceLocator().getTrialService().moveProbandListEntry(WebUtil.getAuthentication(), probandListEntryId, PositionMovement.UP);
			initIn();
			initSets();
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
	}

	public final void normalizePositions() {
		actionPostProcess(normalizePositionsAction());
	}

	public String normalizePositionsAction() {
		try {
			Collection<ProbandListEntryOutVO> updated = WebUtil.getServiceLocator().getTrialService().normalizeProbandListEntryPositions(WebUtil.getAuthentication(), trialId);
			out = null;
			initIn();
			initSets();
			Messages.addLocalizedMessageClientId("probandListEntryMessages", FacesMessage.SEVERITY_INFO, MessageCodes.POSITIONS_UPDATE_SUCCESSFUL, updated.size());
			return UPDATE_OUTCOME;
		} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
			Messages.addMessageClientId("probandListEntryMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessageClientId("probandListEntryMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	public final void addCreateProbandRandomized() {
		actionPostProcess(addCreateProbandRandomizedAction());
	}

	public String addCreateProbandRandomizedAction() {
		return addAction(true, true);
	}

	public final void addCreateProband() {
		actionPostProcess(addCreateProbandAction());
	}

	public String addCreateProbandAction() {
		return addAction(false, true);
	}

	public void setBulkAddLimit(Long limit) {
		this.limit = limit;
	}

	public void setBulkAddRandomize(boolean randomize) {
		this.randomize = randomize;
	}

	public void setBulkAddRating(Long bulkAddRating) {
		this.bulkAddRating = bulkAddRating;
	}

	public void setBulkAddShuffle(boolean shuffle) {
		this.shuffle = shuffle;
	}

	public IDVO getBulkAddGroup() {
		if (bulkAddGroup != null) {
			return IDVO.transformVo(bulkAddGroup);
		}
		return null;
	}

	public void setBulkAddGroup(IDVO bulkAddGroup) {
		if (bulkAddGroup != null) {
			this.bulkAddGroup = (ProbandGroupOutVO) bulkAddGroup.getVo();
		} else {
			this.bulkAddGroup = null;
		}
	}

	public void handleBulkAddGroupSelect(SelectEvent event) {
	}

	public void handleBulkAddGroupUnselect(UnselectEvent event) {
	}
}
