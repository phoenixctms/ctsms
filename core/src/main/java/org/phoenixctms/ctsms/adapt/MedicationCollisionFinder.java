package org.phoenixctms.ctsms.adapt;

import java.util.Collection;
import java.util.HashSet;

import org.hibernate.LockMode;
import org.phoenixctms.ctsms.domain.Medication;
import org.phoenixctms.ctsms.domain.MedicationDao;
import org.phoenixctms.ctsms.domain.Proband;
import org.phoenixctms.ctsms.domain.ProbandDao;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.MedicationInVO;

public class MedicationCollisionFinder extends CollisionFinder<MedicationInVO, Medication, Proband> {

	private ProbandDao probandDao;
	private MedicationDao medicationDao;

	public MedicationCollisionFinder(ProbandDao probandDao, MedicationDao medicationDao) {
		this.medicationDao = medicationDao;
		this.probandDao = probandDao;
	}

	@Override
	protected Proband aquireWriteLock(MedicationInVO in) throws ServiceException {
		return CheckIDUtil.checkProbandId(in.getProbandId(), probandDao, LockMode.PESSIMISTIC_WRITE);
	}

	@Override
	protected boolean equals(MedicationInVO in, Medication existing) {
		return in.getId().equals(existing.getId());
	}

	@Override
	protected Collection<Medication> getCollidingItems(
			MedicationInVO in, Proband root) {
		if (in.getAspId() != null) {
			return medicationDao.findCollidingProbandDiagnosisProcedureAspSubstancesInterval(in.getProbandId(), in.getDiagnosisId(), in.getProcedureId(), in.getAspId(), null,
					CommonUtil.dateToTimestamp(in.getStart()),
					CommonUtil.dateToTimestamp(in.getStop()));
		} else {
			return medicationDao.findCollidingProbandDiagnosisProcedureAspSubstancesInterval(in.getProbandId(), in.getDiagnosisId(), in.getProcedureId(), null,
					new HashSet<Long>(in.getSubstanceIds()),
					CommonUtil.dateToTimestamp(in.getStart()),
					CommonUtil.dateToTimestamp(in.getStop()));
		}
		// .findByProbandCodeInterval(in.getProbandId(), in.getCodeId(), CommonUtil.dateToTimestamp(in.getStart()), CommonUtil.dateToTimestamp(in.getStop()));
	}

	@Override
	protected boolean isNew(MedicationInVO in) {
		return in.getId() == null;
	}
}
