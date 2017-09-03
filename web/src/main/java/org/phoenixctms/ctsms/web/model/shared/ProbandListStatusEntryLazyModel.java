package org.phoenixctms.ctsms.web.model.shared;

import java.util.ArrayList;
import java.util.Collection;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.ProbandListStatusEntryOutVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class ProbandListStatusEntryLazyModel extends LazyDataModelBase {

	private Long probandListEntryId;

	@Override
	protected Collection<ProbandListStatusEntryOutVO> getLazyResult(PSFVO psf) {
		if (probandListEntryId != null) {
			try {
				return WebUtil.getServiceLocator().getTrialService().getProbandListStatusEntryList(WebUtil.getAuthentication(), probandListEntryId, psf);
			} catch (ServiceException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			} catch (AuthorisationException e) {
			} catch (IllegalArgumentException e) {
			}
		}
		return new ArrayList<ProbandListStatusEntryOutVO>();
	}

	public Long getProbandListEntryId() {
		return probandListEntryId;
	}

	@Override
	protected ProbandListStatusEntryOutVO getRowElement(Long id) {
		try {
			return WebUtil.getServiceLocator().getTrialService().getProbandListStatusEntry(WebUtil.getAuthentication(), id);
		} catch (ServiceException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
		} catch (IllegalArgumentException e) {
		}
		return null;
	}

	public void setProbandListEntryId(Long probandListEntryId) {
		this.probandListEntryId = probandListEntryId;
	}
}
