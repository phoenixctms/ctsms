// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.JobType
*/
@Test(groups={"transform","JobTypeDao"})
public class JobTypeDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method JobTypeDao.toJobTypeVO
   *
   * @see org.phoenixctms.ctsms.domain.JobTypeDao#toJobTypeVO(org.phoenixctms.ctsms.domain.JobType source, org.phoenixctms.ctsms.vo.JobTypeVO target)
   */
  @Test
  public void testToJobTypeVO() {
    Assert.fail("Test 'JobTypeDaoTransformTest.testToJobTypeVO' not implemented!");
  }

    /**
   * Test for method jobTypeVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.JobTypeDao#jobTypeVOToEntity(org.phoenixctms.ctsms.vo.JobTypeVO source, org.phoenixctms.ctsms.domain.JobType target, boolean copyIfNull)
   */
  @Test
  public void testJobTypeVOToEntity() {
    Assert.fail("Test 'JobTypeDaoTransformTest.testJobTypeVOToEntity' not implemented!");
  }

}