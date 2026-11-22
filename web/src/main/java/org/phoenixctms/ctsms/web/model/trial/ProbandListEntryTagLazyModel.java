package org.phoenixctms.ctsms.web.model.trial;

import java.util.ArrayList;
import java.util.Collection;

import org.phoenixctms.ctsms.enumeration.InputFieldL10nBundle;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagOutVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class ProbandListEntryTagLazyModel extends LazyDataModelBase<ProbandListEntryTagOutVO> {

	private Long trialId;

	@Override
	protected Collection<ProbandListEntryTagOutVO> getLazyResult(PSFVO psf) {
		if (trialId != null) {
			try {
				return WebUtil.getServiceLocator().getTrialService().getProbandListEntryTagList(WebUtil.getAuthentication(), trialId, psf);
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return new ArrayList<ProbandListEntryTagOutVO>();
	}

	@Override
	protected String getL10nKey(Object vo, InputFieldL10nBundle bundle) {
		if (vo instanceof ProbandListEntryTagOutVO) {
			ProbandListEntryTagOutVO tag = (ProbandListEntryTagOutVO) vo;
			// titles are keys only if the input field is localized:
			if (bundle == InputFieldL10nBundle.TITLES && tag.getField() != null && tag.getField().isLocalized()) {
				return tag.getTitleL10nKey();
			}
		}
		return null;
	}

	@Override
	protected ProbandListEntryTagOutVO getRowElement(Long id) {
		return WebUtil.getProbandListEntryTag(id);
	}

	public Long getTrialId() {
		return trialId;
	}

	public void setTrialId(Long trialId) {
		this.trialId = trialId;
	}
}
