// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.MassMailStatusType
*/
@Test(groups={"transform","MassMailStatusTypeDao"})
public class MassMailStatusTypeDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method MassMailStatusTypeDao.toMassMailStatusTypeVO
   *
   * @see org.phoenixctms.ctsms.domain.MassMailStatusTypeDao#toMassMailStatusTypeVO(org.phoenixctms.ctsms.domain.MassMailStatusType source, org.phoenixctms.ctsms.vo.MassMailStatusTypeVO target)
   */
  @Test
  public void testToMassMailStatusTypeVO() {
    Assert.fail("Test 'MassMailStatusTypeDaoTransformTest.testToMassMailStatusTypeVO' not implemented!");
  }

    /**
   * Test for method massMailStatusTypeVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.MassMailStatusTypeDao#massMailStatusTypeVOToEntity(org.phoenixctms.ctsms.vo.MassMailStatusTypeVO source, org.phoenixctms.ctsms.domain.MassMailStatusType target, boolean copyIfNull)
   */
  @Test
  public void testMassMailStatusTypeVOToEntity() {
    Assert.fail("Test 'MassMailStatusTypeDaoTransformTest.testMassMailStatusTypeVOToEntity' not implemented!");
  }

}