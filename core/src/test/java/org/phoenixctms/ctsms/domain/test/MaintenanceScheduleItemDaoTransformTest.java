// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.MaintenanceScheduleItem
*/
@Test(groups = { "transform", "MaintenanceScheduleItemDao" })
public class MaintenanceScheduleItemDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method MaintenanceScheduleItemDao.toMaintenanceScheduleItemInVO
	 *
	 * @see org.phoenixctms.ctsms.domain.MaintenanceScheduleItemDao#toMaintenanceScheduleItemInVO(org.phoenixctms.ctsms.domain.MaintenanceScheduleItem source, org.phoenixctms.ctsms.vo.MaintenanceScheduleItemInVO target)
	 */
	@Test
	public void testToMaintenanceScheduleItemInVO() {
		Assert.fail("Test 'MaintenanceScheduleItemDaoTransformTest.testToMaintenanceScheduleItemInVO' not implemented!");
	}

	/**
	* Test for method maintenanceScheduleItemInVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.MaintenanceScheduleItemDao#maintenanceScheduleItemInVOToEntity(org.phoenixctms.ctsms.vo.MaintenanceScheduleItemInVO source, org.phoenixctms.ctsms.domain.MaintenanceScheduleItem target, boolean copyIfNull)
	*/
	@Test
	public void testMaintenanceScheduleItemInVOToEntity() {
		Assert.fail("Test 'MaintenanceScheduleItemDaoTransformTest.testMaintenanceScheduleItemInVOToEntity' not implemented!");
	}

	/**
	 * Test for method MaintenanceScheduleItemDao.toMaintenanceScheduleItemOutVO
	 *
	 * @see org.phoenixctms.ctsms.domain.MaintenanceScheduleItemDao#toMaintenanceScheduleItemOutVO(org.phoenixctms.ctsms.domain.MaintenanceScheduleItem source, org.phoenixctms.ctsms.vo.MaintenanceScheduleItemOutVO target)
	 */
	@Test
	public void testToMaintenanceScheduleItemOutVO() {
		Assert.fail("Test 'MaintenanceScheduleItemDaoTransformTest.testToMaintenanceScheduleItemOutVO' not implemented!");
	}

	/**
	* Test for method maintenanceScheduleItemOutVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.MaintenanceScheduleItemDao#maintenanceScheduleItemOutVOToEntity(org.phoenixctms.ctsms.vo.MaintenanceScheduleItemOutVO source, org.phoenixctms.ctsms.domain.MaintenanceScheduleItem target, boolean copyIfNull)
	*/
	@Test
	public void testMaintenanceScheduleItemOutVOToEntity() {
		Assert.fail("Test 'MaintenanceScheduleItemDaoTransformTest.testMaintenanceScheduleItemOutVOToEntity' not implemented!");
	}
}