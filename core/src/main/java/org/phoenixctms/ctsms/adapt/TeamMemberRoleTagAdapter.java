package org.phoenixctms.ctsms.adapt;

import java.util.Collection;

import org.hibernate.LockMode;
import org.phoenixctms.ctsms.domain.TeamMember;
import org.phoenixctms.ctsms.domain.TeamMemberRole;
import org.phoenixctms.ctsms.domain.TeamMemberRoleDao;
import org.phoenixctms.ctsms.domain.Trial;
import org.phoenixctms.ctsms.domain.TrialDao;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.util.ServiceUtil;
import org.phoenixctms.ctsms.vo.TeamMemberInVO;

public class TeamMemberRoleTagAdapter extends TagAdapter<Trial, TeamMemberRole, TeamMember, TeamMemberInVO> {

	private TrialDao trialDao;
	private TeamMemberRoleDao teamMemberRoleDao;

	public TeamMemberRoleTagAdapter(TrialDao trialDao, TeamMemberRoleDao teamMemberRoleDao) {
		this.trialDao = trialDao;
		this.teamMemberRoleDao = teamMemberRoleDao;
	}

	@Override
	protected Trial checkRootId(Long rootId) throws ServiceException {
		return CheckIDUtil.checkTrialId(rootId, trialDao, LockMode.PESSIMISTIC_WRITE);
	}

	@Override
	protected void checkRootTagConstraints(Trial root, TeamMemberRole tag, TeamMemberInVO tagValueIn)
			throws ServiceException {
		ServiceUtil.checkTrialLocked(root);
	}

	@Override
	protected TeamMemberRole checkTagId(Long tagId) throws ServiceException {
		return CheckIDUtil.checkTeamMemberRoleId(tagId, teamMemberRoleDao);
	}

	@Override
	protected boolean checkValueAgainstRegExp(TeamMemberInVO tagValueIn) {
		return true;
	}

	@Override
	protected Collection<TeamMemberRole> findTagsIncludingId(Trial root, Long tagId) {
		return teamMemberRoleDao.findByVisibleId(true, tagId);
	}

	@Override
	protected Integer getMaxOccurrence(TeamMemberRole tag) {
		return tag.getMaxOccurrence();
	}

	@Override
	protected String getMismatchL10nKey(TeamMemberRole tag) {
		return null;
	}

	@Override
	protected String getNameL10nKey(TeamMemberRole tag) {
		return tag.getNameL10nKey();
	}

	@Override
	protected String getRegExp(TeamMemberRole tag) {
		return null;
	}

	@Override
	protected Long getRootId(TeamMemberInVO tagValueIn) {
		return tagValueIn.getTrialId();
	}

	@Override
	protected TeamMemberRole getTag(TeamMember tagValue) {
		return tagValue.getRole();
	}

	@Override
	protected Long getTagId(TeamMemberInVO tagValueIn) {
		return tagValueIn.getRoleId();
	}

	@Override
	protected Collection<TeamMember> getTagValues(Trial root) {
		return root.getMembers();
	}

	@Override
	protected String getValue(TeamMemberInVO tagValueIn) {
		return null;
	}

	@Override
	protected boolean same(TeamMember tagValue, TeamMemberInVO tagValueIn) {
		return tagValue.getId().equals(tagValueIn.getId());
	}

	@Override
	protected void toTagVOCollection(Collection<?> tags) {
		teamMemberRoleDao.toTeamMemberRoleVOCollection(tags);
	}
}
