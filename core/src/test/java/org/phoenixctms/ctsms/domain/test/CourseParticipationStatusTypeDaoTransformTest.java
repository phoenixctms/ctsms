// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.CourseParticipationStatusType
*/
@Test(groups={"transform","CourseParticipationStatusTypeDao"})
public class CourseParticipationStatusTypeDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method CourseParticipationStatusTypeDao.toCourseParticipationStatusTypeVO
   *
   * @see org.phoenixctms.ctsms.domain.CourseParticipationStatusTypeDao#toCourseParticipationStatusTypeVO(org.phoenixctms.ctsms.domain.CourseParticipationStatusType source, org.phoenixctms.ctsms.vo.CourseParticipationStatusTypeVO target)
   */
  @Test
  public void testToCourseParticipationStatusTypeVO() {
    Assert.fail("Test 'CourseParticipationStatusTypeDaoTransformTest.testToCourseParticipationStatusTypeVO' not implemented!");
  }

    /**
   * Test for method courseParticipationStatusTypeVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.CourseParticipationStatusTypeDao#courseParticipationStatusTypeVOToEntity(org.phoenixctms.ctsms.vo.CourseParticipationStatusTypeVO source, org.phoenixctms.ctsms.domain.CourseParticipationStatusType target, boolean copyIfNull)
   */
  @Test
  public void testCourseParticipationStatusTypeVOToEntity() {
    Assert.fail("Test 'CourseParticipationStatusTypeDaoTransformTest.testCourseParticipationStatusTypeVOToEntity' not implemented!");
  }

}