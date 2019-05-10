// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.TrialStatusAction
*/
@Test(groups = { "transform", "TrialStatusActionDao" })
public class TrialStatusActionDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method TrialStatusActionDao.toTrialStatusActionVO
	 *
	 * @see org.phoenixctms.ctsms.domain.TrialStatusActionDao#toTrialStatusActionVO(org.phoenixctms.ctsms.domain.TrialStatusAction source, org.phoenixctms.ctsms.vo.TrialStatusActionVO target)
	 */
	@Test
	public void testToTrialStatusActionVO() {
		Assert.fail("Test 'TrialStatusActionDaoTransformTest.testToTrialStatusActionVO' not implemented!");
	}

	/**
	* Test for method trialStatusActionVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.TrialStatusActionDao#trialStatusActionVOToEntity(org.phoenixctms.ctsms.vo.TrialStatusActionVO source, org.phoenixctms.ctsms.domain.TrialStatusAction target, boolean copyIfNull)
	*/
	@Test
	public void testTrialStatusActionVOToEntity() {
		Assert.fail("Test 'TrialStatusActionDaoTransformTest.testTrialStatusActionVOToEntity' not implemented!");
	}
}