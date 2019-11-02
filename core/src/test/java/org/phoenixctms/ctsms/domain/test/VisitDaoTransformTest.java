// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.Visit
*/
@Test(groups={"transform","VisitDao"})
public class VisitDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method VisitDao.toVisitInVO
   *
   * @see org.phoenixctms.ctsms.domain.VisitDao#toVisitInVO(org.phoenixctms.ctsms.domain.Visit source, org.phoenixctms.ctsms.vo.VisitInVO target)
   */
  @Test
  public void testToVisitInVO() {
    Assert.fail("Test 'VisitDaoTransformTest.testToVisitInVO' not implemented!");
  }

    /**
   * Test for method visitInVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.VisitDao#visitInVOToEntity(org.phoenixctms.ctsms.vo.VisitInVO source, org.phoenixctms.ctsms.domain.Visit target, boolean copyIfNull)
   */
  @Test
  public void testVisitInVOToEntity() {
    Assert.fail("Test 'VisitDaoTransformTest.testVisitInVOToEntity' not implemented!");
  }

  /**
   * Test for method VisitDao.toVisitOutVO
   *
   * @see org.phoenixctms.ctsms.domain.VisitDao#toVisitOutVO(org.phoenixctms.ctsms.domain.Visit source, org.phoenixctms.ctsms.vo.VisitOutVO target)
   */
  @Test
  public void testToVisitOutVO() {
    Assert.fail("Test 'VisitDaoTransformTest.testToVisitOutVO' not implemented!");
  }

    /**
   * Test for method visitOutVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.VisitDao#visitOutVOToEntity(org.phoenixctms.ctsms.vo.VisitOutVO source, org.phoenixctms.ctsms.domain.Visit target, boolean copyIfNull)
   */
  @Test
  public void testVisitOutVOToEntity() {
    Assert.fail("Test 'VisitDaoTransformTest.testVisitOutVOToEntity' not implemented!");
  }

}