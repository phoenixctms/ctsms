// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.TrustedDevice
*/
@Test(groups={"transform","TrustedDeviceDao"})
public class TrustedDeviceDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method TrustedDeviceDao.toTrustedDeviceOutVO
   *
   * @see org.phoenixctms.ctsms.domain.TrustedDeviceDao#toTrustedDeviceOutVO(org.phoenixctms.ctsms.domain.TrustedDevice source, org.phoenixctms.ctsms.vo.TrustedDeviceOutVO target)
   */
  @Test
  public void testToTrustedDeviceOutVO() {
    Assert.fail("Test 'TrustedDeviceDaoTransformTest.testToTrustedDeviceOutVO' not implemented!");
  }

    /**
   * Test for method trustedDeviceOutVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.TrustedDeviceDao#trustedDeviceOutVOToEntity(org.phoenixctms.ctsms.vo.TrustedDeviceOutVO source, org.phoenixctms.ctsms.domain.TrustedDevice target, boolean copyIfNull)
   */
  @Test
  public void testTrustedDeviceOutVOToEntity() {
    Assert.fail("Test 'TrustedDeviceDaoTransformTest.testTrustedDeviceOutVOToEntity' not implemented!");
  }

}