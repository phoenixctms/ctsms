package org.phoenixctms.ctsms.web.model.shared;

import java.util.ArrayList;
import java.util.Collection;

import org.phoenixctms.ctsms.enumeration.ECRFFieldStatusQueue;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.ECRFFieldStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class EcrfFieldStatusEntryLazyModel extends LazyDataModelBase<ECRFFieldStatusEntryOutVO> {

	private Long listEntryId;
	private Long ecrfFieldId;
	private Long index;
	private ECRFFieldStatusQueue queue;

	public EcrfFieldStatusEntryLazyModel(ECRFFieldStatusQueue queue) {
		super();
		this.queue = queue;
	}

	public Long getEcrfFieldId() {
		return ecrfFieldId;
	}

	public Long getIndex() {
		return index;
	}

	@Override
	protected Collection<ECRFFieldStatusEntryOutVO> getLazyResult(PSFVO psf) {
		if (listEntryId != null && ecrfFieldId != null) {
			try {
				return WebUtil.getServiceLocator().getTrialService().getEcrfFieldStatusEntryList(WebUtil.getAuthentication(), queue, listEntryId, ecrfFieldId, index, false, false,
						psf);
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return new ArrayList<ECRFFieldStatusEntryOutVO>();
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

	public void setEcrfFieldId(Long ecrfFieldId) {
		this.ecrfFieldId = ecrfFieldId;
	}

	public void setIndex(Long index) {
		this.index = index;
	}

	public void setListEntryId(Long listEntryId) {
		this.listEntryId = listEntryId;
	}
	// public void setQueue(ECRFFieldStatusQueue queue) {
	// this.queue = queue;
	// }
}
