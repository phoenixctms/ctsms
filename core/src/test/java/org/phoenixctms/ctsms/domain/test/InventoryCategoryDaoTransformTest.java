// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.InventoryCategory
*/
@Test(groups={"transform","InventoryCategoryDao"})
public class InventoryCategoryDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method InventoryCategoryDao.toInventoryCategoryVO
   *
   * @see org.phoenixctms.ctsms.domain.InventoryCategoryDao#toInventoryCategoryVO(org.phoenixctms.ctsms.domain.InventoryCategory source, org.phoenixctms.ctsms.vo.InventoryCategoryVO target)
   */
  @Test
  public void testToInventoryCategoryVO() {
    Assert.fail("Test 'InventoryCategoryDaoTransformTest.testToInventoryCategoryVO' not implemented!");
  }

    /**
   * Test for method inventoryCategoryVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.InventoryCategoryDao#inventoryCategoryVOToEntity(org.phoenixctms.ctsms.vo.InventoryCategoryVO source, org.phoenixctms.ctsms.domain.InventoryCategory target, boolean copyIfNull)
   */
  @Test
  public void testInventoryCategoryVOToEntity() {
    Assert.fail("Test 'InventoryCategoryDaoTransformTest.testInventoryCategoryVOToEntity' not implemented!");
  }

}