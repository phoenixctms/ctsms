package org.phoenixctms.ctsms.web.model.shared;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.phoenixctms.ctsms.enumeration.ECRFFieldStatusQueue;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.ECRFFieldStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.ECRFOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryOutVO;
import org.phoenixctms.ctsms.vo.VisitOutVO;
import org.phoenixctms.ctsms.web.model.EagerDataModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class EcrfFieldStatusEntryLogEagerModel extends EagerDataModelBase<ECRFFieldStatusEntryOutVO> {

	private final static PSFVO INITIAL_PSF = new PSFVO();
	static {
		INITIAL_PSF.setSortField(WebUtil.ECRF_FIELD_STATUS_ENTRY_ID_PSF_PROPERTY_NAME);
		INITIAL_PSF.setSortOrder(false);
	}

	public static EcrfFieldStatusEntryLogEagerModel getCachedFieldStatusEntryLogModel(ECRFFieldStatusQueue queue, ProbandListEntryOutVO listEntry, ECRFOutVO ecrf, VisitOutVO visit,
			HashMap<ECRFFieldStatusQueue, HashMap<Long, HashMap<Long, HashMap<Long, EcrfFieldStatusEntryLogEagerModel>>>> fieldStatusEntryLogModelCache) {
		EcrfFieldStatusEntryLogEagerModel model;
		if (queue != null && listEntry != null && ecrf != null && fieldStatusEntryLogModelCache != null) {
			long listEntryId = listEntry.getId();
			long ecrfId = ecrf.getId();
			Long visitId = visit != null ? visit.getId() : null;
			HashMap<Long, HashMap<Long, HashMap<Long, EcrfFieldStatusEntryLogEagerModel>>> queueEcrfVisitMap;
			if (fieldStatusEntryLogModelCache.containsKey(queue)) {
				queueEcrfVisitMap = fieldStatusEntryLogModelCache.get(queue);
			} else {
				queueEcrfVisitMap = new HashMap<Long, HashMap<Long, HashMap<Long, EcrfFieldStatusEntryLogEagerModel>>>();
				fieldStatusEntryLogModelCache.put(queue, queueEcrfVisitMap);
			}
			HashMap<Long, HashMap<Long, EcrfFieldStatusEntryLogEagerModel>> ecrfVisitMap;
			if (queueEcrfVisitMap.containsKey(listEntryId)) {
				ecrfVisitMap = queueEcrfVisitMap.get(listEntryId);
			} else {
				ecrfVisitMap = new HashMap<Long, HashMap<Long, EcrfFieldStatusEntryLogEagerModel>>();
				queueEcrfVisitMap.put(listEntryId, ecrfVisitMap);
			}
			HashMap<Long, EcrfFieldStatusEntryLogEagerModel> ecrfMap;
			if (ecrfVisitMap.containsKey(ecrfId)) {
				ecrfMap = ecrfVisitMap.get(ecrfId);
			} else {
				ecrfMap = new HashMap<Long, EcrfFieldStatusEntryLogEagerModel>();
				ecrfVisitMap.put(ecrfId, ecrfMap);
			}
			if (ecrfMap.containsKey(visitId)) {
				model = ecrfMap.get(visitId);
			} else {
				model = new EcrfFieldStatusEntryLogEagerModel(queue);
				model.setListEntryId(listEntryId);
				model.setEcrfId(ecrfId);
				model.setVisitId(visitId);
				ecrfMap.put(visitId, model);
			}
		} else {
			model = new EcrfFieldStatusEntryLogEagerModel(queue);
		}
		return model;
	}

	public static EcrfFieldStatusEntryLogEagerModel getCachedFieldStatusEntryLogModel(ECRFFieldStatusQueue queue, ProbandListEntryOutVO listEntry,
			HashMap<ECRFFieldStatusQueue, HashMap<Long, EcrfFieldStatusEntryLogEagerModel>> fieldStatusEntryLogModelCache) {
		EcrfFieldStatusEntryLogEagerModel model;
		if (queue != null && listEntry != null && fieldStatusEntryLogModelCache != null) {
			long listEntryId = listEntry.getId();
			HashMap<Long, EcrfFieldStatusEntryLogEagerModel> listEntryMap;
			if (fieldStatusEntryLogModelCache.containsKey(queue)) {
				listEntryMap = fieldStatusEntryLogModelCache.get(queue);
			} else {
				listEntryMap = new HashMap<Long, EcrfFieldStatusEntryLogEagerModel>();
				fieldStatusEntryLogModelCache.put(queue, listEntryMap);
			}
			if (listEntryMap.containsKey(listEntryId)) {
				model = listEntryMap.get(listEntryId);
			} else {
				model = new EcrfFieldStatusEntryLogEagerModel(queue);
				model.setListEntryId(listEntryId);
				listEntryMap.put(listEntryId, model);
			}
		} else {
			model = new EcrfFieldStatusEntryLogEagerModel(queue);
		}
		return model;
	}

	private Long listEntryId;
	private Long ecrfId;
	private Long visitId;
	private ECRFFieldStatusQueue queue;

	public EcrfFieldStatusEntryLogEagerModel(ECRFFieldStatusQueue queue) {
		super();
		this.queue = queue;
		resetRows();
	}

	@Override
	protected Collection<ECRFFieldStatusEntryOutVO> getEagerResult(PSFVO psf) {
		if (listEntryId != null) {
			try {
				return WebUtil.getServiceLocator().getTrialService()
						.getEcrfFieldStatusEntryLog(WebUtil.getAuthentication(), queue, null, listEntryId, ecrfId, visitId, true, false, new PSFVO(INITIAL_PSF));
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return new ArrayList<ECRFFieldStatusEntryOutVO>();
	}

	public Long getEcrfId() {
		return ecrfId;
	}

	public Long getListEntryId() {
		return listEntryId;
	}

	public ECRFFieldStatusQueue getQueue() {
		return queue;
	}

	@Override
	protected ECRFFieldStatusEntryOutVO getRowElement(Long id) {
		return WebUtil.getEcrfFieldStatusEntry(id);
	}

	public void setEcrfId(Long ecrfId) {
		this.ecrfId = ecrfId;
	}

	public void setListEntryId(Long listEntryId) {
		this.listEntryId = listEntryId;
	}

	public Long getVisitId() {
		return visitId;
	}

	public void setVisitId(Long visitId) {
		this.visitId = visitId;
	}
}
