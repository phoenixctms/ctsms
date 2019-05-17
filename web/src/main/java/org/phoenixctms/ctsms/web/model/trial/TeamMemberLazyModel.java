package org.phoenixctms.ctsms.web.model.trial;

import java.util.ArrayList;
import java.util.Collection;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.TeamMemberOutVO;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class TeamMemberLazyModel extends LazyDataModelBase {

	private Long trialId;

	@Override
	protected Collection<TeamMemberOutVO> getLazyResult(PSFVO psf) {
		if (trialId != null) {
			try {
				return WebUtil.getServiceLocator().getTrialService().getTeamMemberList(WebUtil.getAuthentication(), trialId, null, null, psf);
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return new ArrayList<TeamMemberOutVO>();
	}

	@Override
	protected Long getPageId(IDVO idvo) {
		return ((TeamMemberOutVO) idvo.getVo()).getStaff().getId();
	}

	@Override
	protected TeamMemberOutVO getRowElement(Long id) {
		return WebUtil.getTeamMember(id);
	}

	public Long getTrialId() {
		return trialId;
	}

	public void setTrialId(Long trialId) {
		this.trialId = trialId;
	}
}
