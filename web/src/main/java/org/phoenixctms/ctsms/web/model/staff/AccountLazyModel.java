package org.phoenixctms.ctsms.web.model.staff;

import java.util.ArrayList;
import java.util.Collection;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.UserOutVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class AccountLazyModel extends LazyDataModelBase<UserOutVO> {

	private Long staffId;

	@Override
	protected Collection<UserOutVO> getLazyResult(PSFVO psf) {
		if (staffId != null) {
			try {
				return WebUtil.getServiceLocator().getStaffService().getAccounts(WebUtil.getAuthentication(), staffId, psf);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return new ArrayList<UserOutVO>();
	}

	@Override
	protected UserOutVO getRowElement(Long id) {
		return WebUtil.getUser(id, null, null, null);
	}

	public Long getStaffId() {
		return staffId;
	}

	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}
}
