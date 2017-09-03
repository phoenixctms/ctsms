package org.phoenixctms.ctsms.web.model.proband;

import java.util.ArrayList;
import java.util.Collection;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.ProbandStatusEntryOutVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class ProbandStatusEntryLazyModel extends LazyDataModelBase {

	private Long probandId;

	@Override
	protected Collection<ProbandStatusEntryOutVO> getLazyResult(PSFVO psf) {
		if (probandId != null) {
			try {
				return WebUtil.getServiceLocator().getProbandService().getProbandStatusEntryList(WebUtil.getAuthentication(), probandId, psf);
			} catch (ServiceException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			} catch (AuthorisationException e) {
			} catch (IllegalArgumentException e) {
			}
		}
		return new ArrayList<ProbandStatusEntryOutVO>();
	}

	public Long getProbandId() {
		return probandId;
	}

	@Override
	protected ProbandStatusEntryOutVO getRowElement(Long id) {
		return WebUtil.getProbandStatusEntry(id);
	}

	public void setProbandId(Long probandId) {
		this.probandId = probandId;
	}
}
