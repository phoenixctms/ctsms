package org.phoenixctms.ctsms.web.model.proband;

import java.util.ArrayList;
import java.util.Collection;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.BankAccountOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class BankAccountLazyModel extends LazyDataModelBase<BankAccountOutVO> {

	private Long probandId;

	@Override
	protected Collection<BankAccountOutVO> getLazyResult(PSFVO psf) {
		if (probandId != null) {
			try {
				return WebUtil.getServiceLocator().getProbandService().getBankAccountList(WebUtil.getAuthentication(), probandId, psf);
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return new ArrayList<BankAccountOutVO>();
	}

	public Long getProbandId() {
		return probandId;
	}

	@Override
	protected BankAccountOutVO getRowElement(Long id) {
		try {
			return WebUtil.getServiceLocator().getProbandService().getBankAccount(WebUtil.getAuthentication(), id);
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
		return null;
	}

	public void setProbandId(Long probandId) {
		this.probandId = probandId;
	}
}
