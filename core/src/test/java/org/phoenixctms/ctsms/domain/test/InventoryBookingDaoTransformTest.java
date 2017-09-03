// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.InventoryBooking
*/
@Test(groups={"transform","InventoryBookingDao"})
public class InventoryBookingDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method InventoryBookingDao.toInventoryBookingInVO
   *
   * @see org.phoenixctms.ctsms.domain.InventoryBookingDao#toInventoryBookingInVO(org.phoenixctms.ctsms.domain.InventoryBooking source, org.phoenixctms.ctsms.vo.InventoryBookingInVO target)
   */
  @Test
  public void testToInventoryBookingInVO() {
    Assert.fail("Test 'InventoryBookingDaoTransformTest.testToInventoryBookingInVO' not implemented!");
  }

    /**
   * Test for method inventoryBookingInVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.InventoryBookingDao#inventoryBookingInVOToEntity(org.phoenixctms.ctsms.vo.InventoryBookingInVO source, org.phoenixctms.ctsms.domain.InventoryBooking target, boolean copyIfNull)
   */
  @Test
  public void testInventoryBookingInVOToEntity() {
    Assert.fail("Test 'InventoryBookingDaoTransformTest.testInventoryBookingInVOToEntity' not implemented!");
  }

  /**
   * Test for method InventoryBookingDao.toInventoryBookingOutVO
   *
   * @see org.phoenixctms.ctsms.domain.InventoryBookingDao#toInventoryBookingOutVO(org.phoenixctms.ctsms.domain.InventoryBooking source, org.phoenixctms.ctsms.vo.InventoryBookingOutVO target)
   */
  @Test
  public void testToInventoryBookingOutVO() {
    Assert.fail("Test 'InventoryBookingDaoTransformTest.testToInventoryBookingOutVO' not implemented!");
  }

    /**
   * Test for method inventoryBookingOutVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.InventoryBookingDao#inventoryBookingOutVOToEntity(org.phoenixctms.ctsms.vo.InventoryBookingOutVO source, org.phoenixctms.ctsms.domain.InventoryBooking target, boolean copyIfNull)
   */
  @Test
  public void testInventoryBookingOutVOToEntity() {
    Assert.fail("Test 'InventoryBookingDaoTransformTest.testInventoryBookingOutVOToEntity' not implemented!");
  }

}