package org.phoenixctms.ctsms.adapt;

import java.util.Collection;

import org.hibernate.LockMode;

import org.phoenixctms.ctsms.domain.TeamMember;
import org.phoenixctms.ctsms.domain.TeamMemberDao;
import org.phoenixctms.ctsms.domain.Trial;
import org.phoenixctms.ctsms.domain.TrialDao;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.vo.TeamMemberInVO;

public class TeamMemberRoleCollisionFinder extends CollisionFinder<TeamMemberInVO, TeamMember, Trial> {

	private TrialDao trialDao;
	protected TeamMemberDao teamMemberDao;

	public TeamMemberRoleCollisionFinder(TrialDao trialDao, TeamMemberDao teamMemberDao) {
		this.teamMemberDao = teamMemberDao;
		this.trialDao = trialDao;
	}

	@Override
	protected Trial aquireWriteLock(TeamMemberInVO in)
			throws ServiceException {
		return CheckIDUtil.checkTrialId(in.getTrialId(), trialDao, LockMode.PESSIMISTIC_WRITE);
	}

	@Override
	protected boolean equals(TeamMemberInVO in,
			TeamMember existing) {
		return in.getId().equals(existing.getId());
	}

	@Override
	protected Collection<TeamMember> getCollidingItems(
			TeamMemberInVO in, Trial root) {
		return teamMemberDao.findByTrialStaffRole(in.getTrialId(), in.getStaffId(), in.getRoleId(), null, null);
	}

	@Override
	protected boolean isNew(TeamMemberInVO in) {
		return in.getId() == null;
	}
}