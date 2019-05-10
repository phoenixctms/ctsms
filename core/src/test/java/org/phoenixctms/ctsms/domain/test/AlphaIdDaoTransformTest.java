// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.AlphaId
*/
@Test(groups = { "transform", "AlphaIdDao" })
public class AlphaIdDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method AlphaIdDao.toAlphaIdVO
	 *
	 * @see org.phoenixctms.ctsms.domain.AlphaIdDao#toAlphaIdVO(org.phoenixctms.ctsms.domain.AlphaId source, org.phoenixctms.ctsms.vo.AlphaIdVO target)
	 */
	@Test
	public void testToAlphaIdVO() {
		Assert.fail("Test 'AlphaIdDaoTransformTest.testToAlphaIdVO' not implemented!");
	}

	/**
	* Test for method alphaIdVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.AlphaIdDao#alphaIdVOToEntity(org.phoenixctms.ctsms.vo.AlphaIdVO source, org.phoenixctms.ctsms.domain.AlphaId target, boolean copyIfNull)
	*/
	@Test
	public void testAlphaIdVOToEntity() {
		Assert.fail("Test 'AlphaIdDaoTransformTest.testAlphaIdVOToEntity' not implemented!");
	}
}