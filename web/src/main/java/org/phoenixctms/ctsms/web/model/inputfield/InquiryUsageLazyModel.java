package org.phoenixctms.ctsms.web.model.inputfield;

import java.util.ArrayList;
import java.util.Collection;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.InquiryOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class InquiryUsageLazyModel extends LazyDataModelBase {

	private Long inputFieldId;

	public Long getInputFieldId() {
		return inputFieldId;
	}

	@Override
	protected Collection<InquiryOutVO> getLazyResult(PSFVO psf) {
		if (inputFieldId != null) {
			try {
				return WebUtil.getServiceLocator().getInputFieldService().getInquiryList(WebUtil.getAuthentication(), inputFieldId, psf);
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return new ArrayList<InquiryOutVO>();
	}

	@Override
	protected InquiryOutVO getRowElement(Long id) {
		return WebUtil.getInquiry(id);
	}

	public void setInputFieldId(Long inputFieldId) {
		this.inputFieldId = inputFieldId;
	}
}
