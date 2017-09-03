// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.InventoryTagValue
*/
@Test(groups={"transform","InventoryTagValueDao"})
public class InventoryTagValueDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method InventoryTagValueDao.toInventoryTagValueInVO
   *
   * @see org.phoenixctms.ctsms.domain.InventoryTagValueDao#toInventoryTagValueInVO(org.phoenixctms.ctsms.domain.InventoryTagValue source, org.phoenixctms.ctsms.vo.InventoryTagValueInVO target)
   */
  @Test
  public void testToInventoryTagValueInVO() {
    Assert.fail("Test 'InventoryTagValueDaoTransformTest.testToInventoryTagValueInVO' not implemented!");
  }

    /**
   * Test for method inventoryTagValueInVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.InventoryTagValueDao#inventoryTagValueInVOToEntity(org.phoenixctms.ctsms.vo.InventoryTagValueInVO source, org.phoenixctms.ctsms.domain.InventoryTagValue target, boolean copyIfNull)
   */
  @Test
  public void testInventoryTagValueInVOToEntity() {
    Assert.fail("Test 'InventoryTagValueDaoTransformTest.testInventoryTagValueInVOToEntity' not implemented!");
  }

  /**
   * Test for method InventoryTagValueDao.toInventoryTagValueOutVO
   *
   * @see org.phoenixctms.ctsms.domain.InventoryTagValueDao#toInventoryTagValueOutVO(org.phoenixctms.ctsms.domain.InventoryTagValue source, org.phoenixctms.ctsms.vo.InventoryTagValueOutVO target)
   */
  @Test
  public void testToInventoryTagValueOutVO() {
    Assert.fail("Test 'InventoryTagValueDaoTransformTest.testToInventoryTagValueOutVO' not implemented!");
  }

    /**
   * Test for method inventoryTagValueOutVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.InventoryTagValueDao#inventoryTagValueOutVOToEntity(org.phoenixctms.ctsms.vo.InventoryTagValueOutVO source, org.phoenixctms.ctsms.domain.InventoryTagValue target, boolean copyIfNull)
   */
  @Test
  public void testInventoryTagValueOutVOToEntity() {
    Assert.fail("Test 'InventoryTagValueDaoTransformTest.testInventoryTagValueOutVOToEntity' not implemented!");
  }

}