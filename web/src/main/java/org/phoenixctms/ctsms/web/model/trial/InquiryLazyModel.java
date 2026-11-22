package org.phoenixctms.ctsms.web.model.trial;

import java.util.ArrayList;
import java.util.Collection;

import org.phoenixctms.ctsms.enumeration.InputFieldL10nBundle;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.InquiryOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class InquiryLazyModel extends LazyDataModelBase<InquiryOutVO> {

	private Long trialId;

	@Override
	protected Collection<InquiryOutVO> getLazyResult(PSFVO psf) {
		if (trialId != null) {
			try {
				return WebUtil.getServiceLocator().getTrialService().getInquiryList(WebUtil.getAuthentication(), trialId, null, null, false, psf);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return new ArrayList<InquiryOutVO>();
	}

	@Override
	protected String getL10nKey(Object vo, InputFieldL10nBundle bundle) {
		if (vo instanceof InquiryOutVO) {
			InquiryOutVO inquiry = (InquiryOutVO) vo;
			if (bundle == InputFieldL10nBundle.INQUIRY_CATEGORIES) {
				return inquiry.getCategory();
			}
			// titles are keys only if the input field is localized:
			if (bundle == InputFieldL10nBundle.TITLES && inquiry.getField() != null && inquiry.getField().isLocalized()) {
				return inquiry.getTitleL10nKey();
			}
		}
		return null;
	}

	@Override
	protected InquiryOutVO getRowElement(Long id) {
		return WebUtil.getInquiry(id);
	}

	public Long getTrialId() {
		return trialId;
	}

	public void setTrialId(Long trialId) {
		this.trialId = trialId;
	}
}
