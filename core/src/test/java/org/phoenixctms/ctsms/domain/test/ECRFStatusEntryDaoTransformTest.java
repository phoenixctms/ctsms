// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.ECRFStatusEntry
*/
@Test(groups={"transform","ECRFStatusEntryDao"})
public class ECRFStatusEntryDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method ECRFStatusEntryDao.toECRFStatusEntryVO
   *
   * @see org.phoenixctms.ctsms.domain.ECRFStatusEntryDao#toECRFStatusEntryVO(org.phoenixctms.ctsms.domain.ECRFStatusEntry source, org.phoenixctms.ctsms.vo.ECRFStatusEntryVO target)
   */
  @Test
  public void testToECRFStatusEntryVO() {
    Assert.fail("Test 'ECRFStatusEntryDaoTransformTest.testToECRFStatusEntryVO' not implemented!");
  }

    /**
   * Test for method eCRFStatusEntryVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.ECRFStatusEntryDao#eCRFStatusEntryVOToEntity(org.phoenixctms.ctsms.vo.ECRFStatusEntryVO source, org.phoenixctms.ctsms.domain.ECRFStatusEntry target, boolean copyIfNull)
   */
  @Test
  public void testECRFStatusEntryVOToEntity() {
    Assert.fail("Test 'ECRFStatusEntryDaoTransformTest.testECRFStatusEntryVOToEntity' not implemented!");
  }

}