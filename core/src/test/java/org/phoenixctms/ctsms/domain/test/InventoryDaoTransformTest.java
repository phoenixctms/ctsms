// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.Inventory
*/
@Test(groups={"transform","InventoryDao"})
public class InventoryDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method InventoryDao.toInventoryInVO
   *
   * @see org.phoenixctms.ctsms.domain.InventoryDao#toInventoryInVO(org.phoenixctms.ctsms.domain.Inventory source, org.phoenixctms.ctsms.vo.InventoryInVO target)
   */
  @Test
  public void testToInventoryInVO() {
    Assert.fail("Test 'InventoryDaoTransformTest.testToInventoryInVO' not implemented!");
  }

    /**
   * Test for method inventoryInVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.InventoryDao#inventoryInVOToEntity(org.phoenixctms.ctsms.vo.InventoryInVO source, org.phoenixctms.ctsms.domain.Inventory target, boolean copyIfNull)
   */
  @Test
  public void testInventoryInVOToEntity() {
    Assert.fail("Test 'InventoryDaoTransformTest.testInventoryInVOToEntity' not implemented!");
  }

  /**
   * Test for method InventoryDao.toInventoryOutVO
   *
   * @see org.phoenixctms.ctsms.domain.InventoryDao#toInventoryOutVO(org.phoenixctms.ctsms.domain.Inventory source, org.phoenixctms.ctsms.vo.InventoryOutVO target)
   */
  @Test
  public void testToInventoryOutVO() {
    Assert.fail("Test 'InventoryDaoTransformTest.testToInventoryOutVO' not implemented!");
  }

    /**
   * Test for method inventoryOutVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.InventoryDao#inventoryOutVOToEntity(org.phoenixctms.ctsms.vo.InventoryOutVO source, org.phoenixctms.ctsms.domain.Inventory target, boolean copyIfNull)
   */
  @Test
  public void testInventoryOutVOToEntity() {
    Assert.fail("Test 'InventoryDaoTransformTest.testInventoryOutVOToEntity' not implemented!");
  }

}