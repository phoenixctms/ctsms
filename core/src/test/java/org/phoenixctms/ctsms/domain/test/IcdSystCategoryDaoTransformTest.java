// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.IcdSystCategory
*/
@Test(groups = { "transform", "IcdSystCategoryDao" })
public class IcdSystCategoryDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method IcdSystCategoryDao.toIcdSystCategoryVO
	 *
	 * @see org.phoenixctms.ctsms.domain.IcdSystCategoryDao#toIcdSystCategoryVO(org.phoenixctms.ctsms.domain.IcdSystCategory source, org.phoenixctms.ctsms.vo.IcdSystCategoryVO target)
	 */
	@Test
	public void testToIcdSystCategoryVO() {
		Assert.fail("Test 'IcdSystCategoryDaoTransformTest.testToIcdSystCategoryVO' not implemented!");
	}

	/**
	* Test for method icdSystCategoryVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.IcdSystCategoryDao#icdSystCategoryVOToEntity(org.phoenixctms.ctsms.vo.IcdSystCategoryVO source, org.phoenixctms.ctsms.domain.IcdSystCategory target, boolean copyIfNull)
	*/
	@Test
	public void testIcdSystCategoryVOToEntity() {
		Assert.fail("Test 'IcdSystCategoryDaoTransformTest.testIcdSystCategoryVOToEntity' not implemented!");
	}
}