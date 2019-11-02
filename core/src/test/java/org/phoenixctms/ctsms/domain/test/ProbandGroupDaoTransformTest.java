// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.ProbandGroup
*/
@Test(groups={"transform","ProbandGroupDao"})
public class ProbandGroupDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method ProbandGroupDao.toProbandGroupInVO
   *
   * @see org.phoenixctms.ctsms.domain.ProbandGroupDao#toProbandGroupInVO(org.phoenixctms.ctsms.domain.ProbandGroup source, org.phoenixctms.ctsms.vo.ProbandGroupInVO target)
   */
  @Test
  public void testToProbandGroupInVO() {
    Assert.fail("Test 'ProbandGroupDaoTransformTest.testToProbandGroupInVO' not implemented!");
  }

    /**
   * Test for method probandGroupInVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.ProbandGroupDao#probandGroupInVOToEntity(org.phoenixctms.ctsms.vo.ProbandGroupInVO source, org.phoenixctms.ctsms.domain.ProbandGroup target, boolean copyIfNull)
   */
  @Test
  public void testProbandGroupInVOToEntity() {
    Assert.fail("Test 'ProbandGroupDaoTransformTest.testProbandGroupInVOToEntity' not implemented!");
  }

  /**
   * Test for method ProbandGroupDao.toProbandGroupOutVO
   *
   * @see org.phoenixctms.ctsms.domain.ProbandGroupDao#toProbandGroupOutVO(org.phoenixctms.ctsms.domain.ProbandGroup source, org.phoenixctms.ctsms.vo.ProbandGroupOutVO target)
   */
  @Test
  public void testToProbandGroupOutVO() {
    Assert.fail("Test 'ProbandGroupDaoTransformTest.testToProbandGroupOutVO' not implemented!");
  }

    /**
   * Test for method probandGroupOutVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.ProbandGroupDao#probandGroupOutVOToEntity(org.phoenixctms.ctsms.vo.ProbandGroupOutVO source, org.phoenixctms.ctsms.domain.ProbandGroup target, boolean copyIfNull)
   */
  @Test
  public void testProbandGroupOutVOToEntity() {
    Assert.fail("Test 'ProbandGroupDaoTransformTest.testProbandGroupOutVOToEntity' not implemented!");
  }

}