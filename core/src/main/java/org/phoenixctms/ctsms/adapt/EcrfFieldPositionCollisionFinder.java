package org.phoenixctms.ctsms.adapt;

import java.util.Collection;

import org.hibernate.LockMode;
import org.phoenixctms.ctsms.domain.ECRF;
import org.phoenixctms.ctsms.domain.ECRFDao;
import org.phoenixctms.ctsms.domain.ECRFField;
import org.phoenixctms.ctsms.domain.ECRFFieldDao;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.vo.ECRFFieldInVO;

public class EcrfFieldPositionCollisionFinder extends CollisionFinder<ECRFFieldInVO, ECRFField, ECRF> {

	private ECRFDao ecrfDao;
	private ECRFFieldDao ecrfFieldDao;

	public EcrfFieldPositionCollisionFinder(ECRFDao ecrfDao, ECRFFieldDao ecrfFieldDao) {
		this.ecrfFieldDao = ecrfFieldDao;
		this.ecrfDao = ecrfDao;
	}

	@Override
	protected ECRF aquireWriteLock(ECRFFieldInVO in)
			throws ServiceException {
		return CheckIDUtil.checkEcrfId(in.getEcrfId(), ecrfDao, LockMode.PESSIMISTIC_WRITE);
	}

	@Override
	protected boolean equals(ECRFFieldInVO in,
			ECRFField existing) {
		return in.getId().equals(existing.getId());
	}

	@Override
	protected Collection<ECRFField> getCollidingItems(
			ECRFFieldInVO in, ECRF root) {
		return ecrfFieldDao.findByEcrfSectionPosition(in.getEcrfId(), in.getSection(), in.getPosition());
	}

	@Override
	protected boolean isNew(ECRFFieldInVO in) {
		return in.getId() == null;
	}
}
