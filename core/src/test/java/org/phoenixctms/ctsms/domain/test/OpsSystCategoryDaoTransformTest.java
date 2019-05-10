// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.OpsSystCategory
*/
@Test(groups = { "transform", "OpsSystCategoryDao" })
public class OpsSystCategoryDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method OpsSystCategoryDao.toOpsSystCategoryVO
	 *
	 * @see org.phoenixctms.ctsms.domain.OpsSystCategoryDao#toOpsSystCategoryVO(org.phoenixctms.ctsms.domain.OpsSystCategory source, org.phoenixctms.ctsms.vo.OpsSystCategoryVO target)
	 */
	@Test
	public void testToOpsSystCategoryVO() {
		Assert.fail("Test 'OpsSystCategoryDaoTransformTest.testToOpsSystCategoryVO' not implemented!");
	}

	/**
	* Test for method opsSystCategoryVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.OpsSystCategoryDao#opsSystCategoryVOToEntity(org.phoenixctms.ctsms.vo.OpsSystCategoryVO source, org.phoenixctms.ctsms.domain.OpsSystCategory target, boolean copyIfNull)
	*/
	@Test
	public void testOpsSystCategoryVOToEntity() {
		Assert.fail("Test 'OpsSystCategoryDaoTransformTest.testOpsSystCategoryVOToEntity' not implemented!");
	}
}