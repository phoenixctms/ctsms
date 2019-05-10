// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.AspSubstance
*/
@Test(groups = { "transform", "AspSubstanceDao" })
public class AspSubstanceDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method AspSubstanceDao.toAspSubstanceVO
	 *
	 * @see org.phoenixctms.ctsms.domain.AspSubstanceDao#toAspSubstanceVO(org.phoenixctms.ctsms.domain.AspSubstance source, org.phoenixctms.ctsms.vo.AspSubstanceVO target)
	 */
	@Test
	public void testToAspSubstanceVO() {
		Assert.fail("Test 'AspSubstanceDaoTransformTest.testToAspSubstanceVO' not implemented!");
	}

	/**
	* Test for method aspSubstanceVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.AspSubstanceDao#aspSubstanceVOToEntity(org.phoenixctms.ctsms.vo.AspSubstanceVO source, org.phoenixctms.ctsms.domain.AspSubstance target, boolean copyIfNull)
	*/
	@Test
	public void testAspSubstanceVOToEntity() {
		Assert.fail("Test 'AspSubstanceDaoTransformTest.testAspSubstanceVOToEntity' not implemented!");
	}
}