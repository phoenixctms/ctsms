// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.MassMailRecipient
*/
@Test(groups={"transform","MassMailRecipientDao"})
public class MassMailRecipientDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method MassMailRecipientDao.toMassMailRecipientInVO
   *
   * @see org.phoenixctms.ctsms.domain.MassMailRecipientDao#toMassMailRecipientInVO(org.phoenixctms.ctsms.domain.MassMailRecipient source, org.phoenixctms.ctsms.vo.MassMailRecipientInVO target)
   */
  @Test
  public void testToMassMailRecipientInVO() {
    Assert.fail("Test 'MassMailRecipientDaoTransformTest.testToMassMailRecipientInVO' not implemented!");
  }

    /**
   * Test for method massMailRecipientInVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.MassMailRecipientDao#massMailRecipientInVOToEntity(org.phoenixctms.ctsms.vo.MassMailRecipientInVO source, org.phoenixctms.ctsms.domain.MassMailRecipient target, boolean copyIfNull)
   */
  @Test
  public void testMassMailRecipientInVOToEntity() {
    Assert.fail("Test 'MassMailRecipientDaoTransformTest.testMassMailRecipientInVOToEntity' not implemented!");
  }

  /**
   * Test for method MassMailRecipientDao.toMassMailRecipientOutVO
   *
   * @see org.phoenixctms.ctsms.domain.MassMailRecipientDao#toMassMailRecipientOutVO(org.phoenixctms.ctsms.domain.MassMailRecipient source, org.phoenixctms.ctsms.vo.MassMailRecipientOutVO target)
   */
  @Test
  public void testToMassMailRecipientOutVO() {
    Assert.fail("Test 'MassMailRecipientDaoTransformTest.testToMassMailRecipientOutVO' not implemented!");
  }

    /**
   * Test for method massMailRecipientOutVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.MassMailRecipientDao#massMailRecipientOutVOToEntity(org.phoenixctms.ctsms.vo.MassMailRecipientOutVO source, org.phoenixctms.ctsms.domain.MassMailRecipient target, boolean copyIfNull)
   */
  @Test
  public void testMassMailRecipientOutVOToEntity() {
    Assert.fail("Test 'MassMailRecipientDaoTransformTest.testMassMailRecipientOutVOToEntity' not implemented!");
  }

}