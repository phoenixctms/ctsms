package org.phoenixctms.ctsms.web.model.trial;

import java.util.ArrayList;
import java.util.HashMap;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.ECRFFieldStatusQueue;
import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.ECRFFieldStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.ECRFOutVO;
import org.phoenixctms.ctsms.vo.ECRFStatusEntryVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.model.shared.EcrfFieldStatusEntryBeanBase;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
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
public class EcrfFieldStatusBean extends EcrfFieldStatusEntryBeanBase {

	private Long trialId;
	private ECRFFieldStatusQueue queue;
	private EcrfFieldStatusEntryLogLazyModel ecrfFieldStatusEntryModel;
	private HashMap<Long, EcrfFieldValueAuditTrailEagerModel> ecrfFieldAuditTrailLogModelCache;
	private HashMap<Long, EcrfFieldStatusEntryEagerModel> fieldStatusEntryLogModelCache;
	private ArrayList<SelectItem> filterVisits;
	private ArrayList<SelectItem> filterProbandGroups;
	private HashMap<Long, HashMap<Long, ECRFStatusEntryVO>> ecrfStatusCache;
	private TrialOutVO trial;

	public EcrfFieldStatusBean() {
		super();
		ecrfFieldStatusEntryModel = new EcrfFieldStatusEntryLogLazyModel();
		ecrfFieldAuditTrailLogModelCache = new HashMap<Long, EcrfFieldValueAuditTrailEagerModel>();
		fieldStatusEntryLogModelCache = new HashMap<Long, EcrfFieldStatusEntryEagerModel>();
		ecrfStatusCache = new HashMap<Long, HashMap<Long, ECRFStatusEntryVO>>();
		filterVisits = new ArrayList<SelectItem>();
		filterProbandGroups = new ArrayList<SelectItem>();
	}

	@Override
	protected void addMessages() {
		super.addMessages();
		if (getQueue() == null) {
			Messages.addLocalizedMessageClientId(getMessagesId(), FacesMessage.SEVERITY_INFO,
					MessageCodes.SELECT_ECRF_FIELD_STATUS_ENTRY);
		}
	}

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
		RequestContext requestContext = RequestContext.getCurrentInstance();
		if (requestContext != null) {
			requestContext.addCallbackParam(JSValues.AJAX_OPERATION_SUCCESS.toString(), operationSuccess);
			if (operationSuccess && trialId != null) {
				// WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_VISIT_SCHEDULE_TAB_TITLE_BASE64, JSValues.AJAX_VISIT_SCHEDULE_ITEM_COUNT,
				// MessageCodes.VISIT_SCHEDULE_TAB_TITLE, MessageCodes.VISIT_SCHEDULE_TAB_TITLE_WITH_COUNT, WebUtil.getVisitScheduleItemCount(in.getTrialId(), null));
				WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext,
						JSValues.AJAX_ECRF_FIELD_STATUS_TAB_TITLE_BASE64,
						JSValues.AJAX_ECRF_FIELD_STATUS_COUNT,
						MessageCodes.ECRF_FIELD_STATUS_TAB_TITLE, MessageCodes.ECRF_FIELD_STATUS_TAB_TITLE_WITH_COUNT, new Long(ecrfFieldStatusEntryModel.getRowCount()));
				WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_TRIAL_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_TRIAL_JOURNAL_ENTRY_COUNT,
						MessageCodes.TRIAL_JOURNAL_TAB_TITLE, MessageCodes.TRIAL_JOURNAL_TAB_TITLE_WITH_COUNT,
						WebUtil.getJournalCount(JournalModule.TRIAL_JOURNAL, trialId));
			}
		}
	}

	@Override
	protected String changeAction(Long id) {
		LazyDataModelBase.clearFilters("ecrffieldstatus_list");
		out = null;
		this.trialId = id;
		queue = null;
		listEntryId = null;
		ecrfFieldId = null;
		index = null;
		filterVisits = WebUtil.getVisits(id);
		filterVisits.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		filterProbandGroups = WebUtil.getProbandGroups(id);
		filterProbandGroups.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		trial = WebUtil.getTrial(id);
		clearCaches();
		initIn();
		initSets();
		return CHANGE_OUTCOME;
	}

	private void clearCaches() {
		ecrfFieldAuditTrailLogModelCache.clear();
		fieldStatusEntryLogModelCache.clear();
		ecrfStatusCache.clear();
	}

	@Override
	protected void clearFromCache(ECRFFieldStatusEntryOutVO status) {
		clearFromEcrfFieldAuditTrailModelCache(status);
		clearFromFieldStatusEntryModelCache(status);
	}

	private EcrfFieldValueAuditTrailEagerModel clearFromEcrfFieldAuditTrailModelCache(ECRFFieldStatusEntryOutVO statusVO) {
		if (statusVO != null) {
			if (ecrfFieldAuditTrailLogModelCache.containsKey(statusVO.getId())) {
				return ecrfFieldAuditTrailLogModelCache.remove(statusVO.getId());
			}
		}
		return null;
	}

	private EcrfFieldStatusEntryEagerModel clearFromFieldStatusEntryModelCache(ECRFFieldStatusEntryOutVO statusVO) {
		if (statusVO != null) {
			if (fieldStatusEntryLogModelCache.containsKey(statusVO.getId())) {
				return fieldStatusEntryLogModelCache.remove(statusVO.getId());
			}
		}
		return null;
	}

	private ECRFStatusEntryVO getCachedEcrfStatusEntry(ECRFOutVO ecrfVO, ProbandListEntryOutVO listEntryVO) {
		if (ecrfVO != null && listEntryVO != null) {
			HashMap<Long, ECRFStatusEntryVO> listEntryEcrfStatusCache;
			if (ecrfStatusCache.containsKey(listEntryVO.getId())) {
				listEntryEcrfStatusCache = ecrfStatusCache.get(listEntryVO.getId());
			} else {
				listEntryEcrfStatusCache = new HashMap<Long, ECRFStatusEntryVO>();
				ecrfStatusCache.put(listEntryVO.getId(), listEntryEcrfStatusCache);
			}
			ECRFStatusEntryVO statusEntryVO;
			if (listEntryEcrfStatusCache.containsKey(ecrfVO.getId())) {
				statusEntryVO = listEntryEcrfStatusCache.get(ecrfVO.getId());
			} else {
				statusEntryVO = WebUtil.getEcrfStatusEntry(ecrfVO.getId(), listEntryVO.getId());
				listEntryEcrfStatusCache.put(ecrfVO.getId(), statusEntryVO);
			}
			return statusEntryVO;
		}
		return null;
	}

	public EcrfFieldValueAuditTrailEagerModel getEcrfFieldAuditTrailLogModel(ECRFFieldStatusEntryOutVO statusVO) {
		return EcrfFieldValueAuditTrailEagerModel.getCachedAuditTrailModel(statusVO, ecrfFieldAuditTrailLogModelCache);
	}

	// protected String getDataTableId() {
	// return "ecrffieldstatus_list";
	// }
	public EcrfFieldStatusEntryLogLazyModel getEcrfFieldStatusEntryModel() {
		return ecrfFieldStatusEntryModel;
	}

	public ECRFStatusEntryVO getEcrfStatusEntry(ECRFFieldStatusEntryOutVO statusVO) {
		return getCachedEcrfStatusEntry(statusVO != null ? statusVO.getEcrfField().getEcrf() : null, statusVO != null ? statusVO.getListEntry() : null);
	}

	public EcrfFieldStatusEntryEagerModel getFieldStatusEntryLogModel(ECRFFieldStatusEntryOutVO statusVO) {
		return EcrfFieldStatusEntryEagerModel.getCachedFieldStatusEntryModel(statusVO, fieldStatusEntryLogModelCache);
	}

	public String getFieldStatusEntryLogTabTitle(ECRFFieldStatusEntryOutVO statusVO) {
		if (statusVO != null) {
			return WebUtil.getTabTitleString(MessageCodes.ECRF_FIELD_STATUS_ENTRY_TAB_TITLE, MessageCodes.ECRF_FIELD_STATUS_ENTRY_TAB_TITLE_WITH_COUNT,
					new Long(EcrfFieldStatusEntryEagerModel.getCachedFieldStatusEntryModel(statusVO, fieldStatusEntryLogModelCache).getAllRowCount()),
					WebUtil.getEcrfFieldStatusQueueName(statusVO.getStatus().getQueue()));
		}
		return "";
		//return Messages.getMessage(MessageCodes.ECRF_FIELD_STATUS_ENTRY_TAB_TITLE, getQueueName(), ecrfFieldStatusEntryModel.getRowCount());
	}

	public ArrayList<SelectItem> getFilterProbandGroups() {
		return filterProbandGroups;
	}

	public ArrayList<SelectItem> getFilterVisits() {
		return filterVisits;
	}

	@Override
	protected String getMessagesId() {
		return "ecrffieldstatus_messages";
	}

	@Override
	public ECRFFieldStatusQueue getQueue() {
		return queue;
	}

	@Override
	public String getTitle() {
		if (getQueue() != null) {
			if (out != null) {
				return Messages.getMessage(MessageCodes.ECRF_FIELD_STATUS_ENTRY_TITLE, getQueueName(), Long.toString(out.getId()), out.getStatus().getName());
			} else if (ecrfField != null && probandListEntry != null) {
				return Messages.getMessage(MessageCodes.CREATE_NEW_ECRF_FIELD_STATUS, getQueueName(), CommonUtil.probandOutVOToString(probandListEntry.getProband()),
						ecrfField.getUniqueName());
			} else {
				return Messages.getMessage(MessageCodes.CREATE_NEW_ECRF_FIELD_STATUS_ENTRY, getQueueName());
			}
		}
		return "";
	}

	public TrialOutVO getTrial() {
		return trial;
	}

	@Override
	protected void initSpecificSets() {
		if (out != null) {
			queue = out.getStatus().getQueue();
		}
		ecrfFieldStatusEntryModel.setTrialId(trialId);
		ecrfFieldStatusEntryModel.setQueue(Settings.getEcrfFieldStatusQueue(SettingCodes.ECRF_FIELD_STATUS_QUEUE, Bundle.SETTINGS,
				DefaultSettings.ECRF_FIELD_STATUS_QUEUE));
		ecrfFieldStatusEntryModel.updateRowCount();
	}

	public void refresh() {
		clearCaches();
		ecrfFieldStatusEntryModel.updateRowCount();
		//LazyDataModelBase.clearFilters("useractivity_list");
	}
}
