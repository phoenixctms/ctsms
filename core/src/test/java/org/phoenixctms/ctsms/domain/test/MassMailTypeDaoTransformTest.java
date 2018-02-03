// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.MassMailType
*/
@Test(groups={"transform","MassMailTypeDao"})
public class MassMailTypeDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method MassMailTypeDao.toMassMailTypVO
   *
   * @see org.phoenixctms.ctsms.domain.MassMailTypeDao#toMassMailTypVO(org.phoenixctms.ctsms.domain.MassMailType source, org.phoenixctms.ctsms.vo.MassMailTypVO target)
   */
  @Test
  public void testToMassMailTypVO() {
    Assert.fail("Test 'MassMailTypeDaoTransformTest.testToMassMailTypVO' not implemented!");
  }

    /**
   * Test for method massMailTypVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.MassMailTypeDao#massMailTypVOToEntity(org.phoenixctms.ctsms.vo.MassMailTypVO source, org.phoenixctms.ctsms.domain.MassMailType target, boolean copyIfNull)
   */
  @Test
  public void testMassMailTypVOToEntity() {
    Assert.fail("Test 'MassMailTypeDaoTransformTest.testMassMailTypVOToEntity' not implemented!");
  }

}