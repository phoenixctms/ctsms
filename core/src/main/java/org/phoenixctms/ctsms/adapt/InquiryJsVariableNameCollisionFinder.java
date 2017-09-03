package org.phoenixctms.ctsms.adapt;

import java.util.Collection;

import org.hibernate.LockMode;

import org.phoenixctms.ctsms.domain.Inquiry;
import org.phoenixctms.ctsms.domain.InquiryDao;
import org.phoenixctms.ctsms.domain.Trial;
import org.phoenixctms.ctsms.domain.TrialDao;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.vo.InquiryInVO;

public class InquiryJsVariableNameCollisionFinder extends CollisionFinder<InquiryInVO, Inquiry, Trial> {

	private TrialDao trialDao;
	private InquiryDao inquiryDao;

	public InquiryJsVariableNameCollisionFinder(TrialDao trialDao, InquiryDao inquiryDao) {
		this.inquiryDao = inquiryDao;
		this.trialDao = trialDao;
	}

	@Override
	protected Trial aquireWriteLock(InquiryInVO in)
			throws ServiceException {
		return CheckIDUtil.checkTrialId(in.getTrialId(), trialDao, LockMode.PESSIMISTIC_WRITE);
	}

	@Override
	protected boolean equals(InquiryInVO in,
			Inquiry existing) {
		return in.getId().equals(existing.getId());
	}

	@Override
	protected Collection<Inquiry> getCollidingItems(
			InquiryInVO in, Trial root) {
		return inquiryDao.findByTrialJsVariableName(in.getTrialId(), in.getJsVariableName());
	}

	@Override
	protected boolean isNew(InquiryInVO in) {
		return in.getId() == null;
	}
}
