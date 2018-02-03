package org.phoenixctms.ctsms.adapt;

import java.util.Collection;

import org.hibernate.LockMode;
import org.phoenixctms.ctsms.domain.MassMail;
import org.phoenixctms.ctsms.domain.MassMailDao;
import org.phoenixctms.ctsms.domain.MassMailRecipient;
import org.phoenixctms.ctsms.domain.MassMailRecipientDao;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.vo.MassMailRecipientInVO;

public class MassMailRecipientCollisionFinder extends CollisionFinder<MassMailRecipientInVO, MassMailRecipient, MassMail> {

	private MassMailDao massMailDao;
	private MassMailRecipientDao massMailRecipientDao;

	public MassMailRecipientCollisionFinder(MassMailDao massMailDao, MassMailRecipientDao massMailRecipientDao) {
		this.massMailRecipientDao = massMailRecipientDao;
		this.massMailDao = massMailDao;
	}

	@Override
	protected MassMail aquireWriteLock(MassMailRecipientInVO in) throws ServiceException {
		return CheckIDUtil.checkMassMailId(in.getMassMailId(), massMailDao, LockMode.PESSIMISTIC_WRITE);
	}

	@Override
	protected boolean equals(MassMailRecipientInVO in,
			MassMailRecipient existing) {
		return in.getId().equals(existing.getId());
	}

	@Override
	protected Collection<MassMailRecipient> getCollidingItems(
			MassMailRecipientInVO in, MassMail root) {
		return massMailRecipientDao.findByMassMailProband(in.getMassMailId(), in.getProbandId(), null);
	}

	@Override
	protected boolean isNew(MassMailRecipientInVO in) {
		return in.getId() == null;
	}
}