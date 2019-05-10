// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.MassMail
*/
@Test(groups = { "transform", "MassMailDao" })
public class MassMailDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method MassMailDao.toMassMailInVO
	 *
	 * @see org.phoenixctms.ctsms.domain.MassMailDao#toMassMailInVO(org.phoenixctms.ctsms.domain.MassMail source, org.phoenixctms.ctsms.vo.MassMailInVO target)
	 */
	@Test
	public void testToMassMailInVO() {
		Assert.fail("Test 'MassMailDaoTransformTest.testToMassMailInVO' not implemented!");
	}

	/**
	* Test for method massMailInVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.MassMailDao#massMailInVOToEntity(org.phoenixctms.ctsms.vo.MassMailInVO source, org.phoenixctms.ctsms.domain.MassMail target, boolean copyIfNull)
	*/
	@Test
	public void testMassMailInVOToEntity() {
		Assert.fail("Test 'MassMailDaoTransformTest.testMassMailInVOToEntity' not implemented!");
	}

	/**
	 * Test for method MassMailDao.toMassMailOutVO
	 *
	 * @see org.phoenixctms.ctsms.domain.MassMailDao#toMassMailOutVO(org.phoenixctms.ctsms.domain.MassMail source, org.phoenixctms.ctsms.vo.MassMailOutVO target)
	 */
	@Test
	public void testToMassMailOutVO() {
		Assert.fail("Test 'MassMailDaoTransformTest.testToMassMailOutVO' not implemented!");
	}

	/**
	* Test for method massMailOutVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.MassMailDao#massMailOutVOToEntity(org.phoenixctms.ctsms.vo.MassMailOutVO source, org.phoenixctms.ctsms.domain.MassMail target, boolean copyIfNull)
	*/
	@Test
	public void testMassMailOutVOToEntity() {
		Assert.fail("Test 'MassMailDaoTransformTest.testMassMailOutVOToEntity' not implemented!");
	}
}