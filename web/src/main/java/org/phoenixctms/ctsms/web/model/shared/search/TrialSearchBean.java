package org.phoenixctms.ctsms.web.model.shared.search;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.phoenixctms.ctsms.enumeration.DBModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.CriterionInVO;
import org.phoenixctms.ctsms.vo.SearchResultExcelVO;
import org.phoenixctms.ctsms.vo.TeamMemberOutVO;
import org.phoenixctms.ctsms.vo.TimelineEventOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.vo.TrialTagValueOutVO;
import org.phoenixctms.ctsms.web.component.datatable.DataTable;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.GetParamNames;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean
@ViewScoped
public class TrialSearchBean extends SearchBeanBase {

	private TrialSearchResultLazyModel trialResultModel;
	private HashMap<Long, Collection<IDVO>> trialTagValueCache;
	private HashMap<Long, Collection<IDVO>> teamMemberCache;
	private HashMap<Long, Collection<IDVO>> timelineEventCache;

	public TrialSearchBean() {
		super();
		trialTagValueCache = new HashMap<Long, Collection<IDVO>>();
		teamMemberCache = new HashMap<Long, Collection<IDVO>>();
		timelineEventCache = new HashMap<Long, Collection<IDVO>>();
		trialResultModel = new TrialSearchResultLazyModel();
	}

	private void clearCaches() {
		trialTagValueCache.clear();
		teamMemberCache.clear();
		timelineEventCache.clear();
	}

	@Override
	protected String getCriteriaCommentPreset() {
		return Messages.getString(MessageCodes.TRIAL_CRITERIA_COMMENT_PRESET);
	}

	@Override
	protected String getCriteriaLabelPreset() {
		return Messages.getString(MessageCodes.TRIAL_CRITERIA_LABEL_PRESET);
	}

	@Override
	protected String getCurrentPageIds() {
		return this.trialResultModel.getCurrentPageIds();
	}

	@Override
	protected DBModule getDBModule() {
		return DBModule.TRIAL_DB;
	}

	// public String getDutySelfAllocationLockedString(TrialOutVO trial) {
	// if (trial != null && trial.getDutySelfAllocationLocked()) {
	// if (trial.getDutySelfAllocationLockedUntil() != null && trial.getDutySelfAllocationLockedFrom() != null) {
	// return Messages
	// .getMessage(MessageCodes.DUTY_SELF_ALLOCATION_LOCKED_UNTIL_FROM_LABEL, DateUtil.getDateTimeFormat().format(trial.getDutySelfAllocationLockedUntil()),
	// DateUtil.getDateTimeFormat().format(trial.getDutySelfAllocationLockedFrom()));
	// } else if (trial.getDutySelfAllocationLockedUntil() != null) {
	// return Messages.getMessage(MessageCodes.DUTY_SELF_ALLOCATION_LOCKED_UNTIL_LABEL, DateUtil.getDateTimeFormat().format(trial.getDutySelfAllocationLockedUntil()));
	// } else if (trial.getDutySelfAllocationLockedFrom() != null) {
	// return Messages.getMessage(MessageCodes.DUTY_SELF_ALLOCATION_LOCKED_FROM_LABEL, DateUtil.getDateTimeFormat().format(trial.getDutySelfAllocationLockedFrom()));
	// }
	// }
	// return "";
	// }
	public boolean getEnableExports() {
		return Settings.getBoolean(SettingCodes.ENABLE_TRIAL_SEARCH_EXPORTS, Bundle.SETTINGS, DefaultSettings.ENABLE_TRIAL_SEARCH_EXPORTS);
	}

	public StreamedContent getExcelStreamedContent() throws Exception {
		clearCaches();
		try {
			SearchResultExcelVO excel = WebUtil.getServiceLocator().getSearchService()
					.exportTrial(WebUtil.getAuthentication(), criteriaIn, new HashSet<CriterionInVO>(criterionsIn), null);
			return new DefaultStreamedContent(new ByteArrayInputStream(excel.getDocumentDatas()), excel.getContentType().getMimeType(), excel.getFileName());
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
			throw e;
		} catch (AuthorisationException|ServiceException|IllegalArgumentException e) {
			throw e;
		}
	}

	public String getExclusiveProbandsString(TrialOutVO trial) {
		if (trial != null && trial.getExclusiveProbands() && trial.getBlockingPeriod() != null) {
			return Messages.getMessage(MessageCodes.EXCLUSIVE_PROBANDS_LABEL, WebUtil.variablePeriodToString(trial.getBlockingPeriod(), trial.getBlockingPeriodDays()));
		}
		return "";
	}

	public Long getProbandListEntryCount(TrialOutVO trial) {
		if (trial != null) {
			return WebUtil.getProbandListEntryCount(trial.getId(), null, true);
		}
		return null;
	}

	@Override
	public String getQueryResultTitle() {
		return getQueryResultTitle(trialResultModel.getRowCount());
	}

	@Override
	protected String getResultItemLabel() {
		return Messages.getString(MessageCodes.SEARCH_RESULT_TRIAL_ITEM_LABEL);
	}

	public String getSetPickerIDJSCall(TrialOutVO trial) {
		return getSetPickerIDJSCall(trial == null ? null : trial.getId(), WebUtil.clipStringPicker(WebUtil.trialOutVOToString(trial)));
	}

	public Collection<IDVO> getTeamMembers(TrialOutVO trial) {
		if (trial != null) {
			if (!teamMemberCache.containsKey(trial.getId())) {
				Collection teamMembers = null;
				try {
					teamMembers = WebUtil.getServiceLocator().getTrialService().getTeamMemberList(WebUtil.getAuthentication(), trial.getId(), null, null, null);
				} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
				} catch (AuthenticationException e) {
					WebUtil.publishException(e);
				}
				if (teamMembers == null) {
					teamMembers = new ArrayList<TeamMemberOutVO>();
				}
				IDVO.transformVoCollection(teamMembers);
				teamMemberCache.put(trial.getId(), teamMembers);
				return teamMembers;
			} else {
				return teamMemberCache.get(trial.getId());
			}
		}
		return new ArrayList<IDVO>();
	}

	public Collection<IDVO> getTimelineEvents(TrialOutVO trial) {
		if (trial != null) {
			if (!timelineEventCache.containsKey(trial.getId())) {
				Collection timelineEvents = null;
				try {
					timelineEvents = WebUtil.getServiceLocator().getTrialService().getTimelineEventList(WebUtil.getAuthentication(), trial.getId(), null);
				} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
				} catch (AuthenticationException e) {
					WebUtil.publishException(e);
				}
				if (timelineEvents == null) {
					timelineEvents = new ArrayList<TimelineEventOutVO>();
				}
				IDVO.transformVoCollection(timelineEvents);
				timelineEventCache.put(trial.getId(), timelineEvents);
				return timelineEvents;
			} else {
				return timelineEventCache.get(trial.getId());
			}
		}
		return new ArrayList<IDVO>();
	}

	@Override
	public String getTitle(boolean operationSuccess) {
		return getTitle(MessageCodes.TRIAL_CRITERIA_TITLE, MessageCodes.CREATE_NEW_TRIAL_CRITERIA, operationSuccess);
	}

	public TrialSearchResultLazyModel getTrialResultModel() {
		return trialResultModel;
	}

	public Collection<IDVO> getTrialTagValues(TrialOutVO trial) {
		if (trial != null) {
			if (!trialTagValueCache.containsKey(trial.getId())) {
				Collection trialTagValues = null;
				try {
					trialTagValues = WebUtil.getServiceLocator().getTrialService().getTrialTagValueList(WebUtil.getAuthentication(), trial.getId(), null);
				} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
				} catch (AuthenticationException e) {
					WebUtil.publishException(e);
				}
				if (trialTagValues == null) {
					trialTagValues = new ArrayList<TrialTagValueOutVO>();
				}
				IDVO.transformVoCollection(trialTagValues);
				trialTagValueCache.put(trial.getId(), trialTagValues);
				return trialTagValues;
			} else {
				return trialTagValueCache.get(trial.getId());
			}
		}
		return new ArrayList<IDVO>();
	}

	@PostConstruct
	private void init() {
		initPickTarget();
		Long id = WebUtil.getLongParamValue(GetParamNames.CRITERIA_ID);
		if (id != null) {
			this.load(id);
		} else {
			loadDefault();
		}
	}

	@Override
	protected void initSpecificSets() {
		clearCaches();
	}

	@Override
	public boolean isMarkUnEncrypted() {
		return true;
	}

	@Override
	public String searchAction() {
		clearCaches();
		trialResultModel.setCriteriaIn(criteriaIn);
		trialResultModel.setCriterionsIn(getNewCriterions());
		updateInstantCriteria(true);
		trialResultModel.updateRowCount();
		DataTable.clearFilters(getResultListId());
		return SEARCH_OUTCOME;
	}

	public String trialToColor(TrialOutVO trial) {
		return (trial != null ? WebUtil.colorToStyleClass(trial.getStatus().getColor()) : "");
	}
}
