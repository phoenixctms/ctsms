// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.StaffAddress
*/
@Test(groups={"transform","StaffAddressDao"})
public class StaffAddressDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method StaffAddressDao.toStaffAddressInVO
   *
   * @see org.phoenixctms.ctsms.domain.StaffAddressDao#toStaffAddressInVO(org.phoenixctms.ctsms.domain.StaffAddress source, org.phoenixctms.ctsms.vo.StaffAddressInVO target)
   */
  @Test
  public void testToStaffAddressInVO() {
    Assert.fail("Test 'StaffAddressDaoTransformTest.testToStaffAddressInVO' not implemented!");
  }

    /**
   * Test for method staffAddressInVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.StaffAddressDao#staffAddressInVOToEntity(org.phoenixctms.ctsms.vo.StaffAddressInVO source, org.phoenixctms.ctsms.domain.StaffAddress target, boolean copyIfNull)
   */
  @Test
  public void testStaffAddressInVOToEntity() {
    Assert.fail("Test 'StaffAddressDaoTransformTest.testStaffAddressInVOToEntity' not implemented!");
  }

  /**
   * Test for method StaffAddressDao.toStaffAddressOutVO
   *
   * @see org.phoenixctms.ctsms.domain.StaffAddressDao#toStaffAddressOutVO(org.phoenixctms.ctsms.domain.StaffAddress source, org.phoenixctms.ctsms.vo.StaffAddressOutVO target)
   */
  @Test
  public void testToStaffAddressOutVO() {
    Assert.fail("Test 'StaffAddressDaoTransformTest.testToStaffAddressOutVO' not implemented!");
  }

    /**
   * Test for method staffAddressOutVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.StaffAddressDao#staffAddressOutVOToEntity(org.phoenixctms.ctsms.vo.StaffAddressOutVO source, org.phoenixctms.ctsms.domain.StaffAddress target, boolean copyIfNull)
   */
  @Test
  public void testStaffAddressOutVOToEntity() {
    Assert.fail("Test 'StaffAddressDaoTransformTest.testStaffAddressOutVOToEntity' not implemented!");
  }

}