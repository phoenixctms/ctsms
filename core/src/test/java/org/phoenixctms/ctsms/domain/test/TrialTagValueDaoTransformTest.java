// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.TrialTagValue
*/
@Test(groups = { "transform", "TrialTagValueDao" })
public class TrialTagValueDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method TrialTagValueDao.toTrialTagValueOutVO
	 *
	 * @see org.phoenixctms.ctsms.domain.TrialTagValueDao#toTrialTagValueOutVO(org.phoenixctms.ctsms.domain.TrialTagValue source, org.phoenixctms.ctsms.vo.TrialTagValueOutVO target)
	 */
	@Test
	public void testToTrialTagValueOutVO() {
		Assert.fail("Test 'TrialTagValueDaoTransformTest.testToTrialTagValueOutVO' not implemented!");
	}

	/**
	* Test for method trialTagValueOutVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.TrialTagValueDao#trialTagValueOutVOToEntity(org.phoenixctms.ctsms.vo.TrialTagValueOutVO source, org.phoenixctms.ctsms.domain.TrialTagValue target, boolean copyIfNull)
	*/
	@Test
	public void testTrialTagValueOutVOToEntity() {
		Assert.fail("Test 'TrialTagValueDaoTransformTest.testTrialTagValueOutVOToEntity' not implemented!");
	}

	/**
	 * Test for method TrialTagValueDao.toTrialTagValueInVO
	 *
	 * @see org.phoenixctms.ctsms.domain.TrialTagValueDao#toTrialTagValueInVO(org.phoenixctms.ctsms.domain.TrialTagValue source, org.phoenixctms.ctsms.vo.TrialTagValueInVO target)
	 */
	@Test
	public void testToTrialTagValueInVO() {
		Assert.fail("Test 'TrialTagValueDaoTransformTest.testToTrialTagValueInVO' not implemented!");
	}

	/**
	* Test for method trialTagValueInVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.TrialTagValueDao#trialTagValueInVOToEntity(org.phoenixctms.ctsms.vo.TrialTagValueInVO source, org.phoenixctms.ctsms.domain.TrialTagValue target, boolean copyIfNull)
	*/
	@Test
	public void testTrialTagValueInVOToEntity() {
		Assert.fail("Test 'TrialTagValueDaoTransformTest.testTrialTagValueInVOToEntity' not implemented!");
	}
}