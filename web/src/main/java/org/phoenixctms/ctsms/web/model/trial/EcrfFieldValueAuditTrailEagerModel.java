package org.phoenixctms.ctsms.web.model.trial;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.ECRFFieldStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.ECRFFieldValueOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.model.EagerDataModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class EcrfFieldValueAuditTrailEagerModel extends EagerDataModelBase<ECRFFieldValueOutVO> {

	private final static PSFVO INITIAL_PSF = new PSFVO();
	static {
		INITIAL_PSF.setSortField(WebUtil.ECRF_FIELD_VALUE_ID_PSF_PROPERTY_NAME);
		INITIAL_PSF.setSortOrder(false);
	}

	public static EcrfFieldValueAuditTrailEagerModel getCachedAuditTrailModel(ECRFFieldStatusEntryOutVO status,
			HashMap<Long, EcrfFieldValueAuditTrailEagerModel> auditTrailModelCache) {
		EcrfFieldValueAuditTrailEagerModel model;
		if (status != null && auditTrailModelCache != null) {
			long statusId = status.getId();
			if (auditTrailModelCache.containsKey(statusId)) {
				model = auditTrailModelCache.get(statusId);
			} else {
				model = new EcrfFieldValueAuditTrailEagerModel();
				model.setStatus(status);
				auditTrailModelCache.put(statusId, model);
			}
		} else {
			model = new EcrfFieldValueAuditTrailEagerModel();
		}
		return model;
	}

	private ECRFFieldStatusEntryOutVO status;

	public EcrfFieldValueAuditTrailEagerModel() {
		super();
		resetRows();
	}

	@Override
	protected Collection<ECRFFieldValueOutVO> getEagerResult(PSFVO psf) {
		if (status != null) {
			try {
				return WebUtil.getServiceLocator().getTrialService()
						.getEcrfFieldValue(WebUtil.getAuthentication(), status.getListEntry().getId(), status.getEcrfField().getId(), status.getIndex(), true, false,
								new PSFVO(INITIAL_PSF))
						.getPageValues();
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return new ArrayList<ECRFFieldValueOutVO>();
	}

	@Override
	protected ECRFFieldValueOutVO getRowElement(Long id) {
		return WebUtil.getEcrfFieldValue(id);
	}

	public ECRFFieldStatusEntryOutVO getStatus() {
		return status;
	}

	public void setStatus(ECRFFieldStatusEntryOutVO status) {
		this.status = status;
	}
}
