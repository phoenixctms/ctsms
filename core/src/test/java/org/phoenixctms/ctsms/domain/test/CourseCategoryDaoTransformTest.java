// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.CourseCategory
*/
@Test(groups={"transform","CourseCategoryDao"})
public class CourseCategoryDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method CourseCategoryDao.toCourseCategoryVO
   *
   * @see org.phoenixctms.ctsms.domain.CourseCategoryDao#toCourseCategoryVO(org.phoenixctms.ctsms.domain.CourseCategory source, org.phoenixctms.ctsms.vo.CourseCategoryVO target)
   */
  @Test
  public void testToCourseCategoryVO() {
    Assert.fail("Test 'CourseCategoryDaoTransformTest.testToCourseCategoryVO' not implemented!");
  }

    /**
   * Test for method courseCategoryVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.CourseCategoryDao#courseCategoryVOToEntity(org.phoenixctms.ctsms.vo.CourseCategoryVO source, org.phoenixctms.ctsms.domain.CourseCategory target, boolean copyIfNull)
   */
  @Test
  public void testCourseCategoryVOToEntity() {
    Assert.fail("Test 'CourseCategoryDaoTransformTest.testCourseCategoryVOToEntity' not implemented!");
  }

}