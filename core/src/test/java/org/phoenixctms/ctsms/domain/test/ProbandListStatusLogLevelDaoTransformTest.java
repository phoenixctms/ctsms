// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.ProbandListStatusLogLevel
*/
@Test(groups={"transform","ProbandListStatusLogLevelDao"})
public class ProbandListStatusLogLevelDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method ProbandListStatusLogLevelDao.toProbandListStatusLogLevelVO
   *
   * @see org.phoenixctms.ctsms.domain.ProbandListStatusLogLevelDao#toProbandListStatusLogLevelVO(org.phoenixctms.ctsms.domain.ProbandListStatusLogLevel source, org.phoenixctms.ctsms.vo.ProbandListStatusLogLevelVO target)
   */
  @Test
  public void testToProbandListStatusLogLevelVO() {
    Assert.fail("Test 'ProbandListStatusLogLevelDaoTransformTest.testToProbandListStatusLogLevelVO' not implemented!");
  }

    /**
   * Test for method probandListStatusLogLevelVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.ProbandListStatusLogLevelDao#probandListStatusLogLevelVOToEntity(org.phoenixctms.ctsms.vo.ProbandListStatusLogLevelVO source, org.phoenixctms.ctsms.domain.ProbandListStatusLogLevel target, boolean copyIfNull)
   */
  @Test
  public void testProbandListStatusLogLevelVOToEntity() {
    Assert.fail("Test 'ProbandListStatusLogLevelDaoTransformTest.testProbandListStatusLogLevelVOToEntity' not implemented!");
  }

}