// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.Diagnosis
*/
@Test(groups={"transform","DiagnosisDao"})
public class DiagnosisDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method DiagnosisDao.toDiagnosisInVO
   *
   * @see org.phoenixctms.ctsms.domain.DiagnosisDao#toDiagnosisInVO(org.phoenixctms.ctsms.domain.Diagnosis source, org.phoenixctms.ctsms.vo.DiagnosisInVO target)
   */
  @Test
  public void testToDiagnosisInVO() {
    Assert.fail("Test 'DiagnosisDaoTransformTest.testToDiagnosisInVO' not implemented!");
  }

    /**
   * Test for method diagnosisInVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.DiagnosisDao#diagnosisInVOToEntity(org.phoenixctms.ctsms.vo.DiagnosisInVO source, org.phoenixctms.ctsms.domain.Diagnosis target, boolean copyIfNull)
   */
  @Test
  public void testDiagnosisInVOToEntity() {
    Assert.fail("Test 'DiagnosisDaoTransformTest.testDiagnosisInVOToEntity' not implemented!");
  }

  /**
   * Test for method DiagnosisDao.toDiagnosisOutVO
   *
   * @see org.phoenixctms.ctsms.domain.DiagnosisDao#toDiagnosisOutVO(org.phoenixctms.ctsms.domain.Diagnosis source, org.phoenixctms.ctsms.vo.DiagnosisOutVO target)
   */
  @Test
  public void testToDiagnosisOutVO() {
    Assert.fail("Test 'DiagnosisDaoTransformTest.testToDiagnosisOutVO' not implemented!");
  }

    /**
   * Test for method diagnosisOutVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.DiagnosisDao#diagnosisOutVOToEntity(org.phoenixctms.ctsms.vo.DiagnosisOutVO source, org.phoenixctms.ctsms.domain.Diagnosis target, boolean copyIfNull)
   */
  @Test
  public void testDiagnosisOutVOToEntity() {
    Assert.fail("Test 'DiagnosisDaoTransformTest.testDiagnosisOutVOToEntity' not implemented!");
  }

}