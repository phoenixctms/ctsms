package org.phoenixctms.ctsms.web.model.trial;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.ECRFFieldStatusQueue;
import org.phoenixctms.ctsms.vo.ECRFFieldStatusQueueCountVO;
import org.phoenixctms.ctsms.vo.ECRFProgressSummaryVO;
import org.phoenixctms.ctsms.vo.ECRFProgressVO;
import org.phoenixctms.ctsms.vo.ECRFStatusTypeVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.TrialECRFProgressSummaryVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;

@ManagedBean
@ViewScoped
public class EcrfProgressOverviewBean extends ManagedBeanBase {

	private ArrayList<SelectItem> departments;
	private EcrfProgressSummaryLazyModel ecrfProgressSummaryModel;
	private HashMap<Long, TrialECRFProgressSummaryVO> trialEcrfProgressSummaryCache;
	private Collection<ECRFStatusTypeVO> allStatusTypes;
	private Date start;
	private Date stop;
	private Long probandDepartmentId;

	public EcrfProgressOverviewBean() {
		super();
		trialEcrfProgressSummaryCache = new HashMap<Long, TrialECRFProgressSummaryVO>();
		ecrfProgressSummaryModel = new EcrfProgressSummaryLazyModel();
	}

	public Collection<ECRFStatusTypeVO> getAllStatusTypes() {
		return allStatusTypes;
	}

	private TrialECRFProgressSummaryVO getCachedEcrfProgressSummary(TrialOutVO trialVO) {
		if (trialVO != null) {
			TrialECRFProgressSummaryVO trialECRFProgressSummaryVO;
			if (trialEcrfProgressSummaryCache.containsKey(trialVO.getId())) {
				trialECRFProgressSummaryVO = trialEcrfProgressSummaryCache.get(trialVO.getId());
			} else {
				trialECRFProgressSummaryVO = WebUtil.getTrialEcrfProgressSummary(trialVO.getId(), probandDepartmentId, start, stop);
				trialEcrfProgressSummaryCache.put(trialVO.getId(), trialECRFProgressSummaryVO);
			}
			return trialECRFProgressSummaryVO;
		}
		return null;
	}

	public ArrayList<SelectItem> getDepartments() {
		return departments;
	}

	public TrialECRFProgressSummaryVO getEcrfProgressSummary(TrialOutVO trialVO) {
		return getCachedEcrfProgressSummary(trialVO);
	}

	public String getEcrfProgressSummaryLabel(TrialOutVO trialVO) {
		TrialECRFProgressSummaryVO trialProgressSummary = getCachedEcrfProgressSummary(trialVO);
		if (trialProgressSummary != null && trialProgressSummary.getEcrfTotalCount() > 0l) {
			return Messages.getMessage(MessageCodes.ECRF_PROGRESS_SUMMARY_LABEL, trialProgressSummary.getEcrfDoneCount(), trialProgressSummary.getEcrfTotalCount());
		}
		return null;
	}

	// public ArrayList<SelectItem> getFilterCostTypes() {
	// return filterCostTypes;
	// }
	//
	// public Collection<PaymentMethodVO> getPaymentMethods() {
	// return paymentMethods;
	// }
	public EcrfProgressSummaryLazyModel getEcrfProgressSummaryModel() {
		return ecrfProgressSummaryModel;
	}

	public String getEcrfProgressSummaryOnTimeLabel(TrialOutVO trialVO) {
		TrialECRFProgressSummaryVO trialProgressSummary = getCachedEcrfProgressSummary(trialVO);
		if (trialProgressSummary != null && trialProgressSummary.getEcrfDoneCount() > 0l) {
			return Messages.getMessage(MessageCodes.ECRF_PROGRESS_SUMMARY_LABEL, trialProgressSummary.getEcrfDoneCount() - trialProgressSummary.getEcrfOverdueCount(),
					trialProgressSummary.getEcrfDoneCount());
		}
		return null;
	}

	public int getEcrfProgressSummaryOnTimeValue(TrialOutVO trialVO) {
		TrialECRFProgressSummaryVO trialProgressSummary = getCachedEcrfProgressSummary(trialVO);
		// if (progressSummary != null && progressSummary.getEcrfDoneCount() > 0l) {
		if (trialProgressSummary != null && trialProgressSummary.getEcrfDoneCount() > 0l) {
			return Math.round(((float) Settings.getInt(SettingCodes.PROGRESS_BAR_MAX_VALUE, Bundle.SETTINGS, DefaultSettings.PROGRESS_BAR_MAX_VALUE)
					* (trialProgressSummary.getEcrfDoneCount() - trialProgressSummary.getEcrfOverdueCount())) / (trialProgressSummary.getEcrfDoneCount()));
		}
		return 0;
	}

	public int getEcrfProgressSummaryValue(TrialOutVO trialVO) {
		TrialECRFProgressSummaryVO trialProgressSummary = getCachedEcrfProgressSummary(trialVO);
		// if (progressSummary != null && progressSummary.getEcrfDoneCount() > 0l) {
		if (trialProgressSummary != null && trialProgressSummary.getEcrfTotalCount() > 0l) {
			return Math.round(((float) Settings.getInt(SettingCodes.PROGRESS_BAR_MAX_VALUE, Bundle.SETTINGS, DefaultSettings.PROGRESS_BAR_MAX_VALUE) * trialProgressSummary
					.getEcrfDoneCount()) / (trialProgressSummary.getEcrfTotalCount()));
		}
		return 0;
	}

	public Long getProbandDepartmentId() {
		return probandDepartmentId;
	}

	public Date getStart() {
		return start;
	}

	public Date getStop() {
		return stop;
	}

	public ECRFFieldStatusQueueCountVO getSummaryFieldStatusCount(TrialOutVO trialVO, String queues) {
		TrialECRFProgressSummaryVO trialProgressSummary = getCachedEcrfProgressSummary(trialVO);
		ECRFFieldStatusQueueCountVO result = null;
		if (trialProgressSummary != null && trialProgressSummary.getEcrfStatusEntryCount() > 0l) {
			ArrayList<Enum<ECRFFieldStatusQueue>> queuesToInclude = WebUtil.getEnumList(queues, ECRFFieldStatusQueue.class);
			result = new ECRFFieldStatusQueueCountVO();
			Iterator<ECRFFieldStatusQueueCountVO> it = trialProgressSummary.getEcrfFieldStatusQueueCounts().iterator();
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

	public HashMap<Long, Long> getSummaryStatusCountMap(TrialOutVO trialVO) {
		TrialECRFProgressSummaryVO trialProgressSummary = getCachedEcrfProgressSummary(trialVO);
		if (trialProgressSummary != null && trialProgressSummary.getEcrfStatusEntryCount() > 0l) {
			Iterator<ECRFProgressSummaryVO> listEntryIt = trialProgressSummary.getListEntries().iterator();
			HashMap<Long, Long> countMap = new HashMap<Long, Long>();
			while (listEntryIt.hasNext()) {
				Iterator<ECRFProgressVO> ecrfIt = listEntryIt.next().getEcrfs().iterator();
				while (ecrfIt.hasNext()) {
					ECRFStatusTypeVO status = ecrfIt.next().getStatus();
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
			}
			return countMap;
		}
		return null;
	}

	public void handleDepartmentStartStopChange() {
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
		trialEcrfProgressSummaryCache.clear();
		if (departments == null) {
			StaffOutVO user = WebUtil.getUserIdentity();
			departments = WebUtil.getVisibleDepartments(user == null ? null : user.getDepartment().getId());
		}
		allStatusTypes = WebUtil.getEcrfStatusTypes();
		// filterCostTypes = WebUtil.getMoneyTransferFilterCostTypes(trialMoneyTransferSummaryModel.getDepartmentId(), null, null, null);
		// Collection<PaymentMethodVO> paymentMethodVOs = null;
		// if (paymentMethods == null) {
		// try {
		// paymentMethods = WebUtil.getServiceLocator().getSelectionSetService().getPaymentMethods(WebUtil.getAuthentication());
		// } catch (ServiceException e) {
		// } catch (AuthenticationException e) {
		// WebUtil.publishException(e);
		// } catch (AuthorisationException e) {
		// } catch (IllegalArgumentException e) {
		// }
		// }
		ecrfProgressSummaryModel.updateRowCount();
	}

	@Override
	public boolean isCreateable() {
		return false;
	}

	@Override
	public boolean isCreated() {
		return false;
	}

	@Override
	public String loadAction() {
		initIn();
		initSets();
		return LOAD_OUTCOME;
	}

	public void setProbandDepartmentId(Long probandDepartmentId) {
		this.probandDepartmentId = probandDepartmentId;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public void setStop(Date stop) {
		this.stop = stop;
	}
}