package org.phoenixctms.ctsms.web.model.shared;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.ECRFFieldStatusQueue;
import org.phoenixctms.ctsms.enumeration.ECRFStatusAction;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.AuditTrailExcelVO;
import org.phoenixctms.ctsms.vo.ECRFFieldStatusQueueCountVO;
import org.phoenixctms.ctsms.vo.ECRFOutVO;
import org.phoenixctms.ctsms.vo.ECRFPDFVO;
import org.phoenixctms.ctsms.vo.ECRFProgressSummaryVO;
import org.phoenixctms.ctsms.vo.ECRFProgressVO;
import org.phoenixctms.ctsms.vo.ECRFSectionProgressVO;
import org.phoenixctms.ctsms.vo.ECRFStatusActionVO;
import org.phoenixctms.ctsms.vo.ECRFStatusEntryVO;
import org.phoenixctms.ctsms.vo.ECRFStatusTypeVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryOutVO;
import org.phoenixctms.ctsms.vo.SignatureVO;
import org.phoenixctms.ctsms.web.component.datatable.DataTable;
import org.phoenixctms.ctsms.web.conversion.EcrfSectionProgressItemValue;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.model.trial.EcrfLazyModel;
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

public abstract class EcrfStatusEntryBeanBase extends EcrfDataEntryBeanBase {

	private static ArrayList<SelectItem> mergeEcrfFilterSections(Collection<String> sectionStrings, ECRFProgressVO ecrfProgress, ECRFProgressVO sectionEcrfProgress) {
		ArrayList<SelectItem> filterSections;
		if (sectionStrings != null) {
			HashMap<String, ECRFSectionProgressVO> progressMap = null;
			if (ecrfProgress != null) {
				progressMap = new HashMap<String, ECRFSectionProgressVO>(ecrfProgress.getSections().size());
				Iterator<ECRFSectionProgressVO> it = ecrfProgress.getSections().iterator();
				while (it.hasNext()) {
					ECRFSectionProgressVO progress = it.next();
					progressMap.put(progress.getSection(), progress); // take last index
				}
				if (sectionEcrfProgress != null) {
					it = sectionEcrfProgress.getSections().iterator();
					while (it.hasNext()) {
						ECRFSectionProgressVO progress = it.next();
						progressMap.put(progress.getSection(), progress); // take last index
					}
				}
				ecrfProgress.getSections().clear();
			}
			filterSections = new ArrayList<SelectItem>(sectionStrings.size());
			Iterator<String> it = sectionStrings.iterator();
			while (it.hasNext()) {
				String section = it.next();
				if (progressMap != null) {
					ECRFSectionProgressVO progress = progressMap.get(section);
					filterSections.add(new SelectItem(new EcrfSectionProgressItemValue(progress), section));
					ecrfProgress.getSections().add(progress);
				} else {
					filterSections.add(new SelectItem(section, section));
				}
			}
		} else {
			filterSections = new ArrayList<SelectItem>();
		}
		if (ecrfProgress != null) {
			ECRFSectionProgressVO noSelectionSectionProgress = new EcrfSectionProgressItemValue();
			noSelectionSectionProgress.setSection(CommonUtil.NO_SELECTION_VALUE);
			noSelectionSectionProgress.setIndex(null);
			noSelectionSectionProgress.setSeries(false);
			if (sectionEcrfProgress != null) {
				noSelectionSectionProgress.setFieldCount(sectionEcrfProgress.getFieldCount());
				noSelectionSectionProgress.setSavedValueCount(sectionEcrfProgress.getSavedValueCount());
				noSelectionSectionProgress.setMandatoryFieldCount(sectionEcrfProgress.getMandatoryFieldCount());
				noSelectionSectionProgress.setMandatorySavedValueCount(sectionEcrfProgress.getMandatorySavedValueCount());
				noSelectionSectionProgress.setEcrfFieldStatusQueueCounts(sectionEcrfProgress.getEcrfFieldStatusQueueCounts());
			} else {
				noSelectionSectionProgress.setFieldCount(ecrfProgress.getFieldCount());
				noSelectionSectionProgress.setSavedValueCount(ecrfProgress.getSavedValueCount());
				noSelectionSectionProgress.setMandatoryFieldCount(ecrfProgress.getMandatoryFieldCount());
				noSelectionSectionProgress.setMandatorySavedValueCount(ecrfProgress.getMandatorySavedValueCount());
				noSelectionSectionProgress.setEcrfFieldStatusQueueCounts(ecrfProgress.getEcrfFieldStatusQueueCounts());
			}
			filterSections.add(0, new SelectItem(noSelectionSectionProgress, ""));
		} else {
			filterSections.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		}
		return filterSections;
	}

	private static Boolean statusTypeContainsAction(ECRFStatusTypeVO statusType, ECRFStatusAction statusAction) {
		if (statusType != null && statusAction != null) {
			Iterator<ECRFStatusActionVO> it = statusType.getActions().iterator();
			while (it.hasNext()) {
				ECRFStatusAction action = it.next().getAction();
				if (statusAction.equals(action)) {
					return true;
				}
			}
			return false;
		}
		return null;
	}

	private ArrayList<SelectItem> filterVisits;
	protected ProbandListEntryModel probandListEntryModel;
	protected EcrfLazyModel ecrfModel;
	private HashMap<Long, HashMap<Long, EcrfFieldValueAuditTrailLogEagerModel>> ecrfAuditTrailLogModelCache;
	private HashMap<ECRFFieldStatusQueue, HashMap<Long, HashMap<Long, EcrfFieldStatusEntryLogEagerModel>>> fieldStatusEntryLogModelCache;
	private Collection<ECRFStatusTypeVO> statusTypes;
	private Collection<ECRFStatusTypeVO> allStatusTypes;
	private ArrayList<SelectItem> filterSections;
	private ECRFProgressVO filterEcrfProgress;
	private SignatureVO signature;
	private HashMap<Long, HashMap<Long, ECRFStatusEntryVO>> ecrfStatusCache;
	private HashMap<Long, ECRFProgressSummaryVO> ecrfProgressSummaryCache;
	private HashMap<Long, HashMap<Long, ECRFProgressVO>> ecrfProgressCache;
	private String password;

	public EcrfStatusEntryBeanBase() {
		super();
		ecrfStatusCache = new HashMap<Long, HashMap<Long, ECRFStatusEntryVO>>();
		ecrfProgressSummaryCache = new HashMap<Long, ECRFProgressSummaryVO>();
		ecrfProgressCache = new HashMap<Long, HashMap<Long, ECRFProgressVO>>();
		filterEcrfProgress = null;
		probandListEntryModel = createProbandListEntryModel();
		ecrfModel = new EcrfLazyModel();
		ecrfAuditTrailLogModelCache = new HashMap<Long, HashMap<Long, EcrfFieldValueAuditTrailLogEagerModel>>();
		fieldStatusEntryLogModelCache = new HashMap<ECRFFieldStatusQueue, HashMap<Long, HashMap<Long, EcrfFieldStatusEntryLogEagerModel>>>();
		filterVisits = new ArrayList<SelectItem>();
		filterSections = new ArrayList<SelectItem>();
		statusTypes = new ArrayList<ECRFStatusTypeVO>();
		allStatusTypes = WebUtil.getEcrfStatusTypes();
		password = null;
	}

	@Override
	protected String changeAction(Long id) {
		DataTable.clearFilters(getProbandListEntryDataTableId());
		DataTable.clearFilters(getEcrfDataTableId());
		ecrf = null;
		probandListEntry = null;
		ecrfStatus = null;
		probandListEntryModel.clearSelectedColumns();
		ecrfModel.setProbandListEntryId(null);
		ecrfModel.setActive(true);
		ecrfModel.updateRowCount();
		changeSpecific(id);
		probandListEntryModel.resetRows();
		probandListEntryModel.updateRowCount();
		ecrfFieldValueBean.setFieldQuery(null);
		initSets(false);
		return CHANGE_OUTCOME;
	}

	protected abstract void changeSpecific(Long id);

	private void clearCaches() {
		ecrfStatusCache.clear();
		ecrfProgressSummaryCache.clear();
		ecrfProgressCache.clear();
		ecrfAuditTrailLogModelCache.clear();
		fieldStatusEntryLogModelCache.clear();
	}

	private void clearFromCache(ECRFOutVO ecrfVO, ProbandListEntryOutVO listEntryVO) {
		clearFromEcrfStatusCache(ecrfVO, listEntryVO);
		clearFromEcrfProgressSummaryCache(listEntryVO);
		clearFromEcrfProgressCache(ecrfVO, listEntryVO);
		clearFromEcrfAuditTrailLogModelCache(ecrfVO, listEntryVO);
		ECRFFieldStatusQueue[] queues = ECRFFieldStatusQueue.values();
		for (int i = 0; i < queues.length; i++) {
			clearFromFieldStatusEntryLogModelCache(queues[i], ecrfVO, listEntryVO);
		}
	}

	private EcrfFieldValueAuditTrailLogEagerModel clearFromEcrfAuditTrailLogModelCache(ECRFOutVO ecrfVO, ProbandListEntryOutVO listEntryVO) {
		if (listEntryVO != null && ecrfVO != null) {
			if (ecrfAuditTrailLogModelCache.containsKey(listEntryVO.getId())) {
				HashMap<Long, EcrfFieldValueAuditTrailLogEagerModel> ecrfMap = ecrfAuditTrailLogModelCache.get(listEntryVO.getId());
				if (ecrfMap != null && ecrfMap.containsKey(ecrfVO.getId())) {
					return ecrfMap.remove(ecrfVO.getId());
				}
			}
		}
		return null;
	}

	private ECRFProgressVO clearFromEcrfProgressCache(ECRFOutVO ecrfVO, ProbandListEntryOutVO listEntryVO) {
		if (ecrfVO != null && listEntryVO != null) {
			if (ecrfProgressCache.containsKey(listEntryVO.getId())) {
				HashMap<Long, ECRFProgressVO> listEntryEcrfProgressCache = ecrfProgressCache.get(listEntryVO.getId());
				if (listEntryEcrfProgressCache != null && listEntryEcrfProgressCache.containsKey(ecrfVO.getId())) {
					return listEntryEcrfProgressCache.remove(ecrfVO.getId());
				}
			}
		}
		return null;
	}

	private ECRFProgressSummaryVO clearFromEcrfProgressSummaryCache(ProbandListEntryOutVO listEntryVO) {
		if (listEntryVO != null) {
			if (ecrfProgressSummaryCache.containsKey(listEntryVO.getId())) {
				return ecrfProgressSummaryCache.remove(listEntryVO.getId());
			}
		}
		return null;
	}

	private ECRFStatusEntryVO clearFromEcrfStatusCache(ECRFOutVO ecrfVO, ProbandListEntryOutVO listEntryVO) {
		if (ecrfVO != null && listEntryVO != null) {
			if (ecrfStatusCache.containsKey(listEntryVO.getId())) {
				HashMap<Long, ECRFStatusEntryVO> listEntryEcrfStatusCache = ecrfStatusCache.get(listEntryVO.getId());
				if (listEntryEcrfStatusCache != null && listEntryEcrfStatusCache.containsKey(ecrfVO.getId())) {
					return listEntryEcrfStatusCache.remove(ecrfVO.getId());
				}
			}
		}
		return null;
	}

	private EcrfFieldStatusEntryLogEagerModel clearFromFieldStatusEntryLogModelCache(ECRFFieldStatusQueue queue, ECRFOutVO ecrfVO, ProbandListEntryOutVO listEntryVO) {
		if (queue != null && listEntryVO != null && ecrfVO != null) {
			if (fieldStatusEntryLogModelCache.containsKey(queue)) {
				HashMap<Long, HashMap<Long, EcrfFieldStatusEntryLogEagerModel>> listEntryMap = fieldStatusEntryLogModelCache.get(queue);
				if (listEntryMap != null && listEntryMap.containsKey(listEntryVO.getId())) {
					HashMap<Long, EcrfFieldStatusEntryLogEagerModel> ecrfMap = listEntryMap.get(listEntryVO.getId());
					if (ecrfMap != null && ecrfMap.containsKey(ecrfVO.getId())) {
						return ecrfMap.remove(ecrfVO.getId());
					}
				}
			}
		}
		return null;
	}

	public final void clearValues() {
		actionPostProcess(clearValuesAction());
	}

	public String clearValuesAction() {
		ecrfFieldValueBean.delete();
		initSpecificSets();
		password = null;
		clearFromCache(ecrf, probandListEntry);
		Long listEntryId = probandListEntry == null ? null : probandListEntry.getId();
		Long ecrfId = ecrf == null ? null : ecrf.getId();
		filterSections = getEcrfFilterSections(ecrfId, listEntryId, true);
		if (ecrfStatus == null) {
			ecrfStatus = WebUtil.getEcrfStatusEntry(ecrfId, listEntryId);
			statusTypes = loadEcrfStatusTypes();
			ecrfFieldValueBean.setEcrfStatus(ecrfStatus);
			if (ecrfStatus != null) {
				ecrf = ecrfStatus.getEcrf();
				ecrfFieldValueBean.setEcrf(ecrf);
				probandListEntry = ecrfStatus.getListEntry();
				ecrfFieldValueBean.setProbandListEntry(probandListEntry);
			}
		}
		signature = loadSignature();
		addMessages();
		return UPDATE_OUTCOME;
	}

	protected abstract ProbandListEntryModel createProbandListEntryModel();

	public Collection<ECRFStatusTypeVO> getAllStatusTypes() {
		return allStatusTypes;
	}

	public StreamedContent getAuditTrailExcelStreamedContent(ProbandListEntryOutVO listEntry) throws Exception {
		if (listEntry != null) {
			try {
				AuditTrailExcelVO auditTrailExcel = WebUtil.getServiceLocator().getTrialService().exportAuditTrail(WebUtil.getAuthentication(), null, listEntry.getId(), null);
				return new DefaultStreamedContent(new ByteArrayInputStream(auditTrailExcel.getDocumentDatas()), auditTrailExcel.getContentType().getMimeType(),
						auditTrailExcel.getFileName());
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
				throw e;
			} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
				throw e;
			}
		}
		return null;
	}

	private ECRFProgressVO getCachedEcrfProgress(ECRFOutVO ecrfVO, ProbandListEntryOutVO listEntryVO) {
		if (ecrfVO != null && listEntryVO != null) {
			HashMap<Long, ECRFProgressVO> listEntryEcrfProgressCache;
			if (ecrfProgressCache.containsKey(listEntryVO.getId())) {
				listEntryEcrfProgressCache = ecrfProgressCache.get(listEntryVO.getId());
			} else {
				listEntryEcrfProgressCache = new HashMap<Long, ECRFProgressVO>();
				ecrfProgressCache.put(listEntryVO.getId(), listEntryEcrfProgressCache);
			}
			ECRFProgressVO ecrfProgressVO;
			if (listEntryEcrfProgressCache.containsKey(ecrfVO.getId())) {
				ecrfProgressVO = listEntryEcrfProgressCache.get(ecrfVO.getId());
			} else {
				ecrfProgressVO = WebUtil.getEcrfProgress(ecrfVO.getId(), listEntryVO.getId(), false);
				listEntryEcrfProgressCache.put(ecrfVO.getId(), ecrfProgressVO);
			}
			return ecrfProgressVO;
		}
		return null;
	}

	private ECRFProgressSummaryVO getCachedEcrfProgressSummary(ProbandListEntryOutVO listEntryVO) {
		if (listEntryVO != null) {
			ECRFProgressSummaryVO ecrfProgressSummaryVO;
			if (ecrfProgressSummaryCache.containsKey(listEntryVO.getId())) {
				ecrfProgressSummaryVO = ecrfProgressSummaryCache.get(listEntryVO.getId());
			} else {
				ecrfProgressSummaryVO = WebUtil.getEcrfProgressSummary(listEntryVO.getId(), false, false);
				ecrfProgressSummaryCache.put(listEntryVO.getId(), ecrfProgressSummaryVO);
			}
			return ecrfProgressSummaryVO;
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

	private long getDoneEcrfCount() {
		if (probandListEntry != null) {
			try {
				return WebUtil.getServiceLocator().getTrialService().getEcrfStatusEntryCount(WebUtil.getAuthentication(), probandListEntry.getId(), null, null, null, true, null,
						null, null);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return 0l;
	}

	public EcrfFieldValueAuditTrailLogEagerModel getEcrfAuditTrailLogModel(ECRFOutVO ecrfVO) {
		return EcrfFieldValueAuditTrailLogEagerModel.getCachedAuditTrailLogModel(probandListEntry, ecrfVO, ecrfAuditTrailLogModelCache);
	}

	protected abstract String getEcrfDataTableId();

	public ECRFFieldStatusQueueCountVO getEcrfFieldStatusCount(ECRFOutVO ecrfVO, String queues) {
		ECRFProgressVO ecrfProgress = getCachedEcrfProgress(ecrfVO, probandListEntry);
		ECRFFieldStatusQueueCountVO result = null;
		if (ecrfProgress != null && ecrfProgress.getStatus() != null) {
			ArrayList<Enum<ECRFFieldStatusQueue>> queuesToInclude = WebUtil.getEnumList(queues, ECRFFieldStatusQueue.class);
			result = new ECRFFieldStatusQueueCountVO();
			Iterator<ECRFFieldStatusQueueCountVO> it = ecrfProgress.getEcrfFieldStatusQueueCounts().iterator();
			while (it.hasNext()) {
				ECRFFieldStatusQueueCountVO queueCount = it.next();
				if (queuesToInclude.contains(queueCount.getQueue())) {
					result.setInitial(result.getInitial() + queueCount.getInitial());
					result.setUpdated(result.getUpdated() + queueCount.getUpdated());
					result.setProposed(result.getProposed() + queueCount.getProposed());
					result.setResolved(result.getResolved() + queueCount.getResolved());
					result.setUnresolved(result.getUnresolved() + queueCount.getUnresolved());
				}
				result.setTotal(result.getTotal() + queueCount.getTotal());
			}
		}
		return result;
	}

	private ArrayList<SelectItem> getEcrfFilterSections(Long ecrfId, Long listEntryId, boolean update) {
		ECRFProgressVO sectionEcrfProgress = null;
		if (update && filterEcrfProgress != null) {
			if (!CommonUtil.isEmptyString(ecrfFieldValueBean.getFilterSection())) {
				try {
					sectionEcrfProgress = WebUtil.getServiceLocator().getTrialService().getEcrfProgress(WebUtil.getAuthentication(), listEntryId, ecrfId,
							ecrfFieldValueBean.getFilterSection());
				} catch (AuthenticationException e) {
					WebUtil.publishException(e);
				} catch (AuthorisationException e) {
				} catch (ServiceException e) {
				} catch (IllegalArgumentException e) {
				}
			} else {
				filterEcrfProgress = WebUtil.getEcrfProgress(ecrfId, listEntryId, true);
			}
		} else {
			filterEcrfProgress = WebUtil.getEcrfProgress(ecrfId, listEntryId, true);
		}
		Collection<String> sectionStrings = null;
		if (filterEcrfProgress != null && filterEcrfProgress.getSections().size() > 0) {
			sectionStrings = new ArrayList<String>(filterEcrfProgress.getSections().size());
			Iterator<ECRFSectionProgressVO> it = filterEcrfProgress.getSections().iterator();
			while (it.hasNext()) {
				sectionStrings.add(it.next().getSection());
			}
		}
		return mergeEcrfFilterSections(sectionStrings, filterEcrfProgress, sectionEcrfProgress);
	}

	public EcrfLazyModel getEcrfModel() {
		return ecrfModel;
	}

	public String getEcrfPdfButtonLabel(boolean blank) {
		if (blank) {
			return Messages.getMessage(MessageCodes.BLANK_ECRF_PDF_BUTTON_LABEL, ecrf != null ? ecrf.getName() : null);
		} else {
			return Messages.getMessage(MessageCodes.ECRF_PDF_BUTTON_LABEL, ecrf != null ? ecrf.getName() : null);
		}
	}

	public StreamedContent getEcrfPdfStreamedContent(ProbandListEntryOutVO listEntry, boolean blank) throws Exception {
		if (listEntry != null) {
			try {
				ECRFPDFVO ecrfPdf = WebUtil.getServiceLocator().getTrialService().renderEcrfs(WebUtil.getAuthentication(), null, listEntry.getId(), null, blank);
				return new DefaultStreamedContent(new ByteArrayInputStream(ecrfPdf.getDocumentDatas()), ecrfPdf.getContentType().getMimeType(), ecrfPdf.getFileName());
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
				throw e;
			} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
				throw e;
			}
		}
		return null;
	}

	public ECRFProgressVO getEcrfProgress(ECRFOutVO ecrfVO) {
		return getCachedEcrfProgress(ecrfVO, probandListEntry);
	}

	public String getEcrfProgressLabel(ECRFOutVO ecrfVO) {
		ECRFProgressVO ecrfProgress = getCachedEcrfProgress(ecrfVO, probandListEntry);
		if (ecrfProgress != null && ecrfProgress.getFieldCount() > 0l) {
			return Messages.getMessage(MessageCodes.ECRF_PROGRESS_LABEL, ecrfProgress.getSavedValueCount(), ecrfProgress.getFieldCount());
		}
		return null;
	}

	public ECRFProgressSummaryVO getEcrfProgressSummary(ProbandListEntryOutVO listEntryVO) {
		return getCachedEcrfProgressSummary(listEntryVO);
	}

	public String getEcrfProgressSummaryLabel(ProbandListEntryOutVO listEntryVO) {
		ECRFProgressSummaryVO progressSummary = getCachedEcrfProgressSummary(listEntryVO);
		if (progressSummary != null && progressSummary.getEcrfTotalCount() > 0l) {
			return Messages.getMessage(MessageCodes.ECRF_PROGRESS_SUMMARY_LABEL, progressSummary.getEcrfDoneCount(), progressSummary.getEcrfTotalCount());
		}
		return null;
	}

	public int getEcrfProgressSummaryValue(ProbandListEntryOutVO listEntryVO) {
		ECRFProgressSummaryVO progressSummary = getCachedEcrfProgressSummary(listEntryVO);
		if (progressSummary != null && progressSummary.getEcrfTotalCount() > 0l) {
			return Math.round(((float) Settings.getInt(SettingCodes.PROGRESS_BAR_MAX_VALUE, Bundle.SETTINGS, DefaultSettings.PROGRESS_BAR_MAX_VALUE) * progressSummary
					.getEcrfDoneCount()) / (progressSummary.getEcrfTotalCount()));
		}
		return 0;
	}

	public int getEcrfProgressValue(ECRFOutVO ecrfVO) {
		ECRFProgressVO ecrfProgress = getCachedEcrfProgress(ecrfVO, probandListEntry);
		if (ecrfProgress != null && ecrfProgress.getFieldCount() > 0l) {
			return Math.round(((float) Settings.getInt(SettingCodes.PROGRESS_BAR_MAX_VALUE, Bundle.SETTINGS, DefaultSettings.PROGRESS_BAR_MAX_VALUE) * ecrfProgress
					.getSavedValueCount()) / (ecrfProgress.getFieldCount()));
		}
		return 0;
	}

	public String getEcrfSectionProgressLabel(ECRFSectionProgressVO sectionProgress) {
		if (sectionProgress != null) {
			if (sectionProgress.getSeries()) {
				return Messages.getMessage(MessageCodes.ECRF_SECTION_PROGRESS_SERIES_LABEL, sectionProgress.getIndex() != null ? sectionProgress.getIndex().longValue() : 0l);
			} else if (sectionProgress.getFieldCount() > 0l) {
				return Messages.getMessage(MessageCodes.ECRF_SECTION_PROGRESS_LABEL, sectionProgress.getSavedValueCount(), sectionProgress.getFieldCount());
			}
		}
		return "";
	}

	public int getEcrfSectionProgressValue(ECRFSectionProgressVO sectionProgress) {
		if (sectionProgress != null) {
			if (sectionProgress.getSeries()) {
				return sectionProgress.getIndex() != null ? Settings.getInt(SettingCodes.PROGRESS_BAR_MAX_VALUE, Bundle.SETTINGS, DefaultSettings.PROGRESS_BAR_MAX_VALUE) : 0;
			} else if (sectionProgress.getFieldCount() > 0l) {
				return Math.round(((float) Settings.getInt(SettingCodes.PROGRESS_BAR_MAX_VALUE, Bundle.SETTINGS, DefaultSettings.PROGRESS_BAR_MAX_VALUE) * sectionProgress
						.getSavedValueCount()) / (sectionProgress.getFieldCount()));
			}
		}
		return 0;
	}

	public ECRFStatusEntryVO getEcrfStatusEntry(ECRFOutVO ecrfVO) {
		return getCachedEcrfStatusEntry(ecrfVO, probandListEntry);
	}

	public String getEcrfStatusTypeButtonLabel(ECRFStatusTypeVO statusType) {
		return Messages.getMessage(MessageCodes.NO_ECRF_STATUS_TYPE_BUTTON_LABEL, getEcrfStatusTypeLabel(statusType));
	}

	public boolean getEcrfStatusTypeConfirmationRequired(ECRFStatusTypeVO statusType) {
		return Boolean.TRUE.equals(statusTypeContainsAction(statusType, ECRFStatusAction.CLEAR_VALUES));
	}

	public boolean getEcrfStatusTypePasswordRequired(ECRFStatusTypeVO statusType) {
		if (Settings.getBoolean(SettingCodes.ECRF_STATUS_UPDATE_REQUIRES_PASSWORD, Bundle.SETTINGS,
				DefaultSettings.ECRF_STATUS_UPDATE_REQUIRES_PASSWORD)) {
			return Boolean.TRUE.equals(statusTypeContainsAction(statusType, ECRFStatusAction.SIGN_ECRF));
		} else {
			return false;
		}
	}

	public EcrfFieldStatusEntryLogEagerModel getFieldStatusEntryLogModel(ECRFFieldStatusQueue queue, ECRFOutVO ecrfVO) {
		return EcrfFieldStatusEntryLogEagerModel.getCachedFieldStatusEntryLogModel(queue, probandListEntry, ecrfVO, fieldStatusEntryLogModelCache);
	}

	public String getFieldStatusEntryLogTabTitle(ECRFFieldStatusQueue queue, ECRFOutVO ecrfVO) {
		return WebUtil.getTabTitleString(MessageCodes.ECRF_FIELD_STATUS_ENTRY_TAB_TITLE, MessageCodes.ECRF_FIELD_STATUS_ENTRY_TAB_TITLE_WITH_COUNT,
				new Long(EcrfFieldStatusEntryLogEagerModel.getCachedFieldStatusEntryLogModel(queue, probandListEntry, ecrfVO, fieldStatusEntryLogModelCache).getAllRowCount()),
				WebUtil.getEcrfFieldStatusQueueName(queue));
	}

	public abstract ArrayList<SelectItem> getFilterProbandGroups();

	public ArrayList<SelectItem> getFilterSections() {
		return filterSections;
	}

	public ArrayList<SelectItem> getFilterVisits() {
		return filterVisits;
	}

	public String getPassword() {
		return password;
	}

	protected abstract String getProbandListEntryDataTableId();

	public ProbandListEntryModel getProbandListEntryModel() {
		return probandListEntryModel;
	}

	public ECRFFieldStatusQueueCountVO getSectionFieldStatusCount(ECRFSectionProgressVO sectionProgress, String queues) {
		ECRFFieldStatusQueueCountVO result = null;
		if (sectionProgress != null) {
			ArrayList<Enum<ECRFFieldStatusQueue>> queuesToInclude = WebUtil.getEnumList(queues, ECRFFieldStatusQueue.class);
			result = new ECRFFieldStatusQueueCountVO();
			Iterator<ECRFFieldStatusQueueCountVO> it = sectionProgress.getEcrfFieldStatusQueueCounts().iterator();
			while (it.hasNext()) {
				ECRFFieldStatusQueueCountVO queueCount = it.next();
				if (queuesToInclude.contains(queueCount.getQueue())) {
					result.setInitial(result.getInitial() + queueCount.getInitial());
					result.setUpdated(result.getUpdated() + queueCount.getUpdated());
					result.setProposed(result.getProposed() + queueCount.getProposed());
					result.setResolved(result.getResolved() + queueCount.getResolved());
					result.setUnresolved(result.getUnresolved() + queueCount.getUnresolved());
				}
				result.setTotal(result.getTotal() + queueCount.getTotal());
			}
			if (result.getTotal() == 0l) {
				result = null;
			}
		}
		return result;
	}

	public IDVO getSelectedEcrf() {
		if (ecrf != null) {
			return IDVO.transformVo(ecrf);
		} else {
			return null;
		}
	}

	public IDVO getSelectedProbandListEntry() {
		if (probandListEntry != null) {
			return IDVO.transformVo(probandListEntry);
		} else {
			return null;
		}
	}

	public SignatureVO getSignature() {
		return signature;
	}

	public Collection<ECRFStatusTypeVO> getStatusTypes() {
		return statusTypes;
	}

	public ECRFFieldStatusQueueCountVO getSummaryFieldStatusCount(ProbandListEntryOutVO listEntryVO, String queues) {
		ECRFProgressSummaryVO progressSummary = getCachedEcrfProgressSummary(listEntryVO);
		ECRFFieldStatusQueueCountVO result = null;
		if (progressSummary != null && progressSummary.getEcrfStatusEntryCount() > 0l) {
			ArrayList<Enum<ECRFFieldStatusQueue>> queuesToInclude = WebUtil.getEnumList(queues, ECRFFieldStatusQueue.class);
			result = new ECRFFieldStatusQueueCountVO();
			Iterator<ECRFFieldStatusQueueCountVO> it = progressSummary.getEcrfFieldStatusQueueCounts().iterator();
			while (it.hasNext()) {
				ECRFFieldStatusQueueCountVO queueCount = it.next();
				if (queuesToInclude.contains(queueCount.getQueue())) {
					result.setInitial(result.getInitial() + queueCount.getInitial());
					result.setUpdated(result.getUpdated() + queueCount.getUpdated());
					result.setProposed(result.getProposed() + queueCount.getProposed());
					result.setResolved(result.getResolved() + queueCount.getResolved());
					result.setUnresolved(result.getUnresolved() + queueCount.getUnresolved());
				}
				result.setTotal(result.getTotal() + queueCount.getTotal());
			}
		}
		return result;
	}

	public HashMap<Long, Long> getSummaryStatusCountMap(ProbandListEntryOutVO listEntryVO) {
		ECRFProgressSummaryVO progressSummary = getCachedEcrfProgressSummary(listEntryVO);
		if (progressSummary != null && progressSummary.getEcrfStatusEntryCount() > 0l) {
			Iterator<ECRFProgressVO> it = progressSummary.getEcrfs().iterator();
			HashMap<Long, Long> countMap = new HashMap<Long, Long>();
			while (it.hasNext()) {
				ECRFStatusTypeVO status = it.next().getStatus();
				Long id = (status != null ? status.getId() : null);
				if (id != null) {
					Long count;
					if (countMap.containsKey(id)) {
						count = countMap.get(id);
					} else {
						count = 0l;
					}
					countMap.put(id, count + 1l);
				}
			}
			return countMap;
		}
		return null;
	}

	private long getVerfiedEcrfCount() {
		if (probandListEntry != null) {
			try {
				return WebUtil.getServiceLocator().getTrialService().getEcrfStatusEntryCount(WebUtil.getAuthentication(), probandListEntry.getId(), null, null, null, null, null,
						null, true);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return 0l;
	}

	public void handleFilterSectionChange() {
		ecrfFieldValueBean.changeRootEntity(ecrf == null ? null : ecrf.getId());
		addMessages();
	}

	protected void initSets(boolean setFirstSection) {
		initSpecificSets();
		password = null;
		clearCaches();
		Long listEntryId = probandListEntry == null ? null : probandListEntry.getId();
		Long ecrfId = ecrf == null ? null : ecrf.getId();
		ecrfStatus = WebUtil.getEcrfStatusEntry(ecrfId, listEntryId);
		statusTypes = loadEcrfStatusTypes();
		ecrfFieldValueBean.setEcrfStatus(ecrfStatus);
		if (ecrfStatus != null) {
			ecrf = ecrfStatus.getEcrf();
			probandListEntry = ecrfStatus.getListEntry();
		}
		ecrfFieldValueBean.setProbandListEntry(probandListEntry);
		ecrfFieldValueBean.setFilterSectionProgress(null);
		ecrfFieldValueBean.setFilterIndex(null);
		filterSections = getEcrfFilterSections(ecrfId, listEntryId, false);
		if (setFirstSection) {
			setFirstFilterSection();
		}
		ecrfFieldValueBean.changeRootEntity(ecrfId);
		signature = loadSignature();
		addMessages();
	}

	protected abstract void initSpecificSets();

	public boolean isShowEcrfLog() {
		return Settings.getBoolean(SettingCodes.SHOW_ECRF_STATUS_ECRF_LOG, Bundle.SETTINGS, DefaultSettings.SHOW_ECRF_STATUS_ECRF_LOG);
	}

	public boolean isSignEcrfsVisible() {
		return getVerfiedEcrfCount() > 0l || getDoneEcrfCount() > 0l;
	}

	public boolean isSignVerifiedEcrfsEnabled() {
		return getVerfiedEcrfCount() > 0l;
	}

	@Override
	public String loadAction() {
		initSpecificSets();
		password = null;
		clearFromCache(ecrf, probandListEntry);
		Long listEntryId = probandListEntry == null ? null : probandListEntry.getId();
		Long ecrfId = ecrf == null ? null : ecrf.getId();
		filterSections = getEcrfFilterSections(ecrfId, listEntryId, false);
		ecrfStatus = WebUtil.getEcrfStatusEntry(ecrfId, listEntryId);
		statusTypes = loadEcrfStatusTypes();
		ecrfFieldValueBean.setEcrfStatus(ecrfStatus);
		if (ecrfStatus != null) {
			ecrf = ecrfStatus.getEcrf();
			ecrfFieldValueBean.setEcrf(ecrf);
			probandListEntry = ecrfStatus.getListEntry();
			ecrfFieldValueBean.setProbandListEntry(probandListEntry);
		}
		ecrfFieldValueBean.load();
		signature = loadSignature();
		addMessages();
		return LOAD_OUTCOME;
	}

	private Collection<ECRFStatusTypeVO> loadEcrfStatusTypes() {
		try {
			if (ecrfStatus != null) {
				return WebUtil.getServiceLocator().getSelectionSetService()
						.getEcrfStatusTypeTransitions(WebUtil.getAuthentication(), ecrfStatus.getStatus().getId());
			} else {
				return WebUtil.getServiceLocator().getSelectionSetService().getInitialEcrfStatusTypes(WebUtil.getAuthentication());
			}
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
		return new ArrayList<ECRFStatusTypeVO>();
	}

	private SignatureVO loadSignature() {
		if (ecrf != null && probandListEntry != null) {
			try {
				return WebUtil.getServiceLocator().getTrialService().getEcrfSignature(WebUtil.getAuthentication(), ecrf.getId(), probandListEntry.getId());
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return null;
	}

	public void refreshEcrfProgress() {
		clearFromCache(ecrf, probandListEntry);
		Long listEntryId = probandListEntry == null ? null : probandListEntry.getId();
		Long ecrfId = ecrf == null ? null : ecrf.getId();
		filterSections = getEcrfFilterSections(ecrfId, listEntryId, true);
		appendRequestContextCallbackArgs(true);
	}

	private void setFirstFilterSection() {
		if (filterSections != null && filterSections.size() > 0) {
			Iterator<SelectItem> it = filterSections.iterator();
			while (it.hasNext()) {
				Object value = it.next().getValue();
				if (value instanceof ECRFSectionProgressVO) {
					if (!CommonUtil.isEmptyString(((ECRFSectionProgressVO) value).getSection())) {
						ecrfFieldValueBean.setFilterSectionProgress((ECRFSectionProgressVO) value);
						return;
					}
				} else if (value instanceof String) {
					if (!CommonUtil.isEmptyString((String) value)) {
						ecrfFieldValueBean.setFilterSection((String) value);
						return;
					}
				}
			}
		}
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setSelectedEcrf(IDVO ecrf) {
		if (ecrf != null) {
			this.ecrf = (ECRFOutVO) ecrf.getVo();
			ecrfFieldValueBean.setFieldQuery(null);
			initSets(true);
		}
	}

	public void setSelectedProbandListEntry(IDVO probandListEntry) {
		DataTable.clearFilters(getEcrfDataTableId());
		if (probandListEntry != null) {
			this.probandListEntry = (ProbandListEntryOutVO) probandListEntry.getVo();
		} else {
			this.probandListEntry = null;
		}
		ecrf = null;
		if (this.probandListEntry != null) {
			ecrfModel.setProbandListEntryId(probandListEntry.getId());
			filterVisits = WebUtil.getVisits(this.probandListEntry.getTrial().getId());
			filterVisits.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		} else {
			ecrfModel.setProbandListEntryId(null);
			filterVisits.clear();
		}
		ecrfModel.updateRowCount();
		ecrfFieldValueBean.setFieldQuery(null);
		initSets(false);
	}

	public void signAllEcrfs() {
		actionPostProcess(signAllEcrfsAction());
	}

	public String signAllEcrfsAction() {
		return signEcrfsAction(true);
	}

	private String signEcrfsAction(boolean signAll) {
		try {
			WebUtil.testPassword(password);
			Long listEntryId = probandListEntry == null ? null : probandListEntry.getId();
			Long ecrfId = ecrf == null ? null : ecrf.getId();
			int signed = 0;
			if (listEntryId != null) {
				Collection<ECRFStatusEntryVO> ecrfStatusEntries = WebUtil.getServiceLocator().getTrialService()
						.signVerifiedEcrfs(WebUtil.getAuthentication(), null, listEntryId, signAll);
				signed = ecrfStatusEntries.size();
				if (signed > 0) {
					initSpecificSets();
					Iterator<ECRFStatusEntryVO> it = ecrfStatusEntries.iterator();
					while (it.hasNext()) {
						ECRFStatusEntryVO updatedEcrfStatus = it.next();
						if (listEntryId != null && updatedEcrfStatus.getListEntry().getId() == listEntryId && ecrfId != null && updatedEcrfStatus.getEcrf().getId() == ecrfId) {
							ecrfStatus = updatedEcrfStatus;
						}
						clearFromCache(updatedEcrfStatus.getEcrf(), updatedEcrfStatus.getListEntry());
					}
					if (ecrfStatus != null) {
						statusTypes = loadEcrfStatusTypes();
						ecrfFieldValueBean.setEcrfStatus(ecrfStatus);
						ecrf = ecrfStatus.getEcrf();
						ecrfFieldValueBean.setEcrf(ecrf);
						probandListEntry = ecrfStatus.getListEntry();
						listEntryId = probandListEntry == null ? null : probandListEntry.getId();
						ecrfId = ecrf == null ? null : ecrf.getId();
						filterSections = getEcrfFilterSections(ecrfId, listEntryId, false);
						ecrfFieldValueBean.setProbandListEntry(probandListEntry);
						ecrfFieldValueBean.getPaginator().setToFirstPage();
						ecrfFieldValueBean.getPaginator().updatePSF();
						ecrfFieldValueBean.load();
						signature = loadSignature();
					}
					addMessages();
				}
			}
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_INFO, signed > 0 ? MessageCodes.BULK_UPDATE_OPERATION_SUCCESSFUL : MessageCodes.BULK_UPDATE_OPERATION_INCOMPLETE,
					signed);
			return BULK_UPDATE_OUTCOME;
		} catch (ServiceException e) {
			ecrfFieldValueBean.setInputModelErrorMsgs(e.getData());
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} finally {
			password = null;
		}
		return ERROR_OUTCOME;
	}

	public void signVerifiedEcrfs() {
		actionPostProcess(signVerifiedEcrfsAction());
	}

	public String signVerifiedEcrfsAction() {
		return signEcrfsAction(false);
	}

	@Override
	public String updateAction() {
		ecrfFieldValueBean.update();
		initSpecificSets();
		password = null;
		clearFromCache(ecrf, probandListEntry);
		Long listEntryId = probandListEntry == null ? null : probandListEntry.getId();
		Long ecrfId = ecrf == null ? null : ecrf.getId();
		// slow?:
		// filterSections = WebUtil.getEcrfFilterSections(null, ecrfId, WebUtil.getEcrfProgress(ecrfId, listEntryId, true)); // getCachedEcrfProgress(ecrf, probandListEntry));
		filterSections = getEcrfFilterSections(ecrfId, listEntryId, true);
		if (ecrfStatus == null) {
			ecrfStatus = WebUtil.getEcrfStatusEntry(ecrfId, listEntryId);
			statusTypes = loadEcrfStatusTypes();
			ecrfFieldValueBean.setEcrfStatus(ecrfStatus);
			if (ecrfStatus != null) {
				ecrf = ecrfStatus.getEcrf();
				ecrfFieldValueBean.setEcrf(ecrf);
				probandListEntry = ecrfStatus.getListEntry();
				ecrfFieldValueBean.setProbandListEntry(probandListEntry);
			}
		}
		signature = loadSignature();
		addMessages();
		return UPDATE_OUTCOME;
	}

	public void updateEcrfStatus() {
		actionPostProcess(updateEcrfStatusAction());
	}

	public String updateEcrfStatusAction() {
		Long ecrfStatusTypeId = WebUtil.getLongParamValue(GetParamNames.ECRF_STATUS_TYPE_ID);
		try {
			if (getEcrfStatusTypePasswordRequired(WebUtil.getEcrfStatusType(ecrfStatusTypeId))) {
				WebUtil.testPassword(password);
			}
			Long listEntryId = probandListEntry == null ? null : probandListEntry.getId();
			Long ecrfId = ecrf == null ? null : ecrf.getId();
			ecrfStatus = WebUtil.getServiceLocator().getTrialService()
					.setEcrfStatusEntry(WebUtil.getAuthentication(), ecrfId,
							listEntryId, ecrfStatus == null ? 0l : ecrfStatus.getVersion(), ecrfStatusTypeId, null);
			initSpecificSets();
			clearFromCache(ecrf, probandListEntry);
			statusTypes = loadEcrfStatusTypes();
			ecrfFieldValueBean.setEcrfStatus(ecrfStatus);
			ecrf = ecrfStatus.getEcrf();
			ecrfFieldValueBean.setEcrf(ecrf);
			probandListEntry = ecrfStatus.getListEntry();
			listEntryId = probandListEntry == null ? null : probandListEntry.getId();
			ecrfId = ecrf == null ? null : ecrf.getId();
			filterSections = getEcrfFilterSections(ecrfId, listEntryId, false);
			ecrfFieldValueBean.setProbandListEntry(probandListEntry);
			ecrfFieldValueBean.getPaginator().setToFirstPage();
			ecrfFieldValueBean.getPaginator().updatePSF();
			ecrfFieldValueBean.load();
			signature = loadSignature();
			addMessages();
			addOperationSuccessMessage(MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
			return UPDATE_OUTCOME;
		} catch (ServiceException e) {
			ecrfFieldValueBean.setInputModelErrorMsgs(e.getData());
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} finally {
			password = null;
		}
		return ERROR_OUTCOME;
	}

	@Override
	public String updateSectionAction(EcrfFieldSection section) {
		ecrfFieldValueBean.updateSection(section);
		initSpecificSets();
		password = null;
		clearFromCache(ecrf, probandListEntry);
		Long listEntryId = probandListEntry == null ? null : probandListEntry.getId();
		Long ecrfId = ecrf == null ? null : ecrf.getId();
		filterSections = getEcrfFilterSections(ecrfId, listEntryId, true);
		if (ecrfStatus == null) {
			ecrfStatus = WebUtil.getEcrfStatusEntry(ecrfId, listEntryId);
			statusTypes = loadEcrfStatusTypes();
			ecrfFieldValueBean.setEcrfStatus(ecrfStatus);
			if (ecrfStatus != null) {
				ecrf = ecrfStatus.getEcrf();
				ecrfFieldValueBean.setEcrf(ecrf);
				probandListEntry = ecrfStatus.getListEntry();
				ecrfFieldValueBean.setProbandListEntry(probandListEntry);
			}
		}
		signature = loadSignature();
		addMessages();
		return UPDATE_OUTCOME;
	}

	public void verifySignature() {
		try {
			signature = WebUtil.getServiceLocator().getTrialService()
					.verifyEcrfSignature(WebUtil.getAuthentication(), ecrf == null ? null : ecrf.getId(), probandListEntry == null ? null : probandListEntry.getId());
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
	}
}
