// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.StaffStatusEntry
*/
@Test(groups = { "transform", "StaffStatusEntryDao" })
public class StaffStatusEntryDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method StaffStatusEntryDao.toStaffStatusEntryInVO
	 *
	 * @see org.phoenixctms.ctsms.domain.StaffStatusEntryDao#toStaffStatusEntryInVO(org.phoenixctms.ctsms.domain.StaffStatusEntry source, org.phoenixctms.ctsms.vo.StaffStatusEntryInVO target)
	 */
	@Test
	public void testToStaffStatusEntryInVO() {
		Assert.fail("Test 'StaffStatusEntryDaoTransformTest.testToStaffStatusEntryInVO' not implemented!");
	}

	/**
	* Test for method staffStatusEntryInVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.StaffStatusEntryDao#staffStatusEntryInVOToEntity(org.phoenixctms.ctsms.vo.StaffStatusEntryInVO source, org.phoenixctms.ctsms.domain.StaffStatusEntry target, boolean copyIfNull)
	*/
	@Test
	public void testStaffStatusEntryInVOToEntity() {
		Assert.fail("Test 'StaffStatusEntryDaoTransformTest.testStaffStatusEntryInVOToEntity' not implemented!");
	}

	/**
	 * Test for method StaffStatusEntryDao.toStaffStatusEntryOutVO
	 *
	 * @see org.phoenixctms.ctsms.domain.StaffStatusEntryDao#toStaffStatusEntryOutVO(org.phoenixctms.ctsms.domain.StaffStatusEntry source, org.phoenixctms.ctsms.vo.StaffStatusEntryOutVO target)
	 */
	@Test
	public void testToStaffStatusEntryOutVO() {
		Assert.fail("Test 'StaffStatusEntryDaoTransformTest.testToStaffStatusEntryOutVO' not implemented!");
	}

	/**
	* Test for method staffStatusEntryOutVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.StaffStatusEntryDao#staffStatusEntryOutVOToEntity(org.phoenixctms.ctsms.vo.StaffStatusEntryOutVO source, org.phoenixctms.ctsms.domain.StaffStatusEntry target, boolean copyIfNull)
	*/
	@Test
	public void testStaffStatusEntryOutVOToEntity() {
		Assert.fail("Test 'StaffStatusEntryDaoTransformTest.testStaffStatusEntryOutVOToEntity' not implemented!");
	}
}