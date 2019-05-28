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
import org.phoenixctms.ctsms.web.model.EagerDataModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class EcrfFieldStatusEntryLogEagerModel extends EagerDataModelBase<ECRFFieldStatusEntryOutVO> {

	private final static PSFVO INITIAL_PSF = new PSFVO();
	static {
		INITIAL_PSF.setSortField(WebUtil.ECRF_FIELD_STATUS_ENTRY_ID_PSF_PROPERTY_NAME);
		INITIAL_PSF.setSortOrder(false);
	}

	public static EcrfFieldStatusEntryLogEagerModel getCachedFieldStatusEntryLogModel(ECRFFieldStatusQueue queue, ProbandListEntryOutVO listEntry, ECRFOutVO ecrf,
			HashMap<ECRFFieldStatusQueue, HashMap<Long, HashMap<Long, EcrfFieldStatusEntryLogEagerModel>>> fieldStatusEntryLogModelCache) {
		EcrfFieldStatusEntryLogEagerModel model;
		if (queue != null && listEntry != null && ecrf != null && fieldStatusEntryLogModelCache != null) {
			long listEntryId = listEntry.getId();
			long ecrfId = ecrf.getId();
			HashMap<Long, HashMap<Long, EcrfFieldStatusEntryLogEagerModel>> listEntryMap;
			if (fieldStatusEntryLogModelCache.containsKey(queue)) {
				listEntryMap = fieldStatusEntryLogModelCache.get(queue);
			} else {
				listEntryMap = new HashMap<Long, HashMap<Long, EcrfFieldStatusEntryLogEagerModel>>();
				fieldStatusEntryLogModelCache.put(queue, listEntryMap);
			}
			HashMap<Long, EcrfFieldStatusEntryLogEagerModel> ecrfMap;
			if (listEntryMap.containsKey(listEntryId)) {
				ecrfMap = listEntryMap.get(listEntryId);
			} else {
				ecrfMap = new HashMap<Long, EcrfFieldStatusEntryLogEagerModel>();
				listEntryMap.put(listEntryId, ecrfMap);
			}
			if (ecrfMap.containsKey(ecrfId)) {
				model = ecrfMap.get(ecrfId);
			} else {
				model = new EcrfFieldStatusEntryLogEagerModel(queue);
				model.setListEntryId(listEntryId);
				model.setEcrfId(ecrfId);
				ecrfMap.put(ecrfId, model);
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
	private ECRFFieldStatusQueue queue;

	public EcrfFieldStatusEntryLogEagerModel(ECRFFieldStatusQueue queue) {
		super();
		this.queue = queue;
		resetRows();
	}

	@Override
	protected Collection<ECRFFieldStatusEntryOutVO> getEagerResult(PSFVO psf) {
		if (listEntryId != null) { // && ecrfId != null) {
			try {
				return WebUtil.getServiceLocator().getTrialService()
						.getEcrfFieldStatusEntryLog(WebUtil.getAuthentication(), queue, null, listEntryId, ecrfId, true, false, new PSFVO(INITIAL_PSF)); // ,
				// psf);
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
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
	// public void setQueue(ECRFFieldStatusQueue queue) {
	// this.queue = queue;
	// }
}
