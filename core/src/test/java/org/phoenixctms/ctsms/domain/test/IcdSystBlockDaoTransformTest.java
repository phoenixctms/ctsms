// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.IcdSystBlock
*/
@Test(groups={"transform","IcdSystBlockDao"})
public class IcdSystBlockDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method IcdSystBlockDao.toIcdSystBlockVO
   *
   * @see org.phoenixctms.ctsms.domain.IcdSystBlockDao#toIcdSystBlockVO(org.phoenixctms.ctsms.domain.IcdSystBlock source, org.phoenixctms.ctsms.vo.IcdSystBlockVO target)
   */
  @Test
  public void testToIcdSystBlockVO() {
    Assert.fail("Test 'IcdSystBlockDaoTransformTest.testToIcdSystBlockVO' not implemented!");
  }

    /**
   * Test for method icdSystBlockVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.IcdSystBlockDao#icdSystBlockVOToEntity(org.phoenixctms.ctsms.vo.IcdSystBlockVO source, org.phoenixctms.ctsms.domain.IcdSystBlock target, boolean copyIfNull)
   */
  @Test
  public void testIcdSystBlockVOToEntity() {
    Assert.fail("Test 'IcdSystBlockDaoTransformTest.testIcdSystBlockVOToEntity' not implemented!");
  }

}