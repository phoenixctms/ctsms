// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.StaffCategory
*/
@Test(groups = { "transform", "StaffCategoryDao" })
public class StaffCategoryDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method StaffCategoryDao.toStaffCategoryVO
	 *
	 * @see org.phoenixctms.ctsms.domain.StaffCategoryDao#toStaffCategoryVO(org.phoenixctms.ctsms.domain.StaffCategory source, org.phoenixctms.ctsms.vo.StaffCategoryVO target)
	 */
	@Test
	public void testToStaffCategoryVO() {
		Assert.fail("Test 'StaffCategoryDaoTransformTest.testToStaffCategoryVO' not implemented!");
	}

	/**
	* Test for method staffCategoryVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.StaffCategoryDao#staffCategoryVOToEntity(org.phoenixctms.ctsms.vo.StaffCategoryVO source, org.phoenixctms.ctsms.domain.StaffCategory target, boolean copyIfNull)
	*/
	@Test
	public void testStaffCategoryVOToEntity() {
		Assert.fail("Test 'StaffCategoryDaoTransformTest.testStaffCategoryVOToEntity' not implemented!");
	}
}