// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.MaintenanceType
*/
@Test(groups = { "transform", "MaintenanceTypeDao" })
public class MaintenanceTypeDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method MaintenanceTypeDao.toMaintenanceTypeVO
	 *
	 * @see org.phoenixctms.ctsms.domain.MaintenanceTypeDao#toMaintenanceTypeVO(org.phoenixctms.ctsms.domain.MaintenanceType source, org.phoenixctms.ctsms.vo.MaintenanceTypeVO target)
	 */
	@Test
	public void testToMaintenanceTypeVO() {
		Assert.fail("Test 'MaintenanceTypeDaoTransformTest.testToMaintenanceTypeVO' not implemented!");
	}

	/**
	* Test for method maintenanceTypeVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.MaintenanceTypeDao#maintenanceTypeVOToEntity(org.phoenixctms.ctsms.vo.MaintenanceTypeVO source, org.phoenixctms.ctsms.domain.MaintenanceType target, boolean copyIfNull)
	*/
	@Test
	public void testMaintenanceTypeVOToEntity() {
		Assert.fail("Test 'MaintenanceTypeDaoTransformTest.testMaintenanceTypeVOToEntity' not implemented!");
	}
}