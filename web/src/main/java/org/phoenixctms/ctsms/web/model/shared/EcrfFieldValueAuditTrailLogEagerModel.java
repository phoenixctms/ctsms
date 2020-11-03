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
import org.phoenixctms.ctsms.vo.VisitOutVO;
import org.phoenixctms.ctsms.web.model.EagerDataModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class EcrfFieldValueAuditTrailLogEagerModel extends EagerDataModelBase<ECRFFieldValueOutVO> {

	private final static PSFVO INITIAL_PSF = new PSFVO();
	static {
		INITIAL_PSF.setSortField(WebUtil.ECRF_FIELD_VALUE_ID_PSF_PROPERTY_NAME);
		INITIAL_PSF.setSortOrder(false);
	}

	public static EcrfFieldValueAuditTrailLogEagerModel getCachedAuditTrailLogModel(ProbandListEntryOutVO listEntry, ECRFOutVO ecrf, VisitOutVO visitVO,
			HashMap<Long, HashMap<Long, HashMap<Long, EcrfFieldValueAuditTrailLogEagerModel>>> auditTrailLogModelCache) {
		EcrfFieldValueAuditTrailLogEagerModel model;
		if (listEntry != null && ecrf != null && auditTrailLogModelCache != null) {
			long listEntryId = listEntry.getId();
			long ecrfId = ecrf.getId();
			Long visitId = visitVO != null ? visitVO.getId() : null;
			HashMap<Long, HashMap<Long, EcrfFieldValueAuditTrailLogEagerModel>> ecrfVisitMap;
			if (auditTrailLogModelCache.containsKey(listEntryId)) {
				ecrfVisitMap = auditTrailLogModelCache.get(listEntryId);
			} else {
				ecrfVisitMap = new HashMap<Long, HashMap<Long, EcrfFieldValueAuditTrailLogEagerModel>>();
				auditTrailLogModelCache.put(listEntryId, ecrfVisitMap);
			}
			HashMap<Long, EcrfFieldValueAuditTrailLogEagerModel> ecrfMap;
			if (ecrfVisitMap.containsKey(ecrfId)) {
				ecrfMap = ecrfVisitMap.get(ecrfId);
			} else {
				ecrfMap = new HashMap<Long, EcrfFieldValueAuditTrailLogEagerModel>();
				ecrfVisitMap.put(ecrfId, ecrfMap);
			}
			if (ecrfMap.containsKey(visitId)) {
				model = ecrfMap.get(visitId);
			} else {
				model = new EcrfFieldValueAuditTrailLogEagerModel();
				model.setListEntryId(listEntryId);
				model.setEcrfId(ecrfId);
				model.setVisitId(visitId);
				ecrfMap.put(visitId, model);
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
	private Long visitId;

	public EcrfFieldValueAuditTrailLogEagerModel() {
		super();
		resetRows();
	}

	@Override
	protected Collection<ECRFFieldValueOutVO> getEagerResult(PSFVO psf) {
		if (listEntryId != null) {
			try {
				return WebUtil.getServiceLocator().getTrialService().getEcrfFieldValueLog(WebUtil.getAuthentication(), null, listEntryId, ecrfId, visitId, false,
						new PSFVO(INITIAL_PSF));
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
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

	public Long getVisitId() {
		return visitId;
	}

	public void setVisitId(Long visitId) {
		this.visitId = visitId;
	}
}
