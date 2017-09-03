// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.User
*/
@Test(groups={"transform","UserDao"})
public class UserDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method UserDao.toUserOutVO
   *
   * @see org.phoenixctms.ctsms.domain.UserDao#toUserOutVO(org.phoenixctms.ctsms.domain.User source, org.phoenixctms.ctsms.vo.UserOutVO target)
   */
  @Test
  public void testToUserOutVO() {
    Assert.fail("Test 'UserDaoTransformTest.testToUserOutVO' not implemented!");
  }

    /**
   * Test for method userOutVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.UserDao#userOutVOToEntity(org.phoenixctms.ctsms.vo.UserOutVO source, org.phoenixctms.ctsms.domain.User target, boolean copyIfNull)
   */
  @Test
  public void testUserOutVOToEntity() {
    Assert.fail("Test 'UserDaoTransformTest.testUserOutVOToEntity' not implemented!");
  }

  /**
   * Test for method UserDao.toUserInVO
   *
   * @see org.phoenixctms.ctsms.domain.UserDao#toUserInVO(org.phoenixctms.ctsms.domain.User source, org.phoenixctms.ctsms.vo.UserInVO target)
   */
  @Test
  public void testToUserInVO() {
    Assert.fail("Test 'UserDaoTransformTest.testToUserInVO' not implemented!");
  }

    /**
   * Test for method userInVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.UserDao#userInVOToEntity(org.phoenixctms.ctsms.vo.UserInVO source, org.phoenixctms.ctsms.domain.User target, boolean copyIfNull)
   */
  @Test
  public void testUserInVOToEntity() {
    Assert.fail("Test 'UserDaoTransformTest.testUserInVOToEntity' not implemented!");
  }

}