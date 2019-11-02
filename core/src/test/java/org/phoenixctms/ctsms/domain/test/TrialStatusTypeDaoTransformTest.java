// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.TrialStatusType
*/
@Test(groups={"transform","TrialStatusTypeDao"})
public class TrialStatusTypeDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method TrialStatusTypeDao.toTrialStatusTypeVO
   *
   * @see org.phoenixctms.ctsms.domain.TrialStatusTypeDao#toTrialStatusTypeVO(org.phoenixctms.ctsms.domain.TrialStatusType source, org.phoenixctms.ctsms.vo.TrialStatusTypeVO target)
   */
  @Test
  public void testToTrialStatusTypeVO() {
    Assert.fail("Test 'TrialStatusTypeDaoTransformTest.testToTrialStatusTypeVO' not implemented!");
  }

    /**
   * Test for method trialStatusTypeVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.TrialStatusTypeDao#trialStatusTypeVOToEntity(org.phoenixctms.ctsms.vo.TrialStatusTypeVO source, org.phoenixctms.ctsms.domain.TrialStatusType target, boolean copyIfNull)
   */
  @Test
  public void testTrialStatusTypeVOToEntity() {
    Assert.fail("Test 'TrialStatusTypeDaoTransformTest.testTrialStatusTypeVOToEntity' not implemented!");
  }

}