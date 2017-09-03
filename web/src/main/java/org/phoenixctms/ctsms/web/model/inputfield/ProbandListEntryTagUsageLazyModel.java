package org.phoenixctms.ctsms.web.model.inputfield;

import java.util.ArrayList;
import java.util.Collection;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagOutVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class ProbandListEntryTagUsageLazyModel extends LazyDataModelBase {

	private Long inputFieldId;

	public Long getInputFieldId() {
		return inputFieldId;
	}

	@Override
	protected Collection<ProbandListEntryTagOutVO> getLazyResult(PSFVO psf) {
		if (inputFieldId != null) {
			try {
				return WebUtil.getServiceLocator().getInputFieldService().getProbandListEntryTagList(WebUtil.getAuthentication(), inputFieldId, psf);
			} catch (ServiceException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			} catch (AuthorisationException e) {
			} catch (IllegalArgumentException e) {
			}
		}
		return new ArrayList<ProbandListEntryTagOutVO>();
	}

	@Override
	protected ProbandListEntryTagOutVO getRowElement(Long id) {
		return WebUtil.getProbandListEntryTag(id);
	}

	public void setInputFieldId(Long inputFieldId) {
		this.inputFieldId = inputFieldId;
	}
}
