// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.MimeType
*/
@Test(groups={"transform","MimeTypeDao"})
public class MimeTypeDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method MimeTypeDao.toMimeTypeVO
   *
   * @see org.phoenixctms.ctsms.domain.MimeTypeDao#toMimeTypeVO(org.phoenixctms.ctsms.domain.MimeType source, org.phoenixctms.ctsms.vo.MimeTypeVO target)
   */
  @Test
  public void testToMimeTypeVO() {
    Assert.fail("Test 'MimeTypeDaoTransformTest.testToMimeTypeVO' not implemented!");
  }

    /**
   * Test for method mimeTypeVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.MimeTypeDao#mimeTypeVOToEntity(org.phoenixctms.ctsms.vo.MimeTypeVO source, org.phoenixctms.ctsms.domain.MimeType target, boolean copyIfNull)
   */
  @Test
  public void testMimeTypeVOToEntity() {
    Assert.fail("Test 'MimeTypeDaoTransformTest.testMimeTypeVOToEntity' not implemented!");
  }

}