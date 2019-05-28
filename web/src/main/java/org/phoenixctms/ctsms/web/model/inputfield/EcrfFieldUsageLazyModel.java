package org.phoenixctms.ctsms.web.model.inputfield;

import java.util.ArrayList;
import java.util.Collection;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.ECRFFieldOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class EcrfFieldUsageLazyModel extends LazyDataModelBase<ECRFFieldOutVO> {

	private Long inputFieldId;

	public Long getInputFieldId() {
		return inputFieldId;
	}

	@Override
	protected Collection<ECRFFieldOutVO> getLazyResult(PSFVO psf) {
		if (inputFieldId != null) {
			try {
				return WebUtil.getServiceLocator().getInputFieldService().getEcrfFieldList(WebUtil.getAuthentication(), inputFieldId, psf);
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return new ArrayList<ECRFFieldOutVO>();
	}

	@Override
	protected ECRFFieldOutVO getRowElement(Long id) {
		return WebUtil.getEcrfField(id);
	}

	public void setInputFieldId(Long inputFieldId) {
		this.inputFieldId = inputFieldId;
	}
}
