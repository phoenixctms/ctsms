package org.phoenixctms.ctsms.web.model.staff;

import java.util.ArrayList;
import java.util.Collection;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.CvPositionOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class InstitutionCvPositionLazyModel extends LazyDataModelBase {

	private Long staffId;

	@Override
	protected Collection<CvPositionOutVO> getLazyResult(PSFVO psf) {
		if (staffId != null) {
			try {
				return WebUtil.getServiceLocator().getStaffService().getInstitutionCvPositionList(WebUtil.getAuthentication(), staffId, psf);
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return new ArrayList<CvPositionOutVO>();
	}

	@Override
	protected CvPositionOutVO getRowElement(Long id) {
		return WebUtil.getCvPosition(id);
	}

	public Long getStaffId() {
		return staffId;
	}

	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}
}
