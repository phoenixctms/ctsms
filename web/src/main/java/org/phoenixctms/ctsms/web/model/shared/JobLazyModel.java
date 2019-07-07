package org.phoenixctms.ctsms.web.model.shared;

import java.util.ArrayList;
import java.util.Collection;

import org.phoenixctms.ctsms.enumeration.JobModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.JobOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class JobLazyModel extends LazyDataModelBase<JobOutVO> {

	private Long entityId;
	private JobModule module;

	public Long getEntityId() {
		return entityId;
	}

	@Override
	protected Collection<JobOutVO> getLazyResult(PSFVO psf) {
		if (module != null && entityId != null) {
			try {
				return WebUtil.getServiceLocator().getJobService().getJobs(WebUtil.getAuthentication(), module, entityId, psf);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return new ArrayList<JobOutVO>();
	}

	public JobModule getModule() {
		return module;
	}

	@Override
	protected JobOutVO getRowElement(Long id) {
		try {
			return WebUtil.getServiceLocator().getJobService().getJob(WebUtil.getAuthentication(), id);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
		return null;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public void setModule(JobModule module) {
		this.module = module;
	}
}
