// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.ECRFFieldStatusType
*/
@Test(groups={"transform","ECRFFieldStatusTypeDao"})
public class ECRFFieldStatusTypeDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method ECRFFieldStatusTypeDao.toECRFFieldStatusTypeVO
   *
   * @see org.phoenixctms.ctsms.domain.ECRFFieldStatusTypeDao#toECRFFieldStatusTypeVO(org.phoenixctms.ctsms.domain.ECRFFieldStatusType source, org.phoenixctms.ctsms.vo.ECRFFieldStatusTypeVO target)
   */
  @Test
  public void testToECRFFieldStatusTypeVO() {
    Assert.fail("Test 'ECRFFieldStatusTypeDaoTransformTest.testToECRFFieldStatusTypeVO' not implemented!");
  }

    /**
   * Test for method eCRFFieldStatusTypeVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.ECRFFieldStatusTypeDao#eCRFFieldStatusTypeVOToEntity(org.phoenixctms.ctsms.vo.ECRFFieldStatusTypeVO source, org.phoenixctms.ctsms.domain.ECRFFieldStatusType target, boolean copyIfNull)
   */
  @Test
  public void testECRFFieldStatusTypeVOToEntity() {
    Assert.fail("Test 'ECRFFieldStatusTypeDaoTransformTest.testECRFFieldStatusTypeVOToEntity' not implemented!");
  }

}