// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.TrialType
*/
@Test(groups = { "transform", "TrialTypeDao" })
public class TrialTypeDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method TrialTypeDao.toTrialTypeVO
	 *
	 * @see org.phoenixctms.ctsms.domain.TrialTypeDao#toTrialTypeVO(org.phoenixctms.ctsms.domain.TrialType source, org.phoenixctms.ctsms.vo.TrialTypeVO target)
	 */
	@Test
	public void testToTrialTypeVO() {
		Assert.fail("Test 'TrialTypeDaoTransformTest.testToTrialTypeVO' not implemented!");
	}

	/**
	* Test for method trialTypeVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.TrialTypeDao#trialTypeVOToEntity(org.phoenixctms.ctsms.vo.TrialTypeVO source, org.phoenixctms.ctsms.domain.TrialType target, boolean copyIfNull)
	*/
	@Test
	public void testTrialTypeVOToEntity() {
		Assert.fail("Test 'TrialTypeDaoTransformTest.testTrialTypeVOToEntity' not implemented!");
	}
}