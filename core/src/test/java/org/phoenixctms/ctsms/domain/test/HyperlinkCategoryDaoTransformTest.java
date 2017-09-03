// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.HyperlinkCategory
*/
@Test(groups={"transform","HyperlinkCategoryDao"})
public class HyperlinkCategoryDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method HyperlinkCategoryDao.toHyperlinkCategoryVO
   *
   * @see org.phoenixctms.ctsms.domain.HyperlinkCategoryDao#toHyperlinkCategoryVO(org.phoenixctms.ctsms.domain.HyperlinkCategory source, org.phoenixctms.ctsms.vo.HyperlinkCategoryVO target)
   */
  @Test
  public void testToHyperlinkCategoryVO() {
    Assert.fail("Test 'HyperlinkCategoryDaoTransformTest.testToHyperlinkCategoryVO' not implemented!");
  }

    /**
   * Test for method hyperlinkCategoryVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.HyperlinkCategoryDao#hyperlinkCategoryVOToEntity(org.phoenixctms.ctsms.vo.HyperlinkCategoryVO source, org.phoenixctms.ctsms.domain.HyperlinkCategory target, boolean copyIfNull)
   */
  @Test
  public void testHyperlinkCategoryVOToEntity() {
    Assert.fail("Test 'HyperlinkCategoryDaoTransformTest.testHyperlinkCategoryVOToEntity' not implemented!");
  }

}