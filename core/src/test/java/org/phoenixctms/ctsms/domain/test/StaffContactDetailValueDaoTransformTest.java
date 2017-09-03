// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.StaffContactDetailValue
*/
@Test(groups={"transform","StaffContactDetailValueDao"})
public class StaffContactDetailValueDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method StaffContactDetailValueDao.toStaffContactDetailValueInVO
   *
   * @see org.phoenixctms.ctsms.domain.StaffContactDetailValueDao#toStaffContactDetailValueInVO(org.phoenixctms.ctsms.domain.StaffContactDetailValue source, org.phoenixctms.ctsms.vo.StaffContactDetailValueInVO target)
   */
  @Test
  public void testToStaffContactDetailValueInVO() {
    Assert.fail("Test 'StaffContactDetailValueDaoTransformTest.testToStaffContactDetailValueInVO' not implemented!");
  }

    /**
   * Test for method staffContactDetailValueInVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.StaffContactDetailValueDao#staffContactDetailValueInVOToEntity(org.phoenixctms.ctsms.vo.StaffContactDetailValueInVO source, org.phoenixctms.ctsms.domain.StaffContactDetailValue target, boolean copyIfNull)
   */
  @Test
  public void testStaffContactDetailValueInVOToEntity() {
    Assert.fail("Test 'StaffContactDetailValueDaoTransformTest.testStaffContactDetailValueInVOToEntity' not implemented!");
  }

  /**
   * Test for method StaffContactDetailValueDao.toStaffContactDetailValueOutVO
   *
   * @see org.phoenixctms.ctsms.domain.StaffContactDetailValueDao#toStaffContactDetailValueOutVO(org.phoenixctms.ctsms.domain.StaffContactDetailValue source, org.phoenixctms.ctsms.vo.StaffContactDetailValueOutVO target)
   */
  @Test
  public void testToStaffContactDetailValueOutVO() {
    Assert.fail("Test 'StaffContactDetailValueDaoTransformTest.testToStaffContactDetailValueOutVO' not implemented!");
  }

    /**
   * Test for method staffContactDetailValueOutVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.StaffContactDetailValueDao#staffContactDetailValueOutVOToEntity(org.phoenixctms.ctsms.vo.StaffContactDetailValueOutVO source, org.phoenixctms.ctsms.domain.StaffContactDetailValue target, boolean copyIfNull)
   */
  @Test
  public void testStaffContactDetailValueOutVOToEntity() {
    Assert.fail("Test 'StaffContactDetailValueDaoTransformTest.testStaffContactDetailValueOutVOToEntity' not implemented!");
  }

}