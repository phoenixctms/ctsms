package org.phoenixctms.ctsms.adapt;

import java.sql.Timestamp;
import java.util.Iterator;

import org.phoenixctms.ctsms.domain.Proband;
import org.phoenixctms.ctsms.domain.ProbandListStatusEntry;
import org.phoenixctms.ctsms.domain.ProbandListStatusType;
import org.phoenixctms.ctsms.domain.Trial;
import org.phoenixctms.ctsms.enumeration.VariablePeriod;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.date.DateCalc;

public abstract class BlockingProbandListStatusCollisionFinder<IN, ENTITY> extends CollisionFinder<IN, ENTITY, Proband> {

	protected abstract ProbandListStatusEntry getLastStatus(ENTITY existing);

	protected abstract Timestamp getRealTimestamp(IN in);

	protected abstract Trial getTrial(IN in) throws ServiceException;

	/**
	 * True if the status can leave itself (i.e. has at least one transition to a different status).
	 * Self-only transitions are treated as terminal, same as an empty transition set.
	 */
	private static boolean hasOutgoingTransitions(ProbandListStatusType statusType) {
		Iterator<ProbandListStatusType> it = statusType.getTransitions().iterator();
		while (it.hasNext()) {
			ProbandListStatusType transition = it.next();
			if (transition != null && !statusType.getId().equals(transition.getId())) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected boolean match(IN in,
			ENTITY existing, Proband root) throws ServiceException {
		Trial trial = getTrial(in);
		if (trial.isExclusiveProbands()) {
			ProbandListStatusEntry lastStatus = getLastStatus(existing);
			if (lastStatus == null) {
				return false;
			} else {
				ProbandListStatusType statusType = lastStatus.getStatus();
				if (hasOutgoingTransitions(statusType)) {
					return statusType.isBlocking();
				} else if (statusType.isBlocking()) {
					VariablePeriod blockingPeriod;
					if ((blockingPeriod = trial.getBlockingPeriod()) != null) {
						return lastStatus.getRealTimestamp().compareTo(DateCalc.subInterval(getRealTimestamp(in), blockingPeriod, trial.getBlockingPeriodDays())) > 0;
					}
				}
			}
		}
		return false;
	}
}
