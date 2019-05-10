// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.StaffTag
*/
@Test(groups = { "transform", "StaffTagDao" })
public class StaffTagDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method StaffTagDao.toStaffTagVO
	 *
	 * @see org.phoenixctms.ctsms.domain.StaffTagDao#toStaffTagVO(org.phoenixctms.ctsms.domain.StaffTag source, org.phoenixctms.ctsms.vo.StaffTagVO target)
	 */
	@Test
	public void testToStaffTagVO() {
		Assert.fail("Test 'StaffTagDaoTransformTest.testToStaffTagVO' not implemented!");
	}

	/**
	* Test for method staffTagVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.StaffTagDao#staffTagVOToEntity(org.phoenixctms.ctsms.vo.StaffTagVO source, org.phoenixctms.ctsms.domain.StaffTag target, boolean copyIfNull)
	*/
	@Test
	public void testStaffTagVOToEntity() {
		Assert.fail("Test 'StaffTagDaoTransformTest.testStaffTagVOToEntity' not implemented!");
	}
}