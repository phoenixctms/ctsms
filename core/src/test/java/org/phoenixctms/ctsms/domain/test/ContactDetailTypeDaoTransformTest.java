// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.ContactDetailType
*/
@Test(groups={"transform","ContactDetailTypeDao"})
public class ContactDetailTypeDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method ContactDetailTypeDao.toContactDetailTypeVO
   *
   * @see org.phoenixctms.ctsms.domain.ContactDetailTypeDao#toContactDetailTypeVO(org.phoenixctms.ctsms.domain.ContactDetailType source, org.phoenixctms.ctsms.vo.ContactDetailTypeVO target)
   */
  @Test
  public void testToContactDetailTypeVO() {
    Assert.fail("Test 'ContactDetailTypeDaoTransformTest.testToContactDetailTypeVO' not implemented!");
  }

    /**
   * Test for method contactDetailTypeVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.ContactDetailTypeDao#contactDetailTypeVOToEntity(org.phoenixctms.ctsms.vo.ContactDetailTypeVO source, org.phoenixctms.ctsms.domain.ContactDetailType target, boolean copyIfNull)
   */
  @Test
  public void testContactDetailTypeVOToEntity() {
    Assert.fail("Test 'ContactDetailTypeDaoTransformTest.testContactDetailTypeVOToEntity' not implemented!");
  }

}