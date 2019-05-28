package org.phoenixctms.ctsms.web.model.shared;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.ECRFFieldValueOutVO;
import org.phoenixctms.ctsms.vo.ECRFOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryOutVO;
import org.phoenixctms.ctsms.web.model.EagerDataModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class EcrfFieldValueAuditTrailLogEagerModel extends EagerDataModelBase<ECRFFieldValueOutVO> {

	private final static PSFVO INITIAL_PSF = new PSFVO();
	static {
		INITIAL_PSF.setSortField(WebUtil.ECRF_FIELD_VALUE_ID_PSF_PROPERTY_NAME);
		INITIAL_PSF.setSortOrder(false);
	}

	public static EcrfFieldValueAuditTrailLogEagerModel getCachedAuditTrailLogModel(ProbandListEntryOutVO listEntry, ECRFOutVO ecrf,
			HashMap<Long, HashMap<Long, EcrfFieldValueAuditTrailLogEagerModel>> auditTrailLogModelCache) {
		EcrfFieldValueAuditTrailLogEagerModel model;
		if (listEntry != null && ecrf != null && auditTrailLogModelCache != null) {
			long listEntryId = listEntry.getId();
			long ecrfId = ecrf.getId();
			HashMap<Long, EcrfFieldValueAuditTrailLogEagerModel> ecrfMap;
			if (auditTrailLogModelCache.containsKey(listEntryId)) {
				ecrfMap = auditTrailLogModelCache.get(listEntryId);
			} else {
				ecrfMap = new HashMap<Long, EcrfFieldValueAuditTrailLogEagerModel>();
				auditTrailLogModelCache.put(listEntryId, ecrfMap);
			}
			if (ecrfMap.containsKey(ecrfId)) {
				model = ecrfMap.get(ecrfId);
			} else {
				model = new EcrfFieldValueAuditTrailLogEagerModel();
				model.setListEntryId(listEntryId);
				model.setEcrfId(ecrfId);
				ecrfMap.put(ecrfId, model);
			}
		} else {
			model = new EcrfFieldValueAuditTrailLogEagerModel();
		}
		return model;
	}

	public static EcrfFieldValueAuditTrailLogEagerModel getCachedAuditTrailLogModel(ProbandListEntryOutVO listEntry,
			HashMap<Long, EcrfFieldValueAuditTrailLogEagerModel> auditTrailLogModelCache) {
		EcrfFieldValueAuditTrailLogEagerModel model;
		if (listEntry != null && auditTrailLogModelCache != null) {
			long listEntryId = listEntry.getId();
			if (auditTrailLogModelCache.containsKey(listEntryId)) {
				model = auditTrailLogModelCache.get(listEntryId);
			} else {
				model = new EcrfFieldValueAuditTrailLogEagerModel();
				model.setListEntryId(listEntryId);
				auditTrailLogModelCache.put(listEntryId, model);
			}
		} else {
			model = new EcrfFieldValueAuditTrailLogEagerModel();
		}
		return model;
	}

	private Long listEntryId;
	private Long ecrfId;

	public EcrfFieldValueAuditTrailLogEagerModel() {
		super();
		resetRows();
	}

	@Override
	protected Collection<ECRFFieldValueOutVO> getEagerResult(PSFVO psf) {
		if (listEntryId != null) { // && ecrfId != null) {
			try {
				return WebUtil.getServiceLocator().getTrialService().getEcrfFieldValueLog(WebUtil.getAuthentication(), null, listEntryId, ecrfId, false, new PSFVO(INITIAL_PSF)); // ,
				// psf);
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return new ArrayList<ECRFFieldValueOutVO>();
	}

	public Long getEcrfId() {
		return ecrfId;
	}

	public Long getListEntryId() {
		return listEntryId;
	}

	@Override
	protected ECRFFieldValueOutVO getRowElement(Long id) {
		return WebUtil.getEcrfFieldValue(id);
	}

	public void setEcrfId(Long ecrfId) {
		this.ecrfId = ecrfId;
	}

	public void setListEntryId(Long listEntryId) {
		this.listEntryId = listEntryId;
	}
}
