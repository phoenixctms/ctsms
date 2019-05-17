// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.DataTableColumn
*/
@Test(groups={"transform","DataTableColumnDao"})
public class DataTableColumnDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method DataTableColumnDao.toDataTableColumnVO
   *
   * @see org.phoenixctms.ctsms.domain.DataTableColumnDao#toDataTableColumnVO(org.phoenixctms.ctsms.domain.DataTableColumn source, org.phoenixctms.ctsms.vo.DataTableColumnVO target)
   */
  @Test
  public void testToDataTableColumnVO() {
    Assert.fail("Test 'DataTableColumnDaoTransformTest.testToDataTableColumnVO' not implemented!");
  }

    /**
   * Test for method dataTableColumnVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.DataTableColumnDao#dataTableColumnVOToEntity(org.phoenixctms.ctsms.vo.DataTableColumnVO source, org.phoenixctms.ctsms.domain.DataTableColumn target, boolean copyIfNull)
   */
  @Test
  public void testDataTableColumnVOToEntity() {
    Assert.fail("Test 'DataTableColumnDaoTransformTest.testDataTableColumnVOToEntity' not implemented!");
  }

}