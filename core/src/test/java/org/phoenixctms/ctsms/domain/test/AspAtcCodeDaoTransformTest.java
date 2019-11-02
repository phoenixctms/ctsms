// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.AspAtcCode
*/
@Test(groups={"transform","AspAtcCodeDao"})
public class AspAtcCodeDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method AspAtcCodeDao.toAspAtcCodeVO
   *
   * @see org.phoenixctms.ctsms.domain.AspAtcCodeDao#toAspAtcCodeVO(org.phoenixctms.ctsms.domain.AspAtcCode source, org.phoenixctms.ctsms.vo.AspAtcCodeVO target)
   */
  @Test
  public void testToAspAtcCodeVO() {
    Assert.fail("Test 'AspAtcCodeDaoTransformTest.testToAspAtcCodeVO' not implemented!");
  }

    /**
   * Test for method aspAtcCodeVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.AspAtcCodeDao#aspAtcCodeVOToEntity(org.phoenixctms.ctsms.vo.AspAtcCodeVO source, org.phoenixctms.ctsms.domain.AspAtcCode target, boolean copyIfNull)
   */
  @Test
  public void testAspAtcCodeVOToEntity() {
    Assert.fail("Test 'AspAtcCodeDaoTransformTest.testAspAtcCodeVOToEntity' not implemented!");
  }

}