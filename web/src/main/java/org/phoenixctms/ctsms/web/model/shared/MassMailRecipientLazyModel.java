package org.phoenixctms.ctsms.web.model.shared;

import java.util.ArrayList;
import java.util.Collection;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.MassMailRecipientOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class MassMailRecipientLazyModel extends MassMailRecipientLazyModelBase {

	private Long massMailId;
	private Long probandId;

	public MassMailRecipientLazyModel() {
		super();
	}

	@Override
	protected Collection<MassMailRecipientOutVO> getLazyResult(PSFVO psf) {
		emailMessageCache.clear();
		if (massMailId != null || probandId != null) {
			try {
				return WebUtil.getServiceLocator().getMassMailService().getMassMailRecipientList(WebUtil.getAuthentication(), massMailId, probandId, psf);
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return new ArrayList<MassMailRecipientOutVO>();
	}

	public Long getMassMailId() {
		return massMailId;
	}

	public Long getProbandId() {
		return probandId;
	}

	public void setMassMailId(Long massMailId) {
		this.massMailId = massMailId;
	}

	public void setProbandId(Long probandId) {
		this.probandId = probandId;
	}
}
