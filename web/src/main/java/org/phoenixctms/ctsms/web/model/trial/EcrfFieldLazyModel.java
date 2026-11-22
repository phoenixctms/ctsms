package org.phoenixctms.ctsms.web.model.trial;

import java.util.ArrayList;
import java.util.Collection;

import org.phoenixctms.ctsms.enumeration.InputFieldL10nBundle;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.ECRFFieldOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class EcrfFieldLazyModel extends LazyDataModelBase<ECRFFieldOutVO> {

	private Long trialId;
	private Long ecrfId;

	public Long getEcrfId() {
		return ecrfId;
	}

	@Override
	protected Collection<ECRFFieldOutVO> getLazyResult(PSFVO psf) {
		if (trialId != null || ecrfId != null) {
			try {
				return WebUtil.getServiceLocator().getTrialService().getEcrfFieldList(WebUtil.getAuthentication(), trialId, ecrfId, false, psf);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return new ArrayList<ECRFFieldOutVO>();
	}

	@Override
	protected String getL10nKey(Object vo, InputFieldL10nBundle bundle) {
		if (vo instanceof ECRFFieldOutVO) {
			ECRFFieldOutVO ecrfField = (ECRFFieldOutVO) vo;
			if (bundle == InputFieldL10nBundle.ECRF_FIELD_SECTIONS) {
				return ecrfField.getSection();
			}
			// titles are keys only if the input field is localized:
			if (bundle == InputFieldL10nBundle.TITLES && ecrfField.getField() != null && ecrfField.getField().isLocalized()) {
				return ecrfField.getTitleL10nKey();
			}
		}
		return null;
	}

	@Override
	protected ECRFFieldOutVO getRowElement(Long id) {
		return WebUtil.getEcrfField(id);
	}

	public Long getTrialId() {
		return trialId;
	}

	public void setEcrfId(Long ecrfId) {
		this.ecrfId = ecrfId;
	}

	public void setTrialId(Long trialId) {
		this.trialId = trialId;
	}
}
