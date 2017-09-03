// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.OpsSystBlock
*/
@Test(groups={"transform","OpsSystBlockDao"})
public class OpsSystBlockDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method OpsSystBlockDao.toOpsSystBlockVO
   *
   * @see org.phoenixctms.ctsms.domain.OpsSystBlockDao#toOpsSystBlockVO(org.phoenixctms.ctsms.domain.OpsSystBlock source, org.phoenixctms.ctsms.vo.OpsSystBlockVO target)
   */
  @Test
  public void testToOpsSystBlockVO() {
    Assert.fail("Test 'OpsSystBlockDaoTransformTest.testToOpsSystBlockVO' not implemented!");
  }

    /**
   * Test for method opsSystBlockVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.OpsSystBlockDao#opsSystBlockVOToEntity(org.phoenixctms.ctsms.vo.OpsSystBlockVO source, org.phoenixctms.ctsms.domain.OpsSystBlock target, boolean copyIfNull)
   */
  @Test
  public void testOpsSystBlockVOToEntity() {
    Assert.fail("Test 'OpsSystBlockDaoTransformTest.testOpsSystBlockVOToEntity' not implemented!");
  }

}