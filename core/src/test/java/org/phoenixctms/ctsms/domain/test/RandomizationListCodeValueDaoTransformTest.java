// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.RandomizationListCodeValue
*/
@Test(groups={"transform","RandomizationListCodeValueDao"})
public class RandomizationListCodeValueDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method RandomizationListCodeValueDao.toRandomizationListCodeValueVO
   *
   * @see org.phoenixctms.ctsms.domain.RandomizationListCodeValueDao#toRandomizationListCodeValueVO(org.phoenixctms.ctsms.domain.RandomizationListCodeValue source, org.phoenixctms.ctsms.vo.RandomizationListCodeValueVO target)
   */
  @Test
  public void testToRandomizationListCodeValueVO() {
    Assert.fail("Test 'RandomizationListCodeValueDaoTransformTest.testToRandomizationListCodeValueVO' not implemented!");
  }

    /**
   * Test for method randomizationListCodeValueVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.RandomizationListCodeValueDao#randomizationListCodeValueVOToEntity(org.phoenixctms.ctsms.vo.RandomizationListCodeValueVO source, org.phoenixctms.ctsms.domain.RandomizationListCodeValue target, boolean copyIfNull)
   */
  @Test
  public void testRandomizationListCodeValueVOToEntity() {
    Assert.fail("Test 'RandomizationListCodeValueDaoTransformTest.testRandomizationListCodeValueVOToEntity' not implemented!");
  }

}