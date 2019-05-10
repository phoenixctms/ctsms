// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.Asp
*/
@Test(groups = { "transform", "AspDao" })
public class AspDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method AspDao.toAspVO
	 *
	 * @see org.phoenixctms.ctsms.domain.AspDao#toAspVO(org.phoenixctms.ctsms.domain.Asp source, org.phoenixctms.ctsms.vo.AspVO target)
	 */
	@Test
	public void testToAspVO() {
		Assert.fail("Test 'AspDaoTransformTest.testToAspVO' not implemented!");
	}

	/**
	* Test for method aspVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.AspDao#aspVOToEntity(org.phoenixctms.ctsms.vo.AspVO source, org.phoenixctms.ctsms.domain.Asp target, boolean copyIfNull)
	*/
	@Test
	public void testAspVOToEntity() {
		Assert.fail("Test 'AspDaoTransformTest.testAspVOToEntity' not implemented!");
	}
}