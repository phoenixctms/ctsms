// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.Signature
*/
@Test(groups={"transform","SignatureDao"})
public class SignatureDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method SignatureDao.toSignatureVO
   *
   * @see org.phoenixctms.ctsms.domain.SignatureDao#toSignatureVO(org.phoenixctms.ctsms.domain.Signature source, org.phoenixctms.ctsms.vo.SignatureVO target)
   */
  @Test
  public void testToSignatureVO() {
    Assert.fail("Test 'SignatureDaoTransformTest.testToSignatureVO' not implemented!");
  }

    /**
   * Test for method signatureVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.SignatureDao#signatureVOToEntity(org.phoenixctms.ctsms.vo.SignatureVO source, org.phoenixctms.ctsms.domain.Signature target, boolean copyIfNull)
   */
  @Test
  public void testSignatureVOToEntity() {
    Assert.fail("Test 'SignatureDaoTransformTest.testSignatureVOToEntity' not implemented!");
  }

}