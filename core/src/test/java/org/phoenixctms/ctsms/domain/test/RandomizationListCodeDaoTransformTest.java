// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.RandomizationListCode
*/
@Test(groups={"transform","RandomizationListCodeDao"})
public class RandomizationListCodeDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method RandomizationListCodeDao.toRandomizationListCodeVO
   *
   * @see org.phoenixctms.ctsms.domain.RandomizationListCodeDao#toRandomizationListCodeVO(org.phoenixctms.ctsms.domain.RandomizationListCode source, org.phoenixctms.ctsms.vo.RandomizationListCodeVO target)
   */
  @Test
  public void testToRandomizationListCodeVO() {
    Assert.fail("Test 'RandomizationListCodeDaoTransformTest.testToRandomizationListCodeVO' not implemented!");
  }

    /**
   * Test for method randomizationListCodeVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.RandomizationListCodeDao#randomizationListCodeVOToEntity(org.phoenixctms.ctsms.vo.RandomizationListCodeVO source, org.phoenixctms.ctsms.domain.RandomizationListCode target, boolean copyIfNull)
   */
  @Test
  public void testRandomizationListCodeVOToEntity() {
    Assert.fail("Test 'RandomizationListCodeDaoTransformTest.testRandomizationListCodeVOToEntity' not implemented!");
  }

}