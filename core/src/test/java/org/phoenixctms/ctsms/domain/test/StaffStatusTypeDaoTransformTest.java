// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.StaffStatusType
*/
@Test(groups = { "transform", "StaffStatusTypeDao" })
public class StaffStatusTypeDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method StaffStatusTypeDao.toStaffStatusTypeVO
	 *
	 * @see org.phoenixctms.ctsms.domain.StaffStatusTypeDao#toStaffStatusTypeVO(org.phoenixctms.ctsms.domain.StaffStatusType source, org.phoenixctms.ctsms.vo.StaffStatusTypeVO target)
	 */
	@Test
	public void testToStaffStatusTypeVO() {
		Assert.fail("Test 'StaffStatusTypeDaoTransformTest.testToStaffStatusTypeVO' not implemented!");
	}

	/**
	* Test for method staffStatusTypeVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.StaffStatusTypeDao#staffStatusTypeVOToEntity(org.phoenixctms.ctsms.vo.StaffStatusTypeVO source, org.phoenixctms.ctsms.domain.StaffStatusType target, boolean copyIfNull)
	*/
	@Test
	public void testStaffStatusTypeVOToEntity() {
		Assert.fail("Test 'StaffStatusTypeDaoTransformTest.testStaffStatusTypeVOToEntity' not implemented!");
	}
}