// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.TrainingRecordSection
*/
@Test(groups={"transform","TrainingRecordSectionDao"})
public class TrainingRecordSectionDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method TrainingRecordSectionDao.toTrainingRecordSectionVO
   *
   * @see org.phoenixctms.ctsms.domain.TrainingRecordSectionDao#toTrainingRecordSectionVO(org.phoenixctms.ctsms.domain.TrainingRecordSection source, org.phoenixctms.ctsms.vo.TrainingRecordSectionVO target)
   */
  @Test
  public void testToTrainingRecordSectionVO() {
    Assert.fail("Test 'TrainingRecordSectionDaoTransformTest.testToTrainingRecordSectionVO' not implemented!");
  }

    /**
   * Test for method trainingRecordSectionVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.TrainingRecordSectionDao#trainingRecordSectionVOToEntity(org.phoenixctms.ctsms.vo.TrainingRecordSectionVO source, org.phoenixctms.ctsms.domain.TrainingRecordSection target, boolean copyIfNull)
   */
  @Test
  public void testTrainingRecordSectionVOToEntity() {
    Assert.fail("Test 'TrainingRecordSectionDaoTransformTest.testTrainingRecordSectionVOToEntity' not implemented!");
  }

}