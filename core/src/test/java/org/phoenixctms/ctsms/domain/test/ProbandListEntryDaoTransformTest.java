// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.ProbandListEntry
*/
@Test(groups={"transform","ProbandListEntryDao"})
public class ProbandListEntryDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method ProbandListEntryDao.toProbandListEntryOutVO
   *
   * @see org.phoenixctms.ctsms.domain.ProbandListEntryDao#toProbandListEntryOutVO(org.phoenixctms.ctsms.domain.ProbandListEntry source, org.phoenixctms.ctsms.vo.ProbandListEntryOutVO target)
   */
  @Test
  public void testToProbandListEntryOutVO() {
    Assert.fail("Test 'ProbandListEntryDaoTransformTest.testToProbandListEntryOutVO' not implemented!");
  }

    /**
   * Test for method probandListEntryOutVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.ProbandListEntryDao#probandListEntryOutVOToEntity(org.phoenixctms.ctsms.vo.ProbandListEntryOutVO source, org.phoenixctms.ctsms.domain.ProbandListEntry target, boolean copyIfNull)
   */
  @Test
  public void testProbandListEntryOutVOToEntity() {
    Assert.fail("Test 'ProbandListEntryDaoTransformTest.testProbandListEntryOutVOToEntity' not implemented!");
  }

  /**
   * Test for method ProbandListEntryDao.toProbandListEntryInVO
   *
   * @see org.phoenixctms.ctsms.domain.ProbandListEntryDao#toProbandListEntryInVO(org.phoenixctms.ctsms.domain.ProbandListEntry source, org.phoenixctms.ctsms.vo.ProbandListEntryInVO target)
   */
  @Test
  public void testToProbandListEntryInVO() {
    Assert.fail("Test 'ProbandListEntryDaoTransformTest.testToProbandListEntryInVO' not implemented!");
  }

    /**
   * Test for method probandListEntryInVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.ProbandListEntryDao#probandListEntryInVOToEntity(org.phoenixctms.ctsms.vo.ProbandListEntryInVO source, org.phoenixctms.ctsms.domain.ProbandListEntry target, boolean copyIfNull)
   */
  @Test
  public void testProbandListEntryInVOToEntity() {
    Assert.fail("Test 'ProbandListEntryDaoTransformTest.testProbandListEntryInVOToEntity' not implemented!");
  }

}