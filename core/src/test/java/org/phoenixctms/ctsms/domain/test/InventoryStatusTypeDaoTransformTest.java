// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.InventoryStatusType
*/
@Test(groups={"transform","InventoryStatusTypeDao"})
public class InventoryStatusTypeDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method InventoryStatusTypeDao.toInventoryStatusTypeVO
   *
   * @see org.phoenixctms.ctsms.domain.InventoryStatusTypeDao#toInventoryStatusTypeVO(org.phoenixctms.ctsms.domain.InventoryStatusType source, org.phoenixctms.ctsms.vo.InventoryStatusTypeVO target)
   */
  @Test
  public void testToInventoryStatusTypeVO() {
    Assert.fail("Test 'InventoryStatusTypeDaoTransformTest.testToInventoryStatusTypeVO' not implemented!");
  }

    /**
   * Test for method inventoryStatusTypeVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.InventoryStatusTypeDao#inventoryStatusTypeVOToEntity(org.phoenixctms.ctsms.vo.InventoryStatusTypeVO source, org.phoenixctms.ctsms.domain.InventoryStatusType target, boolean copyIfNull)
   */
  @Test
  public void testInventoryStatusTypeVOToEntity() {
    Assert.fail("Test 'InventoryStatusTypeDaoTransformTest.testInventoryStatusTypeVOToEntity' not implemented!");
  }

}