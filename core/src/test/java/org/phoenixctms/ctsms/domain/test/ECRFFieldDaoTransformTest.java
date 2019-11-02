// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.ECRFField
*/
@Test(groups={"transform","ECRFFieldDao"})
public class ECRFFieldDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method ECRFFieldDao.toECRFFieldInVO
   *
   * @see org.phoenixctms.ctsms.domain.ECRFFieldDao#toECRFFieldInVO(org.phoenixctms.ctsms.domain.ECRFField source, org.phoenixctms.ctsms.vo.ECRFFieldInVO target)
   */
  @Test
  public void testToECRFFieldInVO() {
    Assert.fail("Test 'ECRFFieldDaoTransformTest.testToECRFFieldInVO' not implemented!");
  }

    /**
   * Test for method eCRFFieldInVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.ECRFFieldDao#eCRFFieldInVOToEntity(org.phoenixctms.ctsms.vo.ECRFFieldInVO source, org.phoenixctms.ctsms.domain.ECRFField target, boolean copyIfNull)
   */
  @Test
  public void testECRFFieldInVOToEntity() {
    Assert.fail("Test 'ECRFFieldDaoTransformTest.testECRFFieldInVOToEntity' not implemented!");
  }

  /**
   * Test for method ECRFFieldDao.toECRFFieldOutVO
   *
   * @see org.phoenixctms.ctsms.domain.ECRFFieldDao#toECRFFieldOutVO(org.phoenixctms.ctsms.domain.ECRFField source, org.phoenixctms.ctsms.vo.ECRFFieldOutVO target)
   */
  @Test
  public void testToECRFFieldOutVO() {
    Assert.fail("Test 'ECRFFieldDaoTransformTest.testToECRFFieldOutVO' not implemented!");
  }

    /**
   * Test for method eCRFFieldOutVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.ECRFFieldDao#eCRFFieldOutVOToEntity(org.phoenixctms.ctsms.vo.ECRFFieldOutVO source, org.phoenixctms.ctsms.domain.ECRFField target, boolean copyIfNull)
   */
  @Test
  public void testECRFFieldOutVOToEntity() {
    Assert.fail("Test 'ECRFFieldDaoTransformTest.testECRFFieldOutVOToEntity' not implemented!");
  }

  /**
   * Test for method ECRFFieldDao.toLightECRFFieldOutVO
   *
   * @see org.phoenixctms.ctsms.domain.ECRFFieldDao#toLightECRFFieldOutVO(org.phoenixctms.ctsms.domain.ECRFField source, org.phoenixctms.ctsms.vo.LightECRFFieldOutVO target)
   */
  @Test
  public void testToLightECRFFieldOutVO() {
    Assert.fail("Test 'ECRFFieldDaoTransformTest.testToLightECRFFieldOutVO' not implemented!");
  }

    /**
   * Test for method lightECRFFieldOutVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.ECRFFieldDao#lightECRFFieldOutVOToEntity(org.phoenixctms.ctsms.vo.LightECRFFieldOutVO source, org.phoenixctms.ctsms.domain.ECRFField target, boolean copyIfNull)
   */
  @Test
  public void testLightECRFFieldOutVOToEntity() {
    Assert.fail("Test 'ECRFFieldDaoTransformTest.testLightECRFFieldOutVOToEntity' not implemented!");
  }

}