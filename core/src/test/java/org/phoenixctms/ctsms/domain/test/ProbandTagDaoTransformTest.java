// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.ProbandTag
*/
@Test(groups={"transform","ProbandTagDao"})
public class ProbandTagDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method ProbandTagDao.toProbandTagVO
   *
   * @see org.phoenixctms.ctsms.domain.ProbandTagDao#toProbandTagVO(org.phoenixctms.ctsms.domain.ProbandTag source, org.phoenixctms.ctsms.vo.ProbandTagVO target)
   */
  @Test
  public void testToProbandTagVO() {
    Assert.fail("Test 'ProbandTagDaoTransformTest.testToProbandTagVO' not implemented!");
  }

    /**
   * Test for method probandTagVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.ProbandTagDao#probandTagVOToEntity(org.phoenixctms.ctsms.vo.ProbandTagVO source, org.phoenixctms.ctsms.domain.ProbandTag target, boolean copyIfNull)
   */
  @Test
  public void testProbandTagVOToEntity() {
    Assert.fail("Test 'ProbandTagDaoTransformTest.testProbandTagVOToEntity' not implemented!");
  }

}