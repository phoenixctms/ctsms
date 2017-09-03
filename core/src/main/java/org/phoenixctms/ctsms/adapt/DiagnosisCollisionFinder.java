package org.phoenixctms.ctsms.adapt;

import java.util.Collection;

import org.hibernate.LockMode;
import org.phoenixctms.ctsms.domain.Diagnosis;
import org.phoenixctms.ctsms.domain.DiagnosisDao;
import org.phoenixctms.ctsms.domain.Proband;
import org.phoenixctms.ctsms.domain.ProbandDao;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.DiagnosisInVO;

public class DiagnosisCollisionFinder extends CollisionFinder<DiagnosisInVO, Diagnosis, Proband> {

	private ProbandDao probandDao;
	private DiagnosisDao diagnosisDao;

	public DiagnosisCollisionFinder(ProbandDao probandDao, DiagnosisDao diagnosisDao) {
		this.diagnosisDao = diagnosisDao;
		this.probandDao = probandDao;
	}

	@Override
	protected Proband aquireWriteLock(DiagnosisInVO in) throws ServiceException {
		return CheckIDUtil.checkProbandId(in.getProbandId(), probandDao, LockMode.PESSIMISTIC_WRITE);
	}

	@Override
	protected boolean equals(DiagnosisInVO in, Diagnosis existing) {
		return in.getId().equals(existing.getId());
	}

	@Override
	protected Collection<Diagnosis> getCollidingItems(
			DiagnosisInVO in, Proband root) {
		return diagnosisDao
				.findCollidingProbandCodeInterval(in.getProbandId(), in.getCodeId(), CommonUtil.dateToTimestamp(in.getStart()), CommonUtil.dateToTimestamp(in.getStop()));
	}

	@Override
	protected boolean isNew(DiagnosisInVO in) {
		return in.getId() == null;
	}
}
