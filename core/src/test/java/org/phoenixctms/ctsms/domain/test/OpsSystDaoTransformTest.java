// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.OpsSyst
*/
@Test(groups={"transform","OpsSystDao"})
public class OpsSystDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method OpsSystDao.toOpsSystVO
   *
   * @see org.phoenixctms.ctsms.domain.OpsSystDao#toOpsSystVO(org.phoenixctms.ctsms.domain.OpsSyst source, org.phoenixctms.ctsms.vo.OpsSystVO target)
   */
  @Test
  public void testToOpsSystVO() {
    Assert.fail("Test 'OpsSystDaoTransformTest.testToOpsSystVO' not implemented!");
  }

    /**
   * Test for method opsSystVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.OpsSystDao#opsSystVOToEntity(org.phoenixctms.ctsms.vo.OpsSystVO source, org.phoenixctms.ctsms.domain.OpsSyst target, boolean copyIfNull)
   */
  @Test
  public void testOpsSystVOToEntity() {
    Assert.fail("Test 'OpsSystDaoTransformTest.testOpsSystVOToEntity' not implemented!");
  }

}