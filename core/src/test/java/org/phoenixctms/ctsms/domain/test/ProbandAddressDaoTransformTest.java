// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.ProbandAddress
*/
@Test(groups={"transform","ProbandAddressDao"})
public class ProbandAddressDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method ProbandAddressDao.toProbandAddressInVO
   *
   * @see org.phoenixctms.ctsms.domain.ProbandAddressDao#toProbandAddressInVO(org.phoenixctms.ctsms.domain.ProbandAddress source, org.phoenixctms.ctsms.vo.ProbandAddressInVO target)
   */
  @Test
  public void testToProbandAddressInVO() {
    Assert.fail("Test 'ProbandAddressDaoTransformTest.testToProbandAddressInVO' not implemented!");
  }

    /**
   * Test for method probandAddressInVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.ProbandAddressDao#probandAddressInVOToEntity(org.phoenixctms.ctsms.vo.ProbandAddressInVO source, org.phoenixctms.ctsms.domain.ProbandAddress target, boolean copyIfNull)
   */
  @Test
  public void testProbandAddressInVOToEntity() {
    Assert.fail("Test 'ProbandAddressDaoTransformTest.testProbandAddressInVOToEntity' not implemented!");
  }

  /**
   * Test for method ProbandAddressDao.toProbandAddressOutVO
   *
   * @see org.phoenixctms.ctsms.domain.ProbandAddressDao#toProbandAddressOutVO(org.phoenixctms.ctsms.domain.ProbandAddress source, org.phoenixctms.ctsms.vo.ProbandAddressOutVO target)
   */
  @Test
  public void testToProbandAddressOutVO() {
    Assert.fail("Test 'ProbandAddressDaoTransformTest.testToProbandAddressOutVO' not implemented!");
  }

    /**
   * Test for method probandAddressOutVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.ProbandAddressDao#probandAddressOutVOToEntity(org.phoenixctms.ctsms.vo.ProbandAddressOutVO source, org.phoenixctms.ctsms.domain.ProbandAddress target, boolean copyIfNull)
   */
  @Test
  public void testProbandAddressOutVOToEntity() {
    Assert.fail("Test 'ProbandAddressDaoTransformTest.testProbandAddressOutVOToEntity' not implemented!");
  }

}