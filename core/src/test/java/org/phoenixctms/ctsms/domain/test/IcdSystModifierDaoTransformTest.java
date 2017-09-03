// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.IcdSystModifier
*/
@Test(groups={"transform","IcdSystModifierDao"})
public class IcdSystModifierDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method IcdSystModifierDao.toIcdSystModifierVO
   *
   * @see org.phoenixctms.ctsms.domain.IcdSystModifierDao#toIcdSystModifierVO(org.phoenixctms.ctsms.domain.IcdSystModifier source, org.phoenixctms.ctsms.vo.IcdSystModifierVO target)
   */
  @Test
  public void testToIcdSystModifierVO() {
    Assert.fail("Test 'IcdSystModifierDaoTransformTest.testToIcdSystModifierVO' not implemented!");
  }

    /**
   * Test for method icdSystModifierVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.IcdSystModifierDao#icdSystModifierVOToEntity(org.phoenixctms.ctsms.vo.IcdSystModifierVO source, org.phoenixctms.ctsms.domain.IcdSystModifier target, boolean copyIfNull)
   */
  @Test
  public void testIcdSystModifierVOToEntity() {
    Assert.fail("Test 'IcdSystModifierDaoTransformTest.testIcdSystModifierVOToEntity' not implemented!");
  }

}