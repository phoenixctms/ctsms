package org.phoenixctms.ctsms.adapt;

import org.hibernate.LockMode;

import org.phoenixctms.ctsms.domain.MoneyTransferDao;
import org.phoenixctms.ctsms.domain.Trial;
import org.phoenixctms.ctsms.domain.TrialDao;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.ServiceExceptionCodes;
import org.phoenixctms.ctsms.vo.MoneyTransferInVO;

public class MaxCostTypesAdapter extends MaxCategoriesAdapter<Trial, MoneyTransferInVO> {

	private MoneyTransferDao moneyTransferDao;
	private TrialDao trialDao;
	private Long allowedCount;

	public MaxCostTypesAdapter(Long allowedCount, TrialDao trialDao, MoneyTransferDao moneyTransferDao) {
		this.trialDao = trialDao;
		this.moneyTransferDao = moneyTransferDao;
		this.allowedCount = allowedCount;
	}

	@Override
	protected Trial aquireWriteLock(MoneyTransferInVO in) throws ServiceException {
		return CheckIDUtil.checkTrialId(in.getTrialId(), trialDao, LockMode.PESSIMISTIC_WRITE);
	}

	@Override
	protected boolean containsNew(MoneyTransferInVO in) {
		return moneyTransferDao.getCostTypeCount(null, in.getTrialId(), in.getCostType()) > 0;
	}

	@Override
	protected Long getAllowedCount() {
		return allowedCount;
	}

	@Override
	protected ServiceException getException(Trial root, Long allowedCount) {
		return L10nUtil.initServiceException(ServiceExceptionCodes.MONEY_TRANSFER_COST_TYPE_MAX_NUMBER_EXCEEDED, CommonUtil.trialOutVOToString(trialDao.toTrialOutVO(root)),
				allowedCount.toString());
	}

	@Override
	protected long getExistingCount(MoneyTransferInVO in) {
		return moneyTransferDao.getCostTypeCount(null, in.getTrialId());
	}
}
