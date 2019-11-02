// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.TrialTag
*/
@Test(groups={"transform","TrialTagDao"})
public class TrialTagDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method TrialTagDao.toTrialTagVO
   *
   * @see org.phoenixctms.ctsms.domain.TrialTagDao#toTrialTagVO(org.phoenixctms.ctsms.domain.TrialTag source, org.phoenixctms.ctsms.vo.TrialTagVO target)
   */
  @Test
  public void testToTrialTagVO() {
    Assert.fail("Test 'TrialTagDaoTransformTest.testToTrialTagVO' not implemented!");
  }

    /**
   * Test for method trialTagVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.TrialTagDao#trialTagVOToEntity(org.phoenixctms.ctsms.vo.TrialTagVO source, org.phoenixctms.ctsms.domain.TrialTag target, boolean copyIfNull)
   */
  @Test
  public void testTrialTagVOToEntity() {
    Assert.fail("Test 'TrialTagDaoTransformTest.testTrialTagVOToEntity' not implemented!");
  }

}