// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.ProbandContactDetailValue
*/
@Test(groups = { "transform", "ProbandContactDetailValueDao" })
public class ProbandContactDetailValueDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method ProbandContactDetailValueDao.toProbandContactDetailValueInVO
	 *
	 * @see org.phoenixctms.ctsms.domain.ProbandContactDetailValueDao#toProbandContactDetailValueInVO(org.phoenixctms.ctsms.domain.ProbandContactDetailValue source, org.phoenixctms.ctsms.vo.ProbandContactDetailValueInVO target)
	 */
	@Test
	public void testToProbandContactDetailValueInVO() {
		Assert.fail("Test 'ProbandContactDetailValueDaoTransformTest.testToProbandContactDetailValueInVO' not implemented!");
	}

	/**
	* Test for method probandContactDetailValueInVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.ProbandContactDetailValueDao#probandContactDetailValueInVOToEntity(org.phoenixctms.ctsms.vo.ProbandContactDetailValueInVO source, org.phoenixctms.ctsms.domain.ProbandContactDetailValue target, boolean copyIfNull)
	*/
	@Test
	public void testProbandContactDetailValueInVOToEntity() {
		Assert.fail("Test 'ProbandContactDetailValueDaoTransformTest.testProbandContactDetailValueInVOToEntity' not implemented!");
	}

	/**
	 * Test for method ProbandContactDetailValueDao.toProbandContactDetailValueOutVO
	 *
	 * @see org.phoenixctms.ctsms.domain.ProbandContactDetailValueDao#toProbandContactDetailValueOutVO(org.phoenixctms.ctsms.domain.ProbandContactDetailValue source, org.phoenixctms.ctsms.vo.ProbandContactDetailValueOutVO target)
	 */
	@Test
	public void testToProbandContactDetailValueOutVO() {
		Assert.fail("Test 'ProbandContactDetailValueDaoTransformTest.testToProbandContactDetailValueOutVO' not implemented!");
	}

	/**
	* Test for method probandContactDetailValueOutVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.ProbandContactDetailValueDao#probandContactDetailValueOutVOToEntity(org.phoenixctms.ctsms.vo.ProbandContactDetailValueOutVO source, org.phoenixctms.ctsms.domain.ProbandContactDetailValue target, boolean copyIfNull)
	*/
	@Test
	public void testProbandContactDetailValueOutVOToEntity() {
		Assert.fail("Test 'ProbandContactDetailValueDaoTransformTest.testProbandContactDetailValueOutVOToEntity' not implemented!");
	}
}