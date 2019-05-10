package org.phoenixctms.ctsms.web.model.shared;

import java.util.ArrayList;
import java.util.Collection;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.ECRFFieldValueOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class EcrfFieldValueAuditTrailLazyModel extends LazyDataModelBase {

	private Long listEntryId;
	private Long ecrfFieldId;
	private Long index;

	public EcrfFieldValueAuditTrailLazyModel() {
		super();
	}

	public Long getEcrfFieldId() {
		return ecrfFieldId;
	}

	public Long getIndex() {
		return index;
	}

	@Override
	protected Collection<ECRFFieldValueOutVO> getLazyResult(PSFVO psf) {
		if (listEntryId != null && ecrfFieldId != null) {
			try {
				return WebUtil.getServiceLocator().getTrialService().getEcrfFieldValue(WebUtil.getAuthentication(), listEntryId, ecrfFieldId, index, true, false, psf)
						.getPageValues();
			} catch (ServiceException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			} catch (AuthorisationException e) {
			} catch (IllegalArgumentException e) {
			}
		}
		return new ArrayList<ECRFFieldValueOutVO>();
	}

	public Long getListEntryId() {
		return listEntryId;
	}

	@Override
	protected ECRFFieldValueOutVO getRowElement(Long id) {
		return WebUtil.getEcrfFieldValue(id);
	}

	public void setEcrfFieldId(Long ecrfFieldId) {
		this.ecrfFieldId = ecrfFieldId;
	}

	public void setIndex(Long index) {
		this.index = index;
	}

	public void setListEntryId(Long listEntryId) {
		this.listEntryId = listEntryId;
	}
}
