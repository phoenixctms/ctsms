// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.SurveyStatusType
*/
@Test(groups={"transform","SurveyStatusTypeDao"})
public class SurveyStatusTypeDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method SurveyStatusTypeDao.toSurveyStatusTypeVO
   *
   * @see org.phoenixctms.ctsms.domain.SurveyStatusTypeDao#toSurveyStatusTypeVO(org.phoenixctms.ctsms.domain.SurveyStatusType source, org.phoenixctms.ctsms.vo.SurveyStatusTypeVO target)
   */
  @Test
  public void testToSurveyStatusTypeVO() {
    Assert.fail("Test 'SurveyStatusTypeDaoTransformTest.testToSurveyStatusTypeVO' not implemented!");
  }

    /**
   * Test for method surveyStatusTypeVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.SurveyStatusTypeDao#surveyStatusTypeVOToEntity(org.phoenixctms.ctsms.vo.SurveyStatusTypeVO source, org.phoenixctms.ctsms.domain.SurveyStatusType target, boolean copyIfNull)
   */
  @Test
  public void testSurveyStatusTypeVOToEntity() {
    Assert.fail("Test 'SurveyStatusTypeDaoTransformTest.testSurveyStatusTypeVOToEntity' not implemented!");
  }

}