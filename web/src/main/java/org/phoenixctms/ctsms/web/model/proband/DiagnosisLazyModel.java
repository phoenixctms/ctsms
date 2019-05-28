package org.phoenixctms.ctsms.web.model.proband;

import java.util.ArrayList;
import java.util.Collection;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.DiagnosisOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class DiagnosisLazyModel extends LazyDataModelBase<DiagnosisOutVO> {

	private Long probandId;

	@Override
	protected Collection<DiagnosisOutVO> getLazyResult(PSFVO psf) {
		if (probandId != null) {
			try {
				return WebUtil.getServiceLocator().getProbandService().getDiagnosisList(WebUtil.getAuthentication(), probandId, psf);
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return new ArrayList<DiagnosisOutVO>();
	}

	public Long getProbandId() {
		return probandId;
	}

	@Override
	protected DiagnosisOutVO getRowElement(Long id) {
		try {
			return WebUtil.getServiceLocator().getProbandService().getDiagnosis(WebUtil.getAuthentication(), id);
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
