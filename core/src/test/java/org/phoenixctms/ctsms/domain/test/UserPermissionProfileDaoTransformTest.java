// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.UserPermissionProfile
*/
@Test(groups = { "transform", "UserPermissionProfileDao" })
public class UserPermissionProfileDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method UserPermissionProfileDao.toUserPermissionProfileInVO
	 *
	 * @see org.phoenixctms.ctsms.domain.UserPermissionProfileDao#toUserPermissionProfileInVO(org.phoenixctms.ctsms.domain.UserPermissionProfile source, org.phoenixctms.ctsms.vo.UserPermissionProfileInVO target)
	 */
	@Test
	public void testToUserPermissionProfileInVO() {
		Assert.fail("Test 'UserPermissionProfileDaoTransformTest.testToUserPermissionProfileInVO' not implemented!");
	}

	/**
	* Test for method userPermissionProfileInVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.UserPermissionProfileDao#userPermissionProfileInVOToEntity(org.phoenixctms.ctsms.vo.UserPermissionProfileInVO source, org.phoenixctms.ctsms.domain.UserPermissionProfile target, boolean copyIfNull)
	*/
	@Test
	public void testUserPermissionProfileInVOToEntity() {
		Assert.fail("Test 'UserPermissionProfileDaoTransformTest.testUserPermissionProfileInVOToEntity' not implemented!");
	}

	/**
	 * Test for method UserPermissionProfileDao.toUserPermissionProfileOutVO
	 *
	 * @see org.phoenixctms.ctsms.domain.UserPermissionProfileDao#toUserPermissionProfileOutVO(org.phoenixctms.ctsms.domain.UserPermissionProfile source, org.phoenixctms.ctsms.vo.UserPermissionProfileOutVO target)
	 */
	@Test
	public void testToUserPermissionProfileOutVO() {
		Assert.fail("Test 'UserPermissionProfileDaoTransformTest.testToUserPermissionProfileOutVO' not implemented!");
	}

	/**
	* Test for method userPermissionProfileOutVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.UserPermissionProfileDao#userPermissionProfileOutVOToEntity(org.phoenixctms.ctsms.vo.UserPermissionProfileOutVO source, org.phoenixctms.ctsms.domain.UserPermissionProfile target, boolean copyIfNull)
	*/
	@Test
	public void testUserPermissionProfileOutVOToEntity() {
		Assert.fail("Test 'UserPermissionProfileDaoTransformTest.testUserPermissionProfileOutVOToEntity' not implemented!");
	}
}