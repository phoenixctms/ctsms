package org.phoenixctms.ctsms.adapt;

import java.util.Collection;

import org.hibernate.LockMode;
import org.phoenixctms.ctsms.domain.Proband;
import org.phoenixctms.ctsms.domain.ProbandDao;
import org.phoenixctms.ctsms.domain.Procedure;
import org.phoenixctms.ctsms.domain.ProcedureDao;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.ProcedureInVO;

public class ProcedureCollisionFinder extends CollisionFinder<ProcedureInVO, Procedure, Proband> {

	private ProbandDao probandDao;
	private ProcedureDao procedureDao;

	public ProcedureCollisionFinder(ProbandDao probandDao, ProcedureDao procedureDao) {
		this.procedureDao = procedureDao;
		this.probandDao = probandDao;
	}

	@Override
	protected Proband aquireWriteLock(ProcedureInVO in)
			throws ServiceException {
		return CheckIDUtil.checkProbandId(in.getProbandId(), probandDao, LockMode.PESSIMISTIC_WRITE);
	}

	@Override
	protected boolean equals(ProcedureInVO in, Procedure existing) {
		return in.getId().equals(existing.getId());
	}

	@Override
	protected Collection<Procedure> getCollidingItems(
			ProcedureInVO in, Proband root) {
		return procedureDao
				.findCollidingProbandCodeInterval(in.getProbandId(), in.getCodeId(), CommonUtil.dateToTimestamp(in.getStart()), CommonUtil.dateToTimestamp(in.getStop()));
	}

	@Override
	protected boolean isNew(ProcedureInVO in) {
		return in.getId() == null;
	}
}
