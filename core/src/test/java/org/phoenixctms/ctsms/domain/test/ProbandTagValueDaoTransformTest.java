// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.ProbandTagValue
*/
@Test(groups = { "transform", "ProbandTagValueDao" })
public class ProbandTagValueDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method ProbandTagValueDao.toProbandTagValueInVO
	 *
	 * @see org.phoenixctms.ctsms.domain.ProbandTagValueDao#toProbandTagValueInVO(org.phoenixctms.ctsms.domain.ProbandTagValue source, org.phoenixctms.ctsms.vo.ProbandTagValueInVO target)
	 */
	@Test
	public void testToProbandTagValueInVO() {
		Assert.fail("Test 'ProbandTagValueDaoTransformTest.testToProbandTagValueInVO' not implemented!");
	}

	/**
	* Test for method probandTagValueInVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.ProbandTagValueDao#probandTagValueInVOToEntity(org.phoenixctms.ctsms.vo.ProbandTagValueInVO source, org.phoenixctms.ctsms.domain.ProbandTagValue target, boolean copyIfNull)
	*/
	@Test
	public void testProbandTagValueInVOToEntity() {
		Assert.fail("Test 'ProbandTagValueDaoTransformTest.testProbandTagValueInVOToEntity' not implemented!");
	}

	/**
	 * Test for method ProbandTagValueDao.toProbandTagValueOutVO
	 *
	 * @see org.phoenixctms.ctsms.domain.ProbandTagValueDao#toProbandTagValueOutVO(org.phoenixctms.ctsms.domain.ProbandTagValue source, org.phoenixctms.ctsms.vo.ProbandTagValueOutVO target)
	 */
	@Test
	public void testToProbandTagValueOutVO() {
		Assert.fail("Test 'ProbandTagValueDaoTransformTest.testToProbandTagValueOutVO' not implemented!");
	}

	/**
	* Test for method probandTagValueOutVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.ProbandTagValueDao#probandTagValueOutVOToEntity(org.phoenixctms.ctsms.vo.ProbandTagValueOutVO source, org.phoenixctms.ctsms.domain.ProbandTagValue target, boolean copyIfNull)
	*/
	@Test
	public void testProbandTagValueOutVOToEntity() {
		Assert.fail("Test 'ProbandTagValueDaoTransformTest.testProbandTagValueOutVOToEntity' not implemented!");
	}
}