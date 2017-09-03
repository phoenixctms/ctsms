package org.phoenixctms.ctsms.web.model.trial;





import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.ECRFFieldStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.model.EagerDataModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class EcrfFieldStatusEntryEagerModel extends EagerDataModelBase {

	private final static PSFVO INITIAL_PSF = new PSFVO();
	static {
		INITIAL_PSF.setSortField(WebUtil.ECRF_FIELD_STATUS_ENTRY_ID_PSF_PROPERTY_NAME);
		INITIAL_PSF.setSortOrder(false);
	}

	// private Long listEntryId;
	// private Long ecrfFieldId;
	// private Long index;
	// private ECRFFieldStatusQueue queue;
	public static EcrfFieldStatusEntryEagerModel getCachedFieldStatusEntryModel(ECRFFieldStatusEntryOutVO status,
			HashMap<Long, EcrfFieldStatusEntryEagerModel> fieldStatusEntryModelCache) {
		EcrfFieldStatusEntryEagerModel model;
		if (status != null && fieldStatusEntryModelCache != null) {
			long statusId = status.getId();
			if (fieldStatusEntryModelCache.containsKey(statusId)) {
				model = fieldStatusEntryModelCache.get(statusId);
			} else {
				model = new EcrfFieldStatusEntryEagerModel();
				model.setStatus(status);
				fieldStatusEntryModelCache.put(statusId, model);
			}
		} else {
			model = new EcrfFieldStatusEntryEagerModel();
		}
		return model;
	}

	private ECRFFieldStatusEntryOutVO status;

	public EcrfFieldStatusEntryEagerModel() {
		super();
		resetRows();
	}

	// public Long getEcrfFieldId() {
	// return ecrfFieldId;
	// }
	//
	// public Long getIndex() {
	// return index;
	// }

	@Override
	protected Collection<ECRFFieldStatusEntryOutVO> getEagerResult(PSFVO psf) {
		if (status != null) {
			try {
				return WebUtil
						.getServiceLocator()
						.getTrialService()
						.getEcrfFieldStatusEntryList(WebUtil.getAuthentication(), status.getStatus().getQueue(), status.getListEntry().getId(), status.getEcrfField().getId(),
								status.getIndex(), false, false, new PSFVO(INITIAL_PSF));
			} catch (ServiceException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			} catch (AuthorisationException e) {
			} catch (IllegalArgumentException e) {
			}
		}
		return new ArrayList<ECRFFieldStatusEntryOutVO>();
	}

	// public Long getListEntryId() {
	// return listEntryId;
	// }
	//
	// public ECRFFieldStatusQueue getQueue() {
	// return queue;
	// }

	@Override
	protected ECRFFieldStatusEntryOutVO getRowElement(Long id) {
		return WebUtil.getEcrfFieldStatusEntry(id);
	}


	public ECRFFieldStatusEntryOutVO getStatus() {
		return status;
	}


	public void setStatus(ECRFFieldStatusEntryOutVO status) {
		this.status = status;
	}

	// public void setEcrfFieldId(Long ecrfFieldId) {
	// this.ecrfFieldId = ecrfFieldId;
	// }
	//
	// public void setIndex(Long index) {
	// this.index = index;
	// }
	//
	//
	// public void setListEntryId(Long listEntryId) {
	// this.listEntryId = listEntryId;
	// }

	// public void setQueue(ECRFFieldStatusQueue queue) {
	// this.queue = queue;
	// }



}
