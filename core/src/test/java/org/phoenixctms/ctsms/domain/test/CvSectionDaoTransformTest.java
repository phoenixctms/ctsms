// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.CvSection
*/
@Test(groups={"transform","CvSectionDao"})
public class CvSectionDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method CvSectionDao.toCvSectionVO
   *
   * @see org.phoenixctms.ctsms.domain.CvSectionDao#toCvSectionVO(org.phoenixctms.ctsms.domain.CvSection source, org.phoenixctms.ctsms.vo.CvSectionVO target)
   */
  @Test
  public void testToCvSectionVO() {
    Assert.fail("Test 'CvSectionDaoTransformTest.testToCvSectionVO' not implemented!");
  }

    /**
   * Test for method cvSectionVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.CvSectionDao#cvSectionVOToEntity(org.phoenixctms.ctsms.vo.CvSectionVO source, org.phoenixctms.ctsms.domain.CvSection target, boolean copyIfNull)
   */
  @Test
  public void testCvSectionVOToEntity() {
    Assert.fail("Test 'CvSectionDaoTransformTest.testCvSectionVOToEntity' not implemented!");
  }

}