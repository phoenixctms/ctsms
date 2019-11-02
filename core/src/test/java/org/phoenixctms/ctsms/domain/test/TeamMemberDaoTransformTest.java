// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.TeamMember
*/
@Test(groups={"transform","TeamMemberDao"})
public class TeamMemberDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method TeamMemberDao.toTeamMemberInVO
   *
   * @see org.phoenixctms.ctsms.domain.TeamMemberDao#toTeamMemberInVO(org.phoenixctms.ctsms.domain.TeamMember source, org.phoenixctms.ctsms.vo.TeamMemberInVO target)
   */
  @Test
  public void testToTeamMemberInVO() {
    Assert.fail("Test 'TeamMemberDaoTransformTest.testToTeamMemberInVO' not implemented!");
  }

    /**
   * Test for method teamMemberInVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.TeamMemberDao#teamMemberInVOToEntity(org.phoenixctms.ctsms.vo.TeamMemberInVO source, org.phoenixctms.ctsms.domain.TeamMember target, boolean copyIfNull)
   */
  @Test
  public void testTeamMemberInVOToEntity() {
    Assert.fail("Test 'TeamMemberDaoTransformTest.testTeamMemberInVOToEntity' not implemented!");
  }

  /**
   * Test for method TeamMemberDao.toTeamMemberOutVO
   *
   * @see org.phoenixctms.ctsms.domain.TeamMemberDao#toTeamMemberOutVO(org.phoenixctms.ctsms.domain.TeamMember source, org.phoenixctms.ctsms.vo.TeamMemberOutVO target)
   */
  @Test
  public void testToTeamMemberOutVO() {
    Assert.fail("Test 'TeamMemberDaoTransformTest.testToTeamMemberOutVO' not implemented!");
  }

    /**
   * Test for method teamMemberOutVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.TeamMemberDao#teamMemberOutVOToEntity(org.phoenixctms.ctsms.vo.TeamMemberOutVO source, org.phoenixctms.ctsms.domain.TeamMember target, boolean copyIfNull)
   */
  @Test
  public void testTeamMemberOutVOToEntity() {
    Assert.fail("Test 'TeamMemberDaoTransformTest.testTeamMemberOutVOToEntity' not implemented!");
  }

}