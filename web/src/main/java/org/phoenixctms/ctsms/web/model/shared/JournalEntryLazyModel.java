package org.phoenixctms.ctsms.web.model.shared;

import java.util.ArrayList;
import java.util.Collection;

import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.JournalEntryOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class JournalEntryLazyModel extends LazyDataModelBase<JournalEntryOutVO> {

	private Long entityId;
	private JournalModule module;

	public Long getEntityId() {
		return entityId;
	}

	@Override
	protected Collection<JournalEntryOutVO> getLazyResult(PSFVO psf) {
		if (module != null && entityId != null) {
			if (psf != null) {
				psf.setUpdateRowCount(false);
			}
			try {
				return WebUtil.getServiceLocator().getJournalService().getJournal(WebUtil.getAuthentication(), module, entityId, psf);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return new ArrayList<JournalEntryOutVO>();
	}

	@Override
	public void updateRowCount() {
		setRowCount(CommonUtil.safeLongToInt(CommonUtil.fromCountLimitExceeded(WebUtil.getJournalCountSafe(module, entityId))));
	}

	public JournalModule getModule() {
		return module;
	}

	@Override
	protected JournalEntryOutVO getRowElement(Long id) {
		return WebUtil.getJournalEntry(id);
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public void setModule(JournalModule module) {
		this.module = module;
	}
}
