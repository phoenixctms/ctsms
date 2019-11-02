// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.OpsSystModifier
*/
@Test(groups={"transform","OpsSystModifierDao"})
public class OpsSystModifierDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method OpsSystModifierDao.toOpsSystModifierVO
   *
   * @see org.phoenixctms.ctsms.domain.OpsSystModifierDao#toOpsSystModifierVO(org.phoenixctms.ctsms.domain.OpsSystModifier source, org.phoenixctms.ctsms.vo.OpsSystModifierVO target)
   */
  @Test
  public void testToOpsSystModifierVO() {
    Assert.fail("Test 'OpsSystModifierDaoTransformTest.testToOpsSystModifierVO' not implemented!");
  }

    /**
   * Test for method opsSystModifierVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.OpsSystModifierDao#opsSystModifierVOToEntity(org.phoenixctms.ctsms.vo.OpsSystModifierVO source, org.phoenixctms.ctsms.domain.OpsSystModifier target, boolean copyIfNull)
   */
  @Test
  public void testOpsSystModifierVOToEntity() {
    Assert.fail("Test 'OpsSystModifierDaoTransformTest.testOpsSystModifierVOToEntity' not implemented!");
  }

}