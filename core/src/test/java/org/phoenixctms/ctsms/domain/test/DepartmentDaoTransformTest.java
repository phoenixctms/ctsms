// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.Department
*/
@Test(groups = { "transform", "DepartmentDao" })
public class DepartmentDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method DepartmentDao.toDepartmentVO
	 *
	 * @see org.phoenixctms.ctsms.domain.DepartmentDao#toDepartmentVO(org.phoenixctms.ctsms.domain.Department source, org.phoenixctms.ctsms.vo.DepartmentVO target)
	 */
	@Test
	public void testToDepartmentVO() {
		Assert.fail("Test 'DepartmentDaoTransformTest.testToDepartmentVO' not implemented!");
	}

	/**
	* Test for method departmentVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.DepartmentDao#departmentVOToEntity(org.phoenixctms.ctsms.vo.DepartmentVO source, org.phoenixctms.ctsms.domain.Department target, boolean copyIfNull)
	*/
	@Test
	public void testDepartmentVOToEntity() {
		Assert.fail("Test 'DepartmentDaoTransformTest.testDepartmentVOToEntity' not implemented!");
	}
}