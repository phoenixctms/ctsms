
package org.phoenixctms.ctsms.adapt;

import org.hibernate.LockMode;
import org.phoenixctms.ctsms.domain.ECRFFieldValue;
import org.phoenixctms.ctsms.domain.ECRFFieldValueDao;
import org.phoenixctms.ctsms.domain.ProbandListEntry;
import org.phoenixctms.ctsms.domain.ProbandListEntryDao;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.vo.ECRFFieldValueInVO;

public class EcrfFieldValueCollisionFinder extends CollisionFinder<ECRFFieldValueInVO, ECRFFieldValue, ProbandListEntry> {

	private ProbandListEntryDao probandListEntryDao;
	private ECRFFieldValueDao ecrfFieldValueDao;

	// private ECRFFieldDao ecrfFieldDao;

	public EcrfFieldValueCollisionFinder(ProbandListEntryDao probandListEntryDao, ECRFFieldValueDao ecrfFieldValueDao) { // , ECRFFieldDao ecrfFieldDao) {
		this.ecrfFieldValueDao = ecrfFieldValueDao;
		this.probandListEntryDao = probandListEntryDao;
		// this.ecrfFieldDao = ecrfFieldDao;
	}

	@Override
	protected ProbandListEntry aquireWriteLock(ECRFFieldValueInVO in)
			throws ServiceException {
		return CheckIDUtil.checkProbandListEntryId(in.getListEntryId(), probandListEntryDao, LockMode.PESSIMISTIC_WRITE);
	}

	@Override
	protected boolean equals(ECRFFieldValueInVO in,
			ECRFFieldValue existing) {
		return in.getId().equals(existing.getId());
	}

	@Override
	protected ECRFFieldValue getCollidingItem(
			ECRFFieldValueInVO in, ProbandListEntry root) throws ServiceException {
		// ECRFField ecrfField = ServiceUtil.checkEcrfFieldId(in.getEcrfFieldId(), ecrfFieldDao);
		// in.getIndex() != null ? new Long(in.getIndex()) : null
		return ecrfFieldValueDao.getByListEntryEcrfFieldIndex(in.getListEntryId(), in.getEcrfFieldId(), in.getIndex());
	}

	@Override
	protected boolean isNew(ECRFFieldValueInVO in) {
		return in.getId() == null;
	}
}
