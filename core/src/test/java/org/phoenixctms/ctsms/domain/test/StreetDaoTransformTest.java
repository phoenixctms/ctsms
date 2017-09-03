// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.Street
*/
@Test(groups={"transform","StreetDao"})
public class StreetDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method StreetDao.toStreetVO
   *
   * @see org.phoenixctms.ctsms.domain.StreetDao#toStreetVO(org.phoenixctms.ctsms.domain.Street source, org.phoenixctms.ctsms.vo.StreetVO target)
   */
  @Test
  public void testToStreetVO() {
    Assert.fail("Test 'StreetDaoTransformTest.testToStreetVO' not implemented!");
  }

    /**
   * Test for method streetVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.StreetDao#streetVOToEntity(org.phoenixctms.ctsms.vo.StreetVO source, org.phoenixctms.ctsms.domain.Street target, boolean copyIfNull)
   */
  @Test
  public void testStreetVOToEntity() {
    Assert.fail("Test 'StreetDaoTransformTest.testStreetVOToEntity' not implemented!");
  }

}