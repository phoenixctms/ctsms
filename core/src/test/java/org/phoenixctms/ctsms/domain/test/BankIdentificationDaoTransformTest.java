// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.BankIdentification
*/
@Test(groups={"transform","BankIdentificationDao"})
public class BankIdentificationDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method BankIdentificationDao.toBankIdentificationVO
   *
   * @see org.phoenixctms.ctsms.domain.BankIdentificationDao#toBankIdentificationVO(org.phoenixctms.ctsms.domain.BankIdentification source, org.phoenixctms.ctsms.vo.BankIdentificationVO target)
   */
  @Test
  public void testToBankIdentificationVO() {
    Assert.fail("Test 'BankIdentificationDaoTransformTest.testToBankIdentificationVO' not implemented!");
  }

    /**
   * Test for method bankIdentificationVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.BankIdentificationDao#bankIdentificationVOToEntity(org.phoenixctms.ctsms.vo.BankIdentificationVO source, org.phoenixctms.ctsms.domain.BankIdentification target, boolean copyIfNull)
   */
  @Test
  public void testBankIdentificationVOToEntity() {
    Assert.fail("Test 'BankIdentificationDaoTransformTest.testBankIdentificationVOToEntity' not implemented!");
  }

}