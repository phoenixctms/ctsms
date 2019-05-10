// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.TeamMemberRole
*/
@Test(groups = { "transform", "TeamMemberRoleDao" })
public class TeamMemberRoleDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method TeamMemberRoleDao.toTeamMemberRoleVO
	 *
	 * @see org.phoenixctms.ctsms.domain.TeamMemberRoleDao#toTeamMemberRoleVO(org.phoenixctms.ctsms.domain.TeamMemberRole source, org.phoenixctms.ctsms.vo.TeamMemberRoleVO target)
	 */
	@Test
	public void testToTeamMemberRoleVO() {
		Assert.fail("Test 'TeamMemberRoleDaoTransformTest.testToTeamMemberRoleVO' not implemented!");
	}

	/**
	* Test for method teamMemberRoleVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.TeamMemberRoleDao#teamMemberRoleVOToEntity(org.phoenixctms.ctsms.vo.TeamMemberRoleVO source, org.phoenixctms.ctsms.domain.TeamMemberRole target, boolean copyIfNull)
	*/
	@Test
	public void testTeamMemberRoleVOToEntity() {
		Assert.fail("Test 'TeamMemberRoleDaoTransformTest.testTeamMemberRoleVOToEntity' not implemented!");
	}
}