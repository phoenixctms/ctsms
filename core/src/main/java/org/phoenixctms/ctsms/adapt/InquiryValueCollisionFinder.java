package org.phoenixctms.ctsms.adapt;

import java.util.Collection;

import org.hibernate.LockMode;

import org.phoenixctms.ctsms.domain.InquiryValue;
import org.phoenixctms.ctsms.domain.InquiryValueDao;
import org.phoenixctms.ctsms.domain.Proband;
import org.phoenixctms.ctsms.domain.ProbandDao;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.vo.InquiryValueInVO;

public class InquiryValueCollisionFinder extends CollisionFinder<InquiryValueInVO, InquiryValue, Proband> {

	private ProbandDao probandDao;
	private InquiryValueDao inquiryValueDao;

	public InquiryValueCollisionFinder(ProbandDao probandDao, InquiryValueDao inquiryValueDao) {
		this.inquiryValueDao = inquiryValueDao;
		this.probandDao = probandDao;
	}

	@Override
	protected Proband aquireWriteLock(InquiryValueInVO in)
			throws ServiceException {
		return CheckIDUtil.checkProbandId(in.getProbandId(), probandDao, LockMode.PESSIMISTIC_WRITE);
	}

	@Override
	protected boolean equals(InquiryValueInVO in,
			InquiryValue existing) {
		return in.getId().equals(existing.getId());
	}

	@Override
	protected Collection<InquiryValue> getCollidingItems(
			InquiryValueInVO in, Proband root) {
		return inquiryValueDao.findByProbandInquiry(in.getProbandId(), in.getInquiryId());
	}

	@Override
	protected boolean isNew(InquiryValueInVO in) {
		return in.getId() == null;
	}
}
