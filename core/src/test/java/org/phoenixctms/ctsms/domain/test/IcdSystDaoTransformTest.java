// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.IcdSyst
*/
@Test(groups={"transform","IcdSystDao"})
public class IcdSystDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method IcdSystDao.toIcdSystVO
   *
   * @see org.phoenixctms.ctsms.domain.IcdSystDao#toIcdSystVO(org.phoenixctms.ctsms.domain.IcdSyst source, org.phoenixctms.ctsms.vo.IcdSystVO target)
   */
  @Test
  public void testToIcdSystVO() {
    Assert.fail("Test 'IcdSystDaoTransformTest.testToIcdSystVO' not implemented!");
  }

    /**
   * Test for method icdSystVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.IcdSystDao#icdSystVOToEntity(org.phoenixctms.ctsms.vo.IcdSystVO source, org.phoenixctms.ctsms.domain.IcdSyst target, boolean copyIfNull)
   */
  @Test
  public void testIcdSystVOToEntity() {
    Assert.fail("Test 'IcdSystDaoTransformTest.testIcdSystVOToEntity' not implemented!");
  }

}