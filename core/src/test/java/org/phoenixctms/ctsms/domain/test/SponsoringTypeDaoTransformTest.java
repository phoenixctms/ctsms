// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.SponsoringType
*/
@Test(groups={"transform","SponsoringTypeDao"})
public class SponsoringTypeDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method SponsoringTypeDao.toSponsoringTypeVO
   *
   * @see org.phoenixctms.ctsms.domain.SponsoringTypeDao#toSponsoringTypeVO(org.phoenixctms.ctsms.domain.SponsoringType source, org.phoenixctms.ctsms.vo.SponsoringTypeVO target)
   */
  @Test
  public void testToSponsoringTypeVO() {
    Assert.fail("Test 'SponsoringTypeDaoTransformTest.testToSponsoringTypeVO' not implemented!");
  }

    /**
   * Test for method sponsoringTypeVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.SponsoringTypeDao#sponsoringTypeVOToEntity(org.phoenixctms.ctsms.vo.SponsoringTypeVO source, org.phoenixctms.ctsms.domain.SponsoringType target, boolean copyIfNull)
   */
  @Test
  public void testSponsoringTypeVOToEntity() {
    Assert.fail("Test 'SponsoringTypeDaoTransformTest.testSponsoringTypeVOToEntity' not implemented!");
  }

}