package org.phoenixctms.ctsms.web.model.proband;

import java.util.ArrayList;
import java.util.Collection;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.InventoryBookingOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class ProbandInventoryBookingLazyModel extends LazyDataModelBase {

	private Long probandId;

	@Override
	protected Collection<InventoryBookingOutVO> getLazyResult(PSFVO psf) {
		if (probandId != null) {
			try {
				return WebUtil.getServiceLocator().getProbandService().getProbandInventoryBookingList(WebUtil.getAuthentication(), probandId, psf);
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return new ArrayList<InventoryBookingOutVO>();
	}

	public Long getProbandId() {
		return probandId;
	}

	@Override
	protected InventoryBookingOutVO getRowElement(Long id) {
		return WebUtil.getInventoryBooking(id);
	}

	public void setProbandId(Long probandId) {
		this.probandId = probandId;
	}
}
