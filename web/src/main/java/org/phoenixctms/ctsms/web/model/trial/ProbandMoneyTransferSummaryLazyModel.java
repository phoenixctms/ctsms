package org.phoenixctms.ctsms.web.model.trial;

import java.util.ArrayList;
import java.util.Collection;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.MoneyTransferSummaryVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class ProbandMoneyTransferSummaryLazyModel extends ProbandMoneyTransferSummaryLazyModelBase {

	private boolean total;

	public ProbandMoneyTransferSummaryLazyModel(boolean total) {
		super();
		this.total = total;
	}

	@Override
	protected Collection<MoneyTransferSummaryVO> getLazyResult(PSFVO psf) {
		if (trialId != null) {
			try {
				return WebUtil.getServiceLocator().getTrialService().getProbandMoneyTransferSummaryList(WebUtil.getAuthentication(), trialId, null, null, null, paid, total, psf);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return new ArrayList<MoneyTransferSummaryVO>();
	}

	@Override
	protected MoneyTransferSummaryVO getRowElement(Long id) {
		try {
			return WebUtil.getServiceLocator().getTrialService().getProbandMoneyTransferSummary(WebUtil.getAuthentication(), id, null, null, paid);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
		return null;
	}

	public boolean isTotal() {
		return total;
	}

	public void setTotal(boolean total) {
		this.total = total;
	}
}
