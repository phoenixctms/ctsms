package org.phoenixctms.ctsms.web.model.inputfield;

import java.util.ArrayList;
import java.util.Collection;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class InputFieldSelectionSetValueLazyModel extends LazyDataModelBase {

	private Long inputFieldId;

	public Long getInputFieldId() {
		return inputFieldId;
	}

	@Override
	protected Collection<InputFieldSelectionSetValueOutVO> getLazyResult(PSFVO psf) {
		if (inputFieldId != null) {
			try {
				return WebUtil.getServiceLocator().getInputFieldService().getSelectionSetValueList(WebUtil.getAuthentication(), inputFieldId, null, psf);
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return new ArrayList<InputFieldSelectionSetValueOutVO>();
	}

	@Override
	protected InputFieldSelectionSetValueOutVO getRowElement(Long id) {
		return WebUtil.getInputFieldSelectionSetValue(id);
	}

	public void setInputFieldId(Long inputFieldId) {
		this.inputFieldId = inputFieldId;
	}
}
