// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.Medication
*/
@Test(groups = { "transform", "MedicationDao" })
public class MedicationDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method MedicationDao.toMedicationInVO
	 *
	 * @see org.phoenixctms.ctsms.domain.MedicationDao#toMedicationInVO(org.phoenixctms.ctsms.domain.Medication source, org.phoenixctms.ctsms.vo.MedicationInVO target)
	 */
	@Test
	public void testToMedicationInVO() {
		Assert.fail("Test 'MedicationDaoTransformTest.testToMedicationInVO' not implemented!");
	}

	/**
	* Test for method medicationInVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.MedicationDao#medicationInVOToEntity(org.phoenixctms.ctsms.vo.MedicationInVO source, org.phoenixctms.ctsms.domain.Medication target, boolean copyIfNull)
	*/
	@Test
	public void testMedicationInVOToEntity() {
		Assert.fail("Test 'MedicationDaoTransformTest.testMedicationInVOToEntity' not implemented!");
	}

	/**
	 * Test for method MedicationDao.toMedicationOutVO
	 *
	 * @see org.phoenixctms.ctsms.domain.MedicationDao#toMedicationOutVO(org.phoenixctms.ctsms.domain.Medication source, org.phoenixctms.ctsms.vo.MedicationOutVO target)
	 */
	@Test
	public void testToMedicationOutVO() {
		Assert.fail("Test 'MedicationDaoTransformTest.testToMedicationOutVO' not implemented!");
	}

	/**
	* Test for method medicationOutVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.MedicationDao#medicationOutVOToEntity(org.phoenixctms.ctsms.vo.MedicationOutVO source, org.phoenixctms.ctsms.domain.Medication target, boolean copyIfNull)
	*/
	@Test
	public void testMedicationOutVOToEntity() {
		Assert.fail("Test 'MedicationDaoTransformTest.testMedicationOutVOToEntity' not implemented!");
	}
}