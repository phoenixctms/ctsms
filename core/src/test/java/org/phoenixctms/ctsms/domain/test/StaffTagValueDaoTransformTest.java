// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.StaffTagValue
*/
@Test(groups={"transform","StaffTagValueDao"})
public class StaffTagValueDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method StaffTagValueDao.toStaffTagValueInVO
   *
   * @see org.phoenixctms.ctsms.domain.StaffTagValueDao#toStaffTagValueInVO(org.phoenixctms.ctsms.domain.StaffTagValue source, org.phoenixctms.ctsms.vo.StaffTagValueInVO target)
   */
  @Test
  public void testToStaffTagValueInVO() {
    Assert.fail("Test 'StaffTagValueDaoTransformTest.testToStaffTagValueInVO' not implemented!");
  }

    /**
   * Test for method staffTagValueInVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.StaffTagValueDao#staffTagValueInVOToEntity(org.phoenixctms.ctsms.vo.StaffTagValueInVO source, org.phoenixctms.ctsms.domain.StaffTagValue target, boolean copyIfNull)
   */
  @Test
  public void testStaffTagValueInVOToEntity() {
    Assert.fail("Test 'StaffTagValueDaoTransformTest.testStaffTagValueInVOToEntity' not implemented!");
  }

  /**
   * Test for method StaffTagValueDao.toStaffTagValueOutVO
   *
   * @see org.phoenixctms.ctsms.domain.StaffTagValueDao#toStaffTagValueOutVO(org.phoenixctms.ctsms.domain.StaffTagValue source, org.phoenixctms.ctsms.vo.StaffTagValueOutVO target)
   */
  @Test
  public void testToStaffTagValueOutVO() {
    Assert.fail("Test 'StaffTagValueDaoTransformTest.testToStaffTagValueOutVO' not implemented!");
  }

    /**
   * Test for method staffTagValueOutVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.StaffTagValueDao#staffTagValueOutVOToEntity(org.phoenixctms.ctsms.vo.StaffTagValueOutVO source, org.phoenixctms.ctsms.domain.StaffTagValue target, boolean copyIfNull)
   */
  @Test
  public void testStaffTagValueOutVOToEntity() {
    Assert.fail("Test 'StaffTagValueDaoTransformTest.testStaffTagValueOutVOToEntity' not implemented!");
  }

}