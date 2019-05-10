// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.DutyRosterTurn
*/
@Test(groups = { "transform", "DutyRosterTurnDao" })
public class DutyRosterTurnDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method DutyRosterTurnDao.toDutyRosterTurnInVO
	 *
	 * @see org.phoenixctms.ctsms.domain.DutyRosterTurnDao#toDutyRosterTurnInVO(org.phoenixctms.ctsms.domain.DutyRosterTurn source, org.phoenixctms.ctsms.vo.DutyRosterTurnInVO target)
	 */
	@Test
	public void testToDutyRosterTurnInVO() {
		Assert.fail("Test 'DutyRosterTurnDaoTransformTest.testToDutyRosterTurnInVO' not implemented!");
	}

	/**
	* Test for method dutyRosterTurnInVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.DutyRosterTurnDao#dutyRosterTurnInVOToEntity(org.phoenixctms.ctsms.vo.DutyRosterTurnInVO source, org.phoenixctms.ctsms.domain.DutyRosterTurn target, boolean copyIfNull)
	*/
	@Test
	public void testDutyRosterTurnInVOToEntity() {
		Assert.fail("Test 'DutyRosterTurnDaoTransformTest.testDutyRosterTurnInVOToEntity' not implemented!");
	}

	/**
	 * Test for method DutyRosterTurnDao.toDutyRosterTurnOutVO
	 *
	 * @see org.phoenixctms.ctsms.domain.DutyRosterTurnDao#toDutyRosterTurnOutVO(org.phoenixctms.ctsms.domain.DutyRosterTurn source, org.phoenixctms.ctsms.vo.DutyRosterTurnOutVO target)
	 */
	@Test
	public void testToDutyRosterTurnOutVO() {
		Assert.fail("Test 'DutyRosterTurnDaoTransformTest.testToDutyRosterTurnOutVO' not implemented!");
	}

	/**
	* Test for method dutyRosterTurnOutVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.DutyRosterTurnDao#dutyRosterTurnOutVOToEntity(org.phoenixctms.ctsms.vo.DutyRosterTurnOutVO source, org.phoenixctms.ctsms.domain.DutyRosterTurn target, boolean copyIfNull)
	*/
	@Test
	public void testDutyRosterTurnOutVOToEntity() {
		Assert.fail("Test 'DutyRosterTurnDaoTransformTest.testDutyRosterTurnOutVOToEntity' not implemented!");
	}
}