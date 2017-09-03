// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.CriterionProperty
*/
@Test(groups={"transform","CriterionPropertyDao"})
public class CriterionPropertyDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method CriterionPropertyDao.toCriterionPropertyVO
   *
   * @see org.phoenixctms.ctsms.domain.CriterionPropertyDao#toCriterionPropertyVO(org.phoenixctms.ctsms.domain.CriterionProperty source, org.phoenixctms.ctsms.vo.CriterionPropertyVO target)
   */
  @Test
  public void testToCriterionPropertyVO() {
    Assert.fail("Test 'CriterionPropertyDaoTransformTest.testToCriterionPropertyVO' not implemented!");
  }

    /**
   * Test for method criterionPropertyVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.CriterionPropertyDao#criterionPropertyVOToEntity(org.phoenixctms.ctsms.vo.CriterionPropertyVO source, org.phoenixctms.ctsms.domain.CriterionProperty target, boolean copyIfNull)
   */
  @Test
  public void testCriterionPropertyVOToEntity() {
    Assert.fail("Test 'CriterionPropertyDaoTransformTest.testCriterionPropertyVOToEntity' not implemented!");
  }

}