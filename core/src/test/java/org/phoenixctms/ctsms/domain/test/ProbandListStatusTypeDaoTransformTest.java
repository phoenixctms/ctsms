// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.ProbandListStatusType
*/
@Test(groups={"transform","ProbandListStatusTypeDao"})
public class ProbandListStatusTypeDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method ProbandListStatusTypeDao.toProbandListStatusTypeVO
   *
   * @see org.phoenixctms.ctsms.domain.ProbandListStatusTypeDao#toProbandListStatusTypeVO(org.phoenixctms.ctsms.domain.ProbandListStatusType source, org.phoenixctms.ctsms.vo.ProbandListStatusTypeVO target)
   */
  @Test
  public void testToProbandListStatusTypeVO() {
    Assert.fail("Test 'ProbandListStatusTypeDaoTransformTest.testToProbandListStatusTypeVO' not implemented!");
  }

    /**
   * Test for method probandListStatusTypeVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.ProbandListStatusTypeDao#probandListStatusTypeVOToEntity(org.phoenixctms.ctsms.vo.ProbandListStatusTypeVO source, org.phoenixctms.ctsms.domain.ProbandListStatusType target, boolean copyIfNull)
   */
  @Test
  public void testProbandListStatusTypeVOToEntity() {
    Assert.fail("Test 'ProbandListStatusTypeDaoTransformTest.testProbandListStatusTypeVOToEntity' not implemented!");
  }

}