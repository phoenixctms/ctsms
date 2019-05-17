package org.phoenixctms.ctsms.web.model.proband;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.ProbandListStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.vo.VisitOutVO;
import org.phoenixctms.ctsms.vo.VisitScheduleExcelVO;
import org.phoenixctms.ctsms.vo.VisitScheduleItemOutVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.model.shared.CollidingProbandStatusEntryEagerModel;
import org.phoenixctms.ctsms.web.model.shared.VisitScheduleItemLazyModel;
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
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean
@ViewScoped
public class ProbandVisitScheduleBean extends ManagedBeanBase {

	private static ProbandListStatusEntryOutVO getProbandListStatusEntry(Long probandId, Long visitScheduleItemId) {
		if (probandId != null && visitScheduleItemId != null) {
			try {
				return WebUtil.getServiceLocator().getTrialService().getProbandListStatusEntryAtVisitScheduleItem(WebUtil.getAuthentication(), probandId, visitScheduleItemId);
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return null;
	}

	protected ProbandOutVO proband;
	private Long probandId;
	private ArrayList<SelectItem> filterTrials;
	private ArrayList<SelectItem> trials;
	private VisitScheduleItemLazyModel visitScheduleItemModel;
	private HashMap<Long, CollidingProbandStatusEntryEagerModel> collidingProbandStatusEntryModelCache;
	private HashMap<Long, HashMap<Long, ProbandListStatusEntryOutVO>> statusEntryCache;
	private AddVisitScheduleItemReimbursementBean addReimbursementBean;

	public ProbandVisitScheduleBean() {
		super();
		collidingProbandStatusEntryModelCache = new HashMap<Long, CollidingProbandStatusEntryEagerModel>();
		statusEntryCache = new HashMap<Long, HashMap<Long, ProbandListStatusEntryOutVO>>();
		visitScheduleItemModel = new VisitScheduleItemLazyModel();
		addReimbursementBean = new AddVisitScheduleItemReimbursementBean();
	}

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
		RequestContext requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_PROBAND_VISIT_SCHEDULE_TAB_TITLE_BASE64,
				JSValues.AJAX_PROBAND_VISIT_SCHEDULE_ITEM_COUNT,
				MessageCodes.PROBAND_VISIT_SCHEDULE_TAB_TITLE, MessageCodes.PROBAND_VISIT_SCHEDULE_TAB_TITLE_WITH_COUNT, new Long(visitScheduleItemModel.getRowCount()));
		//		if (operationSuccess && in.getProbandId() != null) {
		//			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_PROBAND_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_PROBAND_JOURNAL_ENTRY_COUNT,
		//					MessageCodes.PROBAND_JOURNAL_TAB_TITLE, MessageCodes.PROBAND_JOURNAL_TAB_TITLE_WITH_COUNT,
		//					WebUtil.getJournalCount(JournalModule.PROBAND_JOURNAL, in.getProbandId()));
		//		}
	}

	@Override
	protected String changeAction(Long id) {
		LazyDataModelBase.clearFilters("probandvisitschedule_list");
		this.probandId = id;
		initIn();
		initSets();
		return CHANGE_OUTCOME;
	}

	public void changeAddReimbursement() {
		Long visitScheduleItemId = WebUtil.getLongParamValue(GetParamNames.VISIT_SCHEDULE_ITEM_ID);
		addReimbursementBean.setProbandId(probandId);
		addReimbursementBean.changeRootEntity(visitScheduleItemId);
	}

	public AddVisitScheduleItemReimbursementBean getAddReimbursementBean() {
		return addReimbursementBean;
	}

	private ProbandListStatusEntryOutVO getCachedProbandListStatusEntry(VisitScheduleItemOutVO visitScheduleItem) {
		if (probandId != null && visitScheduleItem != null) {
			HashMap<Long, ProbandListStatusEntryOutVO> entryCache;
			ProbandListStatusEntryOutVO statusEntry;
			if (statusEntryCache.containsKey(probandId)) {
				entryCache = statusEntryCache.get(probandId);
				if (entryCache.containsKey(visitScheduleItem.getId())) {
					statusEntry = entryCache.get(visitScheduleItem.getId());
				} else {
					statusEntry = getProbandListStatusEntry(probandId, visitScheduleItem.getId());
					entryCache.put(visitScheduleItem.getId(), statusEntry);
				}
			} else {
				entryCache = new HashMap<Long, ProbandListStatusEntryOutVO>();
				statusEntryCache.put(probandId, entryCache);
				statusEntry = getProbandListStatusEntry(probandId, visitScheduleItem.getId());
				entryCache.put(visitScheduleItem.getId(), statusEntry);
			}
			return statusEntry;
		}
		return null;
	}

	public CollidingProbandStatusEntryEagerModel getCollidingProbandStatusEntryModel(VisitScheduleItemOutVO visitScheduleItem) {
		CollidingProbandStatusEntryEagerModel collidingProbandStatusEntryModel = CollidingProbandStatusEntryEagerModel.getCachedCollidingProbandStatusEntryModel(visitScheduleItem,
				true, collidingProbandStatusEntryModelCache);
		collidingProbandStatusEntryModel.setProbandId(probandId);
		return collidingProbandStatusEntryModel;
	}

	public ArrayList<SelectItem> getFilterTrials() {
		return filterTrials;
	}

	public ProbandListStatusEntryOutVO getProbandListStatusEntry(VisitScheduleItemOutVO visitScheduleItem) {
		return getCachedProbandListStatusEntry(visitScheduleItem);
	}

	public String getTravelExpensesVisitScheduleExcelButtonLabel(SelectItem trial) {
		return Messages.getMessage(MessageCodes.TRAVEL_EXPENSES_VISIT_SCHEDULE_LABEL, trial.getLabel());
	}

	public StreamedContent getTravelExpensesVisitScheduleExcelStreamedContent(Long trialId) throws Exception {
		try {
			VisitScheduleExcelVO excel = WebUtil.getServiceLocator().getTrialService().exportVisitSchedule(WebUtil.getAuthentication(), trialId, probandId);
			return new DefaultStreamedContent(new ByteArrayInputStream(excel.getDocumentDatas()), excel.getContentType().getMimeType(), excel.getFileName());
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
			throw e;
		} catch (AuthorisationException|ServiceException|IllegalArgumentException e) {
			throw e;
		}
	}

	public ArrayList<SelectItem> getTrials() {
		return trials;
	}

	public String getVisitScheduleExcelButtonLabel() {
		return getVisitScheduleExcelButtonLabel(null);
	}

	public String getVisitScheduleExcelButtonLabel(SelectItem trial) {
		if (trial != null) {
			return Messages.getMessage(MessageCodes.PROBAND_VISIT_SCHEDULE_TRIAL_LABEL, trial.getLabel());
		} else {
			return Messages.getString(MessageCodes.PROBAND_VISIT_SCHEDULE_ALL_TRIALS_LABEL);
		}
	}

	public StreamedContent getVisitScheduleExcelStreamedContent() throws Exception {
		return getVisitScheduleExcelStreamedContent(null);
	}

	public StreamedContent getVisitScheduleExcelStreamedContent(Long trialId) throws Exception {
		try {
			VisitScheduleExcelVO excel = WebUtil.getServiceLocator().getProbandService().exportVisitSchedule(WebUtil.getAuthentication(), probandId, trialId);
			return new DefaultStreamedContent(new ByteArrayInputStream(excel.getDocumentDatas()), excel.getContentType().getMimeType(), excel.getFileName());
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
			throw e;
		} catch (AuthorisationException|ServiceException|IllegalArgumentException e) {
			throw e;
		}
	}

	public VisitScheduleItemLazyModel getVisitScheduleItemModel() {
		return visitScheduleItemModel;
	}

	@PostConstruct
	private void init() {
		initIn();
		initSets();
	}

	private void initIn() {
	}

	private void initSets() {
		//addReimbursementBean.setProbandId(probandId);
		collidingProbandStatusEntryModelCache.clear();
		statusEntryCache.clear();
		visitScheduleItemModel.setProbandId(probandId);
		visitScheduleItemModel.updateRowCount();
		trials = WebUtil.getParticipationTrials(probandId);
		filterTrials = new ArrayList<SelectItem>(trials);
		filterTrials.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		proband = WebUtil.getProband(probandId, null, null, null);
	}

	@Override
	public boolean isCreateable() {
		return false;
	}

	@Override
	public boolean isCreated() {
		return false;
	}

	public boolean isReimbursementCreateable(VisitScheduleItemOutVO visitScheduleItem) {
		if (proband == null || visitScheduleItem == null) {
			return false;
		} else {
			// ProbandListStatusEntryOutVO listStatusEntry = getCachedProbandListStatusEntry(visitScheduleItem);
			// if (listStatusEntry != null) {
			// if (!listStatusEntry.getStatus().isCount()) {
			// return false;
			// }
			// }
			return !WebUtil.isTrialLocked(visitScheduleItem.getTrial()) && !WebUtil.isProbandLocked(proband) && WebUtil.isProbandPerson(proband);
		}
	}

	@Override
	public String loadAction() {
		initSets();
		LazyDataModelBase.clearFilters("probandvisitschedule_list");
		return LOAD_OUTCOME;
	}

	public String visitScheduleItemToColor(VisitScheduleItemOutVO visitScheduleItem) {
		if (visitScheduleItem != null) {
			ProbandListStatusEntryOutVO listStatusEntry = getCachedProbandListStatusEntry(visitScheduleItem);
			if (listStatusEntry != null) {
				if (!listStatusEntry.getStatus().isCount()) {
					return WebUtil.colorToStyleClass(Settings.getColor(SettingCodes.ENROLLMENT_STATUS_IS_NOT_COUNT_COLOR, Bundle.SETTINGS,
							DefaultSettings.ENROLLMENT_STATUS_IS_NOT_COUNT_COLOR));
				}
				VisitOutVO visit = visitScheduleItem.getVisit();
				if (visit != null) {
					return WebUtil.colorToStyleClass(visit.getType().getColor());
				}
			}
		}
		return "";
	}
}
