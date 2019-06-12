// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.Job
*/
@Test(groups={"transform","JobDao"})
public class JobDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method JobDao.toJobFileVO
   *
   * @see org.phoenixctms.ctsms.domain.JobDao#toJobFileVO(org.phoenixctms.ctsms.domain.Job source, org.phoenixctms.ctsms.vo.JobFileVO target)
   */
  @Test
  public void testToJobFileVO() {
    Assert.fail("Test 'JobDaoTransformTest.testToJobFileVO' not implemented!");
  }

    /**
   * Test for method jobFileVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.JobDao#jobFileVOToEntity(org.phoenixctms.ctsms.vo.JobFileVO source, org.phoenixctms.ctsms.domain.Job target, boolean copyIfNull)
   */
  @Test
  public void testJobFileVOToEntity() {
    Assert.fail("Test 'JobDaoTransformTest.testJobFileVOToEntity' not implemented!");
  }

  /**
   * Test for method JobDao.toJobInVO
   *
   * @see org.phoenixctms.ctsms.domain.JobDao#toJobInVO(org.phoenixctms.ctsms.domain.Job source, org.phoenixctms.ctsms.vo.JobInVO target)
   */
  @Test
  public void testToJobInVO() {
    Assert.fail("Test 'JobDaoTransformTest.testToJobInVO' not implemented!");
  }

    /**
   * Test for method jobInVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.JobDao#jobInVOToEntity(org.phoenixctms.ctsms.vo.JobInVO source, org.phoenixctms.ctsms.domain.Job target, boolean copyIfNull)
   */
  @Test
  public void testJobInVOToEntity() {
    Assert.fail("Test 'JobDaoTransformTest.testJobInVOToEntity' not implemented!");
  }

  /**
   * Test for method JobDao.toJobOutVO
   *
   * @see org.phoenixctms.ctsms.domain.JobDao#toJobOutVO(org.phoenixctms.ctsms.domain.Job source, org.phoenixctms.ctsms.vo.JobOutVO target)
   */
  @Test
  public void testToJobOutVO() {
    Assert.fail("Test 'JobDaoTransformTest.testToJobOutVO' not implemented!");
  }

    /**
   * Test for method jobOutVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.JobDao#jobOutVOToEntity(org.phoenixctms.ctsms.vo.JobOutVO source, org.phoenixctms.ctsms.domain.Job target, boolean copyIfNull)
   */
  @Test
  public void testJobOutVOToEntity() {
    Assert.fail("Test 'JobDaoTransformTest.testJobOutVOToEntity' not implemented!");
  }

}