package org.phoenixctms.ctsms.web.model.user;

import java.util.ArrayList;
import java.util.Collection;

import org.phoenixctms.ctsms.enumeration.DBModule;
import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.JournalEntryOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class UserActivityLazyModel extends LazyDataModelBase {

	private Long modifiedUserId;
	private JournalModule module;
	private DBModule criteriaModule;

	public DBModule getCriteriaModule() {
		return criteriaModule;
	}

	@Override
	protected Collection<JournalEntryOutVO> getLazyResult(PSFVO psf) {
		if (modifiedUserId != null) {
			try {
				return WebUtil.getServiceLocator().getJournalService().getActivity(WebUtil.getAuthentication(), module, modifiedUserId, criteriaModule, null,
						false, psf);
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return new ArrayList<JournalEntryOutVO>();
	}

	public Long getModifiedUserId() {
		return modifiedUserId;
	}

	public JournalModule getModule() {
		return module;
	}

	@Override
	protected JournalEntryOutVO getRowElement(Long id) {
		return WebUtil.getJournalEntry(id);
	}

	public void setCriteriaModule(DBModule criteriaModule) {
		this.criteriaModule = criteriaModule;
	}

	public void setModifiedUserId(Long modifiedUserId) {
		this.modifiedUserId = modifiedUserId;
	}

	public void setModule(JournalModule module) {
		this.module = module;
	}
}
