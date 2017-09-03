// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.InventoryTag
*/
@Test(groups={"transform","InventoryTagDao"})
public class InventoryTagDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method InventoryTagDao.toInventoryTagVO
   *
   * @see org.phoenixctms.ctsms.domain.InventoryTagDao#toInventoryTagVO(org.phoenixctms.ctsms.domain.InventoryTag source, org.phoenixctms.ctsms.vo.InventoryTagVO target)
   */
  @Test
  public void testToInventoryTagVO() {
    Assert.fail("Test 'InventoryTagDaoTransformTest.testToInventoryTagVO' not implemented!");
  }

    /**
   * Test for method inventoryTagVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.InventoryTagDao#inventoryTagVOToEntity(org.phoenixctms.ctsms.vo.InventoryTagVO source, org.phoenixctms.ctsms.domain.InventoryTag target, boolean copyIfNull)
   */
  @Test
  public void testInventoryTagVOToEntity() {
    Assert.fail("Test 'InventoryTagDaoTransformTest.testInventoryTagVOToEntity' not implemented!");
  }

}