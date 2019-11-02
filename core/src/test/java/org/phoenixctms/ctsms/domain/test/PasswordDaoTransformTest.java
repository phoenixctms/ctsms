// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.Password
*/
@Test(groups={"transform","PasswordDao"})
public class PasswordDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method PasswordDao.toPasswordOutVO
   *
   * @see org.phoenixctms.ctsms.domain.PasswordDao#toPasswordOutVO(org.phoenixctms.ctsms.domain.Password source, org.phoenixctms.ctsms.vo.PasswordOutVO target)
   */
  @Test
  public void testToPasswordOutVO() {
    Assert.fail("Test 'PasswordDaoTransformTest.testToPasswordOutVO' not implemented!");
  }

    /**
   * Test for method passwordOutVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.PasswordDao#passwordOutVOToEntity(org.phoenixctms.ctsms.vo.PasswordOutVO source, org.phoenixctms.ctsms.domain.Password target, boolean copyIfNull)
   */
  @Test
  public void testPasswordOutVOToEntity() {
    Assert.fail("Test 'PasswordDaoTransformTest.testPasswordOutVOToEntity' not implemented!");
  }

  /**
   * Test for method PasswordDao.toPasswordInVO
   *
   * @see org.phoenixctms.ctsms.domain.PasswordDao#toPasswordInVO(org.phoenixctms.ctsms.domain.Password source, org.phoenixctms.ctsms.vo.PasswordInVO target)
   */
  @Test
  public void testToPasswordInVO() {
    Assert.fail("Test 'PasswordDaoTransformTest.testToPasswordInVO' not implemented!");
  }

    /**
   * Test for method passwordInVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.PasswordDao#passwordInVOToEntity(org.phoenixctms.ctsms.vo.PasswordInVO source, org.phoenixctms.ctsms.domain.Password target, boolean copyIfNull)
   */
  @Test
  public void testPasswordInVOToEntity() {
    Assert.fail("Test 'PasswordDaoTransformTest.testPasswordInVOToEntity' not implemented!");
  }

}