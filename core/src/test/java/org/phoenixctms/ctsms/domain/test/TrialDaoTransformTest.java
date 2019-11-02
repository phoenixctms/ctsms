// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.Trial
*/
@Test(groups={"transform","TrialDao"})
public class TrialDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method TrialDao.toTrialOutVO
   *
   * @see org.phoenixctms.ctsms.domain.TrialDao#toTrialOutVO(org.phoenixctms.ctsms.domain.Trial source, org.phoenixctms.ctsms.vo.TrialOutVO target)
   */
  @Test
  public void testToTrialOutVO() {
    Assert.fail("Test 'TrialDaoTransformTest.testToTrialOutVO' not implemented!");
  }

    /**
   * Test for method trialOutVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.TrialDao#trialOutVOToEntity(org.phoenixctms.ctsms.vo.TrialOutVO source, org.phoenixctms.ctsms.domain.Trial target, boolean copyIfNull)
   */
  @Test
  public void testTrialOutVOToEntity() {
    Assert.fail("Test 'TrialDaoTransformTest.testTrialOutVOToEntity' not implemented!");
  }

  /**
   * Test for method TrialDao.toTrialInVO
   *
   * @see org.phoenixctms.ctsms.domain.TrialDao#toTrialInVO(org.phoenixctms.ctsms.domain.Trial source, org.phoenixctms.ctsms.vo.TrialInVO target)
   */
  @Test
  public void testToTrialInVO() {
    Assert.fail("Test 'TrialDaoTransformTest.testToTrialInVO' not implemented!");
  }

    /**
   * Test for method trialInVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.TrialDao#trialInVOToEntity(org.phoenixctms.ctsms.vo.TrialInVO source, org.phoenixctms.ctsms.domain.Trial target, boolean copyIfNull)
   */
  @Test
  public void testTrialInVOToEntity() {
    Assert.fail("Test 'TrialDaoTransformTest.testTrialInVOToEntity' not implemented!");
  }

  /**
   * Test for method TrialDao.toTrialRandomizationListVO
   *
   * @see org.phoenixctms.ctsms.domain.TrialDao#toTrialRandomizationListVO(org.phoenixctms.ctsms.domain.Trial source, org.phoenixctms.ctsms.vo.TrialRandomizationListVO target)
   */
  @Test
  public void testToTrialRandomizationListVO() {
    Assert.fail("Test 'TrialDaoTransformTest.testToTrialRandomizationListVO' not implemented!");
  }

    /**
   * Test for method trialRandomizationListVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.TrialDao#trialRandomizationListVOToEntity(org.phoenixctms.ctsms.vo.TrialRandomizationListVO source, org.phoenixctms.ctsms.domain.Trial target, boolean copyIfNull)
   */
  @Test
  public void testTrialRandomizationListVOToEntity() {
    Assert.fail("Test 'TrialDaoTransformTest.testTrialRandomizationListVOToEntity' not implemented!");
  }

}